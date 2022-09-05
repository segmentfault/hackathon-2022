#include "calculator.h"
#include "snake.h"
#include <tchar.h>
using namespace std;

void SetColor(unsigned short ForeColor, unsigned short BackGroundColor) {
	HANDLE hCon = GetStdHandle(STD_OUTPUT_HANDLE);
	SetConsoleTextAttribute(hCon, (ForeColor % 16) | (BackGroundColor % 16 * 16));
	return;
}
char start[17][300] = { //开机载入界面
	{"┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐"},
	{" \\                                                                                                                         /"},
	{"  \\                                                                                                                       /"},
	{"   \\                                                                                                                     /"},
	{"    \\                                                                                                                   /"},
	{"     \\                                                               /~/~/                                             /"},
	{"      \\                                                             /~/~/                                             /"},
	{"       \\                                     Microsotf@             ~ ~     __ ——┐                                 /"},
	{"        \\                                    \\  /\\  / -  _   _|  _  _   _  /_      /                                /"},
	{"         \\                                    \\/  \\/  | | | |_| |_| \\/\\/  __/     /                                /"},
	{"          \\                                                                                                       /"},
	{"           \\                                        _____________________                                        /"},
	{"            \\                                      │                    │                                       /"}, //12行
	{"             \\                                     └────────────────────┘                                      /"},
	{"              \\                                                                                               /"},
	{"               \\  Copyright ΘMicrosoft Corporation                                            Microsoft*    /"},
	{"                \\___________________________________________________________________________________________/"}
};

char Map[41][300] = { //基本框架
	{"┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐"},
	{" \\                                                                                                                         /"},
	{"  \\                                                                                                                       /"},
	{"   \\                                                                                                                     /"},
	{"    \\                                                                                                                   /"},
	{"     \\                                                                                                                 /"},
	{"      \\                                                                                                               /"},
	{"       \\                                                                                                             /"},
	{"        \\                                                                                                           /"},
	{"         \\                                                                                                         /"},
	{"          \\                                                                                                       /"},
	{"           \\                                                                                                     /"},
	{"            \\                                                                                                   /"},
	{"             \\                                                                                                 /"},
	{"              \\                                                                                               /"},
	{"               \\                                                                                             /"},
	{"                \\___________________________________________________________________________________________/"},
	{"                ┌──────────────────────────────────────────────────────────────────────────────────────────┐"},
	{"                │                                                                                  【〓】  │"},//18行
	{"                │──────────────────────────────────────────────────────────────────────────────────────────│"},
	{"                │┌─────┐  ┌───┬───┬───┬───┐┌───┬───┬───┬───┐┌───┬───┬───┬───┐┌───┬───┬───┐                 │"},
	{"                ││ Esc │  │ F1│ F2│ F3│ F4││ F5│ F6│ F7│ F8││ F9│F10│F11│F12││Prt│Scr│Pau│ ┌┐  ┌┐  ┌┐  ┌┐  │"},//21行
	{"                │└─────┘  └───┴───┴───┴───┘└───┴───┴───┴───┘└───┴───┴───┴───┘└───┴───┴───┘ └┘  └┘  └┘  └┘  │"},
	{"                │┌────┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬─────┐┌───┬───┬───┐┌───┬───┬───┬───┐│"},
	{"                ││ `~ │ 1!│ 2@│ 3#│ 4$│ 5%│ 6^│ 7&│ 8*│ 9(│ 0)│ -─│ =+│ ←--││Ins│Hom│Pgu││Num│ / │ * │ - ││"},//24行
	{"                │├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬────┤├───┼───┼───┤├───┼───┼───┼───┤│"},
	{"                ││ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │ [{│ ]}│ \\| ││Del│End│Pgd││ 7 │ 8 │ 9 │   ││"},//26行
	{"                │├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴────┤└───┴───┴───┘├───┼───┼───┤ + ││"},
	{"                ││ Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │ ;:│ \'\"│ Enter │             │ 4 │ 5 │ 6 │   ││"},//28行
	{"                │├──────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴───────┤    ┌───┐    ├───┼───┼───┼───┤│"},
	{"                ││ Shift │ Z │ X │ C │ V │ B │ N │ M │ ,<│ .>│ /?│   Shift  │    │ ↑│    │ 1 │ 2 │ 3 │  │││"},//30行
	{"                │├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴───┴──┬┴───╪───┬──────┤┌───┼───┼───┐├───┴───┼───┤  │││"},
	{"                ││ Ctr│ win│ Alt│            ────           │ Alt│ Fn│ Ctrl ││← │ ↓│ →││  0ins │.de│←┘││"},//32行
	{"                │└────┴────┴────┴───────────────────────────┴────┴───┴──────┘└───┴───┴───┘└───────┴───┴───┘│"},
	{"                │               ┌───────────────────────────────┐                                          │"},
	{"                │               │                               │                                          │"},
	{"                │               │                               │                                          │"},
	{"                │               │───────────────┬───────────────│                                          │"},
	{"                │               └───────────────┴───────────────┘                                          │"},
	{"                └──────────────────────────────────────────────────────────────────────────────────────────┘"}
};

