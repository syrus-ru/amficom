/**
 * $Id: MapMarkElementStrategy.java,v 1.9 2004/12/08 16:20:22 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveMarkCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MarkController;
import com.syrus.AMFICOM.Client.Resource.Map.MotionDescriptor;
import com.syrus.AMFICOM.Client.Resource.MapView.MapSelection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления метки на физической линии
 * 
 * 
 * 
 * @version $Revision: 1.9 $, $Date: 2004/12/08 16:20:22 $
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
		
		MapCoordinatesConverter converter = logicalNetLayer;

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
				
				if (aContext.getApplicationModel().isEnabled(
						MapApplicationModel.ACTION_EDIT_MAP))
				{
					MapNodeLinkElement nodeLink = mark.getNodeLink();
					MapNodeElement sn = mark.getStartNode();
					MapNodeElement en = nodeLink.getOtherNode(sn);

					//Рисование о пределение координат маркера происходит путм проецирования координат
					//курсора на линию на которой маркер находится

					Point anchorPoint = converter.convertMapToScreen(mark.getLocation());
					
					Point start = converter.convertMapToScreen(sn.getLocation());
					Point end = converter.convertMapToScreen(en.getLocation());

					double lengthFromStartNode;
					
					MotionDescriptor md = new MotionDescriptor(start, end, anchorPoint, point);

					lengthFromStartNode = md.lengthFromStartNode;

					while(lengthFromStartNode > md.nodeLinkLength)
					{
						nodeLink = mark.getLink().nextNodeLink(nodeLink);
						if(nodeLink == null)
							lengthFromStartNode = md.nodeLinkLength;
						else
						{
							sn = en;
							en = nodeLink.getOtherNode(sn);
	
							mark.setNodeLink(nodeLink);
							mark.setStartNode(sn);

							start = converter.convertMapToScreen(sn.getLocation());
							end = converter.convertMapToScreen(en.getLocation());

							md = new MotionDescriptor(start, end, anchorPoint, point);

							lengthFromStartNode = md.lengthFromStartNode;
							
							if(lengthFromStartNode < 0)
							{
								lengthFromStartNode = 0;
								break;
							}
						}
					}
					while(lengthFromStartNode < 0)
					{
						nodeLink = mark.getLink().previousNodeLink(mark.getNodeLink());
						if(nodeLink == null)
							lengthFromStartNode = 0;
						else
						{
							en = sn;
							sn = nodeLink.getOtherNode(en);
	
							mark.setNodeLink(nodeLink);
							mark.setStartNode(sn);
	
							start = converter.convertMapToScreen(sn.getLocation());
							end = converter.convertMapToScreen(en.getLocation());

							md = new MotionDescriptor(start, end, anchorPoint, point);

							lengthFromStartNode = md.lengthFromStartNode;
							
							if(lengthFromStartNode > md.nodeLinkLength)
							{
								lengthFromStartNode = md.nodeLinkLength;
								break;
							}
						}
					}

					MarkController mc = (MarkController )logicalNetLayer.getMapViewController().getController(mark);

					mc.adjustPosition(mark, lengthFromStartNode);
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

