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

package com.syrus.AMFICOM.Client.Resource.ISMDirectory;

import java.io.Serializable;
import java.io.IOException;

import java.util.Hashtable;

import com.syrus.AMFICOM.Client.Configure.UI.TransmissionPathTypePane;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;

import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.CORBA.ISMDirectory.TransmissionPathType_Transferable;

public class TransmissionPathType extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	public static final String typ = "pathtype";

	private TransmissionPathType_Transferable transferable;

	public String id = "";
	public String name = "";
	public String description = "";
	public long modified;

	public Hashtable characteristics = new Hashtable();

	public TransmissionPathType()
	{
		transferable = new TransmissionPathType_Transferable();
	}

	public TransmissionPathType(TransmissionPathType_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public TransmissionPathType(
			String id,
			String name,
			String description)
	{
		this.id = id;
		this.name = name;
		this.description = description;

		transferable = new TransmissionPathType_Transferable();
	}

  public ObjectResourceModel getModel()
  {
    return new TransmissionPathTypeModel(this);
  }

  public static ObjectResourceDisplayModel getDefaultDisplayModel()
  {
    return new TransmissionPathTypeDisplayModel();
  }

  public static PropertiesPanel getPropertyPane()
  {
    return new TransmissionPathTypePane();
  }

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		description = transferable.description;
		modified = transferable.modified;

//		for(int i = 0; i < transferable.characteristics.length; i++)
//			characteristics.put(transferable.characteristics[i].type_id, new Characteristic(transferable.characteristics[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.description = description;
		transferable.modified = modified;
/*
		int l = this.characteristics.size();
		int i = 0;
		transferable.characteristics = new Characteristic_Transferable[l];
		for(Enumeration e = characteristics.elements(); e.hasMoreElements();)
		{
			Characteristic ch = (Characteristic )e.nextElement();
			ch.setTransferableFromLocal();
			transferable.characteristics[i++] = ch.transferable;
		}
*/
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
		out.writeLong(modified);
//		out.writeObject(characteristics);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		modified = in.readLong();
//		characteristics = (Hashtable )in.readObject();

		transferable = new TransmissionPathType_Transferable();
	}
}
