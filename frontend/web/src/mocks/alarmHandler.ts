import { rest } from 'msw';
import { getConfig } from '@inplace-frontend-monorepo/shared/api/config';
import { getAlarmsPath } from '@/api/hooks/useGetAlarms';
import { postAlarmPermissionPath } from '@/api/hooks/usePostAlarmPermission';

const config = getConfig();
const BASE_URL = config.baseURL;
const dummyAlarm = [
  {
    alarmId: 1,
    postId: 1,
    commentId: 12,
    content: '"블루베리 스무디" 게시글에서 정민 님이 언급했습니다.',
    checked: false,
    type: 'MENTION',
    createdAt: '59초 전',
    commentPage: 1,
  },
  {
    alarmId: 2,
    postId: 102,
    commentId: 202,
    content: '"농구하러 왔다가 갔어요" 게시글이 신고로 인하여 삭제되었습니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '2분 전',
  },
  {
    alarmId: 3,
    postId: 103,
    commentId: null,
    content: '"나는할말이엄청많은데말이..." 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다.',
    checked: false,
    type: 'REPORT',
    createdAt: '39분 전',
  },
  {
    alarmId: 4,
    postId: 104,
    commentId: 204,
    content: '"아이스티 아메리카노말고"님이 작성하신 샷추가에 댓글을 남겼습니다.',
    checked: true,
    type: 'MENTION',
    createdAt: '2시간 전',
  },
  {
    alarmId: 5,
    postId: 105,
    commentId: null,
    content: '"춘식이" 게시물에서 작성하신 샷추가에 댓글을 남겼습니다.',
    checked: true,
    type: 'MENTION',
    createdAt: '2일 전',
  },
  {
    alarmId: 6,
    postId: 1,
    commentId: 6,
    content: '"안녕" 게시글에서 나는 테스트용 알림입니다.',
    checked: false,
    type: 'MENTION',
    createdAt: '10일 전',
    commentPage: 0,
  },
  {
    alarmId: 7,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 8,
    postId: 107,
    commentId: null,
    content: '"나는할말이엄청많은데말이..." 게시글에 작성한 댓글이 신고로 인하여 삭제되었습니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 9,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 10,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 11,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 12,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
  {
    alarmId: 13,
    postId: 107,
    commentId: null,
    content: '"하이루" 게시물에서 나는 김밥천국 알림입니다.',
    checked: true,
    type: 'REPORT',
    createdAt: '1년 전',
  },
];

export const alarmHandlers = [
  rest.get(`${BASE_URL}${getAlarmsPath()}`, (_, res, ctx) => {
    return res(ctx.status(200), ctx.json(dummyAlarm));
  }),
  rest.post(`${BASE_URL}/alarms/:alarmId`, (req, res, ctx) => {
    const { alarmId } = req.params;
    return res(
      ctx.status(200),
      ctx.json({
        alarmId,
      }),
    );
  }),
  rest.post(`${BASE_URL}${postAlarmPermissionPath()}`, (_, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        permission: true,
      }),
    );
  }),
];

export default alarmHandlers;
