package com.syrus.AMFICOM.Client.General.Report;

import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JComponent;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;

import java.awt.event.ActionEvent;

/**
 * <p>Title: </p>
 * <p>Description: Панель с бегущим заголовком</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 1.0
 */

public class TitledPanel extends JPanel
{
	String title = "";
	public JComponent insidePanel = null;

	public TitledPanel(String title,JComponent panelToRender)
	{
		this.title = title;
		insidePanel = panelToRender;

		PanelsTitle itsTitle = new PanelsTitle (title);

		double koef = 13 / panelToRender.getPreferredSize().getHeight();

		this.setLayout(new GridBagLayout());

		this.add(itsTitle,
						 new GridBagConstraints(0, 0, 1, 1, 1.0, koef, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		this.add(panelToRender,
						 new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0 - koef, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		Dimension rvPrefSize = panelToRender.getPreferredSize();
//		this.setPreferredSize(new Dimension ((int)rvPrefSize.getWidth(),(int)rvPrefSize.getHeight() + 20));
	}
}

class PanelsTitle extends JPanel
{
	String title = "";
	private Timer timer = null;
	private int cutPoint = 0;

	public PanelsTitle(String title)
	{
		this.title = title;
		this.setPreferredSize(new Dimension(-1,20));
		timer = new Timer(500,new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				repaint_title(getGraphics());/////////////////////////////////
			}
		});
		timer.start();
	}

	public void paint (Graphics g)
	{
		super.paint(g);
		int titleWidth = (int)g.getFontMetrics().getStringBounds(title,g).getWidth();
		if ((this.getParent().getWidth() - titleWidth) > 0)
		{
			g.drawString(title,(this.getParent().getWidth() - titleWidth) / 2,13);
			timer.stop();
		}
		else
		{
			repaint_title(g);
			timer.start();
		}
	}

	private void repaint_title (Graphics g)
	{
		if (g == null)
		{
			timer.stop();
			return;
		}
		String stringToDraw = "";
		if (cutPoint == 0)
			stringToDraw = title;
		else
			stringToDraw = title.substring(cutPoint) + "   " + title.substring(0,cutPoint);
		cutPoint++;
		if (cutPoint == title.length())
			cutPoint = 0;

		g.setColor(SystemColor.menu);
		g.fillRect(0,0,this.getParent().getWidth(),15);
		g.setColor(Color.black);
		g.drawString(stringToDraw,0,13);
	}
}
