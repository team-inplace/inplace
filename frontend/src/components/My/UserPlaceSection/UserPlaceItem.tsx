import { PiHeartFill, PiHeartLight } from 'react-icons/pi';
import { Link, useLocation } from 'react-router-dom';

import styled from 'styled-components';

import { useCallback, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { Text } from '@/components/common/typography/Text';
import { Paragraph } from '@/components/common/typography/Paragraph';

import { UserPlaceData } from '@/types';
import { usePostPlaceLike } from '@/api/hooks/usePostPlaceLike';
import useAuth from '@/hooks/useAuth';
import LoginModal from '@/components/common/modals/LoginModal';

const getFullAddress = (addr: UserPlaceData['address']) => {
  return [addr.address1, addr.address2, addr.address3].filter(Boolean).join(' ');
};

export default function UserPlaceItem({ placeId, placeName, influencerName, likes, address }: UserPlaceData) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();
  const [isLike, setIsLike] = useState(likes);
  const [showLoginModal, setShowLoginModal] = useState(false);
  const { mutate: postLike } = usePostPlaceLike();
  const queryClient = useQueryClient();

  const handleLikeClick = useCallback(
    (event: React.MouseEvent<HTMLDivElement>) => {
      event.stopPropagation();
      event.preventDefault();
      if (!isAuthenticated) {
        setShowLoginModal(true);
        return;
      }
      const newLikeStatus = !isLike;
      postLike(
        { placeId, likes: newLikeStatus },
        {
          onSuccess: () => {
            setIsLike(newLikeStatus);
            queryClient.invalidateQueries({ queryKey: ['UserPlace'] }); // 내가 좋아요 한 장소
            queryClient.invalidateQueries({ queryKey: ['placeInfo', placeId] });
          },
          onError: (error) => {
            console.error('Error:', error);
          },
        },
      );
    },
    [isLike, placeId, postLike],
  );
  return (
    <>
      <Wrapper to={`/detail/${placeId}`}>
        <TextWrapper>
          <Text className="overflow" size="m" weight="bold">
            {placeName}
          </Text>
          <Text className="overflow" size="xs" weight="normal">
            {getFullAddress(address)}
          </Text>
          <Paragraph size="xs" weight="normal">
            {influencerName}
          </Paragraph>
        </TextWrapper>
        <LikeIcon onClick={(e: React.MouseEvent<HTMLDivElement>) => handleLikeClick(e)}>
          {isLike ? <PiHeartFill color="#fe7373" size={26} /> : <PiHeartLight size={30} />}
        </LikeIcon>
      </Wrapper>
      {showLoginModal && (
        <LoginModal immediateOpen currentPath={location.pathname} onClose={() => setShowLoginModal(false)} />
      )}
    </>
  );
}
const Wrapper = styled(Link)`
  display: flex;
  align-items: start;
  text-decoration: none;
  gap: 4px;
  width: 180px;
  border-radius: 4px;
  padding: 10px;
  justify-content: space-between;
  color: inherit;

  &:hover {
    background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1b1a1a' : '#d5ecec')};
  }
`;

const LikeIcon = styled.div`
  z-index: 100;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    svg {
      width: 22px;
      height: 22px;
    }
  }
`;
const TextWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  line-height: 130%;
  padding: 8px 0px;
  width: 85%;
  .overflow {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  gap: 4px;
  > *:nth-child(3) {
    margin-top: 12px;
  }
  @media screen and (max-width: 768px) {
    padding: 0px;
    gap: 0px;
    > *:nth-child(3) {
      margin-top: 4px;
    }
  }
`;
