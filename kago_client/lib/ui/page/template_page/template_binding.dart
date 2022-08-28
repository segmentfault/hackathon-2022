import 'package:get/get.dart';
import 'package:kago_client/ui/page/template_page/template_controller.dart';

class TemplateBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => TemplateController());
  }
}
