/*
 * ColorCellRenderer.java Created on 20.08.2004 15:39:41
 *  
 */

package com.syrus.AMFICOM.client.general.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/24 06:54:50 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public class ColorCellRenderer extends JComponent implements TableCellRenderer {

	private static ColorCellRenderer	instance;

	private Color				curColor;

	private Color				unselectedForeground;

	private ColorCellRenderer() {
		// empty
	}

	public static ColorCellRenderer getInstance() {
		if (instance == null)
			instance = new ColorCellRenderer();
		return instance;
	}

	public Component getTableCellRendererComponent(	JTable table,
							Object value,
							boolean isSelected,
							boolean hasFocus,
							int rowIndex,
							int vColIndex) {
		// Set the color to paint
		if (this.curColor != null) {
			//System.out.println(value.getClass().getName());
			this.curColor = (Color) value;
		} else {
			// If color unknown, use table's background
			this.curColor = table.getBackground();
		}

		Color color = super.getBackground();
		color = (color == null) ? table.getBackground() : color;

		if (isSelected) {
			super.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table
					.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			setFont(font);
			Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.unselectedForeground = new Color(
								(int) (c.getRed() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
										* color.getRed()) % 256,
								(int) (c.getGreen() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
										* color.getGreen()) % 256,
								(int) (c.getBlue() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
										* color.getBlue()) % 256);
		} else {
			this.unselectedForeground = table.getBackground();
			setFont(table.getFont());
		}

		if (hasFocus) {
			setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); //$NON-NLS-1$
			if (table.isCellEditable(rowIndex, vColIndex)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground")); //$NON-NLS-1$
				super.setBackground(UIManager.getColor("Table.focusCellBackground")); //$NON-NLS-1$
			}
		} else {
			//setBorder(noFocusBorder);
		}

		return this;
	}

	// Paint current color
	protected void paintComponent(Graphics g) {
		int band = 2;
		int w = getWidth();
		int h = getHeight();
		g.setColor(this.unselectedForeground);
		g.fillRect(0, 0, w - 1, h - 1);
		g.setColor(this.curColor);
		g.fill3DRect(band, band, w - 1 - 2 * band, h - 1 - 2 * band, true);
	}

}