/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.4 2004/10/20 10:14:39 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.util.Iterator;

/**
 *  ������� ��������� ������� �� ������������� �����
 * 
 * 
 * 
 * @version $Revision: 1.4 $, $Date: 2004/10/20 10:14:39 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class GenerateUnboundLinkCablingCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	MapCablePathElement path;
	
	/**
	 * ������������� �����
	 */
	MapUnboundLinkElement unbound;
	
	/**
	 * ��������� �������
	 */
	MapPhysicalLinkElement link;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;

	Map map;

	public GenerateUnboundLinkCablingCommandBundle(MapUnboundLinkElement unbound)
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
			MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
			mnle.setPhysicalLinkId(link.getId());
			link.addNodeLink(mnle);
		}
		path.addLink(link);
		link.getBinding().add(path);

		logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
	}

}

