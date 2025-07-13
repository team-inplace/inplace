import styled from 'styled-components';

type PostContentProps = {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  textareaRef: React.RefObject<HTMLTextAreaElement>;
  handleResizeHeight: (textarea: HTMLTextAreaElement) => void;
};
export default function PostContentTextArea({ value, onChange, textareaRef, handleResizeHeight }: PostContentProps) {
  return (
    <TextArea
      ref={textareaRef}
      placeholder={`자유롭게 입력하세요.\n\n욕설, 비방, 차별, 혐오, 근거 없는 악의적 후기 등 타인의 권리를 침해하는 행위 시 게시물이 삭제되고 서비스 이용이 제한될 수 있습니다.`}
      rows={1}
      value={value}
      onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => {
        onChange(e);
        handleResizeHeight(e.target);
      }}
    />
  );
}
const TextArea = styled.textarea`
  width: 100%;
  min-height: 360px;
  padding: 10px;
  box-sizing: border-box;
  font-size: 16px;
  line-height: 1.4;
  display: flex;
  border: none;
  background: transparent;
  overflow-y: hidden;
  resize: none;
  color: ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : 'black')};

  &::placeholder {
    color: #979797;
  }

  &:focus {
    outline: none;
    border: none;
  }
  @media screen and (max-width: 768px) {
    min-height: 260px;
  }
`;
