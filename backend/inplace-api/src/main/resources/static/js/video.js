let placeRowIdx = 0

function openPlaceSearchModal(element) {
  let videoUrl = null;
  let registered = null;
  if (element) {
    window.selectedVideoId = element.getAttribute("data-video-id");
    videoUrl = element.getAttribute("data-video-url");
    registered = element.getAttribute("data-video-registered") === "true";
  }

  addModalContent(videoUrl);
  setRowClickSelect();
  currentOpenModalId = 'placeSearchModal';

  if (registered === true) {
    document.getElementById("modalTitle").innerText = "장소 수정";
    addPlacesByVideoId(selectedVideoId);
  }
}

function addModalContent(videoUrl) {
  document.getElementById('placeSearchModal').style.display = "block";
  document.getElementById(
      'videoIFrame').src = `https://www.youtube.com/embed/${videoUrl}`;
}

function setRowClickSelect() {
  $('#place-register-tbody').on('click', 'tr', function () {
    // 기존 선택 해제
    $('#place-register-tbody tr').removeClass('selected');

    // 현재 클릭한 row에 selected 클래스 추가
    $(this).addClass('selected');
  });
}

function addPlacesByVideoId(selectedVideoId) {
  $.ajax({
    url: `/places/videos/${selectedVideoId}`,
    method: 'GET',
    contentType: 'application/json',
    success: function(places) {
      places.forEach(place => {
        addPlaceRow(place);
      })
    },
    error: function () {
      alert("장소 정보를 불러오는 데 실패하였습니다.");
      closeModal();
    }
  });
}

function addPlaceRow(place = null) {
  const rowIdx = placeRowIdx++;
  const rowHtml = `
        <tr id="place-row-${rowIdx}">
            <td><input name="videoId" value="${selectedVideoId}" disabled></td>
            <td><input name="placeId" value="${place?.placeId ?? ''}" disabled></td>
            <td><input name="placeName" value="${place?.placeName ?? ''}"></td>
            <td>
                <select name="category" required>
                    <option disabled ${!place ? 'selected' : ''} value="">카테고리</option>
                </select>
            </td>
            <td><input name="address" value="${place?.address ?? ''}"></td>
            <td><input name="x" value="${place?.x ?? ''}"></td>
            <td><input name="y" value="${place?.y ?? ''}"></td>
            <td><input name="kakaoPlaceId" value="${place?.kakaoPlaceId ?? ''}"></td>
            <td><input name="googlePlaceId" value="${place?.googlePlaceId ?? ''}"></td>
        </tr>
    `;

  $('#place-register-tbody').append(rowHtml);

  populateCategoryOptions(rowIdx, place?.category);
}

function populateCategoryOptions(rowIdx, selectedCategory = '') {
  const select = $(`#place-row-${rowIdx} select[name="category"]`);

  categories.forEach(category => {
    const option = $('<option>')
    .val(category.id)
    .text(category.name);

    if (category.name === selectedCategory) {
      option.prop('selected', true);
    }

    select.append(option);
  });
}

function deletePlace(placeId) {
  $.ajax({
    url: `/places/${placeId}`,
    method: 'DELETE',
    contentType: 'application/json',
    success: function() {
      alert("장소 삭제에 성공하였습니다.");
    },
    error: function () {
      alert("장소 삭제에 실패하였습니다.");
    }
  });
}

function deletePlaceRow(row=null) {
  if (row) {
    row.remove();
    return;
  }

  const selectedRow = $('#place-register-tbody tr.selected');
  if (selectedRow.length === 0) {
    alert('삭제할 행을 먼저 선택하세요.');
    return;
  }

  // 존재하던 장소 삭제
  const placeId = selectedRow[0].querySelector('input[name="placeId"]').value;
  if (placeId && confirm("장소를 삭제하시겠습니까?")) {
    deletePlace(placeId);
  }

  selectedRow.remove();
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
          const selectedRow = $('#place-register-tbody tr.selected');
          if (selectedRow.length === 0) {
            alert('정보를 추가할 행을 먼저 선택하세요.');
            return;
          }
          setPlaceForm(
              JSON.stringify($(this).data("place")).replace(/'/g, "&#39;"),
              selectedRow
          );
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
            const selectedRow = $('#place-register-tbody tr.selected');
            if (selectedRow.length === 0) {
              alert('정보를 추가할 행을 먼저 선택하세요.');
              return;
            }
            selectedRow[0].querySelector('input[name="googlePlaceId"]').value = $(this).data(
                "place-id");
          });
    },
    error: function () {
      alert("검색 실패");
    }
  });
}

function setPlaceForm(place, row) {
  place = JSON.parse(place);

  const domRow = row[0];
  domRow.querySelector('input[name="placeName"]').value = place.place_name;
  domRow.querySelector('input[name="address"]').value = place.address_name;
  domRow.querySelector('input[name="x"]').value = place.x;
  domRow.querySelector('input[name="y"]').value = place.y;
  domRow.querySelector('input[name="kakaoPlaceId"]').value = place.id;
}

function setPlaceRegisterInfo(row) {
  const placeId = row.querySelector('input[name="placeId"]').value;
  const placeName = row.querySelector('input[name="placeName"]').value;
  const category = row.querySelector('select[name="category"]').value;
  const address = row.querySelector('input[name="address"]').value;
  const x = row.querySelector('input[name="x"]').value;
  const y = row.querySelector('input[name="y"]').value;
  let googlePlaceId = row.querySelector('input[name="googlePlaceId"]').value;
  const kakaoPlaceId = row.querySelector('input[name="kakaoPlaceId"]').value;

  if (!placeName || !address || !category || !x || !y || !kakaoPlaceId) {
    alert("googlePlaceId를 제외한 input을 채워주세요.");
    return null;
  }

  if (!googlePlaceId && confirm("구글 Place Id가 없는 것이 맞습니까?")) {
    googlePlaceId = null;
  }

  const placeInfo = {
    videoId: window.selectedVideoId,
    placeName: placeName,
    categoryId: category,
    address: address,
    x: x,
    y: y,
    googlePlaceId: googlePlaceId,
    kakaoPlaceId: kakaoPlaceId
  };
  return [placeId, placeInfo];
}

// 등록 함수: 하나의 row만 처리
function registerPlace(row) {
  let placeId = null;
  let placeInfo = null;
  [placeId, placeInfo] = setPlaceRegisterInfo(row);
  if (!placeInfo) return;
  if (placeId) {
    $.ajax({
      url: `/places/${placeId}`,
      method: 'PUT',
      contentType: 'application/json',
      data: JSON.stringify(placeInfo),
      success: function () {
        alert("장소가 수정되었습니다.");
      },
      error: function () {
        alert("장소 수정에 실패했습니다.");
      }
    });
    return;
  }
  $.ajax({
    url: `/places`,
    method: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(placeInfo),
    success: function () {
      alert("장소가 등록되었습니다.");
      if (document.getElementById("modalTitle").innerText !== "장소 수정") {
        deletePlaceRow(row);
      }
    },
    error: function () {
      alert("장소 등록에 실패했습니다.");
    }
  });
}

function registerSelectedPlace() {
  const selectedRow = document.querySelector('#place-register-tbody tr.selected');
  if (!selectedRow) {
    alert('등록할 행을 선택하세요.');
    return;
  }
  registerPlace(selectedRow);
}

function registerAllPlaces() {
  const rows = document.querySelectorAll('#place-register-tbody tr');
  if (rows.length === 0) {
    alert('등록할 행이 없습니다.');
    return;
  }
  rows.forEach(row => registerPlace(row));
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

