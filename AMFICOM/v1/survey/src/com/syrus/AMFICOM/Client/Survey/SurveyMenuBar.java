package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelReport;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class SurveyMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuStart = new JMenu();
	JMenuItem menuStartScheduler = new JMenuItem();
	JMenuItem menuStartAnalize = new JMenuItem();
	JMenuItem menuStartAnalizeExt = new JMenuItem();
	JMenuItem menuStartEvaluation = new JMenuItem();
	JMenuItem menuStartPrognosis = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewMapOpen = new JMenuItem();
	JMenuItem menuViewMapEditor = new JMenuItem();
	JMenuItem menuViewMapClose = new JMenuItem();
	JMenuItem menuViewMapSetup = new JMenuItem();
	JMenuItem menuViewSchemeOpen = new JMenuItem();
	JMenuItem menuViewSchemeEditor = new JMenuItem();
	JMenuItem menuViewSchemeClose = new JMenuItem();
	JMenuItem menuViewMeasurements = new JMenuItem();
	JMenuItem menuViewResults = new JMenuItem();
	JMenuItem menuViewAlarms = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuTemplateReport = new JMenuItem();

	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public SurveyMenuBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public SurveyMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ActionListener actionAdapter = new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					SurveyMenuBar.this.actionPerformed(e);
				}
			};

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuStart.setText(LangModelSurvey.getString("menuStart"));
		menuStart.setName("menuStart");
		menuStartScheduler.setText(LangModelSurvey.getString("Scheduling"));
		menuStartScheduler.setName("menuStartScheduler");
		menuStartScheduler.addActionListener(actionAdapter);
		menuStartAnalize.setText(LangModelSurvey.getString("Evaluation_analysis"));
		menuStartAnalize.setName("menuStartAnalize");
		menuStartAnalize.addActionListener(actionAdapter);
		menuStartAnalizeExt.setText(LangModelSurvey.getString("Researching"));
		menuStartAnalizeExt.setName("menuStartAnalizeExt");
		menuStartAnalizeExt.addActionListener(actionAdapter);
		menuStartEvaluation.setText(LangModelSurvey.getString("Evaluation"));
		menuStartEvaluation.setName("menuStartEvaluation");
		menuStartEvaluation.addActionListener(actionAdapter);
		menuStartPrognosis.setText(LangModelSurvey.getString("Forecasting"));
		menuStartPrognosis.setName("menuStartPrognosis");
		menuStartPrognosis.addActionListener(actionAdapter);

		menuView.setText(LangModelSurvey.getString("View"));
		menuView.setName("menuView");
		menuViewMapOpen.setText(LangModelSurvey.getString("Topological_scheme_of_net"));
		menuViewMapOpen.setName("menuViewMapOpen");
		menuViewMapOpen.addActionListener(actionAdapter);
		menuViewMapEditor.setText(LangModelSurvey.getString("Topology_editor"));
		menuViewMapEditor.setName("menuViewMapEditor");
		menuViewMapEditor.addActionListener(actionAdapter);
		menuViewMapClose.setText(LangModelSurvey.getString("Close_topological_scheme"));
		menuViewMapClose.setName("menuViewMapClose");
		menuViewMapClose.addActionListener(actionAdapter);
		menuViewMapSetup.setText(LangModelSurvey.getString("Map_operations"));
		menuViewMapSetup.setName("menuViewMapSetup");
		menuViewMapSetup.addActionListener(actionAdapter);
		menuViewSchemeOpen.setText(LangModelSurvey.getString("Net_scheme"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(actionAdapter);
		menuViewSchemeEditor.setText(LangModelSurvey.getString("Scheme_editor"));
		menuViewSchemeEditor.setName("menuViewSchemeEditor");
		menuViewSchemeEditor.addActionListener(actionAdapter);
		menuViewSchemeClose.setText(LangModelSurvey.getString("Close_scheme"));
		menuViewSchemeClose.setName("menuViewSchemeClose");
		menuViewSchemeClose.addActionListener(actionAdapter);
		menuViewMeasurements.setText(LangModelSurvey.getString("Measurement_archive"));
		menuViewMeasurements.setName("menuViewMeasurements");
		menuViewMeasurements.addActionListener(actionAdapter);
		menuViewResults.setText(LangModelSurvey.getString("Results_overview"));
		menuViewResults.setName("menuViewResults");
		menuViewResults.addActionListener(actionAdapter);
		menuViewAlarms.setText(LangModelSurvey.getString("Alarm_signals"));
		menuViewAlarms.setName("menuViewAlarms");
		menuViewAlarms.addActionListener(actionAdapter);
		menuViewAll.setText(LangModelSurvey.getString("Allocate_windows"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);

		menuReport.setText(LangModelReport.getString("label_report"));
		menuReport.setName("menuReport");
		menuTemplateReport.setText(LangModelReport.getString("label_reportForTemplate"));
		menuTemplateReport.setName("menuTemplateReport");
		menuTemplateReport.addActionListener(actionAdapter);
    
		menuHelp.setText(LangModel.getString("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModel.getString("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModel.getString("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModel.getString("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModel.getString("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModel.getString("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModel.getString("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModel.getString("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModel.getString("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModel.getString("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuStart.add(menuStartScheduler);
		menuStart.add(menuStartAnalize);
		menuStart.add(menuStartAnalizeExt);
		menuStart.add(menuStartEvaluation);
		menuStart.add(menuStartPrognosis);

		menuView.add(menuViewMapOpen);
		menuView.add(menuViewMapEditor);
		menuView.add(menuViewMapClose);
		menuView.add(menuViewMapSetup);
		menuView.addSeparator();
		menuView.add(menuViewSchemeOpen);
		menuView.add(menuViewSchemeEditor);
		menuView.add(menuViewSchemeClose);
		menuView.addSeparator();
		menuView.add(menuViewMeasurements);
		menuView.add(menuViewResults);
		menuView.add(menuViewAlarms);
		menuView.addSeparator();
		menuView.add(menuViewAll);

		menuReport.add(menuTemplateReport);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuStart);
		this.add(menuView);
		this.add(menuReport);
		this.add(menuHelp);
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
		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
		menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));
		menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
		menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));
		menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
		menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));
		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));
		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuStart.setVisible(aModel.isVisible("menuEvaluate"));
		menuStart.setEnabled(aModel.isEnabled("menuEvaluate"));
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

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));
		menuViewMapOpen.setVisible(aModel.isVisible("menuViewMapOpen"));
		menuViewMapOpen.setEnabled(aModel.isEnabled("menuViewMapOpen"));
		menuViewMapEditor.setVisible(aModel.isVisible("menuViewMapEditor"));
		menuViewMapEditor.setEnabled(aModel.isEnabled("menuViewMapEditor"));
		menuViewMapClose.setVisible(aModel.isVisible("menuViewMapClose"));
		menuViewMapClose.setEnabled(aModel.isEnabled("menuViewMapClose"));
		menuViewMapSetup.setVisible(aModel.isVisible("menuViewMapSetup"));
		menuViewMapSetup.setEnabled(aModel.isEnabled("menuViewMapSetup"));
		menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
		menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
		menuViewSchemeEditor.setVisible(aModel.isVisible("menuViewSchemeEditor"));
		menuViewSchemeEditor.setEnabled(aModel.isEnabled("menuViewSchemeEditor"));
		menuViewSchemeClose.setVisible(aModel.isVisible("menuViewSchemeClose"));
		menuViewSchemeClose.setEnabled(aModel.isEnabled("menuViewSchemeClose"));
		menuViewMeasurements.setVisible(aModel.isVisible("menuViewMeasurements"));
		menuViewMeasurements.setEnabled(aModel.isEnabled("menuViewMeasurements"));
		menuViewResults.setVisible(aModel.isVisible("menuViewResults"));
		menuViewResults.setEnabled(aModel.isEnabled("menuViewResults"));
		menuViewAlarms.setVisible(aModel.isVisible("menuViewAlarms"));
		menuViewAlarms.setEnabled(aModel.isEnabled("menuViewAlarms"));
		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));
		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));
		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));
		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));
		menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
		menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));
		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));
		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));
		menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
		menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));
		menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
		menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));
		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
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
