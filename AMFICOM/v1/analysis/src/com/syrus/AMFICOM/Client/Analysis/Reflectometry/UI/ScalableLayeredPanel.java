package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.*;
import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollBar;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

public class ScalableLayeredPanel extends ResizableLayeredPanel
{
	public static final int hwidth = 20; // число градаций видимой части оси X
	public static final int vheight = 20; // число градаций видимой части оси Y

	protected JScrollBar horizontalBar = new JScrollBar(JScrollBar.HORIZONTAL);
	protected double horizontalMax = 0; // max слайдера оси X
	protected int horizontalValue = 0; // положение слайдера оси X
	protected JScrollBar verticalBar = new JScrollBar(JScrollBar.VERTICAL);
	protected double verticalMax = 0; // max слайдера оси Y
	protected int verticalValue = 0; // положение слайдера оси Y

	protected double maxLength;
	protected double maxDeltaX;
	protected double maxY;
	protected double scale_x;
	protected double scale_y;

	public ScalableLayeredPanel()
	{
		super();

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
		verticalBar.setMaximum(vheight);
		verticalBar.setVisibleAmount(vheight);
		horizontalBar.setMaximum(hwidth);
		horizontalBar.setVisibleAmount(hwidth);

		verticalBar.addAdjustmentListener(new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				if (e.getValue() != verticalValue)
				{
					verticalValue = e.getValue();
					bar_adjustmentValueChanged();
				}
			}
		});
		horizontalBar.addAdjustmentListener(new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				if (e.getValue() != horizontalValue)
				{
					horizontalValue = e.getValue();
					bar_adjustmentValueChanged();
				}
			}
		});

		mainPanel.add (verticalBar, BorderLayout.EAST);
		mainPanel.add (horizontalBar, BorderLayout.SOUTH);

		toolbar.setVisible(true);
	}

	protected ToolBarPanel createToolBar()
	{
		return new ScalableToolBar(this);
	}

	protected void	bar_adjustmentValueChanged ()
	{
		double hsize = (double)horizontalBar.getMaximum();
		double hposition = (double)horizontalBar.getValue();
		double vsize = (double)verticalBar.getMaximum();
		double vposition = (double)verticalBar.getValue();

		for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);

			double factor_x = (panel.y.length*panel.delta_x)/(maxLength*maxDeltaX/scale_x);
			double factor_y = ((double)(panel.max_y - panel.min_y))/((double)maxY/scale_y);

			panel.start = ((int) (panel.y.length * hposition / (hsize * factor_x))) ;
			panel.end = Math.min ((int) (panel.y.length * (hposition + hwidth) / (hsize * factor_x)) + 1, panel.y.length);
			panel.top = ((double)((panel.max_y - panel.min_y) * vposition) / (vsize * factor_y));
			panel.bottom = ((double)((panel.max_y - panel.min_y) * (vsize - vposition - vheight)) / (vsize * factor_y));
		}
		jLayeredPane.repaint();
	}

	public void addGraphPanel (SimpleGraphPanel panel)
	{
		jLayeredPane.add(panel);
		if (panel instanceof ScaledGraphPanel)
			((ScaledGraphPanel)panel).select_by_mouse = true;
	}

	public void removeGraphPanel (SimpleGraphPanel panel)
	{
		jLayeredPane.remove(panel);
	}

	public void removeAllGraphPanels()
	{
		jLayeredPane.removeAll();
	}

	public void updScale2fit()
	{
		horizontalBar.setMaximum(hwidth);
		horizontalBar.setVisibleAmount(hwidth);
		horizontalBar.setValue(0);
		horizontalBar.setMinimum(0);
		horizontalMax = hwidth;

		verticalBar.setMaximum(vheight);
		verticalBar.setVisibleAmount(vheight);
		verticalBar.setValue(0);
		verticalBar.setMinimum(0);
		verticalMax = vheight;

		double max_trace_length = 0;
		double max_delta_x = 0;
		double max_trace_amplitude = 0;

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if((panel.max_y - panel.min_y) > max_trace_amplitude)
			{
				max_trace_amplitude = panel.max_y - panel.min_y;
				scale_y = (double)(jLayeredPane.getHeight()) / max_trace_amplitude;
			}

			if(panel.y.length*panel.delta_x > max_trace_length)
			{
				max_trace_length = panel.y.length*panel.delta_x;
				max_delta_x = panel.delta_x;
				scale_x = (double)(jLayeredPane.getWidth()) / (double)(panel.y.length);
			}
		}

		maxY = max_trace_amplitude * scale_y;
		maxLength = max_trace_length * scale_x/ max_delta_x;
		maxDeltaX = max_delta_x;

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			updScale2fit_panel(panel);
		}

		_width = jLayeredPane.getWidth();
		_height = jLayeredPane.getHeight();
		bar_adjustmentValueChanged();
	}


	public void updScale2fit(int start, int end)
	{
		updScale2fit(start, end, 0, 0);
	}

	protected void updScale2fit(int start, int end, double indent_x, double iy)
	{
		SimpleGraphPanel activePanel = (SimpleGraphPanel)jLayeredPane.getComponent(0);
		if (activePanel == null)
			return;

		if (start < 0)
			start = 0;
		if (end > activePanel.y.length)
			end = activePanel.y.length;

		double min = activePanel.y[start];
		double max = activePanel.y[start];
		for (int i = start; i < end; i++)
		{
			if (activePanel.y[i] > max)
				max = activePanel.y[i];
			else if (activePanel.y[i] < min)
				min = activePanel.y[i];
		}
		int ix = (int)((end - start) * indent_x);

		double _scale_x = activePanel.scale_x;
		double _scale_y = activePanel.scale_y;
		double sc_x = ((double)(jLayeredPane.getWidth()) / (double)(end - start + 2*ix));
		double sc_y = ((double)(jLayeredPane.getHeight()) / (max - min + 2*iy));

		updScale (sc_x/_scale_x, sc_y/_scale_y, 0.5, 0.5);

		horizontalBar.setMinimum(-5);
		horizontalBar.setValue((int)(horizontalMax * ((double)(start - ix)  * activePanel.delta_x) / (maxLength*maxDeltaX/scale_x)));
		verticalBar.setMinimum(-5);
		verticalBar.setValue((int)(verticalMax * ((activePanel.max_y - max - iy) / ((double)maxY/scale_y))));
	}

	void updScale2fit_panel(SimpleGraphPanel panel)
	{
		double factor_x = (panel.y.length*panel.delta_x)/(maxLength*maxDeltaX/scale_x);
		double factor_y = (panel.max_y - panel.min_y)/(maxY/scale_y);

		panel.scale_x = factor_x*((double)(jLayeredPane.getWidth()) / (double)(panel.y.length));
		panel.scale_y = factor_y*((double)(jLayeredPane.getHeight()) / (panel.max_y - panel.min_y));
		//panel.updateScale((double)(jLayeredPane.getWidth())*factor_x,  (double)(jLayeredPane.getHeight()*factor_y));
		//panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));
		panel.setSize(jLayeredPane.getSize());
	}

	public void updScale (double kx, // коэфиициент ресайза по иксу
												double ky, // по игреку
												double rx, // положение точки вокруг которой делается ресайз относительно
												double ry) // ширины/высоты окна (очевидно может изменяться в пределах [0, 1])
	{
		horizontalMax *= kx;
		verticalMax *= ky;

		if (kx > 1)
		{
			horizontalBar.setMaximum((int)horizontalMax);
			horizontalBar.setValue((int)(Math.max(horizontalBar.getMinimum(), horizontalBar.getValue()*kx + hwidth * kx * rx - hwidth * .5)));
		}
		else if (kx < 1)
		{
			horizontalBar.setValue((int)(Math.max(horizontalBar.getMinimum(), horizontalBar.getValue()*kx + hwidth * kx * rx - hwidth * .5)));
			horizontalBar.setMaximum((int)horizontalMax);
		}
		horizontalBar.setVisibleAmount(hwidth);

		if (ky > 1)
		{
			verticalBar.setMaximum((int)verticalMax);
			verticalBar.setValue((int)(Math.max(verticalBar.getMinimum(), verticalBar.getValue()*ky + vheight * ky * ry - vheight * .5)));
		}
		else if (ky < 1)
		{
			verticalBar.setValue((int)(Math.max(verticalBar.getMinimum(), verticalBar.getValue()*ky + vheight * ky * ry - vheight * .5)));
			verticalBar.setMaximum((int)verticalMax);
		}
		verticalBar.setVisibleAmount(vheight);

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			panel.scale_x *=kx;
			panel.scale_y *=ky;
		}

		bar_adjustmentValueChanged ();
	}
}

