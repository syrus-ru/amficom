#include "findLength.h"

const int TREAT_ONLY_DUPLICATE_ZEROES = 0;

// ���������� ����� �������������� �� ������ ���� (�.�. �� ����� �/������)
// ���� ������ �� ����� �������, ������ ������� data_length
// data - ������� �/�
// data_length - �� �����
// return: �������� >=0, <=data; �� ����������� �� ����.
int findReflectogramLength(double *data, int len)
{
	if (len <= 0)
		return 0;

	int width = TREAT_ONLY_DUPLICATE_ZEROES ? 1 : 0;

	// ���� ����������� �������� �� �/�
	double vmin = data[0];
	int i;
	for (i = 0; i < len - width; i++)
		if (vmin > data[i])
			vmin = data[i];

	// ���� ����� ������� X-�������� ����� ������������ ����������
	int lmax = 0; // ����� ����������� ���������
	int xmax = 0; // ��������� ����� ����������� ���������
	int lastX = 0; // ������ �������� ���������
	for (i = 0; i < len - width; i++)
	{
		if (data[i] != vmin)
			continue;
		if (width)
		{
			if (data[i + 1] != vmin)
				continue;
		}
		// ������ ����
		int lcur = i - lastX;
		int xcur = i;
		if (lmax < lcur)
		{
			lmax = lcur;
			xmax = xcur;
		}
		lastX = i;
	}

	// ��������� = ������ ���� ������ �������� ��������� ����� ������������ ����������

	if (xmax)
		return xmax; // ��������� ������
	else
		return len; // ��������� �� ������? //XXX: �������� �� ���?
}

