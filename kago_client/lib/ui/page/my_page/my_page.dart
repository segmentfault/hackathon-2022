import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/res/colors.dart';
import 'package:kago_client/res/shadow_style.dart';
import 'package:kago_client/res/style.dart';
import 'package:kago_client/routes/routes.dart';
import 'package:kago_client/ui/page/my_page/my_controller.dart';
import 'package:kago_client/ui/page/my_page/widget/head_circle_widget.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kago_client/util/toast_util.dart';

/// @class : MyPage
/// @date : 2021/08/20
/// @name : liaoyp
/// @description :我的 View层
class MyPage extends GetSaveView<MyController> {
  const MyPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Scaffold(
        backgroundColor: Colors.grey.withAlpha(13),
        body: ListView(
          // crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const UserInfoWidget(),
            
            Padding(
              padding: const EdgeInsets.all(10.0),
              child: Column(children: [
                const SizedBox(
                    height: 20,
                  ),
                  const TitleSection(text: "荣誉勋章"),
                  Container(color: Colors.white, child: GridGetView()),
                  const TitleSection(text: "打卡记录"),
                  Container(color: Colors.white, child: TodoListView()),
                  const SizedBox(
                    height: 20,
                  ),
                  LogoutButton(
                    onTap: () {
                      ToastUtils.show("退出登录1");
                    },
                  ),
                  const SizedBox(
                    height: 20,
                  ),
              ],),
            )
          ],
        ),
      ),
    );
  }
}

class TitleSection extends StatelessWidget {
  final String text;
  const TitleSection({Key? key, required this.text}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 66,
      alignment: Alignment.centerLeft,
      child: Text(
        text,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
      ),
    );
  }
}

class LogoutButton extends StatelessWidget {
  final VoidCallback? onTap;

  const LogoutButton({Key? key, this.onTap}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 44,
      child: const Center(
          child: Text("退出登录",
              style: TextStyle(
                color: Colors.white,
              ))),
      decoration: const ShapeDecoration(
        color: Colors.redAccent,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(
            Radius.circular(22.0),
          ),
        ),
      ),
    );
  }
}

class GridGetView extends StatelessWidget {
  List listData = [
    {"title": "标题1", "author": "内容1", "image": "assets/images/Group 2029.png"},
    {"title": "标题2", "author": "内容2", "image": "assets/images/Group 2032.png"},
    {"title": "标题3", "author": "内容3", "image": "assets/images/Group 2037.png"},
    {"title": "标题4", "author": "内容4", "image": "assets/images/Group 2038.png"},
    {"title": "标题5", "author": "内容5", "image": "assets/images/Group 2039.png"},
    {"title": "标题6", "author": "内容6", "image": "assets/images/Group 2040.png"},
  ];


  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        //设置列数
        crossAxisCount: 3,
        //设置横向间距
        crossAxisSpacing: 10,
        //设置主轴间距
        mainAxisSpacing: 10,
      ),
      physics: const NeverScrollableScrollPhysics(),
      scrollDirection: Axis.vertical,
      itemCount: listData.length,
      shrinkWrap: true,
      itemBuilder: (context, index) {
        return Padding(
          padding: const EdgeInsets.all(12.0),
          child: Image.asset(
            listData[index]["image"],
            fit: BoxFit.contain,
          ),
        );
      },
    );
  }
}

class TodoListView extends StatelessWidget {
  List listData = [
    {"title": "考研小分队", "type": "视频打卡", "image": "https://www.itying.com/images/flutter/1.png"},
    {"title": "每天5公里", "type": "直播打卡", "image": "https://www.itying.com/images/flutter/2.png"},
    {"title": "每天5公里", "type": "视频打卡", "image": "https://www.itying.com/images/flutter/3.png"},
    {"title": "考研小分队", "type": "直播打卡", "image": "https://www.itying.com/images/flutter/4.png"},
    {"title": "每天5公里", "type": "视频打卡", "image": "https://www.itying.com/images/flutter/5.png"},
    {"title": "考研小分队", "type": "直播打卡", "image": "https://www.itying.com/images/flutter/6.png"},
  ];

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      physics: const NeverScrollableScrollPhysics(),
      scrollDirection: Axis.vertical,
      itemCount: listData.length,
      shrinkWrap: true,
      itemBuilder: (context, index) {
        return Container(
          padding: EdgeInsets.all(8),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                listData[index]["title"],
                style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              Column(
                // mainAxisAlignment: MainAxisAlignment.end,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Row(
                    // mainAxisAlignment: MainAxisAlignment.end,
                    // crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      Icon(Icons.video_call_rounded),
                      Text(
                        listData[index]["type"],
                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                    ],
                  ),
                  Text(
                    DateTime.now().toString().substring(0, 18),
                    style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                ],
              )
            ],
          ),
        );
      },
      separatorBuilder: (BuildContext context, int index) => const Divider(),
    );
  }
}

class UserInfoWidget extends StatelessWidget {
  const UserInfoWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: ColorStyle.color_FE8C28,
      padding: const EdgeInsets.symmetric(vertical: 30),
      child: Row(
        children: [
          ///头像
          GestureDetector(
            onTap: () => Get.toNamed(Routes.userInfoPage),
            child: Container(
              margin: const EdgeInsets.only(left: 24, right: 20),
              child: HeadCircleWidget(
                width: 72,
                height: 72,
              ),
              decoration: ShadowStyle.black12Circle40(),
            ),
          ),
          const SizedBox(
            height: 16,
          ),

          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                child: const Text(
                  //controller.userInfo.nickname ?? "18612184288",
                  "18612184288",
                  style: Styles.style_white_16_bold,
                ),
              ),
              const SizedBox(
                height: 10,
              ),
              Container(
                  // margin: const EdgeInsets.only(left: 16),
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: const [
                      DoubleCountText(
                        title: "完成打卡",
                        desc: "87",
                      ),
                      SizedBox(
                        width: 20,
                      ),
                      DoubleCountText(
                        title: "打卡群",
                        desc: "3",
                      ),
                      SizedBox(
                        width: 20,
                      ),
                      DoubleCountText(
                        title: "等级",
                        desc: "3",
                      ),
                    ],
                  )),
            ],
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
    );
  }
}

class DoubleCountText extends StatelessWidget {
  final String title;
  final String desc;
  const DoubleCountText({Key? key, required this.title, required this.desc}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text(
          title,
          style: Styles.style_white_12,
        ),
        Text(
          desc,
          style: Styles.style_white_16_bold,
        )
      ],
    );
  }
}
