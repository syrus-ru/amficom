package com.syrus.AMFICOM.Client.Resource.Scheme;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class SchemeCableLinkBinding extends LinkedList 
{
	private Map map;

	public SchemeCableLinkBinding(Map map)
	{
		this.map = map;
	}

	public SchemeCableLinkBinding(SchemeCableLinkBinding sclb)
	{
		super(sclb);
		this.map = sclb.getMap();
	}


	public static CableChannelingItem getCCI(
			MapPhysicalLinkElement link, 
			DataSourceInterface dataSource)
	{
		CableChannelingItem cci = new CableChannelingItem(
			dataSource.GetUId(CableChannelingItem.typ));
		cci.startSiteId = link.getStartNode().getId();
		cci.startSpare = MapPropertiesManager.getSpareLength();
		cci.physicalLinkId = link.getId();
		cci.endSpare = MapPropertiesManager.getSpareLength();
		cci.endSiteId = link.getEndNode().getId();
		
		return cci;
	}

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


	public void setMap(Map map)
	{
		this.map = map;
	}


	public Map getMap()
	{
		return map;
	}
}
