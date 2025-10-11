package my.inplace.api.review.dto;


import my.inplace.application.review.dto.ReviewCommand;

public class ReviewRequest {

    public record Save(
        Boolean likes,
        String comments
    ) {

        public ReviewCommand.create toCommandFrom(String uuid) {
            return new ReviewCommand.create(uuid, likes, comments);
        }
    }
}
