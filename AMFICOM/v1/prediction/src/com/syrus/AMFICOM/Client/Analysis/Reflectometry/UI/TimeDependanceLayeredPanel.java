package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.analysis.dadara.*;

public class TimeDependanceLayeredPanel extends ScalableLayeredPanel implements OperationListener
{
	public static final int POWER_LEVEL = 0;
	public static final int ATTENUATION = 1;
	public static final int AMPLITUDE = 2;
	public static final int REFLECTANCE = 3;
	public static final int LOSS = 4;

	protected double maxX;
	Dispatcher dispatcher;

	public TimeDependanceLayeredPanel(Dispatcher dispatcher)
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

		init_module(dispatcher);
	}

	private void jbInit() throws Exception
	{
	}

	protected ToolBarPanel createToolBar()
	{
		return new TimedToolBar(this);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, RefUpdateEvent.typ);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(RefUpdateEvent.typ))
		{
			RefUpdateEvent refUpdateEvent = (RefUpdateEvent)ae;
			if(refUpdateEvent.EVENT_SELECTED)
			{
				String eventNumber = (String)refUpdateEvent.getSource();
				setSelectedEvent(Integer.parseInt(eventNumber));
			}
			if(refUpdateEvent.ANALYSIS_PERFORMED)
			{
				String id = (String)refUpdateEvent.getSource();
				updateEvents(id);
			}
		}
	}

	protected void	bar_adjustmentValueChanged ()
	{
		double hsize = (double)horizontalBar.getMaximum();
		double hposition = (double)horizontalBar.getValue();
		double vsize = (double)verticalBar.getMaximum();
		double vposition = (double)verticalBar.getValue();

		for(int i = 0; i < jLayeredPane.getComponentCount(); i++)
		{
			SimpleGraphPanel p = (SimpleGraphPanel)jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel)
			{
				TimeDependencePanel panel = (TimeDependencePanel)p;

				double factor_x = ((double)(panel.max_x - panel.min_x))/((double)(maxX)/scale_x);
				double factor_y = ((double)(panel.max_y - panel.min_y))/((double)(maxY)/scale_y);

				panel.left = (long)((double)((panel.max_x - panel.min_x) * hposition) / (hsize * factor_x));
				panel.right = (long)((double)((panel.max_x - panel.min_x) * (hsize - hposition - hwidth)) / (hsize * factor_x));
				panel.top = ((double)((panel.max_y - panel.min_y) * vposition) / (vsize * factor_y));
				panel.bottom = ((double)((panel.max_y - panel.min_y) * (vsize - vposition - vheight)) / (vsize * factor_y));
			}
		}
		jLayeredPane.repaint();
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

		double max_trace_width = 0;
		double max_trace_amplitude = 0;

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			if((panel.max_y - panel.min_y) > max_trace_amplitude)
			{
				max_trace_amplitude = panel.max_y - panel.min_y;
				scale_y = (double)(jLayeredPane.getHeight()) / max_trace_amplitude;
			}
			if((panel.max_x - panel.min_x) > max_trace_width)
			{
				max_trace_width = panel.max_x - panel.min_x;
				scale_x = (double)(jLayeredPane.getWidth()) / max_trace_width;
			}
		}

		maxY = max_trace_amplitude * scale_y;
		maxX = max_trace_width * scale_x;

		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			updScale2fit_panel(panel);
		}
		_width = jLayeredPane.getWidth();
		_height = jLayeredPane.getHeight();
		bar_adjustmentValueChanged();
	}

	void updScale2fit_panel(TimeDependencePanel panel)
	{
		double factor_x = (panel.max_x - panel.min_x)/(maxX/scale_x);
		double factor_y = (panel.max_y - panel.min_y)/(maxY/scale_y);

		panel.scale_x = factor_x*((double)(jLayeredPane.getWidth()) / (panel.max_x - panel.min_x));
		panel.scale_y = factor_y*((double)(jLayeredPane.getHeight()) / (panel.max_y - panel.min_y));
		panel.setSize(new Dimension (jLayeredPane.getWidth(), jLayeredPane.getHeight()));
	}

	void setDisplayMode(boolean show_points, boolean show_lines, boolean show_interpolation)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			panel.show_points = show_points;
			panel.show_lines = show_lines;
			panel.show_interpolation = show_interpolation;
		}
		jLayeredPane.repaint();
	}

	public void setAnalysisType (int type)
	{
		ReflectoEventStatistics stats = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			int event = panel.c_event;
			setPanelParams (panel, type, event);
		}
		dispatcher.notify(new OperationEvent(this, 0, "setHisto1D"));
		dispatcher.notify(new OperationEvent(this, 0, "timeDependentDataIsSet"));
		jLayeredPane.repaint();
	}

	public void updateEvents(String id)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			panel.updEvents (id);
			updateToolbarButtons(panel.type, panel.events[panel.c_event]);
		}
	}

	public void setSelectedEvent(int num)
	{
		for(int i=0; i<jLayeredPane.getComponentCount(); i++)
		{
			TimeDependencePanel panel = (TimeDependencePanel)jLayeredPane.getComponent(i);
			int type = panel.type;
			setPanelParams (panel, type, num);
			if (panel.c_event != -1)
				updateToolbarButtons(panel.type, panel.events[panel.c_event]);
		}
		dispatcher.notify(new OperationEvent(this, 0, "setHisto1D"));
		dispatcher.notify(new OperationEvent(this, 0, "timeDependentDataIsSet"));
		jLayeredPane.repaint();
	}

	void setPanelParams (TimeDependencePanel panel, int type, int event)
	{
		ReflectoEventStatistics stats = (ReflectoEventStatistics)Pool.get("statData", "theStatData");
		switch (type)
		{
			case ATTENUATION:
				stats.getAttenuationInformation(event);
				break;
			case AMPLITUDE:
				stats.getSplashAmplitudeInformation(event);
				break;
			case POWER_LEVEL:
				stats.getAmplitudeInformation(event);
				break;
			case LOSS:
				stats.getEnergyLossInformation(event);
				break;
			case REFLECTANCE:
				stats.getReflectanceInformation(event);
		}
		TimeDependenceData[] tdd = (TimeDependenceData[])Pool.get("timeDependentDataId", "timeDependentDataId");
		panel.type = type;
		panel.c_event = event;
		panel.init(tdd);
	}

	void updateToolbarButtons(int type, TraceEvent event)
	{
		TimedToolBar toolBar = (TimedToolBar)toolbar;
		switch (event.type)
		{
			case TraceEvent.LINEAR:
				toolBar.lossButton.setEnabled(true);
				toolBar.amplButton.setEnabled(false);
				toolBar.reflButton.setEnabled(false);
				toolBar.attButton.setEnabled(true);
				toolBar.plevelButton.setEnabled(true);
				break;
			case TraceEvent.WELD:
				toolBar.lossButton.setEnabled(true);
				toolBar.amplButton.setEnabled(false);
				toolBar.reflButton.setEnabled(false);
				toolBar.attButton.setEnabled(false);
				toolBar.plevelButton.setEnabled(true);
				break;
			case TraceEvent.CONNECTOR:
				toolBar.lossButton.setEnabled(true);
				toolBar.amplButton.setEnabled(true);
				toolBar.reflButton.setEnabled(true);
				toolBar.attButton.setEnabled(false);
				toolBar.plevelButton.setEnabled(true);
				break;
			case TraceEvent.INITIATE:
				toolBar.lossButton.setEnabled(false);
				toolBar.amplButton.setEnabled(true);
				toolBar.reflButton.setEnabled(false);
				toolBar.attButton.setEnabled(false);
				toolBar.plevelButton.setEnabled(false);
				break;
			case TraceEvent.TERMINATE:
				toolBar.lossButton.setEnabled(false);
				toolBar.amplButton.setEnabled(true);
				toolBar.reflButton.setEnabled(true);
				toolBar.attButton.setEnabled(false);
				toolBar.plevelButton.setEnabled(true);
				break;
		}
		switch (type)
		{
			case ATTENUATION:
				if (event.type == TraceEvent.WELD)
					toolBar.lossButton.doClick();
				else if (event.type == TraceEvent.CONNECTOR ||
						event.type == TraceEvent.TERMINATE)
					toolBar.reflButton.doClick();
				else if (event.type == TraceEvent.INITIATE)
					toolBar.amplButton.doClick();
				break;
			case AMPLITUDE:
				if (event.type == TraceEvent.LINEAR ||
						event.type == TraceEvent.WELD)
					toolBar.lossButton.doClick();
				break;
			case POWER_LEVEL:
				if (event.type == TraceEvent.INITIATE)
					toolBar.amplButton.doClick();
				break;
			case LOSS:
				if (event.type == TraceEvent.INITIATE)
					toolBar.amplButton.doClick();
				else if (event.type == TraceEvent.TERMINATE)
					toolBar.reflButton.doClick();
				break;
			case REFLECTANCE:
				if (event.type == TraceEvent.LINEAR ||
					 event.type == TraceEvent.WELD)
					toolBar.lossButton.doClick();
				else if (event.type == TraceEvent.INITIATE)
					toolBar.amplButton.doClick();

		}
	}
}

