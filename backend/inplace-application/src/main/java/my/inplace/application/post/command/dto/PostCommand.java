package my.inplace.application.post.command.dto;

import java.util.List;
import my.inplace.domain.post.Comment;
import my.inplace.domain.post.Post;

public class PostCommand {

    public record CreatePost(
        String title,
        String content,
        List<String> imageUrls,
        List<String> imgHashes
    ) {

        public Post toPostEntity(Long authorId) {
            return new Post(title, content, imageUrls, imgHashes, authorId);
        }
    }

    public record UpdatePost(
        Long postId,
        String title,
        String content,
        List<String> imageUrls,
        List<String> imgHashes
    ) {

    }

    public record CreateComment(
        Long postId,
        String comment
    ) {

        public Comment toEntity(Long authorId) {
            return new Comment(postId, authorId, comment);
        }
    }

    public record UpdateComment(
        Long commentId,
        Long postId,
        String comment
    ) {

    }

    public record PostLike(
        Long postId,
        Boolean likes
    ) {

    }

    public record CommentLike(
        Long postId,
        Long commentId,
        Boolean likes
    ) {

    }
}
