import Vuex from 'vuex'
import service from './core/service.js'; // 用于发送请求和处理model层逻辑
import utils from './utils'
import common from './utils/common'
import config from './config.js'
import basicService from '../service/basic.js';
import store_user from './store/user.js' // vuex状态管理
import store_basic from './store/basic.js' // vuex状态管理
import LoginService from '../service/login.js'
import { enums } from './utils/enums'
let store = null;

export {
	utils,
	common,
	config,
	service,
	store
}
export default {
	install(vue, options) {
		// 挂载基础功能
		vue.prototype.$utils = utils
		vue.prototype.$config = config

		Object.keys(common).forEach(v => {
			vue.prototype[v] = common[v]
		})

		vue.use(Vuex)
		store = new Vuex.Store({
			modules: options.store ? {
				user: store_user,
				basic: store_basic,
				...options.store
			} : {
				user: store_user,
				basic: store_basic
			}
		})
		vue.prototype.$store = store

		if (options.hooks) {
			Object.keys(options.hooks).forEach(v => {
				utils.hooks.on(v, options.hooks[v])
			})
		}
		// 挂载埋点功能
		vue.prototype.$eventRecord = (eventId, id) => {
			if(id) { 
				basicService.eventRecord(eventId, id)
			} else { 
				basicService.eventRecord(eventId)
			}
			
		}
		// 挂载授权功能
		vue.prototype.$authorization = () => {
			let userInfo = uni.getStorageSync("userInfo") || {};
			// 注册来源埋点
			if (!userInfo.channel) vue.prototype.$eventRecord(1)
			if (!userInfo.mobile || !userInfo.head) {
				uni.showToast({icon:'none',title:'请先登录'})
				setTimeout(() => {
					uni.navigateTo({
						url: `/pagesA/user/login?isOnload=${true}`,
					});
				},1500)
				return false
			} else {
				return true
			}
		}
		// 挂载枚举功能
		vue.prototype.$getEnumsLabel = (enumsName, value) => {
			let current = enums[enumsName].filter(n => n.value === value)[0]
			if (current) {
				return current.label
			} else {
				return '未知'
			}
		}
		vue.mixin(require('./mixins/event'))
		vue.mixin(require('./mixins/user-info.js'))
		vue.mixin(require('./mixins/login.js'))
	}
}
