package si.feri.itk.projectmanager.dto.clerk;

import lombok.Getter;

@Getter
public enum ClerkEventType {
    USER_CREATED("user.created"),
    USER_UPDATED("user.updated"),
    USER_DELETED("user.deleted");
    private final String value;
    ClerkEventType(String value) {
        this.value = value;
    }
}
