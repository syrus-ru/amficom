/**
 * $Id: BindUnboundNodeToSiteCommandBundle.java,v 1.22 2005/06/22 08:43:46 krupenn Exp $
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
import java.util.List;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.util.Log;

/**
 *  ������� ������������ �������������� �������� � ����.
 * @author $Author: krupenn $
 * @version $Revision: 1.22 $, $Date: 2005/06/22 08:43:46 $
 * @module mapviewclient_v1
 */
public class BindUnboundNodeToSiteCommandBundle extends MapActionCommandBundle
{
	/**
	 * ������������� �������.
	 */
	UnboundNode unbound;
	
	/**
	 * ����.
	 */
	SiteNode site;

	/**
	 * �����, �� ������� ������������ ��������.
	 */
	Map map;

	public BindUnboundNodeToSiteCommandBundle(
			UnboundNode unbound, 
			SiteNode site)
	{
		this.unbound = unbound;
		this.site = site;
	}
	
	public void execute()
	{
		Log.debugMessage(getClass().getName() + "::" + "execute()" + " | " + "method call", Log.FINER);

		try
		{
			MapView mapView = this.logicalNetLayer.getMapView();
			this.map = mapView.getMap();
			// ������ ��������� �����, ���������� ������������� �������
			List cablePaths = mapView.getCablePaths(this.unbound);
			// ����������� �������� ���� ��������� �����
			for(Iterator it = cablePaths.iterator(); it.hasNext();)
			{
				CablePath cp = (CablePath)it.next();
				if(cp.getEndNode().equals(this.unbound))
					cp.setEndNode(this.site);
				if(cp.getStartNode().equals(this.unbound))
					cp.setStartNode(this.site);
			}
			// ������ ��������� �����, ���������� ������������� �������
			List measurementPaths = mapView.getMeasurementPaths(this.unbound);
			// ����������� �������� ���� ��������� �����
			for(Iterator it = measurementPaths.iterator(); it.hasNext();)
			{
				MeasurementPath mp = (MeasurementPath )it.next();
				if(mp.getEndNode().equals(this.unbound))
					mp.setEndNode(this.site);
				if(mp.getStartNode().equals(this.unbound))
					mp.setStartNode(this.site);
			}
			//��� ������������ �������� �������� ���� ����� � ���������� �����
			for(Iterator it = this.map.getNodeLinks(this.unbound).iterator(); it.hasNext();)
			{
				NodeLink nodeLink = (NodeLink)it.next();
				PhysicalLink physicalLink = nodeLink.getPhysicalLink();

				MapElementState pls = nodeLink.getState();
						
				if(nodeLink.getEndNode().equals(this.unbound))
					nodeLink.setEndNode(this.site);
				if(nodeLink.getStartNode().equals(this.unbound))
					nodeLink.setStartNode(this.site);

				super.registerStateChange(nodeLink, pls, nodeLink.getState());
					
				MapElementState pls2 = physicalLink.getState();

				if(physicalLink.getEndNode().equals(this.unbound))
					physicalLink.setEndNode(this.site);
				if(physicalLink.getStartNode().equals(this.unbound))
					physicalLink.setStartNode(this.site);

				super.registerStateChange(physicalLink, pls2, physicalLink.getState());
			}//while(e.hasNext())
			super.removeNode(this.unbound);
			SchemeElement se = this.unbound.getSchemeElement();
			se.setSiteNode(this.site);
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

