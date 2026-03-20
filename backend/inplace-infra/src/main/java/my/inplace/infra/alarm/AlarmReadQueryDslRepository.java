package my.inplace.infra.alarm;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.inplace.domain.alarm.QAlarm;
import my.inplace.domain.alarm.query.AlarmQueryResult.Detail;
import my.inplace.domain.alarm.query.AlarmReadRepository;
import my.inplace.domain.post.QComment;
import my.inplace.domain.post.QPost;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmReadQueryDslRepository implements AlarmReadRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Detail> findAlarms(Long userId) {
        QComment prevComment = new QComment("prevComment");

        return queryFactory
            .select(Projections.constructor(
                Detail.class,
                QAlarm.alarm.id,
                QAlarm.alarm.postId,
                QAlarm.alarm.commentId,
                JPAExpressions
                    .select(prevComment.count())
                    .from(prevComment)
                    .where(
                        prevComment.postId.eq(QAlarm.alarm.postId),
                        prevComment.deleteAt.isNull(),
                        prevComment.id.lt(QAlarm.alarm.commentId)
                    ),
                QAlarm.alarm.content,
                QAlarm.alarm.checked,
                QAlarm.alarm.alarmType,
                QAlarm.alarm.createdAt,
                QPost.post.id.isNull().or(QPost.post.deleteAt.isNotNull()),
                QComment.comment.id.isNull().or(QComment.comment.deleteAt.isNotNull())
            ))
            .from(QAlarm.alarm)
            .leftJoin(QPost.post).on(QPost.post.id.eq(QAlarm.alarm.postId))
            .leftJoin(QComment.comment).on(
                QComment.comment.id.eq(QAlarm.alarm.commentId),
                QComment.comment.postId.eq(QAlarm.alarm.postId)
            )
            .where(QAlarm.alarm.userId.eq(userId))
            .orderBy(QAlarm.alarm.id.desc()) // 최신순
            .fetch();
    }
}
