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
// * Название: описание типов оборудования                                * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\NetworkDirectory\EquipmentType.java           * //
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


import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Configure.UI.EquipmentTypePane;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

import com.syrus.AMFICOM.CORBA.NetworkDirectory.EquipmentType_Transferable;
import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;


import java.io.*;
import java.util.*;

public class EquipmentType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "equipmenttype";

	public EquipmentType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String codename = "";
	public String eq_class = "";
	public String description = "";
	public String manufacturer = "";
	public String manufacturer_code = "";
	public boolean is_holder = false;
	public String image_id = "";
	public long modified = 0;

	public transient boolean is_modified = false;

	public Hashtable characteristics = new Hashtable();

	public EquipmentType()
	{
		transferable = new EquipmentType_Transferable();
	}

	public EquipmentType(EquipmentType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public EquipmentType(
			String id,
			String name,
			String codename,
			String eq_class,
			String description,
			boolean is_holder)
	{
		this.id = id;
		this.name = name;
		this.codename = codename;
		this.eq_class = eq_class;
		this.description = description;
		this.is_holder = is_holder;

		transferable = new EquipmentType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
		eq_class = transferable.eq_class;
		description = transferable.description;
		is_holder = transferable.is_holder;

		manufacturer = transferable.manufacturer;
//		manufacturer_code = transferable.manufacturer_code;

		image_id = transferable.image_id;
		modified = transferable.modified;

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
		transferable.eq_class = eq_class;
		transferable.description = description;

		transferable.manufacturer = manufacturer;
//		transferable.manufacturer_code = manufacturer_code;
		transferable.is_holder = is_holder;

		transferable.image_id = image_id;
		transferable.modified = modified;

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
    return new EquipmentTypeModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new EquipmentTypeDisplayModel();
  }

  public static PropertiesPanel getPropertyPane()
  {
    return new EquipmentTypePane();
  }

	public Object clone()
	{
		EquipmentType eqt = new EquipmentType();
		eqt.is_modified = true;

		eqt.transferable = new EquipmentType_Transferable();

		eqt.id = id;
		eqt.name = name;
		eqt.codename = codename;
		eqt.eq_class = eq_class;
		eqt.description = description;
		eqt.manufacturer = manufacturer;
		eqt.manufacturer_code = manufacturer_code;
		eqt.is_holder = is_holder;
		eqt.image_id =  image_id;
		eqt.modified = modified;

		eqt.characteristics = new Hashtable(characteristics.size());

		return eqt;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(eq_class);
		out.writeObject(description);
		out.writeObject(manufacturer);
		out.writeObject(manufacturer_code);
		out.writeObject(image_id);
		out.writeLong(modified);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		eq_class = (String )in.readObject();
		description = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturer_code = (String )in.readObject();
		image_id = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Hashtable )in.readObject();

		transferable = new EquipmentType_Transferable();
	}
}
