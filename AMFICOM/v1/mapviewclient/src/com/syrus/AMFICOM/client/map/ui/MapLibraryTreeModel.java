/*-
 * $$Id: MapLibraryTreeModel.java,v 1.11 2005/10/11 08:56:12 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.map.controllers.NodeTypeController;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.PhysicalLinkType;
import com.syrus.AMFICOM.map.SiteNodeType;

/**
 * @version $Revision: 1.11 $, $Date: 2005/10/11 08:56:12 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapLibraryTreeModel implements ChildrenFactory {

	public static MapLibraryComparator libraryComparator = new MapLibraryComparator();

	private static MapLibraryTreeModel instance = null;

	private static final String SITENODETYPE_BRANCH = MapEditorResourceKeys.ENTITY_SITE_NODE_TYPE;
	private static final String PHYSICALLINKTYPE_BRANCH = MapEditorResourceKeys.ENTITY_PHYSICAL_LINK_TYPE;
	static final int IMG_SIZE = 16;

	public static NodeTypeComparator nodeTypeComparator = new NodeTypeComparator();
	public static LinkTypeComparator linkTypeComparator = new LinkTypeComparator();

	static final Icon linkTypeIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/linkmode.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	static ImageIcon libraryIcon = new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/maplibrary.gif").getScaledInstance( //$NON-NLS-1$
					IMG_SIZE,
					IMG_SIZE,
					Image.SCALE_SMOOTH));

	protected MapLibraryTreeModel() {
		// empty
	}
	
	public static ChildrenFactory getInstance() {
		if(instance == null) {
			instance = new MapLibraryTreeModel();
		}
		return instance;
	}

	public static PopulatableIconedNode createSingleMapLibraryRoot(MapLibrary mapLibrary) {
		PopulatableIconedNode root = new PopulatableIconedNode(
				MapLibraryTreeModel.getInstance(),
				mapLibrary,
				libraryIcon, 
				true);
		return root;
	}

	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(MapLibraryTreeModel.SITENODETYPE_BRANCH)) {
				populateSiteNodeTypesNode((PopulatableIconedNode )node);
			} else if (s.equals(MapLibraryTreeModel.PHYSICALLINKTYPE_BRANCH)) {
				populatePhysicalLinkTypesNode((PopulatableIconedNode )node);
			}
		}
		else if(node.getObject() instanceof MapLibrary) {
			populateMapLibraryNode((PopulatableIconedNode )node);
		}
	}

	void populateMapLibraryNode(PopulatableIconedNode node) {
		PopulatableIconedNode siteNodeTypesNode;
		PopulatableIconedNode physicalLinkTypesNode;

		if(node.getChildren().size() == 0) {
			siteNodeTypesNode = new PopulatableIconedNode(
					this,
					MapLibraryTreeModel.SITENODETYPE_BRANCH,
					I18N.getString(MapLibraryTreeModel.SITENODETYPE_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(siteNodeTypesNode);

			physicalLinkTypesNode = new PopulatableIconedNode(
					this,
					MapLibraryTreeModel.PHYSICALLINKTYPE_BRANCH,
					I18N.getString(MapLibraryTreeModel.PHYSICALLINKTYPE_BRANCH),
					UIManager.getIcon(MapEditorResourceKeys.ICON_CATALOG),
					true);
			node.addChild(physicalLinkTypesNode);
		}
		else {
			for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
				PopulatableIconedNode childNode = (PopulatableIconedNode )iter.next();
				childNode.populate();
			}
		}
	}

	void populateSiteNodeTypesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapLibrary library = (MapLibrary )parentNode.getObject();

		List siteNodeTypeChildren = new ArrayList();
		for(SiteNodeType siteNodeType : library.getSiteNodeTypes()) {
			if(siteNodeType.isTopological()) {
				siteNodeTypeChildren.add(siteNodeType);
			}
		}
		Collections.sort(siteNodeTypeChildren, MapLibraryTreeModel.nodeTypeComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			IconedNode childNode = (IconedNode )iter.next();
			SiteNodeType type = (SiteNodeType )childNode.getObject();
			if(siteNodeTypeChildren.contains(type)) {
				nodePresense.put(type, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = siteNodeTypeChildren.iterator(); it.hasNext();) {
			SiteNodeType type = (SiteNodeType )it.next();
			IconedNode childNode = (IconedNode )nodePresense.get(type);
			if(childNode == null) {
				Item newItem = new IconedNode(
						type,
						new ImageIcon(NodeTypeController.getImage(type)
								.getScaledInstance(
										IMG_SIZE,
										IMG_SIZE,
										Image.SCALE_SMOOTH)),
						false); 
				node.addChild(newItem);
			}
			else {
				childNode.setIcon(
						new ImageIcon(NodeTypeController.getImage(type)
								.getScaledInstance(
										IMG_SIZE,
										IMG_SIZE,
										Image.SCALE_SMOOTH)));
			}
		}
	}

	void populatePhysicalLinkTypesNode(PopulatableIconedNode node) {
		Item parentNode = node.getParent();
		MapLibrary library = (MapLibrary )parentNode.getObject();

		List physicalLinkTypeChildren = new ArrayList();
		for(PhysicalLinkType physicalLinkType : library.getPhysicalLinkTypes()) {
			if(physicalLinkType.isTopological()) {
				physicalLinkTypeChildren.add(physicalLinkType);
			}
		}
		Collections.sort(physicalLinkTypeChildren, MapLibraryTreeModel.linkTypeComparator);

		java.util.Map nodePresense = new HashMap();

		List toRemove = new LinkedList();

		for(Iterator iter = node.getChildren().iterator(); iter.hasNext();) {
			IconedNode childNode = (IconedNode )iter.next();
			PhysicalLinkType type = (PhysicalLinkType )childNode.getObject();
			if(physicalLinkTypeChildren.contains(type)) {
				nodePresense.put(type, childNode);
			}
			else
				toRemove.add(childNode);
		}
		for(Iterator it = toRemove.iterator(); it.hasNext();) {
			Item childItem = (Item )it.next();
			childItem.setParent(null);
		}

		for(Iterator it = physicalLinkTypeChildren.iterator(); it.hasNext();) {
			PhysicalLinkType type = (PhysicalLinkType )it.next();
			Item childNode = (Item )nodePresense.get(type);
			if(childNode == null) {
				Item newItem = new IconedNode(
						type,
						linkTypeIcon,
						false); 
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

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MapLibraryComparator);
	}
}

final class NodeTypeComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		SiteNodeType type1 = (SiteNodeType )o1;
		SiteNodeType type2 = (SiteNodeType )o2;
		return type1.getName().compareTo(type2.getName());
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof NodeTypeComparator);
	}
}

final class LinkTypeComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		PhysicalLinkType type1 = (PhysicalLinkType )o1;
		PhysicalLinkType type2 = (PhysicalLinkType )o2;
		return type1.getName().compareTo(type2.getName());
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof LinkTypeComparator);
	}
}
