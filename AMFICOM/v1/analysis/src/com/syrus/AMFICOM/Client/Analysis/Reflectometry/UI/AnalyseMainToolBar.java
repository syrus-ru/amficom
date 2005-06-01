package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class AnalyseMainToolBar extends AbstractMainToolBar 
{
//	private ApplicationModel aModel;
//
//	private JButton sessionOpen = new JButton();
	private JButton traceDownload = new JButton();
	private JButton traceAddCompare = new JButton();
	private JButton traceRemoveCompare = new JButton();

	private JButton buttonFileOpen = new JButton();
	private JButton fileAdd = new JButton();
	private JButton fileRemove = new JButton();

	private JButton buttonFileClose = new JButton();
	private JButton buttonExit = new JButton();

//	public final static int img_siz = 16;

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
//		AnalyseMainToolBar_this_actionAdapter actionAdapter =
//				new AnalyseMainToolBar_this_actionAdapter(this);

//		this.sessionOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
//		this.sessionOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
//		this.sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
//		this.sessionOpen.setName("menuSessionNew");
//		this.sessionOpen.addActionListener(actionAdapter);
		
		this.traceDownload.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE));
		this.traceDownload.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.traceDownload.setToolTipText(LangModelAnalyse.getString("menuTraceDownload"));
		this.traceDownload.setName("menuTraceDownload");
		this.traceDownload.addActionListener(super.actionListener);
		
		this.traceAddCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD));
		this.traceAddCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		this.traceAddCompare.setName("menuTraceAddCompare");
		this.traceAddCompare.addActionListener(super.actionListener);
		
		this.traceRemoveCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE ));
		this.traceRemoveCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		this.traceRemoveCompare.setName("menuTraceRemoveCompare");
		this.traceRemoveCompare.addActionListener(super.actionListener);


		this.buttonFileOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_FILE));
		this.buttonFileOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		this.buttonFileOpen.setName("menuFileOpen");
		this.buttonFileOpen.addActionListener(super.actionListener);
		this.fileAdd.setIcon(UIManager.getIcon(ResourceKeys.ICON_ADD_FILE));
		this.fileAdd.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		this.fileAdd.setName("menuFileAddCompare");
		this.fileAdd.addActionListener(super.actionListener);
		this.fileRemove.setIcon(UIManager.getIcon(ResourceKeys.ICON_REMOVE_FILE));
		this.fileRemove.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		this.fileRemove.setName("menuFileRemoveCompare");
		this.fileRemove.addActionListener(super.actionListener);

//		super.add(this.sessionOpen);
		super.addSeparator();
		super.add(this.buttonFileOpen);
		super.add(this.fileAdd);
		super.add(this.fileRemove);
		super.addSeparator();
		super.add(this.traceDownload);
		super.add(this.traceAddCompare);
		super.add(this.traceRemoveCompare);
	}

	public void setModel(ApplicationModel a)
	{
		this.aModel = a;
	}

	public ApplicationModel getModel()
	{
		return this.aModel;
	}

	public void modelChanged(String e) {
		modelChanged(new String[] {e});
	}
	
	public void modelChanged(String e[])
	{
		this.buttonFileOpen.setVisible(true);//this.aModel.isVisible("menuFileOpen"));
		this.buttonFileOpen.setEnabled(true);//this.aModel.isEnabled("menuFileOpen"));

//		this.sessionOpen.setVisible(this.aModel.isVisible("menuSessionNew"));
//		this.sessionOpen.setEnabled(this.aModel.isEnabled("menuSessionNew"));
		this.traceDownload.setVisible(this.aModel.isVisible("menuTraceDownload"));
		this.traceDownload.setEnabled(this.aModel.isEnabled("menuTraceDownload"));
		this.traceAddCompare.setVisible(this.aModel.isVisible("menuTraceAddCompare"));
		this.traceAddCompare.setEnabled(this.aModel.isEnabled("menuTraceAddCompare"));
		this.traceRemoveCompare.setVisible(this.aModel.isVisible("menuTraceRemoveCompare"));
		this.traceRemoveCompare.setEnabled(this.aModel.isEnabled("menuTraceRemoveCompare"));

		this.fileAdd.setEnabled(this.aModel.isEnabled("menuFileAddCompare"));
		this.fileAdd.setVisible(this.aModel.isVisible("menuFileAddCompare"));
		this.fileRemove.setEnabled(this.aModel.isEnabled("menuFileRemoveCompare"));
		this.fileRemove.setVisible(this.aModel.isVisible("menuFileRemoveCompare"));

		this.buttonFileClose.setVisible(this.aModel.isVisible("menuFileClose"));
		this.buttonFileClose.setEnabled(this.aModel.isEnabled("menuFileClose"));
		this.buttonExit.setVisible(this.aModel.isVisible("menuExit"));
		this.buttonExit.setEnabled(this.aModel.isEnabled("menuExit"));
	}

}
