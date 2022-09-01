import 'package:get/get.dart';
import 'chat_controller.dart';

/// @class : HomeBinding
/// @date : 2022/08/26
/// @name : liaoyp
/// @description :聊天 binding层
class ChatBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => ChatController());
  }
}
