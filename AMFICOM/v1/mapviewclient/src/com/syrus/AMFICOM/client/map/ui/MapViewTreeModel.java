/**
 * $Id: MapViewTreeModel.java,v 1.20 2005/08/02 08:48:31 krupenn Exp $ Syrus
 * Systems Научно-технический центр Проект: АМФИКОМ Автоматизированный
 * МногоФункциональный Интеллектуальный Комплекс Объектного Мониторинга
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * @version $Revision: 1.20 $, $Date: 2005/08/02 08:48:31 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MapViewTreeModel implements ChildrenFactory {

	public static final String MAP_BRANCH = "Map";

	public static final String SCHEMES_BRANCH = "schemes";

	public static final String LIBRARIES_BRANCH = "libraries";

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

	static ImageIcon libraryIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/maplibrary.gif").getScaledInstance(
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static MapLibraryComparator libraryComparator = new MapLibraryComparator();

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

	public Collection findNodes(Item item, Collection objects) {
		Collection items = new LinkedList();
		fillFoundNodes(item, objects, items);
		return items;
	}

	public void fillFoundNodes(Item item, Collection objects, Collection items) {
		if(objects.contains(item.getObject()))
			items.add(item);
		for(Iterator iter = item.getChildren().iterator(); iter.hasNext();) {
			Item childNode = (Item )iter.next();
			fillFoundNodes(childNode, objects, items);
		}
	}

	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(MapViewTreeModel.MAP_BRANCH)) {
				populateMapNode((PopulatableIconedNode )node);
			} else if (s.equals(MapViewTreeModel.SCHEMES_BRANCH)) {
				populateSchemesNode((PopulatableIconedNode )node);
			} else if (s.equals(MapViewTreeModel.LIBRARIES_BRANCH)) {
				populateLibrariesNode((PopulatableIconedNode )node);
			}
		}
		else if(node.getObject() instanceof MapView) {
			populateMapViewNode((PopulatableIconedNode )node);
		}
	}

	void populateMapViewNode(PopulatableIconedNode node) {
		PopulatableIconedNode mapNode;
		PopulatableIconedNode schemesNode;
		PopulatableIconedNode librariesNode;

		if(node.getChildren().size() == 0) {
			mapNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.MAP_BRANCH,
					LangModelMap.getString(MapViewTreeModel.MAP_BRANCH),
					folderIcon,
					true);
//			mapNode.populate();
			node.addChild(mapNode);

			schemesNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.SCHEMES_BRANCH,
					LangModelMap.getString(MapViewTreeModel.SCHEMES_BRANCH),
					folderIcon,
					true);
//			schemesNode.populate();
			node.addChild(schemesNode);

			librariesNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.LIBRARIES_BRANCH,
					LangModelMap.getString(MapViewTreeModel.LIBRARIES_BRANCH),
					folderIcon,
					true);
//			mapNode.populate();
			node.addChild(librariesNode);
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

		final List<Scheme> schemesChildren = new ArrayList<Scheme>();
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

	void populateLibrariesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapView mapView = (MapView )parentNode.getObject();

		List librariesChildren = new ArrayList();
		librariesChildren.addAll(mapView.getMap().getMapLibraries());
		Collections.sort(librariesChildren, libraryComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
			MapLibrary library = (MapLibrary )childNode.getObject();
			if(librariesChildren.contains(library)) {
				if(childNode.isPopulated())
					childNode.populate();
				nodePresense.put(library, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = librariesChildren.iterator(); it.hasNext();) {
			MapLibrary library = (MapLibrary )it.next();
			Item childNode = (Item )nodePresense.get(library);
			if(childNode == null) {
				PopulatableIconedNode newItem = new PopulatableIconedNode(
						MapLibraryTreeModel.getInstance(),
						library,
						libraryIcon, 
						true);
//				newItem.populate();
				node.addChild(newItem);
			}
		}
	}
}

final class MapLibraryComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		MapLibrary library1 = (MapLibrary )o1;
		MapLibrary library2 = (MapLibrary )o2;
		return library1.getName().compareTo(library2.getName());
	}

	public boolean equals(Object obj) {
		return (obj instanceof MapLibraryComparator);
	}
}
