/**
 * $Id: MapMarkElementStrategy.java,v 1.18 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

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

/**
 * Стратегия управления метки на физической линии.
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/02/07 16:09:26 $
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
	public void setMapElement(MapElement me)
	{
		this.mark = (Mark)me;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void leftMousePressed(MapState mapState, Point point)
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
				Selection sel = new Selection(super.logicalNetLayer.getMapView().getMap());
				sel.addAll(super.logicalNetLayer.getSelectedElements());
				super.logicalNetLayer.setCurrentMapElement(sel);
			}
		}//MapState.SELECT_ACTION_MODE
		if ((actionMode != MapState.SELECT_ACTION_MODE) && (actionMode != MapState.MOVE_ACTION_MODE))
		{
			super.logicalNetLayer.deselectAll();
		}// ! MapState.SELECT_ACTION_MODE && ! MapState.MOVE_ACTION_MODE
		this.mark.setSelected(true);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseDragged(MapState mapState, Point point)
	{
		int actionMode = mapState.getActionMode();

		MapCoordinatesConverter converter = super.logicalNetLayer;

		if (this.command == null)
			this.command = new MoveMarkCommand(this.mark);
		if (super.aContext.getApplicationModel().isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
		{
			NodeLink nodeLink = this.mark.getNodeLink();
			AbstractNode sn = this.mark.getStartNode();
			AbstractNode en = nodeLink.getOtherNode(sn);
			Point anchorPoint = converter.convertMapToScreen(this.mark.getLocation());
			Point start = converter.convertMapToScreen(sn.getLocation());
			Point end = converter.convertMapToScreen(en.getLocation());
			double lengthFromStartNode;
			MotionDescriptor md = new MotionDescriptor(start, end, anchorPoint, point);
			lengthFromStartNode = md.lengthFromStartNode;
			while (lengthFromStartNode > md.nodeLinkLength)
			{
				nodeLink = this.mark.getPhysicalLink().nextNodeLink(nodeLink);
				if (nodeLink == null)
					lengthFromStartNode = md.nodeLinkLength;
				else
				{
					sn = en;
					en = nodeLink.getOtherNode(sn);
					this.mark.setNodeLink(nodeLink);
					this.mark.setStartNode(sn);
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
				nodeLink = this.mark.getPhysicalLink().previousNodeLink(this.mark.getNodeLink());
				if (nodeLink == null)
					lengthFromStartNode = 0;
				else
				{
					en = sn;
					sn = nodeLink.getOtherNode(en);
					this.mark.setNodeLink(nodeLink);
					this.mark.setStartNode(sn);
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
			MarkController mc = (MarkController)super.logicalNetLayer.getMapViewController().getController(this.mark);
			mc.adjustPosition(this.mark, lengthFromStartNode);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void leftMouseReleased(MapState mapState, Point point)
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

