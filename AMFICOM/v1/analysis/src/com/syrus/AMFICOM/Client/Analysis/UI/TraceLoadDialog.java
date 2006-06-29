/*-
 * $Id: TraceLoadDialog.java,v 1.14 2006/06/29 10:07:18 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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
import com.syrus.AMFICOM.client.util.SynchronousWorker;
import com.syrus.AMFICOM.filter.UI.FilterPanel;
import com.syrus.AMFICOM.filter.UI.TreeFilterUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.14 $, $Date: 2006/06/29 10:07:18 $
 * @module analysis
 */

public class TraceLoadDialog {
	private static TreeFilterUI treeFilterUI;
	static IconedTreeUI treeUI;
	static Set<Result> selectedResults;
	static Set<Identifier> selectedMeasurementIds;

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
			selectedMeasurementIds = new HashSet<Identifier>();
			Item root = new ResultChildrenFactory().getRoot();
			
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
								if (item.getObject() instanceof Measurement) {
									b = true;
									break;
								}
							}
						}
					}
					TraceLoadDialog.okButton.setEnabled(b);
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
		selectedMeasurementIds.clear();
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
						if (item.getObject() instanceof Measurement) {
							selectedMeasurementIds.add(((Measurement)item.getObject()).getId());
						}
					}
				}
			}
			if (!selectedMeasurementIds.isEmpty()) {
				final SynchronousWorker<Boolean> worker = new SynchronousWorker<Boolean>(null,
						I18N.getString("Message.Information.please_wait"), 
						I18N.getString("Message.Information.loading_data"), true) {
					@Override
					public Boolean construct() throws Exception {
//						final LinkedIdsCondition condition2 = new LinkedIdsCondition(selectedMeasurementIds, ObjectEntities.RESULT_CODE);
//					final TypicalCondition condition3 = new TypicalCondition(ResultSort.RESULT_SORT_MEASUREMENT.value(),
//							OperationSort.OPERATION_EQUALS, ObjectEntities.RESULT_CODE, ResultWrapper.COLUMN_SORT);
//					selectedResults = StorableObjectPool.getStorableObjectsByCondition(
//							new CompoundCondition(condition2, CompoundConditionSort.AND, condition3),
						
						StorableObjectCondition condition2 = new LinkedIdsCondition(selectedMeasurementIds, ObjectEntities.RESULT_CODE);
						Set<Result> results = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
						for (Result res : results) {
							if (res.getSort().value() == ResultSort.RESULT_SORT_MEASUREMENT.value()) {
								selectedResults.add(res);	
							}
						}
						return Boolean.valueOf(true);
					}
				};
				
				try {
					worker.execute();
				} catch (ExecutionException e) {
					Log.errorMessage(e);
				}
			}
			if (selectedResults.isEmpty()) {
				// XXX: error processing: выдать сообщение об ошибке, что нет результатов
				result = JOptionPane.CANCEL_OPTION;
			}
		}
		return result;
	}
}
