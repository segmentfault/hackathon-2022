import 'package:flutter/material.dart';
import 'package:kago_client/base/get/get_no_binding_view.dart';
import 'package:kago_client/res/style.dart';
import 'package:kago_client/ui/page/template_page/template_controller.dart';
import 'package:kago_client/widget/bigsize_action_button.dart';
import 'package:get/get.dart';

import '../../../res/colors.dart';

class CrateRoomPage extends GetNoView<TemplateController> {
  const CrateRoomPage({Key? key}) : super(key: key);

  @override
  Widget buildWidget(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color.fromRGBO(245, 245, 245, 1),
      appBar: AppBar(
        title: const Text("新建群打卡"),
      ),
      body: SafeArea(
          top: true,
          child: ListView(
            children: const [
              TitleSection(
                text: "基础信息",
              ),
              RowSection(
                text: "群名",
                desc: "打卡群",
              ),
              TitleSection(
                text: "打卡规则",
              ),
              RowSection(
                text: "打卡次数",
                desc: "1",
              ),
              RowSection(
                text: "打卡时间",
                desc: "0:600-24:00",
              ),
              RowSection(
                text: "打卡方式",
                desc: "图片&视频&直播",
              ),
              //-------
              TitleSection(
                text: "押金",
              ),
              RowSection(
                text: "群押金",
                desc: "🧧 10元",
              ),
              SizedBox(height: 40),

              Padding(
                  padding: EdgeInsets.symmetric(horizontal: 30),
                  child: BigSizeActionButton("新建"))
            ],
          )),
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
      padding: const EdgeInsets.symmetric(horizontal: 12),
      alignment: Alignment.centerLeft,
      child: Text(
        text,
        style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.blueAccent),
      ),
    );
  }
}

class RowSection extends StatelessWidget {
  final String text;
  final String desc;
  const RowSection({Key? key, required this.text, required this.desc}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 55,
      alignment: Alignment.centerLeft,
      padding: const EdgeInsets.symmetric(horizontal: 12),
      decoration: const BoxDecoration(
          color: Colors.white,
          border: Border(
              bottom: BorderSide(width: 0.5, color: ColorStyle.colorShadow),
              top: BorderSide(width: 0.5, color: ColorStyle.colorShadow))),
      child: Row(
        children: [
          Text(
            text,
            style: const TextStyle(
                fontSize: 16,
                fontWeight: FontWeight.normal,
                color: Color.fromRGBO(153, 153, 153, 1)),
          ),
          const Spacer(),
          Row(
            children: [
              Text(
                desc,
                style:
                    const TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.black),
              ),
              const SizedBox(
                width: 3,
              ),
              const Icon(
                Icons.arrow_forward_ios,
                size: 14,
              ),
            ],
          )
        ],
      ),
    );
  }
}
