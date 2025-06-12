package team7.inplace.post.persistence.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.querydsl.core.annotations.QueryProjection;
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
        JsonNode imageInfos,
        Boolean selfLike,
        Integer totalLikeCount,
        Integer totalCommentCount,
        Boolean isMine,
        LocalDateTime createdAt
    ) {

        @QueryProjection
        public DetailedPost {
        }

        public List<String> getImageUrls() {
            if (imageInfos == null || !imageInfos.isArray()) {
                return List.of();
            }

            return StreamSupport.stream(imageInfos.spliterator(), false)
                .map(node -> node.get("imageUrl"))
                .filter(urlNode -> urlNode != null && urlNode.isTextual())
                .map(JsonNode::asText)
                .toList();
        }

        public List<String> getImgHashes() {
            return imageInfos.findValuesAsText("imageHash").stream().toList();
        }
    }

    public record CursorDetailedPost(
        DetailedPost detailedPost,
        Long cursorId
    ) {

        @QueryProjection
        public CursorDetailedPost {
        }
    }
}
