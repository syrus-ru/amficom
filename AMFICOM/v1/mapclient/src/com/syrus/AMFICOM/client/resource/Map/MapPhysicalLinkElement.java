/**
 * $Id: MapPhysicalLinkElement.java,v 1.32 2004/12/22 16:17:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.PhysicalLinkType;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.32 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPhysicalLinkElement extends PhysicalLink
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PROTO_ID = "proto_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	
	public static final String COLUMN_NODE_LINKS = "node_links";	
	public static final String COLUMN_CITY = "city";	
	public static final String COLUMN_STREET = "street";	
	public static final String COLUMN_BUILDING = "building";	

	public static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapLinkPane";

	public MapPhysicalLinkElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("link"));
		selected = false;
	}

	public MapPhysicalLinkElement (
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map,
			PhysicalLinkType type)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("link"));
		this.setMap(map);
		this.setName(id.toString());
		setStartNode(stNode);
		setEndNode(eNode);
		selected = false;
		setType(type);
		
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * @deprecated
	 */
	protected boolean selectionVisible = false;

/*
	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[10][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_PROTO_ID;
			exportColumns[4][0] = COLUMN_START_NODE_ID;
			exportColumns[5][0] = COLUMN_END_NODE_ID;
			exportColumns[6][0] = COLUMN_NODE_LINKS;
			exportColumns[7][0] = COLUMN_CITY;
			exportColumns[8][0] = COLUMN_STREET;
			exportColumns[9][0] = COLUMN_BUILDING;
		}
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = getProto().getId().toString();
		exportColumns[4][1] = getStartNode().getId().toString();
		exportColumns[5][1] = getEndNode().getId().toString();
		exportColumns[6][1] = "";
		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			exportColumns[6][1] += mnle.getId().toString() + " ";
		}
		exportColumns[7][1] = getCity();
		exportColumns[8][1] = getStreet();
		exportColumns[9][1] = getBuilding();
		
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
		if(field.equals(COLUMN_PROTO_ID))
			setMapProtoId(value);
		else
		if(field.equals(COLUMN_START_NODE_ID))
			startNodeId = value;
		else
		if(field.equals(COLUMN_END_NODE_ID))
			endNodeId = value;
		else
		if(field.equals(COLUMN_NODE_LINKS))
		{
			nodeLinkIds.clear();
			for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
				nodeLinkIds.add(it.next());
		}
		else
		if(field.equals(COLUMN_CITY))
			setCity(value);
		else
		if(field.equals(COLUMN_STREET))
			setStreet(value);
		else
		if(field.equals(COLUMN_BUILDING))
			setBuilding(value);
	}
*/
/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapPhysicalLinkElement.typ, clonedId);

		MapPhysicalLinkElement mple = new MapPhysicalLinkElement(
				dataSource.GetUId(MapPhysicalLinkElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource),
				getProto());
				
		mple.changed = changed;
		mple.description = description;
		mple.name = name;
		mple.selected = selected;
		mple.mapProtoId = mapProtoId;

		Pool.put(MapPhysicalLinkElement.typ, mple.getId(), mple);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mple.getId());

		mple.nodeLinkIds = new ArrayList(nodeLinks.size());
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			mple.nodeLinkIds.add(Pool.get(MapPropertiesManager.MAP_CLONED_IDS, mnle.getId()));
		}

		mple.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mple.attributes.put(ea2.type_id, ea2);
		}

		return mple;
	}
*/

}
