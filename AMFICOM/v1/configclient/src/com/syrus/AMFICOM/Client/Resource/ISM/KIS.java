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
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\ISM\KIS.java                                 * //
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

package com.syrus.AMFICOM.Client.Resource.ISM;

import java.util.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Configure.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.ISM.*;

public class KIS extends Equipment
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "kis";
	public Collection access_ports = new ArrayList();

	public KIS()
	{
		super();
	}

	public KIS(Equipment_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public KIS(KISType eq_type)
	{
		id = "new_id";
		name = eq_type.name;
		type_id = eq_type.id;
		longitude = "0.0";
		latitude = "0.0";

		hw_serial = "";
		sw_serial = "";
		hw_version = "";
		sw_version = "";
		description = "equipment " + eq_type.name;
		inventory_nr = "";
		manufacturer = eq_type.manufacturer;
		manufacturer_code = "";
		supplier = "";
		supplier_code = "";

		eq_class = eq_type.eq_class;
		image_id = eq_type.image_id;
		agent_id = "";

//		remarks = new Vector();

		ports = new ArrayList();
		characteristics = new HashMap();
		access_ports = new ArrayList();

		transferable = new Equipment_Transferable();
	}

	public KIS(
			String id,
			String name,
			String type_id,
			String hw_serial,
			String sw_serial,
			String longitude,
			String latitude,
			String description,
			Vector access_port_ids)
	{
		this.id = id;
		this.name = name;
		this.type_id = type_id;
		this.hw_serial = hw_serial;
		this.sw_serial = sw_serial;
		this.longitude = longitude;
		this.latitude = latitude;
		this.description = description;
		this.s_port_ids = access_port_ids;

		transferable = new Equipment_Transferable();
	}
/*
	public void setLocalFromTransferable()
	{
		super.setLocalFromTransferable();
		MyUtil.addToVector(access_port_ids, transferable.s_port_ids);
	}

	public void setTransferableFromLocal()
	{
		super.setTransferableFromLocal();
		transferable.s_port_ids = new String[access_port_ids.size()];
		access_port_ids.copyInto(transferable.s_port_ids);
	}
*/
	public String getTyp()
	{
		return typ;
	}

	public void updateLocalFromTransferable()
	{
		super.updateLocalFromTransferable();
		access_ports = new Vector();

		for(Iterator it = s_port_ids.iterator(); it.hasNext();)
			access_ports.add(Pool.get(AccessPort.typ, (String)it.next()));
	}
/*
	public ObjectResourceModel getModel()
	{
		return new KISModel(this);
	}
*/

	public static PropertiesPanel getPropertyPane()
	{
		return new KISPane();
	}

}

