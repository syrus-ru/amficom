/*-
* $Id: ManagerGraphSelectionListener.java,v 1.10 2005/12/22 14:27:28 bob Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/12/22 14:27:28 $
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
//		final long time0 = System.currentTimeMillis();
		if (managerMainFrame.arranging) {
			return;
		}
		assert Log.debugMessage(e, Log.DEBUGLEVEL03);
		final JInternalFrame frame = 
			(JInternalFrame) managerMainFrame.frames.get(ManagerMainFrame.PROPERTIES_FRAME);
		frame.setTitle(I18N.getString(ManagerMainFrame.PROPERTIES_FRAME));
		if (managerMainFrame.beanUI != null) {
			managerMainFrame.beanUI.disposePropertyPanel();
		}
		
		
		final JPanel panel = managerMainFrame.propertyPanel;
		panel.removeAll();
//		final long time1 = System.currentTimeMillis();
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
//					final long time01 = System.currentTimeMillis();
					final TreeNode[] pathToRoot = treeModel.getPathToRoot((TreeNode) cell);
//					final long time02 = System.currentTimeMillis();
//					final long time03;
					if (pathToRoot != null) {
						final TreePath treePath = new TreePath(pathToRoot);
//						assert Log.debugMessage("3:" + treePath, Log.DEBUGLEVEL03);
						selectionModel.setSelectionPath(treePath);

//						time03 = System.currentTimeMillis();
//						assert Log.debugMessage("it takes setSelectionPath (time03-time02) " + (time03-time02) + " ms", Log.DEBUGLEVEL03);
					} else {
						selectionModel.clearSelection();
//						time03 = System.currentTimeMillis();
//						assert Log.debugMessage("it takes clearSelection (time03-time02) " + (time03-time02) + " ms", Log.DEBUGLEVEL03);
					}
//					final long time04 = System.currentTimeMillis();
//					assert Log.debugMessage("it takes getPathToRoot (time02-time01) " + (time02-time01) + " ms", Log.DEBUGLEVEL03);
//					assert Log.debugMessage("it takes getPathToRoot (time04-time03) " + (time04-time03) + " ms", Log.DEBUGLEVEL03);
				}
				final MPort port = 
					(model.isPort(cell)) ? (MPort)cell : (MPort)((DefaultGraphCell)cell).getChildAt(0);				
				final Object userObject = port.getUserObject();
				final long time01 = System.currentTimeMillis();
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
						deleteAllow &= abstractBean.isDeletable();
						deleteAllow &= !undeletable;
						cutAllow |= perspective.isCuttable(abstractBean);
						if (parentLayoutItem != null) {	
							try {
	
								final boolean parent = graphRoutines.getBean(parentLayoutItem) == abstractBean;
//								final long time0101 = System.currentTimeMillis();
//								assert Log.debugMessage("it takes (time0101-time01) " + (time0101-time01) + " ms", Log.DEBUGLEVEL03);
								if (parent) {
									assert Log.debugMessage(abstractBean + " is a parent ", Log.DEBUGLEVEL10);
								}

								deleteAllow &= !parent;
	
								managerMainFrame.beanUI = perspective.getBeanUI(abstractBean.getCodename());
								final JPanel propertyPanel2 = managerMainFrame.beanUI.getPropertyPanel(abstractBean);
								if (propertyPanel2 != null) {
//									final long time011 = System.currentTimeMillis();
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
//									final long time012 = System.currentTimeMillis();
//									assert Log.debugMessage("it takes (time012-time011) " + (time012-time011) + " ms", Log.DEBUGLEVEL03);
								}
							} catch (ApplicationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
//				final long time02 = System.currentTimeMillis();
//				assert Log.debugMessage("it takes (time02-time01) " + (time02-time01) + " ms", Log.DEBUGLEVEL03);
			} else {
				selectionModel.clearSelection();
			}
		}
//		final long time2 = System.currentTimeMillis();
		panel.revalidate();
		panel.repaint();
//		final long time3 = System.currentTimeMillis();
		boolean enabled = !managerMainFrame.graph.isSelectionEmpty();
		managerMainFrame.remove.setEnabled(enabled && deleteAllow);
		managerMainFrame.cut.setEnabled(enabled && cutAllow);
//		final long time10 = System.currentTimeMillis();
//		assert Log.debugMessage("it takes  (time1-time0) " + (time1-time0) + " ms", Log.DEBUGLEVEL03);
//		assert Log.debugMessage("it takes  (time2-time1) " + (time2-time1) + " ms", Log.DEBUGLEVEL03);
//		assert Log.debugMessage("it takes  (time3-time2) " + (time3-time2) + " ms", Log.DEBUGLEVEL03);
//		assert Log.debugMessage("it takes  (time10-time3) " + (time10-time3) + " ms", Log.DEBUGLEVEL03);
	}
}

