package com.eldermoraes.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface SwAPIGenBot {

    @SystemMessage(MessageConstants.SYSTEM_MESSAGE)
    @UserMessage(MessageConstants.USER_MESSAGE)
    String chat(@MemoryId long i, String people, String planet, String specie, String starship, String vehicle);

}