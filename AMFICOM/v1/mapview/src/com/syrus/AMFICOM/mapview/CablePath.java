/**
 * $Id: CablePath.java,v 1.1 2005/02/01 09:28:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.corba.CableChannelingItem;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Элемент кабельного пути. Описывает привязку кабеля к топологическим линиям.
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/02/01 09:28:17 $
 * @module mapviewclient_v1
 */
public class CablePath implements MapElement
{
	private static final long serialVersionUID = 02L;

	protected Identifier id;

	protected String name;

	protected String description;

	protected List characteristics;


	protected transient boolean selected = false;

	protected transient boolean alarmState = false;

	protected transient boolean removed = false;

	protected transient Map map = null;

	private AbstractNode startNode;
	private AbstractNode endNode;

	protected List sortedNodes = new LinkedList();
	protected List sortedNodeLinks = new LinkedList();
	protected boolean nodeLinksSorted = false;

	protected List links = new LinkedList();
	protected boolean linksSorted = false;
	
	protected SchemeCableLink schemeCableLink;

	protected String mapViewId = "";
	
	protected MapView mapView;
	
	protected CablePathBinding binding;

	protected CablePath(
			SchemeCableLink schemeCableLink,
			Identifier id, 
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;

		this.schemeCableLink = schemeCableLink;
		this.name = schemeCableLink.name();

		if(mapView != null)
		{
			map = mapView.getMap();
		}

		this.startNode = stNode;
		this.endNode = eNode;
		nodeLinksSorted = false;

		this.characteristics = new LinkedList();
		
		binding = new CablePathBinding(this);
	}

	public static CablePath createInstance(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
		throws CreateObjectException 
	{
		if (stNode == null || mapView == null || eNode == null || schemeCableLink == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
			return new CablePath(
				schemeCableLink,
				ide,
				stNode, 
				eNode, 
				mapView);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapCablePathElement.createInstance | cannot generate identifier ", e);
		}
		catch (IllegalObjectEntityException e) 
		{
			throw new CreateObjectException("MapCablePathElement.createInstance | cannot generate identifier ", e);
		}
	}

	public AbstractNode getEndNode() 
	{
		return this.endNode;
	}
	
	public void setEndNode(AbstractNode endNode) 
	{
		this.endNode = endNode;
		nodeLinksSorted = false;
	}
	
	public AbstractNode getStartNode() 
	{
		return this.startNode;
	}
	
	public void setStartNode(AbstractNode startNode) 
	{
		this.startNode = startNode;
		nodeLinksSorted = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List getCharacteristics() 
	{
		return Collections.unmodifiableList(schemeCableLink.characteristicsImpl().getValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		schemeCableLink.addCharacteristic(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		schemeCableLink.removeCharacteristic(ch);
	}
	
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return id;
	}

	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return this.description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink )it.next();
			DoublePoint an = link.getLocation();
			x += an.getX();
			y += an.getY();
			count ++;
		}
		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		return point;
	}


	/**
	 * {@inheritDoc}
	 */
	public void setLocation(DoublePoint location)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.getEndNode().equals(node) )
			return getStartNode();
		if ( this.getStartNode().equals(node) )
			return getEndNode();
		return null;
	}

	public void setSchemeCableLink(SchemeCableLink schemeCableLink)
	{
		this.schemeCableLink = schemeCableLink;
		this.setName(schemeCableLink.name());
	}

	public SchemeCableLink getSchemeCableLink()
	{
		return schemeCableLink;
	}

	//Возвращает топологическую длинну в метрах
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getLinks().iterator();
		while( e.hasNext())
		{
			PhysicalLink link = (PhysicalLink)e.next();
			if(! (link instanceof UnboundLink))
				length = length + link.getLengthLt();
		}
		return length;
	}

	public double getLengthLf()
	{
		return schemeCableLink.physicalLength();
	}

	public double getLengthLo()
	{
		return schemeCableLink.opticalLength();
	}

	public void setLengthLf(double len)
	{
		schemeCableLink.physicalLength(len);
	}

	public void setLengthLo(double len)
	{
		schemeCableLink.opticalLength(len);
	}

	/**
	 * возвращает коэффициент топологической привязки
	 */
	public double getKd()
	{
		double phLen = schemeCableLink.physicalLength();
		if(phLen == 0.0D)
			return 1.0;
		double topLen = getLengthLt();
		if(topLen == 0.0D)
			return 1.0;

		double kd = phLen / topLen;
		return kd;
	}

	public List getLinks()
	{	
		return links;
	}

	public void clearLinks()
	{	
		links.clear();
		binding.clear();
	}

