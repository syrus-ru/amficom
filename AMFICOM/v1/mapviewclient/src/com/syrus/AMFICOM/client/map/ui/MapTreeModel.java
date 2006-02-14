/*-
 * $$Id: MapTreeModel.java,v 1.28 2006/02/14 10:20:07 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D.Double;
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
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.CollectorConditionWrapper;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapUtils;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.PhysicalLinkConditionWrapper;
import com.syrus.AMFICOM.client.map.SiteNodeConditionWrapper;
import com.syrus.AMFICOM.client.map.controllers.MapElementController;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.TopologicalNode;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.28 $, $Date: 2006/02/14 10:20:07 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapTreeModel extends AbstractChildrenFactory {

	public static final String ALL_MAPS_BRANCH = MapEditorResourceKeys.TREE_ALL_MAPS;

	public static final String MAPS_BRANCH = MapEditorResourceKeys.TREE_INNER_MAPS;

	public static final String EXTERNAL_NODES_BRANCH = MapEditorResourceKeys.TREE_EXTERNAL_NODE;

	public static MapElementComparator mapElementComparator = new MapElementComparator();

	public static MapComparator mapComparator = new MapComparator();

	public static SiteNodeTypeComparator siteNodeTypeComparator = new SiteNodeTypeComparator();

	static final int IMG_SIZE = 16;

	public static ImageIcon mapIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/map.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon nodeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/node_in_tree.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon externalNodeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/externalnode.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon linkIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/linkmode.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static ImageIcon collIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/collector.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	NetMapViewer netMapViewer = null;

	public MapTreeModel() {
		// empty
	}
	
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		this.netMapViewer = netMapViewer;
	}

	public PopulatableIconedNode createAllMapsRoot() {
		PopulatableIconedNode root = new PopulatableIconedNode(
				this,
				MapTreeModel.ALL_MAPS_BRANCH,
				this.getObjectName(MapTreeModel.ALL_MAPS_BRANCH),
				mapIcon, 
				true);
		return root;
	}
	
	public PopulatableIconedNode createSingleMapRoot(Map map) {
		PopulatableIconedNode root = new PopulatableIconedNode(
				this,
				map,
				mapIcon, 
				true);
		return root;
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
			return I18N.getString((String )object);
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
		return I18N.getString(MapEditorResourceKeys.NONAME);
	}

	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(MapTreeModel.ALL_MAPS_BRANCH)) {
				populateAllMapsNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapTreeModel.MAPS_BRANCH)) {
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
		FiltrableIconedNode sitesNode;
		FiltrableIconedNode externalNodesNode;
		PopulatableIconedNode nodesNode;
		FiltrableIconedNode linksNode;
		FiltrableIconedNode collectorsNode;

		if(node.getChildren().size() == 0) {
			mapsNode = new PopulatableIconedNode(
					this,
					MapTreeModel.MAPS_BRANCH,
					getObjectName(MapTreeModel.MAPS_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(mapsNode);

			externalNodesNode = new FiltrableIconedNode();
			externalNodesNode.setObject(MapTreeModel.EXTERNAL_NODES_BRANCH);
			externalNodesNode.setIcon(UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG));
			externalNodesNode.setName(getObjectName(MapTreeModel.EXTERNAL_NODES_BRANCH));
			externalNodesNode.setChildrenFactory(this);
			externalNodesNode.setCanHaveChildren(true);
			externalNodesNode.setDefaultCondition(null);
			externalNodesNode.setFilter(new Filter(new SiteNodeConditionWrapper()));
			node.addChild(externalNodesNode);

			sitesNode = new FiltrableIconedNode();
			sitesNode.setObject(MapViewController.ELEMENT_SITENODE);
			sitesNode.setIcon(UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG));
			sitesNode.setName(getObjectName(MapViewController.ELEMENT_SITENODE));
			sitesNode.setChildrenFactory(this);
			sitesNode.setCanHaveChildren(true);
			sitesNode.setDefaultCondition(null);
			sitesNode.setFilter(new Filter(new SiteNodeConditionWrapper()));
			node.addChild(sitesNode);

			nodesNode = new PopulatableIconedNode(
					this,
					MapViewController.ELEMENT_TOPOLOGICALNODE,
					getObjectName(MapViewController.ELEMENT_TOPOLOGICALNODE),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(nodesNode);

			linksNode = new FiltrableIconedNode();
			linksNode.setObject(MapViewController.ELEMENT_PHYSICALLINK);
			linksNode.setIcon(UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG));
			linksNode.setName(getObjectName(MapViewController.ELEMENT_PHYSICALLINK));
			linksNode.setChildrenFactory(this);
			linksNode.setCanHaveChildren(true);
			linksNode.setDefaultCondition(null);
			linksNode.setFilter(new Filter(new PhysicalLinkConditionWrapper()));
			node.addChild(linksNode);

			collectorsNode = new FiltrableIconedNode();
			collectorsNode.setObject(MapViewController.ELEMENT_COLLECTOR);
			collectorsNode.setIcon(UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG));
			collectorsNode.setName(getObjectName(MapViewController.ELEMENT_COLLECTOR));
			collectorsNode.setChildrenFactory(this);
			collectorsNode.setCanHaveChildren(true);
			collectorsNode.setDefaultCondition(null);
			collectorsNode.setFilter(new Filter(new CollectorConditionWrapper()));
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

	void populateAllMapsNode(PopulatableIconedNode node) {
		List<Map> mapsChildren = new LinkedList();
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAP_CODE);
			Set<Map> maps = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			mapsChildren = new ArrayList<Map>(maps);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		Collections.sort(mapsChildren, MapTreeModel.mapComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

//		Collection<Object> childObjects = super.getChildObjects(node);
//		List<Item> toRemove = super.getItemsToRemove(mapsChildren, childObjects);
//		List<Map> toAdd = super.getObjectsToAdd(mapsChildren, childObjects);

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
				node.addChild(newItem);
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

		Set siteNodes = map.getSiteNodes();
		
		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
			if(condition != null) {
				siteNodes = MapUtils.applyCondition(siteNodes, condition);
			} else if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				MapViewController mapViewController = logicalNetLayer.getMapViewController();
				Set<SiteNode> visibleSiteNodes = new HashSet<SiteNode>();
				for(Iterator iter = siteNodes.iterator(); iter.hasNext();) {
					SiteNode siteNode = (SiteNode) iter.next();
					if(mapViewController.isElementVisible(siteNode, visibleBounds)) {
						visibleSiteNodes.add(siteNode);
					}
				}
				siteNodes = visibleSiteNodes;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}

		java.util.Collection types = this.getSiteNodeTypes(siteNodes);

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
		long a = System.currentTimeMillis();
		List<Item> toAdd = new LinkedList<Item>();
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

				toAdd.add(newItem);
//				node.addChild(newItem);
			}
			i++;
		}
		long b = System.currentTimeMillis();
		for(Item item : toAdd) {
			node.addChild(item);
		}
		long c = System.currentTimeMillis();
//		System.out.println("count " + toAdd.size() + ", create children " + (b - a) + " ms, add children " + (c - b) + " ms");
	}

	void populateTypeNode(PopulatableIconedNode node) {
		SiteNodeType type = (SiteNodeType )node.getObject();

		Item parentNode = node.getParent();
		Item parentNode2 = parentNode.getParent();
		Map map = (Map )parentNode2.getObject();

		Set siteNodesSet = map.getSiteNodes();
		
		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)parentNode).getResultingCondition();
			if(condition != null) {
				siteNodesSet = MapUtils.applyCondition(siteNodesSet, condition);
			} else if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				Set<SiteNode> visibleSiteNodes = new HashSet<SiteNode>();
				for(Iterator iter = siteNodesSet.iterator(); iter.hasNext();) {
					SiteNode siteNode = (SiteNode) iter.next();
					MapElementController controller = logicalNetLayer
							.getMapViewController()
								.getController(siteNode);
					if(controller.isElementVisible(siteNode, visibleBounds)) {
						visibleSiteNodes.add(siteNode);
					}
				}
				siteNodesSet = visibleSiteNodes;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}

		List siteNodes = getSiteNodeTypeNodes(siteNodesSet, type);

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
		long a = System.currentTimeMillis();
		List<Item> toAdd = new LinkedList<Item>();
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			Item childNode = (Item )nodePresense.get(site);
			if(childNode == null) {
				Item newItem = new IconedNode(
						site,
						getObjectName(site),
						((IconedNode )node).getIcon(),
						false);
				toAdd.add(newItem);
//				node.addChild(newItem);
			}
			i++;
		}
		long b = System.currentTimeMillis();
		for(Item item : toAdd) {
			node.addChild(item);
		}
		long c = System.currentTimeMillis();
//		System.out.println("count " + toAdd.size() + ", create children " + (b - a) + " ms, add children " + (c - b) + " ms");
	}

	void populateExternalNodesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		Map map = (Map )parentNode.getObject();

		Set siteNodes = map.getExternalNodes();
		
		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
			if(condition != null) {
				siteNodes = MapUtils.applyCondition(siteNodes, condition);
			} else if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				Set<SiteNode> visibleSiteNodes = new HashSet<SiteNode>();
				for(Iterator iter = siteNodes.iterator(); iter.hasNext();) {
					SiteNode siteNode = (SiteNode) iter.next();
					MapElementController controller = logicalNetLayer
							.getMapViewController()
								.getController(siteNode);
					if(controller.isElementVisible(siteNode, visibleBounds)) {
						visibleSiteNodes.add(siteNode);
					}
				}
				siteNodes = visibleSiteNodes;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}

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
						new ImageIcon(NodeTypeController.getImage(site.getType())
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

		Set<TopologicalNode> nodesSet = map.getTopologicalNodes();
		try {
			if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				Set<TopologicalNode> visibleTopologicalNodes = new HashSet<TopologicalNode>();
				for(Iterator iter = nodesSet.iterator(); iter.hasNext();) {
					TopologicalNode topologicalNode = (TopologicalNode) iter.next();
					MapElementController controller = logicalNetLayer
							.getMapViewController()
								.getController(topologicalNode);
					if(controller.isElementVisible(topologicalNode, visibleBounds)) {
						visibleTopologicalNodes.add(topologicalNode);
					}
				}
				nodesSet = visibleTopologicalNodes;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}

		List nodes = new ArrayList(nodesSet);
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
						I18N.getString(MapEditorResourceKeys.NONAME),
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

		Set linksSet = map.getPhysicalLinks();

		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
			if(condition != null) {
				linksSet = MapUtils.applyCondition(linksSet, condition);
			} else if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				Set<PhysicalLink> visibleLinks = new HashSet<PhysicalLink>();
				for(Iterator iter = linksSet.iterator(); iter.hasNext();) {
					PhysicalLink physicalLink = (PhysicalLink) iter.next();
					MapElementController controller = logicalNetLayer
							.getMapViewController()
								.getController(physicalLink);
					if(controller.isElementVisible(physicalLink, visibleBounds)) {
						visibleLinks.add(physicalLink);
					}
				}
				linksSet = visibleLinks;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}
		List links = new ArrayList(linksSet);
		
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

		Set collectorsSet = map.getCollectors();

		try {
			StorableObjectCondition condition = ((FiltrableIconedNode)node).getResultingCondition();
			if(condition != null) {
				collectorsSet = MapUtils.applyCondition(collectorsSet, condition);
			} else if(this.netMapViewer != null) {
				Double visibleBounds = this.netMapViewer.getVisibleBounds();
				LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
				Set<Collector> visibleCollectors = new HashSet<Collector>();
				for(Iterator iter = collectorsSet.iterator(); iter.hasNext();) {
					Collector collector = (Collector) iter.next();
					MapElementController controller = logicalNetLayer
							.getMapViewController()
								.getController(collector);
					if(controller.isElementVisible(collector, visibleBounds)) {
						visibleCollectors.add(collector);
					}
				}
				collectorsSet = visibleCollectors;
			}
		} catch (Exception e) {
			Log.errorMessage(e);
		}
		List collectors = new ArrayList(collectorsSet);
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

	java.util.Collection getSiteNodeTypes(Set siteNodes) {
		java.util.Collection types = new HashSet();
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
			SiteNode site = (SiteNode )it.next();
			types.add(site.getType());
		}
		return types;
	}

	java.util.List getSiteNodeTypeNodes(Set siteNodes, SiteNodeType type) {
		List list = new LinkedList();
		for(Iterator it = siteNodes.iterator(); it.hasNext();) {
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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof SiteNodeTypeComparator);
	}
}

final class MapElementComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		MapElement mapElement1 = (MapElement )o1;
		MapElement mapElement2 = (MapElement )o2;
		return mapElement1.getName().compareTo(mapElement2.getName());
	}

	@Override
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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MapComparator);
	}
}
