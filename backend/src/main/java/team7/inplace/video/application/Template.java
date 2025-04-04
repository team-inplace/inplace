package team7.inplace.video.application;

import lombok.Getter;

@Getter
public enum Template {
    // 카페 관련 템플릿
    CAFE(new String[]{
        "방문한 카페에서 따뜻한 커피를 즐겨보세요!",
        "방문했던 카페에서 여유로운 시간을 보내보세요!",
        "추천하는 사진 명소 예쁜 카페에 방문해보세요!"
    }),

    // 양식 관련 템플릿
    WESTERN(new String[]{
        "추천! 맛있는 서양 음식을 즐길 수 있는 곳!",
        "추천하는 고급스러운 서양 요리를 맛볼 수 있습니다.",
        "극찬! 서양식 디저트를 꼭 한 번 시도해보세요!"
    }),

    // 일식 관련 템플릿
    JAPANESE(new String[]{
        "말하길, 현지와 다름없는 정통 일본 요리 레스토랑.",
        "추천하는 맛있는 일본 초밥과 라멘을 즐겨보세요.",
        "극찬! 일본 요리의 정수를 느껴보세요."
    }),

    // 한식 관련 템플릿
    KOREAN(new String[]{
        "전통 한식을 맛볼 수 있는 한식당입니다.",
        "추천하는 가게에서 정성스럽게 준비된 한식으로 든든한 한 끼를!",
        "극찬! 한식의 깊은 맛을 느껴보세요."
    }),

    // 기본 템플릿
    RESTAURANT(new String[]{
        "추천하는 맛집! 다양한 메뉴를 즐길 수 있습니다.",
        "방문한 식당에서 맛있는 음식을 즐겨보세요!",
        "추천하는 맛집에서 특별한 시간을 보내보세요!"
    }),

    NONE(new String[]{
        "방문한 곳에서 특별한 추억을 만들어보세요!",
        "추천하는 장소에서 특별한 경험을 해보세요!",
        "방문한 곳에서 특별한 시간을 보내보세요!"
    });

    // 템플릿 배열을 반환하는 메서드
    // 필드 선언
    private final String[] templates;

    // 생성자
    Template(String[] templates) {
        this.templates = templates;
    }

    // 특정 템플릿을 랜덤으로 반환하는 메서드
    public String getRandomTemplate() {
        int randomIndex = (int) (Math.random() * templates.length);
        return templates[randomIndex];
    }
}
