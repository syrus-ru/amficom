/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.7 2005/01/30 15:38:17 krupenn Exp $
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
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.Iterator;
import com.syrus.AMFICOM.map.PhysicalLinkBinding;

/**
 *  ������� ��������� ������� �� ������������� �����
 * 
 * 
 * 
 * @version $Revision: 1.7 $, $Date: 2005/01/30 15:38:17 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class GenerateUnboundLinkCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	CablePath path;
	
	/**
	 * ������������� �����
	 */
	UnboundLink unbound;
	
	/**
	 * ��������� �������
	 */
	PhysicalLink link;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;

	Map map;

	public GenerateUnboundLinkCablingCommandBundle(UnboundLink unbound)
	{
		this.unbound = unbound;
		this.path = unbound.getCablePath();
	}
	
	public void execute()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		path.removeLink(unbound);
		link = super.createPhysicalLink(
				unbound.getStartNode(), 
				unbound.getEndNode());
		super.removePhysicalLink(unbound);
		
		// ��������� ��������� ����� � ��������������� �������
		for(Iterator it2 = unbound.getNodeLinks().iterator(); it2.hasNext();)
		{
			NodeLink mnle = (NodeLink)it2.next();
			mnle.setPhysicalLink(link);
			link.addNodeLink(mnle);
		}
		path.addLink(link);
		link.getBinding().add(path);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

