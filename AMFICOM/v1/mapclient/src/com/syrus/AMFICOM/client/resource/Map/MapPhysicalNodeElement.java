/**
 * $Id: MapPhysicalNodeElement.java,v 1.20 2004/12/23 16:35:17 krupenn Exp $
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

/**
 * топологический узел 
 * 
 * 
 * 
 * @version $Revision: 1.20 $, $Date: 2004/12/23 16:35:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @deprecated
 */
public final class MapPhysicalNodeElement extends TopologicalNode
{
	private static final long serialVersionUID = 02L;

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_PHYSICAL_LINK_ID = "physical_link_id";
	public static final String COLUMN_X = "x";
	public static final String COLUMN_Y = "y";
	public static final String COLUMN_ACTIVE = "active";


	protected static String[][] exportColumns = null;


	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public MapPhysicalNodeElement()
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("topologicalnode"));
//		setIconName(CLOSED_NODE);

		selected = false;
	}

	public MapPhysicalNodeElement (
			PhysicalLink physicalLink, 
			DoublePoint location,
            Map map)
		throws ObjectNotFoundException, RetrieveObjectException
	{
		super(new Identifier("topologicalnode"));
		this.setMap(map);
		this.setName(getId().toString());
		setLocation(location);
//		setIconName(CLOSED_NODE);
		setPhysicalLink(physicalLink);

		selected = false;

	}

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
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
/*
	public void setIconName(String iconName)
	{
		try 
		{
			StringFieldCondition condition = new StringFieldCondition(
					String.valueOf(ImageResourceSort._BYTES),
					ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE,
					StringFieldSort.STRINGSORT_INTEGER);
			List bitMaps = ResourceStorableObjectPool.getStorableObjectsByCondition(
					condition, 
					true);

			for (Iterator it = bitMaps.iterator(); it.hasNext(); ) 
			{
				BitmapImageResource ir = (BitmapImageResource )it.next();
				ImageIcon icon = new ImageIcon(ir.getImage().getScaledInstance(
						30, 
						30, 
						Image.SCALE_SMOOTH));
				ImagesPanelLabel ipl = new ImagesPanelLabel(disp, icon, ir);
				imagesPanel.add(ipl);
			}
		}
		catch (ApplicationException ex) 
		{
			ex.printStackTrace();
		}
	}
*/	
}
