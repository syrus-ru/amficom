package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionManager;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.PredictionModel;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.Statistics;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

public class TimeDependanceLayeredPanel extends ScalableLayeredPanel implements 
		CurrentEventChangeListener {
	private static final long serialVersionUID = 7568579524512348177L;

	protected double maxX;
	Dispatcher dispatcher;

	public TimeDependanceLayeredPanel(Dispatcher dispatcher) {
		super();
		init_module(dispatcher);
	}

	@Override
	protected ToolBarPanel createToolBar() {
		return new TimedToolBar(this);
	}

	void init_module(Dispatcher dispatcher1) {
		Heap.addCurrentEventChangeListener(this);
		this.dispatcher = dispatcher1;
	}
	
	public void currentEventChanged() {
		int event = Heap.getCurrentEtalonEvent2();
		setSelectedEvent(event);
	}
	
	@Override
	protected void	bar_adjustmentValueChanged() {
		double hsize = this.horizontalBar.getMaximum();
		double hposition = this.horizontalBar.getValue();
		double vsize = this.verticalBar.getMaximum();
		double vposition = this.verticalBar.getValue();

		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)p;

				double factor_x = (panel.max_x - panel.min_x)/(this.maxX/this.scale_x);
				double factor_y = (panel.maxY - panel.minY)/(this.maxY/this.scale_y);

				panel.left = (long)(((panel.max_x - panel.min_x) * hposition) / (hsize * factor_x));
				panel.right = (long)(((panel.max_x - panel.min_x) * (hsize - hposition - hwidth)) / (hsize * factor_x));
				panel.top = (((panel.maxY - panel.minY) * vposition) / (vsize * factor_y));
				panel.bottom = (((panel.maxY - panel.minY) * (vsize - vposition - vheight)) / (vsize * factor_y));
				
				break;
			}
		}
		this.jLayeredPane.repaint();
	}

	@Override
	public void updScale2fit() {
		this.horizontalBar.setMaximum(hwidth);
		this.horizontalBar.setVisibleAmount(hwidth);
		this.horizontalBar.setValue(0);
		this.horizontalBar.setMinimum(0);
		this.horizontalMax = hwidth;

		this.verticalBar.setMaximum(vheight);
		this.verticalBar.setVisibleAmount(vheight);
		this.verticalBar.setValue(0);
		this.verticalBar.setMinimum(0);
		this.verticalMax = vheight;

		double max_trace_width = 0;
		double max_trace_amplitude = 0;

		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)p;
				if((panel.maxY - panel.minY) > max_trace_amplitude) {
					max_trace_amplitude = panel.maxY - panel.minY;
					this.scale_y = (this.jLayeredPane.getHeight()) / max_trace_amplitude;
				}
				if((panel.max_x - panel.min_x) > max_trace_width) {
					max_trace_width = panel.max_x - panel.min_x;
					this.scale_x = (this.jLayeredPane.getWidth()) / max_trace_width;
				}
				
				this.maxY = max_trace_amplitude * this.scale_y;
				this.maxX = max_trace_width * this.scale_x;		
				
				updScale2fit_panel(panel);	
			}
		}
		this._width = this.jLayeredPane.getWidth();
		this._height = this.jLayeredPane.getHeight();
		bar_adjustmentValueChanged();
	}

	void updScale2fit_panel(TimeDependencePanel panel) {
		double factor_x = (panel.max_x - panel.min_x)/(this.maxX/this.scale_x);
		double factor_y = (panel.maxY - panel.minY)/(this.maxY/this.scale_y);

		panel.scaleX = factor_x*((double)(this.jLayeredPane.getWidth()) / (panel.max_x - panel.min_x));
		panel.scaleY = factor_y*((this.jLayeredPane.getHeight()) / (panel.maxY - panel.minY));
		panel.setSize(new Dimension (this.jLayeredPane.getWidth(), this.jLayeredPane.getHeight()));
	}

	void setDisplayMode(boolean show_points, boolean show_lines, boolean show_interpolation) {
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)p;
				panel.show_points = show_points;
				panel.show_lines = show_lines;
				panel.show_interpolation = show_interpolation;
			}
		}
		this.jLayeredPane.repaint();
	}

	public void updateAnalysisType() {
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)this.jLayeredPane.getComponent(i);
				int event = panel.c_event;
				setPanelParams (panel, event);
			}
		}
		this.jLayeredPane.repaint();
	}
