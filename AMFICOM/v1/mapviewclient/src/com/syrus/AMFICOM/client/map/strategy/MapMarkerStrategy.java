/**
 * $Id: MapMarkerStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * Стратегия управления маркером
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapMarkerStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	ApplicationContext aContext;

	MapMarker marker;

	private static MapMarkerStrategy instance = new MapMarkerStrategy();

	private MapMarkerStrategy()
	{
	}

	public static MapMarkerStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.marker = (MapMarker )me;
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
		Map map = marker.getMap();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();
/*
		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != MapState.SELECT_ACTION_MODE) &&
				(actionMode != MapState.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.deselectAll();
			}
			setSelected(true);
			switch (mouseMode)
			{
				case MapState.MOUSE_DRAGGED:
					//Проверка того что маркер можно перемещать и его перемещение
					if ( lnl.mapMainFrame
							.aContext.getApplicationModel().isEnabled("mapActionMarkerMove"))
					if(isMovable())
					{
						transmissionPath.select();

						//Посылаем сообщение что маркер перемещается
						this.sendMessage_Marker_Moved();

						MotionDescriptor md = getMotionDescriptor(point);

						Vector nl = transmissionPath.sortNodeLinks();
						while ( md.lengthFromStartNode > md.nodeLinkLength )
						{
							if(nodeLinkIndex >= nl.size() - 1)
							{
								md.lengthFromStartNode = md.nodeLinkLength;
								break;
							}

							nodeLinkIndex++;
							MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
//							System.out.print(md.lengthFromStartNode  + " > " + md.nodeLinkLength);
//							System.out.print(" : move from " + nodeLink.getId() + " to " + mnle.getId());
							startNode = endNode;
							nodeLink = mnle;
							endNode = getMap().getOtherNodeOfNodeLink(mnle, startNode);
							md = getMotionDescriptor(point);
//							System.out.println(" (dist " + md.lengthFromStartNode  + ") ");

							double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
							double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

							bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
								new Point(
									(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
									(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

							setAnchor(bufferAnchor);
						}

						while ( md.lengthFromStartNode < 0 )
						{
							if(nodeLinkIndex <= 0)
							{
								md.lengthFromStartNode = 0;
								break;
							}

							nodeLinkIndex--;
							MapNodeLinkElement mnle = (MapNodeLinkElement) nl.get(nodeLinkIndex);
//							System.out.print(md.lengthFromStartNode  + " < 0 ");
//							System.out.print(" : move from " + nodeLink.getId() + " to " + mnle.getId());
							endNode = startNode;
							nodeLink = mnle;
							startNode = getMap().getOtherNodeOfNodeLink(mnle, endNode);
							md = getMotionDescriptor(point);
//							System.out.println(" (dist " + md.lengthFromStartNode  + ") ");

							double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
							double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

							bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
								new Point(
									(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
									(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

							setAnchor(bufferAnchor);
						}

						double startNodeX = lnl.convertLongLatToScreen(startNode.getAnchor()).x;
						double startNodeY = lnl.convertLongLatToScreen(startNode.getAnchor()).y;

						bufferAnchor = getLogicalNetLayer().convertScreenToLongLat(
							new Point(
								(int)Math.round(startNodeX + md.sin_b * ( md.lengthFromStartNode )),
								(int)Math.round(startNodeY + md.cos_b * ( md.lengthFromStartNode )) ) );

						setAnchor(bufferAnchor);
					}// if(isMovable())
					break;
			}//switch

		}//SwingUtilities.isLeftMouseButton(me)
*/
	}
}

