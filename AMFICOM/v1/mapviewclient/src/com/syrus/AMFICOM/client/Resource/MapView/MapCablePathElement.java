/**
 * $Id: MapCablePathElement.java,v 1.12 2004/10/14 15:39:05 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;
import com.syrus.AMFICOM.Client.Resource.MapView.MapCablePathBinding;
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
 * @version $Revision: 1.12 $, $Date: 2004/10/14 15:39:05 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapCablePathElement extends MapLinkElement implements Serializable
{
	private static final long serialVersionUID = 02L;
	public static final String typ = "mapcablepathelement";

	protected List sortedNodes = new LinkedList();
	protected List sortedNodeLinks = new LinkedList();
	protected boolean nodeLinksSorted = false;

	protected List links = new LinkedList();
	protected boolean linksSorted = false;
	
	protected SchemeCableLink schemeCableLink;

	protected String mapViewId = "";
	
	protected MapView mapView;
	
	protected MapCablePathBinding binding;

	public String[][] getExportColumns()
	{
		return null;
	}

	public void setColumn(String field, String value)
	{
	}

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
		
		binding = new MapCablePathBinding(this);

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

	private static final String PROPERTY_PANE_CLASS_NAME = 
			"com.syrus.AMFICOM.Client.Map.Props.MapCablePathPane";

	public static String getPropertyPaneClassName()
	{
		return PROPERTY_PANE_CLASS_NAME;
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

	public boolean isSelectionVisible()
	{
		return isSelected();
	}

	public boolean isVisible(Rectangle2D.Double visibleBounds)
	{
		boolean vis = false;
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link.isVisible(visibleBounds))
			{
				vis = true;
				break;
			}
		}
		return vis;
	}

	public void paint(Graphics g, Rectangle2D.Double visibleBounds, Stroke stroke, Color color, boolean selectionVisible)
	{
		if(!isVisible(visibleBounds))
			return;

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			link.paint(g, visibleBounds, stroke, color, selectionVisible);
		}
	}

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

	public double getLengthLo()
	{
		return schemeCableLink.getOpticalLength();
	}

	public void setLengthLf(double len)
	{
		schemeCableLink.setPhysicalLength(len);
	}

	public void setLengthLo(double len)
	{
		schemeCableLink.setOpticalLength(len);
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

	public void clearLinks()
	{	
		links.clear();
		binding.clear();
	}

	public void setLinks(List list)
	{	
		this.clearLinks();		
		
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			this.addLink((MapPhysicalLinkElement)it.next());
		}
	}


	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void removeLink(MapPhysicalLinkElement link)
	{
		links.remove(link);
//		int index = getBinding().
		getBinding().remove(link);
		nodeLinksSorted = false;
	}

	/**
	 * Внимание! концевые точки линии не обновляются
	 */
	public void addLink(MapPhysicalLinkElement addLink)
	{
//		MapNodeElement stNode = addLink.getStartNode();
//		MapNodeElement enNode = addLink.getEndNode();
//
//		MapNodeElement bufferNode = getStartNode();
//		
//		ListIterator lit = getLinks().iterator();
//		for(; lit.hasNext();)
//		{
//			MapPhysicalLinkElement link = (MapPhysicalLinkElement )lit.next();
//			if(link.getStartNode().equals(stNode)
//				|| link.getStartNode().equals(enNode)
//				|| link.getEndNode().equals(stNode)
//				|| link.getEndNode().equals(enNode))
//			{
//				break;
//			}
//		}
//		lit.add(addLink);
		links.add(addLink);
		binding.put(addLink, MapCablePathBinding.generateCCI(
				addLink,
				this.getMapView().getLogicalNetLayer().getContext().getDataSource()));
		linksSorted = false;
		nodeLinksSorted = false;
		sortLinks();
	}

	public void sortLinks()
	{
		if(!linksSorted)
		{
			MapNodeElement smne = this.getStartNode();
			List origvec = new LinkedList();
			origvec.addAll(getLinks());
			List vec = new LinkedList();
			int count = getLinks().size();
			for (int i = 0; i < count; i++) 
//			while(!smne.equals(this.getEndNode()))
			{
				boolean canSort = false;
				for(Iterator it = origvec.iterator(); it.hasNext();)
				{
					MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();

					if(link.getStartNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getEndNode();
						CableChannelingItem cci = getBinding().getCCI(link);
						cci.startSiteId = link.getStartNode().getId();
						cci.endSiteId = link.getEndNode().getId();
						canSort = true;
						break;
					}
					else
					if(link.getEndNode().equals(smne))
					{
						vec.add(link);
						it.remove();
						smne = link.getStartNode();
						CableChannelingItem cci = getBinding().getCCI(link);
						cci.startSiteId = link.getEndNode().getId();
						cci.endSiteId = link.getStartNode().getId();
						canSort = true;
						break;
					}
				}
				if(!canSort)
					// unconsistent - cannot sort
					return;
			}
			this.links = vec;
			linksSorted = true;
			nodeLinksSorted = false;
		}
	}

	public MapPhysicalLinkElement nextLink(MapPhysicalLinkElement link)
	{
		MapPhysicalLinkElement ret = null;
		if(link == null)
		{
			if(getLinks().size() != 0)
				ret = (MapPhysicalLinkElement )getLinks().get(0);
		}
		else
		{
			int index = getLinks().indexOf(link);
			if(index != getLinks().size() - 1 && index != -1)
				ret = (MapPhysicalLinkElement )getLinks().get(index + 1);
		}
		return ret;
	}

	public MapPhysicalLinkElement previousLink(MapPhysicalLinkElement link)
	{
		MapPhysicalLinkElement ret = null;
		if(link == null)
		{
			if(getLinks().size() != 0)
				ret = (MapPhysicalLinkElement )getLinks().get(getLinks().size() - 1);
		}
		else
		{
			int index = getLinks().indexOf(link);
			if(index > 0)
				ret = (MapPhysicalLinkElement )getLinks().get(index - 1);
		}
		return ret;
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
//		sortLinks();
//		if(!nodeLinksSorted)
		{
			java.util.List pl = getLinks();

			List vec = new LinkedList();
			List nodevec = new LinkedList();

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

	/**
	 * Получить толщину линии
	 */
	public int getLineSize ()
	{
		return MapPropertiesManager.getUnboundThickness();
	}

	/**
	 * Получить вид линии
	 */
	public String getStyle ()
	{
		return MapPropertiesManager.getStyle();
	}

	/**
	 * Получить стиль линии
	 */
	public Stroke getStroke ()
	{
		return MapPropertiesManager.getStroke();
	}

	/**
	 * Получить цвет
	 */
	public Color getColor()
	{
		return MapPropertiesManager.getUnboundLinkColor();
	}

	/**
	 * получить цвет при наличии сигнала тревоги
	 */
	public Color getAlarmedColor()
	{
		return MapPropertiesManager.getAlarmedColor();
	}

	/**
	 * получить толщину линии при наличи сигнала тревоги
	 */
	public int getAlarmedLineSize ()
	{
		return MapPropertiesManager.getAlarmedThickness();
	}

	public Point getBindingPosition(MapPhysicalLinkElement link)
	{
		CableChannelingItem cci = (CableChannelingItem )getBinding().get(link);
		return new Point(cci.row_x, cci.place_y);
	}

	public void setBinding(MapCablePathBinding binding)
	{
		this.binding = binding;
	}

	public MapCablePathBinding getBinding()
	{
		return binding;
	}

	public MapNodeElement getStartUnboundNode()
	{
		MapNodeElement bufferSite = getStartNode();
		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.next();
			if(link instanceof MapUnboundLinkElement)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	public MapNodeElement getEndUnboundNode()
	{
		MapNodeElement bufferSite = getEndNode();
		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			MapPhysicalLinkElement link = (MapPhysicalLinkElement )it.previous();
			if(link instanceof MapUnboundLinkElement)
				break;
			bufferSite = link.getOtherNode(bufferSite);
		}
		return bufferSite;
	}
	
	public MapPhysicalLinkElement getStartLastBoundLink()
	{
		MapPhysicalLinkElement link = null;

		for(Iterator it = getLinks().iterator(); it.hasNext();)
		{
			MapPhysicalLinkElement link2 = (MapPhysicalLinkElement )it.next();
			if(link2 instanceof MapUnboundLinkElement)
				break;
			link = link2;
		}
		
		return link;
	}

	public MapPhysicalLinkElement getEndLastBoundLink()
	{
		MapPhysicalLinkElement link = null;

		for(ListIterator it = getLinks().listIterator(getLinks().size()); it.hasPrevious();)
		{
			MapPhysicalLinkElement link2 = (MapPhysicalLinkElement )it.previous();
			if(link2 instanceof MapUnboundLinkElement)
				break;
			link = link2;
		}
		
		return link;
	}

}
