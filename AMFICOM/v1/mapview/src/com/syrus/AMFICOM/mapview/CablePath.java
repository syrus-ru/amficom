/**
 * $Id: CablePath.java,v 1.8 2005/03/16 12:53:22 bass Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

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
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.CableChannelingItem;

import java.util.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * ������� ���������� ����. ��������� �������� ������ � �������������� ������.
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/03/16 12:53:22 $
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
	protected List sortedNodes = new LinkedList();
	/**
	 * ������������� ������ ���������� �����, �� ������� �������� ������.
	 */
	protected List sortedNodeLinks = new LinkedList();
	/**
	 * ���� ���������� ����������.
	 */
	protected boolean nodeLinksSorted = false;

	/**
	 * ������ �����, �� ������� �������� ������.
	 */
	protected List links = new LinkedList();
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
	public Collection getCharacteristics() 
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
		return this.schemeCableLink.physicalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLo()
	{
		return this.schemeCableLink.opticalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @param len ���������� �����
	 */
	public void setLengthLf(double len)
	{
		this.schemeCableLink.physicalLength(len);
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @param len ���������� �����
	 */
	public void setLengthLo(double len)
	{
		this.schemeCableLink.opticalLength(len);
	}

	/**
	 * ���������� ����������� �������������� ��������. ����������� ���������
	 * �������������� ����� � ����������.
	 * @return ����������� �������������� ��������
	 */
	public double getKd()
	{
		double phLen = this.schemeCableLink.physicalLength();
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
			List origvec = new LinkedList();
			origvec.addAll(this.getLinks());
			List vec = new LinkedList();
			int count = this.getLinks().size();
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
						CableChannelingItem cci = this.getBinding().getCCI(link);
						cci.setStartSiteNode((SiteNode )link.getStartNode());
						cci.setEndSiteNode((SiteNode )link.getEndNode());
						canSort = true;
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						vec.add(link);
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
			this.links = vec;
			this.linksSorted = true;
			this.nodeLinksSorted = false;
		}
	}

	/**
	 * �������� ��������� ����� �� ������� ������������� �����.
	 * @param link �����
	 * @return ��������� �����, ��� <code>null</code>, ���� link - ��������� 
	 * � ������
	 */
	public PhysicalLink nextLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(0);
		}
		else
		{
			int index = this.getLinks().indexOf(link);
			if(index != this.getLinks().size() - 1 && index != -1)
				ret = (PhysicalLink)this.getLinks().get(index + 1);
		}
		return ret;
	}

	/**
	 * �������� ���������� ����� �� ������� ������������� �����.
	 * @param link �����
	 * @return ���������� �����, ��� <code>null</code>, ���� link - ������ 
	 * � ������
	 */
	public PhysicalLink previousLink(PhysicalLink link)
	{
		PhysicalLink ret = null;
		if(link == null)
		{
			if(this.getLinks().size() != 0)
				ret = (PhysicalLink)this.getLinks().get(this.getLinks().size() - 1);
		}
		else
		{
			int index = this.getLinks().indexOf(link);
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
	public java.util.List getSortedNodes()
	{
		if(!this.nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * �������� ������ ��������������� ���������� �����, �������� � ������ 
	 * ������, ������� �� ���������� ���� �����. ���������� ����� ������ 
	 * ������������� ���� ({@link #sortNodes()}) �� ������ ���� �������
	 * @return ������ ��������������� ���������� �����, ��� 
	 * <code>Collections.EMPTY_LIST</code>, ���� ���� �� �������������
	 */
	public java.util.List getSortedNodeLinks()
	{
		if(!this.nodeLinksSorted)
			return Collections.EMPTY_LIST;
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}

	/**
	 * ����������� ��������� ����� �� ������� ������� �� ���������� ����.
	 * ��� ���������� ���������� ����������� ����� ����
	 */
	public void sortNodeLinks()
	{
		this.sortLinks();
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
			this.nodeLinksSorted = true;
		}
	}

	/**
	 * �������� ���������� ��������� ������� �� ����� <code>link</code>.
	 * @param link �����
	 * @return ���������� ��������� � �������
	 */
	public IntPoint getBindingPosition(PhysicalLink link)
	{
		CableChannelingItem cci = (CableChannelingItem )getBinding().get(link);
		return new IntPoint(cci.rowX(), cci.placeY());
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
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			PhysicalLink link = (PhysicalLink)it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics(java.util.Collection)
	 */
	public void setCharacteristics(Collection characteristics) {
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
	 * @see com.syrus.AMFICOM.general.Characterizable#setCharacteristics0(java.util.Collection)
	 */
	public void setCharacteristics0(Collection characteristics) {
		throw new UnsupportedOperationException();
	}
}
