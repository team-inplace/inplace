import { useEffect, useMemo, useRef, useState } from 'react';
import { FaUser, FaSearch } from 'react-icons/fa';
import { ImSpoonKnife } from 'react-icons/im';
import { FaRankingStar } from 'react-icons/fa6';
import { RiMenu3Fill } from 'react-icons/ri';
import styled from 'styled-components';
import DropdownItem from './DropdownItem';
import useClickOutside from '@/hooks/useClickOutside';
import useIsMobile from '@/hooks/useIsMobile';

interface Option {
  label: string;
  id?: number;
  isMain?: boolean;
  mainId?: number;
}

export interface DropdownMenuProps {
  options: Option[];
  onChange: (value: { main: string; sub?: string; lat?: number; lng?: number; id?: number }) => void;
  isMobileOpen: boolean;
  placeholder?: string;
  type: 'influencer' | 'category';
  width: number;
  defaultValue?: string;
  selectedOptions?: string[] | number[];
}

export default function DropdownMenu({
  options,
  onChange,
  isMobileOpen = false,
  placeholder = '',
  type,
  width,
  defaultValue,
  selectedOptions,
}: DropdownMenuProps) {
  const isMobile = useIsMobile();
  const [isOpen, setIsOpen] = useState(isMobile && isMobileOpen);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const [selectedMainOption, setSelectedMainOption] = useState<Option | null>();
  const [searchTerm, setSearchTerm] = useState('');
  const isInitialized = useRef(false);
  const [selectedMainId, setSelectedMainId] = useState<number | null>(null);

  useClickOutside([dropdownRef], () => {
    setIsOpen(false);
  });

  useEffect(() => {
    if (isInitialized.current || !defaultValue || !options?.length) {
      return;
    }

    const mainOption = options.find((option) => option.label === defaultValue);
    if (!mainOption) {
      return;
    }
    setSelectedMainOption(mainOption);
    onChange({
      main: mainOption.label,
    });

    isInitialized.current = true;
  }, [defaultValue, options, onChange]);

  const processedOptions = useMemo(() => {
    return options.map((option) => {
      if (type === 'influencer' || option.isMain !== undefined) {
        return option;
      }
      return {
        ...option,
        isMain: option.mainId === undefined,
      };
    });
  }, [options, type]);

  const mainOptions = useMemo(() => {
    if (type === 'influencer') {
      return processedOptions;
    }
    return processedOptions.filter((option) => option.isMain === true);
  }, [processedOptions, type]);

  useEffect(() => {
    if (!searchTerm || type !== 'category') return;

    const matchingSubOptions = processedOptions.filter(
      (option) => !option.isMain && option.label.toLowerCase().includes(searchTerm.toLowerCase()),
    );

    // 검색 시 서브 옵션에서 일치할 때, 첫 번째로 매칭된 서브옵션의 메인 ID 설정
    if (matchingSubOptions.length > 0 && matchingSubOptions[0].mainId) {
      setSelectedMainId(matchingSubOptions[0].mainId);

      const matchingMainOption = mainOptions.find((option) => option.id === matchingSubOptions[0].mainId);
      if (matchingMainOption) {
        setSelectedMainOption(matchingMainOption);
      }
    }
  }, [searchTerm, processedOptions, type, mainOptions]);

  const filteredOptions = useMemo(() => {
    try {
      return (
        processedOptions?.filter((option) => {
          if (!searchTerm) return true;
          return option?.label?.toLowerCase().includes(searchTerm.toLowerCase());
        }) || []
      );
    } catch {
      return [];
    }
  }, [processedOptions, searchTerm]);

  const filteredMainOptions = useMemo(() => {
    if (type === 'influencer') {
      return filteredOptions;
    }
    return filteredOptions.filter((option) => option.isMain === true);
  }, [filteredOptions, type]);

  const filteredSubOptions = useMemo(() => {
    if (!selectedMainId || type !== 'category') return [];
    const isMainOptionSelected = mainOptions.some(
      (option) => option.id === selectedMainId && option.label.toLowerCase().includes(searchTerm.toLowerCase()),
    );

    if (isMainOptionSelected || !searchTerm) {
      return processedOptions.filter((option) => option.isMain === false && option.mainId === selectedMainId);
    }
    return filteredOptions.filter((option) => option.isMain === false && option.mainId === selectedMainId);
  }, [filteredOptions, processedOptions, selectedMainId, type, searchTerm, mainOptions]);

  const handleMainOptionClick = (option: Option) => {
    if (type === 'category' && option.isMain && option.id) {
      setSelectedMainId(option.id);
      setSelectedMainOption(option);
    } else {
      setSelectedMainOption(option);
      onChange({
        main: option.label,
        id: type === 'category' ? option.id : undefined,
      });
      setIsOpen(false);
      setSelectedMainOption(null);
      setSelectedMainId(null);
    }
  };

  const handleSubOptionClick = (option: Option) => {
    if (option.mainId && option.label === '전체') {
      const mainOption = mainOptions.find((main) => main.id === option.mainId);
      if (mainOption) {
        // 전체를 선택한 경우만 메인 카테고리 이름으로 표시
        onChange({
          main: mainOption.label,
          sub: option.label,
          id: type === 'category' ? mainOption.id : undefined,
        });
      } else {
        onChange({
          main: option.label,
          id: type === 'category' ? option.id : undefined,
        });
      }
    } else {
      // 다른 서브 카테고리는 그대로
      onChange({
        main: option.label,
        id: type === 'category' ? option.id : undefined,
      });
    }
    setIsOpen(false);
    setSelectedMainOption(null);
    setSelectedMainId(null);
  };

  const handleSearchInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
    if (event.target.value === '') {
      setSelectedMainId(null);
    }
  };

  const handleDropdownToggle = () => {
    if (isOpen) {
      setIsOpen(false);
      setSelectedMainOption(null);
      setSelectedMainId(null);
      setSearchTerm('');
    } else {
      setIsOpen(true);
      setSearchTerm('');
    }
  };

  const renderMainOptions = () => {
    const displayOptions = [...filteredMainOptions];

    if (searchTerm && selectedMainId && type === 'category') {
      const selectedMain = mainOptions.find((option) => option.id === selectedMainId);
      if (selectedMain && !displayOptions.some((opt) => opt.id === selectedMain.id)) {
        displayOptions.push(selectedMain);
      }
    }

    return displayOptions.map((option) => {
      const isFiltered =
        selectedOptions &&
        (type === 'category' && option.id
          ? (selectedOptions as number[]).includes(option.id)
          : (selectedOptions as string[]).includes(option.label));

      let icon = null;
      if (type === 'category') {
        if (option.label === '맛집' || option.id === 1) {
          icon = <ImSpoonKnife color="#ec7450" size={16} />;
        } else if (option.label === '놀거리' || option.id === 4) {
          icon = <FaRankingStar color="#3778ef" size={16} />;
        }
      }
      return (
        <DropdownItem
          key={option.label}
          label={option.label}
          onClick={() => handleMainOptionClick(option)}
          type={type}
          isSelected={selectedMainOption?.label === option.label}
          isFiltered={isFiltered}
          isMain
          icon={icon}
        />
      );
    });
  };

  const renderSubOptions = () => {
    if (!selectedMainId || type !== 'category') return null;

    return filteredSubOptions.map((option) => {
      const isFiltered =
        selectedOptions &&
        (type === 'category' && option.id
          ? (selectedOptions as number[]).includes(option.id)
          : (selectedOptions as string[]).includes(option.label));
      return (
        <DropdownItem
          key={option.label}
          label={option.label}
          onClick={() => handleSubOptionClick(option)}
          type={type}
          isSelected={false}
          isFiltered={isFiltered}
          isMain={false}
        />
      );
    });
  };

  const displayValue = placeholder;
  const displayIcon = () => {
    if (type === 'category') {
      return <RiMenu3Fill color="#5ae3fb" />;
    }
    return <FaUser color="#5ae3fb" />;
  };

  return (
    <DropdownContainer ref={dropdownRef} type={type} $width={width}>
      <DropdownButton aria-label={`${type}_필터링`} $isOpen={isOpen} onClick={handleDropdownToggle}>
        {displayIcon()}
        {displayValue}
      </DropdownButton>
      {isOpen && (
        <DropdownMenuContainer
          $type={type}
          $isCategory={type === 'category'}
          $hasSubOptions={!!selectedMainId && type === 'category'}
        >
          <SearchInputContainer>
            <SearchInput placeholder="검색" value={searchTerm} onChange={handleSearchInputChange} />
            <SearchIcon />
          </SearchInputContainer>
          <OptionsContainer>
            <MainOptionsColumn $fullWidth={!selectedMainId || type !== 'category'}>
              {renderMainOptions()}
            </MainOptionsColumn>
            {type === 'category' && selectedMainId && <SubOptionsColumn>{renderSubOptions()}</SubOptionsColumn>}
          </OptionsContainer>
        </DropdownMenuContainer>
      )}
    </DropdownContainer>
  );
}

