let placeRowIdx = 0

function openPlaceSearchModal(element) {
  let videoUrl = null;
  if (element) {
    window.selectedVideoId = element.getAttribute("data-video-id");
    videoUrl = element.getAttribute("data-video-url");
  }

  document.getElementById('placeSearchModal').style.display = "block";
  document.getElementById('videoIFrame').src = `https://www.youtube.com/embed/${videoUrl}`;
  const row = document.getElementById(`place-row-${placeRowIdx}`);
  const videoInput = row.querySelector('input[name="videoId"]');
  videoInput.value = window.selectedVideoId;

  currentOpenModalId = 'placeSearchModal';
  placeRowIdx++;
}

function addPlaceRow() {
  const rowIdx = placeRowIdx++;
  const rowHtml = `
        <tr id="place-row-${rowIdx}">
            <td><input name="videoId" value="${selectedVideoId}" disabled></td>
            <td><input name="placeName"></td>
            <td>
                <select name="category" required>
                    <option disabled selected value="">카테고리</option>
                </select>
            </td>
            <td><input name="address"></td>
            <td><input name="x"></td>
            <td><input name="y"></td>
            <td><input name="kakaoPlaceId"></td>
            <td><input name="googlePlaceId"></td>
        </tr>
    `;

  $('#place-register-tbody').append(rowHtml);

  populateCategoryOptions(rowIdx);
}

function populateCategoryOptions(rowIdx) {
  const row = document.getElementById(`place-row-${rowIdx}`);
  const select = row.querySelector('select[name="category"]');

  categories.forEach(category => {
    select.insertAdjacentHTML(
        'beforeend',
        `<option value="${category.name}">${category.name}</option>`
    );
  });
}

let currentMapProvider = null;
function showTabPane(mapProvider) {
  let currentTabPaneId;
  let currentTabId;
  if (currentMapProvider) {
    currentTabPaneId = `tab-pane-${currentMapProvider}`;
    currentTabId = `tab-${currentMapProvider}`;
    document.getElementById(currentTabPaneId).style.display = 'none';
    document.getElementById(currentTabId).classList.remove("active");
  }
  currentMapProvider = mapProvider;
  currentTabPaneId = `tab-pane-${mapProvider}`;
  currentTabId = `tab-${mapProvider}`;
  document.getElementById(currentTabPaneId).style.display = 'block';
  document.getElementById(currentTabId).classList.add("active");
}

let placeInfo = null;

function searchPlaces(mapProvider) {
  const keyword = document.getElementById(`keyword-${mapProvider}`).value;
  if (!keyword.trim()) {
    alert("검색어를 입력하세요.");
    return;
  }

  if (mapProvider === mapProviderKakao) {
    searchKakaoPlaces(keyword);
  }
  else if (mapProvider === mapProviderGoogle) {
    searchGooglePlaces(keyword);
  }
}

function searchKakaoPlaces(keyword) {
  const ps = new kakao.maps.services.Places();
  ps.keywordSearch(keyword, function (data, status) {
    const tbody = $('#search-tbody-Kakao');
    tbody.empty();

    if (status === kakao.maps.services.Status.OK) {
      data.forEach(place => {
        const row = `
                    <tr>
                        <td>${place.place_name}</td>
                        <td>${place.address_name}</td>
                        <td><button class="set-place-info-${mapProviderKakao}" data-place='${JSON.stringify(
            place)}'>등록</button></td>
                    </tr>`;
        tbody.append(row);
      });
      $(`.set-place-info-${mapProviderKakao}`).off("click").on("click",
          function () {
            setPlaceForm(
                JSON.stringify($(this).data("place")).replace(/'/g, "&#39;"));
          });
    } else {
      alert("장소 검색에 실패했습니다.");
    }
  });
}

function searchGooglePlaces(keyword) {
  $.ajax({
    url: 'https://places.googleapis.com/v1/places:searchText',
    method: 'POST',
    contentType: 'application/json',
    headers: {
      "X-Goog-Api-Key": document.getElementById(
          "google-api-key").getAttribute("data-api-key"),
      "X-Goog-FieldMask": "*"
    },
    data: JSON.stringify({
      textQuery: keyword
    }),
    success: function (res) {
      const tbody = $('#search-tbody-Google');
      tbody.empty();

      res.places.forEach(place => {
        const row = `
                <tr>
                    <td>${place.displayName.text}</td>
                    <td>${place.formattedAddress}</td>
                    <td><button class="set-place-info-${mapProviderGoogle}" data-place-id="${place.id}">등록</button></td>
                </tr>`;
        tbody.append(row);
      });

      // 이벤트 리스너 등록
      $(`.set-place-info-${mapProviderGoogle}`).off("click").on("click",
          function () {
            document.getElementById('place-id-Google').value = $(this).data(
                "place-id");
          });
    },
    error: function () {
      alert("검색 실패");
    }
  });
}

function setPlaceForm(place) {
  place = JSON.parse(place);
  document.getElementById('placeName').value = place.place_name;
  document.getElementById('address').value = place.address_name;
  document.getElementById('x').value = place.x;
  document.getElementById('y').value = place.y;
  document.getElementById('place-id-Kakao').value = place.id;
}

function setPlaceInfo() {
  const placeName = document.getElementById('placeName').value;
  const category = document.getElementById('category').value;
  const address = document.getElementById('address').value;
  const x = document.getElementById('x').value;
  const y = document.getElementById('y').value;
  let googlePlaceId = document.getElementById('place-id-Google').value;
  const kakaoPlaceId = document.getElementById('place-id-Kakao').value;

  if (!placeName || !address || !category || !x || !y || !kakaoPlaceId) {
    alert("googlePlaceId를 제외한 input을 채워주세요.");
    return;
  }

  if (!googlePlaceId && confirm("구글 Place Id가 없는 것이 맞습니까?")) {
    googlePlaceId = null;
  }

  placeInfo = {
    videoId: document.getElementById('videoId').value,
    placeName: placeName,
    category: category,
    address: address,
    x: x,
    y: y,
    googlePlaceId: googlePlaceId,
    kakaoPlaceId: kakaoPlaceId
  };
}

function registerPlace() {
  setPlaceInfo();
  $.ajax({
    url: `/places`,
    method: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(placeInfo),
    success: function () {
      alert("장소가 등록되었습니다.");
      closeModal();
      location.reload();
    },
    error: function () {
      alert("장소 등록에 실패했습니다.");
    }
  });
}

function deleteVideo(element) {
  if (!confirm("정말로 이 영상을 삭제하시겠습니까?")) {
    return;
  }
  const videoId = element.getAttribute("data-video-id");
  $.ajax({
    url: '/videos/' + videoId,
    method: 'DELETE',
    success: function () {
      alert("장소가 삭제되었습니다.");
      location.reload()
    },
    error: function () {
      alert("장소 삭제에 실패했습니다.");
    }
  });
}

