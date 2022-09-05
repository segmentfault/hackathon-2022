import { service } from '../framework';
import { COS_BUCKET } from '../constants';

export default {
  // 生成小程序码
  getMiniProgramQrcodeApi(data) {
    return service.postJson({
      url: '/basic/api/mini/program/qrcode',
      data,
    });
  },
  // 券包信息
  couponPackageListApi() {
    return service.postJson({
      url: '/basic/api/coupon/package/list',
      data: {
        pageNum: 1,
        pageSize: 100,
      },
    });
  },
  // 用户优惠券列表查询- 分页
  getUserCouponListApi(parmas) {
    // let userId = uni.getStorageSync('userInfo').userId
    return service.postJson({
      url: '/basic/api/auth/coupon/getUserCouponList',
      data: {
        // userId,
        ...parmas,
      },
    });
  },
  // 获取图片上传的签名
  async getCosAuth(key) {
    return service.send({
      url: '/basic/api/cos/getAuth',
      contentType: 'json',
      data: {
        bucket: COS_BUCKET,
        httpMethodName: 'PUT',
        filePath: key,
      },
    });
  },
  // 事件埋点
  async eventRecord(eventId, id) {
    const data = {
      userId: uni.getStorageSync('userInfo').userId,
      eventId: eventId,
    };
    // 文章id
    if (id) {
      data.articleId = id;
    }
    return service.send({
      url: '/basic/api/event/record',
      contentType: 'form',
      data,
    });
  },
  //获取坐标静态图
  getMapStaticImg(lat, lon) {
    return service.postJson({
      url: '/basic/api/map/static/img/get',
      data: {
        lat,
        lon,
      },
    });
  },
  //获取分享图片地址
  async getShareImg(parmas) {
    return service.send({
      isToastError: false,
      url: '/game/api/share/picture/get',
      data: { ...parmas },
    });
  },

  //保存分享图片
  async saveShareImg(parmas) {
    return service.postJson({
      url: '/game/api/share/picture/save',
      data: { ...parmas },
    });
  },
};
