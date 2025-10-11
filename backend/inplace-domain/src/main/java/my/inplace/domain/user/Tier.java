package my.inplace.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "user_tiers")
@RequiredArgsConstructor
public class Tier {
//    BRONZE("브론즈", "bronze", 0L, 0L, 0L, "bronzeImg"),
//    SILVER("실버", "silver", 5L, 10L, 10L, "silverImg"),
//    GOLD("골드", "gold", 15L, 30L, 50L, "goldImg"),;

//    PLATINUM("플래티넘", "platinum", 30L, 80L, 120L),
//    DIAMOND("다이아몬드", "diamond", 50L, 150L, 250L),
//    RUBY("루비", "ruby", 80L, 250L, 500L),
//    MASTER("마스터", "master", 120L, 400L, 1000L),
//    GRANDMASTER("그랜드마스터", "grandmaster", 180L, 600L, 3000L),
//    LEGEND("레전드", "legend", 250L, 1000L, 10000L);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "eng_name")
    private String engName;

    @Column(name = "required_posts")
    private Integer requiredPosts;

    @Column(name = "required_comments")
    private Long requiredComments;

    @Column(name = "required_likes")
    private Long requiredLikes;

    @Column(name = "img_url")
    private String imgUrl;

}
