package my.inplace.domain.user;

import my.inplace.domain.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "tier_id")
    private Long tierId;

    @Column(name = "main_badge_id")
    private Long mainBadgeId;

    @Column(name = "post_count")
    private Integer postCount;

    @Column(name = "received_comment_count")
    private Long receivedCommentCount;

    @Column(name = "received_like_count")
    private Long receivedLikeCount;
    
    @Column(name = "report_push_consent")
    private Boolean reportPushConsent;
    
    @Column(name = "mention_push_consent")
    private Boolean mentionPushConsent;

    public User(
        String username, String password, String nickname, String profileImageUrl,
        UserType userType, Role role
    ) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.userType = userType;
        this.role = role;
        this.tierId = 1L;
        this.mainBadgeId = null;
        this.postCount = 0;
        this.receivedCommentCount = 0L;
        this.receivedLikeCount = 0L;
        this.reportPushConsent = false;
        this.mentionPushConsent = false;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
    
    public void deleteFcmToken() {
        this.fcmToken = null;
    }

    public void updateMainBadge(Long badgeId) {
        this.mainBadgeId = badgeId;
    }

    public void updateTier(Long tierId) {
        this.tierId = tierId;
    }

    public void updatePostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public void updateReceivedCommentCount(Long receivedCommentCount) {
        this.receivedCommentCount = receivedCommentCount;
    }

    public void updateReceivedLikeCount(Long receivedLikeCount) {
        this.receivedLikeCount = receivedLikeCount;
    }
    
    public void updateReportPushResent(Boolean tf) {
        this.reportPushConsent = tf;
    }
    
    public void updateMentionPushResent(Boolean tf) {
        this.mentionPushConsent = tf;
    }
}
