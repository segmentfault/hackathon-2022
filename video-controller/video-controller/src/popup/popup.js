document.addEventListener("DOMContentLoaded", function () {
  document.querySelector("#config").addEventListener("click", function () {
    // window.open("../option/options.html");
    chrome.runtime.openOptionsPage()
  });

  document.querySelector("#about").addEventListener("click", function () {
    window.open("https://github.com/AvailableForTheWorld/video-controller");
  });

  document.querySelector("#feedback").addEventListener("click", function () {
    window.open("https://github.com/AvailableForTheWorld/video-controller/issues");
  });

});
