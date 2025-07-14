interface FormDataType {
  title: string;
  content: string;
}

const validatePostForm = ({ title, content }: FormDataType) => {
  if (!title.trim()) return '제목은 한 글자 이상 입력해주세요.';
  if (title.length > 30) return '제목은 30자 내로 작성해주세요.';
  if (!content.trim()) return '내용은 한 글자 이상 입력해주세요.';
  if (content.length > 3000) return '내용은 3000자 내로 작성해주세요.';
  return null;
};

export default validatePostForm;
