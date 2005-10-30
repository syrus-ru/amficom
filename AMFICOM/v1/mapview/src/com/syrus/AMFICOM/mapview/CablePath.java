/*-
 * $Id: CablePath.java,v 1.40 2005/10/30 14:49:09 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * ������� ���������� ����. ��������� �������� ������ � �������������� ������.
 * 
 * @author $Author: bass $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.40 $, $Date: 2005/10/30 14:49:09 $
 * @module mapview
 */
public final class CablePath implements MapElement {
	/**
	 * ���� ���������.
	 */
	private transient boolean selected = false;

	/**
	 * ���� ������� ������� �������.
	 */
	private transient boolean alarmState = false;

	/**
	 * ���� ��������.
	 */
	private transient boolean removed = false;

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
	private transient List<AbstractNode> sortedNodes = new LinkedList<AbstractNode>();
	/**
	 * ������������� ������ ���������� �����, �� ������� �������� ������.
	 */
	private transient List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
	/**
	 * ���� ���������� ����������.
	 */
	private transient boolean nodeLinksSorted = false;

	/**
	 * ������ �����, �� ������� �������� ������.
	 */
	private transient List<PhysicalLink> links = new LinkedList<PhysicalLink>();

	/**
	 * ������� ������.
	 */
	private transient SchemeCableLink schemeCableLink = null;

	/**
	 * ������ �������� ������ � ������.
	 */
	private transient HashMap<CableChannelingItem, PhysicalLink> binding = null;

	/**
	 * �����������.
	 * 
	 * @param schemeCableLink ������� ������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 */
	protected CablePath(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode) {
		this.schemeCableLink = schemeCableLink;

		this.startNode = stNode;
		this.endNode = eNode;
		this.nodeLinksSorted = false;

		this.binding = new HashMap<CableChannelingItem, PhysicalLink>();
	}

