/*
 * $Id: TextIcon.java,v 1.3 2004/09/23 14:53:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/09/23 14:53:17 $
 * @module generalclient_v1
 */
public class TextIcon implements Icon
{
	private String text;

	private Component parent;

	private int height;

	private int width;

	private boolean visible;

	private boolean valid;

	public TextIcon(final String text, final Component parent)
	{
		this(text, parent, true);
	}

	public TextIcon(final String text, final Component parent, final boolean visible)
	{
		if (text == null)
		{
			this.text = "null";
			this.valid = false;
		}
		else
		{
			this.text = text;
			this.valid = true;
		}
		this.parent = parent;
		this.visible = visible;
		FontMetrics fm = this.parent.getFontMetrics(this.parent.getFont());
		height = fm.stringWidth(this.text);
		width = fm.getHeight();
	}

	public int getIconHeight()
	{
		return height + 20;
	}

	public int getIconWidth()
	{
		return width - 10;
	}

	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform t = g2.getTransform();
		g2.translate(0, height + 10);
		g2.rotate (Math.toRadians(270), x - 5, y);
		if (!valid)
			g2.setColor(Color.RED);
		else if (visible)
			g2.setColor(Color.BLACK);
		else
			g2.setColor(Color.GRAY);
		g2.drawString(text, x - 5, y + g2.getFontMetrics(g2.getFont()).getAscent());
		g2.setTransform(t);
	}
}
