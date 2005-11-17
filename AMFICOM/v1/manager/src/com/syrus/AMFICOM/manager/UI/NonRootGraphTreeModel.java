/*-
 * $Id: NonRootGraphTreeModel.java,v 1.7 2005/11/17 09:00:35 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.UI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.perspective.Perspective;

/**
 * @version $Revision: 1.7 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NonRootGraphTreeModel implements TreeModel {

	private List<TreeModelListener>	treeModelListeners;

	List<MPort>						firstLevel;

	protected GraphModel			model;

	protected TreeNode				generalRoot;

	protected TreeNode				root;

	private final ManagerMainFrame	managerMainFrame;

	public NonRootGraphTreeModel(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
		this.model = managerMainFrame.graphModel;

		this.generalRoot = new DefaultMutableTreeNode(".");
		this.firstLevel = new ArrayList<MPort>();
		
		this.model.addGraphModelListener(new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {				
				e.getChange();
				
				refreshFirstLevel();				
			}
		});
	}
	
	void refreshFirstLevel() {
		this.firstLevel.clear();
		
		final Set<AbstractBean> layoutBeans = this.getLayoutBeans();
//		assert Log.debugMessage(layoutBeans, Log.DEBUGLEVEL03);

		MPort rootPort = (MPort) (this.root != null ? this.root.getChildAt(0) : null); 
//		assert Log.debugMessage(rootPort, Log.DEBUGLEVEL03);

		for(int i=0;i<this.model.getRootCount();i++) {
		TreeNode node = (TreeNode) this.model.getRootAt(i);
		if (node.getChildCount() > 0) {
			node = node.getChildAt(0);
			if(this.model.isPort(node)) {
					MPort port = (MPort) node;
					if (port == rootPort) {
						continue;
					}
					
					final AbstractBean bean = port.getBean();
					if (bean == null || layoutBeans != null && !layoutBeans.contains(bean)) {
						continue;
					}
					
//					assert Log.debugMessage("port:" + port , Log.DEBUGLEVEL03);
					
					final List<Port> targets = port.getTargets();
//					assert Log.debugMessage(targets, Log.DEBUGLEVEL03);
					for (Port port2 : targets) {
						
//						System.out.println(".refreshFirstLevel() | target:" + port2 + ", " + rootPort);
						
						if (port2 == rootPort) {									
							this.firstLevel.add(port);
//							assert Log.debugMessage("add:" + port, Log.DEBUGLEVEL03);
						}
					}
					
					if (targets.isEmpty() 
//							&& this.root == null
							) {
//						assert Log.debugMessage("targets is empty" , Log.DEBUGLEVEL03);
						this.firstLevel.add(port);
//						System.out.println(".refreshFirstLevel() | sources empty, add:" + port);
					}
				
				}
			}
		}
		
//		assert Log.debugMessage("firstLevel:" + this.firstLevel, Log.DEBUGLEVEL03);
		
		this.reload((TreeNode) this.getRoot());
	}

	public void addTreeModelListener(TreeModelListener listener) {
		if (this.treeModelListeners == null) {
			this.treeModelListeners = new LinkedList<TreeModelListener>();
		}

		if (!this.treeModelListeners.contains(listener)) {
			this.treeModelListeners.add(listener);
		}

	}

	public void removeTreeModelListener(TreeModelListener listener) {
		if (this.treeModelListeners == null) { return; }

		if (this.treeModelListeners.contains(listener)) {
			this.treeModelListeners.remove(listener);
		}
	}

	public Object getChild(	Object parent,
							int index) {		
		TreeNode node = null;
		final Set<AbstractBean> layoutBeans = this.getLayoutBeans();
		if (parent == this.getRoot()) {			
			node = this.firstLevel.get(index).getParent();
		} else {
			MPort port = (MPort) ((TreeNode)parent).getChildAt(0);
			List<Port> targets = port.getSources();
			int count = 0;
			for(Port port2: targets) {
				MPort mport2 = (MPort)port2;
				AbstractBean bean = mport2.getBean();
				if (bean == null) {
					continue;
				}

				if (layoutBeans != null && !layoutBeans.contains(bean)) {
					continue;
				}

				if(count == index) {
					node = mport2.getParent();
					break;
				}
				count++;
			}
//			node = ((TreeNode) (this.direct ? port.getTargets() : port.getSources()).get(index)).getParent();	
		}
		
		return node;
	}

	public int getChildCount(Object parent) {
//		assert Log.debugMessage("parent:" + parent, Log.DEBUGLEVEL03);
		
		int count = 0;
		final Set<AbstractBean> layoutBeans = this.getLayoutBeans();
		if (parent == this.getRoot()) {
			count = this.firstLevel.size();
//			assert Log.debugMessage("parent == root" , Log.DEBUGLEVEL10);
		} else {
			MPort port = (MPort) ((TreeNode)parent).getChildAt(0);
			List<Port> targets = port.getSources();
			for(Port port2: targets) {
				final MPort mport2 = (MPort)port2;
				final AbstractBean bean = mport2.getBean();

				if (bean == null || layoutBeans != null && !layoutBeans.contains(bean)) {
					continue;
				}
//				assert Log.debugMessage("bean:" + bean, Log.DEBUGLEVEL03);
				count++;
			}
//			count = (this.direct ? port.getTargets() : port.getSources()).size();			
		}
		
//		assert Log.debugMessage("parent:" + parent + ", count:" + count , Log.DEBUGLEVEL03);
		
		return count;
	}

	private Set<AbstractBean> getLayoutBeans() {
		final Perspective perspective = this.managerMainFrame.perspective;
		return perspective != null ? perspective.getLayoutBeans() : null;
	}

	public int getIndexOfChild(	Object parent,
								Object child) {
		
//		System.out.println("NonRootGraphTreeModel.getIndexOfChild() | parent:" + parent + '[' + parent.getClass().getName() + ']' 
//				+", child:" + child + '[' + child.getClass().getName() + ']');
		final Set<AbstractBean> layoutBeans = this.getLayoutBeans();
		TreeNode node = (TreeNode)child;
		node = this.model.isPort(node) ? node : node.getChildAt(0);
		int index = -1;

		if (parent == this.getRoot()) {	
			index = this.firstLevel.indexOf(node);
		} else {
			MPort port = (MPort) (this.model.isPort(parent) ? parent : ((TreeNode)parent).getChildAt(0));
			List<Port> targets = port.getSources();
			for(Port port2: targets) {
				final MPort mport2 = (MPort)port2;
				final AbstractBean bean = mport2.getBean();

				if (bean == null || layoutBeans != null && !layoutBeans.contains(bean)) {
					continue;
				}
				if (port2 == child) {
					break;
				}
				index++;
			}
//			index = (this.direct ? port.getSources() : port.getTargets()).indexOf(node);
		}
		
		return index;
	}

	public final Object getRoot() {
		return this.root == null ? this.generalRoot : this.root;
	}
	
	public final void setRoot(final TreeNode root) {
		this.root = root;			
		this.refreshFirstLevel();
	}

	public boolean isLeaf(Object node) {
		return this.getChildCount(node) == 0;
	}

	public void valueForPathChanged(TreePath path,
									Object newValue) {
		// TODO Auto-generated method stub

	}
	
	public void reload(final TreeNode node) {
		if (node != null) {
			this.fireTreeStructureChanged(this, this.getPathToRoot(node), null, null);
		}
	}
	
	protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
		TreeModelEvent e = null;
		for (TreeModelListener listener : this.treeModelListeners) {
			if (e == null) {
				e = new TreeModelEvent(source, path, childIndices, children);
			}
			listener.treeStructureChanged(e);
		}
	}
	
	public TreeNode[] getPathToRoot(TreeNode aNode) {
//		System.out.println("NonRootGraphTreeModel.getPathToRoot() | aNode:" + aNode + ", " + (aNode == this.getRoot()));
		TreeNode[] pathToRoot = this.getPathToRoot((aNode == this.getRoot() ? aNode :  
				this.model.isPort(aNode) ? aNode.getParent() : aNode), 0);
//		for (int i = 0; i < pathToRoot.length; i++) {
//			System.out.println("NonRootGraphTreeModel.getPathToRoot() | i:" + i + ", " + pathToRoot[i]);
//		}
		return pathToRoot;
	}

	protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
//		System.out.println("NonRootGraphTreeModel.getPathToRoot() | aNode:" + aNode + '[' + aNode.getClass().getName() + "], depth:" + depth);

		TreeNode[] retNodes = null;


		// This method recurses, traversing towards the root in order
		// size the array. On the way back, it fills in the nodes,
		// starting from the root and working back to the original node.

		/*
		 * Check for null, in case someone passed in a null node, or they passed
		 * in an element that isn't rooted at root.
		 */
		if (aNode == null) {
			if (depth == 0) { return null; }
			retNodes = new TreeNode[depth];
		} else {
			depth++;
			if (aNode == this.getRoot()) {
				retNodes = new TreeNode[depth];
			} else {
				MPort port = this.model.isPort(aNode) ? (MPort) aNode : (MPort) aNode.getChildAt(0);
				List<Port> sources = port.getTargets();

				if (!sources.isEmpty()) {
					retNodes = this.getPathToRoot(((MPort) sources.get(0)).getParent(), depth);
				} else {
//					System.err.println("NonRootGraphTreeModel.getPathToRoot() | create [" + (depth + 1) + "], [0] is " + this.getRoot());
					retNodes = new TreeNode[depth + 1];
					retNodes[0] = (TreeNode) this.getRoot();
				}

			}
			
//			System.out.println("NonRootGraphTreeModel.getPathToRoot() | retNodes.length - depth:" + (retNodes.length - depth) + ", " + aNode);
			retNodes[retNodes.length - depth] = aNode;
		}		

		return retNodes;
	}	

}
