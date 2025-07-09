import { useState, useEffect } from 'react';
import styled from 'styled-components';
import BasicImage from '@/assets/images/basic-image.webp';

interface FallbackImageProps extends React.ImgHTMLAttributes<HTMLImageElement> {
  fallbackSrc?: string;
}

export default function FallbackImage({ src, fallbackSrc = BasicImage, alt = '', ...props }: FallbackImageProps) {
  const [currentSrc, setCurrentSrc] = useState<string>(src || fallbackSrc);
  const [hasError, setHasError] = useState<boolean>(false);

  const checkImageExists = async (url: string): Promise<boolean> => {
    try {
      const response = await fetch(url, { method: 'HEAD' });
      return response.ok;
    } catch {
      return false;
    }
  };

  useEffect(() => {
    setCurrentSrc(src || fallbackSrc);
    setHasError(false);

    if (src) {
      checkImageExists(src).then((exists) => {
        if (!exists) {
          setCurrentSrc(fallbackSrc);
          setHasError(true);
        }
      });
    }
  }, [src, fallbackSrc]);

  const handleError = () => {
    if (!hasError) {
      setCurrentSrc(fallbackSrc);
      setHasError(true);
    }
  };

  return <StyledImage src={currentSrc} alt={alt} onError={handleError} {...props} />;
}

const StyledImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: inherit;
  transition:
    opacity 0.3s ease-in-out,
    transform 0.3s ease;
  opacity: ${(props) => (props.loading ? '0' : '1')};
`;
