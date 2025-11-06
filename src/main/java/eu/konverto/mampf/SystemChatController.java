package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system-chat")
public class SystemChatController {
    private final ChatClient chatClient;

    public SystemChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful assistant that provides recipes in metric units.")
                .build();
    }

    @GetMapping
    public String recipe() {
        return chatClient
                .prompt()
                .user("Give me a recipe for a delicious dish")
                .call()
                .content();
    }
}
