//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ���������� ���������� � �������� �������                   * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 1.0                                                          * //
// * ��: 22 jun 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\EmptyConnectionInfo.java                       * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General;

public class EmptyConnectionInfo extends ConnectionInterface
{
	// �������� ����������� ��� ���������� � �������� ���������� �� ���������
	static public final String DEFAULT_objectName = "/AMFICOM/AMFICOM";
	static public final String DEFAULT_serviceURL = "sess_iiop://research:2481:Research";
	static public final String DEFAULT_user = "AMFICOM";
	static public final String DEFAULT_password = "amficom";

	// ���������� ���������� � ��������
	public String objectName = "";	// ��� ���������� �������
	public String serviceURL = "";	// ������ ���������� � �������� �� IIOP
	public String user = "";		// ��� ������������ ��� ������� � ����������
									// �������
	public String password = "";	// ������ ������� � ���������� �������

	public long ConnectTime;		// ����� ������������� � �������
	public int sessions;			// ����� �������� ������ �� �������
									// ����������
	public int connection_state = 0;// ��������� �������/���������� �����

	// ������� ����������� ��� ����������
	public EmptyConnectionInfo()
	{
		// ���������� ��������� �������� ���������� ����������
		initialize();
	}

	// ������������� ���������� ����������
	// ��������� ������� �� ����� �������� ��� �� ���������
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

	// ���������� �������� ���������� �� ���������
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

		// ���������� ����������� �������� �����������
		setActiveConnection(this);
		return this;
	}

	// ��������� ���������� � ��������
	public void Disconnect()
	{
		// ������ ��� ������� ���������� ������ ���� ������� ��� ������ ��
		// ������� ����������
		if(sessions > 0)
			return;
		connection_state = CONNECTION_CLOSED;
		// ���������� ������������ ����������
		if(contains(this))
		{
			System.out.println("Disconnecting " + serviceURL + objectName);
		}
		// �������� ��������� ����������
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

	// ���� �� ���������� � ��������
	public boolean isConnected()
	{
		return connection_state == CONNECTION_OPENED;
	}

	public String toString()
	{
		return "EmptyConnectionInfo service " + this.getServiceURL() + " server object " + this.objectName + " user " + this.getUser() + " connected " + this.isConnected();
	}
}
