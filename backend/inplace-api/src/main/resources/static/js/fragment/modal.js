let currentOpenModalId = null;

function closeModal() {
    if (currentOpenModalId) {
        const modal = document.getElementById(currentOpenModalId);
        const iframe = modal.querySelectorAll("iframe");
        if (modal) {
            modal.style.display = "none";
        }
        if (iframe) {
            iframe.src = "";
        }

        currentOpenModalId = null;
        location.reload()
    }
}

// 모달 외부 클릭시 닫기
window.onclick = function (event) {
    if (currentOpenModalId) {
        const modal = document.getElementById(currentOpenModalId);
        if (event.target === modal) {
            closeModal();
        }
    }
};
