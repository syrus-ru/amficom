/**
 * $Id: MapMeasurementPathElement.java,v 1.13 2004/12/07 17:05:54 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.MonitoredElementSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * элемент пути 
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/12/07 17:05:54 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapMeasurementPathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapmeasurementpathelement";

	protected SchemePath schemePath;
	protected Scheme scheme;

	protected String mapViewId = "";
	
	protected MapView mapView;

	public Object clone(DataSourceInterface dataSource)
		throws CloneNotSupportedException
	{
		throw new UnsupportedOperationException();
	}

	public String[][] getExportColumns()
	{
		throw new UnsupportedOperationException();
	}

	public void setColumn(String field, String value)
	{
		throw new UnsupportedOperationException();
	}

	public MapMeasurementPathElement(
			SchemePath schemePath,
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.setId(id);
		this.setName(schemePath.getName());
		if(mapView != null)
		{
			mapViewId = mapView.getId();
			map = mapView.getMap();
			if(map != null)
				mapId = map.getId();
		}
		setStartNode(stNode);
		setEndNode(eNode);
		attributes = new HashMap();
		
		setSchemePath(schemePath);
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	//этот класс используется для востановления данных из базы
	/**
	 * @deprecated
	 */
	public void setLocalFromTransferable()
	{
		throw new UnsupportedOperationException();
	}

	//этот класс используется для отпрвки данных в базу
	/**
	 * @deprecated
	 */
	public void setTransferableFromLocal()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * @deprecated
	 */
	public String getTyp()
	{
		return typ;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	//Используется для загрузкт данных из базы
	/**
	 * @deprecated
	 */
	public void updateLocalFromTransferable()
	{
		this.startNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, startNodeId);
		if(this.startNode == null)
			this.startNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, startNodeId);

		this.endNode = (MapNodeElement )
				Pool.get(MapSiteNodeElement.typ, endNodeId);
		if(this.endNode == null)
			this.endNode = (MapNodeElement )
					Pool.get(MapPhysicalNodeElement.typ, endNodeId);
		this.mapView = (MapView)Pool.get(MapView.typ, this.mapViewId);
	}

	private static final String PROPERTY_PANE_CLASS_NAME = "";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public void setSchemePath(SchemePath schemePath)
	{
		this.schemePath = schemePath;
		this.name = schemePath.getName();
		this.scheme = (Scheme )Pool.get(Scheme.typ, schemePath.getSchemeId());
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

	/**
	 * @deprecated
	 */
	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		boolean vis = false;
		for(Iterator it = getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			if(cpath.isVisible(visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	/**
	 * @deprecated
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isVisible(visibleBounds))
			return;

		for(Iterator it = getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
			cpath.paint(g, visibleBounds, stroke, color, selectionVisible);
		}
	}

	/**
	 * @deprecated
	 */
	public void paint(Graphics g, Rectangle2D.Double visibleBounds)
	{
		if(!isVisible(visibleBounds))
			return;

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = this.getColor();

		paint(g, visibleBounds, str, color, isSelectionVisible());
	}

	/**
	 * @deprecated
	 */
	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		for(Iterator it = getSortedCablePaths().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isMouseOnThisObject(currentMousePoint))
				return true;
		}
		return false;
	}
	
	/**
	 * @deprecated
	 */
	public Object getTransferable()
	{
		return null;
	}

	//Возвращает топологическую длинну в метрах
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getSortedCablePaths().iterator();
		while( e.hasNext())
		{
			MapCablePathElement cpath = (MapCablePathElement )e.next();
			length = length + cpath.getLengthLt();
		}
		return length;
	}

	public double getLengthLf()
	{
		return schemePath.getPhysicalLength();
	}

	public double getLengthLo()
	{
		return schemePath.getOpticalLength();
	}

	public MapElement getMapElement(PathElement pe)
	{
		MapElement me = null;
		if(pe.getType() == PathElement.SCHEME_ELEMENT)
		{
			SchemeElement se = (SchemeElement )pe.getSchemeElement();
			MapSiteNodeElement site = mapView.findElement(se);
			if(site != null)
			{
				me = site;
			}
		}
		else
		if(pe.getType() == PathElement.LINK)
		{
			SchemeLink link = (SchemeLink )pe.getSchemeLink();
			SchemeElement sse = scheme.getSchemeElementByPort(link.sourcePortId);
			SchemeElement ese = scheme.getSchemeElementByPort(link.targetPortId);
			MapSiteNodeElement ssite = mapView.findElement(sse);
			MapSiteNodeElement esite = mapView.findElement(ese);
			if(ssite != null && ssite == esite)
			{
				me = ssite;
			}
		}
		else
		if(pe.getType() == PathElement.CABLE_LINK)
		{
			SchemeCableLink clink = (SchemeCableLink )pe.getSchemeCableLink();
			MapCablePathElement cp = mapView.findCablePath(clink);
			if(cp != null)
			{
				me = cp;
			}
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
			for(Iterator it = schemePath.links.iterator(); it.hasNext();)
			{
				PathElement pe = (PathElement )it.next();
				if(pe.getType() == PathElement.SCHEME_ELEMENT)
				{
					SchemeElement se = (SchemeElement )pe.getSchemeElement();
					MapSiteNodeElement site = mapView.findElement(se);
					if(site != null)
					{
	//					mPath.addCablePath(site);
					}
				}
				else
				if(pe.getType() == PathElement.LINK)
				{
					SchemeLink link = (SchemeLink )pe.getSchemeLink();
					SchemeElement sse = scheme.getSchemeElementByPort(link.sourcePortId);
					SchemeElement ese = scheme.getSchemeElementByPort(link.targetPortId);
					MapSiteNodeElement ssite = mapView.findElement(sse);
					MapSiteNodeElement esite = mapView.findElement(ese);
					if(ssite == esite)
					{
	//					mPath.addCablePath(ssite);
					}
				}
				else
				if(pe.getType() == PathElement.CABLE_LINK)
				{
					SchemeCableLink clink = (SchemeCableLink )pe.getSchemeCableLink();
					MapCablePathElement cp = mapView.findCablePath(clink);
					if(cp != null)
					{
						unsortedCablePaths.add(cp);
					}
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
	
	public MapNodeElement getStartNode()
	{
		MapSiteNodeElement[] mne = getMapView().getSideNodes(this.getSchemePath());
		
		return mne[0];
	}
	
	public MapNodeElement getEndNode()
	{
		MapSiteNodeElement[] mne = getMapView().getSideNodes(this.getSchemePath());
		
		return mne[1];
	}
	
	public void sortPathElements()
	{
		sortedCablePaths.clear();
		sortedNodeLinks.clear();
		sortedNodes.clear();
		
		MapNodeElement node = getStartNode();

		sortedCablePaths.addAll(getCablePaths());
		
		for(Iterator it = sortedCablePaths.iterator(); it.hasNext();)
		{
			MapCablePathElement cpath = (MapCablePathElement )it.next();
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
			com.syrus.AMFICOM.Client.Resource.ISM.TransmissionPath tp1 = getSchemePath().path;
			com.syrus.AMFICOM.configuration.TransmissionPath tp = 
				(com.syrus.AMFICOM.configuration.TransmissionPath )
				ConfigurationStorableObjectPool.getStorableObject(
					new Identifier(tp1.getId()), 
					true);

//how it should look like:
//			com.syrus.AMFICOM.configuration.TransmissionPath tp = getSchemePath().path;

			me = (MonitoredElement )
				ConfigurationStorableObjectPool.getStorableObject(
						(Identifier )(tp.getMonitoredElementIds().get(0)), 
						true);

		}
		catch (CommunicationException e)
		{
		}
		catch (DatabaseException e)
		{
		}
		catch(Exception e)
		{
		}

		return me;
	}

	public MapNodeLinkElement nextNodeLink(MapNodeLinkElement nl)
	{
		int index = getSortedNodeLinks().indexOf(nl);
		if(index == getSortedNodeLinks().size() - 1)
			return null;
		else
			return (MapNodeLinkElement )getSortedNodeLinks().get(index + 1);
	}

	public MapNodeLinkElement previousNodeLink(MapNodeLinkElement nl)
	{
		int index = getSortedNodeLinks().indexOf(nl);
		if(index == 0)
			return null;
		else
			return (MapNodeLinkElement )getSortedNodeLinks().get(index - 1);
	}

}
