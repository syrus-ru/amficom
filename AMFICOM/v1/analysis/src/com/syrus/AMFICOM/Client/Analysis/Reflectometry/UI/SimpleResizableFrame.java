package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SimpleResizableFrame extends JInternalFrame
{
	public ResizableLayeredPanel panel;
	public ColorManager cMan;

	public SimpleResizableFrame()
	{
		this (new ResizableLayeredPanel());
	}

	public SimpleResizableFrame(ResizableLayeredPanel panel)
	{
		this.panel = panel;

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setIconifiable(true);
		this.setDoubleBuffered(true);

		this.addPropertyChangeListener(JInternalFrame.IS_MAXIMUM_PROPERTY, new java.beans.PropertyChangeListener()
		{
			public void propertyChange(java.beans.PropertyChangeEvent evt)
			{
				this_propertyChanged(evt);
			}
		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				this_mouseDragged(e);
			}
		});

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel, BorderLayout.CENTER);
	}

	public ScaledGraphPanel getTopGraphPanel()
	{
		return panel.getTopPanel();
	}

	public String getReportTitle()
	{
		return getTitle();
	}

	public void setGraph (double[] y, double deltaX, boolean isReversedY, double Kx, double Ky, String id)
	{
		setGraph (y, deltaX, isReversedY, id);
		panel.setScalesCoeffs(Kx, Ky);
	}

	public void setGraph (double[] y, double deltaX, boolean isReversedY, String id)
	{
		ScaledGraphPanel p = new ScaledGraphPanel (panel, y, deltaX);
		p.setColorModel(id);
		p.inversed_y = isReversedY;

		panel.setGraphPanel(p);
		repaint();
	}

	public void setGraph (SimpleGraphPanel p, boolean isReversedY, String id)
	{
		p.setColorModel(id);
		panel.setGraphPanel(p);
		panel.setInversedY(isReversedY);
		repaint();
	}

	public void updScales ()
	{
		panel.updScale2fit();
	}

	void this_mouseDragged(MouseEvent e)
	{
		panel.resize();
	}

	void this_propertyChanged(java.beans.PropertyChangeEvent evt)
	{
		if (evt.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY))
			panel.resize();
	}

	public void doDefaultCloseAction()
	{
		if (isMaximum())
			try
		{
			setMaximum(false);
		}
		catch (java.beans.PropertyVetoException ex)
		{
			ex.printStackTrace();
		}
		super.doDefaultCloseAction();
	}
}
