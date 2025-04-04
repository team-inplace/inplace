import { setupWorker } from 'msw';
import { mainHandlers } from './mainHandlers';
import { detailHandlers } from './detailHandlers';
import { mapHandlers } from './mapHandlers';
import { myHandlers } from './myPageHandlers';
import { InfluencerHandlers } from './influencerHandlers';
import { searchHandlers } from './searchHandlers';
import { reviewHandlers } from './reviewHandlers';

export const worker = setupWorker(
  ...InfluencerHandlers,
  ...mainHandlers,
  ...detailHandlers,
  ...mapHandlers,
  ...myHandlers,
  ...searchHandlers,
  ...reviewHandlers,
);
export default worker;
