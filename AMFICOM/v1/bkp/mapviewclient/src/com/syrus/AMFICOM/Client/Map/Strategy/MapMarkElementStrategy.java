/**
 * $Id: MapMarkElementStrategy.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.Command.Action.MoveMarkCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления метки на физической линии
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapMarkElementStrategy implements  MapStrategy 
{
	LogicalNetLayer logicalNetLayer;
	Point point;
	ApplicationContext aContext;
	
	MapMarkElement mark;
	MoveMarkCommand command;

	private static MapMarkElementStrategy instance = new MapMarkElementStrategy();

	private MapMarkElementStrategy()
	{
	}

	public static MapMarkElementStrategy getInstance()
	{
		return instance;
	}
	
	public void setMapElement(MapElement me)
	{
		this.mark = (MapMarkElement )me;
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
		Map map = mark.getMap();
		
		MapCoordinatesConverter converter = map.getConverter();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if ((actionMode != MapState.SELECT_ACTION_MODE) &&
				(actionMode != MapState.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapView().deselectAll();
//				map.deselectAll();
			}
			mark.setSelected(true);
			switch (mouseMode)
			{
				case MapState.MOUSE_DRAGGED:
					//Проверка того что маркер можно перемещать и его перемещение
					
					if(command == null)
						command = new MoveMarkCommand(mark);
					
					if (aContext.getApplicationModel().isEnabled("mapActionMarkMove"))
					{
//						mark.link.setSelected(true);

//						moveToFromStart(distance);

						//Рисование о пределение координат маркера происходит путм проецирования координат
						//курсора на линию на которой маркер находится

						Point anchorPoint = converter.convertMapToScreen(mark.getAnchor());
						
						Point start = converter.convertMapToScreen(mark.getNodeLink().getStartNode().getAnchor());
						Point end = converter.convertMapToScreen(mark.getNodeLink().getEndNode().getAnchor());

						double lengthFromStartNode = mark.getSizeInDoubleLt();

						double nodeLinkLength =  mark.getNodeLink().getLengthLt();
						mark.getNodeLink().calcScreenSlope();
						double cos_b = mark.getNodeLink().getScreenCos();
						double sin_b = mark.getNodeLink().getScreenSin();

						double lengthThisToMousePoint = Math.sqrt( 
								(point.x - anchorPoint.x) * (point.x - anchorPoint.x) +
								(point.y - anchorPoint.y) * (point.y - anchorPoint.y) );

						double cos_a = 
							( 	(end.x - start.x) * (point.x - anchorPoint.y)
								+ (end.y - start.y) * (point.y - anchorPoint.y) ) 
							/ nodeLinkLength 
							* lengthThisToMousePoint;

						lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;

						if ( lengthFromStartNode > nodeLinkLength )
						{
							mark.setNodeLink(mark.getLink().nextNodeLink(mark.getNodeLink()));
							lengthFromStartNode -= nodeLinkLength;
						}
						else
						if ( lengthFromStartNode < 0 )
						{
							mark.setNodeLink(mark.getLink().previousNodeLink(mark.getNodeLink()));
							lengthFromStartNode += mark.getNodeLink().getLengthLt();
						}

						mark.setAnchor(
							converter.convertScreenToMap(
								new Point(
									(int)Math.round(start.x + sin_b * ( lengthFromStartNode )),
									(int)Math.round(start.y + cos_b * ( lengthFromStartNode )) 
								) 
							) 
						);
					}
					break;
				case MapState.MOUSE_RELEASED:
					if (actionMode == MapState.MOVE_ACTION_MODE)
					{
						logicalNetLayer.getCommandList().add(command);
						logicalNetLayer.getCommandList().execute();
						command = null;
					}//if (actionMode == MapState.MOVE_ACTION_MODE)
					break;
				default:
					break;
			}//switch

		}//SwingUtilities.isLeftMouseButton(me)

	}
}

