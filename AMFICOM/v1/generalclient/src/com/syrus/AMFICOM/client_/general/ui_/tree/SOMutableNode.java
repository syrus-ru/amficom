/*
 * $Id: SOMutableNode.java,v 1.1 2005/03/14 13:30:48 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Color;

import javax.swing.Icon;

import com.syrus.AMFICOM.client_.resource.ObjectResourceController;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/14 13:30:48 $
 * @module generalclient_v1
 */

public class SOMutableNode extends SONode {
	public SOMutableNode(SOTreeDataModel treeDataModel, Object userObject) {
		this(treeDataModel, userObject, true);
	}
	
	public SOMutableNode(SOTreeDataModel treeDataModel, Object userObject, boolean allowsChildren) {
		super(treeDataModel, userObject, allowsChildren);
	}
	
	public String getName() {
		return super.treeDataModel.getNodeName(this);
	}
	
	public Icon getIcon() {
		return super.treeDataModel.getNodeIcon(this);
	}
	
	public Color getColor() {
		return super.treeDataModel.getNodeColor(this);
	}
	
	public ObjectResourceController getNodeController() {
		return super.treeDataModel.getNodeController(this);
	}
}
