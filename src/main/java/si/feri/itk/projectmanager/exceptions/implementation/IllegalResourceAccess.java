package si.feri.itk.projectmanager.exceptions.implementation;

import org.springframework.http.HttpStatus;
import si.feri.itk.projectmanager.exceptions.CustomRuntimeException;

public class IllegalResourceAccess extends CustomRuntimeException {
    public IllegalResourceAccess(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
