import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/res/decoration_style.dart';
import 'package:kago_client/routes/routes.dart';
import 'package:kago_client/ui/dialog/dialog_common_style.dart';
import 'package:kago_client/ui/page/chat_page/chat_page.dart';
import 'package:kago_client/ui/page/home_page/home_controller.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kago_client/widget/logo_title.dart';

/// @class : HomePage
/// @date : 2022/08/26
/// @name : liaoyp
/// @description :我的 View层
class HomePage extends GetSaveView<HomeController> {
  const HomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Colors.white,
        appBar: AppBar(leading: LogoTitleWidget(), leadingWidth: 250,
            // title: LogoTitleWidget(),
            // const Text(
            //   "KAGo",
            //   style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            // ),
            actions: [
              IconButton(
                  onPressed: () {
                    Get.toNamed(Routes.roomTemplatePage);
                    // showBottomSheet(

                    //     context: context,
                    //     backgroundColor: Colors.redAccent,
                    //     builder: (
                    //       context,
                    //     ) {
                    //       return Container(
                    //     height: 88,
                    //     width: 88,
                    //     color: Colors.blue,
                    //   );
                    //     });
                  },
                  icon: Padding(
                    padding: const EdgeInsets.only(right: 12),
                    child: const Icon(
                      Icons.add,
                      size: 35,
                    ),
                  ))
            ]),
        body: SafeArea(
          top: true,
          child: ListView.builder(
              padding: EdgeInsets.zero,
              shrinkWrap: true,
              itemCount: 10,
              itemBuilder: (BuildContext context, int index) {
                return HomeRoomItem(
                  index,
                  onTap: () {
                    Get.toNamed(Routes.chatPage);
                  },
                );
              }),
        ));
  }
}

class HomeRoomItem extends StatelessWidget {
  final int index;
  final VoidCallback? onTap;
  const HomeRoomItem(this.index, {Key? key, this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(12),
        margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
        decoration: DecorationStyle.customizeBorder(
            radius: 8, color: Color.fromRGBO(189, 215, 239, 1)),
        child: Row(
          children: [
            Container(
              height: 88,
              width: 88,
              child: Image.asset(
                "assets/images/home_group_cover.png",
                // height: 34,
              ),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.symmetric(horizontal: 10),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          "考研小分队$index",
                          style: const TextStyle(
                              color: Color.fromRGBO(10, 14, 47, 1),
                              fontSize: 21,
                              fontWeight: FontWeight.bold),
                        ),
                        const Spacer(),
                        Text(
                          "09:58",
                          style: const TextStyle(
                              color: Color.fromRGBO(10, 14, 47, 0.3),
                              fontSize: 14,
                              fontWeight: FontWeight.normal),
                        ),
                      ],
                    ),
                    const SizedBox(
                      height: 10,
                    ),
                    Text(
                      "[16条]小衫：我刚打完卡，放松一下哈哈哈$index",
                      maxLines: 1,
                      style: const TextStyle(
                          color: Color.fromRGBO(10, 14, 47, 0.5),
                          fontSize: 14,
                          fontWeight: FontWeight.normal),
                    ),
                    const SizedBox(
                      height: 5,
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        Row(
                          children: [
                            Icon(
                              Icons.check_box,
                              color: Colors.grey,
                              size: 18,
                            ),
                            Text(
                              "16人打卡",
                              style: const TextStyle(
                                  color: Color.fromRGBO(10, 14, 47, 1),
                                  fontSize: 14,
                                  fontWeight: FontWeight.bold),
                            ),
                          ],
                        ),
                        const SizedBox(
                          width: 15,
                        ),
                        index % 2 == 0
                            ? Row(
                                children: [
                                  Icon(
                                    Icons.live_tv,
                                    size: 18,
                                    color: Color.fromRGBO(244, 95, 95, 1),
                                  ),
                                  Text(
                                    "7人直播中",
                                    style: const TextStyle(
                                        color: Color.fromRGBO(244, 95, 95, 1),
                                        fontSize: 14,
                                        fontWeight: FontWeight.normal),
                                  ),
                                ],
                              )
                            : SizedBox(),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
