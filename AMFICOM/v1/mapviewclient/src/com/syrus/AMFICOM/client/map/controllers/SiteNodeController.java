/**
 * $Id: SiteNodeController.java,v 1.2 2005/01/14 15:03:13 krupenn Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.SiteNode;

import com.syrus.AMFICOM.map.SiteNodeType;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * элемент карты - узел 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/14 15:03:13 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class SiteNodeController extends AbstractNodeController
{
	private static SiteNodeController instance = null;
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapSitePane";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	protected SiteNodeController()
	{
	}
	
	public static MapElementController getInstance()
	{
		if(instance == null)
			instance = new SiteNodeController();
		return instance;
	}

	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!(me instanceof SiteNode))
			return;
		SiteNode site = (SiteNode)me;

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
