import { useContext } from 'react';
import { LuParkingCircle, LuParkingCircleOff, LuParkingSquare, LuParkingSquareOff } from 'react-icons/lu';
import { TbDisabled } from 'react-icons/tb';
import styled from 'styled-components';
import { ImSpoonKnife } from 'react-icons/im';
import { IoCafeOutline } from 'react-icons/io5';
import { CiCreditCard1, CiCreditCardOff } from 'react-icons/ci';
import { Paragraph } from '@/components/common/typography/Paragraph';
import { FacilityInfo } from '@/types';
import NoItem from '@/components/common/layouts/NoItem';
import { ThemeContext } from '@/provider/Themes';

export default function FacilitySign({ category, facilityInfo }: { facilityInfo: FacilityInfo; category: string }) {
  const { theme } = useContext(ThemeContext);
  const iconVariant = theme === 'dark' ? 'white' : '#333333';

  const facilities = {
    acceptsCreditCards: {
      icon: {
        true: <CiCreditCard1 size={34} color={iconVariant} />,
        false: <CiCreditCardOff size={36} color={iconVariant} />,
      },
      label: '카드결제',
    },
    freeParkingLot: {
      icon: {
        true: <LuParkingCircle size={60} color={iconVariant} strokeWidth={1} />,
        false: <LuParkingCircleOff size={60} color={iconVariant} strokeWidth={1} />,
      },
      label: '무료주차',
    },
    paidParkingLot: {
      icon: {
        true: <LuParkingSquare size={60} color={iconVariant} strokeWidth={1} />,
        false: <LuParkingSquareOff size={60} color={iconVariant} strokeWidth={1} />,
      },
      label: '유료주차',
    },
    wheelchairAccessibleSeating: {
      icon: { true: <TbDisabled size={34} color={iconVariant} strokeWidth={1} />, false: null },
      label: '휠체어좌석',
    },
  };

  if (!facilityInfo) {
    return (
      <Wrapper>
        <NoItem message="시설 정보가 없습니다." height={0} logo={false} alignItems="start" />
      </Wrapper>
    );
  }

  return (
    <Wrapper>
      {Object.keys(facilityInfo).length === 0 ? (
        <NoItem message="시설 정보가 없습니다." height={0} logo={false} alignItems="start" />
      ) : (
        <>
          {category && (
            <SignWrapper>
              <Sign>
                {category === '카페' ? <IoCafeOutline size={34} /> : <ImSpoonKnife size={30} color={iconVariant} />}
              </Sign>
              <Paragraph size="xs" weight="bold" variant="mint">
                {category}
              </Paragraph>
            </SignWrapper>
          )}
          {Object.entries(facilities).map(([key, { icon, label }]) => {
            const facilityStatus = facilityInfo[key as keyof FacilityInfo];

            let iconElement = null;
            if (facilityStatus === true) {
              iconElement = icon.true;
            } else if (facilityStatus === false) {
              iconElement = icon.false;
            }

            if (iconElement === null) return null;

            return (
              <SignWrapper key={key} $isParking={key.includes('Parking')}>
                {key.includes('Parking') ? iconElement : <Sign>{iconElement}</Sign>}
                <Paragraph size="xs" weight="normal">
                  {label}
                </Paragraph>
              </SignWrapper>
            );
          })}
        </>
      )}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  display: flex;
  gap: 18px;
  padding: 0px 20px;
  margin-bottom: 20px;

  @media screen and (max-width: 768px) {
    flex-wrap: wrap;
    gap: 6px;
    padding: 0px 10px;
  }
`;

const Sign = styled.div`
  border: 3px solid ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : '#333333')};
  width: 46px;
  height: 46px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 4px;

  @media screen and (max-width: 768px) {
    width: 40px;
    height: 40px;
    border: 2px solid ${({ theme }) => (theme.textColor === '#ffffff' ? 'white' : '#333333')};

    svg {
      width: 30px;
    }
  }
`;

const SignWrapper = styled.div<{ $isParking?: boolean }>`
  display: flex;
  flex-direction: column;
  text-align: center;
  gap: 12px;
  align-items: center;

  @media screen and (max-width: 768px) {
    gap: ${({ $isParking }) => ($isParking ? '4px' : '8px')};
    svg {
      width: ${({ $isParking }) => ($isParking ? '50px' : null)};
      margin-top: ${({ $isParking }) => ($isParking ? '-4px' : null)};
    }
  }
`;
