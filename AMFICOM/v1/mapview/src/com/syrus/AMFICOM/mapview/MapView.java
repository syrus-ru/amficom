/*
* $Id: MapView.java,v 1.73 2005/10/22 13:46:44 krupenn Exp $
*
* Copyright � 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;


import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind.CABLE_SUBNETWORK;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.corba.IdlMapView;
import com.syrus.AMFICOM.mapview.corba.IdlMapViewHelper;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;

/**
 * ����� ������������ ��� �������� ��������, ������������ ��
 * �������������� �����. ������� �������� � ����:
 * <br>&#9;- ������ �������������� ����� {@link Map}, �� ���� ���������
 * ���������������
 * <br>&#9;- ����� ���������� ���� {@link Scheme}, ������� ��������� �� ������
 * �������������� �����
 * 
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @version $Revision: 1.73 $, $Date: 2005/10/22 13:46:44 $
 * @module mapview
 */
public final class MapView extends DomainMember implements Describable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 3256722862181200184L;

	private String name;
	private String description;

	private double scale;
	private double defaultScale;

	/**
	 * �������������� ���������� ������ ����.
	 */
	protected DoublePoint center = new DoublePoint(0, 0);

	private Map map;
	/**
	 *  List&lt;{@link com.syrus.AMFICOM.scheme.Scheme}&gt;
	 */
	private Set<Scheme> schemes;

	private transient Set<UnboundNode> unboundNodes = new HashSet<UnboundNode>();
	private transient Set<UnboundLink> unboundLinks = new HashSet<UnboundLink>();
	private transient Set<NodeLink> unboundNodeLinks = new HashSet<NodeLink>();

	/** ������ �������. */
	private transient Set<CablePath> cablePaths = new HashSet<CablePath>();
	
	/** ������ ������������� �����. */
	private transient Set<MeasurementPath> measurementPaths = new HashSet<MeasurementPath>();
	
	/** ������ ��������. */
	private transient Set<Marker> markers = new HashSet<Marker>();

	private transient Set<AbstractNode> nodeElements;
	private transient Set<PhysicalLink> linkElements;
	private transient Set<NodeLink> nodeLinkElements;

	private void initialize() {
		if(this.markers == null) {
			this.markers = new HashSet<Marker>();
		}
		if(this.unboundNodes == null) {
			this.unboundNodes = new HashSet<UnboundNode>();
		}
		if(this.unboundLinks == null) {
			this.unboundLinks = new HashSet<UnboundLink>();
		}
		if(this.unboundNodeLinks == null) {
			this.unboundNodeLinks = new HashSet<NodeLink>();
		}
		if(this.cablePaths == null) {
			this.cablePaths = new HashSet<CablePath>();
		}
		if(this.measurementPaths == null) {
			this.measurementPaths = new HashSet<MeasurementPath>();
		}
	}

	public MapView(final IdlMapView mvt) throws CreateObjectException {
		try {
			this.fromTransferable(mvt);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	MapView(final Identifier id,
			final Identifier creatorId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final double scale,
			final double defaultScale,
			final Map map) {
		super(id, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), creatorId, creatorId, version, domainId);
		this.name = name;
		this.description = description;
		this.center = new DoublePoint(longitude, latitude);
		this.scale = scale;
		this.defaultScale = defaultScale;
		this.map = map;

		this.schemes = new HashSet<Scheme>();
	}
	
	public static MapView createInstance(final Identifier creatorId,
			final Identifier domainId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final double scale,
			final double defaultScale,
			final Map map) throws CreateObjectException {
		if (domainId == null || name == null || description == null || map == null)
			throw new IllegalArgumentException("Argument is 'null'");
		try {
			final MapView mapView = new MapView(IdentifierPool.getGeneratedIdentifier(ObjectEntities.MAPVIEW_CODE),
					creatorId,
					StorableObjectVersion.createInitial(),
					domainId,
					name,
					description,
					longitude,
					latitude,
					scale,
					defaultScale,
					map);
			mapView.markAsChanged();
			return mapView;
		} catch (IdentifierGenerationException ige) {
			throw new CreateObjectException("Cannot generate identifier ", ige);
		}
	}
	
	@Override
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {

		final IdlMapView mvt = (IdlMapView) transferable;
		super.fromTransferable(mvt, new Identifier(mvt.domainId));

		this.name = mvt.name;
		this.description = mvt.description;

		this.center = new DoublePoint(mvt.longitude, mvt.latitude);
		this.scale = mvt.scale;
		this.defaultScale = mvt.defaultScale;

		final Set<Identifier> schemeIds = Identifier.fromTransferables(mvt.schemeIds);

		final Identifier mapId = new Identifier(mvt.mapId);
		try {
			this.map = StorableObjectPool.getStorableObject(mapId, true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException("MapView.<init> | cannot get map " + mapId.toString(), ae);
		}

		try {
			this.schemes = new HashSet(StorableObjectPool.getStorableObjects(schemeIds, true));
		} catch (ApplicationException ae) {
			throw new CreateObjectException("MapView.<init> | cannot get schemes ", ae);
		}
	}

	@Override
	public Set<Identifiable> getDependencies() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.getDomainId());
		dependencies.add(this.map);
		dependencies.addAll(this.schemes);		
		return dependencies;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlMapView getTransferable(final ORB orb) {
		final IdlIdentifier[] schemeIdsTransferable = Identifier.createTransferables(this.schemes);		

		return IdlMapViewHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue(),
				this.getDomainId().getTransferable(),
				this.name,
				this.description,
				this.center.getX(),
				this.center.getY(),
				this.scale,
				this.defaultScale,
				this.map.getId().getTransferable(),
				schemeIdsTransferable);
	}

	/**
	 * �������� ������ ����.
	 * @return ������ ����
	 */
	public Set<Scheme> getSchemes() {
		return Collections.unmodifiableSet(this.schemes);
	}

	public void addScheme(final Scheme scheme) {
		this.schemes.add(scheme);
		super.markAsChanged();
	}

	public void removeScheme(final Scheme scheme) {
		this.schemes.remove(scheme);
		super.markAsChanged();
	}

	protected void setSchemes0(final Set<Scheme> schemes) {
		this.schemes.clear();
		if (schemes != null)
			this.schemes.addAll(schemes);
	}

	public void setSchemes(final Set<Scheme> schemeIds) {
		this.setSchemes0(schemeIds);
		super.markAsChanged();
	}

	/**
	 * �������� �������� ����.
	 * @return �������� ����
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * ���������� �������� ����.
	 * @param description �������� ����
	 */
	public void setDescription(final String description) {
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * �������� �������� ����.
	 * @return ��������
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���������� ����� �������� ����.
	 * @param name ����� ��������
	 */
	public void setName(final String name) {
		this.name = name;
		super.markAsChanged();
	}	

	protected synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final Identifier domainId,
			final String name,
			final String description,
			final double longitude,
			final double latitude,
			final double scale,
			final double defaultScale,
			final Map map) {
		super.setAttributes(created, modified, creatorId, modifierId, version, domainId);
		this.name = name;
		this.description = description;
		this.center.setLocation(longitude, latitude);
		this.scale = scale;
		this.defaultScale = defaultScale;
		this.map = map;
	}

	public double getDefaultScale() {
		return this.defaultScale;
	}

	public void setDefaultScale(final double defaultScale) {
		this.defaultScale = defaultScale;
		super.markAsChanged();
	}

	/**
	 * �������� �������������� �����.
	 * @return �������������� �����
	 * @todo Should return value taken from pool
	 */
	public Map getMap() {
		return this.map;
	}

	/**
	 * @todo Should return the field named mapId
	 */
	public Identifier getMapId() {
		return this.map == null
				? VOID_IDENTIFIER
				: this.map.getId();
	}

	/**
	 * ���������� �������������� �����.
	 * @param map �������������� �����
	 */
	public void setMap(final Map map) {
		this.map = map;
		super.markAsChanged();
	}

	/**
	 * ���������� ������� ����.
	 * @return �������
	 */
	public double getScale() {
		return this.scale;
	}

	/**
	 * ���������� ������� ����.
	 * @param scale �������
	 */
	public void setScale(final double scale) {
		this.scale = scale;
		super.markAsChanged();
	}

	/**
	 * ���������� ����������� ����� ���� � �������������� �����������.
	 * @param center ����� ����
	 */
	public void setCenter(final DoublePoint center) {
		this.center.setLocation(center);
	}

	/**
	 * �������� ����������� ����� ���� � �������������� �����������.
	 * @return ����� ����
	 */
	public DoublePoint getCenter() {
		return (DoublePoint) this.center.clone();
	}

	/**
	 * ���������� �������������� �������, � ������� ���������� ���������
	 * ������� ������.
	 *
	 * @param schemeCableLink ������
	 * @return ��������� ������� ������, ��� null, ���� ������� �� ������
	 * (�� ������� �� �����)
	 */
	public SiteNode getStartNode(final SchemeCableLink schemeCableLink) {
		try {
			for (final Scheme scheme : this.getSchemes()) {
				if (isSchemeCableLinkTopological(scheme, schemeCableLink)) {
					SchemeCablePort sourceAbstractSchemePort = schemeCableLink.getSourceAbstractSchemePort();
					if(sourceAbstractSchemePort == null) {
						// SchemeCableLink has no start device
						return null;
					}
					SchemeElement sourceSchemeElement = sourceAbstractSchemePort.getParentSchemeDevice().getParentSchemeElement();
					final SchemeElement se =
						getTopologicalSchemeElement(scheme, getTopLevelSchemeElement(sourceSchemeElement));
					return findElement(se);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������� �������������� �������, � ������� ���������� �������� �������
	 * ������.
	 * 
	 * @param schemeCableLink
	 *        ������
	 * @return �������� ������� ������, ��� null, ���� ������� �� ������ (��
	 *         ������� �� �����)
	 */
	public SiteNode getEndNode(final SchemeCableLink schemeCableLink) {
		try {
			for (final Scheme scheme : this.getSchemes()) {
				if(isSchemeCableLinkTopological(scheme, schemeCableLink)) {
					SchemeCablePort targetAbstractSchemePort = schemeCableLink.getTargetAbstractSchemePort();
					if(targetAbstractSchemePort == null) {
						// SchemeCableLink has no end device
						return null;
					}
					SchemeElement targetSchemeElement = targetAbstractSchemePort.getParentSchemeDevice().getParentSchemeElement();
					final SchemeElement se = 
						getTopologicalSchemeElement(scheme, getTopLevelSchemeElement(targetSchemeElement));
					return findElement(se);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������� �������������� �������, � ������� ���������� ��������� �������
	 * ����.
	 * 
	 * @param schemePath
	 *        ����
	 * @return ��������� ������� ����, ��� null, ���� ������� �� ������ (��
	 *         ������� �� �����)
	 */
	public SiteNode getStartNode(final SchemePath schemePath) {
		try {
			for (final Scheme scheme : this.getSchemes()) {
				if (isSchemePathTopological(scheme, schemePath)) {
					final SchemeElement se = 
						getTopologicalSchemeElement(
								scheme, 
								getTopLevelSchemeElement(
										schemePath.getStartSchemeElement()));
					return findElement(se);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������� �������������� �������, � ������� ���������� �������� �������
	 * ����.
	 * 
	 * @param schemePath
	 *        ����
	 * @return �������� ������� ����, ��� null, ���� ������� �� ������ (�� �������
	 *         �� �����)
	 */
	public SiteNode getEndNode(final SchemePath schemePath) {
		try {
			for (final Scheme scheme : this.getSchemes()) {
				if (isSchemePathTopological(scheme, schemePath)) {
					final SchemeElement se = 
						getTopologicalSchemeElement(
								scheme, 
								getTopLevelSchemeElement(
										schemePath.getEndSchemeElement()));
					return findElement(se);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ����� ������� �����, � �������� �������� ������ ������� �������.
	 * 
	 * @param schemeElement
	 *        ������� �����
	 * @return ����. null ���� ������� �� ������
	 */
	public SiteNode findElement(final SchemeElement schemeElement) {
		if (schemeElement == null)
			return null;
		this.initialize();
		for (final UnboundNode unboundNode : this.getUnboundNodes()) {
			if(unboundNode.getSchemeElement().equals(schemeElement)) {
				return unboundNode;
			}
		}
		for (final SiteNode siteNode : this.getMap().getAllSiteNodes()) {
			if (siteNode.equals(schemeElement.getSiteNode())) {
				return siteNode;
			}
		}
		return null;
	}

	/**
	 * ����� ������� ������ �� �����.
	 * @param schemeCableLink ������
	 * @return �������������� ������
	 */
	public CablePath findCablePath(final SchemeCableLink schemeCableLink) {
		this.initialize();
		for (final CablePath cablePath : this.getCablePaths()) {
			if (cablePath.getSchemeCableLink().equals(schemeCableLink)) {
				return cablePath;
			}
		}
		return null;
	}

	/**
	 * ����� �������������� ����, ��������������� �������� ����.
	 * @param schemePath ������� ����
	 * @return �������������� ����
	 */
	public MeasurementPath findMeasurementPath(final SchemePath schemePath) {
		this.initialize();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			if (measurementPath.getSchemePath().equals(schemePath)) {
				return measurementPath;
			}
		}
		return null;
	}

	public Set<UnboundNode> getUnboundNodes() {
		this.initialize();
		return this.unboundNodes;
	}

	public void addUnboundNode(final UnboundNode node) {
		this.initialize();
		this.unboundNodes.add(node);
	}
	
	public void removeUnboundNode(final UnboundNode node) {
		this.initialize();
		this.unboundNodes.remove(node);
	}
	
	public Set<UnboundLink> getUnboundLinks() {
		this.initialize();
		return this.unboundLinks;
	}

	public void addUnboundLink(final UnboundLink link) {
		this.initialize();
		this.unboundLinks.add(link);
	}
	
	public void removeUnboundLink(final UnboundLink link) {
		this.initialize();
		this.unboundLinks.remove(link);
	}
	
	public Set<NodeLink> getUnboundNodeLinks() {
		this.initialize();
		return this.unboundNodeLinks;
	}

	public void addUnboundNodeLink(final NodeLink link) {
		this.initialize();
		this.unboundNodeLinks.add(link);
	}
	
	public void removeUnboundNodeLink(final NodeLink link) {
		this.initialize();
		this.unboundNodeLinks.remove(link);
	}
	
	public Set<AbstractNode> getAllNodes() {
		if(this.nodeElements == null) {
			this.nodeElements = new HashSet<AbstractNode>();
		}
		this.nodeElements.clear();
		this.nodeElements.addAll(this.map.getNodes());
		this.nodeElements.addAll(this.getUnboundNodes());
		this.nodeElements.addAll(this.getMarkers());
		return Collections.unmodifiableSet(this.nodeElements);
	}
	
	public Set<PhysicalLink> getAllPhysicalLinks() {
		if(this.linkElements == null) {
			this.linkElements = new HashSet<PhysicalLink>();
		}
		this.linkElements.clear();
		this.linkElements.addAll(this.map.getAllPhysicalLinks());
		this.linkElements.addAll(this.getUnboundLinks());
		return Collections.unmodifiableSet(this.linkElements);
	}

	public Set<NodeLink> getAllNodeLinks() {
		if(this.nodeLinkElements == null) {
			this.nodeLinkElements = new HashSet<NodeLink>();
		}
		this.nodeLinkElements.clear();
		this.nodeLinkElements.addAll(this.map.getAllNodeLinks());
		this.nodeLinkElements.addAll(this.getUnboundNodeLinks());
		return Collections.unmodifiableSet(this.nodeLinkElements);
	}

	/**
	 * �������� ������ ���������� �����, ���������� �������� ����.
	 * @param node ����
	 * @return ������ ����������
	 */
	public Set<NodeLink> getNodeLinks(final AbstractNode node) {
		final Set<NodeLink> returnNodeLinks = new HashSet<NodeLink>();
		for (final NodeLink nodeLink : this.getAllNodeLinks()) {
			if ((nodeLink.getEndNode().equals(node)) || (nodeLink.getStartNode().equals(node))) {
				returnNodeLinks.add(nodeLink);
			}
		}

		return returnNodeLinks;
	}

	/**
	 * �������� ������ ����� �� ��������������� ������ ���� ���������� �����
	 * ������� ��������.
	 * 
	 * @param node
	 *        ����
	 * @return ������ �����
	 */
	public Set<AbstractNode> getOppositeNodes(final AbstractNode node) {
		final Set<AbstractNode> returnNodes = new HashSet<AbstractNode>();
		for (final NodeLink nodeLink : this.getNodeLinks(node)) {
			if (nodeLink.getEndNode().equals(node)) {
				returnNodes.add(nodeLink.getStartNode());
			}
			else {
				returnNodes.add(nodeLink.getEndNode());
			}
		}

		return returnNodes;
	}

	/**
	 * ���������� �������� �����, ���������� ������ ����, �� �� ������
	 * ����������� � ���������. ���� �������� � � �������� � ����� �����
	 * ����� �, �� ����� ������ <code>�.getOtherNodeLink(�)</code> ������ �, � �����
	 * <code>�.getOtherNodeLink(�)</code> ������ �. ����� �������, ��� ���������������
	 * ���� ���������� ������������ ���������������,
	 * ��� �������� ���� �� ����� ���� ���������, �� ���� ������� �����
	 * �� ������ �������������� � ���������� null
	 * @param node ����
	 * @param nodeLink �������� �����
	 * @return ������ �������� �����
	 */
	public NodeLink getOtherNodeLink(final AbstractNode node, NodeLink nodeLink) {
		if (!node.getClass().equals(TopologicalNode.class)) {
			return null;
		}

		NodeLink otherNodeLink = null;
		for (final NodeLink bufNodeLink : this.getNodeLinks(node)) {
			if (nodeLink != bufNodeLink) {
				otherNodeLink = bufNodeLink;
				break;
			}
		}

		return otherNodeLink;
	}

	/**
	 * �������� ������ �������������� �������.
	 * @return ������ �������������� �������
	 */
	public Set<CablePath> getCablePaths() {
		this.initialize();
		return Collections.unmodifiableSet(this.cablePaths);
	}

	/**
	 * �������� ����� �������������� ������.
	 * @param cablePath �������������� ������
	 */
	public void addCablePath(final CablePath cablePath) {
		this.initialize();
		this.cablePaths.add(cablePath);
	}

	/**
	 * ������� �������������� ������.
	 * @param cablePath �������������� ������
	 */
	public void removeCablePath(final CablePath cablePath) {
		this.initialize();
		this.cablePaths.remove(cablePath);
		this.map.setSelected(cablePath, false);
	}

	/**
	 * �������� ������ �������������� �������, ����������� �� ���������
	 * �����.
	 * @param physicalLink �����
	 * @return ������ �������������� �������
	 */
	public Set<CablePath> getCablePaths(final PhysicalLink physicalLink) {
		this.initialize();
		if(physicalLink instanceof UnboundLink) {
			return Collections.<CablePath>singleton(
					((UnboundLink)physicalLink).getCablePath());
		}
		final Set<CablePath> returnVector = new HashSet<CablePath>();
		for(Iterator iterator = physicalLink.getBinding().getBindObjects().iterator(); iterator.hasNext();) {
			CablePath cablePath = (CablePath) iterator.next();
			returnVector.add(cablePath);
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �������, ���������� ����� ��������� ����.
	 * @param node ����
	 * @return ������ �������������� �������
	 */
	public Set<CablePath> getCablePaths(final AbstractNode node) {
		final Set<CablePath> returnVector = new HashSet<CablePath>();
		for(PhysicalLink physicalLink : this.map.getPhysicalLinksAt(node)) {
			returnVector.addAll(this.getCablePaths(physicalLink));
		}
		return returnVector;
	}

	/**
	 * �������� ������ ����� ������������.
	 * @return ������ ����� ������������
	 */
	public Set<MeasurementPath> getMeasurementPaths() {
		this.initialize();
		return Collections.unmodifiableSet(this.measurementPaths);
	}

	/**
	 * �������� ����� ���� ������������.
	 * @param path ����� ���� ������������
	 */
	public void addMeasurementPath(final MeasurementPath path) {
		this.initialize();
		this.measurementPaths.add(path);
	}

	/**
	 * ������� ���� ������������.
	 * @param path ���� ������������
	 */
	public void removeMeasurementPath(final MeasurementPath path) {
		this.initialize();
		this.measurementPaths.remove(path);
		this.map.setSelected(path, false);
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ���������
	 * �������������� ������.
	 * @param cablePath �������������� ������
	 * @return ������ �������������� �����
	 */
	public List<MeasurementPath> getMeasurementPaths(final CablePath cablePath) {
		this.initialize();
		final LinkedList<MeasurementPath> returnVector = new LinkedList<MeasurementPath>();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			if (measurementPath.getSortedCablePaths().contains(cablePath)) {
				returnVector.add(measurementPath);
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� ����.
	 * 
	 * @param abstractNode
	 *        ����
	 * @return ������ �������������� �����
	 * @throws ApplicationException
	 */
	public List<MeasurementPath> getMeasurementPaths(final AbstractNode abstractNode) throws ApplicationException {
		this.initialize();
		final LinkedList<MeasurementPath> returnVector = new LinkedList<MeasurementPath>();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
				cablePath.sortNodes();
				if (cablePath.getSortedNodes().contains(abstractNode)) {
					returnVector.add(measurementPath);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * ������� ��� �������.
	 */
	public void removeMarkers() {
		this.initialize();
		for (final Marker marker : this.markers) {
			removeMarker(marker);
		}
	}

	/**
	 * ������� ���� ������������.
	 * @param marker ������
	 */
	public void removeMarker(final Marker marker) {
		this.initialize();
		this.markers.remove(marker);
		this.map.setSelected(marker, false);
	}

	/**
	 * �������� ��� �������.
	 * @return ������ ��������
	 */
	public Set<Marker> getMarkers() {
		this.initialize();
		return Collections.unmodifiableSet(this.markers);
	}

	/**
	 * �������� ����� ������.
	 * @param marker ������
	 */
	public void addMarker(final Marker marker) {
		this.initialize();
		this.markers.add(marker);
	}

	/**
	 * �������� ������ �� ��������������.
	 * @param markerId �������������
	 * @return ������
	 */
	public Marker getMarker(final Identifier markerId) {
		this.initialize();
		for (final Marker marker : this.markers) {
			if (marker.equals(markerId)) {
				return marker;
			}
		}
		return null;
	}

	/**
	 * Remove all temporary objects on mapview when mapview was edited and closed
	 * without saving.
	 */
	public void revert() {
		this.initialize();
		this.removeMarkers();
		this.unboundLinks.clear();
		this.unboundNodeLinks.clear();
		this.unboundNodes.clear();
	}

	public static SchemeElement getTopologicalSchemeElement(
			final Scheme scheme,
			final SchemeElement schemeElement)
	throws ApplicationException {
		Scheme parentScheme = schemeElement.getParentScheme();
		if(scheme.equals(parentScheme)) {
			return schemeElement;
		}
		if(parentScheme.getKind().value() == CABLE_SUBNETWORK.value()) {
			return schemeElement;
		}
		SchemeElement parentSchemeElement = parentScheme.getParentSchemeElement();
		if(parentSchemeElement == null) {
			return null;
		}
		return getTopologicalSchemeElement(scheme, parentSchemeElement);
	}

	public static boolean isSchemeElementTopological(final Scheme scheme,
			final SchemeElement schemeElement)
	throws ApplicationException {
		Scheme parentScheme = schemeElement.getParentScheme();
		if(scheme.equals(parentScheme)) {
			return true;
		}
		if(parentScheme.getKind().value() != CABLE_SUBNETWORK.value()) {
			return false;
		}
		SchemeElement parentSchemeElement = parentScheme.getParentSchemeElement();
		if(parentSchemeElement == null) {
			return false;
		}
		if(isSchemeElementTopological(scheme, parentSchemeElement)) {
			return true;
		}
		return false;
	}

	public static boolean isSchemeCableLinkTopological(final Scheme scheme,
			final SchemeCableLink schemeCableLink)
	throws ApplicationException {
		Scheme parentScheme = schemeCableLink.getParentScheme();
		if(scheme.equals(parentScheme)) {
			return true;
		}
		if(parentScheme.getKind().value() != CABLE_SUBNETWORK.value()) {
			return false;
		}
		SchemeElement parentSchemeElement = parentScheme.getParentSchemeElement();
		if(parentSchemeElement == null) {
			return false;
		}
		if(isSchemeElementTopological(scheme, parentSchemeElement)) {
			return true;
		}
		return false;
	}

	public static boolean isSchemePathTopological(final Scheme scheme,
			final SchemePath schemePath)
	throws ApplicationException {
		Scheme parentScheme = schemePath.getParentSchemeMonitoringSolution().getParentScheme();
		if(parentScheme == null) {
			return false;
		}
		if(scheme.equals(parentScheme)) {
			return true;
		}
		if(parentScheme.getKind().value() != CABLE_SUBNETWORK.value()) {
			return false;
		}
		SchemeElement parentSchemeElement = parentScheme.getParentSchemeElement();
		if(parentSchemeElement == null) {
			return false;
		}
		if(isSchemeElementTopological(scheme, parentSchemeElement)) {
			return true;
		}
		return false;
	}

	public static SchemeElement getTopLevelSchemeElement(SchemeElement schemeElement) {
		SchemeElement top = schemeElement;
		while (top.getParentSchemeElement() != null) {
			top = top.getParentSchemeElement();
		}
		return top;
	}

}
