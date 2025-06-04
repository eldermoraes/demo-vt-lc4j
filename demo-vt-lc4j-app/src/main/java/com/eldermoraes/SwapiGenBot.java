package com.eldermoraes;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Singleton;

@RegisterAiService
@Singleton
public interface SwapiGenBot {

    @SystemMessage(MessageConstants.SYSTEM_MESSAGE)
    @UserMessage(MessageConstants.USER_MESSAGE)
    String chat(@MemoryId long i, String people, String planet, String specie, String starship, String vehicle);

}