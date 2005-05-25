/*-
 * $Id: CheckableEditor.java,v 1.1 2005/05/25 07:55:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/05/25 07:55:08 $
 * @module generalclient_v1
 */

public class CheckableEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 3689072858814887473L;
	private static CheckableEditor instance;
	private Item node;
	private JCheckBox box;

	private CheckableEditor() {
		super(new JCheckBox() {
			private static final long serialVersionUID = 3978985479217428273L;
			protected void paintComponent(Graphics g) {
				int x = 0;
				Icon icon = ((BasicRadioButtonUI)getUI()).getDefaultIcon();
				if (icon != null) {
					x += icon.getIconWidth() + getIconTextGap(); 
				}
				g.setColor(CheckableRenderer.selectedBackground);
				Insets i = getInsets();
				g.fillRect(i.left + x, 0 , getWidth() - i.right - i.left - x, getHeight());
				super.paintComponent(g);
			}
			public Dimension getPreferredSize() {
				Dimension ps = super.getPreferredSize();
				return new Dimension(ps.width, CheckableRenderer.preferredHeight);
			}
		});
		this.box = (JCheckBox)getComponent();
		this.box.setOpaque(false);
		this.box.setFocusable(false);
		this.box.setForeground(CheckableRenderer.selectedForeground);
	}

	public static CheckableEditor getInstance() {
		if (instance == null) {
			instance = new CheckableEditor();
			instance.setClickCountToStart(2);
		}
		return instance;
	}
	
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		this.node = (Item)value;
		this.box.setText(this.node.getName());
		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}
	
	public Object getCellEditorValue() {
		this.node = null;
		return super.getCellEditorValue();
	}
	
	public void fireEditingStopped() {
		if (this.node instanceof CheckableNode) {
			((CheckableNode)this.node).setChecked(this.box.isSelected());
		}
		super.fireEditingStopped();
	}
}
