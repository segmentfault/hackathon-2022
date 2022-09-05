root = exports ? this
# !!!! Hotpoor root object
root.Hs or= {}
Hs = root.Hs
hotpoor_ws = null
hotpoor_timestamp = null
hotpoor_ws_device = null
WS_DEVICE =
    UNKNOWN  : 0
    READY    : 1
    OPEN     : 2
    POST     : 3
    BAD      : 4

hotpoor_doc_move = false

hotpoor_doc_info_chrome = null
hotpoor_doc_list = []
hotpoor_doc_json = {}
hotpoor_doc_info = JSON.parse localStorage.getItem("hotpoor_doc_info")
if not hotpoor_doc_info?
    hotpoor_doc_info =
        "hotpoor_doc_page": "/"
        "hotpoor_doc_list": []
        "hotpoor_doc_json": {}
    localStorage.setItem "hotpoor_doc_info", JSON.stringify(hotpoor_doc_info)
hotpoor_doc_list = hotpoor_doc_info["hotpoor_doc_list"]
hotpoor_doc_json = hotpoor_doc_info["hotpoor_doc_json"]

localStorage_save = (hotpoor_doc_info, key, value)->
    hotpoor_doc_info[key] = value
    localStorage.setItem "hotpoor_doc_info", JSON.stringify(hotpoor_doc_info)
    return hotpoor_doc_info
localStorage_save_doc = (hotpoor_doc_info, doc_id)->
    sections = $(".doc_main>.section")
    hotpoor_doc_list = []
    for section in sections
        section_dom = $(section)
        _section_line_number = "#{section_dom.data("line_number")}"
        _section_line_content = section_dom.html()
        hotpoor_doc_list.push _section_line_number
        hotpoor_doc_json[_section_line_number] = _section_line_content
    for k,v of hotpoor_doc_json
        if not k in hotpoor_doc_list
            delete hotpoor_doc_json[k]
    hotpoor_doc_info["hotpoor_doc_list"] = hotpoor_doc_list
    hotpoor_doc_info["hotpoor_doc_json"] = hotpoor_doc_json
    localStorage.setItem "hotpoor_doc_info", JSON.stringify(hotpoor_doc_info)
    return hotpoor_doc_info

$ ->
    load_doc_list = ()->
        $(".doc_main").empty()
        for section in hotpoor_doc_list
            _html = hotpoor_doc_json[section]
            _dom = """
            <div class="section" data-line_number="#{section}" contenteditable="true">
                #{_html}
            </div>
            """
            $(".doc_main").append _dom
        if hotpoor_doc_list.length == 0
            _section = (new Date()).getTime()
            _dom = """
            <div class="section" data-line_number="#{_section}" contenteditable="true">
                <div contenteditable="false" class="section_root">键入以开始</div>
            </div>
            """
            $(".doc_main").append _dom

    load_doc_list()
    auto_save_doc_timer = null
    auto_save_doc_action = ()->
        clearTimeout auto_save_doc_timer
        auto_save_doc_timer = setTimeout ()->
            if $(".section").length == 0
                _section = (new Date()).getTime()
                _html 
                _dom = """
                <div class="section" data-line_number="#{_section}" contenteditable="true"><br>
                </div>
                """
                $(".doc_main").append _dom
            localStorage_save_doc(hotpoor_doc_info, null)
            if $(".section").first().content == ""
                $(".section").first().append """
                    <div contenteditable="false" class="section_root">键入以开始</div>
                """
            auto_save_doc_action()
        ,2000
    auto_save_doc_action()
    lastEditRange = null
    $(".doc_main").on "click", ".section_root", (evt)->
        $(this).remove()
    $(".doc_main").on "keydown",".section",(evt)->
        this_section = $(this).data("line_number")
        if evt.key is "Enter"
            evt.stopPropagation()
            evt.preventDefault()
            _section = (new Date()).getTime()
            _dom = """
            <div class="section" data-line_number="#{_section}" contenteditable="true"><br>
            </div>
            """
            $("[data-line_number=\"#{this_section}\"]").after _dom
            $("[data-line_number=\"#{_section}\"]").focus()
            return
    $(".doc_main").on "keyup",".section",(evt)->
        if evt.key is "Backspace"
            if $(this).html()==""
                $(this).remove()
                console.log "光标应该回到上一行的最后一个位置"
        console.log evt
        # selection = getSelection()
        # # 设置最后光标对象
        # lastEditRange = selection.getRangeAt(0)
        # console.log lastEditRange
    $(".doc_main").on "mousedown", (evt)->
        _section_now = null
        _dom = evt.target
        while not $(_dom).hasClass("section")
            _dom = $(_dom).parent()
        $(".section").removeClass("current_here")
        $(_dom).addClass("current_here")
    $(".doc_main").on "blur", (evt)->
        $(".section").removeClass("current_here")





