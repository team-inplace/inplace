package team7.inplace.post.application.dto;

import java.util.List;
import java.util.Optional;
import team7.inplace.post.domain.Comment;
import team7.inplace.post.domain.Post;
import team7.inplace.post.domain.PostPhoto;

public class PostCommand {

    public record CreatePost(
        String title,
        String content,
        List<String> imageUrls
    ) {

        public Post toPostEntity(Long authorId) {
            return new Post(title, content, authorId);
        }

        public Optional<List<PostPhoto>> toPostPhotos(Long postId) {
            if (imageUrls == null || imageUrls.isEmpty()) {
                return Optional.empty();
            }
            List<PostPhoto> postPhotos = imageUrls.stream()
                .map(imageUrl -> new PostPhoto(postId, imageUrl))
                .toList();
            return Optional.of(postPhotos);
        }
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
