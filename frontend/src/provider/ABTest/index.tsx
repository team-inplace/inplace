import { createContext, useContext, useState, useEffect, ReactNode, useMemo } from 'react';
import { getCookie, setCookie, sendGAEvent, ABTestGroup } from '@/utils/test/googleTestUtils';

interface ABTestContextType {
  testGroups: Record<string, ABTestGroup>;
  getTestGroup: (testName: string) => ABTestGroup;
}

export const ABTestContext = createContext<ABTestContextType | undefined>(undefined);

interface ABTestProviderProps {
  children: ReactNode;
  testDuration?: number;
  testNames?: string[];
}

export default function ABTestProvider({
  children,
  testDuration = 30,
  testNames = ['map_ui_test'],
}: ABTestProviderProps) {
  const [testGroups, setTestGroups] = useState<Record<string, ABTestGroup>>({});
  const [initialized, setInitialized] = useState(false);

  // 마운트 할 때 한번만 테스트 그룹 초기화
  useEffect(() => {
    if (initialized) return;

    const initializedGroups: Record<string, ABTestGroup> = {};

    testNames.forEach((testName) => {
      const cookieName = `ab_test_${testName}`;
      const savedGroup = getCookie(cookieName);

      if (savedGroup === 'A' || savedGroup === 'B') {
        initializedGroups[testName] = savedGroup as ABTestGroup;
      } else {
        const newGroup: ABTestGroup = Math.random() < 0.5 ? 'A' : 'B';
        initializedGroups[testName] = newGroup;
        setCookie(cookieName, newGroup, testDuration);
      }
    });

    setTestGroups(initializedGroups);
    setInitialized(true);
  }, [testNames, testDuration, initialized]);

  // 초기화 끝나고 테스트 그룹이 설정되면 1번만 GA 이벤트 전송
  useEffect(() => {
    if (!initialized || Object.keys(testGroups).length === 0) {
      return;
    }
    if (typeof window !== 'undefined' && window.gtag) {
      // GA4에 사용자 속성 설정
      const userProperties: Record<string, string> = {};
      Object.entries(testGroups).forEach(([testName, group]) => {
        userProperties[`ab_test_${testName}`] = group;
      });

      window.gtag('set', 'user_properties', userProperties);
      // GA4에 이벤트 전송
      Object.entries(testGroups).forEach(([testName, group]) => {
        sendGAEvent('ab_test_count', {
          test_name: testName,
          variation: group,
        });
      });
    }
  }, [testGroups, initialized]);

  const getTestGroup = (testName: string): ABTestGroup => {
    return testGroups[testName];
  };
  const contextValue = useMemo(() => ({ testGroups, getTestGroup }), [testGroups]);

  return <ABTestContext.Provider value={contextValue}>{children}</ABTestContext.Provider>;
}

export const useABTest = (testName = 'map_ui_test'): ABTestGroup => {
  const context = useContext(ABTestContext);

  if (context === undefined) {
    throw new Error('useABTest must be used within an ABTestProvider');
  }

  return context.getTestGroup(testName);
};
