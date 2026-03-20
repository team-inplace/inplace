self.addEventListener('notificationclick', (event) => {
  event.notification.close();
  const { postId, commentId } = event.notification.data || {};
  if (!postId || !commentId) return;

  event.waitUntil(
    clients.matchAll({ type: 'window', includeUncontrolled: true }).then((clientList) => {
      for (const client of clientList) {
        if ('focus' in client) {
          client.postMessage({ type: 'FCM_NOTIFICATION_NAVIGATE', postId, commentId });
          return client.focus();
        }
      }
      return fetch(`/posts/${postId}/comments/${commentId}/position`, { credentials: 'include' })
        .then((res) => res.json())
        .then((data) => clients.openWindow(`/post/${postId}?commentPage=${data.commentPage}&commentId=${commentId}`))
        .catch(() => clients.openWindow(`/post/${postId}`));
    })
  );
});