//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: класс содержит информацию о сессии работы пользователя     * //
// *           с системой                                                 * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\SessionInfo.java                               * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General;

import java.util.Vector;

public abstract class SessionInterface
{

	static public final int SESSION_CLOSED = 0;	// вспомогательные константы
	static public final int SESSION_OPENED = 1;	// состояния сессии

	static private Vector sessions = new Vector();	// открытые сессии

	static private SessionInterface active_session;	// активная сессия

	// инициализация параметров сессии
	// параметры берутся из файла настроек или по умолчанию
	abstract public void initialize();
	// открыть сессию с установленными для нее параметрами
	abstract public SessionInterface OpenSession();
	// закрыть сессию
	abstract public void CloseSession();
	// установить значения параметров по умолчанию
	abstract public void SetDefaults();
	// есть ли открытая сессия
	abstract public boolean isOpened();

	abstract public void setUser(String u);
	abstract public String getUser();
	abstract public void setPassword(String p);
	abstract public String getPassword();
	abstract public void setLogonTime(long t);
	abstract public long getLogonTime();

	abstract public String toString();
	abstract public ConnectionInterface getConnectionInterface();

	abstract public String getUserId();

	abstract public void setDomainId(String domain_id);
	abstract public String getDomainId();

	static public void CloseSession(SessionInterface si)
	{
		if(si == null)
			return;
		si.CloseSession();
	}

	static public void setActiveSession(SessionInterface si)
	{
		if(si != null)
//			if(si.isOpened())
				active_session = si;
	}

	static public SessionInterface getActiveSession()
	{
		return active_session;
	}

	static public void add(SessionInterface si)
	{
		sessions.add(si);
	}

	static public void remove(SessionInterface si)
	{
		sessions.remove(si);
	}

	static public boolean contains(SessionInterface si)
	{
		return sessions.contains(si);
	}

	static public boolean isEmpty()
	{
		return sessions.isEmpty();
	}

	static public void Log(String s)
	{
		System.out.println (s);
	}

	static public void Log(Object s)
	{
		System.out.println (s.toString());
	}
}
