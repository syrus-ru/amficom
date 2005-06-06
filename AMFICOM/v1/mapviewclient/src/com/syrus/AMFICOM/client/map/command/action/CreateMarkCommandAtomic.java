/**
 * $Id: CreateMarkCommandAtomic.java,v 1.13 2005/06/06 12:57:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

/**
 * Команда создания метки на линии
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.13 $, $Date: 2005/06/06 12:57:01 $
 * @module mapviewclient_v1
 */
public class CreateMarkCommandAtomic extends MapActionCommand
{
	/**
	 * созданный элемент метки
	 */
	Mark mark;

	/**
	 * Выбранный фрагмент линии
	 */
	PhysicalLink link;
	
	Map map;
	
	/**
	 * дистанция от начала линии, на которой создается метка
	 */
	double distance;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreateMarkCommandAtomic(
			PhysicalLink link,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.link = link;
		this.point = point;
	}

	public void execute()
	{
		try
		{
			Environment.log(
					Environment.LOG_LEVEL_FINER, 
					"method call", 
					getClass().getName(), 
					"execute()");
			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
				return;
			this.map = this.logicalNetLayer.getMapView().getMap();
			this.link.sortNodeLinks();
			this.distance = 0.0;
			AbstractNode node = this.link.getStartNode();
			for(Iterator it = this.link.getNodeLinks().iterator(); it.hasNext();)
			{
				NodeLink mnle = (NodeLink)it.next();

				NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(mnle);

				if(nlc.isMouseOnElement(mnle, this.point))
				{
					DoublePoint dpoint = this.logicalNetLayer.convertScreenToMap(this.point);
					this.distance += this.logicalNetLayer.distance(node.getLocation(), dpoint);
					break;
				}
				nlc.updateLengthLt(mnle);
				this.distance += mnle.getLengthLt();

				if(mnle.getStartNode().equals(node))
					node = mnle.getEndNode();
				else
					node = mnle.getStartNode();
			}
			try
			{
				this.mark = Mark.createInstance(
						this.logicalNetLayer.getUserId(),
						this.link, 
						this.distance);
			}
			catch (CreateObjectException e)
			{
				e.printStackTrace();
			}
			this.map.addNode(this.mark);
			MarkController mc = (MarkController)getLogicalNetLayer().getMapViewController().getController(this.mark);
			mc.updateScaleCoefficient(this.mark);
			// операция закончена - оповестить слушателей
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
						this.mark,
						MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.mark);
			setResult(Command.RESULT_OK);
		}
		catch(Exception e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
	public void undo()
	{
		this.map.removeNode(this.mark);
	}
	
	public void redo()
	{
		this.map.addNode(this.mark);
	}
}
