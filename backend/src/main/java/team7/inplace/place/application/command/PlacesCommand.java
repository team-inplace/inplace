package team7.inplace.place.application.command;

import com.fasterxml.jackson.databind.JsonNode;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Pageable;
import team7.inplace.place.domain.Place;
import team7.inplace.video.presentation.dto.VideoSearchParams;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlacesCommand {

    public record PlacesCoordinateCommand(
            String topLeftLongitude,
            String topLeftLatitude,
            String bottomRightLongitude,
            String bottomRightLatitude,
            String longitude,
            String latitude,
            Pageable pageable
    ) {

        public static PlacesCoordinateCommand from(VideoSearchParams videoSearchParams,
                                                   Pageable pageable) {
            return new PlacesCoordinateCommand(
                    videoSearchParams.topLeftLongitude(),
                    videoSearchParams.topLeftLatitude(),
                    videoSearchParams.bottomRightLatitude(),
                    videoSearchParams.bottomRightLatitude(),
                    videoSearchParams.longitude(),
                    videoSearchParams.latitude(),
                    pageable
            );
        }
    }

    public record PlacesFilterParamsCommand(
            String categories,
            String influencers
    ) {

        public boolean isCategoryFilterExists() {
            return StringUtils.isNotEmpty(categories);
        }

        public boolean isInfluencerFilterExists() {
            return StringUtils.isNotEmpty(influencers);
        }
    }

    public record Create(
            String placeName,
            String facility,
            String menuImgUrl,
            String category,
            String address,
            String x,
            String y,
            List<OffDay> offDays,
            List<OpenTime> openPeriods,
            List<Menu> menus,
            LocalDateTime menuUpdatedAt,
            List<String> menuboardphotourlList
    ) {

        public Place toEntity() {
            var offDaysParam = offDays.stream()
                    .map(OffDay::toEntityParams)
                    .toList();
            var openPeriodsParam = openPeriods.stream()
                    .map(OpenTime::toEntityParams)
                    .toList();
            var menusParam = menus.stream()
                    .map(Menu::toEntityParams)
                    .toList();

            return new Place(placeName, facility, menuImgUrl, category, address, x, y, offDaysParam,
                    openPeriodsParam,
                    menusParam, menuUpdatedAt, menuboardphotourlList);
        }

        public static Create from(JsonNode locationNode, JsonNode placeNode) {
            if (Objects.isNull(locationNode) || Objects.isNull(placeNode)) {
                return null;
            }

            var basicInfo = placeNode.get("basicInfo");

            String placeName =
                    basicInfo.has("placenamefull") ? basicInfo.get("placenamefull").asText() : " ";
            String facility = basicInfo.has("facilityInfo")
                    ? basicInfo.get("facilityInfo").toString() : " ";

            String menuImgUrl =
                    basicInfo.has("mainphotourl") ? basicInfo.get("mainphotourl").asText() : " ";
            String category =
                    basicInfo.has("category") && basicInfo.get("category").has("cate1name")
                            ? basicInfo.get("category").get("cate1name").asText() : " ";
            String address =
                    basicInfo.has("address") && basicInfo.get("address").has("region") && basicInfo.get(
                                    "address")
                            .get("region").has("newaddrfullname")
                            ? basicInfo.get("address").get("region").get("newaddrfullname").asText()
                            : " ";
            String addressDetail =
                    basicInfo.has("address") && basicInfo.get("address").has("newaddr")
                            && basicInfo.get("address")
                            .get("newaddr").has("newaddrfull")
                            ? basicInfo.get("address").get("newaddr").get("newaddrfull").asText() : " ";

            String x =
                    locationNode.has("documents") && locationNode.get("documents").get(0).has("x")
                            ? locationNode.get("documents").get(0).get("x").asText() : "0.0";
            String y =
                    locationNode.has("documents") && locationNode.get("documents").get(0).has("y")
                            ? locationNode.get("documents").get(0).get("y").asText() : "0.0";

            var timeList = basicInfo.has("openHour") ? basicInfo.get("openHour") : null;
            List<OpenTime> openPeriods = Objects.nonNull(timeList) ?
                    extractOpenPeriods(timeList.has("periodList") ? timeList.get("periodList") : null)
                    : new ArrayList<>();
            List<OffDay> offDays = Objects.nonNull(timeList) ?
                    extractOffDays(timeList.has("offdayList") ? timeList.get("offdayList") : null)
                    : new ArrayList<>();

            var menuNodes = placeNode.has("menuInfo") ? placeNode.get("menuInfo") : null;
            var menus = extractMenus(menuNodes);
            var menuUpdatedAt =
                    menuNodes != null && menuNodes.has("menuUpdatedAt") ? LocalDateTime.parse(
                            placeNode.get("menuInfo").get("timeexp").asText()) : LocalDateTime.now();
            var menuBoardPhotoUrlList =
                    menuNodes != null && menuNodes.has("menuboardphotourlList") ?
                            menuNodes.findValuesAsText("menuboardphotourlList") : new ArrayList<String>();

            return new Create(placeName, facility, menuImgUrl, category,
                    address + " " + addressDetail, x, y, offDays,
                    openPeriods, menus, menuUpdatedAt, menuBoardPhotoUrlList);
        }

        private static List<OpenTime> extractOpenPeriods(JsonNode openTimeList) {
            if (openTimeList == null) {
                return new ArrayList<>();
            }
            List<OpenTime> openTimes = new ArrayList<>();
            for (JsonNode openTimeNode : openTimeList) {
                for (JsonNode timeNode : openTimeNode.get("timeList")) {
                    openTimes.add(OpenTime.from(timeNode));
                }
            }
            return openTimes;
        }

        private static List<OffDay> extractOffDays(JsonNode offdayList) {
            if (offdayList == null) {
                return new ArrayList<>(); // 빈 리스트를 반환하여 null 회피
            }
            List<OffDay> offDays = new ArrayList<>();
            for (JsonNode offDayNode : offdayList) {
                offDays.add(OffDay.from(offDayNode));
            }
            return offDays;
        }

        private static List<Menu> extractMenus(JsonNode menuList) {
            if (menuList == null) {
                return new ArrayList<>();
            }
            List<Menu> menus = new ArrayList<>();
            for (JsonNode menuNode : menuList.get("menuList")) {
                menus.add(Menu.from(menuNode));
            }
            return menus;
        }
    }

    public record OffDay(
            String holidayName,
            String weekAndDay,
            String temporaryHolidays
    ) {

        public static OffDay from(JsonNode offDayNode) {
            String holidayName = offDayNode != null && offDayNode.has("holidayName")
                    ? offDayNode.get("holidayName").asText() : " ";
            String weekAndDay = offDayNode != null && offDayNode.has("weekAndDay")
                    ? offDayNode.get("weekAndDay").asText() : " ";
            String temporaryHolidays = offDayNode != null && offDayNode.has("temporaryHolidays")
                    ? offDayNode.get("temporaryHolidays").asText() : " ";
            return new OffDay(holidayName, weekAndDay, temporaryHolidays);
        }

        public String toEntityParams() {
            return holidayName + "|" + weekAndDay + "|" + temporaryHolidays;
        }
    }

    public record OpenTime(
            String timeName,
            String timeSE,
            String dayOfWeek
    ) {

        public static OpenTime from(JsonNode openTimeNode) {
            String timeName = openTimeNode != null && openTimeNode.has("timeName")
                    ? openTimeNode.get("timeName").asText() : " ";
            String timeSE = openTimeNode != null && openTimeNode.has("timeSE")
                    ? openTimeNode.get("timeSE").asText() : " ";
            String dayOfWeek = openTimeNode != null && openTimeNode.has("dayOfWeek")
                    ? openTimeNode.get("dayOfWeek").asText() : " ";
            return new OpenTime(timeName, timeSE, dayOfWeek);
        }

        public String toEntityParams() {
            return timeName + "|" + timeSE + "|" + dayOfWeek;
        }
    }

    public record Menu(
            String menuName,
            String menuPrice,
            boolean recommend,
            String menuImgUrl,
            String description
    ) {

        public static Menu from(JsonNode menuNode) {
            if (Objects.isNull(menuNode)) {
                return new Menu(" ", "0", false, " ", " ");
            }
            String menuName = menuNode.has("menu") ? menuNode.get("menu").asText() : " ";
            String menuPrice = menuNode.has("price") ? menuNode.get("price").asText() : " ";
            boolean recommend = menuNode.has("recommend") && menuNode.get("recommend").asBoolean();
            String menuImgUrl = menuNode.has("img") ? menuNode.get("img").asText() : " ";
            String description = menuNode.has("desc") ? menuNode.get("desc").asText() : " ";

            return new Menu(menuName, menuPrice, recommend, menuImgUrl, description);
        }

        public String toEntityParams() {
            return menuName + "|" + menuPrice + "|" + recommend + "|" + menuImgUrl + "|"
                    + description;
        }
    }
}
