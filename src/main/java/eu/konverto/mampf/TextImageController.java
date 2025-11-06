package eu.konverto.mampf;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/text-image")
public class TextImageController {
    private final ImageModel imageModel; // show8: image model

    public TextImageController(ImageModel imageModel) {
        this.imageModel = imageModel; // show8: image model
    }

    @GetMapping
    public String recipeImage() {
        ImageResponse response =
                imageModel.call(
                        new ImagePrompt(
                                "Generate an image of a delicious pasta dish",
                                ImageOptionsBuilder.builder().N(1).height(1024).width(1024).build()));

        String imageUrl = response.getResult().getOutput().getUrl();
        return "<html><body><img src=\"" + imageUrl + "\" alt=\"Pasta Dish\"/></body></html>";
    }
}
