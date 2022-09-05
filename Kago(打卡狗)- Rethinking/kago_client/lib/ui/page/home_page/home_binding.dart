import 'package:get/get.dart';
import 'home_controller.dart';


/// @class : HomeBinding
/// @date : 2022/08/26
/// @name : liaoyp
/// @description :首页 binding层
class HomeBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => HomeController());
  }
}
