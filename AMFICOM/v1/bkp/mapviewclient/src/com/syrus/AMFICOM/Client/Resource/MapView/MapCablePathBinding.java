package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;

import java.util.HashMap;
import java.util.Iterator;

public final class MapCablePathBinding extends HashMap
{
	private MapCablePathElement cablePath;
	
	public MapCablePathBinding(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}

	public MapCablePathBinding(MapCablePathBinding mcpb)
	{
		super(mcpb);
		this.cablePath = mcpb.getCablePath();
	}

	public CableChannelingItem getCCI(MapNodeElement node)
	{
		for(Iterator it = super.values().iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(cci.startSiteId.equals(node.getId()))
				return cci;
		}
		return null;
	}
	
	public CableChannelingItem getCCI(MapPhysicalLinkElement link)
	{
		return (CableChannelingItem )(super.get(link));
	}
	
	public Object get(Object key)
	{
		Object entry = null;
		if(key instanceof MapPhysicalLinkElement)
			entry = super.get(key);
		else
		if(key instanceof MapNodeElement)
			entry = this.getCCI((MapNodeElement )key);
		return entry;
	}

	public static CableChannelingItem generateCCI(
			MapPhysicalLinkElement link, 
			DataSourceInterface dataSource)
	{
		CableChannelingItem cci = new CableChannelingItem(
			dataSource.GetUId(CableChannelingItem.typ));
		cci.startSiteId = link.getStartNode().getId();
		if(! (link instanceof MapUnboundLinkElement))
		{
			cci.startSpare = MapPropertiesManager.getSpareLength();
			cci.physicalLinkId = link.getId();
			cci.endSpare = MapPropertiesManager.getSpareLength();
		}
		cci.endSiteId = link.getEndNode().getId();
		
		return cci;
	}

	public void setCablePath(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}

	public MapCablePathElement getCablePath()
	{
		return cablePath;
	}

/*
	public MapNodeElement getStartUnbound(MapNodeElement pathStart)
	{
		MapNodeElement bufferSite = pathStart;
		
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = map.getMapSiteNodeElement(cci.endSiteId);
		}
		return bufferSite;
	}

	public MapNodeElement getEndUnbound(MapNodeElement pathEnd)
	{
		MapNodeElement bufferSite = pathEnd;
		
		for(ListIterator it = super.listIterator(super.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = map.getMapSiteNodeElement(cci.startSiteId);
		}
		return bufferSite;
	}

	public int indexOfSiteNode(String siteId)
	{
		int i = 0;
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(cci.startSiteId.equals(siteId))
			{
				return i;
			}
			else
				i++;
		}
		return -1;
	}

	public int indexOfPhysicalLink(String physicalLinkId)
	{
		int i = 0;
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(cci.physicalLinkId.equals(physicalLinkId))
			{
				return i;
			}
			else
				i++;
		}
		return -1;
	}

	public MapPhysicalLinkElement getStartLastBoundLink(MapNodeElement pathStart)
	{
		MapNodeElement bufferSite = pathStart;
		MapPhysicalLinkElement link = null;
		
		for(Iterator it = super.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = map.getMapSiteNodeElement(cci.endSiteId);
			link = map.getPhysicalLink(cci.physicalLinkId);
		}
		return link;
	}

	public MapPhysicalLinkElement getEndLastBoundLink(MapNodeElement pathEnd)
	{
		MapNodeElement bufferSite = pathEnd;
		MapPhysicalLinkElement link = null;

		for(ListIterator it = super.listIterator(super.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = map.getMapSiteNodeElement(cci.startSiteId);
			link = map.getPhysicalLink(cci.physicalLinkId);
		}
		return link;
	}
*/

}
