package si.feri.itk.projectmanager.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreatedEvent extends ClerkEvent {
    @Getter(onMethod = @__(@JsonProperty("data")))
    @Setter(onMethod = @__(@JsonProperty("data")))
    private ClerkUser user;
}