char password_load[17][300] = {//密码界面
	{"┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐"},
	{" \\                                                                                                                         /"},
	{"  \\  ------------------------------------------------------------------------------------------------------------------   /"},
	{"   \\                                                                                                                     /"},
	{"    \\                                                         |                                                         /"},
	{"     \\                                                        |                                                        /"},
	{"      \\                                                       |  ┌─────────────────────────────────────────────       /"},
	{"       \\                                           /~/~/      |  │┌─────┐  Administrator                             /"},
	{"        \\                                         /~/~/ __ __ |  ││ []│  输入密码                                 /"},
	{"         \\                \\  /\\  / -  _   _|  _   ~ ~  /_   / |  │└─────┘  【|                 】ck[→]            /"},
	{"          \\                \\/  \\/  | | | |_| |_| \\/\\/ __/     |  └───────────────────────────────────────         /"},
	{"           \\                                                  |                                                  /"},
	{"            \\                  要开始，请单击您的用户名       |                                                 /"},
	{"             \\                                                |                                                /"},
	{"              \\                                                                                               /"},
	{"               \\   ----------------------------------------------------------------------------------------  /"},
	{"                \\___________________________________________________________________________________________/"}
};

char ending[17][300] = {//关机界面
	{"┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐"},
	{" \\                                                                                                                         /"},
	{"  \\                                                      正在关机                                                         /"},
	{"   \\                                                                                                                     /"},
	{"    \\                                              请确保你的数据已保存                                                 /"},
	{"     \\                                                                                                                 /"},
	{"      \\                                                     0.0                                                       /"},
	{"       \\                                                                                                             /"},
	{"        \\                                               正在损失数据                                                /"},
	{"         \\                                                                                                         /"},
	{"          \\                                            电脑格式化成功                                             /"},
	{"           \\                                                                                                     /"},
	{"            \\                                           感谢使用  Windows7                                      /"},
	{"             \\                                                                                                 /"},
	{"              \\                                                                                               /"},
	{"               \\                                                                                             /"},
	{"                \\___________________________________________________________________________________________/"}
};

char desktop[17][300] = {//桌面
	{"┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐"},
	{" \\    ─────      ─────     ─────      ┌────┐    ┌────┐                                                //                   /"},
	{"  \\   \\     \\    \\    \\    \\    \\     │    │    │ing │                                    \\\\         //                   /"},
	{"   \\    ─────     ─────     ─────     └────┘    └────┘                                     \\\\       //                   /"},
	{"    \\   记事本     贪吃蛇    计算器     相关     待开发                           ##DDDDDDDDDDDDDDDDDDDDDD##            /"},
	{"     \\    ─────    ─────                                                          ## DDDDDDDDDDDDDDDDDDDD ##           /"},
	{"      \\   \\    \\   \\    \\                                                         ## hh                hh ##          /"},
	{"       \\   ─────    ─────------------ __ __                                       ## hh    //    \\\\    hh ##         /"},
	{"        \\  IE浏览器__猜数字     -------_ -------_------_ ___                      ## hh   //      \\\\   hh ##        /"},
	{"         \\   ____     __--__ -- __ -------_ --- - __ --- __ --------------______  ## hh                hh ##       /"},
	{"          \\__  -------_--__  ------- __ ---------- __ ------ __ ------ __ --- __ -## hh      wwww      hh ##      /"},
	{"           \\  -------_---__  ------- _---- __ -------_ ---_ -------_ -_ ----------## hh    Bilibili    hh ##     /"},
	{"            \\--__ -- __ ----- __ ------ __ -------_ ---  ---- __ -------_ -_ -----## MMMMMMMMMMMMMMMMMMMM ##    /"},
	{"             \\ -_--__  ------- _---- __ -------_ ------- __ -------_ -- __ -------##MMMMMMMMMMMMMMMMMMMMMM##   /"},
	{"              \\_________________________________________________________________________\\/_____________\\/____ /"},
	{"               \\ 开始 \\  qq2020   |  |____|                                                         |  12:00 /"},
	{"                \\___________________________________________________________________________________________/"}
};

