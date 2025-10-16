package my.inplace.application.post.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import my.inplace.application.post.command.dto.PostCommand;
import my.inplace.domain.post.LikedComment;
import my.inplace.domain.post.LikedPost;
import my.inplace.infra.post.jpa.CommentJpaRepository;
import my.inplace.infra.post.jpa.LikedCommentJpaRepository;
import my.inplace.infra.post.jpa.LikedPostJpaRepository;
import my.inplace.infra.post.jpa.PostJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

    @Mock
    PostJpaRepository postJpaRepository;

    @Mock
    LikedPostJpaRepository likedPostJpaRepository;

    @Mock
    CommentJpaRepository commentJpaRepository;

    @Mock
    LikedCommentJpaRepository likedCommentJpaRepository;

    @InjectMocks
    PostCommandService postCommandService;

    @Test
    @DisplayName("게시글 첫번째 좋아요")
    void likePost_newlike() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        PostCommand.PostLike command = new PostCommand.PostLike(postId, true);

        given(postJpaRepository.existsById(postId)).willReturn(true);
        given(likedPostJpaRepository.findByUserIdAndPostId(userId, postId))
            .willReturn(Optional.empty());

        ArgumentCaptor<LikedPost> captor = ArgumentCaptor.forClass(LikedPost.class);
        given(likedPostJpaRepository.save(any(LikedPost.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        postCommandService.likePost(command, userId);

        // then
        verify(postJpaRepository).increaseLikeCount(postId);
        verify(postJpaRepository, never()).decreaseLikeCount(anyLong());
        
        verify(likedPostJpaRepository).save(captor.capture());
        LikedPost savedLikedPost = captor.getValue();
        assertThat(savedLikedPost.getPostId()).isEqualTo(postId);
        assertThat(savedLikedPost.getUserId()).isEqualTo(userId);
        assertThat(savedLikedPost.getIsLiked()).isTrue();
    }

    @Test
    @DisplayName("기존에 좋아요 상태의 게시글 좋아요 취소")
    void likePost_unlike() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        PostCommand.PostLike command = new PostCommand.PostLike(postId, false);

        given(postJpaRepository.existsById(postId)).willReturn(true);
        LikedPost likedPost = LikedPost.from(userId, postId);
        likedPost.updateLike(true); // 기존에 좋아요 상태로 가정
        given(likedPostJpaRepository.findByUserIdAndPostId(userId, postId))
            .willReturn(Optional.of(likedPost));

        // when
        postCommandService.likePost(command, userId);

        // then
        verify(postJpaRepository, never()).increaseLikeCount(anyLong());
        verify(postJpaRepository).decreaseLikeCount(postId);

        verify(likedPostJpaRepository, never()).save(any());
        assertThat(likedPost.getIsLiked()).isFalse();
    }

    @Test
    @DisplayName("댓글 첫번째 좋아요")
    void likeComment_newlike() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        PostCommand.CommentLike command = new PostCommand.CommentLike(postId, commentId, true);

        given(commentJpaRepository.existsCommentByPostIdAndId(postId, commentId)).willReturn(true);
        given(likedCommentJpaRepository.findByUserIdAndCommentId(userId, commentId))
            .willReturn(Optional.empty());

        ArgumentCaptor<LikedComment> captor = ArgumentCaptor.forClass(LikedComment.class);
        given(likedCommentJpaRepository.save(any(LikedComment.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        postCommandService.likeComment(command, userId);

        // then
        verify(commentJpaRepository).increaseLikeCount(commentId);
        verify(commentJpaRepository, never()).decreaseLikeCount(anyLong());

        verify(likedCommentJpaRepository).save(captor.capture());
        LikedComment savedLikedComment = captor.getValue();
        assertThat(savedLikedComment.getCommentId()).isEqualTo(commentId);
        assertThat(savedLikedComment.getUserId()).isEqualTo(userId);
        assertThat(savedLikedComment.getIsLiked()).isTrue();
    }

    @Test
    @DisplayName("기존에 좋아요 상태의 댓글 좋아요 취소")
    void likeComment_unlike() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 1L;
        PostCommand.CommentLike command = new PostCommand.CommentLike(postId, commentId, false);

        given(commentJpaRepository.existsCommentByPostIdAndId(postId, commentId)).willReturn(true);
        LikedComment likedComment = LikedComment.from(userId, commentId);
        likedComment.updateLike(true); // 기존에 좋아요 상태로 가정
        given(likedCommentJpaRepository.findByUserIdAndCommentId(userId, commentId))
            .willReturn(Optional.of(likedComment));

        // when
        postCommandService.likeComment(command, userId);

        // then
        verify(commentJpaRepository, never()).increaseLikeCount(anyLong());
        verify(commentJpaRepository).decreaseLikeCount(commentId);

        verify(likedCommentJpaRepository, never()).save(any());
        assertThat(likedComment.getIsLiked()).isFalse();
    }
}