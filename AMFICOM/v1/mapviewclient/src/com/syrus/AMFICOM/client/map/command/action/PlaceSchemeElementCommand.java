/**
 * $Id: PlaceSchemeElementCommand.java,v 1.25 2005/07/20 17:55:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapApplicationModel;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 * ���������� c[tvysq ������� �� ����� � ������������ � ���������
 * ��� �� �����������
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.25 $, $Date: 2005/07/20 17:55:47 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		if ( !this.aContext.getApplicationModel()
				.isEnabled(MapApplicationModel.ACTION_EDIT_BINDING))
			return;
		
		try {
			// ���� �������������� ����� �� ������, �������� �� �� �������� �����
			if(this.coordinatePoint == null)
				this.coordinatePoint = this.logicalNetLayer.getConverter().convertScreenToMap(this.point);
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			this.site = mapView.findElement(this.schemeElement);
			if(this.site == null)
			{
				MapElement mapElement = this.logicalNetLayer.getMapElementAtPoint(this.point, this.netMapViewer.getVisibleBounds());
				
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
			this.logicalNetLayer.setCurrentMapElement(this.site);
			this.logicalNetLayer.notifySchemeEvent(this.site);
		} catch(Throwable e) {
			setResult(Command.RESULT_NO);
			setException(e);
			e.printStackTrace();
		}

	}
}
