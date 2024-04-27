package si.feri.itk.projectmanager.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;

public class UnauthorizedException extends CustomRuntimeException {
    private static final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    public UnauthorizedException(String message) {
        super(message, httpStatus);
    }

    public UnauthorizedException(String message, Throwable throwable) {
        super(message, throwable, httpStatus);
    }
}
