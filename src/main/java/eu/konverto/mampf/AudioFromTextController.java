package eu.konverto.mampf;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.AudioParameters;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/text-audio")
public class AudioFromTextController {
    private final ChatModel chatModel;

    public AudioFromTextController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    public ResponseEntity<String> getAudio(@RequestParam(name = "q", defaultValue = "What is pasta carbonara") String query) {
        var userMessage = new UserMessage(query);

        ChatResponse response = chatModel.call(new Prompt(
                List.of(userMessage),
                OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW)
                        .outputModalities(List.of("text", "audio"))
                        .outputAudio(new AudioParameters(AudioParameters.Voice.ALLOY, AudioParameters.AudioResponseFormat.WAV))
                        .build()
        ));

        byte[] waveAudio = response.getResult().getOutput().getMedia().get(0).getDataAsByteArray();
        String base64Audio = java.util.Base64.getEncoder().encodeToString(waveAudio);

        String html = "<html><body>" +
                "<audio controls>" +
                "<source src='data:audio/wav;base64," + base64Audio + "' type='audio/wav'>" +
                "Your browser does not support the audio element." +
                "</audio>" +
                "</body></html>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return ResponseEntity.ok()
                .headers(headers)
                .body(html);
    }
}
