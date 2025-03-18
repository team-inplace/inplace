import { useEffect, useRef, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import styled from 'styled-components';
import useDebounce from '@/hooks/useDebounce';
import { useGetSearchComplete } from '@/api/hooks/useGetSearchComplete';
import { SearchComplete } from '@/types';

interface SearchBarProps {
  placeholder?: string;
  isSearchPage?: boolean;
  width?: string;
}

export default function SearchBar({
  placeholder = '키워드를 입력해주세요!',
  isSearchPage = false,
  width = '260px',
}: SearchBarProps) {
  const navigate = useNavigate();
  const [inputValue, setInputValue] = useState('');
  const [dropDownList, setDropDownList] = useState<SearchComplete[]>([]);
  const [itemIndex, setItemIndex] = useState(-1);
  const [isOpen, setIsOpen] = useState(false);
  const [isExpanded, setIsExpanded] = useState(isSearchPage);
  const debouncedInputValue = useDebounce(inputValue, 300);
  const location = useLocation();

  const { data: searchResults } = useGetSearchComplete(debouncedInputValue);

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
        if (!isSearchPage) {
          setIsExpanded(false);
          setInputValue('');
        }
      }
    };
    document.addEventListener('mousedown', handleClickOutside);

    setIsOpen(inputValue !== '' && isExpanded);

    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [searchResults, inputValue, isExpanded, isSearchPage]);

  useEffect(() => {
    setIsOpen(false);
    setDropDownList([]);
  }, [location]);

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

      setTimeout(() => {
        setInputValue('');
        if (!isSearchPage) {
          setIsExpanded(false);
        }
      }, 0);
      navigate(`/search?query=${query}`);
    }
  };

  const toggleSearchBar = () => {
    if (isSearchPage) {
      handleSearch(inputValue);
    } else {
      setIsExpanded(!isExpanded);
      if (isExpanded) {
        setInputValue('');
        setIsOpen(false);
      }
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

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    handleSearch(inputValue);
  };

  return (
    <SearchBarContainer ref={searchBarRef} $width={isSearchPage ? '960px' : width} $isSearchPage={isSearchPage}>
      {(isExpanded || isSearchPage) && (
        <SearchForm $isOpen={isOpen} onSubmit={handleSubmit} $isSearchPage={isSearchPage}>
          <SearchInput
            type="text"
            value={inputValue}
            onChange={handleInputChange}
            onKeyDown={handleDropDownKey}
            placeholder={placeholder}
            $isSearchPage={isSearchPage}
          />
          {isSearchPage && (
            <SearchButton type="submit" aria-label="검색" $isSearchPage={isSearchPage}>
              <SearchIcon $isExpanded />
            </SearchButton>
          )}
        </SearchForm>
      )}

      {!isSearchPage && (
        <SearchButton type="button" aria-label="검색" onClick={toggleSearchBar} $isSearchPage={isSearchPage}>
          <SearchIcon $isExpanded={isExpanded} />
        </SearchButton>
      )}

      {inputValue && isOpen && isExpanded && (
        <SearchDropDownBox $isSearchPage={isSearchPage}>
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

const SearchBarContainer = styled.div<{ $width: string; $isSearchPage: boolean }>`
  display: flex;
  align-items: center;
  justify-content: ${({ $isSearchPage }) => ($isSearchPage ? 'center' : 'flex-end')};
  position: relative;
  width: ${({ $width }) => $width};

  @media screen and (max-width: 768px) {
    width: 90%;
  }
`;

const SearchForm = styled.form<{ $isOpen: boolean; $isSearchPage: boolean }>`
  height: ${({ $isSearchPage }) => ($isSearchPage ? '44px' : '34px')};
  display: flex;
  align-items: center;
  justify-content: ${({ $isSearchPage }) => ($isSearchPage ? 'space-between' : 'flex-start')};
  background: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#414141' : '#ffffff')};
  border-bottom: ${({ $isOpen }) => ($isOpen ? 'none' : null)};
  border-radius: ${({ $isOpen }) => ($isOpen ? '16px 16px 0 0' : '16px')};
  border: 1.5px solid #a5a5a5;
  position: ${({ $isSearchPage }) => ($isSearchPage ? 'relative' : 'absolute')};
  right: ${({ $isSearchPage }) => ($isSearchPage ? 'auto' : '40px')};
  width: ${({ $isSearchPage }) => ($isSearchPage ? '100%' : 'calc(100% - 40px)')};
  animation: ${({ $isSearchPage }) => ($isSearchPage ? 'none' : 'slideFromRight 0.5s forwards')};
  z-index: 3;

  @keyframes slideFromRight {
    from {
      width: 0;
      opacity: 0;
    }
    to {
      width: calc(100% - 40px);
      opacity: 1;
    }
  }
`;

const SearchInput = styled.input<{ $isSearchPage: boolean }>`
  font-size: ${({ $isSearchPage }) => ($isSearchPage ? '16px' : '14px')};
  width: 100%;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? '#ffffff' : '#333333')};
  background: transparent;
  border: none;
  outline: none;
  z-index: 1;
  padding: 0 16px;

  &::placeholder {
    color: #a5a5a5;
    font-weight: normal;
  }
`;

const SearchButton = styled.button<{ $isSearchPage?: boolean }>`
  background: transparent;
  border: none;
  cursor: pointer;
  z-index: 2;
  padding: ${({ $isSearchPage }) => ($isSearchPage ? '0 16px' : '0')};
`;

const SearchIcon = styled.span<{ $isExpanded: boolean }>`
  width: 20px;
  height: 20px;
  background-color: ${(props) => {
    if (props.$isExpanded) return '#55ebff';
    if (props.theme.backgroundColor === '#292929') return '#ffffff';
    return '#292929';
  }};
  mask: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24'%3E%3Cpath d='M23.384,21.619,16.855,15.09a9.284,9.284,0,1,0-1.768,1.768l6.529,6.529a1.266,1.266,0,0,0,1.768,0A1.251,1.251,0,0,0,23.384,21.619ZM2.75,9.5a6.75,6.75,0,1,1,6.75,6.75A6.758,6.758,0,0,1,2.75,9.5Z'/%3E%3C/svg%3E")
    center / contain no-repeat;
  display: block;
`;

const SearchDropDownBox = styled.ul<{ $isSearchPage: boolean }>`
  font-size: 14px;
  display: inline-block;
  position: absolute;
  width: ${({ $isSearchPage }) => ($isSearchPage ? '100%' : 'calc(100% - 37px)')};
  right: ${({ $isSearchPage }) => ($isSearchPage ? 'auto' : '40px')};
  top: ${({ $isSearchPage }) => ($isSearchPage ? '44px' : '24px')};
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
    width: ${({ $isSearchPage }) => ($isSearchPage ? '100%' : '90%')};
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
