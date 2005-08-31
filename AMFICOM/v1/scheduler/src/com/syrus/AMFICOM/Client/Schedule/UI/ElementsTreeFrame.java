
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
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
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MonitoredElement;

public class ElementsTreeFrame extends JInternalFrame implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3761121639580186678L;

	SchedulerModel schedulerModel;

	private Map<String, StorableObject> paramMap = new HashMap<String, StorableObject>();

	LogicalTreeUI treePanel;

	private SelectionListener selectionListener;

	ApplicationContext aContext;

	private ServiceItem rootItem = new ServiceItem("/");

	private Dispatcher dispatcher;

	// public static final String ACCESSPORT_NAME_REFLECTOMETER =
	// "MeasurementPortTypeReflectometry"; //$NON-NLS-1$

	public ElementsTreeFrame(final ApplicationContext aContext) {
		this.aContext = aContext;
		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
	}

	private Identifier getObject(final short entityCode) {
		final JTree tree = this.treePanel.getTree();
		final TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				final Object nodeObject = treePath.getPathComponent(i);
				if (nodeObject instanceof Item) {
					final Item item = (Item) nodeObject;
					Object object = item.getObject();
					if (object instanceof Identifier) {
						final Identifier identifier = (Identifier) object;
						if (identifier != null && entityCode == identifier.getMajor()) {
							return identifier;
						}
					}
				}
			}
		}
		JOptionPane.showMessageDialog(this,
				LangModelSchedule.getString("Have not choosen Measurement element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.OK_OPTION);
		this.schedulerModel.setBreakData();
		return null;
	}
	
	public MeasurementType getMeasurementType() {
		final JTree tree = this.treePanel.getTree();
		final TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				final Object nodeObject = treePath.getPathComponent(i);
				if (nodeObject instanceof Item) {
					final Item item = (Item) nodeObject;
					Object object = item.getObject();
					if (object instanceof MeasurementType) {
						return (MeasurementType) object;
					}
				}
			}
		}
		JOptionPane.showMessageDialog(this,
				LangModelSchedule.getString("Have not choosen Measurement element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.OK_OPTION);
		this.schedulerModel.setBreakData();
		return MeasurementType.UNKNOWN;
	}

	public KIS getKIS() {
		try {
			return (KIS) StorableObjectPool.getStorableObject(this.getObject(ObjectEntities.KIS_CODE), true);
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public MonitoredElement getMonitoredElement() {
		try {
			final Identifier meId = this.getObject(ObjectEntities.MONITOREDELEMENT_CODE);

			MonitoredElement me = null;
			if (meId != null) {
				me = (MonitoredElement) StorableObjectPool.getStorableObject(meId, true);
			}
			return me;
		} catch (ApplicationException e) {
			//
		}
		return null;
	}

	public void setKIS(final KIS kis) {
		// this.paramMap.put(ObjectEntities.KIS, kis);
		this.treePanel.expandAll(true);
		this.selectItems();
	}

	public void setMeasurementType(final MeasurementType measurementType) {
		this.treePanel.expandAll(true);
		this.selectItems();
	}

	public void setMonitoredElement(final MonitoredElement monitoredElement) {
//		Log.debugMessage("ElementsTreeFrame.setMonitoredElement | monitoredElement " + (monitoredElement != null ? monitoredElement.getId() : null), Level.FINEST);
		this.paramMap.put(ObjectEntities.MONITOREDELEMENT, monitoredElement);
		this.treePanel.expandAll(true);
		this.selectItems();
	}

	private void selectItems() {
		final List<Item> list = new LinkedList<Item>();
		this.getSelectItem((Item) this.treePanel.getTreeModel().getRoot(), list);
		this.treePanel.selectedItems(list);
	}

	private void getSelectItem(final Item parent, final List<Item> list) {
		final Object object = parent.getObject();
		if (object instanceof Identifier) {
			final Identifier identifier = (Identifier) object;
			final StorableObject storableObject = this.paramMap.get(ObjectEntities.codeToString(identifier.getMajor()));
			if (storableObject != null && storableObject.getId().equals(identifier)) {
				list.add(parent);
			}
		}

		final List<Item> children = parent.getChildren();
		if (!children.isEmpty()) {
			for (final Item item : children) {
				this.getSelectItem(item, list);
			}
		}
	}

	public void setElements(final Collection<Item> elements) {
		final List<Item> childs = new ArrayList<Item>(this.rootItem.getChildren());
		for(final Item item: childs) {
			item.setParent(null);
		}
		for (final Item item : elements) {
			System.err.println("ElementsTreeFrame.setElements() | " + item);
			if (item instanceof IconPopulatableItem) {
				IconPopulatableItem iconPopulatableItem = (IconPopulatableItem)item;
				iconPopulatableItem.setIcon(UIManager.getIcon(ResourceKeys.ICON_MINI_FOLDER));
			}
			this.rootItem.addChild(item);
			this.treePanel.addItem(item);
			this.treePanel.expandAll(item);
		}
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName();
		final Object newValue = evt.getNewValue();
		if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE)) {
			this.setMeasurementType((MeasurementType) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MEASUREMENT_TYPES)) {
			this.setElements((Collection) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_SET_MONITORED_ELEMENT)) {
			this.setMonitoredElement((MonitoredElement) newValue);
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_MEASUREMENT_TYPE)) {
			final MeasurementType measurementType = getMeasurementType();
			if (measurementType != null) {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
						SchedulerModel.COMMAND_SET_MEASUREMENT_TYPE,
						null,
						measurementType));
			}
		} else if (propertyName.equals(SchedulerModel.COMMAND_GET_MONITORED_ELEMENT)) {
			final MonitoredElement monitoredElement = getMonitoredElement();
			if (monitoredElement != null) {
				this.dispatcher.firePropertyChange(new PropertyChangeEvent(this,
						SchedulerModel.COMMAND_SET_MONITORED_ELEMENT,
						null,
						monitoredElement));
			}
		}
	}

	public void init() {
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
		if (this.treePanel == null) {
			final Dispatcher dispatcher1 = this.aContext.getDispatcher();
			this.treePanel = new LogicalTreeUI(this.rootItem, false);
			this.treePanel.setRenderer(IconPopulatableItem.class, new ItemTreeIconLabelCellRenderer());
			this.selectionListener = new SelectionListener() {

				public void selectedItems(final Collection<Item> items) {
					for (final Item item : items) {
						final Object object = item.getObject();
						if (object instanceof Identifier) {
							final Identifier identifier = (Identifier) object;
							final short major = identifier.getMajor();
							switch (major) {
								case ObjectEntities.MONITOREDELEMENT_CODE: {
									Item parent1 = item;
									while (true) {
										if (!parent1.canHaveParent()) {
											break;
										}
										parent1 = parent1.getParent();
										if (parent1 == null) {
											break;
										}
										final Object object2 = parent1.getObject();
										if (object2 instanceof MeasurementType) {
											break;
										}
									}

									final Item parent = parent1;
									SwingUtilities.invokeLater(new Runnable() {

										public void run() {
											dispatcher1.firePropertyChange(new PropertyChangeEvent(ElementsTreeFrame.this,
													SchedulerModel.COMMAND_CHANGE_ME_TYPE,
													null,
													identifier));
											try {
												ElementsTreeFrame.this.schedulerModel.setSelectedMonitoredElement((MonitoredElement) StorableObjectPool.getStorableObject(identifier,
														true),
														parent != null ? (MeasurementType) parent.getObject() : null);
											} catch (ApplicationException e) {
												AbstractMainFrame.showErrorMessage(ElementsTreeFrame.this, e);
											}
										}
									});

								}
									break;								
							}
						} else if (object instanceof MeasurementType) {
							ElementsTreeFrame.this.schedulerModel.setSelectedMeasurementType((MeasurementType) object);
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
