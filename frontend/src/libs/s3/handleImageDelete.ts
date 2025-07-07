export default async function handleDeleteImages(removedUrls: string[]) {
  if (removedUrls.length === 0) return;
  const deleteResponse = await fetch(
    `https://ea5wr09wv3.execute-api.ap-northeast-2.amazonaws.com?ids=${removedUrls.join(',')}`,
    {
      method: 'DELETE',
      credentials: 'include',
    },
  );
  if (!deleteResponse.ok) throw new Error('이미지 삭제에 실패했습니다.');
}
