// Import the functions you need from the SDKs you need
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.0/firebase-messaging.js');
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  storageBucket: import.meta.env.VITE_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: import.meta.env.VITE_FIREBASE_MESSAGING_SENDER_ID,
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
  measurementId: import.meta.env.VITE_FIREBASE_MEASUREMENT_ID,
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  const { title, body, icon } = payload.notification || {};
  const { postId, commentId } = payload.data || {};
  self.registration.showNotification(title || '알림', { body, icon, data: { postId, commentId } });
});

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