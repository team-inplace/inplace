import { createContext, ReactNode, useEffect, useMemo, useState } from 'react';
import { ThemeProvider as StyledThemeProvider } from 'styled-components';

const darkMode = {
  backgroundColor: '#292929',
  textColor: '#ffffff',
};

const lightMode = {
  backgroundColor: '#f7fcff',
  textColor: '#333333',
};

type ThemeContextType = {
  theme: string;
  toggleTheme: () => void;
};

export const ThemeContext = createContext<ThemeContextType>({
  theme: 'dark',
  toggleTheme: () => {},
});

interface ThemeProviderProps {
  children: ReactNode;
}

export default function ThemeProvider({ children }: ThemeProviderProps) {
  const [theme, setTheme] = useState(() => {
    const settingTheme = localStorage.getItem('theme');
    return settingTheme || 'dark';
  });

  useEffect(() => {
    localStorage.setItem('theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(theme === 'dark' ? 'light' : 'dark');
  };
  const themeMode = theme === 'dark' ? darkMode : lightMode;

  return (
    <ThemeContext.Provider value={useMemo(() => ({ theme, toggleTheme }), [theme])}>
      <StyledThemeProvider theme={themeMode}>{children}</StyledThemeProvider>
    </ThemeContext.Provider>
  );
}
