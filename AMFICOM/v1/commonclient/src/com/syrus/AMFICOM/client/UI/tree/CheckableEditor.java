/*-
 * $Id: CheckableEditor.java,v 1.3 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI.tree;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicRadioButtonUI;

import com.syrus.AMFICOM.logic.Item;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.3 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */
public class CheckableEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 3689072858814887473L;
	private static CheckableEditor instance;
	private Item node;
	private JCheckBox box;

	private CheckableEditor() {
		super(new JCheckBox() {
			private static final long serialVersionUID = 3978985479217428273L;

			@Override
			protected void paintComponent(final Graphics g) {
				int x = 0;
				final Icon icon = ((BasicRadioButtonUI) getUI()).getDefaultIcon();
				if (icon != null) {
					x += icon.getIconWidth() + getIconTextGap();
				}
				g.setColor(CheckableRenderer.selectedBackground);
				final Insets i = this.getInsets();
				g.fillRect(i.left + x, 0, getWidth() - i.right - i.left - x, getHeight());
				super.paintComponent(g);
			}

			@Override
			public Dimension getPreferredSize() {
				final Dimension ps = super.getPreferredSize();
				return new Dimension(ps.width, CheckableRenderer.preferredHeight);
			}
		});
		this.box = (JCheckBox) getComponent();
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

	@Override
	public Component getTreeCellEditorComponent(final JTree tree,
			final Object value,
			final boolean isSelected,
			final boolean expanded,
			final boolean leaf,
			final int row) {
		this.node = (Item) value;
		this.box.setText(this.node.getName());
		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}

	@Override
	public Object getCellEditorValue() {
		this.node = null;
		return super.getCellEditorValue();
	}

	@Override
	public void fireEditingStopped() {
		if (this.node instanceof CheckableNode) {
			((CheckableNode) this.node).setChecked(this.box.isSelected());
		}
		super.fireEditingStopped();
	}
}
