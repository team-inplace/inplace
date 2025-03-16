import { useEffect, useMemo, useRef, useState } from 'react';
import { FaSearch } from 'react-icons/fa';
import { IoIosArrowUp, IoIosArrowDown } from 'react-icons/io';
import styled from 'styled-components';
import DropdownItem from './DropdownItem';
import useDetectClose from '@/hooks/useDetectClose';

interface Option {
  label: string;
  lat?: number;
  lng?: number;
  subOptions?: Option[];
}

interface DropdownMenuProps {
  options: Option[];
  multiLevel?: boolean;
  onChange: (value: { main: string; sub?: string; lat?: number; lng?: number }) => void;
  placeholder?: string;
  type: 'location' | 'influencer' | 'category';
  defaultValue?: { main: string; sub?: string };
  selectedOptions?: string[] | { main: string; sub?: string }[];
}

export default function DropdownMenu({
  options,
  multiLevel = false,
  onChange,
  placeholder = '',
  type,
  defaultValue,
  selectedOptions,
}: DropdownMenuProps) {
  const [isOpen, setIsOpen] = useState(false);
  const ref = useDetectClose({ onDetected: () => setIsOpen(false) });
  const [selectedMainOption, setSelectedMainOption] = useState<Option | null>();
  const [selectedSubOption, setSelectedSubOption] = useState<Option | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const isInitialized = useRef(false);

  useEffect(() => {
    if (isInitialized.current || !defaultValue?.main || !options?.length) {
      return;
    }

    const mainOption = options.find((option) => option.label === defaultValue.main);
    if (!mainOption) {
      return;
    }
    const updates = () => {
      setSelectedMainOption(mainOption);
      if (defaultValue.sub && mainOption.subOptions) {
        const subOption = mainOption.subOptions.find((sub) => sub.label === defaultValue.sub);
        if (subOption) {
          setSelectedSubOption(subOption);
        }
      }

      onChange({
        main: mainOption.label,
        sub: undefined,
        lat: mainOption.lat,
        lng: mainOption.lng,
      });
    };

    updates();
    isInitialized.current = true;
  }, [defaultValue, options, onChange]);

  const filteredOptions = useMemo(() => {
    try {
      return (
        options?.filter((option) => {
          const mainMatch = option?.label?.toLowerCase().includes(searchTerm.toLowerCase());

          const hasMatchingSubOption = option?.subOptions?.some((subOption) =>
            subOption?.label?.toLowerCase().includes(searchTerm.toLowerCase()),
          );
          return mainMatch || hasMatchingSubOption;
        }) || []
      );
    } catch {
      return [];
    }
  }, [options, searchTerm]);

  useEffect(() => {
    if (!searchTerm || !options?.length) return;
    const mainOptionWithSubMatch = options.find((option) =>
      option?.subOptions?.some?.((subOption) => subOption?.label?.toLowerCase().includes(searchTerm.toLowerCase())),
    );

    if (mainOptionWithSubMatch) {
      setSelectedMainOption(mainOptionWithSubMatch);
    }
  }, [searchTerm, options]);

  const handleMainOptionClick = (option: Option) => {
    setSelectedMainOption(option);
    setSelectedSubOption(null);
    if (!multiLevel || !option.subOptions) {
      onChange({
        main: option.label,
        sub: undefined,
        lat: option.lat,
        lng: option.lng,
      });
      setIsOpen(false);
      setSelectedMainOption(null);
    }
  };

  const handleSubOptionClick = (subOption: Option) => {
    setSelectedSubOption(subOption);
    onChange({
      main: selectedMainOption!.label,
      sub: subOption.label === '전체' ? undefined : subOption.label,
      lat: subOption.lat,
      lng: subOption.lng,
    });
    setIsOpen(false);
    setSelectedMainOption(null);
    setSelectedSubOption(null);
  };

  const handleSearchInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const renderMainOptions = () => {
    return filteredOptions.map((option) => {
      const isFiltered =
        type === 'location'
          ? (selectedOptions as { main: string; sub?: string }[])?.some((item) => item.main === option.label)
          : (selectedOptions as string[])?.includes(option.label);

      return (
        <DropdownItem
          key={option.label}
          label={option.label}
          onClick={() => handleMainOptionClick(option)}
          type={type}
          isSelected={selectedMainOption === option}
          isFiltered={isFiltered}
        />
      );
    });
  };

  const renderSubOptions = () => {
    if (!selectedMainOption || !selectedMainOption.subOptions) return null;
    const allOption: Option = {
      label: '전체',
      lat: selectedMainOption.lat,
      lng: selectedMainOption.lng,
    };
    const filteredSubOptions = [allOption, ...selectedMainOption.subOptions].filter(
      (subOption) => !searchTerm || subOption.label.toLowerCase().includes(searchTerm.toLowerCase()),
    );

    return filteredSubOptions.map((subOption) => {
      const isFiltered = (selectedOptions as { main: string; sub?: string }[])?.some(
        (item) =>
          item.main === selectedMainOption.label &&
          (subOption.label === '전체' ? !item.sub : item.sub === subOption.label),
      );
      return (
        <DropdownItem
          key={subOption.label}
          label={subOption.label}
          onClick={() => handleSubOptionClick(subOption)}
          type={type}
          isSelected={selectedSubOption === subOption}
          isFiltered={isFiltered}
        />
      );
    });
  };

  const displayValue = placeholder;

  return (
    <DropdownContainer ref={ref} type={type}>
      <DropdownButton aria-label="dropdown_btn" $isOpen={isOpen} onClick={() => setIsOpen(!isOpen)}>
        {displayValue}
        {isOpen ? <IoIosArrowUp /> : <IoIosArrowDown />}
      </DropdownButton>
      {isOpen && (
        <DropdownMenuContainer $multiLevel={multiLevel} $hasSubOptions={!!selectedMainOption?.subOptions}>
          <SearchInputContainer>
            <SearchInput placeholder="검색" value={searchTerm} onChange={handleSearchInputChange} />
            <SearchIcon />
          </SearchInputContainer>
          <OptionsContainer>
            <MainOptions>{renderMainOptions()}</MainOptions>
            {multiLevel && selectedMainOption?.subOptions && <SubOptions>{renderSubOptions()}</SubOptions>}
          </OptionsContainer>
        </DropdownMenuContainer>
      )}
    </DropdownContainer>
  );
}

