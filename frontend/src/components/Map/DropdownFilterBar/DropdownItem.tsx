import styled from 'styled-components';

interface DropdownItemProps {
  label: string;
  onClick: () => void;
  type: 'location' | 'influencer' | 'category';
  isSelected?: boolean;
  isFiltered?: boolean;
  isMain?: boolean;
  icon?: React.ReactNode;
  children?: React.ReactNode;
}

export default function DropdownItem({
  label,
  onClick,
  children,
  type,
  isSelected,
  isFiltered,
  isMain,
  icon,
}: DropdownItemProps) {
  return (
    <DropdownItems onClick={onClick} type={type} $isSelected={isSelected} $isFiltered={isFiltered} $isMain={isMain}>
      {icon && <IconContainer>{icon}</IconContainer>}
      {label}
      {children}
    </DropdownItems>
  );
}

const DropdownItems = styled.div<{
  type: 'location' | 'influencer' | 'category';
  $isSelected?: boolean;
  $isFiltered?: boolean;
  $isMain?: boolean;
}>`
  padding: 10px 16px;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  background-color: ${(props) => {
    if (props.$isFiltered) return '#e8f9ff';
    if (props.$isSelected) return '#e6f0ff';
    return 'transparent';
  }};

  &:hover {
    background-color: ${($isSelected) => ($isSelected ? '#e8f9ff' : '#f0f0f0')};
  }

  @media screen and (max-width: 768px) {
    padding: 12px 12px;
    font-size: 14px;
    background-color: ${({ $isSelected, $isFiltered, theme }) => {
      if ($isFiltered && theme.backgroundColor === '#292929') return '#1d1d1d';
      if ($isSelected && theme.backgroundColor === '#292929') return '#1d1d1d';
      return null;
    }};

    &:hover {
      background-color: ${({ theme }) => {
        if (theme.backgroundColor === '#292929') return '#1d1d1d';
        return null;
      }};
    }
  }
`;

const IconContainer = styled.span`
  display: inline-flex;
  margin-right: 8px;
  align-items: center;
`;
