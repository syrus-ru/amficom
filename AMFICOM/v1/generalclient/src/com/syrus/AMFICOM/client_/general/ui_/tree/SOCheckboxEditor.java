/*
 * $Id: SOCheckboxEditor.java,v 1.1 2005/03/17 14:44:00 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/03/17 14:44:00 $
 * @module generalclient_v1
 */

public class SOCheckboxEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 3689072858814887473L;
	private static SOCheckboxEditor instance;
	private SONode node;
	private JCheckBox box;

	private SOCheckboxEditor() {
		super(new JCheckBox() {
			private static final long serialVersionUID = 3978985479217428273L;
			protected void paintComponent(Graphics g) {
				int x = 0;
				Icon icon = ((BasicRadioButtonUI)getUI()).getDefaultIcon();
				if (icon != null) {
					x += icon.getIconWidth() + getIconTextGap(); 
				}
				g.setColor(SOCheckboxRenderer.selectedBackground);
				Insets i = getInsets();
				g.fillRect(i.left + x, 0 , getWidth() - i.right - i.left - x, getHeight());
				super.paintComponent(g);
			}
			public Dimension getPreferredSize() {
				Dimension ps = super.getPreferredSize();
				return new Dimension(ps.width, SOCheckboxRenderer.preferredHeight);
			}
//			public void setSelected(boolean b){
//				super.setSelected(b);
//			}
//			public boolean isSelected(){
//				return super.isSelected();
//			}
		});
		box = (JCheckBox)getComponent();
		box.setOpaque(false);
		box.setFocusable(false);
		box.setForeground(SOCheckboxRenderer.selectedForeground);
	}

	public static SOCheckboxEditor getInstance() {
		if (instance == null) {
			instance = new SOCheckboxEditor();
		}
		return instance;
	}
	
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		node = (SONode)value;
//		if (node instanceof SOCheckableNode) {
//			boolean b = ((SOCheckableNode)node).isChecked();
//			box.setSelected(true);
//		}
		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}
	
	public Object getCellEditorValue() {
		if (node instanceof SOCheckableNode) {
			((SOCheckableNode)node).setChecked(box.isSelected());
		}
		node = null;
		return super.getCellEditorValue();
	}
}
