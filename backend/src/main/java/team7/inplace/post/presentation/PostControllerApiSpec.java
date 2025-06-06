package team7.inplace.post.presentation;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import team7.inplace.post.presentation.dto.PostRequest;

@RequestMapping("/posts")
@Tag(name = "게시글 관련 API", description = "게시글 관련 API입니다.")
public interface PostControllerApiSpec {

    ResponseEntity<Void> createPost(@RequestBody PostRequest.CreatePost postRequest);

    ResponseEntity<Void> createComment(
        @PathVariable(value = "postId") Long postId,
        @RequestBody PostRequest.CreateComment commentRequest
    );
}
