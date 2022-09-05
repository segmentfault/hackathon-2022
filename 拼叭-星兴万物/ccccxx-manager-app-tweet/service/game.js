import {
	service,
	utils
} from '../framework';

export default {
	//埋点
	getEventRecord(data) {
		return service.send({
			url: '/basic/api/event/record',
			data,
		});
	},
	// 获取打电话的号码
	getPhoneNumber({
		userId
	}) {
		return service.postForm({
			url: '/game/api/auth/get/phone/number',
			data: {
				toUserId: userId,
				userType: 1,
			},
		});
	},
	// 编辑组局
	editGroupBureau(data) {
		return service.postJson({
			url: '/game/api/auth/update',
			data,
		});
	},
	// 退出组局
	quitGroupBureau(gameId) {
		return service.send({
			url: '/game/api/auth/quit',
			data: {
				gameId,
			},
		});
	},
	/**
	 * 组局的分类信息
	 */
	typeList() {
		return service.send({
			url: '/game/api/game/type/list',
		});
	},

	// 发布组局
	createGame(p) {
		return service.postJson({
			url: '/game/api/auth/release',
			data: p,
		});
	},

	// 组局详情
	gameDetail(gameId) {
		return service.post({
			url: '/game/api/transform/detail',
			data: {
				gameId,
			},
		});
	},
	
	// 二维码
	ewmDetai(params) {
		return service.postJson({
			url: 'https://hsww.pinbar.vip/basic/hsww/mp/qrcode',
			data:params,
		});
	},

	// 加入组局
	joinGame(p) {
		return service.postForm({
			url: '/game/api/auth/join',
			data: p,
		});
	},

	// 审核加入组局的人
	audit(data) {
		return service.postForm({
			url: '/game/api/auth/audit',
			data,
		});
	},

	// 踢出组局
	kick(data) {
		return service.postForm({
			url: '/game/api/auth/kick',
			data,
		});
	},
	// 完成组局
	finish(gameId) {
		return service.postForm({
			url: '/game/api/auth/complete',
			data: {
				gameId,
			},
		});
	},
	// 商家列表
	getSupplierList(typeId, activityId = '', packageId = '') {
		return service.postJson({
			url: '/game/api/supplier/list',
			data: {
				pageNum: 1,
				pageSize: 100,
				typeId,
				activityId,
			},
		});
	},

	// 商家详情
	getSupplierDetail(supplierId) {
		return service.postForm({
			url: '/game/api/supplier/info',
			data: {
				supplierId,
			},
		});
	},

	// 活动报名
	applyActivity(data) {
		return service.postJson({
			loadingText: '报名中...',
			url: '/game/api/auth/activity/apply',
			data,
		});
	},
	// 修改活动报名
	applyActivityEdit(data) {
		return service.postJson({
			loadingText: '修改中...',
			url: '/game/api/auth/activity/info/update',
			data,
		});
	},
	// 取消报名
	cancelApplyActivity(activityId) {
		return service.postForm({
			loadingText: '正在取消...',
			url: '/game/api/auth/activity/apply/cancel',
			data: {
				activityId,
			},
		});
	},

	// 报名详情
	applyActivityDetail(activityId) {
		return service
			.postForm({
				url: '/game/api/auth/activity/apply/detail',
				data: {
					activityId: activityId,
				},
				isToastError: false,
			})
			.catch((err) => {
				if (err.code === 3) {
					// 尚未报名
					return Promise.resolve();
				} else {
					return Promise.reject(err);
				}
			});
	},

	// 获取活动报名参数
	getActiveParams(activityId) {
		return service.postForm({
			url: '/game/api/activity/apply/params',
			data: {
				activityId,
			},
		});
	},

	// 他人分享的报名详情
	getShareActivityDetail(userId, activityId) {
		return service.postForm({
			url: '/game/api/activity/apply/detail',
			data: {
				userId,
				activityId,
			},
		});
	},

	// 活动助力
	boost(activityUserId, activityUserType = 1, boostSource = 1, activityId) {
		return service.postForm({
			url: '/game/api/auth/activity/boost',
			loadingText: '助力中...',
			data: {
				activityUserId,
				activityUserType,
				boostSource,
				activityId,
			},
		});
	},

	/**
	 * 绑定关系
	 * parentType 1用户 2商家
	 * parentId 上级id
	 */
	bindRelation(row) {
		return service.postForm({
			url: '/game/api/auth/distribution/relation/bind',
			isToastError: false,
			data: row,
		});
	},

	// 获取助力的优惠券 1261助力券
	myCoupon(templateId) {
		return service.get({
			url: '/basic/api/auth/coupon/detail',
			data: {
				templateId,
			},
		});
	},

	// 活动信息
	queryActivityDetail(activityId = 1) {
		return service.post({
			url: '/game/api/activity/get/by/id',
			data: {
				activityId,
			},
		});
	},

	//弹幕
	queryBarrageList(params) {
		return service.post({
			url: '/game/api/auth/activity/boost/user/list',
			data: params,
		});
	},

	//获取二维码
	queryQRcode(params) {
		return service.postJson({
			url: '/basic/api/mini/program/qrcode',
			data: params,
		});
	},

	//打开二维码
	openQRcode(params) {
		return service.postJson({
			url: '/game/api/auth/activity/boost/face',
			data: params,
		});
	},

	//关闭二维码
	closeQRcode(params) {
		return service.postJson({
			url: '/game/api/auth/activity/boost/face/close',
			data: params,
		});
	},

	//轮询二维码
	askQRcode() {
		return service.postJson({
			url: '/game/api/auth/activity/boost/face/get',
		});
	},

	//商家人气榜
	querySupplierList(params) {
		return service.postJson({
			url: '/game/api/auth/activity/supplier/query',
			data: {
				pageNo: 1,
				pageSize: 20,
				...params,
			},
		});
	},

	//玩家人气榜
	queryUserList({
		pageNo = 1,
		pageSize = 5
	}) {
		return service.postJson({
			url: '/game/api/auth/activity/user/query',
			data: {
				pageNo,
				pageSize,
			},
		});
	},

	//助力列表
	queryBoostList(params) {
		return service.postJson({
			url: '/game/api/auth/activity/boost/query',
			data: params,
		});
	},

	//前一名用户信息
	queryLastUserList(params) {
		return service.postJson({
			url: '/game/api/auth/activity/user/get',
			data: params,
		});
	},

	//获取是否助力
	queryHasBoost(activityUserId, activityUserType) {
		return service.get({
			url: '/game/api/auth/activity/boost/get',
			data: {
				activityUserId,
				activityUserType,
			},
		});
	},

	//获取是否报名
	queryHasSignup(activityId = 1) {
		return service.get({
			url: '/game/api/auth/activity/user/is/signup',
			data: {
				activityId,
			},
		});
	},
	// 查询活动
	queryHasSignup(activityId = 1) {
		return service.get({
			url: '/game/api/auth/activity/user/is/signup',
			data: {
				activityId,
			},
		});
	},
	// 当前活动
	currentActivity() {
		return service.post({
			url: '/game/api/current/activity',
			data: {
				position: 'GAME',
			},
		});
	},
	//一键组局
	releaseActivity(activityId) {
		return service.post({
			url: '/game/api/auth/release/before/check/activity',
			data: {
				activityId: activityId,
			},
		});
	},
	// 商家列表
	getSupplierList(data) {
		return service.postJson({
			url: '/game/api/supplier/list',
			data,
		});
	},
	//商家详情
	getSupplierInfo(data) {
		return service.postForm({
			url: '/game/api/supplier/info',
			data,
		});
	},

	//照片助力
	pictureBoost(data) {
		return service.postForm({
			url: '/game/api/auth/picture/boost',
			data,
		});
	},

	//照片墙助力列表
	getPictureBoostList(data) {
		return service.postJson({
			url: '/game/api/auth/picture/boost/query/page',
			data,
		});
	},
	//照片墙列表
	getPictureList(data) {
		return service.postJson({
			url: '/game/api/auth/picture/page',
			data,
		});
	},
	// 获取活动日期
	getActivityDate(typeId) {
		return service.post({
			url: `/game/api/activity/date?typeId=${typeId}`,
		});
	},

	// 组局活动信息
	getGameActivity(gameId, gameUserId = null) {
		let apiUrl = `gameId=${gameId}`;
		if (gameUserId) {
			apiUrl = `${apiUrl}&gameUserId=${gameUserId}`;
		}
		return service.post({
			url: `/game/api/transform/game/activity/info?${apiUrl}`,
		});
	},
	// 组局助力
	activityBoot(userId, gameId, gameUserId, boostSource = 1) {
		return service.postForm({
			url: '/game/api/auth/game/activity/boost',
			loadingText: '助力中...',
			data: {
				userId,
				gameId,
				gameUserId,
				boostSource,
			},
		});
	},

	// 抽奖列表
	getLuckPage(params) {
		return service.postJson({
			url: `/game/api/auth/luck/page`,
			data: params,
		});
	},

	//抽奖详情
	getDrawDetail(data) {
		return service.postJson({
			url: '/game/api/auth/luck/get',
			data,
		});
	},

	//参与抽奖用户
	getDrawUser(data) {
		return service.postJson({
			url: '/game/api/auth/luck/user/page',
			data,
		});
	},
	//获取优惠券模板
	getconponList(data) {
		return service.post({
			url: '/basic/api/couponTemplates/by/templateIds',
			data,
		});
	},

	//参与抽奖用户
	getDrawUser(data) {
		return service.postJson({
			url: '/game/api/auth/luck/user/page',
			data,
		});
	},

	//获取中奖用户
	getPrizeUser(data) {
		return service.postJson({
			url: '/game/api/auth/luck/win/user/list',
			data,
		});
	},

	//抽奖
	draw(data) {
		return service.postJson({
			url: '/game/api/auth/luck/draw',
			data,
		});
	},
	//获取用户抽奖排名信息
	getuserhelp(data) {
		return service.postJson({
			url: '/game/api/auth/luck/help/by/user',
			data
		});
	},
	//获取抽奖助力列表
	gethelpList(data) {
		return service.postJson({
			url: '/game/api/auth/luck/help/page',
			data
		});
	},
	// 用户的抽佣团队
	brokerage(data) {
		return service.postJson({
			url: '/game/api/auth/brokerage/relation/list/of/user',
			data,
		});
	},
	// 用户的抽佣团队
	getShareInfo(data) {
		return service.post({
			url: '/game/api/transform/share/config',
			data,
		});
	},
	//助力
	assistance(data) {
		return service.postJson({
			url: '/game/api/auth/luck/help',
			data,
		});
	},
	//获取给我助力的用户
	helpMeUser(data) {
		return service.postJson({
			url: '/game/api/auth/luck/my/help/page',
			data,
		});
	},
};
