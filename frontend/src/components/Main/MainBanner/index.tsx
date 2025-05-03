import { useEffect, useState, useRef } from 'react';
import { GrPrevious, GrNext } from 'react-icons/gr';
import styled from 'styled-components';

import BannerItem from '@/components/Main/MainBanner/BannerItem';

import { BannerData } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';
import useIsMobile from '@/hooks/useIsMobile';

const getTransformValue = ({
  $currentIndex,
  $isMobile,
  $items,
  $isReturning,
  $animationProgress,
}: {
  $currentIndex: number;
  $isMobile: boolean;
  $items: BannerData[];
  $isReturning: boolean;
  $animationProgress: number;
}) => {
  if ($isMobile) {
    return `translateX(-${$currentIndex * 100}%)`;
  }

  if ($currentIndex === 0 && !$isReturning) {
    return 'none';
  }

  const totalItems = $items.length;
  const maxIndex = totalItems - 2;

  if ($isReturning) {
    const startPosition = maxIndex * 50;

    // 애니메이션 위치 계산 (animationProgress는 0(시작)에서 1(완료)로 변함)
    const position = startPosition - startPosition * $animationProgress;

    return `translateX(-${position}%)`;
  }
  const isLastTwoItems = $currentIndex >= maxIndex && $currentIndex <= totalItems - 1;

  if (isLastTwoItems) {
    return `translateX(-${maxIndex * 50}%)`;
  }

  return `translateX(-${$currentIndex * 50}%)`;
};

export default function MainBanner({ items = [] }: { items: BannerData[] }) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isReturning, setIsReturning] = useState(false);
  const [animationProgress, setAnimationProgress] = useState(0);
  const animationRef = useRef<number | null>(null);
  const isMobile = useIsMobile();

  const filteredItems = items.filter((item) => {
    if (isMobile) {
      return item.isMobile === true || !item.isMain;
    }
    return item.isMobile === false;
  });

  const sortedItems = [...filteredItems].sort((a, b) => {
    if (a.isMain && !b.isMain) return -1;
    if (!a.isMain && b.isMain) return 1;
    return 0;
  });

  // 애니메이션 프레임 처리
  const animateReturn = (startTime: number) => {
    const animationDuration = 400; // ms

    const animate = (currentTime: number) => {
      const elapsedTime = currentTime - startTime;
      const progress = Math.min(elapsedTime / animationDuration, 1);

      setAnimationProgress(progress);

      if (progress < 1) {
        animationRef.current = requestAnimationFrame(animate);
      } else {
        // 애니메이션 완료
        setIsReturning(false);
        setAnimationProgress(0);
        setCurrentIndex(0);
      }
    };

    animationRef.current = requestAnimationFrame(animate);
  };

  useEffect(() => {
    setCurrentIndex(0);
  }, [isMobile]);

  useEffect(() => {
    return () => {
      if (animationRef.current) {
        cancelAnimationFrame(animationRef.current);
      }
    };
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      if (sortedItems.length > 1 && !isReturning) {
        const maxIndex = isMobile ? sortedItems.length - 1 : sortedItems.length - 2;

        if (currentIndex === maxIndex) {
          setIsReturning(true);
          animateReturn(performance.now());
        } else {
          setCurrentIndex((prev) => prev + 1);
        }
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [sortedItems.length, isMobile, currentIndex, isReturning]);

  const handleBtnPrevClick = () => {
    if (!isReturning) {
      setCurrentIndex((prevIndex) => Math.max(prevIndex - 1, 0));
    }
  };

  const handleBtnNextClick = () => {
    if (sortedItems.length > 1 && !isReturning) {
      const maxIndex = isMobile ? sortedItems.length - 1 : sortedItems.length - 2;

      if (currentIndex === maxIndex) {
        setIsReturning(true);
        animateReturn(performance.now());
      } else {
        setCurrentIndex((prev) => Math.min(prev + 1, maxIndex));
      }
    }
  };

  return (
    <Container>
      {sortedItems.length === 0 ? (
        <NoItem message="배너 정보가 없어요!" height={400} />
      ) : (
        <>
          <PrevBtn
            aria-label="배너 스크롤 왼쪽"
            onClick={handleBtnPrevClick}
            disabled={currentIndex === 0 || isReturning}
          >
            <PrevIconWrapper>
              <GrPrevious size={40} />
            </PrevIconWrapper>
          </PrevBtn>
          <NextBtn
            aria-label="배너 스크롤 오른쪽"
            onClick={handleBtnNextClick}
            disabled={currentIndex === (isMobile ? sortedItems.length - 1 : sortedItems.length - 2) || isReturning}
          >
            <NextIconWrapper>
              <GrNext size={40} />
            </NextIconWrapper>
          </NextBtn>
          <CarouselWrapper>
            <CarouselContainer
              $currentIndex={currentIndex}
              $isMobile={isMobile}
              $items={sortedItems}
              $isReturning={isReturning}
              $animationProgress={animationProgress}
            >
              {sortedItems.map((item) => (
                <BannerItem
                  key={item.id}
                  id={item.id}
                  imageUrl={item.imageUrl}
                  influencerId={item.influencerId}
                  isMain={item.isMain}
                  isMobile={item.isMobile}
                  isFirst={
                    (currentIndex === 0 && !isReturning && !isMobile) ||
                    (isReturning && animationProgress > 0.5 && !isMobile)
                  }
                />
              ))}
            </CarouselContainer>
          </CarouselWrapper>
        </>
      )}
    </Container>
  );
}

const Container = styled.div`
  position: relative;
  width: 100%;
  display: flex;
  align-items: center;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const PrevIconWrapper = styled.div`
  filter: drop-shadow(-1px 0 1px rgba(0, 0, 0, 0.4));
`;

const NextIconWrapper = styled.div`
  filter: drop-shadow(1px 0 1px rgba(0, 0, 0, 0.4));
`;

const PrevBtn = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  left: 10px;
  z-index: 1;
  color: white;

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

const NextBtn = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  position: absolute;
  right: 10px;
  z-index: 1;
  color: white;

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
`;

const CarouselWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  overflow: hidden;
`;

const CarouselContainer = styled.div<{
  $currentIndex: number;
  $isMobile: boolean;
  $items: BannerData[];
  $isReturning: boolean;
  $animationProgress: number;
}>`
  display: flex;
  transition: ${(props) => (props.$isReturning ? 'none' : 'transform 0.5s ease-in-out')};
  transform: ${getTransformValue};
  width: 100%;
`;
