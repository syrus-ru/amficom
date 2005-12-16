/*-
* $Id: PerspectiveTreeModel.java,v 1.11 2005/12/16 09:35:16 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.util.Log;

/**
 * 
 * TODO rebuild moving methods to nodes (pattern visitor)
 * 
 * @version $Revision: 1.11 $, $Date: 2005/12/16 09:35:16 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class PerspectiveTreeModel implements TreeModel {

	static final Level LOGLEVEL = Log.DEBUGLEVEL10;
	
	/** Root of the tree. */
    protected DefaultMutableTreeNode root;
	
    /** Listeners */
	private List<TreeModelListener>	treeModelListeners;

	private final ManagerMainFrame	managerMainFrame;

	private List<PerspectiveMutableTreeNode> rootItems;
	
	public PerspectiveTreeModel(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;
		this.root = new DefaultMutableTreeNode("Administration");
		this.rootItems = new ArrayList<PerspectiveMutableTreeNode>();
	}	
	
	private ManagerHandler getManagerHandler() {
		return this.managerMainFrame.getManagerHandler();
	}
	
	public void fillRootItems() {
		final Collection<Perspective> perspectives = 
			this.getManagerHandler().getPerspectives();
		this.rootItems.clear();
		for (final Perspective perspective : perspectives) {
			assert Log.debugMessage(perspective, LOGLEVEL);
			final PerspectiveMutableTreeNode perspectiveNode = 
				new PerspectiveMutableTreeNode(perspective);
			this.rootItems.add(perspectiveNode);
		}
		this.reload();
	}
	
	public Object getRoot() {
		return this.root;
	}
	
	private List<ActionMutableTreeNode> getActions(final Perspective perspective){
		try {
			return perspective.getActionNodes();
		} catch (final ApplicationException e) {
//			 TODO Auto-generated catch block
			return Collections.emptyList();
		}
	}
	
	public Object getChild(	final Object parent,
	                       	final int index) {
		if (parent == this.root) {			
			return this.rootItems.get(index);
		}		
		
		if (parent instanceof PerspectiveMutableTreeNode) {			
			final PerspectiveMutableTreeNode perspectiveNode = 
				(PerspectiveMutableTreeNode) parent;			
			final List<ManagerGraphCell> firstLevel = perspectiveNode.getFirstLevel();
			final int size = firstLevel.size();
			if (index < size) {
				final ManagerGraphCell cell = firstLevel.get(index);
				return cell;
			}
			final Perspective perspective = perspectiveNode.getPerspective();
			final List<ActionMutableTreeNode> actions = this.getActions(perspective);
			return actions.get(index - size);
		}
		if (parent instanceof ManagerGraphCell) {
			final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();
			final ManagerGraphCell cell = (ManagerGraphCell) parent;
			final Perspective perspective = cell.getPerspective();
			final List<AbstractBean> layoutBeans = 
				perspective.getLayoutBeans();
//			assert Log.debugMessage("parent:" + parent, LOGLEVEL);
//			assert Log.debugMessage(layoutBeans, LOGLEVEL);
			final MPort port = (MPort) cell.getChildAt(0);
			final List<Port> sources = port.getSources();
			int count = 0;
			ManagerGraphCell node = null;
			for(final Port port2: sources) {
				final MPort mport2 = (MPort)port2;
				final AbstractBean bean = mport2.getBean();
//				assert Log.debugMessage("1:" + bean, LOGLEVEL);
				if (bean == null) {
					continue;
				}
//				assert Log.debugMessage("2:" + bean, LOGLEVEL);
				if (layoutBeans != null && !layoutBeans.contains(bean)) {
					continue;
				}

				node = (ManagerGraphCell) mport2.getParent();
				if (cellBuffer.isExists(node)) {
					node = null;
					continue;
				}
				
//				assert Log.debugMessage("3:" + bean, LOGLEVEL);
				if(count == index) {
//					assert Log.debugMessage("node found", LOGLEVEL);
					break;
				}
				
				node = null;
				
				count++;
			}

			if (node == null) {
				final AbstractBean bean = port.getBean();
				final Perspective subPerspective = perspective.getSubPerspective(bean);
				if (subPerspective != null) {
					final LayoutItem parentLayoutItem = 
						subPerspective.getParentLayoutItem();
					final GraphRoutines graphRoutines = 
						this.managerMainFrame.getGraphRoutines();
					final ManagerGraphCell graphCell = 
						graphRoutines.getDefaultGraphCell(parentLayoutItem, false);
					final AbstractBean bean2 = graphCell.getAbstractBean();
					if (!bean.getCodename().equals(bean2.getCodename())) {
						return graphCell;
					}
					final Object child = this.getChild(graphCell, index);
					if (child != null) {
						return child;
					}
					final int childCount = this.getChildCount(graphCell);
					assert Log.debugMessage(
							"parent:" + parent
							+ ", childCount:" 
							+ childCount
							+ " "
							+ graphCell
							+ ", count:" + count, 
						LOGLEVEL);
					
					final List<ActionMutableTreeNode> actions = 
						this.getActions(subPerspective);
					
					return actions.get(index - count);
				}
				
				final List<ActionMutableTreeNode> actions = this.getActions(perspective);
//				assert Log.debugMessage(actions, LOGLEVEL);
				return actions.get(index - count);
				
			}
			
			return node;
		}
		
		return null;
	}

	public int getChildCount(final Object parent) {
		if (parent == this.root) {
			return this.rootItems.size();
		}
//		assert Log.debugMessage(parent, LOGLEVEL);
		if (parent instanceof PerspectiveMutableTreeNode) {
			final PerspectiveMutableTreeNode perspectiveNode = 
				(PerspectiveMutableTreeNode) parent;
			final List<ManagerGraphCell> firstLevel = perspectiveNode.getFirstLevel();
			final Perspective perspective = perspectiveNode.getPerspective();
			
			final LayoutItem parentLayoutItem = perspective.getParentLayoutItem();
			
			if (parentLayoutItem != null) {
				return firstLevel.size();
			}
			
			final List<ActionMutableTreeNode> actions = this.getActions(perspective);			
			final int count = firstLevel.size() + actions.size();
			return count;
		}
		if (parent instanceof ManagerGraphCell) {
			final ManagerGraphCell cell = (ManagerGraphCell) parent;
			final Perspective perspective = cell.getPerspective();
			
			int count = this.getChildCount(cell);
			
			final AbstractBean bean = cell.getAbstractBean();
			final Perspective subPerspective = perspective.getSubPerspective(bean);
			if (subPerspective != null) {
				final LayoutItem parentLayoutItem = subPerspective.getParentLayoutItem();
				final GraphRoutines graphRoutines = 
					this.managerMainFrame.getGraphRoutines();
				assert parentLayoutItem != null : "parentLayoutItem of " + subPerspective + " is null";
				final ManagerGraphCell graphCell = 
					graphRoutines.getDefaultGraphCell(parentLayoutItem, false);				
				final AbstractBean bean2 = graphCell.getAbstractBean();
				if (bean.getCodename().equals(bean2.getCodename())) {
					final List<ActionMutableTreeNode> actions = this.getActions(subPerspective);
					count += this.getChildCount(graphCell) + actions.size();
				} else {
					count++;
				}
			}
			
			final LayoutItem parentLayoutItem = perspective.getParentLayoutItem();
			
			final List<ActionMutableTreeNode> actions = this.getActions(perspective);
//			assert Log.debugMessage(actions, LOGLEVEL);
			if (parentLayoutItem != null) {
				final GraphRoutines graphRoutines = 
					this.managerMainFrame.getGraphRoutines();
				final ManagerGraphCell graphCell = 
					graphRoutines.getDefaultGraphCell(parentLayoutItem, false);
				if (graphCell == cell) {
					count += actions.size();
				}
			}
//			assert Log.debugMessage("total childCount of " + parent + " is " + count, LOGLEVEL);
			return count;
			
		}
		return 0;
	}
	
	private final int getChildCount(final ManagerGraphCell cell) {
		final CellBuffer cellBuffer = this.managerMainFrame.getCellBuffer();
		final Perspective perspective = cell.getPerspective();
		final List<AbstractBean> layoutBeans = 
			perspective.getLayoutBeans();
		
		final MPort port = (MPort) cell.getChildAt(0);
		final List<Port> sources = port.getSources();
		
		int count = 0;
		for(final Port port2: sources) {
			final MPort mport2 = (MPort)port2;
			final AbstractBean bean = mport2.getBean();
			if (bean == null || bean == null) {
				continue;
			}
		
			if (layoutBeans != null && !layoutBeans.contains(bean)) {
				continue;
			}
			
			final ManagerGraphCell parent = (ManagerGraphCell) mport2.getParent();
			final boolean exists = cellBuffer.isExists(parent);
//			assert Log.debugMessage(parent + " , " + exists, LOGLEVEL);
			if(exists){
				continue;
			}
			count++;
		}
//		assert Log.debugMessage(cell + " > " + count, LOGLEVEL);
		return count;
	}
	
	public boolean isLeaf(final Object node) {
		return this.getChildCount(node) == 0;
	}

	public void valueForPathChanged(final TreePath path,
			final Object newValue) {
		// nothing
	}

	public int getIndexOfChild(final Object parent,
			final Object child) {
		assert Log.debugMessage("parent:" 
				+ parent 
				+", child:"
				+ child, 
			LOGLEVEL);
		if (parent == this.root) {
			final int indexOf = this.rootItems.indexOf(child);
			assert Log.debugMessage("1:" + "index of " + child + '@' + parent + " is " + indexOf, LOGLEVEL);
			return indexOf;
		}
		if (parent instanceof PerspectiveMutableTreeNode) {
			final PerspectiveMutableTreeNode perspectiveNode = 
				(PerspectiveMutableTreeNode) parent;
			final List<ManagerGraphCell> firstLevel = perspectiveNode.getFirstLevel();
			
			final int firstLevelIndex = firstLevel.indexOf(child);
			if (firstLevelIndex >= 0) {
				assert Log.debugMessage("2:"  + "index of " + child + '@' + parent + " is " + firstLevelIndex, LOGLEVEL);
				return firstLevelIndex;
			}
			
			final Perspective perspective = perspectiveNode.getPerspective();
			final List<ActionMutableTreeNode> actions = this.getActions(perspective);
			final int indexOf = actions.indexOf(child);
			assert Log.debugMessage("3:"  + "index of " + child + '@' + parent + " is " +  indexOf, LOGLEVEL);
			return indexOf;
		}
		if (parent instanceof ManagerGraphCell) {	
			
			final ManagerGraphCell cell = (ManagerGraphCell) parent;
			final Perspective perspective = cell.getPerspective();
			assert Log.debugMessage("parent:" 
						+ parent + " > " 
						+ cell + ", perspective: " 
						+ cell.getPerspective(), 
					LOGLEVEL);
			final List<AbstractBean> layoutBeans = 
				perspective.getLayoutBeans();
			final MPort port = (MPort) cell.getChildAt(0);
			final List<Port> sources = port.getSources();
			int count = 0;
			for(final Port port2: sources) {
				final MPort mport2 = (MPort)port2;
				final AbstractBean bean = mport2.getBean();
				if (bean == null) {
					continue;
				}

				if (layoutBeans != null && !layoutBeans.contains(bean)) {
					continue;
				}

				final ManagerGraphCell node = (ManagerGraphCell) mport2.getParent();
//				assert Log.debugMessage(node + ", " + node.getPerspective(), LOGLEVEL);
				if (node == child) {
					assert Log.debugMessage("4:"  + "index of " + child + '@' + parent + " is " +  count, LOGLEVEL);
					return count;
				}
				
				count++;
			}

			if (child instanceof ManagerGraphCell) {
				final ManagerGraphCell childCell = (ManagerGraphCell) child;
//				assert Log.debugMessage("child:" + child + " > "+ (childCell).getPerspective(), LOGLEVEL);
				
				final Perspective subPerspective = childCell.getPerspective();
				assert Log.debugMessage(childCell + ", " + subPerspective, LOGLEVEL);
				if (subPerspective != null) {
					final LayoutItem parentLayoutItem = subPerspective.getParentLayoutItem();
					final GraphRoutines graphRoutines = 
						this.managerMainFrame.getGraphRoutines();
					final ManagerGraphCell graphCell = 
						graphRoutines.getDefaultGraphCell(parentLayoutItem, false);
					assert Log.debugMessage("graphCell:" + graphCell, LOGLEVEL);
					if (graphCell == childCell) {
						assert Log.debugMessage("4,5: index of " + child + '@' + parent + " is 0" , LOGLEVEL);
						// XXX
						final Set<ManagerGraphCell> defaultGraphCells = 
							graphRoutines.getDefaultGraphCells(childCell.getAbstractBean().getId());
						for (final ManagerGraphCell cell2 : defaultGraphCells) {
							if (cell2.getPerspective() == perspective) {
								return this.getIndexOfChild(parent, cell2);
							}
						}
						return 0;
					}
					final int index;					
					// do not enter with the same parent 
					if (parent == graphCell) {
						index = count;
					} else {
						index = this.getIndexOfChild(graphCell, child);
					}
					
					assert Log.debugMessage(" 5:"  + "index of " + child + '@' + parent + " is " +  index, LOGLEVEL);
					return index;
				}
			}
			
			
		}
		assert Log.debugMessage("index of " + child + '@' + parent + " cannot calculate, -1", LOGLEVEL);
		return -1;
	}

	public void addTreeModelListener(final TreeModelListener listener) {
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

	public void reload() {		
		this.reload(this.root);
		final Perspective perspective = this.managerMainFrame.getPerspective();
		final LayoutItem parentLayoutItem = perspective.getParentLayoutItem();
		if (parentLayoutItem != null) {
			final GraphRoutines graphRoutines = this.managerMainFrame.getGraphRoutines();
			final ManagerGraphCell defaultGraphCell = 
				graphRoutines.getDefaultGraphCell(parentLayoutItem);
			final GraphSelectionModel selectionModel = 
				this.managerMainFrame.graph.getSelectionModel();
			selectionModel.setSelectionCell(defaultGraphCell);
			
		}
	}
	
	public void reload(final TreeNode node) {
		if (node != null) {
			this.fireTreeStructureChanged(this, this.getPathToRoot(node), null, null);
		}
	}
	
	protected void fireTreeStructureChanged(final Object source, 
			final Object[] path, 
			final int[] childIndices, 
			final Object[] children) {
		TreeModelEvent e = null;
		if (this.treeModelListeners != null) {
			for (TreeModelListener listener : this.treeModelListeners) {
				if (e == null) {
					e = new TreeModelEvent(source, path, childIndices, children);
				}
				listener.treeStructureChanged(e);
			}
		}
	}
	
    public TreeNode[] getPathToRoot(final TreeNode aNode) {
        final TreeNode[] pathToRoot = this.getPathToRoot(aNode, 0);
//        if (Log.isLoggable(LOGLEVEL)) 
//        {
//        	assert Log.debugMessage("node:" + aNode, LOGLEVEL);
//			for (final TreeNode node : pathToRoot) {
//				assert Log.debugMessage(node, LOGLEVEL);
//			}
//		}
		return pathToRoot;
    }
	
    
    protected TreeNode[] getPathToRoot(final TreeNode aNode, int depth) {
        TreeNode[]              retNodes;
	// This method recurses, traversing towards the root in order
	// size the array. On the way back, it fills in the nodes,
	// starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
			if (depth == 0) { 
				return null; 
			}
			retNodes = new TreeNode[depth];
		} else {
			depth++;
			if (aNode == this.root) {
				retNodes = new TreeNode[depth];
			} else {
				retNodes = this.getPathToRoot(this.getParent(aNode), depth);
			}
			retNodes[retNodes.length - depth] = aNode;
		}
        return retNodes;
    }
    
    private TreeNode getParent(final TreeNode treeNode) {
    	if (treeNode instanceof PerspectiveMutableTreeNode) {
    		final TreeNode parent = this.root;
//    		assert Log.debugMessage("1:" + treeNode + ", " + parent, LOGLEVEL);
			return parent;
    	}
    	if (treeNode instanceof ManagerGraphCell) {
    		final ManagerGraphCell managerGraphCell = (ManagerGraphCell) treeNode;
    		final MPort port = managerGraphCell.getMPort();
    		final List<Port> targets = port.getTargets();
    		if (!targets.isEmpty()) {
    			final TreeNode parent = ((MPort)targets.get(0)).getParent();
//    			assert Log.debugMessage("2:" + treeNode + ", " + parent, LOGLEVEL);
				return parent;
    		}

    		final Perspective perspective = managerGraphCell.getPerspective();
    		final TreeNode parent = this.getParent(perspective);
    		if (parent instanceof ManagerGraphCell) {
    			AbstractBean bean = managerGraphCell.getAbstractBean();
    			final ManagerGraphCell cell2 = (ManagerGraphCell)parent;
    			AbstractBean bean2 = cell2.getAbstractBean();
    			if (bean2.getCodename().equals(bean.getCodename())) {
    				return this.getParent(parent);
    			}
    		}
//    		assert Log.debugMessage("3:" + treeNode + ", " + parent, LOGLEVEL);
			return parent;
    	}
    	return this.root;
    }
    
    private TreeNode getParent(final Perspective perspective) {
    	final Perspective superPerspective = perspective.getSuperPerspective();
		if (superPerspective == null) {
			for (final PerspectiveMutableTreeNode treeNode : this.rootItems) {
				if (treeNode.getPerspective() == perspective) {
//					assert Log.debugMessage("/0" + perspective + ", " + treeNode, LOGLEVEL);
					return treeNode;
				}
			}
//			assert Log.debugMessage("/1" + perspective + ", " + this.root, LOGLEVEL);
			return this.root;
		}
		final List<AbstractBean> layoutBeans = 
			superPerspective.getLayoutBeans();
		for (final AbstractBean bean : layoutBeans) {
			if (superPerspective.getSubPerspective(bean) == perspective) {
				final GraphRoutines graphRoutines = 
					this.managerMainFrame.getGraphRoutines();
				final ManagerGraphCell defaultGraphCell = 
					graphRoutines.getDefaultGraphCell(bean, superPerspective);
				return defaultGraphCell;
			}
		}
//		assert Log.debugMessage("/3" + perspective + ", " + this.root, LOGLEVEL);
		return this.root;
    }
	
	final class PerspectiveMutableTreeNode extends DefaultMutableTreeNode {
		private final Perspective	perspective;
		
		private final List<ManagerGraphCell>		firstLevel;
		
		private final GraphModel			model;

		public PerspectiveMutableTreeNode(final Perspective perspective) {
			super(perspective.getName(), true);
			this.perspective = perspective;
			this.model = managerMainFrame.graphModel;
			this.firstLevel = new ArrayList<ManagerGraphCell>();		
			perspective.addPropertyChangeListener(new PropertyChangeListener() {

				public void propertyChange(final PropertyChangeEvent evt) {
					assert Log.debugMessage(perspective, LOGLEVEL);
					updateParentItems();					
				}
			});
			this.updateParentItems();
		}
		
		public final Perspective getPerspective() {
			return this.perspective;
		}		
		
		public final List<ManagerGraphCell> getFirstLevel(){
			return this.firstLevel;
		}
		
		public final void updateParentItems() {
			final CellBuffer cellBuffer = managerMainFrame.getCellBuffer();
			assert Log.debugMessage(LOGLEVEL);
			this.firstLevel.clear();

			for(int i=0;i<this.model.getRootCount();i++) {
				final TreeNode node = (TreeNode) this.model.getRootAt(i);
				if (!this.model.isPort(node) && !this.model.isEdge(node)) {
					final ManagerGraphCell cell = (ManagerGraphCell) node;
					final Perspective perspective2 = cell.getPerspective();
					final MPort port = cell.getMPort();					
					if (perspective2 != this.perspective || cellBuffer.isExists(cell)) {
						continue;
					}
					assert Log.debugMessage(this.perspective + " > port: " +port, LOGLEVEL);
					final List<Port> targets = port.getTargets();
					boolean targetEmpty = true;
					for (final Port port2 : targets) {
						final MPort port3 = (MPort) port2;
						final ManagerGraphCell cell3 = (ManagerGraphCell) port3.getParent();
						if (port2 == null) {									
							this.firstLevel.add(cell);
						}
						
						if (targetEmpty && cell3.getPerspective() == this.perspective) {
							targetEmpty = false; 
						}
						
					}
					
					if (targetEmpty) {
						this.firstLevel.add(cell);
					}
				}
			}	
			
			assert Log.debugMessage(this.perspective + " > firstLevel: " + this.firstLevel, LOGLEVEL);
			
			reload(getRoot());
		}
	}
}

