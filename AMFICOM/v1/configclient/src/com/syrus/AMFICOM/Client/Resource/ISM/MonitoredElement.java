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
// * Название: описание линии связи                                       * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Network\Link.java                             * //
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

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.ISM.*;
import com.syrus.AMFICOM.Client.General.UI.*;

public class MonitoredElement extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "monitoredelement";

	public MonitoredElement_Transferable transferable;

	public String id = "";
	public String element_name = "";
	public String element_type = "";
	public String element_id = "";
	public String local_address = "";
	public String access_port_id = "";
	public String domain_id = "";

	public long modified;

	public ObjectResource element;

	public MonitoredElement()
	{
		transferable = new MonitoredElement_Transferable();
	}

	public MonitoredElement(MonitoredElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		element_name = transferable.element_name;
		element_id = transferable.element_id;
		local_address = transferable.local_address;
		element_type = transferable.element_type;
		modified = transferable.modified;
		access_port_id = transferable.access_port_id;
		domain_id = transferable.domain_id;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.element_name = element_name;
		transferable.element_id = element_id;
		transferable.element_type = element_type;
		transferable.local_address = local_address;
		transferable.modified = modified;
		transferable.access_port_id = access_port_id;
		transferable.domain_id = domain_id;
	}

	public String getTyp()
	{
		return typ;
	}
	
	public String getName()
	{
		return element_name;
	}

	public String getId()
	{
		return id;
	}

	public String getDomainId()
	{
		return domain_id;
	}

	public long getModified()
	{
		return modified;
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new MonitoredElementDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new MonitoredElementModel(this);
	}

	public void updateLocalFromTransferable()
	{
		if(element_type.equals("path"))
		{
			element = (ObjectResource )Pool.get(TransmissionPath.typ, element_id);
		}
		else
		if(element_type.equals("equipment"))
		{
			element = (ObjectResource )Pool.get(Equipment.typ, element_id);
		}
		else
		if(element_type.equals("port"))
		{
			element = (ObjectResource )Pool.get(Port.typ, element_id);
		}
		else
		if(element_type.equals("link"))
		{
			element = (ObjectResource )Pool.get(Link.typ, element_id);
		}
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(element_name);
		out.writeObject(element_type);
		out.writeObject(element_id);
		out.writeObject(local_address);
		out.writeLong(modified);
		out.writeObject(access_port_id);
		out.writeObject(domain_id);
		out.writeObject(element);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		element_name = (String )in.readObject();
		element_type = (String )in.readObject();
		element_id = (String )in.readObject();
		local_address = (String )in.readObject();
		modified = in.readLong();
		access_port_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		element = (ObjectResource )in.readObject();

		transferable = new MonitoredElement_Transferable();
		updateLocalFromTransferable();
	}
}