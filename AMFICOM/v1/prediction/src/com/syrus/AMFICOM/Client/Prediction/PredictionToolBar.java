package com.syrus.AMFICOM.Client.Prediction;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class PredictionToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton buttonOpenSession = new JButton();
//  JButton buttonCloseSession = new JButton();

	JButton loadStatistics = new JButton();
	JButton traceAddCompare = new JButton();
	JButton traceRemoveCompare = new JButton();
	JButton countPrediction = new JButton();
	JButton savePrediction = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;


	public PredictionToolBar()
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

	private void jbInit() throws Exception
	{
		AnalyseMainToolBar_this_actionAdapter actionAdapter =
				new AnalyseMainToolBar_this_actionAdapter(this);
		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		buttonOpenSession = new JButton();
		buttonOpenSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonOpenSession.setText("");
		buttonOpenSession.setMaximumSize(buttonSize);
		buttonOpenSession.setPreferredSize(buttonSize);
		buttonOpenSession.setToolTipText(LangModelPrediction.getString("menuSessionOpen"));
		buttonOpenSession.setName("menuSessionOpen");
		buttonOpenSession.addActionListener(actionAdapter);

//    buttonCloseSession = new JButton();
//    buttonCloseSession.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session2.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
//    buttonCloseSession.setText("");
//    buttonCloseSession.setMaximumSize(buttonSize);
//    buttonCloseSession.setPreferredSize(buttonSize);
//    buttonCloseSession.setToolTipText(LangModelPrediction.getString("menuSessionClose"));
//    buttonCloseSession.setName("menuSessionClose");
//    buttonCloseSession.addActionListener(actionAdapter);


		loadStatistics = new JButton();
		loadStatistics.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		loadStatistics.setText("");
		loadStatistics.setMaximumSize(buttonSize);
		loadStatistics.setPreferredSize(buttonSize);
		loadStatistics.setToolTipText(LangModelPrediction.getString("menuViewDataLoad"));
		loadStatistics.setName("menuViewDataLoad");
		loadStatistics.addActionListener(actionAdapter);

		traceAddCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_add.gif")));
		traceAddCompare.setMaximumSize(buttonSize);
		traceAddCompare.setPreferredSize(buttonSize);
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName("menuTraceAddCompare");
		traceAddCompare.addActionListener(actionAdapter);
		traceRemoveCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_remove.gif")));
		traceRemoveCompare.setMaximumSize(buttonSize);
		traceRemoveCompare.setPreferredSize(buttonSize);
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName("menuTraceRemoveCompare");
		traceRemoveCompare.addActionListener(actionAdapter);

		countPrediction = new JButton();
		countPrediction.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		countPrediction.setText("");
		countPrediction.setMaximumSize(buttonSize);
		countPrediction.setPreferredSize(buttonSize);
		countPrediction.setToolTipText(LangModelPrediction.getString("menuViewCountPrediction"));
		countPrediction.setName("menuViewCountPrediction");
		countPrediction.addActionListener(actionAdapter);

		savePrediction = new JButton();
		savePrediction.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		savePrediction.setText("");
		savePrediction.setMaximumSize(buttonSize);
		savePrediction.setPreferredSize(buttonSize);
		savePrediction.setToolTipText(LangModelPrediction.getString("menuViewSavePrediction"));
		savePrediction.setName("menuViewSavePrediction");
		savePrediction.addActionListener(actionAdapter);


		add(buttonOpenSession);
//    add(buttonCloseSession);
		addSeparator();
		add(loadStatistics);
		add(traceAddCompare);
		add(traceRemoveCompare);
		addSeparator();
		add(countPrediction);
		add(savePrediction);
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
		buttonOpenSession.setVisible(aModel.isVisible("menuSessionOpen"));
		buttonOpenSession.setEnabled(aModel.isEnabled("menuSessionOpen"));

//    buttonCloseSession.setVisible(aModel.isVisible("menuSessionClose"));
//    buttonCloseSession.setEnabled(aModel.isEnabled("menuSessionClose"));

		loadStatistics.setVisible(aModel.isVisible("menuViewDataLoad"));
		loadStatistics.setEnabled(aModel.isEnabled("menuViewDataLoad"));
		traceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
		traceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
		traceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
		traceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));

		countPrediction.setVisible(aModel.isVisible("menuViewCountPrediction"));
		countPrediction.setEnabled(aModel.isEnabled("menuViewCountPrediction"));

		savePrediction.setVisible(aModel.isVisible("menuViewSavePrediction"));
		savePrediction.setEnabled(aModel.isEnabled("menuViewSavePrediction"));
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
/*
	void attenuationButton_actionPerformed(ActionEvent e)
	{
		attenuationButton.setSelected(true);
		amplitudeButton.setSelected(false);
		reflectionButton.setSelected(false);
		anaType = "att";
	}

	void amplitudeButton_actionPerformed(ActionEvent e)
	{
		attenuationButton.setSelected(false);
		amplitudeButton.setSelected(true);
		reflectionButton.setSelected(false);
		anaType = "ampl";
	}

	void reflectionButton_actionPerformed(ActionEvent e)
	{
		attenuationButton.setSelected(false);
		amplitudeButton.setSelected(false);
		reflectionButton.setSelected(true);
		anaType = "refl";
	}

*/

}

class AnalyseMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	PredictionToolBar adaptee;

	AnalyseMainToolBar_this_actionAdapter(PredictionToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