/*
	public void updateEvents(String id) {
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)this.jLayeredPane.getComponent(i);
				panel.updEvents (id);
				updateToolbarButtons(panel.c_event);
			}
		}
	}*/

	public void setSelectedEvent(int num) {
		updateToolbarButtons(num);
		PredictionModel.setEventNumber(num);
		for(int i = 0; i < this.jLayeredPane.getComponentCount(); i++) {
			SimpleGraphPanel p = (SimpleGraphPanel)this.jLayeredPane.getComponent(i);
			if (p instanceof TimeDependencePanel) {
				TimeDependencePanel panel = (TimeDependencePanel)this.jLayeredPane.getComponent(i);
				setPanelParams (panel, num);
			}
		}
		this.jLayeredPane.repaint();
	}

	void setPanelParams (TimeDependencePanel panel, int event) {
		PredictionManager stats = PredictionModel.getPredictionManager();
		if (stats != null && event != -1) {
			Statistics statistics;
			switch (PredictionModel.getEventType()) {
			case PredictionModel.ATTENUATION:
				statistics = stats.getAttenuationInfo(event);
				break;
			case PredictionModel.AMPLITUDE:
				statistics = stats.getSplashAmplitudeInfo(event);
				break;
			case PredictionModel.POWER_LEVEL:
				statistics = stats.getAmplitudeInfo(event);
				break;
			case PredictionModel.LOSS:
				statistics = stats.getEnergyLossInfo(event);
				break;
			case PredictionModel.REFLECTANCE:
				statistics = stats.getReflectanceInfo(event);
				break;
			default:
				Log.errorMessage("PredictionModel is no initialyzed yet");
				return;
			}
			panel.init(statistics.getTimeDependence());
			panel.setLinearCoeffs(statistics.getLc());
		}
		panel.c_event = event;
	}
	
	void updateToolbarButtons(int nEvent) {
		final PredictionManager predictionManager = PredictionModel.getPredictionManager();
		final TimedToolBar toolBar = (TimedToolBar)this.toolbar;
		final boolean hasAttenuationInfo = predictionManager != null && nEvent != -1 
				&& predictionManager.hasAttenuationInfo(nEvent); 
		final boolean hasEnergyLossInfo = predictionManager != null && nEvent != -1 
				&& predictionManager.hasEnergyLossInfo(nEvent); 
		final boolean hasSplashAmplitudeInfo = predictionManager != null && nEvent != -1 
				&& predictionManager.hasSplashAmplitudeInfo(nEvent);
		final boolean hasAmplitudeInfo = predictionManager != null && nEvent != -1 
				&& predictionManager.hasAmplitudeInfo(nEvent);
		
		toolBar.lossButton.setEnabled(hasEnergyLossInfo);
		toolBar.attButton.setEnabled(hasAttenuationInfo);
		toolBar.plevelButton.setEnabled(hasSplashAmplitudeInfo);
		toolBar.amplButton.setEnabled(hasAmplitudeInfo);
		
		if (hasEnergyLossInfo && toolBar.lossButton.isSelected()) {
			 toolBar.lossButton.doClick();
		} else if (hasAttenuationInfo && toolBar.attButton.isSelected()) {
			 toolBar.attButton.doClick();
		} else if (hasAmplitudeInfo && toolBar.amplButton.isSelected()) {
			 toolBar.amplButton.doClick();
		} else if (hasSplashAmplitudeInfo && toolBar.plevelButton.isSelected()) {
			 toolBar.plevelButton.doClick();
		} else if (hasEnergyLossInfo) {
			 toolBar.lossButton.doClick();
		} else if (hasAttenuationInfo) {
			 toolBar.attButton.doClick();
		} else if (hasAmplitudeInfo) {
			 toolBar.amplButton.doClick();
		} else if (hasSplashAmplitudeInfo) {
			 toolBar.plevelButton.doClick();
		} else {
			
		}
	}
}

