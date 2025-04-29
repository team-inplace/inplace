import { styled } from 'styled-components';
import { useRef, useState } from 'react';
import { MdOutlineDriveFileRenameOutline } from 'react-icons/md';
import { useQueryClient } from '@tanstack/react-query';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { useGetUserInfluencer } from '@/api/hooks/useGetUserInfluencer';
import { useGetUserPlace } from '@/api/hooks/useGetUserPlace';
import { Text } from '@/components/common/typography/Text';
import { useGetUserReview } from '@/api/hooks/useGetUserReview';
import MyReview from '@/components/My/UserReview';
import InfiniteBaseLayout from '@/components/My/infiniteBaseLayout';
import useInfiniteScroll from '@/hooks/useInfiniteScroll';
import { usePatchNickname } from '@/api/hooks/usePatchNickname';
import { useGetUserInfo } from '@/api/hooks/useGetUserInfo';

export default function MyPage() {
  const influencerRef = useRef<HTMLDivElement>(null);
  const {
    data: influencers,
    fetchNextPage: influencerFetchNextPage,
    hasNextPage: influencerHasNextPage,
    isFetchingNextPage: influencerIsFetchingNextPage,
  } = useGetUserInfluencer(10);
  const influencerLoadMoreRef = useInfiniteScroll({
    fetchNextPage: influencerFetchNextPage,
    hasNextPage: influencerHasNextPage,
    isFetchingNextPage: influencerIsFetchingNextPage,
  });

  const placeRef = useRef<HTMLDivElement>(null);
  const {
    data: places,
    fetchNextPage: placeFetchNextPage,
    hasNextPage: placeHasNextPage,
    isFetchingNextPage: placeIsFetchingNextPage,
  } = useGetUserPlace(10);
  const placeLoadMoreRef = useInfiniteScroll({
    fetchNextPage: placeFetchNextPage,
    hasNextPage: placeHasNextPage,
    isFetchingNextPage: placeIsFetchingNextPage,
  });

  const reviewRef = useRef<HTMLDivElement>(null);
  const { data: reviews, fetchNextPage, hasNextPage, isFetchingNextPage } = useGetUserReview(10);
  const reviewLoadMoreRef = useInfiniteScroll({
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  });
  const { data: userNickname } = useGetUserInfo();
  const [nickname, setNickname] = useState(userNickname?.nickname || '');
  const [isVisible, setIsVisible] = useState(true);
  const { mutate: patchNickname } = usePatchNickname();
  const queryClient = useQueryClient();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    patchNickname(nickname, {
      onSuccess: () => {
        localStorage.setItem('nickname', nickname);
        setIsVisible(true);
        queryClient.invalidateQueries({ queryKey: ['UserInfo'] });
      },
      onError: () => {
        alert('닉네임 변경에 실패했습니다. 다시 시도해주세요!');
      },
    });
  };
  return (
    <Wrapper>
      <TitleWrapper>
        {isVisible ? (
          <NickNameWrapper>
            <Text size="l" weight="bold">
              <Text size="xl" weight="bold" style={{ color: '#47c8d9' }}>
                {userNickname?.nickname}
              </Text>
              <CustomButton aria-label="닉네임 수정" onClick={() => setIsVisible(false)}>
                <MdOutlineDriveFileRenameOutline size={24} color="#47c8d9" />
              </CustomButton>
              님, 안녕하세요!
            </Text>
          </NickNameWrapper>
        ) : (
          <Form onSubmit={handleSubmit}>
            <Input type="text" value={nickname} onChange={(e) => setNickname(e.target.value)} />
            <CustomButton aria-label="닉네임 수정 완료" type="submit">
              <MdOutlineDriveFileRenameOutline size={24} color="#47c8d9" />
            </CustomButton>
          </Form>
        )}
        <Paragraph size="m" weight="bold">
          인플레이스를 이용해주셔서 감사합니다.
        </Paragraph>
      </TitleWrapper>
      <InfiniteBaseLayout
        type="influencer"
        mainText=""
        SubText="나의 인플루언서"
        items={influencers.pages.flatMap((page) => page.content)}
        loadMoreRef={influencerLoadMoreRef}
        sectionRef={influencerRef}
        hasNextPage={influencerHasNextPage}
        fetchNextPage={influencerFetchNextPage}
        isFetchingNextPage={influencerIsFetchingNextPage}
      />
      <InfiniteBaseLayout
        type="place"
        mainText=""
        SubText="나의 관심 장소"
        items={places.pages.flatMap((page) => page.content)}
        loadMoreRef={placeLoadMoreRef}
        sectionRef={placeRef}
        hasNextPage={placeHasNextPage}
        fetchNextPage={placeFetchNextPage}
        isFetchingNextPage={placeIsFetchingNextPage}
      />
      <MyReview
        mainText="나의 리뷰"
        items={reviews.pages.flatMap((page) => page.content)}
        loadMoreRef={reviewLoadMoreRef}
        sectionRef={reviewRef}
        hasNextPage={hasNextPage}
        isFetchingNextPage={isFetchingNextPage}
      />
    </Wrapper>
  );
}
const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 60px;
  padding: 30px 0px 60px;

  @media screen and (max-width: 768px) {
    width: 100%;
    gap: 40px;
    align-items: center;
  }
`;
const TitleWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 14px;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 10px;
    margin-bottom: 10px;
  }
`;
const Form = styled.form`
  display: flex;
  align-items: end;
`;
const Input = styled.input`
  background: none;
  border: none;
  font-size: 32px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : '#333333')};
  height: 32px;
  max-width: 200px;
  padding: 0;
  &:focus {
    outline: none;
  }
`;
const CustomButton = styled.button`
  background: none;
  border: none;
  display: flex;
  align-content: end;
  padding: 0;
  cursor: pointer;
`;
const NickNameWrapper = styled.div`
  display: flex;
  align-items: center;
  span {
    align-items: end;
    display: flex;
  }
`;
