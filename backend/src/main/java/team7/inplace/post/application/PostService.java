package team7.inplace.post.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team7.inplace.global.cursor.CursorResult;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;
import team7.inplace.liked.likedComment.domain.LikedComment;
import team7.inplace.liked.likedComment.persistence.LikedCommentRepository;
import team7.inplace.liked.likedPost.domain.LikedPost;
import team7.inplace.liked.likedPost.persistence.LikedPostRepository;
import team7.inplace.post.application.dto.PostCommand;
import team7.inplace.post.application.dto.PostCommand.CreateComment;
import team7.inplace.post.application.dto.PostCommand.CreatePost;
import team7.inplace.post.application.dto.PostCommand.UpdateComment;
import team7.inplace.post.application.dto.PostCommand.UpdatePost;
import team7.inplace.post.domain.Post;
import team7.inplace.post.application.dto.PostInfo;
import team7.inplace.post.application.dto.PostInfo.PostImages;
import team7.inplace.post.persistence.CommentJpaRepository;
import team7.inplace.post.persistence.CommentReadRepository;
import team7.inplace.post.persistence.PostJpaRepository;
import team7.inplace.post.persistence.PostReadRepository;
import team7.inplace.post.persistence.dto.CommentQueryResult.DetailedComment;
import team7.inplace.post.persistence.dto.PostQueryResult.DetailedPost;
import team7.inplace.post.persistence.dto.PostQueryResult.UserSuggestion;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostJpaRepository postJpaRepository;
    private final PostReadRepository postReadRepository;
    private final LikedPostRepository likedPostRepository;

    private final CommentJpaRepository commentJpaRepository;
    private final CommentReadRepository commentReadRepository;
    private final LikedCommentRepository likedCommentRepository;


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

    @Transactional
    public void createPost(CreatePost command, Long userId) {
        var post = command.toPostEntity(userId);
        postJpaRepository.save(post);
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

    // TODO : likeCount 업데이트 후 배치 업데이트 하는 과정 필요
    @Transactional
    public void likePost(PostCommand.PostLike command, Long userId) {
        var postId = command.postId();
        if (!postJpaRepository.existsById(postId)) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }

        var likedPost = likedPostRepository.findByUserIdAndPostId(userId, postId)
            .orElseGet(() -> {
                var newLikedPost = LikedPost.from(userId, postId);
                return likedPostRepository.save(newLikedPost);
            });

        var isLiked = likedPost.updateLike(command.likes());
        if (isLiked) {
            postJpaRepository.increaseLikeCount(postId);
            return;
        }
        postJpaRepository.decreaseLikeCount(postId);
    }

    @Transactional(readOnly = true)
    public Page<DetailedComment> getCommentsByPostId(Long postId, Long userId, Pageable pageable) {
        if (!postJpaRepository.existsById(postId)) {
            throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
        }
        return commentReadRepository.findCommentsByPostId(postId, userId, pageable);
    }

    @Transactional
    public Long createComment(CreateComment command, Long userId) {
        if (postJpaRepository.existsById(command.postId())) {
            var comment = command.toEntity(userId);
            commentJpaRepository.save(comment);
            postJpaRepository.increaseCommentCount(command.postId());
            return comment.getId();
        }
        throw InplaceException.of(PostErrorCode.POST_NOT_FOUND);
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
        postJpaRepository.decreaseCommentCount(postId);
    }

    @Transactional
    public void likeComment(PostCommand.CommentLike command, Long userId) {
        var commentId = command.commentId();
        var postId = command.postId();
        if (!commentJpaRepository.existsCommentByPostIdAndId(postId, commentId)) {
            throw InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND);
        }

        var likedComment = likedCommentRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseGet(() -> {
                var newLikedComment = LikedComment.from(userId, commentId);
                return likedCommentRepository.save(newLikedComment);
            });

        var isLiked = likedComment.updateLike(command.likes());
        if (isLiked) {
            commentJpaRepository.increaseLikeCount(commentId);
            return;
        }
        commentJpaRepository.decreaseLikeCount(commentId);
    }

    @Transactional(readOnly = true)
    public List<UserSuggestion> getUserSuggestions(
        Long userId, Long postId, String keyword
    ) {
        var suggestions = postReadRepository.findCommentUserSuggestions(postId, keyword);

        return suggestions.stream()
            .distinct()
            .filter(suggestion -> !suggestion.userId().equals(userId))
            .toList();

    }

    @Transactional(readOnly = true)
    public String getPostContentById(Long postId) {
        return postJpaRepository.findContentById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
    }
    
    @Transactional(readOnly = true)
    public Long getPostAuthorIdById(Long postId) {
        return postJpaRepository.findUserIdById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
    }
    
    @Transactional(readOnly = true)
    public Long getCommentAuthorIdById(Long commentId) {
        return commentJpaRepository.findAuthorIdById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
    }
    
    @Transactional(readOnly = true)
    public String getPostTitleById(Long postId) {
        return postJpaRepository.findTitleById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));
    }
    
    @Transactional(readOnly = true)
    public Long getPostIdById(Long commentId) {
        return commentJpaRepository.findPostIdById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
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

    @Transactional(readOnly = true)
    public Long getAuthorIdByPostId(Long postId) {
        return postJpaRepository.findAuthorIdByPostId(postId);
    }
    
    public PostInfo.PostImages getPostImageDetails(Long postId, Long userId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));

        post.checkAuthor(userId);

        return PostImages.from(post);
    }
}
