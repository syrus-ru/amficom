/*
 * $Id: SOCheckableNode.java,v 1.2 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.tree.*;
import javax.swing.tree.TreeCellRenderer;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOCheckableNode extends SOMutableNode {
	private static final long serialVersionUID = 3690756185380697907L;
	private boolean isChecked;
	private static SOCheckboxRenderer renderer = SOCheckboxRenderer.getInstance();
	private static SOCheckboxEditor editor = SOCheckboxEditor.getInstance();

	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj) {
		this(treeDataModel, obj, true);
	}

	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj, boolean allowsChildren) {
		this(treeDataModel, obj, allowsChildren, false);
	}
	
	public SOCheckableNode(SOTreeDataModel treeDataModel, Object obj, boolean allowsChildren, boolean isChecked) {
		super(treeDataModel, obj, allowsChildren);
		setChecked(isChecked);
		setEditable(true);
	}
	
	public boolean isChecked() {
		return isChecked;
	}
	
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
		for (Enumeration enumeration = children(); enumeration.hasMoreElements();) {
			SONode node = (SONode)enumeration.nextElement();
			if (node instanceof SOCheckableNode)
				((SOCheckableNode)node).setChecked(isChecked);
		}
	}
	
	public TreeCellRenderer getRenderer() {
		renderer.setSelected(this.isChecked);
		return renderer;
	}
	
	public TreeCellEditor getEditor() {
		JCheckBox box = (JCheckBox)editor.getComponent();
		box.setText(getName());
		return editor;
	}
}
