import 'package:kago_client/res/shadow_style.dart';
import 'package:kago_client/ui/page/home_page/home_page.dart';
import 'package:kago_client/ui/page/my_page/my_page.dart';
import 'package:flutter/material.dart';
import 'package:kago_client/res/colors.dart';
import 'package:kago_client/res/strings.dart';
import 'package:get/get.dart';
import 'package:kago_client/ui/page/square_page/square_page.dart';

import 'widget/home_tab_title.dart';

/// @class : HomePage
/// @date : 2021/08/19
/// @name : liaoyp
/// @description :主页 View层
class MainPage extends StatefulWidget {
  const MainPage({Key? key}) : super(key: key);

  @override
  State<MainPage> createState() => HomeTabOptionsState();
}

class HomeTabOptionsState extends State<MainPage>
    with SingleTickerProviderStateMixin, WidgetsBindingObserver {
  ///控制器
  TabController? tabController;

  ///监听应用从后台切换到前台时，读取粘贴板中的数据，验证URL，已保存分享
  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    super.didChangeAppLifecycleState(state);
  }

  @override
  void initState() {
    tabController = TabController(length: 3, vsync: this);
    WidgetsBinding.instance?.addObserver(this);

    ///监听TabBar切换事件
    tabController?.addListener(() {
      var index = tabController?.index;

    });
  }

  @override
  void dispose() {
    super.dispose();
    tabController?.dispose();
    WidgetsBinding.instance?.removeObserver(this);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ColorStyle.color_F8F9FC,
      body: TabBarView(
        controller: tabController,
        children: const [HomePage(), SquarePage(), MyPage()],
      ),
      bottomNavigationBar: Container(
        // height: 50,
        padding: const EdgeInsets.only(bottom: 34),
        decoration: ShadowStyle.white12TopSpread4Blur10(radius: 0),
        child: TabBar(
          indicator: const BoxDecoration(),
          labelColor: ColorStyle.color_24CF5F,
          unselectedLabelColor: ColorStyle.color_B8C0D4,
          controller: tabController,
          tabs: [
            TabTitleIcon(
              title: StringStyles.homeComplex.tr,
              icon: Icons.turned_in,
            ),
            TabTitleIcon(
              title: StringStyles.homeProject.tr,
              icon: Icons.square,
            ),
            TabTitleIcon(
              title: StringStyles.homeMy.tr,
              icon: Icons.person,
            ),
          ],
        ),
      ),
    );
  }
}
