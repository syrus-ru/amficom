/**
 * $Id: MapEditorTreeModel.java,v 1.9 2005/09/25 16:08:03 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/25 16:08:03 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapEditorTreeModel extends AbstractChildrenFactory {

	public static final String MAP_EDITOR_TREE_ROOT = MapEditorResourceKeys.TREE_MAP_EDITOR_ROOT;

	private MapView mapView;
	private PopulatableIconedNode root;
	private PopulatableIconedNode topologyNode;

	public MapEditorTreeModel() {
		// empty
	}

	public MapEditorTreeModel(MapView mapView) {
		this.mapView = mapView;
	}

	public PopulatableIconedNode getRoot() {
		if(this.root == null) {
			this.root = new PopulatableIconedNode(
				this,
				MapEditorTreeModel.MAP_EDITOR_TREE_ROOT,
				LangModelMap.getString(MapEditorTreeModel.MAP_EDITOR_TREE_ROOT),
				UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG), 
				true);
		}
		return this.root;
	}

	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(MapEditorTreeModel.MAP_EDITOR_TREE_ROOT)) {
				populateRootNode((PopulatableIconedNode )node);
			}
		}
	}

	private void populateRootNode(PopulatableIconedNode node) {
		if(this.mapView == null) {
			return;
		}

		Collection contents = super.getChildObjects(node);
		if (!contents.contains(this.mapView)) {
			PopulatableIconedNode item = MapViewTreeModel.createSingleMapViewRoot(this.mapView);
			item.getChildrenFactory().populate(item);
			node.addChild(item);
//			System.out.println("add " + this.mapView.getName() + " to " + node.getObject().toString());
		}
		if(this.topologyNode == null) {
			TopologyTreeModel topologyTreeModel = new TopologyTreeModel();
			this.topologyNode = topologyTreeModel.getRoot();
			node.addChild(this.topologyNode);
			this.topologyNode.getChildrenFactory().populate(this.topologyNode);
//			System.out.println("add " + this.topologyNode.getObject().toString() + " to " + node.getObject().toString());
		}
		for(Item item : node.getChildren()) {
			((PopulatableIconedNode) item).getChildrenFactory().populate(item);
//			System.out.println("populate " + item.getObject().toString() + " at " + node.getObject().toString());
		}
	}

	public MapView getMapView() {
		return this.mapView;
	}

	public void setMapView(MapView mapView) {
		if(mapView == null
				|| (this.mapView != null
						&& !mapView.equals(this.mapView)) ) {
			List<Item> children = new LinkedList<Item>(this.getRoot().getChildren());
//			System.out.println("remove " + children.size() + " children");
			for(Item item : children) {
//				System.out.println("remove " + item.getObject().toString() + "...");
				item.setParent(null);
//				System.out.println("removed " + item.getObject().toString());
			}
			this.topologyNode = null;
		}

		this.mapView = mapView;

		this.getRoot().getChildrenFactory().populate(this.getRoot());
	}

	public void updateTopologyTree(NetMapViewer netMapViewer) {
		if(this.topologyNode != null) {
			this.topologyNode.setParent(null);
//			System.out.println("remove topologyNode");
		}
		TopologyTreeModel topologyTreeModel = new TopologyTreeModel();
		this.topologyNode = topologyTreeModel.getRoot();
		topologyTreeModel.setNetMapViewer(netMapViewer);
		this.getRoot().addChild(this.topologyNode);
//		System.out.println("add " + this.topologyNode.getObject().toString() + " to " + this.getRoot().getObject().toString());
	}

}

