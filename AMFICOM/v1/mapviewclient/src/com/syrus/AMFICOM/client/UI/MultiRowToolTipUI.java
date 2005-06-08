/*
 * $Id: MultiRowToolTipUI.java,v 1.1 2005/06/08 13:44:06 krupenn Exp $ 
 * Syrus Systems. 
 * Научно-технический центр. 
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.client.UI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.text.View;

/**
 * Позволяет организовывать всплывающие подсказки в несколько строк
 * 
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module commonclient_v1
 */
public class MultiRowToolTipUI extends BasicToolTipUI {
	static MultiRowToolTipUI sharedInstance = new MultiRowToolTipUI();

	public static ComponentUI createUI(JComponent c) {
		return sharedInstance;
	}

	public void paint(Graphics g, JComponent c) {
		Font font = c.getFont();
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
		Dimension size = c.getSize();
		g.setColor(c.getBackground());
		g.fillRect(0, 0, size.width, size.height);
		g.setColor(c.getForeground());
		g.setFont(font);
		String tipText = ((JToolTip )c).getTipText();
		if(tipText == null)
			tipText = "";
		View v = (View )c.getClientProperty(BasicHTML.propertyKey);
		if(v != null) {
			Rectangle paintTextR = c.getBounds();
			Insets insets = c.getInsets();
			paintTextR.x += insets.left;
			paintTextR.y += insets.top;
			paintTextR.width -= insets.left + insets.right;
			paintTextR.height -= insets.top + insets.bottom;
			v.paint(g, paintTextR);
		}
		else {
			MultiRowString mrs = new MultiRowString(tipText);
			for(int i = 0; i < mrs.getRowCount(); i++)
				g.drawString(
						mrs.get(i), 
						3, 
						2 + metrics.getAscent() + metrics.getHeight() * i);
		}
	}

	public Dimension getPreferredSize(JComponent c) {
		FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(c.getFont());
		Insets insets = c.getInsets();
		Dimension prefSize = new Dimension(
				insets.left + insets.right,
				insets.top + insets.bottom);
		String text = ((JToolTip )c).getTipText();
		if(text != null && text.length() != 0) {
			View v = (c != null) ? (View )c.getClientProperty("html") : null;
			if(v != null) {
				prefSize.width += (int )v.getPreferredSpan(View.X_AXIS);
				prefSize.height += (int )v.getPreferredSpan(View.Y_AXIS);
			}
			else {
				MultiRowString mrs = new MultiRowString(text);
				int maxsize = SwingUtilities.computeStringWidth(fm, mrs.get(0));
				for(int i = 1; i < mrs.getRowCount(); i++)
					maxsize = Math.max(
							maxsize, 
							SwingUtilities.computeStringWidth(fm, mrs.get(i)));
				prefSize.width += maxsize + 6;
				prefSize.height += fm.getHeight() * mrs.getRowCount() + 4;
			}
		}
		return prefSize;
	}
}
