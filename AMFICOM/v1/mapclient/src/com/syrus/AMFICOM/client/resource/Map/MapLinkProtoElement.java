/**
 * $Id: MapLinkProtoElement.java,v 1.11 2004/11/19 14:40:10 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * тип физической линии 
 * 
 * 
 * 
 * @version $Revision: 1.11 $, $Date: 2004/11/19 14:40:10 $
 * @module
 * @author $Author: krupenn $
 * @see
 */	
public final class MapLinkProtoElement 
		extends MapLinkElement
		implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "maplinkproto";

	public static final String TUNNEL = "tunnel";
	public static final String COLLECTIOR = "collector";
	public static final String UNBOUND = "cable";

	protected MapLinkProtoElement_Transferable transferable;

	protected long modified;
	
	/**
	 * Размерность тоннеля.
	 * Для тоннеля обозначает размерность матрицы труб в разрезе,
	 * для участка коллектора - число полок и мест на полках
	 */
	protected Dimension bindingDimension;

	/** имя класса панели свойств объекта */
	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapLinkProtoElement(
			String id,
			String name,
			String description,
			Dimension bindingDimension)
	{
		super();
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setBindingDimension(bindingDimension);
	}
	
	public MapLinkProtoElement(MapLinkProtoElement_Transferable transferable)
	{
		super();
		this.transferable = transferable;
		this.setLocalFromTransferable();
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.modified = transferable.modified;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

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

	public void updateLocalFromTransferable()
	{
	}
	
	public Object getTransferable()
	{
		return transferable;
	}
	
	public String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		throw new UnsupportedOperationException();
	}
	
	public long getModified()
	{
		return modified;
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		throw new UnsupportedOperationException();
	}

	public String getToolTipText()
	{
		return "proto " + this.getName();
	}

	public String getName()
	{
		return name;
	}

	public Point2D.Double getAnchor()
	{
		throw new UnsupportedOperationException();
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeLong(modified);
		out.writeObject(attributes);
	}

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


	public void setBindingDimension(Dimension bindingDimension)
	{
		this.bindingDimension = bindingDimension;
	}


	public Dimension getBindingDimension()
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

}

