function openInfluencerEditModal(element) {
    const editId = element.getAttribute("data-id");
    const editJob = element.getAttribute("data-job");
    const editName = element.getAttribute("data-name");
    const editImg = element.getAttribute("data-img");

    const editImgElement = document.getElementById('editImg');
    document.getElementById('editId').value = editId;
    document.getElementById('editJob').value = editJob;
    document.getElementById('editName').value = editName;
    editImgElement.src = editImg;
    editImgElement.style.display = "none";
    document.getElementById('editModal').style.display = "block";
    currentOpenModalId='editModal'
}

function updateInfluencer() {
    const id = document.getElementById('editId').value;
    const formData = {
        influencerName: document.getElementById('editName').value,
        influencerImgUrl: document.getElementById('editImg').src,
        influencerJob: document.getElementById('editJob').value,
    };

    $.ajax({
        url: `/influencers/${id}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify(formData),
        success: function (response) {
            alert('인플루언서 정보가 수정되었습니다.');
            document.getElementById('editModal').style.display = 'none';
            location.reload();
        },
        error: function (xhr, status, error) {
            alert('수정 중 오류가 발생했습니다.');
            console.error(error);
        }
    });
}

function toggleStatus(id, currentStatus) {
    const message = currentStatus ?
        "공개하시겠습니까?" :
        "비공개하시겠습니까?";

    if (confirm(message)) {
        const status = !currentStatus;

        $.ajax({
            url: `/influencers/${id}/visibility`,
            type: 'PATCH',
            contentType: 'application/json',
            success: function (response) {
                location.reload();
            },
            error: function (xhr, status, error) {
                alert('상태 변경 중 오류가 발생했습니다.');
                console.error(error);
            }
        });
    }
}
