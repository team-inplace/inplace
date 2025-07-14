import styled from 'styled-components';
import { useState } from 'react';
import DropdownMenu, { DropdownMenuProps } from './DropdownMenu';

interface DropdownItem {
  type: 'dropdown';
  id: string;
  props: DropdownMenuProps;
}

interface SeparatorItem {
  type: 'separator';
  id: string;
}

export type FilterBarItem = DropdownItem | SeparatorItem;

interface DropdownFilterBarProps {
  items: FilterBarItem[];
}
export default function DropdownFilterBar({ items }: DropdownFilterBarProps) {
  const [isHover, setIsHover] = useState<boolean>(false);

  return (
    <MobileBarContainer onMouseEnter={() => setIsHover(true)} onMouseLeave={() => setIsHover(false)}>
      {items.map((item) =>
        item.type === 'dropdown' ? (
          <DropdownMenu key={item.id} {...item.props} />
        ) : (
          <Separator key={item.id} $isHidden={isHover} />
        ),
      )}
    </MobileBarContainer>
  );
}

const MobileBarContainer = styled.div`
  height: 38px;
  display: flex;
  align-items: center;
  border: 1px solid #a5a5a5;
  border-radius: 16px;
  background-color: white;
  box-sizing: border-box;

  svg {
    margin-right: 4px;
  }

  @media screen and (max-width: 768px) {
    height: 40px;
    width: 100%;
    border-radius: 0px;
    background: transparent;
    border: none;
    border-bottom: ${({ theme }) => (theme.backgroundColor === '#292929' ? '1px solid #a5a5a5' : '1px solid #8e8e8e')};
  }
`;

const Separator = styled.div<{ $isHidden?: boolean }>`
  width: 1px;
  height: 45%;
  background-color: ${({ $isHidden }) => ($isHidden ? 'white' : '#c7c7c7')};
  margin: auto 0px;
`;
