package team7.inplace.post.presentation.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import team7.inplace.global.cursor.CursorResponse;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.post.persistence.dto.CommentQueryResult;
import team7.inplace.post.persistence.dto.PostQueryResult;
import team7.inplace.user.presentation.dto.UserResponse;

public class PostResponse {

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

    public record SimpleList(
        List<SimplePost> posts,
        CursorResponse cursor
    ) {

        public static SimpleList from(
            CursorResult<PostQueryResult.DetailedPost> postQueryResult
        ) {
            List<SimplePost> posts = postQueryResult.value().stream()
                .map(SimplePost::from)
                .toList();
            return new SimpleList(posts,
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
    }

    public record DetailedPost(
        Long postId,
        UserResponse.Info author,
        String title,
        String content,
        List<PostImage> imageUrls,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        String createdAt
    ) {

        public static DetailedPost from(PostQueryResult.DetailedPost postQueryResult) {
            List<PostImage> images = new ArrayList<>();
            for (int i = 0; i < postQueryResult.getImageUrls().size(); i++) {
                images.add(
                    new PostImage(postQueryResult.getImageUrls().get(i),
                        postQueryResult.getImgHashes().get(i)
                    )
                );
            }
            return new DetailedPost(
                postQueryResult.postId(),
                new UserResponse.Info(
                    postQueryResult.userNickname(),
                    postQueryResult.userImageUrl()
                ),
                postQueryResult.title(),
                postQueryResult.content(),
                images,
                postQueryResult.selfLike(),
                postQueryResult.totalLikeCount(),
                postQueryResult.totalCommentCount(),
                postQueryResult.isMine(),
                formatCreatedAt(postQueryResult.createdAt())
            );
        }
    }

    public record PostImage(
        String imageUrl,
        String imageHash
    ) {

    }

    public record DetailedComment(
        Long commentId,
        UserResponse.Info author,
        String content,
        Boolean selfLike,
        Integer totalLikeCount,
        Boolean isMine,
        String createdAt
    ) {

        public static DetailedComment from(CommentQueryResult.DetailedComment commentQueryResult) {
            return new DetailedComment(
                commentQueryResult.commentId(),
                new UserResponse.Info(commentQueryResult.userNickname(),
                    commentQueryResult.userImageUrl()),
                commentQueryResult.content(),
                commentQueryResult.selfLike(),
                commentQueryResult.totalLikeCount(),
                commentQueryResult.isMine(),
                formatCreatedAt(commentQueryResult.createdAt())
            );
        }
    }
}
