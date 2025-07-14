export default function useExtractYoutubeVideoId(url: string) {
  const extractVideoId = () => {
    const match = url.match(/(?:https?:\/\/)?(?:www\.)?youtu(?:be\.com\/watch\?v=|\.be\/)([\w-]*)(&(amp;)?[\w?=]*)?/);
    return match && match[1] ? match[1] : null;
  };

  return extractVideoId();
}
