/*
 * $Id: SONode.java,v 1.1 2005/03/14 13:30:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/14 13:30:48 $
 * @module generalclient_v1
 */

public abstract class SONode extends DefaultMutableTreeNode {
	protected SOTreeDataModel treeDataModel;
	private boolean expanded = false;
	
	public SONode(SOTreeDataModel treeDataModel, Object userObject) {
		this(treeDataModel, userObject, true);
	}
	
	public SONode(SOTreeDataModel treeDataModel, Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		this.treeDataModel = treeDataModel;
	}
	
	public SOTreeDataModel getTreeDataModel() {
		return this.treeDataModel;
	}
	
	public abstract String getName();
	public abstract Icon getIcon();
	public abstract Color getColor();
	public abstract ObjectResourceController getNodeController();
	
	public boolean isExpanded() {
		return this.expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
