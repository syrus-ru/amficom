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
// * ��������: ���������� ��������� ����� ���������� ��������� ����       * //
// *           (�������� ���������� ������ pmServer � ������ pmRISDImpl)  * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.2                                                          * //
// * ��: 22 jan 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\ISM\KIS.java                                 * //
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

package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Network.CatalogElementModel;

public class PortTypeModel extends CatalogElementModel
{
	private PortType portType;
	public PortTypeModel(PortType portType)
	{
		super(portType);
		this.portType = portType;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return portType.id;
		if(col_id.equals("name"))
			return portType.name;
		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return portType.characteristics;
	}
}

