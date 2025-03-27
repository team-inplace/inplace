import { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import styled from 'styled-components';
import { IoIosClose, IoIosSearch, IoIosArrowUp, IoIosArrowDown } from 'react-icons/io';
import { useGetSearchComplete } from '@/api/hooks/useGetSearchComplete';
import useDebounce from '@/hooks/useDebounce';
import { useGetSearchKakaoKeyword } from '@/hooks/api/useGetSearchKakaoKeyword';

interface KakaoKeywordDocuments {
  place_name: string;
  x: string;
  y: string;
}
interface MapSearchBarProps {
  setCenter: React.Dispatch<React.SetStateAction<{ lat: number; lng: number }>>;
  setSelectedPlaceName: React.Dispatch<React.SetStateAction<string>>;
}
interface DropdownItem {
  id: string;
  text: string;
}

export default function MapSearchBar({ setCenter, setSelectedPlaceName }: MapSearchBarProps) {
  const DEBOUNCE_DELAY_MS = 300;
  const MAX_LOCATION_RESULTS = 5;

  const [inputValue, setInputValue] = useState('');
  const [searchType, setSearchType] = useState<'location' | 'place'>('location');
  const [preventDropdownOpen, setPreventDropdownOpen] = useState(false);
  const [itemIndex, setItemIndex] = useState(-1);
  const [isOpen, setIsOpen] = useState(false);
  const [isTypeOpen, setIsTypeOpen] = useState(false);
  const debouncedInput = useDebounce(inputValue, DEBOUNCE_DELAY_MS);

  const searchBarRef = useRef<HTMLDivElement>(null);
  const searchTypeRef = useRef<HTMLDivElement>(null);

  const { data: searchPlaceResults } = useGetSearchComplete(
    debouncedInput,
    'place',
    searchType === 'place' && !!debouncedInput,
  );
  const { data: searchLocationResults } = useGetSearchKakaoKeyword(
    debouncedInput,
    searchType === 'location' && !!debouncedInput,
  );

  const dropDownList: DropdownItem[] = useMemo(() => {
    if (searchType === 'place' && searchPlaceResults) {
      return searchPlaceResults.map((item, index) => ({
        id: `place_${index}`,
        text: item.result,
      }));
    }

    if (searchType === 'location' && searchLocationResults) {
      return searchLocationResults.documents
        .slice(0, MAX_LOCATION_RESULTS)
        .map((item: KakaoKeywordDocuments, index: number) => ({
          id: `location_${index}`,
          text: item.place_name,
        }));
    }

    return [];
  }, [searchPlaceResults, searchLocationResults, searchType]);

  // 외부 클릭 처리
  const handleClickOutside = useCallback((event: MouseEvent) => {
    if (searchTypeRef.current?.contains(event.target as Node)) {
      setIsOpen(false);
      return;
    }

    if (searchBarRef.current?.contains(event.target as Node)) {
      setIsTypeOpen(false);
      return;
    }

    setIsOpen(false);
    setIsTypeOpen(false);
  }, []);

  useEffect(() => {
    if (inputValue === '') {
      setIsOpen(false);
      setItemIndex(-1);
      setSelectedPlaceName('');
    } else if (!preventDropdownOpen) {
      setIsOpen(true);
    }

    document.addEventListener('mousedown', handleClickOutside);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [inputValue, preventDropdownOpen, handleClickOutside]);

  const handleSearch = useCallback(
    (searchValue: string, isIndexChoice: boolean) => {
      if (!searchValue.trim()) return;

      // 위치 검색인 경우
      if (searchType === 'location' && searchLocationResults?.documents?.length) {
        const index = isIndexChoice ? itemIndex : 0;
        const selected = searchLocationResults.documents[index];

        if (isIndexChoice || selected.place_name === searchValue) {
          setCenter({ lat: selected.y, lng: selected.x });
          setItemIndex(-1);
          setIsOpen(false);
        }
      }
      // 장소 검색인 경우
      if (searchType === 'place') {
        setSelectedPlaceName(searchValue);
      }
    },
    [searchType, searchLocationResults, itemIndex],
  );

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newInputValue = event.target.value;
    setInputValue(newInputValue);
    setPreventDropdownOpen(false);
  };

  const handleDropDownItem = (item: string) => {
    setPreventDropdownOpen(true);
    setInputValue(item);
    setIsOpen(false);
    handleSearch(item, true);
  };

  const handleDropDownKey = useCallback(
    (event: React.KeyboardEvent<HTMLInputElement>) => {
      if (!inputValue || event.nativeEvent.isComposing || !isOpen) return;
      switch (event.key) {
        case 'ArrowDown':
          setItemIndex((prev) => (prev === dropDownList.length - 1 ? 0 : prev + 1));
          break;
        case 'ArrowUp':
          setItemIndex((prev) => (prev <= 0 ? dropDownList.length - 1 : prev - 1));
          break;
        case 'Enter':
          if (itemIndex >= 0) {
            handleDropDownItem(dropDownList[itemIndex].text);
          } else {
            handleSearch(inputValue, false);
          }
          setIsOpen(false);
          break;
        case 'Escape':
          setIsOpen(false);
          break;
        default:
      }
    },
    [inputValue, isOpen, dropDownList, handleDropDownItem, handleSearch],
  );

  const handleClickSearchRemove = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setInputValue('');
    setSelectedPlaceName('');
  };

  const placeholder =
    searchType === 'location' ? '랜드마크를 입력해주세요 ex) 경북대' : '장소 이름을 입력해주세요 ex) 유정분식';

  return (
    <SearchBarContainer ref={searchBarRef}>
      <SearchForm $isOpen={isOpen} $isTypeOpen={isTypeOpen}>
        <SearchType ref={searchTypeRef}>
          <DropdownButton
            type="button"
            aria-label="searchType_btn"
            onClick={() => {
              setIsTypeOpen(!isTypeOpen);
              setIsOpen(false);
            }}
          >
            {searchType === 'location' ? '위치' : '장소'}
            {isTypeOpen ? <IoIosArrowUp size={12} /> : <IoIosArrowDown size={12} />}
          </DropdownButton>
          {isTypeOpen && (
            <OptionsContainer>
              <TypeItem
                className={searchType === 'location' ? 'selected' : ''}
                onClick={() => {
                  setSearchType('location');
                  setIsTypeOpen(false);
                  setSelectedPlaceName('');
                }}
              >
                위치
              </TypeItem>
              <TypeItem
                className={searchType === 'place' ? 'selected' : ''}
                onClick={() => {
                  setSearchType('place');
                  setIsTypeOpen(false);
                }}
              >
                장소
              </TypeItem>
            </OptionsContainer>
          )}
        </SearchType>
        <SearchInput
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          onKeyDown={handleDropDownKey}
          placeholder={placeholder}
        />
        <SearchRemoveButton aria-label="search-remove-btn" onClick={handleClickSearchRemove}>
          {inputValue ? <IoIosClose size={26} /> : <IoIosSearch size={20} />}
        </SearchRemoveButton>
      </SearchForm>
      {inputValue && isOpen && (
        <SearchDropDownBox>
          {dropDownList.length === 0 ? (
            <SearchDropDownItem>검색 결과가 없습니다.</SearchDropDownItem>
          ) : (
            <>
              {dropDownList.map((item, index) => {
                // inputValue가 처음 나타나는 인덱스
                const matchIndex = item.text.toLowerCase().indexOf(inputValue.toLowerCase());
                return (
                  <SearchDropDownItem
                    key={item.id}
                    onClick={() => handleDropDownItem(item.text)}
                    onMouseOver={() => setItemIndex(index)}
                    className={itemIndex === index ? 'selected' : ''}
                  >
                    {matchIndex !== -1 ? (
                      // 일치하는 글자 빨간색으로 강조
                      <>
                        {item.text.substring(0, matchIndex)}
                        <span style={{ color: 'red' }}>
                          {item.text.substring(matchIndex, matchIndex + inputValue.length)}
                        </span>
                        {item.text.substring(matchIndex + inputValue.length)}
                      </>
                    ) : (
                      // 일치하는 글자가 없는 경우 그대로 보여줌(위치 검색)
                      item.text
                    )}
                  </SearchDropDownItem>
                );
              })}
            </>
          )}
        </SearchDropDownBox>
      )}
    </SearchBarContainer>
  );
}

