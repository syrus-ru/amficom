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
// * ��������: �����, ����������� ��������, ���������� ��� ���� ��������, * //
// *           ����������� � ���������� �� � ����. ������ ������          * //
// *           �������� ��������� ������� ������ � ����� ������������     * //
// *           �� ���� ����������� ����� ��� �������� ����������,         * //
// *           ��������� � ������� ����������� �� � ����                  * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\ObjectResource.java                           * //
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

package com.syrus.AMFICOM.Client.Resource;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class StubResource extends ObjectResource
{
	static final public String typ = "stub";

	public StubResource()
	{
		super();
	}

	public StubResource(String typ)
	{
		super(typ);
	}
	
	public String getTyp()
	{
		return typ;
	}
	
	public Object getTransferable()
	{
		return null;
	}
	
	public String getName()
	{
		return "";
	}
	
	public String getId()
	{
		return "";
	}

	public String getDomainId()
	{
		return "sysdomain";
	}
	
	public void setLocalFromTransferable()
	{
	}
	
	public void setTransferableFromLocal()
	{
	}
	
	public void updateLocalFromTransferable()
	{
	}

	public String getColumnName(String col_id)
	{
		return "";
	}

	public String getColumnValue(String col_id)
	{
		return "";
	}

	public String getPropertyValue(String col_id)
	{
		return "";
	}
	
	public String getPropertyName(String col_id)
	{
		return "";
	}

}