const DropdownContainer = styled.div<{ type: 'influencer' | 'category'; $width: number }>`
  position: relative;
  height: 100%;
  width: ${({ $width }) => `${$width}px`};
  align-items: center;
  align-content: center;

  @media screen and (max-width: 768px) {
    width: 100%;
  }
`;

const DropdownButton = styled.button<{ $isOpen: boolean }>`
  width: 100%;
  height: 100%;
  border: none;
  background-color: ${({ $isOpen }) => ($isOpen ? '#e8f9ff' : '#ffffff')};
  font-weight: ${({ $isOpen }) => ($isOpen ? 'bold' : 'normal')};
  border-radius: 16px;
  display: flex;
  color: #333333;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 16px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  &:hover {
    background-color: #e8f9ff;
    border: none;
  }

  @media screen and (max-width: 768px) {
    padding: 0px 8px 6px 8px;
    font-size: 14px;
    border-radius: 0px;
    background-color: transparent;
    color: ${({ $isOpen, theme }) => {
      if ($isOpen && theme.backgroundColor === '#292929') return 'white';
      if ($isOpen && !(theme.backgroundColor === '#292929')) return 'black';
      return 'grey';
    }};

    &:hover {
      background-color: transparent;
    }
  }
`;

const DropdownMenuContainer = styled.div<{
  $type: 'influencer' | 'category';
  $isCategory: boolean;
  $hasSubOptions: boolean;
}>`
  position: absolute;
  top: 100%;
  ${({ $type }) => {
    if ($type === 'influencer') {
      return `
        right: 0;
      `;
    }
    return `
        left: 0;
      `;
  }}
  width: ${(props) => {
    if (props.$isCategory && props.$hasSubOptions) {
      return '200%';
    }
    return '150%';
  }};
  background: #ffffff;
  color: #333333;
  box-shadow: 0 3px 12px 0 rgb(0 0 0/0.15);
  padding: 2px;
  box-sizing: border-box;
  border-radius: 8px;
  margin-top: 4px;
  max-height: 300px;
  z-index: 101;

  @media screen and (max-width: 768px) {
    height: 100vh;
    width: 200%;
    background: transparent;
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
    margin-top: 2px;
    border-radius: 4px;
    overflow: hidden;
  }
`;

