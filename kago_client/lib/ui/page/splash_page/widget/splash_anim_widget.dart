import 'package:kago_client/base/get/get_common_view.dart';
import 'package:kago_client/res/r.dart';
import 'package:kago_client/res/strings.dart';
import 'package:kago_client/res/style.dart';
import 'package:kago_client/routes/routes.dart';
import 'package:kago_client/ui/page/splash_page/splash_controller.dart';
import 'package:kago_client/util/save/sp_util.dart';
import 'package:flutter/cupertino.dart';
import 'package:get/get.dart';

/// @class : SplashAnimWidget
/// @date : 2021/08/17
/// @name : liaoyp
/// @description :动画Widget
class SplashAnimWidget extends GetCommonView<SplashController> {
  const SplashAnimWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return AnimatedOpacity(
        onEnd: () {
          Get.offNamed(SpUtil.getUserInfo() == null
              ? Routes.loginPage
              : Routes.homePage);
        },
        opacity: controller.opacityLevel,
        duration: const Duration(milliseconds: 2000),
        child: Container(
          margin: const EdgeInsets.only(top: 120),
          alignment: Alignment.center,
          child: Column(
            children: [
              Image.asset(
                R.assetsImagesApplication,
                fit: BoxFit.fitWidth,
                width: 110,
                height: 110,
              ),
              Container(
                margin: const EdgeInsets.only(top: 16),
                child:  Text(
                  StringStyles.appName.tr,
                  style: Styles.style_black_36,
                ),
              ),
            ],
          ),
        ));
  }
}
