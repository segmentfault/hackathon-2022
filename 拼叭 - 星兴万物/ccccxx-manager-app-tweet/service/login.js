import { service, store } from '../framework';
import IM from '../pages/im/lib/index.js';

let globalLoginPromise, loginCbPromise, checkLoginPromise;

export default {
  // 静默登录
  async defLogin() {
    // 如果已经登录过了 则结束
    if (uni.getStorageSync('userInfo')) {
      return;
    }
    let login = await uni.login();
    let res = await service.send({
      isLogin: true,
      url: '/basic/api/mini/program/user/login/by/code',
      isToastError: false,
      data: {
        code: login[1].code,
      },
    });
    if (res && res.ticket) {
      store.commit('setMyUserTicket', res.ticket);
      store.commit('setMyUserId', res.userId);
      store.commit('setMyUserInfo', res);
      // 一旦登录 永久缓存
      uni.setStorageSync('userInfo', res);
      uni.setStorageSync('ticket', res.ticket);
      // IM.init()
    }
  },
  // 打开小程序时检查登录态
  checkLogin() {
    // checkLoginPromise = uni.login().then(login => {
    // 	return service.send({
    // 		isLogin: true,
    // 		url: '/basic/api/mini/program/user/login/by/code',
    // 		isToastError: true,
    // 		data: {
    // 			code: login[1].code
    // 		}
    // 	}).then(res => {
    // 		if (res && res.ticket) {
    // 			store.commit('setMyUserTicket', res.ticket)
    // 			store.commit('setMyUserId', res.userId)
    // 			store.commit('setMyUserInfo', res)
    // 			// 一旦登录 永久缓存
    // 			uni.setStorageSync('userInfo', res)
    // 			uni.setStorageSync('ticket', res.ticket)
    // 			// IM.init()
    // 		} else {
    // 			return Promise.reject('')
    // 		}
    // 	})
    // })
    // return checkLoginPromise
  },

  // 登录方法，没登录的话先跳登录页
  login() {
    if (store.state.user.myUserInfo) {
      return Promise.resolve();
    }
    return this.checkLogin().catch(() => {
      if (!globalLoginPromise) {
        globalLoginPromise = new Promise((resolve, reject) => {
          uni.navigateTo({
            url: '/pagesA/user/login',
          });
          store.commit('setMyUserTicket', res.ticket);
          store.commit('setMyUserId', res.userId);
          store.commit('setMyUserInfo', res);
          loginCbPromise = {
            resolve,
            reject,
          };
        }).catch(() => {
          globalLoginPromise = null;
          return Promise.reject('');
        });
      }
      return globalLoginPromise;
    });
  },

  getLoginCbPromise() {
    return loginCbPromise;
  },

  /**
   * 登录
   */
  sendLogin({ code, iv, encryptedData }) {
    return service.postJson({
      url: '/basic/api/mini/program/user/login2',
      isLogin: true,
      data: {
        code,
        iv,
        encryptedData,
        appid: uni.getAccountInfoSync().miniProgram.appId,
      },
    });
    // .then(res => {
    // 	store.commit('setMyUserTicket', res.ticket)
    // 	store.commit('setMyUserId', res.userId)
    // 	store.commit('setMyUserInfo', res)
    // 	uni.setStorageSync('myUserTicket', res.ticket)
    // 	uni.setStorageSync('userInfo', res)
    // 	// IM.init()
    // }).catch(e => {
    // 	("e", e)
    // })
  },

  /**
   * 绑定信息数据
   * @returns
   */
  bindInfo(data) {
    return service
      .send({
        url: '/user/api/auth/update',
        contentType: 'json',
        data: data,
      })
      .then((res) => {
        store.commit('setMyUserInfo', res);
        return res;
      });
  },

  /**
   * 获取登录人的用户信息
   */
  getUserInfo() {
    return service
      .send({
        url: '/user/api/auth/user/info',
      })
      .then((res) => {
        store.commit('setMyUserInfo', res);
        return res;
      });
  },
  /**
   * 获取用户信息根据ID
   */
  getUserInfoById(userId) {
    return service.send({
      url: '/user/api/user/info/by/userid',
      data: {
        userId,
      },
    });
  },
  // 微信登录，返回用户信息(ticket)
  getUserLoginInfo(code) {
    return service.post({
      url: '/basic/api/mini/program/user/login',
      data: {
        code,
      },
    });
  },

  //获取用户手机号
  getUserPhone(params) {
    return service.postJson({
      url: '/basic/api/mini/program/parse/mobile',
      data: params,
    });
  },

  getWxCode() {
    return new Promise((resolve, reject) => {
      uni.login({
        provider: 'weixin',
        success(res) {
          resolve(res.code);
        },
        fail(err) {
          reject(err);
        },
      });
    });
  },
};
