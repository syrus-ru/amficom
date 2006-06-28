
package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class AnalyseMainToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = -8757740087779429631L;

	public AnalyseMainToolBar() {
		this.initItems();
	}

	private void initItems() {
		final JButton traceDownload = new JButton();
		final JButton modelDownload = new JButton();
		final JButton traceAddCompare = new JButton();
		final JButton traceRemoveCompare = new JButton();

		final JButton buttonFileOpen = new JButton();
		final JButton fileAdd = new JButton();
		final JButton fileRemove = new JButton();

		final JButton buttonFileClose = new JButton();
		final JButton buttonExit = new JButton();

		final JButton checkMismatch = new JButton();

		traceDownload.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE));
		traceDownload.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceDownload.setToolTipText(LangModelAnalyse.getString("menuTraceDownload"));
		traceDownload.setName(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD);
		traceDownload.addActionListener(super.actionListener);
		
		modelDownload.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_MODEL));
		modelDownload.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		modelDownload.setToolTipText(LangModelAnalyse.getString("menuModelDownload"));
		modelDownload.setName(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD);
		modelDownload.addActionListener(super.actionListener);

		traceAddCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD));
		traceAddCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE);
		traceAddCompare.addActionListener(super.actionListener);

		traceRemoveCompare.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE));
		traceRemoveCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE);
		traceRemoveCompare.addActionListener(super.actionListener);

		buttonFileOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_FILE));
		buttonFileOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		buttonFileOpen.setToolTipText(LangModelAnalyse.getString("menuFileOpen"));
		buttonFileOpen.setName(AnalyseApplicationModel.MENU_FILE_OPEN);
		buttonFileOpen.addActionListener(super.actionListener);

		fileAdd.setIcon(UIManager.getIcon(ResourceKeys.ICON_ADD_FILE));
		fileAdd.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileAdd.setToolTipText(LangModelAnalyse.getString("menuFileAddCompare"));
		fileAdd.setName(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE);
		fileAdd.addActionListener(super.actionListener);

		fileRemove.setIcon(UIManager.getIcon(ResourceKeys.ICON_REMOVE_FILE));
		fileRemove.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		fileRemove.setToolTipText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		fileRemove.setName(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE);
		fileRemove.addActionListener(super.actionListener);

		checkMismatch.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_CHECK_MISMATCH));
		checkMismatch.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		checkMismatch.addActionListener(super.actionListener);
		checkMismatch.setName(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH);
		checkMismatch.setToolTipText(LangModelAnalyse.getString("commandCheckMismatch"));

		addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}

			public void modelChanged(String e[]) {
				ApplicationModel aModel = AnalyseMainToolBar.this.getApplicationModel();

				buttonFileOpen.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_FILE_OPEN));
				buttonFileOpen.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_FILE_OPEN));
				
				traceDownload.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD));
				traceDownload.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_DOWNLOAD));
				modelDownload.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD));
				modelDownload.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_MODELING_DOWNLOAD));
				traceAddCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				traceAddCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				traceRemoveCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				traceRemoveCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));

				fileAdd.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE));
				fileAdd.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_FILE_ADD_COMPARE));
				fileRemove.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE));
				fileRemove.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_FILE_REMOVE_COMPARE));

				buttonFileClose.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_FILE_CLOSE));
				buttonFileClose.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE));
				buttonExit.setVisible(aModel.isVisible(ApplicationModel.MENU_EXIT));
				buttonExit.setEnabled(aModel.isEnabled(ApplicationModel.MENU_EXIT));

				checkMismatch.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH));
				checkMismatch.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_CHECK_MISMATCH));
			}
		});

		super.addSeparator();
		super.add(buttonFileOpen);
		super.add(fileAdd);
		super.add(fileRemove);
		super.addSeparator();
		super.add(traceDownload);
		super.add(modelDownload);
		super.add(traceAddCompare);
		super.add(traceRemoveCompare);
		if (checkMismatch != null) {
			super.addSeparator();
			super.add(checkMismatch);
		}
	}
}
