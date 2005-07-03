/*-
 * $Id: CheckableEditor.java,v 1.3 2005/05/18 14:01:21 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.general.ui_.tree_;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 14:01:21 $
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
		box = (JCheckBox)getComponent();
		box.setOpaque(false);
		box.setFocusable(false);
		box.setForeground(CheckableRenderer.selectedForeground);
	}

	public static CheckableEditor getInstance() {
		if (instance == null) {
			instance = new CheckableEditor();
			instance.setClickCountToStart(2);
		}
		return instance;
	}
	
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
		node = (Item)value;
		box.setText(node.getName());
		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}
	
	public Object getCellEditorValue() {
		node = null;
		return super.getCellEditorValue();
	}
	
	public void fireEditingStopped() {
		if (node instanceof CheckableNode) {
			((CheckableNode)node).setChecked(box.isSelected());
		}
		super.fireEditingStopped();
	}
}
