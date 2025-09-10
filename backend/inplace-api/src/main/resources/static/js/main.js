function showLoader(message = '처리 중입니다...') {
    document.getElementById('loaderText').textContent = message;
    document.getElementById('loader').style.display = 'block';
}

function hideLoader() {
    document.getElementById('loader').style.display = 'none';
}

function forceCrawling() {
    if (confirm('크롤링을 시작하시겠습니까?')) {
        showLoader('크롤링중입니다.');
        $.ajax({
            url: '/crawling/video',
            method: 'POST',
            success: function (response) {
                hideLoader();
                alert('크롤링이 완료되었습니다.');
            },
            error: function (xhr, status, error) {
                hideLoader();
                alert('크롤링 시작 중 오류가 발생했습니다.');
            }
        });
    }
}

function forceViewUpdate() {
    if (confirm('조회수 업데이트를 시작하시겠습니까?')) {
        showLoader('조회수 업데이트중 입니다.');
        $.ajax({
            url: '/crawling/video/view',
            method: 'POST',
            success: function (response) {
                hideLoader();
                alert('조회수 업데이트가 완료되었습니다.');
            },
            error: function (xhr, status, error) {
                hideLoader();
                alert('조회수 업데이트 중 오류가 발생했습니다.');
            }
        });
    }
}

function updateCoolNewVideo() {
    $.ajax({
        url: '/videos/update',
        method: 'GET',
        success: function() {
            alert("Cool/New Video 업데이트 성공하였습니다.");
        },
        error: function () {
            alert("Cool/New Video 업데이트에 실패하였습니다.");
        }
    });
}
