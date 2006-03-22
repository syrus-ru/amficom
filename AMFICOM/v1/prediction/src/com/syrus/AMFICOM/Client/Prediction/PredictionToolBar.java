package com.syrus.AMFICOM.Client.Prediction;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Lang.LangModelPrediction;
import com.syrus.AMFICOM.Client.General.Model.AnalyseApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.PredictionApplicationModel;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.ResourceKeys;

public class PredictionToolBar extends AbstractMainToolBar {
	private static final long serialVersionUID = 8396692415881094802L;

	public PredictionToolBar() {
		initItems();
	}
	
	private void initItems() {
		final JButton loadStatistics = new JButton();
		loadStatistics.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		loadStatistics.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		loadStatistics.setToolTipText(LangModelPrediction.getString("menuViewDataLoad"));
		loadStatistics.setName(PredictionApplicationModel.MENU_VIEW_DATA_LOAD);
		loadStatistics.addActionListener(super.actionListener);
		
		final JButton traceAddCompare = new JButton();
		traceAddCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_add.gif")));
		traceAddCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceAddCompare.setToolTipText(LangModelAnalyse.getString("menuTraceAddCompare"));
		traceAddCompare.setName(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE);
		traceAddCompare.addActionListener(super.actionListener);
		
		final JButton traceRemoveCompare = new JButton();
		traceRemoveCompare.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/download_remove.gif")));
		traceRemoveCompare.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		traceRemoveCompare.setToolTipText(LangModelAnalyse.getString("menuTraceRemoveCompare"));
		traceRemoveCompare.setName(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE);
		traceRemoveCompare.addActionListener(super.actionListener);
		
		final JButton countPrediction = new JButton();
		countPrediction.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/perform_analysis.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		countPrediction.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		countPrediction.setToolTipText(LangModelPrediction.getString("menuViewCountPrediction"));
		countPrediction.setName(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION);
		countPrediction.addActionListener(super.actionListener);
		
		final JButton savePrediction = new JButton();
		savePrediction.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/save.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		savePrediction.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		savePrediction.setToolTipText(LangModelPrediction.getString("menuViewSavePrediction"));
		savePrediction.setName(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION);
		savePrediction.addActionListener(super.actionListener);
		
		addSeparator();
		add(loadStatistics);
		add(traceAddCompare);
		add(traceRemoveCompare);
		addSeparator();
		add(countPrediction);
		add(savePrediction);
		
		addApplicationModelListener(new ApplicationModelListener() {
			public void modelChanged(String e) {
				modelChanged(new String[] { e });
			}
			
			public void modelChanged(String e[]) {
				ApplicationModel aModel = PredictionToolBar.this.getApplicationModel();
				loadStatistics.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_DATA_LOAD));
				loadStatistics.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_DATA_LOAD));
				traceAddCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				traceAddCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_ADD_COMPARE));
				traceRemoveCompare.setVisible(aModel.isVisible(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				traceRemoveCompare.setEnabled(aModel.isEnabled(AnalyseApplicationModel.MENU_TRACE_REMOVE_COMPARE));
				
				countPrediction.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION));
				countPrediction.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_COUNT_PREDICTION));
				
				savePrediction.setVisible(aModel.isVisible(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION));
				savePrediction.setEnabled(aModel.isEnabled(PredictionApplicationModel.MENU_VIEW_SAVE_PREDICTION));
			}
		});
	}
}
