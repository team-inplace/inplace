package team7.inplace.post.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team7.inplace.global.converter.JsonNodeConverter;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostPhoto {

    @Convert(converter = JsonNodeConverter.class)
    private JsonNode imageUrls;

    public PostPhoto(List<String> imageUrls, List<String> imgHashes) {
        validatePhoto(imageUrls, imgHashes);
        this.imageUrls = photosToJsonNode(imageUrls, imgHashes);
    }

    public List<String> getImageUrls() {
        return imageUrls.findValuesAsText("imageUrl").stream().toList();
    }

    public List<String> getImgHashes() {
        return imageUrls.findValuesAsText("imgHash").stream().toList();
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
                .put("imgHash", imgHashes.get(i));
        }
        return JsonNodeFactory.instance.arrayNode().addAll(Arrays.asList(photosArray));
    }

    public PostPhoto update(List<String> imageUrls, List<String> imgHashes) {
        return new PostPhoto(imageUrls, imgHashes);
    }

}
