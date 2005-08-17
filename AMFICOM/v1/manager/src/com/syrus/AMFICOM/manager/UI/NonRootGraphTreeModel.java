/*-
 * $Id: NonRootGraphTreeModel.java,v 1.3 2005/08/17 15:59:40 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.UI;

import java.util.ArrayList;
import java.util.HashSet;
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

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.MPort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class NonRootGraphTreeModel implements TreeModel {

	private List<TreeModelListener>	treeModelListeners;

	List<MPort>						firstLevel;

	Set<String>						availableCodenames;

	boolean							direct;

	protected GraphModel			model;

	protected TreeNode				generalRoot;

	protected TreeNode				root;

	public NonRootGraphTreeModel(final GraphModel model, boolean direct) {
		this.model = model;
		this.direct = direct;

		this.generalRoot = new DefaultMutableTreeNode("'/'");
		this.firstLevel = new ArrayList<MPort>();
		
		this.availableCodenames = new HashSet<String>();
		
		this.model.addGraphModelListener(new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {
				e.getChange();
				
				NonRootGraphTreeModel.this.refreshFirstLevel();

				
			}
		});
	}
	
	void refreshFirstLevel() {
		this.firstLevel.clear();
		
		for(int i=0;i<this.model.getRootCount();i++) {
		TreeNode node = (TreeNode) this.model.getRootAt(i);
		if (node.getChildCount() > 0) {
			node = node.getChildAt(0);
			if(this.model.isPort(node)) {
				
					MPort port = (MPort) node;
					
					MPort rootPort = (MPort) (this.root != null ? this.root.getChildAt(0) : null); 

					AbstractBean bean = port.getBean();
					if (bean == null) {
						continue;
					}
					
					boolean found = false;
					String beanCodeName = bean.getCodeName();
					for(String codename : this.availableCodenames) {
						if (beanCodeName.startsWith(codename)) {
							found = true;
							break;
						}
					}
					if (!found) {
						continue;
					}
					
//					System.out.println(".refreshFirstLevel() | port:" + port);
					
					List<Port> sources = this.direct ? port.getSources() : port.getTargets(); 
					for (Port port2 : sources) {
						
//						System.out.println(".refreshFirstLevel() | source:" + port2 + ", " + rootPort);
						
						if (port2 == rootPort) {									
							this.firstLevel.add(port);
//							System.out.println(".refreshFirstLevel() | add:" + port);
						}
					}
					
					if (sources.isEmpty() 
//							&& this.root == null
							) {
						this.firstLevel.add(port);
//						System.out.println(".refreshFirstLevel() | sources empty, add:" + port);
					}
				
				}
			}
		}
		
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
//		System.out.println("NonRootGraphTreeModel.getChild() | parent:" + parent + ", index:" + index);
		
		TreeNode node = null;
		
		if (parent == this.getRoot()) {			
			node = this.firstLevel.get(index).getParent();
		} else {
			MPort port = (MPort) ((TreeNode)parent).getChildAt(0);
			List<Port> targets = this.direct ? port.getTargets() : port.getSources();
			int count = 0;
			for(Port port2: targets) {
				MPort mport2 = (MPort)port2;
				AbstractBean bean = mport2.getBean();
				if (bean == null) {
					continue;
				}
				
				boolean found = false;
				String beanCodeName = bean.getCodeName();
				for(String codename : this.availableCodenames) {
					if (beanCodeName.startsWith(codename)) {
						found = true;
						break;
					}
				}
				if (!found) {
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
		
//		System.out.println("NonRootGraphTreeModel.getChildCount() | parent:" + parent);
		
		int count = 0;

		if (parent == this.getRoot()) {
			count = this.firstLevel.size();
		} else {
			MPort port = (MPort) ((TreeNode)parent).getChildAt(0);
			List<Port> targets = this.direct ? port.getTargets() : port.getSources();
			for(Port port2: targets) {
				MPort mport2 = (MPort)port2;
				AbstractBean bean = mport2.getBean();
				if (bean == null) {
					continue;
				}
				
				boolean found = false;
				String beanCodeName = bean.getCodeName();
				for(String codename : this.availableCodenames) {
					if (beanCodeName.startsWith(codename)) {
						found = true;
						break;
					}
				}
				if (!found) {
					continue;
				}
				count++;
			}
//			count = (this.direct ? port.getTargets() : port.getSources()).size();			
		}

//		System.out.println("NonRootGraphTreeModel.getChildCount() | parent:" + parent + ", count:" + count);
		
		return count;
	}

	public int getIndexOfChild(	Object parent,
								Object child) {
		
//		System.out.println("NonRootGraphTreeModel.getIndexOfChild() | parent:" + parent + '[' + parent.getClass().getName() + ']' 
//				+", child:" + child + '[' + child.getClass().getName() + ']');
		
		TreeNode node = (TreeNode)child;
		node = this.model.isPort(node) ? node : node.getChildAt(0);
		int index = -1;

		if (parent == this.getRoot()) {	
			index = this.firstLevel.indexOf(node);
		} else {
			MPort port = (MPort) (this.model.isPort(parent) ? parent : ((TreeNode)parent).getChildAt(0));
			List<Port> targets = this.direct ? port.getTargets() : port.getSources();
			for(Port port2: targets) {
				MPort mport2 = (MPort)port2;
				AbstractBean bean = mport2.getBean();
				if (bean == null) {
					continue;
				}
				
				boolean found = false;
				String beanCodeName = bean.getCodeName();
				for(String codename : this.availableCodenames) {
					if (beanCodeName.startsWith(codename)) {
						found = true;
						break;
					}
				}
				if (!found) {
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
	
	public final void setRoot(TreeNode root) {
		this.root = root;
		this.refreshFirstLevel();
	}

	public boolean isLeaf(Object node) {
		return this.getChildCount(node) == 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath,
	 *      java.lang.Object)
	 */
	public void valueForPathChanged(TreePath path,
									Object newValue) {
		// TODO Auto-generated method stub

	}
	
	public void reload(TreeNode node) {
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
				List<Port> sources = this.direct ? port.getSources() : port.getTargets();

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
	
	public final boolean isDirect() {
		return this.direct;
	}
	
	public final void setDirect(boolean direct) {
		this.direct = direct;
	}

	
	/**
	 * @param availableCodenames The availableCodename to set.
	 */
	public final void setAvailableCodenames(Set<String> availableCodenames) {
		this.availableCodenames.clear();
		if (availableCodenames != null) {
			this.availableCodenames.addAll(availableCodenames);
		}
		this.refreshFirstLevel();
	}
	
	public final void addAvailableCodename(String availableCodename) {
		this.availableCodenames.add(availableCodename);
		this.refreshFirstLevel();
	}
	
	public final void removeAllAvailableCodenames() {
		this.availableCodenames.clear();
		this.refreshFirstLevel();
	}
}
