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
            contract.description("Some description");
            contract.name("some name");
            contract.priority(8);
            contract.ignored();
            contract.request(request -> {
                request.url("/api/hello");
                request.method(request.POST());
                request.body(ContractVerifierUtil.map().entry("name", request.$(request.anyAlphaUnicode())).entry(
                        "surname", request.$(request.anyNonBlankString())));
                request.bodyMatchers(bodyMatchers -> {
                    bodyMatchers.jsonPath("$.name", bodyMatchers.byRegex(".*"));
                    bodyMatchers.jsonPath("$.surname", bodyMatchers.byRegex(".+"));
                });
            });
            contract.response(response -> {
                response.fixedDelayMilliseconds(1000);
                response.status(response.OK());
                response.body("Hello Juan Perez");
            });
        }));
    }
}
