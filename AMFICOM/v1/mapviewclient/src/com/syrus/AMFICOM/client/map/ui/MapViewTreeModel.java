/*-
 * $$Id: MapViewTreeModel.java,v 1.34 2006/02/08 14:10:19 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;

/**
 * @version $Revision: 1.34 $, $Date: 2006/02/08 14:10:19 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapViewTreeModel implements ChildrenFactory {

	public static final String ALL_MAP_VIEWS_BRANCH = MapEditorResourceKeys.TREE_ALL_MAP_VIEWS;

	public static final String MAP_BRANCH = MapEditorResourceKeys.TREE_MAP;

	public static final String SCHEMES_BRANCH = MapEditorResourceKeys.TREE_SCHEMES;

	public static final String LIBRARIES_BRANCH = MapEditorResourceKeys.TREE_LIBRARIES;

	static final int IMG_SIZE = 16;

	static ImageIcon mapViewIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/mapview.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	public static MapViewComparator mapViewComparator = new MapViewComparator();
	
	private static MapViewTreeModel instance;
	private static SchemeTreeModel schemeTreeModel;

	protected MapViewTreeModel() {
		// empty
	}

	public static MapViewTreeModel getInstance(ApplicationContext aContext) {
		if(instance == null) {
			instance = new MapViewTreeModel();
			schemeTreeModel = new SchemeTreeModel(aContext);
			schemeTreeModel.setForceSorting(true);
		}
		return instance;
	}

	public static PopulatableIconedNode createAllMapViewsRoot(ApplicationContext aContext) {
		PopulatableIconedNode root = new PopulatableIconedNode(
				MapViewTreeModel.getInstance(aContext),
				MapViewTreeModel.ALL_MAP_VIEWS_BRANCH,
				I18N.getString(MapViewTreeModel.ALL_MAP_VIEWS_BRANCH),
				mapViewIcon, 
				true);
		return root;
	}

	public static PopulatableIconedNode createSingleMapViewRoot(MapView mapView, ApplicationContext aContext) {
		PopulatableIconedNode root = new PopulatableIconedNode(
				MapViewTreeModel.getInstance(aContext),
				mapView,
				mapViewIcon, 
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
			if (s.equals(MapViewTreeModel.ALL_MAP_VIEWS_BRANCH)) {
				populateAllMapViewsNode((PopulatableIconedNode )node);
			} else if (s.equals(MapViewTreeModel.MAP_BRANCH)) {
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

	void populateAllMapViewsNode(PopulatableIconedNode node) {
		List<MapView> mapViewsChildren = new LinkedList();
		try {
			Identifier domainId = LoginManager.getDomainId();
			StorableObjectCondition condition = new LinkedIdsCondition(
					domainId,
					ObjectEntities.MAPVIEW_CODE);
			Set<MapView> mapViews = StorableObjectPool.getStorableObjectsByCondition(
					condition,
					true);
			mapViewsChildren = new ArrayList<MapView>(mapViews);
		} catch(ApplicationException e) {
			e.printStackTrace();
			return;
		}

		Collections.sort(mapViewsChildren, MapViewTreeModel.mapViewComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
			MapView mapView = (MapView )childNode.getObject();
			if(mapViewsChildren.contains(mapView)) {
				if(childNode.isPopulated())
					childNode.populate();
				nodePresense.put(mapView, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = mapViewsChildren.iterator(); it.hasNext();) {
			MapView mapView = (MapView )it.next();
			Item childNode = (Item )nodePresense.get(mapView);
			if(childNode == null) {
				PopulatableIconedNode newItem = new PopulatableIconedNode(
						this,
						mapView,
						mapViewIcon, 
						true);
				node.addChild(newItem);
			}
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
					I18N.getString(MapViewTreeModel.MAP_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(mapNode);

			schemesNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.SCHEMES_BRANCH,
					I18N.getString(MapViewTreeModel.SCHEMES_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(schemesNode);

			librariesNode = new PopulatableIconedNode(
					this,
					MapViewTreeModel.LIBRARIES_BRANCH,
					I18N.getString(MapViewTreeModel.LIBRARIES_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(librariesNode);
			
		}
//		else {
			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
//				if(childNode.isPopulated())
					childNode.populate();
			}
//		}
	}

	void populateMapNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapView mapView = (MapView )parentNode.getObject();
		PopulatableIconedNode childNode = null;
		if(node.getChildren().size() != 0) {
			childNode = (PopulatableIconedNode )node.getChildren().iterator().next();
			if(childNode.getObject().equals(mapView.getMapId())) {
				if(childNode.isPopulated())
					childNode.populate();
			}
			else {
				childNode.setParent(null);
				childNode = null;
			}
		}
		if(childNode == null) {
			MapTreeModel mapTreeModel = new MapTreeModel();
			childNode = mapTreeModel.createSingleMapRoot(mapView.getMap());
			node.addChild(childNode);
			childNode.populate();
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
				
				node.addChild(new PopulatableIconedNode(schemeTreeModel, scheme, UIManager.getIcon(SchemeResourceKeys.ICON_SCHEME)));
				
//				MapSchemeTreeNode newItem = new MapSchemeTreeNode(
//						MapSchemeTreeModel.getInstance(),
//						scheme,
//						MapSchemeTreeModel.schemeIcon, 
//						true);
//				newItem.setTopological(true);
////				newItem.populate();
//				node.addChild(newItem);
			}
		}
	}

	void populateLibrariesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapView mapView = (MapView )parentNode.getObject();

		List librariesChildren = new ArrayList();
		librariesChildren.addAll(mapView.getMap().getMapLibraries());
		Collections.sort(librariesChildren, MapLibraryTreeModel.libraryComparator);

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
				PopulatableIconedNode newItem = MapLibraryTreeModel.createSingleMapLibraryRoot(library);
//				newItem.populate();
				node.addChild(newItem);
			}
		}
	}
}

final class MapViewComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		MapView mapView1 = (MapView )o1;
		MapView mapView2 = (MapView )o2;
		return mapView1.getName().compareTo(mapView2.getName());
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MapViewComparator);
	}
}
