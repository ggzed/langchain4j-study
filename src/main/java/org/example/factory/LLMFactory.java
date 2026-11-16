package org.example.factory;

import org.example.factory.service.LLMService;
import org.example.factory.service.OpenAiLLMService;
import org.example.factory.service.QwenLLMService;

/**
 * 工厂方法构造不同的大模型
 */
public class LLMFactory {
    public enum ModelType {
        QWEN, OPENAI
    }

    public static LLMService createLLMService(ModelType type) {
        return switch (type) {
            case QWEN -> new QwenLLMService();
            case OPENAI -> new OpenAiLLMService();
        };
    }
}
