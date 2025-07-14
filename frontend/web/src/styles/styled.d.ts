import 'styled-components';

interface CustomTheme {
  backgroundColor: string;
  textColor: string;
}

declare module 'styled-components' {
  export interface DefaultTheme extends CustomTheme {}
}
