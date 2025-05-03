import styled from 'styled-components';
import { GrPrevious, GrNext } from 'react-icons/gr';

import { useRef } from 'react';
import { UserPlaceData } from '@/types';
import UserPlaceItem from './UserPlaceItem';
import NoItem from '@/components/common/layouts/NoItem';

export default function UserPlaceSection({ items = [] }: { items: UserPlaceData[] }) {
  const listRef = useRef<HTMLDivElement | null>(null);

  const scrollList = (direction: 'left' | 'right') => {
    if (!listRef.current) return;

    const someWidth = 400;
    const { scrollLeft, scrollWidth, clientWidth } = listRef.current;
    const remainingScroll = direction === 'left' ? scrollLeft : scrollWidth - clientWidth - scrollLeft;

    const scrollAmount =
      direction === 'left' ? -Math.min(someWidth, remainingScroll) : Math.min(someWidth, remainingScroll);

    listRef.current.scrollBy({ left: scrollAmount, behavior: 'smooth' });
  };

  return (
    <SectionContainer>
      {items.length === 0 ? (
        <NoItem message="장소 정보가 없어요!" height={180} />
      ) : (
        <>
          <ArrowButton
            aria-label="마이 장소 왼쪽"
            onClick={() => scrollList('left')}
            className="left-arrow"
            direction="left"
          >
            <GrPrevious size={30} />
          </ArrowButton>
          <ListContainer ref={listRef}>
            {items.map((place) => {
              return (
                <UserPlaceItem
                  key={place.placeId}
                  placeId={place.placeId}
                  placeName={place.placeName}
                  videoUrl={place.videoUrl}
                  influencerName={place.influencerName}
                  address={place.address}
                  likes={place.likes}
                />
              );
            })}
          </ListContainer>
          {items.length > 5 && (
            <ArrowButton
              aria-label="마이 장소 오른쪽"
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
const ListContainer = styled.div`
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
  }
`;
const ArrowButton = styled.button<{ direction: 'left' | 'right' }>`
  position: absolute;
  font-size: 24px;
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
