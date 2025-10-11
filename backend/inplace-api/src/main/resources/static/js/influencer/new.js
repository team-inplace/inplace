document.getElementById("searchInput")
    .addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
        searchChannels();
    }
});

function searchChannels() {
    const query = document.getElementById("searchInput").value;
    if (!query) {
        alert("검색어를 입력해주세요.");
        return;
    }
    const API_KEY = document.getElementById('youtubeApiKey').value;
    const apiUrl = `https://www.googleapis.com/youtube/v3/search?part=snippet&type=channel&q=${encodeURIComponent(query)}&key=${API_KEY}`;

    $.ajax({
        url: apiUrl,
        type: 'GET',
        success: function (response) {
            displaySearchResults(response.items);
        },
        error: function (xhr, status, error) {
            alert('채널 검색 중 오류가 발생했습니다.');
            console.error(error);
        }
    });
}

function displaySearchResults(items) {
    const tbody = $("#searchResultsBody");
    tbody.empty();

    items.forEach(item => {
        const row = $('<tr>');
        row.html(`
           <td><img src="${item.snippet.thumbnails.default.url}" alt="채널 썸네일"></td>
           <td>${item.snippet.channelTitle}</td>
           <td><a href="https://www.youtube.com/channel/${item.id.channelId}" target="_blank">채널 보기</a></td>
           <td><button onclick="openInfluencerRegisterModal('${item.id.channelId}', '${item.snippet.channelTitle}', '${item.snippet.thumbnails.default.url}')">등록</button></td>
       `);
        tbody.append(row);
    });
}

function openInfluencerRegisterModal(channelId, channelTitle, thumbnailUrl) {
    // 채널 정보를 hidden input에 저장
    document.getElementById("channelId").value = channelId;
    document.getElementById("channelTitle").value = channelTitle;
    document.getElementById("thumbnailUrl").value = thumbnailUrl;

    // 모달 표시
    document.getElementById("registerModal").style.display = "block";
    currentOpenModalId = "registerModal";
}

function selectChannel() {
    const formData = {
        influencerName: document.getElementById("name").value,
        influencerImgUrl: document.getElementById("thumbnailUrl").value,
        influencerJob: document.getElementById("job").value,
        channelTitle: document.getElementById("channelTitle").value,
        channelId: document.getElementById("channelId").value
    };

    $.ajax({
        url: '/influencers',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function () {
            alert('인플루언서가 성공적으로 등록되었습니다.');
            document.getElementById("influencerForm").reset();
            closeModal();
            searchChannels();
        },
        error: function (xhr, status, error) {
            alert('등록 중 오류가 발생했습니다.');
            console.error(error);
        }
    });
}