char bilibili[14][200] = {//更多
	{"  \\                                        //        首先感谢bilibili!       "},
	{"   \\                           \\\\         //         去年暑假在B站刷到了一个关于虚拟笔记本的视频      "},
	{"    \\                           \\\\       //          这次的程序设计的灵感就来源于那个视频      "},
	{"     \\                  ##DDDDDDDDDDDDDDDDDDDDDD##   然后呢 我就想着自己不如去做做试试       "},
	{"      \\                 ## DDDDDDDDDDDDDDDDDDDD ##   然后我发现 真的要哭了┭┮﹏┭┮       "},
	{"       \\                ## hh                hh ##   BUG不断天天都在改BUG       "},
	{"        \\               ## hh    //    \\\\    hh ##   还有打印这些图形是真的不容易       "},
	{"         \\              ## hh   //      \\\\   hh ##   整整花了我一天       "},
	{"          \\             ## hh                hh ##   就一直在做视图┭┮﹏┭┮       "},
	{"           \\            ## hh      wwww      hh ##          "},
	{"            \\           ## hh    Bilibili    hh ##   不过呢做完以后还是很有成就感的       "},
	{"             \\          ## MMMMMMMMMMMMMMMMMMMM ##   也学到了很多       "},
	{"              \\         ##MMMMMMMMMMMMMMMMMMMMMM##   bilibili干杯！       "},
	{"               \\              \\/            \\/"}
};

int keyboard[20][30] = {
	//把键盘看成一个地图让move函数在上面移动返回的数值对应下面的 keyboard_place[120][5]是对应按键的全部信息
	//keybord[x][y]的值是keyboard_place[120][5]的某一行
	//与虚拟键盘的位置相照应 方便逻辑化处理
	{0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,   0,   0,   0,   0,   0},
	{0, 1,  1,  2,  3,  4,  5,  6,  7,  8,  9,  10, 11, 12, 13, 14, 15,  16,  0,   104, 104, 0,  0},
	{0, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32,  33,  34,  35,  36,  37, 0},
	{0, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53,  54,  55,  56,  57,  58, 0},
	{0, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 71, 0,  53,  0,   72,  73,  74,  58, 0},
	{0, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 86, 86, 0,  87,  0,   88,  89,  90,  91, 0},
	{0, 92, 93, 94, 95, 95, 95, 95, 95, 95, 95, 96, 97, 98, 98, 99, 100, 101, 102, 102, 103, 91, 0},
	{0, 0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,   0,   0,   0,   0,   0},
};

