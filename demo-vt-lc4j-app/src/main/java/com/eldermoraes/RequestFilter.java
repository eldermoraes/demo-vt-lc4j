package com.eldermoraes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Provider
@ApplicationScoped
public class RequestFilter implements ClientRequestFilter {

    @ConfigProperty(name="demo-llama.token")
    Optional<String> llamaToken;

    @ConfigProperty(name="demo-nomic.token")
    Optional<String> nomicToken;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        Map<String, String> aiServiceMap = Map.of(
                "LlamaAIService", llamaToken.orElse(""),
                "NomicAIService", nomicToken.orElse("")
        );

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();

            for (Map.Entry<String, String> entry : aiServiceMap.entrySet()) {
                if (className.contains(entry.getKey())) {
                    requestContext.getHeaders().putSingle("Authorization", entry.getValue());
                    return;
                }
            }
        }
    }
}
