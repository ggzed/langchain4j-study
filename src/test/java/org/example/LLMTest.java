package org.example;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import org.example.factory.LLMFactory;
import org.example.factory.service.LLMService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 免费学习用的通用 OpenAI 兼容测试。
 * <p>
 * 默认：智谱 GLM-4.7-Flash（官方标注为免费文本模型，国内直连，无需绑卡）。
 * 在 IDEA 的 Run Configuration -> Environment variables 里设置 LLM_API_KEY 即可运行。
 * <p>
 * 换服务商只改环境变量，代码不用动：
 * 智谱       LLM_BASE_URL=https://open.bigmodel.cn/api/paas/v4               LLM_MODEL=glm-4.7-flash
 * 硅基流动   LLM_BASE_URL=https://api.siliconflow.cn/v1                      LLM_MODEL=Qwen/Qwen3-8B
 * 阿里百炼   LLM_BASE_URL=https://dashscope.aliyuncs.com/compatible-mode/v1  LLM_MODEL=qwen-plus
 * 本地 Ollama LLM_BASE_URL=http://localhost:11434/v1                         LLM_MODEL=qwen3:4b（key 随便填）
 * <p>
 * 注意：langchain4j 会自动把 /chat/completions 拼到 baseUrl 后面，baseUrl 不要以 / 结尾。
 */
@SpringBootTest
public class LLMTest {

    @Test
    public void helloWorld() {
        String baseUrl = System.getenv().getOrDefault("LLM_BASE_URL",
                "https://open.bigmodel.cn/api/paas/v4");
        String modelName = System.getenv().getOrDefault("LLM_MODEL", "glm-5.3-flash");
        String apiKey = firstEnv("LLM_API_KEY", "ZHIPU_API_KEY", "OPENAI_API_KEY");

        if (apiKey == null) {
            throw new IllegalStateException(
                    "缺少 API Key：请在 IDEA 的 Run Configuration -> Environment variables 中设置 "
                            + "LLM_API_KEY（智谱控制台 open.bigmodel.cn 可免费创建）。"
                            + "若改的是 Windows 系统环境变量，需重启 IDEA 才会生效。");
        }

        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .build();

        System.out.println("baseUrl = " + baseUrl);
        System.out.println("model   = " + modelName);
        System.out.println(model.chat("Who you are?"));
    }

    private static String firstEnv(String... names) {
        for (String name : names) {
            String value = System.getenv(name);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    @Autowired
    private OpenAiChatModel model;

    @Test
    public void testSpringboot() {
        String answer = model.chat("你是谁");
        System.out.println(answer);
    }

    @Autowired
    private QwenChatModel qwenChatModel;

    @Test
    public void testDashScope() {
        String answer = qwenChatModel.chat("你是谁");
        System.out.println(answer);
    }

    @Test
    public void testDashScopeWX() {
        String apiKey = firstEnv("DASHSCOPE_API_KEY");
        if (apiKey == null) {
            throw new IllegalStateException(
                    "缺少环境变量 DASHSCOPE_API_KEY：请在 Run Configuration -> Environment variables 中配置后再运行图片测试。");
        }
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey(apiKey)
                .build();
        Response<Image> response = wanxImageModel.generate("美女");
        System.out.println(response.content().url());
    }

    @Test
    public void testQwenFactory() {
        LLMService llmService = LLMFactory.createLLMService(LLMFactory.ModelType.OPENAI);
        System.out.println(llmService.chat("你是谁？"));
    }
}
