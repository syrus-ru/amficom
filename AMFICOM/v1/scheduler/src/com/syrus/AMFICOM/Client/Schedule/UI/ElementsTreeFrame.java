
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.IconPopulatableItem;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.ItemTreeIconLabelCellRenderer;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.logic.ServiceItem;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.util.Log;

public class ElementsTreeFrame extends JInternalFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3761121639580186678L;

	SchedulerModel				schedulerModel;

	private Map					paramMap			= new HashMap();

	LogicalTreeUI				treePanel;

	private SelectionListener	selectionListener;

	ApplicationContext			aContext;

	private Item				rootItem			= new ServiceItem("/");

	private Dispatcher			dispatcher;

	// public static final String ACCESSPORT_NAME_REFLECTOMETER =
	// "MeasurementPortTypeReflectometry"; //$NON-NLS-1$

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
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
			return (KIS) StorableObjectPool.getStorableObject(this
					.getObject(ObjectEntities.KIS_ENTITY_CODE), true);
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public MeasurementType getMeasurementType() {
		try {
			Identifier measurementTypeId = this.getObject(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
			return measurementTypeId != null ? (MeasurementType) StorableObjectPool.getStorableObject(
				measurementTypeId, true) : null;
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public MonitoredElement getMonitoredElement() {
		try {
			Identifier meId = this.getObject(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE);

			MonitoredElement me = null;
			if (meId != null)
				me = (MonitoredElement) StorableObjectPool.getStorableObject(meId, true);
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
		this.treePanel.expandAll(true);
		this.selectItems();

	}

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		Log.debugMessage("ElementsTreeFrame.setMonitoredElement | monitoredElement " + (monitoredElement != null ? monitoredElement.getId() : null), Log.FINEST);
		this.paramMap.put(ObjectEntities.MONITORED_ELEMENT_ENTITY, monitoredElement);
		this.treePanel.expandAll(true);
		this.selectItems();

		
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
			if (item instanceof IconPopulatableItem) {
				IconPopulatableItem iconPopulatableItem = (IconPopulatableItem)item;
				iconPopulatableItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
			}
			this.rootItem.addChild(item);
			this.treePanel.addItem(item);
			this.treePanel.expandAll(item);
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		Object newValue = evt.getNewValue();
		if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE)) {
			this.setMeasurementType((MeasurementType) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPES)) {
			setElements((Collection) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MONITORED_ELEMENT)) {
			this.setMonitoredElement((MonitoredElement) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_MEASUREMENT_TYPE)) {
			MeasurementType measurementType = getMeasurementType();
			if (measurementType != null) {
				this.dispatcher
						.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE,
																	null, measurementType));
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_MONITORED_ELEMENT)) {
			MonitoredElement monitoredElement = getMonitoredElement();
			if (monitoredElement != null) {
				this.dispatcher
						.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_MONITORED_ELEMENT,
																	null, monitoredElement));
			}
		}

	}

	public void init() {
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
		if (this.treePanel == null) {
			final Dispatcher dispatcher = this.aContext.getDispatcher();
			this.treePanel = new LogicalTreeUI(this.rootItem, false);
			this.treePanel.setRenderer(IconPopulatableItem.class, new ItemTreeIconLabelCellRenderer());
			this.selectionListener = new SelectionListener() {

				public void selectedItems(Collection items) {
					for (Iterator it = items.iterator(); it.hasNext();) {
						Item item = (Item) it.next();
						Object object = item.getObject();
						if (object instanceof Identifier) {
							final Identifier identifier = (Identifier) object;
							short major = identifier.getMajor();
							switch (major) {
								case ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE: {
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
													.firePropertyChange(new PropertyChangeEvent(
																								ElementsTreeFrame.this,
																								SchedulerModel.COMMAND_CHANGE_ME_TYPE,
																								null, identifier));
											try {
												ElementsTreeFrame.this.schedulerModel.setSelectedMonitoredElement(
													(MonitoredElement) StorableObjectPool
															.getStorableObject(identifier, true), parent != null
															? (MeasurementType) StorableObjectPool
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
												.setSelectedMeasurementType((MeasurementType) StorableObjectPool
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

		// final Dispatcher dispatcher = this.aContext.getDispatcher();
		this.dispatcher = this.aContext.getDispatcher();

		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPES, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MEASUREMENT_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_MONITORED_ELEMENT, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_MONITORED_ELEMENT, this);

	}

}
