import 'package:flutter/material.dart';
import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/ui/page/square_page/square_controller.dart';
import 'package:kago_client/widget/ripple_widget.dart';

/// @class : SquarePage
/// @date : 2021/9/9
/// @name : liaoyp
/// @description :发现 View层
class SquarePage extends GetSaveView<SquareController> {
  const SquarePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        top: true,
        child: ListView.builder(
          padding: EdgeInsets.zero,
          shrinkWrap: true,
          itemCount: controller.projectData.length,
          itemBuilder: (BuildContext context, int index) {
            return Material(
                color: Colors.transparent, child: Ripple(onTap: () {}, child: SquareItem()));
          },
        ),
      ),
    );
  }
}

class SquareItem extends StatefulWidget {
  SquareItem({Key? key}) : super(key: key);

  @override
  State<SquareItem> createState() => _SquareItemState();
}

class _SquareItemState extends State<SquareItem> {
  @override
  Widget build(BuildContext context) {
    return Card(
      child: Container(
        child: Text("test"),
      ),
    );
  }
}
