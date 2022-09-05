#include<bits/stdc++.h>
using namespace std;

char in[51]={'\0'},out[51]={'\0'},c;
typedef struct Node{
	int data;
	char str;
	struct Node *next;
}*linkstack;
typedef struct Lkstack{
	struct Node *top;
	int length;
}*Lstack;
void initstack(Lstack s){
	s->top=NULL;
	s->length=0;
	return;
}
void Pushc(Lstack s,char e){
	linkstack l=new Node;
	l->str=e;
	l->next=s->top;
	s->top=l;//将新的节点l赋给栈顶指针
	s->length++;
	return;
}
void Popc(Lstack s,char *e){
	if(s->length==0) return ;
	*e=s->top->str;
	s->top=s->top->next;
	s->length--;
}
void Pushn(Lstack s,int e){
	linkstack l=new Node;
	l->data=e;
	l->next=s->top;
	s->top=l;//将新的节点l赋给栈顶指针
	s->length++;
	return;
}
void Popn(Lstack s,int *e){
	if(s->length==0) return ;
	*e=s->top->data;
	s->top=s->top->next;
	s->length--;
}
int lenstack(Lstack s){
	return s->length;
}
void change(Lstack s){
	int j=0;
	/*中缀表达式转后缀表达式：
	从左到右遍历中缀表达式，
	若是数字就输出——成为后缀表达式的一部分；
	若是符号，则判断其与栈顶符号的优先级，
	若是右括号或优先级不高于栈顶符号则栈顶元素依次出栈并输出，并将当前符号入栈*/
	for(int i=0;in[i]!='\0';i++){
		while(isdigit(in[i]))//判断该字符是含有数字
		{/*过滤数字字符，直接输出，直到下一位不是数字字符打印空格跳出循环 */
			out[j++]=in[i];
			i++;
			if(!isdigit(in[i])&&in[i]!='\0'){
				out[j++]=' ';
			}
		}
		if(in[i]=='\0') break;
		if(in[i]<48||in[i]>57){//符号
			if(in[i]=='+'||in[i]=='-'){//加减运算符得到优先级最低
				if(!lenstack(s)) Pushc(s,in[i]);//栈顶为空，直接入栈
				else {
					while(lenstack(s)){
						Popc(s,&c);
						if(c!='('){
							out[j++]=c;
							out[j++]=' ';
						}
						else{
							Pushc(s,c);
							break;
						}
					}
					Pushc(s,in[i]);
				}
			}
			else {
				if(in[i]==')'){
					Popc(s,&c);
					while(c!='('){
						out[j++]=c;
						out[j++]=' ';
						Popc(s,&c);
					}
				}
				else {//乘、除、左括号都是优先级高的，直接入栈
					Pushc(s,in[i]);
				}
			}
		}
	}
	out[j++]=' ';
	while(lenstack(s)){
		Popc(s,&c);
		out[j++]=c;
	}
}
void AIexpre(Lstack s,int *e){
	int num=-1,a,b;
	for(int i=0;out[i]!='\0';i++){
		while(isdigit(out[i])){  //过滤数字
			if(num==-1) num=0,num=out[i++]-'0';
			else num=num*10+out[i++]-'0';
			if(out[i]==' '&&num!=-1)
			{
				Pushn(s,num); //将转换的数进行压栈
				num=-1;
				break;
			}
		}
		switch(out[i]){
			case '+':
				Popn(s,&a);
				Popn(s,&b);
				Pushn(s,a+b);
				break;
			case '-':
				Popn(s,&a);
				Popn(s,&b);
				Pushn(s,b-a);
				break;
			case '*':
				Popn(s,&a);
				Popn(s,&b);
				Pushn(s,b*a);
				break;
			case '/':
				Popn(s,&a);
				Popn(s,&b);
				Pushn(s,b/a);
				break;
		}
	}
	Popn(s,e);
}
int calculator(char name[]){
	int j=0,ans;
	Lstack s=new Lkstack;
	initstack(s);//in来存中缀表达式  out来存放后缀表达式的符号
	for(int i=0;name[i]!='\0';i++) in[i]=name[i];
	change(s);//中缀转后缀表达式
	AIexpre(s,&ans);//计算结果
	return ans;
}

