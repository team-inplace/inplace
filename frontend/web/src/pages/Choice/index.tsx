import { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import { Text } from '@/components/common/typography/Text';
import BaseLayout from '@/components/common/BaseLayout';
import { useGetAllInfluencers } from '@/api/hooks/useGetAllInfluencers';
import { usePostMultipleInfluencerLike } from '@/api/hooks/usePostMultipleInfluencerLike';
import Button from '@/components/common/Button';
import Pagination from '@/components/common/Pagination';
import InfluencerSearchBar from '@/components/common/InfluencerSearchBar';
import { useGetSearchInfluencers } from '@/api/hooks/useGetSearchInfluencers';
import useDebounce from '@/hooks/useDebounce';
import useTheme from '@/hooks/useTheme';
import useIsMobile from '@/hooks/useIsMobile';
import { usePostDeviceToken } from '@/api/hooks/usePostDeviceToken';
import { setupFCMToken } from '@/libs/FCM';

export default function ChoicePage() {
  const PAGE_SIZE_MOBILE = 9;
  const PAGE_SIZE_DESKTOP = 10;
  const DEBOUNCE_DELAY_MS = 300;

  const navigate = useNavigate();
  const { mutateAsync: postMultipleLikes } = usePostMultipleInfluencerLike();
  const { mutateAsync: postDeviceToken } = usePostDeviceToken();
  const [selectedInfluencers, setSelectedInfluencers] = useState<Set<number>>(new Set());
  const [currentPage, setCurrentPage] = useState(1);
  const [inputValue, setInputValue] = useState('');
  const isMobile = useIsMobile();
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  useEffect(() => {
    const isFirstUser = document.cookie
      .split('; ')
      .find((row) => row.startsWith('is_first_user='))
      ?.split('=')[1];

    if (isFirstUser !== 'true') {
      navigate('/', { replace: true });
      return;
    }
    const setupFCMForNewUser = async () => {
      try {
        await setupFCMToken(postDeviceToken);
      } catch (fcmError) {
        console.error('FCM 설정 실패:', fcmError);
      }
    };
    setupFCMForNewUser();
    document.cookie = 'is_first_user=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; domain=.inplace.my; Secure';
  }, [navigate]);

  const debouncedInputValue = useDebounce(inputValue, DEBOUNCE_DELAY_MS);

  const { data: allInfluencersData } = useGetAllInfluencers({
    page: currentPage - 1,
    size: isMobile ? PAGE_SIZE_MOBILE : PAGE_SIZE_DESKTOP,
  });

  const { data: filteredData } = useGetSearchInfluencers({
    value: debouncedInputValue,
    page: currentPage - 1,
    size: 10,
  });
  const pageableData = debouncedInputValue ? filteredData : allInfluencersData;

  useEffect(() => {
    setCurrentPage(1);
  }, [debouncedInputValue]);

  const handlePageChange = (pageNum: number) => {
    setCurrentPage(pageNum);
  };

  const handleSkip = () => {
    navigate('/');
  };

  const handleStart = async () => {
    try {
      if (selectedInfluencers.size > 0) {
        await postMultipleLikes({
          influencerIds: [...selectedInfluencers],
          likes: true,
        });
      }
    } catch (error) {
      console.error('좋아요 처리 중 오류 발생:', error);
    } finally {
      navigate('/');
    }
  };

  const handleToggleLike = (influencerId: number, isLiked: boolean) => {
    setSelectedInfluencers((prev) => {
      const newSet = new Set(prev);
      if (isLiked) {
        newSet.add(influencerId);
      } else {
        newSet.delete(influencerId);
      }
      return newSet;
    });
  };

  const buttonStyle = {
    fontWeight: 'bold',
    width: isMobile ? '140px' : '170px',
    height: isMobile ? '40px' : '46px',
    fontSize: isMobile ? '16px' : '18px',
  };

  return (
    <PageContainer>
      <Title>
        <Text size="l" weight="bold">
          관심 있는{' '}
          <Text size="ll" weight="bold" style={{ color: '#47c8d9' }}>
            인플루언서
          </Text>
          를 선택하세요!
        </Text>
      </Title>
      <InfluencerSearchBar inputValue={inputValue} setInputValue={setInputValue} />
      <LayoutWrapper>
        <BaseLayout
          type="influencer"
          mainText=""
          SubText=""
          items={pageableData?.content || []}
          showMoreButton={false}
          isChoice
          onToggleLike={handleToggleLike}
          selectedInfluencers={selectedInfluencers}
        />
      </LayoutWrapper>
      <Pagination
        currentPage={currentPage}
        totalPages={pageableData?.totalPages || 0}
        totalItems={pageableData?.totalElements}
        onPageChange={handlePageChange}
        itemsPerPage={pageableData?.pageable.pageSize}
      />
      <ButtonWrapper>
        <Button aria-label="건너뛰기" variant="white" style={buttonStyle} onClick={handleSkip}>
          건너뛰기
        </Button>
        <Button
          aria-label="시작하기"
          variant="mint"
          style={{
            ...buttonStyle,
            ...(isDarkMode ? {} : { background: '#47c8d9' }),
          }}
          onClick={handleStart}
        >
          시작하기
        </Button>
      </ButtonWrapper>
    </PageContainer>
  );
}

const PageContainer = styled.div`
  padding: 16px 0;
  display: flex;
  flex-direction: column;
  gap: 30px;

  @media screen and (max-width: 768px) {
    width: 100%;
    gap: 20px;
    align-items: center;
  }
`;

const LayoutWrapper = styled.div`
  @media screen and (max-width: 768px) {
    width: 100%;
    display: flex;
    justify-content: center;
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  width: 960px;
  margin-bottom: 30px;

  @media screen and (max-width: 768px) {
    width: 90%;
    margin-bottom: 20px;
  }
`;

const Title = styled.div`
  width: 90%;
`;
