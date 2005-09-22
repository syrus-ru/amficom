/*
 * $Id: ReportTemplateElementsTreeModel.java,v 1.10 2005/09/22 14:50:03 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.util.Collection;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ResultChildrenFactory;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.analysis.report.AnalysisReportModel;
import com.syrus.AMFICOM.client.analysis.report.EvaluationReportModel;
import com.syrus.AMFICOM.client.analysis.report.SurveyReportModel;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.map.ui.MapTreeModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.modelling.report.ModelingReportModel;
import com.syrus.AMFICOM.client.observe.report.ObserveReportModel;
import com.syrus.AMFICOM.client.prediction.report.PredictionReportModel;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTreeItem;
import com.syrus.AMFICOM.client.scheduler.report.SchedulerReportModel;
import com.syrus.AMFICOM.client.scheme.report.SchemeReportModel;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.util.Log;

public class ReportTemplateElementsTreeModel implements ChildrenFactory, VisualManagerFactory {
	private static final String TREE_ROOT = "report.Tree.rootTemplates";
	protected static final String REPORT_DATA_ROOT = "report.Tree.reportData";
	protected static final String TEMPLATE_ELEMENTS_ROOT = "report.Tree.templateElements";
	protected static final String AVAILABLE_TEMPLATES = "report.Tree.availableTemplates";	
	
	private static final String ICON_CATALOG = "icon.catalog";

	ApplicationContext aContext;
	PopulatableIconedNode root;

	public ReportTemplateElementsTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public VisualManager getVisualManager(Item node) {
		// for any other strings return null Manager
		return null;
	}
	
	public void populate(Item node) {
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(TREE_ROOT)) {
				createRootItems(node);
			}
			else if (s.equals(AVAILABLE_TEMPLATES)) {
			}
			else if (s.equals(REPORT_DATA_ROOT)) {
				createReportElementsModels(node);
			}
			else if (s.equals(TEMPLATE_ELEMENTS_ROOT)) {
				try {
					createTemplateElementsModels(node);
				} catch (CreateModelException e) {
					Log.errorMessage("ReportTemplateElementsTreeModel.populate | " + e.getMessage());
					Log.errorException(e);			
					JOptionPane.showMessageDialog(
							Environment.getActiveWindow(),
							e.getMessage(),
							LangModelReport.getString("report.Exception.error"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} 
		else {
			if (node.getObject() instanceof ReportModel) {
				createReportModelItems(node);
			}
		}
	}
	
	public static final Object getRootObject() {
		return ReportTemplateElementsTreeModel.TREE_ROOT;
	}
	
	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode(
				this,
				ReportTemplateElementsTreeModel.TREE_ROOT,
				LangModelReport.getString(ReportTemplateElementsTreeModel.TREE_ROOT),
				UIManager.getIcon(ICON_CATALOG));
		}
		return this.root;
	}
	
	private void createRootItems(Item node) {
		node.addChild(new PopulatableIconedNode(
				this,
				ReportTemplateElementsTreeModel.REPORT_DATA_ROOT,
				LangModelReport.getString(ReportTemplateElementsTreeModel.REPORT_DATA_ROOT),
				UIManager.getIcon(ICON_CATALOG)));
				
		node.addChild(new PopulatableIconedNode(
				this,
				ReportTemplateElementsTreeModel.TEMPLATE_ELEMENTS_ROOT,
				LangModelReport.getString(ReportTemplateElementsTreeModel.TEMPLATE_ELEMENTS_ROOT),
				UIManager.getIcon(ICON_CATALOG)));
	}
	
	private void createTemplateElementsModels(Item node) throws CreateModelException {
		//Модель для модуля "Карта"
		ReportModel mapReportModel =
			ReportModelPool.getModel(MapReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				mapReportModel,
				mapReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Схема"
		ReportModel schemeReportModel =
			ReportModelPool.getModel(SchemeReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				schemeReportModel,
				schemeReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));
		
		//Модель для модуля "Анализ"
		ReportModel analysisReportModel =
			ReportModelPool.getModel(AnalysisReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				analysisReportModel,
				analysisReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Измерения"
		ReportModel evaluationReportModel =
			ReportModelPool.getModel(EvaluationReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				evaluationReportModel,
				evaluationReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Исследование"
		ReportModel surveyReportModel =
			ReportModelPool.getModel(SurveyReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				surveyReportModel,
				surveyReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Наблюдение"
		ReportModel observeReportModel =
			ReportModelPool.getModel(ObserveReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				observeReportModel,
				observeReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Планирование тестов"
		ReportModel schedulerReportModel =
			ReportModelPool.getModel(SchedulerReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				schedulerReportModel,
				schedulerReportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG)));
		
//		//Модель для модуля "Моделирование"
//		ReportModel modellingReportModel =
//			ReportModelPool.getModel(ModelingReportModel.class.getName());
//		node.addChild(new PopulatableIconedNode(
//				this,
//				modellingReportModel,
//				modellingReportModel.getLocalizedName(),
//				UIManager.getIcon(ICON_CATALOG)));
//
//		//Модель для модуля "Прогнозирование"
//		ReportModel predictionReportModel =
//			ReportModelPool.getModel(PredictionReportModel.class.getName());
//		node.addChild(new PopulatableIconedNode(
//				this,
//				predictionReportModel,
//				predictionReportModel.getLocalizedName(),
//				UIManager.getIcon(ICON_CATALOG)));
	}

	private void createReportElementsModels(Item node) {
		PopulatableIconedNode schemeRoot = (PopulatableIconedNode)(new SchemeTreeModel(this.aContext)).getRoot();
		schemeRoot.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_SCHEME));
		node.addChild(schemeRoot);
		
		PopulatableIconedNode mapRoot = (new MapTreeModel()).createAllMapsRoot();
		node.addChild(mapRoot);
		
		PopulatableIconedNode analysisRoot = (new ResultChildrenFactory()).getRoot();
		analysisRoot.setIcon(UIManager.getIcon(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS));		
		node.addChild(analysisRoot);
	}
	
	private void createReportModelItems(Item node) {
		ReportModel reportModel = (ReportModel) node.getObject();
		
		Collection<String> itemsToAdd = null;
		String parentItem = (String)node.getParent().getObject();
		
		if (parentItem.equals(TEMPLATE_ELEMENTS_ROOT))
			itemsToAdd = reportModel.getTemplateElementNames();
				
		if (itemsToAdd == null)
			throw new AssertionError("Trying to create report element items in wrong branch!");
				
		for (String item : itemsToAdd) {
			node.addChild(new PopulatableIconedNode(
				this,
				new ReportTreeItem(reportModel.getClass().getName(),item),
				reportModel.getReportElementName(item),
				this.getIconForReportType(reportModel.getReportKind(item)),
				false));
		}
	}
	
	private Icon getIconForReportType(ReportType type){
		if (type.equals(ReportType.TABLE))
			return UIManager.getIcon(ReportBuilderResourceKeys.TABLE_TEMPLATE_ELEMENT);

		if (type.equals(ReportType.SCHEMA))
			return UIManager.getIcon(ReportBuilderResourceKeys.SCHEME_TEMPLATE_ELEMENT);

		if (type.equals(ReportType.GRAPH))
			return UIManager.getIcon(ReportBuilderResourceKeys.GRAPH_TEMPLATE_ELEMENT);

		throw new AssertionError ("Unknown type of report");
	}
}
