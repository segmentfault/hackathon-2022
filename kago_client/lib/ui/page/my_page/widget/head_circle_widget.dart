import 'dart:math';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

/// @class : HeadCircleWidget
/// @date : 2021/08/23
/// @name : liaoyp
/// @description :圆角头像处理框
class HeadCircleWidget extends StatelessWidget {
  double width = 60;
  double height = 60;
  String? assetName;
  HeadCircleWidget(
      {Key? key, this.width = 60, this.height = 60, this.assetName})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ClipRRect(
      borderRadius: BorderRadius.circular(50),
      child: Container(
          width: width,
          height: height,
          color: Colors.white,
          child: Image.asset(assetName ?? 'assets/images/image 26.png')),
    );
  }
}