int keyboard_place[120][5] = {//第一项代表功能 第二项代表状态 第三项代表字符宽度 第四五项则为 x y坐标
	{0, 0, 0, 0,  0},        //第一项如果是字符那么那就是返回字符    如果是数字1-9那么就是功能按键功能对应如下
	{1, 1, 3, 21, 19},      //1-10
	{0, 0, 2, 21, 28},      //1 ~ 返回    2 ~删除     3确认   4shift  5上 6左 7下 8右  9开机
	{0, 0, 2, 21, 32},
	{0, 0, 2, 21, 36},
	{0, 0, 2, 21, 40},
	{0, 0, 2, 21, 45},
	{0, 0, 2, 21, 49},
	{0, 0, 2, 21, 53},
	{0, 0, 2, 21, 57},
	{0, 0, 2, 21, 62},
	{0, 0, 3, 21, 65}, // 11
	{0, 0, 3, 21, 69},
	{0, 0, 3, 21, 73},
	{0, 0, 3, 21, 78},
	{0, 0, 3, 21, 82},
	{0, 0, 3, 21, 86},
	{'`', '~', 2, 24, 19},//17
	{'1', '!', 2, 24, 24},
	{'2', '@', 2, 24, 28},
	{'3', '#', 2, 24, 32},
	{'4', '$', 2, 24, 36}, // 21
	{'5', '%', 2, 24, 40},
	{'6', '^', 2, 24, 44},
	{'7', '&', 2, 24, 48},
	{'8', '*', 2, 24, 52},
	{'9', '(', 2, 24, 56},
	{'0', ')', 2, 24, 60},
	{'-', '_', 2, 24, 64},
	{'=', '+', 2, 24, 68},
	{2, 2, 4, 24, 72},//30 删除键
	{0, 0, 3, 24, 78}, // 31
	{0, 0, 3, 24, 82},
	{0, 0, 3, 24, 86},
	{0, 0, 3, 24, 91},
	{'/', '/', 1, 24, 96},
	{'*', '*', 1, 24, 100},
	{'-', '-', 1, 24, 104},
	{0, 0, 3, 26, 19},
	{'q', 'Q', 1, 26, 25},
	{'w', 'W', 1, 26, 29},
	{'e', 'E', 1, 26, 33}, // 41
	{'r', 'R', 1, 26, 37},
	{'t', 'T', 1, 26, 41},
	{'y', 'Y', 1, 26, 45},
	{'u', 'U', 1, 26, 49},
	{'i', 'I', 1, 26, 53},
	{'o', 'O', 1, 26, 57},
	{'p', 'P', 1, 26, 61},
	{'[', '{', 2, 26, 65},
	{']', '}', 2, 26, 69},
	{'\\', '|', 2, 26, 73}, // 51
	{0, 0, 3, 26, 78},
	{0, 0, 3, 26, 82},
	{0, 0, 3, 26, 86},
	{'7', '7', 1, 26, 92},
	{'8', '8', 1, 26, 96},
	{'9', '9', 1, 26, 100},
	{'+', '+', 1, 27, 104},
	{0, 0, 4, 28, 19},
	{'a', 'A', 1, 28, 26},
	{'s', 'S', 1, 28, 30}, // 61
	{'d', 'D', 1, 28, 34},
	{'f', 'F', 1, 28, 38},
	{'g', 'G', 1, 28, 42},
	{'h', 'H', 1, 28, 46},
	{'j', 'J', 1, 28, 50},
	{'k', 'K', 1, 28, 54},
	{'l', 'L', 1, 28, 58},
	{';', ':', 2, 28, 62},
	{'\'', '\"', 2, 28, 66},
	{3, 3, 5, 28, 70}, // 71
	{'4', '4', 1, 28, 92},
	{'5', '5', 1, 28, 96},
	{'6', '6', 1, 28, 100},
	{4, 4, 5, 30, 19},
	{'z', 'Z', 1, 30, 27},
	{'x', 'X', 1, 30, 31},
	{'c', 'C', 1, 30, 35},
	{'v', 'V', 1, 30, 39},
	{'b', 'B', 1, 30, 43},
	{'n', 'N', 1, 30, 47}, // 81
	{'m', 'M', 1, 30, 51},
	{',', '<', 2, 30, 55},
	{'.', '>', 2, 30, 59},
	{'/', '?', 2, 30, 63},
	{4, 4, 5, 30, 69},
	{5, 5, 2, 30, 83},
	{'1', '1', 1, 30, 92},
	{'2', '2', 1, 30, 96},
	{'3', '3', 1, 30, 100},
	{3, 3, 3, 32, 103}, // 91
	{0, 0, 3, 32, 19},
	{0, 0, 3, 32, 24},
	{0, 0, 3, 32, 29},
	{' ', ' ', 4, 32, 45},
	{0, 0, 3, 32, 62},
	{0, 0, 2, 32, 67},
	{0, 0, 4, 32, 71},
	{6, 6, 2, 32, 78},
	{7, 7, 2, 32, 83},
	{8, 8, 2, 32, 87}, // 101
	{'0', '0', 4, 32, 93},
	{'.', '.', 4, 32, 100},
	{9, 9, 4, 18, 101}
};

int desktop_map[3][8] = { {0, 0, 0, 0, 0},
							{0, 1, 2, 3, 4},
							{0, 5, 6, 0, 0} }; //对应的移动操作记录
int desktop_place[10][2] = { {0, 0}, {2, 9}, {2, 19}, {2, 29}, {2, 40},{6,12},{6, 21} };//五个图标对应的坐标


int x, y, rex, rey, lx, ly, dx = 9, dy = 2, ldx, ldy, dex = 1, dey = 1;//x y 对应虚拟键盘的位置 rex,rey对应实际坐标 lx,ly之前的坐标  d...x,y屏幕光标的对应的变量 作用和前面的几个一样
int tx = 20, ty = 3, word_line = 0, maxlen = 80, word_len = 0, maxline = 10;//txt模块的坐标 文本行数
int cx = 22, cy = 4, posture_len = 0;//同上
char user_word[15][90];//记事本模块中加载的用户打印的文字
int if_input = 0, if_start = 0, if_desktop = 0, if_txt = 0, if_calculator = 0, if_shift = 0, if_guess = 0;//各个功能的开启状态记录
char password[20] = { "123" }, user_password[20] = { "\0" }, posture[50] = { "\0" };//默认密码 用户输入的密码
int num = 0, maxnum = 20;//已输入的密码位数  最大可支持的密码位数
int ans = 0;//猜数字游戏所生成的答案


