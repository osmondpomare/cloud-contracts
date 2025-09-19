package co.com.nequi.api.config;

import co.com.nequi.api.Handler;
import co.com.nequi.api.RouterRest;
import co.com.nequi.api.dto.Person;
import co.com.nequi.api.validation.RequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RequestValidator requestValidator;

    @Test
    void corsConfigurationShouldAllowOrigins() {

        given(requestValidator.validate(new Person("John", "Doe"))).willReturn(Person.builder().name("John").surname(
                "Doe").build());

        webTestClient.post()
                .uri("/api/hello")
                .bodyValue(new Person("John", "Doe"))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}