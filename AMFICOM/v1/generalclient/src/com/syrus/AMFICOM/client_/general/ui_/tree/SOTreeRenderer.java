/*
 * $Id: SOTreeRenderer.java,v 1.2 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOTreeRenderer extends DefaultTreeCellRenderer {
	private static final long serialVersionUID = 3689072858814887473L;
	private static SOTreeRenderer instance;
	
	private SOTreeRenderer() {
		// empty
	}
	
	public static DefaultTreeCellRenderer getInstance() {
		if (instance == null) {
			instance = new SOTreeRenderer();
		}
		return instance;
	}
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		SONode node = (SONode )value;
		TreeCellRenderer renderer = node.getRenderer();
		return renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	}
}
