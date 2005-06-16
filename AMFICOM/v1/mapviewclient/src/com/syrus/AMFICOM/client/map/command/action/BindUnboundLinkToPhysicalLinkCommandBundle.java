/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.16 2005/06/16 10:57:19 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 *  ������� ������������ ������������� ����� � �������. �������� ����
 *  ������������� ����� � ������� ������ ���������
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.16 $, $Date: 2005/06/16 10:57:19 $
 * @module mapviewclient_v1 
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

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// ��������� ������������� �����
			super.removeUnboundLink(this.unbound);
			// ����������� ���������� � �������� ���������� ����
			CablePath cablePath = this.unbound.getCablePath();
			cablePath.removeLink(this.unbound);
			cablePath.addLink(this.link, CableController.generateCCI(cablePath, this.link, LoginManager.getUserId()));
			this.link.getBinding().add(cablePath);
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		}
		catch(Throwable e)
		{
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}
	
}