	/**
	 * ������� ����� ������� ������.
	 * 
	 * @param schemeCableLink ������� ������
	 * @param stNode ��������� ����
	 * @param eNode �������� ����
	 * @return ����� �������������� ������ �������� �������.
	 */
	public static CablePath createInstance(
			SchemeCableLink schemeCableLink,
			AbstractNode stNode,
			AbstractNode eNode) {
		if(stNode == null || eNode == null || schemeCableLink == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return new CablePath(schemeCableLink, stNode, eNode);
	}

	/**
	 * Get this.endNode.
	 * 
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() {
		return this.endNode;
	}

	/**
	 * Set {@link #endNode}.
	 * 
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode) {
		this.endNode = endNode;
		this.nodeLinksSorted = false;
	}

	/**
	 * Get {@link #startNode}.
	 * 
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() {
		return this.startNode;
	}

	/**
	 * Set {@link #startNode}.
	 * 
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode) {
		this.startNode = startNode;
		this.nodeLinksSorted = false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Identifier getId() {
		return this.schemeCableLink.getId();
	}

	/**
	 * �������� ��������.
	 * 
	 * @return ��������
	 */
	public String getName() {
		return this.schemeCableLink.getName();
	}

	/**
	 * ���������� ��������.
	 * 
	 * @param name ����� ��������
	 */
	public void setName(String name) {
		this.schemeCableLink.setName(name);
	}

	/**
	 * �������� ��������.
	 * 
	 * @return ��������
	 */
	public String getDescription() {
		return this.schemeCableLink.getDescription();
	}

	/**
	 * ���������� ��������.
	 * 
	 * @param description ����� ��������
	 */
	public void setDescription(String description) {
		this.schemeCableLink.setDescription(description);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return this.alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		try {
			int count = 0;
			DoublePoint point = new DoublePoint(0.0, 0.0);
			double x = 0.0D;
			double y = 0.0D;
	
			for(Iterator it = this.getLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();
				DoublePoint an = link.getLocation();
				x += an.getX();
				y += an.getY();
				count++;
			}
			x /= count;
			y /= count;
	
			point.setLocation(x, y);
	
			return point;
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			return null;
		}
	}

	/**
	 * Suppress since this class is transient
	 */
	public void setLocation(@SuppressWarnings("unused") DoublePoint location) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved() {
		return this.removed;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/**
	 * �������� ���� �� ������ ����� ��������������� ������.
	 * 
	 * @param node �������� ����
	 * @return ������ �������� ����
	 */
	public AbstractNode getOtherNode(AbstractNode node) {
		if(this.endNode.equals(node))
			return this.startNode;
		if(this.startNode.equals(node))
			return this.endNode;
		return null;
	}

	/**
	 * ���������� ������� ������.
	 * 
	 * @param schemeCableLink ������� ������
	 */
	public void setSchemeCableLink(SchemeCableLink schemeCableLink) {
		this.schemeCableLink = schemeCableLink;
	}

	/**
	 * �������� ������� ������.
	 * 
	 * @return ������� ������
	 */
	public SchemeCableLink getSchemeCableLink() {
		return this.schemeCableLink;
	}

	/**
	 * ���������� �������������� ����� � ������.
	 * 
	 * @return �������������� �����
	 * @throws ApplicationException
	 */
	public double getLengthLt() throws ApplicationException {
		double length = 0;
		Iterator e = this.getLinks().iterator();
		while(e.hasNext()) {
			PhysicalLink link = (PhysicalLink) e.next();
//			if(!(link instanceof UnboundLink))
				length = length + link.getLengthLt();
		}
		return length;
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * 
	 * @return ���������� �����
	 */
	public double getLengthLf() {
		return this.schemeCableLink.getPhysicalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * 
	 * @return ���������� �����
	 */
	public double getLengthLo() {
		return this.schemeCableLink.getOpticalLength();
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * 
	 * @param len ���������� �����
	 */
	public void setLengthLf(double len) {
		this.schemeCableLink.setPhysicalLength(len);
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * 
	 * @param len ���������� �����
	 */
	public void setLengthLo(double len) {
		this.schemeCableLink.setOpticalLength(len);
	}

	/**
	 * ���������� ����������� �������������� ��������. ����������� ���������
	 * �������������� ����� � ����������.
	 * 
	 * @return ����������� �������������� ��������
	 * @throws ApplicationException
	 */
	public double getKd() throws ApplicationException {
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
	 * 
	 * @return ������ �����
	 * @throws ApplicationException
	 */
	public List<PhysicalLink> getLinks() throws ApplicationException {
		this.links.clear();
		for(CableChannelingItem cci : this.schemeCableLink.getPathMembers()) {
			this.links.add(this.binding.get(cci));
		}
		return Collections.unmodifiableList(this.links);
	}

	/**
	 * �������� ������ �����, �� ������� �������� ������.
	 */
	public void clearLinks() {
		this.links.clear();
		this.binding.clear();
	}

	/**
	 * ������ ����� �� ������. <br>
	 * ��������! �������� ����� ����� �� �����������.
	 */
	public void removeLink(final CableChannelingItem cci) {
		this.getBinding().remove(cci);
		this.nodeLinksSorted = false;
	}

	/**
	 * �������� ����� � ������. <br>
	 * ��������! �������� ����� ����� �� �����������
	 * 
	 * @param addLink ����������� �����
	 * @param cci ������, ����������� �������� ������ � �����
	 */
	public void addLink(
			final PhysicalLink addLink,
			final CableChannelingItem cci) {
		this.binding.put(cci, addLink);
		this.nodeLinksSorted = false;
	}

	private CableChannelingItem getElementAt(int index) throws ApplicationException {
		SortedSet<CableChannelingItem> cableChannelingItems = 
			this.schemeCableLink.getPathMembers();
		int count = cableChannelingItems.size();
		if(index < count && index >= 0) {
			int i = 0;
			for(CableChannelingItem nextCableChannelingItem : cableChannelingItems) {
				if(i == index)
					return nextCableChannelingItem;
				i++;
			}
		}
		return null;
	}

	/**
	 * �������� ��������� ����� �� ������� ������������� �����.
	 * 
	 * @return ��������� �����, ��� <code>null</code>, ���� link - ���������
	 *         � ������
	 * @throws ApplicationException
	 */
	public CableChannelingItem nextLink(CableChannelingItem cableChannelingItem) throws ApplicationException {
		CableChannelingItem ret = null;
		if(cableChannelingItem == null) {
			SortedSet<CableChannelingItem> cableChannelingItems = 
				this.schemeCableLink.getPathMembers();
			ret = cableChannelingItems.first();
		}
		else {
			int index = cableChannelingItem.getSequentialNumber();
			ret = getElementAt(index + 1);
		}
		return ret;
	}

	/**
	 * �������� ���������� ����� �� ������� ������������� �����.
	 * 
	 * @param cableChannelingItem �������� �����
	 * @return ���������� �����, ��� <code>null</code>, ���� link - ������ �
	 *         ������
	 * @throws ApplicationException
	 */
	public CableChannelingItem previousLink(
			CableChannelingItem cableChannelingItem) throws ApplicationException {
		CableChannelingItem ret = null;
		if(cableChannelingItem == null) {
			SortedSet<CableChannelingItem> cableChannelingItems = 
				this.schemeCableLink.getPathMembers();
			ret = cableChannelingItems.last();
		}
		else {
			int index = cableChannelingItem.getSequentialNumber();
			ret = getElementAt(index - 1);
		}
		return ret;
	}

	/**
	 * ����������� ����, �������� � ������ ������, ������� �� ���������� ����.
	 * ���������� ����� ���������� ������� � ����������� ����������
	 * @throws ApplicationException
	 */
	public void sortNodes() throws ApplicationException {
		this.sortNodeLinks();
	}

	/**
	 * �������� ������ ��������������� �����, �������� � ������ ������, �������
	 * �� ���������� ���� �����. ���������� ����� ������ ������������� ���� ({@link #sortNodes()})
	 * �� ������ ���� �������
	 * 
	 * @return ������ ��������������� �����, ���
	 *         <code>Collections.EMPTY_LIST</code>, ���� ���� ��
	 *         �������������
	 */
	public List<AbstractNode> getSortedNodes() {
		if(!this.nodeLinksSorted) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * �������� ������ ��������������� ���������� �����, �������� � ������
	 * ������, ������� �� ���������� ���� �����. ���������� ����� ������
	 * ������������� ���� ({@link #sortNodes()}) �� ������ ���� �������
	 * 
	 * @return ������ ��������������� ���������� �����, ���
	 *         <code>Collections.EMPTY_LIST</code>, ���� ���� ��
	 *         �������������
	 */
	public List<NodeLink> getSortedNodeLinks() {
		if(!this.nodeLinksSorted) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}

	/**
	 * ����������� ��������� ����� �� ������� ������� �� ���������� ����. ���
	 * ���������� ���������� ����������� ����� ����
	 * @throws ApplicationException
	 */
	public void sortNodeLinks() throws ApplicationException {
		// if(!nodeLinksSorted)
		{
			List<NodeLink> list = new LinkedList<NodeLink>();
			List<AbstractNode> nodeList = new LinkedList<AbstractNode>();

			AbstractNode node = getStartNode();

			for(Iterator it = this.getLinks().iterator(); it.hasNext();) {
				PhysicalLink link = (PhysicalLink) it.next();

				link.sortNodeLinks();

				if(link.getStartNode().equals(node)) {
					list.addAll(link.getNodeLinks());
					nodeList.addAll(link.getSortedNodes());
				}
				else {
					final List<NodeLink> nodeLinks = link.getNodeLinks();
					final List<NodeLink> nodeLinksList = 
						new ArrayList<NodeLink>(nodeLinks);
					for(final ListIterator<NodeLink> listIterator = 
							nodeLinksList.listIterator(nodeLinksList.size()); 
							listIterator.hasPrevious();) {
						list.add(listIterator.previous());
					}
					List<AbstractNode> sortedNodes2 = link.getSortedNodes();
					for(final ListIterator<AbstractNode> listIterator = 
							sortedNodes2.listIterator(sortedNodes2.size()); 
							listIterator.hasPrevious();) {
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
	 * �������� ������, �������� �������� ������ � ������.
	 * 
	 * @return ��������
	 */
	public HashMap<CableChannelingItem, PhysicalLink> getBinding() {
		return this.binding;
	}

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ������ � ������ ����
	 * ������ �� ������� ������������� �����.
	 * 
	 * @return ����
	 * @throws ApplicationException
	 */
	public AbstractNode getStartUnboundNode() throws ApplicationException {
		AbstractNode bufferSite = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink) it.next();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ������ � ����� ����
	 * ��������� �� ������� ������������� �����.
	 * 
	 * @return ����
	 * @throws ApplicationException
	 */
	public AbstractNode getEndUnboundNode() throws ApplicationException {
		AbstractNode bufferSite = getEndNode();
		List<PhysicalLink> thislinks = this.getLinks();
		for(ListIterator it = thislinks.listIterator(thislinks.size()); 
				it.hasPrevious();) {
			PhysicalLink link = (PhysicalLink) it.previous();
			if(link instanceof UnboundLink)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;

	}

	public CableChannelingItem getFirstCCI(PhysicalLink physicalLink) throws ApplicationException {
		for(CableChannelingItem cci2 : this.schemeCableLink.getPathMembers()) {
			if(this.binding.get(cci2).equals(physicalLink))
				return cci2;
		}
		return null;
	}

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ��������� � ������
	 * ����������� �����.
	 * 
	 * @return ����������� �����
	 * @throws ApplicationException
	 */
	public CableChannelingItem getStartLastBoundLink() throws ApplicationException {
		CableChannelingItem cci = null;
		for(Iterator it = this.schemeCableLink.getPathMembers().iterator(); 
				it.hasNext();) {
			CableChannelingItem cci2 = (CableChannelingItem) it.next();
			if(cci2.getPhysicalLink() == null)
				break;
			cci = cci2;
		}
		return cci;
	}

	/**
	 * �������� �� ������ �����, �������� � ��������� ����, ��������� � �����
	 * ����������� �����.
	 * 
	 * @return ����������� �����
	 * @throws ApplicationException
	 */
	public CableChannelingItem getEndLastBoundLink() throws ApplicationException {
		CableChannelingItem cci = null;
		for(Iterator it = this.schemeCableLink.getPathMembers().iterator(); 
				it.hasNext();) {
			CableChannelingItem cci2 = (CableChannelingItem) it.next();
			if(cci == null)
				cci = cci2;
			if(cci2.getPhysicalLink() == null)
				cci = null;
		}
		return cci;
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public void revert(MapElementState state) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return this.schemeCableLink;
	}

}
