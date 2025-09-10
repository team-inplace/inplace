package my.inplace.application.alarm.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {
    public static Duration calculateSecond(LocalDateTime createdAt) {
        LocalDateTime nowTime = LocalDateTime.now();
        return Duration.between(createdAt, nowTime);
    }
    
    public static String betweenTime(LocalDateTime createdAt) {
        Duration duration = calculateSecond(createdAt);
        
        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (seconds < 60) {
            return seconds + "초 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (hours < 24) {
            return hours + "시간 전";
        } else {
            return days + "일 전";
        }
    }
}
