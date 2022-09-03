import 'package:kago_client/ui/page/login_page/login_controller.dart';
import 'package:kago_client/ui/page/register_page/register_controller.dart';
import 'package:get/get.dart';


/// @class : RegisterBinding
/// @date : 2021/08/16
/// @name : liaoyp
/// @description :启动页 binding层
class RegisterBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => RegisterController());
  }
}
