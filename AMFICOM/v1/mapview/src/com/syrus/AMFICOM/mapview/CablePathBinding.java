/**
 * $Id: CablePathBinding.java,v 1.1 2005/02/01 09:28:17 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;

import java.util.HashMap;
import java.util.Iterator;

/**
 * ����� ������ ������ � �������� ������ � ������.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/02/01 09:28:17 $
 * @module mapviewclient_v1
 */
public final class CablePathBinding extends HashMap
{
	/**
	 * ������.
	 */
	private CablePath cablePath;
	
	/**
	 * �����������.
	 * @param cablePath ������
	 */
	public CablePathBinding(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}

	/**
	 * �����������.
	 * @param mcpb ��������
	 */
	public CablePathBinding(CablePathBinding mcpb)
	{
		super(mcpb);
		this.cablePath = mcpb.getCablePath();
	}

	/**
	 * ����� ������ �������� ������, ������������ �� �������� ����.
	 * @param node ����
	 * @return ������ ��������, ��� <code>null</code>, ���� �� ������
	 */
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
	
	/**
	 * ����� ������ �������� ������ � �������� �����.
	 * @param link �����
	 * @return ������ ��������, ��� <code>null</code>, ���� �� ������
	 */
	public CableChannelingItem getCCI(PhysicalLink link)
	{
		return (CableChannelingItem )(super.get(link));
	}
	
	/**
	 * �������� ������ �������� � ��������.
	 * @param key �������
	 * @return ������ ��������, ��� <code>null</code>, ���� �� ������
	 */
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


	/**
	 * ���������� ������.
	 * @param cablePath ������
	 */
	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}

	/**
	 * �������� ������.
	 * @return ������
	 */
	public CablePath getCablePath()
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
