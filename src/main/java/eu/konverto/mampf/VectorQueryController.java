package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vector-query")
public class VectorQueryController {
    private final ChatClient chatClient;

    public VectorQueryController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(retrievalAugmentationAdvisor)
                .build();
    }

    @GetMapping
    public String ragQuery(@RequestParam(name = "q", defaultValue = "what is carbonara") String userQuery) {
        return chatClient.prompt()
                .user(userQuery)
                .call()
                .content();
    }
}
