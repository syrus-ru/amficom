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

public abstract class SessionInterface
{

	static public final int SESSION_CLOSED = 0;	// ��������������� ���������
	static public final int SESSION_OPENED = 1;	// ��������� ������

	static private Vector sessions = new Vector();	// �������� ������

	static private SessionInterface active_session;	// �������� ������

	// ������������� ���������� ������
	// ��������� ������� �� ����� �������� ��� �� ���������
	abstract public void initialize();
	// ������� ������ � �������������� ��� ��� �����������
	abstract public SessionInterface OpenSession();
	// ������� ������
	abstract public void CloseSession();
	// ���������� �������� ���������� �� ���������
	abstract public void SetDefaults();
	// ���� �� �������� ������
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
