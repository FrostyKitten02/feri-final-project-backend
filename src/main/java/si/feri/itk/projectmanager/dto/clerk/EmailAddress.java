package si.feri.itk.projectmanager.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAddress {
    private String id;
    @Getter(onMethod = @__(@JsonProperty("email_address")))
    @Setter(onMethod = @__(@JsonProperty("email_address")))
    private String emailAddress;
}
