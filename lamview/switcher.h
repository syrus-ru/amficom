/*
 * �����, �������������� �������������� � OTAU �� ����� COM-�����.
 * �� �������, ������ ����� �������� � ����������� OTAU,
 * �� �������� ������ ��� ������ OTAU.
 * ����� �������� ������� �� ������������, � �.�. �� ����������,
 * � ����� ������ repeatStatus.
 */

#ifndef switch_OTAU_H
#define switch_OTAU_H

#include <windows.h>

class Switcher {
	HANDLE comHandle;
	int otau_number;
	bool *otau_conn; // flags 'OTAU is connected'

	int initialize_OTAU(int comPort, bool hardInit);
	int set_COM_port_properties(const HANDLE com_port_handle);
	DWORD send_switch_command(const HANDLE com_port_handle,
			const char* command, // ASCIIZ
			char* reply,
			unsigned int reply_size); // we will not modify reply_size
	int search_number_of_OTAUs(char* string, int str_size);

public:
	/**
	 * ������������� �������������.
	 * comPort - ����� COM-����� (1,2,3,4)
	 * hardInit - true - ������ hard ������������� (� ������������� OTAU � ����������)
	 *            false - ������� �������������
	 * XXX: ����� ����������� �� ��������� �������������
	 */
	Switcher(int comPort, bool hardInit);

	~Switcher();

	/**
	 * -- not tested --
	 * �������� ����� OTAU �� ������ �����
	 */
	int getNumberOfOtaus();
	/**
	 * ����������� OTAU
	 * otauId - ����� OTAU
	 * otauPort - ����, � ������� ���� ��������� ��������� OTAU,
	 *            ���� -1, ���� ���� ������� � ��������� '���������'
	 * ��������: � OTAU ���� ����-��� ����� 75 ���, ����� ��������
	 *           OTAU ������������� ��������� � ��������� '���������'.
	 *           �� ��������� ���� ��������, ����������� repeatStatus()
	 */
	int switch_OTAU(int otauId, int otauPort); // rc: 0: fail, 1: success
	/**
	 * ��� ������� ���� ��������� � �������� �� ����� 75 ���,
	 * ����� ������������� �������������� ���������� OTAU.
	 * ����������, ���������� ����� ����� ���������� ����� ���� �������.
	 * ���������� �������� ��������� 20-50 ������.
	 * ��� ������ � ����������� OTAU, ��� ������� ������������ ����� ���
	 * OTAU, ����������� � ��������� '���������'.
	 */
	int repeatStatus(); // rc: 0: fail, 1: success
};

#endif
