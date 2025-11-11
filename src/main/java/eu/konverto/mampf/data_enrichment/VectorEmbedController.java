package eu.konverto.mampf.data_enrichment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vector-embed")
@Tag(name= "Data Enrichment")
public class VectorEmbedController {
    private final EmbeddingModel embeddingModel;

    public VectorEmbedController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping
    @Operation(summary = "Embed text to vector")
    public String embedText(@RequestParam(name = "text", defaultValue = "Pasta carbonara") String text) {
        float[] vector = embeddingModel.embed(text);
        return "Embedded text of length " + text.length() + " to vector of length " + vector.length;
    }
}
