/**
 * $Id: MeasurementPath.java,v 1.1 2005/02/01 09:28:17 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.general.Characteristic;
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
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeUtils;
import com.syrus.AMFICOM.scheme.corba.Scheme;
import com.syrus.AMFICOM.scheme.corba.SchemePath;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Элемент пути.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.1 $, $Date: 2005/02/01 09:28:17 $
 * @module mapviewclient_v1
 * @todo getCablePaths(), getStartNode(), getEndNode()
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

	protected SchemePath schemePath;
	protected Scheme scheme;

	protected MapView mapView;

	/**
	 * Конструктор.
	 * @param schemePath схемный путь
	 * @param id идентификатор
	 * @param mapView вид
	 */
	protected MeasurementPath(
			SchemePath schemePath,
			Identifier id, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.schemePath = schemePath;
		this.name = schemePath.name();
		if(mapView != null)
		{
			map = mapView.getMap();
		}
		this.characteristics = new LinkedList();
	}

	public static MeasurementPath createInstance(
			SchemePath schemePath,
			MapView mapView)
		throws CreateObjectException 
	{
		if (mapView == null || schemePath == null)
			throw new IllegalArgumentException("Argument is 'null'");
		
		try
		{
			Identifier ide =
				LocalIdentifierGenerator.generateIdentifier(ObjectEntities.SITE_NODE_ENTITY_CODE);
//				IdentifierPool.getGeneratedIdentifier(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE);
			return new MeasurementPath(
				schemePath,
				ide,
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

	/**
	 * {@inheritDoc}
	 */
	public List getCharacteristics() 
	{
		return Collections.unmodifiableList(this.characteristics);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addCharacteristic(Characteristic ch)
	{
		this.characteristics.add(ch);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		this.characteristics.remove(ch);
	}
	
	public void setId(Identifier id)
	{
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	public Map getMap()
	{
		return map;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setMap(Map map)
	{
		this.map = map;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		getMap().setSelected(this, selected);
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
		return alarmState;
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
		location.setLocation(x /= count, y /= count);
		
		return location;
	}


	/**
	 * {@inheritDoc}
	 */
	public boolean isRemoved()
	{
		return removed;
	}

	/**
	 * {@inheritDoc}
	 */
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

	// to avoid instantiation of multiple objects
	protected List unsortedCablePaths = new LinkedList();

	protected List getCablePaths()
	{
/*
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
*/
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
//		SiteNode[] mne = getMapView().getSideNodes(this.getSchemePath());
		SiteNode[] mne = new SiteNode[2];
		
		return mne[0];
	}
	
	public AbstractNode getEndNode()
	{
//		SiteNode[] mne = getMapView().getSideNodes(this.getSchemePath());
		SiteNode[] mne = new SiteNode[2];
		
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

	/**
	 * {@inheritDoc}
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
