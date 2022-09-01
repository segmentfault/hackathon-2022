//统一管理所有后端api接口路径
let api = {
  findMemberBaseInfoByCode : '/user/jscode2session',
  miniAppLogin : '/user/login',
  getToken : '/user/token',
  //发布商品
  issueShop: "/xianzhi/pub",
  //获取用户信息
  getUserDelites:"/user/getUserInfo",
  //获取所有商品列表
  getAllShop:"/xianzhi/goodsList",
  //查看与用户的聊天记录
  chattingRecords:"/chat_record",
  //删除商品
  deleteShop:"/xianzhi/del/",
  //修改商品
  choseShop:"/xianzhi/update",
  //查看自己的所有订单
  showMyShop:"/xianzhi/getMyGoods",
  //搜索商品
  searchShop:"/xianzhi/search",
  // 根据goodId 查看某一商品
  showShopDetailForgoodId:"/xianzhi/select",
  //发布文章
  vomitslotPub:"/vomitslot/pub",
  //查看所有吐槽贴
  getAllVomitslot:"/vomitslot/getAll",
  //查看某个吐槽贴
  vomitslotSelect:"/vomitslot/select",
  //点赞或取消点赞
  vomitslotLike:"/vomitslot/like",
  //查看评论详情
  vomitslotCommentDetail:"/vomitslot/commentDetail",
  //经纬度发送至服务器
  postLngLat:"/love/getNear"
};
export{api};