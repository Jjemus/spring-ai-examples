package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image-text")
public class ImageTextController {
    private final ChatClient chatClient;

    @Value("classpath:/test.jpg")
    private Resource image;

    public ImageTextController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping
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
