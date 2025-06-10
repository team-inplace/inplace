import styled from 'styled-components';
import { RxDotsVertical } from 'react-icons/rx';
import { useRef, useState } from 'react';
import useClickOutside from '@/hooks/useClickOutside';

interface EditMenuProps {
  mine?: boolean;
  onEdit?: () => void;
  onDelete?: () => void;
  onReport?: () => void;
  menuItems?: Array<{ label: string; onClick: () => void }>;
  ariaLabels: string;
}

export default function EditMenu({ mine, onEdit, onDelete, onReport, menuItems, ariaLabels }: EditMenuProps) {
  const [showEditOptions, setShowEditOptions] = useState(false);
  const editRef = useRef<HTMLDivElement>(null);

  useClickOutside([editRef], () => setShowEditOptions(false));
  const handleMenuClick = (callback?: () => void) => () => {
    setShowEditOptions(false);
    if (callback) callback();
  };

  return (
    <EditMenuWrapper ref={editRef}>
      <EditBtn aria-label={`${ariaLabels} 편집 드롭다운 버튼`} onClick={() => setShowEditOptions((v) => !v)}>
        <RxDotsVertical size={22} />
      </EditBtn>
      {showEditOptions && (
        <EditDropdown>
          {menuItems ? (
            menuItems.map((item) => (
              <EditItem key={item.label} onClick={handleMenuClick(item.onClick)}>
                {item.label}
              </EditItem>
            ))
          ) : (
            <>
              {mine && onEdit && (
                <EditItem aria-label={`${ariaLabels} 수정 버튼`} onClick={handleMenuClick(onEdit)}>
                  수정
                </EditItem>
              )}
              {mine && onDelete && (
                <EditItem aria-label={`${ariaLabels} 삭제 버튼`} onClick={handleMenuClick(onDelete)}>
                  삭제
                </EditItem>
              )}
              {onReport && (
                <EditItem aria-label={`${ariaLabels} 신고 버튼`} onClick={handleMenuClick(onReport)}>
                  신고
                </EditItem>
              )}
            </>
          )}
        </EditDropdown>
      )}
    </EditMenuWrapper>
  );
}

const EditMenuWrapper = styled.div`
  position: relative;
  height: fit-content;
`;

const EditBtn = styled.button`
  background: none;
  border: none;
  cursor: pointer;

  svg {
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  }
`;
const EditDropdown = styled.div`
  position: absolute;
  top: 100%;
  right: 10px;
  z-index: 2;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  width: 90px;
`;
const EditItem = styled.button`
  width: 100%;
  padding: 10px 12px;
  display: flex;
  align-items: center;
  background: none;
  border: none;
  cursor: pointer;

  &:hover {
    background-color: #e9e9e9;
    border-radius: 4px;
  }
`;
