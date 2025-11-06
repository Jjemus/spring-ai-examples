package eu.konverto.mampf;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/evaluation")
public class TestingEvaluationController {
    private final ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;

    public TestingEvaluationController(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public RecipeEvaluation recipeWithEvaluation(@RequestParam(defaultValue = "a delicious dish") String dish) {
        String userQuery = "Give me a recipe for " + dish;

        String aiResponse = chatClient
                .prompt()
                .user(userQuery)
                .call()
                .content();

        RelevancyEvaluator evaluator = new RelevancyEvaluator(chatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                userQuery,
                List.of(),
                aiResponse
        );

        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);

        return new RecipeEvaluation(
                dish,
                aiResponse,
                evaluationResponse.getScore(),
                evaluationResponse.isPass()
        );
    }

    private record RecipeEvaluation(
            String dishRequested,
            String recipe,
            double relevancyScore,
            boolean isRelevant
    ) {}
}
