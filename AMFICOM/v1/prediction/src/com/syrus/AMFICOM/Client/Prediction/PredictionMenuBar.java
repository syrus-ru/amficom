package com.syrus.AMFICOM.Client.Prediction;

import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

public class PredictionMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionOpen = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
//	JMenuItem menuSessionSave = new JMenuItem();
//	JMenuItem menuSessionUndo = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewDataLoad = new JMenuItem();
	JMenuItem menuTraceAddCompare = new JMenuItem();
	JMenuItem menuTraceRemoveCompare = new JMenuItem();
	JMenuItem menuTraceClose = new JMenuItem();
	JMenuItem menuViewCountPrediction = new JMenuItem();
	JMenuItem menuViewSavePrediction = new JMenuItem();

	JMenu menuReport = new JMenu();
	JMenuItem menuReportCreate = new JMenuItem();

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

	public PredictionMenuBar()
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

	public PredictionMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		StatisticsMenuBar_this_actionAdapter actionAdapter =
				new StatisticsMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModelPrediction.Text("menuSession"));
		menuSession.setName("menuSession");
		menuSessionOpen.setText(LangModelPrediction.Text("menuSessionOpen"));
		menuSessionOpen.setName("menuSessionOpen");
		menuSessionOpen.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModelPrediction.Text("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModelPrediction.Text("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModelPrediction.Text("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(
				LangModelPrediction.Text("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);

		menuSessionDomain.setText(LangModelPrediction.Text("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
//		menuSessionSave.setText(LangModelPrediction.Text("menuSessionSave"));
//		menuSessionSave.setName("menuSessionSave");
//		menuSessionSave.addActionListener(actionAdapter);
//		menuSessionUndo.setText(LangModelPrediction.Text("menuSessionUndo"));
//		menuSessionUndo.setName("menuSessionUndo");
//		menuSessionUndo.addActionListener(actionAdapter);
		menuExit.setText(LangModelPrediction.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuView.setText(LangModelPrediction.Text("menuView"));
		menuView.setName("menuView");

		menuViewDataLoad.setText(LangModelPrediction.Text("menuViewDataLoad"));
		menuViewDataLoad.setName("menuViewDataLoad");
		menuViewDataLoad.addActionListener(actionAdapter);

		menuTraceAddCompare.setText(LangModelAnalyse.Text("menuTraceAddCompare"));
		menuTraceAddCompare.setName("menuTraceAddCompare");
		menuTraceAddCompare.addActionListener(actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.Text("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		menuTraceRemoveCompare.addActionListener(actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.Text("menuFileClose"));
		menuTraceClose.setName("menuTraceClose");
		menuTraceClose.addActionListener(actionAdapter);

		menuViewCountPrediction.setText(LangModelPrediction.Text("menuViewCountPrediction"));
		menuViewCountPrediction.setName("menuViewCountPrediction");
		menuViewCountPrediction.addActionListener(actionAdapter);

		menuViewSavePrediction.setText(LangModelPrediction.Text("menuViewSavePrediction"));
		menuViewSavePrediction.setName("menuViewSavePrediction");
		menuViewSavePrediction.addActionListener(actionAdapter);

		menuReport.setText(LangModelAnalyse.Text("menuReport"));
		menuReport.setName("menuReport");
		menuReportCreate.setText(LangModelAnalyse.Text("menuReportCreate"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.addActionListener(actionAdapter);
		menuReport.add(menuReportCreate);

		menuHelp.setText(LangModelPrediction.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModelPrediction.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModelPrediction.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModelPrediction.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModelPrediction.Text("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModelPrediction.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModelPrediction.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModelPrediction.Text("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModelPrediction.Text("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModelPrediction.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionOpen);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
//		menuSession.add(menuSessionSave);
//		menuSession.add(menuSessionUndo);
//		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewDataLoad);
		menuView.add(menuTraceAddCompare);
		menuView.addSeparator();
		menuView.add(menuTraceClose);
		menuView.add(menuTraceRemoveCompare);
		menuView.addSeparator();
		menuView.add(menuViewCountPrediction);
		menuView.add(menuViewSavePrediction);

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
		int count = e.length;
		int i;

		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionOpen.setVisible(aModel.isVisible("menuSessionOpen"));
		menuSessionOpen.setEnabled(aModel.isEnabled("menuSessionOpen"));
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
//		menuSessionSave.setVisible(aModel.isVisible("menuSessionSave"));
//		menuSessionSave.setEnabled(aModel.isEnabled("menuSessionSave"));
//		menuSessionUndo.setVisible(aModel.isVisible("menuSessionUndo"));
//		menuSessionUndo.setEnabled(aModel.isEnabled("menuSessionUndo"));
		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));
		menuViewDataLoad.setVisible(aModel.isVisible("menuViewDataLoad"));
		menuViewDataLoad.setEnabled(aModel.isEnabled("menuViewDataLoad"));
		menuViewCountPrediction.setVisible(aModel.isVisible("menuViewCountPrediction"));
		menuViewCountPrediction.setEnabled(aModel.isEnabled("menuViewCountPrediction"));

		menuViewSavePrediction.setVisible(aModel.isVisible("menuViewSavePrediction"));
		menuViewSavePrediction.setEnabled(aModel.isEnabled("menuViewSavePrediction"));

		menuTraceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
		menuTraceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
		menuTraceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));
		menuTraceClose.setVisible(aModel.isVisible("menuTraceClose"));
		menuTraceClose.setEnabled(aModel.isEnabled("menuTraceClose"));

		menuReport.setVisible(aModel.isVisible("menuReport"));
		menuReport.setEnabled(aModel.isEnabled("menuReport"));
		menuReportCreate.setVisible(aModel.isVisible("menuReportCreate"));
		menuReportCreate.setEnabled(aModel.isEnabled("menuReportCreate"));

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

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

class StatisticsMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	PredictionMenuBar adaptee;

	StatisticsMenuBar_this_actionAdapter(PredictionMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

