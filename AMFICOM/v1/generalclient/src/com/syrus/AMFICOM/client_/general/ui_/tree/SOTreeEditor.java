/*
 * $Id: SOTreeEditor.java,v 1.1 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOTreeEditor extends DefaultTreeCellEditor {
	private static DefaultTreeCellEditor instance;
	private TreeCellEditor editor;
	private SOTreeEditor(JTree tree, DefaultTreeCellRenderer renderer) {
		super(tree, renderer, null);
	}
	
	public static DefaultTreeCellEditor getInstance(JTree tree, DefaultTreeCellRenderer renderer) {
		if (instance == null)
			instance = new SOTreeEditor(tree, renderer);
		return instance;
	}
	
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		editor = ((SONode)value).getEditor();
		return editor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}
	
  public Object getCellEditorValue() {
  	if (editor != null) {
  		Object obj = editor.getCellEditorValue();
  		editor = null;
  		return obj;
  	}
    return super.getCellEditorValue();
  }
 
  public boolean isCellEditable(EventObject event) {
  	if (event != null) {
			if (event.getSource() instanceof JTree) {
				setTree((JTree) event.getSource());
				if (event instanceof MouseEvent) {
					MouseEvent me = (MouseEvent)event;
					TreePath path = tree.getPathForLocation(me.getX(), me.getY());
					if (path != null) {
						SONode node = (SONode)path.getLastPathComponent();
						if (node != null)
							return (node.isEditable() && canEditImmediately(node, me));
					}
				}
			}
		}
		return false;
	}
  
	protected boolean canEditImmediately(SONode node, MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e) &&
					node.isLeaf() && e.getClickCount() > 1) {
				return true;
		}
		return super.canEditImmediately(e);
	}
}
