// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import '@testing-library/jest-dom';
import server from './mocks/server';
import { DataLayerEvent } from './utils/test/googleTestUtils';

declare global {
  interface Window {
    dataLayer: DataLayerEvent[];
  }
}

beforeAll(() => {
  server.listen();
  (window as Window).dataLayer = [];
});

afterEach(() => server.resetHandlers());

afterAll(() => server.close());
