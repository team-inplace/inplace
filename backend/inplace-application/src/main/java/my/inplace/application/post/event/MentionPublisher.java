package my.inplace.application.post.event;

import lombok.RequiredArgsConstructor;
import my.inplace.application.alarm.event.AlarmEventBuilder;
import my.inplace.domain.alarm.AlarmType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class MentionPublisher {
    private static final String PARSING_REGEX = "<<@([^:>]+):([^>]+)>>";
    
    private final ApplicationEventPublisher eventPublisher;
    
    public void processMention(Long postId, Long commentId, String sender, String commentContent) {
        for (String receiver : parseMentionedUser(commentContent)) {
            AlarmEventBuilder.create()
                .postId(postId)
                .commentId(commentId)
                .receiver(receiver)
                .sender(sender)
                .type(AlarmType.MENTION)
                .publish(eventPublisher);
        }
    }
    
    private List<String> parseMentionedUser(String content) {
        List<String> mentions = new ArrayList<>();
        Pattern pattern = Pattern.compile(PARSING_REGEX);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            mentions.add(matcher.group(2));
        }
        
        return mentions;
    }
}
