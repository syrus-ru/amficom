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
// * Название: Информация соединения с сервером системы                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 22 jun 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\ConnectionInterface.java                       * //
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class ConnectionInterface implements Cloneable
{
	static public final int CONNECTION_CLOSED = 0;// вспомогательные константы
	static public final int CONNECTION_OPENED = 1;// состояния соединения

	static private Vector connections = new Vector();// список соединений

	static private ConnectionInterface active_connection;
											// активное соединение с сервером

	// инициализация параметров соединения
	// параметры берутся из файла настроек или по умолчанию
	abstract public void initialize();
	// установить значения параметров по умолчанию
	abstract public void SetDefaults();
	abstract public ConnectionInterface Connect();
	// разорвать соединение с сервером
	abstract public void Disconnect();
	// есть ли соединение с сервером
	abstract public boolean isConnected();
	abstract public void setObjectName(String on);
	abstract public String getObjectName();
	abstract public void setServiceURL(String su);
	abstract public String getServiceURL();
	abstract public void setServerIP(String sip);
	abstract public String getServerIP();
	abstract public void setTCPport(String p);
	abstract public String getTCPport();
	abstract public void setSID(String sid);
	abstract public String getSID();
	abstract public void setUser(String u);
	abstract public String getUser();
	abstract public void setPassword(String p);
	abstract public String getPassword();
	abstract public String toString();

	protected Object clone() throws CloneNotSupportedException {
		ConnectionInterface connectionInterface = null;
		try {
			connectionInterface = (ConnectionInterface) (super.clone());
			/*
			 * No internal fields to clone.
			 */
		} catch (CloneNotSupportedException cnse) {
			/*
			 * Never.
			 */
			cnse.printStackTrace();
		}
		return connectionInterface;
	}

	public Object _clone() {
		try {
			return clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
			return null;
		}
	}

	// произвести попытку соединения с сервером
	static public ConnectionInterface Connect(ConnectionInterface ci)
	{
		if(ci == null)
			return null;
		return ci.Connect();
	}

	// разорвать соединение с сервером
	static public void Disconnect(ConnectionInterface ci)
	{
		if(ci == null)
			return;
		ci.Disconnect();
	}

	static public void setActiveConnection(ConnectionInterface ci)
	{
		if(ci != null)
//			if(ci.isConnected())
				active_connection = ci;
	}

	static public ConnectionInterface getActiveConnection()
	{
		return active_connection;
	}
	
	static public void add(ConnectionInterface ci)
	{
		connections.add(ci);
	}
	
	static public void remove(ConnectionInterface ci)
	{
		connections.remove(ci);
	}
	
	static public boolean contains(ConnectionInterface ci)
	{
		return connections.contains(ci);
	}
	
	// вывести диагностическое сообщение
	static public void Log(String s)
	{
		System.out.println (s);
	}

	static public void Log(Object s)
	{
		System.out.println (s.toString());
	}

}
