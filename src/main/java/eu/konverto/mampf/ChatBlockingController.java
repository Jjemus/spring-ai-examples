package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blocking-chat")
public class ChatBlockingController {
    private final ChatClient chatClient;

    public ChatBlockingController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("recipe")
    public String recipe() {
        return chatClient
                .prompt()
                .user("Give me a recipe for a delicious dish")
                .call()
                .content();
    }
}
