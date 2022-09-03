import 'package:kago_client/ui/page/login_page/login_controller.dart';
import 'package:get/get.dart';


/// @class : SplashBinding
/// @date : 2021/08/16
/// @name : liaoyp
/// @description :启动页 binding层
class LoginBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => LoginController());
  }
}
