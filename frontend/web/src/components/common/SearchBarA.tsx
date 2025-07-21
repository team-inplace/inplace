import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import useDebounce from '@/hooks/useDebounce';
import { useGetSearchComplete } from '@/api/hooks/useGetSearchComplete';
import { SearchComplete } from '@/types';

interface SearchBarProps {
  placeholder?: string;
}

export default function SearchBar({ placeholder = '키워드를 입력해주세요!' }: SearchBarProps) {
  const navigate = useNavigate();
  const [inputValue, setInputValue] = useState('');
  const [dropDownList, setDropDownList] = useState<SearchComplete[]>([]);
  const [itemIndex, setItemIndex] = useState(-1);
  const [isOpen, setIsOpen] = useState(false);

  const debouncedInputValue = useDebounce(inputValue, 300);

  const { data: searchResults } = useGetSearchComplete(debouncedInputValue, 'all', !!debouncedInputValue);

  const searchBarRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (searchResults) {
      setDropDownList(searchResults);
      setItemIndex(-1);
    } else {
      setDropDownList([]);
    }

    const handleClickOutside = (event: MouseEvent) => {
      if (searchBarRef.current && !searchBarRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);

    setIsOpen(inputValue !== '');

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [searchResults, inputValue]);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newInputValue = event.target.value;
    if (newInputValue !== inputValue) {
      setInputValue(newInputValue);
    }
  };

  const handleDropDownItem = (item: string) => {
    setInputValue(item);
    setItemIndex(-1);
    setIsOpen(false);
    handleSearch(item);
  };

  const handleSearch = (searchValue: string) => {
    if (searchValue.trim()) {
      const query = encodeURIComponent(searchValue);
      setTimeout(() => setInputValue(''), 0);
      navigate(`/search?query=${query}`);
    }
  };

  const handleDropDownKey = (event: React.KeyboardEvent<HTMLInputElement>) => {
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
          handleDropDownItem(dropDownList[itemIndex].result);
        } else {
          handleSearch(inputValue);
        }
        setIsOpen(false);
        break;
      default:
    }
  };

  return (
    <SearchBarContainer ref={searchBarRef}>
      <SearchInputWrapper $isOpen={isOpen}>
        <SearchInput
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          onKeyDown={handleDropDownKey}
          placeholder={placeholder}
        />
        <SearchIconWrapper role="button" aria-label="검색바 아이콘 버튼_A" onClick={() => handleSearch(inputValue)} />
      </SearchInputWrapper>
      {inputValue && isOpen && (
        <SearchDropDownBox>
          {dropDownList.length === 0 ? (
            <SearchDropDownItem>해당하는 키워드가 없습니다!</SearchDropDownItem>
          ) : (
            <>
              {dropDownList.map((item, index) => {
                const matchIndex = item.result.toLowerCase().indexOf(inputValue.toLowerCase());
                return (
                  <SearchDropDownItem
                    key={item.result}
                    onClick={() => handleDropDownItem(item.result)}
                    onMouseOver={() => setItemIndex(index)}
                    className={itemIndex === index ? 'selected' : ''}
                  >
                    {matchIndex !== -1 ? (
                      <>
                        {item.result.substring(0, matchIndex)}
                        <span style={{ color: 'red' }}>
                          {item.result.substring(matchIndex, matchIndex + inputValue.length)}
                        </span>
                        {item.result.substring(matchIndex + inputValue.length)}
                        <span style={{ color: '#a7a7a7', marginLeft: '12px', alignItems: 'end' }}>
                          {item.searchType}
                        </span>
                      </>
                    ) : (
                      <>
                        {item.result}
                        <span style={{ color: '#a7a7a7', marginLeft: '12px', alignItems: 'end' }}>
                          {item.searchType}
                        </span>
                      </>
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
  width: 100%;
  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const SearchInputWrapper = styled.div<{ $isOpen: boolean }>`
  display: flex;
  align-items: center;
  background: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#414141' : '#ffffff')};
  padding: 12px 16px;
  border: 1.5px solid #a5a5a5;
  border-bottom: ${({ $isOpen }) => ($isOpen ? 'none' : null)};
  border-radius: ${({ $isOpen }) => ($isOpen ? '16px 16px 0 0' : '16px')};
  z-index: 3;
`;

const SearchInput = styled.input`
  font-size: 16px;
  flex: 1;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};
  background: transparent;
  border: none;
  margin-right: 8px;
  outline: none;
  z-index: 1;
  padding: 0;

  &::placeholder {
    color: #a5a5a5;
    font-weight: normal;
  }
`;

const SearchIconWrapper = styled.div`
  width: 20px;
  height: 20px;
  background-color: #55ebff;
  mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M23.384,21.619,16.855,15.09a9.284,9.284,0,1,0-1.768,1.768l6.529,6.529a1.266,1.266,0,0,0,1.768,0A1.251,1.251,0,0,0,23.384,21.619ZM2.75,9.5a6.75,6.75,0,1,1,6.75,6.75A6.758,6.758,0,0,1,2.75,9.5Z'/%3E%3C/svg%3E")
    center / contain no-repeat;
  cursor: pointer;
`;

const SearchDropDownBox = styled.ul`
  font-size: 14px;
  display: inline-block;
  position: absolute;
  width: 960px;
  padding: 8px 0px;
  background-color: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#414141' : '#ffffff')};
  border: 1.5px solid #a5a5a5;
  border-top: none;
  border-radius: 0 0 16px 16px;
  box-shadow: 0 10px 10px rgb(0, 0, 0, 0.3);
  list-style-type: none;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};
  box-sizing: border-box;
  z-index: 10;

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const SearchDropDownItem = styled.li`
  padding: 12px 16px;
  border-radius: 0 0 6px 6px;
  &.selected {
    background-color: #686868;
    background: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#686868' : '#d5ecec')};
  }
`;
