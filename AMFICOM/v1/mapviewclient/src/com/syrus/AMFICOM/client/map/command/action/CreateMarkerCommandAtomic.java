/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.1 2004/10/26 13:32:01 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
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
import java.util.List;

/**
 * Команда создания метки на линии
 * 
 * @version $Revision: 1.1 $, $Date: 2004/10/26 13:32:01 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateMarkerCommandAtomic extends MapActionCommand
{
	/**
	 * созданный элемент метки
	 */
	MapMarker marker;

	/**
	 * Выбранный фрагмент линии
	 */
	MapMeasurementPathElement path;
	
	MapView mapView;
	
	/**
	 * дистанция от начала линии, на которой создается метка
	 */
	double distance;
	
	/**
	 * точка, в которой создается новый топологический узел
	 */
	Point point;

	public CreateMarkerCommandAtomic(
			MapMeasurementPathElement path,
			Point point)
	{
		super(MapActionCommand.ACTION_DRAW_NODE);
		this.path = path;
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
				.isEnabled(MapApplicationModel.ACTION_USE_MARKER))
			return;
		
		mapView = logicalNetLayer.getMapView();

//		Point2D.Double coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		MapNodeElement node = path.getStartNode();
		path.sortPathElements();
		List nodeLinks = path.getSortedNodeLinks();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
				
			if(mnle.isMouseOnThisObject(point))
			{
				Point2D.Double dpoint = logicalNetLayer.convertScreenToMap(point);

				marker = new MapMarker(
						aContext.getDataSource().GetUId(MapMarker.typ),
						mapView, 
						node,
						mnle.getOtherNode(node),
						mnle,
						path,
						dpoint);
		
				Pool.put(MapMarker.typ, marker.getId(), marker);
				mapView.getMap().addNode(marker);
				
				marker.notifyMarkerCreated();
				
				break;
			}
			else
			{
				mnle.updateLengthLt();
			}

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					marker,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(marker);
	}
	
}
