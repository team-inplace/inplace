package my.inplace.api.review;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import my.inplace.api.review.dto.ReviewRequest;
import my.inplace.api.review.dto.ReviewResponse;

@Tag(name = "리뷰", description = "리뷰 관련 API")
public interface ReviewControllerApiSpec {

    @Operation(summary = "리뷰의 정보를 조회합니다.", description = "UUID를 바탕으로 장소정보, 인플루언서 이름, 유저 닉네임을 반환합니다.")
    ResponseEntity<ReviewResponse.Invitation> getReviewInfo(
        @Parameter(description = "리뷰 UUID", required = true) @PathVariable String uuid
    );

    @Operation(summary = "리뷰 작성", description = "장소에 대한 리뷰를 작성합니다.")
    ResponseEntity<Void> saveReview(
        @PathVariable String uuid,
        @RequestBody ReviewRequest.Save request
    );

    @Operation(summary = "리뷰 삭제", description = "본인이 작성한 리뷰를 삭제합니다.")
    ResponseEntity<Void> deleteReview(@PathVariable Long id);
}
