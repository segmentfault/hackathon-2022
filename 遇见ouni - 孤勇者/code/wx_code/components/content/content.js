// components/content/content.js
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        // title: {
        //     type: String,
        //     value: "标题"
        // },
        username: {
            type: String,
            value: "用户名"
        },
        time: {
            type: String,
            value: "发表时间"
        },
        content: {
            type: String,
            value: "正文"
        },
        like: {
            type: Number,
            value: 0
        },
        comment: {
            type: Number,
            value: 0
        },
    },

    /**
     * 组件的初始数据
     */
    data: {

    },

    /**
     * 组件的方法列表
     */
    methods: {

    }
})