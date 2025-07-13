import { useState, useEffect } from 'react';

export default function useGetLocation() {
  const GEOLOCATION_CONFIG = {
    maximumAge: 500000,
    timeout: 50000,
  };

  const [location, setLocation] = useState<{ lat: number; lng: number } | null>(null);

  useEffect(() => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const newCenter = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
          };
          setLocation(newCenter);
        },
        (error) => {
          console.error('Error fetching location', error);
        },
        GEOLOCATION_CONFIG,
      );
    }
    console.error('Geolocation is not supported by this browser.');
  }, []);

  return location;
}