void print_password() {//打印密码界面
	gotoxy(0, 0);//覆盖打印
	for (int i = 0; i < 18; i++) printf("%s\n", password_load[i]);
	return;
}
void input_passerword(char word) {//输入密码
	//获取用户要输入的字符并执行相应操作
	gotoxy(77 + num, 9);//定位光标
	if (num < maxnum) {//位数没有超过上限就可以在屏幕上显示密码
		cout << word;
		user_password[num] = word;//储存密码
		num++;//当前位数+1
	}
	else return;
	gotoxy(lx, ly);//恢复光标至虚拟键盘
	return;
}
void back() {//密码输入界面的删除
	num--;//位数-1
	user_password[num] = '\0';//将最后一位更新为空字符
	if (num < 0) num = 0;//防止越界
	gotoxy(77, 9);//回到密码框起始
	cout << "                 ";//用空格覆盖
	gotoxy(77, 9);
	printf("%s", user_password);//讲用户输入的密码再次显示

	return;
}
void print_start(int key = 1, int in_animation = 1, int in_password = 1) {//打印开始界面
	//可选三个参数 分别代表 是否需要隔行扫描的视觉效果  是否播放开机进度条动画  是否需要输入密码
	for (int i = 0; i < 5; i++) {
		printf("%s\n", start[i]);
		if (key) Sleep(100);//Sleep函数计时以ms为单位 100ms=0.1s
	}
	for (int i = 0; i < 69; i++) printf("%c", start[5][i]);
	int j = 68;

	//打印Windows7 标志
	//设置颜色 背景为红 前景为白
	SetColor(7, 4);
	printf("/~/");
	//背景为绿 前景为白
	SetColor(7, 2);
	printf("~/");
	//重设为灰白白背景 然后打印边框与空格
	SetColor(0, 7);
	printf("                                             /\n");
	if (key) Sleep(100);
	printf("      \\                                                             ");
	//背景为蓝 前景为白
	SetColor(7, 9);
	printf("/~/");
	//背景为黄 前景为白
	SetColor(7, 6);
	printf("~/");

	//Windows7 标志打印完毕 背景恢复为灰白色 打印后续内容
	SetColor(0, 7);
	printf("                                             /\n");
	for (int i = 7; i < 18; i++) {
		if (key) Sleep(100);
		printf("%s\n", start[i]);
	}

	//播放开机动画（伪）
	if (in_animation)
		for (int k = 0; k < 3; k++) {//打印三次
			gotoxy(52, 12);//进度条在12行 初始位置在52
			//起始动效
			cout << "■";
			Sleep(100);
			for (int i = 52; i < 70; i += 2) { //进度条长度为 21 中文字符进度条长度占位2个字符 每次自增2
				gotoxy(i, 12);
				for (int j = i - 4; j <= i - 2; j += 2) {
					cout << "■";
					Sleep(100);
				}
				gotoxy(52, 12);
				for (int j = 52; j <= i - 2; j++) cout << ' ';//更新掉用过的进度条
				Sleep(100);
			}
			//末尾动效调整
			gotoxy(68, 12);
			cout << "  ";
			Sleep(100);
			cout << "  ";
		}

	//打印密码界面
	if (in_password) print_password();

	return;
}
void print_init_op(int key = 1) {//默认会有间隔
	system("title 虚拟电脑");//设置cmd窗口标题
	system("mode con cols=130 lines=50");//设置窗口 高度和宽度
	system("color 70");

	//打印整体框架
	for (int i = 0; i < 41; i++) {
		printf("%s\n", Map[i]);
		if (key) Sleep(120);//ms 120 0.12s
	}
	return;
}
void init_shell(int key = 1, int if_pass = 0) {//初始化操作

	system("color 07");
	if (if_pass) {
		cout << "************\t使用前提示\t************" << endl;
		cout << "请通过 ↑↓←→ 和 空格进行操作" << endl;
		cout << "将光标移动到开机键点击空格即可开机！" << endl;
		printf("默认密码是：%s\n", password);
		cout << "桌面程序只有记事本可以运行" << endl;
		cout << "Windows7系统祝您使用愉快!" << endl;
		cout << "按任意键继续:" << endl;
		getch();
	}

	print_init_op(key);
	x = 1, y = 20;//模拟键盘中 开机键的位置
	rex = keyboard_place[keyboard[x][y]][4];//获取实际x坐标
	rey = keyboard_place[keyboard[x][y]][3];//获取实际y坐标
	gotoxy(rex, rey);

	return;
}
void load_desktop() {//打印桌面
	if (if_desktop == 0) {//若桌面状态没有开启
		if_desktop = 1;//更新状态
		gotoxy(0, 0);//覆盖打印
		system("color 70");
		for (int i = 0; i < 18; i++) {
			printf("%s\n", desktop[i]);
			Sleep(50);
		}
		gotoxy(dx, dy);//转到模块对应行
		cout << "[-]";//打印状态图标
	}
	else if_desktop = 0;//更新状态
	return;
}
void print_ending(int key = 1) {//打印关机界面
	gotoxy(0, 0);//覆盖打印
	for (int i = 0; i < 17; i++) printf("%s\n", Map[i]);
	gotoxy(0, 0);//覆盖打印
	for (int i = 0; i < 17; i++) {
		if (key) Sleep(1000);
		printf("%s\n", ending[i]);
	}
	Sleep(2000);//暂停2s 后刷新屏幕
	gotoxy(0, 0);
	for (int i = 0; i < 17; i++) printf("%s\n", Map[i]);
	return;
}
void load_more() {//加载更多
	gotoxy(0, 0);
	for (int i = 0; i < 17; i++) printf("%s\n", Map[i]);
	gotoxy(0, 2);
	for (int i = 0; i < 14; i++) {
		printf("%s\n", bilibili[i]);
		Sleep(500);
	}
	return;
}
void load_txt() {//加载记事本模块
	if (if_txt == 0) {
		gotoxy(0, 0);
		for (int i = 0; i < 17; i++) {
			printf("%s\n", Map[i]);
			Sleep(100);
		}
		if_txt = 1;//更新状态
		gotoxy(18, 3);//覆盖打印
		cout << "请写下你想写下的话：";
		if (user_word[0][0] != '\0') {//若之前有过使用记录则将之前的输入全部打印
			for (int i = 0; i < word_line; i++) {
				gotoxy(20, ty + i);
				printf("%s\n", user_word[i]);

			}
		}
		ty++;//更新光标位置
	}
	else if_txt = 0;
	gotoxy(rex, rey);
	return;
}
void input_txt(char word, int back = 0) {
	gotoxy(tx, ty);
	if (word_line < maxline && word_len < maxlen && back == 0) {
		tx++;//光标右移
		user_word[word_line][word_len] = word;//存入字符
		word_len++;//长度增加
		cout << word;
	}
	else if (back == 1) {
		word_len--;
		user_word[word_line][word_len] = '\0';
		if (word_len < 0) {//换行处理
			word_len = maxlen - 1;//长度变更为最大长度-1
			ty--;//横坐标-1
			tx = 20 + maxlen - 1;//纵坐标更新
			gotoxy(tx, ty);
			cout << "  ";
		}
		else {
			gotoxy(tx - 1, ty);
			cout << "  ";
			tx--;
		}
	}
	if (word_len >= maxlen) {//若此行字数已满
		word_len = 0;//更新字数
		word_line++;//行数+1
		tx = 20;//更新坐标
		ty++;
		gotoxy(tx, ty);
	}
	if (!back)cout << "_";//打印状态符_
	gotoxy(rex, rey);//返回虚拟键盘

	return;
}
void load_calculator(char word, int back = 0) {
	if (if_calculator == 0) {//若首次加载计算器模块则打印相应提示
		gotoxy(0, 0);
		//打印整体框架
		for (int i = 0; i < 17; i++) {
			printf("%s\n", Map[i]);
			Sleep(50);
		}
		gotoxy(20, 3);
		cout << "请写下要计算的式子:(写完只有记得敲一下回=才会生成结果哦~)";
		if_calculator = 1;//更新状态
	}
	else {
		gotoxy(cx, cy);
		if (word == '=') {
			cout << " = " << calculator(posture);//计算后输出结果
		}
		else {
			if (back == 0) {//输入操作
				cx++;//光标右移
				posture[posture_len] = word;//存入字符
				posture_len++;//长度增加
				cout << word;
			}
			else {//删除操作
				posture_len--;//式子长度-1
				posture[posture_len] = '\0';//更新式子
				gotoxy(cx - 1, cy);//移动光标
				cout << "  ";//覆盖打印
				cx--;//更新坐标
			}
		}
	}

	gotoxy(rex, rey);//光标返回虚拟键盘

	return;
}
int guess_number(int number) {
	if (if_guess == 0) {//若首次加载猜数字模块则打印相应提示
		gotoxy(0, 0);
		//打印整体框架
		for (int i = 0; i < 17; i++) {
			printf("%s\n", Map[i]);
			Sleep(50);
		}
		gotoxy(20, 3);
		cout << "欢迎来到猜数字小游戏";
		gotoxy(20, 4);
		cout << "答案在0~10之间哦，选好数字之后敲下空格确认答案哦~";
		if_guess = 1;//更新状态
	}
	else {
		gotoxy(cx, cy + 1);
		int num = number - '0';
		cout << num << "   ";
		if (num > ans) cout << '(' << "猜大了猜大了" << ')';
		else if (num == ans) {
			cout << '(' << "恭喜你 猜对了！(*^▽^*)" << ')';
			return 1;
		}
		else cout << '(' << "猜小了猜小了" << ')';
	}
	return 0;
}

