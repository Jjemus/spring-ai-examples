package eu.konverto.mampf;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple example demonstrating the use of RelevancyEvaluator
 * to test if AI responses are relevant to user queries.
 */
@SpringBootTest
public class RecipeRelevancyEvaluatorTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Test
    public void testRecipeResponseRelevancy() {
        // Create a ChatClient
        ChatClient chatClient = chatClientBuilder.build();

        // Define the user query
        String userQuery = "Give me a recipe for pasta carbonara";

        // Get the AI response
        String aiResponse = chatClient
                .prompt()
                .user(userQuery)
                .call()
                .content();

        // Create a RelevancyEvaluator with the same ChatClient builder
        RelevancyEvaluator evaluator = new RelevancyEvaluator(chatClientBuilder);

        // Build an evaluation request
        EvaluationRequest evaluationRequest = new EvaluationRequest(
                userQuery,      // The original user question
                List.of(),           // Supporting data (optional context)
                aiResponse      // The AI-generated response to evaluate
        );

        // Evaluate the response
        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);

        // Print the results
        System.out.println("User Query: " + userQuery);
        System.out.println("AI Response: " + aiResponse);
        System.out.println("Evaluation Score: " + evaluationResponse.getScore());
        System.out.println("Is Passing: " + evaluationResponse.isPass());

        // Assert that the response is relevant (passes the evaluation)
        assertTrue(evaluationResponse.isPass(),
                "The AI response should be relevant to the recipe query");
    }
}

