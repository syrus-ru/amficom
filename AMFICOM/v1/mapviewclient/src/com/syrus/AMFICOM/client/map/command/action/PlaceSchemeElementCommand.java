/**
 * $Id: PlaceSchemeElementCommand.java,v 1.16 2005/05/27 15:14:55 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.awt.Point;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.MapNavigateEvent;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapApplicationModel;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;

/**
 * ���������� c[tvysq ������� �� ����� � ������������ � ���������
 * ��� �� �����������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/05/27 15:14:55 $
 * @module mapviewclient_v1
 */
public class PlaceSchemeElementCommand extends MapActionCommandBundle
{
	/**
	 * ���������� ����
	 */
	SiteNode site = null;
	
	/**
	 * ��������� ������������� �������
	 */
	UnboundNode unbound = null;
	
	/**
	 * ����������� ������� �������
	 */
	SchemeElement schemeElement = null;
	
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
			SchemeElement schemeElement,
			DoublePoint dpoint)
	{
		this.schemeElement = schemeElement;
		this.coordinatePoint = dpoint;
	}


	public PlaceSchemeElementCommand(
			SchemeElement se,
			Point point)
	{
		this.schemeElement = se;
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
		
		try {
			// ���� �������������� ����� �� ������, �������� �� �� �������� �����
			if(this.coordinatePoint == null)
				this.coordinatePoint = this.logicalNetLayer.convertScreenToMap(this.point);
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			this.site = mapView.findElement(this.schemeElement);
			if(this.site == null)
			{
				MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(this.point);
				
				if(mapElement instanceof SiteNode
					&& !(mapElement instanceof UnboundNode))
				{
					this.site = (SiteNode )mapElement;
					this.schemeElement.setSiteNode(this.site);
				}
				else
				{
					this.unbound = super.createUnboundNode(this.coordinatePoint, this.schemeElement);
					this.site = this.unbound;
				}
				
				this.logicalNetLayer.getMapViewController().scanCables(this.schemeElement.getParentScheme());
			}
			// �������� ��������� - ���������� ����������
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
			this.logicalNetLayer.sendMapEvent(new MapNavigateEvent(
					this.site, 
					MapNavigateEvent.MAP_ELEMENT_SELECTED_EVENT));
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}

	}
}