int move(int key = 1, int in_animation = 1, int in_password = 1) {

	int ch = 0;

	while (ch != 32) {
		lx = x, ly = y;//记录前一个状态
		ch = getch();
		switch (ch) {
	  		case 0xe0:
				switch (getch()) {
				case 72://上
					x--;
					break;
				case 75://左
					y--;
					break;
				case 80://下
					x++;
					break;
				case 77://右
					y++;
					break;
				}
				break;
			case 27://esc键
				return 0;
				break;
			break;
		}

		//键位的智能移动
		if (keyboard[x][y] == 0)  x = lx, y = ly;//恢复到前一个状态 动态黏贴
		rex = keyboard_place[keyboard[x][y]][4];//获取实际x坐标
		rey = keyboard_place[keyboard[x][y]][3];//获取实际y坐标
		gotoxy(rex, rey);
	}

	//判断shift的状态
	if (keyboard_place[keyboard[x][y]][0] == 4 && ch == 32) {//光标停留在shift键且按下了空格 视为更新shift
		if (if_shift == 1) if_shift = 0;
		else if_shift = 1;
	}


	//判断开机
	if (x == 1 && (y == 19 || y == 20) && ch == 32 && if_start == 0) {  //此时光标停留在开机键 未开机 且 按下了空格 视为开机
		if_start = 1;	//更新开机状态
		if_input = 1;	//更新输入状态
		gotoxy(0, 0);//光标回到最初
		print_start(key, in_animation, in_password);//播放开机画面
		gotoxy(rex, rey);//恢复光标位置
	}//判断关机
	else if (x == 1 && (y == 19 || y == 20) && ch == 32 && if_start == 1) {//此时光标停留在开机键 已开机 且 按下了空格 视为关机
		if_start = 0;		//更新开机状态
		if_input = 0;		//更新输入状态
		load_desktop();	//更新桌面状态
		for (int i = 0; i < 20; i++) user_password[i] = '\0';//重置密码状态
		num = 0;//重置
		gotoxy(0, 0);//光标回到最初
		print_ending(key);//播放关机画面
	}

	//输入密码
	if (if_input == 1 && (ch == 32 || keyboard_place[keyboard[x][y]][0] == ' ') && keyboard_place[keyboard[x][y]][0] > 9) {//输入状态开启 监控到了空格 则将输入的密码显示到屏幕上
		if (if_shift == 1) input_passerword(keyboard_place[keyboard[x][y]][1]);//shift键开启后输入相应字符
		else input_passerword(keyboard_place[keyboard[x][y]][0]);
	}
	//确认密码
	if (if_input == 1 && ch == 32 && keyboard_place[keyboard[x][y]][0] == 3) {//输入状态开启 且读到了空格 判断为用户敲完密码进行确认
		if (strcmp(password, user_password) == 0) {//密码输入正确
			load_desktop();	//载入桌面
			if_input = 0;		//输入状态关闭
			rex = keyboard_place[keyboard[--x][y]][4];//调整坐标
			rey = keyboard_place[keyboard[x][y]][3];//调整坐标
			gotoxy(rex, rey);
		}
		else {
			num = 0;
			for (int i = 0; i < 20; i++) user_password[i] = '\0';//重置密码状态
			gotoxy(77 + num, 9);
			cout << "密码错误！请重新输入";	//打印错误提示
			Sleep(1500);
			gotoxy(0, 9);
			printf("%s", password_load[9]);	//恢复界面
			gotoxy(77, 9);
		}
	}

	//删除字符
	if (if_input == 1 && ch == 32 && keyboard_place[keyboard[x][y]][0] == 2) {//输入状态开启 且监控到了空格 判用户执行删除键
		back();
	}

	//桌面光标移动
	if (if_desktop == 1 && ch == 32 && keyboard_place[keyboard[x][y]][0] >= 5 && keyboard_place[keyboard[x][y]][0] <= 8) {//桌面状态开启 监控到空格 且位置在上下左右几个键上 则进行相应的桌面光标移动
		ldx = dex, ldy = dey;
		switch (keyboard_place[keyboard[x][y]][0]) {
		case 5:		//上
			dex--;
			break;
		case 6:		//左
			dey--;
			break;
		case 7:		//下
			dex++;
			break;
		case 8:     //右
			dey++;
			break;
		}

		if (desktop_map[dex][dey] == 0)  dex = ldx, dey = ldy;	//恢复到前一个状态
		dx = desktop_place[desktop_map[dex][dey]][1];		//获取实际x坐标
		dy = desktop_place[desktop_map[dex][dey]][0];		//获取实际y坐标

		//刷新行					两次状态不在同一行则刷新上次所在行
		if (dex != ldx)	gotoxy(0, desktop_place[desktop_map[ldx][ldy]][0]), printf("%s", desktop[desktop_place[desktop_map[ldx][ldy]][0]]);
		else	gotoxy(0, dy), printf("%s", desktop[dy]);//两次状态在同一行则刷新此行

		gotoxy(dx, dy);//转到模块对应行
		cout << "[-]";//打印区分标识
		gotoxy(rex, rey);//返回虚拟键盘

	}

	//桌面小组件
	if (if_desktop == 1 && ch == 32 && keyboard_place[keyboard[x][y]][0] == 3) {
		switch (desktop_map[dex][dey]) {//判断组件类别
		case 1://TXT记事本
			load_txt();
			break;
		case 2://小游戏(开发中）
			gotoxy(0, 0);
			for (int i = 0; i < 17; i++) {
				printf("%s\n", Map[i]);
				Sleep(100);
			}
			gotoxy(30, 3);
			load_shake();
			Sleep(2000);
			load_desktop();
			break;
		case 3://计算器
			load_calculator('\0');
			break;
		case 4://更多信息
			load_more();
			break;
		case 5://IE浏览器
			ShellExecute(NULL, _T("open"), _T("explorer.exe"), _T("http://www.baidu.com"), NULL, SW_SHOW);
			break;
		case 6://猜数字
			srand(time(0));
			ans = rand() % 11;//生成的随机数在0~10之间
			guess_number(0);
			break;
		}
	}

	//记事本模块
	if (if_txt == 1 && (ch == 32 || keyboard_place[keyboard[x][y]][0] == ' ')) {//若记事本已打开 且监控到了空格操作 那么就执行相应判断
		if (keyboard_place[keyboard[x][y]][0] > 9) {	//若执行的键位是字符键位的话 则在屏幕上显示字符
			if (if_shift == 1) input_txt(keyboard_place[keyboard[x][y]][1]);
			else input_txt(keyboard_place[keyboard[x][y]][0]);
		}
		else if (keyboard_place[keyboard[x][y]][0] == 2) input_txt(keyboard_place[keyboard[x][y]][0], 1);//若执行的是删除操作的话 则删除字符
	}

	//计算器模块
	if (if_calculator == 1 && ch == 32) {	//若计算器已打开 且监控到了空格操作 那么就执行相应判断
		if (keyboard_place[keyboard[x][y]][0] > 9) {	//若执行的键位是字符键位的话 则在屏幕上显示字符
			if (if_shift == 1) load_calculator(keyboard_place[keyboard[x][y]][1]);
			else load_calculator(keyboard_place[keyboard[x][y]][0]);
		}
		else if (keyboard_place[keyboard[x][y]][0] == 2) load_calculator(keyboard_place[keyboard[x][y]][0], 1);
	}

	//猜数字
	if (if_guess == 1 && ch == 32) {//若猜数字小游戏已打开 且监控到了空格操作 那么就执行相应判断
		if (keyboard_place[keyboard[x][y]][0] > 9) {
			if (if_shift == 1) guess_number(keyboard_place[keyboard[x][y]][1]);
			else guess_number(keyboard_place[keyboard[x][y]][0]);
		}
	}

	//桌面组件退出判定
	if (ch == 32 && keyboard_place[keyboard[x][y]][0] == 1) {//且监控到了esc键被执行 那么就退出桌面组件
		if_txt = 0;
		if_calculator = 0;
		if_guess = 0;
		cx = 22, cy = 4;
		for (int i = 0; i < 50; i++) posture[i] = '\0';
		load_desktop();
	}


	return 1;
}

