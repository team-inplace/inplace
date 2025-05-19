package team7.inplace.search.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import team7.inplace.influencer.presentation.dto.InfluencerResponse;
import team7.inplace.search.application.dto.AutoCompletionInfo;
import team7.inplace.search.presentation.dto.SearchRequest;
import team7.inplace.search.presentation.dto.SearchResponse;
import team7.inplace.video.presentation.dto.VideoResponse;

@Tag(name = "검색 API입니다.")
public interface SearchControllerApiSpec {

    @Operation(summary = "추천 검색어를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "검색어 조회 성공")
    ResponseEntity<List<AutoCompletionInfo>> searchKeywords(
        @ModelAttribute SearchRequest.AutoComplete request
    );

    @Operation(summary = "비디오를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "비디오 검색 성공")
    ResponseEntity<List<VideoResponse.Detail>> searchVideo(String keyword);

    @Operation(summary = "인플루언서를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "인플루언서 검색 성공")
    ResponseEntity<List<InfluencerResponse.Info>> getInfluencersForPaging(String keyword);

    @Operation(summary = "장소를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "장소 검색 성공")
    ResponseEntity<List<SearchResponse.Place>> searchPlace(String keyword);

    @Operation(summary = "인플루언서 검색창 전용 / 페이징 처리된 인플루언서 페이지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "인플루언서 페이지 조회 성공")
    ResponseEntity<Page<InfluencerResponse.Info>> getInfluencersForPaging(
        String keyword, @PageableDefault Pageable pageable
    );
}
