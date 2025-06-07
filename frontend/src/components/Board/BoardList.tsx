import styled from 'styled-components';
import { BoardListData, PageableData } from '@/types';
import Boarditem from './Boarditem';

interface Props {
  items:
    | {
        pages: PageableData<BoardListData>[];
        pageParams: number[];
      }
    | undefined;
  activeCategory: string;
}
export default function BoardList({ items, activeCategory }: Props) {
  return (
    <Wrapper>
      {items?.pages.flatMap((page) =>
        page.content.map((item) => (
          <div key={item.boardId}>
            <Boarditem item={item} activeCategory={activeCategory} />
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
