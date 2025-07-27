package team7.inplace.post.application.dto;

import java.util.ArrayList;
import java.util.List;
import team7.inplace.post.domain.Post;

public class PostInfo {

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
}
