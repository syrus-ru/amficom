package com.syrus.AMFICOM.Client.Survey;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class SurveyToolBar extends JToolBar implements ApplicationModelListener
{
	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
	JButton menuViewMapOpen = new JButton();
	JButton menuViewSchemeOpen = new JButton();
	JButton menuViewArchive = new JButton();
	JButton menuViewAlarms = new JButton();
	JButton menuViewResult = new JButton();
	JButton menuEvaluateScheduler = new JButton();
	JButton menuEvaluateAnalize = new JButton();
	JButton menuEvaluatePrognosis = new JButton();
	JButton menuEvaluateAnalizeExt = new JButton();
	JButton menuEvaluateNorms = new JButton();

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
		SurveyToolBar_this_actionAdapter actionAdapter =
				new SurveyToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);

		menuViewMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuViewMapOpen.setMaximumSize(buttonSize);
		menuViewMapOpen.setPreferredSize(buttonSize);
		menuViewMapOpen.setToolTipText(LangModelSurvey.getString("Topological_scheme_of_net"));
		menuViewMapOpen.setName("menuVisualizeNetGIS");
		menuViewMapOpen.addActionListener(actionAdapter);

		menuViewSchemeOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").
				getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewSchemeOpen.setMaximumSize(buttonSize);
		menuViewSchemeOpen.setPreferredSize(buttonSize);
		menuViewSchemeOpen.setToolTipText(LangModelSurvey.getString("Net_scheme"));
		menuViewSchemeOpen.setName("menuVisualizeNet");
		menuViewSchemeOpen.addActionListener(actionAdapter);

		menuViewArchive.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/archive_mini.gif").
                                getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));  
		menuViewArchive.setMaximumSize(buttonSize);
		menuViewArchive.setPreferredSize(buttonSize);
		menuViewArchive.setToolTipText(LangModelSurvey.getString("Measurement_archive"));
		menuViewArchive.setName("menuEvaluateArchive");
		menuViewArchive.addActionListener(actionAdapter);

		menuViewAlarms.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/alarm_bell_red.gif").
                                getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));  
		menuViewAlarms.setMaximumSize(buttonSize);
		menuViewAlarms.setPreferredSize(buttonSize);
		menuViewAlarms.setToolTipText(LangModelSurvey.getString("Alarm_signals"));
		menuViewAlarms.setName("menuMaintainAlarm");
		menuViewAlarms.addActionListener(actionAdapter);

		menuViewResult.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/result.gif").
                                getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));  
		menuViewResult.setMaximumSize(buttonSize);
		menuViewResult.setPreferredSize(buttonSize);
		menuViewResult.setToolTipText(LangModelSurvey.getString("Results_overview"));
		menuViewResult.setName("menuEvaluateResult");
		menuViewResult.addActionListener(actionAdapter);

		menuEvaluateScheduler.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/scheduling_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuEvaluateScheduler.setMaximumSize(buttonSize);
		menuEvaluateScheduler.setPreferredSize(buttonSize);
		menuEvaluateScheduler.setToolTipText(LangModelSurvey.getString("Scheduling"));
		menuEvaluateScheduler.setName("menuEvaluateScheduler");
		menuEvaluateScheduler.addActionListener(actionAdapter);

		menuEvaluateAnalize.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/analyse_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuEvaluateAnalize.setMaximumSize(buttonSize);
		menuEvaluateAnalize.setPreferredSize(buttonSize);
		menuEvaluateAnalize.setToolTipText(LangModelSurvey.getString("Evaluation_analysis"));
		menuEvaluateAnalize.setName("menuEvaluateAnalize");
		menuEvaluateAnalize.addActionListener(actionAdapter);

		menuEvaluateAnalizeExt.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/survey_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuEvaluateAnalizeExt.setMaximumSize(buttonSize);
		menuEvaluateAnalizeExt.setPreferredSize(buttonSize);
		menuEvaluateAnalizeExt.setToolTipText(LangModelSurvey.getString("Researching"));
		menuEvaluateAnalizeExt.setName("menuEvaluateAnalizeExt");
		menuEvaluateAnalizeExt.addActionListener(actionAdapter);

		menuEvaluateNorms.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/evaluation_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuEvaluateNorms.setMaximumSize(buttonSize);
		menuEvaluateNorms.setPreferredSize(buttonSize);
		menuEvaluateNorms.setToolTipText(LangModelSurvey.getString("Evaluation"));
		menuEvaluateNorms.setName("menuEvaluateNorms");
		menuEvaluateNorms.addActionListener(actionAdapter);

		menuEvaluatePrognosis.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif").
				getScaledInstance(img_siz, img_siz, Image.SCALE_DEFAULT)));
		menuEvaluatePrognosis.setMaximumSize(buttonSize);
		menuEvaluatePrognosis.setPreferredSize(buttonSize);
		menuEvaluatePrognosis.setToolTipText(LangModelSurvey.getString("Forecasting"));
		menuEvaluatePrognosis.setName("menuEvaluatePrognosis");
		menuEvaluatePrognosis.addActionListener(actionAdapter);

		add(sessionOpen);
		addSeparator();
		add(menuViewMapOpen);
		add(menuViewSchemeOpen);
		add(menuViewArchive);
		addSeparator();
		add(menuViewAlarms);
		add(menuViewResult);
		addSeparator();
		add(menuEvaluateScheduler);
		add(menuEvaluateAnalize);
		add(menuEvaluateAnalizeExt);
		add(menuEvaluateNorms);
		add(menuEvaluatePrognosis);
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
		sessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		sessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuViewMapOpen.setVisible(aModel.isVisible("menuVisualizeNet"));
		menuViewMapOpen.setEnabled(aModel.isEnabled("menuVisualizeNet"));
		menuViewSchemeOpen.setVisible(aModel.isVisible("menuVisualizeNetGIS"));
		menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuVisualizeNetGIS"));
		menuViewArchive.setVisible(aModel.isVisible("menuEvaluateArchive"));
		menuViewArchive.setEnabled(aModel.isEnabled("menuEvaluateArchive"));
		menuViewAlarms.setVisible(aModel.isVisible("menuMaintainAlarm"));
		menuViewAlarms.setEnabled(aModel.isEnabled("menuMaintainAlarm"));
		menuViewResult.setVisible(aModel.isVisible("menuEvaluateResult"));
		menuViewResult.setEnabled(aModel.isEnabled("menuEvaluateResult"));
		menuEvaluateScheduler.setVisible(aModel.isVisible("menuEvaluateScheduler"));
		menuEvaluateScheduler.setEnabled(aModel.isEnabled("menuEvaluateScheduler"));
		menuEvaluateAnalize.setVisible(aModel.isVisible("menuEvaluateAnalize"));
		menuEvaluateAnalize.setEnabled(aModel.isEnabled("menuEvaluateAnalize"));
		menuEvaluateAnalizeExt.setVisible(aModel.isVisible("menuEvaluateAnalizeExt"));
		menuEvaluateAnalizeExt.setEnabled(aModel.isEnabled("menuEvaluateAnalizeExt"));
		menuEvaluateNorms.setVisible(aModel.isVisible("menuEvaluateNorms"));
		menuEvaluateNorms.setEnabled(aModel.isEnabled("menuEvaluateNorms"));
		menuEvaluatePrognosis.setVisible(aModel.isVisible("menuEvaluatePrognosis"));
		menuEvaluatePrognosis.setEnabled(aModel.isEnabled("menuEvaluatePrognosis"));
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

class SurveyToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	SurveyToolBar adaptee;

	SurveyToolBar_this_actionAdapter(SurveyToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		System.out.println("SurveyToolBar: actionPerformed");
		adaptee.this_actionPerformed(e);
	}
}
