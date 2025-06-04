package com.eldermoraes;

import io.quarkiverse.langchain4j.ModelName;
import io.quarkiverse.langchain4j.auth.ModelAuthProvider;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Optional;

@ApplicationScoped
public class EmbeddingAuth implements ModelAuthProvider {

    @ConfigProperty(name="demo-llama.token")
    Optional<String> llamaToken;

    @Override
    public String getAuthorization(Input input) {

        if (input.uri().getPath().contains("completions")){
            return llamaToken.orElse("");
        } else{
            return "";
        }
    }
}
