/**
 * $Id: MapPipePathElement.java,v 1.15 2005/01/14 10:27:00 krupenn Exp $
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
 * коллектор. 
 * 
 * 
 * 
 * @version $Revision: 1.15 $, $Date: 2005/01/14 10:27:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public abstract class MapPipePathElement 
{

/*	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		String clonedId = (String)Pool.get(MapPropertiesManager.MAP_CLONED_IDS, id);
		if (clonedId != null)
			return Pool.get(MapPipePathElement.typ, clonedId);

		MapPipePathElement mppe = (MapPipePathElement )super.clone();
		mppe.setId(dataSource.GetUId(MapPipePathElement.typ));
		mppe.setStartNode((MapNodeElement )getStartNode().clone(dataSource));
		mppe.setEndNode((MapNodeElement )getEndNode().clone(dataSource));
		mppe.setMap((Map )getMap().clone(dataSource));
				
		mppe.description = description;
		mppe.name = name;
		mppe.selected = selected;

		Pool.put(MapPipePathElement.typ, mppe.getId(), mppe);
		Pool.put(MapPropertiesManager.MAP_CLONED_IDS, id, mppe.getId());

		mppe.physicalLinkIds = new ArrayList(physicalLinkIds.size());
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			mppe.physicalLinkIds.add(Pool.get(MapPropertiesManager.MAP_CLONED_IDS, mple.getId()));
		}

		mppe.attributes = new HashMap();
		for(Iterator it = attributes.values().iterator(); it.hasNext();)
		{
			ElementAttribute ea = (ElementAttribute )it.next();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mppe.attributes.put(ea2.type_id, ea2);
		}

		return mppe;
	}
*/
}