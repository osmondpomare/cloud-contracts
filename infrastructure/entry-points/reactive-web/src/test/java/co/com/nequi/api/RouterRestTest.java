package co.com.nequi.api;

import co.com.nequi.api.dto.Person;
import co.com.nequi.api.validation.RequestValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;


    @MockitoBean
    private RequestValidator requestValidator;

    @Test
    void sayHelloIsSuccessful() {
        given(requestValidator.validate(new Person("John", "Doe"))).willReturn(Person.builder().name("John").surname(
                "Doe").build());


        webTestClient.post()
                .uri("/api/hello")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new Person("John", "Doe"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isNotBlank();
                        }
                );
    }
}
