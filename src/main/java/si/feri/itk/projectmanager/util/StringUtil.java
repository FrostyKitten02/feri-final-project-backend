package si.feri.itk.projectmanager.util;

public class StringUtil {
    private StringUtil() {}
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
