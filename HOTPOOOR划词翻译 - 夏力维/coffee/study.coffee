console.log "this is translate_demo_ver 2.js"
$ ->
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
                    content:content
                    lan:fromLan
                    to:toLan
                dataType:'json'
                type:'POST' 
                success:(data) ->
                    console.log data
                    if data.result?
                        for result_i in data.result
                            content_results = result_i["trans_result"]
                            for result in content_results
                                content_result = "#{content_result}#{result["dst"]}\n"
                            $("#translate_content_card_content_aim").val(content_result)
                    $("#translate_content_card_move_title").text "翻译 · Translate"
                error: (data) ->
                    console.log data
                    $("#translate_content_card_move_title").text "翻译 · Translate"
        ,300

    $("body").append """
        <div id="translate_content_card">
            <div id="translate_content_card_move">
                <img src="https://www.hotpoor.com/static/img/translate.png">
            </div>
            <div id="translate_content_card_tools" style="display:none"></div>
            <div id="translate_content_card_content">
                <textarea id="translate_content_card_content_aim"></textarea>
            </div>          
        </div>"""
    translate_content_card_move = false
    translate_content_card_move_x = 0
    translate_content_card_move_y = 0
    mouse_x = null
    mouse_y = null
    mouse_move_x = null
    mouse_move_y = null

    $("body").on "mousedown", "#translate_content_card_move",(e) ->
        e.stopPropagation()
        e.preventDefault()
        translate_content_card_move = true
        translate_content_card_move_x = parseInt $("#translate_content_card").css("left")
        translate_content_card_move_y = parseInt $("#translate_content_card").css("top")   
        mouse_x = e.clientX
        mouse_y = e.clientY

    $(window).on "mousemove", (e) ->
        if translate_content_card_move
            if e.which
                e.preventDefault()
                e.stopPropagation()
                mouse_move_x = e.clientX
                mouse_move_y = e.clientY
                move_value_x = mouse_move_x - mouse_x
                move_value_y = mouse_move_y - mouse_y

                $("#translate_content_card").css
                    "left":(translate_content_card_move_x+move_value_x)+"px",
                    "top":(translate_content_card_move_y+move_value_y)+"px",                    
            else
                translate_content_card_move = false



    getWord = () ->
        word = if window.getSelection then window.getSelection() else document.selection.createRange().text
        return word

    $(window).on "mouseup", (e) ->
        translate_content_card_move = false
        translate_content = getWord().toString()
        if translate_content.length > 0
            hotpoor_translate translate_content, "en", "zh"










