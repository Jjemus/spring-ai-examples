package eu.konverto.mampf;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memory-chat")
@Tag(name= "Text")
public class ChatMemoryController {
    private final ChatClient chatClient;
    private final PromptChatMemoryAdvisor promptChatMemoryAdvisor;

    public ChatMemoryController(ChatClient.Builder builder) {
        this.chatClient = builder.build();

        var memory =
                MessageWindowChatMemory.builder()
                        .chatMemoryRepository(new InMemoryChatMemoryRepository())
                        .build();
        this.promptChatMemoryAdvisor = PromptChatMemoryAdvisor.builder(memory).build();
    }

    @GetMapping
    @Operation(summary = "Get a chat response with memory for the last recipe")
    public String recipeLast() {
        String previousResponse = chatClient
                .prompt()
                .advisors(promptChatMemoryAdvisor)
                .user("Give me a recipe for a delicious dish")
                .call()
                .content();

        return chatClient
                .prompt()
                .advisors(promptChatMemoryAdvisor)
                .user("What was the last recipe I asked for?")
                .call()
                .content();
    }
}
