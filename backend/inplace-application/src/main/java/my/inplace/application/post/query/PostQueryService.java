package my.inplace.application.post.query;

import my.inplace.common.cursor.CursorResult;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.domain.post.Post;
import my.inplace.domain.post.PostTitle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import my.inplace.infra.post.CommentReadQueryDslRepository;
import my.inplace.infra.post.PostReadQueryDslRepository;
import my.inplace.infra.post.jpa.CommentJpaRepository;
import my.inplace.infra.post.jpa.PostJpaRepository;
import my.inplace.domain.post.query.CommentQueryResult.DetailedComment;
import my.inplace.domain.post.query.PostQueryResult.DetailedPost;
import my.inplace.domain.post.query.PostQueryResult.UserSuggestion;
import my.inplace.application.post.query.dto.PostResult;

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
    public PostTitle getPostTitleById(Long postId) {
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

    public List<PostResult.ReportedPost> findReportedPost() {
        return postJpaRepository.findAllByIsReportedTrueAndDeleteAtIsNull()
            .stream()
            .map(PostResult.ReportedPost::from)
            .toList();
    }

    public List<PostResult.ReportedComment> findReportedComment() {
        return commentJpaRepository.findAllByIsReportedTrueAndDeleteAtIsNull()
            .stream()
            .map(PostResult.ReportedComment::from)
            .toList();
    }
    
    public Pair<Integer, Integer> getCommentIndexByPostIdAndCommentId(Long postId, Long commentId) {
        long indexOfComment = postJpaRepository.findIndexOfComment(postId, commentId);
        int pageNumber = (int) indexOfComment / 10;
        int offset = (int) indexOfComment % 10;
        
        return Pair.of(pageNumber, offset);
    }
}
