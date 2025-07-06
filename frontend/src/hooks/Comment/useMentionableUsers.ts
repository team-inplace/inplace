import { useEffect, useState } from 'react';
import { SearchUserComplete } from '@/types';

export default function useMentionableUsers(searchResults?: SearchUserComplete[]) {
  const [mentionableUsers, setMentionableUsers] = useState<SearchUserComplete[]>([]);

  useEffect(() => {
    if (searchResults && searchResults.length > 0) {
      setMentionableUsers((prev) => {
        const newUsers = searchResults.filter((user) => !prev.some((u) => u.userId === user.userId));
        return [...prev, ...newUsers];
      });
    }
  }, [searchResults]);

  return mentionableUsers;
}
