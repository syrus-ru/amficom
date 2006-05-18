/*-
 * $$Id: MapSchemeTreeNode.java,v 1.4 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import javax.swing.Icon;

import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.logic.ChildrenFactory;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MapSchemeTreeNode extends PopulatableIconedNode {
	boolean topological = false;
	boolean dragDropEnabled = false;

	public MapSchemeTreeNode(
			ChildrenFactory factory,
			Namable object,
			boolean allowsChildren) {
		super(factory, object, allowsChildren);
	}

	public MapSchemeTreeNode(
			ChildrenFactory factory,
			Namable object,
			Icon icon,
			boolean allowsChildren) {
		super(factory, object, icon, allowsChildren);
	}

	public MapSchemeTreeNode(ChildrenFactory factory, Namable object, Icon icon) {
		super(factory, object, icon);
	}

	public MapSchemeTreeNode(ChildrenFactory factory, Namable object) {
		super(factory, object);
	}

	public MapSchemeTreeNode(ChildrenFactory factory, Object object, String name) {
		super(factory, object, name);
	}

	public MapSchemeTreeNode(
			ChildrenFactory factory,
			Object object,
			String name,
			boolean allowsChildren) {
		super(factory, object, name, allowsChildren);
	}

	public MapSchemeTreeNode(
			ChildrenFactory factory,
			Object object,
			String name,
			Icon icon,
			boolean allowsChildren) {
		super(factory, object, name, icon, allowsChildren);
	}

	public MapSchemeTreeNode(
			ChildrenFactory factory,
			Object object,
			String name,
			Icon icon) {
		super(factory, object, name, icon);
	}

	public void setTopological(boolean t) {
		this.topological = t;
	}

	public boolean isTopological() {
		return this.topological;
	}

	public boolean isDragDropEnabled() {
		return this.dragDropEnabled;
	}

	public void setDragDropEnabled(boolean dragDropEnabled) {
		this.dragDropEnabled = dragDropEnabled;
	}

}
