import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/res/colors.dart';
import 'package:kago_client/res/shadow_style.dart';
import 'package:kago_client/res/style.dart';
import 'package:kago_client/routes/routes.dart';
import 'package:kago_client/ui/page/my_page/my_controller.dart';
import 'package:kago_client/ui/page/my_page/widget/head_circle_widget.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

/// @class : MyPage
/// @date : 2021/08/20
/// @name : liaoyp
/// @description :我的 View层
class MyPage extends GetSaveView<MyController> {
  const MyPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(top: kTextTabBarHeight + 6),
      child: Column(
        children: [
          Row(
            children: [
              ///头像
              GestureDetector(
                onTap: () => Get.toNamed(Routes.userInfoPage),
                child: Container(
                  margin: const EdgeInsets.only(left: 24),
                  child: HeadCircleWidget(
                    width: 72,
                    height: 72,
                  ),
                  decoration: ShadowStyle.black12Circle40(),
                ),
              ),

              ///用户名称
              Container(
                margin: const EdgeInsets.only(left: 16),
                child: Text(
                  controller.userInfo.nickname,
                  style: Styles.style_1A2F51_18,
                ),
              ),

              ///占位
              const Expanded(
                child: Text(''), // 中间用Expanded控件
              ),

              ///设置
              GestureDetector(
                onTap: () => Get.toNamed(Routes.settingPage),
                child: ClipRRect(
                  borderRadius: const BorderRadius.only(
                    topLeft: Radius.circular(20),
                    bottomLeft: Radius.circular(20),
                  ),
                  child: Container(
                    color: ColorStyle.color_F3F3F3,
                    padding: const EdgeInsets.symmetric(vertical: 8, horizontal: 16),
                    child: const Icon(Icons.settings),
                  ),
                ),
              )
            ],
          ),
        ],
      ),
    );
  }
}
