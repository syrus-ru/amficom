/*-
 * $Id: RefreshWindowTreeCommand.java,v 1.1 2005/11/16 18:50:40 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.reportbuilder.command.template;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateModelException;
import com.syrus.AMFICOM.client.report.ReportModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderApplicationModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportBuilderMainFrame;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateElementsTreeModel;
import com.syrus.AMFICOM.client.reportbuilder.ReportTemplateElementsTreeMouseListener;
import com.syrus.AMFICOM.client.reportbuilder.TemplateTypeChooser;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.PopulatableItem;
import com.syrus.util.Log;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/11/16 18:50:40 $
 * @module reportbuilder
 */

public class RefreshWindowTreeCommand extends AbstractCommand {
	
	ApplicationContext aContext;
	ReportBuilderMainFrame mainFrame;
	
	public static final String TREE_FRAME = "report.Tree.rootTemplates";
	
	ReportTemplateElementsTreeModel model;
	JInternalFrame treeFrame;
		
	public RefreshWindowTreeCommand(
			ApplicationContext aContext,
			ReportBuilderMainFrame mainFrame) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
		init();
	}
	
	private void init() {
		Log.debugMessage(".createValue | TREE_FRAME", Level.FINEST);
		treeFrame = new JInternalFrame();
		treeFrame.setName(ReportBuilderApplicationModel.MENU_WINDOW_TREE);
		treeFrame.setIconifiable(true);
		treeFrame.setClosable(true);
		treeFrame.setResizable(true);
		treeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		treeFrame.setFrameIcon(UIManager.getIcon(ResourceKeys.ICON_GENERAL));
		treeFrame.setTitle(I18N.getString(TREE_FRAME));
		
		model = new ReportTemplateElementsTreeModel(this.aContext);
		
		IconedTreeUI iconedTreeUI = new IconedTreeUI(new PopulatableItem());
				
		TreeFilterUI treeFilterUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());				

		treeFrame.getContentPane().setLayout(new BorderLayout());
		treeFrame.getContentPane().add(treeFilterUI.getPanel(), BorderLayout.CENTER);
		JTree tree = iconedTreeUI.getTree();
		this.mainFrame.reportBuilderTreeMouseListener =
			new ReportTemplateElementsTreeMouseListener(tree);
		this.mainFrame.reportBuilderTreeMouseListener.setContext(
				this.aContext);
		tree.addMouseListener(this.mainFrame.reportBuilderTreeMouseListener);

		this.mainFrame.getDesktopPane().add(treeFrame);
	}
	
	@Override
	public void execute() {
		this.treeFrame.getContentPane().removeAll();
		
		ReportModel reportModel = TemplateTypeChooser.getSelectedModule();
		IconedTreeUI iconedTreeUI = new IconedTreeUI(this.model.createRoot(reportModel));
		TreeFilterUI treeFilterUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());
		this.treeFrame.getContentPane().add(treeFilterUI.getPanel(), BorderLayout.CENTER);
		iconedTreeUI.getSyncButton().setVisible(false);
		
		boolean enable = (reportModel == null) ? true : false;
		iconedTreeUI.getRefreshButton().setEnabled(enable);
		treeFilterUI.getShowFilterButton().setEnabled(enable);
		this.mainFrame.innerToolbar.getChangeViewButton().setEnabled(enable);
		
		this.treeFrame.setVisible(true);
	}
	
}
