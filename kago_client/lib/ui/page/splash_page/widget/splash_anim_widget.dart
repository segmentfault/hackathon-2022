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
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Image.asset(
                R.assetsImagesApplication,
                fit: BoxFit.fitWidth,
                width: 248,
                // height: 110,
              ),
              SafeArea(
                bottom: true,
                child: Container(
                  margin: const EdgeInsets.only(top: 16),
                  child: Text(
                    "RTE 2022",
                    style: Styles.style_white_10,
                  ),
                ),
              ),
            ],
          ),
        ));
  }
}
