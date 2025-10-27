package my.inplace.api.post;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import my.inplace.application.post.command.PostCommandFacade;
import my.inplace.api.post.dto.PostRequest;
import my.inplace.api.post.dto.PostRequest.UpsertComment;
import my.inplace.api.post.dto.PostRequest.UpsertPost;
import my.inplace.api.post.dto.PostResponse.DetailedComment;
import my.inplace.api.post.dto.PostResponse.DetailedPost;
import my.inplace.api.post.dto.PostResponse.DetailedPostImages;
import my.inplace.api.post.dto.PostResponse.SimpleList;
import my.inplace.api.post.dto.PostResponse.UserSuggestion;
import my.inplace.application.post.query.PostQueryFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController implements PostControllerApiSpec {

    private final PostQueryFacade postQueryFacade;
    private final PostCommandFacade postCommandFacade;

    @Override
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody UpsertPost postRequest) {
        postCommandFacade.createPost(postRequest.toCommand());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PutMapping("/{postId}")
    public ResponseEntity<Void> updatePost(
        @PathVariable Long postId,
        @RequestBody UpsertPost postRequest
    ) {
        postCommandFacade.updatePost(postRequest.toUpdateCommand(postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PostMapping("/likes")
    public ResponseEntity<Void> likePost(
        @RequestBody PostRequest.PostLike request
    ) {
        postCommandFacade.likePost(request.toCommand());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @PathVariable Long postId
    ) {
        postCommandFacade.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<SimpleList> getPosts(
        @RequestParam(required = false) Long cursorValue,
        @RequestParam(required = false) Long cursorId,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "createdAt") String sort
    ) {
        var posts = postQueryFacade.getPosts(cursorValue, cursorId, size, sort);

        var response = SimpleList.from(posts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{postId}")
    public ResponseEntity<DetailedPost> getPostById(@PathVariable Long postId) {
        var post = postQueryFacade.getPostById(postId);

        var response = DetailedPost.from(post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{postId}/images/details")
    public ResponseEntity<DetailedPostImages> getPostImageDetails(
        @PathVariable Long postId
    ) {
        var postImage = postQueryFacade.getPostImageDetails(postId);

        var response = DetailedPostImages.from(postImage);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(
        @PathVariable Long postId,
        @RequestBody UpsertComment commentRequest
    ) {
        postCommandFacade.createComment(commentRequest.toCommand(postId));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @PostMapping("/{postId}/comments/likes")
    public ResponseEntity<Void> likeComment(
        @PathVariable("postId") Long postId,
        @RequestBody PostRequest.CommentLike request
    ) {
        postCommandFacade.likeComment(request.toCommand(postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId,
        @RequestBody UpsertComment commentRequest
    ) {
        postCommandFacade.updateComment(commentRequest.toUpdateCommand(commentId, postId));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId
    ) {
        postCommandFacade.deleteComment(postId, commentId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @GetMapping("/{postId}/comments")
    public ResponseEntity<Page<DetailedComment>> getCommentsByPostId(
        @PathVariable("postId") Long postId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        var comments = postQueryFacade.getCommentsByPostId(postId, pageable);

        var response = comments.map(DetailedComment::from);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{postId}/comments/complete")
    public ResponseEntity<List<UserSuggestion>> getCommentUserSuggestions(
        @PathVariable(value = "postId") Long postId,
        @RequestParam(value = "value", required = true) String value
    ) {
        var userSuggestions = postQueryFacade.getCommentUserSuggestions(postId, value);

        var response = userSuggestions.stream()
            .map(UserSuggestion::from)
            .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
