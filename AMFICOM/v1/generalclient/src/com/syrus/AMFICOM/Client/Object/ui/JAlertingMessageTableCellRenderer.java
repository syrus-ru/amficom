/*
 * $Id: JAlertingMessageTableCellRenderer.java,v 1.1.1.1 2004/05/27 11:24:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.Object.ui;

import java.awt.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:24:21 $
 * @author $Author: bass $
 *
 * @bug In Motif L&F, both icon and selection colors are black.
 * @bug In native GTK L&F, we're unable to pull JOptionPane icons from UIManager
 *      resources.
 */
final class JAlertingMessageTableCellRenderer extends DefaultTableCellRenderer {
	private static final Color EVEN_BACKGROUND = new Color(204, 204, 204);

	private static final Font FONT = new Font("SansSerif", Font.PLAIN, 12);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");

    public Component getTableCellRendererComponent(JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) {
		String text;
		if (value instanceof Date)
			text = DATE_FORMAT.format((Date) value);
		else
			text = value.toString();
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(Color.BLACK);
			setBackground(((row % 2) == 1) ? EVEN_BACKGROUND : Color.WHITE);
		}
		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
		} else {
			setBorder(noFocusBorder);
		}
		setFont(FONT.deriveFont(table.getFont().getSize2D()));
		/**
		 * @todo Put into table not just the identifier of MessageType, but
		 *       MessageType itself. Then, we'll be able to re-enable table
		 *       column reordering and add icons based on message type.
		 */
		if (column == 1) {
			setHorizontalAlignment(CENTER);
			setIcon(UIManager.getIcon("OptionPane.informationIcon"));
			setText(null);
		} else {
			setHorizontalAlignment(LEADING);
			setIcon(null);
			setText(text);
		}
		setToolTipText((text.length() == 0) ? null : text);
		return this;
    }
}
