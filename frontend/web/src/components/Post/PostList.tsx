import styled from 'styled-components';
import { PostListData, CursorData } from '@/types';
import Postitem from './Postitem';
import Loading from '../common/layouts/Loading';

interface Props {
  items:
    | {
        pages: CursorData<PostListData>[];
        pageParams: ({ cursorId: number; cursorValue: number } | null)[];
      }
    | undefined;
  activeCategory: string;
  scrollRef: React.RefCallback<HTMLDivElement>;
  isFetchingNextPage: boolean;
  hasNextPage: boolean;
}
export default function PostList({ items, activeCategory, scrollRef, isFetchingNextPage, hasNextPage }: Props) {
  return (
    <Wrapper>
      {items?.pages.flatMap((page) =>
        page.posts.map((item) => (
          <div key={item.postId}>
            <Postitem item={item} activeCategory={activeCategory} />
            <Separator />
          </div>
        )),
      )}
      {(hasNextPage || isFetchingNextPage) && (
        <LoadMoreTrigger ref={scrollRef}>
          <Loading size={30} />
        </LoadMoreTrigger>
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div``;
const Separator = styled.div`
  height: 1px;
  width: 100%;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#6d6d6d' : '#d4d4d4')};
`;
const LoadMoreTrigger = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;
