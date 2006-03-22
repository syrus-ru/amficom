package com.syrus.AMFICOM.Client.Prediction;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModel;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;

public class PredictionMenuBar extends AbstractMainMenuBar {
	private static final long serialVersionUID = -8372931013887467154L;

	public PredictionMenuBar(ApplicationModel aModel) {
		super(aModel);
	}
	
	@Override
	protected void addMenuItems() {
		final JMenu menuView = new JMenu();
		final JMenuItem menuViewDataLoad = new JMenuItem();
		final JMenuItem menuTraceAddCompare = new JMenuItem();
		final JMenuItem menuTraceRemoveCompare = new JMenuItem();
		final JMenuItem menuTraceClose = new JMenuItem();
		final JMenuItem menuViewCountPrediction = new JMenuItem();
		final JMenuItem menuViewSavePrediction = new JMenuItem();
		
		final JMenu menuReport = new JMenu();
		final JMenuItem menuReportCreate = new JMenuItem();
		
		menuView.setText(LangModelPrediction.getString("menuView"));
		menuView.setName(PredictionApplicationModel.MENU_VIEW);
		
		menuViewDataLoad.setText(LangModelPrediction.getString("menuViewDataLoad"));
		menuViewDataLoad.setName(PredictionApplicationModel.MENU_VIEW_DATA_LOAD);
		menuViewDataLoad.addActionListener(super.actionAdapter);
		
		menuTraceAddCompare.setText(LangModelAnalyse.getString("menuTraceAddCompare"));
		menuTraceAddCompare.setName(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE);
		menuTraceAddCompare.addActionListener(super.actionAdapter);
		menuTraceRemoveCompare.setText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		menuTraceRemoveCompare.setName(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE);
		menuTraceRemoveCompare.addActionListener(super.actionAdapter);
		menuTraceClose.setText(LangModelAnalyse.getString("menuFileClose"));
		menuTraceClose.setName(AnalyseApplicationModel.MENU_FILE_CLOSE);
		menuTraceClose.addActionListener(super.actionAdapter);
		
		menuViewCountPrediction.setText(LangModelPrediction.getString("menuViewCountPrediction"));
		menuViewCountPrediction.setName(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION);
		menuViewCountPrediction.addActionListener(super.actionAdapter);
		
		menuViewSavePrediction.setText(LangModelPrediction.getString("menuViewSavePrediction"));
		menuViewSavePrediction.setName(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION);
		menuViewSavePrediction.addActionListener(super.actionAdapter);
		
		menuReport.setText(LangModelAnalyse.getString("menuReport"));
		menuReport.setName(PredictionApplicationModel.MENU_REPORT);
		menuReportCreate.setText(LangModelAnalyse.getString("menuReportCreate"));
		menuReportCreate.setName(PredictionApplicationModel.MENU_REPORT_CREATE);
		menuReportCreate.addActionListener(super.actionAdapter);
		menuReport.add(menuReportCreate);
		
		menuView.add(menuViewDataLoad);
		menuView.add(menuTraceAddCompare);
		menuView.addSeparator();
		menuView.add(menuTraceClose);
		menuView.add(menuTraceRemoveCompare);
		menuView.addSeparator();
		menuView.add(menuViewCountPrediction);
		menuView.add(menuViewSavePrediction);
		
		add(menuView);
		add(menuReport);
		
		this.addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e});
			}
			
			public void modelChanged(String e[])
			{
				ApplicationModel aModel = PredictionMenuBar.this.getApplicationModel();
				
				menuView.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW));
				menuView.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW));
				menuViewDataLoad.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_DATA_LOAD));
				menuViewDataLoad.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_DATA_LOAD));
				menuViewCountPrediction.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION));
				menuViewCountPrediction.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION));
				
				menuViewSavePrediction.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION));
				menuViewSavePrediction.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION));
				
				menuTraceAddCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				menuTraceAddCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				menuTraceRemoveCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				menuTraceRemoveCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				menuTraceClose.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_FILE_CLOSE));
				menuTraceClose.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_FILE_CLOSE));
				
				menuReport.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_REPORT));
				menuReport.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_REPORT));
				menuReportCreate.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_REPORT_CREATE));
				menuReportCreate.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_REPORT_CREATE));
			}
		});
	}
}
