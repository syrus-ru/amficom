/**
 * $Id: MapMarkElementStrategy.java,v 1.4 2004/10/19 11:48:28 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления метки на физической линии
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/19 11:48:28 $
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
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof MapSelection)
					{
						MapSelection sel = (MapSelection )mel;
						sel.add(mark);
					}
					else
					{
						MapSelection sel = new MapSelection(logicalNetLayer);
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				mark.setSelected(true);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
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
					
					mark.getNodeLink().updateLengthLt();
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
						mark.getNodeLink().updateLengthLt();
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
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
					logicalNetLayer.getCommandList().add(command);
					logicalNetLayer.getCommandList().execute();
					command = null;
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
			}//MapState.MOUSE_RELEASED

		}//SwingUtilities.isLeftMouseButton(me)

	}
}

