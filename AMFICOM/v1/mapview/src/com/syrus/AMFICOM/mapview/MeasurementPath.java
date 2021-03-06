/*-
 * $Id: MeasurementPath.java,v 1.61 2006/06/27 13:44:44 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;

/**
 * ??????? ????.
 *
 * @author $Author: arseniy $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.61 $, $Date: 2006/06/27 13:44:44 $
 * @module mapview
 */
public final class MeasurementPath implements MapElement {
	/**
	 * ???? ?????????.
	 */
	private transient boolean selected = false;

	/**
	 * ???? ??????? ??????? ???????.
	 */
	private transient boolean alarmState = false;

	/**
	 * ???? ????????.
	 */
	private transient boolean removed = false;

	/**
	 * ???? ?????, ? ???????? ???????? ????????? ???? ??????.
	 */
	private transient AbstractNode startNode = null;

	/**
	 * ???? ?????, ? ???????? ???????? ???????? ???? ??????.
	 */
	private transient AbstractNode endNode = null;

	/**
	 * ??????? ????.
	 */
	private SchemePath schemePath;

	/**
	 * ??? ?????.
	 */
	private MapView mapView;

	/**
	 * ????????????? ?????? ????????? ?????, ?? ??????? ????????
	 * ????????????? ????.
	 * to avoid instantiation of multiple objects.
	 */
	private List<CablePath> sortedCablePaths = new LinkedList<CablePath>();
	/**
	 * ????????????? ?????? ?????????? ?????, ?? ??????? ????????
	 * ????????????? ????.
	 * to avoid instantiation of multiple objects.
	 */
	private List<NodeLink> sortedNodeLinks = new LinkedList<NodeLink>();
	/**
	 * ????????????? ?????? ?????, ?? ??????? ????????
	 * ????????????? ????.
	 * to avoid instantiation of multiple objects.
	 */
	private List<AbstractNode> sortedNodes = new LinkedList<AbstractNode>();

	/**
	 * ???????????.
	 * @param schemePath ??????? ????
	 * @param mapView ???
	 */
	private MeasurementPath(final SchemePath schemePath,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final MapView mapView) {
		this.startNode = stNode;
		this.endNode = eNode;
		this.mapView = mapView;

		this.schemePath = schemePath;
	}

