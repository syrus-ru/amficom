/**
 * $Id: MapPhysicalNodeElement.java,v 1.15 2004/10/06 14:10:05 krupenn Exp $
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
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalNodeElement_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

/**
 * топологический узел 
 * 
 * 
 * 
 * @version $Revision: 1.15 $, $Date: 2004/10/06 14:10:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalNodeElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapnodeelement";

	protected MapPhysicalNodeElement_Transferable transferable;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_ACTIVE = "active";

	public static final String CLOSED_NODE = "node";
	public static final String OPEN_NODE = "void";

	public static final String CLOSED_NODE_IMAGE = "images/node.gif";
	public static final String OPEN_NODE_IMAGE = "images/void.gif";

	public final static Rectangle DEFAULT_BOUNDS = new Rectangle(10, 10);
	public final static Rectangle MIN_BOUNDS = new Rectangle(2, 2);
	public final static Rectangle MAX_BOUNDS = new Rectangle(15, 15);

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private boolean canBind = false;

	static
	{
		MapPropertiesManager.setOriginalImage(OPEN_NODE, new ImageIcon(OPEN_NODE_IMAGE).getImage());
		MapPropertiesManager.setOriginalImage(CLOSED_NODE, new ImageIcon(CLOSED_NODE_IMAGE).getImage());
	}

	protected static String[][] exportColumns = null;

	/**
	 * Флаг показывающий закрыт ли узел
	 * true значит что из узла выходит две линии, false одна
	 */
	protected boolean active = false;

	protected String physicalLinkId = "";

	public MapPhysicalNodeElement()
	{
		setImageId(CLOSED_NODE);
		attributes = new HashMap();

		setBounds(bounds);
		selected = false;

		transferable = new MapPhysicalNodeElement_Transferable();
	}

	public MapPhysicalNodeElement(MapPhysicalNodeElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapPhysicalNodeElement (
			String id, 
			String physicalLinkId, 
			Point2D.Double anchor,
            Map map,
            Rectangle bounds)
	{
		this.map = map;
		this.id = id;
		this.name = id;
		setAnchor(anchor);
		this.mapId = map.getId();
		setImageId(CLOSED_NODE);
		this.physicalLinkId = physicalLinkId;
		attributes = new HashMap();

		setBounds(bounds);
		selected = false;

		transferable = new MapPhysicalNodeElement_Transferable();
	}

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get("mapclonedids", id);
		if (clonedId != null)
			return Pool.get(MapPhysicalNodeElement.typ, clonedId);

		MapPhysicalNodeElement mpne = new MapPhysicalNodeElement(
				dataSource.GetUId(MapPhysicalNodeElement.typ),
				(String )Pool.get("mapclonedids", physicalLinkId),
				new Point2D.Double(anchor.x, anchor.y),
				(Map)map.clone(dataSource), 
				bounds);
				
		mpne.active = active;
		mpne.alarmState = alarmState;
		mpne.changed = changed;
		mpne.description = description;
		mpne.name = name;
		mpne.optimizerAttribute = optimizerAttribute;
		mpne.scaleCoefficient = scaleCoefficient;
		mpne.selected = selected;

		Pool.put(MapPhysicalNodeElement.typ, mpne.getId(), mpne);
		Pool.put("mapclonedids", id, mpne.getId());

		mpne.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mpne.attributes.put(ea2.type_id, ea2);
		}

		return mpne;
	}

	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
		this.anchor.x = Double.parseDouble(transferable.longitude);
		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.mapId = transferable.mapId;
		this.active = transferable.active;
		this.physicalLinkId = transferable.physicalLinkId;
		
		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));
	}

	public void setTransferableFromLocal()
	{
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.description = this.description;
		transferable.longitude = String.valueOf(this.anchor.x);
		transferable.latitude = String.valueOf(this.anchor.y);
		transferable.mapId = map.id;
		transferable.physicalLinkId = this.physicalLinkId;
		transferable.active = this.active;

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

	public String getTyp()
	{
		return typ;
	}

	/**
	 * Используется для для загрузки класса из базы данных
	 */
	public void updateLocalFromTransferable()
	{
		this.map = (Map)Pool.get(Map.typ, this.mapId);
	}

	public Object getTransferable()
	{
		return transferable;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public Rectangle getDefaultBounds()
	{
		return DEFAULT_BOUNDS;
	}
	
	public Rectangle getMinBounds()
	{
		return MIN_BOUNDS;
	}
	
	public Rectangle getMaxBounds()
	{
		return MAX_BOUNDS;
	}

	public void setActive(boolean active)
	{
		if(active)
			setImageId(CLOSED_NODE);
		else
			setImageId(OPEN_NODE);
		setScaleCoefficient(this.scaleCoefficient);
		this.active = active;
	}

	public boolean isActive()
	{
		return active;
	}

	public String getPhysicalLinkId()
	{
		return physicalLinkId;
	}
	
	public void setPhysicalLinkId(String pId)
	{
		this.physicalLinkId = pId;
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		super.paint(g, visibleBounds);

		if (isCanBind())
		{
			MapCoordinatesConverter converter = getMap().getConverter();
			
			Point p = converter.convertMapToScreen(getAnchor());
	
			Graphics2D pg = (Graphics2D )g;
			
			int width = getBounds().width + 20;
			int height = getBounds().height + 20;
			
			pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
			pg.setColor(MapPropertiesManager.getCanBindColor());
			pg.drawRect( 
					p.x - width / 2,
					p.y - height / 2,
					width,
					height);
		}
	}

	public MapElementState getState()
	{
		return new MapPhysicalNodeElementState(this);
	}

	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapPhysicalNodeElementState mpnes = (MapPhysicalNodeElementState )state;
		
		setActive(mpnes.active);
		setPhysicalLinkId(mpnes.physicalLinkId);
	}

	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[7][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_X;
			exportColumns[5][0] = COLUMN_Y;
			exportColumns[6][0] = COLUMN_ACTIVE;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = physicalLinkId;
		exportColumns[4][1] = String.valueOf(getAnchor().x);
		exportColumns[5][1] = String.valueOf(getAnchor().y);
		exportColumns[6][1] = String.valueOf(isActive());
		
		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			setId(value);
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
		else
		if(field.equals(COLUMN_PHYSICAL_LINK_ID))
			physicalLinkId = value;
		else
		if(field.equals(COLUMN_X))
			anchor.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			anchor.y = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_ACTIVE))
			setActive(Boolean.valueOf(value).booleanValue());
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeDouble(anchor.x);
		out.writeDouble(anchor.y);
		out.writeObject(mapId);
		out.writeObject(getImageId());
		out.writeBoolean(active);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		anchor = new Point2D.Double( );
		anchor.x = in.readDouble();
		anchor.y = in.readDouble();
		mapId = (String )in.readObject();
		this.setImageId((String )in.readObject());
		active = in.readBoolean();

		attributes = (HashMap )in.readObject();

		transferable = new MapPhysicalNodeElement_Transferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}


	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}


	public boolean isCanBind()
	{
		return canBind;
	}
}
