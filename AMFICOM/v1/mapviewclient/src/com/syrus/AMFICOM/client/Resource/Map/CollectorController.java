/**
 * $Id: CollectorController.java,v 1.4 2004/12/23 16:57:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.map.Collector;

import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/12/23 16:57:59 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class CollectorController extends AbstractLinkController
{
	private static CollectorController instance = null;
	
	private CollectorController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new CollectorController();
		return instance;
	}

	public String getToolTipText(MapElement me)
	{
		if(! (me instanceof Collector))
			return null;

		Collector collector = (Collector )me;
		
		return collector.getName();
	}

	public boolean isSelectionVisible(MapElement me)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof Collector))
			return false;

		Collector collector = (Collector )me;

		
		boolean vis = false;
		for(Iterator it = collector.getPhysicalLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			if(plc.isElementVisible(link, visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof Collector))
			return;

		Collector collector = (Collector )me;
		
		if(!isElementVisible(collector, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(collector);
		Stroke str = new BasicStroke(
				getLineSize(collector), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = getColor(collector);

		for(Iterator it = collector.getPhysicalLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink )it.next();
			PhysicalLinkController plc = (PhysicalLinkController )getLogicalNetLayer().getMapViewController().getController(link);
			plc.paint(link, g, visibleBounds, str, color, false);
		}
	}

	/**
	 * точка находится на фрагменте, если она находится в рамках линий выделения
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		if(! (me instanceof Collector))
			return false;

		Collector collector = (Collector )me;
		
		return false;
	}

}
