/**
 * $Id: MapSelection.java,v 1.1 2004/09/13 12:02:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * набор выбранных элементов 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:02:01 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapSelection
    implements MapElement 
{
	protected ArrayList elements = new ArrayList();

	protected Point2D.Double anchor = new Point2D.Double(0, 0);
	
	protected Map map;

	protected boolean removed = false;

	public boolean isRemoved()
	{
		return removed;
	}
	
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public void add(MapElement me)
	{
		elements.add(me);
		recalcAnchor();
	}
	
	public void remove(MapElement me)
	{
		elements.remove(me);
		recalcAnchor();
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

	public void paint (Graphics g)
	{
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
}
