/**
 * $Id: MeasurementPath.java,v 1.2 2005/01/13 15:16:24 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.mapview;

import com.syrus.AMFICOM.configuration.Characteristic;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LocalIdentifierGenerator;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.PathElementPackage.Type;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import com.syrus.AMFICOM.Client.Map.mapview.CablePath;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

/**
 * элемент пути 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/13 15:16:24 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MeasurementPath implements MapElement
{
	private static final long serialVersionUID = 02L;

	protected Identifier id;

	protected String	name;

	protected String	description;

	protected List		characteristics;


	protected transient boolean selected = false;

	protected transient boolean alarmState = false;

	protected transient boolean removed = false;

	protected transient Map map = null;

	private AbstractNode					startNode;
	private AbstractNode					endNode;

	protected SchemePath schemePath;
	protected Scheme scheme;

	protected MapView mapView;

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public MeasurementPath(
			SchemePath schemePath,
			Identifier id, 
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.setId(id);
		this.setName(schemePath.name());
		if(mapView != null)
		{
			map = mapView.getMap();
		}
		setStartNode(stNode);
		setEndNode(eNode);
		this.characteristics = new LinkedList();
		
		setSchemePath(schemePath);
	}

	public static MeasurementPath createInstance(
			SchemePath schemePath,
			AbstractNode stNode, 
			AbstractNode eNode, 
			MapView mapView)
		throws CreateObjectException 
	{
		if (stNode == null || mapView == null || eNode == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
			return new MeasurementPath(
				schemePath,
				ide,
				stNode, 
				eNode, 
				mapView);
		}
		catch (IllegalObjectEntityException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
		catch (IdentifierGenerationException e)
		{
			throw new CreateObjectException("MapMeasurementPathElement.createInstance | cannot generate identifier ", e);
		}
	}

	public void setEndNode(AbstractNode endNode) 
	{
		throw new UnsupportedOperationException();
	}
	
	public void setStartNode(AbstractNode startNode) 
	{
		throw new UnsupportedOperationException();
	}
	
	public List getCharacteristics() 
	{
		return Collections.unmodifiableList(this.characteristics);
	}

	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
	}

	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
	}
	
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	public Identifier getId()
	{
		return id;
	}

	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getDescription() 
	{
		return this.description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
	}

	public void setAlarmState(boolean alarmState)
	{
		this.alarmState = alarmState;
	}

	public boolean getAlarmState()
	{
		return alarmState;
	}

	protected DoublePoint location = new DoublePoint(0.0, 0.0);

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
		location.setLocation(x /= count, y /= count);
		
		return location;
	}


	public void setLocation(DoublePoint location)
	{
		throw new UnsupportedOperationException();
	}

	public boolean isRemoved()
	{
		return removed;
	}

	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
		this.name = schemePath.name();
		this.scheme = schemePath.scheme();
	}

	public SchemePath getSchemePath()
	{
		return schemePath;
	}

	/**
	 * @deprecated
	 */
	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	//Возвращает топологическую длинну в метрах
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

	public double getLengthLf()
	{
		return SchemeUtils.getPhysicalLength(schemePath);
	}

	public double getLengthLo()
	{
		return SchemeUtils.getOpticalLength(schemePath);
	}

	public MapElement getMapElement(PathElement pe)
	{
		MapElement me = null;
		switch(pe.type().value())
		{
			case Type._SCHEME_ELEMENT:
				SchemeElement se = (SchemeElement )pe.abstractSchemeElement();
				SiteNode site = mapView.findElement(se);
				if(site != null)
				{
					me = site;
				}
				break;
			case Type._SCHEME_LINK:
				SchemeLink link = (SchemeLink )pe.abstractSchemeElement();
				SchemeElement sse = SchemeUtils.getSchemeElementByDevice(scheme, link.sourceSchemePort().schemeDevice());
				SchemeElement ese = SchemeUtils.getSchemeElementByDevice(scheme, link.targetSchemePort().schemeDevice());
				SiteNode ssite = mapView.findElement(sse);
				SiteNode esite = mapView.findElement(ese);
				if(ssite != null && ssite.equals(esite))
				{
					me = ssite;
				}
				break;
			case Type._SCHEME_CABLE_LINK:
				SchemeCableLink clink = (SchemeCableLink )pe.abstractSchemeElement();
				CablePath cp = mapView.findCablePath(clink);
				if(cp != null)
				{
					me = cp;
				}
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return me;
	}

	// to avoid instantiation of multiple objects
	protected List unsortedCablePaths = new LinkedList();

	protected List getCablePaths()
	{
		synchronized(unsortedCablePaths)
		{
			unsortedCablePaths.clear();
			for(int i = 0; i < schemePath.links().length; i++)
			{
				PathElement pe = (PathElement )schemePath.links()[i];
				switch(pe.type().value())
				{
					case Type._SCHEME_ELEMENT:
						SchemeElement se = (SchemeElement )pe.abstractSchemeElement();
						SiteNode site = mapView.findElement(se);
						if(site != null)
						{
		//					mPath.addCablePath(site);
						}
						break;
					case Type._SCHEME_LINK:
						SchemeLink link = (SchemeLink )pe.abstractSchemeElement();
						SchemeElement sse = SchemeUtils.getSchemeElementByDevice(scheme, link.sourceSchemePort().schemeDevice());
						SchemeElement ese = SchemeUtils.getSchemeElementByDevice(scheme, link.targetSchemePort().schemeDevice());
						SiteNode ssite = mapView.findElement(sse);
						SiteNode esite = mapView.findElement(ese);
						if(ssite == esite)
						{
		//					mPath.addCablePath(ssite);
						}
						break;
					case Type._SCHEME_CABLE_LINK:
						SchemeCableLink clink = (SchemeCableLink )pe.abstractSchemeElement();
						CablePath cp = mapView.findCablePath(clink);
						if(cp != null)
						{
							unsortedCablePaths.add(cp);
						}
						break;
					default:
						throw new UnsupportedOperationException();
				}
			}
		}
		return unsortedCablePaths;
	}

	// to avoid instantiation of multiple objects
	protected List sortedCablePaths = new LinkedList();
	// to avoid instantiation of multiple objects
	protected List sortedNodeLinks = new LinkedList();
	// to avoid instantiation of multiple objects
	protected List sortedNodes = new LinkedList();

	public List getSortedNodeLinks()
	{
		return sortedNodeLinks;
	}
	
	public List getSortedNodes()
	{
		return sortedNodes;
	}
	
	public List getSortedCablePaths()
	{
		return sortedCablePaths;
	}
	
	public AbstractNode getStartNode()
	{
		SiteNode[] mne = getMapView().getSideNodes(this.getSchemePath());
		
		return mne[0];
	}
	
	public AbstractNode getEndNode()
	{
		SiteNode[] mne = getMapView().getSideNodes(this.getSchemePath());
		
		return mne[1];
	}
	
	public void sortPathElements()
	{
		sortedCablePaths.clear();
		sortedNodeLinks.clear();
		sortedNodes.clear();
		
		AbstractNode node = getStartNode();

		sortedCablePaths.addAll(getCablePaths());
		
		for(Iterator it = sortedCablePaths.iterator(); it.hasNext();)
		{
			CablePath cpath = (CablePath)it.next();
			cpath.sortNodeLinks();
			if(cpath.getStartNode().equals(node))
			{
				sortedNodeLinks.addAll(cpath.getSortedNodeLinks());
				sortedNodes.addAll(cpath.getSortedNodes());
			}
			else
			{
				for(ListIterator lit = cpath.getSortedNodeLinks().listIterator(cpath.getSortedNodeLinks().size()); lit.hasPrevious();)
				{
					sortedNodeLinks.add(lit.previous());
				}
				for(ListIterator lit = cpath.getSortedNodes().listIterator(cpath.getSortedNodes().size()); lit.hasPrevious();)
				{
					sortedNodes.add(lit.previous());
				}
			}
			node = cpath.getOtherNode(node);

			// to avoid duplicate entry
			sortedNodes.remove(node);
		}
		sortedNodes.add(node);
	}

	public Identifier getMonitoredElementId()
	{
		Identifier meid = null;
		MonitoredElement me = getMonitoredElement();
		if(me != null)
			meid = me.getId();
		return meid;
	}

	public MonitoredElement getMonitoredElement()
	{
		MonitoredElement me = null;
		try
		{
			TransmissionPath tp = getSchemePath().pathImpl();

			me = (MonitoredElement )
				ConfigurationStorableObjectPool.getStorableObject(
						(Identifier )(tp.getMonitoredElementIds().get(0)), 
						true);

		}
		catch (CommunicationException e)
		{
			e.printStackTrace();
		}
		catch (DatabaseException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return me;
	}

	public NodeLink nextNodeLink(NodeLink nl)
	{
		int index = getSortedNodeLinks().indexOf(nl);
		if(index == getSortedNodeLinks().size() - 1)
			return null;
		else
			return (NodeLink)getSortedNodeLinks().get(index + 1);
	}

	public NodeLink previousNodeLink(NodeLink nl)
	{
		int index = getSortedNodeLinks().indexOf(nl);
		if(index == 0)
			return null;
		else
			return (NodeLink)getSortedNodeLinks().get(index - 1);
	}

	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	public Object[][] exportColumns()
	{
		throw new UnsupportedOperationException();
	}
}
