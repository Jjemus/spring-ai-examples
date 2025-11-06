package eu.konverto.mampf;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/audio-text")
public class AudioTextController {
    private final ChatModel chatModel;

    public AudioTextController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    public String getAudioText() {
        var audioResource = new ClassPathResource("sample.mp3");

        var userMessage = UserMessage.builder().text("What is this recording about?")
                .media(List.of(new Media(MimeTypeUtils.parseMimeType("audio/mp3"), audioResource)))
                .build();

        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage),
                OpenAiChatOptions.builder().model(OpenAiApi.ChatModel.GPT_4_O_AUDIO_PREVIEW).build()));

        return response.getResult().getOutput().getText();
    }
}
