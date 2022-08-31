/**
 * 每一个数组代表一天
 * count：课程节数
 * siesta：是否休息
 * 注：课程数据要和 index.js 中的times对应
 * */
var courseList = [
    //周一
    [{count:2, name:"高等代数", room:"3教203", bgcolor:"#AFEEEE"},
     {count:1},
     {count:1},
     {count:1},
     {count:1, siesta: 1},
     {count:2, name:"数学分析", room:"15教432", bgcolor:"#FFB6C1"},
     {count:1},
     {count:1},
     {count:1}
    ],

    [{count:2, name:"高等代数", room:"3教203", bgcolor:"#AFEEEE"},
     {count:2, name:"数据结构", room:"15教204", bgcolor:"#98FB98"},
     {count:1, siesta: 1},
     {count:1},
     {count:1},
     {count:1, name:"体育", room:"第4操场", bgcolor:"#E6E6FA"},
     {count:1},
     {count:1}
    ],

    [{count:1},
     {count:1},
     {count:2, name:"C语言", room:"5教102", bgcolor:"#A9A9A9"},
     {count:1, siesta: 1},
     {count:1},
     {count:2, name:"数学分析", room:"15教432", bgcolor:"#FFB6C1"},
     {count:1},
     {count:1},
     {count:1}
    ],

    [{count:2, name:"Java编程", room:"15教102", bgcolor:"#FA8072"},
     {count:1},
     {count:1},
     {count:1, siesta: 1},
     {count:2, name:"数据库", room:"5教302", bgcolor:"#7D9ECD"},
     {count:1},
     {count:1},
     {count:1}
    ],

    [{count:1, name:"英语听说", bgcolor:"#FADFA0", room:"多媒体3102"},
     {count:1},
     {count:1, name:"英语读写", bgcolor:"#7D9ECD", room:"多媒体3102"},
     {count:1},
     {count:1, siesta: 1},
     {count:1},
     {count:1},
     {count:2, name:"语文", bgcolor:"#FFB6C1", room:"多媒体3102"},
     {count:1}
    ],
    
    [{count:1, name:"数学", bgcolor:"#AFEEEE", room:"多媒体3102"},
     {count:3, name:"计算机", bgcolor:"#A9A9A9", room:"多媒体3102"},
     {count:1, siesta: 1},
     {count:1},
     {count:1},
     {count:1},
     {count:1},
     {count:1}
    ],

    [{count:1},
     {count:1, name:"英语", bgcolor:"#98FB98", room:"多媒体3102"},
     {count:1},
     {count:1},
     {count:1, siesta: 1},
     {count:2, name:"Java编程", bgcolor:"#FA8072", room:"多媒体3102"},
     {count:1},
     {count:1},
     {count:1}
    ],

]

module.exports = {
    courseList: courseList
}

