/*
 * $Id: SONode.java,v 1.2 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;
import java.util.*;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.*;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public abstract class SONode extends DefaultMutableTreeNode {
	protected SOTreeDataModel treeDataModel;
	private boolean expanded = false;
	private boolean editable = false;
	
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
	
	public String getName() {
		return this.treeDataModel.getNodeName(this);
	}
	
	public Icon getIcon() {
		return this.treeDataModel.getNodeIcon(this);
	}
	
	public Color getColor() {
		return this.treeDataModel.getNodeColor(this);
	}
	
	public ObjectResourceController getNodeController() {
		return this.treeDataModel.getNodeController(this);
	}
	
	public void updateChildNodes() {
		this.treeDataModel.updateChildNodes(this);
	}
	
	public abstract TreeCellRenderer getRenderer();
	public abstract TreeCellEditor getEditor();
	
	public boolean isExpanded() {
		return this.expanded;
	}
	
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public List getChildrenUserObjects() {
		List contents = new ArrayList(getChildCount());
		for (Enumeration en = children(); en.hasMoreElements();)
			contents.add(((SONode)en.nextElement()).getUserObject());
		return contents;
	}
}
