/**
 * $Id: MapUnboundLinkElement.java,v 1.3 2004/09/18 14:12:04 krupenn Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/09/18 14:12:04 $
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

	boolean isSelectionVisible()
	{
//		return isSelected() || cablePath.isSelected();
		return isSelected() || cablePath.isSelectionVisible();
	}

	/**
	 * Рисуем NodeLink взависимости от того выбрана она или нет
	 * а так же если она выбрана выводим её рамер
	 */
	public void paint(Graphics g)
	{
//		super.paint(g);

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				MapPropertiesManager.getUnboundThickness(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		paint(g, str, MapPropertiesManager.getUnboundLinkColor(), isSelectionVisible());
//		paint(g, str, MapPropertiesManager.getUnboundLinkColor(), isSelected() || cablePath.isSelected());
	}
}
