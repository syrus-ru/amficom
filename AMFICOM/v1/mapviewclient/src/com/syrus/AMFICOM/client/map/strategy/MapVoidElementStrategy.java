/**
 * $Id: MapVoidElementStrategy.java,v 1.7 2004/10/18 15:33:00 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.MapView.VoidMapElement;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;

/**
 * Стратегия управления элементами, когда нет выбранных элементов
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2004/10/18 15:33:00 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapVoidElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;
	
	Map map;
	MapView mapView;

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
		this.mapView = ((VoidMapElement)me).getMapView();
		this.map = ((VoidMapElement)me).getMap();
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
		
//		Rectangle2D.Double visibleBounds = logicalNetLayer.getVisibleBounds();
	
		//Пробегаем и смотрим вхотит ли в область MapNodeElement
		while (e.hasNext())
		{
			MapNodeElement node = (MapNodeElement )e.next();
//			if(node.isVisible(visibleBounds))
			{
				Point p = logicalNetLayer.convertMapToScreen(node.getAnchor());
	
				if (selectionRect.contains(p))
					node.setSelected(true);
				else
					node.setSelected(false);
			}
		}

		e = logicalNetLayer.getMapView().getMap().getNodeLinks().iterator();

		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
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
		}
		else
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			for(Iterator it = logicalNetLayer.getMapView().getMap().getPhysicalLinks().iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
				boolean select = true;
				for(Iterator it2 = link.getNodeLinks().iterator(); it2.hasNext();)
				{
					MapNodeLinkElement nodeLink = (MapNodeLinkElement )it2.next();
					Point p;
					if (! (
						selectionRect.contains(logicalNetLayer.convertMapToScreen(nodeLink.getStartNode().getAnchor())) 
						&& selectionRect.contains(logicalNetLayer.convertMapToScreen(nodeLink.getEndNode().getAnchor()))))
				   {
						select = false;
				   }
				}
				link.setSelected(select);
			}
		}
		
		Set selection = logicalNetLayer.getSelectedElements();
		if(selection.size() == 1)
		{
			MapElement me = (MapElement )selection.iterator().next();
			logicalNetLayer.setCurrentMapElement(me);
		}
		else
		if(selection.size() > 1)
		{
			MapSelection sel = new MapSelection(logicalNetLayer);
			sel.addAll(selection);
			logicalNetLayer.setCurrentMapElement(sel);
		}

		logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}
}

