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
// * Версия: 0.2                                                          * //
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

package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import java.io.IOException;
import java.io.Serializable;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;

import com.syrus.AMFICOM.Client.Configure.UI.AccessPortTypePane;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.MyUtil;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;

import com.syrus.AMFICOM.CORBA.Network.Characteristic_Transferable;
import com.syrus.AMFICOM.CORBA.ISMDirectory.AccessPortType_Transferable;


public class AccessPortType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	static final public String typ = "accessporttype";

	private AccessPortType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public String access_type = "";
	public long modified;

	public Vector test_type_ids = new Vector();

	public Hashtable characteristics = new Hashtable();

	public AccessPortType()
	{
		transferable = new AccessPortType_Transferable();
	}

	public AccessPortType(AccessPortType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public AccessPortType(
			String id,
			String name,
			String description,
			String access_type)
	{
		this.id = id;
		this.name = name;
		this.description = description;
		this.access_type = access_type;

		transferable = new AccessPortType_Transferable();
	}

  public ObjectResourceModel getModel()
  {
    return new AccessPortTypeModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new AccessPortTypeDisplayModel();
  }

  public static PropertiesPanel getPropertyPane()
  {
    return new AccessPortTypePane();
  }

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		access_type = transferable.access_type;
		modified = transferable.modified;

		MyUtil.addToVector(test_type_ids, transferable.test_type_ids);

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].id, new Characteristic(transferable.characteristics[i]));

		for(int i = 0; i < transferable.characteristics.length; i++)
			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.access_type = access_type;
		transferable.modified = modified;

		test_type_ids.copyInto(transferable.test_type_ids);

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

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeObject(access_type);
		out.writeLong(modified);
		out.writeObject(test_type_ids);
		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		access_type = (String )in.readObject();
		modified = in.readLong();
		test_type_ids = (Vector )in.readObject();
		characteristics = (Hashtable )in.readObject();

		transferable = new AccessPortType_Transferable();
	}
}
