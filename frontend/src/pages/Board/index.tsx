import styled from 'styled-components';
import { IoIosArrowDown } from 'react-icons/io';
import { GoPencil } from 'react-icons/go';
import { useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Button from '@/components/common/Button';
import BoardList from '@/components/Board/BoardList';
import { useGetInfinitBoardList } from '@/api/hooks/useGetInfinitBoardList';

export default function BoardPage() {
  const dropdownRef = useRef<HTMLDivElement>(null);
  const [activeCategory, setActiveCategory] = useState('전체 게시글');
  const navigate = useNavigate();
  const location = useLocation();

  const getInitialSortOption = (): string => {
    const searchParams = new URLSearchParams(location.search);
    return searchParams.get('sort') || 'publishTime';
  };

  const [sortOption, setSortOption] = useState(getInitialSortOption());
  const [showSortOptions, setShowSortOptions] = useState(false);

  const { data: boardList } = useGetInfinitBoardList({ size: 5, sort: sortOption });

  const sortLabel: Record<string, string> = {
    publishTime: '최신순',
    popularity: '인기순',
    likes: '좋아요순',
  };

  const handleSortChange = (option: string) => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set('sort', option);
    if (!searchParams.has('page')) {
      searchParams.set('page', '1');
    }

    navigate(`${location.pathname}?${searchParams.toString()}`);
    setSortOption(option);
    setShowSortOptions(false);
  };

  return (
    <>
      <Header>
        <BoardCategory>
          <Tap
            aria-label="전체 게시글 보기"
            $active={activeCategory === '전체 게시글'}
            onClick={() => setActiveCategory('전체 게시글')}
          >
            전체 게시글
          </Tap>
        </BoardCategory>
        <Button
          aria-label="게시판 글쓰기"
          variant="visit"
          size="small"
          style={{ width: '90px', gap: '4px', height: '36px' }}
        >
          <GoPencil size={16} />
          글쓰기
        </Button>
      </Header>
      <SortSection ref={dropdownRef}>
        <StyledButton
          aria-label="게시판 정렬"
          variant="white"
          size="small"
          onClick={() => setShowSortOptions(!showSortOptions)}
        >
          <span>{sortLabel[sortOption]}</span>
          <IoIosArrowDown size={16} />
        </StyledButton>
        {showSortOptions && (
          <SortDropdown>
            <SortItem onClick={() => handleSortChange('publishTime')}>최신순 {sortOption === 'publishTime'}</SortItem>
            <SortItem onClick={() => handleSortChange('popularity')}>인기순 {sortOption === 'popularity'}</SortItem>
            <SortItem onClick={() => handleSortChange('likes')}>좋아요순 {sortOption === 'likes'}</SortItem>
          </SortDropdown>
        )}
      </SortSection>
      <Wrapper>
        <BoardList items={boardList} />
      </Wrapper>
    </>
  );
}

const BoardCategory = styled.div``;
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;

  @media screen and (max-width: 768px) {
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 40px;
    align-items: center;
  }
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  align-items: center;
`;
const Tap = styled.button<{ $active: boolean }>`
  color: ${($active) => ($active ? '#55EBFF' : 'white')};
  border: none;
  background: none;
  font-size: 24px;
  font-weight: bold;
`;
const SortSection = styled.div`
  position: relative;
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;

  @media screen and (max-width: 768px) {
    margin-bottom: 6px;
    margin-top: -14px;
  }
`;

const StyledButton = styled(Button)`
  justify-content: space-between;
  gap: 8px;
  padding: 6px 10px;
  width: 90px;
  cursor: pointer;
  font-size: 14px;
  margin-left: auto;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    width: 80px;
    font-size: 12px;
    padding: 4px 8px;
  }
`;

const SortDropdown = styled.div`
  position: absolute;
  top: 100%;
  z-index: 2;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 90px;
  margin-top: 4px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};

  @media screen and (max-width: 768px) {
    width: 80px;
  }
`;

const SortItem = styled.div`
  padding: 10px 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#222222' : '#daeeee')};
  }

  @media screen and (max-width: 768px) {
    font-size: 12px;
    padding: 8px 10px;
  }
`;
