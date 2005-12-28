#ifndef _BGSWITCHER_H
#define _BGSWITCHER_H

/**
 * ������������� ������ OTAU �� ������ COM-�����, ��������������
 * ����������� (� ��������� ������) ��������� OTAU �� ���������� ���������.
 */

const int SWITCHER_PORT_DISCONNECTED = -1;
const int SWITCHER_PORT_UNDEFINED = -2;
const int BGSWITCHER_SLEEP_TIME = 500;
const int BGSWITCHER_N_SLEEPS_TO_REANIMATE = (25000 / BGSWITCHER_SLEEP_TIME);

class BgSwitcher {
private:
	// client-to-server
	bool hardInitMode;
	bool toStop;
	int comPort;
	int otauId;
	int desiredPort;

	// server-to-client
	bool isError; // if true, BgSwitcher is not referred by server thread anymore, i/o handles are released
	bool isStarted;
	int activePort;

	void finalize(); // stops server thread; returns when ready to be destroyed
	void sleep(); // delay BGSWITCHER_SLEEP_TIME

public:
	/**
	 * ������� ������������� ��� ������� COM-����� � ������� OTAU.
	 * ���������� ����� ����������� �� ��������� �������������.
	 * hardInitMode - true, ���� ������������ ����� ��� ������������� ������������� OTAU. ������������� �������� false
	 */
	BgSwitcher(int com_port, int otau_id, bool hardInitMode); // XXX: blocks till end of initialization
	/**
	 * ��������� ������ � COM-������ � OTAU
	 */
	~BgSwitcher();

	// client API - status
	/**
	 * ������, �������� �� ������.
	 * ���� ������ ��������, ������ ������������ � �������
	 * ������������. ���, ��� ������� �������� ����������� ��� �����������
	 * ��������� ������ - ��� ������� ���� ��������� BgSwitcher'�,
	 * � ������� �����.
	 * ��� ������������� ������ ������� ������������ ������ � OTAU �
	 * ������������� COM-����, � ������ ����� ����� ������������ ���� ������.
	 * 
	 */
	bool c_isError(); // returns true if a error ecountered (any error is treated as unrecoverable)

	// client API - switch in sync mode
	/**
	 * ����������� ����.
	 * port - ������������� ����� - �������� ����� �����,
	 *        ���� SWITCHER_PORT_DISCONNECTED, ���� ���� ������� � ��������� '���������'
	 * ����� ����������� ������ ������ ����� ��������� ������������
	 * ��� ��� ����������� ������.
	 * ���� ����������� ����� ����� ��� �������, ����� ���������� ���������� ���������� (returns).
	 */
	void c_switchTo(int port); // blocks till switch is complete

	// client APU - switch in async mode
	/**
	 * ����������� ���� (����������)
	 * port - ������������� ����� - �������� ����� �����,
	 *        ���� SWITCHER_PORT_DISCONNECTED, ���� ���� ������� � ��������� '���������'
	 * ����� ����������� ������ ������, ��� ����� ��������� ������������,
	 * � �� ������ ������� �� ��������� ������.
	 * (����������, �� ���������� ���� ��������� ������������ �� ����������� c_switchToAsync())
	 * ��� ����, ����� ��������� �������, ����� ���� ����� ������� ����������,
	 * ����������� c_wait()
	 */
	void c_switchToAsync(int port); // blocks for a smaller period; will need to invoke c_wait()
	/**
	 * ��������� �������, ����� ���������� �������� ������������ �����, ������� c_switchToAsync()
	 */
	void c_wait(); // wait till switch request completes

	// server API, is used internally by thread started by the class
	bool s_getHardInitMode();
	int s_getComPort();
	int s_getOtauId();
	int s_getDesiredPort();
	bool s_getToStop();
	void s_setStarted(bool);
	void s_setActivePort(int);
	void s_setError(); // sets to true
	void s_sleep(); // delay BGSWITCHER_SLEEP_TIME
};

#endif
