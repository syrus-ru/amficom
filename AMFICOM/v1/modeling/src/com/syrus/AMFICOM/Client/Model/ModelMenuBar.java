package com.syrus.AMFICOM.Client.Model;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI.AnalyseMainMenuBar;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelModel;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ModelApplicationModel;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.I18N;

public class ModelMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = 2888050801820884065L;

	public ModelMenuBar(ApplicationModel aModel) {
		super(aModel);
	}
	
	@Override
	protected void addMenuItems() {
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewMapViewOpen = new JMenuItem();
		final JMenuItem menuViewSchemeOpen = new JMenuItem();
		
		final JMenu menuFile = new JMenu();
		final JMenuItem menuFileOpen = new JMenuItem();
		final JMenuItem menuFileOpenAs = new JMenu();
		final JMenuItem menuFileOpenAsBellcore = new JMenuItem();
		final JMenuItem menuFileOpenAsWavetek = new JMenuItem();
		final JMenuItem menuFileSave = new JMenuItem();
		final JMenuItem menuFileSaveAs = new JMenu();
		final JMenuItem menuFileSaveAsText = new JMenuItem();
		final JMenuItem menuFileClose = new JMenuItem();
		final JMenuItem menuFileAddCompare = new JMenuItem();
		final JMenuItem menuFileRemoveCompare = new JMenuItem();
		
		final JMenu menuTrace = new JMenu();
		final JMenuItem menuViewModelLoad = new JMenuItem();
		final JMenuItem menuTraceAddCompare = new JMenuItem();
		final JMenuItem menuTraceRemoveCompare = new JMenuItem();
		final JMenuItem menuTraceClose = new JMenuItem();
		final JMenuItem menuViewModelSave = new JMenuItem();
		
		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();
		
		final JMenu menuWindow = new JMenu();
		final JMenuItem	menuWindowArrange	= new JMenuItem();
		final JMenuItem menuWindowTraceSelector = new JMenuItem();
		final JMenuItem menuWindowPrimaryParameters = new JMenuItem();
		final JMenuItem menuWindowAnalysis = new JMenuItem();
		final JMenuItem menuWindowTransData = new JMenuItem();
		final JMenuItem menuWindowModelRapameters = new JMenuItem();
		final JMenuItem menuWindowScheme = new JMenuItem();
		final JMenuItem menuWindowMap = new JMenuItem();
		final JMenuItem menuWindowGeneralProperties = new JMenuItem();
		final JMenuItem menuWindowCharacteristics = new JMenuItem();
		final JMenuItem menuWindowAdditionalProperties = new JMenuItem();
		
		menuView.setText(I18N.getString(ModelApplicationModel.MENU_APPEARANCE));
		menuView.setName("menuView");
		menuViewModelSave.setText(LangModelModel.getString("menuViewModelSave"));
		menuViewModelSave.setName("menuViewModelSave");
		menuViewModelSave.addActionListener(super.actionAdapter);
		menuViewModelLoad.setText(LangModelModel.getString("menuViewModelLoad"));
		menuViewModelLoad.setName("menuViewModelLoad");
		menuViewModelLoad.addActionListener(super.actionAdapter);

		menuViewMapViewOpen.setText(I18N.getString(ModelApplicationModel.MENU_APPEARANCE_MAP));
		menuViewMapViewOpen.setName("menuViewMapViewOpen");
		menuViewMapViewOpen.addActionListener(super.actionAdapter);
		menuViewSchemeOpen.setText(I18N.getString(ModelApplicationModel.MENU_APPEARANCE_SCHEME));
		menuViewSchemeOpen.setName("menuViewSchemeOpen");
		menuViewSchemeOpen.addActionListener(super.actionAdapter);
		
		menuFile.setText(LangModelAnalyse.getString("menuFile"));
		menuFile.setName("menuFile");
		menuFileOpen.setText(LangModelAnalyse.getString("menuFileOpen"));
		menuFileOpen.setName("menuFileOpen");
		menuFileOpen.addActionListener(super.actionAdapter);
		menuFileOpenAs.setText(LangModelAnalyse.getString("menuFileOpenAs"));
		menuFileOpenAs.setName("menuFileOpenAs");
		menuFileOpenAs.addActionListener(super.actionAdapter);
		menuFileOpenAsBellcore.setText(LangModelAnalyse.getString("menuFileOpenAsBellcore"));
		menuFileOpenAsBellcore.setName("menuFileOpenAsBellcore");
		menuFileOpenAsBellcore.addActionListener(super.actionAdapter);
		menuFileOpenAsWavetek.setText(LangModelAnalyse.getString("menuFileOpenAsWavetek"));
		menuFileOpenAsWavetek.setName("menuFileOpenAsWavetek");
		menuFileOpenAsWavetek.addActionListener(super.actionAdapter);
		menuFileSave.setText(LangModelAnalyse.getString("menuFileSave"));
		menuFileSave.setName("menuFileSave");
		menuFileSave.addActionListener(super.actionAdapter);
		menuFileSaveAs.setText(LangModelAnalyse.getString("menuFileSaveAs"));
		menuFileSaveAs.setName("menuFileSaveAs");
		menuFileSaveAsText.setText(LangModelAnalyse.getString("menuFileSaveAsText"));
		menuFileSaveAsText.setName("menuFileSaveAsText");
		menuFileSaveAsText.addActionListener(super.actionAdapter);
		menuFileClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuFileClose.setName("menuFileClose");
		menuFileClose.addActionListener(super.actionAdapter);
		menuFileAddCompare.setText(LangModelAnalyse.getString("menuFileAddCompare"));
		menuFileAddCompare.setName("menuFileAddCompare");
		menuFileAddCompare.addActionListener(super.actionAdapter);
		menuFileRemoveCompare.setText(LangModelAnalyse.getString("menuFileRemoveCompare"));
		menuFileRemoveCompare.setName("menuFileRemoveCompare");
		menuFileRemoveCompare.addActionListener(super.actionAdapter);
		menuTrace.setText(LangModelAnalyse.getString("menuTrace"));
		menuTrace.setName("menuTrace");
		menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		menuTraceAddCompare.setName("menuTraceAddCompare");
		menuTraceAddCompare.addActionListener(super.actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName("menuTraceRemoveCompare");
		menuTraceRemoveCompare.addActionListener(super.actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuTraceClose.setName("menuFileClose");
		menuTraceClose.addActionListener(super.actionAdapter);
		
		menuReport.setText(LangModelAnalyse.getString("menuReport"));
		menuReport.setName("menuReport");
		menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		menuReportCreate.setName("menuReportCreate");
		menuReportCreate.addActionListener(super.actionAdapter);
				
		menuWindow.setText(I18N.getString(ApplicationModel.MENU_VIEW));
		menuWindow.setName(ApplicationModel.MENU_VIEW);
		menuWindowArrange.setText(I18N.getString(ApplicationModel.MENU_VIEW_ARRANGE));
		menuWindowArrange.setName(ApplicationModel.MENU_VIEW_ARRANGE);
		menuWindowArrange.addActionListener(this.actionAdapter);
		menuWindowAdditionalProperties.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES));
		menuWindowAdditionalProperties.setName(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES);
		menuWindowAdditionalProperties.addActionListener(super.actionAdapter);
		menuWindowAnalysis.setText(I18N.getString(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS));
		menuWindowAnalysis.setName(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS);
		menuWindowAnalysis.addActionListener(super.actionAdapter);
		menuWindowCharacteristics.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS));
		menuWindowCharacteristics.setName(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS);
		menuWindowCharacteristics.addActionListener(super.actionAdapter);
		menuWindowGeneralProperties.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES));
		menuWindowGeneralProperties.setName(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES);
		menuWindowGeneralProperties.addActionListener(super.actionAdapter);
		menuWindowMap.setText(I18N.getString(MapEditorApplicationModel.ITEM_VIEW_MAP));
		menuWindowMap.setName(MapEditorApplicationModel.ITEM_VIEW_MAP);
		menuWindowMap.addActionListener(super.actionAdapter);
		menuWindowModelRapameters.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS));
		menuWindowModelRapameters.setName(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS);
		menuWindowModelRapameters.addActionListener(super.actionAdapter);
		menuWindowPrimaryParameters.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_TREE));
		menuWindowPrimaryParameters.setName(ModelApplicationModel.MENU_WINDOW_TREE);
		menuWindowPrimaryParameters.addActionListener(super.actionAdapter);
		menuWindowScheme.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_SCHEME));
		menuWindowScheme.setName(ModelApplicationModel.MENU_WINDOW_SCHEME);
		menuWindowScheme.addActionListener(super.actionAdapter);
		menuWindowTraceSelector.setText(I18N.getString(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR));
		menuWindowTraceSelector.setName(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR);
		menuWindowTraceSelector.addActionListener(super.actionAdapter);
		menuWindowTransData.setText(I18N.getString(ModelApplicationModel.MENU_WINDOW_TRANS_DATA));
		menuWindowTransData.setName(ModelApplicationModel.MENU_WINDOW_TRANS_DATA);
		menuWindowTransData.addActionListener(super.actionAdapter);
		
		menuView.add(menuViewMapViewOpen);
		menuView.add(menuViewSchemeOpen);
		
		menuFile.add(menuFileOpen);
		menuFile.add(menuFileOpenAs);
		menuFileOpenAs.add(menuFileOpenAsBellcore);
		menuFileOpenAs.add(menuFileOpenAsWavetek);
		menuFile.add(menuFileAddCompare);
		menuFile.addSeparator();
		menuFile.add(menuFileClose);
		menuFile.add(menuFileRemoveCompare);
		menuFile.addSeparator();
		menuFile.add(menuFileSave);
		menuFile.add(menuFileSaveAs);
		menuFileSaveAs.add(menuFileSaveAsText);
		
		menuTrace.add(menuViewModelLoad);
		menuTrace.add(menuTraceAddCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuTraceClose);
		menuTrace.add(menuTraceRemoveCompare);
		menuTrace.addSeparator();
		menuTrace.add(menuViewModelSave);
		
		menuReport.add(menuReportCreate);
		
		menuWindow.add(menuWindowArrange);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowAnalysis);
		menuWindow.add(menuWindowTraceSelector);
		menuWindow.add(menuWindowPrimaryParameters);
		menuWindow.add(menuWindowTransData);
		menuWindow.add(menuWindowModelRapameters);
		menuWindow.add(menuWindowScheme);
		menuWindow.add(menuWindowMap);
		menuWindow.add(menuWindowGeneralProperties);
		menuWindow.add(menuWindowCharacteristics);
		menuWindow.add(menuWindowAdditionalProperties);
		
		add(menuView);
		add(menuFile);
		add(menuTrace);
		add(menuReport);
		add(menuWindow);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}
			
			public void modelChanged(String e[])
			{
				ApplicationModel aModel = ModelMenuBar.this.getApplicationModel();
				
				menuView.setVisible(aModel.isVisible("menuView"));
				menuView.setEnabled(aModel.isEnabled("menuView"));
				menuViewMapViewOpen.setVisible(aModel.isVisible("menuViewMapViewOpen"));
				menuViewMapViewOpen.setEnabled(aModel.isEnabled("menuViewMapViewOpen"));
				menuViewSchemeOpen.setVisible(aModel.isVisible("menuViewSchemeOpen"));
				menuViewSchemeOpen.setEnabled(aModel.isEnabled("menuViewSchemeOpen"));
				
				menuViewModelSave.setVisible(aModel.isVisible("menuViewModelSave"));
				menuViewModelSave.setEnabled(aModel.isEnabled("menuViewModelSave"));
				menuViewModelLoad.setVisible(aModel.isVisible("menuViewModelLoad"));
				menuViewModelLoad.setEnabled(aModel.isEnabled("menuViewModelLoad"));
				menuFileOpen.setVisible(aModel.isVisible("menuFileOpen"));
				menuFileOpen.setEnabled(aModel.isEnabled("menuFileOpen"));
				menuFileOpenAs.setVisible(aModel.isVisible("menuFileOpenAs"));
				menuFileOpenAs.setEnabled(aModel.isEnabled("menuFileOpenAs"));
				menuFileOpenAsBellcore.setVisible(aModel.isVisible("menuFileOpenAsBellcore"));
				menuFileOpenAsBellcore.setEnabled(aModel.isEnabled("menuFileOpenAsBellcore"));
				menuFileOpenAsWavetek.setVisible(aModel.isVisible("menuFileOpenAsWavetek"));
				menuFileOpenAsWavetek.setEnabled(aModel.isEnabled("menuFileOpenAsWavetek"));
				menuFileSave.setVisible(aModel.isVisible("menuFileSave"));
				menuFileSave.setEnabled(aModel.isEnabled("menuFileSave"));
				menuFileSaveAs.setVisible(aModel.isVisible("menuFileSaveAs"));
				menuFileSaveAs.setEnabled(aModel.isEnabled("menuFileSaveAs"));
				menuFileSaveAsText.setVisible(aModel.isVisible("menuFileSaveAsText"));
				menuFileSaveAsText.setEnabled(aModel.isEnabled("menuFileSaveAsText"));
				menuFileClose.setVisible(aModel.isVisible("menuFileClose"));
				menuFileClose.setEnabled(aModel.isEnabled("menuFileClose"));
				menuFileAddCompare.setVisible(aModel.isVisible("menuFileAddCompare"));
				menuFileAddCompare.setEnabled(aModel.isEnabled("menuFileAddCompare"));
				menuFileRemoveCompare.setVisible(aModel.isVisible("menuFileRemoveCompare"));
				menuFileRemoveCompare.setEnabled(aModel.isEnabled("menuFileRemoveCompare"));
				
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
				
				menuWindow.setVisible(aModel.isVisible(ApplicationModel.MENU_VIEW));
				menuWindow.setEnabled(aModel.isEnabled(ApplicationModel.MENU_VIEW));
				menuWindowArrange.setVisible(aModel.isVisible(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowArrange.setEnabled(aModel.isEnabled(ApplicationModel.MENU_VIEW_ARRANGE));
				menuWindowAdditionalProperties.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES));
				menuWindowAdditionalProperties.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_ADDITIONAL_PROPERTIES));
				menuWindowAnalysis.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS));
				menuWindowAnalysis.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_WINDOW_ANALYSIS));
				menuWindowCharacteristics.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS));
				menuWindowCharacteristics.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_CHARACTERISTICS));
				menuWindowGeneralProperties.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES));
				menuWindowGeneralProperties.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_GENERAL_PROPERTIES));
				menuWindowMap.setVisible(aModel.isVisible(MapEditorApplicationModel.ITEM_VIEW_MAP));
				menuWindowMap.setEnabled(aModel.isEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP));
				menuWindowModelRapameters.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS));
				menuWindowModelRapameters.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_MODEL_PARAMETERS));
				menuWindowPrimaryParameters.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_TREE));
				menuWindowPrimaryParameters.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_TREE));
				menuWindowScheme.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_SCHEME));
				menuWindowScheme.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_SCHEME));
				menuWindowTraceSelector.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR));
				menuWindowTraceSelector.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_WINDOW_TRACESELECTOR));
				menuWindowTransData.setVisible(aModel.isVisible(ModelApplicationModel.MENU_WINDOW_TRANS_DATA));
				menuWindowTransData.setEnabled(aModel.isEnabled(ModelApplicationModel.MENU_WINDOW_TRANS_DATA));
			}
		});
	}
}
