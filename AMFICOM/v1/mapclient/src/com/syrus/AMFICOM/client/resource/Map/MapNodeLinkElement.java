/**
 * $Id: MapNodeLinkElement.java,v 1.25 2004/12/23 16:35:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;

/**
 * элемнт карты - фрагмент линии
 * 
 * 
 * 
 * @version $Revision: 1.25 $, $Date: 2004/12/23 16:35:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public final class MapNodeLinkElement extends NodeLink
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";	
	public static final String COLUMN_START_NODE_ID = "start_node_id";	
	public static final String COLUMN_END_NODE_ID = "end_node_id";	

	public static String[][] exportColumns = null;

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapNodeLinkElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("nodelink"));

		selected = false;
	}

	public MapNodeLinkElement (
			AbstractNode stNode, 
			AbstractNode eNode, 
			Map map)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("nodelink"));
		
		setMap(map);

		this.setName(id.toString());
		setStartNode(stNode);
		setEndNode(eNode);

		selected = false;
	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
/*	
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
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[3][1] = getPhysicalLink().getId().toString();
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

}
