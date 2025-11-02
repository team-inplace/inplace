package my.inplace.application.post.query.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import my.inplace.application.user.dto.UserResult;
import my.inplace.domain.post.Comment;
import my.inplace.domain.post.Post;
import my.inplace.domain.post.query.PostQueryResult;

public class PostResult {

    public record PostImages(
        List<PostImage> images
    ) {

        public static PostImages from(Post post) {
            List<PostImage> images = new ArrayList<>();
            for (int i = 0; i < post.getImageUrls().size(); i++) {
                images.add(new PostImage(post.getImageUrls().get(i), post.getImgHashes().get(i)));
            }
            return new PostImages(images);
        }
    }

    public record PostImage(
        String imageUrl,
        String imageHash
    ) {

    }

    public record ReportedPost(
        Long id,
        Long authorId,
        String content,
        Boolean isReported,
        LocalDateTime deleteAt
    ) {

        public static ReportedPost from(Post post) {
            return new ReportedPost(
                post.getId(),
                post.getAuthorId(),
                post.getContent(),
                post.getIsReported(),
                post.getDeleteAt()
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

        public static ReportedComment from(Comment comment) {
            return new ReportedComment(
                comment.getId(),
                comment.getAuthorId(),
                comment.getContent(),
                comment.getIsReported(),
                comment.getDeleteAt()
            );
        }

    }

    public record DetailedPost(
        Long postId,
        String userNickname,
        String userImageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl,
        String title,
        String content,
        List<PostImage> imageInfos, // TODO : JsonNode -> List<Image> 반환으로 변경
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

        public static DetailedPost from(PostQueryResult.DetailedPost post) {
            return new DetailedPost(
                post.postId(),
                post.userNickname(),
                post.userImageUrl(),
                post.tierImageUrl(),
                post.mainBadgeImageUrl(),
                post.title(),
                post.content(),
                post.imageInfos().stream()
                    .map(image -> new PostImage(image.imageUrl(), image.imageHash()))
                    .toList(),
                post.selfLike(),
                post.totalLikeCount(),
                post.totalCommentCount(),
                post.isMine(),
                post.createdAt()
            );
        }
        
        public static DetailedPost of(MyPost myPost, UserResult.Simple userResult) {
            return new DetailedPost(
                myPost.postId(),
                userResult.nickname(),
                userResult.profileImageUrl(),
                userResult.tierImageUrl(),
                userResult.mainBadgeImageUrl(),
                myPost.title(),
                myPost.content(),
                myPost.postImages().images(),
                myPost.selfLiked(),
                myPost.totalLike(),
                myPost.totalComment(),
                Boolean.TRUE,
                myPost.createdAt()
            );
        }
    }
    
    public record MyPost(
        Long postId,
        String title,
        String content,
        PostImages postImages,
        Boolean selfLiked,
        Integer totalLike,
        Integer totalComment,
        LocalDateTime createdAt
    ) {
        public static MyPost of(Post post, Boolean selfLiked) {
            return new MyPost(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                PostImages.from(post),
                selfLiked,
                post.getTotalLikeCount(),
                post.getTotalCommentCount(),
                post.getCreatedAt()
            );
        }
    }

    public record CursorDetailedPost(
        PostQueryResult.DetailedPost detailedPost,
        Long cursorId
    ) {

    }

    public record UserSuggestion(
        Long userId,
        String userNickname,
        String userImageUrl
    ) {

        public static UserSuggestion from(PostQueryResult.UserSuggestion user) {
            return new UserSuggestion(
                user.userId(),
                user.userNickname(),
                user.userImageUrl()
            );
        }

    }
}
