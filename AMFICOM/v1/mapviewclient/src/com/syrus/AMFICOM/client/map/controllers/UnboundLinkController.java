/**
 * $Id: UnboundLinkController.java,v 1.3 2005/02/03 16:24:01 krupenn Exp $
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
 * Контроллер элемента непривязанной линии (участка непривязанного кабеля). 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public final class UnboundLinkController extends PhysicalLinkController
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	/**
	 * Instace.
	 */
	private static UnboundLinkController instance = null;
	
	/**
	 * Private constructor.
	 */
	private UnboundLinkController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new UnboundLinkController();
		return instance;
	}

	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
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
