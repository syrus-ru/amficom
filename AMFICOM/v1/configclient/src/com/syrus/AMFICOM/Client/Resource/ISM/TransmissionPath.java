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
import com.syrus.AMFICOM.Client.Configure.UI.*;

public class TransmissionPath extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "path";

	public TransmissionPath_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String KIS_id = "";
	public String access_port_id = "";
	public String local_address = "";
	public String monitored_element_id = "";
	public String domain_id = "";

	public long modified;

	public Vector links = new Vector();

	public Hashtable characteristics = new Hashtable();

	public TransmissionPath()
	{
		transferable = new TransmissionPath_Transferable();
	}

	public TransmissionPath(
			String id,
			String name,
			String description,
			String KIS_id,
			String access_port_id,
			String local_address)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.KIS_id = KIS_id;
		this.access_port_id = access_port_id;
		this.local_address = local_address;

		transferable = new TransmissionPath_Transferable();
	}

	public TransmissionPath(TransmissionPath_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		KIS_id = transferable.KIS_id;
		access_port_id = transferable.access_port_id;
		local_address = transferable.local_address;
		monitored_element_id = transferable.monitored_element_id;
		domain_id = transferable.domain_id;

		modified = transferable.modified;

//		MyUtil.addToVector(link_ids, transferable.link_ids);

		for(int i = 0; i < transferable.links.length; i++)
			links.add( new TransmissionPathElement(transferable.links[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.KIS_id = KIS_id;
		transferable.access_port_id = access_port_id;
		transferable.local_address = local_address;
		transferable.monitored_element_id = monitored_element_id;
		transferable.domain_id = domain_id;

		transferable.modified = modified;

//		link_ids.copyInto(transferable.link_ids);
		transferable.links = new TransmissionPathElement_Transferable[links.size()];
		for(int i = 0; i < links.size(); i++)
		{
			TransmissionPathElement link = (TransmissionPathElement )links.get(i);
			transferable.links[i] = (TransmissionPathElement_Transferable )link.getTransferable();
		}

		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Enumeration e = characteristics.elements(); e.hasMoreElements();)
		{
			Characteristic ch = (Characteristic )e.nextElement();
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

	public long getModified()
	{
		return modified;
	}

	public void updateLocalFromTransferable()
	{
/*
		links = new Vector();
		for(int i = 0; i < link_ids.size(); i++)
		{
			Link link = (Link )Pool.get("link", (String )link_ids.get(i));
			links.add(link);
		}
*/
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public ObjectResourceModel getModel()
	{
		return new TransmissionPathModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new TransmissionPathDisplayModel();
	}

	public static PropertiesPanel getPropertyPane()
	{
//		return new TransmissionPathGeneralPanel();
		return new TransmissionPathPane();
	}

	public Vector sortPorts()
	{
		Vector vec;
		Enumeration e;
		AccessPort ap;
		ObjectResource endport;
		ObjectResource startport;
		Equipment cur_eq;
		ObjectResource bufendport;
		ObjectResource bufstartport;
		TransmissionPathElement tpe;

		vec = new Vector();

		ap = (AccessPort )Pool.get(AccessPort.typ, this.access_port_id);
		cur_eq = (Equipment )Pool.get("kisequipment", ap.KIS_id);

		TransmissionPathElement []pe =
				(TransmissionPathElement[])links.toArray(new TransmissionPathElement[links.size()]);
		TransmissionPathElement []tmp = new TransmissionPathElement[pe.length];

		for(int i = 0; i < pe.length; i++)
			tmp[pe[i].n] = pe[i];
		pe = tmp;

		for(int i = 0; i < pe.length; i++) // count through all of the Path Elements
		{
			tpe = pe[i];
			if(tpe.thread_id == null || tpe.thread_id.equals(""))
			{
				Link link = (Link )Pool.get(Link.typ, tpe.link_id);
				bufstartport = (ObjectResource )Pool.get(Port.typ, link.start_port_id);
				bufendport = (ObjectResource )Pool.get(Port.typ, link.end_port_id);

				if(((Port )bufstartport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufstartport;
					endport = bufendport;
				}
				else
				if(((Port )bufendport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufendport;
					endport = bufstartport;
				}
				else
					return new Vector();

				cur_eq = (Equipment )Pool.get("kisequipment", ((Port )endport).equipment_id);
			}
			else
			{
				CableLink link = (CableLink )Pool.get(CableLink.typ, tpe.link_id);
				bufstartport = (ObjectResource )Pool.get(CablePort.typ, link.start_port_id);
				bufendport = (ObjectResource )Pool.get(CablePort.typ, link.end_port_id);

				if(((CablePort )bufstartport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufstartport;
					endport = bufendport;
				}
				else
				if(((CablePort )bufendport).equipment_id.equals(cur_eq.getId()))
				{
					startport = bufendport;
					endport = bufstartport;
				}
				else
					return new Vector();

				cur_eq = (Equipment )Pool.get("kisequipment", ((CablePort )endport).equipment_id);
			}
			vec.add(startport);
			vec.add(endport);
		}
		return vec;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(KIS_id);
		out.writeObject(access_port_id);
		out.writeObject(local_address);
		out.writeObject(monitored_element_id);
		out.writeObject(domain_id);
		out.writeLong(modified);
		out.writeObject(links);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		KIS_id = (String )in.readObject();
		access_port_id = (String )in.readObject();
		local_address = (String )in.readObject();
		monitored_element_id = (String )in.readObject();
		domain_id = (String )in.readObject();
		modified = in.readLong();
		links = (Vector )in.readObject();
		characteristics = (Hashtable )in.readObject();

		transferable = new TransmissionPath_Transferable();
		updateLocalFromTransferable();
	}
}