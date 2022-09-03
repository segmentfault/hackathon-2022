import 'package:kago_client/http/request_repository.dart';
import 'package:get/get.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';


/// @class : BaseGetController
/// @date : 2021/08/26
/// @name : liaoyp
/// @description :基类 Controller
class BaseGetController extends GetxController{


  ///HTTP请求仓库
  late RequestRepository request;


  @override
  void onInit() {
    super.onInit();
    request = Get.find<RequestRepository>();
  }



}

