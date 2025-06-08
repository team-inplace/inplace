package team7.inplace.post.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import team7.inplace.global.exception.InplaceException;
import team7.inplace.global.exception.code.PostErrorCode;

class PostTest {

    @Test
    @DisplayName("단일 사진 JsonNode 변환 테스트")
    void singlePhotoTransferTest() {
        //given
        final Long authorId = 1L;
        final String title = "test";
        final String content = "test content";
        final List<String> imageUrls = List.of("https://example.com/image.jpg");
        final List<String> imgHashes = List.of("hash123");

        final Integer expectedJsonNodeSize = 1;

        //when
        Post post = new Post(title, content, imageUrls, imgHashes, authorId);
        List<String> photoUrls = post.getImageUrls();
        List<String> photoHashes = post.getImgHashes();

        //then
        assertThat(photoUrls.size()).isEqualTo(expectedJsonNodeSize);
        assertThat(photoUrls).isEqualTo(imageUrls);
        assertThat(photoHashes).isEqualTo(imgHashes);

    }

    @Test
    @DisplayName("다중 사진 JsonNode 변환 테스트")
    void multiplePhotosTransferTest() {
        //given
        final Long authorId = 1L;
        final String title = "test";
        final String content = "test content";
        final List<String> imageUrls = List.of(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg"
        );
        final List<String> imgHashes = List.of("hash123", "hash456");
        final Integer expectedJsonNodeSize = 2;

        //when
        Post post = new Post(title, content, imageUrls, imgHashes, authorId);
        List<String> photoUrls = post.getImageUrls();
        List<String> photoHashes = post.getImgHashes();

        //then
        assertThat(photoUrls.size()).isEqualTo(expectedJsonNodeSize);
        assertThat(photoUrls).isEqualTo(imageUrls);
        assertThat(photoHashes).isEqualTo(imgHashes);
    }

    @Test
    @DisplayName("중복된 이미지 해쉬 발생 시 예외 발생 테스트")
    void duplicateImageHashTest() {
        //given
        final Long authorId = 1L;
        final String title = "test";
        final String content = "test content";
        final List<String> imageUrls = List.of(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg"
        );
        final List<String> imgHashes = List.of("hash123", "hash123");

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_PHOTOS_DUPLICATED.message());
    }

    @Test
    @DisplayName("사진 개수 초과 시 예외 발생 테스트")
    void photoSizeExceededTest() {
        //given
        final Long authorId = 1L;
        final String title = "test";
        final String content = "test content";
        final List<String> imageUrls = List.of(
            "https://example.com/image1.jpg",
            "https://example.com/image2.jpg",
            "https://example.com/image3.jpg",
            "https://example.com/image4.jpg",
            "https://example.com/image5.jpg",
            "https://example.com/image6.jpg",
            "https://example.com/image7.jpg",
            "https://example.com/image8.jpg",
            "https://example.com/image9.jpg",
            "https://example.com/image10.jpg",
            "https://example.com/image11.jpg"
        );
        final List<String> imgHashes = List.of(
            "hash1", "hash2", "hash3", "hash4", "hash5",
            "hash6", "hash7", "hash8", "hash9", "hash10",
            "hash11"
        );

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_PHOTOS_SIZE_EXCEEDED.message());
    }

    @Test
    @DisplayName("게시글 제목이 비어있을 때 예외 발생 테스트")
    void postTitleEmptyTest() {
        //given
        final Long authorId = 1L;
        final String title = "";
        final String content = "test content";
        final List<String> imageUrls = List.of("https://example.com/image.jpg");
        final List<String> imgHashes = List.of("hash123");

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_TITLE_EMPTY.message());
    }

    @Test
    @DisplayName("게시글 내용이 비어있을 때 예외 발생 테스트")
    void postContentEmptyTest() {
        //given
        final Long authorId = 1L;
        final String title = "test title";
        final String content = "";
        final List<String> imageUrls = List.of("https://example.com/image.jpg");
        final List<String> imgHashes = List.of("hash123");

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_CONTENT_EMPTY.message());
    }

    @Test
    @DisplayName("게시글 제목이 30자를 초과할 때 예외 발생 테스트")
    void postTitleLengthExceededTest() {
        //given
        final Long authorId = 1L;
        final String title = "a".repeat(31); // 31 characters
        final String content = "test content";
        final List<String> imageUrls = List.of();
        final List<String> imgHashes = List.of();

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_TITLE_LENGTH_EXCEEDED.message());
    }

    @Test
    @DisplayName("게시글 내용이 3000자를 초과할 때 예외 발생 테스트")
    void postContentLengthExceededTest() {
        //given
        final Long authorId = 1L;
        final String title = "test title";
        final String content = "a".repeat(3001);
        final List<String> imageUrls = List.of();
        final List<String> imgHashes = List.of();

        //when & then
        assertThatThrownBy(() -> new Post(title, content, imageUrls, imgHashes, authorId))
            .isInstanceOf(InplaceException.class)
            .hasMessage(PostErrorCode.POST_CONTENT_LENGTH_EXCEEDED.message());
    }

    @Test
    @DisplayName("이미지가 없는 게시글 생성 테스트")
    void postWithoutImagesTest() {
        //given
        final Long authorId = 1L;
        final String title = "test title";
        final String content = "test content";
        final List<String> imageUrls = List.of();
        final List<String> imgHashes = List.of();

        //when
        Post post = new Post(title, content, imageUrls, imgHashes, authorId);

        //then
        assertThat(post.getImageUrls().size()).isEqualTo(0);
        assertThat(post.getImgHashes().size()).isEqualTo(0);
    }
}