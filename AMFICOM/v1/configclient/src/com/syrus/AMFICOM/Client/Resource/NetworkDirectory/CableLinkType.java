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
// *        Client\Resource\NetworkDirectory\LinkType.java                * //
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

import java.io.*;
import java.util.*;

import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Configure.UI.CableLinkTypePane;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

public class CableLinkType extends StubResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "cablelinktype";

	public CableLinkType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String codename = "";
	public String description = "";
	public String year = "";
	public String standard = "";
	public String link_class = "";
	public String manufacturer = "";
	public String manufacturer_code = "";
	public boolean is_holder;

	public transient boolean is_modified = false;

	public String image_id = "";
	public long modified;

	public Map characteristics = new HashMap();

	public List cable_threads = new ArrayList();

	public CableLinkType()
	{
		transferable = new CableLinkType_Transferable();
	}

	public CableLinkType(CableLinkType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public CableLinkType(
			String id,
			String name)
	{
		this.id = id;
		this.name = name;

		transferable = new CableLinkType_Transferable();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		codename = transferable.codename;
//		description = transferable.description;
//		year = transferable.year;
//		standard = transferable.standard;
		link_class = transferable.link_class;
		manufacturer = transferable.manufacturer;
		manufacturer_code = transferable.manufacturer_code;
//		is_holder = transferable.is_holder;

		image_id = transferable.image_id;
		modified = transferable.modified;

		for (int i = 0; i < transferable.cable_threads.length; i++)
			cable_threads.add(new CableTypeThread(transferable.cable_threads[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.codename = codename;
//		transferable.description = description;
//		transferable.year = year;
//		transferable.standard = standard;
		transferable.link_class = link_class;
		transferable.manufacturer = manufacturer;
		transferable.manufacturer_code = manufacturer_code;
//		transferable.is_holder = is_holder;

		transferable.image_id = image_id;
		transferable.modified = modified;

		transferable.cable_threads = new CableTypeThread_Transferable[cable_threads.size()];

		int counter = 0;
		for (Iterator it = cable_threads.iterator(); it.hasNext();)
		{
			CableTypeThread thread = (CableTypeThread)it.next();
			thread.setTransferableFromLocal();
			transferable.cable_threads[counter++] = (CableTypeThread_Transferable)thread.getTransferable();
		}

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

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new CableLinkTypeDisplayModel();
	}

	public ObjectResourceModel getModel()
	{
		return new CableLinkTypeModel(this);
	}

	public static PropertiesPanel getPropertyPane()
	{
		return new CableLinkTypePane();
//		return new CableLinkGeneralPanel();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(codename);
		out.writeObject(link_class);
		out.writeObject(manufacturer);
		out.writeObject(manufacturer_code);
		out.writeObject(image_id);
		out.writeLong(modified);
		out.writeObject(characteristics);

		out.writeObject(cable_threads);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		codename = (String )in.readObject();
		link_class = (String )in.readObject();
		manufacturer = (String )in.readObject();
		manufacturer_code = (String )in.readObject();
		image_id = (String )in.readObject();
		modified = in.readLong();
		characteristics = (Map )in.readObject();

		cable_threads = (List )in.readObject();

		transferable = new CableLinkType_Transferable();
	}
}
