import { setupServer } from 'msw/node';
import { mainHandlers } from './mainHandlers';
import { detailHandlers } from './detailHandlers';
import { mapHandlers } from './mapHandlers';
import { myHandlers } from './myPageHandlers';
import { searchHandlers } from './searchHandlers';
import { reviewHandlers } from './reviewHandlers';
import { postHandlers } from './postHandlers';

const server = setupServer(
  ...mainHandlers,
  ...detailHandlers,
  ...mapHandlers,
  ...myHandlers,
  ...searchHandlers,
  ...reviewHandlers,
  ...postHandlers,
);
export default server;
