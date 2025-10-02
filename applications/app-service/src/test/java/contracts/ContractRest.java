package contracts;

import org.springframework.cloud.contract.spec.Contract;
import org.springframework.cloud.contract.verifier.util.ContractVerifierUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

public class ContractRest implements Supplier<Collection<Contract>> {

    @Override
    public Collection<Contract> get() {
        return Collections.singletonList(Contract.make(contract -> {
            contract.description("Should return greeting when valid person data is provided");
            contract.name("shouldReturnGreetingForValidPerson");
            contract.request(request -> {
                request.url("/api/hello");
                request.method(request.POST());
                request.headers(headers -> {
                    headers.contentType(headers.applicationJson());
                });
                request.body(ContractVerifierUtil.map()
                        .entry("name", request.$(request.consumer(request.anyAlphaUnicode()), request.producer("Juan")))
                        .entry("surname", request.$(request.consumer(request.anyNonBlankString()), request.producer("Perez"))));
                request.bodyMatchers(bodyMatchers -> {
                    bodyMatchers.jsonPath("$.name", bodyMatchers.byRegex("[a-zA-Z]+"));
                    bodyMatchers.jsonPath("$.surname", bodyMatchers.byRegex(".+"));
                });
            });
            contract.response(response -> {
                response.status(response.OK());
                response.headers(headers -> {
                    headers.contentType(headers.textPlain());
                });
                response.body(response.$(response.consumer(response.anyNonBlankString()), 
                                      response.producer("Hello Juan Perez")));
            });
        }));
    }
}
