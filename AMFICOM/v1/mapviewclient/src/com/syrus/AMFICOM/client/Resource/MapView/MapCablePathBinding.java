package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItemDefaultFactory;

import java.util.HashMap;
import java.util.Iterator;

public final class MapCablePathBinding extends HashMap
{
	private MapCablePathElement cablePath;
	
	private static CableChannelingItemDefaultFactory cciFactory = new CableChannelingItemDefaultFactory();
	
	public MapCablePathBinding(MapCablePathElement cablePath)
	{
		this.cablePath = cablePath;
	}

	public MapCablePathBinding(MapCablePathBinding mcpb)
	{
		super(mcpb);
		this.cablePath = mcpb.getCablePath();
	}

	public CableChannelingItem getCCI(AbstractNode node)
	{
		for(Iterator it = super.values().iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(cci.startSiteNodeImpl().equals(node))
				return cci;
		}
		return null;
	}
	
	public CableChannelingItem getCCI(PhysicalLink link)
	{
		return (CableChannelingItem )(super.get(link));
	}
	
	public Object get(Object key)
	{
		Object entry = null;
		if(key instanceof PhysicalLink)
			entry = super.get(key);
		else
		if(key instanceof AbstractNode)
			entry = this.getCCI((AbstractNode)key);
		return entry;
	}

	public static CableChannelingItem generateCCI(PhysicalLink link)
	{
		CableChannelingItem cci = cciFactory.newInstance();
		cci.startSiteNodeImpl((SiteNode )link.getStartNode());
		if(! (link instanceof MapUnboundLinkElement))
		{
			cci.startSpare(MapPropertiesManager.getSpareLength());
			cci.physicalLinkImpl(link);
			cci.endSpare(MapPropertiesManager.getSpareLength());
		}
		cci.endSiteNodeImpl((SiteNode )link.getEndNode());
		
		try
		{
			SchemeStorableObjectPool.putStorableObject(cci);
		}
		catch (IllegalObjectEntityException e)
		{
			e.printStackTrace();
			return null;
		}

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
