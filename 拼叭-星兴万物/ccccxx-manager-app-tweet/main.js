import Vue from 'vue';
// 环境变量挂载
import framework, { store, utils } from './framework';

import App from './App';
const app = new Vue({
  ...App,
});
import login from './service/login.js';
import uView from 'uview-ui';
import Tabbar from '@/components/tabbar/tabbar.vue';

Vue.component('view-tabbar', Tabbar);
Vue.use(uView);
Vue.use(framework, {
  store: {
    im: require('./pages/im/lib/store.js').default,
  },
  hooks: {
    beforeHttp: (options) => {
      options.headers = {
        ...(options.headers || {}),
        'x-client-appid': 1,
      };
      let mergeHeader = () =>
        (options.headers = {
          ...(options.headers || {}),
          'x-client-ticket': uni.getStorageSync('userInfo').ticket,
          userId: uni.getStorageSync('userInfo').userId,
        });
      // 需要鉴权的接口或者埋点功能要求先登录
      if (
        options.url.includes('/api/auth') ||
        options.url.includes('/api/transform') ||
        options.url.includes('/event/record') ||
        options.url.includes('/api/purchase')
      ) {
        return login.defLogin().then(() => {
          mergeHeader();
          if (options.url.includes('/event/record') && !options.data.userId) {
            options.data.userId = uni.getStorageSync('userInfo').userId;
          }
        });
      }
    },
    resolveHttpSuccess: (res, options) => {
      'request => ', options, res;
      return options.returnAll ? res : res.data;
    },
    catchHttpError: (errMsg, options) => {
      // 处理下登录失效的 就删掉本地的ticket和用户信息
      if (errMsg && (errMsg.code === 500101 || errMsg.code == 16752)) {
        uni.setStorageSync('ticket', '');
        uni.setStorageSync('userInfo', '');
        uni.showToast({
          title: '您的登录已过期,请重新登录!',
          icon: 'none',
        });
        if (!uni.getStorageSync('loginPast')) {
          uni.setStorageSync('loginPast', 1);
          setTimeout(() => {
            uni.navigateTo({
              url: `/pagesA/user/login?isOnload=${true}`,
            });
          }, 1500);
        }
      }
      return Promise.reject(errMsg);
    },
    toastError: (text) => {
      uni.showToast({
        icon: 'none',
        title: text,
      });
    },
  },
});

app.$mount();

import avatar from './components/avatar.vue';
Vue.component('avatar', avatar);
import countDown from './components/count-down.vue';
Vue.component('countDown', countDown);

import constants from './constants.js';
Vue.mixin({
  computed: {
    $constants() {
      return constants;
    },
  },
});

// 格式化组局时间
Vue.prototype.$formatGameTime = function (times) {
  let DAY = new Date(times);
  let weekArr = ['日', '一', '二', '三', '四', '五', '六'];
  let md = utils.dayjs(DAY).format('MM/DD');
  let d = utils.dayjs(DAY).format('d');
  let t = utils.dayjs(DAY).format('HH:mm');
  return `${md} 周${weekArr[d]} ${t}`;
};
