/**
 * $Id: CablePath.java,v 1.11 2005/04/01 13:08:48 bob Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * ������� ���������� ����. ��������� �������� ������ � �������������� ������.
 * @author $Author: bob $
 * @version $Revision: 1.11 $, $Date: 2005/04/01 13:08:48 $
 * @module mapviewclient_v1
 */
public class CablePath implements MapElement
{
	/**
	 * �������������.
	 */
	protected Identifier id;

	/**
	 * ��������.
	 */
	protected String name;

	/**
	 * ��������.
	 */
	protected String description;

	/**
	 * ���� ���������.
	 */
	protected transient boolean selected = false;

	/**
	 * ���� ������� ������� �������.
	 */
	protected transient boolean alarmState = false;

	/**
	 * ���� ��������.
	 */
	protected transient boolean removed = false;

	/**
	 * �������������� �����.
	 */
	protected transient Map map = null;

	/**
	 * ���� �����, � �������� �������� ��������� ���� ������.
	 */
	private AbstractNode startNode;

	/**
	 * ���� �����, � �������� �������� �������� ���� ������.
	 */
	private AbstractNode endNode;

	/**
	 * ������������� ������ �����, �� ������� �������� ������.
	 */
	protected SortedSet sortedNodes = new TreeSet();
	/**
	 * ������������� ������ ���������� �����, �� ������� �������� ������.
	 */
	protected SortedSet sortedNodeLinks = new TreeSet();
	/**
	 * ���� ���������� ����������.
	 */
	protected boolean nodeLinksSorted = false;

	/**
	 * ������ �����, �� ������� �������� ������.
	 */
	protected SortedSet links = new TreeSet();
	/**
	 * ���� ���������� �����.
	 */
	protected boolean linksSorted = false;
	
	/**
	 * ������� ������.
	 */
	protected SchemeCableLink schemeCableLink;

	/**
	 * ���.
	 */
	protected MapView mapView;
	
	/**
	 * ������ �������� ������ � ������.
	 */
	protected CablePathBinding binding;

	protected static final SortedSet EMPTY_SORTED_SET = new TreeSet();
	
	/**
	 * �����������.
	 * @param schemeCableLink ������� ������
	 * @param id �������������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param mapView ���
	 */
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
		this.name = schemeCableLink.getName();

		if(mapView != null)
		{
			this.map = mapView.getMap();
		}

		this.startNode = stNode;
		this.endNode = eNode;
		this.nodeLinksSorted = false;

