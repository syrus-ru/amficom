/*-
 * $Id: ManagerMainFrame.java,v 1.36 2005/12/22 14:24:08 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.TreePath;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.UI.PerspectiveTreeModel.PerspectiveMutableTreeNode;
import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.util.Log;
/**
 * @version $Revision: 1.36 $, $Date: 2005/12/22 14:24:08 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ManagerMainFrame extends AbstractMainFrame {	
	
	JGraph						graph;

	JTree						tree;

	private PerspectiveTreeModel treeModel;

	JPanel				propertyPanel;

	// Undo Manager
	protected GraphUndoManager	undoManager;

	// Actions which Change State
	protected Action			undo, redo, remove, group, ungroup, tofront, toback,
			cut, copy, paste;

	Perspective			perspective;
	
	boolean						arranging;
	
	ManagerGraphModel	graphModel;
	
	UIDefaults					frames;
	
	static final String							TREE_FRAME			= "Manager.Label.ElementsTree";
	static final String							GRAPH_FRAME			= "Manager.Label.Graph";
	static final String							PROPERTIES_FRAME	= "Manager.Label.Properties";

	JScrollPane pane;

	BeanUI	beanUI;
	
	ManagerHandler	managerHandler;

	GraphRoutines	graphRoutines;

	private Point	location;

	private CellBuffer	buffer;
	
	public ManagerMainFrame(final ApplicationContext aContext) {
		super(aContext, "Manager", new AbstractMainMenuBar(aContext.getApplicationModel()) {

			private JMenu				menuView;
			@Override
			protected void addMenuItems() {

				this.menuView = new JMenu(I18N.getString(ApplicationModel.MENU_VIEW));
				this.menuView.setName(ApplicationModel.MENU_VIEW);

				final JMenuItem menuViewTreeItem = new JMenuItem(I18N.getString(TREE_FRAME));
				menuViewTreeItem.setName(TREE_FRAME);
				menuViewTreeItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewTreeItem);

				final JMenuItem menuViewGraphItem = new JMenuItem(I18N.getString(GRAPH_FRAME));
				menuViewGraphItem.setName(GRAPH_FRAME);
				menuViewGraphItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewGraphItem);

				final JMenuItem menuViewPropertiesItem = new JMenuItem(I18N.getString(PROPERTIES_FRAME));
				menuViewPropertiesItem.setName(PROPERTIES_FRAME);
				menuViewPropertiesItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewPropertiesItem);

				this.menuView.addSeparator();

				final JMenuItem menuViewArrangeItem = new JMenuItem(I18N.getString(ApplicationModel.MENU_VIEW_ARRANGE));
				menuViewArrangeItem.setName(ApplicationModel.MENU_VIEW_ARRANGE);
				menuViewArrangeItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewArrangeItem);

				this.add(this.menuView);

				this.addApplicationModelListener(new ApplicationModelListener() {

					public void modelChanged(String elementName) {
						this.modelChanged();
					}

					public void modelChanged(String[] elementNames) {
						this.modelChanged();
					}

					private void modelChanged() {
						menuViewTreeItem.setVisible(getApplicationModel().isVisible(TREE_FRAME));
						menuViewTreeItem.setEnabled(getApplicationModel().isEnabled(TREE_FRAME));

						menuViewGraphItem.setVisible(getApplicationModel().isVisible(GRAPH_FRAME));
						menuViewGraphItem.setEnabled(getApplicationModel().isEnabled(GRAPH_FRAME));

						menuViewPropertiesItem.setVisible(getApplicationModel().isVisible(PROPERTIES_FRAME));
						menuViewPropertiesItem.setEnabled(getApplicationModel().isEnabled(PROPERTIES_FRAME));
						
						menuViewArrangeItem.setVisible(getApplicationModel().isVisible(
							ApplicationModel.MENU_VIEW_ARRANGE));
						menuViewArrangeItem.setEnabled(getApplicationModel().isEnabled(
							ApplicationModel.MENU_VIEW_ARRANGE));

					}
				});				
			}
		}, new AbstractMainToolBar() {});
		
		final JDesktopPane desktopPane1 = this.desktopPane;
		
		
		this.setWindowArranger(new WindowArranger(this) {

			@Override
			public void arrange() {
				ManagerMainFrame f = (ManagerMainFrame) this.mainframe;

				int w = desktopPane1.getSize().width;
				int h = desktopPane1.getSize().height;

				int width = w / 5;

				final ApplicationModel model = f.getModel();
				
				if (model.isVisible(TREE_FRAME) && 
						model.isVisible(GRAPH_FRAME) && 
						model.isVisible(PROPERTIES_FRAME)) {
					JInternalFrame treeFrame = ((JInternalFrame) f.frames.get(TREE_FRAME));
					JInternalFrame graphFrame = ((JInternalFrame) f.frames.get(GRAPH_FRAME));
					JInternalFrame propertiesFrame = ((JInternalFrame) f.frames.get(PROPERTIES_FRAME));
	
					normalize(treeFrame);
					normalize(graphFrame);
					normalize(propertiesFrame);
					
					treeFrame.setSize(width, h/2);
					graphFrame.setSize(w - width, h);
					propertiesFrame.setSize(width, h/2);				
					
					treeFrame.setLocation(0, 0);
					propertiesFrame.setLocation(0, treeFrame.getY() + treeFrame.getHeight());
					graphFrame.setLocation(treeFrame.getWidth(), 0);
					
					
					treeFrame.setVisible(true);
					graphFrame.setVisible(true);
					propertiesFrame.setVisible(true);
				}
			}
		});
		
	}
	

	@Override
	protected void initModule() {
		super.initModule();
		
		final JDesktopPane desktopPane1 = this.desktopPane;
		
		this.frames = new UIDefaults();		
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			@SuppressWarnings({"hiding","unqualified-field-access", "synthetic-access"})
			public Object createValue(UIDefaults table) {
				
				final JInternalFrame frame = new JInternalFrame(I18N.getString(TREE_FRAME), true);
				frame.setIconifiable(true);
//						 Construct Model and Graph
				graphModel = new ManagerGraphModel(ManagerMainFrame.this);
				
				graph = new JGraph(graphModel,
					new GraphLayoutCache(graphModel,
							new DefaultCellViewFactory(),
							true));


				
				graphRoutines = new GraphRoutines(ManagerMainFrame.this);
				
				//	Use a Custom Marquee Handler
				graph.setMarqueeHandler(new ManagerMarqueeHandler(ManagerMainFrame.this));

				// Control-drag should clone selection
				graph.setCloneable(true);

				// Enable edit without final RETURN keystroke
				graph.setInvokesStopCellEditing(true);

				// When over a cell, jump to its default port (we only have one, anyway)
				graph.setJumpToDefaultPort(true);
				
				
				undoManager = new GraphUndoManager() {
					// Override Superclass
					@Override
					public void undoableEditHappened(UndoableEditEvent e) {
						// First Invoke Superclass
						super.undoableEditHappened(e);
						// Then Update Undo/Redo Buttons
						updateHistoryButtons();
					}
				};

				// Register UndoManager with the Model
				graph.getModel().addUndoableEditListener(undoManager);

				createModelListener();
				
				frames.get(GRAPH_FRAME);
				
				final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
				managerHandler = 
					extensionLauncher.getExtensionHandler(ManagerHandler.class.getName());
				
				createTreeModel();

				enableDragAndDrop();
				
				final JScrollPane pane = new JScrollPane(ManagerMainFrame.this.tree);
				frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
				desktopPane1.add(frame);
				frame.getContentPane().add(pane);
				
				return frame;
			}
		});		
		
		
		this.frames.put(GRAPH_FRAME, new UIDefaults.LazyValue() {

			@SuppressWarnings("unqualified-field-access")
			public Object createValue(UIDefaults table){
			
			// show graph frame
			
			final JInternalFrame frame = new JInternalFrame(I18N.getString(GRAPH_FRAME), true);
				
			final JPanel panel = new JPanel(new GridBagLayout());
			
			final GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			gbc.weightx = 1.0;

			final JToolBar createToolBar = createToolBar();
			panel.add(createToolBar, gbc);
			
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			
			gbc.gridheight = GridBagConstraints.RELATIVE;
			
			ManagerMainFrame.this.pane = new JScrollPane(ManagerMainFrame.this.graph);
			
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			panel.add(ManagerMainFrame.this.pane, gbc);
			
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 0.0;
			
			
			frame.setIconifiable(true);
			desktopPane1.add(frame);
			frame.getContentPane().add(panel);
			frame.pack();

			frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));			
			return frame;
		}
		});		
		
		this.frames.put(PROPERTIES_FRAME, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table){
				
	//			 show property frame
				JInternalFrame frame = new JInternalFrame(I18N.getString(PROPERTIES_FRAME), true);
				frame.setIconifiable(true);
				frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
				desktopPane1.add(frame);				
				ManagerMainFrame.this.propertyPanel = new JPanel(new GridBagLayout());
				final GridBagConstraints gbc2 = new GridBagConstraints();
				gbc2.fill = GridBagConstraints.BOTH;
				gbc2.weightx = 1.0;
				gbc2.weighty = 1.0;
				gbc2.gridwidth = GridBagConstraints.REMAINDER;
	
				
				frame.getContentPane().add(ManagerMainFrame.this.propertyPanel);			
				return frame;
	
			}
		});	
		
		final ApplicationModel applicationModel = this.aContext.getApplicationModel();
		applicationModel.setCommand(ManagerModel.FLUSH_COMMAND, new FlushCommand(this));	
		
		applicationModel.setCommand(TREE_FRAME, this.getShowWindowLazyCommand(this.frames, TREE_FRAME));
		applicationModel.setCommand(GRAPH_FRAME, this.getShowWindowLazyCommand(this.frames, GRAPH_FRAME));
		applicationModel.setCommand(PROPERTIES_FRAME, this.getShowWindowLazyCommand(this.frames, PROPERTIES_FRAME));
		
		

	}

	private void createPerspectives() {
		final Collection<Perspective> perspectives = this.managerHandler.getPerspectives();
		for (final Perspective perspective : perspectives) {
			this.setPerspective(perspective);
		}		
	}


	@Override
	public void loggedIn() {
		final ApplicationModel model = this.aContext.getApplicationModel();
		model.setEnabled(ApplicationModel.MENU_VIEW, true);
		model.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		model.setEnabled(TREE_FRAME, true);
		model.setEnabled(GRAPH_FRAME, true);
		model.setEnabled(PROPERTIES_FRAME, true);

		model.setVisible(ApplicationModel.MENU_VIEW, true);
		model.setVisible(ApplicationModel.MENU_VIEW_ARRANGE, true);
		model.setVisible(TREE_FRAME, true);
		model.setVisible(GRAPH_FRAME, true);
		model.setVisible(PROPERTIES_FRAME, true);
		
		model.fireModelChanged();
		
		this.windowArranger.arrange();
		
		
		new ProcessingDialog(new Runnable() {
			public void run() {
				graph.setVisible(false);

				managerHandler.setManagerMainFrame(ManagerMainFrame.this);
				try {
					managerHandler.loadPerspectives();
				} catch (ApplicationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				createPerspectives();
				
				treeModel.fillRootItems();
				
				graph.setVisible(true);
			}
		}, I18N.getString("Common.ProcessingDialog.PlsWait"));
		
	}
	
	@Override
	public void loggedOut() {
	}
	
	@Override
	protected void setDefaultModel(ApplicationModel model) {
		super.setDefaultModel(model);		
		model.setEnabled(ApplicationModel.MENU_VIEW, true);
		model.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		model.setEnabled(TREE_FRAME, true);
		model.setEnabled(GRAPH_FRAME, true);
		model.setEnabled(PROPERTIES_FRAME, true);

		model.setVisible(ApplicationModel.MENU_VIEW, true);
		model.setVisible(ApplicationModel.MENU_VIEW_ARRANGE, true);
		model.setVisible(TREE_FRAME, true);
		model.setVisible(GRAPH_FRAME, true);
		model.setVisible(PROPERTIES_FRAME, true);
		
		model.fireModelChanged();
	}
	
	private void createModelListener() {
		this.graph.getModel().addGraphModelListener(new ManagerGraphModelListener(this));
	}
	
	final JToolBar createPerspecives() {	
		final JToolBar perspectives = new JToolBar();
		perspectives.setFloatable(false);
		perspectives.addSeparator();
		return perspectives;
	}
	
	JToolBar createToolBar() {
		JToolBar graphToolBar = new JToolBar(SwingConstants.HORIZONTAL);
		graphToolBar.setFloatable(false);
		
		{
			// Toggle Connect Mode
			final Icon connectonIcon = 
				UIManager.getIcon("com.syrus.AMFICOM.manager.resources.action.connecton");
			
			final Icon connectoffIcon = 
				UIManager.getIcon("com.syrus.AMFICOM.manager.resources.action.connectoff");
			
			final AbstractAction action = new AbstractAction("", connectonIcon) {
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setPortsVisible(!ManagerMainFrame.this.graph.isPortsVisible());
					this.putValue(SMALL_ICON, ManagerMainFrame.this.graph.isPortsVisible() ? connectonIcon : connectoffIcon);
					this.putValue(SHORT_DESCRIPTION, 
						I18N.getString(ManagerMainFrame.this.graph.isPortsVisible() ?  
							"Manager.Action.connectionEnable" : 
							"Manager.Action.connectionDisable"));
				}
			};
			action.actionPerformed(null);
			graphToolBar.add(action);
		}
		
//		 Undo
		graphToolBar.addSeparator();
		Icon undoIcon = UIManager.getIcon("com.syrus.AMFICOM.manager.resources.action.undo");
		this.undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		this.undo.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Undo"));
		this.undo.setEnabled(false);
//		toolBar.add(this.undo);

		// Redo
		Icon redoIcon = UIManager.getIcon("com.syrus.AMFICOM.manager.resources.action.redo");
		this.redo = new AbstractAction("", redoIcon) {
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		};
		this.redo.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Redo"));
		this.redo.setEnabled(false);
//		toolBar.add(this.redo);

		//
		// Edit Block
		//
		graphToolBar.addSeparator();

		final JComponent component = graph;
		final InputMap imap = component.getInputMap();
		final ActionMap map = component.getActionMap();
		
//		 Cut
		this.buffer = new CellBuffer(this);
		
		final Icon cutIcon = UIManager.getIcon("org.jgraph.example.resources.action.cut");
		
		this.cut = new AbstractAction("", cutIcon) {

			@SuppressWarnings({"unqualified-field-access","synthetic-access", "unchecked"})
			public void actionPerformed(final ActionEvent e) {
				
				if (!ManagerMainFrame.this.graph.isSelectionEmpty()) {
					final GraphModel model = ManagerMainFrame.this.graph.getModel();
					final GraphLayoutCache graphLayoutCache = graph.getGraphLayoutCache();
					final ManagerGraphCell selectionCell = (ManagerGraphCell) graph.getSelectionCell();
					final MPort port = selectionCell.getMPort();
					final List list = new ArrayList();
					for(final Iterator iterator = port.edges(); iterator.hasNext();) {
						list.add(iterator.next());						
					}
					model.remove(list.toArray());

					
					buffer.putCells(selectionCell);
					graphLayoutCache.setVisible(selectionCell, false);

					try {
						final Perspective perspective2 = getPerspective();					
						final AbstractBean abstractBean = selectionCell.getAbstractBean();
						abstractBean.disposeLayoutItem();
						perspective2.removeLayoutBean(abstractBean);
					} catch (ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				updateBufferButtons();
			}
		};

		graphToolBar.add(this.cut);
		this.cut.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Cut"));
		this.cut.setEnabled(false);
		
		imap.put(KeyStroke.getKeyStroke("ctrl X"), "cut");
		map.put("cut", this.cut);
		
		// Paste
		final Icon pasteIcon = UIManager.getIcon("org.jgraph.example.resources.action.paste");
		this.paste = new AbstractAction("", pasteIcon) {

			@SuppressWarnings({"synthetic-access","unqualified-field-access"})
			public void actionPerformed(final ActionEvent e) {				
				final GraphLayoutCache cache = graph.getGraphLayoutCache();
				arranging = true;
				ManagerGraphCell managerGraphCell = buffer.getCell();
				buffer.clear();
				if (managerGraphCell != null) {
					cache.setVisible(managerGraphCell, true);
					managerGraphCell.setPerspective(perspective);
					try {
						perspective.putBean(managerGraphCell.getAbstractBean());
					} catch (final ApplicationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					perspective.firePropertyChangeEvent(new PropertyChangeEvent(this, "layoutBeans", null, null));
				}
				arranging = false;
				updateBufferButtons();
			}
		};
		graphToolBar.add(this.paste);
		this.paste.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Paste"));
		
		imap.put(KeyStroke.getKeyStroke("ctrl V"), "paste");
		map.put("paste", this.paste);				
		
		this.updateBufferButtons();

		
		// Remove
		final Icon deleteIcon = UIManager.getIcon("org.jgraph.example.resources.action.delete");
		this.remove = new AbstractAction("", deleteIcon) {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (!ManagerMainFrame.this.graph.isSelectionEmpty()) {
					Object[] cells = ManagerMainFrame.this.graph.getSelectionCells();
					final GraphModel model = ManagerMainFrame.this.graph.getModel();
					cells = ManagerMainFrame.this.graph.getDescendants(cells);
					final List list = new ArrayList(3 * cells.length / 2);
					for(final Object cell: cells) {
						if (model.isPort(cell)) {
							final Port port = (Port)cell;
							for(final Iterator it=port.edges();it.hasNext();) {
								list.add(it.next());
							}
						}						
						list.add(cell);
					}
					model.remove(list.toArray());
					
					
				}
				
			}
		};
		this.remove.setEnabled(false);
		graphToolBar.add(this.remove);
		this.remove.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Delete"));
		
		imap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
		map.put("delete", this.remove);		
		
		graphToolBar.addSeparator();
		{
			// Zoom Std

			final Icon zoomIcon = UIManager.getIcon("org.jgraph.example.resources.action.zoom");
			final Action zoom = new AbstractAction("", zoomIcon) {
				
				private static final long	serialVersionUID	= 1338961419658950016L;

				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setScale(1.0);
				}
			};
			graphToolBar.add(zoom);
			
			zoom.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.ActualSize"));
		}
		
		{
			// Zoom In
			final Icon zoomInIcon = UIManager.getIcon("org.jgraph.example.resources.action.zoomin");
			final AbstractAction zoomIn = new AbstractAction("", zoomInIcon) {
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setScale(2.0 * ManagerMainFrame.this.graph.getScale());
				}
			};
			graphToolBar.add(zoomIn);
			zoomIn.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.ZoomIn"));
		}
		
		{
			// Zoom Out
			final Icon zoomOutIcon = UIManager.getIcon("org.jgraph.example.resources.action.zoomout");
			final AbstractAction zoomOut = new AbstractAction("", zoomOutIcon) {
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setScale(ManagerMainFrame.this.graph.getScale() / 2.0);
				}
			};
			graphToolBar.add(zoomOut);
			zoomOut.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.ZoomOut"));
		}
		
		
		graphToolBar.addSeparator();
		
		{
			
			final AbstractAction flush = new AbstractAction("", UIManager.getIcon(ResourceKeys.ICON_REFRESH)) {
				
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.getContext().getApplicationModel().getCommand(ManagerModel.FLUSH_COMMAND).execute();
					}
			};
			graphToolBar.add(flush);
			flush.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Save"));
		}
		
		return graphToolBar;
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

	protected void updateBufferButtons() {
		boolean exists = this.buffer.isExists();
		if (exists) {
			final AbstractBean abstractBean = 
				this.buffer.getCell().getAbstractBean();
			exists = this.perspective.isSupported(abstractBean);
		}
		
		this.paste.setEnabled(exists);
	}
	
	private void createTreeModel() {		
		this.treeModel = new PerspectiveTreeModel(this);
		this.tree = new JTree(this.treeModel);
		
		this.tree.setRootVisible(true);

		this.tree.setCellRenderer(new MTreeCellRenderer());
		
		this.graph.getSelectionModel().addGraphSelectionListener(new ManagerGraphSelectionListener(this));
		
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			private boolean selected = false;
			public void valueChanged(final TreeSelectionEvent e) {				
				if (this.selected) {
					return;
				}
				this.selected = true;				
				final Object lastPathComponent = e.getPath().getLastPathComponent();
				final GraphSelectionModel selectionModel = ManagerMainFrame.this.graph.getSelectionModel();
				if (lastPathComponent instanceof ManagerGraphCell) {
					final ManagerGraphCell cell = (ManagerGraphCell) lastPathComponent;					
					final Perspective perspective2 = cell.getPerspective();
//					assert Log.debugMessage("1 set perspective " + perspective2, Log.DEBUGLEVEL03);
					setPerspective(perspective2);
					if (e.isAddedPath()) {
						selectionModel.setSelectionCell(lastPathComponent);
					} 
				} else if (lastPathComponent instanceof ActionMutableTreeNode){
					final ActionMutableTreeNode actionTreeNode = 
						(ActionMutableTreeNode) lastPathComponent;
					final Perspective perspective2 = actionTreeNode.getPerspective();
//					assert Log.debugMessage("2 set perspective " + perspective2, Log.DEBUGLEVEL03);
					setPerspective(perspective2);
				} else if (lastPathComponent instanceof PerspectiveTreeModel.PerspectiveMutableTreeNode) {
					final PerspectiveTreeModel.PerspectiveMutableTreeNode cell = 
						(PerspectiveMutableTreeNode) lastPathComponent;
					final Perspective perspective2 = cell.getPerspective();
//					assert Log.debugMessage("3 set perspective " + perspective2, Log.DEBUGLEVEL03);
					setPerspective(perspective2);
				}
				this.selected = false;
			}
		});
		
		
	}
	
	private void enableDragAndDrop() {
//		 enable d'n'd
		final ActionTransferHandler transferHandler = new ActionTransferHandler(this);
		this.tree.setTransferHandler(transferHandler);
		this.tree.setDragEnabled(true);
		this.graph.setTransferHandler(transferHandler);
		this.graph.setDragEnabled(true);
		
		try {
			this.graph.getDropTarget().addDropTargetListener(new DropTargetAdapter() {
				public void drop(final DropTargetDropEvent dtde) {
					setDropLocation(dtde.getLocation());
					transferHandler.getAction().actionPerformed(null);
				}
			});
		} catch (final TooManyListenersException e1) {
			// never occur, just only one listener
			assert false;
		}
	}
	
	void setDropLocation(final Point location) {
		this.location = location;		
	}
	
	public final Point getDropLocation() {
		return this.location;
	}
	
	final void setPerspectiveTab(final Perspective perspective) {
		try {		
			final long time0 = System.currentTimeMillis();
			this.perspective = perspective;
			this.putPerspective(perspective);
			final long time1 = System.currentTimeMillis();
			
			final JInternalFrame frame = 
				(JInternalFrame) this.frames.get(GRAPH_FRAME);
			frame.setTitle(I18N.getString(GRAPH_FRAME) 
				+ " : " 
				+ this.perspective.getName());
						
			this.graphRoutines.arrangeLayoutItems(perspective);
			final long time2 = System.currentTimeMillis();
			this.graphRoutines.showLayerName(perspective.getCodename(), true);
			final long time3 = System.currentTimeMillis();
//			this.graphRoutines.fixLayoutItemCharacteristics();
			final long time4 = System.currentTimeMillis();
			this.graph.getSelectionModel().clearSelection();
			updateBufferButtons();
			final long time5 = System.currentTimeMillis();
			assert Log.debugMessage("1-0 takes " + (time1 - time0) + " ms", Log.DEBUGLEVEL03);
			assert Log.debugMessage("2-1 takes " + (time2 - time1) + " ms", Log.DEBUGLEVEL03);
			assert Log.debugMessage("3-2 takes " + (time3 - time2) + " ms", Log.DEBUGLEVEL03);
			assert Log.debugMessage("4-3 takes " + (time4 - time3) + " ms", Log.DEBUGLEVEL03);
			assert Log.debugMessage("5-4 takes " + (time5 - time4) + " ms", Log.DEBUGLEVEL03);			
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final void putPerspective(final Perspective perspective) 
	throws ApplicationException {
////		 here items put into graphCache, when they can be used
		this.graphRoutines.arrangeLayoutItems(perspective);
		perspective.perspectiveApplied();
//		this.graphRoutines.fixLayoutItemCharacteristics();
	}
	
	public boolean isPerspectiveValid() {
		if (this.perspective != null) {
			final boolean perspectiveValid = this.perspective.isValid();
			if (!perspectiveValid) {
				assert Log.debugMessage(this.perspective.getCodename() , Log.DEBUGLEVEL03);
				JOptionPane.showMessageDialog(this.graph, 
					I18N.getString("Manager.Error.LayoutIsInvalid"),
					I18N.getString("Error"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}
		return true;
	}
	

	public final JGraph getGraph() {
		return this.graph;
	}

	public Perspective getPerspective() {
		return this.perspective;
	}
	
	public final void setPerspective(final Perspective perspective) {
		final long time0 = System.currentTimeMillis();
//		assert Log.debugMessage(perspective, Log.DEBUGLEVEL03);
		assert perspective != null;
		if (this.perspective == perspective) {
//			assert Log.debugMessage(" 1 " + perspective + " is takes " 
//				+ (System.currentTimeMillis() - time0) 
//				+ " ms", Log.DEBUGLEVEL03);
			return;
		}

		if (!this.isPerspectiveValid()) {
//			assert Log.debugMessage(" 2 " + perspective + " is takes " 
//				+ (System.currentTimeMillis() - time0) 
//				+ " ms", Log.DEBUGLEVEL03);
			return;
		}
		this.setPerspectiveTab(perspective);
		assert Log.debugMessage(perspective + " is takes " 
			+ (System.currentTimeMillis() - time0) 
			+ " ms", Log.DEBUGLEVEL03);
	}	

	public final ManagerHandler getManagerHandler() {
		return this.managerHandler;
	}
	
	public final GraphRoutines getGraphRoutines() {
		return this.graphRoutines;
	}
	
	public final PerspectiveTreeModel getTreeModel() {
		return this.treeModel;
	}

	public final CellBuffer getCellBuffer() {
		return this.buffer;
	}
	
}
