package org.example.factory.service;

import dev.langchain4j.model.openai.OpenAiChatModel;

public class OpenAiLLMService implements LLMService {

    private final OpenAiChatModel openAiChatModel;

    public OpenAiLLMService() {
        this.openAiChatModel = OpenAiChatModel.builder()
                .apiKey(LlmEnv.apiKey())
                .baseUrl(LlmEnv.baseUrl())
                .modelName(LlmEnv.modelName())
                .build();
    }

    @Override
    public String chat(String prompt) {
        return openAiChatModel.chat(prompt);
    }
}
