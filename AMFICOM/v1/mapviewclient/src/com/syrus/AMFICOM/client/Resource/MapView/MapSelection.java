/**
 * $Id: MapSelection.java,v 1.12 2004/12/08 16:20:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * набор выбранных элементов 
 * 
 * 
 * 
 * @version $Revision: 1.12 $, $Date: 2004/12/08 16:20:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @todo copy/paste, properties
 */
public final class MapSelection extends StubResource
    implements MapElement 
{
	protected List elements = new LinkedList();

	protected DoublePoint location = new DoublePoint(0, 0);
	
	protected Map map;
	
	protected LogicalNetLayer lnl;

	private boolean physicalNodeSelection = true;
	private boolean unboundNodeSelection = true;
	private boolean unboundLinkSelection = true;
	private boolean unboundCableSelection = true;
	private boolean unboundSelection = true;
	private boolean physicalLinkSelection = true;

	public String[][] getExportColumns()
	{
		return null;
	}

	public void setColumn(String field, String value)
	{
	}

	public MapSelection(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
		this.map = lnl.getMapView().getMap();
	}

	public boolean isRemoved()
	{
		return false;
	}
	
	public void setRemoved(boolean removed)
	{
	}

	public void clear()
	{
		elements.clear();
		recalcLocation();
		recalcType();
	}

	public void add(MapElement me)
	{
		elements.add(me);
		recalcLocation();
		recalcType();
	}

	public void addAll(Collection coll)
	{
		elements.addAll(coll);
		recalcLocation();
		recalcType();
	}

	public void remove(MapElement me)
	{
		elements.remove(me);
		recalcLocation();
		recalcType();
	}

	public DoublePoint getLocation()
	{
		return location;
	}

	protected void recalcLocation()
	{
		MapElement me;
		location.x = 0.0D;
		location.y = 0.0D;
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			DoublePoint pt = me.getLocation();

			location.x += pt.x;
			location.y += pt.y;
		}
		location.x /= elements.size();
		location.y /= elements.size();
	}

	private void recalcType()
	{
		physicalNodeSelection = true;
		unboundNodeSelection = true;
		unboundLinkSelection = true;
		unboundCableSelection = true;
		unboundSelection = true;
		physicalLinkSelection = true;

		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(! (me instanceof MapPhysicalNodeElement))
				physicalNodeSelection = false;
			if(! (me instanceof MapUnboundNodeElement))
				unboundNodeSelection = false;
			if(! (me instanceof MapUnboundLinkElement))
				unboundLinkSelection = false;
			if(! (me instanceof MapCablePathElement))
				unboundCableSelection = false;
			if(	! (me instanceof MapUnboundNodeElement) &&
				! (me instanceof MapUnboundLinkElement) &&
				! (me instanceof MapCablePathElement))
				unboundSelection = false;
			if(! (me instanceof MapPhysicalLinkElement))
				physicalLinkSelection = false;
		}
	}

	public void setLocation(DoublePoint aLocation)
	{
	}

	public boolean isSelected()
	{
		throw new UnsupportedOperationException();
	}

	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void setSelected(boolean selected)
	{
	}

	public String getId()
	{
		return "selection";
	}

	public void setId(String id)
	{
		throw new UnsupportedOperationException();
	}

	public String getName()
	{
		return "selection";
	}

	public void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public String getToolTipText()
	{
		return getName();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		throw new UnsupportedOperationException();
	}
	
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}
	
	public List getElements()
	{
		return elements;
	}


	public boolean isPhysicalNodeSelection()
	{
		return physicalNodeSelection;
	}


	public boolean isUnboundNodeSelection()
	{
		return unboundNodeSelection;
	}


	public boolean isUnboundLinkSelection()
	{
		return unboundLinkSelection;
	}


	public boolean isUnboundSelection()
	{
		return unboundSelection;
	}


	public boolean isPhysicalLinkSelection()
	{
		return physicalLinkSelection;
	}


	public boolean isUnboundCableSelection()
	{
		return unboundCableSelection;
	}
}
