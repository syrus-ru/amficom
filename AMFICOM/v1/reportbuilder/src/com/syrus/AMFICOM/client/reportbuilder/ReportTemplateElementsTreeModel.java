/*
 * $Id: ReportTemplateElementsTreeModel.java,v 1.5 2005/09/05 07:58:03 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.util.Collection;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.analysis.report.AnalysisReportModel;
import com.syrus.AMFICOM.client.analysis.report.EvaluationReportModel;
import com.syrus.AMFICOM.client.analysis.report.SurveyReportModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.modelling.ModellingReportModel;
import com.syrus.AMFICOM.client.prediction.PredictionReportModel;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModelPool;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;

public class ReportTemplateElementsTreeModel implements ChildrenFactory, VisualManagerFactory {
	private static final String TREE_ROOT = "report.Tree.availableElements";
	private static final String REPORT_ELEMENTS_ROOT = "report.Tree.reportElements";
	private static final String TEMPLATE_ELEMENTS_ROOT = "report.Tree.templateElements";
	
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
		Collection<Object> contents = CommonUIUtilities.getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(TREE_ROOT)) {
				createRootItems(node, contents);
			} 
			else if (s.equals(REPORT_ELEMENTS_ROOT)) {
				createReportElementsModels(node, contents);
			}
			else if (s.equals(TEMPLATE_ELEMENTS_ROOT)) {
				createTemplateElementsModels(node, contents);
			}
		} 
		else {
			if (node.getObject() instanceof ReportModel) {
				createReportModelItems(node, contents);
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
	
	private void createRootItems(Item node, Collection contents) {
		node.addChild(new PopulatableIconedNode(
				this,
				ReportTemplateElementsTreeModel.REPORT_ELEMENTS_ROOT,
				LangModelReport.getString(ReportTemplateElementsTreeModel.REPORT_ELEMENTS_ROOT),
				UIManager.getIcon(ICON_CATALOG)));
				
		node.addChild(new PopulatableIconedNode(
				this,
				ReportTemplateElementsTreeModel.TEMPLATE_ELEMENTS_ROOT,
				LangModelReport.getString(ReportTemplateElementsTreeModel.TEMPLATE_ELEMENTS_ROOT),
				UIManager.getIcon(ICON_CATALOG)));
	}
	
	private void createTemplateElementsModels(Item node, Collection contents) {
		//Модель для модуля "Анализ"
		ReportModel analysisReportModel =
			ReportModelPool.getModel(AnalysisReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				analysisReportModel,
				analysisReportModel.getName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Измерения"
		ReportModel evaluationReportModel =
			ReportModelPool.getModel(EvaluationReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				evaluationReportModel,
				evaluationReportModel.getName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Исследование"
		ReportModel surveyReportModel =
			ReportModelPool.getModel(SurveyReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				surveyReportModel,
				surveyReportModel.getName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Моделирование"
		ReportModel modellingReportModel =
			ReportModelPool.getModel(ModellingReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				modellingReportModel,
				modellingReportModel.getName(),
				UIManager.getIcon(ICON_CATALOG)));

		//Модель для модуля "Прогнозирование"
		ReportModel predictionReportModel =
			ReportModelPool.getModel(PredictionReportModel.class.getName());
		node.addChild(new PopulatableIconedNode(
				this,
				predictionReportModel,
				predictionReportModel.getName(),
				UIManager.getIcon(ICON_CATALOG)));
	}

	private void createReportElementsModels(Item node, Collection contents) {
//		String type = (IdlKind) node.getObject();
//		
//		try {
//			TypicalCondition condition = new TypicalCondition(String.valueOf(type.value()), 
//					OperationSort.OPERATION_EQUALS, ObjectEntities.SCHEME_CODE,
//					SchemeWrapper.COLUMN_KIND);
//			Set<StorableObject> schemes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
//			
//			Collection toAdd = CommonUIUtilities.getObjectsToAdd(schemes, contents);
//			Collection<Item> toRemove = CommonUIUtilities.getItemsToRemove(schemes, node.getChildren());
//
//			for (Item child : toRemove) {
//				child.setParent(null);
//			}			
//			for (Iterator it = toAdd.iterator(); it.hasNext();) {
//				Scheme sc = (Scheme) it.next();
//				node.addChild(new PopulatableIconedNode(this, sc));
//			}
//		} 
//		catch (ApplicationException ex1) {
//			ex1.printStackTrace();
//		}
	}
	
	private void createReportModelItems(Item node, Collection contents) {
		ReportModel reportModel = (ReportModel) node.getObject();
		
		Collection<String> itemsToAdd = null;
		String parentItem = (String)node.getParent().getObject();
		
		if (parentItem.equals(TEMPLATE_ELEMENTS_ROOT))
			itemsToAdd = reportModel.getTemplateElementNames();
		else if (parentItem.equals(REPORT_ELEMENTS_ROOT))
			itemsToAdd = reportModel.getReportElementNames();
				
		if (itemsToAdd == null)
			throw new AssertionError("Trying to create report element items in wrong branch!");
				
		for (String item : itemsToAdd) {
			node.addChild(new PopulatableIconedNode(
				this,
				item,
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
