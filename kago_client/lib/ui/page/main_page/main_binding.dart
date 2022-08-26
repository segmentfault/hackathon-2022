
import 'package:kago_client/ui/page/home_page/home_controller.dart';
import 'package:kago_client/ui/page/home_page/home_page.dart';
import 'package:kago_client/ui/page/my_page/my_controller.dart';
import 'package:get/get.dart';


/// @class : MainBinding
/// @date : 2022/08/26
/// @name : liaoyp
/// @description :首页 binding层
class MainBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => HomeController());
    Get.lazyPut(() => MyController());
    // Get.lazyPut(() => SquareController());
  }
}
