/**
 * $Id: MapSelection.java,v 1.3 2004/09/27 07:41:34 krupenn Exp $
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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * набор выбранных элементов 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/27 07:41:34 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapSelection
    implements MapElement 
{
	protected List elements = new LinkedList();

	protected Point2D.Double anchor = new Point2D.Double(0, 0);
	
	protected Map map;
	
	protected LogicalNetLayer lnl;

	private boolean physicalNodeSelection = true;
	private boolean unboundNodeSelection = true;
	private boolean unboundLinkSelection = true;
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

		for(Iterator it = lnl.getSelectedElements().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isSelected())
				elements.add(me);
		}

		recalcAnchor();
		recalcType();
	}

	public boolean isRemoved()
	{
		return false;
	}
	
	public void setRemoved(boolean removed)
	{
	}

	public void add(MapElement me)
	{
		elements.add(me);
		recalcAnchor();
		recalcType();
	}
	
	public void remove(MapElement me)
	{
		elements.remove(me);
		recalcAnchor();
		recalcType();
	}

	public Point2D.Double getAnchor()
	{
		return anchor;
	}

	protected void recalcAnchor()
	{
		MapElement me;
		anchor = new Point2D.Double(0.0, 0.0);
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			me = (MapElement )it.next();
			Point2D.Double pt = (Point2D.Double )me.getAnchor();

			anchor.x += pt.x;
			anchor.y += pt.y;
		}
		anchor.x /= elements.size();
		anchor.y /= elements.size();
	}

	private void recalcType()
	{
		physicalNodeSelection = true;
		unboundNodeSelection = true;
		unboundLinkSelection = true;
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
			if(	! (me instanceof MapUnboundNodeElement) &&
				! (me instanceof MapUnboundLinkElement))
				unboundSelection = false;
			if(! (me instanceof MapPhysicalLinkElement))
				physicalLinkSelection = false;
		}
	}

	public void setAnchor(Point2D.Double aAnchor)
	{
	}

	public boolean isSelected()
	{
		return true;
	}

	public MapElementState getState()
	{
		return null;
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
	}

	public String getName()
	{
		return "selection";
	}

	public void setName(String name)
	{
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		throw new UnsupportedOperationException();
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
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

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		return true;
	}

	public boolean isMovable()
	{
		return true;
	}
	
	public String getPropertyPaneClassName()
	{
		return "";
	}

	public String getToolTipText()
	{
		return getName();
	}

	public Object clone(DataSourceInterface dataSource)
	{
		return null;
	}
	
	public void revert(MapElementState state)
	{
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
}
