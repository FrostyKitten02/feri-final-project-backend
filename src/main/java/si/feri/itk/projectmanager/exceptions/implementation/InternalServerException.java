package si.feri.itk.projectmanager.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;

public class InternalServerException extends CustomRuntimeException {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final String defaultMsg = "Internal server error";

    public InternalServerException() {
        super(defaultMsg, status);
    }
    public InternalServerException(String message) {
        super(message, status);
    }
    public InternalServerException(String message, Throwable throwable) {
        super(message, throwable, status);
    }
}
