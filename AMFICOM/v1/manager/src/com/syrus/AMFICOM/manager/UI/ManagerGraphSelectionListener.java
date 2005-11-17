/*-
* $Id: ManagerGraphSelectionListener.java,v 1.4 2005/11/17 09:00:35 bob Exp $
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
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/17 09:00:35 $
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
	public void valueChanged(GraphSelectionEvent e) {		
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
		final NonRootGraphTreeModel treeModel = managerMainFrame.getTreeModel();
		
		final Perspective perspective = managerMainFrame.getPerspective();
		
		boolean deleteAllow = true;
		
		if (model.isEdge(cell)) {
			if (e.isAddedCell()) {
				Edge edge = (Edge)cell;
				TreeNode sourceNode = (TreeNode) edge.getSource();
				TreeNode targetNode = (TreeNode) edge.getSource();
				if (sourceNode != null) {
					managerMainFrame.tree.scrollPathToVisible(new TreePath(treeModel.getPathToRoot(sourceNode)));
				}
				if (targetNode != null) {
					managerMainFrame.tree.scrollPathToVisible(new TreePath(treeModel.getPathToRoot(targetNode)));
				}
			} else {
				selectionModel.clearSelection();
			}
		} else {				
			if (e.isAddedCell()){
				if (!model.isPort(cell) && !model.isEdge(cell)) {
					TreeNode[] pathToRoot = treeModel.getPathToRoot((TreeNode) cell);
					if (pathToRoot != null) {
						TreePath path = new TreePath(pathToRoot);
						selectionModel.setSelectionPath(path);
					} else {
						selectionModel.clearSelection();
					}
					
				}
				MPort port = (model.isPort(cell)) ? (MPort)cell : (MPort)((DefaultGraphCell)cell).getChildAt(0);				
				final Object userObject = port.getUserObject();
				
				if (userObject instanceof AbstractBean) {
					final AbstractBean abstractBean = (AbstractBean)userObject;
					if (perspective.isSupported(abstractBean)) {
						deleteAllow &= perspective.isDeletable(abstractBean);
	
						try {
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
			} else {
				selectionModel.clearSelection();
			}
		}

		panel.revalidate();
		panel.repaint();
		
		boolean enabled = !managerMainFrame.graph.isSelectionEmpty();
		
		managerMainFrame.remove.setEnabled(enabled && deleteAllow);
	}
}

