import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'app/app_theme.dart';
import 'res/strings.dart';
import 'routes/routes.dart';
import 'ui/page/splash_page/splash_binding.dart';
import 'ui/page/splash_page/splash_page.dart';
import 'util/injection_init.dart';
import 'util/keyboard_util.dart';
import 'util/locale_util.dart';
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Injection.init();
  runApp(GetMaterialApp(
    getPages: Routes.routePage,
    debugShowCheckedModeBanner: false,
    initialRoute: '/',
    builder: (context, child) => Scaffold(
      // Global GestureDetector that will dismiss the keyboard
      body: GestureDetector(
        onTap: () {
          KeyboardUtils.hideKeyboard(context);
        },
        child: child,
      ),
    ),

    ///主题颜色
    theme: appThemeData,

    ///国际化支持-来源配置
    translations: Messages(),

    ///国际化支持-默认语言
    locale: LocaleOptions.getDefault(),

    ///国际化支持-备用语言
    fallbackLocale: const Locale('en', 'US'),

    defaultTransition: Transition.fade,
    initialBinding: SplashBinding(),
    home: const SplashPage(),
  ));
}
