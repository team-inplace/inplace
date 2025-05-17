package team7.inplace.video.persistence.dto;

public record VideoFilterCondition(
    Boolean registered,
    Long influencerId
) {

    public static VideoFilterCondition of(Boolean registered, Long influencerId) {
        return new VideoFilterCondition(registered, influencerId);
    }
}
