/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.14 2005/02/08 15:11:09 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkerController;
import com.syrus.AMFICOM.Client.Map.Controllers.MeasurementPathController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;

/**
 * Команда создания метки на линии
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.14 $, $Date: 2005/02/08 15:11:09 $
 * @module mapviewclient_v1
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

		this.mapView = this.logicalNetLayer.getMapView();

		AbstractNode node = this.path.getStartNode();
		this.path.sortPathElements();
		List nodeLinks = this.path.getSortedNodeLinks();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();

			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mnle);
			MeasurementPathController mpc = (MeasurementPathController )getLogicalNetLayer().getMapViewController().getController(this.path);

			if(nlc.isMouseOnElement(mnle, this.point))
			{
				DoublePoint dpoint = this.logicalNetLayer.convertScreenToMap(this.point);

				try
				{
					this.marker = Marker.createInstance(
							this.logicalNetLayer.getUserId(),
							this.mapView, 
							node,
							mnle.getOtherNode(node),
							mnle,
							this.path,
							mpc.getMonitoredElement(this.path).getId(),
							dpoint);

					this.mapView.addMarker(this.marker);

					MarkerController mc = (MarkerController )
							getLogicalNetLayer().getMapViewController().getController(this.marker);

					mc.updateScaleCoefficient(this.marker);

					mc.notifyMarkerCreated(this.marker);
				}
				catch (ApplicationException e)
				{
					e.printStackTrace();
				}

				break;
			}
			nlc.updateLengthLt(mnle);

			node = mnle.getOtherNode(node);
		}

		// операция закончена - оповестить слушателей
		this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.marker,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		this.logicalNetLayer.setCurrentMapElement(this.marker);
	}
	
}
