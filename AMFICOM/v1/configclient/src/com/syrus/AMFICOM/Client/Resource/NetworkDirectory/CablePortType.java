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
// * Название: описание типов сетевых портов                              * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.2                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\NetworkDirectory\PortType.java                * //
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

package com.syrus.AMFICOM.Client.Resource.NetworkDirectory;

import java.io.Serializable;
import java.io.IOException;

import java.util.Hashtable;
import java.util.Enumeration;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Configure.UI.CablePortTypePane;

import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CablePortType_Transferable;

public class CablePortType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "cableporttype";

	public CablePortType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String codename = "";
	public String description = "";
	public String year = "";
	public String body = "";
	public String standard = "";
	public String interface_id = "";
	public String p_class = "";
	public long modified = 0;

	public transient boolean is_modified = false;

	public Hashtable characteristics = new Hashtable();

	public CablePortType()
	{
		transferable = new CablePortType_Transferable();
	}

	public CablePortType(CablePortType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public CablePortType(
			String id,
			String name,
			String codename,
			String description,
			String year,
			String body,
			String standard,
			String interface_id)
	{
		this.id = id;
		this.name = name;
		this.codename = codename;
		this.description = description;
		this.year = year;
		this.body = body;
		this.standard = standard;
		transferable.interface_id = interface_id;

		transferable = new CablePortType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		description = transferable.description;
		year = transferable.year;
		body = transferable.body;
		standard = transferable.standard;
		interface_id = transferable.interface_id;
		modified = transferable.modified;
		p_class = transferable.p_class;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
		transferable.description = description;
		transferable.year = year;
		transferable.body = body;
		transferable.standard = standard;
		transferable.interface_id = interface_id;
		transferable.modified = modified;
		transferable.p_class = p_class;

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
		return "sysdomain";
	}

	public void updateLocalFromTransferable()
	{
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

  public ObjectResourceModel getModel()
  {
    return new CablePortTypeModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new CablePortTypeDisplayModel();
  }

  public static PropertiesPanel getPropertyPane()
  {
    return new CablePortTypePane();
  }

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(description);
		out.writeObject(year);
		out.writeObject(body);
		out.writeObject(standard);
		out.writeObject(interface_id);
		out.writeObject(p_class);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		description = (String )in.readObject();
		year = (String )in.readObject();
		body = (String )in.readObject();
		standard = (String )in.readObject();
		interface_id = (String )in.readObject();
		p_class = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Hashtable )in.readObject();

		transferable = new CablePortType_Transferable();
	}
}
