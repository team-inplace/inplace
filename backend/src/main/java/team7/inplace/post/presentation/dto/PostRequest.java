package team7.inplace.post.presentation.dto;

import java.util.List;
import team7.inplace.post.application.dto.PostCommand;

public class PostRequest {

    public record CreatePost(
        String title,
        String content,
        List<CreatePhoto> imageUrls
    ) {

        public PostCommand.CreatePost toCommand() {
            return new PostCommand.CreatePost(
                title,
                content,
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(CreatePhoto::imageUrl)
                    .toList(),
                imageUrls == null ? List.of() : imageUrls.stream()
                    .map(CreatePhoto::imgHash)
                    .toList()
            );
        }
    }

    public record CreateComment(
        String content
    ) {

        public PostCommand.CreateComment toCommand(Long postId) {
            return new PostCommand.CreateComment(postId, content);
        }
    }

    public record CreatePhoto(
        String imageUrl,
        String imgHash
    ) {

    }
}
