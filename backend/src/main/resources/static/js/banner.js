// 이미지 미리보기 기능
function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        reader.readAsDataURL(input.files[0]);
    }
}

function openBannerPreviewModal(src) {
    document.getElementById('previewModal').style.display = "block";
    document.getElementById('modalImage').src = src;
    currentOpenModalId = 'previewModal';
}

// form submit 이벤트 처리
document.getElementById('imageUploadForm').addEventListener('submit', function (e) {
    e.preventDefault();

    // 필수 입력값 확인
    const imageFile = document.getElementById('imageFile').files[0];
    const imageName = document.getElementById('imageName').value;
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (!imageFile) {
        alert('이미지를 선택해주세요.');
        return;
    }

    if (!imageName.trim()) {
        alert('이미지 이름을 입력해주세요.');
        return;
    }

    if (!startDate || !endDate) {
        alert('날짜를 선택해주세요.');
        return;
    }

    // 날짜 유효성 검사
    if (new Date(endDate) < new Date(startDate)) {
        alert('종료 날짜는 시작 날짜 이후여야 합니다.');
        return;
    }

    // FormData 생성 및 전송
    const formData = new FormData(this);

    // 체크박스 값을 명시적으로 설정
    formData.set('isFixed', document.getElementById('isFixed').checked);
    formData.set('isMain', document.getElementById('isMain').checked);
    formData.set('isMobile', document.getElementById('isMobile').checked);

    fetch('/banners', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                // 서버에서 전달된 에러 메시지를 사용
                return response.text().then(text => {
                    throw new Error(text || '업로드 실패');
                });
            }
            return response.text();
        })
        .then(message => {
            alert(message || '이미지가 성공적으로 업로드되었습니다.');
            location.reload();
        })
        .catch(error => {
            alert(error.message || '업로드 중 오류가 발생했습니다.');
            console.error('Error:', error);
        });
});

// 이미지 삭제 요청
function deleteImage(element) {
    const imageId = element.getAttribute('data-id');
    if (confirm('이미지를 삭제하시겠습니까?')) {
        fetch(`/banners/${imageId}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text || '삭제 실패');
                    });
                }
                return response.text();
            })
            .then(message => {
                alert(message || '이미지가 성공적으로 삭제되었습니다.');
                location.reload();
            })
            .catch(error => {
                alert(error.message || '삭제 중 오류가 발생했습니다.');
                console.error('Error:', error);
            });
    }
}
