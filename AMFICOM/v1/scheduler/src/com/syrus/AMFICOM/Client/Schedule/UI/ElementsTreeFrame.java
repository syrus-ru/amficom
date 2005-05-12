
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.logic.ServiceItem;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;

public class ElementsTreeFrame extends JInternalFrame implements Commandable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3761121639580186678L;

	private Command				command;

	SchedulerModel				schedulerModel;

	private Map					paramMap						= new HashMap();

	LogicalTreeUI				treePanel;

	private SelectionListener	selectionListener;

	ApplicationContext			aContext;

	private Item				rootItem						= new ServiceItem("/");

//	public static final String	ACCESSPORT_NAME_REFLECTOMETER	= "MeasurementPortTypeReflectometry";	//$NON-NLS-1$

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.command = new WindowCommand(this);
	}

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}

	private Identifier getObject(short entityCode) {
		JTree tree = this.treePanel.getTree();
		TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				Object nodeObject = treePath.getPathComponent(i);
				if (nodeObject instanceof Item) {
					Item item = (Item) nodeObject;
					Identifier identifier = (Identifier) item.getObject();
					if (identifier != null && entityCode == identifier.getMajor())
						return identifier;
				}
			}
		}
		JOptionPane.showMessageDialog(this,
			LangModelSchedule.getString("Have not choosen Measurement element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.OK_OPTION);
		this.schedulerModel.setBreakData();
		return null;
	}

	public KIS getKIS() {
		try {
			return (KIS) ConfigurationStorableObjectPool.getStorableObject(this
					.getObject(ObjectEntities.KIS_ENTITY_CODE), true);
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public MeasurementType getMeasurementType() {
		try {
			return (MeasurementType) MeasurementStorableObjectPool.getStorableObject(this
					.getObject(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public MonitoredElement getMonitoredElement() {
		try {
			Identifier meId = this.getObject(ObjectEntities.ME_ENTITY_CODE);

			MonitoredElement me = null;
			if (meId != null)
				me = (MonitoredElement) ConfigurationStorableObjectPool.getStorableObject(meId, true);
			return me;
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public void setKIS(KIS kis) {
		// this.paramMap.put(ObjectEntities.KIS_ENTITY, kis);
		this.treePanel.expandAll(true);
		this.selectItems();
	}

	public void setMeasurementType(MeasurementType measurementType) {
		// this.paramMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY,
		// measurementType);
		this.treePanel.expandAll(true);
		this.selectItems();

	}

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		// try {
		// MeasurementPort measurementPort = (MeasurementPort)
		// ConfigurationStorableObjectPool.getStorableObject(
		// monitoredElement.getMeasurementPortId(), true);
		// this.paramMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY,
		// measurementPort);
		this.paramMap.put(ObjectEntities.ME_ENTITY, monitoredElement);
		this.treePanel.expandAll(true);
		this.selectItems();

		// } catch (ApplicationException e) {
		// SchedulerModel.showErrorMessage(this, e);
		// }
	}

	private void selectItems() {
		List list = new LinkedList();
		this.getSelectItem((Item) this.treePanel.getTreeModel().getRoot(), list);
		this.treePanel.selectedItems(list);
	}

	private void getSelectItem(	Item parent,
								List list) {
		Object object = parent.getObject();
		if (object instanceof Identifier) {
			Identifier identifier = (Identifier) object;
			StorableObject storableObject = (StorableObject) this.paramMap.get(ObjectEntities.codeToString(identifier
					.getMajor()));
			if (storableObject != null && storableObject.getId().equals(identifier)) {
				list.add(parent);
			}
		}

		List children = parent.getChildren();
		if (!children.isEmpty()) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				Item item = (Item) it.next();
				this.getSelectItem(item, list);
			}
		}
	}

	public void setElements(Collection elements) {
		for (Iterator it = elements.iterator(); it.hasNext();) {
			Item item = (Item) it.next();
			this.rootItem.addChild(item);
			this.treePanel.addItem(item);
			this.treePanel.expandAll(item);
		}
	}

	public void init() {
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
		if (this.treePanel == null) {
			final Dispatcher dispatcher = this.aContext.getDispatcher();
			this.treePanel = new LogicalTreeUI(this.rootItem, false);
			this.selectionListener = new SelectionListener() {

				public void selectedItems(Collection items) {
					for (Iterator it = items.iterator(); it.hasNext();) {
						Item item = (Item) it.next();
						Object object = item.getObject();
						if (object instanceof Identifier) {
							final Identifier identifier = (Identifier) object;
							short major = identifier.getMajor();
							switch (major) {
								case ObjectEntities.ME_ENTITY_CODE: {
									Item parent1 = item;
									while (true) {
										if (!parent1.canHaveParent())
											break;
										parent1 = parent1.getParent();
										if (parent1 == null)
											break;
										Object object2 = parent1.getObject();
										if (object2 instanceof Identifier) {
											Identifier identifier2 = (Identifier) object2;
											if (identifier2.getMajor() == ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE)
												break;
										}
									}

									final Item parent = parent1;
									SwingUtilities.invokeLater(new Runnable() {

										public void run() {
											dispatcher
													.notify(new OperationEvent(identifier, 0,
																				SchedulerModel.COMMAND_CHANGE_ME_TYPE));
											try {
												ElementsTreeFrame.this.schedulerModel.setSelectedMonitoredElement(
													(MonitoredElement) ConfigurationStorableObjectPool
															.getStorableObject(identifier, true), parent != null
															? (MeasurementType) MeasurementStorableObjectPool
																	.getStorableObject((Identifier) parent.getObject(),
																		true) : null);
											} catch (ApplicationException e) {
												SchedulerModel.showErrorMessage(ElementsTreeFrame.this, e);
											}
										}
									});

								}
									break;
								case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE: {
									try {
										ElementsTreeFrame.this.schedulerModel
												.setSelectedMeasurementType((MeasurementType) MeasurementStorableObjectPool
														.getStorableObject(identifier, true));
									} catch (ApplicationException e) {
										SchedulerModel.showErrorMessage(ElementsTreeFrame.this, e);
									}
								}
									break;
							}
						}
					}

				}
			};
			this.treePanel.addSelectionListener(this.selectionListener);
			setContentPane(this.treePanel.getTree());

		}

		final Dispatcher dispatcher = this.aContext.getDispatcher();
		
		OperationListener listener = new OperationListener() {
			private boolean skip = false;
			
			public void operationPerformed(OperationEvent e) {
				String actionCommand = e.getActionCommand();
				Object obj = e.getSource();
				if (actionCommand.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE)) {
					if (!this.skip) {
					setMeasurementType((MeasurementType) obj);
					}
				} else if (actionCommand.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPES)) {
					setElements((Collection) obj);
				} else if (actionCommand.equals(SchedulerModel.COMMAND_SET_MONITORED_ELEMENT)) {
					if (!this.skip) {
					setMonitoredElement((MonitoredElement) obj);
					}
				} else if (actionCommand.equals(SchedulerModel.COMMAND_GET_MEASUREMENT_TYPE)) {
					MeasurementType measurementType = getMeasurementType();
					if (measurementType != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(measurementType, 0, SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE));
						this.skip = false;
					}					
				}   else if (actionCommand.equals(SchedulerModel.COMMAND_GET_MONITORED_ELEMENT)) {
					MonitoredElement monitoredElement = getMonitoredElement();
					if (monitoredElement != null) {
						this.skip = true;
						dispatcher.notify(new OperationEvent(monitoredElement, 0, SchedulerModel.COMMAND_SET_MONITORED_ELEMENT));
						this.skip = false;
					}					
				}  
			}
		};
		
		dispatcher.register(listener, SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE);
		dispatcher.register(listener, SchedulerModel.COMMAND_SET_MEASUREMENT_TYPES);
		dispatcher.register(listener, SchedulerModel.COMMAND_SET_MONITORED_ELEMENT);
		dispatcher.register(listener, SchedulerModel.COMMAND_GET_MEASUREMENT_TYPE);
		dispatcher.register(listener, SchedulerModel.COMMAND_GET_MONITORED_ELEMENT);

	}

}
