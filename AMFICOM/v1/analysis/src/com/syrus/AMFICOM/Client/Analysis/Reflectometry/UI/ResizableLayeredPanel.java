package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import oracle.jdeveloper.layout.*;

public class ResizableLayeredPanel extends JPanel
{
	protected JPanel mainPanel = new JPanel();
	protected JLayeredPane jLayeredPane = new JLayeredPane();
	protected ToolBarPanel toolbar;
	protected ColorManager cMan;

	protected int _width;
	protected int _height;

	protected boolean first_shown = true;

	public ResizableLayeredPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception ex)
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
			if (buttons[i].equals(ToolBarPanel.separator))
				toolbar.insert(new JToolBar.Separator());
			else
				toolbar.insert((Component)commands.get(buttons[i]));
		}

		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(jLayeredPane, BorderLayout.CENTER);

		this.add(toolbar, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);

		toolbar.setVisible(false);

		updColorModel();
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

	protected void updColorModel()
	{
		mainPanel.setBackground(SystemColor.window);
	}

	public void setGraphPanel(SimpleGraphPanel panel)
	{
		jLayeredPane.removeAll();
		jLayeredPane.add(panel);
		updScale2fit();
	}

	public void setGraphPanel(SimpleGraphPanel panel, int start, int end)
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
			panel.scale_x *= kx;
			panel.scale_y *= ky;
			panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));
		}
		jLayeredPane.repaint();
	}

	public void resize()
	{
		int width = jLayeredPane.getWidth();
		int height = jLayeredPane.getHeight();

		resize ((double)width/(double)_width, (double)height/(double)_height);

		_width = width;
		_height = height;
	}

	public void updScale2fit()
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			panel.scale_x = ((double)(jLayeredPane.getWidth()) / (double)(panel.y.length));
			panel.scale_y = ((double)(jLayeredPane.getHeight()) / (panel.max_y - panel.min_y));
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

		panel.scale_x = ((double)(jLayeredPane.getWidth()) / (end - start + 2*ix));
		panel.scale_y = ((double)(jLayeredPane.getHeight()) / (max - min + 2*iy));
		panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));

		_width = jLayeredPane.getWidth();
		_height = jLayeredPane.getHeight();

		if (start - ix < 0)
			panel.setGraphBounds(start - ix, end + ix - start + ix);
		else
			panel.setGraphBounds(start - ix, end + ix);
		panel.top = panel.max_y - max - iy;
		panel.bottom = min - panel.min_y - iy;

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

class ToolBarPanel extends JToolBar
{
	protected ResizableLayeredPanel panel;
	protected static final Dimension btn_size = new Dimension(24, 24);
	protected int position = 0;

	protected static final String separator = "separator";

	protected Map actions = new HashMap();

	protected static String[] buttons = new String[]
	{
	};

	public ToolBarPanel(ResizableLayeredPanel panel)
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
		//setPreferredSize(new Dimension (1, btn_size.height + 5));
		setBorder(BorderFactory.createEtchedBorder());
		setLayout (new XYLayout());
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		return actions;
	}

	AbstractButton createToolButton(
			AbstractButton b,
			Dimension preferred_size,
			String text,
			String tooltip,
			Icon icon,
			ActionListener actionListener,
			boolean isEnabled)
	{
		if (preferred_size != null)
			b.setPreferredSize(preferred_size);
		if (text != null)
			b.setText (text);
		if (tooltip != null)
			b.setToolTipText (tooltip);
		if (icon != null)
			b.setIcon(icon);
		if (actionListener != null)
			b.addActionListener(actionListener);
		b.setEnabled(isEnabled);
		b.setFocusable(false);
		return b;
	}

	public void insert (Component c)
	{
		add (c, new XYConstraints(position, 0, -1, -1));
		position += c.getPreferredSize().width;
	}
}