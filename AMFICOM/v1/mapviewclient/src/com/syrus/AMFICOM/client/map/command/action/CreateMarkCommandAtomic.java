/**
 * $Id: CreateMarkCommandAtomic.java,v 1.3 2004/11/16 17:31:17 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;

import java.awt.Point;
import java.awt.geom.Point2D;

import java.util.Iterator;

/**
 * Команда создания метки на линии
 * 
 * @version $Revision: 1.3 $, $Date: 2004/11/16 17:31:17 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateMarkCommandAtomic extends MapActionCommand
{
	/**
	 * созданный элемент метки
	 */
	MapMarkElement mark;

	/**
	 * Выбранный фрагмент линии
	 */
	MapPhysicalLinkElement link;
	
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
			MapPhysicalLinkElement link,
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

//		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		link.sortNodeLinks();
		distance = 0.0;
		MapNodeElement node = link.getStartNode();
		for(Iterator it = link.getNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
				
			if(mnle.isMouseOnThisObject(point))
			{
				Point2D.Double dpoint = logicalNetLayer.convertScreenToMap(point);
				distance += logicalNetLayer.distance(node.getAnchor(), dpoint);
				break;
			}
			else
			{
				mnle.updateLengthLt();
				distance += mnle.getLengthLt();
			}

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}

		mark = new MapMarkElement(
				aContext.getDataSource().GetUId(MapMarkElement.typ),
				map, 
				link, 
				distance);

		Pool.put(MapMarkElement.typ, mark.getId(), mark);
		map.addNode(mark);

		mark.setScaleCoefficient(logicalNetLayer.getDefaultScale() / logicalNetLayer.getCurrentScale());
		
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
		Pool.remove(MapMarkElement.typ, mark.getId());
	}
	
	public void redo()
	{
		map.addNode(mark);
		Pool.put(MapMarkElement.typ, mark.getId(), mark);
	}
}
