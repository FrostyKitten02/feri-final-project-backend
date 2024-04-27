package si.feri.itk.projectmanager.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;

public class BadRequestException extends CustomRuntimeException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
