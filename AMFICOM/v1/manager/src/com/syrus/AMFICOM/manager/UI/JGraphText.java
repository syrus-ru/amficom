package com.syrus.AMFICOM.manager.UI;

/*
 * $Id: JGraphText.java,v 1.7 2005/07/15 11:59:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.7 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.event.GraphModelEvent.GraphModelChange;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.PortView;
import org.jgraph.graph.ConnectionSet.Connection;

import com.syrus.AMFICOM.manager.ARMBeanFactory;
import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.LangModelManager;
import com.syrus.AMFICOM.manager.MCMBeanFactory;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.RTUBeanFactory;
import com.syrus.AMFICOM.manager.ServerBeanFactory;
import com.syrus.AMFICOM.manager.UserBeanFactory;

public class JGraphText {	
	
	JGraph graph;
	
	JTree tree;
	
	DefaultGraphCell rootItem; 
	
	private int edgeCount = 0;

	GraphTreeModel	treeModel;
	
	private JPanel panel;
	
	// Undo Manager
	protected GraphUndoManager undoManager;

	// Actions which Change State
	protected Action undo, redo, remove, group, ungroup, tofront, toback, cut,
			copy, paste;

	
	public JGraphText() {
		// Construct Model and Graph
		this.createRootItem();
		
		GraphModel model = new ManagerGraphModel(this.rootItem, false);
		
		this.graph = new JGraph(model,
			new GraphLayoutCache(model,
					new DefaultCellViewFactory(),
					true));
		
		//	Use a Custom Marquee Handler
		this.graph.setMarqueeHandler(new MyMarqueeHandler());

		// Control-drag should clone selection
		this.graph.setCloneable(true);

		// Enable edit without final RETURN keystroke
		this.graph.setInvokesStopCellEditing(true);

		// When over a cell, jump to its default port (we only have one, anyway)
		this.graph.setJumpToDefaultPort(true);
		
		this.createTreeModel();
		
		this.undoManager = new GraphUndoManager() {
			// Override Superclass
			public void undoableEditHappened(UndoableEditEvent e) {
				// First Invoke Superclass
				super.undoableEditHappened(e);
				// Then Update Undo/Redo Buttons
				updateHistoryButtons();
			}
		};

		// Add Listeners to Graph
		//
		// Register UndoManager with the Model
		this.graph.getModel().addUndoableEditListener(this.undoManager);
		
//		GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
//		graphLayoutCache.setVisible(this.rootItem, false);
		
		this.createModelListener();
		
	}
	
//	private void createChilden() {
//		int childCount = 0;
//		
//		String name = "Child" + (++childCount);
//		DefaultGraphCell[] cells = new DefaultGraphCell[5];
//		cells[0] = this.createChild(null, name, 160, 20, 20, 20, Color.CYAN, false);
//		
//		name = "Child" + (++childCount);
//		cells[1] = this.createChild(cells[0], name, 20, 160, 20, 20, Color.CYAN, false);
//
//		cells[2] = this.createChild(cells[1], "Icon", 160, 160, 0, 0, null, false);
//		
//		{
//			AttributeMap attributes = cells[2].getAttributes();
//			URL resource = JGraphText.class.getClassLoader().getResource("jgraph.gif");
//			if (resource != null) {
//				GraphConstants.setIcon(attributes, new ImageIcon(resource));
//				attributes.remove(GraphConstants.BORDER);
//				attributes.remove(GraphConstants.BORDERCOLOR);
//			}
//		}
//
//		name = "Child" + (++childCount);
//		cells[3] = this.createChild(cells[0], name, 260, 60, 20, 20, Color.CYAN, false);
//		
//		name = "Child" + (++childCount);
//		cells[4] = this.createChild(null, name, 280, 20, 20, 20, Color.CYAN, false);
//
//		GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
//		graphLayoutCache.insert(cells);	
//
//		this.graph.getSelectionModel().clearSelection();
//	}
	
	private void createModelListener() {
		this.graph.getModel().addGraphModelListener(new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {
				GraphModelChange change = e.getChange();
				boolean direct = JGraphText.this.treeModel.isDirect();
				Object[] inserted = change.getInserted();
				if (inserted != null) {
					for(Object insertedObject: inserted) {
						if (insertedObject instanceof Edge) {
							Edge edge = (Edge)insertedObject;
							Object source = edge.getSource();
							Object target = edge.getTarget();
							
							MutableTreeNode targetParent = (MutableTreeNode)((DefaultPort)(direct ? target : source)).getParent();
							MutableTreeNode sourceParent = (MutableTreeNode)((DefaultPort)(direct ? source : target)).getParent();
							System.out.println(".graphChanged() | source:" + sourceParent + ", target:" + targetParent);
							if (sourceParent != JGraphText.this.rootItem) {
								JGraphText.this.treeModel.removeNodeFromParent(targetParent);
							}
							JGraphText.this.treeModel.clearCache();
							JGraphText.this.treeModel.insertNodeInto(targetParent,
								sourceParent);
						}
					}
				} else {
				
					Object[] removed = change.getRemoved();
					if (removed != null) {
						for(Object removedObject: removed) {
							System.out.println(".graphChanged() | removedObject:" + removedObject);
						}
					}
					
					{
						
						
						ConnectionSet connectionSet = change.getConnectionSet();
						if (connectionSet != null) {
							// connections changed !
							Set connections = connectionSet.getConnections();
							for(Object oConnection: connections) {
								Connection connection = (Connection)oConnection;
								Edge edge = (Edge) connection.getEdge();
								
								DefaultPort oldPort = (DefaultPort) connection.getPort();							
								MutableTreeNode oldPortParent = (MutableTreeNode) oldPort.getParent();
	
								boolean source = connection.isSource();
								
								DefaultPort newPort = (DefaultPort) (source ? edge.getSource() : edge.getTarget());							
								MutableTreeNode newPortParent = (MutableTreeNode) newPort.getParent();
	
								System.out.println(".graphChanged() | oldPortParent:" + oldPortParent +", newPortParent:" + newPortParent
									+ ", edge:" + edge);
	
								DefaultPort sourcePort = (DefaultPort) (source ? edge.getTarget() : edge.getSource());							
								MutableTreeNode sourcePortParent = (MutableTreeNode) sourcePort.getParent();
								
								System.out.println(".graphChanged() | source is " + source);
								
								if (source) {
									// TODO unimplemented yet
								} else {
									JGraphText.this.treeModel.removeNodeFromParent(oldPortParent);
									JGraphText.this.treeModel.removeNodeFromParent(newPortParent);
									
									JGraphText.this.treeModel.clearCache();
									
									JGraphText.this.treeModel.insertNodeInto(newPortParent, sourcePortParent);								
									JGraphText.this.treeModel.insertNodeInto(oldPortParent, JGraphText.this.rootItem);
								}
								
							}
						} else {
							for(Object object: change.getChanged()) {
								TreeNode treeNode = (TreeNode)object;
								if (treeNode.getAllowsChildren()) {
									JGraphText.this.treeModel.nodeChanged(treeNode);
								}
							}
						}
					}
				}
				
//				JGraphText.this.treeModel.reload((TreeNode) JGraphText.this.treeModel.getRoot());
				
			}	
		});
	}
	
	JPanel getPanel() {
		if (this.panel == null) {
			this.panel = new JPanel(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			this.panel.add(this.createToolBar(), gbc);
			
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			this.panel.add(this.createEntityToolBar(), gbc);
			
			gbc.weightx = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(this.tree), new JScrollPane(this.graph));
			this.panel.add(splitPane, gbc);
		}
		
		return this.panel;
	}
	
	private JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar(SwingConstants.HORIZONTAL);
		toolBar.setFloatable(false);
		
		{
			// Toggle Connect Mode
			URL connectUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/connecton.gif");
			final ImageIcon connectonIcon = new ImageIcon(connectUrl);
			
			connectUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/connectoff.gif");
			
			final ImageIcon connectoffIcon = new ImageIcon(connectUrl);
			
			AbstractAction action = new AbstractAction("", connectonIcon) {
				public void actionPerformed(ActionEvent e) {
					graph.setPortsVisible(!JGraphText.this.graph.isPortsVisible());
					this.putValue(SMALL_ICON, JGraphText.this.graph.isPortsVisible() ? connectonIcon : connectoffIcon);
					this.putValue(SHORT_DESCRIPTION, LangModelManager.getString(JGraphText.this.graph.isPortsVisible() ?  "Action.connectionEnable" : "Action.connectionDisable"));
				}
			};
			action.actionPerformed(null);
			toolBar.add(action);
		}
		
//		 Undo
		toolBar.addSeparator();
		URL undoUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/undo.gif");
		ImageIcon undoIcon = new ImageIcon(undoUrl);
		this.undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		this.undo.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.Undo"));
		this.undo.setEnabled(false);
		toolBar.add(this.undo);

		// Redo
		URL redoUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/redo.gif");
		ImageIcon redoIcon = new ImageIcon(redoUrl);
		this.redo = new AbstractAction("", redoIcon) {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		this.redo.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.Redo"));
		this.redo.setEnabled(false);
		toolBar.add(this.redo);

		//
		// Edit Block
		//
		toolBar.addSeparator();
		Action action;
		URL url;

		// Copy
		action = javax.swing.TransferHandler // JAVA13:
												// org.jgraph.plaf.basic.TransferHandler
				.getCopyAction();
		url = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/copy.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
//		 TODO
//		toolBar.add(copy = new EventRedirector(action));

		// Paste
		action = javax.swing.TransferHandler // JAVA13:
												// org.jgraph.plaf.basic.TransferHandler
				.getPasteAction();
		url = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/paste.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
//		 TODO
//		toolBar.add(paste = new EventRedirector(action));

		// Cut
		action = javax.swing.TransferHandler // JAVA13:
												// org.jgraph.plaf.basic.TransferHandler
				.getCutAction();
		url = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/cut.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		// TODO
//		toolBar.add(cut = new EventRedirector(action));

		// Remove
		URL removeUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		this.remove = new AbstractAction("", removeIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					cells = graph.getDescendants(cells);
					graph.getModel().remove(cells);
				}
			}
		};
		this.remove.setEnabled(false);
		toolBar.add(this.remove);

		
//		 TODO
//		// To Front
//		toolBar.addSeparator();
//		URL toFrontUrl = getClass().getClassLoader().getResource(
//				"com/syrus/AMFICOM/manager/resources/icons/tofront.gif");
//		ImageIcon toFrontIcon = new ImageIcon(toFrontUrl);
//		tofront = new AbstractAction("", toFrontIcon) {
//			public void actionPerformed(ActionEvent e) {
//				if (!graph.isSelectionEmpty())
//					toFront(graph.getSelectionCells());
//			}
//		};
//		tofront.setEnabled(false);
//		toolBar.add(tofront);
//
//		// To Back
//		URL toBackUrl = getClass().getClassLoader().getResource(
//				"com/syrus/AMFICOM/manager/resources/icons/toback.gif");
//		ImageIcon toBackIcon = new ImageIcon(toBackUrl);
//		toback = new AbstractAction("", toBackIcon) {
//			public void actionPerformed(ActionEvent e) {
//				if (!graph.isSelectionEmpty())
//					toBack(graph.getSelectionCells());
//			}
//		};
//		toback.setEnabled(false);
//		toolBar.add(toback);
		
		toolBar.addSeparator();
		{
			// Zoom Std

			URL zoomUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoom.gif");
			ImageIcon zoomIcon = new ImageIcon(zoomUrl);
			Action zoom = new AbstractAction("", zoomIcon) {
				
				private static final long	serialVersionUID	= 1338961419658950016L;

				public void actionPerformed(ActionEvent e) {
					graph.setScale(1.0);
				}
			};
			toolBar.add(zoom);
			
			zoom.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.ActualSize"));
		}
		
		{
			// Zoom In
			URL zoomInUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoomin.gif");
			ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
			AbstractAction zoomIn = new AbstractAction("", zoomInIcon) {
				public void actionPerformed(ActionEvent e) {
					graph.setScale(2 * graph.getScale());
				}
			};
			toolBar.add(zoomIn);
			zoomIn.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.ZoomIn"));
		}
		
		{
			// Zoom Out
			URL zoomOutUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoomout.gif");
			ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
			AbstractAction zoomOut = new AbstractAction("", zoomOutIcon) {
				public void actionPerformed(ActionEvent e) {
					graph.setScale(graph.getScale() / 2);
				}
			};
			toolBar.add(zoomOut);
			zoomOut.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.ZoomOut"));
		}

//		// Group
//		toolBar.addSeparator();
//		URL groupUrl = getClass().getClassLoader().getResource(
//				"com/syrus/AMFICOM/manager/resources/icons/group.gif");
//		ImageIcon groupIcon = new ImageIcon(groupUrl);
//		this.group = new AbstractAction("", groupIcon) {
//			public void actionPerformed(ActionEvent e) {
////				 TODO
////				group(graph.getSelectionCells());
//			}
//		};
//		this.group.setEnabled(false);
//		toolBar.add(this.group);
//
//		// Ungroup
//		URL ungroupUrl = getClass().getClassLoader().getResource(
//				"com/syrus/AMFICOM/manager/resources/icons/ungroup.gif");
//		ImageIcon ungroupIcon = new ImageIcon(ungroupUrl);
//		ungroup = new AbstractAction("", ungroupIcon) {
//			public void actionPerformed(ActionEvent e) {
//				// TODO
////				ungroup(graph.getSelectionCells());
//			}
//		};
//		this.ungroup.setEnabled(false);
//		toolBar.add(this.ungroup);
		
		return toolBar;
	}
	
	// Undo the last Change to the Model or the View
	public void undo() {
		try {
			this.undoManager.undo(this.graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Redo the last Change to the Model or the View
	public void redo() {
		try {
			this.undoManager.redo(this.graph.getGraphLayoutCache());
		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			updateHistoryButtons();
		}
	}

	// Update Undo/Redo Button State based on Undo Manager
	protected void updateHistoryButtons() {
		// The View Argument Defines the Context
		this.undo.setEnabled(this.undoManager.canUndo(this.graph.getGraphLayoutCache()));
		this.redo.setEnabled(this.undoManager.canRedo(this.graph.getGraphLayoutCache()));
	}
	
	private JToolBar createEntityToolBar() {
		JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		this.createAction(toolBar, UserBeanFactory.getInstance());
		this.createAction(toolBar, ARMBeanFactory.getInstance());
		toolBar.addSeparator();
		this.createAction(toolBar, RTUBeanFactory.getInstance());
		this.createAction(toolBar, ServerBeanFactory.getInstance());
		this.createAction(toolBar, MCMBeanFactory.getInstance());
		toolBar.addSeparator();
		this.createAction(toolBar, NetBeanFactory.getInstance());
		return toolBar;
	}
	
	private void createAction(final JToolBar toolBar,
	                          final AbstractBeanFactory factory) {
		final String name = factory.getName();
		Icon icon = factory.getIcon();
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			private Map<String, Integer> entityIndices;
			
			public void actionPerformed(ActionEvent e) {
				AbstractBean bean = factory.createBean();
				
				if (this.entityIndices == null) {
					this.entityIndices = new HashMap<String, Integer>();
				}
				Integer i = this.entityIndices.get(name);
				int index = (i != null ? i : 0) + 1;
				this.entityIndices.put(name, index);
				JGraphText.this.createChild(JGraphText.this.rootItem, factory.getShortName() + "-" + index, bean, 20, 20, 0, 0, factory.getImage());
				
				
			}
		};
		
		
		
		action.putValue(Action.SHORT_DESCRIPTION, name);
		
		JButton button = toolBar.add(action);
		
		button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println(".mouseEntered() | name:" + name);
			}
		});
		
		button.addFocusListener(new FocusAdapter() {			
			public void focusGained(FocusEvent e) {
				System.out.println(".focusGained() | name:" + name);
			};
		});
	}
	
	DefaultEdge createEdge(DefaultGraphCell source, DefaultGraphCell target) {
		return this.createEdge(source, target, true);
	}
	
	private DefaultEdge createEdge(DefaultGraphCell source, DefaultGraphCell target, boolean addToGraph) {
		
		DefaultPort sourcePort = (DefaultPort) source.getChildAt(0);
		DefaultPort targetPort = (DefaultPort) target.getChildAt(0);
		if (sourcePort != targetPort) {
			Object sourceObject = sourcePort.getUserObject();
			Object targetObject = targetPort.getUserObject();
			boolean canConnect = true;
			if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
				AbstractBean sourceBean = (AbstractBean) sourceObject;
				AbstractBean targetBean = (AbstractBean) targetObject;
				canConnect = sourceBean.isTargetValid(targetBean);
			}
			
			if (canConnect) {
				DefaultEdge edge = new DefaultEdge();
				// "edge" + (++this.edgeCount)
				edge.setSource(sourcePort);
				edge.setTarget(targetPort);
//				 Set Arrow Style for edge
				this.createEdgeAttributes(edge);
				
				GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
				System.out.println("JGraphText.createChild() | insert " + edge + "\n\t"+source+" -> " + target);
				graphLayoutCache.insert(edge);		
				graphLayoutCache.setVisibleImpl(new Object[] {edge}, addToGraph);
				
				this.graph.getSelectionModel().clearSelection();
				
				return edge;
			}
		}

		return null;
	}
	
	
	private void createEdgeAttributes(Edge edge) {
		AttributeMap attributes = edge.getAttributes();
		GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_CLASSIC);
		GraphConstants.setEditable(attributes, false);
		GraphConstants.setEndFill(attributes, true);
		GraphConstants.setLabelAlongEdge(attributes, true);
	}
	
//	private DefaultGraphCell createChild(DefaultGraphCell parentCell, String name, double x,
//	             			double y, double w, double h, Color bg, boolean raised) {
//		DefaultGraphCell cell = this.createVertex(name, x, y, w, h, bg, raised);
//		this.createEdge(this.treeModel.isDirect() ? this.rootItem : cell, this.treeModel.isDirect() ? cell : this.rootItem, false);
//		if (parentCell != null && parentCell != this.rootItem) {
//			this.createEdge(this.treeModel.isDirect() ? parentCell : cell, this.treeModel.isDirect() ?  cell : parentCell);
//		}
//		return cell;
//	}
	
	DefaultGraphCell createChild(DefaultGraphCell parentCell, String name, Object object, double x,
	         	             			double y, double w, double h, Icon image) {
		DefaultGraphCell cell = this.createVertex(name, object, x, y, w, h, image);
 		GraphLayoutCache cache = this.graph.getGraphLayoutCache();
 		System.out.println("JGraphText.createChild() | insert " + cell);
		cache.insert(cell);	

 		this.createEdge(this.treeModel.isDirect() ? this.rootItem : cell, this.treeModel.isDirect() ? cell : this.rootItem, false);
 		if (parentCell != null && parentCell != this.rootItem) {
 			this.createEdge(this.treeModel.isDirect() ? parentCell : cell, this.treeModel.isDirect() ?  cell : parentCell);
 		}
 		return cell;
	}
	
	private void createRootItem() {
		
//		DefaultGraphCell[] cells = new DefaultGraphCell[1];
		this.rootItem = createVertex("Root", -100, -100, 0, 0, Color.LIGHT_GRAY, true);
//		this.rootItem = cells[0];
//		
//		this.graph.getSelectionModel().clearSelection();
//		
//		
//		GraphConstants.setAutoSize(cells[0].getAttributes(), true);
//		
//		GraphLayoutCache cache = this.graph.getGraphLayoutCache();		
//		cache.insert(cells);		
	}

	
	private void createTreeModel() {		
		
		GraphConstants.setAutoSize(this.rootItem.getAttributes(), true);
		GraphLayoutCache cache = this.graph.getGraphLayoutCache();		
		cache.insert(this.rootItem);
		
		this.treeModel = new GraphTreeModel(this.graph.getModel(), false);
		this.tree = new JTree(this.treeModel);
		
		this.tree.setRootVisible(false);
		
		this.graph.getSelectionModel().addGraphSelectionListener(new GraphSelectionListener() {
			public void valueChanged(GraphSelectionEvent e) {
				final Object cell = e.getCell();
				final TreeSelectionModel selectionModel = JGraphText.this.tree.getSelectionModel();
//				System.out.println(".valueChanged() | " + e +"\n\t" + cell + '[' + cell.getClass().getName() + ']');
				if (cell instanceof Edge) {
					if (e.isAddedCell()) {
						Edge edge = (Edge)cell;
						JGraphText.this.tree.scrollPathToVisible(new TreePath(JGraphText.this.treeModel.getPathToRoot(((TreeNode) edge.getSource()).getParent())));
						JGraphText.this.tree.scrollPathToVisible(new TreePath(JGraphText.this.treeModel.getPathToRoot(((TreeNode) edge.getTarget()).getParent())));
					} else {
						selectionModel.clearSelection();
					}
				} else {				
					if (e.isAddedCell()){
						TreeNode[] pathToRoot = JGraphText.this.treeModel.getPathToRoot((TreeNode) cell);
						if (pathToRoot != null) {
							TreePath path = new TreePath(pathToRoot);
							selectionModel.setSelectionPath(path);
						} else {
							selectionModel.clearSelection();
						}
					} else {
						selectionModel.clearSelection();
					}
				}
			}
		});
		
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				final Object lastPathComponent = e.getPath().getLastPathComponent();
				final GraphSelectionModel selectionModel = JGraphText.this.graph.getSelectionModel();
				if (e.isAddedPath()) {
					selectionModel.setSelectionCell(lastPathComponent);
				} else {
					selectionModel.clearSelection();
				}
			}
		});
	}

	//
	// PopupMenu
	//
//	public JPopupMenu createPopupMenu(final Point pt, final Object cell) {
//		JPopupMenu menu = new JPopupMenu();
//		if (cell != null) {
//			// Edit
//			menu.add(new AbstractAction("Edit") {
//				public void actionPerformed(ActionEvent e) {
//					graph.startEditingAtCell(cell);
//				}
//			});
//		}
//		// Remove
//		if (!graph.isSelectionEmpty()) {
//			menu.addSeparator();
//			menu.add(new AbstractAction("Remove") {
//				public void actionPerformed(ActionEvent e) {
//					remove.actionPerformed(e);
//				}
//			});
//		}
//		menu.addSeparator();
//		// Insert
//		menu.add(new AbstractAction("Insert") {
//			public void actionPerformed(ActionEvent ev) {
////				insert(pt);
//			}
//		});
//		return menu;
//	}
	
	public static void main(String[] args) {

		JGraphText text = new JGraphText();
		JFrame frame = new JFrame("JGraphText");
		frame.getContentPane().add(text.getPanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(640, 480);
		frame.setVisible(true);
	}
	
	
	private DefaultGraphCell createVertex(	final String name,
	                                      	final Object object,
	                                      	final double x,
											final double y,
											final double w,
											final double h,
											final Icon image) {

		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(),
			new Rectangle2D.Double(x, y, w, h));

		GraphConstants.setAutoSize(cell.getAttributes(), true);

		
		AttributeMap attributes = cell.getAttributes();
		GraphConstants.setIcon(attributes, image);
		attributes.remove(GraphConstants.BORDER);
		attributes.remove(GraphConstants.BORDERCOLOR);

		// Add a Port
		DefaultPort port = new DefaultPort(object);
		cell.add(port);
		return cell;
	}
	
	private DefaultGraphCell createVertex(String name, double x,
			double y, double w, double h, Color bg, boolean raised) {

		
		// Create vertex with the given name
		DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(), 
			new Rectangle2D.Double(x, y, w, h));

		GraphConstants.setAutoSize(cell.getAttributes(), true);

		// Set fill color
		if (bg != null) {
			GraphConstants.setGradientColor(cell.getAttributes(), bg);
			GraphConstants.setOpaque(cell.getAttributes(), true);
		}

		// Set raised border
		if (raised) {
			GraphConstants.setBorder(cell.getAttributes(), BorderFactory
					.createRaisedBevelBorder());
		} else {
			// Set black border
			GraphConstants.setBorderColor(cell.getAttributes(), Color.BLACK);
		}
		
		// Add a Port
		DefaultPort port = new DefaultPort("port:" + name);
		cell.add(port);
		return cell;
	}

	//
	// Custom MarqueeHandler

	// MarqueeHandler that Connects Vertices and Displays PopupMenus
	public class MyMarqueeHandler extends BasicMarqueeHandler {

		// Holds the Start and the Current Point
		protected Point2D start, current;

		// Holds the First and the Current Port
		protected PortView port, firstPort;

		// Override to Gain Control (for PopupMenu and ConnectMode)
		public boolean isForceMarqueeEvent(MouseEvent e) {
			System.out.println("MyMarqueeHandler.isForceMarqueeEvent()");
			if (e.isShiftDown())
				return false;
			// If Right Mouse Button we want to Display the PopupMenu
			if (SwingUtilities.isRightMouseButton(e))
				// Return Immediately
				return true;
			// Find and Remember Port
			this.port = getSourcePortAt(e.getPoint());
			// If Port Found and in ConnectMode (=Ports Visible)
			if (this.port != null && JGraphText.this.graph.isPortsVisible())
				return true;
			// Else Call Superclass
			return super.isForceMarqueeEvent(e);
		}

		// Display PopupMenu or Remember Start Location and First Port
		public void mousePressed(final MouseEvent e) {
			// If Right Mouse Button
			if (SwingUtilities.isRightMouseButton(e)) {
				// TODO
//				// Find Cell in Model Coordinates
				DefaultGraphCell cell = (DefaultGraphCell) graph.getFirstCellForLocation(e.getX(), e.getY());
				System.out.println("MyMarqueeHandler.mousePressed() | cell:" + cell);
				if (cell.getAllowsChildren()) {
					DefaultPort port = (DefaultPort) cell.getChildAt(0);
					Object userObject = port.getUserObject();
					if (userObject instanceof AbstractBean) {
						AbstractBean bean = (AbstractBean)userObject;
						JPopupMenu menu = bean.getMenu(JGraphText.this.graph, cell);
						if (menu != null) {
							menu.show(graph, e.getX(), e.getY());
						}
					}
				}
				
//				// Create PopupMenu for the Cell 
//				JPopupMenu menu = createPopupMenu(e.getPoint(), cell);
//				// Display PopupMenu
//				menu.show(graph, e.getX(), e.getY());
//				// Else if in ConnectMode and Remembered Port is Valid
			} else if (this.port != null && JGraphText.this.graph.isPortsVisible()) {
				// Remember Start Location
				this.start = JGraphText.this.graph.toScreen(this.port.getLocation(null));
				// Remember First Port
				this.firstPort = this.port;
			} else {
				// Call Superclass
				super.mousePressed(e);
			}
		}

		// Find Port under Mouse and Repaint Connector
		public void mouseDragged(MouseEvent e) {
			// If remembered Start Point is Valid
			if (this.start != null) {
				// Fetch Graphics from Graph
				Graphics g = JGraphText.this.graph.getGraphics();
				// Reset Remembered Port
				PortView newPort = getTargetPortAt(e.getPoint());
				// Do not flicker (repaint only on real changes)
				if (newPort == null || newPort != this.port) {
					// Xor-Paint the old Connector (Hide old Connector)
					paintConnector(Color.BLACK, JGraphText.this.graph.getBackground(), g);
					// If Port was found then Point to Port Location
					this.port = newPort;
					if (this.port != null)
						this.current = JGraphText.this.graph.toScreen(this.port.getLocation(null));
					// Else If no Port was found then Point to Mouse Location
					else
						this.current = JGraphText.this.graph.snap(e.getPoint());
					// Xor-Paint the new Connector
					paintConnector(JGraphText.this.graph.getBackground(), Color.BLACK, g);
				}
			}
			// Call Superclass
			super.mouseDragged(e);
		}

		public PortView getSourcePortAt(Point2D point) {
			// Disable jumping
			JGraphText.this.graph.setJumpToDefaultPort(false);
			PortView result;
			try {
				// Find a Port View in Model Coordinates and Remember
				result = JGraphText.this.graph.getPortViewAt(point.getX(), point.getY());
			} finally {
				JGraphText.this.graph.setJumpToDefaultPort(true);
			}
			return result;
		}

		// Find a Cell at point and Return its first Port as a PortView
		protected PortView getTargetPortAt(Point2D point) {
			// Find a Port View in Model Coordinates and Remember
			return JGraphText.this.graph.getPortViewAt(point.getX(), point.getY());
		}

		// Connect the First Port and the Current Port in the Graph or Repaint
		public void mouseReleased(MouseEvent e) {
			// If Valid Event, Current and First Port
			if (e != null && this.port != null && this.firstPort != null
					&& this.firstPort != this.port) {
				// Then Establish Connection
				// connect((Port) firstPort.getCell(), (Port) port.getCell());
				DefaultPort sourcePort = (DefaultPort) this.firstPort.getCell();
				DefaultPort targetPort = (DefaultPort) this.port.getCell();
				DefaultEdge edge = JGraphText.this.createEdge((DefaultGraphCell)sourcePort.getParent(), (DefaultGraphCell)targetPort.getParent());
				if (edge != null) {
					Object userObject = sourcePort.getUserObject();
					if (userObject instanceof AbstractBean) {
						System.out.println("MyMarqueeHandler.mouseReleased()");
						AbstractBean bean = (AbstractBean)userObject;
						bean.updateEdgeAttributes(edge, targetPort);
						GraphLayoutCache graphLayoutCache = JGraphText.this.graph.getGraphLayoutCache();
						graphLayoutCache.refresh(graphLayoutCache.getMapping(edge, true), true);
					}
				}
				e.consume();
				// Else Repaint the Graph
			} else
				JGraphText.this.graph.repaint();
			// Reset Global Vars
			this.firstPort = this.port = null;
			this.start = this.current = null;
			// Call Superclass
			super.mouseReleased(e);
		}

		// Show Special Cursor if Over Port
		public void mouseMoved(MouseEvent e) {
			// Check Mode and Find Port
			if (e != null && getSourcePortAt(e.getPoint()) != null
					&& JGraphText.this.graph.isPortsVisible()) {
				// Set Cusor on Graph (Automatically Reset)
				JGraphText.this.graph.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
				// Consume Event
				// Note: This is to signal the BasicGraphUI's
				// MouseHandle to stop further event processing.
				e.consume();
			} else
				// Call Superclass
				super.mouseMoved(e);
		}

		// Use Xor-Mode on Graphics to Paint Connector
		protected void paintConnector(Color fg, Color bg, Graphics g) {
			// Set Foreground
			g.setColor(fg);
			// Set Xor-Mode Color
			g.setXORMode(bg);
			// Highlight the Current Port
			paintPort(JGraphText.this.graph.getGraphics());
			// If Valid First Port, Start and Current Point
			if (this.firstPort != null && this.start != null && this.current != null)
				// Then Draw A Line From Start to Current Point
				g.drawLine((int) this.start.getX(), (int) this.start.getY(),
						(int) this.current.getX(), (int) this.current.getY());
		}

		// Use the Preview Flag to Draw a Highlighted Port
		protected void paintPort(Graphics g) {
			// If Current Port is Valid
			if (this.port != null) {
				// If Not Floating Port...
				boolean o = (GraphConstants.getOffset(this.port.getAttributes()) != null);
				// ...Then use Parent's Bounds
				Rectangle2D r = (o) ? this.port.getBounds() : this.port.getParentView()
						.getBounds();
				// Scale from Model to Screen
				r = JGraphText.this.graph.toScreen((Rectangle2D) r.clone());
				// Add Space For the Highlight Border
				r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
						.getHeight() + 6);
				// Paint Port in Preview (=Highlight) Mode
				JGraphText.this.graph.getUI().paintCell(g, this.port, r, true);
			}
		}

	} // End of Editor.MyMarqueeHandler
}
