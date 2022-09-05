import {
	service,
	utils
} from '../framework';

export default {
	articleget(param) {
		return service.postJson({
			url: '/document/api/auth/article/get',
			data: param,
		});
	},
	getRecord(param) {
		return service.postJson({
			url: '/document/api/auth/vote/getRecord',
			data: param,
		});
	},
	addRecord(param) {
		return service.postJson({
			url: '/document/api/auth/vote/addRecord',
			data: param,
		});
	},
	praise(param) {
		return service.postJson({
			url: '/document/api/auth/article/praise',
			data: param,
		});
	},
	cancelPraise(param) {
		return service.postJson({
			url: '/document/api/auth/article/cancel/praise',
			data: param,
		});
	},
	message(param) {
		return service.postJson({
			url: '/document/api/auth/article/message/query',
			data: param,
		});
	},
		
	itemMessage(params){
		return service.postJson({
			url: '/document/api/auth/message/reply/query',
			data: params,
		});
	},
	messageAdd(param) {
		return service.postJson({
			url: '/document/api/auth/article/message/add',
			data: param,
		});
	},
	itemessageAdd(param) {
		return service.postJson({
			url: '/document/api/auth/article/reply/add',
			data: param,
		});
	},
	
	
	updateInfo(param) {
		//更新用户信息
		return service.postJson({
			url: '/user/api/auth/update',
			data: param,
		});
	}
};
