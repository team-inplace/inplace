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
}
export default function BoardList({ items }: Props) {
  return (
    <Wrapper>
      {items?.pages.flatMap((page) =>
        page.content.map((item) => (
          <>
            <Boarditem key={item.boardId} item={item} />
            <Separator />
          </>
        )),
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div``;
const Separator = styled.div`
  height: 1px;
  width: 100%;
  background-color: #6d6d6d;
`;
