/**
 * $Id: MapPipePathElement.java,v 1.13 2004/12/22 16:17:38 krupenn Exp $
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
import com.syrus.AMFICOM.map.Collector;

/**
 * коллектор. 
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/12/22 16:17:38 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapPipePathElement extends Collector
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_LINKS = "links";	

	public static String[][] exportColumns = null;

	public MapPipePathElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("collector"));
	}

	public MapPipePathElement(
			String name,
			Map map)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("collector"));
		this.setName(name);
		this.setMap(map);
	}
	
/*
	public String[][] getExportColumns()
	{
		if(exportColumns == null)
		{
			exportColumns = new String[4][2];
			exportColumns[0][0] = COLUMN_ID;
			exportColumns[1][0] = COLUMN_NAME;
			exportColumns[2][0] = COLUMN_DESCRIPTION;
			exportColumns[3][0] = COLUMN_LINKS;
		}
		exportColumns[0][1] = getId().toString();
		exportColumns[1][1] = getName();
		exportColumns[2][1] = getDescription();
		exportColumns[3][1] = "";
		for(Iterator it = getPhysicalLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement mple = (MapPhysicalLinkElement )it.next();
			exportColumns[3][1] += mple.getId() + " ";
		}

		return exportColumns;
	}
	
	public void setColumn(String field, String value)
	{
		if(field.equals(COLUMN_ID))
			setId(new Identifier(value));
		else
		if(field.equals(COLUMN_NAME))
			setName(value);
		else
		if(field.equals(COLUMN_DESCRIPTION))
			setDescription(value);
		else
		if(field.equals(COLUMN_LINKS))
		{
			physicalLinkIds.clear();
			for(Iterator it = ResourceUtil.parseStrings(value).iterator(); it.hasNext();)
				physicalLinkIds.add(it.next());
		}
	}
*/	

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