import styled from 'styled-components';
import { AiFillMessage } from 'react-icons/ai';
import { RiErrorWarningFill } from 'react-icons/ri';
import useTheme from '@/hooks/useTheme';
import { Text } from '@/components/common/typography/Text';
import { AlarmData } from '@/types';

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

export default function AlarmItem({ content, checked, type, createdAt }: AlarmData) {
  const { theme } = useTheme();
  const isDarkMode = theme === 'dark';

  const renderIcon = () => {
    const icon = getIcon(type);
    if (icon) {
      return (
        <IconContainer>
          {icon}
          {!checked && <RedDot />}
        </IconContainer>
      );
    }
    return !checked ? <RedDot /> : null;
  };

  const renderTitle = () => {
    const title = getTitle(type);
    return title ? (
      <StyledText size="xxs" weight="bold">
        {title}
      </StyledText>
    ) : null;
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
