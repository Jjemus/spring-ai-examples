package eu.konverto.mampf.text;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-chat")
@Tag(name= "Text")
public class ChatSystemController {
    private final ChatClient chatClient;

    public ChatSystemController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful assistant that provides recipes in metric units.")
                .build();
    }

    @GetMapping
    @Operation(summary = "Get a chat response with system instructions")
    public String recipe() {
        return chatClient
                .prompt()
                .user("Give me a recipe for a delicious dish")
                .call()
                .content();
    }
}