const DropdownContainer = styled.div<{ type: 'location' | 'influencer' | 'category' }>`
  position: relative;
  min-width: ${(props) => (props.type === 'influencer' ? '170px' : '130px')};
  max-width: ${(props) => (props.type === 'influencer' ? '300px' : '400px')};

  @media screen and (max-width: 768px) {
    min-width: ${(props) => (props.type === 'influencer' ? '130px' : '90px')};
    max-width: ${(props) => (props.type === 'influencer' ? '240px' : '200px')};
  }
`;

const DropdownButton = styled.button<{ $isOpen: boolean }>`
  width: 100%;
  padding: 8px 10px;
  background: #ffffff;
  border: 2px solid rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  font-weight: 700;
  font-size: 16px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#004cff' : '#3b63c3')};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  &:hover {
    background-color: #f8f8f8;
  }

  @media screen and (max-width: 768px) {
    padding: 6px 8px;
    font-size: 14px;
  }
`;

const DropdownMenuContainer = styled.div<{ $multiLevel: boolean; $hasSubOptions: boolean }>`
  position: absolute;
  top: 100%;
  left: 0;
  width: ${(props) => (props.$multiLevel && props.$hasSubOptions ? '200%' : '100%')};
  background: #ffffff;
  color: black;
  border: 2px solid rgba(0, 0, 0, 0.1);
  box-sizing: border-box;
  border-radius: 8px;
  margin-top: 4px;
  max-height: 300px;
  z-index: 101;

  @media screen and (max-width: 768px) {
    max-height: 200px;
    margin-top: 2px;
    border-radius: 4px;
    overflow: hidden;
  }
`;

const SearchInputContainer = styled.div`
  position: relative;
  width: 100%;

  @media screen and (max-width: 768px) {
    padding: 4px;
  }
`;

const SearchInput = styled.input`
  width: 100%;
  padding: 10px;
  padding-right: 30px;
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  outline: none;
  font-size: 16px;
  box-sizing: border-box;

  @media screen and (max-width: 768px) {
    padding: 6px;
    padding-right: 24px;
    font-size: 16px;
  }
`;

const SearchIcon = styled(FaSearch)`
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#004cff' : '#3b63c3')};
  cursor: pointer;

  @media screen and (max-width: 768px) {
    right: 12px;
    width: 12px;
    height: 12px;
  }
`;

const OptionsContainer = styled.div`
  display: flex;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;

const MainOptions = styled.div`
  flex: 1;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;

const SubOptions = styled.div`
  flex: 1;
  border-left: 1px solid rgba(0, 0, 0, 0.1);
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;
