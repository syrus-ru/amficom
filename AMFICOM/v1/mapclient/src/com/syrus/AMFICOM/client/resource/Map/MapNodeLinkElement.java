/**
 * $Id: MapNodeLinkElement.java,v 1.23 2004/12/08 16:20:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.CORBA.General.ElementAttribute_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapNodeLinkElement_Transferable;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttribute;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;

/**
 * элемнт карты - фрагмент линии
 * 
 * 
 * 
 * @version $Revision: 1.23 $, $Date: 2004/12/08 16:20:01 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapNodeLinkElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;

	/**
	 * @deprecated
	 */
	public static final String typ = "mapnodelinkelement";

	/**
	 * @deprecated
	 */
	protected MapNodeLinkElement_Transferable transferable;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	

	/** идентификатор линии, в составе которой находится фрагмент */
	protected String physicalLinkId;

	/** топологическая длина */
	protected double lengthLt = 0.0D;

	public static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapNodeLinkElement()
	{
		attributes = new HashMap();

		selected = false;

		transferable = new MapNodeLinkElement_Transferable();
	}

	/**
	 * @deprecated
	 */
	public MapNodeLinkElement(MapNodeLinkElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public MapNodeLinkElement (
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map)
	{
		this.map = map;

		this.id = id;
		this.name = id;
		if(map != null)
			mapId = map.getId();
		startNode = stNode;
		endNode = eNode;
		attributes = new HashMap();

		selected = false;

		transferable = new MapNodeLinkElement_Transferable();
	}

	/**
	 * @deprecated
	 */
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapNodeLinkElement.typ, clonedId);

		MapNodeLinkElement mnle = new MapNodeLinkElement(
				dataSource.GetUId(MapNodeLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map)map.clone(dataSource) );
				
		mnle.changed = changed;
		mnle.description = description;
		mnle.name = name;
		mnle.physicalLinkId = physicalLinkId;
		mnle.selected = selected;

		Pool.put(MapNodeLinkElement.typ, mnle.getId(), mnle);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mnle.getId());

		mnle.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mnle.attributes.put(ea2.type_id, ea2);
		}

		return mnle;
	}
*/
	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		this.id = transferable.id;
		this.name = transferable.name;
		this.mapId = transferable.mapId;
		this.physicalLinkId = transferable.physicalLinkId;
		this.startNodeId = transferable.startNodeId;
		this.endNodeId = transferable.endNodeId;

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
		transferable.mapId = map.id;
		transferable.startNodeId = this.startNode.getId();
		transferable.endNodeId = this.endNode.getId();
		transferable.physicalLinkId = this.physicalLinkId;

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
		this.startNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, endNodeId);

		this.map = (Map )Pool.get(Map.typ, this.mapId);
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
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public void setPhysicalLink(MapPhysicalLinkElement plink)
	{
		setPhysicalLinkId(plink.getId());
	}
	
	public MapPhysicalLinkElement getPhysicalLink()
	{
		return getMap().getPhysicalLink(getPhysicalLinkId());
	}
	
	public boolean getAlarmState()
	{
		return getPhysicalLink().getAlarmState();
	}

	public String getPhysicalLinkId()
	{
		return this.physicalLinkId;
	}
	
	public void setPhysicalLinkId(String physicalLinkId)
	{
		this.physicalLinkId = physicalLinkId;
		if(getStartNode() instanceof MapPhysicalNodeElement)
			((MapPhysicalNodeElement )getStartNode()).setPhysicalLinkId(physicalLinkId);
		if(getEndNode() instanceof MapPhysicalNodeElement)
			((MapPhysicalNodeElement )getEndNode()).setPhysicalLinkId(physicalLinkId);
	}

	/**
	 * Получить топологическую длинну NodeLink
	 */
	public double getLengthLt()
	{
		return lengthLt;
	}

	public DoublePoint getLocation()
	{
		return new DoublePoint(
			(getStartNode().getLocation().getX() + getEndNode().getLocation().getX()) / 2,
			(getStartNode().getLocation().getY() + getEndNode().getLocation().getY()) / 2);
	}
	
	/**
	 * получить текущее состояние
	 */
	public MapElementState getState()
	{
		return new MapNodeLinkElementState(this);
	}

	/**
	 * восстановить состояние
	 */
	public void revert(MapElementState state)
	{
		super.revert(state);
		
		MapNodeLinkElementState mnles = (MapNodeLinkElementState )state;

		setPhysicalLinkId(mnles.physicalLinkId);

//		updateLengthLt();
	}
	
	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[6][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PHYSICAL_LINK_ID;
			exportColumns[4][0] = COLUMN_START_NODE_ID;
			exportColumns[5][0] = COLUMN_END_NODE_ID;
		}
		exportColumns[0][1] = getId();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getPhysicalLinkId();
		exportColumns[4][1] = getStartNode().getId();
		exportColumns[5][1] = getEndNode().getId();
		
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
			setPhysicalLinkId(value);
		else
		if(field.equals(COLUMN_START_NODE_ID))
			startNodeId = value;
		else
		if(field.equals(COLUMN_END_NODE_ID))
			endNodeId = value;
	}

	/**
	 * @deprecated
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(mapId);
		out.writeObject(physicalLinkId);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);
		out.writeObject(attributes);
		
		out.writeDouble(lengthLt);
	}

	/**
	 * @deprecated
	 */
	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		mapId = (String )in.readObject();
		physicalLinkId = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();
		
		lengthLt = in.readDouble();

		transferable = new MapNodeLinkElement_Transferable();

		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}
}
