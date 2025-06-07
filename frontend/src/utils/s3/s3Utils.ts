export async function hashImage(file: File) {
  const arrayBuffer = await file.arrayBuffer();
  const hashBuffer = await crypto.subtle.digest('SHA-256', arrayBuffer); // 해시 생성
  const hashArray = new Uint8Array(hashBuffer);
  const hashHex = Array.from(hashArray)
    .map((byte: number) => byte.toString(16).padStart(2, '0'))
    .join('');
  return hashHex;
}

export function getObjectKeyFromS3Url(s3Url: string) {
  const match = s3Url.match(/amazonaws\.com\/[^/]+\/(.+)$/);
  return match ? match[1] : null;
}
