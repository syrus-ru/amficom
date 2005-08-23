package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.client.UI.tree.IconedNode;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeModel;

public final class MapViewTreePanel extends JPanel {

	ApplicationContext aContext;

	private BorderLayout borderLayout1 = new BorderLayout();

	public MapViewTreePanel(ApplicationContext aContext) {
		this.aContext = aContext;
		jbInit();
	}

	private void jbInit() {
		this.setLayout(this.borderLayout1);

		MapViewTreeModel model = MapViewTreeModel.getInstance();

		Item root = new IconedNode("root", LangModelGeneral.getString("root"));

		IconedTreeUI iconedTreeUI = new IconedTreeUI(root);
		TreeFilterUI flterTreeUI = new TreeFilterUI(iconedTreeUI, new FilterPanel());

		JTree tree = iconedTreeUI.getTree();

		ItemTreeModel treeModel = (ItemTreeModel )tree.getModel();
		treeModel.setAllwaysSort(false);

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		MapViewTreeEventHandler mapViewTreeEventHandler = new MapViewTreeEventHandler(iconedTreeUI, this.aContext, model, root);
		tree.addTreeSelectionListener(mapViewTreeEventHandler);
		tree.addTreeWillExpandListener(mapViewTreeEventHandler);
		tree.addMouseListener(new MapViewTreeMouseListener(tree, this.aContext));

		this.add(flterTreeUI.getPanel(), BorderLayout.CENTER);
	}

}