const SearchInputContainer = styled.div`
  position: relative;
  width: 100%;

  @media screen and (max-width: 768px) {
    padding: 2px 4px;
  }
`;

const SearchInput = styled.input`
  width: 100%;
  padding: 10px;
  border: none;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  outline: none;
  font-size: 16px;
  box-sizing: border-box;

  @media screen and (max-width: 768px) {
    font-size: 16px;
    background: transparent;
    border-bottom: ${({ theme }) =>
      theme.backgroundColor === '#292929' ? '1px solid rgba(255, 255, 255, 0.1);' : '1px solid rgba(0, 0, 0, 0.1);'};
    border-radius: 0px;
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? 'white' : 'black')};
  }
`;

const SearchIcon = styled(FaSearch)`
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #a3a3a3;
  cursor: pointer;

  @media screen and (max-width: 768px) {
    right: 12px;
    width: 14px;
    height: 14px;
    color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#d8d8d8' : '#7c7c7c')};
  }
`;

const OptionsContainer = styled.div`
  display: flex;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 85%;
  }
`;

const MainOptionsColumn = styled.div<{ $fullWidth?: boolean }>`
  width: ${(props) => (props.$fullWidth ? '100%' : '50%')};
  max-height: 250px;
  overflow-y: auto;
  border-right: ${(props) => (props.$fullWidth ? 'none' : '1px solid #eee')};

  @media screen and (max-width: 768px) {
    max-height: 100%;
    border-right: ${({ theme, $fullWidth }) => {
      if ($fullWidth) return 'none';
      if (theme.backgroundColor === '#292929') return '1px solid #444';
      return '1px solid #eee';
    }};
  }
`;

const SubOptionsColumn = styled.div`
  width: 50%;
  max-height: 250px;
  overflow-y: auto;

  @media screen and (max-width: 768px) {
    max-height: 100%;
  }
`;
