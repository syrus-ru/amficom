/*-
 * $$Id: MapViewTreePanel.java,v 1.33 2006/02/08 12:11:07 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.logic.ItemTreeModel;

/**
 * @version $Revision: 1.33 $, $Date: 2006/02/08 12:11:07 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapViewTreePanel extends JPanel {

	ApplicationContext aContext;

	private BorderLayout borderLayout1 = new BorderLayout();

	public MapViewTreePanel(ApplicationContext aContext) {
		this.aContext = aContext;
		jbInit();
	}

	private void jbInit() {
		this.setLayout(this.borderLayout1);

		MapEditorTreeModel model = new MapEditorTreeModel(this.aContext);

		IconedTreeUI iconedTreeUI = new IconedTreeUI(model.getRoot());
		TreeFilterUI flterTreeUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());

		JTree tree = iconedTreeUI.getTree();

		ItemTreeModel treeModel = (ItemTreeModel)tree.getModel();
		treeModel.setAllwaysSort(false);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		MapViewTreeEventHandler mapViewTreeEventHandler = new MapViewTreeEventHandler(iconedTreeUI, this.aContext, model.getRoot());
		tree.addTreeSelectionListener(mapViewTreeEventHandler);
		tree.addTreeWillExpandListener(mapViewTreeEventHandler);
		tree.addMouseListener(new MapViewTreeMouseListener(tree, this.aContext));

		this.add(flterTreeUI.getPanel(), BorderLayout.CENTER);
	}

}


