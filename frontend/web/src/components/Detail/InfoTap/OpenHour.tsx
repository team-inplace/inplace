import styled from 'styled-components';

import { Text } from '@/components/common/typography/Text';

import NoItem from '@/components/common/layouts/NoItem';

export default function OpenHour({ openHour }: { openHour: string[] }) {
  return (
    <Wrapper>
      {openHour.length === 0 && openHour.length === 0 ? (
        <NoItem message="정보가 없습니다." height={0} logo={false} alignItems="start" />
      ) : (
        <HourItem>
          {openHour.map((period) => (
            <TimeItem key={period}>
              <Text size="xs" weight="normal">
                {period}
              </Text>
            </TimeItem>
          ))}
        </HourItem>
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  margin-bottom: 10px;
  padding: 0px 20px;
  line-height: 170%;

  @media screen and (max-width: 768px) {
    line-height: 140%;
    padding: 0px 10px;
  }
`;

const HourItem = styled.div`
  display: flex;
  flex-direction: column;
  gap: 6px;

  @media screen and (max-width: 768px) {
    gap: 2px;
  }
`;

const TimeItem = styled.div`
  display: flex;
  gap: 6px;
`;
