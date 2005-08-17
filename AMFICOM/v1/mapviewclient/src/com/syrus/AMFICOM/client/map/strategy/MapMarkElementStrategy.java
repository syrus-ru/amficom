/**
 * $Id: MapMarkElementStrategy.java,v 1.31 2005/08/17 14:14:20 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.strategy;

import java.awt.Point;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.command.action.MoveMarkCommand;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.ui.MotionDescriptor;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.Selection;

/**
 * Стратегия управления метки на физической линии.
 * @author $Author: arseniy $
 * @version $Revision: 1.31 $, $Date: 2005/08/17 14:14:20 $
 * @module mapviewclient
 */
public final class MapMarkElementStrategy extends AbstractMapStrategy 
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
	{//empty
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
	@Override
	public void setMapElement(MapElement me)
	{
		this.mark = (Mark)me;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMousePressed(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if ((actionMode == MapState.SELECT_ACTION_MODE))
		{
			MapElement mel = super.logicalNetLayer.getCurrentMapElement();
			if (mel instanceof Selection)
			{
				Selection sel = (Selection)mel;
				sel.add(this.mark);
			}
			else
			{
				Selection sel = new Selection();
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		super.logicalNetLayer.getMapView().getMap().setSelected(this.mark, true);
//		this.netMapViewer.getLogicalNetLayer().sendSelectionChangeEvent();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseDragged(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		MapCoordinatesConverter converter = super.logicalNetLayer.getConverter();

		if (this.command == null)
			this.command = new MoveMarkCommand(this.mark);
		if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			NodeLink nodeLink = this.mark.getNodeLink();
			AbstractNode startNode = this.mark.getStartNode();
			AbstractNode endNode = nodeLink.getOtherNode(startNode);
			Point anchorPoint = converter.convertMapToScreen(this.mark.getLocation());
			Point startPoint = converter.convertMapToScreen(startNode.getLocation());
			Point endPoint = converter.convertMapToScreen(endNode.getLocation());
			double lengthFromStartNode;
			MotionDescriptor motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
			lengthFromStartNode = motionDescriptor.lengthFromStartNode;
			while (lengthFromStartNode > motionDescriptor.nodeLinkLength)
			{
				nodeLink = this.mark.getPhysicalLink().nextNodeLink(nodeLink);
				if (nodeLink == null)
					lengthFromStartNode = motionDescriptor.nodeLinkLength;
				else
				{
					startNode = endNode;
					endNode = nodeLink.getOtherNode(startNode);
					this.mark.setNodeLink(nodeLink);
					this.mark.setStartNode(startNode);
					startPoint = converter.convertMapToScreen(startNode.getLocation());
					endPoint = converter.convertMapToScreen(endNode.getLocation());
					motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
					lengthFromStartNode = motionDescriptor.lengthFromStartNode;
					if (lengthFromStartNode < 0)
					{
						lengthFromStartNode = 0;
						break;
					}
				}
			}
			while (lengthFromStartNode < 0)
			{
				nodeLink = this.mark.getPhysicalLink().previousNodeLink(this.mark.getNodeLink());
				if (nodeLink == null)
					lengthFromStartNode = 0;
				else
				{
					endNode = startNode;
					startNode = nodeLink.getOtherNode(endNode);
					this.mark.setNodeLink(nodeLink);
					this.mark.setStartNode(startNode);
					startPoint = converter.convertMapToScreen(startNode.getLocation());
					endPoint = converter.convertMapToScreen(endNode.getLocation());
					motionDescriptor = new MotionDescriptor(startPoint, endPoint, anchorPoint, point);
					lengthFromStartNode = motionDescriptor.lengthFromStartNode;
					if (lengthFromStartNode > motionDescriptor.nodeLinkLength)
					{
						lengthFromStartNode = motionDescriptor.nodeLinkLength;
						break;
					}
				}
			}
			MarkController mc = (MarkController)super.logicalNetLayer.getMapViewController().getController(this.mark);
			mc.adjustPosition(this.mark, lengthFromStartNode);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void leftMouseReleased(MapState mapState, Point point)
		throws MapConnectionException, MapDataException
	{
		int actionMode = mapState.getActionMode();

		if (actionMode == MapState.MOVE_ACTION_MODE)
		{
			super.logicalNetLayer.getCommandList().add(this.command);
			super.logicalNetLayer.getCommandList().execute();
			//set this.command to null since it is to be reused
			this.command = null;
		}//MapState.MOVE_ACTION_MODE
	}

}

