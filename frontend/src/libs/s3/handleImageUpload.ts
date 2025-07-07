import { getObjectKeyFromS3Url } from '@/utils/s3/s3Utils';
import { UploadImage } from '@/types';

const CLOUDFRONT_DOMAIN = 'https://d1d3zg2ervqwcu.cloudfront.net';

export default async function handleImageUpload(newImages: UploadImage[]) {
  const ids = newImages.map((image) => image.file.name).join(',');

  const response = await fetch(`https://fh318e0yce.execute-api.ap-northeast-2.amazonaws.com?ids=${ids}`);
  if (!response.ok) throw new Error('Failed to get presigned URLs');

  const { urls } = await response.json();

  // S3에 업로드
  const uploadedImageUrls = await Promise.all(
    urls.map((presignedUrl: string, idx: number) => {
      const { file } = newImages[idx];
      return fetch(presignedUrl, {
        method: 'PUT',
        headers: { 'Content-Type': file.type },
        body: file,
      }).then((res) => {
        if (!res.ok) throw new Error(`Failed to upload file ${file.name}`);
        return presignedUrl.split('?')[0];
      });
    }),
  );

  return uploadedImageUrls
    .map(getObjectKeyFromS3Url)
    .filter(Boolean)
    .map((objectKey, idx) => ({
      imageUrl: `${CLOUDFRONT_DOMAIN}/${objectKey}`,
      hash: newImages[idx].hash,
    }));
}