//	public void setLinks(List list)
//	{	
//		this.clearLinks();		
//		
//		for(Iterator it = list.iterator(); it.hasNext();)
//		{
//			this.addLink((PhysicalLink)it.next());
//		}
//	}


	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeLink(PhysicalLink link)
	{
		links.remove(link);
//		int index = getBinding().
		getBinding().remove(link);
		nodeLinksSorted = false;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addLink(PhysicalLink addLink, CableChannelingItem cci)
	{
		links.add(addLink);
		binding.put(addLink, cci);
		linksSorted = false;
		nodeLinksSorted = false;
		sortLinks();
	}

	public void sortLinks()
	{
		if(!linksSorted)
		{
			AbstractNode smne = this.getStartNode();
			List origvec = new LinkedList();
			origvec.addAll(getLinks());
			List vec = new LinkedList();
			int count = getLinks().size();
			for (int i = 0; i < count; i++) 
//			while(!smne.equals(this.getEndNode()))
			{
				boolean canSort = false;
				for(Iterator it = origvec.iterator(); it.hasNext();)
				{
					PhysicalLink link = (PhysicalLink)it.next();

					if(link.getStartNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getEndNode();
						CableChannelingItem cci = getBinding().getCCI(link);
						cci.startSiteNodeImpl((SiteNode )link.getStartNode());
						cci.endSiteNodeImpl((SiteNode )link.getEndNode());
						canSort = true;
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getStartNode();
						CableChannelingItem cci = getBinding().getCCI(link);
						cci.startSiteNodeImpl((SiteNode )link.getEndNode());
						cci.endSiteNodeImpl((SiteNode )link.getStartNode());
						canSort = true;
						break;
					}
				}
				if(!canSort)
					// unconsistent - cannot sort
					return;
			}
			this.links = vec;
			linksSorted = true;
			nodeLinksSorted = false;
		}
	}

	public PhysicalLink nextLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(getLinks().size() != 0)
				ret = (PhysicalLink)getLinks().get(0);
		}
		else
		{
			int index = getLinks().indexOf(link);
			if(index != getLinks().size() - 1 && index != -1)
				ret = (PhysicalLink)getLinks().get(index + 1);
		}
		return ret;
	}

	public PhysicalLink previousLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(getLinks().size() != 0)
				ret = (PhysicalLink)getLinks().get(getLinks().size() - 1);
		}
		else
		{
			int index = getLinks().indexOf(link);
			if(index > 0)
				ret = (PhysicalLink)getLinks().get(index - 1);
		}
		return ret;
	}

	public void sortNodes()
	{
		sortNodeLinks();
	}
	
	public java.util.List getSortedNodes()
	{
		if(!nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return sortedNodes;
	}

	public java.util.List getSortedNodeLinks()
	{
		if(!nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return sortedNodeLinks;
	}

	public void sortNodeLinks()
	{
		sortLinks();
//		if(!nodeLinksSorted)
		{
			java.util.List pl = getLinks();

			List vec = new LinkedList();
			List nodevec = new LinkedList();

			AbstractNode node = getStartNode();

			for(Iterator it = pl.iterator(); it.hasNext();)
			{
				PhysicalLink link = (PhysicalLink)it.next();
				
				link.sortNodeLinks();
				
				if(link.getStartNode().equals(node))
				{
					vec.addAll(link.getNodeLinks());
					nodevec.addAll(link.getSortedNodes());
				}
				else
				{
					for(ListIterator lit = link.getNodeLinks().listIterator(link.getNodeLinks().size()); lit.hasPrevious();)
					{
						vec.add(lit.previous());
					}
					for(ListIterator lit = link.getSortedNodes().listIterator(link.getSortedNodes().size()); lit.hasPrevious();)
					{
						nodevec.add(lit.previous());
					}
				}
				node = link.getOtherNode(node);

				// to avoid duplicate entry
				nodevec.remove(node);
			}
			// add last node
			nodevec.add(getEndNode());
				
			this.sortedNodeLinks = vec;
			this.sortedNodes = nodevec;
			nodeLinksSorted = true;
		}
	}

	public IntPoint getBindingPosition(PhysicalLink link)
	{
		CableChannelingItem cci = (CableChannelingItem )getBinding().get(link);
		return new IntPoint(cci.rowX(), cci.placeY());
	}

	public void setBinding(CablePathBinding binding)
	{
		this.binding = binding;
	}

	public CablePathBinding getBinding()
	{
		return binding;
	}

	public AbstractNode getStartUnboundNode()
	{
		AbstractNode bufferSite = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	public AbstractNode getEndUnboundNode()
	{
		AbstractNode bufferSite = getEndNode();
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link = (PhysicalLink)it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	public PhysicalLink getStartLastBoundLink()
	{
		PhysicalLink link = null;

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			PhysicalLink link2 = (PhysicalLink)it.next();
			if(link2 instanceof UnboundLink)
				break;
			link = link2;
		}
		
		return link;
	}

	public PhysicalLink getEndLastBoundLink()
	{
		PhysicalLink link = null;

		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link2 = (PhysicalLink)it.previous();
			if(link2 instanceof UnboundLink)
				break;
			link = link2;
		}
		
		return link;
	}

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
