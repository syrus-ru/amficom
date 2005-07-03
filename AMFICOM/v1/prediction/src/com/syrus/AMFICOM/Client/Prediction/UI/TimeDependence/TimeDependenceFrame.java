package com.syrus.AMFICOM.Client.Prediction.UI.TimeDependence;

import java.util.Iterator;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.*;
import com.syrus.AMFICOM.Client.Resource.Pool;

public class TimeDependenceFrame extends ScalableFrame implements OperationListener
{
	public static final String dataIsSetCommand = "timeDependentDataIsSet";
	public static final String dataId = "timeDependentDataId";

	private Dispatcher dispatcher;
	private JToggleButton showLinesButton = new JToggleButton();
	private JToggleButton showPointsButton = new JToggleButton();
	private JToggleButton showPredictionButton = new JToggleButton();

	public TimeDependenceFrame(Dispatcher dispatcher)
	{
		super(new TimeDependanceLayeredPanel(dispatcher));

		setDispatcher(dispatcher);
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
		showLinesButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLinesButton_actionPerformed(e);
			}
		});
		showPointsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPointsButton_actionPerformed(e);
			}
		});
		showPredictionButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPredictionButton_actionPerformed(e);
			}
		});

		showLinesButton.setSelected(true);
		showPointsButton.setSelected(true);
		showPredictionButton.setSelected(true);

		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().
															 getImage("images/general.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		 setTitle(LangModelPrediction.getString("TimedGraphTitle"));

		showLinesButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/show_stat_lines.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		showLinesButton.setToolTipText("Связать точки линиями");

		showPointsButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/show_stat_points.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		showPointsButton.setToolTipText("Показать точки");

		showPredictionButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/show_stat_prediction.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		showPredictionButton.setToolTipText("Показать результат экстраполирования");
	}

	public void setTrace (String id)
	{
		double[] y = new double[] {1, 30, 400};
		TimeDependencePanel p = new TimeDependencePanel(panel, y, 3);
		super.setGraph(p, true, id);
		panel.setGraphPanel(p);
		panel.setInversedY(false);
	}


	public void setDispatcher( Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
		dispatcher.register(this, dataIsSetCommand);
	}


	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals(dataIsSetCommand))
		{
			Object o = Pool.get(dataId, dataId);
			if(o != null && o instanceof TimeDependenceData [])
			{
				setData((TimeDependenceData [])o);
			}
			panel.repaint();
		}
	}

	public void setData(TimeDependenceData[] data)
	{
		Object coeffs = Pool.get("linearCoeffs", "MyLinearCoeffs");
		for (Iterator it = panels.values().iterator(); it.hasNext();)
		{
			SimpleGraphPanel p = (SimpleGraphPanel)it.next();
			if (p instanceof TimeDependencePanel)
			{
				((TimeDependencePanel)p).init(data);
				if(coeffs != null && coeffs instanceof LinearCoeffs)
					((TimeDependencePanel)p).setLinearCoeffs((LinearCoeffs)coeffs);
			}
		}
		panel.updScale2fit();
	}

	void showLinesButton_actionPerformed(ActionEvent e)
	{
		updateViewStyle();
	}

	void showPointsButton_actionPerformed(ActionEvent e)
	{
		updateViewStyle();
	}

	void showPredictionButton_actionPerformed(ActionEvent e)
	{
		updateViewStyle();
	}


	private void updateViewStyle()
	{
		for (Iterator it = panels.values().iterator(); it.hasNext();)
		{
			SimpleGraphPanel sp = (SimpleGraphPanel)it.next();
			if (sp instanceof TimeDependencePanel)
			{
				TimeDependencePanel p = (TimeDependencePanel)sp;
		/*		if (showLinesButton.isSelected())
					p.showLines = true;
				else
					p.showLines = false;
				if (showPointsButton.isSelected())
					p.showPoints = true;
				else
					p.showPoints = false;
				if (showPredictionButton.isSelected())
					p.showPrediction = true;
				else
					p.showPrediction = false;*/
			}
		panel.repaint();
		}
	}

}