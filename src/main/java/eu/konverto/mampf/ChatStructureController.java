package eu.konverto.mampf;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/structure-chat")
@Tag(name= "Text")
public class ChatStructureController {
    private final ChatClient chatClient;

    public ChatStructureController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private record Recipe(String name, String description, List<String> ingredients, String instructions) {}

    @GetMapping
    @Operation(summary = "Get a chat response with a structured recipe")
    public Recipe recipeJson() {
        return chatClient.prompt()
                .user("Give me a recipe for a delicious dish")
                .call()
                .entity(Recipe.class);
    }
}
