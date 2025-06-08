package team7.inplace.post.application;

import lombok.RequiredArgsConstructor;
import team7.inplace.global.annotation.Facade;
import team7.inplace.post.application.dto.PostCommand;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.security.util.AuthorizationUtil;

@Facade
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public void createPost(CreatePost command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createPost(command, userId);
    }

    public void createComment(PostCommand.CreateComment command) {
        var userId = AuthorizationUtil.getUserIdOrThrow();
        postService.createComment(command, userId);
    }
}
