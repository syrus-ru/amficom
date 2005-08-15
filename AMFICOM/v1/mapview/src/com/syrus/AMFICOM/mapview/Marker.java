/*-
 * $Id: Marker.java,v 1.34 2005/08/15 14:08:40 krupenn Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * ��������: ������ ���������� ���������� ��������� Lo, ����������      *
 *         ����������������� �����, �� ������������ ���������� Lf,      *
 *         �������� ��� ���������������� �������, � ��������������      *
 *         ���������� Lt, ���������� � ���������� �������� ��           *
 *         ����������� � ����������� ��� ����������� � ���� �����       *
 *         ���������� ��������� ������������ �� �������������:          *
 *             Ku = Lo / Lf                                             *
 *             Kd = Lf / Lt                                             *
 *         ���������� ����������� ������� �� ����� � �� ��������������  *
 *         �������� �� ������ ��������� Lo, � ����� � ��� �����������   *
 *         � ��������� �� ���� �������������� ��������� �������������   *
 *         �� Lo � Lf � Lt � ��������, � ���������� ��������� Lt        *
 *         ������������� � Lf � Lo. ���������� ���������� ������        *
 *         �������� ������� � �����, � ���� ������ ������������ Lf,     *
 *         ��� ��� �������������� ����� �� �������� ���������� � Ku,    *
 *         � ���� �������������� �������������� ������ �����            *
 *         �����������, ����� ���� ����� ������������ Lo.               *
 *
 *
 *
 * @version $Revision: 1.34 $, $Date: 2005/08/15 14:08:40 $
 * @module mapview
 * @author $Author: krupenn $
 */

public class Marker extends AbstractNode {
	private static final long serialVersionUID = 02L;
	
	/**
	 * ������������� ������������ �������.
	 */
	protected Identifier monitoredElementId;

	/**
	 * ��������� �� ������ �������������� ���� �� �������.
	 */
	protected double physicalDistance = 0.0;

	/**
	 * ���������. ���������������� � ������������ � ����� �������.
	 */
	protected Object descriptor;

	/**
	 * ��� �����.
	 */
	protected MapView mapView;
	/**
	 * ������������� ����, �� ������� ��������� ������.
	 */
	protected MeasurementPath measurementPath;
	/**
	 * ������� ������, �� ������� ��������� ������.
	 */
	protected CablePath cpath;
	/**
	 * ������� �������� �����, �� ������� ��������� ������.
	 */
	protected NodeLink nodeLink;
	/**
	 * ��������� ���� �� ��������� �����, �� ������� ��������� ������.
	 */
	protected AbstractNode startNode;
	/**
	 * �������� ���� �� ��������� �����, �� ������� ��������� ������.
	 */
	protected AbstractNode endNode;

	/**
	 * �������� ������� ������������� �� �����.
	 *
	 * @param id �������������
	 * @param creatorId ������������
	 * @param mapView ��� �����
	 * @param startNode ��������� ���� ���������
	 * @param endNode �������� ���� ���������
	 * @param nodeLink ��������
	 * @param path ������������� ����
	 * @param monitoredElementId ����������� ������
	 * @param dpoint �������������� ���������� �������
	 */
	protected Marker(final Identifier id,
			final Identifier creatorId,
			final MapView mapView,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final NodeLink nodeLink,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final DoublePoint dpoint) {
		this(id, creatorId, mapView, path, monitoredElementId, String.valueOf(id.getMinor()));

		this.startNode = startNode;
		this.endNode = endNode;
		this.nodeLink = nodeLink;
		setLocation(dpoint);
	}

