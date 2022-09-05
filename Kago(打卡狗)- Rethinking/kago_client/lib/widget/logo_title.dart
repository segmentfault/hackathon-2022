import 'package:flutter/material.dart';

class LogoTitleWidget extends StatelessWidget {
  const LogoTitleWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          // SizedBox(
          //   width: 15,
          // ),
          Image.asset(
            "assets/images/title_logo.png",
            height: 34,
          ),
        ],
      ),
    );
  }
}
