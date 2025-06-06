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
import team7.inplace.post.persistence.PostPhotoJapRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostPhotoJapRepository postPhotoJapRepository;
    private final CommentJpaRepository commentJpaRepository;

    @Transactional
    public void createPost(CreatePost command, Long userId) {
        var post = command.toPostEntity(userId);
        postJpaRepository.save(post);
        
        var photos = command.toPostPhotos(post.getId());
        if (photos.isEmpty()) {
            return;
        }
        postPhotoJapRepository.saveAll(photos.get());
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
