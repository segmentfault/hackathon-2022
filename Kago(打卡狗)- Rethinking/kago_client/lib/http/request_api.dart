
/// @class : RequestApi
/// @name : liaoyp
/// @description :请求接口管理
class RequestApi{

  ///前缀地址
  static const String baseurl = 'https://dev.dakago.liaoquanzhi.cn/';

  ///登录接口
  static const String apiLogin = 'api/sign/in';
  ///注册接口
  static const String apiRegister= 'api/sign/upByAccount';
  /// 直播间签到接口
  static const String apiRoomSignin = 'api/room/signin';
  ///直播间是否签到接口
  static const String apiRoomIsSign = 'api/room/isSign';
  ///创建直播间
  static const String apiRoomCreate= 'api/room/create';
  //加入直播间接口
  static const String apiRoomUserJoin= 'api/room/user/join';
}

