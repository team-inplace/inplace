import styled from 'styled-components';
import { GrPrevious, GrNext } from 'react-icons/gr';

import { useRef } from 'react';
import SpotItem from './SpotItem';
import { SpotData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';

export default function SpotSection({ items = [] }: { items: SpotData[] }) {
  const listRef = useRef<HTMLDivElement | null>(null);

  const scrollList = (direction: 'left' | 'right') => {
    if (!listRef.current) return;

    const someWidth = 520;
    const { scrollLeft, scrollWidth, clientWidth } = listRef.current;
    const remainingScroll = direction === 'left' ? scrollLeft : scrollWidth - clientWidth - scrollLeft;

    const scrollAmount =
      direction === 'left' ? -Math.min(someWidth, remainingScroll) : Math.min(someWidth, remainingScroll);

    listRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
  };
  return (
    <SectionContainer>
      {items.length === 0 ? (
        <NoItem message="그곳 정보가 없어요!" height={200} />
      ) : (
        <>
          <ArrowButton aria-label="left_btn" onClick={() => scrollList('left')} className="left-arrow" direction="left">
            <GrPrevious size={40} />
          </ArrowButton>
          <ListContainer ref={listRef}>
            {items.map((spot) => {
              return (
                <SpotItem
                  key={spot.videoId}
                  videoId={spot.videoId}
                  influencerName={spot.influencerName}
                  videoUrl={spot.videoUrl}
                  place={spot.place}
                />
              );
            })}
          </ListContainer>
          {items.length > 3 && (
            <ArrowButton
              aria-label="right_btn"
              onClick={() => scrollList('right')}
              className="right-arrow"
              direction="right"
            >
              <GrNext size={40} />
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
const ListContainer = styled.div`
  overflow: hidden;
  display: flex;
  gap: 40px;
  overflow-x: auto;
  scroll-snap-type: x mandatory;
  scroll-behavior: smooth;
  scrollbar-width: none;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }

  @media screen and (max-width: 768px) {
    gap: 18px;
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
    background: ${({ direction, theme }) =>
      direction === 'left'
        ? `linear-gradient(to right, ${theme.backgroundColor === '#292929' ? 'rgba(0, 0, 0, 0.7)' : 'rgba(50, 50, 50, 0.5)'}, transparent 90%)`
        : `linear-gradient(to left, ${theme.backgroundColor === '#292929' ? 'rgba(0, 0, 0, 0.7)' : 'rgba(50, 50, 50, 0.5)'}, transparent 90%)`};
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
