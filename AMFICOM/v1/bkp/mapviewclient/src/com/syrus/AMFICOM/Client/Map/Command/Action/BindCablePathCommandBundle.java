/**
 * $Id: BindCablePathCommandBundle.java,v 1.2 2004/09/21 14:59:20 krupenn Exp $
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

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *  ������� �������� �������� ���������� ������ MapNodeElement. �������
 * ������� ��  ������������������ ��������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class BindCablePathCommandBundle extends MapActionCommandBundle
{
	/**
	 * ��������� ����
	 */
	MapCablePathElement path;
	MapNodeProtoElement proto;

	/**
	 * �����, �� ������� ������������ ��������
	 */
	MapView mapView;
	Map map;

	public BindCablePathCommandBundle(MapCablePathElement path, MapNodeProtoElement proto)
	{
		this.path = path;
		this.proto = proto;
	}
	
	public void execute()
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "execute()");

		mapView = logicalNetLayer.getMapView();
		map = mapView.getMap();
		
		List chi = new LinkedList();
		MapSiteNodeElement site = (MapSiteNodeElement )path.getStartNode();
		MapSiteNodeElement linkedsite = checkSite(site);

		for(Iterator it = path.getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

			if(site == link.getEndNode())
				site = (MapSiteNodeElement )link.getStartNode();
			else
				site = (MapSiteNodeElement )link.getEndNode();

			linkedsite = checkSite(site);

			if(link instanceof MapUnboundLinkElement)
			{
				MapUnboundLinkElement un = (MapUnboundLinkElement )link;
				link = createPhysicalLink(un.getStartNode(), un.getEndNode());
				removePhysicalLink(un);
				for(Iterator it2 = un.getNodeLinks().iterator(); it2.hasNext();)
				{
					MapNodeLinkElement mnle = (MapNodeLinkElement )it2.next();
					mnle.setPhysicalLinkId(link.getId());
					link.addNodeLink(mnle);
				}
			}

			chi.add(getCCI(link));
		}

		path.getSchemeCableLink().channelingItems = chi;
	}

	private MapSiteNodeElement checkSite(MapSiteNodeElement site)
	{
		MapSiteNodeElement site2 = site;
		if(site instanceof MapUnboundNodeElement)
		{
			site2 = createSite(site.getAnchor(), proto);
			removeNode(site);
			((MapUnboundNodeElement )site).getSchemeElement().siteId = site2.getId();
			for(Iterator it = path.getLinks().iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
				if(link.getStartNode() == site)
					link.setStartNode(site2);
				if(link.getEndNode() == site)
					link.setEndNode(site2);
			}
		}
		return site2;
	}

	private CableChannelingItem getCCI(MapPhysicalLinkElement link)
	{
		CableChannelingItem cci = new CableChannelingItem(
			path
				.getMapView()
					.getLogicalNetLayer()
						.getContext()
							.getDataSourceInterface()
								.GetUId(CableChannelingItem.typ));
		cci.startSiteId = link.getStartNode().getId();
		cci.startSpare = MapPropertiesManager.getSpareLength();
		cci.physicalLinkId = link.getId();
		cci.endSpare = MapPropertiesManager.getSpareLength();
		cci.endSiteId = link.getEndNode().getId();
		
		path.getSchemeCableLink().channelingItems.add(cci);
		
		return cci;
	}
	
}

