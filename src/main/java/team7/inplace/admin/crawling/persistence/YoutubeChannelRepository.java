package team7.inplace.admin.crawling.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import team7.inplace.admin.crawling.domain.YoutubeChannel;

public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {
    Optional<YoutubeChannel> findYoutubeChannelByPlayListUUID(String playListUUID);
}
