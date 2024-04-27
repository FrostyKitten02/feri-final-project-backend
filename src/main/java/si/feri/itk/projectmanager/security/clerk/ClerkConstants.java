package si.feri.itk.projectmanager.security.clerk;


//DOC for clerk jwt verification
//https://clerk.com/docs/backend-requests/handling/manual-jwt
public class ClerkConstants {
    private ClerkConstants() {}
    public static final String CLEAR_SESSION_COOKIE_NAME = "__session";
    public static final String CLERK_PAYLOAD_SESSION_ID = "sid";
    public static final String CLERK_PAYLOAD_USER_ID = "sub";

}
