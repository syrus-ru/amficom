
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.ElementsViewer;
import com.syrus.AMFICOM.Client.Schedule.KISEditor;
import com.syrus.AMFICOM.Client.Schedule.MeasurementTypeEditor;
import com.syrus.AMFICOM.Client.Schedule.MonitoredElementEditor;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Schedule.item.ElementItem;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.logic.LogicalTreeUI;
import com.syrus.AMFICOM.logic.SelectionListener;
import com.syrus.AMFICOM.measurement.MeasurementType;

public class ElementsTreeFrame extends JInternalFrame implements KISEditor, MonitoredElementEditor,
		MeasurementTypeEditor, ElementsViewer {

	private Command				command;

	private SchedulerModel		schedulerModel;

	private Map					paramMap	= new HashMap();

	private LogicalTreeUI		treePanel;

	private SelectionListener	selectionListener;

	ApplicationContext			aContext;

	public ElementsTreeFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		setTitle(LangModelSchedule.getString("Comonents_Tree")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
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

	private Object getObject(Class clazz) {
		JTree tree = this.treePanel.getTree();
		TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getPathComponent(i);
				Object nodeObject = node.getUserObject();
				if (nodeObject instanceof Item) {
					Item item = (Item) nodeObject;
					Object object = item.getObject();
					if (object.getClass().equals(clazz))
						return object;					
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
		return (KIS) this.getObject(KIS.class);
	}

	public MeasurementType getMeasurementType() {
		return (MeasurementType) this.getObject(MeasurementType.class);
	}

	public MonitoredElement getMonitoredElement() {
		return (MonitoredElement) this.getObject(MonitoredElement.class);
	}

	public void setKIS(KIS kis) {
		this.paramMap.put(ObjectEntities.KIS_ENTITY, kis);
		this.treePanel.expandAll(true);
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.paramMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY, measurementType);
		this.treePanel.expandAll(true);

	}

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		try {
			MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
				monitoredElement.getMeasurementPortId(), true);
			this.paramMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY, measurementPort);
			this.paramMap.put(ObjectEntities.ME_ENTITY, monitoredElement);
			this.treePanel.expandAll(true);

		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
	}

	public void setElements(Collection elements) {
		this.treePanel.addItems(elements);
		this.treePanel.getTree().revalidate();
		this.treePanel.getTree().repaint();

	}

	public void init() {
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
		if (this.treePanel == null) {
			final Dispatcher dispatcher = this.aContext.getDispatcher();
			this.treePanel = new LogicalTreeUI();
			this.selectionListener = new SelectionListener() {

				public void selectedItems(Collection items) {
					for (Iterator it = items.iterator(); it.hasNext();) {
						ElementItem item = (ElementItem) it.next();
						Object object = item.getObject();
						if (object instanceof MeasurementType) {
							MeasurementType measurementType = (MeasurementType) object;
							dispatcher.notify(new OperationEvent(measurementType.getId(), 0,
																	SchedulerModel.COMMAND_CHANGE_TEST_TYPE));
						} else if (object instanceof MeasurementPort) {
							MeasurementPort port = (MeasurementPort) object;
							dispatcher.notify(new OperationEvent(port, 0, SchedulerModel.COMMAND_CHANGE_PORT_TYPE));

						} else if (object instanceof MonitoredElement) {
							MonitoredElement me = (MonitoredElement) object;
							dispatcher.notify(new OperationEvent(me.getId(), 0, SchedulerModel.COMMAND_CHANGE_ME_TYPE));
						}
					}

				}
			};
			this.treePanel.addSelectionListener(this.selectionListener);
			setContentPane(this.treePanel.getPanel());

		}
		this.schedulerModel.setMeasurementTypeEditor(this);
		this.schedulerModel.setMonitoredElementEditor(this);
		this.schedulerModel.setKisEditor(this);
		this.schedulerModel.setElementsViewer(this);

	}

}