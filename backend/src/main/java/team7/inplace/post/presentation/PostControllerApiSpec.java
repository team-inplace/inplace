package team7.inplace.post.presentation;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import team7.inplace.post.presentation.dto.PostResponse;

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

    @DeleteMapping("/{postId}")
    ResponseEntity<Void> deletePost(@PathVariable(value = "postId") Long postId);

    @GetMapping()
    ResponseEntity<PostResponse.SimpleList> getPosts(
        @RequestParam(value = "cursorId") Long cursorId,
        @RequestParam(value = "size", defaultValue = "5") int size,
        @RequestParam(value = "orderBy", defaultValue = "createAt") String orderBy
    );

    @PostMapping("/{postId}/comments")
    ResponseEntity<Void> createComment(
        @PathVariable(value = "postId") Long postId,
        @RequestBody UpsertComment commentRequest
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
}
