/**
 * $Id: Selection.java,v 1.3 2005/01/20 14:37:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.mapview;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.TopologicalNode;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;

/**
 * набор выбранных элементов 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/01/20 14:37:52 $
 * @module
 * @author $Author: krupenn $
 * @see
 * @todo copy/paste, properties
 */
public final class Selection
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

	public Selection(LogicalNetLayer lnl)
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

	public void setAlarmState(boolean alarmState)
	{
		throw new UnsupportedOperationException();
	}

	public boolean getAlarmState()
	{
		throw new UnsupportedOperationException();
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
		double x = 0.0D;
		double y = 0.0D;
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			me = (MapElement)it.next();
			DoublePoint pt = me.getLocation();

			x += pt.getX();
			y += pt.getY();
		}
		x /= elements.size();
		y /= elements.size();
		
		location.setLocation(x, y);
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
			MapElement me = (MapElement)it.next();
			if(! (me instanceof TopologicalNode))
				physicalNodeSelection = false;
			if(! (me instanceof UnboundNode))
				unboundNodeSelection = false;
			if(! (me instanceof UnboundLink))
				unboundLinkSelection = false;
			if(! (me instanceof CablePath))
				unboundCableSelection = false;
			if(	! (me instanceof UnboundNode) &&
				! (me instanceof UnboundLink) &&
				! (me instanceof CablePath))
				unboundSelection = false;
			if(! (me instanceof PhysicalLink))
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

	public Identifier getId()
	{
		throw new UnsupportedOperationException();
	}

	public void setId(Identifier id)
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

	public List getCharacteristics() 
	{
		throw new UnsupportedOperationException();
	}

	public void addCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	public void removeCharacteristic(Characteristic ch)
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

	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
