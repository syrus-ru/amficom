/**
 * $Id: MapUnboundLinkElement.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

import java.io.Serializable;

import java.util.Iterator;

/**
 * элемент линии 
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
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

	public ObjectResourceModel getModel()
	{
		return null;//new MapPhysicalLinkElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapPhysicalLinkElementDisplayModel();
	}
	
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapLinkPane();
	}

	/**
	 * Рисуем NodeLink взависимости от того выбрана она или нет
	 * а так же если она выбрана выводим её рамер
	 */
	public void paint (Graphics g)
	{
		super.paint(g);

		MapCoordinatesConverter converter = getMap().getConverter();

		Graphics2D p = (Graphics2D )g;
		
		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				MapPropertiesManager.getUnboundThickness(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		p.setStroke(str );
		p.setColor(MapPropertiesManager.getUnboundLinkColor());

		for(Iterator it = getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement nodelink = (MapNodeLinkElement )it.next();

			Point from = converter.convertMapToScreen( nodelink.getStartNode().getAnchor());
			Point to = converter.convertMapToScreen( nodelink.getEndNode().getAnchor());

			p.drawLine( from.x, from.y, to.x, to.y);
			if ( isSelected())
			{
				p.setStroke(MapPropertiesManager.getSelectionStroke());

				double l = 4;
				double l1 = 6;
				double cos_a = (from.y - to.y) 
					/ Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				double sin_a = (from.x - to.x) 
					/ Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				p.setColor(MapPropertiesManager.getFirstSelectionColor());
				p.drawLine(
						from.x + (int)(l * cos_a), 
						from.y  - (int)(l * sin_a), 
						to.x + (int)(l * cos_a), 
						to.y - (int)(l * sin_a));
				p.drawLine(
						from.x - (int)(l * cos_a), 
						from.y  + (int)(l * sin_a), 
						to.x - (int)(l * cos_a), 
						to.y + (int)(l * sin_a));

				p.setColor(MapPropertiesManager.getSecondSelectionColor());
				p.drawLine(
						from.x + (int)(l1 * cos_a), 
						from.y  - (int)(l1 * sin_a), 
						to.x + (int)(l1 * cos_a), 
						to.y - (int)(l1 * sin_a));
				p.drawLine(
						from.x - (int)(l1 * cos_a), 
						from.y  + (int)(l1 * sin_a), 
						to.x - (int)(l1 * cos_a), 
						to.y + (int)(l1 * sin_a));
			}
		}
	}

}
