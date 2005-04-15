/**
 * $Id: GenerateUnboundLinkCablingCommandBundle.java,v 1.12 2005/04/15 11:12:33 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Command.Action;

import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Controllers.CableController;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.UnboundLink;

/**
 * ������� ��������� ������� �� ������������� �����.
 * @author $Author: peskovsky $
 * @version $Revision: 1.12 $, $Date: 2005/04/15 11:12:33 $
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
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"execute()");

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
			this.path.addLink(this.link, CableController.generateCCI(this.link, this.logicalNetLayer.getUserId()));
			this.link.getBinding().add(this.path);
			this.logicalNetLayer.sendMapEvent(new MapEvent(this, MapEvent.MAP_CHANGED));
		} catch(Throwable e) {
			setException(e);
			setResult(Command.RESULT_NO);
			e.printStackTrace();
		}
	}

}

