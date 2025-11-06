package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vector-topk")
public class VectorTopKController {
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public VectorTopKController(VectorStore vectorStore, ChatClient.Builder builder) {
        this.vectorStore = vectorStore;
        this.chatClient = builder.build();
    }

    @GetMapping
    public String getTopK(@RequestParam(name = "q", defaultValue = "recipe for carbonara pasta") String userQuery) {
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .topK(1)
                .build();

        List<Document> retrievedDocs = retriever.retrieve(new Query(userQuery));

        String formattedDocuments = retrievedDocs.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n---\n" + b);

        return chatClient.prompt()
                .user("Using this document as additional information, provide a detailed answer to the query: " + userQuery + "\n\nDocument:\n" + formattedDocuments)
                .call()
                .content();
    }
}
