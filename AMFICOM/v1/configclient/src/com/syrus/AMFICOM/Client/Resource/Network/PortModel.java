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

package com.syrus.AMFICOM.Client.Resource.Network;

import java.util.Map;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;

public class PortModel extends CatalogElementModel
{
	private Port port;
	public PortModel(Port port)
	{
		super(port);
		this.port = port;
	}

	public String getColumnValue(String col_id)
	{
		if(col_id.equals("id"))
			return port.id;
		if(col_id.equals("name"))
			return port.name;
		if(col_id.equals("description"))
			return port.description;
		if(col_id.equals("type_id"))
			return port.type_id;
		if(col_id.equals("equipment_id"))
			return port.equipment_id;
		if(col_id.equals("interface_id"))
			return port.interface_id;
		if(col_id.equals("address_id"))
			return port.address_id;
		if(col_id.equals("domain_id"))
			return port.domain_id;

		return "";
	}

	public Map getCharacteristics(ObjectResource obj)
	{
		return port.characteristics;
	}
}

