import { useState } from 'react';
import { FaCheck } from 'react-icons/fa';
import styled from 'styled-components';

const categoryMapping = {
  RESTAURANT: '음식점',
  CAFE: '카페',
  JAPANESE: '일식',
  KOREAN: '한식',
  WESTERN: '양식',
} as const;

type BackendCategory = keyof typeof categoryMapping;

type ToggleButtonProps = {
  options: BackendCategory[];
  onSelect: (selected: BackendCategory[]) => void;
};

export default function ToggleButton({ options, onSelect }: ToggleButtonProps) {
  const [selectedOptions, setSelectedOptions] = useState<BackendCategory[]>([]);

  const handleClick = (option: BackendCategory) => {
    let newSelectedOptions: BackendCategory[];
    if (selectedOptions.includes(option)) {
      newSelectedOptions = selectedOptions.filter((item) => item !== option);
    } else {
      newSelectedOptions = [...selectedOptions, option];
    }
    setSelectedOptions(newSelectedOptions);
    onSelect(newSelectedOptions);
  };

  return (
    <ToggleButtonContainer>
      {options.map((option) => (
        <Button
          key={option}
          aria-label="check_btn"
          $isActive={selectedOptions.includes(option)}
          onClick={() => handleClick(option)}
        >
          <ButtonText $isActive={selectedOptions.includes(option)}>{categoryMapping[option]}</ButtonText>
          {selectedOptions.includes(option) && (
            <CheckIconWrapper>
              <FaCheck color="red" size={10} />
            </CheckIconWrapper>
          )}
        </Button>
      ))}
    </ToggleButtonContainer>
  );
}

const ToggleButtonContainer = styled.div`
  display: flex;
  gap: 20px;
  margin: 14px 0;

  @media screen and (max-width: 768px) {
    width: 90%;
    gap: 10px;
    flex-wrap: wrap;
  }
`;

const Button = styled.button<{ $isActive: boolean }>`
  position: relative;
  padding: 0 48px;
  height: 40px;
  border: none;
  background: ${(props) => (props.$isActive ? '#DBFBFF' : '#F9F9F9')};
  border-radius: 18px;
  cursor: pointer;
  transition: background-color 0.2s;
  white-space: nowrap;
  &:hover {
    background: ${(props) => (props.$isActive ? '#DBFBFF' : '#F0F0F0')};
  }

  @media screen and (max-width: 768px) {
    padding: 0 44px;
    height: 34px;
  }
`;

const ButtonText = styled.span<{ $isActive: boolean }>`
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  font-weight: 700;
  font-size: 16px;
  color: ${(props) => (props.$isActive ? '#004BFE' : '#202020')};
  ${(props) => (props.$isActive ? 'left: 20%; right: 36%;' : 'left: 0px; right: 0px; text-align: center;')}

  @media screen and (max-width: 768px) {
    font-size: 14px;
  }
`;

const CheckIconWrapper = styled.div`
  position: absolute;
  width: 16px;
  height: 16px;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: #004cff;
  border: 2px solid #ffffff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  @media screen and (max-width: 768px) {
    width: 14px;
    height: 14px;
    svg {
      width: 8px;
      height: 8px;
    }
  }
`;
