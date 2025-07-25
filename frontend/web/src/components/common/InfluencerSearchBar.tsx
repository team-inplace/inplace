import styled from 'styled-components';

interface SearchBarProps {
  inputValue: string;
  setInputValue: (value: string) => void;
  placeholder?: string;
}

export default function InfluencerSearchBar({
  inputValue,
  setInputValue,
  placeholder = '인플루언서 이름을 입력해주세요!',
}: SearchBarProps) {
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newInputValue = event.target.value;
    setInputValue(newInputValue);
  };

  return (
    <SearchBarContainer>
      <SearchInputWrapper>
        <SearchInput type="text" value={inputValue} onChange={handleInputChange} placeholder={placeholder} />
        <SearchIconWrapper />
      </SearchInputWrapper>
    </SearchBarContainer>
  );
}

const SearchBarContainer = styled.div`
  width: 100%;
  height: 44px;

  @media screen and (max-width: 768px) {
    width: 90%;
    height: 36px;
  }
`;

const SearchInputWrapper = styled.div`
  display: flex;
  align-items: center;
  background: ${({ theme }) => (theme.backgroundColor === '#292929' ? '#414141' : '#ffffff')};
  padding: 12px 16px;
  border: 1.5px solid #a5a5a5;
  border-radius: 16px;
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
