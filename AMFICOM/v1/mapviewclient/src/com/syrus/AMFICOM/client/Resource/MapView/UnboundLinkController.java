/**
 * $Id: UnboundLinkController.java,v 1.3 2004/12/23 16:58:00 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElementController;
import com.syrus.AMFICOM.Client.Resource.Map.PhysicalLinkController;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * линейный элемента карты 
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2004/12/23 16:58:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class UnboundLinkController extends PhysicalLinkController
{
	private static UnboundLinkController instance = null;
	
	private UnboundLinkController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new UnboundLinkController();
		return instance;
	}

	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof MapUnboundLinkElement))
			return false;

		MapUnboundLinkElement link = (MapUnboundLinkElement )me;
		
		CableController cc = (CableController )getLogicalNetLayer().getMapViewController().getController(link.getCablePath());

		return link.isSelected() || cc.isSelectionVisible(link.getCablePath());
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof MapUnboundLinkElement))
			return;

		MapUnboundLinkElement link = (MapUnboundLinkElement )me;
		
		if(!isElementVisible(link, visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )getStroke(link);
		Stroke str = new BasicStroke(
				MapPropertiesManager.getUnboundThickness(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		paint(link, g, visibleBounds, str, MapPropertiesManager.getUnboundLinkColor(), false);
	}
}
