package eu.konverto.mampf.testing;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/fact-check")
@Tag(name= "Testing & Evaluation")
public class TestingFactCheckController {
    private final ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;

    public TestingFactCheckController(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    @Operation(summary = "Get a recipe along with its fact-check evaluation")
    public FactCheckEvaluation recipeFactCheck(
            @RequestParam(defaultValue = "What is pasta carbonara?") String question) {

        String aiResponse = chatClient
                .prompt()
                .user(question)
                .call()
                .content();

        List<Document> supportingFacts = List.of(
                new Document("Carbonara is a traditional Italian pasta dish made with eggs, cheese (Pecorino Romano), guanciale (cured pork cheek), and black pepper."),
                new Document("The main ingredients of pasta carbonara are pasta, eggs, Pecorino Romano cheese, guanciale, and black pepper. Cream is NOT traditionally used in authentic carbonara."),
                new Document("Guanciale is the traditional meat used in carbonara, though pancetta is sometimes used as a substitute.")
        );

        FactCheckingEvaluator evaluator = new FactCheckingEvaluator(chatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                question,
                supportingFacts,  // The factual information to check against
                aiResponse
        );

        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);

        return new FactCheckEvaluation(
                question,
                aiResponse,
                evaluationResponse.isPass()
        );
    }

    public record FactCheckEvaluation(
            String question,
            String answer,
            boolean isFactuallyCorrect
    ) {}
}