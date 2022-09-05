import hooks from '../utils/hooks.js'
import config from '../config.js'

const CONTENT_TYPE_MAP = {
	'json': 'application/json',
	'form': 'application/x-www-form-urlencoded'
};

export default {

	// 发送请求
	async send(options) {
		options = Object.assign({
			isToastError: true,
			contentType: "form",
			returnAll: false,
			loadingText: "",
			headers:{}
		}, options)
		options.loadingText && uni.showLoading({title: options.loadingText,mask: true});
		options.contentType = options.contentType;

		options.url = (options.url.indexOf('http://') === 0 || options.url.indexOf('https://') === 0) ? options.url : ((
			config.baseUrl) + options.url);
		await hooks.callHook('beforeHttp', options)
		return uni.request({
			url: options.url,
			method: options.method || 'post',
			timeout: options.timeout || 100000,
			header: {
				...options.headers,
				'content-type': CONTENT_TYPE_MAP[(options.contentType || 'json').toLowerCase()]
			},
			data: options.data
		}).then(res => {
			uni.stopPullDownRefresh()
			options.loadingText && uni.hideLoading()
			uni.stopPullDownRefresh()
			if (res[1]) {
				let resp = res[1].data || {}
				if (res[1].statusCode === 200 && resp.code==0) {
					return hooks.callHook('resolveHttpSuccess', resp, options)
				} else {
					if (resp.code === 16752){
						uni.reLaunch({
							url: '/pagesA/user/blacklist'
						});
					}
					if(resp.code === 8){
						options.isToastError && uni.showToast({icon: 'none', title:'登录失败,请重新登录!'})
					}else{
						options.isToastError && uni.showToast({icon: 'none', title: resp.msg || '网络请求失败'})
					}
					hooks.callHook('catchHttpError', resp, options)
					return Promise.reject(resp)
				}
			} else {
				res[0] && hooks.callHook('toastError', res[0].msg || res[0].errMsg || '网络请求失败');
				hooks.callHook('catchHttpError',  res[0], options)
				return Promise.reject(res[0])
			}
		}).catch(err => uni.showToast({icon: 'none',title:err.code == 8 ? '获取手机号失败,请重试!' : err.msg}))
	},

	get(options) {
		options.method = 'get';
		return this.send(options);
	},

	postJson(options) {
		options.contentType = 'json';
		return this.send(options);
	},

	postForm(options) {
		options.contentType = 'form';
		return this.post(options);
	},

	post(options) {
		options.method = 'post';
		return this.send(options);
	},

	put(options) {
		options.method = 'put';
		return this.send(options);
	},

	delete(options) {
		options.method = 'delete';
		return this.send(options);
	}

}
