/*-
 * $Id: MeasurementPath.java,v 1.33 2005/07/20 15:01:32 bass Exp $
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
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.DataPackage.Kind;

/**
 * ������� ����.
 *
 * @author $Author: bass $
 * @version $Revision: 1.33 $, $Date: 2005/07/20 15:01:32 $
 * @module mapviewclient_v1
 */
public final class MeasurementPath implements MapElement
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
	 * ������� ����.
	 */
	protected SchemePath schemePath;

	/**
	 * ��� �����.
	 */
	protected MapView mapView;

	/**
	 * ������������� ������ ��������� �����, �� ������� ��������
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List<CablePath> sortedCablePaths = new LinkedList<CablePath>();
	/**
	 * ������������� ������ ���������� �����, �� ������� ��������
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
	/**
	 * ������������� ������ �����, �� ������� ��������
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List<AbstractNode> sortedNodes = new LinkedList<AbstractNode>();

	/**
	 * �����������.
	 * @param schemePath ������� ����
	 * @param mapView ���
	 */
	protected MeasurementPath(
			SchemePath schemePath,
			AbstractNode stNode,
			AbstractNode eNode,
			MapView mapView)
	{
		this.startNode = stNode;
		this.endNode = eNode;
		this.mapView = mapView;

		this.schemePath = schemePath;
	}

	/**
	 * ������� ����� ������� ����.
	 * @param schemePath ������� ����
	 * @param mapView ��� �����
	 * @return ����� ������� ����
	 */
	public static MeasurementPath createInstance(
			SchemePath schemePath,
			AbstractNode stNode,
			AbstractNode eNode,
			MapView mapView)
	{
		if (mapView == null || stNode == null || eNode == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		return new MeasurementPath(
			schemePath,
			stNode,
			eNode,
			mapView);
	}

	/**
	 * {@inheritDoc}
	 * @throws ApplicationException 
	 */
	public Set<Characteristic> getCharacteristics() throws ApplicationException
	{
		return this.schemePath.getCharacteristics();
	}

	/**
	 * {@inheritDoc}
	 */
	public Identifier getId()
	{
		return this.schemePath.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName()
	{
		return this.schemePath.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(String name)
	{
		this.schemePath.setName(name);
	}

	/**
	 * �������� ��������.
	 * @return ��������
	 */
	public String getDescription()
	{
		return this.schemePath.getDescription();
	}

	/**
	 * ���������� ��������.
	 * @param description ��������
	 */
	public void setDescription(String description)
	{
		this.schemePath.setDescription(description);
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

	protected DoublePoint location = new DoublePoint(0.0, 0.0);

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation()
	{
		int count = 0;
		double x = 0.0D;
		double y = 0.0D;

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			DoublePoint an = cpath.getLocation();
			x += an.getX();
			y += an.getY();
			count++;
		}
		this.location.setLocation(x /= count, y /= count);
		
		return this.location;
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
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode()
	{
		return this.endNode;
//		return getMapView().getEndNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #endNode}.
	 * @param endNode new endNode
	 */
	public void setEndNode(AbstractNode endNode)
	{
		this.endNode = endNode;
	}
	
	/**
	 * Get {@link #startNode}.
	 * @return this.startNode
	 */
	public AbstractNode getStartNode()
	{
		return this.startNode;
//		return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(AbstractNode startNode)
	{
		this.startNode = startNode;
	}
	
	/**
	 * ���������� ������� ����.
	 * @param schemePath ������� ����.
	 */
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
	}

	/**
	 * �������� ������� ����.
	 * @return ������� ����
	 */
	public SchemePath getSchemePath()
	{
		return this.schemePath;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	/**
	 * ���������� �������������� ������ � ������
	 * @return �������������� �����
	 */
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getSortedCablePaths().iterator();
		while( e.hasNext())
		{
			CablePath cpath = (CablePath)e.next();
			length = length + cpath.getLengthLt();
		}
		return length;
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLf()
	{
		return SchemeUtils.getPhysicalLength(this.schemePath);
	}

	/**
	 * ���������� ���������� ����� � ������.
	 * @return ���������� �����
	 */
	public double getLengthLo()
	{
		return SchemeUtils.getOpticalLength(this.schemePath);
	}

	/**
	 * ��������������� ������ ��������� �����, �� ������� ��������
	 * ������������� ����.
	 * to avoid instantiation of multiple objects.
	 */
	protected List<CablePath> unsortedCablePaths = new LinkedList<CablePath>();

	/**
	 * �������� ������ �������������� �������, ������� ������ � ������ ����.
	 * ������ �������� �����������.
	 * @return ������ ��������� �����
	 */
	protected List<CablePath> getCablePaths() {
		synchronized (this.unsortedCablePaths) {
			final Scheme scheme = this.schemePath.getParentScheme();

			this.unsortedCablePaths.clear();
			for (final PathElement pathElement : this.schemePath.getPathMembers()) {
				switch (pathElement.getKind().value()) {
					case Kind._SCHEME_ELEMENT:
						final SchemeElement schemeElement = (SchemeElement) pathElement.getAbstractSchemeElement();
						final SiteNode site = this.mapView.findElement(schemeElement);
						if (site != null) {
							// TODO think if link to 'site' is needed for mPath
							// mPath.addCablePath(site);
						}
						break;
					case Kind._SCHEME_LINK:
						final SchemeLink schemeLink = (SchemeLink) pathElement.getAbstractSchemeElement();
						final SchemeElement startSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme,
								schemeLink.getSourceAbstractSchemePort().getParentSchemeDevice());
						final SchemeElement endSchemeElement = SchemeUtils.getSchemeElementByDevice(scheme,
								schemeLink.getTargetAbstractSchemePort().getParentSchemeDevice());
						final SiteNode startSiteNode = this.mapView.findElement(startSchemeElement);
						final SiteNode endSiteNode = this.mapView.findElement(endSchemeElement);
						if (startSiteNode == endSiteNode) {
							// TODO think if link to 'link' is needed for mPath
							// mPath.addCablePath(startSiteNode);
						}
						break;
					case Kind._SCHEME_CABLE_LINK:
						final SchemeCableLink schemeCableLink = (SchemeCableLink) pathElement.getAbstractSchemeElement();
						final CablePath cablePath = this.mapView.findCablePath(schemeCableLink);
						if (cablePath != null) {
							this.unsortedCablePaths.add(cablePath);
						}
						break;
					default:
						throw new UnsupportedOperationException("MeasurementPath.getCablePaths: Unknown path element kind: "
								+ pathElement.getKind());
				}
			}
		}
		return Collections.unmodifiableList(this.unsortedCablePaths);
	}

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List getSortedNodeLinks()
	{
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}
	
	/**
	 * Get {@link #sortedNodes}.
	 * @return this.sortedNodes
	 */
	public List getSortedNodes()
	{
		return Collections.unmodifiableList(this.sortedNodes);
	}
	
	/**
	 * Get {@link #sortedCablePaths}.
	 * @return this.sortedCablePaths
	 */
	public List<CablePath> getSortedCablePaths()
	{
		return Collections.unmodifiableList(this.sortedCablePaths);
	}
	
	/**
	 * ����������� �������� ����. �������� ���������� ��������� �����,
	 * ���������� ����� � �����.
	 */
	public void sortPathElements()
	{
		this.sortedCablePaths.clear();
		this.sortedNodeLinks.clear();
		this.sortedNodes.clear();
		
		AbstractNode node = getStartNode();

		this.sortedCablePaths.addAll(getCablePaths());
		
		for(Iterator it = this.sortedCablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			cpath.sortNodeLinks();
			if(cpath.getStartNode().equals(node))
			{
				this.sortedNodeLinks.addAll(cpath.getSortedNodeLinks());
				this.sortedNodes.addAll(cpath.getSortedNodes());
			} else
			{
				final List<NodeLink> reversedSortedNodeLinks = new ArrayList<NodeLink>(cpath.getSortedNodeLinks());
				Collections.reverse(reversedSortedNodeLinks);
				for (int i = 0; i < reversedSortedNodeLinks.size(); i++)
					this.sortedNodeLinks.add(reversedSortedNodeLinks.get(i));
				final List<AbstractNode> reversedSortedNodes = new ArrayList<AbstractNode>(cpath.getSortedNodes());
				Collections.reverse(reversedSortedNodes);
				for (int i = 0; i < reversedSortedNodes.size(); i++)
					this.sortedNodes.add(reversedSortedNodes.get(i));
			}
			node = cpath.getOtherNode(node);

			// to avoid duplicate entry
			this.sortedNodes.remove(node);
		}
		this.sortedNodes.add(node);
	}

	/**
	 * �������� ��������� �������� �� ������� ������������� ����������.
	 * @param nodeLink ��������
	 * @return ��������� ��������, ��� <code>null</code>, ���� nl - ���������
	 * � ������
	 */
	public NodeLink nextNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == getSortedNodeLinks().size() - 1)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index + 1);
	}

	/**
	 * �������� ���������� �������� �� ������� ������������� ����������.
	 * @param nodeLink ��������
	 * @return ���������� ��������, ��� <code>null</code>, ���� nl - ������
	 * � ������
	 */
	public NodeLink previousNodeLink(NodeLink nodeLink)
	{
		int index = getSortedNodeLinks().indexOf(nodeLink);
		if(index == 0)
			return null;

		return (NodeLink)getSortedNodeLinks().get(index - 1);
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

}
