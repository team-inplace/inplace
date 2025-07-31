package team7.inplace.post.presentation.dto;

import java.util.List;
import team7.inplace.post.application.dto.PostCommand;

public class PostRequest {

    public record UpsertPost(
        String title,
        String content,
        List<UpsertPhoto> imageUrls
    ) {

        public PostCommand.CreatePost toCommand() {
            return new PostCommand.CreatePost(
                title,
                content,
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(UpsertPhoto::imageUrl)
                    .toList(),
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(UpsertPhoto::hash)
                    .toList()
            );
        }

        public PostCommand.UpdatePost toUpdateCommand(Long postId) {
            return new PostCommand.UpdatePost(
                postId,
                title,
                content,
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(UpsertPhoto::imageUrl)
                    .toList(),
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(UpsertPhoto::hash)
                    .toList()
            );
        }
    }

    public record UpsertPhoto(
        String imageUrl,
        String hash
    ) {

    }

    public record UpsertComment(
        String comment
    ) {

        public PostCommand.CreateComment toCommand(Long postId) {
            return new PostCommand.CreateComment(postId, comment);
        }

        public PostCommand.UpdateComment toUpdateCommand(Long commentId, Long postId) {
            return new PostCommand.UpdateComment(commentId, postId, comment);
        }
    }

    public record PostLike(
        Long postId,
        Boolean likes
    ) {

        public PostCommand.PostLike toCommand() {
            return new PostCommand.PostLike(postId, likes);
        }
    }

    public record CommentLike(
        Long commentId,
        Boolean likes
    ) {

        public PostCommand.CommentLike toCommand(Long postId) {
            return new PostCommand.CommentLike(postId, commentId, likes);
        }
    }
}
