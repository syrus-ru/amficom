/**
 * $Id: CreateMarkCommand.java,v 1.3 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
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
 * Разместить элемент типа mpe на карте. используется при переносе 
 * (drag/drop), в точке point (в экранных координатах)
 * 
 * @version $Revision: 1.3 $, $Date: 2004/09/21 14:59:20 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateMarkCommand extends MapActionCommand
{
	/**
	 * Выбранный фрагмент линии
	 */
	MapMarkElement mark;
	MapPhysicalLinkElement link;
	
	Map map;
	
	double distance;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreateMarkCommand(
			MapPhysicalLinkElement link,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.link = link;
		this.point = point;
	}

	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		if ( !getLogicalNetLayer().getContext().getApplicationModel()
				.isEnabled("mapActionCreateEquipment"))
			return;
		
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
	
		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		map = logicalNetLayer.getMapView().getMap();

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
				aContext.getDataSourceInterface().GetUId(MapMarkElement.typ),
				map, 
				link, 
				distance);

		Pool.put(MapMarkElement.typ, mark.getId(), mark);
		map.addNode(mark);
		
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
