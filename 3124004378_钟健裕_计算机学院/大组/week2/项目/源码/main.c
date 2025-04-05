#define _CRT_SECURE_NO_WARNINGS 1 //取消安全警告
//计算器，可实现多项小数简单四则运算
//支持括号，支持负数，支持小数点
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#define MAX_LEN 100000 //定义最大字符串长度
#define MAX_NUM 100//定义最大数字长度
#define N 1000

char str[MAX_LEN]; char* p_str = str; //定义字符串变量//定义指针，指针总是指向最后一个元素
double num[N]; double* p_num = num; //定义数字变量//定义指针，指针指向最后一个元素
char op[N] = { '(' }; char* p_op = op; //定义运算符变量//定义指针，指针指向最后一个元素
int chart[200];//符号优先级

//计算两个数字的运算结果
double count1(char x, double num1, double num2)
{
	switch (x)
	{
	case '+':return num1 + num2; break;
	case '-':return num2 - num1; break;
	case '*':
	case 'X':
	case 'x':
		return num1 * num2; break;
	case '/':return num2 / num1; break;
	}
}

//转换字符串为数字
double count2(char* x, char* p_snum, int point, char is_point, char is_)
{
	double num0 = 0;
	int t = p_snum - x;
	if (is_point)
	{
		for (int i = 1; i <= t; i++)
		{
			num0 += (x[i] - '0') * pow(10, point - i);
		}
	}
	else
	{
		for (int i = 1; i <= t; i++)
		{
			num0 = num0 * 10 + (x[i] - '0');
		}
	}
	if (is_)
	{
		num0 = -num0;
	}
	return num0;
}

void get_num() //获取数字
{
	//重置指针至开始位置
	p_str = str;
	//定义数字字符串变量，用于存储算式中一项的数字的字符串
	char strNum[MAX_NUM];
	//////
	char* pStrNum = strNum;
	//小数点位置
	int point = 0;
	//是否有小数点，初始化无小数点；
	char isPoint = 0;
	//是否有负号，初始化无负号；
	char is_ = 0;
	for (; 1;)
	{
		//获取字符串中的字符
		char c = *p_str;
		//////
		p_str++;
		//如果连续出现‘-’，将其转化为‘-（num1+num2）’
		if (c == '-')
		{
			if (*p_op == '-')
				c = '+';
		}
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == 'X' || c == 'x')
		{
			//判断当前符号前是否有数字(处理负数)
			if (strNum != pStrNum)
			{
				*(p_num + 1) = count2(strNum, pStrNum, point, isPoint, is_);
				p_num++;
			}
			//刷新数字字符串
			pStrNum = strNum;
			isPoint = 0;
			is_ = 0;
			//判断符号优先级
			if (chart[c] >= chart[*p_op])
			{
				*(p_op + 1) = c;
				p_op++;
			}
			else
			{
				for (; 1;)
				{
					char x = *p_op;
					if (chart[c] >= chart[x])
					{
						*(p_op + 1) = c;
						p_op++;
						break;
					}
					*p_op = '\0'; p_op--;
					double num1 = *p_num; *p_num = '\0'; p_num--;
					double num2 = *p_num; *p_num = '\0'; p_num--;
					double result = count1(x, num1, num2);
					*(p_num + 1) = result; p_num++;
				}
			}
			continue;
		}

		if (c == '(')
		{
			*(p_op + 1) = c;
			p_op++;
			continue;
		}

		if (c == ')')
		{
			*(p_num + 1) = count2(strNum, pStrNum, point, isPoint, is_);
			p_num++;
			pStrNum = strNum;
			isPoint = 0;
			is_ = 0;
			for (; 1;)
			{
				char x = *(p_op);
				*p_op = '\0';
				p_op--;
				if (x == '(')
				{
					break;
				}
				double num1 = *p_num; *p_num = '\0'; p_num--;
				double num2 = *p_num; *p_num = '\0'; p_num--;
				double result = count1(x, num1, num2);
				*(p_num + 1) = result; p_num++;
			}
			continue;
		}

		if (c >= '0' && c <= '9')
		{
			pStrNum++;
			*(pStrNum) = c;
			continue;
		}
		if (c == '.')
		{
			//已经有小数点了
			if (isPoint) 
			{
				printf("非法输入\n");
				return;
			}
			point = pStrNum - strNum;
			isPoint = 1;
			continue;
		}

		if (c == '\0')
		{
			//字符串结束,进行最后的计算
			if (strNum != pStrNum)
			{
				*(p_num + 1) = count2(strNum, pStrNum, point, isPoint, is_);
				p_num++;
			}
			pStrNum = strNum;
			isPoint = 0;
			is_ = 0;
			for (; 1;)
			{
				char x = *p_op;
				if (p_op == &op[0])
				{
					printf("%lf\n", num[1]);
					break;
				}

				*p_op = '\0';
				p_op--;
				double num1 = *p_num; *p_num = '\0'; p_num--;
				double num2 = *p_num; *p_num = '\0'; p_num--;
				*(p_num + 1) = count1(x, num1, num2);
				p_num++;
			}
			return;
		}
	}
}

int main()
{
	//是否非法输入
	char is_ilegal = 0; 
	//定义字符串变量
	memset(str, '\0', sizeof(str)); p_str = str;
	//定义数字变量
	memset(num, 0, sizeof(num)); p_num = num;
	//定义运算符变量
	memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
	//为运算符设置优先级
	chart['+'] = 1; chart['-'] = 1; chart['*'] = 2; chart['/'] = 2; chart['('] = 0;
	chart[')'] = 10; chart['x'] = 2; chart['X'] = 2;

	printf("计算器，可实现多项、小数、简单四则运算\n支持括号，支持负数，支持小数点\n");
	printf("此程序只能计算各项数及每一步计算结果的精度都小于double的算式\n");
	printf("按下b键退出程序\n");
	printf("请输入算式：\n");

	for (; 1;)
	{

		char c;
		c = getchar();
		if (c == 'b')
		{
			//退出
			return 0;
		}
		//过滤空格
		if (c != ' ')
		{
			if (c != '\n')
			{
				//判断是否非法输入
				if (!((c >= '0' && c <= '9') || c == '.' || c == '+' || c == '-' || c == '*' || c == '/' || c == 'X' || c == 'x' || c == '(' || c == ')'))
				{
					is_ilegal = 1;
				}
				*p_str = c;
				p_str++;
			}
			else
			{
				*p_str = '\0';
				p_str = str;
				if (is_ilegal)
				{
					printf("非法输入\n");
					//初始化字符串变量
					memset(str, '\0', sizeof(str)); p_str = str; 
					//初始化栈上的数字变量
					memset(num, 0, sizeof(num)); p_num = num; 
					//初始化栈上的运算符变量
					memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
					printf("请输入算式：\n");
					is_ilegal = 0;
					continue;
				}
				get_num();
				//初始化字符串变量
				memset(str, '\0', sizeof(str)); p_str = str; 
				//初始化栈上的数字变量
				memset(num, 0, sizeof(num)); p_num = num; 
				//初始化栈上的运算符变量
				memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
				printf("请输入算式：\n");
			}
		}
	}

}