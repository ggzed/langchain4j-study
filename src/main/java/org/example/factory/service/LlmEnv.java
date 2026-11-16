package org.example.factory.service;

/**
 * 大模型接入参数的统一来源：全部通过环境变量读取，源码中不出现明文密钥。
 * <p>
 * 变量名与 application.properties 中的占位符保持一致：
 * api-key=${DASHSCOPE_API_KEY}、base-url 中的 ${WorkspaceId}、model-name。
 */
final class LlmEnv {

    private LlmEnv() {
    }

    static String apiKey() {
        return require("DASHSCOPE_API_KEY");
    }

    static String baseUrl() {
        return "https://" + require("WorkspaceId") + ".cn-beijing.maas.aliyuncs.com/compatible-mode/v1";
    }

    static String modelName() {
        return System.getenv().getOrDefault("LLM_MODEL", "deepseek-v4.1-flash-1017");
    }

    private static String require(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("缺少环境变量 " + name
                    + "：请在 Run Configuration -> Environment variables 或系统环境变量中配置"
                    + "（对应 application.properties 中的 ${" + name + "}）。");
        }
        return value;
    }
}
