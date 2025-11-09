package my.inplace.application.post.query;

import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.inplace.application.post.query.dto.CommentResult;
import my.inplace.application.post.query.dto.PostResult;
import my.inplace.common.cursor.CursorResult;
import my.inplace.common.exception.InplaceException;
import my.inplace.common.exception.code.PostErrorCode;
import my.inplace.domain.post.Post;
import my.inplace.domain.post.PostTitle;
import my.inplace.domain.post.query.CommentQueryResult.DetailedComment;
import my.inplace.domain.post.query.PostQueryResult.DetailedPost;
import my.inplace.domain.post.query.PostQueryResult.UserSuggestion;
import my.inplace.infra.post.CommentReadQueryDslRepository;
import my.inplace.infra.post.PostReadQueryDslRepository;
import my.inplace.infra.post.jpa.CommentJpaRepository;
import my.inplace.infra.post.jpa.LikedPostJpaRepository;
import my.inplace.infra.post.jpa.PostJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostQueryService {

    private final PostJpaRepository postJpaRepository;
    private final LikedPostJpaRepository likedPostJpaRepository;
    private final PostReadQueryDslRepository postReadRepository;

    private final CommentJpaRepository commentJpaRepository;
    private final CommentReadQueryDslRepository commentReadRepository;

    @Transactional(readOnly = true)
    public CursorResult<DetailedPost> getPosts(
        Long userId, Long cursorValue, Long cursorId, int size, String orderBy
    ) {
        return postReadRepository.findPostsOrderBy(userId, cursorValue, cursorId, size, orderBy);
    }
    
    @Transactional(readOnly = true)
    public Page<PostResult.SimplePost> getMyPosts(Long userId, Pageable pageable) {
        var posts = postJpaRepository.findPostsByAuthorId(userId, pageable);
        
        var postIds = posts.getContent().stream()
             .map(Post::getId)
             .toList();
        
        var likedPostIds = likedPostJpaRepository.findLikedPostIdsByUserIdAndPostIds(userId, postIds);
        
        return posts.map(post -> PostResult.SimplePost.of(post, likedPostIds.contains(post.getId())));
    }
    
    @Transactional(readOnly = true)
    public Page<PostResult.SimplePost> getLikedPosts(Long userId, Pageable pageable) {
        var postIds = likedPostJpaRepository.findLikedPostIdsByUserId(userId);
        
        return postJpaRepository.findAllByIdIn(postIds, pageable)
            .map(post -> PostResult.SimplePost.of(post, true));
    }
    
    @Transactional(readOnly = true)
    public Page<PostResult.SimplePost> getCommentedPosts(Long userId, Pageable pageable) {
        var commentIds = commentJpaRepository.findCommentIdsByUserId(userId);
        var postIds = postJpaRepository.findPostsByCommentIds(commentIds);
        
        var likedPostIds = likedPostJpaRepository.findLikedPostIdsByUserIdAndPostIds(userId, postIds);
        
        var posts = postJpaRepository.findAllByIdIn(postIds, pageable);
        
        return posts.map(post -> PostResult.SimplePost.of(post, likedPostIds.contains(post.getId())));
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
    
    public Long getCommentIndexByPostIdAndCommentId(Long postId, Long commentId) {
        return postJpaRepository.findIndexOfComment(postId, commentId);
    }
}
