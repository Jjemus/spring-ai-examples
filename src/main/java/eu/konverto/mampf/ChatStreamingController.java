package eu.konverto.mampf;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/streaming-chat")
@Tag(name= "Text")
public class ChatStreamingController {
    private final ChatClient chatClient;

    public ChatStreamingController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    @Operation(summary = "Get a streaming chat response with a recipe")
    public Flux<String> recipeStream() {
        return chatClient.prompt().user("Give me a recipe for a delicious dish").stream().content();
    }
}
