/**
 * $Id: MapNodeLinkElement.java,v 1.26 2005/01/14 10:27:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.Map;


/**
 * элемнт карты - фрагмент линии
 * 
 * 
 * 
 * @version $Revision: 1.26 $, $Date: 2005/01/14 10:27:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public abstract class MapNodeLinkElement
{

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
