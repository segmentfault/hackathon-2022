$(function () {
    $('#empty').removeClass('hide');
    $('.list').hide();
    chrome.storage.sync.get(['urls'], function (result) {
        $(".list").children().remove();
        var str = "";
        let len = result.urls.length;
        if (len == 0) {
            $('#empty').removeClass('hide');
            $('.list').hide();
            return;
        }
        for (let i = 0; i < len; i++) {
            str += '<li draggable="true"><i class="icon" data-id="' + i + '">X</i><span data-url="' + result.urls[i].url + '">' + result.urls[i].title + '</span></li>';
        }
        $(".list").append(str);
        $('#empty').addClass('empty hide');
        $('.list').show();
    });

    $('.list').on('click', 'span', function () {
        var url = $(this).attr('data-url');
        chrome.tabs.create({
            url: url
        });
    })
    $('.list').on('click', 'i', function () {
        chrome.storage.sync.get(['urls'], function (result) {
            urls = result.urls;
        });
        let id = $(this).attr('data-id');
        urls.splice(id, 1);
        chrome.storage.sync.set({
            urls: urls
        });
        $(this).parent().remove();
        if (urls.length == 0) {
            $('#empty').removeClass('hide');
            $('.list').hide();
        }
    })

})