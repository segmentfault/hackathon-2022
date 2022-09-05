import { service, utils } from '../framework';

export default {
  // 已卖出去的数量
  getProductSoldNumApi() {
    return service.postForm({
      url: '/user/api/product/sold/num',
      data: {
        productType: 1,
      },
    });
  },
  // 获取用户优惠券的数量
  getAvailPackageNumApi() {
    let userId = uni.getStorageSync('userInfo').userId;
    return service.postForm({
      url: '/basic/api/auth/coupon/getAvailPackageNum',
      data: {
        status: 1,
        userId,
      },
    });
  },
  // 我分享的链接购买券包的用户列表
  getMyShareListApi() {
    return service.postJson({
      url: '/user/api/auth/purchase/history/list/of/my/share',
      data: {
        pageNum: 1,
        pageSize: 100,
      },
    });
  },
  //收益明细
  getAwardDetail(pageNum) {
    return service.postJson({
      url: '/user/api/auth/user/yield/list',
      data: {
        pageNum,
        pageSize: 10,
      },
    });
  },
  //查看余额
  getBalance(userId) {
    return service.postJson({
      url: '/user/api/auth/user/balance',
      data: {
        userId,
      },
    });
  },
  //申请提现
  applyDeposit(userId, mobile, money) {
    return service.send({
      url: '/user/api/auth/user/withdrawal/apply',
      data: {
        userId,
        mobile,
        money,
      },
    });
  },
  //提现记录
  depositRcord(data) {
    return service.postJson({
      url: '/user/api/auth/user/settlement/list',
      data,
    });
  },
  queryUserInfo() {
    return service.postForm({
      url: '/user/api/auth/user/info',
    });
  },

  queryGameList(param) {
    //我的组局列表
    return service.postJson({
      url: '/game/api/auth/list',
      data: param,
    });
  },

  updateInfo(param) {
    //更新用户信息
    return service.postJson({
      url: '/user/api/auth/update',
      data: param,
    });
  },

  // 我得优惠券列表
  myCouponList(param) {
    return service.get({
      url: '/basic/api/auth/coupon/getUserCoupons',
      contentType: 'form',
      data: param,
    });
  },

  queryTagList() {
    //获取兴趣列表
    return service.postJson({
      url: '/user/api/user/interest/list',
      data: {},
    });
  },

  //用户的分销关系数量统计
  queryUserTotal() {
    return service.postJson({
      url: '/game/api/auth/relation/total/of/user',
    });
  },

  //用户的团队
  queryUserRelation(param) {
    return service.postJson({
      url: '/game/api/auth/distribution/relation/list/of/user',
      data: param,
    });
  },

  //获取优惠券核销状态
  queryCouponState(usedCode) {
    return service.get({
      url: '/basic/api/auth/coupon/getCouponUsedStatus',
      data: { usedCode },
    });
  },
  //获取用户消息设置状态
  getUserMessageInfo() {
    return service.postJson({
      url: '/user/api/auth/user/message/get',
    });
  },
  //更新用户消息设置
  updateUserMessage(param) {
    return service.postJson({
      url: '/user/api/auth/user/message/update',
      data: param,
    });
  },
  // 统一下单
  async unifiedOrder(param) {
    return service.send({
      url: '/user/api/auth/purchase/product/unified/order',
      contentType: 'form',
      data: param,
    });
  },
  // 购买记录
  updateHistoryList(param) {
    return service.postJson({
      url: '/user/api/auth/purchase/history/list',
      data: param,
    });
  },

  // 更新照片墙
  updatePicture(param) {
    return service.postJson({
      url: '/user/api/auth/update/picture',
      data: param,
    });
  },
  getIdentity() {
    return service.post({
      url: '/user/api/auth/user/identity',
    });
  },
  joinIdentity() { 
    return service.post({
      url: '/user/api/auth/user/join/hunter',
    });
  }
};
