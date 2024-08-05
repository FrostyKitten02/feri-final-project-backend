package si.feri.itk.projectmanager.dto.clerk;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface IClerkEventType {
    @JsonIgnore
    ClerkEventType getExpectedType();
    String getType();
}
