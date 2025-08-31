import styled from 'styled-components';
import { IoMdInformationCircleOutline } from 'react-icons/io';
import Button from '@/components/common/Button';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { Text } from '@/components/common/typography/Text';

export default function PermissionModal({ onClose }: { onClose: () => void }) {
  const handleClose = () => {
    console.log('PermissionModal closed');
    onClose();
  };

  const handleModalClick = (event: React.MouseEvent<HTMLDivElement>) => {
    event.stopPropagation();
  };

  return (
    <Overlay>
      <Wrapper onClick={handleModalClick}>
        <DescriptionSection>
          <Paragraph size="l" weight="bold" variant="#333333">
            인플레이스 권한 요청
          </Paragraph>
          <Paragraph size="xs" weight="normal" variant="#333333">
            더욱 편리한 서비스를 위해 아래 권한을 허용해 주세요!
          </Paragraph>
        </DescriptionSection>
        <PermissionSection>
          <PermissionItem>
            <IoMdInformationCircleOutline size={24} color="#333333" />
            <Paragraph size="m" weight="bold" variant="#333333">
              알림 권한
            </Paragraph>
          </PermissionItem>
          <Text size="xs" weight="normal" variant="#333333">
            새로운 소식과 알림을 받아볼 수 있어요.
          </Text>
          <PermissionItem>
            <IoMdInformationCircleOutline size={24} color="#333333" />
            <Paragraph size="m" weight="bold" variant="#333333">
              위치 권한
            </Paragraph>
          </PermissionItem>
          <Text size="xs" weight="normal" variant="#333333">
            주변 장소를 쉽게 찾고, 맞춤 추천을 받을 수 있어요.
          </Text>
        </PermissionSection>
        <BtnContainer>
          <Button aria-label="권한 허용 확인" variant="mint" size="small" onClick={handleClose}>
            확인
          </Button>
        </BtnContainer>
      </Wrapper>
    </Overlay>
  );
}
const Overlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
`;
const Wrapper = styled.div`
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  width: 400px;
  border-radius: 8px;
  background-color: white;
  padding: 60px 0px;
  border: 30px solid #e8f9ff;

  display: flex;
  flex-direction: column;
  text-align: center;
  align-items: center;
  gap: 36px;
`;

const DescriptionSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 14px;
`;

const BtnContainer = styled.div`
  display: flex;
  justify-content: center;
  width: 40%;
`;
const PermissionSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 12px;
  width: 80%;
`;
const PermissionItem = styled.div`
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 14px;
`;
