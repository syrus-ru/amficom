/**
 * $Id: MapEditorTreeModel.java,v 1.1 2005/09/14 10:19:21 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @version $Revision: 1.1 $, $Date: 2005/09/14 10:19:21 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public final class MapEditorTreeModel implements ChildrenFactory {

	public static final String MAP_EDITOR_TREE_ROOT = "mapeditorroot";

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
				new MapEditorTreeModel(),
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

		Collection contents = CommonUIUtilities.getChildObjects(node);
		if (!contents.contains(this.mapView)) {
			PopulatableIconedNode item = MapViewTreeModel.createSingleMapViewRoot(this.mapView);
			item.getChildrenFactory().populate(item);
			node.addChild(item);
		}
		if(this.topologyNode == null) {
			TopologyTreeModel topologyTreeModel = new TopologyTreeModel();
			this.topologyNode = topologyTreeModel.getRoot();
			node.addChild(this.topologyNode);
			this.topologyNode.getChildrenFactory().populate(this.topologyNode);
		}
	}

	public MapView getMapView() {
		return this.mapView;
	}

	public void setMapView(MapView mapView) {
		if(mapView == null
				|| (this.mapView != null
						&& mapView.equals(this.mapView)) ) {
			List children = new LinkedList(this.root.getChildren());
			for(Iterator iter = children.iterator(); iter.hasNext();) {
				Item item = (Item )iter.next();
				item.setParent(null);
			}
		}

		this.mapView = mapView;

		this.root.getChildrenFactory().populate(this.root);
	}

}

