#ifndef _BGSWITCHER_H
#define _BGSWITCHER_H

/**
 * Переключатель одного OTAU на данном COM-порту, обеспечивающий
 * асинхронную (в отдельном потоке) поддержку OTAU во включенном состоянии.
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
	 * Создать переключатель для данного COM-порта и данного OTAU.
	 * Вызывающий поток блокируется до окончания инициализации.
	 * hardInitMode - true, если пользователь хочет при инициализации перезагрузить OTAU. Рекомендуется значение false
	 */
	BgSwitcher(int com_port, int otau_id, bool hardInitMode); // XXX: blocks till end of initialization
	/**
	 * Завершает работу с COM-портом и OTAU
	 */
	~BgSwitcher();

	// client API - status
	/**
	 * Узнать, возникли ли ошибки.
	 * Если ошибка возникла, работа прекращается и команды
	 * игнорируются. Все, что клиенту остается предпринять для исправления
	 * возникшей ошибки - это удалить этот экземпляр BgSwitcher'а,
	 * и создать новый.
	 * При возникновении ошибки сначала прекращается работа с OTAU и
	 * освобождается COM-порт, и только после этого выставляется флаг ошибки.
	 * 
	 */
	bool c_isError(); // returns true if a error ecountered (any error is treated as unrecoverable)

	// client API - switch in sync mode
	/**
	 * Переключить порт.
	 * port - положительное число - желаемый номер порта,
	 *        либо SWITCHER_PORT_DISCONNECTED, если надо перейти в состояние 'отключено'
	 * Метод заканчивает работу только после окончания переключения
	 * или при обнаружении ошибки.
	 * Если запрошенный номер порта уже включен, метод немедленно возвращает управление (returns).
	 */
	void c_switchTo(int port); // blocks till switch is complete

	// client APU - switch in async mode
	/**
	 * Переключить порт (асинхронно)
	 * port - положительное число - желаемый номер порта,
	 *        либо SWITCHER_PORT_DISCONNECTED, если надо перейти в состояние 'отключено'
	 * Метод заканчивает работу раньше, чем после окончания переключения,
	 * а во многих случаях не блокирует вообще.
	 * (фактически, он дожидается лишь окончания переключения от предыдущего c_switchToAsync())
	 * Для того, чтобы дождаться момента, когда порт будет реально переключен,
	 * используйте c_wait()
	 */
	void c_switchToAsync(int port); // blocks for a smaller period; will need to invoke c_wait()
	/**
	 * Дождаться момента, когда закончится операция переключения порта, начатая c_switchToAsync()
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
