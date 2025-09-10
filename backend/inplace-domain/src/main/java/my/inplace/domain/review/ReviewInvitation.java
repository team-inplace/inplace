package my.inplace.domain.review;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ReviewInvitation {

    private String invitationId;
    private Long userId;
    private Long placeId;
}