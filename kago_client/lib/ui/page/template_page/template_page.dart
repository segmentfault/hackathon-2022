import 'package:flutter/material.dart';
import 'package:kago_client/base/get/get_save_state_view.dart';
import 'package:kago_client/ui/page/template_page/template_controller.dart';

class TemplatePage extends GetSaveView<TemplateController> {
  
  const TemplatePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text("新建群打卡"),
      ),
      body: SafeArea(
          top: true,
          child: Padding(
            padding: const EdgeInsets.all(10.0),
            child: ListView(
              children: [
                const TitleSection(
                  text: "选择场景",
                  desc: "多种打卡场景，快速开始吧",
                ),
                GridGetView(),
              ],
            ),
          )),
    );
  }
}

class GridGetView extends StatelessWidget {
  List listData = [
    {"title": "标题1", "author": "内容1", "image": "https://www.itying.com/images/flutter/1.png"},
    {"title": "标题2", "author": "内容2", "image": "https://www.itying.com/images/flutter/2.png"},
    {"title": "标题3", "author": "内容3", "image": "https://www.itying.com/images/flutter/3.png"},
    {"title": "标题4", "author": "内容4", "image": "https://www.itying.com/images/flutter/4.png"},
    {"title": "标题5", "author": "内容5", "image": "https://www.itying.com/images/flutter/5.png"},
    {"title": "标题6", "author": "内容6", "image": "https://www.itying.com/images/flutter/6.png"},
  ];

  @override
  Widget build(BuildContext context) {
    return GridView.builder(
      gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
        //设置列数
        crossAxisCount: 2,
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
        return Image.network(
          listData[index]["image"],
          fit: BoxFit.cover,
        );
      },
    );
  }
}

class TitleSection extends StatelessWidget {
  final String text;
  final String desc;
  const TitleSection({Key? key, required this.text, required this.desc}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      // height: 66,
      alignment: Alignment.centerLeft,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            text,
            style: const TextStyle(
                fontSize: 26, fontWeight: FontWeight.bold, color: Colors.blueAccent),
          ),
          const SizedBox(height: 6,),
          Text(
            desc,
            style: const TextStyle(
                fontSize: 14, fontWeight: FontWeight.normal, color: Colors.blueAccent),
          ),
           const SizedBox(
            height: 15,
          ),
        ],
      ),
    );
  }
}