	/**
	 * ??????? ????? ??????? ????.
	 * 
	 * @param schemePath
	 *        ??????? ????
	 * @param mapView
	 *        ??? ?????
	 * @return ????? ??????? ????
	 */
	public static MeasurementPath createInstance(final SchemePath schemePath,
			final AbstractNode stNode,
			final AbstractNode eNode,
			final MapView mapView) {
		if (mapView == null || stNode == null || eNode == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");

		return new MeasurementPath(schemePath, stNode, eNode, mapView);
	}

	/**
	 * {@inheritDoc}
	 */
	public Identifier getId() {
		return this.schemePath.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return this.schemePath.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setName(final String name) {
		this.schemePath.setName(name);
	}

	/**
	 * ???????? ????????.
	 * 
	 * @return ????????
	 */
	public String getDescription() {
		return this.schemePath.getDescription();
	}

	/**
	 * ?????????? ????????.
	 * @param description ????????
	 */
	public void setDescription(final String description) {
		this.schemePath.setDescription(description);
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
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAlarmState(final boolean alarmState) {
		this.alarmState = alarmState;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean getAlarmState() {
		return this.alarmState;
	}

	private DoublePoint location = new DoublePoint(0.0, 0.0);

	/**
	 * {@inheritDoc}
	 */
	public DoublePoint getLocation() {
		double x = 0.0D; 
		double y = 0.0D;

		int count = this.sortedCablePaths.size();
		if(count > 0) {
			for (final CablePath cablePath : this.sortedCablePaths) {
				final DoublePoint an = cablePath.getLocation();
				x += an.getX();
				y += an.getY();
			}
			this.location.setLocation(x /= count, y /= count);
		}
		// else leave intact
		return this.location;
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
	public void setRemoved(final boolean removed) {
		this.removed = removed;
	}

	/**
	 * Get this.endNode.
	 * @return this.endNode
	 */
	public AbstractNode getEndNode() {
		return this.endNode;
		// return getMapView().getEndNode(this.getSchemePath());
	}

	/**
	 * Set {@link #endNode}.
	 * 
	 * @param endNode
	 *        new endNode
	 */
	public void setEndNode(final AbstractNode endNode) {
		this.endNode = endNode;
	}

	/**
	 * Get {@link #startNode}.
	 * 
	 * @return this.startNode
	 */
	public AbstractNode getStartNode() {
		return this.startNode;
		// return getMapView().getStartNode(this.getSchemePath());
	}
	
	/**
	 * Set {@link #startNode}.
	 * @param startNode new startNode
	 */
	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	/**
	 * ?????????? ??????? ????.
	 * 
	 * @param schemePath
	 *        ??????? ????.
	 */
	public void setSchemePath(final SchemePath schemePath) {
		this.schemePath = schemePath;
	}

	/**
	 * ???????? ??????? ????.
	 * 
	 * @return ??????? ????
	 */
	public SchemePath getSchemePath() {
		return this.schemePath;
	}

	/**
	 * ?????????? ?????????????? ?????? ? ??????
	 * 
	 * @return ?????????????? ?????
	 * @throws ApplicationException
	 */
	public double getLengthLt() throws ApplicationException {
		double length = 0;
		for (final CablePath cablePath : this.getSortedCablePaths()) {
			length = length + cablePath.getLengthLt();
		}
		return length;
	}

	/**
	 * ?????????? ?????????? ????? ? ??????.
	 * 
	 * @return ?????????? ?????
	 * @throws ApplicationException	
	 */
	public double getLengthLf() throws ApplicationException {
		return this.schemePath.getPhysicalLength();
	}

	/**
	 * ?????????? ?????????? ????? ? ??????.
	 * 
	 * @return ?????????? ?????
	 * @throws ApplicationException
	 */
	public double getLengthLo() throws ApplicationException {
		return this.schemePath.getOpticalLength();
	}

	/**
	 * ??????????????? ?????? ????????? ?????, ?? ??????? ????????
	 * ????????????? ????.
	 * to avoid instantiation of multiple objects.
	 */
//	private List<CablePath> unsortedCablePaths = new LinkedList<CablePath>();

	/**
	 * ????????????? ?????? ?????????????? ???????, ??????? ?????? ? ?????? ????.
	 * ?????? ???????? ???????????.
	 */
	private void sortCablePaths() throws ApplicationException {
			this.sortedCablePaths.clear();
			for (final PathElement pathElement : this.schemePath.getPathMembers()) {
				switch (pathElement.getKind().value()) {
					case IdlKind._SCHEME_ELEMENT:
//						final SchemeElement schemeElement = (SchemeElement) abstractSchemeElement;
//						final SiteNode site = this.mapView.findElement(schemeElement);
//						if (site != null) {
							// TODO think if link to 'site' is needed for mPath
							// mPath.addCablePath(site);
						
//						}
						break;
					case IdlKind._SCHEME_LINK:
//						final SchemeLink schemeLink = pathElement.getSchemeLink();
//
//						SchemeElement innerSourceElement = schemeLink.getSourceAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
//						SchemeElement topSourceElement = MapView.getTopLevelSchemeElement(innerSourceElement);
//						final SchemeElement startSchemeElement = MapView.getTopologicalSchemeElement(scheme, topSourceElement);
//
//						SchemeElement innerTargetElement = schemeLink.getTargetAbstractSchemePort().getParentSchemeDevice().getParentSchemeElement();
//						SchemeElement topTargetElement = MapView.getTopLevelSchemeElement(innerTargetElement);
//						final SchemeElement endSchemeElement = MapView.getTopologicalSchemeElement(scheme, topTargetElement);
//
//						final SiteNode startSiteNode = this.mapView.findElement(startSchemeElement);
//						final SiteNode endSiteNode = this.mapView.findElement(endSchemeElement);
//						if (startSiteNode == endSiteNode) {
							// TODO think if link to 'link' is needed for mPath
							// mPath.addCablePath(startSiteNode);
//						}
						break;
					case IdlKind._SCHEME_CABLE_LINK:
						final SchemeCableLink schemeCableLink = pathElement.getSchemeCableLink();
						final CablePath cablePath = this.mapView.findCablePath(schemeCableLink);
						if (cablePath != null) {
							this.sortedCablePaths.add(cablePath);
						}
						break;
					default:
						throw new UnsupportedOperationException("MeasurementPath.getCablePaths: Unknown path element kind: "
								+ pathElement.getKind());
				}
			}
	}

	/**
	 * Get {@link #sortedNodeLinks}.
	 * @return this.sortedNodeLinks
	 */
	public List<NodeLink> getSortedNodeLinks() {
		return Collections.unmodifiableList(this.sortedNodeLinks);
	}

	/**
	 * Get {@link #sortedNodes}.
	 * 
	 * @return this.sortedNodes
	 */
	public List<AbstractNode> getSortedNodes() {
		return Collections.unmodifiableList(this.sortedNodes);
	}

	/**
	 * Get {@link #sortedCablePaths}.
	 * 
	 * @return this.sortedCablePaths
	 */
	public List<CablePath> getSortedCablePaths() {
		return Collections.unmodifiableList(this.sortedCablePaths);
	}
	
	/**
	 * ??????????? ???????? ????. ???????? ?????????? ????????? ?????, ??????????
	 * ????? ? ?????.
	 */
	public void sortPathElements() throws ApplicationException {
		this.sortedCablePaths.clear();
		this.sortedNodeLinks.clear();
		this.sortedNodes.clear();

		AbstractNode node = getStartNode();

		sortCablePaths();

		for (final CablePath cablePath : this.sortedCablePaths) {
			cablePath.sortNodeLinks();
			final List<NodeLink> sortedNodeLinks2 = cablePath.getSortedNodeLinks();
			final List<AbstractNode> sortedNodes2 = cablePath.getSortedNodes();
			
			if (cablePath.getStartNode().equals(node)) {
				this.sortedNodeLinks.addAll(sortedNodeLinks2);
				this.sortedNodes.addAll(sortedNodes2);
			} else {
				for (final ListIterator<NodeLink> it = sortedNodeLinks2.listIterator(sortedNodeLinks2.size()); it.hasPrevious();) {
					this.sortedNodeLinks.add(it.previous());
				}
				for (final ListIterator<AbstractNode> it = sortedNodes2.listIterator(sortedNodes2.size()); it.hasPrevious();) {
					this.sortedNodes.add(it.previous());
				}
			}
			node = cablePath.getOtherNode(node);

			// to avoid duplicate entry
			this.sortedNodes.remove(node);
		}
		this.sortedNodes.add(node);
	}

	/**
	 * ???????? ????????? ???????? ?? ??????? ????????????? ??????????.
	 * 
	 * @param nodeLink
	 *        ????????
	 * @return ????????? ????????, ??? <code>null</code>, ???? nl - ????????? ?
	 *         ??????
	 */
//	public NodeLink nextNodeLink(final NodeLink nodeLink) {
//		List nodeLinks = getSortedNodeLinks();
//		final int index = nodeLinks.indexOf(nodeLink);
//		if (index == nodeLinks.size() - 1)
//			return null;
//
//		return (NodeLink) nodeLinks.get(index + 1);
//	}

	/**
	 * ???????? ?????????? ???????? ?? ??????? ????????????? ??????????.
	 * @param nodeLink ????????
	 * @return ?????????? ????????, ??? <code>null</code>, ???? nl - ??????
	 * ? ??????
	 */
//	public NodeLink previousNodeLink(final NodeLink nodeLink) {
//		List nodeLinks = getSortedNodeLinks();
//		int index = nodeLinks.indexOf(nodeLink);
//		if (index == 0)
//			return null;
//
//		return (NodeLink) nodeLinks.get(index - 1);
//	}

	/**
	 * {@inheritDoc} Suppress since this class is transient
	 */
	public MapElementState getState() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient
	 */
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException();
	}

	public Characterizable getCharacterizable() {
		return this.schemePath;
	}

}
