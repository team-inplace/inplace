package my.inplace.domain.alarm.query;

import java.util.List;
import my.inplace.domain.alarm.query.AlarmQueryResult.Detail;

public interface AlarmReadRepository {

    List<Detail> findAlarms(Long userId);
}