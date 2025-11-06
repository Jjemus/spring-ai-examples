package eu.konverto.mampf;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/embed")
public class EmbedController {
    private final EmbeddingModel embeddingModel;

    public EmbedController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping
    public String embedText(@RequestParam("text") String text) {
        float[] vector = embeddingModel.embed(text);
        return "Embedded text of length " + text.length() + " to vector of length " + vector.length;
    }
}
