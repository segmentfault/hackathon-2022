console.log "this is translate_demo.js"
$ ->
    get_list = ()->
        a=document.getElementsByClassName("shop-all-list")[0].getElementsByTagName("ul")[0].getElementsByTagName("li")
        result = []
        for i in [0..a.length-1]
            link = a[i].getElementsByClassName("tit")[0].getElementsByTagName("a")[0].getAttribute("href")
            title = a[i].getElementsByClassName("tit")[0].getElementsByTagName("a")[0].getElementsByTagName("h4")[0].textContent
            result_item = {
                "link":link,
                "title":title,
            }
            result.push(result_item)
        result_str = JSON.stringify(result)
        $.ajax
            url: "http://192.168.100.90:8888/api/add"
            data:
                pll: result_str
            dataType: 'json'
            type: 'GET'
            success: (data)->
                console.log(data)
            error: (data)->
                console.log(data)
    # $(window).on "load",()->
    #     get_list()

    translate_content_card_show = false
    isChina = (s)-> 
        patrn=/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi
        if not patrn.exec s
            return false
        else
            return true

    hotpoor_translate_save = null
    hotpoor_translate = (content,fromLan,toLan)->
        clearTimeout hotpoor_translate_save
        hotpoor_translate_save = setTimeout ()->
            content_result = ""
            $("#translate_content_card_content_aim").empty()
            $("#translate_content_card_move_title").text "翻译中·Translating..."
            $.ajax
                url: "https://www.hotpoor.com/api/baiduai/translate"
                data:
                    content: content
                    lan: fromLan
                    to: toLan
                dataType: 'json'
                type: 'POST'
                success: (data) ->
                    console.log data
                    if data.result?
                        for result_i in data.result
                            content_results = result_i["trans_result"]
                            for result in content_results
                                content_result = """#{content_result}
                                    <div>#{result["dst"]}</div>
                                    """
                            $("#translate_content_card_content_aim").html content_result
                            $("#translate_content_card_move_count").html """#{content_result.length}/1000"""
                    $("#translate_content_card_move_title").text "翻译·Translate"
                error: (data)->
                    console.log data
                    $("#translate_content_card_move_title").text "翻译·Translate"
        ,300

    $("body").append """
        <div id="translate_content_card" style="display:none;">
            <div id="translate_content_card_move">
                <img src="https://www.hotpoor.com/static/img/translate.png">
                <span id="translate_content_card_move_title">翻译·Translate</span>
                <span id="translate_content_card_move_count">0/1000</span>
            </div>
            <div id="translate_content_card_tools" style="display:none"></div>
            <div id="translate_content_card_content">
                <div id="translate_content_card_content_aim" contenteditable="true"></div>
            </div>
        </div>"""
    translate_content_card_control = false
    translate_content_card_move = false
    translate_content_card_move_x = 0
    translate_content_card_move_y = 0
    mouse_x = null
    mouse_y = null
    mouse_move_x = null
    mouse_move_y = null
    $("body").on "mousedown", "#translate_content_card_move", (e)->
        e.stopPropagation()
        e.preventDefault()
        translate_content_card_move = true
        # translate_content_card_move_x = parseInt $("#translate_content_card").css("left")
        # translate_content_card_move_y = parseInt $("#translate_content_card").css("top")
        translate_content_card_move_x = parseInt $("#translate_content_card").css("right")
        translate_content_card_move_y = parseInt $("#translate_content_card").css("top")
        mouse_x = e.clientX
        mouse_y = e.clientY
    $("body").on "mouseup", "#translate_content_card", (e)->
        e.stopPropagation()
        e.preventDefault()
        translate_content_card_control = true
    $("body").on "mousedown", "#translate_content_card", (e)->
        translate_content_card_control = true

    $("body").on "mouseup", "#translate_content_card_onoff", (e)->
        console.log "body click translate_content_card_onoff hide"
        $("#translate_content_card").show()
        translate_content_card_show = true
        mouse_x_now = e.clientX
        mouse_y_now = e.clientY-15
        $("#translate_content_card").css
            "right":($(window).width()-mouse_x_now-260)+"px",
            "top":mouse_y_now+"px",
        $("#translate_content_card_onoff").remove()
    $(window).on "mousemove",(e)->
        if translate_content_card_move
            if e.which
                e.preventDefault()
                e.stopPropagation()
                mouse_move_x = e.clientX
                mouse_move_y = e.clientY
                move_value_x = mouse_move_x - mouse_x
                move_value_y = mouse_move_y - mouse_y
                $("#translate_content_card").css
                    # "left":(translate_content_card_move_x+move_value_x)+"px",
                    # "top":(translate_content_card_move_y+move_value_y)+"px",
                    "right":(translate_content_card_move_x-move_value_x)+"px",
                    "top":(translate_content_card_move_y+move_value_y)+"px",
            else
                translate_content_card_move = false

    getWord= ()->
        word = if window.getSelection then window.getSelection() else document.selection.createRange().text
        return word
    latest_translate_content = null
    
    window_mouseup_later = null
    $(window).on "mouseup",(e)->
        clearTimeout window_mouseup_later
        window_mouseup_later = setTimeout ()->
            translate_content = getWord().toString()
            if translate_content isnt ""
                if translate_content_card_show
                    if not translate_content_card_control
                        if translate_content isnt latest_translate_content
                            $("#translate_content_card_move_count").html """0/1000"""
                            translate_content_length = translate_content.length
                            if translate_content_length <= 2000
                                if isChina(translate_content)
                                    hotpoor_translate translate_content, "zh", "en"
                                else
                                    hotpoor_translate translate_content, "en", "zh"
                                latest_translate_content = translate_content
                            else if translate_content_length > 2000
                                $("#translate_content_card_content_aim").html "大哥大姐，内容太多翻得扛不住哟。<br>少一点吧~"
                                $("#translate_content_card_move_count").html """#{translate_content_length}/1000"""

                    translate_content_card_control = false
                    translate_content_card_move = false
                else
                    translate_content_card_show = false
                    $("#translate_content_card_onoff").remove()
                    $("#translate_content_card").hide()
                    mouse_x_now = e.clientX+3
                    mouse_y_now = e.clientY-15
                    $("body").append """
                        <div id="translate_content_card_onoff" style="left:#{mouse_x_now}px;top:#{mouse_y_now}px;">
                            <img src="https://www.hotpoor.com/static/img/translate.png">
                        </div>
                    """
                    translate_content_card_move = false
            else
                translate_content_card_show = false
                $("#translate_content_card_onoff").remove()
                $("#translate_content_card").hide()
                translate_content_card_control = false
                translate_content_card_move = false
        ,0
