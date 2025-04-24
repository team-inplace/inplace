import styled from 'styled-components';
import { GrPrevious, GrNext } from 'react-icons/gr';
import { useRef } from 'react';
import NoItem from '@/components/common/layouts/NoItem';
import Loading from '@/components/common/layouts/Loading';

type Props<T> = {
  items: T[];
  fetchNextPage: () => void;
  hasNextPage: boolean;
  loadMoreRef: React.RefCallback<HTMLDivElement>;
  isFetchingNextPage: boolean;
  renderItem: (item: T) => JSX.Element;
  noItemMessage: string;
  dataAttr: string;
  type: string;
};

export default function InfiniteScrollSection<T>({
  items,
  fetchNextPage,
  hasNextPage,
  loadMoreRef,
  isFetchingNextPage,
  renderItem,
  noItemMessage,
  dataAttr,
  type,
}: Props<T>) {
  const listRef = useRef<HTMLDivElement | null>(null);

  const scrollList = (direction: 'left' | 'right') => {
    if (!listRef.current) return;

    const someWidth = 400;
    const { scrollLeft, scrollWidth, clientWidth } = listRef.current;
    const remainingScroll = direction === 'left' ? scrollLeft : scrollWidth - clientWidth - scrollLeft;

    const scrollAmount =
      direction === 'left' ? -Math.min(someWidth, remainingScroll) : Math.min(someWidth, remainingScroll);

    listRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });

    if (direction === 'right' && hasNextPage) {
      const listItems = listRef.current.querySelectorAll(`div[${dataAttr}]`);
      if (listItems.length >= 10) {
        const tenthItem = listItems[9];
        const { left, right } = tenthItem.getBoundingClientRect();
        const { left: containerLeft, right: containerRight } = listRef.current.getBoundingClientRect();

        if (right <= containerRight && left >= containerLeft) {
          fetchNextPage();
        }
      }
    }
  };

  return (
    <SectionContainer>
      {items.length === 0 ? (
        <NoItem message={noItemMessage} height={200} />
      ) : (
        <>
          <ArrowButton aria-label="left_btn" onClick={() => scrollList('left')} className="left-arrow" direction="left">
            <GrPrevious size={30} />
          </ArrowButton>
          <ListContainer ref={listRef} $type={type}>
            {items.map((item) => renderItem(item))}
            {(hasNextPage || isFetchingNextPage) && (
              <LoadMoreTrigger ref={loadMoreRef}>
                <Loading size={30} />
              </LoadMoreTrigger>
            )}
          </ListContainer>
          {items.length > 5 && (
            <ArrowButton
              aria-label="right_btn"
              onClick={() => scrollList('right')}
              className="right-arrow"
              direction="right"
            >
              <GrNext size={30} />
            </ArrowButton>
          )}
        </>
      )}
    </SectionContainer>
  );
}

const SectionContainer = styled.div`
  display: flex;
  align-items: center;
  position: relative;
  overflow: visible;
  width: 100%;
`;

const ListContainer = styled.div<{ $type: string }>`
  overflow: hidden;
  display: flex;
  gap: 30px;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }

  & > * {
    flex-shrink: 0;
  }

  @media screen and (max-width: 768px) {
    gap: 18px;
    height: ${({ $type }) => ($type === 'influencer' ? '190px' : null)};
  }
`;

const ArrowButton = styled.button<{ direction: 'left' | 'right' }>`
  position: absolute;
  height: 100%;
  color: transparent;
  background: none;
  border: none;
  cursor: pointer;
  transition:
    color 0.3s,
    background 0.3s;
  padding: 0;
  z-index: 9;

  &:hover {
    color: white;
    background: ${({ direction }) =>
      direction === 'left'
        ? 'linear-gradient(to right, rgba(0, 0, 0, 0.7), transparent 90%)'
        : 'linear-gradient(to left, rgba(0, 0, 0, 0.7), transparent 90%)'};
  }

  &.left-arrow {
    left: 0;
  }

  &.right-arrow {
    right: 0;
  }

  svg {
    ${({ direction }) => (direction === 'left' ? 'padding-right: 20px' : 'padding-left: 20px')};
  }

  @media screen and (max-width: 768px) {
    svg {
      height: 20px;
    }
  }
`;

const LoadMoreTrigger = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60px;
  margin-top: 20px;
`;
