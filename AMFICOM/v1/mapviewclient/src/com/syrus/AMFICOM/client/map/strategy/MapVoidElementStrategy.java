/**
 * $Id: MapVoidElementStrategy.java,v 1.16 2005/02/02 07:56:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Selection;
import com.syrus.AMFICOM.mapview.VoidElement;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import java.util.Iterator;
import java.util.Set;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления элементами, когда нет выбранных элементов.
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/02/02 07:56:01 $
 * @module mapviewclient_v1
 */
public final class MapVoidElementStrategy extends MapStrategy 
{
	/**
	 * Топологическая схема.
	 */
	Map map;
	/**
	 * Вид карты.
	 */
	MapView mapView;

	/**
	 * Instance.
	 */
	private static MapVoidElementStrategy instance = new MapVoidElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapVoidElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapVoidElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.mapView = ((VoidElement)me).getMapView();
		this.map = ((VoidElement)me).getMap();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.NULL_ACTION_MODE)
		{
			logicalNetLayer.deselectAll();
		}//MapState.NULL_ACTION_MODE
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (actionMode == MapState.NULL_ACTION_MODE &&
			operationMode == MapState.NO_OPERATION)
		{
			mapState.setActionMode(MapState.SELECT_MARKER_ACTION_MODE);
		}//MapState.NULL_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();
		int operationMode = mapState.getOperationMode();

		if (actionMode == MapState.SELECT_MARKER_ACTION_MODE &&
			operationMode == MapState.NO_OPERATION)
		{
			int startX = logicalNetLayer.getStartPoint().x;
			int startY = logicalNetLayer.getStartPoint().y;
			int endX = point.x;
			int endY = point.y;
			Rectangle selectionRect = new Rectangle(
					Math.min(startX, endX),
					Math.min(startY, endY),
					Math.abs(endX - startX),
					Math.abs(endY - startY));
			selectElementsInRect(selectionRect);
		}//MapState.SELECT_MARKER_ACTION_MODE && MapState.NO_OPERATION
	}

	/**
	 * Выделить все элементы, попадающие в область.
	 * @param selectionRect область выборки в экранных координатах
	 */
	protected  void selectElementsInRect(Rectangle selectionRect)
	{
		Map map = logicalNetLayer.getMapView().getMap();
		//Здесь просто проверяется что элемент содержится в прямоугольной области
		Iterator e = map.getNodes().iterator();
		
		//Пробегаем и смотрим вхотит ли в область MapNodeElement
		while (e.hasNext())
		{
			AbstractNode node = (AbstractNode)e.next();
			{
				Point p = logicalNetLayer.convertMapToScreen(node.getLocation());
	
				if (selectionRect.contains(p))
				{
					node.setSelected(true);
				}
				else
				{
					node.setSelected(false);
				}
			}
		}

		e = map.getNodeLinks().iterator();

		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_NODE_LINK)
		{
			//Пробегаем и смотрим вхотит ли в область nodeLink
			while (e.hasNext() )
			{
				NodeLink nodeLink = (NodeLink)e.next();
				if (
					selectionRect.contains(
						logicalNetLayer.convertMapToScreen(
							nodeLink.getStartNode().getLocation())) 
					&& selectionRect.contains(
						logicalNetLayer.convertMapToScreen(
							nodeLink.getEndNode().getLocation())))
				{
					nodeLink.setSelected(true);
				}
				else
				{
					nodeLink.setSelected(false);
				}
			}
		}
		else
		if(logicalNetLayer.getMapState().getShowMode() == MapState.SHOW_PHYSICAL_LINK)
		{
			for(Iterator it = map.getPhysicalLinks().iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				boolean select = true;
				for(Iterator it2 = link.getNodeLinks().iterator(); it2.hasNext();)
				{
					NodeLink nodeLink = (NodeLink)it2.next();
					if (! (
						selectionRect.contains(
							logicalNetLayer.convertMapToScreen(
								nodeLink.getStartNode().getLocation())) 
						&& selectionRect.contains(
							logicalNetLayer.convertMapToScreen(
								nodeLink.getEndNode().getLocation()))))
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
			MapElement me = (MapElement)selection.iterator().next();
			logicalNetLayer.setCurrentMapElement(me);
		}
		else
		if(selection.size() > 1)
		{
			Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
			sel.addAll(selection);
			logicalNetLayer.setCurrentMapElement(sel);
		}

		logicalNetLayer.getMapState().setActionMode(MapState.NULL_ACTION_MODE);
	}
}

