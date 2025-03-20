import styled from 'styled-components';
import { IoClose } from 'react-icons/io5';
import { Text } from '@/components/common/typography/Text';

type SelectedOption = {
  main: string;
  sub?: string;
  lat?: number;
  lng?: number;
};

type Props = {
  selectedLocations: SelectedOption[];
  selectedInfluencers: string[];
  selectedCategories: string[];
  onClearLocation: (option: SelectedOption) => void;
  onClearInfluencer: (influencer: string) => void;
  onClearCategory: (category: string) => void;
};

export default function Chip({
  selectedLocations,
  selectedInfluencers,
  selectedCategories,
  onClearLocation,
  onClearInfluencer,
  onClearCategory,
}: Props) {
  return (
    <Container>
      {selectedLocations.map((location) => (
        <FilterChip key={`${location.main}-${location.sub}`}>
          <Text size="xs" weight="bold" variant="#36617f">
            {location.sub ? `${location.main} > ${location.sub}` : location.main}
          </Text>
          <ClearButton aria-label="loca-clear_btn" onClick={() => onClearLocation(location)}>
            <IoClose size={14} />
          </ClearButton>
        </FilterChip>
      ))}

      {selectedInfluencers.map((influencer) => (
        <FilterChip key={influencer}>
          <Text size="xs" weight="bold" variant="#36617f">
            {influencer}
          </Text>
          <ClearButton aria-label="infl-clear_btn" onClick={() => onClearInfluencer(influencer)}>
            <IoClose size={14} />
          </ClearButton>
        </FilterChip>
      ))}

      {selectedCategories.map((category) => (
        <FilterChip key={category}>
          <Text size="xs" weight="bold" variant="#36617f">
            {category}
          </Text>
          <ClearButton aria-label="cate-clear_btn" onClick={() => onClearCategory(category)}>
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
  margin: 16px 0;
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
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#e8f9ff' : '#d2edf2')};
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
