/*-
 * $Id: ManagerMainFrame.java,v 1.15 2005/11/07 15:24:19 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.UI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionLauncher;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.manager.ManagerHandler;
import com.syrus.AMFICOM.manager.Perspective;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/11/07 15:24:19 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerMainFrame extends AbstractMainFrame {	
	
	JGraph						graph;

	JTree						tree;

	private NonRootGraphTreeModel		treeModel;

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

	private JToolBar	entityToolBar;
	
	JScrollPane pane;
	JTabbedPane tabbedPane;

	Map<JComponent, Perspective>	perspectiveMap;

	BeanUI	beanUI;

	ManagerHandler	managerHandler;

	private GraphRoutines	graphRoutines;
	
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
		
		
		final boolean localDomainAccess = Boolean.parseBoolean(System.getProperty("manager.localdomain.access"));
		if (localDomainAccess){
			// XXX testing bypass
			super.toolBar.addSeparator();
			final AbstractAction enterDomains = new AbstractAction("D") {	
				@SuppressWarnings({"unqualified-field-access","synthetic-access"})
				public void actionPerformed(ActionEvent e) {
					final JButton button = (JButton) e.getSource();
					button.setEnabled(false);
					windowArranger.arrange();
					ApplicationContext context = ManagerMainFrame.this.getContext();
					ApplicationModel applicationModel = context.getApplicationModel();
					Command command = applicationModel.getCommand(ManagerModel.DOMAINS_COMMAND);
					command.execute();				
				}
			};			
			enterDomains.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('D'));
			enterDomains.putValue(Action.MNEMONIC_KEY, Integer.valueOf(KeyEvent.VK_D));
			super.toolBar.add(enterDomains);
		}
		
		
		
		this.setWindowArranger(new WindowArranger(this) {

			@Override
			public void arrange() {
				ManagerMainFrame f = (ManagerMainFrame) this.mainframe;

				int w = desktopPane1.getSize().width;
				int h = desktopPane1.getSize().height;

				int width = w / 5;

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
		});

		
	
		super.windowArranger.arrange();
		
	}
	

	@Override
	protected void initModule() {
		super.initModule();
		
		final JDesktopPane desktopPane1 = this.desktopPane;
		
		this.frames = new UIDefaults();		
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			@SuppressWarnings("hiding")
			public Object createValue(UIDefaults table) {
				final JScrollPane pane = new JScrollPane(ManagerMainFrame.this.tree);
				final JInternalFrame frame = new JInternalFrame(I18N.getString(TREE_FRAME), true);
				frame.setIconifiable(true);
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
			
			final JPanel panel = new JPanel(new GridBagLayout());
			
			final GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			gbc.weightx = 1.0;

			panel.add(createToolBar(), gbc);
			
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			
			gbc.gridheight = GridBagConstraints.RELATIVE;
			
			panel.add(createEntityToolBar(), gbc);
			
			ManagerMainFrame.this.pane = new JScrollPane(ManagerMainFrame.this.graph);
			
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			ManagerMainFrame.this.tabbedPane = new JTabbedPane();
			
			tabbedPane.getModel().addChangeListener(new ChangeListener() {
					
				
					private ChangeEvent	event;

					public void stateChanged(final ChangeEvent evt) {						
						if (this.event != null) {
							return;
						}						
						this.event = evt;
						ManagerMainFrame.this.setPerspectiveTab(isPerspectiveValid() ?
							ManagerMainFrame.this.perspectiveMap.get(
								ManagerMainFrame.this.tabbedPane.getComponentAt(ManagerMainFrame.this.tabbedPane.getSelectedIndex())) :
							ManagerMainFrame.this.perspective);
						this.event = null;
					}	
			});
			
			panel.add(ManagerMainFrame.this.tabbedPane, gbc);
			
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 0.0;
			
			JInternalFrame frame = new JInternalFrame(I18N.getString(GRAPH_FRAME), true);
			frame.setIconifiable(true);
			desktopPane1.add(frame);
			frame.getContentPane().add(panel);

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
		applicationModel.setCommand(ManagerModel.DOMAINS_COMMAND, new DomainsPerspectiveCommand(this));
		applicationModel.setCommand(ManagerModel.FLUSH_COMMAND, new FlushCommand(this));	
		
		applicationModel.setCommand(TREE_FRAME, this.getShowWindowLazyCommand(this.frames, TREE_FRAME));
		applicationModel.setCommand(GRAPH_FRAME, this.getShowWindowLazyCommand(this.frames, GRAPH_FRAME));
		applicationModel.setCommand(PROPERTIES_FRAME, this.getShowWindowLazyCommand(this.frames, PROPERTIES_FRAME));
		
		final ExtensionLauncher extensionLauncher = ExtensionLauncher.getInstance();
		this.managerHandler = 
			extensionLauncher.getExtensionHandler("com.syrus.AMFICOM.manager.ManagerHandler");
		
		this.managerHandler.setManagerMainFrame(this);
		
		// Construct Model and Graph
		this.graphModel = new ManagerGraphModel();
		
		this.graph = new JGraph(this.graphModel,
			new GraphLayoutCache(this.graphModel,
					new DefaultCellViewFactory(),
					true)) {
			@Override
			public String getToolTipText(MouseEvent e) {
				return e.getX() + " x " + e.getY();
			}
		};
		
		this.graph.setToolTipText("");
		
		this.graphRoutines = new GraphRoutines(this);
		
		//	Use a Custom Marquee Handler
		this.graph.setMarqueeHandler(new ManagerMarqueeHandler(this));

		// Control-drag should clone selection
		this.graph.setCloneable(true);

		// Enable edit without final RETURN keystroke
		this.graph.setInvokesStopCellEditing(true);

		// When over a cell, jump to its default port (we only have one, anyway)
		this.graph.setJumpToDefaultPort(true);
		
		this.createTreeModel();
		
		this.undoManager = new GraphUndoManager() {
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
		this.graph.getModel().addUndoableEditListener(this.undoManager);

		this.createModelListener();		
	}

	@Override
	public void loggedIn() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void loggedOut() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void setDefaultModel(ApplicationModel aModel) {
		super.setDefaultModel(aModel);		
		aModel.setEnabled(ApplicationModel.MENU_VIEW, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		aModel.setEnabled(TREE_FRAME, true);
		aModel.setEnabled(GRAPH_FRAME, true);
		aModel.setEnabled(PROPERTIES_FRAME, true);

		
		aModel.setVisible(ApplicationModel.MENU_VIEW, true);
		aModel.setVisible(ApplicationModel.MENU_VIEW_ARRANGE, true);
		aModel.setVisible(TREE_FRAME, true);
		aModel.setVisible(GRAPH_FRAME, true);
		aModel.setVisible(PROPERTIES_FRAME, true);
		
		aModel.fireModelChanged();
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
			URL connectUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/connecton.gif");
			final ImageIcon connectonIcon = new ImageIcon(connectUrl);
			
			connectUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/connectoff.gif");
			
			final ImageIcon connectoffIcon = new ImageIcon(connectUrl);
			
			AbstractAction action = new AbstractAction("", connectonIcon) {
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
		URL undoUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/undo.gif");
		ImageIcon undoIcon = new ImageIcon(undoUrl);
		this.undo = new AbstractAction("", undoIcon) {
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		};
		this.undo.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.Undo"));
		this.undo.setEnabled(false);
//		toolBar.add(this.undo);

		// Redo
		URL redoUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/redo.gif");
		ImageIcon redoIcon = new ImageIcon(redoUrl);
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
		Action action;
		URL url;

		// Copy
		action = TransferHandler // JAVA13:
												// org.jgraph.plaf.basic.TransferHandler
				.getCopyAction();
		url = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/copy.gif");
		action.putValue(Action.SMALL_ICON, new ImageIcon(url));
		// Remove
		URL removeUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		this.remove = new AbstractAction("", removeIcon) {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				if (!ManagerMainFrame.this.graph.isSelectionEmpty()) {
					Object[] cells = ManagerMainFrame.this.graph.getSelectionCells();
					GraphModel model = ManagerMainFrame.this.graph.getModel();
					cells = ManagerMainFrame.this.graph.getDescendants(cells);
					List list = new ArrayList(3 * cells.length / 2);
					for(Object cell: cells) {
						if (model.isPort(cell)) {
							Port port = (Port)cell;
							for(Iterator it=port.edges();it.hasNext();) {
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
		
		graphToolBar.addSeparator();
		{
			// Zoom Std

			URL zoomUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoom.gif");
			ImageIcon zoomIcon = new ImageIcon(zoomUrl);
			Action zoom = new AbstractAction("", zoomIcon) {
				
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
			URL zoomInUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoomin.gif");
			ImageIcon zoomInIcon = new ImageIcon(zoomInUrl);
			AbstractAction zoomIn = new AbstractAction("", zoomInIcon) {
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setScale(2.0 * ManagerMainFrame.this.graph.getScale());
				}
			};
			graphToolBar.add(zoomIn);
			zoomIn.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.ZoomIn"));
		}
		
		{
			// Zoom Out
			URL zoomOutUrl = getClass().getClassLoader().getResource(
					"com/syrus/AMFICOM/manager/resources/icons/zoomout.gif");
			ImageIcon zoomOutIcon = new ImageIcon(zoomOutUrl);
			AbstractAction zoomOut = new AbstractAction("", zoomOutIcon) {
				public void actionPerformed(ActionEvent e) {
					ManagerMainFrame.this.graph.setScale(ManagerMainFrame.this.graph.getScale() / 2.0);
				}
			};
			graphToolBar.add(zoomOut);
			zoomOut.putValue(Action.SHORT_DESCRIPTION, I18N.getString("Manager.Action.ZoomOut"));
		}
		
		
		graphToolBar.addSeparator();
		
		{
			
			AbstractAction flush = new AbstractAction("", UIManager.getIcon(ResourceKeys.ICON_REFRESH)) {
				
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
	
	final JToolBar createEntityToolBar() {
		this.entityToolBar = new JToolBar(SwingConstants.VERTICAL);
		this.entityToolBar.setFloatable(false);
		return this.entityToolBar;
	}
	
	public final JButton addAction(final AbstractAction abstractAction) {
		return this.entityToolBar.add(abstractAction);
	}	

	private void createTreeModel() {		
		this.treeModel = new NonRootGraphTreeModel(this.graph.getModel(), this.graphRoutines);
		this.tree = new JTree(this.treeModel);
		
		this.tree.setRootVisible(true);
		
		this.graph.getSelectionModel().addGraphSelectionListener(new ManagerGraphSelectionListener(this));
		
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				final Object lastPathComponent = e.getPath().getLastPathComponent();
				final GraphSelectionModel selectionModel = ManagerMainFrame.this.graph.getSelectionModel();
				if (e.isAddedPath()) {
					selectionModel.setSelectionCell(lastPathComponent);
				} 
			}
		});
	}
	
	final void setPerspectiveTab(final Perspective perspective) {
		try {		
			
			final String codename = perspective.getCodename();			
			int index = -1;
			for(int i=0; i < this.tabbedPane.getTabCount(); i++) {
				if (this.perspectiveMap.get(this.tabbedPane.getComponentAt(i)).getCodename().equals(codename)) {
					index = i;
					break;
				}
			}			
			
			if (index == -1) {
				final JPanel panel = new JPanel(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 1.0;
				gbc.weighty = 1.0;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridheight = GridBagConstraints.REMAINDER;				
				panel.add(this.pane, gbc);
				
				if (this.perspectiveMap == null) {
					this.perspectiveMap = new HashMap<JComponent, Perspective>();
				}
				
				this.perspectiveMap.put(panel, perspective);
				this.tabbedPane.addTab(perspective.getName(), panel);
				this.tabbedPane.setSelectedComponent(panel);
				return;
			}
			
			this.perspective = perspective;
			
			final JPanel panel = (JPanel) this.tabbedPane.getComponentAt(index);
			panel.removeAll();
			final GridBagLayout gridLayout = (GridBagLayout) panel.getLayout();
			final GridBagConstraints gbc = gridLayout.getConstraints(panel);
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridheight = GridBagConstraints.REMAINDER;				
			panel.add(this.pane, gbc);
			this.tabbedPane.setSelectedIndex(index);		
			
			this.perspective.createNecessaryItems();	
			
			// here items put into graphCache, when they can be used
			this.graphRoutines.arrangeLayoutItems();
			
			this.perspective.perspectiveApplied();
			final JInternalFrame frame = 
				(JInternalFrame) this.frames.get(GRAPH_FRAME);
			frame.setTitle(I18N.getString(GRAPH_FRAME) 
				+ " : " 
				+ this.perspective.getName());
						
			this.entityToolBar.removeAll();
			this.perspective.addEntities(this.entityToolBar);
			this.entityToolBar.revalidate();
			this.entityToolBar.repaint();
			
		} catch (final ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isPerspectiveValid() {
		if (this.perspective != null) {
			if (!this.perspective.isValid()) {
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
		assert perspective != null;		
		if (!this.isPerspectiveValid()) {
			return;
		}
		this.setPerspectiveTab(perspective);
	}	

	public final ManagerHandler getManagerHandler() {
		return this.managerHandler;
	}
	
	public final GraphRoutines getGraphRoutines() {
		return this.graphRoutines;
	}


	
	public final NonRootGraphTreeModel getTreeModel() {
		return this.treeModel;
	}
}
