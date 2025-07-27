package team7.inplace.post.presentation;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import team7.inplace.post.presentation.dto.PostRequest;
import team7.inplace.post.presentation.dto.PostRequest.UpsertComment;
import team7.inplace.post.presentation.dto.PostRequest.UpsertPost;
import team7.inplace.post.presentation.dto.PostResponse.DetailedComment;
import team7.inplace.post.presentation.dto.PostResponse.DetailedPost;
import team7.inplace.post.presentation.dto.PostResponse.DetailedPostImages;
import team7.inplace.post.presentation.dto.PostResponse.SimpleList;
import team7.inplace.post.presentation.dto.PostResponse.UserSuggestion;

@RequestMapping("/posts")
@Tag(name = "게시글 관련 API", description = "게시글 관련 API입니다.")
public interface PostControllerApiSpec {

    @PostMapping
    ResponseEntity<Void> createPost(@RequestBody UpsertPost postRequest);

    @PutMapping("/{postId}")
    ResponseEntity<Void> updatePost(
        @PathVariable(value = "postId") Long postId,
        @RequestBody PostRequest.UpsertPost postRequest
    );

    @PostMapping("/likes")
    ResponseEntity<Void> likePost(
        @RequestBody PostRequest.PostLike request
    );

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable(value = "postId") Long postId);

    @GetMapping()
    @ApiResponse(
        responseCode = "200",
        description = "게시글 목록 조회 성공",
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SimpleList.class)
        )
    )
    ResponseEntity<SimpleList> getPosts(
        @RequestParam(value = "cursorId", required = false) Long cursorId,
        @RequestParam(value = "size", defaultValue = "5") int size,
        @RequestParam(value = "sort", defaultValue = "createAt") String sort
    );

    @GetMapping("/{postId}")
    @ApiResponse(
        responseCode = "200",
        description = "게시글 상세 조회 성공",
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DetailedPost.class)
        )
    )
    ResponseEntity<DetailedPost> getPostById(@PathVariable(value = "postId") Long postId);

    @GetMapping("/{postId}/images/details")
    @ApiResponse(
        responseCode = "200",
        description = "게시글 이미지 상세 조회 성공",
        content = @io.swagger.v3.oas.annotations.media.Content(
            mediaType = "application/json",
            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DetailedPostImages.class)
        )
    )
    ResponseEntity<DetailedPostImages> getPostImageDetails(
        @PathVariable(value = "postId") Long postId
    );

    @PostMapping("/{postId}/comments")
    ResponseEntity<Void> createComment(
        @PathVariable(value = "postId") Long postId,
        @RequestBody UpsertComment commentRequest
    );

    @PostMapping("/{postId}/comments/likes")
    ResponseEntity<Void> likeComment(
        @PathVariable(value = "postId") Long postId,
        @RequestBody PostRequest.CommentLike request
    );

    @PutMapping("/{postId}/comments/{commentId}")
    ResponseEntity<Void> updateComment(
        @PathVariable(value = "postId") Long postId,
        @PathVariable(value = "commentId") Long commentId,
        @RequestBody UpsertComment commentRequest
    );

    @DeleteMapping("/{postId}/comments/{commentId}")
    ResponseEntity<Void> deleteComment(
        @PathVariable(value = "postId") Long postId,
        @PathVariable(value = "commentId") Long commentId
    );

    @GetMapping("/{postId}/comments")
    ResponseEntity<Page<DetailedComment>> getCommentsByPostId(
        @PathVariable(value = "postId") Long postId,
        @PageableDefault(size = 10) Pageable pageable
    );

    @GetMapping("/{postId}/comments/complete")
    ResponseEntity<List<UserSuggestion>> getCommentUserSuggestions(
        @PathVariable(value = "postId") Long postId,
        @RequestParam(value = "value", required = true) String value
    );
}
