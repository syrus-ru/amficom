/**
 * $Id: MapMarkElementStrategy.java,v 1.15 2005/02/01 17:18:16 krupenn Exp $
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
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Command.Action.MoveMarkCommand;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.Client.Map.UI.MotionDescriptor;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления метки на физической линии.
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/02/01 17:18:16 $
 * @module mapviewclient_v1
 */
public final class MapMarkElementStrategy extends MapStrategy 
{
	/**
	 * Метка.
	 */
	Mark mark;
	/**
	 * Команда перемещения метки вдоль линии. Команда создается при начале
	 * перемещения и исполняется при завершении перемещения.
	 */
	MoveMarkCommand command;

	/**
	 * instance.
	 */
	private static MapMarkElementStrategy instance = new MapMarkElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapMarkElementStrategy()
	{
	}

	/**
	 * Get instance.
	 * @return instance
	 */
	public static MapMarkElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.mark = (Mark)me;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();
		
		int mouseMode = mapState.getMouseMode();

		Point point = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				leftMousePressed(mapState, point);
			}//MapState.MOUSE_PRESSED
			else
			if(mouseMode == MapState.MOUSE_DRAGGED)
			{
				leftMouseDragged(mapState, point);
				
			}//MapState.MOUSE_DRAGGED
			else
			if(mouseMode == MapState.MOUSE_RELEASED)
			{
				leftMouseReleased(mapState, point);
			}//MapState.MOUSE_RELEASED

		}//SwingUtilities.isLeftMouseButton(me)

	}

	/**
	 * Process left mouse dragged.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		MapCoordinatesConverter converter = logicalNetLayer;

		if (command == null)
			command = new MoveMarkCommand(mark);
		if (aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			NodeLink nodeLink = mark.getNodeLink();
			AbstractNode sn = mark.getStartNode();
			AbstractNode en = nodeLink.getOtherNode(sn);
			Point anchorPoint = converter.convertMapToScreen(mark.getLocation());
			Point start = converter.convertMapToScreen(sn.getLocation());
			Point end = converter.convertMapToScreen(en.getLocation());
			double lengthFromStartNode;
			MotionDescriptor md = new MotionDescriptor(start, end, anchorPoint, point);
			lengthFromStartNode = md.lengthFromStartNode;
			while (lengthFromStartNode > md.nodeLinkLength)
			{
				nodeLink = mark.getPhysicalLink().nextNodeLink(nodeLink);
				if (nodeLink == null)
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
					if (lengthFromStartNode < 0)
					{
						lengthFromStartNode = 0;
						break;
					}
				}
			}
			while (lengthFromStartNode < 0)
			{
				nodeLink = mark.getPhysicalLink().previousNodeLink(mark.getNodeLink());
				if (nodeLink == null)
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
					if (lengthFromStartNode > md.nodeLinkLength)
					{
						lengthFromStartNode = md.nodeLinkLength;
						break;
					}
				}
			}
			MarkController mc = (MarkController)logicalNetLayer.getMapViewController().getController(mark);
			mc.adjustPosition(mark, lengthFromStartNode);
		}
	}

	/**
	 * Process left mouse released.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMouseReleased(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			logicalNetLayer.getCommandList().add(command);
			logicalNetLayer.getCommandList().execute();
			//set command to null since it is to be reused
			command = null;
		}//MapState.MOVE_ACTION_MODE
	}

	/**
	 * Process left mouse pressed.
	 * @param mapState map state
	 * @param point new point
	 */
	void leftMousePressed(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(mark);
			}
			else
			{
				Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
				sel.addAll(logicalNetLayer.getSelectedElements());
				logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		mark.setSelected(true);
	}
}

