/**
 * 常量管理
 */

import TIM from 'tim-wx-sdk';

const PAY_TYPES = [{ // 组局支付类型
	name: '我请客',
	value: 1
}, {
	name: '所有人AA',
	value: 2
}, {
	name: '男生AA',
	value: 3
}, {
	name: '女生AA',
	value: 4
}]

const COMPLETE_TYPES = [{ // 组局完成状态
	name: '待开始',
	value: 1
}, {
	name: '已开始',
	value: 2
}, {
	name: '已结束',
	value: 3
}, {
	name: '已解散',
	value: 4
},]

const MEMBER_STATES = { // 组局成员的状态
	JOINED: 1, // 已通过
	WAIT: 2, // 待通过
	EXIT: 3, // 自退
	REJECT: 4, // 未通过
	KICK: 5 // 被踢
}


export default {
	PAY_TYPES,
	COMPLETE_TYPES,
	MSG_TYPES: TIM.TYPES,
	MEMBER_STATES,
}

// COS 常量
export const COS_BUCKET_MAPS = {
	'IMAGE': 'image-1306191496'
}
export const COS_BUCKET = 'IMAGE'
export const COS_REGION = 'ap-nanjing'
