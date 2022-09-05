
/// @class : UserEntity
/// @date : 2022/08/26
/// @name : liaoyp
/// @description : 用户信息
class UserEntity {

  String account = '';
  String avatar = '';
  int id = 0;
  String nickname = '';
  String password = '';
  String token = '';
  int type = 0;
  String username = '';

  UserEntity({
    required this.account,
    required this.avatar,
    required this.id,
    required this.nickname,
    required this.password,
    required this.token,
    required this.type,
    required this.username,
  });

  UserEntity.fromJson(Map<dynamic, dynamic> json) {
    account = json["account"].toString();
    avatar = json["avatar"].toString();
    id = json["id"]?.toInt();
    nickname = json["nickName"].toString();
    password = json["password"].toString();
    token = json["token"].toString();
  }


  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data["account"] = account;
    data["avatar"] = avatar;
    data["id"] = id;
    data["nickName"] = nickname;
    data["password"] = password;
    data["token"] = token;
    return data;
  }
}
