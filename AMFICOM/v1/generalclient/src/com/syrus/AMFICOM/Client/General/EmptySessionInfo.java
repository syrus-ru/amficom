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
// * ��������: ����� �������� ���������� � ������ ������ ������������     * //
// *           � ��������                                                 * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\SessionInfo.java                               * //
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

import java.util.Vector;

public class EmptySessionInfo extends SessionInterface
{
	// ���������� ��� ����������� ������
	public String ISMuser;			// ������������ �������
	public String ISMpassword;		// ������ ������������
	public EmptyConnectionInfo ci;		// ���������� ������

	public int sessid;						// ���������� ������������� ������
	public long LogonTime;					// ����� ������ ������
	public int session_state = 0;			// ��������� ������

	public String domain_id = "";

	// ������� ����������� ��� ����������
	public EmptySessionInfo()
	{
		// ���������� ��������� �������� ���������� ������
		initialize();
	}

	// ����������� - ����� ������ ��� ����������
	public EmptySessionInfo(ConnectionInterface ci)
	{
		this();
		if(ci instanceof EmptyConnectionInfo)
			this.ci = (EmptyConnectionInfo )ci;
	}

	// ������������� ���������� ������
	// ��������� ������� �� ����� �������� ��� �� ���������
	public void initialize()
	{
		// ���� ���� �������� ������, �� ������ �� ���� �������� ����������
		ISMuser = "userName";
	}

	public void SetDefaults()
	{
	}

	// ������� ������ � �������������� ��� ��� �����������
	public SessionInterface OpenSession()
	{
		// ���� �� ������� ���������� �� ����� ���������� �� ���������
		if(ci == null)
		{
			System.out.println("ci for si " + this.getUser() + " was null");
			return null;
		}

		// ���� ���������� �� ����������� �� ���������� ����������
		// � ��������
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
		add(this);				// �������� � ������ �������� ������
		session_state = SESSION_OPENED;	// ��������� ������ - �������
		setActiveSession(this);
		return this;
	}

	// ������� ������
	public void CloseSession()
	{
		// ���� ������ �������, �� �������
		if(contains(this))
		{
			ci.sessions--;
		}
		session_state = SESSION_CLOSED;
		remove(this);		// ������� �� ������ �������� ������
		if(isEmpty())
		{
			setActiveSession(null);
			System.out.println("this is last session = closing connection");
		}
	}

	// ���� �� �������� ������
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

