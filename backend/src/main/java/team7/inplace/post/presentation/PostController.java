package team7.inplace.post.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team7.inplace.post.application.PostFacade;
import team7.inplace.post.presentation.dto.PostRequest.UpsertComment;
import team7.inplace.post.presentation.dto.PostRequest.UpsertPost;
import team7.inplace.post.presentation.dto.PostResponse.SimpleList;
import team7.inplace.post.presentation.dto.PostResponse.SimplePost;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostControllerApiSpec {

    private final PostFacade postFacade;

    @Override
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody UpsertPost postRequest) {
        postFacade.createPost(postRequest.toCommand());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable Long postId,
        @RequestBody UpsertPost postRequest
    ) {
        postFacade.updatePost(postRequest.toUpdateCommand(postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId
    ) {
        postFacade.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<SimpleList> getPosts(
        @RequestParam Long cursorId,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "") String sort
    ) {
        var posts = postFacade.getPosts(cursorId, size, sort);

        var response = SimpleList.from(posts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<SimplePost> getPostById(@PathVariable Long postId) {
        var post = postFacade.getPostById(postId);

        var response = SimplePost.from(post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(
        @PathVariable Long postId,
        @RequestBody UpsertComment commentRequest
    ) {
        postFacade.createComment(commentRequest.toCommand(postId));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId,
        @RequestBody UpsertComment commentRequest
    ) {
        postFacade.updateComment(commentRequest.toUpdateCommand(commentId, postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId
    ) {
        postFacade.deleteComment(postId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
