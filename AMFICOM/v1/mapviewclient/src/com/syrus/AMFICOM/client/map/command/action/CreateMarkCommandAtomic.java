/**
 * $Id: CreateMarkCommandAtomic.java,v 1.15 2005/06/22 08:43:46 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.controllers.MarkController;
import com.syrus.AMFICOM.client.map.controllers.NodeLinkController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.util.Log;

/**
 * Команда создания метки на линии
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.15 $, $Date: 2005/06/22 08:43:46 $
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
			Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);
			if ( !getLogicalNetLayer().getContext().getApplicationModel()
					.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
				return;
			MapCoordinatesConverter converter = this.logicalNetLayer.getConverter();
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
					DoublePoint dpoint = converter.convertScreenToMap(this.point);
					this.distance += converter.distance(node.getLocation(), dpoint);
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
						LoginManager.getUserId(),
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
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
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
