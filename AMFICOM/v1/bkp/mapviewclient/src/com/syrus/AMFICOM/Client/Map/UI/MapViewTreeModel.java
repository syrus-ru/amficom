/**
 * $Id: MapViewTreeModel.java,v 1.8 2005/04/06 17:41:12 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.Controllers.MapViewController;
import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.client_.general.ui_.tree_.IconedNode;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.MapView;


/**
 * @version $Revision: 1.8 $, $Date: 2005/04/06 17:41:12 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapViewTreeModel extends ItemTreeModel 
{
	public static final String MAP_BRANCH = "Map";
	public static final String MAPS_BRANCH = "innermaps";

	MapView mapView = null;

	Item topLevelItem;

	static MapElementComparator mapElementComparator = new MapElementComparator();
	static MapComparator mapComparator = new MapComparator();
	static SiteNodeTypeComparator siteNodeTypeComparator = new SiteNodeTypeComparator();

	static final int IMG_SIZE = 16;

	static ImageIcon mapViewIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/mapview.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));
	static ImageIcon mapIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/map.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));
	static ImageIcon nodeIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/node_in_tree.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));
	static ImageIcon linkIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/linkmode.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));
	static ImageIcon collIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/collector.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));

	static ImageIcon folderIcon = new ImageIcon(
			Toolkit.getDefaultToolkit()
				.getImage("images/folder.gif")
					.getScaledInstance(
						IMG_SIZE,
						IMG_SIZE,
						Image.SCALE_SMOOTH));

	public MapViewTreeModel(Item root) {
		super(root);
//		buildTree(mapView);
	}
	
	public void setMapView(MapView mapView) {
		buildTree(mapView);
	}

	public Item findNode(Object object)
	{
		return findNode((Item )getRoot(), object);
	}

	public Item findNode(Item item, Object object)
	{
		if(item.getObject().equals(object))
			return item;
		for(Iterator iter = item.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			Item foundNode = findNode(childNode, object);
			if(foundNode != null)
				return foundNode;
		}
		return null;
		
	}

	void buildTree(MapView mapView) {
		if(mapView == null) {
			Item root = (Item )getRoot();
			root.getChildren().clear();
			this.topLevelItem = new IconedNode("noname", getObjectName("noname"));
			root.addChild(this.topLevelItem);
		}
		else {
			if(this.mapView != null && this.mapView.equals(mapView)) {
				updateTree(this.topLevelItem);
			}
			else {
				Item root = (Item )getRoot();
				root.getChildren().clear();
				this.topLevelItem = new IconedNode(mapView, getObjectName(mapView), mapViewIcon);
				root.addChild(this.topLevelItem);
				Item mapNode = new IconedNode(
						MapViewTreeModel.MAP_BRANCH,
						getObjectName(MapViewTreeModel.MAP_BRANCH),
						folderIcon);
				this.topLevelItem.addChild(mapNode);
				mapNode.addChild(buildMapTree(mapView.getMap()));
			}
		}
		this.mapView = mapView;
	}

	Item buildMapTree(Map map) {
		Item node;
			
		node = new IconedNode(map, getObjectName(map), mapIcon);

		node.addChild(buildMapsTree(map));
		node.addChild(buildSiteTree(map));
		node.addChild(buildNodeTree(map));
		node.addChild(buildLinkTree(map));
		node.addChild(buildCollTree(map));
		return node;
	}

	public String getObjectName(Object object) {
		if(object instanceof String)
			return LangModelMap.getString((String )object);
		else if(object instanceof MapView) {
			MapView mapView = (MapView )object;
			return mapView.getName();
		}
		else if(object instanceof Map) {
			Map map = (Map )object;
			return map.getName();
		}
		else if(object instanceof SiteNodeType) {
			SiteNodeType type = (SiteNodeType )object;
			return type.getName();
		}
		else if(object instanceof MapElement) {
			MapElement mapElement = (MapElement )object;
			return mapElement.getName();
		}
		return LangModelMap.getString("noname");
	}

	
	public String getNodeName(Item node) {
		if(node.getObject() instanceof String)
			return LangModelMap.getString((String )(node.getObject()));
		else if(node.getObject() instanceof MapView) {
			MapView mapView = (MapView )node.getObject();
			return mapView.getName();
		}
		else if(node.getObject() instanceof Map) {
			Map map = (Map )node.getObject();
			return map.getName();
		}
		else if(node.getObject() instanceof SiteNodeType) {
			SiteNodeType type = (SiteNodeType )node.getObject();
			return type.getName();
		}
		else if(node.getObject() instanceof MapElement) {
			MapElement mapElement = (MapElement )node.getObject();
			return mapElement.getName();
		}
		return LangModelMap.getString("noname");
	}

	Item buildMapsTree(Map map) {
		Item mapChildrenNode = new IconedNode(
				MapViewTreeModel.MAPS_BRANCH, 
				getObjectName(MapViewTreeModel.MAPS_BRANCH),
				folderIcon);

		List mapsChildren = new ArrayList();
		mapsChildren.addAll(map.getMaps());
		Collections.sort(mapsChildren, MapViewTreeModel.mapComparator);
		
		for(Iterator it = mapsChildren.iterator(); it.hasNext();)
		{
			Map innerMap = (Map )it.next();
			mapChildrenNode.addChild(buildMapTree(innerMap));
		}

		return mapChildrenNode;
	}

	Item buildSiteTree(Map map) {
		Item siteChildrenNode = new IconedNode(
				MapViewController.ELEMENT_SITENODE, 
				getObjectName(MapViewController.ELEMENT_SITENODE),
				folderIcon);

		java.util.Map types = this.getSiteNodeTypes(map);
		for(Iterator it = types.keySet().iterator(); it.hasNext();)
		{
			SiteNodeType type = (SiteNodeType )it.next();
			List siteNodes = (List )types.get(type);
			siteChildrenNode.addChild(buildSiteTree(type, siteNodes));
		}
		return siteChildrenNode;
	}

	Item buildSiteTree(SiteNodeType type, List siteNodes) {
		IconedNode siteChildrenNode = new IconedNode(
				type, 
				getObjectName(type),
				new ImageIcon(
					NodeTypeController.getImage(type).getScaledInstance(
						IMG_SIZE, 
						IMG_SIZE, 
						Image.SCALE_SMOOTH)));

		Collections.sort(siteNodes, MapViewTreeModel.mapElementComparator);
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			siteChildrenNode.addChild(new IconedNode(site, getObjectName(site), siteChildrenNode.getIcon(), true));
		}
				
		return siteChildrenNode;
	}


	Item buildNodeTree(Map map) {
		Item nodeChildrenNode = new IconedNode(
				MapViewController.ELEMENT_TOPOLOGICALNODE, 
				getObjectName(MapViewController.ELEMENT_TOPOLOGICALNODE),
				folderIcon);
		List nodes = new ArrayList(map.getTopologicalNodes());
		Collections.sort(nodes, MapViewTreeModel.mapElementComparator);
		for(Iterator it = nodes.iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			nodeChildrenNode.addChild(new IconedNode(topologicalNode, getObjectName(topologicalNode), nodeIcon, true));
		}
		return nodeChildrenNode;
	}

	Item buildLinkTree(Map map) {
		Item linkChildrenNode = new IconedNode(
				MapViewController.ELEMENT_PHYSICALLINK, 
				getObjectName(MapViewController.ELEMENT_PHYSICALLINK),
				folderIcon);
		List links = new ArrayList(map.getPhysicalLinks());
		Collections.sort(links, MapViewTreeModel.mapElementComparator);
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			linkChildrenNode.addChild(new IconedNode(link, getObjectName(link), linkIcon, true));
		}
		return linkChildrenNode;
	}

	Item buildCollTree(Map map) {
		Item collChildrenNode = new IconedNode(
				MapViewController.ELEMENT_COLLECTOR, 
				getObjectName(MapViewController.ELEMENT_COLLECTOR),
				folderIcon);
		List collectors = new ArrayList(map.getCollectors());
		Collections.sort(collectors, MapViewTreeModel.mapElementComparator);
		for(Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			collChildrenNode.addChild(new IconedNode(collector, getObjectName(collector), collIcon, true));
		}
		return collChildrenNode;

	}

	java.util.Map getSiteNodeTypes(Map map) {
		java.util.Map types = new HashMap();
		for(Iterator it = map.getSiteNodes().iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			List list = (List )types.get(site.getType());
			if(list == null) {
				list = new LinkedList();
				types.put(site.getType(), list);
			}
			list.add(site);
		}
		return types;
	}

	void updateTree(Item node) {
		Item mapTopNode = (Item )node.getChildren().iterator().next();
		
		Item mapNode = (Item )mapTopNode.getChildren().iterator().next();
		Map map = (Map )mapNode.getObject();
		if(map.equals(this.mapView.getMap()))
			updateMapTree(mapNode);
		else {
			mapTopNode.getChildren().remove(mapNode);
			mapTopNode.addChild(buildMapTree(this.mapView.getMap()));
		}
	}

	void updateMapTree(Item node) {
		Map map = (Map )node.getObject();
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			if(childNode.getObject().equals(MapViewTreeModel.MAPS_BRANCH))
				updateMapsTree(childNode, map);
			else if(childNode.getObject().equals(MapViewController.ELEMENT_SITENODE))
				updateSiteTree(childNode, map);
			else if(childNode.getObject().equals(MapViewController.ELEMENT_TOPOLOGICALNODE))
				updateNodeTree(childNode, map);
			else if(childNode.getObject().equals(MapViewController.ELEMENT_PHYSICALLINK))
				updateLinkTree(childNode, map);
			else if(childNode.getObject().equals(MapViewController.ELEMENT_COLLECTOR))
				updateCollTree(childNode, map);
		}
	}

	void updateMapsTree(Item node, Map map) {
		List mapsChildren = new ArrayList();
		mapsChildren.addAll(map.getMaps());
		Collections.sort(mapsChildren, MapViewTreeModel.mapComparator);
		
		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			Map innerMap = (Map )childNode.getObject();
			if(mapsChildren.contains(innerMap)) {
				updateMapTree(childNode);
				nodePresense.put(innerMap, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = mapsChildren.iterator(); it.hasNext();) {
			Map innerMap = (Map )it.next();
			Item childNode = (Item )nodePresense.get(innerMap);
			if(childNode == null) {
				Item newItem = buildMapTree(innerMap);
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void updateSiteTree(Item node, Map map) {
		java.util.Map types = this.getSiteNodeTypes(map);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			SiteNodeType type = (SiteNodeType )childNode.getObject();
			if(types.containsKey(type)) {
				List siteNodes = (List )types.get(type);
				updateSiteTree(childNode, siteNodes);
				nodePresense.put(type, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = types.keySet().iterator(); it.hasNext();) {
			SiteNodeType type = (SiteNodeType )it.next();
			Item childNode = (Item )nodePresense.get(type);
			if(childNode == null) {
				List siteNodes = (List )types.get(type);
				Item newItem = buildSiteTree(type, siteNodes);
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void updateSiteTree(Item node, List siteNodes) {
		Collections.sort(siteNodes, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			SiteNode site = (SiteNode )childNode.getObject();
			if(siteNodes.contains(site))
				nodePresense.put(site, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			Item childNode = (Item )nodePresense.get(site);
			if(childNode == null) {
				Item newItem = new IconedNode(site, getObjectName(site), ((IconedNode )node).getIcon(), true); 
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}


	void updateNodeTree(Item node, Map map) {
		List nodes = new ArrayList(map.getTopologicalNodes());
		Collections.sort(nodes, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			TopologicalNode topologicalNode = (TopologicalNode )childNode.getObject();
			if(nodes.contains(topologicalNode))
				nodePresense.put(topologicalNode, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = nodes.iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			Item childNode = (Item )nodePresense.get(topologicalNode);
			if(childNode == null) {
				Item newItem = new IconedNode(topologicalNode, getObjectName(topologicalNode), nodeIcon, true);
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void updateLinkTree(Item node, Map map) {
		List links = new ArrayList(map.getPhysicalLinks());
		Collections.sort(links, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			PhysicalLink link = (PhysicalLink )childNode.getObject();
			if(links.contains(link))
				nodePresense.put(link, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			Item childNode = (Item )nodePresense.get(link);
			if(childNode == null) {
				Item newItem = new IconedNode(link, getObjectName(link), linkIcon, true);
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void updateCollTree(Item node, Map map) {
		List collectors = new ArrayList(map.getCollectors());
		Collections.sort(collectors, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			Collector collector = (Collector )childNode.getObject();
			if(collectors.contains(collector))
				nodePresense.put(collector, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.getChildren().remove(it.next());
		}
		
		int i = 0;
		for(Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			Item childNode = (Item )nodePresense.get(collector);
			if(childNode == null) {
				Item newItem = new IconedNode(collector, getObjectName(collector), collIcon, true);
//				if(node.getChildren().isEmpty())
					node.addChild(newItem);
//				else
//					node.getChildren().add(i, newItem);
			}
			i++;
		}
	}
}

final class SiteNodeTypeComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		SiteNodeType type1 = (SiteNodeType )o1;
		SiteNodeType type2 = (SiteNodeType )o2;
		return type1.getName().compareTo(type2.getName());
	}

	public boolean equals(Object obj) {
		return (obj instanceof MapElementComparator);
	}
}

final class MapElementComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		MapElement mapElement1 = (MapElement )o1;
		MapElement mapElement2 = (MapElement )o2;
		return mapElement1.getName().compareTo(mapElement2.getName());
	}

	public boolean equals(Object obj) {
		return (obj instanceof MapElementComparator);
	}
}

final class MapComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		Map map1 = (Map )o1;
		Map map2 = (Map )o2;
		return map1.getName().compareTo(map2.getName());
	}

	public boolean equals(Object obj) {
		return (obj instanceof MapComparator);
	}
}
