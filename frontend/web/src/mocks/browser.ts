import { setupWorker } from 'msw';
import { mainHandlers } from './mainHandlers';
import { detailHandlers } from './detailHandlers';
import { mapHandlers } from './mapHandlers';
import { myHandlers } from './myPageHandlers';
import { InfluencerHandlers } from './influencerHandlers';
import { searchHandlers } from './searchHandlers';
import { reviewHandlers } from './reviewHandlers';
import { postHandlers } from './postHandlers';

export const worker = setupWorker(
  ...InfluencerHandlers,
  ...mainHandlers,
  ...detailHandlers,
  ...mapHandlers,
  ...myHandlers,
  ...searchHandlers,
  ...reviewHandlers,
  ...postHandlers,
);
export default worker;
