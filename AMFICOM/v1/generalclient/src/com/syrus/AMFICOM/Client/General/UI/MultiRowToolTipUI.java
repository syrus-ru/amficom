package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.io.MyStringTokenizer;

import java.util.Vector;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.plaf.ToolTipUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;

import javax.swing.plaf.basic.*;

public class MultiRowToolTipUI extends BasicToolTipUI 
{
    static MultiRowToolTipUI sharedInstance = new MultiRowToolTipUI();

	public MultiRowToolTipUI()
	{
		super();
	}

    public static ComponentUI createUI(JComponent c) 
	{
        return sharedInstance;
    }

    public void paint(Graphics g, JComponent c) 
	{
        Font font = c.getFont();
		FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
//        FontMetrics metrics = g.getFontMetrics(font);
        Dimension size = c.getSize();
        g.setColor(c.getBackground());
        g.fillRect(0, 0, size.width, size.height);
        g.setColor(c.getForeground());
        g.setFont(font);
		// fix for bug 4153892
		String tipText = ((JToolTip)c).getTipText();
		if (tipText == null) 
		{
			tipText = "";
		}
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
			for(int i = 0; i < mrs.getRowCount(); i++)
				g.drawString(mrs.get(i), 3, 2 + metrics.getAscent() + metrics.getHeight() * i);
		}
    }

    public Dimension getPreferredSize(JComponent c) 
	{
        Font font = c.getFont();
		FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
//        FontMetrics fm = c.getGraphics().getFontMetrics(font);
		Insets insets = c.getInsets();
		Dimension prefSize = new Dimension(
				insets.left + insets.right,
				insets.top + insets.bottom);
		String text = ((JToolTip )c).getTipText();

		if ((text == null) || text.equals("")) 
		{
            text = "";
        }
        else 
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
				for(int i = 1; i < mrs.getRowCount(); i++)
				{
					maxsize = Math.max(maxsize, SwingUtilities.computeStringWidth(fm, mrs.get(i)));
				}
				
				prefSize.width += maxsize + 6;
				prefSize.height += fm.getHeight() * mrs.getRowCount() + 4;
			}
        }
		return prefSize;
    }
}
