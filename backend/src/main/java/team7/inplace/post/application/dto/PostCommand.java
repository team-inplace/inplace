package team7.inplace.post.application.dto;

import java.util.List;
import team7.inplace.post.domain.Comment;
import team7.inplace.post.domain.Post;

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
        String content
    ) {

        public Comment toEntity(Long authorId) {
            return new Comment(postId, authorId, content);
        }
    }
}
