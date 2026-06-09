package com.krotname.checker.client;

import java.io.IOException;
import java.util.Optional;

@FunctionalInterface
public interface DadataClient {
    Optional<String> fetchCompanyState(String inn) throws IOException, InterruptedException;
}
