
import 'package:kago_client/base/get/controller/base_page_controller.dart';
import 'package:kago_client/util/ext/refresher_extension.dart';
import 'package:kago_client/widget/pull_smart_refresher.dart';
import 'package:pull_to_refresh/pull_to_refresh.dart';

/// @class : SquareController
/// @date : 2021/09/09
/// @name : liaoyp
/// @description :广场 控制器层
class SquareController extends BaseGetPageController {
  List projectData = [];

  ///请求积分明细
  @override
  void requestData(RefreshController controller, {Refresh refresh = Refresh.first}) {
    request.requestSquareModule(page, success: (data, over) {
      RefreshExtension.onSuccess(controller, refresh, over);

      ///下拉刷新需要清除列表
      if (refresh != Refresh.down) {
        projectData.clear();
      }
      projectData.addAll(data);
      showSuccess(projectData);
      update();
    }, fail: (code, msg) {
      showError();
      RefreshExtension.onError(controller, refresh);
    });
  }
}
