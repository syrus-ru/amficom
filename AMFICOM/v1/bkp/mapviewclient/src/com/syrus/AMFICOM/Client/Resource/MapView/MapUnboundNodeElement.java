/**
 * $Id: MapUnboundNodeElement.java,v 1.6 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import java.io.Serializable;

/**
 * уэел 
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapUnboundNodeElement extends MapSiteNodeElement implements Serializable
{
	protected SchemeElement schemeElement;
	
	protected boolean canBind = false;

	/**
	 * @deprecated
	 */
	public MapUnboundNodeElement(
		SchemeElement schemeElement,
		String id,
		Point2D.Double anchor,
		Map map,
		MapNodeProtoElement pe)
	{
		super(id, anchor, map, pe);

		setSchemeElement(schemeElement);
	}
	
	public MapUnboundNodeElement(
		SchemeElement schemeElement,
		String id,
		DoublePoint location,
		Map map,
		MapNodeProtoElement pe)
	{
		super(id, location, map, pe);

		setSchemeElement(schemeElement);
	}

	public void setCanBind(boolean canBind)
	{
		this.canBind = canBind;
	}
	
	public boolean getCanBind()
	{
		return this.canBind;
	}

	/**
	 * @deprecated
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		super.paint(g, visibleBounds);

		MapCoordinatesConverter converter = getMap().getConverter();
		
		Point p = converter.convertMapToScreen(getAnchor());

		Graphics2D pg = (Graphics2D )g;
		
		int width = getBounds().width + 20;
		int height = getBounds().height + 20;
		
		pg.setStroke(new BasicStroke(MapPropertiesManager.getUnboundThickness()));
		if (getCanBind())
		{
			pg.setColor(MapPropertiesManager.getCanBindColor());
		}
		else
		{
			pg.setColor(MapPropertiesManager.getUnboundElementColor());
		}
		pg.drawRect( 
				p.x - width / 2,
				p.y - height / 2,
				width,
				height);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}


	public void setSchemeElement(SchemeElement schemeElement)
	{
		this.schemeElement = schemeElement;
		setName(schemeElement.getName());
	}


	public SchemeElement getSchemeElement()
	{
		return schemeElement;
	}

}
