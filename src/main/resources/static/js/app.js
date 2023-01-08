$(function () {
    $('a.deleteConfirm').click(function () {
      if (!confirm("삭제하시겠습니까?")) return false; // 취소 시 삭제안함
    })
});

$(function () {
  if($('#content').length){
    ClassicEditor.create(document.querySelector('#content')).catch((error) => {
      console.error(error);
    })
  }
  if($('#description').length){
    ClassicEditor.create(document.querySelector('#description')).catch((error) => {
      console.log(error);
    })
  }
})
