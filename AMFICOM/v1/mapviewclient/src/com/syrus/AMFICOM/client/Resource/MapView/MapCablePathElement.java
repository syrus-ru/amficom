/**
 * $Id: MapCablePathElement.java,v 1.1 2004/09/13 12:33:43 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;

import com.syrus.AMFICOM.Client.Map.MapCoordinatesConverter;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapUnboundLinkElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;

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
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:43 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapCablePathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapcablepathelement";

//	public MapCablePathElement_Transferable transferable;

	protected ArrayList physicalLinkIds = new ArrayList();

	protected ArrayList sortedNodes = new ArrayList();
	protected ArrayList sortedNodeLinks = new ArrayList();
	protected boolean nodeLinksSorted = false;

	protected ArrayList links = new ArrayList();
	protected boolean linksSorted = false;
	
	protected SchemeCableLink schemeCableLink;

	protected String mapViewId = "";
	
	protected MapView mapView;

/*
	public MapPathElement( MapCablePathElement_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}
*/
	public MapCablePathElement(
			SchemeCableLink schemeCableLink,
			String id, 
			MapNodeElement stNode, 
			MapNodeElement eNode, 
			MapView mapView)
	{
		this.mapView = mapView;

		this.id = id;
		this.name = id;
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

//		transferable = new MapCablePathElement_Transferable();
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

	public void updateAttributes()
	{
/*
		attributes.clear();
	    for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(
					transferable.attributes[i].type_id, 
					Pool.get(ElementAttribute.typ, transferable.attributes[i].id));
*/
	}
	//этот класс используется для востановления данных из базы
	public void setLocalFromTransferable()
	{
/*
		this.id = transferable.id;
		this.name = transferable.name;
		this.type_id = transferable.type_id;
		this.description = transferable.description;
		this.owner_id = transferable.owner_id;
		this.mapID = transferable.map_id;
		this.startNode_id = transferable.startNode_id;
		this.endNode_id = transferable.endNode_id;
		this.ism_map_id = transferable.ism_map_id;

		for(int i = 0; i < transferable.attributes.length; i++)
			attributes.put(transferable.attributes[i].type_id, new ElementAttribute(transferable.attributes[i]));

		this.PATH_ID = transferable.path_id;

		this.physicalLink_ids = new Vector();
		for (int i = 0; i < transferable.physicalLink_ids.length; i++)
			this.physicalLink_ids.add( transferable.physicalLink_ids[i]);
*/
	}

	//этот класс используется для отпрвки данных в базу
	public void setTransferableFromLocal()
	{
/*
		transferable.id = this.id;
		transferable.name = this.name;
		transferable.type_id = this.type_id;
		transferable.description = this.description;
		transferable.owner_id = map.user_id ;
		transferable.map_id = map.id;
		transferable.ism_map_id = ism_map_id;
		transferable.startNode_id = this.startNode.getId();
		transferable.endNode_id = this.endNode.getId();

		int l = this.attributes.size();
		int i = 0;
		transferable.attributes = new ElementAttribute_Transferable[l];
		for(Enumeration e = attributes.elements(); e.hasMoreElements();)
		{
			ElementAttribute ea = (ElementAttribute )e.nextElement();
			ea.setTransferableFromLocal();
			transferable.attributes[i++] = ea.transferable;
		}
		transferable.path_id = this.PATH_ID;

		transferable.physicalLink_ids = new String[ this.physicalLink_ids.size()];

		for (i = 0; i < transferable.physicalLink_ids.length; i++)
			transferable.physicalLink_ids[i] = (String) this.physicalLink_ids.get(i);
*/
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
	
	public static String getPropertyPaneClassName()
	{
		return "com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane";
	}
	
	public static PropertiesPanel getPropertyPane()
	{
		return null;//new MapPathPane();
	}
	

	public void setSchemeCableLink(SchemeCableLink schemeCableLink)
	{
		this.schemeCableLink = schemeCableLink;
	}


	public SchemeCableLink getSchemeCableLink()
	{
		return schemeCableLink;
	}
	
	public MapNodeElement getStartUnbound()
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

	public MapLinkElement getStartLastBoundLink()
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

	public MapLinkElement getEndLastBoundLink()
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

	public void paint(Graphics g)
	{
		MapCoordinatesConverter converter = getMapView().getMap().getConverter();
	
		Graphics2D p = (Graphics2D )g;

		BasicStroke stroke = (BasicStroke )this.getStroke();
		Stroke str = new BasicStroke(
				this.getLineSize(), 
				stroke.getEndCap(), 
				stroke.getLineJoin(), 
				stroke.getMiterLimit(), 
				stroke.getDashArray(), 
				stroke.getDashPhase());

		Iterator e = getSortedNodeLinks().iterator();

		while ( e.hasNext())
		{
			MapNodeLinkElement nodeLinkElement = (MapNodeLinkElement )e.next();

			Point from = converter.convertMapToScreen(
					nodeLinkElement.getStartNode().getAnchor());
			Point to = converter.convertMapToScreen(
					nodeLinkElement.getEndNode().getAnchor());

			p.setStroke(str);
			p.setColor(this.getColor());

			if (this.getAlarmState())
			{
				if ( MapPropertiesManager.isShowAlarmState() )
					p.setColor(this.getAlarmedColor());
				else
					p.setColor(this.getColor());
			}
			else
				p.setColor( this.getColor());

			p.drawLine(from.x, from.y, to.x, to.y);

			if (isSelected())
			{
				p.setStroke(MapPropertiesManager.getSelectionStroke());

				double l = 4;
				double l1 = 6;
				double cos_a = (from.y - to.y) /
					Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				double sin_a = (from.x - to.x) /
					Math.sqrt( 
							(from.x - to.x) * (from.x - to.x) 
							+ (from.y - to.y) * (from.y - to.y) );

				p.setColor(MapPropertiesManager.getFirstSelectionColor());
				p.drawLine(
						from.x + (int )(l * cos_a), 
						from.y  - (int )(l * sin_a), 
						to.x + (int )(l * cos_a), 
						to.y - (int )(l * sin_a));
				p.drawLine(
						from.x - (int )(l * cos_a), 
						from.y  + (int )(l * sin_a), 
						to.x - (int )(l * cos_a), 
						to.y + (int )(l * sin_a));

				p.setColor(MapPropertiesManager.getSecondSelectionColor());
				p.drawLine(
						from.x + (int )(l1 * cos_a), 
						from.y  - (int )(l1 * sin_a), 
						to.x + (int )(l1 * cos_a), 
						to.y - (int )(l1 * sin_a));
				p.drawLine(
						from.x - (int )(l1 * cos_a), 
						from.y  + (int )(l1 * sin_a), 
						to.x - (int )(l1 * cos_a), 
						to.y + (int )(l1 * sin_a));
			}

		}
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
		return null;//transferable;
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
/*
	//Возвращает строительную длинну в метрах
	public double getSizeInDoubleLf()
	{
		SchemePath sp = (SchemePath )Pool.get(SchemePath.typ, this.scheme_path_id);
		if(sp == null)
			return 0.0;
		return sp.getPhysicalLength();
	}

	public double getDistanceFromStartLf(Point pt)
	{
		double distance = 0.0;
		MapNodeElement node = startNode;
		for(Iterator it = sortNodeLinks().iterator(); it.hasNext();)
		{
			MapNodeLinkElement mnle = (MapNodeLinkElement )it.next();
			if(mnle.isMouseOnThisObject(pt))
			{
				distance += mnle.getLengthLf(node, pt);
				break;
			}
			else
				distance += mnle.getLengthLf();

			if(mnle.startNode.equals(node))
				node = mnle.endNode;
			else
				node = mnle.startNode;
		}
		return distance;
	}
*/
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
		if(!nodeLinksSorted)
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

		out.writeObject(physicalLinkIds);
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

		physicalLinkIds = (ArrayList )in.readObject();

//		transferable = new MapPathElement_Transferable();

		updateLocalFromTransferable();
		Pool.put(getTyp(), getId(), this);
		Pool.put("serverimage", getId(), this);
	}

}
