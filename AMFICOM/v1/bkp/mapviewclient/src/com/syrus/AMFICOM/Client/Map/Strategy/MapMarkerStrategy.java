/**
 * $Id: MapMarkerStrategy.java,v 1.5 2004/10/27 15:46:24 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;

import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * Стратегия управления маркером
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/10/27 15:46:24 $
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
					}
					else
					{
						logicalNetLayer.deselectAll();
					}
				}
				else
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				marker.setSelected(true);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				//Проверка того что маркер можно перемещать и его перемещение
				if ( logicalNetLayer.getContext().getApplicationModel().isEnabled(
							MapApplicationModel.ACTION_USE_MARKER))
				{
					//Посылаем сообщение что маркер перемещается
					marker.notifyMarkerMoved();
					
					MapNodeLinkElement nodeLink = marker.getNodeLink();
					MapNodeElement sn = marker.getStartNode();
					MapNodeElement en = marker.getEndNode();

//					MapMarker.MotionDescriptor md = marker.getMotionDescriptor(point);

					Point anchorPoint = converter.convertMapToScreen(marker.getAnchor());
					
					Point start = converter.convertMapToScreen(sn.getAnchor());
					Point end = converter.convertMapToScreen(en.getAnchor());

					double lengthFromStartNode = marker.startToThis();
					
					double nodeLinkLength =  nodeLink.getLengthLt();

					double lengthThisToMousePoint = Math.sqrt( 
							(point.x - anchorPoint.x) * (point.x - anchorPoint.x) +
							(point.y - anchorPoint.y) * (point.y - anchorPoint.y) );

//					double gamma = Math.atan2((point.x - anchorPoint.x), (point.y - anchorPoint.y));
//					double betta = Math.atan2((end.x - start.x), (end.y - start.y));
//					double cos_a = Math.cos(gamma - betta);

					double cos_a = 	( 	(end.x - start.x) * (point.x - anchorPoint.x)
							+ (end.y - start.y) * (point.y - anchorPoint.y) ) 
						/ (nodeLinkLength * lengthThisToMousePoint);

					lengthFromStartNode = lengthFromStartNode + cos_a * lengthThisToMousePoint;

					if ( lengthFromStartNode > nodeLinkLength )
					{
						nodeLink = marker.nextNodeLink();
						if(nodeLink == null)
							lengthFromStartNode = nodeLinkLength;
						else
						{
							sn = en;
							en = nodeLink.getOtherNode(sn);
	
							marker.setNodeLink(nodeLink);
							marker.setStartNode(sn);
							marker.setEndNode(en);
	
							lengthFromStartNode -= nodeLinkLength;
						}
					}
					else
					if ( lengthFromStartNode < 0 )
					{
						nodeLink = marker.previousNodeLink();
						if(nodeLink == null)
							lengthFromStartNode = 0;
						else
						{
							en = sn;
							sn = nodeLink.getOtherNode(en);
	
							marker.setNodeLink(nodeLink);
							marker.setStartNode(sn);
							marker.setEndNode(en);
	
							lengthFromStartNode += nodeLink.getLengthLt();
						}
					}

					marker.adjustPosition(lengthFromStartNode);
				}// MapApplicationModel.ACTION_USE_MARKER
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				if (actionMode == MapState.MOVE_ACTION_MODE)
				{
				}//if (actionMode == MapState.MOVE_ACTION_MODE)
			}//MapState.MOUSE_RELEASED

		}//SwingUtilities.isLeftMouseButton(me)

	}
}

