package eu.konverto.mampf;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple example demonstrating the use of FactCheckingEvaluator
 * to test if AI responses are factually accurate based on provided documents.
 */
@SpringBootTest
public class RecipeFactCheckingEvaluatorTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Test
    public void testCarbonaraIngredientFactCheck() {
        ChatClient chatClient = chatClientBuilder.build();

        String userQuery = "What are the main ingredients in pasta carbonara?";

        String aiResponse = chatClient
                .prompt()
                .user(userQuery)
                .call()
                .content();

        List<Document> factualDocuments = List.of(
                new Document("Pasta carbonara is made with eggs, Pecorino Romano cheese, guanciale, and black pepper."),
                new Document("Traditional carbonara does NOT contain cream, garlic, or onions.")
        );

        FactCheckingEvaluator evaluator = new FactCheckingEvaluator(chatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                userQuery,
                factualDocuments,  // The facts to check against
                aiResponse
        );

        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);

        System.out.println("User Query: " + userQuery);
        System.out.println("AI Response: " + aiResponse);
        System.out.println("Factual Accuracy Score: " + evaluationResponse.getScore());
        System.out.println("Is Factually Correct: " + evaluationResponse.isPass());

        assertTrue(evaluationResponse.isPass(),
                "The AI response should be factually accurate according to the provided documents");
    }

    @Test
    public void testIncorrectQuestionFactCheck() {
        ChatClient chatClient = chatClientBuilder.build();

        String userQuery = "Does carbonara contain cream?";

        String aiResponse = chatClient
                .prompt()
                .user(userQuery)
                .call()
                .content();

        List<Document> factualDocuments = List.of(
                new Document("Traditional Italian carbonara is made without cream. The creamy texture comes from the eggs and cheese."),
                new Document("Adding cream to carbonara is considered inauthentic by traditional Italian cooking standards.")
        );

        FactCheckingEvaluator evaluator = new FactCheckingEvaluator(chatClientBuilder);

        EvaluationRequest evaluationRequest = new EvaluationRequest(
                userQuery,
                factualDocuments,
                aiResponse
        );

        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);

        System.out.println("\n--- Cream in Carbonara Fact Check ---");
        System.out.println("User Query: " + userQuery);
        System.out.println("AI Response: " + aiResponse);
        System.out.println("Factual Accuracy Score: " + evaluationResponse.getScore());
        System.out.println("Is Factually Correct: " + evaluationResponse.isPass());

        assertTrue(evaluationResponse.isPass(),
                "The AI should correctly state facts about carbonara ingredients");
    }
}

