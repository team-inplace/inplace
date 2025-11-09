package my.inplace.application.post.util;

import org.springframework.stereotype.Component;

@Component
public class MentionMessageFactory {
    
    private static final String MENTION_TITLE = "새로운 언급 알림";
    private static final String MENTION_CONTENT = "%s 게시글에서 %s 님이 언급했습니다.";
    
    public String createTitle() {
        return MENTION_TITLE;
    }
    
    public String createMessage(String title, String sender) {
        return String.format(MENTION_CONTENT, title, sender);
    }
}
