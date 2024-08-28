package si.feri.itk.projectmanager.dto.clerk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClerkUser {
    private String id;

    @Getter(onMethod = @__(@JsonProperty("first_name")))
    @Setter(onMethod = @__(@JsonProperty("first_name")))
    private String firstName;

    @Getter(onMethod = @__(@JsonProperty("last_name")))
    @Setter(onMethod = @__(@JsonProperty("last_name")))
    private String lastName;

    @Getter(onMethod = @__(@JsonProperty("primary_email_address_id")))
    @Setter(onMethod = @__(@JsonProperty("primary_email_address_id")))
    private String primaryEmailAddressId;

    @Getter(onMethod = @__(@JsonProperty("image_url")))
    @Setter(onMethod = @__(@JsonProperty("image_url")))
    private String imageUrl;

    @Getter(onMethod = @__(@JsonProperty("email_addresses")))
    @Setter(onMethod = @__(@JsonProperty("email_addresses")))
    private List<EmailAddress> emailAddresses;
}
