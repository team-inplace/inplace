package my.inplace.application.post.command;

import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.domain.post.LikedComment;
import my.inplace.domain.post.LikedPost;
import my.inplace.application.post.command.dto.PostCommand;
import my.inplace.infra.post.jpa.CommentJpaRepository;
import my.inplace.infra.post.jpa.LikedCommentJpaRepository;
import my.inplace.infra.post.jpa.LikedPostJpaRepository;
import my.inplace.infra.post.jpa.PostJpaRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostCommandService {

    private final PostJpaRepository postJpaRepository;
    private final LikedPostJpaRepository likedPostJpaRepository;

    private final CommentJpaRepository commentJpaRepository;
    private final LikedCommentJpaRepository likedCommentJpaRepository;

    @Transactional
    public void createPost(PostCommand.CreatePost command, Long userId) {
        var post = command.toPostEntity(userId);
        postJpaRepository.save(post);
    }

    @Transactional
    public void updatePost(PostCommand.UpdatePost updateCommand, Long userId) {
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

    // TODO : likeCount 업데이트 후 배치 업데이트 하는 과정 필요
    @Transactional
    public void likePost(PostCommand.PostLike command, Long userId) {
        var postId = command.postId();
        if (!postJpaRepository.existsById(postId)) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }

        var likedPost = likedPostJpaRepository.findByUserIdAndPostId(userId, postId)
            .orElseGet(() -> {
                var newLikedPost = LikedPost.from(userId, postId);
                return likedPostJpaRepository.save(newLikedPost);
            });

        var isLiked = likedPost.updateLike(command.likes());
        if (isLiked) {
            postJpaRepository.increaseLikeCount(postId);
            return;
        }
        postJpaRepository.decreaseLikeCount(postId);
    }

    @Transactional
    public Long createComment(PostCommand.CreateComment command, Long userId) {
        if (postJpaRepository.existsById(command.postId())) {
            var comment = command.toEntity(userId);
            commentJpaRepository.save(comment);
            postJpaRepository.increaseCommentCount(command.postId());
            return comment.getId();
        }
        throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
    }

    @Transactional
    public void updateComment(PostCommand.UpdateComment updateCommand, Long userId) {
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
        postJpaRepository.decreaseCommentCount(postId);
    }

    @Transactional
    public void likeComment(PostCommand.CommentLike command, Long userId) {
        var commentId = command.commentId();
        var postId = command.postId();
        if (!commentJpaRepository.existsCommentByPostIdAndId(postId, commentId)) {
            throw InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND);
        }

        var likedComment = likedCommentJpaRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseGet(() -> {
                var newLikedComment = LikedComment.from(userId, commentId);
                return likedCommentJpaRepository.save(newLikedComment);
            });

        var isLiked = likedComment.updateLike(command.likes());
        if (isLiked) {
            commentJpaRepository.increaseLikeCount(commentId);
            return;
        }
        commentJpaRepository.decreaseLikeCount(commentId);
    }

    @Transactional
    public void deletePostSoftly(Long postId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
        post.deleteSoftly();
    }

    @Transactional
    public void reportPost(Long postId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
        post.report();
    }

    @Transactional
    public void unreportPost(Long postId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
        post.unreported();
    }

    @Transactional(readOnly = true)
    public String getCommentContentById(Long commentId) {
        return commentJpaRepository.findContentById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional
    public void deleteCommentSoftly(Long commentId) {
        var comment = commentJpaRepository.findById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
        if (!postJpaRepository.existsById(comment.getPostId())) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }
        comment.deleteSoftly();
    }

    @Transactional
    public void reportComment(Long commentId) {
        var comment = commentJpaRepository.findById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
        comment.report();
    }

    @Transactional
    public void unreportComment(Long commentId) {
        var comment = commentJpaRepository.findById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
        comment.unreport();
    }
}
