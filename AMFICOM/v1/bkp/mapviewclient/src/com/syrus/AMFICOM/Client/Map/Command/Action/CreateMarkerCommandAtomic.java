/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.13 2005/02/02 08:58:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Controllers.MeasurementPathController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
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
import java.util.List;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;

/**
 * Команда создания метки на линии
 * 
 * @version $Revision: 1.13 $, $Date: 2005/02/02 08:58:39 $
 * @module map_v2
 * @author $Author: krupenn $
 */
public class CreateMarkerCommandAtomic extends MapActionCommand
{
	/**
	 * созданный элемент метки
	 */
	Marker marker;

	/**
	 * Выбранный фрагмент линии
	 */
	MeasurementPath path;

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
			MeasurementPath path,
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

		AbstractNode node = path.getStartNode();
		path.sortPathElements();
		List nodeLinks = path.getSortedNodeLinks();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mnle);
			MeasurementPathController mpc = (MeasurementPathController )getLogicalNetLayer().getMapViewController().getController(path);

			if(nlc.isMouseOnElement(mnle, point))
			{
				DoublePoint dpoint = logicalNetLayer.convertScreenToMap(point);

				try
				{
					marker = com.syrus.AMFICOM.mapview.Marker.createInstance(
							logicalNetLayer.getUserId(),
							mapView, 
							node,
							mnle.getOtherNode(node),
							mnle,
							path,
							mpc.getMonitoredElement(path).getId(),
							dpoint);

					mapView.addMarker(marker);

					MarkerController mc = (MarkerController )
							getLogicalNetLayer().getMapViewController().getController(marker);

					mc.updateScaleCoefficient(marker);

					mc.notifyMarkerCreated(marker);
				}
				catch (ApplicationException e)
				{
					e.printStackTrace();
				}

				break;
			}
			else
			{
				nlc.updateLengthLt(mnle);
			}

			node = mnle.getOtherNode(node);
		}

		// операция закончена - оповестить слушателей
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					marker,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(marker);
	}
	
}
