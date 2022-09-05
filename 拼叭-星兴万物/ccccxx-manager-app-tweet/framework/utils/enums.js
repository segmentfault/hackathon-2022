// 枚举集合 TODO
export const enums = {
    // 优惠券状态
    couponState: [
        { label: '已核销', value: 0 },
        { label: '待使用', value: 1 },
        { label: '已取消', value: 2 },
        { label: '已过期', value: 3 }
    ],
    activeCenterType: [
        { label: '未报名', value: 0 },
        { label: '已报名', value: 1 }
    ],
	//剧本/商户类型
	sType:[
		{label:'欢乐',value:45},
		{label:'情感',value:74},
		{label:'恐怖',value:77},
		{label:'硬核',value:10},
		{label:'阵营',value:38},
		{label:'其他',value:19}
	],
	pepType:[
		{label:'4人以下',value:4},
		{label:'5人',value:5},
		{label:'6人',value:6},
		{label:'7人',value:7},
		{label:'8人',value:8},
		{label:'9人',value:9},
		{label:'10人以上',value:10}
	],
    scriptTypeList: [
        { label: '欢乐', value: 45 },
        { label: '情感', value: 1 },
        { label: '恐怖', value: 77 },
        { label: '硬核', value: 10 },
        { label: '阵营', value: 38 },
        { label: '其他', value: 19 }
    ],
    secretTypeList: [
        { label: '测试1', value: 125 },
        { label: '测试2', value: 126 },
        { label: '测试3', value: 127 },
    ],
    scriptTypeListPeple: [
        { label: '4人以下', value: 4 },
        { label: '5', value: 5 },
        { label: '6', value: 6 },
        { label: '7', value: 7 },
        { label: '8', value: 8 },
        { label: '9', value: 9 },
        { label: '10人以上', value: 10 }
    ],
}