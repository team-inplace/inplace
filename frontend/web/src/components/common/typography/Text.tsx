import styled from 'styled-components';

export interface IText {
  size: 'xxs' | 'xs' | 's' | 'm' | 'l' | 'xl' | 'xxl' | string;
  weight: 'bold' | 'normal';
  variant?: 'mint' | 'background' | 'white' | 'grey' | string;
}

export const Text = styled.span<IText>`
  font-weight: ${(props) => props.weight ?? 'normal'};

  font-size: ${(props) => {
    switch (props.size) {
      case 'xxs':
        return '14px';
      case 'xs':
        return '16px';
      case 's':
        return '18px';
      case 'm':
        return '20px';
      case 'l':
        return '24px';
      case 'll':
        return '28px';
      case 'xl':
        return '32px';
      case 'xxl':
        return '40px';
      default:
        return props.size;
    }
  }};

  @media screen and (max-width: 768px) {
    font-size: ${(props) => {
      switch (props.size) {
        case 'xxs':
          return '12px';
        case 'xs':
          return '12px';
        case 's':
          return '14px';
        case 'm':
          return '16px';
        case 'l':
          return '18px';
        case 'll':
          return '20px';
        case 'xl':
          return '24px';
        case 'xxl':
          return '32px';
        default:
          return '14px';
      }
    }};
  }

  color: ${(props) => {
    switch (props.variant) {
      case 'mint':
        return '#55EBFF';
      case 'background':
        return '#292929';
      case 'grey':
        return '#979797';
      case 'white':
        return '#ffffff';
      default:
        return props.variant;
    }
  }};
`;
