package my.inplace.domain.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private JsonNode imageInfos;

    public PostPhoto(List<String> imageInfos, List<String> imgHashes) {
        validatePhoto(imageInfos, imgHashes);
        this.imageInfos = photosToJsonNode(imageInfos, imgHashes);
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
        if (imageInfos == null || !imageInfos.isArray()) {
            return List.of();
        }

        return StreamSupport.stream(imageInfos.spliterator(), false)
            .map(node -> node.get("imageHash"))
            .filter(urlNode -> urlNode != null && urlNode.isTextual())
            .map(JsonNode::asText)
            .toList();
    }

    public Map<String, String> getImageUrlMap() {
        Map<String, String> imageUrlMap = new HashMap<>();
        List<String> imageUrls = getImageUrls();
        List<String> imgHashes = getImgHashes();
        for (int i = 0; i < imageUrls.size(); i++) {
            imageUrlMap.put(imgHashes.get(i), imageUrls.get(i));
        }

        return Map.copyOf(imageUrlMap);
    }

    private void validatePhoto(List<String> imageUrls, List<String> imgHashes) {
        if (imageUrls == null || imgHashes == null) {
            return;
        }
        validateImageSize(imageUrls);
        validateHashDuplicated(imageUrls, imgHashes);
    }

    private void validateImageSize(List<String> imageUrls) {
        if (imageUrls.size() > 10) {
            throw InplaceException.of(PostErrorCode.POST_PHOTOS_SIZE_EXCEEDED);
        }
    }

    private void validateHashDuplicated(List<String> imageUrls, List<String> imgHashes) {
        if (imageUrls.size() == imgHashes.stream().distinct().count()) {
            return;
        }

        throw InplaceException.of(PostErrorCode.POST_PHOTOS_DUPLICATED);
    }

    private JsonNode photosToJsonNode(List<String> imageUrls, List<String> imgHashes) {
        var photosArray = new JsonNode[imageUrls.size()];
        for (int i = 0; i < imageUrls.size(); i++) {
            photosArray[i] = JsonNodeFactory.instance.objectNode()
                .put("imageUrl", imageUrls.get(i))
                .put("imageHash", imgHashes.get(i));
        }
        return JsonNodeFactory.instance.arrayNode().addAll(Arrays.asList(photosArray));
    }

    public PostPhoto update(List<String> imageUrls, List<String> imgHashes) {
        return new PostPhoto(imageUrls, imgHashes);
    }

}
