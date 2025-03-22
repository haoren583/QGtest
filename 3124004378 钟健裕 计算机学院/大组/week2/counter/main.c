#define _CRT_SECURE_NO_WARNINGS 1 //ȡ����ȫ����
//����������ʵ�ֶ���С������������
//֧�����ţ�֧�ָ�����֧��С����
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#define MAX_LEN 100000 //��������ַ�������
#define MAX_NUM 100//����������ֳ���
#define N 1000

char str[MAX_LEN]; char* p_str = str; //�����ַ�������//����ָ�룬ָ������ָ�����һ��Ԫ��
double num[N]; double* p_num = num; //�������ֱ���//����ָ�룬ָ��ָ�����һ��Ԫ��
char op[N] = { '(' }; char* p_op = op; //�������������//����ָ�룬ָ��ָ�����һ��Ԫ��
int chart[200];//�������ȼ�

//�����������ֵ�������
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

//ת���ַ���Ϊ����
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

void get_num() //��ȡ����
{
	//����ָ������ʼλ��
	p_str = str;
	//���������ַ������������ڴ洢��ʽ��һ������ֵ��ַ���
	char strNum[MAX_NUM];
	//////
	char* pStrNum = strNum;
	//С����λ��
	int point = 0;
	//�Ƿ���С���㣬��ʼ����С���㣻
	char isPoint = 0;
	//�Ƿ��и��ţ���ʼ���޸��ţ�
	char is_ = 0;
	for (; 1;)
	{
		//��ȡ�ַ����е��ַ�
		char c = *p_str;
		//////
		p_str++;
		//����������֡�-��������ת��Ϊ��-��num1+num2����
		if (c == '-')
		{
			if (*p_op == '-')
				c = '+';
		}
		if (c == '+' || c == '-' || c == '*' || c == '/' || c == 'X' || c == 'x')
		{
			//�жϵ�ǰ����ǰ�Ƿ�������(������)
			if (strNum != pStrNum)
			{
				*(p_num + 1) = count2(strNum, pStrNum, point, isPoint, is_);
				p_num++;
			}
			//ˢ�������ַ���
			pStrNum = strNum;
			isPoint = 0;
			is_ = 0;
			//�жϷ������ȼ�
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
			//�Ѿ���С������
			if (isPoint) 
			{
				printf("�Ƿ�����\n");
				return;
			}
			point = pStrNum - strNum;
			isPoint = 1;
			continue;
		}

		if (c == '\0')
		{
			//�ַ�������,�������ļ���
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
	//�Ƿ�Ƿ�����
	char is_ilegal = 0; 
	//�����ַ�������
	memset(str, '\0', sizeof(str)); p_str = str;
	//�������ֱ���
	memset(num, 0, sizeof(num)); p_num = num;
	//�������������
	memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
	//Ϊ������������ȼ�
	chart['+'] = 1; chart['-'] = 1; chart['*'] = 2; chart['/'] = 2; chart['('] = 0;
	chart[')'] = 10; chart['x'] = 2; chart['X'] = 2;

	printf("����������ʵ�ֶ��С��������������\n֧�����ţ�֧�ָ�����֧��С����\n");
	printf("�˳���ֻ�ܼ����������ÿһ���������ľ��ȶ�С��double����ʽ\n");
	printf("����b���˳�����\n");
	printf("��������ʽ��\n");

	for (; 1;)
	{

		char c;
		c = getchar();
		if (c == 'b')
		{
			//�˳�
			return 0;
		}
		//���˿ո�
		if (c != ' ')
		{
			if (c != '\n')
			{
				//�ж��Ƿ�Ƿ�����
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
					printf("�Ƿ�����\n");
					//��ʼ���ַ�������
					memset(str, '\0', sizeof(str)); p_str = str; 
					//��ʼ��ջ�ϵ����ֱ���
					memset(num, 0, sizeof(num)); p_num = num; 
					//��ʼ��ջ�ϵ����������
					memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
					printf("��������ʽ��\n");
					is_ilegal = 0;
					continue;
				}
				get_num();
				//��ʼ���ַ�������
				memset(str, '\0', sizeof(str)); p_str = str; 
				//��ʼ��ջ�ϵ����ֱ���
				memset(num, 0, sizeof(num)); p_num = num; 
				//��ʼ��ջ�ϵ����������
				memset(op, '\0', sizeof(op)); op[0] = '('; p_op = op; 
				printf("��������ʽ��\n");
			}
		}
	}

}