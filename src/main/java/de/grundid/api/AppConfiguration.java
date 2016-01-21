package de.grundid.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableIntegration
@ImportResource(locations = "classpath:META-INF/spring/integration/imap-idle-config.xml")
public class AppConfiguration {

    @Bean
    public RestOperations restOperations() {
        return new RestTemplate();
    }
}
