/**
 * $Id: MapViewController.java,v 1.11 2005/02/08 15:11:10 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Controllers;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
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
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.mapview.UnboundNode;
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
import java.util.Iterator;

/**
 * ����� ������������ ��� ���������� ����������� � ���������������
 * ��������� ������� � ��������� ����� � ������ �������������� ��������.
 * @author $Author: krupenn $
 * @version $Revision: 1.11 $, $Date: 2005/02/08 15:11:10 $
 * @module mapviewclient_v1
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
	private MapView mapView;

	/**
	 * ��������� �����������. ������������ 
	 * {@link MapViewController#getInstance(LogicalNetLayer)}.
	 * @param logicalNetLayer ���������� ����
	 */
	private MapViewController(LogicalNetLayer logicalNetLayer)
	{
		this.logicalNetLayer = logicalNetLayer;
	}
	
	/**
	 * Instance getter.
	 * @param logicalNetLayer ���������� ����
	 * @return ���������� ����
	 */
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

		ctlMap.put(com.syrus.AMFICOM.mapview.CablePath.class,
			CableController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.MeasurementPath.class,
			MeasurementPathController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.UnboundNode.class,
			UnboundNodeController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.UnboundLink.class,
			UnboundLinkController.getInstance());
		ctlMap.put(com.syrus.AMFICOM.mapview.Marker.class,
			MarkerController.getInstance());
	}

	/**
	 * �������� ���������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ����������
	 */
	public MapElementController getController(MapElement me)
	{
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
	public void paint (MapElement me, Graphics g, Rectangle2D.Double visibleBounds)
	{
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
	{
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
	public boolean isElementVisible(MapElement me, Rectangle2D.Double visibleBounds)
	{
		return getController(me).isElementVisible(me, visibleBounds);
	}

	/**
	 * �������� ����� ����������� ��������� ��� �������� �����.
	 * @param me ������� �����
	 * @return ������ ��� ����������� ���������
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

	/**
	 * ���������� ���, � ������� ����� �������� ����������.
	 * @param mapView ���
	 */
	public void setMapView(com.syrus.AMFICOM.mapview.MapView mapView)
	{
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
	public void setMap(Map map)
	{
		this.mapView.setMap(map);
		scanSchemes();
	}

	/**
	 * �������� �������� ������ ����.
	 * @return �������� ������ ����
	 */
	public com.syrus.AMFICOM.mapview.MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * ����������� ��� �������� ������ ������� �������������.
	 * @param creatorId ������������� ������������
	 * @param domainId ������������� ������
	 * @param map ������ �� �������������� �����
	 * @return new MapView
	 * @throws com.syrus.AMFICOM.general.CreateObjectException
	 *    ��. {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *    	Identifier,
	 *  		Identifier,
	 *  		String,
	 *  		String,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		Map)}
	 * @deprecated use {@link com.syrus.AMFICOM.mapview.MapView#createInstance(
	 *    	Identifier,
	 *  		Identifier,
	 *  		String,
	 *  		String,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		double,
	 *  		Map)}
	 */
	public static MapView createMapView(Identifier creatorId, Identifier domainId, Map map)
		throws CreateObjectException
	{
		return com.syrus.AMFICOM.mapview.MapView.createInstance(
			creatorId,
			domainId,
			LangModelMap.getString("New"),
			"",
			0.0D,
			0.0D,
			1.0D,
			1.0D,
			map);
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
				for(Iterator it = this.mapView.getMeasurementPaths().iterator(); it.hasNext();)
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
	 * ����������� �����, ���������� � ���, �� ������� ������������ ��������� ����.
	 */
	public void scanSchemes()
	{
		for(Iterator it = this.mapView.getSchemes().iterator(); it.hasNext();)
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
		this.mapView.addScheme(sch);
		scanElements(sch);
	}

	/**
	 * ������ ����� �� ����.
	 * @param sch �����
	 */
	public void removeScheme(Scheme sch)
	{
		this.mapView.removeScheme(sch);
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
		while(this.mapView.getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )this.mapView.getSchemes().get(0);
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
		SiteNode node = this.mapView.findElement(schemeElement);
		if(node == null)
		{
			Equipment equipment = schemeElement.equipmentImpl();
			if(equipment != null 
				&& (equipment.getLongitude() != 0.0D
					|| equipment.getLatitude() != 0.0D) )
			{
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
		SiteNode cableStartNode = this.mapView.getStartNode(schemeCableLink);
		SiteNode cableEndNode = this.mapView.getEndNode(schemeCableLink);
		CablePath cp = this.mapView.findCablePath(schemeCableLink);
		if(cp == null)
		{
			if(cableStartNode != null && cableEndNode != null)
			{
				placeElement(schemeCableLink);
			}
		}
		else
		{
			if(cableStartNode == null || cableEndNode == null)
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
		SiteNode pathStartNode = this.mapView.getStartNode(schemePath);
		SiteNode pathEndNode = this.mapView.getEndNode(schemePath);
		MeasurementPath mp = this.mapView.findMeasurementPath(schemePath);
		if(mp == null)
		{
			if(pathStartNode != null && pathEndNode != null)
			{
				placeElement(schemePath);
			}
		}
		else
		{
			if(pathStartNode == null || pathEndNode == null)
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
			MeasurementPath mp = this.mapView.findMeasurementPath(path);
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
			CablePath cp = this.mapView.findCablePath(scl);
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
			SiteNode site = this.mapView.findElement(se);
			if(site != null)
			{
				if(site instanceof UnboundNode)
				{
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
	public void placeElement(SchemeElement se, DoublePoint point)
	{
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
	public void unplaceElement(SiteNode node, SchemeElement se)
	{
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������ �� �������������� �����.
	 * @param scl ������
	 */
	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}
	
	/**
	 * ������ ������ �� ����. ������������� �������� �� ���� �������� 
	 * � �������� ������������� ����������.
	 * @param cablePath ������
	 */
	public void unplaceElement(CablePath cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(this.logicalNetLayer);
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
		cmd.setLogicalNetLayer(this.logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ������ �������������� ���� �� ����.
	 * @param mp �������������� ����
	 */
	public void unplaceElement(MeasurementPath mp)
	{
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
