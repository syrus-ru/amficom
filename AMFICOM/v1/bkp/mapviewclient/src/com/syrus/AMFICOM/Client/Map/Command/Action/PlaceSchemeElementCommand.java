/**
 * $Id: PlaceSchemeElementCommand.java,v 1.6 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * ���������� c[tvysq ������� �� ����� � ������������ � ���������
 * ��� �� �����������
 * 
 * @version $Revision: 1.6 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class PlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * ���������� ����
	 */
	MapSiteNodeElement site = null;
	
	/**
	 * ��������� ������������� �������
	 */
	MapUnboundNodeElement unbound = null;
	
	/**
	 * ����������� ������� �������
	 */
	SchemeElement se = null;
	
	Map map;
	
	/**
	 * �������� �����, � ������� ����������� �������
	 */
	Point point = null;

	/**
	 * �������������� �����, � ������� ����������� �������
	 */
	DoublePoint coordinatePoint = null;

	public PlaceSchemeElementCommand(
			SchemeElement se,
			DoublePoint dpoint)
	{
		this.se = se;
		this.coordinatePoint = dpoint;
	}


	public PlaceSchemeElementCommand(
			SchemeElement se,
			Point point)
	{
		this.se = se;
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
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		// ���� �������������� ����� �� ������, �������� �� �� �������� �����
		if(coordinatePoint == null)
			coordinatePoint = logicalNetLayer.convertScreenToMap(point);
		
		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();

		site = mapView.findElement(se);
		if(site == null)
		{
			MapElement me = logicalNetLayer.getMapElementAtPoint(point);
			
			if(me instanceof MapSiteNodeElement
				&& !(me instanceof MapUnboundNodeElement))
			{
				site = (MapSiteNodeElement )me;
				se.siteId = site.getId();
			}
			else
			{
				unbound = super.createUnboundNode(coordinatePoint, se);
				site = unbound;
			}
			
			mapView.scanCables((Scheme )Pool.get(Scheme.typ, se.getSchemeId()));
		}

		// �������� ��������� - ���������� ����������
		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		logicalNetLayer.sendMapEvent(new MapNavigateEvent(
				site, 
				MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
		logicalNetLayer.setCurrentMapElement(site);
		logicalNetLayer.notifySchemeEvent(site);

	}
}
