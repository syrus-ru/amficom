/**
 * $Id: CablePath.java,v 1.21 2005/07/12 13:55:31 bass Exp $
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
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.IntPoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;

/**
 * ������� ���������� ����. ��������� �������� ������ � �������������� ������.
 * @author $Author: bass $
 * @version $Revision: 1.21 $, $Date: 2005/07/12 13:55:31 $
 * @module mapviewclient_v1
 */
public final class CablePath implements MapElement
{
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
	 * ���� �����, � �������� �������� ��������� ���� ������.
	 */
	private transient AbstractNode startNode = null;

	/**
	 * ���� �����, � �������� �������� �������� ���� ������.
	 */
	private transient AbstractNode endNode = null;

	/**
	 * ������������� ������ �����, �� ������� �������� ������.
	 */
	protected transient List sortedNodes = new LinkedList();
	/**
	 * ������������� ������ ���������� �����, �� ������� �������� ������.
	 */
	protected transient List sortedNodeLinks = new LinkedList();
	/**
	 * ���� ���������� ����������.
	 */
	protected transient boolean nodeLinksSorted = false;

	/**
	 * ������ �����, �� ������� �������� ������.
	 */
	protected transient List links = new LinkedList();
	/**
	 * ���� ���������� �����.
	 */
	protected transient boolean linksSorted = false;
	
	/**
	 * ������� ������.
	 */
	protected transient SchemeCableLink schemeCableLink = null;

	/**
	 * ������ �������� ������ � ������.
	 */
	protected transient CablePathBinding binding = null;

	protected static final List EMPTY_SORTED_LIST = new LinkedList();
	
	/**
	 * �����������.
	 * @param schemeCableLink ������� ������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 */
	protected CablePath(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode)
	{
		this.schemeCableLink = schemeCableLink;

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
	 * @return ����� �������������� ������
	 * �������� �������.
	 */
	public static CablePath createInstance(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode)
	{
		if (stNode == null || eNode == null || schemeCableLink == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new CablePath(
			schemeCableLink,
			stNode,
			eNode);
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
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.schemeCableLink.getId();
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getName()
	{
		return this.schemeCableLink.getName();
	}

	/**
	 * ���������� ��������.
	 * @param name ����� ��������
	 */
	public void setName(String name)
	{
		this.schemeCableLink.setName(name);
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getDescription()
	{
		return this.schemeCableLink.getDescription();
	}

	/**
	 * ���������� ��������.
	 * @param description ����� ��������
	 */
	public void setDescription(String description)
	{
		this.schemeCableLink.setDescription(description);
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
			count++;
		}
		x /= count;
		y /= count;
		
		point.setLocation(x, y);
		
		return point;
	}


	/**
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
	 * �������� ���� �� ������ ����� ��������������� ������.
	 * @param node �������� ����
	 * @return ������ �������� ����
	 */
	public AbstractNode getOtherNode(AbstractNode node)
	{
		if ( this.endNode.equals(node) )
			return this.startNode;
		if ( this.startNode.equals(node) )
			return this.endNode;
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
	public List getLinks()
	{	
		return Collections.unmodifiableList(this.links);
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
			int count = origList.size();
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
					} else
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
		PhysicalLink ret = null;
		if(physicalLink == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(0);
		} else
		{
			int index = this.getLinks().indexOf(physicalLink);
			if(index != this.getLinks().size() - 1 && index != -1)
				ret = (PhysicalLink)this.getLinks().get(index + 1);
		}
		return ret;
	}

	/**
	 * �������� ���������� ����� �� ������� ������������� �����.
	 * @param physicalLink �����
	 * @return ���������� �����, ��� <code>null</code>, ���� link - ������
	 * � ������
	 */
	public PhysicalLink previousLink(PhysicalLink physicalLink)
	{
		PhysicalLink ret = null;
		if(physicalLink == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(this.getLinks().size() - 1);
		} else
		{
			int index = this.getLinks().indexOf(physicalLink);
			if(index > 0)
				ret = (PhysicalLink)this.getLinks().get(index - 1);
		}
		return ret;
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
	public List getSortedNodes()
	{
		if(!this.nodeLinksSorted)
			return EMPTY_SORTED_LIST;
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * �������� ������ ��������������� ���������� �����, �������� � ������
	 * ������, ������� �� ���������� ���� �����. ���������� ����� ������
	 * ������������� ���� ({@link #sortNodes()}) �� ������ ���� �������
	 * @return ������ ��������������� ���������� �����, ���
	 * <code>Collections.EMPTY_LIST</code>, ���� ���� �� �������������
	 */
	public List getSortedNodeLinks()
	{
		if(!this.nodeLinksSorted)
			return EMPTY_SORTED_LIST;
		return Collections.unmodifiableList(this.sortedNodeLinks);
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
					List nodeLinks = link.getNodeLinks();
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
	@SuppressWarnings("deprecation")
	public Map getExportMap() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(Set)
	 */
	public void setCharacteristics(Set characteristics) {
		this.schemeCableLink.setCharacteristics(characteristics);
	}

	/**
	 * @param characteristics
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(Set)
	 */
	public void setCharacteristics0(Set characteristics) {
		this.schemeCableLink.setCharacteristics0(characteristics);
	}
}
