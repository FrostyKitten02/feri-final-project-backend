package si.feri.itk.projectmanager.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;

public class ItemNotFoundException extends CustomRuntimeException {
    public ItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}