/*-
 * $Id: ManagerMainFrame.java,v 1.4 2005/09/06 16:16:26 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager.UI;

import static com.syrus.AMFICOM.manager.DomainBeanWrapper.KEY_NAME;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.UndoableEditEvent;
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
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphSelectionModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.AbstractMainMenuBar;
import com.syrus.AMFICOM.client.model.AbstractMainToolBar;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.ApplicationModelListener;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.manager.ARMBeanFactory;
import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.Bean;
import com.syrus.AMFICOM.manager.DomainBeanFactory;
import com.syrus.AMFICOM.manager.LangModelManager;
import com.syrus.AMFICOM.manager.MCMBeanFactory;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.NetBeanFactory;
import com.syrus.AMFICOM.manager.Perspective;
import com.syrus.AMFICOM.manager.RTUBeanFactory;
import com.syrus.AMFICOM.manager.ServerBeanFactory;
import com.syrus.AMFICOM.manager.UserBeanFactory;
import com.syrus.AMFICOM.resource.LayoutItem;
import com.syrus.AMFICOM.resource.LayoutItemWrapper;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/09/06 16:16:26 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerMainFrame extends AbstractMainFrame implements GraphSelectionListener {	
	
	JGraph						graph;

	JTree						tree;

	NonRootGraphTreeModel		treeModel;

	JPanel				propertyPanel;

	// Undo Manager
	protected GraphUndoManager	undoManager;

	// Actions which Change State
	protected Action			undo, redo, remove, group, ungroup, tofront, toback,
			cut, copy, paste;

	GridBagConstraints	gbc2;

	public JButton				userButton;

	public JButton				armButton;

	public JButton				rtuButton;

	public JButton				serverButton;

	public JButton				mcmButton;

	public JButton				netButton;

	public JButton				domainButton;

	public JButton				domainsButton;

	public JLabel				currentPerspectiveLabel;

	Perspective			perspective;
	
	Identifier			xTypeId;
	Identifier			yTypeId;
	Identifier			nonStorableObjectNameTypeId;
	
	boolean						arranging;
	
	private Map<String, AbstractBean> beanMap;
	private Map<String, AbstractBeanFactory> factoryMap;

	private ManagerGraphModel	graphModel;
	
	UIDefaults					frames;
	
	static final String TREE_FRAME = "Label.ElementsTree";
	static final String GRAPH_FRAME = "Label.Graph";
	static final String PROPERTIES_FRAME = "Label.Properties";
	
	public ManagerMainFrame(final ApplicationContext aContext) {
		super(aContext, "Manager", new AbstractMainMenuBar(aContext.getApplicationModel()) {

			private JMenu				menuView;
			@Override
			protected void addMenuItems() {

				this.menuView = new JMenu(LangModelGeneral.getString(ApplicationModel.MENU_VIEW));
				this.menuView.setName(ApplicationModel.MENU_VIEW);

				final JMenuItem menuViewTreeItem = new JMenuItem(LangModelManager.getString(TREE_FRAME));
				menuViewTreeItem.setName(TREE_FRAME);
				menuViewTreeItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewTreeItem);

				final JMenuItem menuViewGraphItem = new JMenuItem(LangModelManager.getString(GRAPH_FRAME));
				menuViewGraphItem.setName(GRAPH_FRAME);
				menuViewGraphItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewGraphItem);

				final JMenuItem menuViewPropertiesItem = new JMenuItem(LangModelManager.getString(PROPERTIES_FRAME));
				menuViewPropertiesItem.setName(PROPERTIES_FRAME);
				menuViewPropertiesItem.addActionListener(this.actionAdapter);
				this.menuView.add(menuViewPropertiesItem);

				this.menuView.addSeparator();

				final JMenuItem menuViewArrangeItem = new JMenuItem(LangModelGeneral.getString(ApplicationModel.MENU_VIEW_ARRANGE));
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

				JInternalFrame treeFrame = ((JInternalFrame) f.frames.get(TREE_FRAME));
				JInternalFrame graphFrame = ((JInternalFrame) f.frames.get(GRAPH_FRAME));
				JInternalFrame propertiesFrame = ((JInternalFrame) f.frames.get(PROPERTIES_FRAME));

				normalize(treeFrame);
				normalize(graphFrame);
				normalize(propertiesFrame);
				
				treeFrame.setSize(width, h);
				graphFrame.setSize(w - 2 * width, h);
				propertiesFrame.setSize(width, h);
				
				
				treeFrame.setLocation(0, 0);
				graphFrame.setLocation(treeFrame.getWidth(), 0);
				propertiesFrame.setLocation(graphFrame.getX() + graphFrame.getWidth(), 0);
				
				treeFrame.setVisible(true);
				graphFrame.setVisible(true);
				propertiesFrame.setVisible(true);
				
				// XXX bypass for local testing
				ManagerMainFrame.this.domainsButton.doClick();
			}
		});

		this.frames = new UIDefaults();		
		
		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				JScrollPane pane = new JScrollPane(ManagerMainFrame.this.tree);
				JInternalFrame frame = new JInternalFrame(LangModelManager.getString(TREE_FRAME), true);
				desktopPane1.add(frame);
				frame.getContentPane().add(pane);
				return frame;
			}
		});		
		
		this.frames.put(GRAPH_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table){
			
			// show graph frame
			
			JPanel panel = new JPanel(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			Box box = Box.createHorizontalBox();
			
			final JToolBar perspectiveBox = createPerspecives();
			ManagerMainFrame.this.currentPerspectiveLabel = new JLabel();
			
			box.add(new JLabel(LangModelManager.getString("Label.Levels") + ':'));
			box.add(perspectiveBox);
			
			
			panel.add(box, gbc);
			
			gbc.weightx = 1.0;

			panel.add(createToolBar(), gbc);
			
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			
			gbc.gridheight = GridBagConstraints.RELATIVE;
			
			panel.add(createEntityToolBar(), gbc);
			
			JScrollPane pane = new JScrollPane(ManagerMainFrame.this.graph);
			
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			panel.add(pane, gbc);
			
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 0.0;
			panel.add(ManagerMainFrame.this.currentPerspectiveLabel, gbc);
			
			JInternalFrame frame = new JInternalFrame(LangModelManager.getString(GRAPH_FRAME), true);
			desktopPane1.add(frame);
			frame.getContentPane().add(panel);

			frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));			
			return frame;
		}
		});		
		
		this.frames.put(PROPERTIES_FRAME, new UIDefaults.LazyValue() {

				public Object createValue(UIDefaults table){
				
	//			 show property frame
				JInternalFrame frame = new JInternalFrame(LangModelManager.getString(PROPERTIES_FRAME), true);
				desktopPane1.add(frame);
				ManagerMainFrame.this.propertyPanel = new JPanel(new GridBagLayout());
				ManagerMainFrame.this.gbc2 = new GridBagConstraints();
				ManagerMainFrame.this.gbc2.fill = GridBagConstraints.BOTH;
				ManagerMainFrame.this.gbc2.weightx = 1.0;
				ManagerMainFrame.this.gbc2.weighty = 1.0;
				ManagerMainFrame.this.gbc2.gridwidth = GridBagConstraints.REMAINDER;
	
				
				frame.getContentPane().add(ManagerMainFrame.this.propertyPanel);			
				return frame;
	
			}
		});	
	
		super.windowArranger.arrange();
		
	}
	

	@Override
	protected void initModule() {
		super.initModule();
		
		final ApplicationModel applicationModel = this.aContext.getApplicationModel();
		applicationModel.setCommand(ManagerModel.DOMAINS_COMMAND, new DomainsPerspective(this));
		applicationModel.setCommand(ManagerModel.FLUSH_COMMAND, new FlushCommand());	
		
		this.beanMap = new HashMap<String, AbstractBean>();
		this.factoryMap = new HashMap<String, AbstractBeanFactory>();
		
		// Construct Model and Graph
		this.graphModel = new ManagerGraphModel();
		
		this.graph = new JGraph(this.graphModel,
			new GraphLayoutCache(this.graphModel,
					new DefaultCellViewFactory(),
					true));
		
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
		this.graph.getModel().addGraphModelListener(
			new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {
				if (ManagerMainFrame.this.arranging) {
					return;
				}
				GraphModelChange change = e.getChange();
				
				GraphModel model = ManagerMainFrame.this.graph.getModel();
				
				Object[] inserted = change.getInserted();
				Object[] changed = change.getChanged();
				Object[] removed = change.getRemoved();
				
				if (inserted != null) {
					for(Object insertedObject : inserted) {
						if (model.isPort(insertedObject)) {
							TreeNode[] pathToRoot = ManagerMainFrame.this.treeModel.getPathToRoot((TreeNode) insertedObject);
							if (pathToRoot != null)
								ManagerMainFrame.this.tree.scrollPathToVisible(new TreePath(pathToRoot));
						}
					}
				}
				
				if (changed != null && removed == null) {
					for(Object changedObject : changed) {
						Log.debugMessage(".graphChanged() | changedObject " + changedObject + '[' + changedObject.getClass().getName() + ']', Log.DEBUGLEVEL10);
						if (model.isPort(changedObject)) {
							TreeNode[] pathToRoot = ManagerMainFrame.this.treeModel.getPathToRoot((TreeNode) changedObject);
							if (pathToRoot != null) {
								ManagerMainFrame.this.tree.scrollPathToVisible(new TreePath(pathToRoot));
							}
						} else  if (model.isEdge(changedObject)) {
							Edge edge = (Edge) changedObject;
							final MPort source = (MPort) edge.getSource();
							final MPort target = (MPort) edge.getTarget();
							source.updateCache();
							target.updateCache();
							
							Log.debugMessage(".graphChanged() | " + source  +" -> " + target,
								Log.DEBUGLEVEL10);
							AbstractBean bean = source.getUserObject();
							
							
							ConnectionSet connectionSet = change.getConnectionSet();							
							MPort source2 = (MPort) connectionSet.getPort(edge, true);
							MPort target2 = (MPort) connectionSet.getPort(edge, false);

							Log.debugMessage(".graphChanged() | ' " + source2  +" -> " + target2, Log.DEBUGLEVEL10);
							
							if (source2 != null) {
								source2.updateCache();
							}
							
							if (target2 == null) {
								AbstractBean bean2 = source2.getUserObject();
								Log.debugMessage(".graphChanged() | " + bean2, Log.DEBUGLEVEL10);
								bean2.applyTargetPort(null, null);
								try {
									LayoutItem layoutItem = this.getLayoutItem(bean2.getCodeName());
									Log.debugMessage(
										"JGraphText.createModelListener | set layoutItem:" 
										+ layoutItem.getName() 
										+ ", parentId:" + Identifier.VOID_IDENTIFIER,
										Log.DEBUGLEVEL10);
									layoutItem.setParentId(Identifier.VOID_IDENTIFIER);
								} catch (ApplicationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							} else {
								target2.updateCache();
							}
							bean.applyTargetPort(target2, target);
							
							
							if (!ManagerMainFrame.this.arranging) {								
								String codeName = bean.getCodeName();
								try {									
									LayoutItem sourceItem = this.getLayoutItem(codeName);
									Identifier targetItemId = 
										target != null ? 
												this.getLayoutItem(target.getBean().getCodeName()).getId() :
												Identifier.VOID_IDENTIFIER;
									sourceItem.setParentId(targetItemId);
								} catch (ApplicationException e2) {
									e2.printStackTrace();
									JOptionPane.showMessageDialog(ManagerMainFrame.this.graph, 
										e2.getMessage(), 
										LangModelManager.getString("Error"),
										JOptionPane.ERROR_MESSAGE);
								}
							}
							
						} else {
							DefaultGraphCell cell = (DefaultGraphCell)changedObject;
							MPort port = (MPort) cell.getChildAt(0);
							AbstractBean bean = port.getBean();
							if (bean != null) {
								bean.setName((String) cell.getUserObject());
							}
							
							AttributeMap attributes = cell.getAttributes();
							Rectangle2D rectangle2D = GraphConstants.getBounds(attributes);
							String title = cell.getUserObject().toString();

							if (!ManagerMainFrame.this.arranging) {								
								String codeName = bean.getCodeName();
								try {
									LayoutItem item = this.getLayoutItem(codeName);
									if (item == null) {
										item = LayoutItem.createInstance(LoginManager.getUserId(),
											Identifier.VOID_IDENTIFIER,
											ManagerMainFrame.this.perspective.getPerspectiveName(),
											codeName);
										
										Characteristic.createInstance(LoginManager.getUserId(),
											(CharacteristicType) StorableObjectPool.getStorableObject(ManagerMainFrame.this.xTypeId, true),
											"x",
											"x",
											Integer.toString((int) rectangle2D.getX()),
											item,
											true,
											true);
										
										Characteristic.createInstance(LoginManager.getUserId(),
											(CharacteristicType) StorableObjectPool.getStorableObject(ManagerMainFrame.this.yTypeId, true),
											"y",
											"y",
											Integer.toString((int) rectangle2D.getY()),
											item,
											true,
											true);
										
										if (bean.getId().isVoid()) {
											Characteristic.createInstance(LoginManager.getUserId(),
												(CharacteristicType) StorableObjectPool.getStorableObject(ManagerMainFrame.this.nonStorableObjectNameTypeId, true),
												"title",
												"title",
												title,
												item,
												true,
												true);
										}
										
									} else {
										for(Characteristic characteristic : item.getCharacteristics(false)) {
											String codename = characteristic.getType().getCodename();
											if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
												characteristic.setValue(Integer.toString((int) rectangle2D.getX()));
											} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
												characteristic.setValue(Integer.toString((int) rectangle2D.getY()));
											} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
												characteristic.setValue(title);
											}
										}
	
									}
									
								} catch (ApplicationException e2) {
									e2.printStackTrace();
									JOptionPane.showMessageDialog(ManagerMainFrame.this.graph, 
										e2.getMessage(), 
										LangModelManager.getString("Error"),
										JOptionPane.ERROR_MESSAGE);
								}
							}
						
						}
					}
				} 
				if (removed != null) {
					if (changed != null) {
						for(Object changedObject : changed) {
							System.out.println(".graphChanged() | changedObject after delete " + changedObject + '[' + changedObject.getClass().getName() + ']');
						}
					}
					for(Object removedObject : removed) {
						System.out.println(".graphChanged() | removedObject " + removedObject + '[' + removedObject.getClass().getName() + ']');
						 if (model.isEdge(removedObject)) {
							Edge edge = (Edge) removedObject;
							ConnectionSet connectionSet = change.getConnectionSet();							
							MPort source = (MPort) connectionSet.getPort(edge, true);
							MPort target = (MPort) connectionSet.getPort(edge, false);

							AbstractBean bean = source.getUserObject();
							bean.applyTargetPort(target, null);
							try {
								final LayoutItem layoutItem = this.getLayoutItem(bean.getCodeName());
								Log.debugMessage(".graphChanged | removedObject | layoutItem:" 
										+ layoutItem.getName() 
										+ ", layoutName:" 
										+ layoutItem.getLayoutName(),
									Log.DEBUGLEVEL10);
								layoutItem.setParentId(Identifier.VOID_IDENTIFIER);
								
								bean.dispose();																
							} catch (ApplicationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							
						 } 
					}
				}
			}
			
			private LayoutItem getLayoutItem(final String codename) throws ApplicationException {
				CompoundCondition compoundCondition = 
					new CompoundCondition(new TypicalCondition(
						ManagerMainFrame.this.perspective.getPerspectiveName(), 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.LAYOUT_ITEM_CODE,
						LayoutItemWrapper.COLUMN_LAYOUT_NAME),
						CompoundConditionSort.AND,
						new LinkedIdsCondition(
							LoginManager.getUserId(),
							ObjectEntities.LAYOUT_ITEM_CODE)
//			XXX uncommented when using javac				
//							{
//						
//							@Override
//							public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
//								return storableObjects.isEmpty();
//							}
//						}
			);
				
				compoundCondition.addCondition(new TypicalCondition(
					codename, 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.LAYOUT_ITEM_CODE,
					StorableObjectWrapper.COLUMN_NAME));
				
				Set<LayoutItem> layoutItems = 
					StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
				
				if (layoutItems.isEmpty()) {
					return null;
				}
				return layoutItems.iterator().next();
			}
			
		});
	}
	
	JToolBar createPerspecives() {		
		
		JToolBar perspectives = new JToolBar();
		
		perspectives.setFloatable(false);
		
		this.domainsButton = perspectives.add(new AbstractAction(LangModelManager.getString("Action.Domains")) {
			public void actionPerformed(ActionEvent e) {
				ApplicationContext context = ManagerMainFrame.this.getContext();
				ApplicationModel applicationModel = context.getApplicationModel();
				Command command = applicationModel.getCommand(ManagerModel.DOMAINS_COMMAND);
				command.execute();
//					ManagerMainFrame.this.getContext().getApplicationModel().getCommand(ManagerModel.DOMAINS_COMMAND).execute();
			}
		});	

		perspectives.addSeparator();
		
		JButton button = perspectives.add(new AbstractAction("", new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/refresh.gif"))) {
			
			public void actionPerformed(ActionEvent e) {
				ManagerMainFrame.this.getContext().getApplicationModel().getCommand(ManagerModel.FLUSH_COMMAND).execute();
				}
		});
		
		button.setToolTipText(LangModelManager.getString("Action.Save"));
		
		return perspectives;
	}
	
	private void arrangeLayoutItems() throws ApplicationException {
		this.arranging = true;
		TypicalCondition typicalCondition = new TypicalCondition(
			this.perspective.getPerspectiveName(), 
			OperationSort.OPERATION_EQUALS,
			ObjectEntities.LAYOUT_ITEM_CODE,
			LayoutItemWrapper.COLUMN_LAYOUT_NAME);
		
		LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
			LoginManager.getUserId(),
			ObjectEntities.LAYOUT_ITEM_CODE) {
			@Override
			public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
				return storableObjects.isEmpty();
			}
		};
		
		CompoundCondition compoundCondition = 
			new CompoundCondition(typicalCondition,
				CompoundConditionSort.AND,
				linkedIdsCondition);
		
		
		Set<LayoutItem> layoutItems = 
			StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
		
		if (this.xTypeId == null) {
			typicalCondition = new TypicalCondition(
				LayoutItem.CHARACTERISCTIC_TYPE_X, 
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.CHARACTERISTIC_TYPE_CODE,
				StorableObjectWrapper.COLUMN_CODENAME);
			
			Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			
			this.xTypeId = storableObjectsByCondition.iterator().next().getId();
		
		}
		if (this.yTypeId == null) {
			typicalCondition = new TypicalCondition(
					LayoutItem.CHARACTERISCTIC_TYPE_Y, 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
				
			Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			
			this.yTypeId = storableObjectsByCondition.iterator().next().getId();
		}
		
		if (this.nonStorableObjectNameTypeId == null) {
			typicalCondition = new TypicalCondition(
					LayoutItem.CHARACTERISCTIC_TYPE_NAME, 
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.CHARACTERISTIC_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME);
				
			Set<CharacteristicType> storableObjectsByCondition = 
				StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
			
			this.nonStorableObjectNameTypeId = storableObjectsByCondition.iterator().next().getId();
		}

		for(LayoutItem layoutItem : layoutItems) {
			this.arrangeCell(layoutItem);
		}
		this.arranging = false;	
	}

	public DefaultGraphCell arrangeCell(final LayoutItem item) 
	throws NumberFormatException, ApplicationException {
		GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		GraphModel model = this.graph.getModel();
		
		DefaultGraphCell parentCell = null;
		Identifier parentId = item.getParentId();
		if (!parentId.isVoid()) {
			LayoutItem parentItem = StorableObjectPool.getStorableObject(parentId, true);
			parentCell = this.arrangeCell(parentItem);
		}
		
		
		String name = item.getName();		
		DefaultGraphCell itemCell = null;		
		int x = 0;
		int y = 0;
		String title = null;
		for(Characteristic characteristic : item.getCharacteristics(false)) {
			String codename = characteristic.getType().getCodename();
			if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_X)) {
				x = Integer.parseInt(characteristic.getValue());
			} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_Y)) {
				y = Integer.parseInt(characteristic.getValue());
			} else if (codename.equals(LayoutItem.CHARACTERISCTIC_TYPE_NAME)) {
				title = characteristic.getValue();
			}
		}
		
		for(int i = 0; i<model.getRootCount(); i++) {
			DefaultGraphCell cell = (DefaultGraphCell) model.getRootAt(i);
			if (!model.isEdge(cell) && !model.isPort(cell)) {				
				MPort port = (MPort) cell.getChildAt(0);
				AbstractBean bean = port.getBean();
				if (name.equals(bean.getCodeName())) {
					if (!graphLayoutCache.isVisible(cell)) {
						graphLayoutCache.setVisible(cell, true);
					}
					
					itemCell = cell;
					
					AttributeMap attributeMap = new AttributeMap();
					GraphConstants.setBounds(attributeMap,
						new Rectangle2D.Double(x, y, 0, 0));
					if (title != null) {
						GraphConstants.setValue(attributeMap,
							title);
					}
					Map viewMap = new Hashtable();
					viewMap.put(itemCell, attributeMap);
					model.edit(viewMap, null, null, null);
					
					
					if (parentCell != null) {
						MPort parentPort = (MPort) parentCell.getChildAt(0);
						Edge connectionEdge = null;
						for(int j = 0; j<model.getRootCount(); j++) {
							DefaultGraphCell edgeCell = (DefaultGraphCell) model.getRootAt(j);
							if (model.isEdge(edgeCell)) {
								Edge edge = (Edge) edgeCell;
								Object target = edge.getSource();
								Object source = edge.getTarget();								
								if (target == port && source == parentPort) {
									connectionEdge = edge;
									break;
								}
							}
						}
						
						
						if (connectionEdge != null) {
							// make edge visible
							if (!graphLayoutCache.isVisible(connectionEdge)) {
								graphLayoutCache.setVisible(connectionEdge, true);
							}
						} else {
							// otherwise create edge
							this.createEdge(cell, parentCell);
						}
					}
					
				}
			}
		}
		
		if (itemCell == null) {
			AbstractBeanFactory factory = this.getFactory(name);
			
			AbstractBean bean = this.getCell(item);
			
			itemCell = this.createChild(
				null, 
				title != null ? title : bean.getName(), 
				bean, 
				x, 
				y, 
				0, 
				0, 
				factory.getImage());
			if (!graphLayoutCache.isVisible(itemCell)) {
				graphLayoutCache.setVisible(itemCell, true);
			}
			
			if (parentCell != null) {
				MPort port = (MPort) itemCell.getChildAt(0);
				MPort parentPort = (MPort) parentCell.getChildAt(0);
				Edge connectionEdge = null;
				for(int j = 0; j<model.getRootCount(); j++) {
					DefaultGraphCell edgeCell = (DefaultGraphCell) model.getRootAt(j);
					if (model.isEdge(edgeCell)) {
						Edge edge = (Edge) edgeCell;
						Object target = edge.getSource();
						Object source = edge.getTarget();
						
						if (target == port && source == parentPort) {
							connectionEdge = edge;
							break;
						}
					}
				}
				
				if (connectionEdge != null) {
					// make edge visible
					if (!graphLayoutCache.isVisible(connectionEdge)) {
						graphLayoutCache.setVisible(connectionEdge, true);
					}
				} else {
					// otherwise create edge
					this.createEdge(itemCell, parentCell);
				}
			}
		}
		
		return itemCell;
	}
	
	private AbstractBeanFactory getFactory(final String name) {
		for(final String  codename: this.factoryMap.keySet()) {
			if (name.startsWith(codename)) {
				return this.factoryMap.get(codename);
			}
		}
		return null;
	}
	
	public AbstractBean getCell(final LayoutItem layoutItem) {
		final String name = layoutItem.getName();
		
		AbstractBean bean = this.beanMap.get(name);
		
		if (bean == null) {
			AbstractBeanFactory factory = this.getFactory(name);
			bean = factory.createBean(name);
			this.beanMap.put(name, bean);
		}
		
		return bean;
	}
	
	public void showOnly(final String[] names) {
		final GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		final GraphModel model = this.graph.getModel();
		
		for(int i = 0; i<model.getRootCount(); i++) {
			final Object rootAt = model.getRootAt(i);
			if (!model.isEdge(rootAt) && !model.isPort(rootAt)) {
				MPort port = (MPort) ((TreeNode)rootAt).getChildAt(0);
				AbstractBean bean = port.getBean();				
				boolean hide = true;
				
				if (bean != null) {
					String codeName = bean.getCodeName();
					for (String name : names) {
						if (codeName.startsWith(name)) {
							hide = false;
							break;
						}
					}
				}
				
				graphLayoutCache.setVisible(port.getParent(), !hide);
			}
		}
		
		this.treeModel.removeAllAvailableCodenames();
		for(String codename: names) {
			this.treeModel.addAvailableCodename(codename);
		}
		
		this.graph.repaint();
		this.undoManager.discardAllEdits();
		this.updateHistoryButtons();
	}
	
	public void hideTillCell(final MPort startPort, 
	                         final MPort port) {
		
		GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		graphLayoutCache.setVisible(startPort.getParent(), false);

		if (startPort == port) {
			return;
		}
		final List<Port> targets = startPort.getSources();
		
		for(Port targetPort: targets) {			
				this.hideTillCell((MPort) targetPort, port);
		}
	}
	
	public void showOnlyDescendants(final DefaultGraphCell cell) {		
		GraphModel model = this.graph.getModel();		
		
		MPort selectedPort = (MPort) cell.getChildAt(0);
		
		for(int i = 0; i<model.getRootCount(); i++) {
			Object rootAt = model.getRootAt(i);
			if (!model.isEdge(rootAt) && !model.isPort(rootAt)) {
				MPort port = (MPort) ((TreeNode)rootAt).getChildAt(0);
				List<Port> sources = port.getTargets();
				if (sources.isEmpty()) {
					this.hideTillCell(port, selectedPort);
				}
			}
			
		}
		this.treeModel.setRoot(cell);
	}
	
	JToolBar createToolBar() {
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
					ManagerMainFrame.this.graph.setPortsVisible(!ManagerMainFrame.this.graph.isPortsVisible());
					this.putValue(SMALL_ICON, ManagerMainFrame.this.graph.isPortsVisible() ? connectonIcon : connectoffIcon);
					this.putValue(SHORT_DESCRIPTION, LangModelManager.getString(ManagerMainFrame.this.graph.isPortsVisible() ?  "Action.connectionEnable" : "Action.connectionDisable"));
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
		this.redo.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.Redo"));
		this.redo.setEnabled(false);
//		toolBar.add(this.redo);

		//
		// Edit Block
		//
		toolBar.addSeparator();
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
		toolBar.add(this.remove);
		this.remove.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.Delete"));
		
		toolBar.addSeparator();
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
					ManagerMainFrame.this.graph.setScale(2.0 * ManagerMainFrame.this.graph.getScale());
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
					ManagerMainFrame.this.graph.setScale(ManagerMainFrame.this.graph.getScale() / 2.0);
				}
			};
			toolBar.add(zoomOut);
			zoomOut.putValue(Action.SHORT_DESCRIPTION, LangModelManager.getString("Action.ZoomOut"));
		}
		
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
	
	JToolBar createEntityToolBar() {
		JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
		toolBar.setFloatable(false);
		this.userButton = this.createAction(toolBar, UserBeanFactory.getInstance(this));
		this.armButton = this.createAction(toolBar, ARMBeanFactory.getInstance(this));
		toolBar.addSeparator();
		this.rtuButton = this.createAction(toolBar, RTUBeanFactory.getInstance(this));
		this.serverButton = this.createAction(toolBar, ServerBeanFactory.getInstance(this));
		this.mcmButton = this.createAction(toolBar, MCMBeanFactory.getInstance(this));
		toolBar.addSeparator();
		this.netButton = this.createAction(toolBar, NetBeanFactory.getInstance(this));
		toolBar.addSeparator();
		this.domainButton = this.createAction(toolBar, DomainBeanFactory.getInstance(this));
		return toolBar;
	}	
	
	private JButton createAction(final JToolBar toolBar,
	                          final AbstractBeanFactory factory) {
		final String name = factory.getName();
		Icon icon = factory.getIcon();
		this.factoryMap.put(factory.getCodename(), factory);
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			private Map<String, Integer> entityIndices;
			
			public void actionPerformed(ActionEvent e) {
				try {
					AbstractBean bean = factory.createBean(ManagerMainFrame.this.perspective);
					if (this.entityIndices == null) {
						this.entityIndices = new HashMap<String, Integer>();
					}
					Integer i = this.entityIndices.get(name);
					int index = (i != null ? i : 0) + 1;
					this.entityIndices.put(name, index);
					ManagerMainFrame.this.createChild(null, 
						factory.getShortName() + "-" + index, 
						bean, 
						20, 
						20, 
						0, 
						0, 
						factory.getImage());
				} catch (CreateObjectException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ManagerMainFrame.this.graph, 
						e1.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				} catch (IllegalObjectEntityException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(ManagerMainFrame.this.graph, 
						e1.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);

				}
				
				
			}
		};		
	
		action.putValue(Action.SHORT_DESCRIPTION, name);
		return toolBar.add(action);
	}
	

	DefaultEdge createEdge(final DefaultGraphCell source, 
	                       final DefaultGraphCell target) {
		return this.createEdge(source, 
			target, 
			true);
	}
	
	private DefaultEdge createEdge(final DefaultGraphCell source, 
	                               final DefaultGraphCell target,
	                               final boolean addToGraph) {
		
		MPort sourcePort = (MPort) source.getChildAt(0);
		MPort targetPort = (MPort) target.getChildAt(0);
		if (sourcePort != targetPort) {
			final boolean canConnect =
				this.graphModel.isConnectionPermitted(sourcePort, targetPort);
			if (canConnect) {
				DefaultEdge edge = new DefaultEdge();
				edge.setSource(sourcePort);
				edge.setTarget(targetPort);
//				 Set Arrow Style for edge
				this.createEdgeAttributes(edge);
				
				GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
				graphLayoutCache.insert(edge);		
				graphLayoutCache.setVisibleImpl(new Object[] {edge}, addToGraph);
				
				this.graph.getSelectionModel().clearSelection();
				try {
					CompoundCondition compoundCondition = 
						new CompoundCondition(new TypicalCondition(
							this.perspective.getPerspectiveName(), 
							OperationSort.OPERATION_EQUALS,
							ObjectEntities.LAYOUT_ITEM_CODE,
							LayoutItemWrapper.COLUMN_LAYOUT_NAME),
							CompoundConditionSort.AND,
							new LinkedIdsCondition(
								LoginManager.getUserId(),
								ObjectEntities.LAYOUT_ITEM_CODE) {
								@Override
								public boolean isNeedMore(Set< ? extends StorableObject> storableObjects) {
									return storableObjects.isEmpty();
								}
							});
					
					AbstractBean targetBean = targetPort.getBean();
					AbstractBean sourceBean = sourcePort.getBean();
					
					TypicalCondition condition = new TypicalCondition(
						sourceBean.getCodeName(), 
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.LAYOUT_ITEM_CODE,
						StorableObjectWrapper.COLUMN_NAME);
					
					compoundCondition.addCondition(condition);
					
					Set<LayoutItem> sourceItems = 
						StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
					
					condition.setValue(targetBean.getCodeName());

					Set<LayoutItem> targetItems = 
						StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
					
					assert !sourceItems.isEmpty();
					assert !targetItems.isEmpty();
					
					LayoutItem sourceItem = sourceItems.iterator().next();
					LayoutItem targetItem = targetItems.iterator().next();
					
					sourceItem.setParentId(targetItem.getId());
					
				} catch (ApplicationException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this.graph, 
						e.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				}
				
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

	
	DefaultGraphCell createChild(DefaultGraphCell parentCell, String name, Object object, double x,
	         	             			double y, double w, double h, Icon image) {
		DefaultGraphCell cell = this.createVertex(name, object, x, y, w, h, image);
 		GraphLayoutCache cache = this.graph.getGraphLayoutCache();
		cache.insert(cell);	

 		if (parentCell != null) {
 			DefaultEdge edge = this.createEdge(cell, parentCell);
 			if (object instanceof AbstractBean) {
				AbstractBean bean = (AbstractBean)object;
				bean.updateEdgeAttributes(edge, (MPort) parentCell.getChildAt(0));
			}
 		}
 		return cell;
	}

	public void valueChanged(GraphSelectionEvent e) {		
		this.propertyPanel.removeAll();		
		if (e == null) {
			return;
		}
		
		final Object cell = e.getCell();
		final TreeSelectionModel selectionModel = this.tree.getSelectionModel();
		final GraphModel model = this.graph.getModel();
		
		if (model.isEdge(cell)) {
			if (e.isAddedCell()) {
				Edge edge = (Edge)cell;
				TreeNode sourceNode = (TreeNode) edge.getSource();
				TreeNode targetNode = (TreeNode) edge.getSource();
				if (sourceNode != null) {
					this.tree.scrollPathToVisible(new TreePath(this.treeModel.getPathToRoot(sourceNode)));
				}
				if (targetNode != null) {
					this.tree.scrollPathToVisible(new TreePath(this.treeModel.getPathToRoot(targetNode)));
				}
			} else {
				selectionModel.clearSelection();
			}
		} else {				
			if (e.isAddedCell()){
				if (!model.isPort(cell) && !model.isEdge(cell)) {
					TreeNode[] pathToRoot = this.treeModel.getPathToRoot((TreeNode) cell);
					if (pathToRoot != null) {
						TreePath path = new TreePath(pathToRoot);
						selectionModel.setSelectionPath(path);
					} else {
						selectionModel.clearSelection();
					}
					
				}
				MPort port = (model.isPort(cell)) ? (MPort)cell : (MPort)((DefaultGraphCell)cell).getChildAt(0);				
				Object userObject = port.getUserObject();
				
				if (userObject instanceof AbstractBean) {
					JPanel propertyPanel2 = ((AbstractBean)userObject).getPropertyPanel();
					if (propertyPanel2 != null) {
						this.propertyPanel.add(propertyPanel2, this.gbc2);
					}					
				}
				
			} else {
				selectionModel.clearSelection();
			}
		}

		this.propertyPanel.revalidate();
		this.propertyPanel.repaint();
		
		boolean enabled = !this.graph.isSelectionEmpty();
		this.remove.setEnabled(enabled);
	}

	
	private void createTreeModel() {		
		this.treeModel = new NonRootGraphTreeModel(this.graph.getModel());
		this.tree = new JTree(this.treeModel);
		
		this.tree.setRootVisible(false);
		
		this.graph.getSelectionModel().addGraphSelectionListener(this);
		
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

	private DefaultGraphCell createVertex(	final String name,
	                                      	final Object object,
	                                      	final double x,
											final double y,
											final double w,
											final double h,
											final Icon image) {

		// Create vertex with the given name
		final DefaultGraphCell cell = new DefaultGraphCell(name);

		// Set bounds
		GraphConstants.setBounds(cell.getAttributes(),
			new Rectangle2D.Double(x, y, w, h));

		GraphConstants.setAutoSize(cell.getAttributes(), true);

		
		AttributeMap attributes = cell.getAttributes();
		GraphConstants.setIcon(attributes, image);
		attributes.remove(GraphConstants.BORDER);
		attributes.remove(GraphConstants.BORDERCOLOR);

		// Add a Port
		MPort port = new MPort(object);
		cell.add(port);
		
		if (object instanceof Bean) {
			Bean bean = (Bean)object;
			bean.addPropertyChangeListener(new PropertyChangeListener() {

				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals(KEY_NAME)) {
						AttributeMap attributeMap = new AttributeMap();
						GraphConstants.setValue(attributeMap, evt.getNewValue());
						Map viewMap = new Hashtable();
						viewMap.put(cell, attributeMap);
						ManagerMainFrame.this.graph.getModel().edit(viewMap, null, null, null);
					}
					
				}
			});
		}
		
		return cell;
	}
	
	public final JGraph getGraph() {
		return this.graph;
	}

	public Perspective getPerspective() {
		return this.perspective;
	}
	
	public final void setPerspective(final Perspective perspective) {
		assert perspective != null;		
		this.perspective = perspective;
		try {
			this.arrangeLayoutItems();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public final Dispatcher getDispatcher() {
		return this.dispatcher;
	}
}
