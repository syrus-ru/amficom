/*
 * $Id: ReportTemplateElementsTreeModel.java,v 1.1.1.1 2005/12/02 11:37:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.util.Collection;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.UI.ResultChildrenFactory;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.map.ui.MapTreeModel;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.report.ReportModel.ReportType;
import com.syrus.AMFICOM.client.reportbuilder.templaterenderer.ReportTreeItem;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeTreeModel;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;

public class ReportTemplateElementsTreeModel implements ChildrenFactory, VisualManagerFactory {
	
	protected static final String REPORT_DATA_ROOT = "report.Tree.reportData";
		
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
		//nothing to do
	}
	
	public Item createRoot(ReportModel reportModel) {
		if(reportModel == null) {
			this.root = new PopulatableIconedNode(
					this,
					ReportTemplateElementsTreeModel.REPORT_DATA_ROOT,
					I18N.getString(ReportTemplateElementsTreeModel.REPORT_DATA_ROOT),
					UIManager.getIcon(ICON_CATALOG));
			createReportElementsModels(this.root);
			return this.root;
		}
			
		this.root = new PopulatableIconedNode(
				this,
				reportModel,
				reportModel.getLocalizedName(),
				UIManager.getIcon(ICON_CATALOG));
		createReportModelItems(this.root);			
		return this.root;
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
