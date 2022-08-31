#include <stdio.h>
#include <windows.h>
#include <time.h>
#include <conio.h>
#include <stdlib.h>

void BackGround(unsigned int ForeColor = 7, unsigned int BackGroundColor = 0) {
	HANDLE handle = GetStdHandle(STD_OUTPUT_HANDLE);  //获取控制台的句柄
	SetConsoleTextAttribute(handle, ForeColor + BackGroundColor * 0x10);//改变当前光标的背景和字体颜色
}

void gotoxy(int xx, int yy) {//移动光标
			//x指列数 y指行数
	HANDLE handle = GetStdHandle(STD_OUTPUT_HANDLE);//获取设备句柄
	COORD pos;//设定cmd窗口坐标变量
	pos.X = xx;
	pos.Y = yy;
	SetConsoleCursorPosition(handle, pos);//移动光标到指定位置
	return;
}
int basex = 30, basey = 1;

typedef struct {
	int direct, len, vict, graph;//方向 长度 胜负 分数
}look;
look check;
//初始化地图数据
void restart(int bk[20][15], int move[20][15]) {
	//bk为总的地图数据记录整个地图，为1时表示墙体，为2时表示果实，为3时表示蛇
	//move记录蛇走过的路程，用来打印蛇时判断用
	int pp, qq;  //用来记录获取的随机坐标

	//接下来要初始化整个地图
	for (int i = 0; i <= 16; i++) {
		for (int j = 0; j < 15; j++) {
			if (i == 0 || i == 16 || j == 0 || j == 14)  bk[i][j] = 1;//边界判定 记为墙体
			else bk[i][j] = 0; //0表示什么都没有
			move[i][j] = 0;    //该数组用来记录蛇移动的轨迹
		}
	}

	//将蛇初始化在8,8坐标上
	bk[8][8] = 3;
	move[8][8] = 1;//更新状态
	move[0][0] = 1;//用此来记录步数
	pp = rand() % 15 ;//范围是1-14
	qq = rand() % 15 ;
	bk[pp][qq] = 2;//表示这个位置有果实了
	check.direct = 1;//表示朝向,向上
	check.len = 1;//表示长度
	check.vict = 0;//当为1是表示失败
	check.graph = 0;//记录得分

	//接下来要绘制地图
	for (int i = 0; i <= 16; i++) {
		for (int j = 0; j < 15; j++) {
			gotoxy(basex + i * 2, basey + j);//光标移动,每个光标都是矩形
			switch (bk[i][j]) {//判断当前区域的类型
				case 0:
					BackGround(0, 0);
					break;//如果没有东西打印黑色
				case 1:
					BackGround(0, 1);
					break;//墙打印蓝色
				case 2:
					BackGround(7, 0);
					printf("☆");
					break;//果实打印灰色
				case 3:
					BackGround(0, 3);
					break;//蛇打印湖蓝色
				default:
					break;
			}
			printf("  ");//地图中直接就是涂空格符(颜色覆盖)
		}
	}

	//接下来要显示积分//
	gotoxy(basex + 35, basey + 0);
	BackGround(0, 7);
	printf("现在得分是:%d,请再接再厉!^_^", check.vict);
}


//**运动主体**//
void main_map(int bk[20][15], int xy[2], int move[20][15]) {
	//bk是地图信息，xy记录坐标，move记录蛇的运动轨迹
	int b[10], qq = 0, pp = 0;//b用来吸收输入，qq和pp用来随机初始化果实坐标
	if (kbhit()) {//记录按下的是哪个方向键
		b[0] = getch();//用b来记录
		if (b[0] == 224)  b[0] = getch();//如果为224表示为方向键，但是要再一次获取才行
		if (b[0] == 72 && check.direct != 2)
			//如果输入的为上并且朝向不为下
			check.direct = 1;
		if (b[0] == 80 && check.direct != 1)
			check.direct = 2;
		if (b[0] == 75 && check.direct != 4)
			check.direct = 3;
		if (b[0] == 77 && check.direct != 3)
			check.direct = 4;
	}
	switch (check.direct) {
		case 1:
			//往上走
			xy[1]--;
			break;
		case 2:
			//往下走
			xy[1]++;
			break;
		case 3:
			//往左走
			xy[0]--;
			break;
		case 4:
			//往右走
			xy[0]++;
			break;
	}

	//接下来蛇就开始走动了//
	move[0][0]++;//蛇的步数加一
	move[xy[0]][xy[1]] = move[0][0];//记录当前格子中蛇的轨迹
	gotoxy(basex + 35, basey + 2);
	BackGround(0, 7);
	printf("横坐标:%d,纵坐标:%d  ", xy[0], xy[1]);
	gotoxy(basex + xy[0] * 2, basey + xy[1]);//这里蛇头就往前移动了
	BackGround(0, 3);//与蛇体一个颜色
	printf("  ");

	//如果吃了果实//
	if (bk[xy[0]][xy[1]] == 2) {
		check.vict++;//分数加一
		check.len++;//长度加一
		//更新分数
		gotoxy(basex + 35, basey + 0);
		BackGround(0, 7);
		printf("现在得分是:%d,请再接再厉!^_^", check.vict);
		while (bk[pp][qq] != 0) {
			pp = rand() % 15;
			qq = rand() % 15;
		}
		bk[pp][qq] = 2;//将这个地方变为果实
		gotoxy(basex + pp * 2, basey + qq);
		BackGround(7, 0);
		printf("☆");
	}

	//如果撞了墙或者自己//
	if (bk[xy[0]][xy[1]] == 1 || bk[xy[0]][xy[1]] == 3) {
		check.graph = 1;//表示已经输了
		gotoxy(basex + 6, basey + 6);
		BackGround(0, 7);
		printf("你输了，最后得分:%d", check.vict);
		return;
	}

	bk[xy[0]][xy[1]] = 3;//使这个位置变成蛇
	//接下来要检测蛇然后刷新蛇的位置//
	for (int i = 0; i <= 16; i++)
		for (int j = 0; j < 15; j++) {
			if (move[i][j] == move[xy[0]][xy[1]] - check.len) {
				//如果符合这个条件,则表示蛇已经移动出这个位置了
				//要删除这个位置的蛇尾巴
				//一次只有一个方块会符合要求
				bk[i][j] = 0;
				gotoxy(basex + i * 2, basey + j);
				BackGround(0, 0);
				printf("  ");
				break;//一次只找一个-
			}
		}
}
int load_shake() {
	int bk[20][15], xy[2], move[20][15];
	xy[1] = xy[0] = 8;
	srand((unsigned) time(NULL));//初始化随机种子
	printf("请按任意键继续...");
	getch();
	restart(bk, move);
	while (check.graph == 0) {
		Sleep(200);//休眠400ms一次
		main_map(bk, xy, move);
	}
	
	return 0;
}

