package eu.konverto.mampf.data_enrichment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vector-init")
@Tag(name= "Data Enrichment")
public class VectorInitController {
    private final VectorStore vectorStore;

    public VectorInitController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping
    @Operation(summary = "Initialize vector store with sample documents")
    public String initVector() {
        List<Document> documents = List.of(
                new Document("Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!! Spring AI rocks!!", Map.of("meta1", "meta1")),
                new Document("Pasta Carbonara is a traditional Italian dish made with eggs, cheese, pancetta, and pepper.", Map.of("meta1", "meta1")),
                new Document("You walk forward facing the past and you turn back toward the future.", Map.of("meta2", "meta2")));

        vectorStore.add(documents);

        return "initialized " + documents.size();
    }
}
