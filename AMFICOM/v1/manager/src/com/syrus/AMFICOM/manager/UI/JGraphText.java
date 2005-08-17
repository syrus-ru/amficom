package com.syrus.AMFICOM.manager.UI;

/*
 * $Id: JGraphText.java,v 1.21 2005/08/17 15:59:40 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

/**
 * @version $Revision: 1.21 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
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

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
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

public class JGraphText implements GraphSelectionListener {	
	
	JGraph						graph;

	JTree						tree;

	NonRootGraphTreeModel		treeModel;

	private JPanel				propertyPanel;

	// Undo Manager
	protected GraphUndoManager	undoManager;

	// Actions which Change State
	protected Action			undo, redo, remove, group, ungroup, tofront, toback,
			cut, copy, paste;

	private GridBagConstraints	gbc2;

	public JButton				userButton;

	public JButton				armButton;

	public JButton				rtuButton;

	public JButton				serverButton;

	public JButton				mcmButton;

	public JButton				netButton;

	public JButton				domainButton;

	public JButton				domainsButton;

	public JLabel				currentPerspectiveLabel;

	private boolean				direct				= false;

	public static Dispatcher	entityDispatcher	= new Dispatcher();

	private Perspective			perspective;
	private Identifier			xTypeId;
	private Identifier			yTypeId;
	private Identifier			nonStorableObjectNameTypeId;

	boolean						arranging;

	final ApplicationContext	context;
	
	public JGraphText(final ApplicationContext aContext) {
		this.context = aContext;
		// Construct Model and Graph
		GraphModel model = new ManagerGraphModel(this.direct);
		
		this.graph = new JGraph(model,
			new GraphLayoutCache(model,
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
	
	private void createModelListener() {
		this.graph.getModel().addGraphModelListener(
			new GraphModelListener() {
			public void graphChanged(GraphModelEvent e) {
				GraphModelChange change = e.getChange();
				
				GraphModel model = JGraphText.this.graph.getModel();
				
				Object[] inserted = change.getInserted();
				Object[] changed = change.getChanged();
				Object[] removed = change.getRemoved();
				
				if (inserted != null) {
					for(Object insertedObject : inserted) {
						if (model.isPort(insertedObject)) {
							TreeNode[] pathToRoot = JGraphText.this.treeModel.getPathToRoot((TreeNode) insertedObject);
							if (pathToRoot != null)
								JGraphText.this.tree.scrollPathToVisible(new TreePath(pathToRoot));
						}
					}
				}
				
				if (changed != null && removed == null) {
					for(Object changedObject : changed) {
						System.out.println(".graphChanged() | changedObject " + changedObject + '[' + changedObject.getClass().getName() + ']');
						if (model.isPort(changedObject)) {
							TreeNode[] pathToRoot = JGraphText.this.treeModel.getPathToRoot((TreeNode) changedObject);
							if (pathToRoot != null) {
								JGraphText.this.tree.scrollPathToVisible(new TreePath(pathToRoot));
							}
						} else  if (model.isEdge(changedObject)) {
							Edge edge = (Edge) changedObject;
							final MPort source = (MPort) edge.getSource();
							final MPort target = (MPort) edge.getTarget();
							System.out.println(".graphChanged() | " + source  +" -> " + target);						
							
							
							AbstractBean bean = (AbstractBean) source.getUserObject();
							
							
							ConnectionSet connectionSet = change.getConnectionSet();							
							MPort source2 = (MPort) connectionSet.getPort(edge, true);
							MPort target2 = (MPort) connectionSet.getPort(edge, false);

							System.out.println(".graphChanged() | ' " + source2  +" -> " + target2);
							
							if (target2 == null) {
								AbstractBean bean2 = (AbstractBean) source2.getUserObject();
								System.out
										.println("JGraphText.createModelListener() | " + bean2);
//								bean2.applyTargetPort(target2, null);
							}
							
							bean.applyTargetPort(target2, target);
							
							
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

							if (!arranging) {								
								String codeName = bean.getCodeName();
								try {
									CompoundCondition compoundCondition = 
										new CompoundCondition(new TypicalCondition(
											perspective.getPerspectiveName(), 
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
									
									compoundCondition.addCondition(new TypicalCondition(
										codeName, 
										OperationSort.OPERATION_EQUALS,
										ObjectEntities.LAYOUT_ITEM_CODE,
										StorableObjectWrapper.COLUMN_NAME));
									
									Set<LayoutItem> layoutItems = 
										StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
									
									if (layoutItems.isEmpty()) {
										LayoutItem item = LayoutItem.createInstance(LoginManager.getUserId(),
											Identifier.VOID_IDENTIFIER,
											perspective.getPerspectiveName(),
											codeName);
										
										Characteristic.createInstance(LoginManager.getUserId(),
											(CharacteristicType) StorableObjectPool.getStorableObject(xTypeId, true),
											"x",
											"x",
											Integer.toString((int) rectangle2D.getX()),
											item,
											true,
											true);
										
										Characteristic.createInstance(LoginManager.getUserId(),
											(CharacteristicType) StorableObjectPool.getStorableObject(yTypeId, true),
											"y",
											"y",
											Integer.toString((int) rectangle2D.getY()),
											item,
											true,
											true);
										
										if (bean.getId().isVoid()) {
											Characteristic.createInstance(LoginManager.getUserId(),
												(CharacteristicType) StorableObjectPool.getStorableObject(nonStorableObjectNameTypeId, true),
												"title",
												"title",
												title,
												item,
												true,
												true);
										}
										
									} else {
										LayoutItem item = layoutItems.iterator().next();
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
									JOptionPane.showMessageDialog(graph, 
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
							
							AbstractBean bean = (AbstractBean) source.getUserObject();
							
							try {
								CompoundCondition compoundCondition = 
									new CompoundCondition(new TypicalCondition(
										perspective.getPerspectiveName(), 
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
								
								compoundCondition.addCondition(new TypicalCondition(
									bean.getCodeName(), 
									OperationSort.OPERATION_EQUALS,
									ObjectEntities.LAYOUT_ITEM_CODE,
									StorableObjectWrapper.COLUMN_NAME));
								
								Set<LayoutItem> layoutItems = 
									StorableObjectPool.getStorableObjectsByCondition(compoundCondition, true);
								
								LayoutItem item = layoutItems.iterator().next();
								item.setParentId(Identifier.VOID_IDENTIFIER);
							} catch (ApplicationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							bean.applyTargetPort(target, null);
						 } 
					}
				}
			}	
		});
	}
	
	public void openFrames(final JDesktopPane desktopPane) {
		{
			// show tree frame
			JScrollPane pane = new JScrollPane(this.tree);
			JInternalFrame frame = new JInternalFrame(LangModelManager.getString("Label.ElementsTree"), true);
			desktopPane.add(frame);
			frame.getContentPane().add(pane);
			
			frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
			frame.setSize(200, 600);
			frame.setLocation(0, 0);
			
			frame.setVisible(true);
		}
		
		{
			
			// show graph frame
			
			JPanel panel = new JPanel(new GridBagLayout());
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			Box box = Box.createHorizontalBox();
			
			JToolBar perspectiveBox = this.createPerspecives();
			this.currentPerspectiveLabel = new JLabel();
			
			box.add(new JLabel(LangModelManager.getString("Label.Levels") + ':'));
			box.add(perspectiveBox);
			
			
			panel.add(box, gbc);
			
			gbc.weightx = 1.0;

			panel.add(this.createToolBar(), gbc);
			
			gbc.weightx = 0.0;
			gbc.weighty = 1.0;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			
			gbc.gridheight = GridBagConstraints.RELATIVE;
			
			panel.add(this.createEntityToolBar(), gbc);
			
			JScrollPane pane = new JScrollPane(this.graph);
			
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			
			panel.add(pane, gbc);
			
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 0.0;
			panel.add(this.currentPerspectiveLabel, gbc);
			
			JInternalFrame frame = new JInternalFrame(LangModelManager.getString("Label.Graph"), true);
			desktopPane.add(frame);
			frame.getContentPane().add(panel);

			frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
			frame.setSize(800, 800);
			frame.setLocation(200, 0);

			frame.setVisible(true);			
			
		}
		
		{
			
//			 show property frame
			JInternalFrame frame = new JInternalFrame(LangModelManager.getString("Label.Properties"), true);
			desktopPane.add(frame);
			this.propertyPanel = new JPanel(new GridBagLayout());
			this.gbc2 = new GridBagConstraints();
			this.gbc2.fill = GridBagConstraints.BOTH;
			this.gbc2.weightx = 1.0;
			this.gbc2.weighty = 1.0;
			this.gbc2.gridwidth = GridBagConstraints.REMAINDER;

			
			frame.getContentPane().add(this.propertyPanel);
			
			frame.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
			frame.setSize(200, 200);
			frame.setLocation(0, 600);
			
			frame.setVisible(true);
			

		}		
		
		this.domainsButton.doClick();
	}	
	
	private JToolBar createPerspecives() {		
		
		JToolBar perspectives = new JToolBar();
		
		perspectives.setFloatable(false);
		
		this.domainsButton = perspectives.add(new AbstractAction(LangModelManager.getString("Action.Domains")) {
			public void actionPerformed(ActionEvent e) {
					JGraphText.this.context.getApplicationModel().getCommand(ManagerModel.DOMAINS_COMMAND).execute();
			}
		});	

		perspectives.addSeparator();
		
		JButton button = perspectives.add(new AbstractAction("", new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/refresh.gif"))) {
			
			public void actionPerformed(ActionEvent e) {
				JGraphText.this.context.getApplicationModel().getCommand(ManagerModel.FLUSH_COMMAND).execute();
				}
		});
		
		button.setToolTipText(LangModelManager.getString("Action.Save"));
		
		return perspectives;
	}
	
	private void arrange() throws ApplicationException {
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
	
	public DefaultGraphCell arrangeCell(LayoutItem item) throws NumberFormatException, ApplicationException {
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
								Object target =  (this.direct ? edge.getTarget() : edge.getSource());
								Object source =  (this.direct ? edge.getSource() : edge.getTarget());
								
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
							this.createEdge(this.direct ? parentCell : cell, this.direct ?  cell : parentCell);
						}
					}
					
				}
			}
		}
		
		if (itemCell == null) {
			AbstractBeanFactory factory = null;
			if (name.startsWith(ARMBeanFactory.ARM_CODENAME)) {
				factory = ARMBeanFactory.getInstance();
			} else if (name.startsWith(NetBeanFactory.NET_CODENAME)) {
				factory = NetBeanFactory.getInstance();
			} else if (name.startsWith(ObjectEntities.DOMAIN)) {
				factory = DomainBeanFactory.getInstance();
			} else if (name.startsWith(ObjectEntities.MCM)) {
				factory = MCMBeanFactory.getInstance();
			} else if (name.startsWith(ObjectEntities.KIS)) {
				factory = RTUBeanFactory.getInstance();
			} else if (name.startsWith(ObjectEntities.SERVER)) {
				factory = ServerBeanFactory.getInstance();
			} else if (name.startsWith(ObjectEntities.SYSTEMUSER)) {
				factory = UserBeanFactory.getInstance();
			}			
			
			AbstractBean bean = factory.createBean(name);
			
			itemCell = this.createChild(
				parentCell, 
				title != null ? title : bean.getName(), 
				bean, 
				x, 
				y, 
				0, 
				0, 
				factory.getImage());
		}
		
		return itemCell;
	}
	
	public void showOnly(String[] names) {
		GraphLayoutCache graphLayoutCache = this.graph.getGraphLayoutCache();
		GraphModel model = this.graph.getModel();
		
		for(int i = 0; i<model.getRootCount(); i++) {
			Object rootAt = model.getRootAt(i);
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
		final List<Port> targets = (this.direct ? startPort.getTargets() : startPort.getSources());
		
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
				List<Port> sources = this.direct ? port.getSources() : port.getTargets();
				if (sources.isEmpty()) {
					System.out.println("JGraphText.showOnlyDescendants() | empty sources have " + rootAt + '[' + rootAt.getClass().getName() + ']');
					this.hideTillCell(port, selectedPort);
				}
			}
			
		}
		this.treeModel.setRoot(cell);
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
		// Remove
		URL removeUrl = getClass().getClassLoader().getResource(
				"com/syrus/AMFICOM/manager/resources/icons/delete.gif");
		ImageIcon removeIcon = new ImageIcon(removeUrl);
		this.remove = new AbstractAction("", removeIcon) {
			public void actionPerformed(ActionEvent e) {
				if (!graph.isSelectionEmpty()) {
					Object[] cells = graph.getSelectionCells();
					GraphModel model = graph.getModel();
					cells = graph.getDescendants(cells);
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
		this.userButton = this.createAction(toolBar, UserBeanFactory.getInstance());
		this.armButton = this.createAction(toolBar, ARMBeanFactory.getInstance());
		toolBar.addSeparator();
		this.rtuButton = this.createAction(toolBar, RTUBeanFactory.getInstance());
		this.serverButton = this.createAction(toolBar, ServerBeanFactory.getInstance());
		this.mcmButton = this.createAction(toolBar, MCMBeanFactory.getInstance());
		toolBar.addSeparator();
		this.netButton = this.createAction(toolBar, NetBeanFactory.getInstance());
		toolBar.addSeparator();
		this.domainButton = this.createAction(toolBar, DomainBeanFactory.getInstance());
		return toolBar;
	}	
	
	private JButton createAction(final JToolBar toolBar,
	                          final AbstractBeanFactory factory) {
		final String name = factory.getName();
		Icon icon = factory.getIcon();
		AbstractAction action = new AbstractAction(icon != null ? "" : name, icon) {
			private Map<String, Integer> entityIndices;
			
			public void actionPerformed(ActionEvent e) {
				try {
					AbstractBean bean = factory.createBean(perspective);
					
					if (this.entityIndices == null) {
						this.entityIndices = new HashMap<String, Integer>();
					}
					Integer i = this.entityIndices.get(name);
					int index = (i != null ? i : 0) + 1;
					this.entityIndices.put(name, index);
					JGraphText.this.createChild(null, factory.getShortName() + "-" + index, bean, 20, 20, 0, 0, factory.getImage());
				} catch (CreateObjectException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(graph, 
						e1.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				} catch (IllegalObjectEntityException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(graph, 
						e1.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);

				}
				
				
			}
		};		
	
		action.putValue(Action.SHORT_DESCRIPTION, name);
		return toolBar.add(action);
	}
	

	DefaultEdge createEdge(DefaultGraphCell source, DefaultGraphCell target) {
		return this.createEdge(source, target, true);
	}
	
	private DefaultEdge createEdge(DefaultGraphCell source, DefaultGraphCell target, boolean addToGraph) {
		
		MPort sourcePort = (MPort) source.getChildAt(0);
		MPort targetPort = (MPort) target.getChildAt(0);
		if (sourcePort != targetPort) {
			Object sourceObject = sourcePort.getUserObject();
			Object targetObject = targetPort.getUserObject();
			boolean canConnect = true;
			if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
				AbstractBean sourceBean = (AbstractBean) sourceObject;
				AbstractBean targetBean = (AbstractBean) targetObject;
				canConnect = sourceBean.isTargetValid(targetBean);
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
					JOptionPane.showMessageDialog(graph, 
						e.getMessage(), 
						LangModelManager.getString("Error"),
						JOptionPane.ERROR_MESSAGE);
				}
				
				return edge;
			}
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
 			DefaultEdge edge = this.createEdge(this.treeModel.isDirect() ? parentCell : cell, this.treeModel.isDirect() ?  cell : parentCell);
 			if (object instanceof AbstractBean) {
				AbstractBean bean = (AbstractBean)object;
				bean.updateEdgeAttributes(edge, (MPort) (this.treeModel.isDirect() ?  cell : parentCell).getChildAt(0));
			}
 		}
 		return cell;
	}

	public void valueChanged(GraphSelectionEvent e) {
		final Object cell = e.getCell();
		final TreeSelectionModel selectionModel = this.tree.getSelectionModel();
		final GraphModel model = this.graph.getModel();
		
		this.propertyPanel.removeAll();
		
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
		this.treeModel = new NonRootGraphTreeModel(this.graph.getModel(), this.direct);
		this.tree = new JTree(this.treeModel);
		
		this.tree.setRootVisible(false);
		
		this.graph.getSelectionModel().addGraphSelectionListener(this);
		
		this.tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				final Object lastPathComponent = e.getPath().getLastPathComponent();
				final GraphSelectionModel selectionModel = JGraphText.this.graph.getSelectionModel();
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
						graph.getModel().edit(viewMap, null, null, null);
					}
					
				}
			});
		}
		
		return cell;
	}
	
	public final JGraph getGraph() {
		return this.graph;
	}

	
	public final boolean isDirect() {
		return this.direct;
	}

	
	public final void setPerspective(final Perspective perspective) {
		assert perspective != null;		
		this.perspective = perspective;
		try {
			this.arrange();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