class TimedToolBar extends ScalableToolBar {
	private static final long serialVersionUID = -2001050955206303052L;
	protected static final String points = "points";
	protected static final String lines = "lines";
	protected static final String approx = "approx";

	protected static final String att = "att";
	protected static final String loss = "loss";
	protected static final String plevel = "plevel";
	protected static final String ampl = "ampl";

	JToggleButton pointsButton = new JToggleButton();
	JToggleButton linesButton = new JToggleButton();
	JToggleButton approximationButton = new JToggleButton();

	JToggleButton attButton = new JToggleButton();
	JToggleButton lossButton = new JToggleButton();
	JToggleButton plevelButton = new JToggleButton();
	JToggleButton amplButton = new JToggleButton();

	public TimedToolBar(TimeDependanceLayeredPanel panel) {
		super(panel);
	}

	protected static String[] buttons = new String[] {
		att, loss, plevel, ampl, SEPARATOR, points, lines, approx, SEPARATOR, EX, DX, EY, DY, FIX
	};

	@Override
	protected String[] getButtons() {
		return buttons;
	}

	@Override
	protected Map<String, AbstractButton> createGraphButtons() {
		Map<String, AbstractButton> buttons1 = super.createGraphButtons();

		buttons1.put(loss,
				createToolButton(
				this.lossButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("loss"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/loss.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						lossButton_actionPerformed(e);
					}
				},
				true));
		
		buttons1.put(att,
				createToolButton(
				this.attButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("attenuation"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/attenuation.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						attButton_actionPerformed(e);
					}
				},
				true));
		buttons1.put(plevel,
				createToolButton(
				this.plevelButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("power_level"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/entrance.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						plevelButton_actionPerformed(e);
					}
				},
				true));
		buttons1.put(ampl,
				createToolButton(
				this.amplButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("amplitude"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/amplitude.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						amplButton_actionPerformed(e);
					}
				},
				true));
		buttons1.put(points,
				createToolButton(
				this.pointsButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("show_points"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/points.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleDisplayMode();
					}
				},
				true));
		buttons1.put(lines,
				createToolButton(
				this.linesButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("show_lines"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/lines.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleDisplayMode();
					}
				},
				true));
		buttons1.put(approx,
				createToolButton(
				this.approximationButton,
				null,
				UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON),
				null,
				LangModelPrediction.getString("show_approximation"),
				new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/approximation.gif")),
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						toggleDisplayMode();
					}
				},
				true));

		ButtonGroup group = new ButtonGroup();
		group.add(this.attButton);
		group.add(this.plevelButton);
		group.add(this.amplButton);
		group.add(this.lossButton);

		this.lossButton.setSelected(true);
		this.pointsButton.setSelected(true);
		this.linesButton.setSelected(true);
		this.approximationButton.setSelected(true);
		return buttons1;
	}

	void lossButton_actionPerformed(ActionEvent e) {
		TimeDependanceLayeredPanel panel1 = (TimeDependanceLayeredPanel)super.panel;
		PredictionModel.setEventType(PredictionModel.LOSS);
		panel1.updateAnalysisType();
	}

	void attButton_actionPerformed(ActionEvent e) {
		TimeDependanceLayeredPanel panel1 = (TimeDependanceLayeredPanel)super.panel;
		PredictionModel.setEventType(PredictionModel.ATTENUATION);
		panel1.updateAnalysisType();
	}

	void plevelButton_actionPerformed(ActionEvent e) {
		TimeDependanceLayeredPanel panel1 = (TimeDependanceLayeredPanel)super.panel;
		PredictionModel.setEventType(PredictionModel.POWER_LEVEL);
		panel1.updateAnalysisType();
	}

	void amplButton_actionPerformed(ActionEvent e) {
		TimeDependanceLayeredPanel panel1 = (TimeDependanceLayeredPanel)super.panel;
		PredictionModel.setEventType(PredictionModel.AMPLITUDE);
		panel1.updateAnalysisType();
	}

	void toggleDisplayMode() {
		TimeDependanceLayeredPanel panel1 = (TimeDependanceLayeredPanel)super.panel;
		panel1.setDisplayMode(this.pointsButton.isSelected(),
												 this.linesButton.isSelected(),
												 this.approximationButton.isSelected());
	}
}
