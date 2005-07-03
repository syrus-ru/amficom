/*
 * $Id: JAlertingMessageTableHeaderRenderer.java,v 1.1.1.1 2004/05/27 11:24:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Object.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 */
final class JAlertingMessageTableHeaderRenderer
		extends DefaultTableCellRenderer {
	private static final Border BORDER
		= BorderFactory.createBevelBorder(BevelBorder.RAISED);

	private static final Color DARK_RED = new Color(128, 0, 0);

	private static final Font FONT = new Font("SansSerif", Font.BOLD, 12);

	public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		String text = value.toString();
		setBackground(DARK_RED);
		setBorder(BORDER);
		setFont(FONT.deriveFont(table.getFont().getSize2D()));
		setForeground(Color.WHITE);
		setText(text);
		setToolTipText(text);
		return this;
	}
}
