package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public class SurveyToolBar extends JToolBar implements ApplicationModelListener
{
	public final static int imgSize = 16;
	public final static int btnSize = 24;

	private ApplicationModel aModel;

	JButton menuSessionOpen = new JButton();

	JButton menuViewMapOpen = new JButton();
	JButton menuViewSchemeOpen = new JButton();

	JButton menuViewMeasurements = new JButton();
	JButton menuViewAlarms = new JButton();
	JButton menuViewResults = new JButton();

	JButton menuStartScheduler = new JButton();
	JButton menuStartAnalize = new JButton();
	JButton menuStartAnalizeExt = new JButton();
	JButton menuStartEvaluation = new JButton();
	JButton menuStartPrognosis = new JButton();

	public SurveyToolBar()
	{
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		ActionListener actionAdapter = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					SurveyToolBar.this.actionPerformed(e);
				}
			};

		Dimension buttonSize = new Dimension(btnSize, btnSize);

		menuSessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuSessionOpen.setMaximumSize(buttonSize);
		menuSessionOpen.setPreferredSize(buttonSize);
		menuSessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		menuSessionOpen.setName("menuSessionNew");
		menuSessionOpen.addActionListener(actionAdapter);

		menuViewMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuViewMapOpen.setMaximumSize(buttonSize);
		menuViewMapOpen.setPreferredSize(buttonSize);
		menuViewMapOpen.setToolTipText(LangModelSurvey.getString("Topological_scheme_of_net"));
		menuViewMapOpen.setName("menuViewMapOpen");
		menuViewMapOpen.addActionListener(actionAdapter);

		menuViewSchemeOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").
				getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewSchemeOpen.setMaximumSize(buttonSize);
		menuViewSchemeOpen.setPreferredSize(buttonSize);
		menuViewSchemeOpen.setToolTipText(LangModelSurvey.getString("Net_scheme"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(actionAdapter);

		menuViewMeasurements.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/archive_mini.gif").
                                getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));  
		menuViewMeasurements.setMaximumSize(buttonSize);
		menuViewMeasurements.setPreferredSize(buttonSize);
		menuViewMeasurements.setToolTipText(LangModelSurvey.getString("Measurement_archive"));
		menuViewMeasurements.setName("menuViewMeasurements");
		menuViewMeasurements.addActionListener(actionAdapter);

		menuViewAlarms.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/alarm_bell_red.gif").
                                getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));  
		menuViewAlarms.setMaximumSize(buttonSize);
		menuViewAlarms.setPreferredSize(buttonSize);
		menuViewAlarms.setToolTipText(LangModelSurvey.getString("Alarm_signals"));
		menuViewAlarms.setName("menuViewAlarms");
		menuViewAlarms.addActionListener(actionAdapter);

		menuViewResults.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").
                                getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));  
		menuViewResults.setMaximumSize(buttonSize);
		menuViewResults.setPreferredSize(buttonSize);
		menuViewResults.setToolTipText(LangModelSurvey.getString("Results_overview"));
		menuViewResults.setName("menuViewResults");
		menuViewResults.addActionListener(actionAdapter);

		menuStartScheduler.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/scheduling_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuStartScheduler.setMaximumSize(buttonSize);
		menuStartScheduler.setPreferredSize(buttonSize);
		menuStartScheduler.setToolTipText(LangModelSurvey.getString("Scheduling"));
		menuStartScheduler.setName("menuStartScheduler");
		menuStartScheduler.addActionListener(actionAdapter);

		menuStartAnalize.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/analyse_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuStartAnalize.setMaximumSize(buttonSize);
		menuStartAnalize.setPreferredSize(buttonSize);
		menuStartAnalize.setToolTipText(LangModelSurvey.getString("Evaluation_analysis"));
		menuStartAnalize.setName("menuStartAnalize");
		menuStartAnalize.addActionListener(actionAdapter);

		menuStartAnalizeExt.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/survey_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuStartAnalizeExt.setMaximumSize(buttonSize);
		menuStartAnalizeExt.setPreferredSize(buttonSize);
		menuStartAnalizeExt.setToolTipText(LangModelSurvey.getString("Researching"));
		menuStartAnalizeExt.setName("menuStartAnalizeExt");
		menuStartAnalizeExt.addActionListener(actionAdapter);

		menuStartEvaluation.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/evaluation_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuStartEvaluation.setMaximumSize(buttonSize);
		menuStartEvaluation.setPreferredSize(buttonSize);
		menuStartEvaluation.setToolTipText(LangModelSurvey.getString("Evaluation"));
		menuStartEvaluation.setName("menuStartEvaluation");
		menuStartEvaluation.addActionListener(actionAdapter);

		menuStartPrognosis.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif").
				getScaledInstance(imgSize, imgSize, Image.SCALE_DEFAULT)));
		menuStartPrognosis.setMaximumSize(buttonSize);
		menuStartPrognosis.setPreferredSize(buttonSize);
		menuStartPrognosis.setToolTipText(LangModelSurvey.getString("Forecasting"));
		menuStartPrognosis.setName("menuStartPrognosis");
		menuStartPrognosis.addActionListener(actionAdapter);

		add(menuSessionOpen);
		addSeparator();
		add(menuViewMapOpen);
		add(menuViewSchemeOpen);
		addSeparator();
		add(menuViewMeasurements);
		add(menuViewAlarms);
		add(menuViewResults);
		addSeparator();
		add(menuStartScheduler);
		add(menuStartAnalize);
		add(menuStartAnalizeExt);
		add(menuStartEvaluation);
		add(menuStartPrognosis);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		menuSessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuViewMapOpen.setVisible(aModel.isVisible("menuViewMapOpen"));
		menuViewMapOpen.setEnabled(aModel.isEnabled("menuViewMapOpen"));
		menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
		menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
		menuViewMeasurements.setVisible(aModel.isVisible("menuViewMeasurements"));
		menuViewMeasurements.setEnabled(aModel.isEnabled("menuViewMeasurements"));
		menuViewAlarms.setVisible(aModel.isVisible("menuViewAlarms"));
		menuViewAlarms.setEnabled(aModel.isEnabled("menuViewAlarms"));
		menuViewResults.setVisible(aModel.isVisible("menuViewResults"));
		menuViewResults.setEnabled(aModel.isEnabled("menuViewResults"));
		menuStartScheduler.setVisible(aModel.isVisible("menuStartScheduler"));
		menuStartScheduler.setEnabled(aModel.isEnabled("menuStartScheduler"));
		menuStartAnalize.setVisible(aModel.isVisible("menuStartAnalize"));
		menuStartAnalize.setEnabled(aModel.isEnabled("menuStartAnalize"));
		menuStartAnalizeExt.setVisible(aModel.isVisible("menuStartAnalizeExt"));
		menuStartAnalizeExt.setEnabled(aModel.isEnabled("menuStartAnalizeExt"));
		menuStartEvaluation.setVisible(aModel.isVisible("menuStartEvaluation"));
		menuStartEvaluation.setEnabled(aModel.isEnabled("menuStartEvaluation"));
		menuStartPrognosis.setVisible(aModel.isVisible("menuStartPrognosis"));
		menuStartPrognosis.setEnabled(aModel.isEnabled("menuStartPrognosis"));
	}

	public void actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command.execute();
	}
}
