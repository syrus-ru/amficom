#ifndef _ArrList_H
#define _ArrList_H

/*
 * �������� ������� ������������, � ArrList ������ ������������� ������
 * ���������� ����� �� �������� �� ��������.
 * ������������ ����� ��� ������ � ������ ���������� - disposeAll() - ��������
 * ������������� ��� �������� ������������.
 */

class ArrList
{
private:
	void **storage;
	int allocated;
	int used;
	void extendToUsedPlusOne();
public:
	ArrList();
	void disposeAll(); // ��������������� ������� ��� �������� ���� ���������, ������� ������ ������������
	~ArrList(); // ������� ������, �� �� ��������
	void *operator[](int id); // ������ � ���������� �������� (getter ��� ������)
	void set(int id, void *obj); // ���������� ����� �������� ��� �������� (setter ��� ������)
	void add(void *obj); // ��������� � ����� ������� ����� �������
	void slowInsert(int pos, void *obj); // ��������� ����� ������� �� ������� pos; i � � �� 0 �� getLength() ���.
	int getLength(); // �����
	void qsort(int(*fcmp)(const void**, const void**)); // �������� qsort ��� ������
};

/*
 * �������� ������������� 1:
 *
 * ArrList al = new ArrList;
 * al.add(new Obj(...));
 * al.add(new Obj(...));
 * al.qsort(fcmp);
 * for (i = 0; i < al.getLength(); i++) { ... }
 * al.disposeAll();
 * delete al;
 *
 * �������� ������������� 2:
 *
 * Obj *objs = new Obj[N];
 * ...
 * ArrList al = new ArrList;
 * for (i = 0; i < N; i++) { al.add(obj[i]); }
 * al.qsort(fcmp);
 * for (i = 0; i < al.getLength(); i++) { ... }
 * delete al;
 * delete[] objs;
 */

#endif


