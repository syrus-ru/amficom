package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;

public class ScalableLayeredPanel extends ResizableLayeredPanel
{
	public static final int hwidth = 20; // число градаций видимой части оси X
	public static final int vheight = 20; // число градаций видимой части оси Y

	protected JScrollBar horizontalBar = new JScrollBar(Adjustable.HORIZONTAL);
	protected double horizontalMax = 0; // max слайдера оси X
	protected int horizontalValue = 0; // положение слайдера оси X
	protected JScrollBar verticalBar = new JScrollBar(Adjustable.VERTICAL);
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
		double hsize = horizontalBar.getMaximum();
		double hposition = horizontalBar.getValue();
		double vsize = verticalBar.getMaximum();
		double vposition = verticalBar.getValue();

		for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);

			double factor_x = (panel.y.length*panel.deltaX)/(maxLength*maxDeltaX/scale_x);
			double factor_y = (panel.maxY - panel.minY)/(maxY/scale_y);

			panel.setGraphBounds(
					((int) (panel.y.length * hposition / (hsize * factor_x))),
					(int) (panel.y.length * (hposition + hwidth) / (hsize * factor_x)) + 1);
			panel.top = (((panel.maxY - panel.minY) * vposition) / (vsize * factor_y));
			panel.bottom = (((panel.maxY - panel.minY) * (vsize - vposition - vheight)) / (vsize * factor_y));
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

		double maxTraceLength = 0;
		double maxDeltaX = 0;
		double maxTraceAmplitude = 0;

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel panel = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if((panel.maxY - panel.minY) > maxTraceAmplitude)
			{
				maxTraceAmplitude = panel.maxY - panel.minY;
				scale_y = jLayeredPane.getHeight() / maxTraceAmplitude;
			}

			if(panel.y.length*panel.deltaX > maxTraceLength)
			{
				maxTraceLength = panel.y.length*panel.deltaX;
				maxDeltaX = panel.deltaX;
				scale_x = (double)(jLayeredPane.getWidth()) / (double)(panel.y.length);
			}
		}

		maxY = maxTraceAmplitude * scale_y;
		maxLength = maxTraceLength * scale_x/ maxDeltaX;
		this.maxDeltaX = maxDeltaX;

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

		double _scale_x = activePanel.scaleX;
		double _scale_y = activePanel.scaleY;
		double sc_x = ((double)(jLayeredPane.getWidth()) / (double)(end - start + 2*ix));
		double sc_y = (jLayeredPane.getHeight() / (max - min + 2*iy));

		updScale (sc_x/_scale_x, sc_y/_scale_y, 0.5, 0.5);

		horizontalBar.setMinimum(-5);
		horizontalBar.setValue((int)(horizontalMax * ((start - ix)  * activePanel.deltaX) / (maxLength*maxDeltaX/scale_x)));
		verticalBar.setMinimum(-5);
		verticalBar.setValue((int)(verticalMax * ((activePanel.maxY - max - iy) / (maxY/scale_y))));
	}

	void updScale2fit_panel(SimpleGraphPanel panel)
	{
		double factor_x = (panel.y.length*panel.deltaX)/(maxLength*maxDeltaX/scale_x);
		double factor_y = (panel.maxY - panel.minY)/(maxY/scale_y);

		panel.scaleX = factor_x*((double)(jLayeredPane.getWidth()) / (double)(panel.y.length));
		panel.scaleY = factor_y*((jLayeredPane.getHeight()) / (panel.maxY - panel.minY));
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
			panel.scaleX *=kx;
			panel.scaleY *=ky;
		}

		bar_adjustmentValueChanged ();
	}
}

class ScalableToolBar extends ToolBarPanel
{
	protected static final String EX = "exButton";
	protected static final String EY = "eyButton";
	protected static final String DX = "dxButton";
	protected static final String DY = "dyButton";
	protected static final String FIX = "fitButton";

	private JButton exButton = new JButton();
	private JButton eyButton = new JButton();
	private JButton dxButton = new JButton();
	private JButton dyButton = new JButton();
	private JButton fitButton = new JButton();

	protected static String[] buttons = new String[]
	{
		EX, DX, EY, DY, FIX
	};

	public ScalableToolBar(ScalableLayeredPanel panel)
	{
		super(panel);
	}

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

		buttons.put(
				EX,
				createToolButton(
				exButton,
				btn_size,
				null,
				LangModelAnalyse.getString("encreasex"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_X),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						exButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				EY,
				createToolButton(
				eyButton,
				btn_size,
				null,
				LangModelAnalyse.getString("encreasey"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_Y),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						eyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				DX,
				createToolButton(
				dxButton,
				btn_size,
				null,
				LangModelAnalyse.getString("decreasex"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_X),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dxButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				DY,
				createToolButton(
				dyButton,
				btn_size,
				null,
				LangModelAnalyse.getString("decreasey"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_Y),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						dyButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				FIX,
				createToolButton(
				fitButton,
				btn_size,
				null,
				LangModelAnalyse.getString("fittoscreen"),
				UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_FIT),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						fitButton_actionPerformed(e);
					}
				},
				true));

		return buttons;
	}

	void dxButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.horizontalMax * k < ScalableLayeredPanel.hwidth)
			k = ScalableLayeredPanel.hwidth / panel.horizontalMax;
		if (panel.horizontalBar.getMaximum() > ScalableLayeredPanel.hwidth)
			panel.updScale(k, 1, .5, .5);
	}

	void exButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.horizontalBar.getMaximum() < ScalableLayeredPanel.hwidth * 1000)
			panel.updScale(k, 1, .5, .5);
	}

	void dyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = .8;
		if (panel.verticalMax * k < ScalableLayeredPanel.vheight)
			k = ScalableLayeredPanel.vheight / panel.verticalMax;
		if (panel.verticalBar.getMaximum() > ScalableLayeredPanel.vheight)
			panel.updScale(1, k, .5, .5);
	}

	void eyButton_actionPerformed(ActionEvent e)
	{
		ScalableLayeredPanel panel = (ScalableLayeredPanel)super.panel;
		double k = 1.25;
		if (panel.verticalBar.getMaximum() < ScalableLayeredPanel.vheight * 150)
			panel.updScale(1, k, .5, .5);
	}

	void fitButton_actionPerformed(ActionEvent e)
	{
		panel.updScale2fit();
	}
}
