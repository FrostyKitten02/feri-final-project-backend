package si.feri.itk.projectmanager.dto.clerk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class UserUpdatedEvent extends ClerkEvent implements IClerkEventType {
    @Getter(onMethod = @__(@JsonProperty("data")))
    @Setter(onMethod = @__(@JsonProperty("data")))
    private ClerkUser user;
    @Override
    @JsonIgnore
    public ClerkEventType getExpectedType() {
        return ClerkEventType.USER_UPDATED;
    }
}
