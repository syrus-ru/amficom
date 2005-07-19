/*-
 * $Id: TraceLoadDialog.java,v 1.1 2005/07/19 13:21:53 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.client.UI.tree.IconedTreeUI;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.filterclient.MeasurementConditionWrapper;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.newFilter.Filter;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/07/19 13:21:53 $
 * @module analysis_v1
 */

public class TraceLoadDialog {
	private static TreeFilterUI treeFilterUI;
	private static IconedTreeUI treeUI;
	private static Set<Result> selectedResults;
	
	static JOptionPane optionPane;
	static JDialog dialog;
	static JButton okButton;
	static JButton cancelButton;
	static int result;
	
	public static Set<Result> getResults() {
		return selectedResults;
	}

	public static int showDialog(JFrame frame) {
		if (treeFilterUI == null) {
			selectedResults = new HashSet<Result>();
			ResultChildrenFactory.getInstance().setDomainId(LoginManager.getDomainId());
			Item root = ResultChildrenFactory.getRoot();
			
			treeUI = new IconedTreeUI(root);
			treeUI.getTree().expandPath(new TreePath(root));
			treeUI.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			
			FilterPanel filterPanel = new FilterPanel();
			treeFilterUI = new TreeFilterUI(treeUI, filterPanel);
			treeUI.getTree().setRootVisible(true);
			treeUI.getTree().addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					boolean b = false;
					TreePath[] paths = e.getPaths();
					for (int i = 0; i < paths.length; i++) {
						for (int j = 0; j < paths[i].getPathCount(); j++) {
							Item item = (Item)paths[i].getPathComponent(j);
							if (item.getObject() instanceof Result) {
								b = true;
								break;
							}
						}
					}
					TraceLoadDialog.okButton.setEnabled(b);
				}
			});
			okButton = new JButton(LangModelGeneral.getString("Button.OK"));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result = JOptionPane.OK_OPTION;
					dialog.dispose();
				}
			});
			cancelButton = new JButton(LangModelGeneral.getString("Button.Cancel"));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			});
			optionPane = new JOptionPane(treeFilterUI.getPanel(), JOptionPane.PLAIN_MESSAGE,
					JOptionPane.OK_CANCEL_OPTION, null, new Object[] { okButton,
							cancelButton}, null);
		}
		selectedResults.clear();
		okButton.setEnabled(false);
		result = JOptionPane.CANCEL_OPTION;
		
		dialog = optionPane.createDialog(frame, LangModelAnalyse.getString("trace"));
		dialog.setResizable(true);
		dialog.setVisible(true);
		
		if (result == JOptionPane.OK_OPTION) {
			TreePath[] paths = treeUI.getTree().getSelectionPaths();
			if (paths != null) {
				for (int i = 0; i < paths.length; i++) {
					for (int j = 0; j < paths[i].getPathCount(); j++) {
						Item item = (Item) paths[i].getPathComponent(j);
						if (item.getObject() instanceof Result) {
							selectedResults.add((Result) item.getObject());
						}
					}
				}
			}
			if (selectedResults.isEmpty()) {
				result = JOptionPane.CANCEL_OPTION;
			}
		}
		return result;
	}
}