	/**
	 * �������� ������� ������������� �� �����.
	 * 
	 * @param creatorId
	 *        ������������
	 * @param mapView
	 *        ��� �����
	 * @param startNode
	 *        ��������� ���� ���������
	 * @param endNode
	 *        �������� ���� ���������
	 * @param nodeLink
	 *        ��������
	 * @param path
	 *        ������������� ����
	 * @param monitoredElementId
	 *        ����������� ������
	 * @param dpoint
	 *        �������������� ���������� �������
	 * @return ����� ������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *         ������ �������
	 */
	public static Marker createInstance(final Identifier creatorId,
			final MapView mapView,
			final AbstractNode startNode,
			final AbstractNode endNode,
			final NodeLink nodeLink,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final DoublePoint dpoint) throws CreateObjectException {
		if (startNode == null || mapView == null || endNode == null || path == null || dpoint == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Identifier ide = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE);
			return new Marker(ide, creatorId, mapView, startNode, endNode, nodeLink, path, monitoredElementId, dpoint);
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * �������� ������� �� ������ ����������� ��������� � ��������� ����������
	 * ���������.
	 * 
	 * @param id
	 *        �������������
	 * @param creatorId
	 *        ������������
	 * @param mapView
	 *        ��� �����
	 * @param opticalDistance
	 *        ���������� ��������� �� ������ ����
	 * @param path
	 *        ������������� ����
	 * @param monitoredElementId
	 *        ������������� ������������ �������
	 * @param name
	 *        �������� �������
	 */
	public Marker(final Identifier id,
			final Identifier creatorId,
			final MapView mapView,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final String name) {
		super(id);

		final long time = System.currentTimeMillis();
		super.created = new Date(time);
		super.modified = new Date(time);
		super.creatorId = creatorId;
		super.modifierId = super.creatorId;
		super.name = id.toString();
		super.description = "";
		super.location = new DoublePoint(0.0, 0.0);
		super.name = name;

		this.mapView = mapView;
		this.monitoredElementId = monitoredElementId;
		this.measurementPath = path;
		this.startNode = this.measurementPath.getStartNode();
	}

	/**
	 * �������� ������� �� ������ ����������� ��������� � ��������� ����������
	 * ���������.
	 * 
	 * @param creatorId
	 *        ������������
	 * @param mapView
	 *        ��� �����
	 * @param path
	 *        ������������� ����
	 * @param monitoredElementId
	 *        ������������� ������������ �������
	 * @param name
	 *        �������� �������
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 * @return ����� ������
	 */
	public static Marker createInstance(final Identifier creatorId,
			final MapView mapView,
			final MeasurementPath path,
			final Identifier monitoredElementId,
			final String name) throws CreateObjectException {
		if (monitoredElementId == null || mapView == null || path == null)
			throw new IllegalArgumentException("Argument is 'null'");

		try {
			final Identifier ide = IdentifierPool.getGeneratedIdentifier(ObjectEntities.SITENODE_CODE);
			return new Marker(ide, creatorId, mapView, path, monitoredElementId, name);
		} catch (IdentifierGenerationException e) {
			throw new CreateObjectException("MapMarker.createInstance | cannot generate identifier ", e);
		}
	}

	/**
	 * ���������� �������������.
	 * 
	 * @param id
	 *        �������������
	 */
	public void setId(final Identifier id) {
		super.id = id;
	}

	/**
	 * ���������� ��� �����.
	 * 
	 * @param mapView
	 *        ��� �����
	 */
	public void setMapView(final MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * �������� ��� �����.
	 * 
	 * @return ��� �����
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	public NodeLink previousNodeLink() {
		NodeLink nLink;
		final int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
		if (index == 0) {
			nLink = null;
		}
		else {
			nLink = (NodeLink) (this.measurementPath.getSortedNodeLinks().get(index - 1));
		}
		return nLink;
	}

	public NodeLink nextNodeLink() {
		NodeLink nLink;
		final int index = this.measurementPath.getSortedNodeLinks().indexOf(this.nodeLink);
		if (index == this.measurementPath.getSortedNodeLinks().size() - 1) {
			nLink = null;
		}
		else {
			nLink = (NodeLink) (this.measurementPath.getSortedNodeLinks().get(index + 1));
		}
		return nLink;
	}

//	public SiteNode getLeft() {
//		final List<AbstractNode> nodes = this.cpath.getSortedNodes();
//		SiteNode previous = null;
//		for (final AbstractNode node : nodes) {
//			if (node instanceof SiteNode) {
//				previous = (SiteNode) node;
//			}
//			if (node.equals(this.startNode)) {
//				break;
//			}
//		}
//		return previous;
//	}
//
//	public SiteNode getRight() {
//		final List<AbstractNode> nodes = this.cpath.getSortedNodes();
//		SiteNode found = null;
//
//		for (final AbstractNode node : nodes) {
//			if (node instanceof SiteNode) {
//				found = (SiteNode) node;
//			}
//			if (node.equals(this.endNode)) {
//				break;
//			}
//		}
//		return found;
//	}

	public void setMeasurementPath(final MeasurementPath measurementPath) {
		this.measurementPath = measurementPath;
	}

	public MeasurementPath getMeasurementPath() {
		return this.measurementPath;
	}

	public void setDescriptor(final Object descriptor) {
		this.descriptor = descriptor;
	}

	public Object getDescriptor() {
		return this.descriptor;
	}

	public void setNodeLink(final NodeLink nodeLink) {
		this.nodeLink = nodeLink;
	}

	public NodeLink getNodeLink() {
		return this.nodeLink;
	}

	public void setStartNode(final AbstractNode startNode) {
		this.startNode = startNode;
	}

	public AbstractNode getStartNode() {
		return this.startNode;
	}

	public void setEndNode(final AbstractNode endNode) {
		this.endNode = endNode;
	}

	public AbstractNode getEndNode() {
		return this.endNode;
	}

	public CablePath getCablePath() {
		return this.cpath;
	}

	public void setCablePath(final CablePath cpath) {
		this.cpath = cpath;
	}

	public void setMonitoringElementId(final Identifier monitoringElementId) {
		this.monitoredElementId = monitoringElementId;
	}

	public Identifier getMonitoringElementId() {
		return this.monitoredElementId;
	}

	public void setPhysicalDistance(final double physicalDistance) {
		this.physicalDistance = physicalDistance;
	}

	public double getPhysicalDistance() {
		return this.physicalDistance;
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
	public void revert(final MapElementState state) {
		throw new UnsupportedOperationException();
	}

////////////////////////////////////////////////////////////////////////////////

	/**
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	public void insert() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc} Suppress since this class is not storable (unlike
	 * {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is not storable
	 * (unlike {@link com.syrus.AMFICOM.general.StorableObject})
	 */
	@Override
	public final IdlStorableObject getTransferable(final ORB orb) {
		throw new UnsupportedOperationException();
	}

}
