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
// *        Client\Resource\ISMDirectory\AccessPortType.java             * //
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

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.ISM.AccessPort_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;
import com.syrus.AMFICOM.Client.Configure.UI.AccessPortPane;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

public class AccessPort extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "accessport";

	private AccessPort_Transferable transferable;

	public String id = "";
	public String name = "";
	public String type_id = "";
	public String port_id = "";
	public String KIS_id = "";
	public String local_id = "";
	public String domain_id = "";

	public Map characteristics = new HashMap();

	public AccessPort()
	{
		transferable = new AccessPort_Transferable();
	}

	public AccessPort(AccessPort_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}
/*
	public AccessPort(MapConnectionPoint mcpe, KIS kis, Port port)
	{
		AccessPortType accessPortType = (AccessPortType )Pool.get("accessporttype", mcpe.access_port_type_id);
//		AccessPort accessPort = (AccessPort )Pool.get("accessport", mcpe.access_port_id);

		this.id = kis.id + "." + accessPortType.id;
		this.name = accessPortType.getName();
		this.type_id = mcpe.access_port_type_id;
		this.port_id = port.id;
		this.KIS_id = kis.id;
		this.local_id = "";//accessPort.local_id;

		transferable = new AccessPort_Transferable();
	}
*/
	public AccessPort(
			String id,
			String name,
			String type_id,
			String port_id,
			String KIS_id,
			String local_id)
	{
		this.id = id;
		this.name = name;
		this.type_id = type_id;
		this.port_id = port_id;
		this.KIS_id = KIS_id;
		this.local_id = local_id;

		transferable = new AccessPort_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		type_id = transferable.type_id;
		port_id = transferable.port_id;
		KIS_id = transferable.KIS_id;
		local_id = transferable.local_id;
		domain_id = transferable.domain_id;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.type_id = type_id;
		transferable.port_id = port_id;
		transferable.KIS_id = KIS_id;
		transferable.local_id = local_id;
		transferable.domain_id = domain_id;

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Iterator it = characteristics.values().iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			ch.setTransferableFromLocal();
			transferable.characteristics[i++] = ch.transferable;
		}
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return domain_id;
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new AccessPortModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new AccessPortDisplayModel();
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new AccessPortPane();
//		return new AccessPortGeneralPanel();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(port_id);
		out.writeObject(KIS_id);
		out.writeObject(local_id);
		out.writeObject(domain_id);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		type_id = (String )in.readObject();
		port_id = (String )in.readObject();
		KIS_id = (String )in.readObject();
		local_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		characteristics = (Map)in.readObject();

		transferable = new AccessPort_Transferable();
		updateLocalFromTransferable();
	}
}
