package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/streaming-chat")
public class ChatStreamingController {
    private final ChatClient chatClient;

    public ChatStreamingController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    public Flux<String> recipeStream() {
        return chatClient.prompt().user("Give me a recipe for a delicious dish").stream().content();
    }
}
