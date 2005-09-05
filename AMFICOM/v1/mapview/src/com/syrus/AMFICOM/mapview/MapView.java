/*
* $Id: MapView.java,v 1.55 2005/09/05 12:25:40 krupenn Exp $
*
* Copyright � 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
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
import com.syrus.AMFICOM.mapview.corba.IdlMapView;
import com.syrus.AMFICOM.mapview.corba.IdlMapViewHelper;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;

/**
 * ����� ������������ ��� �������� ��������, ������������ ��
 * �������������� �����. ������� �������� � ����:
 * <br>&#9;- ������ �������������� ����� {@link Map}, �� ���� ���������
 * ���������������
 * <br>&#9;- ����� ���������� ���� {@link Scheme}, ������� ��������� �� ������
 * �������������� �����
 * @author $Author: krupenn $
 * @version $Revision: 1.55 $, $Date: 2005/09/05 12:25:40 $
 * @module mapview
 * @todo use getCenter, setCenter instead of pair longitude, latitude
 */
public final class MapView extends DomainMember implements Namable {

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

	/** ������ �������. */
	protected transient Set<CablePath> cablePaths = new HashSet<CablePath>();
	
	/** ������ ������������� �����. */
	protected transient Set<MeasurementPath> measurementPaths = new HashSet<MeasurementPath>();
	
	/** ������ ��������. */
	protected transient Set<Marker> markers = new HashSet<Marker>();

	/**
	 * ������������� ������ ���� ��������� �������������� �����
	 */
	protected transient List<MapElement> allElements;

	MapView(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);

		try {
			DatabaseContext.getDatabase(ObjectEntities.MAPVIEW_CODE).retrieve(this);
		} catch (IllegalDataException e) {
			throw new RetrieveObjectException(e.getMessage(), e);
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
		this.allElements = new LinkedList<MapElement>();
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
			this.schemes = StorableObjectPool.getStorableObjects(schemeIds, true);
		} catch (ApplicationException ae) {
			throw new CreateObjectException("MapView.<init> | cannot get schemes ", ae);
		}

		this.allElements = new LinkedList<MapElement>();
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
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
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

	public double getLatitude() {
		return this.center.getX();
	}

	public void setLatitude(final double latitude) {
		this.center.setLocation(this.center.getX(), latitude);
		super.markAsChanged();
	}

	public double getLongitude() {
		return this.center.getY();
	}

	public void setLongitude(final double longitude) {
		this.center.setLocation(longitude, this.center.getY());
		super.markAsChanged();
	}

