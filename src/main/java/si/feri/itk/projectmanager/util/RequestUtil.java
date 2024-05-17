package si.feri.itk.projectmanager.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.paging.SortInfo;
import si.feri.itk.projectmanager.paging.request.SortInfoRequest;
import si.feri.itk.projectmanager.security.SecurityConstants;

@Slf4j
public class RequestUtil {
    private RequestUtil() {}
    public static String getUserId(HttpServletRequest servletRequest) {
        return (String)servletRequest.getAttribute(SecurityConstants.USER_ID);
    }

    public static String getUserIdStrict(HttpServletRequest servletRequest) {
        String userId = getUserId(servletRequest);
        if (StringUtil.isNullOrEmpty(userId)) {
            log.warn("Unauthorized user tried to create a project");
            //this should never happen, we have a big problem if it does!
            throw new BadRequestException("User is not logged in");
        }
        return userId;
    }


    public static <T extends SortInfo.IField> SortInfo<T> getSortInfoFromRequest(SortInfoRequest<T> sortInfoRequest) {
        if (sortInfoRequest != null) {
            return sortInfoRequest.toSortInfo();
        }
        return null;
    }
}
