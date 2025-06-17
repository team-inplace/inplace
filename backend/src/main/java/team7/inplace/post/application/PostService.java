package team7.inplace.post.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;
import team7.inplace.post.application.dto.PostCommand.CreateComment;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.application.dto.PostCommand.UpdateComment;
import team7.inplace.post.application.dto.PostCommand.UpdatePost;
import team7.inplace.post.persistence.CommentJpaRepository;
import team7.inplace.post.persistence.CommentReadRepository;
import team7.inplace.post.persistence.PostJpaRepository;
import team7.inplace.post.persistence.PostReadRepository;
import team7.inplace.post.persistence.dto.CommentQueryResult.DetailedComment;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostReadRepository postReadRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final CommentReadRepository commentReadRepository;

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

    @Transactional
    public void updatePost(UpdatePost updateCommand, Long userId) {
        var post = postJpaRepository.findById(updateCommand.postId())
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));

        post.update(
            userId,
            updateCommand.title(),
            updateCommand.content(),
            updateCommand.imageUrls(),
            updateCommand.imgHashes()
        );
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
        post.deleteSoftly(userId);
    }

    @Transactional
    public void updateComment(UpdateComment updateCommand, Long userId) {
        if (!postJpaRepository.existsById(updateCommand.postId())) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }
        var comment = commentJpaRepository.findById(updateCommand.commentId())
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));

        comment.updateContent(userId, updateCommand.comment());
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        if (!postJpaRepository.existsById(postId)) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }
        var comment = commentJpaRepository.findById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));

        comment.deleteSoftly(userId);
    }

    @Transactional(readOnly = true)
    public CursorResult<DetailedPost> getPosts(
        Long userId, Long cursorId, int size, String orderBy
    ) {
        return postReadRepository.findPostsOrderBy(userId, cursorId, size, orderBy);
    }

    @Transactional(readOnly = true)
    public DetailedPost getPostById(Long postId, Long userId) {
        return postReadRepository.findPostById(postId, userId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<DetailedComment> getCommentsByPostId(Long postId, Long userId, Pageable pageable) {
        if (!postJpaRepository.existsById(postId)) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }
        return commentReadRepository.findCommentsByPostId(postId, userId, pageable);
    }
}
