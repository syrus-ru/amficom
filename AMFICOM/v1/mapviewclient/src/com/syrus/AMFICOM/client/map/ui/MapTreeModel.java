/**
 * $Id: MapTreeModel.java,v 1.6 2005/06/22 08:43:49 krupenn Exp $ 
 * Syrus Systems 
 * Научно-технический центр 
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный Интеллектуальный 
 * Комплекс Объектного Мониторинга
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/22 08:43:49 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapTreeModel implements ChildrenFactory {

	public static final String MAPS_BRANCH = "innermaps";

	public static final String EXTERNAL_NODES_BRANCH = "externalnode";

	public static final String NONAME_BRANCH = "noname";

	public static MapElementComparator mapElementComparator = new MapElementComparator();

	public static MapComparator mapComparator = new MapComparator();

	public static SiteNodeTypeComparator siteNodeTypeComparator = new SiteNodeTypeComparator();

	static final int IMG_SIZE = 16;

	public static ImageIcon mapIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/map.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon nodeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/node_in_tree.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon externalNodeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/externalnode.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon linkIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/linkmode.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon collIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/collector.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon folderIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/folder.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	private static MapTreeModel instance;

	protected MapTreeModel() {
		// empty
	}
	
	public static MapTreeModel getInstance() {
		if(instance == null)
			instance = new MapTreeModel();
		return instance;
	}

	public Item findNode(Item item, Object object) {
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

	public String getObjectName(Object object) {
		if(object instanceof String)
			return LangModelMap.getString((String )object);
		else
		if(object instanceof Map) {
			Map map = (Map )object;
			return map.getName();
		}
		else
		if(object instanceof SiteNodeType) {
			SiteNodeType type = (SiteNodeType )object;
			return type.getName();
		}
		else
		if(object instanceof MapElement) {
			MapElement mapElement = (MapElement )object;
			return mapElement.getName();
		}
		return LangModelMap.getString(NONAME_BRANCH);
	}

	public String getNodeName(Item node) {
		if(node.getObject() instanceof String)
			return LangModelMap.getString((String )(node.getObject()));
		else
		if(node.getObject() instanceof MapView) {
			MapView mapView = (MapView )node.getObject();
			return mapView.getName();
		}
		else
		if(node.getObject() instanceof Map) {
			Map map = (Map )node.getObject();
			return map.getName();
		}
		else
		if(node.getObject() instanceof SiteNodeType) {
			SiteNodeType type = (SiteNodeType )node.getObject();
			return type.getName();
		}
		else
		if(node.getObject() instanceof MapElement) {
			MapElement mapElement = (MapElement )node
					.getObject();
			return mapElement.getName();
		}
		return LangModelMap.getString(NONAME_BRANCH);
	}

	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(MapTreeModel.MAPS_BRANCH)) {
				populateMapsNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapTreeModel.EXTERNAL_NODES_BRANCH)) {
				populateExternalNodesNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapViewController.ELEMENT_SITENODE)) {
				populateSitesNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapViewController.ELEMENT_TOPOLOGICALNODE)) {
				populateNodesNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapViewController.ELEMENT_PHYSICALLINK)) {
				populateLinksNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapViewController.ELEMENT_COLLECTOR)) {
				populateCollectorsNode((PopulatableIconedNode )node);
			}
		}
		else if(node.getObject() instanceof Map) {
			populateMapNode((PopulatableIconedNode )node);
		}
		else if(node.getObject() instanceof SiteNodeType) {
			populateTypeNode((PopulatableIconedNode )node);
		}
			
	}

	void populateMapNode(PopulatableIconedNode node) {
		PopulatableIconedNode mapsNode;
		PopulatableIconedNode sitesNode;
		PopulatableIconedNode externalNodesNode;
		PopulatableIconedNode nodesNode;
		PopulatableIconedNode linksNode;
		PopulatableIconedNode collectorsNode;

		if(node.getChildren().size() == 0) {
			mapsNode = new PopulatableIconedNode(
					this,
					MapTreeModel.MAPS_BRANCH,
					getObjectName(MapTreeModel.MAPS_BRANCH),
					folderIcon,
					true);
//			mapsNode.populate();
			node.addChild(mapsNode);

			externalNodesNode = new PopulatableIconedNode(
					this,
					MapTreeModel.EXTERNAL_NODES_BRANCH,
					getObjectName(MapTreeModel.EXTERNAL_NODES_BRANCH),
					folderIcon,
					true);
//			externalNodesNode.populate();
			node.addChild(externalNodesNode);

			sitesNode = new PopulatableIconedNode(
					this,
					MapViewController.ELEMENT_SITENODE,
					getObjectName(MapViewController.ELEMENT_SITENODE),
					folderIcon,
					true);
//			sitesNode.populate();
			node.addChild(sitesNode);

			nodesNode = new PopulatableIconedNode(
					this,
					MapViewController.ELEMENT_TOPOLOGICALNODE,
					getObjectName(MapViewController.ELEMENT_TOPOLOGICALNODE),
					folderIcon,
					true);
//			nodesNode.populate();
			node.addChild(nodesNode);

			linksNode = new PopulatableIconedNode(
					this,
					MapViewController.ELEMENT_PHYSICALLINK,
					getObjectName(MapViewController.ELEMENT_PHYSICALLINK),
					folderIcon,
					true);
//			linksNode.populate();
			node.addChild(linksNode);

			collectorsNode = new PopulatableIconedNode(
					this,
					MapViewController.ELEMENT_COLLECTOR,
					getObjectName(MapViewController.ELEMENT_COLLECTOR),
					folderIcon,
					true);
//			collectorsNode.populate();
			node.addChild(collectorsNode);
		}
		else {
			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
				if(childNode.isPopulated())
					childNode.populate();
			}
		}
	}

	void populateMapsNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		List mapsChildren = new ArrayList();
		mapsChildren.addAll(map.getMaps());
		Collections.sort(mapsChildren, MapTreeModel.mapComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
			Map innerMap = (Map )childNode.getObject();
			if(mapsChildren.contains(innerMap)) {
				if(childNode.isPopulated())
					childNode.populate();
				nodePresense.put(innerMap, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = mapsChildren.iterator(); it.hasNext();) {
			Map innerMap = (Map )it.next();
			Item childNode = (Item )nodePresense.get(innerMap);
			if(childNode == null) {
				PopulatableIconedNode newItem = new PopulatableIconedNode(
						this,
						innerMap,
						mapIcon, 
						true);
//				newItem.populate();
				node.addChild(newItem);
			}
		}
	}

	void populateSitesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		java.util.Collection types = this.getSiteNodeTypes(map);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
			SiteNodeType type = (SiteNodeType )childNode.getObject();
			if(types.contains(type)) {
				if(childNode.isPopulated())
					childNode.populate();
				nodePresense.put(type, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = types.iterator(); it.hasNext();) {
			SiteNodeType type = (SiteNodeType )it.next();
			Item childNode = (Item )nodePresense.get(type);
			if(childNode == null) {
				PopulatableIconedNode newItem = new PopulatableIconedNode(
						this,
						type,
						getObjectName(type),
						new ImageIcon(NodeTypeController.getImage(type)
								.getScaledInstance(
										IMG_SIZE,
										IMG_SIZE,
										Image.SCALE_SMOOTH)),
						true);

				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void populateTypeNode(PopulatableIconedNode node) {
		SiteNodeType type = (SiteNodeType )node.getObject();

		Item parentNode = node.getParent();
		Item parentNode2 = parentNode.getParent();
		Map map = (Map )parentNode2.getObject();

		List siteNodes = getSiteNodeTypeNodes(map, type);

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
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			Item childNode = (Item )nodePresense.get(site);
			if(childNode == null) {
//				PopulatableIconedNode newItem = new PopulatableIconedNode(
				Item newItem = new IconedNode(
						site,
						getObjectName(site),
						((IconedNode )node).getIcon(),
						false);
				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void populateExternalNodesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		Set siteNodes = map.getExternalNodes();

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
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			Item childNode = (Item )nodePresense.get(site);
			if(childNode == null) {
//				PopulatableIconedNode newItem = new PopulatableIconedNode(
				Item newItem = new IconedNode(
						site,
						getObjectName(site),
						new ImageIcon(NodeTypeController.getImage((SiteNodeType )site.getType())
								.getScaledInstance(
										IMG_SIZE,
										IMG_SIZE,
										Image.SCALE_SMOOTH)),
						false);
				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void populateNodesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		List nodes = new ArrayList(map.getTopologicalNodes());
		Collections.sort(nodes, MapTreeModel.mapElementComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			TopologicalNode topologicalNode = (TopologicalNode )childNode
					.getObject();
			if(nodes.contains(topologicalNode))
				nodePresense.put(topologicalNode, childNode);
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = nodes.iterator(); it.hasNext();) {
			TopologicalNode topologicalNode = (TopologicalNode )it.next();
			Item childNode = (Item )nodePresense.get(topologicalNode);
			if(childNode == null) {
				Item newItem = new IconedNode(
						topologicalNode,
						getObjectName(topologicalNode),
						nodeIcon,
						false);
				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void populateLinksNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		List links = new ArrayList(map.getPhysicalLinks());
		Collections.sort(links, MapTreeModel.mapElementComparator);

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
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = links.iterator(); it.hasNext();) {
			PhysicalLink link = (PhysicalLink )it.next();
			Item childNode = (Item )nodePresense.get(link);
			if(childNode == null) {
				Item newItem = new IconedNode(
						link,
						getObjectName(link),
						linkIcon,
						false);
				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	void populateCollectorsNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		List collectors = new ArrayList(map.getCollectors());
		Collections.sort(collectors, MapTreeModel.mapElementComparator);

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
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		int i = 0;
		for(Iterator it = collectors.iterator(); it.hasNext();) {
			Collector collector = (Collector )it.next();
			Item childNode = (Item )nodePresense.get(collector);
			if(childNode == null) {
				Item newItem = new IconedNode(
						collector,
						getObjectName(collector),
						collIcon,
						false);
				// if(node.getChildren().isEmpty())
				node.addChild(newItem);
				// else
				// node.getChildren().add(i, newItem);
			}
			i++;
		}
	}

	java.util.Collection getSiteNodeTypes(Map map) {
		java.util.Collection types = new HashSet();
		for(Iterator it = map.getSiteNodes().iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			types.add(site.getType());
		}
		return types;
	}

	java.util.List getSiteNodeTypeNodes(Map map, SiteNodeType type) {
		List list = new LinkedList();
		for(Iterator it = map.getSiteNodes().iterator(); it.hasNext();) {
			SiteNode site = (SiteNode ) it.next();
			if(site.getType().equals(type))
				list.add(site);
		}
		return list;
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
