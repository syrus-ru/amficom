/**
 * $Id: MapViewController.java,v 1.25 2005/05/27 15:14:56 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.Marker;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.mapview.UnboundNode;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.SchemeUtils;

/**
 * ����� ������������ ��� ���������� ����������� � ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.25 $, $Date: 2005/05/27 15:14:56 $
 * @module mapviewclient_v1
 */
public final class MapViewController {
	public static final String ELEMENT_SITENODE = "sitenode";
	public static final String ELEMENT_WELL = "well";
	public static final String ELEMENT_PIQUET = "piquet";
	public static final String ELEMENT_PHYSICALLINK = "physicallink";
	public static final String ELEMENT_COLLECTOR = "collector";
	public static final String ELEMENT_CABLEPATH = "cablepath";
	public static final String ELEMENT_TOPOLOGICALNODE = "topologicalnode";
	public static final String ELEMENT_MARK = "mark";
	public static final String ELEMENT_MEASUREMENTPATH = "measurementpath";
	public static final String ELEMENT_MARKER = "marker";
	public static final String ELEMENT_CABLEINLET = "cableinlet";
	public static final String ELEMENT_NODELINK = "nodelink";

	public static String getMapElementReadableType(MapElement mapElement) {
		if(mapElement instanceof SiteNode) {
			SiteNode site = (SiteNode )mapElement;
			return ((SiteNodeType )site.getType()).getName();
		}
		return LangModelMap.getString(MapViewController.getMapElementType(mapElement));
	}

	public static String getMapElementType(MapElement mapElement)
	{
		if(mapElement instanceof SiteNode)
			return MapViewController.ELEMENT_SITENODE;
		else if(mapElement instanceof PhysicalLink)
			return MapViewController.ELEMENT_PHYSICALLINK;
		else if(mapElement instanceof Collector)
			return MapViewController.ELEMENT_COLLECTOR;
		else if(mapElement instanceof CablePath)
			return MapViewController.ELEMENT_CABLEPATH;
		else if(mapElement instanceof TopologicalNode)
			return MapViewController.ELEMENT_TOPOLOGICALNODE;
		else if(mapElement instanceof Mark)
			return MapViewController.ELEMENT_MARK;
		else if(mapElement instanceof MeasurementPath)
			return MapViewController.ELEMENT_MEASUREMENTPATH;
		else if(mapElement instanceof Marker)
			return MapViewController.ELEMENT_MARKER;
		else if(mapElement instanceof NodeLink)
			return MapViewController.ELEMENT_NODELINK;
		return "";
	}

	/** ���-������� ������������ ��������� �����. */
	private static java.util.Map ctlMap = new HashMap();
	
	/** Instance. */
	private static MapViewController instance = null;
	
	/** ������ �� ���������� ����, �� ������� ������������ ���. */
	protected LogicalNetLayer logicalNetLayer = null;

	/** �������� ������. */
	private MapView mapView;

	/**
	 * ��������� �����������. ������������ 
	 * {@link MapViewController#getInstance(LogicalNetLayer)}.
	 * @param logicalNetLayer ���������� ����
	 */
	private MapViewController(LogicalNetLayer logicalNetLayer) {
		this.logicalNetLayer = logicalNetLayer;
	}
	
	/**
	 * Instance getter.
	 * @param logicalNetLayer ���������� ����
	 * @return ���������� ����
	 */
	public static MapViewController getInstance(LogicalNetLayer logicalNetLayer) {
		if(instance == null)
			instance = new MapViewController(logicalNetLayer);
		return instance;
	}

