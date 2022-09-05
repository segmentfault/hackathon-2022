import { service } from '../framework';

export default {
  // 商品详情
  getGoodsDetail(data) {
    return service.postJson({
      url: '/product/api/auth/goods/get/by/id',
      data,
    });
  },

  // 最大返佣
  getMaxPrice(data) {
    return service.postJson({
      url: '/product/api/auth/goods/max/price',
      data,
    });
  },

  // 可玩剧本
  getCanPlayPlay(data) {
    return service.postJson({
      url: '/product/api/auth/goods/page/by/supplier',
      data,
    });
  },
  // 发现页剧本
  getGoodsFindPage(data) {
    return service.postJson({
      url: '/product/api/auth/goods/find/page',
      data,
    });
  },
  //秒送页列表
  getSeckillPage(data) {
    return service.postJson({
      url: '/product/api/auth/kill/goods/supplier/page',
      data,
    });
  },
  // 商品日期价格
  getGoodsDate(data) {
    return service.post({
      url: '/product/api/goods/sku/date',
      data,
    });
  },

  // 创建订单
  createOrder(data) {
    return service.postJson({
      url: '/product/api/auth/goods/create/order',
      data,
    });
  },
  // 订单列表
  orderList(data) {
    return service.postJson({
      url: '/product/api/auth/goods/order/page',
      data,
    });
  },
  // 取消订单
  cancelOrder(data) {
    return service.postJson({
      url: '/product/api/auth/goods/cancel/order',
      data,
    });
  },
  // 订单详情
  orderDetail(data) {
    return service.postJson({
      url: '/product/api/auth/goods/order/detail',
      data,
    });
  },
  // 删除订单
  deleteOrder(data) {
    return service.postJson({
      url: '/product/api/auth/goods/delete/order',
      data,
    });
  },
  // 二次支付
  payment(data) {
    return service.postJson({
      url: '/product/api/auth/goods/pay/order',
      data,
    });
  },
  // 退款
  refund(data) {
    return service.postJson({
      url: '/product/api/auth/goods/refund/order',
      data,
    });
  },
  // 秒杀商品列表
  seckillList(data) {
    return service.postJson({
      url: '/product/api/auth/kill/goods/supplier/page',
      data,
    });
  },
  // 秒杀商品详情
  seckillDetail(data) {
    return service.postJson({
      url: '/product/api/auth/goods/kill/get/by/id',
      data,
    });
  },
	//获取订单短链
	getOrderChain(data) {
	  return service.postJson({
	    url: '/product/api/auth/order/get/write',
	    data,
	  });
	},
	//获取订单核销状态
	getOrderWirteStatus(data) {
	  return service.postJson({
	    url: '/product/api/auth/order/get/is/success',
	    data,
	  });
	},
};
