/**
 * $Id: MapUnboundLinkElement.java,v 1.7 2004/10/19 11:48:28 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.io.Serializable;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/10/19 11:48:28 $
 * @module
 * @author $Author: krupenn $
 * @see
 */

//MapPhysicalPathElement
public class MapUnboundLinkElement extends MapPhysicalLinkElement implements Serializable
{
	protected MapCablePathElement cablePath;
	
	public MapUnboundLinkElement(
			String id,
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			Map map,
			MapLinkProtoElement proto)
	{
		super(id, stNode, eNode, map, proto);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public boolean isSelectionVisible()
	{
		return isSelected() || cablePath.isSelectionVisible();
	}

	/**
	 * Рисуем NodeLink взависимости от того выбрана она или нет
	 * а так же если она выбрана выводим её рамер
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				MapPropertiesManager.getUnboundThickness(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		paint(g, visibleBounds, str, MapPropertiesManager.getUnboundLinkColor(), false);
	}


	public void setCablePath(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}


	public MapCablePathElement getCablePath()
	{
		return cablePath;
	}
}
