$(function () {
    $('a.deleteConfirm').click(function () {
      if (!confirm("삭제하시겠습니까?")) return false; // 취소 시 삭제안함
    })
});
