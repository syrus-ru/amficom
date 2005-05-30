/**
 * $Id: MapViewTreeModel.java,v 1.14 2005/05/30 12:19:02 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * @version $Revision: 1.14 $, $Date: 2005/05/30 12:19:02 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapViewTreeModel implements ChildrenFactory {

	public static final String MAP_BRANCH = "Map";

	public static final String SCHEMES_BRANCH = "schemes";

	public static final String NONAME_BRANCH = "noname";

	static final int IMG_SIZE = 16;

	static ImageIcon mapViewIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/mapview.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon folderIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/folder.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	MapSchemeTreeModel schemeTreeModel;
	MapTreeModel mapTreeModel;

	private static MapViewTreeModel instance;
	
	protected MapViewTreeModel() {
		// empty
	}
	
	public static MapViewTreeModel getInstance() {
		if(instance == null)
			instance = new MapViewTreeModel();
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
		if(object instanceof MapView) {
			MapView mapView = (MapView )object;
			return mapView.getName();
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
			if (s.equals(MapViewTreeModel.MAP_BRANCH)) {
				populateMapNode((PopulatableIconedNode )node);
			}
			else if (s.equals(MapViewTreeModel.SCHEMES_BRANCH)) {
				populateSchemesNode((PopulatableIconedNode )node);
			}
		}
		else if(node.getObject() instanceof MapView) {
			populateMapViewNode((PopulatableIconedNode )node);
		}
	}

	void populateMapViewNode(PopulatableIconedNode node) {
		PopulatableIconedNode mapNode;
		PopulatableIconedNode schemesNode;

		if(node.getChildren().size() == 0) {
			mapNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.MAP_BRANCH,
					getObjectName(MapViewTreeModel.MAP_BRANCH),
					folderIcon,
					true);
//			mapNode.populate();
			node.addChild(mapNode);

			schemesNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.SCHEMES_BRANCH,
					getObjectName(MapViewTreeModel.SCHEMES_BRANCH),
					folderIcon,
					true);
//			schemesNode.populate();
			node.addChild(schemesNode);
		}
		else {
			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
				if(childNode.isPopulated())
					childNode.populate();
			}
		}
	}

	void populateMapNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapView mapView = (MapView )parentNode.getObject();
		if(node.getChildren().size() == 0) {
			PopulatableIconedNode mapNode = new PopulatableIconedNode(
					MapTreeModel.getInstance(),
					mapView.getMap(), 
					MapTreeModel.mapIcon,
					true);
			node.addChild(mapNode);
		}
		else {
			PopulatableIconedNode childNode = (PopulatableIconedNode )node.getChildren().iterator().next();
			if(childNode.getObject().equals(mapView.getMap())) {
				if(childNode.isPopulated())
					childNode.populate();
			}
			else {
				childNode.setParent(null);
				childNode = new PopulatableIconedNode(
						MapTreeModel.getInstance(),
						mapView.getMap(), 
						MapTreeModel.mapIcon,
						true);
				node.addChild(childNode);
			}
		}
	}

	void populateSchemesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapView mapView = (MapView )parentNode.getObject();

		List schemesChildren = new ArrayList();
		schemesChildren.addAll(mapView.getSchemes());
		Collections.sort(schemesChildren, MapSchemeTreeModel.schemeComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
			Scheme scheme = (Scheme )childNode.getObject();
			if(schemesChildren.contains(scheme)) {
				if(childNode.isPopulated())
					childNode.populate();
				nodePresense.put(scheme, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = schemesChildren.iterator(); it.hasNext();) {
			Scheme scheme = (Scheme )it.next();
			Item childNode = (Item )nodePresense.get(scheme);
			if(childNode == null) {
				MapSchemeTreeNode newItem = new MapSchemeTreeNode(
						MapSchemeTreeModel.getInstance(),
						scheme,
						MapSchemeTreeModel.schemeIcon, 
						true);
				newItem.setTopological(true);
//				newItem.populate();
				node.addChild(newItem);
			}
		}
	}

}
