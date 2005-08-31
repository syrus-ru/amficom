/*
 * $Id: ReportTemplateElementsTreeModel.java,v 1.3 2005/08/31 10:29:03 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder;

import java.util.Collection;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.LangModelReport;
import com.syrus.AMFICOM.client.report.ReportModel;
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
		String grandParentItem = (String)node.getParent().getParent().getObject();
		
		if (grandParentItem.equals(TEMPLATE_ELEMENTS_ROOT))
			itemsToAdd = reportModel.getTemplateElementNames();
		else if (grandParentItem.equals(REPORT_ELEMENTS_ROOT))
			itemsToAdd = reportModel.getReportElementNames();
				
		if (itemsToAdd == null)
			throw new AssertionError("Trying to create report element items in wrong branch!");
				
		for (String item : itemsToAdd) {
			node.addChild(new PopulatableIconedNode(
				this,
				item,
				LangModelReport.getString(item),
				UIManager.getIcon(ReportModel.getIconForReportType(
					reportModel.getReportKind(item)))));
		}
	}
}
