/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.16 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Environment;
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
 * ������� �������� ����� �� �����
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class CreateMarkerCommandAtomic extends MapActionCommand
{
	/**
	 * ��������� ������� �����
	 */
	Marker marker;

	/**
	 * ��������� �������� �����
	 */
	MeasurementPath path;

	MapView mapView;

	/**
	 * ��������� �� ������ �����, �� ������� ��������� �����
	 */
	double distance;

	/**
	 * �����, � ������� ��������� ����� �������������� ����
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
		try
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
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
						this.marker,
						MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.marker);
			setResult(Command.RESULT_OK);
		}
		catch(Exception e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}
