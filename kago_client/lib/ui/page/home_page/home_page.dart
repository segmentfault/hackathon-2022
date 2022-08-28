import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/ui/dialog/dialog_common_style.dart';
import 'package:kago_client/ui/page/home_page/home_controller.dart';
import 'package:flutter/material.dart';

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
        appBar: AppBar(
            title: const Text(
              "KAGo",
              style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
            ),
            actions: [
              IconButton(
                  onPressed: () {
                    showBottomSheet(

                        context: context,
                        backgroundColor: Colors.redAccent,
                        builder: (
                          context,
                        ) {
                          return Container(
                        height: 88,
                        width: 88,
                        color: Colors.blue,
                      );
                        });
                  },
                  icon: const Icon(
                    Icons.more_horiz,
                    size: 30,
                  ))
            ]),
        body: SafeArea(
          top: true,
          child: ListView.builder(
            padding: EdgeInsets.zero,
            shrinkWrap: true,
            itemCount: 10,
            itemBuilder: (BuildContext context, int index) {
              return Card(
                margin: const EdgeInsets.all(12),
                child: Container(
                  padding: const EdgeInsets.all(10),
                  child: Row(
                    // mainAxisAlignment: MainAxisAlignment.spaceBetween,

                    children: [
                      Container(
                        height: 88,
                        width: 88,
                        color: Colors.blue,
                      ),
                      Padding(
                        padding: const EdgeInsets.symmetric(horizontal: 10),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          mainAxisAlignment: MainAxisAlignment.spaceAround,
                          children: [
                            Text(
                              "打开全名称$index",
                              style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                            ),
                            const SizedBox(
                              height: 10,
                            ),
                            Text(
                              "打卡描述$index",
                              style: const TextStyle(fontSize: 14, fontWeight: FontWeight.normal),
                            )
                          ],
                        ),
                      ),
                      const Spacer(),
                      Text(
                        "时间",
                        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                      ),
                    ],
                  ),
                ),
              );
            },
          ),
        ));
  }
}
