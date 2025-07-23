import styled from 'styled-components';
import { AiFillMessage } from 'react-icons/ai';
import { RiErrorWarningFill } from 'react-icons/ri';
import useTheme from '@/hooks/useTheme';
import { Text } from '@/components/common/typography/Text';
import { AlarmData } from '@/types';

export default function AlarmItem({ content, checked, type, createdAt }: AlarmData) {
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  const renderIcon = () => {
    if (type === 'MENTION') {
      return (
        <IconContainer>
          <AiFillMessage size={20} />
          {!checked && <RedDot />}
        </IconContainer>
      );
    }
    if (type === 'REPORT') {
      return (
        <IconContainer>
          <RiErrorWarningFill size={20} />
          {!checked && <RedDot />}
        </IconContainer>
      );
    }
    return !checked ? <RedDot /> : null;
  };

  const renderTitle = () => {
    if (type === 'MENTION') {
      return (
        <StyledText size="xxs" weight="bold">
          [멘션]
        </StyledText>
      );
    }
    if (type === 'REPORT') {
      return (
        <StyledText size="xxs" weight="bold">
          [신고]
        </StyledText>
      );
    }
    return null;
  };

  return (
    <AlarmContainer $isDarkMode={isDarkMode}>
      {renderIcon()}
      <StyledText size="xxs" weight="normal">
        {renderTitle()}&nbsp;&nbsp;{content}{' '}
        <CreatedAtText size="12px" weight="normal">
          {createdAt}
        </CreatedAtText>
      </StyledText>
    </AlarmContainer>
  );
}

const AlarmContainer = styled.div<{ $isDarkMode: boolean }>`
  position: relative;
  display: flex;
  align-items: flex-start;
  padding: 12px 10px;
  background-color: ${(props) => (props.$isDarkMode ? '#1a1a1a' : '#ffffff')};
  cursor: pointer;
  gap: 8px;

  &:hover {
    background-color: ${(props) => (props.$isDarkMode ? '#2a2a2a' : '#f0f2f4')};
  }

  &:last-child {
    border-bottom: none;
  }
`;

const StyledText = styled(Text)`
  line-height: 1.4;
  flex: 1;
`;

const CreatedAtText = styled(Text)`
  color: #b0b0b0;
  white-space: nowrap;
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
