import { service, utils } from '../framework';

export default {
  /**
   * 组局的分类信息
   * @returns
   */
  typeList() {
    return service.send({
      url: '/game/api/game/type/list',
    });
  },

  /**
   * 首页banner
   */
  bannerList() {
    return service.send({
      url: '/basic/api/banner/list',
      data: {
        type: 1,
      },
    });
  },

  /**
   * 首页banner
   */
  bannerListBuy() {
    return service.send({
      url: '/basic/api/banner/list',
      data: {
        type: 2,
      },
    });
  },

  /**
   * 组局列表, 如果已登录可能有些许不同
   * @param {*} data 条件参数
   * @returns
   */
  gameList(data) {
    return service.postJson({
      url: '/game/api/transform/list',
      data,
    });
  },
  formatGameTime(times) {
    let DAY = new Date(times);
    let weekArr = ['日', '一', '二', '三', '四', '五', '六'];
    let md = utils.dayjs(DAY).format('MM/DD');
    let d = utils.dayjs(DAY).format('d');
    let t = utils.dayjs(DAY).format('HH:mm');
    return `${md} 周${weekArr[d]} ${t}`;
  },
  // 我加入的组局
  myJoin(data) {
    return service
      .postJson({
        url: '/game/api/auth/list',
        data,
      })
      .then((res) => {
        res.forEach((item) => {
          item.formatTime = this.formatGameTime(item.gameStartTime);
        });
        return res;
      });
  },
  // 我得优惠券列表
  myCouponList() {
    return service.get({
      url: '/basic/api/auth/coupon/getUserCoupons',
    });
  },
};
