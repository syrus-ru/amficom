/*
 * ColorCellRenderer.java Created on 20.08.2004 15:39:41
 *  
 */

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

/**
 * Renderer for java.awt.Color cell at JTable.
 * see {@link java.awt.Color}
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:54:27 $
 * @author $Author: arseniy $
 * @module commonclient
 */
public class ColorCellRenderer extends JComponent implements TableCellRenderer {
	private static final long serialVersionUID = -8692636402285639594L;

	private static ColorCellRenderer instance;

	private Color curColor;

	private Color unselectedForeground;

	private ColorCellRenderer() {
		// empty
	}

	/**
	 * There is no need in more than one instance of this renderer.
	 * @return ColorCellRenderer instance. 
	 */
	public static ColorCellRenderer getInstance() {
		if (instance == null) {
			instance = new ColorCellRenderer();
		}
		return instance;
	}

	public Component getTableCellRendererComponent(final JTable table,
			final Object value,
			final boolean isSelected,
			final boolean hasFocus,
			final int rowIndex,
			final int vColIndex) {
		// // Set the color to paint
		// if (this.curColor != null) {
		// //System.out.println(value.getClass().getName());
		this.curColor = (Color) value;
		// } else {
		// // If color unknown, use table's background
		// this.curColor = table.getBackground();
		// }

		Color color = super.getBackground();
		color = (color == null) ? table.getBackground() : color;

		if (isSelected) {
			super.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : table.getForeground());
			Font font = table.getFont();
			font = new Font(font.getName(), Font.BOLD | Font.ITALIC, font.getSize());
			super.setFont(font);
			final Color c = table.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.unselectedForeground = new Color((int) (c.getRed() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
					* color.getRed()) % 256,
					(int) (c.getGreen() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA * color.getGreen()) % 256,
					(int) (c.getBlue() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA * color.getBlue()) % 256);
		} else {
			this.unselectedForeground = table.getBackground();
			super.setFont(table.getFont());
		}

		if (hasFocus) {
			super.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); //$NON-NLS-1$
			if (table.isCellEditable(rowIndex, vColIndex)) {
				super.setForeground(UIManager.getColor("Table.focusCellForeground")); //$NON-NLS-1$
				super.setBackground(UIManager.getColor("Table.focusCellBackground")); //$NON-NLS-1$
			}
		} else {
			// super.setBorder(noFocusBorder);
		}

		return this;
	}

	/**
	 * Paint current color
	 */ 
	@Override
	protected void paintComponent(final Graphics g) {
		final int band = 2;
		final int w = getWidth();
		final int h = getHeight();
		g.setColor(this.unselectedForeground);
		g.fillRect(0, 0, w - 1, h - 1);
		g.setColor(this.curColor);
		g.fill3DRect(band, band, w - 1 - 2 * band, h - 1 - 2 * band, true);
	}

}
