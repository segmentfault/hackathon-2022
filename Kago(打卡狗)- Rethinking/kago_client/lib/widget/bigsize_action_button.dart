import 'package:flutter/material.dart';

class BigSizeActionButton extends StatelessWidget {
  final VoidCallback? onTap;
  final Color? color;
  final String text;

  const BigSizeActionButton(
    this.text, {
    Key? key,
    this.onTap,
    this.color,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 44,
      child: Center(
          child: Text(text,
              style: const TextStyle(
                color: Colors.white,
              ))),
      decoration: ShapeDecoration(
        color: color ?? Colors.redAccent,
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(
            Radius.circular(22.0),
          ),
        ),
      ),
    );
  }
}
