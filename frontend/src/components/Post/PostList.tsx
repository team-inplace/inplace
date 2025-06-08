import styled from 'styled-components';
import { PostListData, PageableData } from '@/types';
import Postitem from './Postitem';

interface Props {
  items:
    | {
        pages: PageableData<PostListData>[];
        pageParams: number[];
      }
    | undefined;
  activeCategory: string;
}
export default function PostList({ items, activeCategory }: Props) {
  return (
    <Wrapper>
      {items?.pages.flatMap((page) =>
        page.content.map((item) => (
          <div key={item.postId}>
            <Postitem item={item} activeCategory={activeCategory} />
            <Separator />
          </div>
        )),
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
