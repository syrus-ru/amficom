/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.18 2005/06/22 08:43:47 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.util.Iterator;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.controllers.CableController;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.util.Log;

/**
 * ������� ��������� ������� �� ������������� �����.
 * @author $Author: krupenn $
 * @version $Revision: 1.18 $, $Date: 2005/06/22 08:43:47 $
 * @module mapviewclient_v1
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
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		this.mapView = this.logicalNetLayer.getMapView();
		this.map = this.mapView.getMap();
		
		try {
			this.path.removeLink(this.unbound);
			this.link = super.createPhysicalLink(
					this.unbound.getStartNode(), 
					this.unbound.getEndNode());
			super.removePhysicalLink(this.unbound);
			// ��������� ��������� ����� � ��������������� �������
			for(Iterator it2 = this.unbound.getNodeLinks().iterator(); it2.hasNext();)
			{
				NodeLink mnle = (NodeLink)it2.next();
				mnle.setPhysicalLink(this.link);
				this.link.addNodeLink(mnle);
			}
			this.path.addLink(this.link, CableController.generateCCI(this.path, this.link, LoginManager.getUserId()));
			this.link.getBinding().add(this.path);
			this.logicalNetLayer.sendMapEvent(MapEvent.MAP_CHANGED);
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}

