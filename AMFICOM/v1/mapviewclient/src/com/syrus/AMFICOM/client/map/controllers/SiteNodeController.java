/**
 * $Id: SiteNodeController.java,v 1.3 2005/02/03 16:24:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Контроллер сетевого узла.
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/03 16:24:01 $
 * @module mapviewclient_v1
 */
public class SiteNodeController extends AbstractNodeController
{
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

	/**
	 * Instance
	 */
	private static SiteNodeController instance = null;
	
	/**
	 * Получить имя класса панели, описывающей свойства кабельного пути.
	 * @return имя класса
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * Private constructor.
	 */
	protected SiteNodeController()
	{// empty
	}
	
	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new SiteNodeController();
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	public void paint (MapElement mapElement, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(mapElement instanceof SiteNode))
			return;
		SiteNode site = (SiteNode)mapElement;

		if(!isElementVisible(site, visibleBounds))
			return;
		
		super.paint(site, g, visibleBounds);
		
		if(MapPropertiesManager.isShowNodesNames())
		{
			MapCoordinatesConverter converter = getLogicalNetLayer();
			
			Point p = converter.convertMapToScreen(site.getLocation());
	
			int width = getBounds(site).width;
			int height = getBounds(site).height;

			g.setColor(MapPropertiesManager.getBorderColor());
			g.setFont(MapPropertiesManager.getFont());

			int fontHeight = g.getFontMetrics().getHeight();
			String text = site.getName();
			int textWidth = g.getFontMetrics().stringWidth(text);
			int centerX = p.x + width / 2;
			int centerY = p.y + height;

			g.drawRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextBackground());
			g.fillRect(
					centerX,
					centerY - fontHeight + 2,
					textWidth,
					fontHeight);

			g.setColor(MapPropertiesManager.getTextColor());
			g.drawString(
					text,
					centerX,
					centerY);
		}
	}
}
