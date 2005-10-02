/*-
 * $Id: NameableListCellRenderer.java,v 1.1 2005/10/02 06:19:21 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.syrus.AMFICOM.general.Namable;

/**
 * Namable list cell renderer
 * 
 * @version $Revision: 1.1 $, $Date: 2005/10/02 06:19:21 $
 * @author $Author: bob $
 * @author Kholshin Stanislav
 * @module commonclient
 */

public final class NameableListCellRenderer implements ListCellRenderer {
	private JLabel label;
	
	public NameableListCellRenderer() {
		this.label = new JLabel();
		this.label.setOpaque(true);
	}

	public final Component getListCellRendererComponent(final JList list,
			final Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {

		final Namable namable = (Namable) value;
		
		if (isSelected) {
			this.label.setBackground(list.getSelectionBackground());
			this.label.setForeground(list.getSelectionForeground());
		} else {
			this.label.setBackground(list.getBackground());
			this.label.setForeground(list.getForeground());
		}
		this.label.setText(namable.getName());
		return this.label;
	}
}