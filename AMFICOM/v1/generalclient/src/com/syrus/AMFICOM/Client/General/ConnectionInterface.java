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
// *        Client\General\ConnectionInterface.java                       * //
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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public abstract class ConnectionInterface implements Cloneable
{
	static public final int CONNECTION_CLOSED = 0;// ��������������� ���������
	static public final int CONNECTION_OPENED = 1;// ��������� ����������

	static private Vector connections = new Vector();// ������ ����������

	static private ConnectionInterface active_connection;
											// �������� ���������� � ��������

	// ������������� ���������� ����������
	// ��������� ������� �� ����� �������� ��� �� ���������
	abstract public void initialize();
	// ���������� �������� ���������� �� ���������
	abstract public void SetDefaults();
	abstract public ConnectionInterface Connect();
	// ��������� ���������� � ��������
	abstract public void Disconnect();
	// ���� �� ���������� � ��������
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

	// ���������� ������� ���������� � ��������
	static public ConnectionInterface Connect(ConnectionInterface ci)
	{
		if(ci == null)
			return null;
		return ci.Connect();
	}

	// ��������� ���������� � ��������
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
	
	// ������� ��������������� ���������
	static public void Log(String s)
	{
		System.out.println (s);
	}

	static public void Log(Object s)
	{
		System.out.println (s.toString());
	}

}
