package eu.konverto.mampf;

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
public class FactCheckController {
    private final ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;

    public FactCheckController(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public FactCheckEvaluation recipeFactCheck(
            @RequestParam(defaultValue = "What is the main ingredient in carbonara?") String question) {

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
                evaluationResponse.getScore(),
                evaluationResponse.isPass()
        );
    }

    public record FactCheckEvaluation(
            String question,
            String answer,
            double factualAccuracyScore,
            boolean isFactuallyCorrect
    ) {}
}