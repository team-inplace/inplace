import { SearchUserComplete } from '@/types';

export function convertMentionsToEntities(text: string, mentionableUsers: SearchUserComplete[]) {
  const regex = /@([A-Za-z0-9가-힣_]+)/g;
  return text.replace(regex, (full, nickname) => {
    const user = mentionableUsers.find((u) => u.nickname === nickname);
    if (user) {
      return `<<@${user.userId}:${user.nickname}>>`;
    }
    return full;
  });
}

export function extractMentionQuery(text: string, cursorPos: number): string {
  const beforeCursor = text.slice(0, cursorPos);
  const atIndex = beforeCursor.lastIndexOf('@');
  if (atIndex === -1) return '';
  const match = beforeCursor.slice(atIndex + 1).match(/^[A-Za-z0-9가-힣_]+/);
  return match ? match[0] : '';
}
