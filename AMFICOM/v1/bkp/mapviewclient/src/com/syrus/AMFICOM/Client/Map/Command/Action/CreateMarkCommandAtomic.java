/**
 * $Id: CreateMarkCommandAtomic.java,v 1.7 2004/12/24 15:42:11 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * Команда создания метки на линии
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/24 15:42:11 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_MAP))
			return;
		
		map = logicalNetLayer.getMapView().getMap();

		link.sortNodeLinks();
		distance = 0.0;
		AbstractNode node = link.getStartNode();

		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			NodeLinkController nlc = (NodeLinkController)getLogicalNetLayer().getMapViewController().getController(mnle);

			if(nlc.isMouseOnElement(mnle, point))
			{
				DoublePoint dpoint = logicalNetLayer.convertScreenToMap(point);
				distance += logicalNetLayer.distance(node.getLocation(), dpoint);
				break;
			}
			else
			{
				nlc.updateLengthLt(mnle);
				distance += mnle.getLengthLt();
			}

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}

		try
		{
			mark = Mark.createInstance(
					new Identifier(aContext.getSessionInterface().getAccessIdentifier().user_id),
					link, 
					distance);
		}
		catch (CreateObjectException e)
		{
			e.printStackTrace();
		}

		map.addNode(mark);

		MarkController mc = (MarkController)getLogicalNetLayer().getMapViewController().getController(mark);

		mc.updateScaleCoefficient(mark);
		
		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					mark,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(mark);
	}
	
	public void undo()
	{
		map.removeNode(mark);
	}
	
	public void redo()
	{
		map.addNode(mark);
	}
}
