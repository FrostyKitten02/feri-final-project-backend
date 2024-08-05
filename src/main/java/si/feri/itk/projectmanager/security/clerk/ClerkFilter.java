package si.feri.itk.projectmanager.security.clerk;

import com.microsoft.kiota.HttpMethod;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.base64url.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.exceptions.implementation.UnauthorizedException;
import si.feri.itk.projectmanager.security.SecurityConstants;
import si.feri.itk.projectmanager.util.StringUtil;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

@Component
@Slf4j
public class ClerkFilter extends OncePerRequestFilter {
    private final String clerkPublicKey;
    private final RequestMatcher[] requestMatchers = new RequestMatcher[3];
    private final String clerkUserCreatedPath = "/clerk/user-created";
    private final String clerkKey;
    private final RequestMatcher clerkControllerMatcher;
    public ClerkFilter(
                @Value("${clerk.public-jwt-key}") String clerkPublicKey,
                @Value("${springdoc.api-docs.path}") String swaggerDocs,
                @Value("${swagger.ui-url}") String swaggerUi,
                @Value("${springdoc.api-docs.yaml-path}") String swaggerYaml,
                @Value("${api.key.clerk}") String clerkKey
    ) {
        this.clerkPublicKey = clerkPublicKey;
        this.clerkKey = clerkKey;
        //TODO also use IpAddressFilterToCheckClerkIp
        //list of ips on https://docs.svix.com/webhook-ips.json
        clerkControllerMatcher = new AntPathRequestMatcher("/clerk/webhook/**");
        requestMatchers[0] = new AntPathRequestMatcher(swaggerDocs + "/**", HttpMethod.GET.name());
        requestMatchers[1] = new AntPathRequestMatcher(swaggerUi + "/**", HttpMethod.GET.name());
        requestMatchers[2] = new AntPathRequestMatcher(swaggerYaml + "/**", HttpMethod.GET.name());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: we should make another filter to authenticate api-s, but currently this is good!
        if (clerkControllerMatcher.matches(request)) {
            String key = request.getHeader(SecurityConstants.API_KEY_HEADER);
            if (StringUtil.isNullOrEmpty(key) || !key.equals(clerkKey)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                throw new UnauthorizedException("UNAUTHORIZED API!");
            }

            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        String session = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals(ClerkConstants.CLEAR_SESSION_COOKIE_NAME)) {
                    session = c.getValue();
                    break;
                }
            }
        }

        if (StringUtil.isNullOrEmpty(session)) {
            String authHeader = request.getHeader("Authorization");
            if (StringUtil.isNullOrEmpty(authHeader)) {
                throw new BadRequestException("Authorization header not found");
            }
            session = authHeader.replace("Bearer ", "");
        }


        if (StringUtil.isNullOrEmpty(session)) {
            throw new BadRequestException("Session cookie not found");
        }

        try {
            KeyFactory kFactory = KeyFactory.getInstance("RSA");
            byte[] yourKey =  Base64.decode(clerkPublicKey);
            X509EncodedKeySpec spec =  new X509EncodedKeySpec(yourKey);
            PublicKey publicKey = (PublicKey) kFactory.generatePublic(spec);
            Jws<Claims> jwt = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(session);
            Map<String, Object> payload = jwt.getPayload();
            String sessionId = payload.get(ClerkConstants.CLERK_PAYLOAD_SESSION_ID).toString();
            String userId = payload.get(ClerkConstants.CLERK_PAYLOAD_USER_ID).toString();
            request.setAttribute(SecurityConstants.SESSION_ID, sessionId);
            request.setAttribute(SecurityConstants.USER_ID, userId);
        } catch (ExpiredJwtException ex) {
            log.error("Token expired" + ex.getLocalizedMessage(), ex);
            throw new UnauthorizedException("Token expired" + ex.getLocalizedMessage(), ex);
        } catch (Exception e) {
            log.error("Error while verifying token" + e.getLocalizedMessage(), e);
            throw new InternalServerException("Error while verifying token" + e.getLocalizedMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}
