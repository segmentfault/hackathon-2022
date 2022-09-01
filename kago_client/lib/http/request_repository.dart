import 'package:kago_client/http/http_request.dart';
import 'package:kago_client/http/request.dart';
import 'package:kago_client/http/request_api.dart';
import 'package:kago_client/model/request_register.dart';
import 'package:kago_client/util/save/sp_util.dart';

typedef SuccessOver<T> = Function(T data, bool over);

class RequestRepository {
  ///登录请求
  /// [account]账号
  /// [password]密码
  /// [password]重复密码
  /// [success] 请求成功回调
  /// [fail] 请求失败回调
  register(
    String account,
    String password,
    String rePassword, {
    Success<UserEntity>? success,
    Fail? fail,
  }) {
    Request.post<dynamic>(RequestApi.apiRegister, {
      "account": account,
      "password": password,
      "repassword": rePassword
    }, success: (data) {
      var registerInfo = UserEntity.fromJson(data);
      registerInfo.password = password;
      SpUtil.putUserInfo(registerInfo);
      if (success != null) {
        success(registerInfo);
      }
    }, fail: (code, msg) {
      if (fail != null) {
        fail(code, msg);
      }
    });
  }

  login(
    String account,
    String password, {
    Success<UserEntity>? success,
    Fail? fail,
  }) {
    Request.post<dynamic>(
        RequestApi.apiLogin, {"account": account, "password": password},
        success: (data) {
      var loginInfo = UserEntity.fromJson(data);
      loginInfo.password = password;
      SpUtil.putUserInfo(loginInfo);
      if (success != null) {
        success(loginInfo);
      }
    }, fail: (code, msg) {
      if (fail != null) {
        fail(code, msg);
      }
    });
  }

  googlLogin(
    String openId,
    String email,
    String nickName, {
    Success<UserEntity>? success,
    Fail? fail,
  }) {
    Request.post<dynamic>(RequestApi.apiLogin, {
      "account": email,
      "openId": openId,
      "email": email,
      "nickName": nickName,
      "biz": "google_login",
    }, success: (data) {
      var loginInfo = UserEntity.fromJson(data);
      // loginInfo.password = password;
      SpUtil.putUserInfo(loginInfo);
      if (success != null) {
        success(loginInfo);
      }
    }, fail: (code, msg) {
      if (fail != null) {
        fail(code, msg);
      }
    });
  }
}
