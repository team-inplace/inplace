package my.inplace.application.alarm.util;

import org.springframework.stereotype.Component;

@Component
public class ReportMessageFactory {

    private static final String REPORT_POST_TITLE = "게시글 신고 삭제 알림";
    private static final String REPORT_COMMENT_TITLE = "댓글 신고 삭제 알림";

    private static final String REPORT_POST_CONTENT = "%s 게시글이 신고 검토 결과 삭제되었습니다.";
    private static final String REPORT_COMMENT_CONTENT = "%s 게시글의 댓글이 신고 검토 결과 삭제되었습니다.";

    public String createPostTitle() {
        return REPORT_POST_TITLE;
    }

    public String createPostMessage(String postTitle) {
        return String.format(REPORT_POST_CONTENT, postTitle);
    }

    public String createCommentTitle() {
        return REPORT_COMMENT_TITLE;
    }

    public String createCommentMessage(String postTitle) {
        return String.format(REPORT_COMMENT_CONTENT, postTitle);
    }
}