/****************************************************/
/*         ������������� ���������                  */
/****************************************************/
#ifndef __STUDENT_H__

#define ENTRY   extern
#define LOCAL   static

ENTRY double
studentDF(double n, double x);
//����������� ����������� ����, ��� ��������� ��������,
// ������������� ������������� ��������� (T-�������������)
// c n ��������� �������, �� ����������� (������ ��� �����) x.

ENTRY double
inv_studentDF(double n, double p);
// �� ������ ����������� p ����������� �������� q,
// ��� �������� studentDF(n,q) ������ p.

#define	__STUDENT_H__	/* Prevents redefinition	*/
#endif              	/* Ends #ifndef __STUDENT_H__	*/


