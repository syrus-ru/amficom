package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.ReflectoEventStatistics;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.TimeDependenceData;
import com.syrus.AMFICOM.analysis.dadara.TraceEvent;
import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

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

class TimedToolBar extends ToolBarPanel
{
	ButtonGroup gr = new ButtonGroup();

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
		setLayout (new XYLayout());

		attButton.setMaximumSize(btn_size);
		attButton.setMinimumSize(btn_size);
		attButton.setPreferredSize(btn_size);
//		attButton.setFocusable(false);
		attButton.setToolTipText(LangModelPrediction.getString("attenuation"));
		attButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/attenuation.gif")));
		attButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				attButton_actionPerformed(e);
			}
		});
		lossButton.setMaximumSize(btn_size);
		lossButton.setMinimumSize(btn_size);
		lossButton.setPreferredSize(btn_size);
//		attButton.setFocusable(false);
		lossButton.setToolTipText(LangModelPrediction.getString("loss"));
		lossButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/loss.gif")));
		lossButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				lossButton_actionPerformed(e);
			}
		});
		plevelButton.setMaximumSize(btn_size);
		plevelButton.setMinimumSize(btn_size);
		plevelButton.setPreferredSize(btn_size);
//		amplButton.setFocusable(false);
		plevelButton.setToolTipText(LangModelPrediction.getString("power_level"));
		plevelButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/entrance.gif")));
		plevelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				plevelButton_actionPerformed(e);
			}
		});
		amplButton.setMaximumSize(btn_size);
		amplButton.setMinimumSize(btn_size);
		amplButton.setPreferredSize(btn_size);
//		reflButton.setFocusable(false);
		amplButton.setToolTipText(LangModelPrediction.getString("amplitude"));
		amplButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/amplitude.gif")));
		amplButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				amplButton_actionPerformed(e);
			}
		});

		reflButton.setMaximumSize(btn_size);
		reflButton.setMinimumSize(btn_size);
		reflButton.setPreferredSize(btn_size);
//		reflButton.setFocusable(false);
		reflButton.setToolTipText(LangModelPrediction.getString("reflectance"));
		reflButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/reflect.gif")));
		reflButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				reflButton_actionPerformed(e);
			}
		});

		pointsButton.setMaximumSize(btn_size);
		pointsButton.setMinimumSize(btn_size);
		pointsButton.setPreferredSize(btn_size);
//		attButton.setFocusable(false);
		pointsButton.setToolTipText(LangModelPrediction.getString("show_points"));
		pointsButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/points.gif")));
		pointsButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				toggleDisplayMode();
			}
		});
		linesButton.setMaximumSize(btn_size);
		linesButton.setMinimumSize(btn_size);
		linesButton.setPreferredSize(btn_size);
//		amplButton.setFocusable(false);
		linesButton.setToolTipText(LangModelPrediction.getString("show_lines"));
		linesButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/lines.gif")));
		linesButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				toggleDisplayMode();
			}
		});
		approximationButton.setMaximumSize(btn_size);
		approximationButton.setMinimumSize(btn_size);
		approximationButton.setPreferredSize(btn_size);
//		reflButton.setFocusable(false);
		approximationButton.setToolTipText(LangModelPrediction.getString("show_approximation"));
		approximationButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/approximation.gif")));
		approximationButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				toggleDisplayMode();
			}
		});

		gr.add(attButton);
		gr.add(plevelButton);
		gr.add(amplButton);
		gr.add(reflButton);
		gr.add(lossButton);

		add(plevelButton, new XYConstraints(0, 0, -1, -1));
		add(lossButton, new XYConstraints(btn_size.width, 0, -1, -1));
		add(attButton, new XYConstraints(2 * btn_size.width, 0, -1, -1));
		add(reflButton, new XYConstraints(3 * btn_size.width, 0, -1, -1));
		add(amplButton, new XYConstraints(4 * btn_size.width, 0, -1, -1));

		add(pointsButton, new XYConstraints(6 * btn_size.width, 0, -1, -1));
		add(linesButton, new XYConstraints(7 * btn_size.width, 0, -1, -1));
		add(approximationButton, new XYConstraints(8 * btn_size.width, 0, -1, -1));

		lossButton.setSelected(true);
		pointsButton.setSelected(true);
		linesButton.setSelected(true);
		approximationButton.setSelected(true);
	}

	protected int getItemsCount ()
	{
		return 9;
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
