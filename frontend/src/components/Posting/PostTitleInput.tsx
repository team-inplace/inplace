import styled from 'styled-components';

type PostTitleProps = {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
};
export default function PostTitleInput({ value, onChange }: PostTitleProps) {
  return <InputField type="text" placeholder="제목을 입력해주세요" value={value} onChange={onChange} />;
}
const InputField = styled.input`
  width: 100%;
  padding: 10px;
  border: none;
  box-sizing: border-box;
  background: transparent;
  border-radius: 5px;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};
  font-size: 20px;

  &:focus {
    outline: none;
    border: none;
  }
  &::placeholder {
    color: #979797;
  }
`;
