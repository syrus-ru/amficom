/*
 * $Id: SOMutableNode.java,v 1.2 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import javax.swing.tree.*;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOMutableNode extends SONode {
	private static final long serialVersionUID = 3689069555917796403L;
	private static TreeCellRenderer renderer = SOTextRenderer.getInstance();
	private static TreeCellEditor editor = SOTextEditor.getInstance();
	
	public SOMutableNode(SOTreeDataModel treeDataModel, Object userObject) {
		this(treeDataModel, userObject, true);
	}
	
	public SOMutableNode(SOTreeDataModel treeDataModel, Object userObject, boolean allowsChildren) {
		super(treeDataModel, userObject, allowsChildren);
	}

	public TreeCellRenderer getRenderer() {
		return renderer;
	}

	public TreeCellEditor getEditor() {
		return editor;
	}
}
