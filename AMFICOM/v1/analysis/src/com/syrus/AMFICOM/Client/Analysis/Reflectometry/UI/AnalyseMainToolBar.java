package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

public class AnalyseMainToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton sessionOpen = new JButton();
	JButton traceDownload = new JButton();
	JButton traceAddCompare = new JButton();
	JButton traceRemoveCompare = new JButton();

	JButton buttonFileOpen = new JButton();
	JButton fileAdd = new JButton();
	JButton fileRemove = new JButton();

	JButton buttonFileClose = new JButton();
	JButton buttonExit = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public AnalyseMainToolBar()
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

		sessionOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		sessionOpen.setMaximumSize(buttonSize);
		sessionOpen.setPreferredSize(buttonSize);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(actionAdapter);
		traceDownload.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE));
		traceDownload.setMaximumSize(buttonSize);
		traceDownload.setPreferredSize(buttonSize);
		traceDownload.setToolTipText(LangModelAnalyse.getString("menuTraceDownload"));
		traceDownload.setName("menuTraceDownload");
		traceDownload.addActionListener(actionAdapter);
		traceAddCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD));
		traceAddCompare.setMaximumSize(buttonSize);
		traceAddCompare.setPreferredSize(buttonSize);
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName("menuTraceAddCompare");
		traceAddCompare.addActionListener(actionAdapter);
		traceRemoveCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE ));
		traceRemoveCompare.setMaximumSize(buttonSize);
		traceRemoveCompare.setPreferredSize(buttonSize);
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName("menuTraceRemoveCompare");
		traceRemoveCompare.addActionListener(actionAdapter);


		buttonFileOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_FILE));
		buttonFileOpen.setMaximumSize(buttonSize);
		buttonFileOpen.setPreferredSize(buttonSize);
		buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		buttonFileOpen.setName("menuFileOpen");
		buttonFileOpen.addActionListener(actionAdapter);
		fileAdd.setIcon(UIManager.getIcon(ResourceKeys.ICON_ADD_FILE));
		fileAdd.setMaximumSize(buttonSize);
		fileAdd.setPreferredSize(buttonSize);
		fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		fileAdd.setName("menuFileAddCompare");
		fileAdd.addActionListener(actionAdapter);
		fileRemove.setIcon(UIManager.getIcon(ResourceKeys.ICON_REMOVE_FILE));
		fileRemove.setMaximumSize(buttonSize);
		fileRemove.setPreferredSize(buttonSize);
		fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		fileRemove.setName("menuFileRemoveCompare");
		fileRemove.addActionListener(actionAdapter);

		add(sessionOpen);
		addSeparator();
		add(buttonFileOpen);
		add(fileAdd);
		add(fileRemove);
		addSeparator();
		add(traceDownload);
		add(traceAddCompare);
		add(traceRemoveCompare);
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
		buttonFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
		buttonFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));

		sessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		sessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));
		traceDownload.setVisible(aModel.isVisible("menuTraceDownload"));
		traceDownload.setEnabled(aModel.isEnabled("menuTraceDownload"));
		traceAddCompare.setVisible(aModel.isVisible("menuTraceAddCompare"));
		traceAddCompare.setEnabled(aModel.isEnabled("menuTraceAddCompare"));
		traceRemoveCompare.setVisible(aModel.isVisible("menuTraceRemoveCompare"));
		traceRemoveCompare.setEnabled(aModel.isEnabled("menuTraceRemoveCompare"));

		fileAdd.setEnabled(aModel.isEnabled("menuFileAddCompare"));
		fileAdd.setVisible(aModel.isVisible("menuFileAddCompare"));
		fileRemove.setEnabled(aModel.isEnabled("menuFileRemoveCompare"));
		fileRemove.setVisible(aModel.isVisible("menuFileRemoveCompare"));

		buttonFileClose.setVisible(aModel.isVisible("menuFileClose"));
		buttonFileClose.setEnabled(aModel.isEnabled("menuFileClose"));
		buttonExit.setVisible(aModel.isVisible("menuExit"));
		buttonExit.setEnabled(aModel.isEnabled("menuExit"));
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

class AnalyseMainToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	AnalyseMainToolBar adaptee;

	AnalyseMainToolBar_this_actionAdapter(AnalyseMainToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}