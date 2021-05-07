package dev.michel.accountservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    public static final Contact DEFAULT_CONTACT = new Contact("Michel Olvera", "https://github.com/michelolvera",
            "michel.olvera.p@outlook.com");

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Account-Service", "Microservicio para la creación de cuentas y movimientos", "1.0",
            "Copyright", DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>());

    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.OAS_30).select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
                .build().apiInfo(DEFAULT_API_INFO)
                .protocols(new TreeSet<>(Arrays.asList("http", "https")));
    }
}
