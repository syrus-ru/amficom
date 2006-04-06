/*-
 * $Id: ModelLoadDialog.java,v 1.1.2.1 2006/04/06 13:01:43 saa Exp $
 *
 * Copyright ї 2006 Syrus Systems.
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
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingResultParameter;
import com.syrus.util.Log;

public class ModelLoadDialog {
	private static TreeFilterUI treeFilterUI;
	static IconedTreeUI treeUI;
	private static Set<ModelingResultParameter> selectedResults;

	static JOptionPane optionPane;
	static JDialog dialog;
	static JButton okButton;
	static JButton cancelButton;
	static int result;

	public static Set<ModelingResultParameter> getResults() {
		return selectedResults;
	}

	public static int showDialog(JFrame frame) {
		if (treeFilterUI == null) {
			selectedResults = new HashSet<ModelingResultParameter>();
			Item root = new ModelChildrenFactory().getRoot();

			treeUI = new IconedTreeUI(root);
			treeUI.getTree().expandPath(new TreePath(root));
			treeUI.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
			treeUI.getTreeUI().getTreeModel().setAllwaysSort(false);
			
			FilterPanel filterPanel = new FilterPanel();
			treeFilterUI = new TreeFilterUI(treeUI, filterPanel);
			treeUI.getTree().setRootVisible(true);
			treeUI.getTree().addTreeSelectionListener(new TreeSelectionListener() {
				public void valueChanged(TreeSelectionEvent e) {
					boolean b = false;
					TreePath[] paths = treeUI.getTree().getSelectionPaths();
					if (paths != null) {
						for (int i = 0; i < paths.length; i++) {
							for (int j = 0; j < paths[i].getPathCount(); j++) {
								Item item = (Item)paths[i].getPathComponent(j);
								if (item.getObject() instanceof Measurement || 
										item.getObject() instanceof Modeling) {
									b = true;
									break;
								}
							}
						}
					}
					ModelLoadDialog.okButton.setEnabled(b);
				}
			});
			okButton = new JButton(I18N.getString("Common.Button.OK"));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					result = JOptionPane.OK_OPTION;
					dialog.dispose();
				}
			});
			cancelButton = new JButton(I18N.getString("Common.Button.Cancel"));
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
			Set<Identifier> selectedModelIds = new HashSet<Identifier>();
			TreePath[] paths = treeUI.getTree().getSelectionPaths();
			if (paths != null) {
				for (int i = 0; i < paths.length; i++) {
					for (int j = 0; j < paths[i].getPathCount(); j++) {
						Item item = (Item) paths[i].getPathComponent(j);
						if (item.getObject() instanceof Modeling) {
							selectedModelIds.add(((Modeling)item.getObject()).getId());
						}
					}
				}
			}

			try {
				if (!selectedModelIds.isEmpty()) {
					StorableObjectCondition condition2 = new LinkedIdsCondition(selectedModelIds, ObjectEntities.MODELINGRESULTPARAMETER_CODE);
					Set<ModelingResultParameter> resultSet = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
					for (ModelingResultParameter modelingResult: resultSet) {
						selectedResults.add(modelingResult);
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
			if (selectedResults.isEmpty() && selectedModelIds.isEmpty()) {
				// XXX: error processing: выдать сообщение об ошибке, что нет результатов
				result = JOptionPane.CANCEL_OPTION;
			}
		}
		return result;
	}
}
