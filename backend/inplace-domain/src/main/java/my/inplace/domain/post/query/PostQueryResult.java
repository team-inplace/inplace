package my.inplace.domain.post.query;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

public class PostQueryResult {

    public record DetailedPost(
        Long postId,
        String userNickname,
        String userImageUrl,
        String tierImageUrl,
        String mainBadgeImageUrl,
        String title,
        String content,
        List<Image> imageInfos,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

        public DetailedPost(
            Long postId,
            String userNickname,
            String userImageUrl,
            String tierImageUrl,
            String mainBadgeImageUrl,
            String title,
            String content,
            JsonNode imageInfosNode,
            Boolean selfLike,
            Integer totalLikeCount,
            Integer totalCommentCount,
            Boolean isMine,
            LocalDateTime createdAt
        ) {
            this(
                postId,
                userNickname,
                userImageUrl,
                tierImageUrl,
                mainBadgeImageUrl,
                title,
                content,
                convertJsonNodeToImageList(imageInfosNode),
                selfLike,
                totalLikeCount,
                totalCommentCount,
                isMine,
                createdAt
            );
        }

        private static List<Image> convertJsonNodeToImageList(JsonNode imageInfosNode) {
            if (imageInfosNode == null || !imageInfosNode.isArray()) {
                return List.of();
            }

            return StreamSupport.stream(imageInfosNode.spliterator(), false)
                .filter(node -> node.has("imageUrl") && node.has("imageHash"))
                .map(node -> new Image(
                    node.get("imageUrl").asText(),
                    node.get("imageHash").asText()
                ))
                .toList();
        }
    }

    public record CursorDetailedPost(
        DetailedPost detailedPost,
        Long cursorId
    ) {

    }

    public record UserSuggestion(
        Long userId,
        String userNickname,
        String userImageUrl
    ) {

    }

    public record Image(
        String imageUrl,
        String imageHash
    ) {

    }
}
