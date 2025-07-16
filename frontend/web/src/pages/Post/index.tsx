import styled from 'styled-components';
import { IoIosArrowDown } from 'react-icons/io';
import { GoPencil } from 'react-icons/go';
import { useRef, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Button from '@/components/common/Button';
import PostList from '@/components/Post/PostList';
import { useGetInfinitPostList } from '@/api/hooks/useGetInfinitPostList';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';
import useClickOutside from '@/hooks/useClickOutside';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';
import ScrollToTop from '@/components/common/Button/ScrollToTop';
import useIsMobile from '@/hooks/useIsMobile';
import useTheme from '@/hooks/useTheme';
import { HEADER_HEIGHT } from '@/components/common/layouts/Header';
import useScrollToTop from '@/hooks/useScrollToTop';

export default function PostPage() {
  const { isAuthenticated } = useAuth();
  const dropdownRef = useRef<HTMLDivElement>(null);
  const [activeCategory, setActiveCategory] = useState('전체 게시글');
  const [showLoginModal, setShowLoginModal] = useState(false);
  const isMobile = useIsMobile();
  const { theme } = useTheme();
  const isDark = theme === 'dark';
  const navigate = useNavigate();
  const location = useLocation();
  const handleScrollToTop = useScrollToTop();

  const getInitialSortOption = (): string => {
    const searchParams = new URLSearchParams(location.search);
    return searchParams.get('sort') || 'createdAt';
  };

  const [sortOption, setSortOption] = useState(getInitialSortOption());
  const [showSortOptions, setShowSortOptions] = useState(false);

  const {
    data: postList,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useGetInfinitPostList({ size: 5, sort: sortOption });

  const scrollRef = useInfiniteScroll({ fetchNextPage, hasNextPage, isFetchingNextPage });

  const sortLabel: Record<string, string> = {
    createdAt: '최신순',
    popularity: '인기순',
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

  const handlePosting = () => {
    if (!isAuthenticated) {
      setShowLoginModal(true);
      return;
    }
    navigate('/posting', { state: { type: 'create' } });
  };

  useClickOutside([dropdownRef], () => {
    setShowSortOptions(false);
  });
  return (
    <Wrapper>
      <MobileHeader>
        <Header>
          <PostCategory>
            <Tap
              aria-label="전체 게시글 보기"
              $active={activeCategory === '전체 게시글'}
              onClick={() => {
                setActiveCategory('전체 게시글');
                handleScrollToTop();
              }}
            >
              {isMobile ? '전체' : '전체 게시글'}
            </Tap>
          </PostCategory>
          <RightHeader>
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
                  <SortItem onClick={() => handleSortChange('createdAt')}>최신순 {sortOption === 'createdAt'}</SortItem>
                  <SortItem onClick={() => handleSortChange('popularity')}>
                    인기순 {sortOption === 'popularity'}
                  </SortItem>
                </SortDropdown>
              )}
            </SortSection>
            <WriteButton aria-label="게시판 글쓰기" size="small" onClick={handlePosting}>
              <GoPencil size={14} />
              글쓰기
            </WriteButton>
          </RightHeader>
        </Header>
      </MobileHeader>
      <PostWrapper>
        <PostList
          items={postList}
          activeCategory={activeCategory}
          scrollRef={scrollRef}
          isFetchingNextPage={isFetchingNextPage}
          hasNextPage={hasNextPage}
        />
      </PostWrapper>
      <MobileWriteButton
        aria-label="모바일 게시판 글쓰기"
        size="small"
        variant={isDark ? 'outline' : 'blackOutline'}
        onClick={handlePosting}
      >
        <GoPencil size={20} />
      </MobileWriteButton>
      <ScrollToTop />
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 100%;
  @media screen and (max-width: 768px) {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
`;
const PostCategory = styled.div``;
const PostWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 50px;

  @media screen and (max-width: 768px) {
    width: 90%;
    margin-top: 32px;
  }
`;

const Header = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin: 20px 0px 0px;
  align-items: center;
  @media screen and (max-width: 768px) {
    margin-top: 0px;
  }
`;

const MobileHeader = styled.div`
  @media screen and (max-width: 768px) {
    display: flex;
    justify-content: space-between;
    width: 90%;
    position: fixed;
    top: ${HEADER_HEIGHT}px;
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};
    padding: 10px 0px;
  }
`;
const Tap = styled.button<{ $active: boolean }>`
  color: ${({ $active, theme }) => {
    if ($active) return theme.backgroundColor === '#292929' ? '#55ebff' : '#47c8d9';
    return 'black';
  }};
  border: none;
  background: none;
  font-size: 24px;
  font-weight: bold;

  @media screen and (max-width: 768px) {
    font-size: 16px;
    padding: 0;
  }
`;
const SortSection = styled.div`
  position: relative;
  display: flex;
  justify-content: flex-end;

  @media screen and (max-width: 768px) {
    width: 90%;
    margin: 0;
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

const RightHeader = styled.div`
  display: flex;
  align-items: center;
  gap: 12px;
`;
const WriteButton = styled(Button)`
  width: 80px;
  gap: 4px;
  height: 30px;
  font-size: 14px;
  color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#55ebff' : 'black')};
  box-shadow: ${({ theme }) =>
    theme.backgroundColor === '#292929' ? '0 0 0 1px #55ebff inset' : '0 0 0 1px black inset'};
  background: none;
  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1b1a1a' : '#e5f6f6')};
  }
  @media screen and (max-width: 768px) {
    display: none;
  }
`;
const MobileWriteButton = styled(Button)`
  display: none;
  @media screen and (max-width: 768px) {
    display: block;
    position: fixed;
    right: max(30px, calc(50% - 480px));
    bottom: 8%;
    width: 46px;
    height: 46px;
    border-radius: 50%;
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#292929' : '#ecfbfb')};
    cursor: pointer;
    z-index: 10;
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
