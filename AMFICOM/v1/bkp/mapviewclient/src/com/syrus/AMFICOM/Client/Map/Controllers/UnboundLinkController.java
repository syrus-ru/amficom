/**
 * $Id: UnboundLinkController.java,v 1.2 2005/01/31 12:19:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.map.MapElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

/**
 * Элемент непривязанной линии (участка непривязанного кабеля). 
 * @author $Author: krupenn $
 * @version $Revision: 1.2 $, $Date: 2005/01/31 12:19:18 $
 * @module mapviewclient_v1
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

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelectionVisible(MapElement me)
	{
		if(! (me instanceof UnboundLink))
			return false;

		UnboundLink link = (UnboundLink)me;
		
		CableController cc = (CableController)getLogicalNetLayer().getMapViewController().getController(link.getCablePath());

		return link.isSelected() || cc.isSelectionVisible(link.getCablePath());
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(! (me instanceof UnboundLink))
			return;

		UnboundLink link = (UnboundLink)me;
		
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
