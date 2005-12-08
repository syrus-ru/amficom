/*-
* $Id: ManagerGraphSelectionListener.java,v 1.7 2005/12/08 16:07:04 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.7 $, $Date: 2005/12/08 16:07:04 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
final class ManagerGraphSelectionListener implements GraphSelectionListener {

	
	private final ManagerMainFrame	managerMainFrame;

	public ManagerGraphSelectionListener(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
	}
	
	@SuppressWarnings({"unqualified-field-access","unchecked"})
	public void valueChanged(final GraphSelectionEvent e) {
		if (managerMainFrame.arranging) {
			return;
		}
		
		final JInternalFrame frame = 
			(JInternalFrame) managerMainFrame.frames.get(ManagerMainFrame.PROPERTIES_FRAME);
		frame.setTitle(I18N.getString(ManagerMainFrame.PROPERTIES_FRAME));
		if (managerMainFrame.beanUI != null) {
			managerMainFrame.beanUI.disposePropertyPanel();
		}
		final JPanel panel = managerMainFrame.propertyPanel;
		panel.removeAll();		
		if (e == null) {			
			return;
		}
		
		final Object cell = e.getCell();
		final TreeSelectionModel selectionModel = managerMainFrame.tree.getSelectionModel();
		final GraphModel model = managerMainFrame.graph.getModel();
		final PerspectiveTreeModel treeModel = managerMainFrame.getTreeModel();
		
		final Perspective perspective = managerMainFrame.getPerspective();
		
		boolean deleteAllow = true;
		boolean cutAllow = false;
		
		if (model.isEdge(cell)) {
			if (e.isAddedCell()) {
				final Edge edge = (Edge)cell;
				final TreeNode sourceNode = (TreeNode) edge.getSource();
				final TreeNode targetNode = (TreeNode) edge.getSource();
				if (sourceNode != null) {
					final TreePath treePath = new TreePath(treeModel.getPathToRoot(sourceNode));
//					assert Log.debugMessage("1:" + treePath, Log.DEBUGLEVEL03);
					managerMainFrame.tree.scrollPathToVisible(treePath);
				}
				if (targetNode != null) {
					final TreePath treePath = new TreePath(treeModel.getPathToRoot(targetNode));
//					assert Log.debugMessage("2:" + treePath, Log.DEBUGLEVEL03);
					managerMainFrame.tree.scrollPathToVisible(treePath);
				}
			} else {
				selectionModel.clearSelection();
			}
		} else {				
			if (e.isAddedCell()){
				if (!model.isPort(cell) && !model.isEdge(cell)) {
					TreeNode[] pathToRoot = treeModel.getPathToRoot((TreeNode) cell);
					if (pathToRoot != null) {
						final TreePath treePath = new TreePath(pathToRoot);
//						assert Log.debugMessage("3:" + treePath, Log.DEBUGLEVEL03);
						selectionModel.setSelectionPath(treePath);
					} else {
						selectionModel.clearSelection();
					}
					
				}
				MPort port = (model.isPort(cell)) ? (MPort)cell : (MPort)((DefaultGraphCell)cell).getChildAt(0);				
				final Object userObject = port.getUserObject();
				
				if (userObject instanceof AbstractBean) {
					final AbstractBean abstractBean = (AbstractBean)userObject;
					final boolean supported = perspective.isSupported(abstractBean);
					if (supported) {
						final LayoutItem parentLayoutItem = perspective.getParentLayoutItem();
						final GraphRoutines graphRoutines = managerMainFrame.getGraphRoutines();
						final boolean undeletable = perspective.isUndeletable(abstractBean);
						assert Log.debugMessage(abstractBean 
								+ " is " 
								+ (undeletable ? "undeletable " : "deletable"), 
							Log.DEBUGLEVEL10);
						deleteAllow &= !undeletable;
						cutAllow |= perspective.isCuttable(abstractBean);
						if (parentLayoutItem != null) {	
							try {
	
								final boolean parent = graphRoutines.getBean(parentLayoutItem) == abstractBean;
								if (parent) {
									assert Log.debugMessage(abstractBean + " is a parent ", Log.DEBUGLEVEL10);
								}

								deleteAllow &= !parent;
	
								managerMainFrame.beanUI = perspective.getBeanUI(abstractBean.getCodename());
								JPanel propertyPanel2 = managerMainFrame.beanUI.getPropertyPanel(abstractBean);
								if (propertyPanel2 != null) {
									frame.setTitle(I18N.getString(ManagerMainFrame.PROPERTIES_FRAME)
										+ " : "
										+ ((AbstractBean)userObject).getName());
									final GridBagLayout gridBagLayout = 
										(GridBagLayout) panel.getLayout();
									final GridBagConstraints gbc = gridBagLayout.getConstraints(panel);
									gbc.fill = GridBagConstraints.BOTH;
									gbc.weightx = 1.0;
									gbc.weighty = 1.0;
									gbc.gridwidth = GridBagConstraints.REMAINDER;
									panel.add(propertyPanel2, gbc);
								}
							} catch (ApplicationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}		
			
			} else {
				selectionModel.clearSelection();
			}
		}

		panel.revalidate();
		panel.repaint();
		
		boolean enabled = !managerMainFrame.graph.isSelectionEmpty();
//		assert Log.debugMessage("enabled:" + enabled , Log.DEBUGLEVEL03);
//		assert Log.debugMessage("deleteAllow:" + deleteAllow, Log.DEBUGLEVEL03);
		managerMainFrame.remove.setEnabled(enabled && deleteAllow);
		managerMainFrame.cut.setEnabled(enabled && cutAllow);
	}
}