	/**
	 * �������� �������������� �����.
	 * @return �������������� �����
	 */
	public Map getMap() {
		return this.map;
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
				if (SchemeUtils.getTopologicalCableLinks(scheme).contains(schemeCableLink)) {
					SchemeCablePort sourceAbstractSchemePort = schemeCableLink.getSourceAbstractSchemePort();
					if(sourceAbstractSchemePort == null) {
						// SchemeCableLink has no start device
						return null;
					}
					final SchemeElement se = SchemeUtils.getTopologicalElement(
							scheme, 
							sourceAbstractSchemePort.getParentSchemeDevice().getParentSchemeElement());
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
				if (SchemeUtils.getTopologicalCableLinks(scheme).contains(schemeCableLink)) {
					SchemeCablePort targetAbstractSchemePort = schemeCableLink.getTargetAbstractSchemePort();
					if(targetAbstractSchemePort == null) {
						// SchemeCableLink has no end device
						return null;
					}
					final SchemeElement se = SchemeUtils.getTopologicalElement(
							scheme, 
							targetAbstractSchemePort.getParentSchemeDevice().getParentSchemeElement());
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
				if (scheme.getTopologicalPaths().contains(schemePath)) {
					final SchemeElement se = SchemeUtils.getTopologicalElement(scheme, schemePath.getStartSchemeElement());
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
				if (scheme.getTopologicalPaths().contains(schemePath)) {
					final SchemeElement se = SchemeUtils.getTopologicalElement(scheme, schemePath.getEndSchemeElement());
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
		for (final SiteNode siteNode : this.getMap().getAllSiteNodes()) {
			if (siteNode instanceof UnboundNode) {
				if (((UnboundNode) siteNode).getSchemeElement().equals(schemeElement)) {
					return siteNode;
				}
			}
			if (schemeElement.getSiteNode() != null && schemeElement.getSiteNode().equals(siteNode)) {
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
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			if (measurementPath.getSchemePath().equals(schemePath)) {
				return measurementPath;
			}
		}
		return null;
	}

	/**
	 * �������� ������ �������������� �������.
	 * @return ������ �������������� �������
	 */
	public Set<CablePath> getCablePaths() {
		return Collections.unmodifiableSet(this.cablePaths);
	}

	/**
	 * �������� ����� �������������� ������.
	 * @param cablePath �������������� ������
	 */
	public void addCablePath(final CablePath cablePath) {
		this.cablePaths.add(cablePath);
	}

	/**
	 * ������� �������������� ������.
	 * @param cablePath �������������� ������
	 */
	public void removeCablePath(final CablePath cablePath) {
		this.cablePaths.remove(cablePath);
		this.map.setSelected(cablePath, false);
	}

	/**
	 * �������� ������ �������������� �������, ����������� �� ���������
	 * �����.
	 * @param physicalLink �����
	 * @return ������ �������������� �������
	 */
	public List<CablePath> getCablePaths(final PhysicalLink physicalLink) {
		final LinkedList<CablePath> returnVector = new LinkedList<CablePath>();
		for (final CablePath cablePath : this.getCablePaths()) {
			if (cablePath.getLinks().contains(physicalLink)) {
				returnVector.add(cablePath);
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �������, ���������� ����� ��������� ����.
	 * @param node ����
	 * @return ������ �������������� �������
	 */
	public List<CablePath> getCablePaths(final AbstractNode node) {
		final LinkedList<CablePath> returnVector = new LinkedList<CablePath>();
		for (final CablePath cablePath : this.getCablePaths()) {
			cablePath.sortNodes();
			if (cablePath.getSortedNodes().contains(node)) {
				returnVector.add(cablePath);
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �������, ���������� ����� ���������
	 * �������� �����.
	 * @param nodeLink �������� �����
	 * @return ������ �������������� �������
	 */
	public List<CablePath> getCablePaths(final NodeLink nodeLink) {
		final LinkedList<CablePath> returnVector = new LinkedList<CablePath>();
		for (final CablePath cablePath : this.getCablePaths()) {
			cablePath.sortNodeLinks();
			if (cablePath.getSortedNodeLinks().contains(nodeLink)) {
				returnVector.add(cablePath);
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ ����� ������������.
	 * @return ������ ����� ������������
	 */
	public Set<MeasurementPath> getMeasurementPaths() {
		return Collections.unmodifiableSet(this.measurementPaths);
	}

	/**
	 * �������� ����� ���� ������������.
	 * @param path ����� ���� ������������
	 */
	public void addMeasurementPath(final MeasurementPath path) {
		this.measurementPaths.add(path);
	}

	/**
	 * ������� ���� ������������.
	 * @param path ���� ������������
	 */
	public void removeMeasurementPath(final MeasurementPath path) {
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
		final LinkedList<MeasurementPath> returnVector = new LinkedList<MeasurementPath>();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			if (measurementPath.getSortedCablePaths().contains(cablePath)) {
				returnVector.add(measurementPath);
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� �����.
	 * @param physicalLink �����
	 * @return ������ �������������� �����
	 */
	public List<MeasurementPath> getMeasurementPaths(final PhysicalLink physicalLink) {
		final LinkedList<MeasurementPath> returnVector = new LinkedList<MeasurementPath>();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
				if (cablePath.getLinks().contains(physicalLink)) {
					returnVector.add(measurementPath);
					break;
				}
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
	 */
	public List<MeasurementPath> getMeasurementPaths(final AbstractNode abstractNode) {
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
	 * �������� ������ �������������� �����, ���������� ����� ��������� ��������
	 * �����.
	 * 
	 * @param nodeLink
	 *        �������� �����
	 * @return ������ �������������� �����
	 */
	public List<MeasurementPath> getMeasurementPaths(final NodeLink nodeLink) {
		final LinkedList<MeasurementPath> returnVector = new LinkedList<MeasurementPath>();
		for (final MeasurementPath measurementPath : this.getMeasurementPaths()) {
			for (final CablePath cablePath : measurementPath.getSortedCablePaths()) {
				cablePath.sortNodeLinks();
				if (cablePath.getSortedNodeLinks().contains(nodeLink)) {
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
		for (final Marker marker : this.markers) {
			removeMarker(marker);
		}
	}

	/**
	 * ������� ���� ������������.
	 * @param marker ������
	 */
	public void removeMarker(final Marker marker) {
		this.markers.remove(marker);
		this.map.removeNode(marker);
		this.map.setSelected(marker, false);
	}

	/**
	 * �������� ��� �������.
	 * @return ������ ��������
	 */
	public Set<Marker> getMarkers() {
		return Collections.unmodifiableSet(this.markers);
	}

	/**
	 * �������� ����� ������.
	 * @param marker ������
	 */
	public void addMarker(final Marker marker) {
		this.markers.add(marker);
		this.getMap().addNode(marker);
	}

	/**
	 * �������� ������ �� ��������������.
	 * @param markerId �������������
	 * @return ������
	 */
	public Marker getMarker(final Identifier markerId) {
		for (final Marker marker : this.markers) {
			if (marker.getId().equals(markerId)) {
				return marker;
			}
		}
		return null;
	}

	/**
	 * �������� ������ ���� ��������� ��������� �����.
	 * @return ������ ���� �������������� ���������
	 */
	public List<MapElement> getAllElements() {
		this.allElements.clear();

		this.allElements.addAll(this.getMap().getAllElements());
		this.allElements.addAll(this.getCablePaths());
		this.allElements.addAll(this.getMeasurementPaths());
		this.allElements.addAll(this.markers);

		return Collections.unmodifiableList(this.allElements);
	}

	/**
	 * Remove all temporary objects on mapview when mapview was edited and closed
	 * without saving.
	 */
	public void revert() {
		this.removeMarkers();
	}

}
