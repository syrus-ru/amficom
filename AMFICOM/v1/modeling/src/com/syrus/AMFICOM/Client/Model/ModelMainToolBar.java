package com.syrus.AMFICOM.Client.Model;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

public class ModelMainToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
//  JButton sessionClose= new JButton();

	JButton menuViewMapOpen = new JButton();
	JButton menuViewSchemeOpen = new JButton();
	JButton menuViewModelLoad = new JButton();

	JButton traceAddCompare = new JButton();
	JButton traceRemoveCompare = new JButton();
	JButton buttonFileOpen = new JButton();
	JButton fileAdd = new JButton();
	JButton fileRemove = new JButton();
	JButton saveModel = new JButton();
	JButton performModeling = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public ModelMainToolBar()
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
		ModelMainToolBar_this_actionAdapter actionAdapter =
				new ModelMainToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModelModel.getString("menuSessionOpen"));
		sessionOpen.setName("menuSessionOpen");
		sessionOpen.addActionListener(actionAdapter);

//    sessionClose.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/close_session2.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
//    sessionClose.setMaximumSize(buttonSize);
//    sessionClose.setPreferredSize(buttonSize);
//    sessionClose.setToolTipText(LangModelModel.getString("menuSessionClose"));
//    sessionClose.setName("menuSessionClose");
//    sessionClose.addActionListener(actionAdapter);

		menuViewMapOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif")
				.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewMapOpen.setMaximumSize(buttonSize);
		menuViewMapOpen.setPreferredSize(buttonSize);
		menuViewMapOpen.setToolTipText(LangModelModel.getString("menuViewMapOpen"));
		menuViewMapOpen.setName("menuViewMapOpen");
		menuViewMapOpen.addActionListener(actionAdapter);

		menuViewModelLoad.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_model.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewModelLoad.setMaximumSize(buttonSize);
		menuViewModelLoad.setPreferredSize(buttonSize);
		menuViewModelLoad.setToolTipText(LangModelModel.getString("menuViewModelLoad"));
		menuViewModelLoad.setName("menuViewModelLoad");
		menuViewModelLoad.addActionListener(actionAdapter);

		menuViewSchemeOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		menuViewSchemeOpen.setMaximumSize(buttonSize);
		menuViewSchemeOpen.setPreferredSize(buttonSize);
		menuViewSchemeOpen.setToolTipText(LangModelModel.getString("menuViewSchemeOpen"));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(actionAdapter);

		traceAddCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_add.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		traceAddCompare.setMaximumSize(buttonSize);
		traceAddCompare.setPreferredSize(buttonSize);
		traceAddCompare.setToolTipText(LangModelAnalyse.ToolTip("menuTraceAddCompare"));
		traceAddCompare.setName("menuTraceAddCompare");
		traceAddCompare.addActionListener(actionAdapter);

		traceRemoveCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_remove.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		traceRemoveCompare.setMaximumSize(buttonSize);
		traceRemoveCompare.setPreferredSize(buttonSize);
		traceRemoveCompare.setToolTipText(LangModelAnalyse.ToolTip("menuTraceRemoveCompare"));
		traceRemoveCompare.setName("menuTraceRemoveCompare");
		traceRemoveCompare.addActionListener(actionAdapter);

		buttonFileOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/openfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		buttonFileOpen.setMaximumSize(buttonSize);
		buttonFileOpen.setPreferredSize(buttonSize);
		buttonFileOpen.setToolTipText(LangModelAnalyse.ToolTip("menuFileOpen"));
		buttonFileOpen.setName("menuFileOpen");
		buttonFileOpen.addActionListener(actionAdapter);

		fileAdd.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addfile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		fileAdd.setMaximumSize(buttonSize);
		fileAdd.setPreferredSize(buttonSize);
		fileAdd.setToolTipText(LangModelAnalyse.ToolTip("menuFileAddCompare"));
		fileAdd.setName("menuFileAddCompare");
		fileAdd.addActionListener(actionAdapter);

		fileRemove.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/removefile.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		fileRemove.setMaximumSize(buttonSize);
		fileRemove.setPreferredSize(buttonSize);
		fileRemove.setToolTipText(LangModelAnalyse.ToolTip("menuFileRemoveCompare"));
		fileRemove.setName("menuFileRemoveCompare");
		fileRemove.addActionListener(actionAdapter);

		saveModel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		saveModel.setMaximumSize(buttonSize);
		saveModel.setPreferredSize(buttonSize);
		saveModel.setToolTipText(LangModelModel.getString("menuViewModelSave"));
		saveModel.setName("menuViewModelSave");
		saveModel.addActionListener(actionAdapter);

		performModeling.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		performModeling.setMaximumSize(buttonSize);
		performModeling.setPreferredSize(buttonSize);
		performModeling.setToolTipText(LangModelModel.getString("menuViewPerformModeling"));
		performModeling.setName("menuViewPerformModeling");
		performModeling.addActionListener(actionAdapter);

		add(sessionOpen);
//    add(sessionClose);
		addSeparator();
		add(menuViewMapOpen);
		add(menuViewSchemeOpen);
		addSeparator();
		add(buttonFileOpen);
		add(fileAdd);
		add(fileRemove);
		addSeparator();
		add(menuViewModelLoad);
		add(traceAddCompare);
		add(traceRemoveCompare);
		addSeparator();
		add(performModeling);
		add(saveModel);

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
		sessionOpen.setVisible(aModel.isVisible("menuSessionOpen"));
		sessionOpen.setEnabled(aModel.isEnabled("menuSessionOpen"));
//    sessionClose.setVisible(aModel.isVisible("menuSessionClose"));
//    sessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuViewMapOpen.setVisible(aModel.isVisible("menuViewMapOpen"));
		menuViewMapOpen.setEnabled(aModel.isEnabled("menuViewMapOpen"));
		menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
		menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
		menuViewModelLoad.setVisible(aModel.isVisible("menuViewModelLoad"));
		menuViewModelLoad.setEnabled(aModel.isEnabled("menuViewModelLoad"));
		traceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
		traceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
		traceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
		traceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));
		buttonFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
		buttonFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));
		fileAdd.setEnabled(aModel.isEnabled("menuFileAddCompare"));
		fileAdd.setVisible(aModel.isVisible("menuFileAddCompare"));
		fileRemove.setEnabled(aModel.isEnabled("menuFileRemoveCompare"));
		fileRemove.setVisible(aModel.isVisible("menuFileRemoveCompare"));
		saveModel.setVisible(aModel.isVisible("menuViewModelSave"));
		saveModel.setEnabled(aModel.isEnabled("menuViewModelSave"));
		performModeling.setVisible(aModel.isVisible("menuViewPerformModeling"));
		performModeling.setEnabled(aModel.isEnabled("menuViewPerformModeling"));
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

class ModelMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ModelMainToolBar adaptee;

	ModelMainToolBar_this_actionAdapter(ModelMainToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
