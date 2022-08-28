import 'package:kago_client/ui/page/home_page/home_binding.dart';
import 'package:kago_client/ui/page/home_page/home_page.dart';
import 'package:kago_client/ui/page/login_page/login_binding.dart';
import 'package:kago_client/ui/page/login_page/login_page.dart';
import 'package:kago_client/ui/page/main_page/main_binding.dart';
import 'package:kago_client/ui/page/main_page/main_page.dart';
import 'package:kago_client/ui/page/register_page/register_binding.dart';
import 'package:kago_client/ui/page/register_page/register_page.dart';
import 'package:kago_client/ui/page/splash_page/splash_binding.dart';
import 'package:kago_client/ui/page/splash_page/splash_page.dart';
import 'package:get/get.dart';
import 'package:kago_client/ui/page/template_page/template_binding.dart';
import 'package:kago_client/ui/page/template_page/template_page.dart';

/// @class : Routes
/// @name : liaoyp
/// @description :页面管理
abstract class Routes {
  ///入口模块
  static const String splashPage = '/splash';

  ///登录模块
  static const String loginPage = '/login';

  ///注册
  static const String registerPage = '/register';

  ///主页
  static const String homePage = '/home';

  /// 新建打卡群
  static const String roomTemplatePage = '/roomTemplatePage';

  ///用户信息模块
  static const String userInfoPage = '/userInfo';

  ///设置
  static const String settingPage = '/setting';

  ///语言
  static const String settingLanguagePage = '/language';

  ///页面合集
  static final routePage = [
    GetPage(name: splashPage, page: () => const SplashPage(), binding: SplashBinding()),
    GetPage(name: registerPage, page: () => const RegisterPage(), binding: RegisterBinding()),
    GetPage(name: homePage, page: () => const MainPage(), binding: MainBinding()),
    GetPage(name: roomTemplatePage, page: () => const TemplatePage(), binding: TemplateBinding()),
    GetPage(name: loginPage, page: () => const LoginPage(), binding: LoginBinding()),
  ];
}