class ScalableToolBar extends ToolBarPanel
{
	protected static final String ex = "exButton";
	protected static final String ey = "eyButton";
	protected static final String dx = "dxButton";
	protected static final String dy = "dyButton";
	protected static final String fit = "fitButton";

	JButton exButton = new JButton();
	JButton eyButton = new JButton();
	JButton dxButton = new JButton();
	JButton dyButton = new JButton();
	JButton fitButton = new JButton();

	protected static String[] buttons = new String[]
	{
		ex, dx, ey, dy, fit
	};

	public ScalableToolBar(ScalableLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Hashtable createGraphButtons()
	{
		Hashtable buttons = new Hashtable();

		buttons.put(
				ex,
				createToolButton(
				exButton,
				btn_size,
				null,
				LangModelAnalyse.String("encreasex"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/enlargex.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						exButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				ey,
				createToolButton(
				eyButton,
				btn_size,
				null,
				LangModelAnalyse.String("encreasey"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/enlargey.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				dx,
				createToolButton(
				dxButton,
				btn_size,
				null,
				LangModelAnalyse.String("decreasex"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reducex.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dxButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				dy,
				createToolButton(
				dyButton,
				btn_size,
				null,
				LangModelAnalyse.String("decreasey"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reducey.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				fit,
				createToolButton(
				fitButton,
				btn_size,
				null,
				LangModelAnalyse.String("fittoscreen"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/fit.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fitButton_actionPerformed(e);
					}
				},
				true));

		buttons.putAll(super.createGraphButtons());
		return buttons;
	}

	void dxButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.horizontalMax * k < panel.hwidth)
			k = (double)panel.hwidth / panel.horizontalMax;
		if (panel.horizontalBar.getMaximum() > panel.hwidth)
			panel.updScale(k, 1, .5, .5);
	}

	void exButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.horizontalBar.getMaximum() < panel.hwidth * 1000)
			panel.updScale(k, 1, .5, .5);
	}

	void dyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.verticalMax * k < panel.vheight)
			k = (double)panel.vheight / (double)panel.verticalMax;
		if (panel.verticalBar.getMaximum() > panel.vheight)
			panel.updScale(1, k, .5, .5);
	}

	void eyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.verticalBar.getMaximum() < panel.vheight * 150)
			panel.updScale(1, k, .5, .5);
	}

	void fitButton_actionPerformed(ActionEvent e)
	{
		panel.updScale2fit();
	}
}
