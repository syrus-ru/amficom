/**
 * $Id: CreateMarkerCommandAtomic.java,v 1.7 2004/12/22 16:38:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.NodeLinkController;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMarker;
import com.syrus.AMFICOM.Client.Resource.MapView.MapMeasurementPathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.MapView.MarkerController;
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

/**
 * ������� �������� ����� �� �����
 * 
 * @version $Revision: 1.7 $, $Date: 2004/12/22 16:38:39 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class CreateMarkerCommandAtomic extends MapActionCommand
{
	/**
	 * ��������� ������� �����
	 */
	MapMarker marker;

	/**
	 * ��������� �������� �����
	 */
	MapMeasurementPathElement path;
	
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

		AbstractNode node = path.getStartNode();
		path.sortPathElements();
		List nodeLinks = path.getSortedNodeLinks();
		for(Iterator it = nodeLinks.iterator(); it.hasNext();)
		{
			NodeLink mnle = (NodeLink)it.next();
				
			NodeLinkController nlc = (NodeLinkController )getLogicalNetLayer().getMapViewController().getController(mnle);

			if(nlc.isMouseOnElement(mnle, point))
			{
				DoublePoint dpoint = logicalNetLayer.convertScreenToMap(point);

				try
				{
					marker = new MapMarker(
							IdentifierPool.getGeneratedIdentifier(ObjectEntities.MARK_ENTITY_CODE),
							mapView, 
							node,
							mnle.getOtherNode(node),
							mnle,
							path,
							dpoint);

					mapView.addMarker(marker);
					
					MarkerController mc = (MarkerController )getLogicalNetLayer().getMapViewController().getController(marker);
	
					mc.updateScaleCoefficient(marker);
					
					mc.notifyMarkerCreated(marker);
				}
				catch (IllegalObjectEntityException e)
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

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					marker,
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(marker);
	}
	
}