class TimedToolBar extends ScalableToolBar
{
	protected static final String points = "points";
	protected static final String lines = "lines";
	protected static final String approx = "approx";

	protected static final String att = "att";
	protected static final String loss = "loss";
	protected static final String plevel = "plevel";
	protected static final String ampl = "ampl";
	protected static final String refl = "refl";


	JToggleButton pointsButton = new JToggleButton();
	JToggleButton linesButton = new JToggleButton();
	JToggleButton approximationButton = new JToggleButton();

	JToggleButton attButton = new JToggleButton();
	JToggleButton lossButton = new JToggleButton();
	JToggleButton plevelButton = new JToggleButton();
	JToggleButton amplButton = new JToggleButton();
	JToggleButton reflButton = new JToggleButton();

	public TimedToolBar(TimeDependanceLayeredPanel panel)
	{
		super(panel);
	}

	protected static String[] buttons = new String[]
	{
			att, loss, plevel, ampl, refl, separator, points, lines, approx, separator, ex, dx, ey, dy, fit
	};

	protected String[] getButtons()
	{
		return buttons;
	}

	protected Map createGraphButtons()
	{
		Map buttons = super.createGraphButtons();

		buttons.put(
				loss,
				createToolButton(
				lossButton,
				btn_size,
				null,
				LangModelPrediction.getString("loss"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/loss.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						lossButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
			att,
				createToolButton(
				attButton,
				btn_size,
				null,
				LangModelPrediction.getString("attenuation"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/attenuation.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						attButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				plevel,
				createToolButton(
				plevelButton,
				btn_size,
				null,
				LangModelPrediction.getString("power_level"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/entrance.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						plevelButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				ampl,
				createToolButton(
				amplButton,
				btn_size,
				null,
				LangModelPrediction.getString("amplitude"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/amplitude.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						amplButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				refl,
				createToolButton(
				reflButton,
				btn_size,
				null,
				LangModelPrediction.getString("reflectance"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reflect.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						reflButton_actionPerformed(e);
					}
				},
				true));
		buttons.put(
				points,
				createToolButton(
				pointsButton,
				btn_size,
				null,
				LangModelPrediction.getString("show_points"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/points.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						toggleDisplayMode();
					}
				},
				true));
		buttons.put(
				lines,
				createToolButton(
				linesButton,
				btn_size,
				null,
				LangModelPrediction.getString("show_lines"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/lines.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						toggleDisplayMode();
					}
				},
				true));
		buttons.put(
				approx,
				createToolButton(
				approximationButton,
				btn_size,
				null,
				LangModelPrediction.getString("show_approximation"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/approximation.gif")),
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						toggleDisplayMode();
					}
				},
				true));

		ButtonGroup group = new ButtonGroup();
		group.add(attButton);
		group.add(plevelButton);
		group.add(amplButton);
		group.add(reflButton);
		group.add(lossButton);

		lossButton.setSelected(true);
		pointsButton.setSelected(true);
		linesButton.setSelected(true);
		approximationButton.setSelected(true);
		return buttons;
	}

	void lossButton_actionPerformed(ActionEvent e)
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setAnalysisType(panel.LOSS);
	}

	void attButton_actionPerformed(ActionEvent e)
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setAnalysisType(panel.ATTENUATION);
	}

	void plevelButton_actionPerformed(ActionEvent e)
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setAnalysisType(panel.POWER_LEVEL);
	}

	void amplButton_actionPerformed(ActionEvent e)
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setAnalysisType(panel.AMPLITUDE);
	}

	void reflButton_actionPerformed(ActionEvent e)
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setAnalysisType(panel.REFLECTANCE);
	}

	void toggleDisplayMode()
	{
		TimeDependanceLayeredPanel panel = (TimeDependanceLayeredPanel)super.panel;
		panel.setDisplayMode(pointsButton.isSelected(),
												 linesButton.isSelected(),
												 approximationButton.isSelected());
	}
}
