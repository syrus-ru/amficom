/**
 * $Id: MapViewController.java,v 1.3 2005/01/30 15:38:18 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.RemoveNodeCommandAtomic;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeCableLinkCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.Client.Map.Command.Action.UnPlaceSchemePathCommand;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemeCableLink;
import com.syrus.AMFICOM.scheme.corba.SchemeElement;
import com.syrus.AMFICOM.scheme.corba.SchemePath;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import java.util.Collection;
import java.util.HashMap;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.Client.Map.Controllers.SiteNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.MapElementController;
import com.syrus.AMFICOM.Client.Map.Controllers.CollectorController;
import com.syrus.AMFICOM.Client.Map.Controllers.TopologicalNodeController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.PhysicalLinkController;
import com.syrus.AMFICOM.Client.Map.Controllers.MarkController;
import com.syrus.AMFICOM.Client.Map.Controllers.UnboundNodeController;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundLink;
import com.syrus.AMFICOM.Client.Map.mapview.UnboundNode;
import com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath;
import com.syrus.AMFICOM.Client.Map.mapview.Marker;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ����� ������������ ��� �������� � ���������� �� ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������
 * 
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/01/30 15:38:18 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class MapViewController
{
	/** ���-������� ������������ ��������� �����. */
	private static java.util.Map ctlMap = new HashMap();
	
	/** Instance. */
	private static MapViewController instance = null;
	
	/** ������ �� ���������� ����, �� ������� ������������ ���. */
	protected LogicalNetLayer logicalNetLayer = null;

	/** �������� ������. */
	private com.syrus.AMFICOM.mapview.MapView mapView;

	/** ������ �������. */
	protected List cablePaths = new LinkedList();
	
	/** ������ ������������� �����. */
	protected List measurementPaths = new LinkedList();
	
	/** ������ ��������. */
	protected List markers = new LinkedList();
	
	private MapViewController(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}
	
	public static MapViewController getInstance(LogicalNetLayer logicalNetLayer)
	{
		if(instance == null)
			instance = new MapViewController(logicalNetLayer);
		return instance;
	}

	static
	{
		ctlMap.put(com.syrus.AMFICOM.map.TopologicalNode.class,
			TopologicalNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.SiteNode.class,
			SiteNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.NodeLink.class,
			NodeLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.PhysicalLink.class,
			PhysicalLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Mark.class,
			MarkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.map.Collector.class,
			CollectorController.getInstance());

		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.CablePath.class,
			CableController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.MeasurementPath.class,
			MeasurementPathController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundNode.class,
			UnboundNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.UnboundLink.class,
			UnboundLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.Client.Map.mapview.Marker.class,
			MarkerController.getInstance());
	}

	public MapElementController getController(MapElement me)
	{
		MapElementController controller = (MapElementController)ctlMap.get(me.getClass());
		if(controller != null)
			controller.setLogicalNetLayer(logicalNetLayer);
		return controller;
	}


	/**
	 * ��������� ��������
	 */
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
		getController(me).paint(me, g, visibleBounds);
	}

	/**
	 * ���������� ����, �����������, ��� ����� currentMousePoint ���������
	 * � ������������ �������� ��������. ��� ���� ������� ������������
	 * ��������� ������, ��� ����� ������-������������ �����. ������ ��������
	 * ����� mouseTolerancy
	 */
	public boolean isMouseOnElement(MapElement me, Point currentMousePoint)
	{
		return getController(me).isMouseOnElement(me, currentMousePoint);
	}

	/**
	 * ����������, �������� �� ������� � ������� visibleBounds.
	 * ������������ ��� ��������� (������������ ������ ��������, ��������
	 * � ������� �������)
	 */
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * ����� ����������� ���������
	 */
	String getToolTipText(MapElement me)
	{
		return getController(me).getToolTipText(me);
	}


	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapViewPanel";

	/**
	 * �������� ��� ������, ������������ ������ ������� ����.
	 * @return ��� ������
	 */
	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	public void setMapView(com.syrus.AMFICOM.mapview.MapView mapView)
	{
		this.mapView = mapView;

		mapView.setLongitude(logicalNetLayer.getCenter().getX());
		mapView.setLatitude(logicalNetLayer.getCenter().getY());

		mapView.setScale(logicalNetLayer.getScale());

		revert();
	}

	/**
	 * �������� �������� ������ ����.
	 * @return �������� ������ ����
	 */
	public com.syrus.AMFICOM.mapview.MapView getMapView()
	{
		return mapView;
	}

	/**
	 * ����������� ��� �������� ������ ������� �������������.
	 * @param creatorId ������������� ������������
	 * @param domainId ������������� ������
	 * @param map ������ �� �������������� �����
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *  ��. {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *  	Identifier,
	 *		Identifier,
	 *		String,
	 *		String,
	 *		double,
	 *		double,
	 *		double,
	 *		double,
	 *		Map)}
	 */
	public static MapView createMapView(Identifier creatorId, Identifier domainId, Map map)
		throws CreateObjectException
	{
		MapView mapView = com.syrus.AMFICOM.mapview.MapView.createInstance(
			creatorId,
			domainId,
			LangModelMap.getString("New"),
			"",
			0.0D,
			0.0D,
			1.0D,
			1.0D,
			map);

		return mapView;
	}

	/**
	 * ����������� �����, ���������� � ���, �� ������� ������������ ��������� ����.
	 */
	public void scanSchemes()
	{
		for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
		{
			scanElements((Scheme )it.next());
		}
	}


	/**
	 * �������� ����� � ���.
	 * @param sch �����
	 */
	public void addScheme(Scheme sch)
	{
		mapView.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * ������ ����� �� ����.
	 * @param sch �����
	 */
	public void removeScheme(Scheme sch)
	{
		mapView.removeScheme(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	/**
	 * ������ ��� ����� �� ����. ����������� ������������ ���������
	 * ���� ���� �� ����, ��������� �������� ������ ���������
	 * ����� �������� � ���� ������������ ��������� ����� � �� ��������
	 * �� ����.
	 */
	public void removeSchemes()
	{
		while(mapView.getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )mapView.getSchemes().get(0);
			removeScheme(sch);
		}
	}

	/**
	 * ����������� ������� ����� �� ������� ��� �������� � �������� ����� ���
	 * ��������� ������ �������������� ��������.
	 * @param schemeElement ������� �����
	 */
	public void scanElement(SchemeElement schemeElement)
	{
		SiteNode node = findElement(schemeElement);
		if(node == null)
		{
			if(schemeElement.equipmentImpl() != null)
			{
				Equipment equipment = schemeElement.equipmentImpl();
				if(equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D)
				{
					placeElement(
						schemeElement, 
						new DoublePoint(
							equipment.getLongitude(), 
							equipment.getLatitude()));
				}
			}
		}
	}
	
	/**
	 * ����������� ��� �������� �����.
	 * @param scheme �����
	 */
	public void scanElements(Scheme scheme)
	{
		
		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();)
		{
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
	public void scanCable(SchemeCableLink schemeCableLink)
	{
		SiteNode[] mne = getSideNodes(schemeCableLink);
		CablePath cp = findCablePath(schemeCableLink);
		if(cp == null)
		{
			if(mne[0] != null && mne[1] != null)
			{
				placeElement(schemeCableLink);
			}
		}
		else
		{
			if(mne[0] == null || mne[1] == null)
			{
				unplaceElement(cp);
			}
			else
			{
				placeElement(schemeCableLink);
			}
		}
	}
	
	/**
	 * ����������� ��� ������ �����.
	 * @param scheme �����
	 */
	public void scanCables(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	/**
	 * ����������� ������� ���� �� ������� ��� �������� � ��������.
	 * @param schemePath ������� ����
	 */
	public void scanPath(SchemePath schemePath)
	{
		SiteNode[] mne = getSideNodes(schemePath);
		MeasurementPath mp = findMeasurementPath(schemePath);
		if(mp == null)
		{
			if(mne[0] != null && mne[1] != null)
			{
				placeElement(schemePath);
			}
		}
		else
		{
			if(mne[0] == null || mne[1] == null)
			{
				unplaceElement(mp);
			}
			else
			{
				placeElement(schemePath);
			}
		}
	}

	/**
	 * ����������� ��� ������� ���� �� �����.
	 * @param scheme �����
	 */
	public void scanPaths(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	/**
	 * ������ ��� ������� ���� �������� ����� �� ����.
	 * @param scheme �����
	 */
	public void removePaths(Scheme scheme)
	{
		Collection schemePaths = SchemeUtils.getTopologicalPaths(scheme);
		for(Iterator it = schemePaths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			MeasurementPath mp = findMeasurementPath(path);
			if(mp != null)
			{
				unplaceElement(mp);
			}
		}
	}

	/**
	 * ������ ��� ������ �������� ����� �� ����.
	 * @param scheme �����
	 */
	public void removeCables(Scheme scheme)
	{
		Collection schemeCables = SchemeUtils.getTopologicalCableLinks(scheme);
		for(Iterator it = schemeCables.iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			CablePath cp = findCablePath(scl);
			if(cp != null)
			{
				unplaceElement(cp);
			}
		}
	}

	/**
	 * ������ ��� �������� �������� ����� �� ����.
	 * @param scheme �����
	 */
	public void removeElements(Scheme scheme)
	{
		Collection schemeElements = SchemeUtils.getTopologicalElements(scheme);
		for(Iterator it = schemeElements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )it.next();
			SiteNode site = findElement(se);
			if(site != null)
			{
				if(site instanceof UnboundNode)
				{
					RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(site);
					cmd.setLogicalNetLayer(logicalNetLayer);
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
	public void placeElement(SchemeElement se, DoublePoint point)
	{
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ������ ������� ����� �� ����. ������������� ������� �� �������� �����
	 * ��� �������� �������������� ��������.
	 * @param node ������� �����
	 * @param se ������� �����
	 */
	public void unplaceElement(SiteNode node, SchemeElement se)
	{
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������ �� �������������� �����.
	 * @param scl ������
	 */
	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * ������ ������ �� ����. ������������� �������� �� ���� �������� 
	 * � �������� ������������� ����������.
	 * @param cp ������
	 */
	public void unplaceElement(CablePath cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������� ���� �� �����. ������������ ��� �������� 
	 * (drag/drop).
	 * @param sp ������� ����
	 */
	public void placeElement(SchemePath sp)
	{
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ������ �������������� ���� �� ����.
	 * @param mp �������������� ����
	 */
	public void unplaceElement(MeasurementPath mp)
	{
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ��������� ���������� � ��������� ����� �������������� ��������� ������
	 * �� ��������� �����, � ������� ��������� ��������� � �������� �������
	 * �������� ������.
	 * 
	 * @param cable �������������� ������
	 * @param scl ������� ������
	 */
	public void correctStartEndNodes(CablePath cable, SchemeCableLink scl)
	{
		SiteNode[] node = getSideNodes(scl);
		if(node[0] != null && node[1] != null)
		{
			cable.setStartNode(node[0]);
			cable.setEndNode(node[1]);
		}
	}
	
	/**
	 * ���� ������� �����������, ����� ������ ��� ��� ������ ������ 
	 * getSideNodes �� ���������� ����� ������ ������
	 */
	private static SiteNode[] linkSideNodes = new SiteNode[2];
	
	/**
	 * ���������� ������ �� ���� �������������� ���������, � ������� 
	 * ����������� �������� �������� ������. ���� ������� �� ������ (��
	 * ������� �� �����), ��������������� ������� ������� ����� null.
	 * 
	 * @param scl ������
	 * @return ������ �������� �����:
	 * 	linkSideNodes[0] - ��������� ����
	 *  linkSideNodes[1] - �������� ����
	 */
	public SiteNode[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalCableLinks(sch).contains(scl))
				{
					SchemeElement se = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.sourceAbstractSchemePort().schemeDevice()));
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = 
						SchemeUtils.getTopologicalElement(
							sch,
							SchemeUtils.getSchemeElementByDevice(sch, scl.targetAbstractSchemePort().schemeDevice()));
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}
	
	/**
	 * ���������� ������ �� ���� �������������� ���������, � ������� 
	 * ����������� �������� �������� �������� ����. ���� ������� �� ������ (��
	 * ������� �� �����), ��������������� ������� ������� ����� null.
	 * @param path ����
	 * @return ������ �������� �����:
	 * 	linkSideNodes[0] - ��������� ����
	 *  linkSideNodes[1] - �������� ����
	 */
	public SiteNode[] getSideNodes(SchemePath path)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = mapView.getSchemes().iterator(); it.hasNext();)
			{
				Scheme sch = (Scheme )it.next();
				if(SchemeUtils.getTopologicalPaths(sch).contains(path))
				{
					SchemeElement se = SchemeUtils.getTopologicalElement(
							sch,
							path.startDevice());
					linkSideNodes[0] = findElement(se);

					SchemeElement se2 = SchemeUtils.getTopologicalElement(
							sch,
							path.endDevice());
					linkSideNodes[1] = findElement(se2);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			linkSideNodes[0] = null;
			linkSideNodes[1] = null;
		}
		return linkSideNodes;
	}
	
	/**
	 * ����� ������� �����, � �������� �������� ������ ������� �������.
	 * @param se ������� �����
	 * @return ����.
	 * 	null ���� ������� �� ������
	 */
	public SiteNode findElement(SchemeElement se)
	{
		if(se == null)
			return null;
		for(Iterator it = mapView.getMap().getSiteNodes().iterator(); it.hasNext();)
		{
			SiteNode node = (SiteNode )it.next();
			if(node instanceof UnboundNode)
				if(((UnboundNode)node).getSchemeElement().equals(se))
					return node;
			if(se.siteNodeImpl() != null
				&& se.siteNodeImpl().equals(node))
						return node;
		}
		return null;
	}

	/**
	 * ����� ������� ������ �� �����.
	 * @param scl ������
	 * @return �������������� ������
	 */
	public CablePath findCablePath(SchemeCableLink scl)
	{

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getSchemeCableLink().equals(scl))
					return cp;
		}
		return null;
	}

	/**
	 * ����� �������������� ����, ��������������� �������� ����.
	 * @param path ������� ����
	 * @return �������������� ����
	 */
	public MeasurementPath findMeasurementPath(SchemePath path)
	{
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSchemePath().equals(path))
				return mp;
		}
		return null;
	}

	/**
	 * �������� ������ �������������� �������.
	 * @return ������ �������������� �������
	 */
	public List getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * �������� ����� �������������� ������.
	 * @param cable �������������� ������
	 */
	public void addCablePath(CablePath cable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addCablePath(" + cable + ")");

		cablePaths.add(cable);
	}

	/**
	 * ������� �������������� ������.
	 * @param cable �������������� ������
	 */
	public void removeCablePath(CablePath cable)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeCablePath(" + cable + ")");

		cablePaths.remove(cable);
		cable.setSelected(false);
	}

	/**
	 * �������� ������ �������������� �������, ����������� �� ���������
	 * �����.
	 * @param link �����
	 * @return ������ �������������� �������
	 */
	public List getCablePaths(PhysicalLink link)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + link + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			if(cp.getLinks().contains(link))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �������, ���������� ����� ��������� ����.
	 * @param node ����
	 * @return ������ �������������� �������
	 */
	public List getCablePaths(AbstractNode node)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + node + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(node))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �������, ���������� ����� ��������� 
	 * �������� �����.
	 * @param nodeLink �������� �����
	 * @return ������ �������������� �������
	 */
	public List getCablePaths(NodeLink nodeLink)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + nodeLink + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			CablePath cp = (CablePath)it.next();
			cp.sortNodeLinks();
			if(cp.getSortedNodeLinks().contains(nodeLink))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * �������� ������ ����� ������������.
	 * @return ������ ����� ������������
	 */
	public List getMeasurementPaths()
	{
		return measurementPaths;
	}

	/**
	 * �������� ����� ���� ������������.
	 * @param path ����� ���� ������������
	 */
	public void addMeasurementPath(MeasurementPath path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + path + ")");

		measurementPaths.add(path);
	}

	/**
	 * ������� ���� ������������.
	 * @param path ���� ������������
	 */
	public void removeMeasurementPath(MeasurementPath path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeTransmissionPath(" + path + ")");

		measurementPaths.remove(path);
		path.setSelected(false);
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� 
	 * �������������� ������.
	 * @param cpath �������������� ������
	 * @return ������ �������������� �����
	 */
	public List getMeasurementPaths(CablePath cpath)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + cpath + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			if(mp.getSortedCablePaths().contains(cpath))
				returnVector.add(mp);
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� �����.
	 * @param link �����
	 * @return ������ �������������� �����
	 */
	public List getMeasurementPaths(PhysicalLink link)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + link + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				if(cp.getLinks().contains(link))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� ����.
	 * @param node ����
	 * @return ������ �������������� �����
	 */
	public List getMeasurementPaths(AbstractNode node)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + node + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodes();
				if(cp.getSortedNodes().contains(node))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	/**
	 * �������� ������ �������������� �����, ���������� ����� ��������� 
	 * �������� �����.
	 * @param nodeLink �������� �����
	 * @return ������ �������������� �����
	 */
	public List getMeasurementPaths(NodeLink nodeLink)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + nodeLink + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MeasurementPath mp = (MeasurementPath)it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				CablePath cp = (CablePath)it2.next();
				cp.sortNodeLinks();
				if(cp.getSortedNodeLinks().contains(nodeLink))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
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
		throws CommunicationException, DatabaseException
	{
		MeasurementPath path = null;
		MonitoredElement me = (MonitoredElement )
			ConfigurationStorableObjectPool.getStorableObject(meId, true);
		if(me.getSort().equals(MonitoredElementSort.MONITOREDELEMENT_SORT_TRANSMISSION_PATH))
		{
			Identifier tpId = (Identifier )(me.getMonitoredDomainMemberIds().get(0));
			TransmissionPath tp = (TransmissionPath )
				ConfigurationStorableObjectPool.getStorableObject(tpId, true);
			if(tp != null)
			{
				for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
				{
					MeasurementPath mp = (MeasurementPath)it.next();
					if(mp.getSchemePath().pathImpl().equals(tp))
					{
						path = mp;
						break;
					}
				}
			}
		}

		return path;
	}

	/**
	 * ������� ��� �������.
	 */
	public void removeMarkers()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarkers()");
		mapView.getMap().getNodes().removeAll(markers);
		markers.clear();
	}

	/**
	 * ������� ���� ������������.
	 * @param marker ������
	 */
	public void removeMarker(Marker marker)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarker(" + marker + ")");

		markers.remove(marker);
		mapView.getMap().removeNode(marker);
		marker.setSelected(false);
	}

	/**
	 * �������� ��� �������.
	 * @return ������ ��������
	 */
	public List getMarkers()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarkers()");
		
		return markers;
	}

	/**
	 * �������� ����� ������.
	 * @param marker ������
	 */
	public void addMarker(Marker marker)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addMarker(" + marker + ")");

		markers.add(marker);
		mapView.getMap().addNode(marker);
	}

	/**
	 * �������� ������ �� ��������������.
	 * @param markerId �������������
	 * @return ������
	 */
	public Marker getMarker(Identifier markerId)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getMarker(" + markerId + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			Marker marker = (Marker)e.next();
			if ( marker.getId().equals(markerId))
				return marker;
		}
		return null;
	}

	/**
	 * �������� ������ ���� ��������� ��������� �����.
	 * @return ������ ���� �������������� ���������
	 */
	public List getAllElements()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getAllElements()");
		
		List returnVector = mapView.getMap().getAllElements();
		
		Iterator e;

		e = getCablePaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = getMeasurementPaths().iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		e = markers.iterator();
		while (e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			returnVector.add( mapElement);
		}

		return returnVector;
	}

	/**
	 * �������� ����� ���� ���������.
	 */
	public void deselectAll()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"deselectAll()");
		
		Iterator e = getAllElements().iterator();
		while ( e.hasNext())
		{
			MapElement mapElement = (MapElement)e.next();
			mapElement.setSelected(false);
		}
		mapView.getMap().clearSelection();
	}

	/**
	 * Remove all temporary objects on mapview when mapview was edited and
	 * closed without saving.
	 */
	public void revert()
	{
		removeMarkers();
	}

}
