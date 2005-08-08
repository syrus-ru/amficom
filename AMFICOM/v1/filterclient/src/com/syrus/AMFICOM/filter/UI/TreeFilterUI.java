/*-
 * $Id: TreeFilterUI.java,v 1.2 2005/08/08 11:41:00 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:41:00 $
 * @module filterclient
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