	static {
		ctlMap.put(TopologicalNode.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(SiteNode.class,
			SiteNodeController.getInstance());
		ctlMap.put(NodeLink.class,
			NodeLinkController.getInstance());
		ctlMap.put(PhysicalLink.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(Mark.class,
			MarkController.getInstance());
		ctlMap.put(Collector.class,
			CollectorController.getInstance());

		ctlMap.put(CablePath.class,
			CableController.getInstance());
		ctlMap.put(MeasurementPath.class,
			MeasurementPathController.getInstance());
		ctlMap.put(UnboundNode.class,
			UnboundNodeController.getInstance());
		ctlMap.put(UnboundLink.class,
			UnboundLinkController.getInstance());
		ctlMap.put(Marker.class,
			MarkerController.getInstance());
	}

	/**
	 * �������� ���������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ����������
	 */
	public MapElementController getController(MapElement me) {
		MapElementController controller = (MapElementController)ctlMap.get(me.getClass());
		if(controller != null)
			controller.setLogicalNetLayer(this.logicalNetLayer);
		return controller;
	}


	/**
	 * ���������� �������. ��� ��������� ���������� ����������� �������� 
	 * ��������� �������� ����� � ������� �������, � ������������ ������
	 * � ���� ������.
	 * @param me ������� �����, ������� ���������� ����������
	 * @param g ����������� ��������
	 * @param visibleBounds ������� ������
	 */
	public void paint(
			MapElement me,
			Graphics g,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� {@link com.syrus.AMFICOM.Client.Map.MapPropertiesManager#getMouseTolerancy()}.
	 * @param me ������� �����
	 * @param currentMousePoint ����� � �������� �����������
	 * @return <code>true</code>, ���� ����� �� �������� �����, ����� 
	 * <code>false</code>
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
			throws MapConnectionException, MapDataException {
		return getController(me).isMouseOnElement(me, currentMousePoint);
	}

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������).
	 * @param me ������� �����
	 * @param visibleBounds ������� ������
	 * @return <code>true</code>, ���� ������� �������� � �������, ����� 
	 * <code>false</code>
	 */
	public boolean isElementVisible(
			MapElement me,
			Rectangle2D.Double visibleBounds)
			throws MapConnectionException, MapDataException {
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * �������� ����� ����������� ��������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ������ ��� ����������� ���������
	 */
	String getToolTipText(MapElement me) {
		return getController(me).getToolTipText(me);
	}

	/**
	 * ���������� ���, � ������� ����� �������� ����������.
	 * @param mapView ���
	 */
	public void setMapView(com.syrus.AMFICOM.mapview.MapView mapView)
			throws MapConnectionException, MapDataException {
		this.mapView = mapView;

		this.mapView.setLongitude(this.logicalNetLayer.getCenter().getX());
		this.mapView.setLatitude(this.logicalNetLayer.getCenter().getY());

		this.mapView.setScale(this.logicalNetLayer.getScale());

		this.mapView.revert();
	}
	
	/**
	 * ���������� �������������� �����.
	 * @param map �������������� �����
	 */
	public void setMap(Map map) {
		this.mapView.setMap(map);
		scanSchemes();
	}

	/**
	 * �������� �������� ������ ����.
	 * @return �������� ������ ����
	 */
	public MapView getMapView() {
		return this.mapView;
	}

	/**
	 * �������� �������������� ���� �� �������������� �������������� ��������.
	 * @param meId ������������� �������������� ��������
	 * @return �������������� ����
	 * @throws com.syrus.AMFICOM.general.CommunicationException 
	 *  ��. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 * @throws com.syrus.AMFICOM.general.DatabaseException
	 *  ��. {@link ConfigurationStorableObjectPool#getStorableObject(Identifier, boolean)}
	 */
	public MeasurementPath getMeasurementPathByMonitoredElementId(Identifier meId)
			throws ApplicationException {
		MeasurementPath path = null;
		MonitoredElement me = (MonitoredElement )
			StorableObjectPool.getStorableObject(meId, true);
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH)) {
			Identifier tpId = (Identifier )(me.getMonitoredDomainMemberIds().iterator().next());
			TransmissionPath tp = (TransmissionPath )
				StorableObjectPool.getStorableObject(tpId, true);
			if(tp != null) {
				for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();) {
					MeasurementPath mp = (MeasurementPath)it.next();
					if(mp.getSchemePath().getTransmissionPath().equals(tp)) {
						path = mp;
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * ����������� �����, ���������� � ���, �� ������� ������������ ��������� ����.
	 */
	public void scanSchemes() {
		for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();) {
			scanElements((Scheme )it.next());
		}
	}


	/**
	 * �������� ����� � ���.
	 * 
	 * @param sch �����
	 */
	public void addScheme(Scheme sch) {
		this.mapView.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * ������ ����� �� ����.
	 * 
	 * @param sch �����
	 */
	public void removeScheme(Scheme sch) {
		this.mapView.removeScheme(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	/**
	 * ������ ��� ����� �� ����. ����������� ������������ ��������� ���� ���� ��
	 * ����, ��������� �������� ������ ��������� ����� �������� � ����
	 * ������������ ��������� ����� � �� �������� �� ����.
	 */
	public void removeSchemes() {
		while(!this.mapView.getSchemes().isEmpty()) {
			Scheme sch = (Scheme )this.mapView.getSchemes().iterator().next();
			removeScheme(sch);
		}
	}

	/**
	 * ����������� ������� ����� �� ������� ��� �������� � �������� ����� ���
	 * ��������� ������ �������������� ��������.
	 * 
	 * @param schemeElement ������� �����
	 */
	public void scanElement(SchemeElement schemeElement) {
		SiteNode node = this.mapView.findElement(schemeElement);
		if(node == null) {
			Equipment equipment = schemeElement.getEquipment();
			if(equipment != null 
				&& (equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D) ) {
				placeElement(
					schemeElement, 
					new DoublePoint(
						equipment.getLongitude(), 
						equipment.getLatitude()));
			}
		}
	}
	
	/**
	 * ����������� ��� �������� �����.
	 * @param scheme �����
	 */
	public void scanElements(Scheme scheme) {

		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();) {
			SchemeElement element = (SchemeElement )it.next();
			scanElement(element);
		}
		scanCables(scheme);
	}

	/**
	 * ����������� ������ �� ������� ��� �������� � ������� ��� ���������
	 * ������ �������������� ������ �� �������������� �����.
	 * @param schemeCableLink ������
	 */
	public void scanCable(SchemeCableLink schemeCableLink) {
		SiteNode cableStartNode = this.mapView.getStartNode(schemeCableLink);
		SiteNode cableEndNode = this.mapView.getEndNode(schemeCableLink);
		CablePath cp = this.mapView.findCablePath(schemeCableLink);
		if(cp == null) {
			if(cableStartNode != null && cableEndNode != null) {
				placeElement(schemeCableLink);
			}
		}
		else {
			if(cableStartNode == null || cableEndNode == null) {
				unplaceElement(cp);
			}
			else {
				placeElement(schemeCableLink);
			}
		}
	}
	
	/**
	 * ����������� ��� ������ �����.
	 * 
	 * @param scheme �����
	 */
	public void scanCables(Scheme scheme) {
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();) {
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	/**
	 * ����������� ������� ���� �� ������� ��� �������� � ��������.
	 * @param schemePath ������� ����
	 */
	public void scanPath(SchemePath schemePath) {
		SiteNode pathStartNode = this.mapView.getStartNode(schemePath);
		SiteNode pathEndNode = this.mapView.getEndNode(schemePath);
		MeasurementPath mp = this.mapView.findMeasurementPath(schemePath);
		if(mp == null) {
			if(pathStartNode != null && pathEndNode != null) {
				placeElement(schemePath);
			}
		}
		else {
		if(pathStartNode == null || pathEndNode == null) {
			unplaceElement(mp);
		}
		else {
			placeElement(schemePath);
		}
		}
	}

	/**
	 * ����������� ��� ������� ���� �� �����.
	 * 
	 * @param scheme �����
	 */
	public void scanPaths(Scheme scheme) {
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();) {
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	/**
	 * ������ ��� ������� ���� �������� ����� �� ����.
	 * @param scheme �����
	 */
	public void removePaths(Scheme scheme) {
		Collection schemePaths = SchemeUtils.getTopologicalPaths(scheme);
		for(Iterator it = schemePaths.iterator(); it.hasNext();) {
			SchemePath path = (SchemePath )it.next();
			MeasurementPath mp = this.mapView.findMeasurementPath(path);
			if(mp != null) {
				unplaceElement(mp);
			}
		}
	}

	/**
	 * ������ ��� ������ �������� ����� �� ����.
	 * 
	 * @param scheme �����
	 */
	public void removeCables(Scheme scheme) {
		Collection schemeCables = SchemeUtils.getTopologicalCableLinks(scheme);
		for(Iterator it = schemeCables.iterator(); it.hasNext();) {
			SchemeCableLink scl = (SchemeCableLink )it.next();
			CablePath cp = this.mapView.findCablePath(scl);
			if(cp != null) {
				unplaceElement(cp);
			}
		}
	}

	/**
	 * ������ ��� �������� �������� ����� �� ����.
	 * 
	 * @param scheme �����
	 */
	public void removeElements(Scheme scheme) {
		Collection schemeElements = SchemeUtils.getTopologicalElements(scheme);
		for(Iterator it = schemeElements.iterator(); it.hasNext();) {
			SchemeElement se = (SchemeElement )it.next();
			SiteNode site = this.mapView.findElement(se);
			if(site != null) {
				if(site instanceof UnboundNode) {
					RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(site);
					cmd.setLogicalNetLayer(this.logicalNetLayer);
					cmd.execute();
				}
			}
		}
	}

	/**
	 * ���������� ������� �� �����.
	 * @param se ������� �����
	 * @param point �������������� �����
	 */
	public void placeElement(SchemeElement se, DoublePoint point) {
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ������ ������� ����� �� ����. ������������� ������� �� �������� �����
	 * ��� �������� �������������� ��������.
	 * @param node ������� �����
	 * @param se ������� �����
	 */
	public void unplaceElement(SiteNode node, SchemeElement se) {
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������ �� �������������� �����.
	 * @param scl ������
	 */
	public void placeElement(SchemeCableLink scl) {
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * ������ ������ �� ����. ������������� �������� �� ���� �������� 
	 * � �������� ������������� ����������.
	 * @param cablePath ������
	 */
	public void unplaceElement(CablePath cablePath) {
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cablePath);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������� ���� �� �����. ������������ ��� �������� 
	 * (drag/drop).
	 * @param sp ������� ����
	 */
	public void placeElement(SchemePath sp) {
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ������ �������������� ���� �� ����.
	 * @param mp �������������� ����
	 */
	public void unplaceElement(MeasurementPath mp) {
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

/* from SiteNode

	//���������� ������ ����� ������ ������� ����,
	//������������� �� ����������� �������������� ��������
	public PathElement countPhysicalLength(SchemePath sp, PathElement pe, Enumeration pathelements)
	{
		physical_length = 0.0;

		if(this.elementId == null || this.elementId.equals(""))
			return pe;
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, this.elementId);

		Vector vec = new Vector();
		Enumeration e = se.getAllSchemesLinks();
		for(;e.hasMoreElements();)
			vec.add(e.nextElement());

		for(;;)
		{
			if(pe.is_cable)
			{
				SchemeCableLink schemeCableLink =
						(SchemeCableLink)Pool.get(SchemeCableLink.typ, pe.linkId);
				if(schemeCableLink == null)
				{
					System.out.println("Something wrong... - schemeCableLink == null");
					return pe;
				}
				if(!vec.contains(schemeCableLink))
					return pe;
				physical_length += schemeCableLink.getPhysicalLength();
			}
			else
			{
				SchemeLink schemeLink =
						(SchemeLink )Pool.get(SchemeLink.typ, pe.linkId);
				if(schemeLink == null)
				{
					System.out.println("Something wrong... - schemeLink == null");
					return pe;
				}
				if(!vec.contains(schemeLink))
					return pe;
				physical_length += schemeLink.getPhysicalLength();
			}
			if(pathelements.hasMoreElements())
				pe = (PathElement )pathelements.nextElement();
			else
				return null;
		}

		return pe;//stub
	}
*/	
}