package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/structure-chat")
public class StructureChatController {
    private final ChatClient chatClient;

    public StructureChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private record Recipe(String name, String description, List<String> ingredients, String instructions) {}

    @GetMapping
    public Recipe recipeJson() {
        return chatClient.prompt()
                .user("Give me a recipe for a delicious dish")
                .call()
                .entity(Recipe.class);
    }
}
