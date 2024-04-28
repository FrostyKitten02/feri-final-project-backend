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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;
import si.feri.itk.projectmanager.exceptions.implementation.UnauthorizedException;
import si.feri.itk.projectmanager.security.SecurityConstants;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

@Component
@Slf4j
public class ClerkFilter extends OncePerRequestFilter {
    private final String clerkPublicKey;
    private final RequestMatcher[] requestMatchers = new RequestMatcher[2];

    public ClerkFilter(
                @Value("${clerk.public-jwt-key}") String clerkPublicKey,
                @Value("${springdoc.api-docs.path}") String swaggerDocs,
                @Value("${swagger.ui-url}") String swaggerUi
    ) {
        this.clerkPublicKey = clerkPublicKey;
        requestMatchers[0] = new AntPathRequestMatcher(swaggerDocs + "/**", HttpMethod.GET.name());
        requestMatchers[1] = new AntPathRequestMatcher(swaggerUi + "/**", HttpMethod.GET.name());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        Cookie sessionCookie = null;
        for (Cookie c : cookies) {
            if (c.getName().equals(ClerkConstants.CLEAR_SESSION_COOKIE_NAME)) {
                sessionCookie = c;
                break;
            }
        }

        if (sessionCookie == null) {
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
                    .parseSignedClaims(sessionCookie.getValue());
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
