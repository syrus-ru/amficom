/**
 * $Id: BindUnboundLinkToPhysicalLinkCommandBundle.java,v 1.19 2005/07/24 12:41:05 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;

/**
 *  ������� ������������ ������������� ����� � �������. �������� ����
 *  ������������� ����� � ������� ������ ���������
 * 
 * 
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.19 $, $Date: 2005/07/24 12:41:05 $
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Level.FINER);

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// ��������� ������������� �����
			super.removeUnboundLink(this.unbound);
			// ����������� ���������� � �������� ���������� ����
			CablePath cablePath = this.unbound.getCablePath();

			CableChannelingItem cableChannelingItem = cablePath.getFirstCCI(this.unbound);
			CableChannelingItem newCableChannelingItem = CableController.generateCCI(cablePath, this.link);
			newCableChannelingItem.insertSelfBefore(cableChannelingItem);
			cableChannelingItem.setParentPathOwner(null, false);
			cablePath.removeLink(cableChannelingItem);
			cablePath.addLink(this.link, newCableChannelingItem);

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

