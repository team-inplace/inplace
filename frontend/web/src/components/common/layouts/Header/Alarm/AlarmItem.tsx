import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQueryClient } from '@tanstack/react-query';
import styled from 'styled-components';
import { AiFillMessage } from 'react-icons/ai';
import { IoClose } from 'react-icons/io5';
import { RiErrorWarningFill } from 'react-icons/ri';
import { Text } from '@/components/common/typography/Text';
import { AlarmData } from '@/types';
import { usePostAlarmCheck } from '@/api/hooks/usePostAlarmCheck';
import { useDeleteAlarm } from '@/api/hooks/useDeleteAlarm';

const getIcon = (type: string) => {
  switch (type) {
    case 'MENTION':
      return <AiFillMessage size={20} />;
    case 'REPORT':
      return <RiErrorWarningFill size={20} />;
    default:
      return null;
  }
};

const getTitle = (type: string) => {
  switch (type) {
    case 'MENTION':
      return '[멘션]';
    case 'REPORT':
      return '[신고]';
    default:
      return null;
  }
};

export default function AlarmItem({
  alarmId,
  postId,
  commentId,
  content,
  checked,
  type,
  createdAt,
  commentPage,
}: AlarmData) {
  const { mutate: postAlarmCheck } = usePostAlarmCheck();
  const { mutate: deleteAlarm } = useDeleteAlarm();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [isChecked, setIsChecked] = useState(checked);

  useEffect(() => {
    setIsChecked(checked);
  }, [checked]);

  const renderIcon = () => {
    const icon = getIcon(type);
    if (icon) {
      return (
        <IconContainer>
          {icon}
          {!isChecked && <RedDot />}
        </IconContainer>
      );
    }
    return !isChecked ? <RedDot /> : null;
  };

  const renderTitle = () => {
    const title = getTitle(type);
    return title ? (
      <StyledText size="xxs" weight="bold">
        {title}
      </StyledText>
    ) : null;
  };

  const handleAlarmClick = () => {
    if (!isChecked) {
      if (type === 'MENTION' && commentPage !== null) {
        navigate(`/post/${postId}?commentPage=${commentPage}&commentId=${commentId}`);
      }
      postAlarmCheck(
        { alarmId },
        {
          onSuccess: () => {
            setIsChecked(true);
            queryClient.invalidateQueries({ queryKey: ['alarms'] });
          },
          onError: () => {
            alert('알림 읽음 처리에 실패했습니다. 다시 시도해주세요.');
          },
        },
      );
    }
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation();
    const isConfirm = window.confirm('알림을 삭제하시겠습니까?');
    if (!isConfirm) return;

    deleteAlarm(alarmId.toString(), {
      onSuccess: () => {
        alert('알림이 삭제되었습니다.');
        queryClient.invalidateQueries({ queryKey: ['alarms'] });
      },
      onError: () => {
        alert('알림 삭제에 실패했습니다. 다시 시도해주세요.');
      },
    });
  };

  return (
    <AlarmContainer onClick={handleAlarmClick}>
      {renderIcon()}
      <StyledText size="xxs" weight="normal">
        {renderTitle()}&nbsp;&nbsp;{content}{' '}
        <CreatedAtText size="12px" weight="normal">
          {createdAt}
        </CreatedAtText>
      </StyledText>
      <ButtonWrapper>
        <DeleteButton onClick={handleDelete}>
          <IoClose size={16} />
        </DeleteButton>
      </ButtonWrapper>
    </AlarmContainer>
  );
}

const AlarmContainer = styled.div`
  position: relative;
  display: flex;
  align-items: flex-start;
  padding: 12px 20px 12px 10px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#1a1a1a' : '#ffffff')};
  cursor: pointer;
  gap: 8px;

  &:last-child {
    border-bottom: none;
  }

  @media screen and (max-width: 768px) {
    padding: 16px 8px;
  }
`;

const StyledText = styled(Text)`
  line-height: 1.4;
  flex: 1;
`;

const CreatedAtText = styled(Text)`
  color: #b0b0b0;
  white-space: nowrap;

  @media screen and (max-width: 768px) {
    font-size: 12px;
  }
`;

const IconContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 2px;
`;

const RedDot = styled.div`
  position: absolute;
  top: 0px;
  left: 1px;
  width: 6px;
  height: 6px;
  background-color: #ff4747;
  border-radius: 50%;
`;

const ButtonWrapper = styled.div`
  position: absolute;
  top: 12px;
  right: -6px;
`;

const DeleteButton = styled.button`
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: flex-end;

  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#b0b0b0' : '#666666')};
  }
`;
