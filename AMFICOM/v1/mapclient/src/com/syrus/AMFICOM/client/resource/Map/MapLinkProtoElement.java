/**
 * $Id: MapLinkProtoElement.java,v 1.12 2004/12/07 17:02:03 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapLinkProtoElement_Transferable;
import com.syrus.AMFICOM.Client.General.UI.LineComboBox;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.lang.UnsupportedOperationException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * тип физической линии 
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2004/12/07 17:02:03 $
 * @module
 * @author $Author: krupenn $
 * @see
 */	
public final class MapLinkProtoElement extends StubResource
//		extends MapLinkElement
		implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "maplinkproto";

	public static final String TUNNEL = "tunnel";
	public static final String COLLECTIOR = "collector";
	public static final String UNBOUND = "cable";

	/**
	 * @deprecated
	 */
	protected String id = "";
	
	/**
	 * @deprecated
	 */
	protected String name = "";
	
	/**
	 * @deprecated
	 */
	protected String description = "";

	/** атрибуты отображения */
	public java.util.Map attributes = new HashMap();

	/**
	 * @deprecated
	 */
	protected MapLinkProtoElement_Transferable transferable;

	/**
	 * @deprecated
	 */
	protected long modified;
	
	/**
	 * Размерность тоннеля.
	 * Для тоннеля обозначает размерность матрицы труб в разрезе,
	 * для участка коллектора - число полок и мест на полках
	 */
	protected IntDimension bindingDimension;

	/** имя класса панели свойств объекта */
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapLinkProtoElement(
			String id,
			String name,
			String description,
			IntDimension bindingDimension)
	{
		super();
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setBindingDimension(bindingDimension);
	}
	
	/**
	 * @deprecated
	 */
	public MapLinkProtoElement(MapLinkProtoElement_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		this.setLocalFromTransferable();
	}

	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.modified = transferable.modified;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	/**
	 * @deprecated
	 */
	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.modified = modified;

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
	}

	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
	}
	
	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}
	
	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * @deprecated
	 */
	public String getDomainId()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @deprecated
	 */
	public long getModified()
	{
		return modified;
	}

	/**
	 * @deprecated
	 */
	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public String getToolTipText()
	{
		return "proto " + this.getName();
	}

	/**
	 * @deprecated
	 */
	public Point2D.Double getAnchor()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	/**
	 * @deprecated
	 */
	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @deprecated
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeLong(modified);
		out.writeObject(attributes);
	}

	/**
	 * @deprecated
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		modified = in.readLong();
		attributes = (HashMap )in.readObject();

		transferable = new MapLinkProtoElement_Transferable();
	}


	public void setBindingDimension(IntDimension bindingDimension)
	{
		this.bindingDimension = bindingDimension;
	}


	public IntDimension getBindingDimension()
	{
		return bindingDimension;
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * Установить толщину линии
	 * @deprecated
	 */
	public void setLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"thickness");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"thickness");
			attributes.put("thickness", ea);
		}
		ea.value = String.valueOf(size);
	}

	/**
	 * Получить толщину линии
	 * @deprecated
	 */
	public int getLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("thickness");
		if(ea == null)
			return MapPropertiesManager.getThickness();
		return Integer.parseInt(ea.value);
	}

	/**
	 * Установить вид линии
	 * @deprecated
	 */
	public void setStyle (String style)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"style");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					style,
					"style");
			attributes.put("style", ea);
		}
		ea.value = style;
	}

	/**
	 * Получить вид линии
	 * @deprecated
	 */
	public String getStyle ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStyle();
		return ea.value;
	}

	/**
	 * Получить стиль линии
	 * @deprecated
	 */
	public Stroke getStroke ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("style");
		if(ea == null)
			return MapPropertiesManager.getStroke();

		return LineComboBox.getStrokeByType(ea.value);

	}

	/**
	 * Установить цвет
	 * @deprecated
	 */
	public void setColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"color");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"color");
			attributes.put("color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
	}

	/**
	 * Получить цвет
	 * @deprecated
	 */
	public Color getColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("color");
		if(ea == null)
			return MapPropertiesManager.getColor();
		return new Color(Integer.parseInt(ea.value));
	}

	/**
	 * установить цвет при наличии сигнала тревоги
	 * @deprecated
	 */
	public void setAlarmedColor (Color color)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ,
					"alarmed_color");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(color.getRGB()),
					"alarmed_color");
			attributes.put("alarmed_color", ea);
		}
		ea.value = String.valueOf(color.getRGB());
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 * @deprecated
	 */
	public Color getAlarmedColor()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_color");
		if(ea == null)
			return MapPropertiesManager.getAlarmedColor();
		return new Color(Integer.parseInt(ea.value));
	}

	/**
	 * установить толщину линии при наличи сигнала тревоги
	 * @deprecated
	 */
	public void setAlarmedLineSize (int size)
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
		{
			ElementAttributeType eat = (ElementAttributeType )Pool.get(
					ElementAttributeType.typ, 
					"alarmed_thickness");
			if(eat == null)
				return;
			ea = new ElementAttribute(
					"attr" + System.currentTimeMillis(),
					eat.getName(),
					String.valueOf(size),
					"alarmed_thickness");
			attributes.put("alarmed_thickness", ea);
		}
		ea.value = String.valueOf(size);
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 * @deprecated
	 */
	public int getAlarmedLineSize ()
	{
		ElementAttribute ea = (ElementAttribute )attributes.get("alarmed_thickness");
		if(ea == null)
			return MapPropertiesManager.getAlarmedThickness();
		return Integer.parseInt(ea.value);
	}

}

