/*-
 * $Id: TreeFilterUI.java,v 1.4 2005/09/21 14:25:11 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter.UI;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.newFilter.Filter;
import com.syrus.AMFICOM.newFilter.LangModelFilter;

/**
 * @author $Author: max $
 * @version $Revision: 1.4 $, $Date: 2005/09/21 14:25:11 $
 * @module filterclient
 */

public class TreeFilterUI {

	IconedTreeUI	treeUI;
	FilterPanel		filterUI;
	JPanel			mainPanel;
	Filter			filter;

	public TreeFilterUI(IconedTreeUI treeUI, FilterPanel filterUI) {
		this.treeUI = treeUI;
		this.filterUI = filterUI;

		this.treeUI.getTree().addTreeSelectionListener(
				new TreeSelectionListener() {
					public void valueChanged(TreeSelectionEvent e) {
						if (e.isAddedPath()) {
							TreePath path = e.getPath();
							Object obj = path.getLastPathComponent();
							if (obj instanceof Filtrable) {
								setFilter(((Filtrable) obj).getFilter());
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
			ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit()
					.getImage("images/filter.gif"));
			String title = LangModelFilter.getString("filter");

			final JToggleButton showFilterButton = new JToggleButton();
			showFilterButton.setIcon(icon);
			showFilterButton.setToolTipText(title);
			showFilterButton.setMargin(UIManager
					.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			showFilterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TreeFilterUI.this.filterUI.setVisible(showFilterButton
							.isSelected());
					TreeFilterUI.this.mainPanel.updateUI();
				}
			});
			JToolBar toolBar = this.treeUI.getToolBar();
			toolBar.addSeparator();
			toolBar.add(showFilterButton);

			this.mainPanel = new JPanel(new BorderLayout());
			this.mainPanel.add(toolBar, BorderLayout.NORTH);
			this.mainPanel.add(this.treeUI.getPanel(), BorderLayout.CENTER);
			this.mainPanel.add(this.filterUI, BorderLayout.SOUTH);
			this.filterUI.setVisible(false);
		}
		return this.mainPanel;
	}
}
