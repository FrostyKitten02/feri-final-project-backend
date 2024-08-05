package si.feri.itk.projectmanager.util;

import si.feri.itk.projectmanager.dto.clerk.IClerkEventType;
import si.feri.itk.projectmanager.exceptions.implementation.BadRequestException;
import si.feri.itk.projectmanager.exceptions.implementation.InternalServerException;

public class ClerkEventUtil {
    private ClerkEventUtil() {
    }

    public static void validateEvent(IClerkEventType event) {
        if (event.getExpectedType() == null) {
            throw new InternalServerException("Expected event type not defined");
        }

        if (StringUtil.isNullOrEmpty(event.getType())) {
            throw new BadRequestException("Event type not defined");
        }

        if (!event.getExpectedType().getValue().equals(event.getType())) {
            throw new BadRequestException("Invalid event type");
        }
    }
}
