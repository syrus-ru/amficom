/**
 * $Id: MapPhysicalNodeElement.java,v 1.18 2004/12/08 16:20:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * топологический узел 
 * 
 * 
 * 
 * @version $Revision: 1.18 $, $Date: 2004/12/08 16:20:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapPhysicalNodeElement extends MapNodeElement implements Serializable
{
	private static final long serialVersionUID = 02L;

	/**
	 * @deprecated
	 */
	public static final String typ = "mapnodeelement";

	/**
	 * @deprecated
	 */
	protected MapPhysicalNodeElement_Transferable transferable;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_ACTIVE = "active";

	/**
	 * @deprecated
	 */
	public static final String CLOSED_NODE = "node";
	/**
	 * @deprecated
	 */
	public static final String OPEN_NODE = "void";

	/**
	 * physical node can be bound to site only if it is part of an unbound link
	 */
	private boolean canBind = false;

	protected static String[][] exportColumns = null;

	/**
	 * Флаг показывающий закрыт ли узел
	 * true значит что из узла выходит две линии, false одна
	 */
	protected boolean active = false;

	protected String physicalLinkId = "";

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapPhysicalNodeElement()
	{
		setImageId(CLOSED_NODE);
		attributes = new HashMap();

//		setBounds(bounds);
		selected = false;

		transferable = new MapPhysicalNodeElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapPhysicalNodeElement(MapPhysicalNodeElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapPhysicalNodeElement (
			String id, 
			String physicalLinkId, 
			DoublePoint location,
            Map map)
	{
		this.map = map;
		this.setId(id);
		this.setName(id);
		setLocation(location);
		this.mapId = map.getId();
		setImageId(CLOSED_NODE);
		this.physicalLinkId = physicalLinkId;
		attributes = new HashMap();

		selected = false;

		transferable = new MapPhysicalNodeElement_Transferable();
	}

	/**
	 * @deprecated
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapPhysicalNodeElement.typ, clonedId);

		MapPhysicalNodeElement mpne = new MapPhysicalNodeElement(
				dataSource.GetUId(MapPhysicalNodeElement.typ),
				(String )Pool.get(MapPropertiesManager.MAP_CLONED_IDS, physicalLinkId),
				new Point2D.Double(anchor.x, anchor.y),
				(Map)map.clone(dataSource)); 
				
		mpne.active = active;
		mpne.alarmState = alarmState;
		mpne.changed = changed;
		mpne.description = description;
		mpne.name = name;
		mpne.optimizerAttribute = optimizerAttribute;
		mpne.scaleCoefficient = scaleCoefficient;
		mpne.selected = selected;

		Pool.put(MapPhysicalNodeElement.typ, mpne.getId(), mpne);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mpne.getId());

		mpne.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mpne.attributes.put(ea2.type_id, ea2);
		}

		return mpne;
	}
*/
	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.description = transferable.description;
//		this.anchor.x = Double.parseDouble(transferable.longitude);
//		this.anchor.y = Double.parseDouble(transferable.latitude);
		this.mapId = transferable.mapId;
		this.active = transferable.active;
		this.physicalLinkId = transferable.physicalLinkId;
		
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
//		transferable.longitude = String.valueOf(this.anchor.x);
//		transferable.latitude = String.valueOf(this.anchor.y);
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

	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	/**
	 * Используется для для загрузки класса из базы данных
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		this.map = (Map)Pool.get(Map.typ, this.mapId);
	}

	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return transferable;
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	/**
	 * установить активность топологического узла.
	 * узел активен, если он находится в середине связи, и не активен, если
	 * он находится на конце связи. активные и неактивные топологические узлы
	 * отображаются разными иконками
	 */
	public void setActive(boolean active)
	{
		if(active)
			setImageId(CLOSED_NODE);
		else
			setImageId(OPEN_NODE);
//		setScaleCoefficient(this.scaleCoefficient);
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
		exportColumns[4][1] = String.valueOf(getLocation().x);
		exportColumns[5][1] = String.valueOf(getLocation().y);
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
			location.x = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_Y))
			location.y = Double.parseDouble(value);
		else
		if(field.equals(COLUMN_ACTIVE))
			setActive(Boolean.valueOf(value).booleanValue());
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);
		out.writeDouble(location.x);
		out.writeDouble(location.y);
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
		location = new DoublePoint( );
		location.x = in.readDouble();
		location.y = in.readDouble();
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
