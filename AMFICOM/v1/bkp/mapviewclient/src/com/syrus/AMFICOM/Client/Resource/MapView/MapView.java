/**
 * $Id: MapView.java,v 1.24 2004/12/22 16:38:43 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 *
 * ���������: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Resource.MapView;

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
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ����� ������������ ��� �������� ��������, ������������ �� 
 * �������������� �����. ������� �������� � ����:
 * 		- ������ ����� Map, �� ���� ��������� ���������������
 *      - ����� ���������� ���� Scheme, ������� ��������� �� �������
 *        ���������
 * 
 * 
 * 
 * @version $Revision: 1.24 $, $Date: 2004/12/22 16:38:43 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public final class MapView
{
	private static final long serialVersionUID = 01L;

	protected Identifier id;
	protected String name = "��� ��������";
	protected String description;
	protected Identifier domainId;
	protected long created;
	protected long modified;
	protected Identifier createdBy;
	protected Identifier modifiedBy;

	protected List schemeIds = new LinkedList();
	protected double scale = 0.00001;
	protected double longitude = 0.0;
	protected double latitude = 0.0;
	
	protected LogicalNetLayer logicalNetLayer = null;
	
	protected boolean isOpened = false;

	protected boolean changed = false;

	protected Map map;
	protected List schemes = new LinkedList();

	protected List cablePaths = new LinkedList();
	protected List measurementPaths = new LinkedList();
	/** ������ �������� */
	protected List markers = new LinkedList();

	/**
	 * 
	 * @param logical comments
	 */
	public MapView(LogicalNetLayer logical)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView(" + logical + ")");
		setLogicalNetLayer(logical);
		created = System.currentTimeMillis();
	}

	/**
	 * constructor
	 */
	public MapView()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"MapView()");
		setLogicalNetLayer(null);
		created = System.currentTimeMillis();
	}

	/**
	 * ������
	 */
	public String getName()
	{
		return name;
	}
	
	public void setName(String name )
	{
		this.name = name;
	}
	
	/**
	 * ������
	 */
	public Identifier getId()
	{
		return id;
	}
	
	public void setId(Identifier id)
	{
		this.id = id;
	}

	/**
	 * ������
	 */
	public Identifier getDomainId()
	{
		return domainId;
	}
	
	public void setDomainId(Identifier domainId)
	{
		this.domainId = domainId;
	}
	
	/**
	 * ������
	 */
	public long getModified()
	{
		return modified;
	}

	public Map getMap()
	{
		return this.map;
	}
	
	public void setMap(Map mc)
	{
		this.map = mc;
	}
	
	public List getSchemes()
	{
		return this.schemes;
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapViewPanel";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}

	/**
	 * ���� ����, ��� �������� ������ � ���� �����
	 */
	public boolean isOpened()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"isOpened()");
		
		return this.isOpened;
	}
	
	/**
	 * ���������� ���� �������� ��� ����������� ���� � ���� �����
	 */
	public void setOpened(boolean opened)
	{
		this.isOpened = opened;
	}

	/**
	 * ���������� ����������� ����� ���� � �������������� �����������
	 */
	public double getScale()
	{
		return scale;
	}
	
	/**
	 * ���������� ����������� ����� ���� � �������������� �����������
	 */
	public void setScale(double scale)
	{
		this.scale = scale;
	}
	
	/**
	 * ���������� �������� �������, ������
	 */
	public void setLongLat( double longit, double latit)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLongLat(" + longit + ", " + latit + ")");
		
		longitude = longit;
		latitude = latit;
	}
	/**
	 * ���������� ����������� ����� ���� �����
	 */
	public void setCenter(DoublePoint center)
	{
		longitude = center.x;
		latitude = center.y;
	}

	/**
	 * �������� ����������� ����� ���� �����
	 */
	public DoublePoint getCenter()
	{
		return new DoublePoint(longitude, latitude);
	}

	/**
	 * ���������� ������ �� ����� ���������� �����, ��� ������������� �������� 
	 * ��������� � ���� �����
	 */
	public void setLogicalNetLayer(LogicalNetLayer logical)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setLogicalNetLayer(" + logical + ")");
		
		logicalNetLayer = logical;

		if(logicalNetLayer != null)
		{
			longitude = logicalNetLayer.getCenter().x;
			latitude = logicalNetLayer.getCenter().y;

			scale = logicalNetLayer.getScale();

			revert();

//			if(map != null)
//			{
//				map.setConverter(logicalNetLayer);
//			}
		}
	}

	/**
	 * �������� ������ �� ���� ���������� ����
	 */
	public LogicalNetLayer getLogicalNetLayer()
	{
		return logicalNetLayer;
	}

	public void addScheme(Scheme sch)
	{
		this.schemes.add(sch);
		scanElements(sch);
	}

	public void removeScheme(Scheme sch)
	{
		this.schemes.remove(sch);
		removePaths(sch);
		removeCables(sch);
		removeElements(sch);
	}

	public void removeSchemes()
	{
		while(getSchemes().size() != 0)
		{
			Scheme sch = (Scheme )getSchemes().get(0);
			removeScheme(sch);
		}
	}

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
	
	public void scanElements(Scheme scheme)
	{
		
		for(Iterator it = SchemeUtils.getTopologicalElements(scheme).iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement )it.next();
			scanElement(element);
		}
		scanCables(scheme);
	}

	public void scanCable(SchemeCableLink schemeCableLink)
	{
		SiteNode[] mne = getSideNodes(schemeCableLink);
		MapCablePathElement cp = findCablePath(schemeCableLink);
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
	
	public void scanCables(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalCableLinks(scheme).iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			scanCable(scl);
		}
		scanPaths(scheme);
	}

	public void scanPath(SchemePath schemePath)
	{
		SiteNode[] mne = getSideNodes(schemePath);
		MapMeasurementPathElement mp = findMeasurementPath(schemePath);
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

	public void scanPaths(Scheme scheme)
	{
		for(Iterator it = SchemeUtils.getTopologicalPaths(scheme).iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			scanPath(path);
		}
	}

	public void removePaths(Scheme scheme)
	{
		Collection schemePaths = SchemeUtils.getTopologicalPaths(scheme);
		for(Iterator it = schemePaths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath )it.next();
			MapMeasurementPathElement mp = findMeasurementPath(path);
			if(mp != null)
			{
				unplaceElement(mp);
			}
		}
	}

	public void removeCables(Scheme scheme)
	{
		Collection schemeCables = SchemeUtils.getTopologicalCableLinks(scheme);
		for(Iterator it = schemeCables.iterator(); it.hasNext();)
		{
			SchemeCableLink scl = (SchemeCableLink )it.next();
			MapCablePathElement cp = findCablePath(scl);
			if(cp != null)
			{
				unplaceElement(cp);
			}
		}
	}

	public void removeElements(Scheme scheme)
	{
		Collection schemeElements = SchemeUtils.getTopologicalElements(scheme);
		for(Iterator it = schemeElements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement )it.next();
			SiteNode site = findElement(se);
			if(site != null)
			{
				if(site instanceof MapUnboundNodeElement)
				{
					RemoveNodeCommandAtomic cmd = new RemoveNodeCommandAtomic(site);
					cmd.setLogicalNetLayer(logicalNetLayer);
					cmd.execute();
//					unplaceElement(site, se);
				}
			}
		}
	}

	/**
	 * ���������� ������� �� �����.
	 */
	public void placeElement(SchemeElement se, DoublePoint point)
	{
		PlaceSchemeElementCommand cmd = new PlaceSchemeElementCommand(se, point);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void unplaceElement(SiteNode node, SchemeElement se)
	{
		UnPlaceSchemeElementCommand cmd = new UnPlaceSchemeElementCommand(node, se);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void placeElement(SchemeCableLink scl)
	{
		PlaceSchemeCableLinkCommand cmd = new PlaceSchemeCableLinkCommand(scl);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}
	
	public void unplaceElement(MapCablePathElement cp)
	{
		UnPlaceSchemeCableLinkCommand cmd = new UnPlaceSchemeCableLinkCommand(cp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ���������� ������� sp ���� mtppe �� �����. ������������ ��� �������� 
	 * (drag/drop)
	 */
	public void placeElement(SchemePath sp)
	{
		PlaceSchemePathCommand cmd = new PlaceSchemePathCommand(sp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	public void unplaceElement(MapMeasurementPathElement mp)
	{
		UnPlaceSchemePathCommand cmd = new UnPlaceSchemePathCommand(mp);
		cmd.setLogicalNetLayer(logicalNetLayer);
		cmd.execute();
	}

	/**
	 * ��������� ���������� � ��������� ����� �������������� ��������� ������
	 * �� ��������� �����, � ������� ��������� ��������� � �������� �������
	 * �������� ������
	 * 
	 * @param mcpe
	 * @param scl
	 */
	public void correctStartEndNodes(MapCablePathElement mcpe, SchemeCableLink scl)
	{
		SiteNode[] mne = getSideNodes(scl);
		if(mne[0] != null && mne[1] != null)
		{
			mcpe.setStartNode(mne[0]);
			mcpe.setEndNode(mne[1]);
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
	 * ������� �� �����), ��������������� ������� ������� ����� null
	 * 
	 * @param scl
	 * @return 
	 * 	linkSideNodes[0] - ��������� ����
	 *  linkSideNodes[1] - �������� ����
	 */
	public SiteNode[] getSideNodes(SchemeCableLink scl)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
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
	
	public SiteNode[] getSideNodes(SchemePath path)
	{
		linkSideNodes[0] = null;
		linkSideNodes[1] = null;
		try
		{	
			for(Iterator it = getSchemes().iterator(); it.hasNext();)
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
	 * @param se
	 * @return 
	 * 	null ���� ������� �� ������
	 */
	public SiteNode findElement(SchemeElement se)
	{
		if(se == null)
			return null;
		for(Iterator it = getMap().getSiteNodes().iterator(); it.hasNext();)
		{
			SiteNode node = (SiteNode )it.next();
			if(node instanceof MapUnboundNodeElement)
				if(((MapUnboundNodeElement)node).getSchemeElement().equals(se))
					return node;
			if(se.siteNodeImpl() != null
				&& se.siteNodeImpl().equals(node))
						return node;
		}
		return null;
	}

	/**
	 * ����� ������� � ��������������� id �� �����.
	 */
	public MapCablePathElement findCablePath(SchemeCableLink scl)
	{

		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			if(cp.getSchemeCableLink().equals(scl))
					return cp;
		}
		return null;
	}

	public MapMeasurementPathElement findMeasurementPath(SchemePath path)
	{
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			if(mp.getSchemePath().equals(path))
				return mp;
		}
		return null;
	}

	/**
	 * �������� ������ ����� ������������
	 */
	public List getCablePaths()
	{
		return cablePaths;
	}

	/**
	 * �������� ����� ���� ������������
	 */
	public void addCablePath(MapCablePathElement ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + ob + ")");

		cablePaths.add(ob);
	}

	/**
	 * ������� ���� ������������
	 */
	public void removeCablePath(MapCablePathElement ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeCablePath(" + ob + ")");

		cablePaths.remove(ob);
		ob.setSelected(false);
//		removedElements.add(ob);
	}

	public List getCablePaths(PhysicalLink mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			if(cp.getLinks().contains(mple))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(AbstractNode mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			cp.sortNodes();
			if(cp.getSortedNodes().contains(mne))
				returnVector.add(cp);
		}
		return returnVector;
	}

	public List getCablePaths(NodeLink mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getCablePaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cp = (MapCablePathElement)it.next();
			cp.sortNodeLinks();
			if(cp.getSortedNodeLinks().contains(mnle))
				returnVector.add(cp);
		}
		return returnVector;
	}

	/**
	 * �������� ������ ����� ������������
	 */
	public List getMeasurementPaths()
	{
		return measurementPaths;
	}

	/**
	 * �������� ����� ���� ������������
	 */
	public void addMeasurementPath(MapMeasurementPathElement ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addTransmissionPath(" + ob + ")");

		measurementPaths.add(ob);
	}

	/**
	 * ������� ���� ������������
	 */
	public void removeMeasurementPath(MapMeasurementPathElement ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeTransmissionPath(" + ob + ")");

		measurementPaths.remove(ob);
		ob.setSelected(false);
//		removedElements.add(ob);
	}

	public List getMeasurementPaths(MapCablePathElement cpath)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + cpath + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			if(mp.getSortedCablePaths().contains(cpath))
				returnVector.add(mp);
		}
		return returnVector;
	}

	public List getMeasurementPaths(PhysicalLink mple)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mple + ")");
		
		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
				if(cp.getLinks().contains(mple))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public List getMeasurementPaths(AbstractNode mne)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mne + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getCablePaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
				cp.sortNodes();
				if(cp.getSortedNodes().contains(mne))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public List getMeasurementPaths(NodeLink mnle)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getPaths(" + mnle + ")");

		LinkedList returnVector = new LinkedList();
		for(Iterator it = getMeasurementPaths().iterator(); it.hasNext();)
		{
			MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
			for(Iterator it2 = mp.getSortedCablePaths().iterator(); it2.hasNext();)
			{
				MapCablePathElement cp = (MapCablePathElement)it2.next();
				cp.sortNodeLinks();
				if(cp.getSortedNodeLinks().contains(mnle))
				{
					returnVector.add(mp);
					break;
				}
			}
		}
		return returnVector;
	}

	public MapMeasurementPathElement getMeasurementPathByMonitoredElementId(Identifier meId)
	{
		MapMeasurementPathElement path = null;
		try
		{
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
						MapMeasurementPathElement mp = (MapMeasurementPathElement )it.next();
						if(mp.getSchemePath().pathImpl().equals(tp))
						{
							path = mp;
							break;
						}
					}
				}
			}
		}
		catch (CommunicationException e)
		{
		}
		catch (DatabaseException e)
		{
		}

		return path;
	}

	/**
	 * ������� ��� �������
	 */
	public void removeMarkers()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarkers()");
		getMap().getNodes().removeAll(markers);
		markers.clear();
	}

	/**
	 * ������� ���� ������������
	 */
	public void removeMarker(MapMarker ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"removeMarker(" + ob + ")");

		markers.remove(ob);
		getMap().removeNode(ob);
		ob.setSelected(false);
	}

	/**
	 * �������� ��� �������
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
	 * �������� ����� ���� ������������
	 */
	public void addMarker(MapMarker ob)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"addMarker(" + ob + ")");

		markers.add(ob);
		getMap().addNode(ob);
	}

	/**
	 * �������� ������ �� ID
	 */
	public MapMarker getMarker(Identifier markerID)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getMarker(" + markerID + ")");
		
		Iterator e = markers.iterator();
		while( e.hasNext())
		{
			MapMarker marker = (MapMarker)e.next();
			if ( marker.getId().equals(markerID))
				return marker;
		}
		return null;
	}

	/**
	 * �������� ������ ���� ��������� ��������� �����
	 */
	public List getAllElements()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"getAllElements()");
		
		List returnVector = getMap().getAllElements();
		
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
	 * �������� ����� ���� ���������
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
		getMap().clearSelection();
	}

	/**
	 * remove all temporary objects on mapview when mapview was edited and
	 * closed without saving
	 */
	public void revert()
	{
		removeMarkers();
	}
	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getDescription()
	{
		return description;
	}

	public Identifier getCreatedBy()
	{
		return createdBy;
	}


	public long getCreated()
	{
		return created;
	}


	public void setChanged(boolean changed)
	{
		this.changed = changed;
	}


	public boolean isChanged()
	{
		return changed;
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

/*	public Object clone(DataSourceInterface dataSource)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"clone(" + dataSource + ")");
		
		MapView mv = new MapView();
		mv.id = dataSource.GetUId(com.syrus.AMFICOM.Client.Resource.MapView.MapView.typ);
		mv.name = name + "(copy)";

		mv.description = description;
		mv.domainId = domainId;
		mv.mapId = mapId;
		mv.schemeIds = new LinkedList();
		for(Iterator it = schemes.iterator(); it.hasNext();)
			mv.schemeIds.add(((Scheme )it.next()).getId());

		mv.createdBy = dataSource.getSession().getUserId();
		mv.modified = mv.created;
		mv.modifiedBy = mv.createdBy;

		mv.scale = scale;
		mv.longitude = longitude;
		mv.latitude = latitude;

		mv.markers = new LinkedList();

		Pool.put(MapView.typ, mv.getId(), mv);
		
		return mv;
	}
*/

}