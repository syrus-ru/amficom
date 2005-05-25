package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.client.resource.ResourceKeys;


public class SimpleResizableFrame extends JInternalFrame
{
	public ResizableLayeredPanel panel;

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
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
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
				this_mouseDragged();
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

	void this_mouseDragged()
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
