/*
 * $Id: ColorListCellRenderer.java,v 1.4 2005/09/09 18:54:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.UI;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Renderer for java.awt.Color cell at JList.
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/09 18:54:27 $
 * @module commonclient
 */

public class ColorListCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = -7358452357896522533L;

	private static ColorListCellRenderer instance;
	private Color curColor;
	private Color unselectedForeground;

	private ColorListCellRenderer() {
		// empty
	}

	/**
	 * There is no need in more than one instance of this renderer.
	 * 
	 * @return ColorCellRenderer instance.
	 */
	public static ColorListCellRenderer getInstance() {
		if (instance == null) {
			instance = new ColorListCellRenderer();
			instance.setText(" ");
		}
		return instance;
	}

	public Component getListCellRendererComponent(final JList list,
			final Object value,
			final int index,
			final boolean isSelected,
			final boolean hasFocus) {

		if (value instanceof JComponent) {
			return (JComponent) value;
		}

		// Set the color to paint
		if (this.curColor != null) {
			// System.out.println(value.getClass().getName());
			this.curColor = (Color) value;
		} else {
			// If color unknown, use table's background
			this.curColor = list.getBackground();
		}

		Color color = super.getBackground();
		color = (color == null) ? list.getBackground() : color;

		if (isSelected) {
			super.setForeground((this.unselectedForeground != null) ? this.unselectedForeground : list.getForeground());
			Color c = list.getSelectionBackground();
			// calculate color with alpha-channel weight alpha
			this.unselectedForeground = new Color((int) (c.getRed() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA
					* color.getRed()) % 256,
					(int) (c.getGreen() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA * color.getGreen()) % 256,
					(int) (c.getBlue() * AbstractLabelCellRenderer.ONE_MINUS_ALPHA + AbstractLabelCellRenderer.ALPHA * color.getBlue()) % 256);
		} else {
			this.unselectedForeground = list.getBackground();
		}
		return this;
	}

	/**
	 * Paint current color
	 */ 
	@Override
	protected void paintComponent(final Graphics g) {
		final int w = getWidth();
		final int h = getHeight();
		g.setColor(this.curColor);
		g.fillRect(0, 0, w, h);
	}

}
