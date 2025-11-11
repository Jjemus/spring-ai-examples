package eu.konverto.mampf.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image-text")
@Tag(name= "Images")
public class ImageToTextController {
    private final ChatClient chatClient;

    @Value("classpath:/test.jpg")
    private Resource image;

    public ImageToTextController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
    @Operation(summary = "Describe a dish from an image")
    public String recipeImageDescribe() {
        return chatClient
                .prompt()
                .user(
                        u ->
                                u.text("What dish is this?")
                                        .media(MimeTypeUtils.IMAGE_JPEG, image))
                .call()
                .content();
    }
}
