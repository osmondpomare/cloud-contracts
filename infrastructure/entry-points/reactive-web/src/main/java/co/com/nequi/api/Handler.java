package co.com.nequi.api;

import co.com.nequi.api.dto.Person;
import co.com.nequi.api.validation.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RequestValidator requestValidator;

    public Mono<ServerResponse> sayHello(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Person.class)
                .doOnNext(requestValidator::validate)
                .map(p -> String.format("Hello %s %s", p.getName(), p.getSurname()))
                .flatMap(greeting -> ServerResponse.ok().bodyValue(greeting));

    }
}
