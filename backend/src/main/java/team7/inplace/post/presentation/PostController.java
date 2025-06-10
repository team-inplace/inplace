package team7.inplace.post.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.post.application.PostFacade;
import team7.inplace.post.presentation.dto.PostRequest.UpsertComment;
import team7.inplace.post.presentation.dto.PostRequest.UpsertPost;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostControllerApiSpec {

    private final PostFacade postFacade;

    @Override
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody UpsertPost postRequest) {
        postFacade.createPost(postRequest.toCommand());

        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable Long postId,
        @RequestBody UpsertPost postRequest
    ) {
        postFacade.updatePost(postRequest.toUpdateCommand(postId));

        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId
    ) {
        postFacade.deletePost(postId);

        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(
        @PathVariable Long postId,
        @RequestBody UpsertComment commentRequest
    ) {
        postFacade.createComment(commentRequest.toCommand(postId));

        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId,
        @RequestBody UpsertComment commentRequest
    ) {
        postFacade.updateComment(commentRequest.toUpdateCommand(commentId, postId));

        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId
    ) {
        postFacade.deleteComment(postId, commentId);

        return ResponseEntity.ok().build();
    }
}
