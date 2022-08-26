import 'package:kago_client/base/get/get_save_state_view.dart';
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
    return  Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        top: true,
        child: ListView.builder(
            padding: EdgeInsets.zero,
            shrinkWrap: true,
            itemCount: 10,
            itemBuilder: (BuildContext context, int index) {
              return Card(
                margin: const EdgeInsets.all(10),
                child: Container(
                  height: 100,
                  child: Text("打开群 $index"),
                ),
              );
            },
          ),
      ));
  }
}
