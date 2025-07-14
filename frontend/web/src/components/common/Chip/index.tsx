import styled from 'styled-components';
import { IoClose } from 'react-icons/io5';
import { Text } from '@/components/common/typography/Text';

type Props = {
  selectedInfluencers: string[];
  selectedCategories: { id: number; label: string }[];
  onClearInfluencer: (influencer: string) => void;
  onClearCategory: (category: number) => void;
};

export default function Chip({ selectedInfluencers, selectedCategories, onClearInfluencer, onClearCategory }: Props) {
  return (
    <Container>
      {selectedInfluencers.map((influencer) => (
        <FilterChip key={influencer}>
          <Text size="xxs" weight="normal" variant="#36617f">
            {influencer}
          </Text>
          <ClearButton aria-label="인플루언서 칩 제거" onClick={() => onClearInfluencer(influencer)}>
            <IoClose size={14} />
          </ClearButton>
        </FilterChip>
      ))}

      {selectedCategories.map((category) => (
        <FilterChip key={category.id}>
          <Text size="xxs" weight="normal" variant="#36617f">
            {category.label}
          </Text>
          <ClearButton aria-label="카테고리 칩 제거" onClick={() => onClearCategory(category.id)}>
            <IoClose size={14} />
          </ClearButton>
        </FilterChip>
      ))}
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin: 12px 0;
  @media screen and (max-width: 768px) {
    padding: 0;
    gap: 8px;
    margin: 14px 0;
    flex-wrap: nowrap;
    overflow-x: auto;
    scroll-snap-type: x mandatory;
    scroll-behavior: smooth;
    scrollbar-width: none;
    -ms-overflow-style: none;
    &::-webkit-scrollbar {
      display: none;
    }

    & > * {
      flex-shrink: 0;
    }
  }
  x & > * {
    flex-shrink: 0;
  }
`;

const FilterChip = styled.div`
  display: flex;
  align-items: center;
  justify-content: 'space-between';
  padding: 4px 10px 4px 18px;
  height: 24px;
  border-radius: 18px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#e8f9ff' : '#daeeee')};
  color: black;
  @media screen and (max-width: 768px) {
    padding: 4px 10px 4px 14px;
    font-size: 14px;
    height: 20px;
  }
`;

const ClearButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 2px;
  padding: 2px;
  border-radius: 50%;
  cursor: pointer;
  background: none;
  border: none;
  color: inherit;

  &:hover {
    background-color: rgba(0, 0, 0, 0.05);
  }
`;
