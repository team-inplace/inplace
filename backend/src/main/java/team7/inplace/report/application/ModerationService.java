package team7.inplace.report.application;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.moderation.ModerationPrompt;
import org.springframework.ai.moderation.ModerationResponse;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModerationService {

    private final OpenAiModerationModel moderationModel;

    public boolean isContentFlagged(String content) {
        ModerationPrompt prompt = new ModerationPrompt(content);
        ModerationResponse response = moderationModel.call(prompt);
        return response.getResults().get(0).getOutput().getResults().get(0).isFlagged();
    }
}
