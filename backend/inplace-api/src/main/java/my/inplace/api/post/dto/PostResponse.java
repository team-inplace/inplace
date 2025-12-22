package my.inplace.api.post.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import my.inplace.common.cursor.CursorResult;
import my.inplace.api.global.CursorResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import my.inplace.application.post.query.dto.CommentResult;
import my.inplace.application.post.query.dto.PostResult;
import my.inplace.api.user.dto.UserResponse;

public class PostResponse {

    public static String formatCreatedAt(LocalDateTime createdAt) {
        var now = LocalDateTime.now();
        var duration = Duration.between(createdAt, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) {
            return "방금 전";
        }
        if (minutes < 60) {
            return minutes + "분 전";
        }
        if (hours < 24) {
            return hours + "시간 전";
        }
        if (days < 7) {
            return days + "일 전";
        }
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public record SimpleList(
        List<SimplePost> contents,
        CursorResponse cursor
    ) {

        public static SimpleList from(
            CursorResult<PostResult.DetailedPost> postResult
        ) {
            List<SimplePost> posts = postResult.value().stream()
                .map(SimplePost::from)
                .toList();

            return new SimpleList(
                posts,
                new CursorResponse(
                    postResult.hasNext(),
                    postResult.nextCursorValue(),
                    postResult.nextCursorId()
                )
            );
        }
    }

    public record SimplePost(
        Long postId,
        UserResponse.Simple author,
        String title,
        String content,
        @JsonInclude(NON_NULL)
        String imageUrl,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        String createdAt
    ) {

        public static SimplePost from(PostResult.DetailedPost postResult) {
            return new SimplePost(
                postResult.postId(),
                new UserResponse.Simple(
                    postResult.userNickname(),
                    postResult.userImageUrl(),
                    postResult.tierImageUrl(),
                    postResult.mainBadgeImageUrl()
                ),
                postResult.title(),
                postResult.content(),
                postResult.imageInfos().isEmpty()
                    ? null
                    : postResult.imageInfos().get(0).imageUrl(),
                postResult.selfLike(),
                postResult.totalLikeCount(),
                postResult.totalCommentCount(),
                postResult.isMine(),
                formatCreatedAt(postResult.createdAt())
            );
        }
    }

    public record DetailedPost(
        Long postId,
        UserResponse.Simple author,
        String title,
        String content,
        List<SimplePostImage> imageUrls,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        String createdAt
    ) {

        public static DetailedPost from(PostResult.DetailedPost postResult) {
            List<SimplePostImage> images = postResult.imageInfos().stream()
                .map(image -> new SimplePostImage(image.imageUrl()))
                .toList();
            return new DetailedPost(
                postResult.postId(),
                new UserResponse.Simple(
                    postResult.userNickname(),
                    postResult.userImageUrl(),
                    postResult.tierImageUrl(),
                    postResult.mainBadgeImageUrl()
                ),
                postResult.title(),
                postResult.content(),
                images,
                postResult.selfLike(),
                postResult.totalLikeCount(),
                postResult.totalCommentCount(),
                postResult.isMine(),
                formatCreatedAt(postResult.createdAt())
            );
        }
    }

    public record SimplePostImage(
        String imageUrl
    ) {

    }

    public record DetailedPostImages(
        List<DetailedPostImage> images
    ) {

        public static DetailedPostImages from(PostResult.PostImages images) {
            List<DetailedPostImage> detailedImages = images.images().stream()
                .map(image -> new DetailedPostImage(image.imageUrl(), image.imageHash()))
                .toList();
            return new DetailedPostImages(detailedImages);
        }
    }

    public record DetailedPostImage(
        String imageUrl,
        String imageHash
    ) {

    }

    public record DetailedComment(
        Long commentId,
        UserResponse.Simple author,
        String content,
        Boolean selfLike,
        Integer totalLikeCount,
        Boolean isMine,
        String createdAt
    ) {

        public static DetailedComment from(CommentResult.DetailedComment commentQueryResult) {
            return new DetailedComment(
                commentQueryResult.commentId(),
                new UserResponse.Simple(commentQueryResult.userNickname(),
                    commentQueryResult.userImageUrl(),
                    commentQueryResult.tierImageUrl(),
                    commentQueryResult.mainBadgeImageUrl()),
                commentQueryResult.content(),
                commentQueryResult.selfLike(),
                commentQueryResult.totalLikeCount(),
                commentQueryResult.isMine(),
                formatCreatedAt(commentQueryResult.createdAt())
            );
        }
    }

    public record UserSuggestion(
        Long userId,
        String nickname,
        String imageUrl
    ) {

        public static UserSuggestion from(PostResult.UserSuggestion userSuggestion) {
            return new UserSuggestion(
                userSuggestion.userId(),
                userSuggestion.userNickname(),
                userSuggestion.userImageUrl()
            );
        }
    }

    public record ReportedPost(
        Long id,
        Long authorId,
        String content,
        Boolean isReported,
        LocalDateTime deleteAt
    ) {

        public static ReportedPost from(PostResult.ReportedPost reportedPost) {
            return new ReportedPost(
                reportedPost.id(),
                reportedPost.authorId(),
                reportedPost.content(),
                reportedPost.isReported(),
                reportedPost.deleteAt()
            );
        }
    }

    public record ReportedComment(
        Long id,
        Long authorId,
        String content,
        Boolean isReported,
        LocalDateTime deleteAt
    ) {

        public static ReportedComment from(PostResult.ReportedComment reportedComment) {
            return new ReportedComment(
                reportedComment.id(),
                reportedComment.authorId(),
                reportedComment.content(),
                reportedComment.isReported(),
                reportedComment.deleteAt()
            );
        }
    }
}
