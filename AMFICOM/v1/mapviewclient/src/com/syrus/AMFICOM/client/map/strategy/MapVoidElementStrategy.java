/**
 * $Id: MapVoidElementStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.VoidMapElement;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.util.Iterator;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления элементами, когда нет выбранных элементов
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapVoidElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;
	
	Map map;

	private static MapVoidElementStrategy instance = new MapVoidElementStrategy();

	private MapVoidElementStrategy()
	{
	}

	public static MapVoidElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.map = ((VoidMapElement )me).getMap();
	}

	public void setLogicalNetLayer(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
		this.aContext = logicalNetLayer.getContext();
	}

	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if (actionMode == MapState.NULL_ACTION_MODE &&
				mouseMode == MapState.MOUSE_DRAGGED)
			{
				mapState.setActionMode(MapState.SELECT_MARKER_ACTION_MODE);
			}
			else
			if (actionMode == MapState.NULL_ACTION_MODE &&
				mouseMode == MapState.MOUSE_PRESSED)
			{
				logicalNetLayer.deselectAll();
			}
			else
			if (actionMode == MapState.SELECT_MARKER_ACTION_MODE &&
				operationMode == MapState.NO_OPERATION &&
				mouseMode == MapState.MOUSE_RELEASED)
			{
				mapState.setActionMode(MapState.NULL_ACTION_MODE);

				int startX = logicalNetLayer.getStartPoint().x;
				int startY = logicalNetLayer.getStartPoint().y;
				int endX = me.getPoint().x;
				int endY = me.getPoint().y;
				Rectangle selectionRect = new Rectangle(
						Math.min(startX, endX),
						Math.min(startY, endY),
						Math.abs(endX - startX),
						Math.abs(endY - startY));
				selectElementsInRect(selectionRect);
			}
		}
	}

	public void selectElementsInRect(Rectangle selectionRect)
	{
		//Здесь просто проверяется что элемент содержится в прямоугольной области
		Iterator e = logicalNetLayer.getMapView().getMap().getNodes().iterator();
	
		//Пробегаем и смотрим вхотит ли в область MapNodeElement
		while (e.hasNext())
		{
			MapNodeElement node = (MapNodeElement )e.next();
			Point p = logicalNetLayer.convertMapToScreen(node.getAnchor());

			if (selectionRect.contains(p))
			{
				node.setSelected(true);
			}
			else
				node.setSelected(false);
		}

		e = logicalNetLayer.getMapView().getMap().getNodeLinks().iterator();

		//Пробегаем и смотрим вхотит ли в область nodeLink
		while (e.hasNext() )
		{
			MapNodeLinkElement nodeLink = (MapNodeLinkElement )e.next();
			Point p;
			if (
				selectionRect.contains(logicalNetLayer.convertMapToScreen(nodeLink.getStartNode().getAnchor())) 
				&& selectionRect.contains(logicalNetLayer.convertMapToScreen(nodeLink.getEndNode().getAnchor())))
	       {
				nodeLink.setSelected(true);
			}
			else
				nodeLink.setSelected(false);
		}

		logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}
}

