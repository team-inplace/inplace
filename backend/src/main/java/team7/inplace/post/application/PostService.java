package team7.inplace.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;
import team7.inplace.post.application.dto.PostCommand.CreateComment;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.persistence.CommentJpaRepository;
import team7.inplace.post.persistence.PostJpaRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional
    public void createPost(CreatePost command, Long userId) {
        var post = command.toPostEntity(userId);
        postJpaRepository.save(post);
    }

    @Transactional
    public void createComment(CreateComment command, Long userId) {
        if (postJpaRepository.existsById(command.postId())) {
            var comment = command.toEntity(userId);
            commentJpaRepository.save(comment);
            return;
        }
        throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
    }
}
