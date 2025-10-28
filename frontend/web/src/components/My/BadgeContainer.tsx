import styled from 'styled-components';
import { useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import { useGetAllBadge } from '@/api/hooks/useGetAllBadge';
import { Text } from '../common/typography/Text';
import { BadgeData } from '@/types';
import Button from '../common/Button';
import { usePatchBadge } from '@/api/hooks/usePatchBadge';

export default function BadgeContainer({ baseBadge }: { baseBadge: number | null }) {
  const queryClient = useQueryClient();
  const { data: badgeList = [] } = useGetAllBadge();
  const [currentBadge, setCurrentBadge] = useState<number | null>(baseBadge);
  const { mutate: patchBadge } = usePatchBadge();

  const handleChangeBadge = (badge: BadgeData) => {
    if (!badge.isOwned) return;
    setCurrentBadge(badge.id);
  };
  const handleClickBadgeBtn = () => {
    if (currentBadge === null) {
      alert('변경할 칭호를 선택해주세요!');
      return;
    }
    if (currentBadge === baseBadge) {
      alert('기존 칭호와 같습니다. 다른 칭호를 선택해주세요!');
      return;
    }
    patchBadge(currentBadge, {
      onSuccess: () => {
        alert('칭호가 변경되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['UserInfo'] });
      },
      onError: () => {
        alert('칭호 변경에 실패했습니다. 다시 시도해주세요!');
      },
    });
  };
  return (
    <Wrapper>
      <BadgeListWrapper>
        {badgeList.map((badge) => {
          return (
            <BadgeWrapper
              key={badge.id}
              $isOwned={badge.isOwned}
              $isSelected={currentBadge === badge.id}
              onClick={() => handleChangeBadge(badge)}
            >
              <BadgeImage src={badge.imgUrl} alt={badge.name} />
              <Text size="xs" weight="normal">
                {badge.name}
              </Text>
            </BadgeWrapper>
          );
        })}
      </BadgeListWrapper>
      <StyledButton variant="mint" size="small" onClick={handleClickBadgeBtn}>
        변경
      </StyledButton>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: end;
  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 16px;
    align-items: center;
  }
`;

const BadgeListWrapper = styled.div`
  display: grid;
  gap: 14px;
  grid-template-columns: repeat(4, 1fr);
  justify-items: center;
  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 12px;
    grid-template-columns: repeat(2, 1fr);
  }
`;

const BadgeWrapper = styled.div<{ $isOwned: boolean; $isSelected: boolean }>`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
  border-radius: 6px;
  padding: 12px 16px;
  filter: ${({ $isOwned }) => (!$isOwned ? 'grayscale(100%) opacity(40%)' : 'none')};
  transition: filter 0.3s ease;
  border: ${({ $isSelected }) => ($isSelected ? '1px solid #55EBFF' : '1px solid transparent')};
  box-sizing: border-box;
  background-color: #1f1f1f;
  cursor: ${({ $isOwned }) => ($isOwned ? 'pointer' : 'default')};
`;
const BadgeImage = styled.img`
  width: 70%;
`;
const StyledButton = styled(Button)`
  width: 100px;
  @media screen and (max-width: 768px) {
    width: 90px;
  }
`;
