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
// *        Client\General\EmptyConnectionInfo.java                       * //
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

public class EmptyConnectionInfo extends ConnectionInterface
{
	// значения необходимых для соединения с сервером переменных по умолчанию
	static public final String DEFAULT_objectName = "/AMFICOM/AMFICOM";
	static public final String DEFAULT_serviceURL = "sess_iiop://research:2481:Research";
	static public final String DEFAULT_user = "AMFICOM";
	static public final String DEFAULT_password = "amficom";

	// переменные соединения с сервером
	public String objectName = "";	// имя серверного объекта
	public String serviceURL = "";	// строка соединения с сервером по IIOP
	public String user = "";		// имя пользователя для доступа к серверному
									// объекту
	public String password = "";	// пароль доступа к серверному объекту

	public long ConnectTime;		// время подсоединения к серверу
	public int sessions;			// число открытых сессий по данному
									// соединению
	public int connection_state = 0;// индикатор наличия/отсутствия связи

	// базовый конструктор без параметров
	public EmptyConnectionInfo()
	{
		// установить начальные значения параметров соединения
		initialize();
	}

	// инициализация параметров соединения
	// параметры берутся из файла настроек или по умолчанию
	public void initialize()
	{
		objectName = "test";
		serviceURL = "test";
		user = "test";
		password = "test";
	}

	protected Object clone() throws CloneNotSupportedException {
		EmptyConnectionInfo emptyConnectionInfo = null;
		try {
			emptyConnectionInfo = (EmptyConnectionInfo) (super.clone());
			emptyConnectionInfo.connection_state = connection_state;
			emptyConnectionInfo.sessions = sessions;
			emptyConnectionInfo.ConnectTime = ConnectTime;
			emptyConnectionInfo.objectName = new String(objectName);
			emptyConnectionInfo.password = new String(password);
			emptyConnectionInfo.serviceURL = new String(serviceURL);
			emptyConnectionInfo.user = new String(user);
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
		return emptyConnectionInfo;
	}

	// установить значения параметров по умолчанию
	public void SetDefaults()
	{
		objectName = DEFAULT_objectName;
		serviceURL = DEFAULT_serviceURL;
		user = DEFAULT_user;
		password = DEFAULT_password;
	}

	public ConnectionInterface Connect()
	{
		connection_state = CONNECTION_OPENED;
		ConnectTime = System.currentTimeMillis();
		sessions = 0;
		add(this);

		// соединение становиться активным соединением
		setActiveConnection(this);
		return this;
	}

	// разорвать соединение с сервером
	public void Disconnect()
	{
		// прежде чем закрыть соединение должны быть закрыты все сессии по
		// данному соединению
		if(sessions > 0)
			return;
		connection_state = CONNECTION_CLOSED;
		// соединение определяется контекстом
		if(contains(this))
		{
			System.out.println("Disconnecting " + serviceURL + objectName);
		}
		// изменить состояние соединения
		remove(this);
		if(this.equals(getActiveConnection()))
			setActiveConnection(null);
	}

	public void setObjectName(String on)
	{
		objectName = on;
	}

	public String getObjectName()
	{
		return objectName;
	}

	public void setServiceURL(String su)
	{
		serviceURL = su;
	}

	public String getServiceURL()
	{
		return serviceURL;
	}

	public void setServerIP(String sip)
	{
	}

	public String getServerIP()
	{
		return serviceURL;
	}

	public void setTCPport(String p)
	{
	}

	public String getTCPport()
	{
		return serviceURL;
	}

	public void setSID(String sid)
	{
	}

	public String getSID()
	{
		return serviceURL;
	}

	public void setUser(String u)
	{
		user = u;
	}

	public String getUser()
	{
		return user;
	}

	public void setPassword(String p)
	{
		password = p;
	}

	public String getPassword()
	{
		return password;
	}

	// есть ли соединение с сервером
	public boolean isConnected()
	{
		return connection_state == CONNECTION_OPENED;
	}

	public String toString()
	{
		return "EmptyConnectionInfo service " + this.getServiceURL() + " server object " + this.objectName + " user " + this.getUser() + " connected " + this.isConnected();
	}
}
