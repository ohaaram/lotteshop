$(document).ready(function () {
    $(".slider > ul").bxSlider({
      easing: "linear",
        auto: true,
      touchEnabled: false//슬라이더 이미지가 클릭 될 수 있게 함.
    });




    var best = $("aside > .best");

    $(window).scroll(function () {
      var t = $(this).scrollTop();

      if (t > 620) {
        best.css({
          position: "fixed",
          top: "0",
        });
      } else {
        best.css({ position: "static" });
      }
    });
});