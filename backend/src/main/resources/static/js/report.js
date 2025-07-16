function showSection(type) {
  sessionStorage.setItem('currentTab', type);
  document.getElementById('reported-posts').style.display = (type === 'posts') ? 'block' : 'none';
  document.getElementById('reported-comments').style.display = (type === 'comments') ? 'block' : 'none';

  document.getElementById('tab-posts').classList.toggle('active', type === 'posts');
  document.getElementById('tab-comments').classList.toggle('active', type === 'comments');
}

// 페이지 로드 시 탭 상태 복원
window.addEventListener('load', () => {
  const savedTab = sessionStorage.getItem('currentTab');
  if (savedTab === 'comments') {
    showSection('comments');
  } else {
    showSection('posts');
  }
});

// 모달 열기 함수
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.style.display = "flex";
    currentOpenModalId = modalId;
  }
}

function openContentModal(element) {
  document.getElementById('contentModalBody').textContent = element.getAttribute(
      'data-full-content');
  openModal('contentModal');
}