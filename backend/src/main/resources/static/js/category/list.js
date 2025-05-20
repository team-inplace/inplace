async function expandSubCategory(element) {
    const isOpen = element.classList.contains("fa-chevron-up");
    const mainRow = element.closest("tr");
    if (!isOpen) {
        const parentId = element.getAttribute("data-category-id");

        try {
            const categories = await getSubCategories(parentId);
            categories.reverse().forEach(category => {
                    // 다음 row에 추가 하기
                    const subRow = document.createElement("tr");
                    subRow.className = "sub-row";
                    subRow.innerHTML =
                        `
                <td></td>
                <td>${category.id}</td>
                <td>${category.name}</td>
                <td>${category.engName}</td>
                <td>${category.parentId}</td>
                <td>
                    <a href="/admin/category/${category.id}/edit">
                        수정
                    </a>
                    <button type="button" onclick="deleteCategory(${category.id})">
                        삭제
                    </button>
                </td>

                `;
                    mainRow.insertAdjacentElement("afterend", subRow);
                }
            )
            element.classList.remove("fa-chevron-down");
            element.classList.add("fa-chevron-up");
        } catch (e) {
            console.error("카테고리 불러오기 실패:", e);
        }
        return;
    }

    let next = mainRow.nextElementSibling;
    while (next && next.classList.contains("sub-row")) {
        const toRemove = next;
        next = next.nextElementSibling;
        toRemove.remove();
    }
    element.classList.remove("fa-chevron-up");
    element.classList.add("fa-chevron-down");
}

function getSubCategories(parentId) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: `/places/categories/parent/${parentId}`,
            method: 'GET',
            contentType: 'application/json',
            success: function (categories) {
                resolve(categories);
            },
            error: function () {
                alert("서브 카테고리를 가져오는데 실패했습니다.");
                reject(new Error("카테고리 요청 실패"));
            }
        });
    });
}

function deleteCategory(categoryId) {
    if (!confirm("카테코리를 삭제하시겠습니까?")) {
        return;
    }
    $.ajax({
        url: `/places/categories/${categoryId}`,
        method: 'DELETE',
        contentType: 'application/json',
        success: function () {
            alert("카테고리를 삭제했습니다.");
            location.reload();
        },
        error: function () {
            alert("카테고리 삭제를 실패했습니다.");
        }
    });
}
