/*
 * $Id: MultiRowToolTipUI.java,v 1.6 2004/09/15 13:17:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.*;
import javax.swing.text.View;

/**
 * @version $Revision: 1.6 $, $Date: 2004/09/15 13:17:56 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class MultiRowToolTipUI extends BasicToolTipUI
{
	static MultiRowToolTipUI sharedInstance = new MultiRowToolTipUI();

	public static ComponentUI createUI(JComponent c)
	{
		return sharedInstance;
	}

	public void paint(Graphics g, JComponent c)
	{
		Font font = c.getFont();
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
		Dimension size = c.getSize();
		g.setColor(c.getBackground());
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(c.getForeground());
		g.setFont(font);
		String tipText = ((JToolTip) c).getTipText();
		if (tipText == null)
			tipText = "";
		View v = (View) c.getClientProperty(BasicHTML.propertyKey);
		if (v != null)
		{
			Rectangle paintTextR = c.getBounds();
			Insets insets = c.getInsets();
			paintTextR.x += insets.left;
			paintTextR.y += insets.top;
			paintTextR.width -= insets.left+insets.right;
			paintTextR.height -= insets.top+insets.bottom;
			v.paint(g, paintTextR);
		}
		else
		{
			MultiRowString mrs = new MultiRowString(tipText);
			for (int i = 0; i < mrs.getRowCount(); i++)
				g.drawString(mrs.get(i), 3, 2 + metrics.getAscent() + metrics.getHeight() * i);
		}
	}

	public Dimension getPreferredSize(JComponent c)
	{
		FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(c.getFont());
		Insets insets = c.getInsets();
		Dimension prefSize = new Dimension(insets.left + insets.right, insets.top + insets.bottom);
		String text = ((JToolTip) c).getTipText();
		if (text != null)
		{
			View v = (c != null) ? (View) c.getClientProperty("html") : null;
			if (v != null)
			{
				prefSize.width += (int) v.getPreferredSpan(View.X_AXIS);
				prefSize.height += (int) v.getPreferredSpan(View.Y_AXIS);
			}
			else
			{
				MultiRowString mrs = new MultiRowString(text);
				int maxsize = SwingUtilities.computeStringWidth(fm, mrs.get(0));
				for (int i = 1; i < mrs.getRowCount(); i++)
					maxsize = Math.max(maxsize, SwingUtilities.computeStringWidth(fm, mrs.get(i)));
				prefSize.width += maxsize + 6;
				prefSize.height += fm.getHeight() * mrs.getRowCount() + 4;
			}
		}
		return prefSize;
	}
}
