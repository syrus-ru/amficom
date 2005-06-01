
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class AnalyseMainToolBar extends AbstractMainToolBar {

	public AnalyseMainToolBar() {
		this.initItems();
	}

	private void initItems() {
		final JButton traceDownload = new JButton();
		final JButton traceAddCompare = new JButton();
		final JButton traceRemoveCompare = new JButton();

		final JButton buttonFileOpen = new JButton();
		final JButton fileAdd = new JButton();
		final JButton fileRemove = new JButton();

		final JButton buttonFileClose = new JButton();
		final JButton buttonExit = new JButton();
		traceDownload.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE));
		traceDownload.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceDownload.setToolTipText(LangModelAnalyse.getString("menuTraceDownload"));
		traceDownload.setName("menuTraceDownload");
		traceDownload.addActionListener(super.actionListener);

		traceAddCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD));
		traceAddCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName("menuTraceAddCompare");
		traceAddCompare.addActionListener(super.actionListener);

		traceRemoveCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE));
		traceRemoveCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName("menuTraceRemoveCompare");
		traceRemoveCompare.addActionListener(super.actionListener);

		buttonFileOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_FILE));
		buttonFileOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		buttonFileOpen.setName("menuFileOpen");
		buttonFileOpen.addActionListener(super.actionListener);
		fileAdd.setIcon(UIManager.getIcon(ResourceKeys.ICON_ADD_FILE));
		fileAdd.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		fileAdd.setName("menuFileAddCompare");
		fileAdd.addActionListener(super.actionListener);
		fileRemove.setIcon(UIManager.getIcon(ResourceKeys.ICON_REMOVE_FILE));
		fileRemove.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		fileRemove.setName("menuFileRemoveCompare");
		fileRemove.addActionListener(super.actionListener);

		addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}

			public void modelChanged(String e[]) {
				buttonFileOpen.setVisible(true);
				buttonFileOpen.setEnabled(true);

				ApplicationModel aModel = AnalyseMainToolBar.this.getApplicationModel();

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
		});

		super.addSeparator();
		super.add(buttonFileOpen);
		super.add(fileAdd);
		super.add(fileRemove);
		super.addSeparator();
		super.add(traceDownload);
		super.add(traceAddCompare);
		super.add(traceRemoveCompare);
	}

}
