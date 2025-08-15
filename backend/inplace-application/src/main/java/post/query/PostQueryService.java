package post.query;

import base.CursorResult;
import exception.InplaceException;
import exception.code.PostErrorCode;
import java.util.List;

import exception.code.PostErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import post.query.dto.PostResult;
import post.jpa.CommentJpaRepository;
import post.jpa.PostJpaRepository;
import post.query.CommentQueryResult.DetailedComment;
import post.query.PostQueryResult.DetailedPost;
import post.query.PostQueryResult.UserSuggestion;
import review.CommentReadQueryDslRepository;
import review.PostReadQueryDslRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostQueryService {

    private final PostJpaRepository postJpaRepository;
    private final PostReadQueryDslRepository postReadRepository;

    private final CommentJpaRepository commentJpaRepository;
    private final CommentReadQueryDslRepository commentReadRepository;

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

    @Transactional(readOnly = true)
    public String getCommentContentById(Long commentId) {
        return commentJpaRepository.findContentById(commentId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Long getAuthorIdByPostId(Long postId) {
        return postJpaRepository.findAuthorIdByPostId(postId);
    }

    public PostResult.PostImages getPostImageDetails(Long postId, Long userId) {
        var post = postJpaRepository.findById(postId)
            .orElseThrow(() -> InplaceException.of(PostErrorCode.POST_NOT_FOUND));

        post.checkAuthor(userId);

        return PostResult.PostImages.from(post);
    }
}
