package si.feri.itk.projectmanager;

import jakarta.servlet.http.HttpServletRequest;
import si.feri.itk.projectmanager.security.SecurityConstants;

public class TestUtil {
    private TestUtil() {}

    public static void addUserIdToRequest(HttpServletRequest request, String userId) {
        request.setAttribute(SecurityConstants.USER_ID, userId);
    }

}