const SearchBarContainer = styled.div`
  width: 340px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  position: relative;
  z-index: 100;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const SearchForm = styled.div<{ $isOpen: boolean; $isTypeOpen: boolean }>`
  position: relative;
  height: 38px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border: 1.5px solid #a5a5a5;
  background: #ffffff;
  z-index: 2;
  border-bottom-color: ${({ $isOpen }) => ($isOpen ? 'transparent' : null)};
  border-radius: ${({ $isOpen }) => ($isOpen ? '16px 16px 0 0' : '16px')};
  border-bottom-left-radius: ${({ $isTypeOpen }) => ($isTypeOpen ? '0px' : null)};
`;

const SearchInput = styled.input`
  font-size: 14px;
  width: 100%;
  color: #333333;
  background: transparent;
  border: none;
  outline: none;
  z-index: 1;

  &::placeholder {
    color: #a5a5a5;
  }
`;

const SearchRemoveButton = styled.button`
  background: transparent;
  border: none;
  cursor: pointer;
  z-index: 2;
  padding: 0 8px 0px 6px;
`;

const SearchDropDownBox = styled.ul`
  font-size: 14px;
  display: inline-block;
  position: absolute;
  width: 100%;
  right: 0px;
  top: 100%;
  padding: 8px 0px;
  background-color: #ffffff;
  border: 1.5px solid #a5a5a5;
  border-top: none;
  border-radius: 0 0 16px 16px;
  box-shadow: 0 10px 10px rgb(0, 0, 0, 0.3);
  list-style-type: none;
  color: #333333;
  z-index: 100;
  box-sizing: border-box;
  margin-top: -1.5px;
`;

const SearchDropDownItem = styled.li`
  padding: 12px 16px;
  border-radius: 0 0 6px 6px;

  &.selected {
    background: #d5ecec;
  }
`;

const SearchType = styled.div`
  width: 22%;
  position: relative;
`;

const DropdownButton = styled.button`
  width: 100%;
  padding: 10px;
  padding-right: 8px;
  background: none;
  border: none;
  border-radius: 16px 0px 0px 16px;
  display: flex;
  align-items: center;
  gap: 2px;
  cursor: pointer;
  font-size: 14px;
  color: #333333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  @media screen and (max-width: 768px) {
    padding: 6px;
  }
`;

const OptionsContainer = styled.div`
  display: inline-block;
  position: absolute;
  display: flex;
  flex-direction: column;
  top: 100%;
  cursor: pointer;
  width: 90%;
  left: -1.5px;
  padding: 4px 0px 8px 0px;
  background-color: #ffffff;
  border: 1.5px solid #a5a5a5;
  border-top: none;
  border-radius: 0 0 16px 16px;
  z-index: 102;

  @media screen and (max-width: 768px) {
    max-height: 150px;
  }
`;
const TypeItem = styled.div`
  padding: 6px 10px;
  font-size: 14px;
  color: #333333;
  border-radius: 0 0 4px 4px;

  &.selected {
    display: none;
  }

  &:hover {
    background-color: #d5ecec;
  }
`;
