package eu.konverto.mampf;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool-chat")
@Tag(name= "Text")
public class ChatToolController {
    private final ChatClient chatClient;

    public ChatToolController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultTools(new PreferenceTool())
                .build();
    }

    @GetMapping
    @Operation(summary = "Get a chat response with tools for weather and user preferences")
    public String recipeWeather() {
        return chatClient.prompt("Give me a recipe that is suitable for current weather ")
                .tools(new WeatherTools())
                .call()
                .content();
    }

    private class WeatherTools {
        @Tool(name = "weather", description = "Get the current weather")
        String getWeather(String location) {
            return "The weather in " + location + " is sunny with a high of 25°C.";
        }

        @Tool(name = "location", description = "Get the user's location")
        String getLocation() {
            return "Südtirol"; // Placeholder for actual location data
        }
    }

    private class PreferenceTool {
        @Tool(name = "preference", description = "Get user preference for recipes")
        String getPreference() {
            return "German cuisine";
        }
    }
}
