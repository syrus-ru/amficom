/*-
 * $Id: TreeFilterUI.java,v 1.1 2005/06/22 07:30:17 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.filter.UI.Filtrable;
import com.syrus.AMFICOM.newFilter.Filter;

/**
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/06/22 07:30:17 $
 * @module filterclient_v1
 */

public class TreeFilterUI {
	
	IconedTreeUI treeUI;
	FilterPanel filterUI;
	JPanel mainPanel;
	Filter filter;
	
	public TreeFilterUI(IconedTreeUI treeUI, FilterPanel filterUI) {
		this.treeUI = treeUI;
		this.filterUI = filterUI;
		
		this.treeUI.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				if (e.isAddedPath()) {
					TreePath path = e.getPath();
					Object obj = path.getLastPathComponent();
					if (obj instanceof Filtrable) {
						setFilter(((Filtrable)obj).getFilter());
					} else {
						setFilter(null);
					}
				}
			}
		});
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
		this.filterUI.setFilter(filter);
	}
	
	public JComponent getPanel() {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel(new BorderLayout());
			this.mainPanel.add(this.treeUI.getToolBar(), BorderLayout.NORTH);
			this.mainPanel.add(this.treeUI.getPanel(), BorderLayout.CENTER);
			this.mainPanel.add(this.filterUI, BorderLayout.SOUTH);
		}
		return this.mainPanel;
	}
}
