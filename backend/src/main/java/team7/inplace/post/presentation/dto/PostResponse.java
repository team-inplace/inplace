package team7.inplace.post.presentation.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import team7.inplace.global.cursor.CursorResponse;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.persistence.dto.PostQueryResult;
import team7.inplace.user.presentation.dto.UserResponse;

public class PostResponse {

    public record DetailedList(
        List<SimplePost> posts,
        CursorResponse cursor
    ) {

        public static DetailedList from(
            CursorResult<PostQueryResult.DetailedPost> postQueryResult
        ) {
            List<SimplePost> posts = postQueryResult.value().stream()
                .map(SimplePost::from)
                .toList();
            return new DetailedList(posts,
                new CursorResponse(postQueryResult.hasNext(), postQueryResult.nextCursorId()));
        }
    }

    public record SimplePost(
        Long postId,
        UserResponse.Info author,
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

        public static SimplePost from(PostQueryResult.DetailedPost postQueryResult) {
            return new SimplePost(
                postQueryResult.postId(),
                new UserResponse.Info(postQueryResult.userNickname(),
                    postQueryResult.userImageUrl()),
                postQueryResult.title(),
                postQueryResult.content(),
                postQueryResult.getImageUrls().isEmpty()
                    ? null
                    : postQueryResult.getImageUrls().get(0),
                postQueryResult.selfLike(),
                postQueryResult.totalLikeCount(),
                postQueryResult.totalCommentCount(),
                postQueryResult.isMine(),
                formatCreatedAt(postQueryResult.createdAt())
            );
        }

        private static String formatCreatedAt(LocalDateTime createdAt) {
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
    }
}
