/**
 * $Id: CablePathBinding.java,v 1.1 2005/02/01 09:28:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Класс хранит данные о привязке кабеля к линиям.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/02/01 09:28:17 $
 * @module mapviewclient_v1
 */
public final class CablePathBinding extends HashMap
{
	/**
	 * кабель.
	 */
	private CablePath cablePath;
	
	/**
	 * Конструктор.
	 * @param cablePath кабель
	 */
	public CablePathBinding(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}

	/**
	 * Конструктор.
	 * @param mcpb привязка
	 */
	public CablePathBinding(CablePathBinding mcpb)
	{
		super(mcpb);
		this.cablePath = mcpb.getCablePath();
	}

	/**
	 * Найти объект привязки кабеля, начинающийся на заданном узле.
	 * @param node узел
	 * @return объект привязки, или <code>null</code>, если не найден
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
	 * Найти объект привязки кабеля к заданной линии.
	 * @param link линия
	 * @return объект привязки, или <code>null</code>, если не найден
	 */
	public CableChannelingItem getCCI(PhysicalLink link)
	{
		return (CableChannelingItem )(super.get(link));
	}
	
	/**
	 * Получить объект привязки к элементу.
	 * @param key элемент
	 * @return объект привязки, или <code>null</code>, если не найден
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
	 * Установить кабель.
	 * @param cablePath кабель
	 */
	public void setCablePath(CablePath cablePath)
	{
		this.cablePath = cablePath;
	}

	/**
	 * Получить кабель.
	 * @return кабель
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
