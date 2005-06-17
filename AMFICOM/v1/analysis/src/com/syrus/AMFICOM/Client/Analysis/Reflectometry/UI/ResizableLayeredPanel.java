package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Map;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class ResizableLayeredPanel extends JPanel
{
	protected JPanel mainPanel = new JPanel();
	protected JLayeredPane jLayeredPane = new JLayeredPane();
	protected ToolBarPanel toolbar;

	protected int _width;
	protected int _height;

	protected boolean first_shown = true;

	public ResizableLayeredPanel()
	{
		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.addComponentListener(new ComponentAdapter()
		{
			public void componentShown(ComponentEvent e)
			{
				this_componentShown(e);
			}
		});

		this.setDoubleBuffered(true);
		this.setBorder(BorderFactory.createLoweredBevelBorder());
		this.setLayout (new BorderLayout());

		toolbar = createToolBar();
		Map commands = toolbar.createGraphButtons();

		String[] buttons = toolbar.getButtons();
		for (int i = 0; i < buttons.length; i++)
		{
			if (buttons[i].equals(ToolBarPanel.SEPARATOR)) {
				JToolBar.Separator s = new JToolBar.Separator();
				s.setOrientation(toolbar.getOrientation() == SwingConstants.VERTICAL ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
				toolbar.add(s);
			} else
				toolbar.add((Component)commands.get(buttons[i]));
		}

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jLayeredPane, BorderLayout.CENTER);

		this.add(toolbar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		
		this.mainPanel.setBackground(UIManager.getColor(ResourceKeys.COLOR_GRAPHICS_BACKGROUND));

		toolbar.setVisible(false);

	}

	public ScaledGraphPanel getTopPanel()
	{
		Component[] components = jLayeredPane.getComponents();
		for (int i = 0; i < components.length; i++)
			if (components[i] instanceof ScaledGraphPanel)
				return (ScaledGraphPanel)components[i];
		return null;
	}

	void this_componentShown(ComponentEvent e)
	{
		if (first_shown)
		{
			first_shown = false;
			updScale2fit();
		}
	}

	public void setToolBarVisible (boolean b)
	{
		toolbar.setVisible(b);
	}


	public void setGraphPanel(SimpleGraphPanel panel)
	{
		jLayeredPane.removeAll();
		jLayeredPane.add(panel);
		updScale2fit();
	}

	// unused?
	private void setGraphPanel(SimpleGraphPanel panel, int start, int end)
	{
		jLayeredPane.removeAll();
		jLayeredPane.add(panel);
		updScale2fit(start, end, 0, 0);
	}

	public void removeGraphPanel(SimpleGraphPanel panel)
	{
		jLayeredPane.remove(panel);
	}

	public void resize (double kx, double ky)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			panel.scaleX *= kx;
			panel.scaleY *= ky;
			panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));
		}
//		this.jLayeredPane.setSize(this.getSize());
//		this.jLayeredPane.revalidate();
//		this.jLayeredPane.repaint();
	}

	public void resize()
	{
		int width = jLayeredPane.getWidth();
		int height = jLayeredPane.getHeight();
		height = height > 1 ? height : 1;
		width = width > 1 ? width : 1;

		resize ((double)width/(double)_width, (double)height/(double)_height);

		_width = width;
		_height = height;
	}

	public void updScale2fit()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			panel.scaleX = ((double)(jLayeredPane.getWidth()) / (double)(panel.y.length));
			panel.scaleY = ((jLayeredPane.getHeight()) / (panel.maxY - panel.minY));
			panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));
		}
		jLayeredPane.repaint();

		_width = jLayeredPane.getWidth();
		_height = jLayeredPane.getHeight();
	}

	protected void updScale2fit(int start, int end, double indent_x, double iy)
	{
		SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(0);
		if (panel == null)
			return;

		if (start < 0)
			start = 0;
		if (end > panel.y.length)
			end = panel.y.length;

		double min = panel.y[start];
		double max = panel.y[start];
		for (int i = start; i < end; i++)
		{
			if (panel.y[i] > max)
				max = panel.y[i];
			else if (panel.y[i] < min)
				min = panel.y[i];
		}

		int ix = (int)((end - start) * indent_x);
		//double iy = (max - min) * indent_y;

		panel.scaleX = ((double)(jLayeredPane.getWidth()) / (end - start + 2*ix));
		panel.scaleY = ((jLayeredPane.getHeight()) / (max - min + 2*iy));
		panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));

		_width = jLayeredPane.getWidth();
		_height = jLayeredPane.getHeight();

		if (start - ix < 0)
			panel.setGraphBounds(start - ix, end + ix - start + ix);
		else
			panel.setGraphBounds(start - ix, end + ix);
		panel.top = panel.maxY - max - iy;
		panel.bottom = min - panel.minY - iy;

		jLayeredPane.repaint();
	}

	public void updScale (double kx, double ky,	double rx, double ry)
	{
		updScale2fit();
	}

	public void setInversedY (boolean b)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ScaledGraphPanel)
			{
				((ScaledGraphPanel)panel).inversed_y = b;
				jLayeredPane.repaint();
				return;
			}
		}
	}

	public void setScalesCoeffs (double Kx, double Ky)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (panel instanceof ScaledGraphPanel)
			{
				((ScaledGraphPanel)panel).Kx = Kx;
				((ScaledGraphPanel)panel).Ky = Ky;
				jLayeredPane.repaint();
				return;
			}
		}
	}

	protected ToolBarPanel createToolBar()
	{
		return new ToolBarPanel(this);
	}

}
