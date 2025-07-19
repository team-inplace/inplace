import { useRef, useState } from 'react';
import styled from 'styled-components';
import Button from '../Button';
import { Text } from '../typography/Text';
import useClickOutside from '@/hooks/useClickOutside';

interface ReportModalProps {
  onClose: () => void;
  onSubmit: (type: string, content: string) => void;
}

const REPORT_TYPES = [
  '허위 정보를 기재하였습니다.',
  '부적절한 사진입니다.',
  '부적절한 내용입니다.',
  '도배 및 광고성 글입니다.',
  '기타',
];

export default function ReportModal({ onClose, onSubmit }: ReportModalProps) {
  const [selectedType, setSelectedType] = useState('');
  const [content, setContent] = useState('');
  const [showDropdown, setShowDropdown] = useState(false);

  const dropdownRef = useRef<HTMLDivElement>(null);

  useClickOutside([dropdownRef], () => {
    setShowDropdown(false);
  });

  return (
    <Overlay onClick={onClose}>
      <Modal onClick={(e) => e.stopPropagation()}>
        <StyledText size="m" weight="bold" variant="black">
          신고하기
        </StyledText>
        <ReportSection>
          <Text size="s" weight="normal" variant="black">
            신고 유형
          </Text>
          <DropdownWrapper ref={dropdownRef}>
            <DropdownButton onClick={() => setShowDropdown((prev) => !prev)}>
              {selectedType || '신고 유형을 선택하세요'}
              <span>▾</span>
            </DropdownButton>
            {showDropdown && (
              <DropdownList>
                {REPORT_TYPES.map((type) => (
                  <DropdownItem
                    key={type}
                    onClick={() => {
                      setSelectedType(type);
                      setShowDropdown(false);
                    }}
                  >
                    {type}
                  </DropdownItem>
                ))}
              </DropdownList>
            )}
          </DropdownWrapper>
          <Text size="s" weight="normal" variant="black">
            신고 내용 (선택)
          </Text>
          <textarea
            placeholder="예) 부적절한 사진입니다. 혐오 내용이 포함되어 있습니다."
            value={content}
            onChange={(e) => setContent(e.target.value)}
          />
        </ReportSection>
        <ReportSection>
          <Text size="xxs" weight="normal" variant="grey">
            ※ 허위 신고 시 이용 제한 등 불이익을 받을 수 있습니다. <br />
            ※ 신고 내용은 운영팀에서 검토 후 처리되며, 사실 확인에 시간이 소요될 수 있습니다. <br />
          </Text>
        </ReportSection>
        <ModalButtons>
          <Button size="small" variant="blackOutline" onClick={onClose}>
            취소
          </Button>
          <Button
            size="small"
            variant="black"
            onClick={() => {
              if (!selectedType) {
                alert('신고 유형을 선택해주세요.');
                return;
              }
              onSubmit(selectedType, content);
              onClose();
            }}
          >
            제출
          </Button>
        </ModalButtons>
      </Modal>
    </Overlay>
  );
}

const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
`;

const Modal = styled.div`
  background: white;
  padding: 40px 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 400px;
  display: flex;
  flex-direction: column;
  gap: 30px;
`;
const StyledText = styled(Text)`
  text-align: center;
`;
const ReportSection = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
  textarea {
    width: 100%;
    padding: 8px;
    font-size: 14px;
    box-sizing: border-box;
    min-height: 150px;
    resize: vertical;
  }
  span {
    line-height: 1.3;
  }
`;
const DropdownWrapper = styled.div`
  position: relative;
`;

const DropdownButton = styled.button`
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  color: black;
  border-radius: 6px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
`;

const DropdownList = styled.ul`
  position: absolute;
  top: calc(100% + 4px);
  width: 100%;
  background: white;
  border: 1px solid #ddd;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  z-index: 1000;
`;

const DropdownItem = styled.li`
  padding: 12px;
  font-size: 14px;
  cursor: pointer;
  color: black;
  &:hover {
    background-color: #f3f3f3;
  }
`;

const ModalButtons = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 10px;
`;
