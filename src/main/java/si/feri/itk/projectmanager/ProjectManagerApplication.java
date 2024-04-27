package si.feri.itk.projectmanager;

import com.microsoft.kiota.authentication.ApiKeyAuthenticationProvider;
import com.microsoft.kiota.authentication.ApiKeyLocation;
import com.microsoft.kiota.http.OkHttpRequestAdapter;
import io.github.zzhorizonzz.sdk.ClerkClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableConfigurationProperties
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class ProjectManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagerApplication.class, args);
    }

    @Bean
    public ClerkClient clerkClient() {
        final ApiKeyAuthenticationProvider authProvider = new ApiKeyAuthenticationProvider("Bearer sk_test_2eu9dVq8JZChiLozsIPO2tjk3xvn2uI6p0GrjoxgXj", "authorization", ApiKeyLocation.HEADER);
        final OkHttpRequestAdapter adapter = new OkHttpRequestAdapter(authProvider);
        final ClerkClient client = new ClerkClient(adapter);
        return client;
    }


}
