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
// *        Client\Resource\Scheme\ElementAttribute.java                  * //
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

package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;

import java.awt.*;

import com.syrus.AMFICOM.CORBA.Scheme.ElementAttribute_Transferable;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ElementAttribute extends StubResource
		implements Cloneable, Serializable
{
	private static final long serialVersionUID = 01L;
	public ElementAttribute_Transferable transferable;

	public String id = "";
	public String name = "";
	public String value = "";
	public String type_id = "";
	public boolean editable = true;
	public boolean visible = true;

	static public final String typ = "attribute";

	public ElementAttribute()
	{
		transferable = new ElementAttribute_Transferable();
	}

	public ElementAttribute(ElementAttribute_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public ElementAttribute(
			String id,
			String name,
			String value,
			String type_id)
	{
		this.id = id;
		this.name = name;
		this.value = value;
		this.type_id = type_id;

		transferable = new ElementAttribute_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		ElementAttribute ea = new ElementAttribute(
				dataSource.GetUId(ElementAttribute.typ),
				name,
				value,
				type_id);

		ea.changed = changed;
		ea.editable = editable;
		ea.visible = visible;

		return ea;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		value = transferable.value;
		type_id = transferable.type_id;
		editable = transferable.editable;
		visible = transferable.visible;
	}

	public void setTransferableFromLocal()
	{
		transferable.id = id;
		transferable.name = name;
		transferable.value = value;
		transferable.type_id = type_id;
		transferable.editable = editable;
		transferable.visible = visible;
	}

	public void updateLocalFromTransferable()
	{
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return type_id;
	}

	public String getDomainId()
	{
		return "sysdomain";
	}

	public String getTyp()
	{
		return typ;
	}

	public Component getRenderer()
		{
		if(type_id.equals("thickness"))
		{
			LineThickComboBox lineThickComboBox = new LineThickComboBox();
			lineThickComboBox.setSelected(new Integer(Integer.parseInt(value)));
			return lineThickComboBox;
		}
		else
		if(type_id.equals("color"))
		{
			ColorComboBox colorComboBox = new ColorComboBox();
			colorComboBox.setSelected(new Color(Integer.parseInt(value)));
			return colorComboBox;
		}
		else
		if(type_id.equals("style"))
		{
			LineComboBox lineComboBox = new LineComboBox();
			lineComboBox.setSelected(value);
			return lineComboBox;
		}
		else
		if(type_id.equals("show_length"))
		{
			TrueFalseComboBox myTrueFalseComboBox = new TrueFalseComboBox();
			TrueFalseComboBox.CurentState cs = myTrueFalseComboBox.returnValue;
			cs.setState(Boolean.getBoolean(value));
			myTrueFalseComboBox.setState(cs);
			return myTrueFalseComboBox;
		}
		else
			return null;
	}

	public Component getEditor()
		{
		if(type_id.equals("thickness"))
		{
			LineThickComboBox lineThickComboBox = new LineThickComboBox();
			lineThickComboBox.setSelected(new Integer(Integer.parseInt(value)));
			return lineThickComboBox;
		}
		else
		if(type_id.equals("color"))
		{
			ColorComboBox colorComboBox = new ColorComboBox();
			colorComboBox.setSelected(new Color(Integer.parseInt(value)));
			return colorComboBox;
		}
		else
		if(type_id.equals("style"))
		{
			LineComboBox lineComboBox = new LineComboBox();
			lineComboBox.setSelected(value);
			return lineComboBox;
		}
		else
		if(type_id.equals("show_length"))
		{
			TrueFalseComboBox myTrueFalseComboBox = new TrueFalseComboBox();
			TrueFalseComboBox.CurentState cs = myTrueFalseComboBox.returnValue;
			cs.setState(Boolean.getBoolean(value));
			myTrueFalseComboBox.setState(cs);
			return myTrueFalseComboBox;
		}
		else
			return null;
	}

	public void setValue(Object val)
	{
		if(type_id.equals("thickness"))
		{
			value = String.valueOf(((Integer )val).intValue());
		}
		else
		if(type_id.equals("color"))
		{
			value = String.valueOf(((Color )val).getRGB());
		}
		else
		if(type_id.equals("style"))
		{
			value = ((LineComboBox.MyLine)val).text;
		}
		else
		if(type_id.equals("show_length"))
		{
			boolean bool = ((TrueFalseComboBox.CurentState)val).getState();
			value = String.valueOf(bool);
		}
		else
			value = (String )val;
	}

	public boolean isEditable()
	{
		return editable;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public Object clone()
	{
		ElementAttribute ea = new ElementAttribute( id, name, value, type_id);
		return ea;
	}

	public ElementAttribute duplicate()
	{
		try
		{
			return (ElementAttribute )clone();
		}
		catch(Exception e)
		{
			System.out.println("could not clone");
			e.printStackTrace();
			return null;
		}
//		return new ElementAttribute(id, name, value, type_id);
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(type_id);
		out.writeObject(value);
		out.writeBoolean(editable);
		out.writeBoolean(visible);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		type_id = (String )in.readObject();
		value = (String )in.readObject();
		editable = in.readBoolean();
		visible = in.readBoolean();

		transferable = new ElementAttribute_Transferable();
	}
}
