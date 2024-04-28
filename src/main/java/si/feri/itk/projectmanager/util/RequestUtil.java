package si.feri.itk.projectmanager.util;

import jakarta.servlet.http.HttpServletRequest;
import si.feri.itk.projectmanager.security.SecurityConstants;

public class RequestUtil {
    private RequestUtil() {}
    public static String getUserId(HttpServletRequest servletRequest) {
        return (String)servletRequest.getAttribute(SecurityConstants.USER_ID);
    }

}
