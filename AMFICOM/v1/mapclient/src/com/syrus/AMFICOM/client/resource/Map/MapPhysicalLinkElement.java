/**
 * $Id: MapPhysicalLinkElement.java,v 1.34 2005/01/14 10:27:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;


/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.34 $, $Date: 2005/01/14 10:27:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public abstract class MapPhysicalLinkElement 
{

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
