/**
 * $Id: MapCablePathElement.java,v 1.5 2004/09/21 14:59:20 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.MapView;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.General.UI.PropertiesPanel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.CableChannelingItem;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Stroke;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * элемент пути 
 * 
 * 
 * 
 * @version $Revision: 1.5 $, $Date: 2004/09/21 14:59:20 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapCablePathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapcablepathelement";

	protected ArrayList sortedNodes = new ArrayList();
	protected ArrayList sortedNodeLinks = new ArrayList();
	protected boolean nodeLinksSorted = false;

	protected ArrayList links = new ArrayList();
	protected boolean linksSorted = false;
	
	protected SchemeCableLink schemeCableLink;

	protected String mapViewId = "";
	
	protected MapView mapView;

	public MapCablePathElement(
			SchemeCableLink schemeCableLink,
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.name = schemeCableLink.getName();
		if(mapView != null)
		{
			mapViewId = mapView.getId();
			map = mapView.getMap();
			if(map != null)
				mapId = map.getId();
		}
		startNode = stNode;
		endNode = eNode;
		attributes = new HashMap();

		setSchemeCableLink(schemeCableLink);
	}

	public void setMapView(MapView mapView)
	{
		this.mapView = mapView;
	}
	
	public MapView getMapView()
	{
		return this.mapView;
	}

	public Object clone(DataSourceInterface dataSource)
	{
/*
		String cloned_id = (String)Pool.get("mapclonedids", id);
		if (cloned_id != null)
			return Pool.get(MapPathElement.typ, cloned_id);

		MapPathElement mtpe = new MapPathElement(
				dataSource.GetUId(MapPathElement.typ),
				(MapNodeElement )startNode.clone(dataSource),
				(MapNodeElement )endNode.clone(dataSource),
				(Map )map.clone(dataSource) );
				
		mtpe.changed = changed;
		mtpe.description = description;
//		mtpe.endNode = (MapNodeElement )endNode.clone(dataSource);
		mtpe.endNode_id = endNode_id;
		mtpe.name = name;
		mtpe.scheme_path_id = (String )Pool.get("schemeclonedids", scheme_path_id);
		mtpe.selected = selected;
		mtpe.show_alarmed = show_alarmed;
//		mtpe.startNode = (MapNodeElement )startNode.clone(dataSource);
		mtpe.startNode_id = startNode_id;
		mtpe.type_id = type_id;

		Pool.put(MapPathElement.typ, mtpe.getId(), mtpe);
		Pool.put("mapclonedids", id, mtpe.getId());

		mtpe.physicalLink_ids = new Vector(physicalLink_ids.size());
		for (int i = 0; i < physicalLink_ids.size(); i++)
			mtpe.physicalLink_ids.add(Pool.get("mapclonedids", (String )physicalLink_ids.get(i)));

		mtpe.attributes = new Hashtable();
		for(Enumeration enum = attributes.elements(); enum.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )enum.nextElement();
			ElementAttribute ea2 = (ElementAttribute )ea.clone(dataSource);
			mtpe.attributes.put(ea2.type_id, ea2);
		}

		return mtpe;
*/
		return null;
	}

	//этот класс используется для востановления данных из базы
	public void setLocalFromTransferable()
	{
	}

	//этот класс используется для отпрвки данных в базу
	public void setTransferableFromLocal()
	{
	}

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

	public ObjectResourceModel getModel()
	{
		return null;//new MapPathElementModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return null;//new MapPathElementDisplayModel();
	}
	
	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane";

	public String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
	}
	
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapPathPane();
	}

	public void setSchemeCableLink(SchemeCableLink schemeCableLink)
	{
		this.schemeCableLink = schemeCableLink;
		this.name = schemeCableLink.getName();
	}

	public SchemeCableLink getSchemeCableLink()
	{
		return schemeCableLink;
	}

	public MapNodeElement getStartUnbound()
	{
		MapNodeElement bufferSite = getStartNode();
		
		for(Iterator it = getSchemeCableLink().channelingItems.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = map.getMapSiteNodeElement(cci.endSiteId);
		}
		return null;
	}
	
	public MapNodeElement getStartUnbound1()
	{
		MapNodeElement mne = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
				return mne;
			mne = link.getOtherNode(mne);
		}
		return null;
	}

	public MapNodeElement getEndUnbound()
	{
		MapNodeElement bufferSite = getEndNode();
		
		List ccis = (List )getSchemeCableLink().channelingItems;
		
		for(ListIterator it = ccis.listIterator(ccis.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return bufferSite;
			}
			bufferSite = map.getMapSiteNodeElement(cci.startSiteId);
		}
		return null;
	}
	
	public MapNodeElement getEndUnbound1()
	{
		MapNodeElement mne = getEndNode();
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.previous();
			if(link instanceof MapUnboundLinkElement)
				return mne;
			mne = link.getOtherNode(mne);
		}
		return null;
	}

	public MapPhysicalLinkElement getStartLastBoundLink()
	{
		MapNodeElement bufferSite = getStartNode();
		MapPhysicalLinkElement link = null;
		
		for(Iterator it = getSchemeCableLink().channelingItems.iterator(); it.hasNext();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.next();
			if(! cci.startSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = map.getMapSiteNodeElement(cci.endSiteId);
			link = map.getPhysicalLink(cci.physicalLinkId);
		}
		return null;
	}

	public MapLinkElement getStartLastBoundLink1()
	{
		MapPhysicalLinkElement blink = null;
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
				return blink;
			blink = link;
		}
		return null;
	}

	public MapPhysicalLinkElement getEndLastBoundLink()
	{
		MapNodeElement bufferSite = getEndNode();
		MapPhysicalLinkElement link = null;

		List ccis = (List )getSchemeCableLink().channelingItems;
		
		for(ListIterator it = ccis.listIterator(ccis.size()); it.hasPrevious();)
		{
			CableChannelingItem cci = (CableChannelingItem )it.previous();
			if(! cci.endSiteId.equals(bufferSite.getId()))
			{
				return link;
			}
			bufferSite = map.getMapSiteNodeElement(cci.startSiteId);
			link = map.getPhysicalLink(cci.physicalLinkId);
		}
		return null;
	}

	public MapLinkElement getEndLastBoundLink1()
	{
		MapPhysicalLinkElement blink = null;
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.previous();
			if(link instanceof MapUnboundLinkElement)
				return blink;
			blink = link;
		}
		return null;
	}

	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	public void paint(Graphics g, Stroke stroke, Color color, boolean selectionVisible)
	{
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			link.paint(g, stroke, color, selectionVisible);
		}
	}

	public void paint(Graphics g)
	{
		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());
		Color color = this.getColor();

		paint(g, str, color, isSelectionVisible());
	}

	public boolean isMouseOnThisObject(Point currentMousePoint)
	{
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapElement me = (MapElement )it.next();
			if(me.isMouseOnThisObject(currentMousePoint))
				return true;
		}
		return false;
	}
	
	public Object getTransferable()
	{
		return null;
	}

	//Возвращает топологическую длинну в метрах
	public double getLengthLt()
	{
		double length = 0;
		Iterator e = getLinks().iterator();
		while( e.hasNext())
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )e.next();
			length = length + link.getLengthLt();
		}
		return length;
	}

	public double getLengthLf()
	{
		return schemeCableLink.getPhysicalLength();
	}

	public double getDistanceFromStartLt(Point pt)
	{
		double distance = 0.0;
		MapNodeElement node = getStartNode();
		sortNodeLinks();
		for(Iterator it = getSortedNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			if(mnle.isMouseOnThisObject(pt))
			{
				Point2D.Double dpoint = getMap().getConverter().convertScreenToMap(pt);
				distance += getMap().getConverter().distance(dpoint, node.getAnchor());
				break;
			}
			else
				distance += mnle.getLengthLt();

			if(mnle.getStartNode().equals(node))
				node = mnle.getEndNode();
			else
				node = mnle.getStartNode();
		}
		return distance;
	}

	/**
	 * возвращает коэффициент топологической привязки
	 */
	public double getKd()
	{
		double phLen = schemeCableLink.getPhysicalLength();
		if(phLen == 0.0D)
			return 1.0;
		double topLen = getLengthLt();
		if(topLen == 0.0D)
			return 1.0;

		double Kd = phLen / topLen;
		return Kd;
	}

	/**
	 * Возвращяет длинну линии пересчитанную на коэффициент топологической 
	 * привязки
	 */
	public double getDistanceFromStartLf(Point pt)
	{
		double kd = getKd();
		return getDistanceFromStartLt(pt) * kd;
	}

	public List getLinks()
	{	
		return links;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeLink(MapPhysicalLinkElement link)
	{
		links.remove(link);
		linksSorted = false;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addLink(MapPhysicalLinkElement addLink)
	{
		links.add(addLink);
		linksSorted = false;
	}

	public void sortLinks()
	{
		if(!linksSorted)
		{
			MapNodeElement smne = this.getStartNode();
			ArrayList vec = new ArrayList();
			while(!smne.equals(this.getEndNode()))
			{
				for(Iterator it = getLinks().iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

					if(link.getStartNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getEndNode();
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getStartNode();
						break;
					}
				}
			}
			this.links = vec;
			linksSorted = true;
			nodeLinksSorted = false;
		}
	}

	public MapPhysicalLinkElement nextLink(MapPhysicalLinkElement link)
	{
		int index = getLinks().indexOf(link);
		if(index == getLinks().size() - 1)
			return link;
		else
			return (MapPhysicalLinkElement )getLinks().get(index + 1);
	}

	public MapPhysicalLinkElement previousNodeLink(MapPhysicalLinkElement link)
	{
		int index = getLinks().indexOf(link);
		if(index == 0)
			return link;
		else
			return (MapPhysicalLinkElement )getLinks().get(index - 1);
	}

	public void sortNodes()
	{
		sortNodeLinks();
	}
	
	public java.util.List getSortedNodes()
	{
		if(!nodeLinksSorted)
			return null;
		return sortedNodes;
	}

	public java.util.List getSortedNodeLinks()
	{
		if(!nodeLinksSorted)
			return null;
//			throw new Exception("NodeLinks not sorted!");
		return sortedNodeLinks;
	}

	public void sortNodeLinks()
	{
		sortLinks();
//		if(!nodeLinksSorted)
		{
			java.util.List pl = getLinks();

			ArrayList vec = new ArrayList();
			ArrayList nodevec = new ArrayList();

			for(Iterator it = pl.iterator(); it.hasNext();)
			{
				MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
				
				link.sortNodeLinks();
				
				vec.addAll(link.getNodeLinks());
				
				for(Iterator it2 = link.getSortedNodes().iterator(); it2.hasNext();)
				{
					MapNodeElement mne = (MapNodeElement )it2.next();

					// avoid duplicate nodes - do not add last node in list
					if(it2.hasNext())
						nodevec.add(mne);
				}
			}
			
			// add last node
			nodevec.add(getEndNode());
				
			this.sortedNodeLinks = vec;
			this.sortedNodes = nodevec;
			nodeLinksSorted = true;
		}
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(description);

		startNodeId = getStartNode().getId();
		endNodeId = getEndNode().getId();

		out.writeObject(startNodeId);
		out.writeObject(endNodeId);

		out.writeObject(attributes);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		description = (String )in.readObject();
		startNodeId = (String )in.readObject();
		endNodeId = (String )in.readObject();
		attributes = (HashMap )in.readObject();

		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

}
