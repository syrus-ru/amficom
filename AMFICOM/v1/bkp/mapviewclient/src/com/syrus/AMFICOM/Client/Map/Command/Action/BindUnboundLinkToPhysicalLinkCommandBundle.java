/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.5 2004/12/24 15:42:11 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 *  ������� ������������ ������������� ����� � �������. �������� ����
 *  ������������� ����� � ������� ������ ���������
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/12/24 15:42:11 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ������������� �����
	 */
	UnboundLink unbound;
	
	/**
	 * �������
	 */
	PhysicalLink link;
	
	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		UnboundLink unbound, 
		PhysicalLink link)
	{
		this.unbound = unbound;
		this.link = link;
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		MapView mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		// ��������� ������������� �����
		super.removeUnboundLink(unbound);
		
		// ����������� ���������� � �������� ���������� ����
		CablePath cp = unbound.getCablePath();
		cp.removeLink(unbound);
		cp.addLink(link);
		link.getBinding().add(cp);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}
	
}

