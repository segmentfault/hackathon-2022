import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'colors.dart';

/// @class : DecorationStyle
/// @date : 2021/9/6
/// @name : liaoyp
/// @description : 背景
class DecorationStyle {
  ///圆角背景
  ///背景：自定义
  ///圆角：自定义
  static BoxDecoration customize(Color color, double radius) {
    return BoxDecoration(
      color: color,
      borderRadius: BorderRadius.all(Radius.circular(radius)),
    );
  }

  static BoxDecoration fullPageGradient() {
    return const BoxDecoration(
        gradient: LinearGradient(
            colors: [
          Color.fromRGBO(111, 176, 244, 1),
          Color.fromRGBO(167, 208, 252, 1),
        ],
            stops: [
          0.0,
          1.0
        ],
            begin: FractionalOffset.topRight,
            end: FractionalOffset.bottomLeft,
            tileMode: TileMode.repeated));
  }

  ///圆角背景
  ///背景：阴影色
  ///圆角：30
  static BoxDecoration colorShadowRadius30() {
    return const BoxDecoration(
      color: ColorStyle.colorShadow,
      borderRadius: BorderRadius.all(Radius.circular(30)),
    );
  }

  ///圆角背景
  ///背景：自定义
  ///圆角：自定义
  static BoxDecoration customizeBorder(
      {Color color = Colors.black, double radius = 20, double border = 1}) {
    return BoxDecoration(
        border: Border.all(color: color, width: border),
        borderRadius: BorderRadius.circular((radius)));
  }

  /// 分割线
  ///背景：阴影色
  ///分割线：0.5
  static BoxDecoration colorShadowBorderStand() {
    return const BoxDecoration(
        border: Border(
            bottom: BorderSide(width: 0.5, color: ColorStyle.colorShadow)));
  }
}
