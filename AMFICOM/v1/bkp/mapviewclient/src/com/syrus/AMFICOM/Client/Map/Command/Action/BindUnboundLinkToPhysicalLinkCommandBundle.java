/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.2 2004/10/18 15:33:00 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

/**
 *  ������� ������������ ������������� ����� � �������. �������� ����
 *  ������������� ����� � ������� ������ ���������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/10/18 15:33:00 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindUnboundLinkToPhysicalLinkCommandBundle extends MapActionCommandBundle
{
	/**
	 * ������������� �����
	 */
	MapUnboundLinkElement unbound;
	
	/**
	 * �������
	 */
	MapPhysicalLinkElement link;
	
	/**
	 * �����, �� ������� ������������ ��������
	 */
	Map map;

	public BindUnboundLinkToPhysicalLinkCommandBundle(
		MapUnboundLinkElement unbound, 
		MapPhysicalLinkElement link)
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
		MapCablePathElement cp = unbound.getCablePath();
		cp.removeLink(unbound);
		cp.addLink(link);
		link.getBinding().add(cp);

		logicalNetLayer.repaint();
	}
	
}

