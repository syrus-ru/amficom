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

public class EmptySessionInfo extends SessionInterface
{
	// переменные для организации сессии
	public String ISMuser;			// пользователь системы
	public String ISMpassword;		// пароль пользователя
	public EmptyConnectionInfo ci;		// соединение сессии

	public int sessid;						// уникальный идентификатор сессии
	public long LogonTime;					// время начала сессии
	public int session_state = 0;			// состояние сессии

	public String domain_id = "";

	// базовый конструктор без параметров
	public EmptySessionInfo()
	{
		// установить начальные значения параметров сессии
		initialize();
	}

	// конструктор - новая сессия для соединения
	public EmptySessionInfo(ConnectionInterface ci)
	{
		this();
		if(ci instanceof EmptyConnectionInfo)
			this.ci = (EmptyConnectionInfo )ci;
	}

	// инициализация параметров сессии
	// параметры берутся из файла настроек или по умолчанию
	public void initialize()
	{
		// если файл настроек открыт, то вынуть из него значения параметров
		ISMuser = "userName";
	}

	public void SetDefaults()
	{
	}

	// открыть сессию с установленными для нее параметрами
	public SessionInterface OpenSession()
	{
		// если не указано соединение то берем соединение по умолчанию
		if(ci == null)
		{
			System.out.println("ci for si " + this.getUser() + " was null");
			return null;
		}

		// если соединение не установлено то произвести соединение
		// с сервером
		if(!ci.isConnected())
		{
			System.out.println("ci " + ci.getServiceURL() + ci.getObjectName() + " for si " + this.ISMuser + " not connected");
				return null;
		}
		LogonTime = System.currentTimeMillis();
		Log("Logged on on " +
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss").
					format(new java.util.Date(LogonTime)) +
			" as " + ISMuser);
		add(this);				// добавить в список открытых сессий
		session_state = SESSION_OPENED;	// состояние сессии - открыта
		setActiveSession(this);
		return this;
	}

	// закрыть сессию
	public void CloseSession()
	{
		// если сессия открыта, то закрыть
		if(contains(this))
		{
			ci.sessions--;
		}
		session_state = SESSION_CLOSED;
		remove(this);		// удалить из списка открытых сессий
		if(isEmpty())
		{
			setActiveSession(null);
			System.out.println("this is last session = closing connection");
		}
	}

	// есть ли открытая сессия
	public boolean isOpened()
	{
		return session_state == SESSION_OPENED;
//		return !(sessions.isEmpty());
	}

	public void setUser(String u)
	{
		ISMuser = u;
	}
	
	public String getUser()
	{
		return ISMuser;
	}
	
	public void setPassword(String p)
	{
		ISMpassword = p;
	}
	
	public String getPassword()
	{
		return ISMpassword;
	}
	
	public void setLogonTime(long t)
	{
		LogonTime = t;
	}
	
	public long getLogonTime()
	{
		return LogonTime;
	}
	
	public ConnectionInterface getConnectionInterface()
	{
		return ci;
	}
	
	public String getUserId()
	{
		return getUser();
	}
	
	public void setDomainId(String domain_id)
	{
		this.domain_id = domain_id;
	}
	
	public String getDomainId()
	{
		return domain_id;
	}

	public String toString()
	{
		return "EmptySessionInfo for user " + this.getUser() + " opened " + this.isOpened();
	}
}

