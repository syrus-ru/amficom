/*
 *  ласс, обеспечивающий взаимодействие с OTAU на одном COM-порту.
 * ѕо задумке, должен уметь работать с несколькими OTAU,
 * но проверен только дл€ одного OTAU.
 * ”меет посылать команду на переключение, в т.ч. на отключение,
 * а также коману repeatStatus.
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
	 * »нициализаци€ переключател€.
	 * comPort - номер COM-порта (1,2,3,4)
	 * hardInit - true - долга€ hard инициализаци€ (с перезагрузкой OTAU и автотестом)
	 *            false - быстра€ инициализаци€
	 * XXX: поток блокируетс€ до окончани€ инициализации
	 */
	Switcher(int comPort, bool hardInit);

	~Switcher();

	/**
	 * -- not tested --
	 * ѕолучить число OTAU на данном порту
	 */
	int getNumberOfOtaus();
	/**
	 * ѕереключить OTAU
	 * otauId - номер OTAU
	 * otauPort - порт, в который надо перевести указанное OTAU,
	 *            либо -1, если надо перейти в состо€ние 'отключено'
	 * ¬нимание: в OTAU есть тайм-аут около 75 сек, после которого
	 *           OTAU автоматически переходит в состо€ние 'отключено'.
	 *           ¬о избежании этой проблемы, пользуйтесь repeatStatus()
	 */
	int switch_OTAU(int otauId, int otauPort); // rc: 0: fail, 1: success
	/**
	 * Ёту команду надо выполн€ть с периодом не менее 75 сек,
	 * чтобы предотвратить автоматическое отключение OTAU.
	 * ѕожалуйста, учитывайте также врем€ выполнени€ самой этой команды.
	 * –екомендую значени€ интервала 20-50 секунд.
	 * ѕри работе с несколькими OTAU, эта команда поддерживает сразу все
	 * OTAU, накод€щиес€ в состо€нии 'соединено'.
	 */
	int repeatStatus(); // rc: 0: fail, 1: success
};

#endif
