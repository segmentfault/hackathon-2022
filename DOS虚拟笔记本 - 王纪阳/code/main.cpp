#include "map.h"

using namespace std;

//extern int if_password;

void key_num(){
	int ch=0;
	while(1){
		ch=getch();
		if(ch==8) return ;
		else cout<<ch<<endl;
	}
	return ;
}


int main() {
	
	init_shell(1,1);//两个可选参数分别代表 1.是否打印使用前提示 2.
	//print_password();
	while(1){
		if(!move(1,1,1)){
			gotoxy(0,40);
			return 1;
		}
	}
	
	return 0;
}
