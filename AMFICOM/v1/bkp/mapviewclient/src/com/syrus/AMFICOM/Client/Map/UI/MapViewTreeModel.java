/**
 * $Id: MapViewTreeModel.java,v 1.7 2005/04/05 15:48:07 krupenn Exp $
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
import java.util.Comparator;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;


/**
 * @version $Revision: 1.7 $, $Date: 2005/04/05 15:48:07 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapViewTreeModel extends DefaultTreeModel implements TreeModel {
	public static final String MAP_BRANCH = "Map";
	public static final String MAPS_BRANCH = "innermaps";

	MapView mapView = null;

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

	public MapViewTreeModel(MapView mapView) {
		super(null);
//		buildTree(mapView);
	}
	
	public void setMapView(MapView mapView) {
//		buildTree(mapView);
	}
/*
	public StorableObjectTreeNode findNode(Object object)
	{
		return findNode((StorableObjectTreeNode )(this.root), object);
	}

	public StorableObjectTreeNode findNode(StorableObjectTreeNode node, Object object)
	{
		if(node.getUserObject().equals(object))
			return node;
		for (int i = node.getChildCount() - 1; i >= 0; i--)
		{
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			StorableObjectTreeNode foundNode = findNode(childNode, object);
			if(foundNode != null)
				return foundNode;
		}
		return null;
		
	}

	void buildTree(MapView mapView) {
		if(mapView == null) {
			this.root = new StorableObjectTreeNode("noname", getObjectName("noname"));
		}
		else {
			if(this.mapView != null && this.mapView.equals(mapView)) {
				updateTree((StorableObjectTreeNode )this.root);
			}
			else {
				this.root = new StorableObjectTreeNode(mapView, getObjectName(mapView), mapViewIcon);
				StorableObjectTreeNode mapNode = new StorableObjectTreeNode(
						MapViewTreeModel.MAP_BRANCH,
						getObjectName(MapViewTreeModel.MAP_BRANCH),
						folderIcon);
				((StorableObjectTreeNode )this.root).add(mapNode);
				mapNode.add(buildMapTree(mapView.getMap()));
			}
		}
		this.mapView = mapView;
	}

	StorableObjectTreeNode buildMapTree(Map map) {
		StorableObjectTreeNode node;
			
		node = new StorableObjectTreeNode(map, getObjectName(map), mapIcon);

		node.add(buildMapsTree(map));
		node.add(buildSiteTree(map));
		node.add(buildNodeTree(map));
		node.add(buildLinkTree(map));
		node.add(buildCollTree(map));
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

	
	public String getNodeName(StorableObjectTreeNode node) {
		if(node.getUserObject() instanceof String)
			return LangModelMap.getString((String )(node.getUserObject()));
		else if(node.getUserObject() instanceof MapView) {
			MapView mapView = (MapView )node.getUserObject();
			return mapView.getName();
		}
		else if(node.getUserObject() instanceof Map) {
			Map map = (Map )node.getUserObject();
			return map.getName();
		}
		else if(node.getUserObject() instanceof SiteNodeType) {
			SiteNodeType type = (SiteNodeType )node.getUserObject();
			return type.getName();
		}
		else if(node.getUserObject() instanceof MapElement) {
			MapElement mapElement = (MapElement )node.getUserObject();
			return mapElement.getName();
		}
		return LangModelMap.getString("noname");
	}

	StorableObjectTreeNode buildMapsTree(Map map) {
		StorableObjectTreeNode mapChildrenNode = new StorableObjectTreeNode(
				MapViewTreeModel.MAPS_BRANCH, 
				getObjectName(MapViewTreeModel.MAPS_BRANCH),
				folderIcon);

		List mapsChildren = new ArrayList();
		mapsChildren.addAll(map.getMaps());
		Collections.sort(mapsChildren, MapViewTreeModel.mapComparator);
		
		for(Iterator it = mapsChildren.iterator(); it.hasNext();)
		{
			Map innerMap = (Map )it.next();
			mapChildrenNode.add(buildMapTree(innerMap));
		}

		return mapChildrenNode;
	}

	StorableObjectTreeNode buildSiteTree(Map map) {
		StorableObjectTreeNode siteChildrenNode = new StorableObjectTreeNode(
				MapViewController.ELEMENT_SITENODE, 
				getObjectName(MapViewController.ELEMENT_SITENODE),
				folderIcon);

		java.util.Map types = this.getSiteNodeTypes(map);
		for(Iterator it = types.keySet().iterator(); it.hasNext();)
		{
			SiteNodeType type = (SiteNodeType )it.next();
			List siteNodes = (List )types.get(type);
			siteChildrenNode.add(buildSiteTree(map, type, siteNodes));
		}
		return siteChildrenNode;
	}

	StorableObjectTreeNode buildSiteTree(Map map, SiteNodeType type, List siteNodes) {
		StorableObjectTreeNode siteChildrenNode = new StorableObjectTreeNode(
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
			siteChildrenNode.add(new StorableObjectTreeNode(site, getObjectName(site), (ImageIcon)(siteChildrenNode.getIcon()), true));
		}
				
		return siteChildrenNode;
	}


	StorableObjectTreeNode buildNodeTree(Map map) {
		StorableObjectTreeNode nodeChildrenNode = new StorableObjectTreeNode(
				MapViewController.ELEMENT_TOPOLOGICALNODE, 
				getObjectName(MapViewController.ELEMENT_TOPOLOGICALNODE),
				folderIcon);
		List nodes = new ArrayList(map.getTopologicalNodes());
		Collections.sort(nodes, MapViewTreeModel.mapElementComparator);
		for(Iterator it = nodes.iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			nodeChildrenNode.add(new StorableObjectTreeNode(topologicalNode, getObjectName(topologicalNode), nodeIcon, true));
		}
		return nodeChildrenNode;
	}

	StorableObjectTreeNode buildLinkTree(Map map) {
		StorableObjectTreeNode linkChildrenNode = new StorableObjectTreeNode(
				MapViewController.ELEMENT_PHYSICALLINK, 
				getObjectName(MapViewController.ELEMENT_PHYSICALLINK),
				folderIcon);
		List links = new ArrayList(map.getPhysicalLinks());
		Collections.sort(links, MapViewTreeModel.mapElementComparator);
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			linkChildrenNode.add(new StorableObjectTreeNode(link, getObjectName(link), linkIcon, true));
		}
		return linkChildrenNode;
	}

	StorableObjectTreeNode buildCollTree(Map map) {
		StorableObjectTreeNode collChildrenNode = new StorableObjectTreeNode(
				MapViewController.ELEMENT_COLLECTOR, 
				getObjectName(MapViewController.ELEMENT_COLLECTOR),
				folderIcon);
		List collectors = new ArrayList(map.getCollectors());
		Collections.sort(collectors, MapViewTreeModel.mapElementComparator);
		for(Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			collChildrenNode.add(new StorableObjectTreeNode(collector, getObjectName(collector), collIcon, true));
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

	void updateTree(StorableObjectTreeNode node) {
		StorableObjectTreeNode mapTopNode = (StorableObjectTreeNode )node.getChildAt(0);
		
		StorableObjectTreeNode mapNode = (StorableObjectTreeNode )mapTopNode.getChildAt(0);
		Map map = (Map )mapNode.getUserObject();
		if(map.equals(this.mapView.getMap()))
			updateMapTree(mapNode);
		else {
			mapTopNode.remove(mapNode);
			mapTopNode.add(buildMapTree(this.mapView.getMap()));
		}
	}

	void updateMapTree(StorableObjectTreeNode node) {
		Map map = (Map )node.getUserObject();
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			if(childNode.getUserObject().equals(MapViewTreeModel.MAPS_BRANCH))
				updateMapsTree(childNode, map);
			else if(childNode.getUserObject().equals(MapViewController.ELEMENT_SITENODE))
				updateSiteTree(childNode, map);
			else if(childNode.getUserObject().equals(MapViewController.ELEMENT_TOPOLOGICALNODE))
				updateNodeTree(childNode, map);
			else if(childNode.getUserObject().equals(MapViewController.ELEMENT_PHYSICALLINK))
				updateLinkTree(childNode, map);
			else if(childNode.getUserObject().equals(MapViewController.ELEMENT_COLLECTOR))
				updateCollTree(childNode, map);
		}
	}

	void updateMapsTree(StorableObjectTreeNode node, Map map) {
		List mapsChildren = new ArrayList();
		mapsChildren.addAll(map.getMaps());
		Collections.sort(mapsChildren, MapViewTreeModel.mapComparator);
		
		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			Map innerMap = (Map )childNode.getUserObject();
			if(mapsChildren.contains(innerMap)) {
				updateMapTree(childNode);
				nodePresense.put(innerMap, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;
		for(Iterator it = mapsChildren.iterator(); it.hasNext();) {
			Map innerMap = (Map )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(innerMap);
			if(childNode == null)
				insertNodeInto(buildMapTree(innerMap), node, i);
			i++;
		}
	}

	void updateSiteTree(StorableObjectTreeNode node, Map map) {
		java.util.Map types = this.getSiteNodeTypes(map);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			SiteNodeType type = (SiteNodeType )childNode.getUserObject();
			if(types.containsKey(type)) {
				List siteNodes = (List )types.get(type);
				updateSiteTree(childNode, type, siteNodes);
				nodePresense.put(type, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;

		for(Iterator it = types.keySet().iterator(); it.hasNext();) {
			SiteNodeType type = (SiteNodeType )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(type);
			if(childNode == null) {
				List siteNodes = (List )types.get(type);
				insertNodeInto(buildSiteTree(map, type, siteNodes), node, i);
			}
			i++;
		}
	}

	void updateSiteTree(StorableObjectTreeNode node, SiteNodeType type, List siteNodes) {
		Collections.sort(siteNodes, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			SiteNode site = (SiteNode )childNode.getUserObject();
			if(siteNodes.contains(site))
				nodePresense.put(site, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;

		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(site);
			if(childNode == null)
				insertNodeInto(new StorableObjectTreeNode(site, getObjectName(site), (ImageIcon)(node.getIcon()), true), node, i);
			i++;
		}
	}


	void updateNodeTree(StorableObjectTreeNode node, Map map) {
		List nodes = new ArrayList(map.getTopologicalNodes());
		Collections.sort(nodes, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			TopologicalNode topologicalNode = (TopologicalNode )childNode.getUserObject();
			if(nodes.contains(topologicalNode))
				nodePresense.put(topologicalNode, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;
		for(Iterator it = nodes.iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(topologicalNode);
			if(childNode == null)
				insertNodeInto(new StorableObjectTreeNode(topologicalNode, getObjectName(topologicalNode), nodeIcon, true), node, i);
			i++;
		}
	}

	void updateLinkTree(StorableObjectTreeNode node, Map map) {
		List links = new ArrayList(map.getPhysicalLinks());
		Collections.sort(links, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			PhysicalLink link = (PhysicalLink )childNode.getUserObject();
			if(links.contains(link))
				nodePresense.put(link, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(link);
			if(childNode == null)
				insertNodeInto(new StorableObjectTreeNode(link, getObjectName(link), linkIcon, true), node, i);
			i++;
		}
	}

	void updateCollTree(StorableObjectTreeNode node, Map map) {
		List collectors = new ArrayList(map.getCollectors());
		Collections.sort(collectors, MapViewTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();
		
		List toRemove = new LinkedList();
		
		for (int i = 0; i < node.getChildCount(); i++) {
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )node.getChildAt(i);
			Collector collector = (Collector )childNode.getUserObject();
			if(collectors.contains(collector))
				nodePresense.put(collector, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();)
		{
			node.remove((StorableObjectTreeNode )it.next());
		}
		
		int i = 0;
		for(Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			StorableObjectTreeNode childNode = (StorableObjectTreeNode )nodePresense.get(collector);
			if(childNode == null)
				insertNodeInto(new StorableObjectTreeNode(collector, getObjectName(collector), collIcon, true), node, i);
			i++;
		}
	}
*/
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