		this.binding = new CablePathBinding(this);
	}

	/**
	 * ������� ����� ������� ������.
	 * @param schemeCableLink ������� ������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @param mapView ���
	 * @return ����� �������������� ������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException ��� �������������
	 * �������� �������.
	 */
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

	/**
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() 
	{
		return this.endNode;
	}
	
	/**
	 * Set {@link #endNode}.
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode) 
	{
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}
	
	/**
	 * Get {@link #startNode}.
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() 
	{
		return this.startNode;
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode) 
	{
		this.startNode = startNode;
		this.nodeLinksSorted = false;
	}
	
	/**
	 *
	 * {@inheritDoc}
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristics()
	 */
	public Set getCharacteristics() 
	{
		return this.schemeCableLink.getCharacteristics();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		this.schemeCableLink.addCharacteristic(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		this.schemeCableLink.removeCharacteristic(ch);
	}
	
	/**
	 * Set id.
	 * @param id id
	 */
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.id;
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getName() 
	{
		return this.name;
	}

	/**
	 * ���������� ��������.
	 * @param name ����� ��������
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getDescription() 
	{
		return this.description;
	}

	/**
	 * ���������� ��������.
	 * @param description ����� ��������
	 */
	public void setDescription(String description) 
	{
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return this.map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState()
	{
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		int count = 0;
		DoublePoint point = new DoublePoint(0.0, 0.0);
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = this.getLinks().iterator(); it.hasNext();)
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
	 * Suppress since this class is transient 
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
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	/**
	 * ���������� ��� �����.
	 * @param mapView ��� �����
	 */
	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	/**
	 * �������� ��� �����.
	 * @return ��� �����
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * �������� ���� �� ������ ����� ��������������� ������.
	 * @param node �������� ����
	 * @return ������ �������� ����
	 */
	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.getEndNode().equals(node) )
			return this.getStartNode();
		if ( this.getStartNode().equals(node) )
			return this.getEndNode();
		return null;
	}

	/**
	 * ���������� ������� ������.
	 * @param schemeCableLink ������� ������
	 */
	public void setSchemeCableLink(SchemeCableLink schemeCableLink)
	{
		this.schemeCableLink = schemeCableLink;
		this.setName(schemeCableLink.getName());
	}

	/**
	 * �������� ������� ������.
	 * @return ������� ������
	 */
	public SchemeCableLink getSchemeCableLink()
	{
		return this.schemeCableLink;
	}

	/**
	 * ���������� �������������� ����� � ������.
	 * @return �������������� �����
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = this.getLinks().iterator();
		while( e.hasNext())
		{
			PhysicalLink link = (PhysicalLink)e.next();
			if(! (link instanceof UnboundLink))
				length = length + link.getLengthLt();
		}
		return length;
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLf()
	{
		return this.schemeCableLink.getPhysicalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLo()
	{
		return this.schemeCableLink.getOpticalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @param len ���������� �����
	 */
	public void setLengthLf(double len)
	{
		this.schemeCableLink.setPhysicalLength(len);
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @param len ���������� �����
	 */
	public void setLengthLo(double len)
	{
		this.schemeCableLink.setOpticalLength(len);
	}

	/**
	 * ���������� ����������� �������������� ��������. ����������� ���������
	 * �������������� ����� � ����������.
	 * @return ����������� �������������� ��������
	 */
	public double getKd()
	{
		double phLen = this.schemeCableLink.getPhysicalLength();
		if(phLen == 0.0D)
			return 1.0;
		double topLen = this.getLengthLt();
		if(topLen == 0.0D)
			return 1.0;

		double kd = phLen / topLen;
		return kd;
	}

	/**
	 * �������� ������ �����, �� ������� �������� ������.
	 * @return ������ �����
	 */
	public SortedSet getLinks()
	{	
		return Collections.unmodifiableSortedSet(this.links);
	}

	/**
	 * �������� ������ �����, �� ������� �������� ������.
	 */
	public void clearLinks()
	{	
		this.links.clear();
		this.binding.clear();
	}

	/**
	 * ������ ����� �� ������.
	 * <br>��������! �������� ����� ����� �� �����������.
	 * @param link �����
	 */
	public void removeLink(PhysicalLink link)
	{
		this.links.remove(link);
		this.getBinding().remove(link);
		this.nodeLinksSorted = false;
	}

	/**
	 * �������� ����� � ������.
	 * <br>��������! �������� ����� ����� �� �����������
	 * @param addLink ����������� �����
	 * @param cci ������, ����������� �������� ������ � �����
	 */
	public void addLink(PhysicalLink addLink, CableChannelingItem cci)
	{
		this.links.add(addLink);
		this.binding.put(addLink, cci);
		this.linksSorted = false;
		this.nodeLinksSorted = false;
		this.sortLinks();
	}

	/**
	 * ����������� ������ �����, �� ������� �������� ������, ������� � 
	 * ���������� ���� ������ �� ���������.
	 */
	public void sortLinks()
	{
		if(!this.linksSorted)
		{
			AbstractNode smne = this.getStartNode();
			List origList = new LinkedList();
			origList.addAll(this.getLinks());
			List list = new LinkedList();
			int count = this.getLinks().size();
			for (int i = 0; i < count; i++) 
//			while(!smne.equals(this.getEndNode()))
			{
				boolean canSort = false;
				for(Iterator it = origList.iterator(); it.hasNext();)
				{
					PhysicalLink link = (PhysicalLink)it.next();

					if(link.getStartNode().equals(smne))
					{
						list.add(link);
						it.remove();
						smne = link.getEndNode();
						CableChannelingItem cci = this.getBinding().getCCI(link);
						cci.setStartSiteNode((SiteNode )link.getStartNode());
						cci.setEndSiteNode((SiteNode )link.getEndNode());
						canSort = true;
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						list.add(link);
						it.remove();
						smne = link.getStartNode();
						CableChannelingItem cci = this.getBinding().getCCI(link);
						cci.setStartSiteNode((SiteNode )link.getEndNode());
						cci.setEndSiteNode((SiteNode )link.getStartNode());
						canSort = true;
						break;
					}
				}
				if(!canSort)
					// unconsistent - cannot sort
					return;
			}
			this.links.clear();
			this.links.addAll(list);
			this.linksSorted = true;
			this.nodeLinksSorted = false;
		}
	}

	/**
	 * �������� ��������� ����� �� ������� ������������� �����.
	 * @param physicalLink �����
	 * @return ��������� �����, ��� <code>null</code>, ���� link - ��������� 
	 * � ������
	 */
	public PhysicalLink nextLink(PhysicalLink physicalLink)
	{
		final SortedSet physicalLinksTailSet  = this.links.tailSet(physicalLink);
		if (physicalLinksTailSet.size() == 1)
			return null;
		final Iterator physicalLinksIterator = physicalLinksTailSet.iterator();
		physicalLinksIterator.next();
		return (PhysicalLink) physicalLinksIterator.next();
//		PhysicalLink ret = null;
//		if(link == null)
//		{
//			if(this.getLinks().size() != 0)
//				ret = (PhysicalLink)this.getLinks().get(0);
//		}
//		else
//		{
//			int index = this.getLinks().indexOf(link);
//			if(index != this.getLinks().size() - 1 && index != -1)
//				ret = (PhysicalLink)this.getLinks().get(index + 1);
//		}
//		return ret;
	}

	/**
	 * �������� ���������� ����� �� ������� ������������� �����.
	 * @param physicalLink �����
	 * @return ���������� �����, ��� <code>null</code>, ���� link - ������ 
	 * � ������
	 */
	public PhysicalLink previousLink(PhysicalLink physicalLink)
	{
		final SortedSet physicalLinksHeadSet = this.links.headSet(physicalLink); 
		return physicalLinksHeadSet.isEmpty() ? null : (PhysicalLink) physicalLinksHeadSet.last();
//		PhysicalLink ret = null;
//		if(link == null)
//		{
//			if(this.getLinks().size() != 0)
//				ret = (PhysicalLink)this.getLinks().get(this.getLinks().size() - 1);
//		}
//		else
//		{
//			int index = this.getLinks().indexOf(link);
//			if(index > 0)
//				ret = (PhysicalLink)this.getLinks().get(index - 1);
//		}
//		return ret;
	}

	/**
	 * ����������� ����, �������� � ������ ������, ������� �� ���������� ����.
	 * ���������� ����� ���������� ������� � ����������� ����������
	 */
	public void sortNodes()
	{
		this.sortNodeLinks();
	}
	
	/**
	 * �������� ������ ��������������� �����, �������� � ������ ������, �������
	 * �� ���������� ���� �����. ���������� ����� ������ ������������� ���� 
	 * ({@link #sortNodes()}) �� ������ ���� �������
	 * @return ������ ��������������� �����, ��� <code>Collections.EMPTY_LIST</code>, ����
	 * ���� �� �������������
	 */
	public SortedSet getSortedNodes()
	{
		if(!this.nodeLinksSorted)
			return EMPTY_SORTED_SET;
		return Collections.unmodifiableSortedSet(this.sortedNodes);
	}

	/**
	 * �������� ������ ��������������� ���������� �����, �������� � ������ 
	 * ������, ������� �� ���������� ���� �����. ���������� ����� ������ 
	 * ������������� ���� ({@link #sortNodes()}) �� ������ ���� �������
	 * @return ������ ��������������� ���������� �����, ��� 
	 * <code>Collections.EMPTY_LIST</code>, ���� ���� �� �������������
	 */
	public SortedSet getSortedNodeLinks()
	{
		if(!this.nodeLinksSorted)
			return EMPTY_SORTED_SET;
		return Collections.unmodifiableSortedSet(this.sortedNodeLinks);
	}

	/**
	 * ����������� ��������� ����� �� ������� ������� �� ���������� ����.
	 * ��� ���������� ���������� ����������� ����� ����
	 */
	public void sortNodeLinks() {
		this.sortLinks();
		// if(!nodeLinksSorted)
		{
			List list = new LinkedList();
			List nodeList = new LinkedList();

			AbstractNode node = getStartNode();

			for (Iterator it = this.links.iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();

				link.sortNodeLinks();

				if (link.getStartNode().equals(node)) {
					list.addAll(link.getNodeLinks());
					nodeList.addAll(link.getSortedNodes());
				} else {
					SortedSet nodeLinks = link.getNodeLinks();
					List nodeLinksList = new ArrayList(nodeLinks);
					for (ListIterator listIterator = nodeLinksList.listIterator(nodeLinksList.size()); listIterator
							.hasPrevious();) {
						list.add(listIterator.previous());
					}
					List sortedNodes2 = link.getSortedNodes();
					for (ListIterator listIterator = sortedNodes2.listIterator(sortedNodes2.size()); listIterator
							.hasPrevious();) {
						nodeList.add(listIterator.previous());
					}
				}
				node = link.getOtherNode(node);

				// to avoid duplicate entry
				nodeList.remove(node);
			}
			// add last node
			nodeList.add(getEndNode());

			this.sortedNodeLinks.clear();
			this.sortedNodeLinks.addAll(list);
			this.sortedNodes.clear();
			this.sortedNodes.addAll(nodeList);
			this.nodeLinksSorted = true;
		}
	}

	/**
	 * �������� ���������� ��������� ������� �� ����� <code>link</code>.
	 * 
	 * @param link
	 *            �����
	 * @return ���������� ��������� � �������
	 */
	public IntPoint getBindingPosition(PhysicalLink link)
	{
		CableChannelingItem cci = (CableChannelingItem )getBinding().get(link);
		return new IntPoint(cci.getRowX(), cci.getPlaceY());
	}

	/**
	 * ���������� ������, �������� �������� ������ � ������.
	 * @param binding ��������
	 */
	public void setBinding(CablePathBinding binding)
	{
		this.binding = binding;
	}

	/**
	 * �������� ������, �������� �������� ������ � ������.
	 * @return ��������
	 */
	public CablePathBinding getBinding()
	{
		return this.binding;
	}

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ������ � ������ ����
	 * ������ �� ������� ������������� �����.
	 * @return ����
	 */
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
	
	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ������ � ����� ����
	 * ��������� �� ������� ������������� �����.
	 * @return ����
	 */
	public AbstractNode getEndUnboundNode()
	{
		AbstractNode bufferSite = getEndNode();
		/*/
		List list = new ArrayList(this.getLinks());
		for(ListIterator it = list.listIterator(list.size()); it.hasPrevious();)
		{
			PhysicalLink link = (PhysicalLink)it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		/*/
		UnboundLink unboundLink = null;
		for(Iterator it=this.getLinks().iterator();it.hasNext();) {
			PhysicalLink link = (PhysicalLink)it.next();
			if(link instanceof UnboundLink)
				unboundLink = (UnboundLink)link;
		}
		if (unboundLink != null)
			bufferSite = unboundLink.getOtherNode(bufferSite);
		//*/
		return bufferSite;

	}
	
	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ��������� � ������
	 * ����������� �����.
	 * @return ����������� �����
	 */
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

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ��������� � �����
	 * ����������� �����.
	 * @return ����������� �����
	 */
	public PhysicalLink getEndLastBoundLink()
	{
		
		PhysicalLink link = null;
		/*/
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link2 = (PhysicalLink)it.previous();
			if(link2 instanceof UnboundLink)
				break;
			link = link2;
		}
		/*/		
		for(Iterator it = getLinks().iterator(); it.hasNext();) {
			PhysicalLink link2 = (PhysicalLink)it.next();
			if(!(link2 instanceof UnboundLink))
				link = link2;
		}
		//*/
		return link;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(Set characteristics) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Characterizable#getCharacteristicSort()
	 */
	public CharacteristicSort getCharacteristicSort() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		throw new UnsupportedOperationException();
	}
}
