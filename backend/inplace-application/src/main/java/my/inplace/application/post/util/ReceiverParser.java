package my.inplace.application.post.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ReceiverParser {
    
    private static final String PARSING_REGEX = "<<@([^:>]+):([^>]+)>>";
    
    public List<String> parseMentionedUser(String content) {
        List<String> mentions = new ArrayList<>();
        Pattern pattern = Pattern.compile(PARSING_REGEX);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            mentions.add(matcher.group(2));
        }
        
        return mentions;
    }
}
