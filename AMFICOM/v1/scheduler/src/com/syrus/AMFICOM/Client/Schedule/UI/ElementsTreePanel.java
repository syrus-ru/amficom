
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TreeDataSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.Client.General.UI.UniTreePanel;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.KISEditor;
import com.syrus.AMFICOM.Client.Schedule.MeasurementTypeEditor;
import com.syrus.AMFICOM.Client.Schedule.MonitoredElementEditor;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;

public class ElementsTreePanel extends JPanel implements OperationListener, KISEditor, MonitoredElementEditor,
		MeasurementTypeEditor {

	private class TestsTreeModel extends ObjectResourceTreeModel {

		private TreeModel				model;

		private ObjectResourceTreeNode	root;

		public TestsTreeModel(ApplicationContext aContext) throws ApplicationException {
			this.root = new ObjectResourceTreeNode(ROOT_NODE_NAME, LangModelSchedule.getString("TestType"), true, //$NON-NLS-1$ //$NON-NLS-2$
													(ImageIcon) UIStorage.FOLDER_ICON);

			for (Iterator measurementTypeIt = ElementsTreePanel.this.measurementTypes.iterator(); measurementTypeIt.hasNext();) {
				MeasurementType measurementType = (MeasurementType) measurementTypeIt.next();

				ObjectResourceTreeNode testTypeNode = new ObjectResourceTreeNode(measurementType, measurementType
						.getDescription(), true, (ImageIcon) UIStorage.FOLDER_ICON);
				this.root.add(testTypeNode);
				if (ElementsTreePanel.this.kiss != null) {
					List measurementPortTypeIds = new LinkedList();
					for (Iterator kisTypeIt = ElementsTreePanel.this.kiss.iterator(); kisTypeIt.hasNext();) {
						KIS kis = (KIS) kisTypeIt.next();
						for (Iterator it = kis.getMeasurementPortIds().iterator(); it.hasNext();) {
							MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool
									.getStorableObject((Identifier) it.next(), true);

							MeasurementPortType measurementPortType = (MeasurementPortType) measurementPort.getType();
							measurementPortTypeIds.add(measurementPortType.getId());
						}
					}

					LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																					measurementPortTypeIds,
																					ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					// linkedIdsCondition.setDomainId(sessionInterface.getDomainIdentifier());

					Collection measurementTypesFormeasurementPortType = MeasurementStorableObjectPool
							.getStorableObjectsByCondition(linkedIdsCondition, true);
					for (Iterator kisTypeIt = ElementsTreePanel.this.kiss.iterator(); kisTypeIt.hasNext();) {
						KIS kis = (KIS) kisTypeIt.next();
						for (Iterator it = kis.getMeasurementPortIds().iterator(); it.hasNext();) {
							MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool
									.getStorableObject((Identifier) it.next(), true);
							for (Iterator mtIter = measurementTypesFormeasurementPortType.iterator(); mtIter.hasNext();) {
								MeasurementType mt = (MeasurementType) mtIter.next();
								if (mt.getId().equals(measurementType.getId())) {
									ObjectResourceTreeNode kisNode = new ObjectResourceTreeNode(
																								kis,
																								kis.getName(),
																								true,
																								(ImageIcon) UIStorage.TESTING_ICON,
																								false);
									testTypeNode.add(kisNode);

									ObjectResourceTreeNode accessPortNode = new ObjectResourceTreeNode(
																										measurementPort,
																										measurementPort
																												.getName(),
																										true,
																										(ImageIcon) UIStorage.PORT_ICON,
																										false);
									kisNode.add(accessPortNode);
									for (Iterator meIt = ElementsTreePanel.this.monitoredElements.iterator(); meIt.hasNext();) {
										MonitoredElement me = (MonitoredElement) meIt.next();
										/**
										 * FIXME load somewhere else
										 */
										// ElementsTreePanel.this.surveyDsi.getTestSetupByME(me.getId());
										if (me.getMeasurementPortId().equals(measurementPort.getId())) {
											// meList.add(me);
											ObjectResourceTreeNode meNode = new ObjectResourceTreeNode(
																										me,
																										me.getName(),
																										true,
																										(ImageIcon) UIStorage.PATHMODE_ICON,
																										true);
											accessPortNode.add(meNode);
										}
									}
								}
							}
							break;
						}
					}
				}
			}
			this.model = new DefaultTreeModel(this.root);

		}

		public java.util.List getChildNodes(ObjectResourceTreeNode node) {

			// Object obj = node.getObject();
			int count = node.getChildCount();
			// ObjectResourceTreeNode parent =
			// (ObjectResourceTreeNode)
			// node.getParent();
			// System.out.println("getChildNodes\t" +
			// obj.getClass().getName()
			// + "\tnode.getChildCount():" + count);

			java.util.List list = new LinkedList();
			for (int i = 0; i < count; i++)
				list.add(this.model.getChild(node, i));
			return list;
		}

		public Class getNodeChildClass(ObjectResourceTreeNode node) {
			Object obj = node.getObject();
			Class clazz = null;
			if (obj instanceof String) {
				if (node.getObject().equals(ROOT_NODE_NAME))
					clazz = MeasurementType.class; //$NON-NLS-1$
			} else if (obj instanceof MeasurementType) {
				// //System.out.println("testType:" +
				// ((TestType) obj).name);
				MeasurementType measurementType = (MeasurementType) obj;
				// //System.out.println("testType.id:"+testType.id);
				ElementsTreePanel.this.skipTestUpdate = true;
				ElementsTreePanel.this.dispatcher.notify(new OperationEvent(measurementType.getId(), 0,
																			SchedulerModel.COMMAND_CHANGE_TEST_TYPE));
				ElementsTreePanel.this.skipTestUpdate = false;
				clazz = KIS.class;
			} else if (obj instanceof KIS) {
				// KIS kis = (KIS) obj;
				// System.out.println("KIS:" + kis.type_id);
				// skipTestUpdate = true;
				// dispatcher.notify(new
				// OperationEvent(kis.getId(), 0,
				// TestParametersPanel.COMMAND_CHANGE_KIS));
				// // Vector ports = kis.access_ports;
				// // for (Enumeration e = ports.elements();
				// e.hasMoreElements();)
				// // {
				// // AccessPort port = (AccessPort)
				// e.nextElement();
				// // dispatcher.notify(new
				// OperationEvent(port.type_id, 0,
				// //
				// TestParametersPanel.COMMAND_CHANGE_PORT_TYPE));
				// // }
				// skipTestUpdate = false;
				clazz = MeasurementPort.class;
			} else if (obj instanceof MeasurementPort) {
				// System.out.println("AccessPort:" +
				// ((AccessPort)
				// obj).type_id);
				ElementsTreePanel.this.skipTestUpdate = true;
				MeasurementPort port = (MeasurementPort) obj;
				clazz = MonitoredElement.class;
				ElementsTreePanel.this.dispatcher.notify(new OperationEvent(port, 0,
																			SchedulerModel.COMMAND_CHANGE_PORT_TYPE));

				java.util.List list = this.getChildNodes(node);
				for (Iterator it = list.iterator(); it.hasNext();) {
					ObjectResourceTreeNode n = (ObjectResourceTreeNode) it.next();
					Object o = n.getObject();
					MonitoredElement me = (MonitoredElement) o;
					ElementsTreePanel.this.dispatcher.notify(new OperationEvent(me.getId(), 0,
																				SchedulerModel.COMMAND_CHANGE_ME_TYPE));

				}
				ElementsTreePanel.this.skipTestUpdate = false;
			} else if (obj instanceof MonitoredElement) {
				MonitoredElement me = (MonitoredElement) obj;
				clazz = me.getClass();
				ElementsTreePanel.this.skipTestUpdate = true;
				ElementsTreePanel.this.dispatcher.notify(new OperationEvent(me.getId(), 0,
																			SchedulerModel.COMMAND_CHANGE_ME_TYPE));
				ElementsTreePanel.this.skipTestUpdate = false;

			}

			return clazz;
		}

		public ImageIcon getNodeIcon(ObjectResourceTreeNode node) {
			if (node != null)
				return null;
			return null;
		}

		public Color getNodeTextColor(ObjectResourceTreeNode node) {
			return null;
		}

		public ObjectResourceTreeNode getRoot() {
			return this.root;
		}

		public void nodeAfterSelected(ObjectResourceTreeNode node) {
			// nothing to do
		}

		public void nodeBeforeExpanded(ObjectResourceTreeNode node) {
			// nothing to do
		}

	}

	public static final String	ACCESSPORT_NAME_REFLECTOMETER	= "MeasurementPortTypeReflectometry";	//$NON-NLS-1$

	private static final String	ROOT_NODE_NAME					= "root";

	boolean						skipTestUpdate					= false;

	private ApplicationContext	aContext;
	private SchedulerModel		schedulerModel;

	private JButton				delMapGroupButton;
	Dispatcher					dispatcher;
	private JButton				loadButton;	
	private Map					paramMap						= new HashMap();	
	private JScrollPane			scrollPane						= new JScrollPane();
	private UniTreePanel		utp;
	
	Collection monitoredElements;
	Collection measurementTypes;
	Collection kiss;

	public ElementsTreePanel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.schedulerModel = (SchedulerModel) this.aContext.getApplicationModel();
		this.schedulerModel.setMeasurementTypeEditor(this);
		this.schedulerModel.setMonitoredElementEditor(this);
		this.schedulerModel.setKisEditor(this);

		setLayout(new BorderLayout());

		// Toolbar
		this.loadButton = new JButton();
		this.loadButton.setIcon(UIStorage.OPEN_FILE_ICON);
		this.loadButton.setToolTipText(LangModelSchedule.getString("Open")); //$NON-NLS-1$
		this.loadButton.setMargin(UIStorage.INSET_NULL);
		this.loadButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.loadButton.setFocusPainted(false);
		this.loadButton.setEnabled(false);
		this.loadButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				/**
				 * @todo do loading tree
				 */
				// loadButton_actionPerformed();
			}
		});

		this.delMapGroupButton = new JButton();
		this.delMapGroupButton.setMargin(UIStorage.INSET_NULL);
		this.delMapGroupButton.setIcon(UIStorage.DELETE_ICON);
		this.delMapGroupButton.setToolTipText(LangModelSchedule.getString("Delete")); //$NON-NLS-1$
		this.delMapGroupButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.delMapGroupButton.setFocusPainted(false);
		this.delMapGroupButton.setEnabled(false);
		this.delMapGroupButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				/**
				 * @todo do delete tree
				 */
				// delMapGroupButton_actionPerformed();
			}
		});

		{
			Dimension d = new Dimension(24, 24);
			UIStorage.setRigidSize(this.delMapGroupButton, d);
			UIStorage.setRigidSize(this.loadButton, d);
		}

		Box toolBar = new Box(BoxLayout.X_AXIS);
		toolBar.add(this.loadButton);
		toolBar.add(this.delMapGroupButton);
		toolBar.add(Box.createHorizontalGlue());
		add(toolBar, BorderLayout.NORTH);

		// TREE
		this.utp = new UniTreePanel(this.dispatcher, aContext, ((SchedulerModel) this.aContext.getApplicationModel())
				.getTreeModel());
		this.utp.getTree().setRootVisible(true);
		this.utp.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollPane.getViewport().add(this.utp);
		add(this.scrollPane, BorderLayout.CENTER);

		initModule(this.dispatcher);
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
		this.expandAll(true);
	}

	public void setKISs(Collection kiss) {
		this.kiss = kiss;
		this.regenerateTree();		
	}
	
	public void setMeasurementType(MeasurementType measurementType) {
		this.paramMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY, measurementType);
		this.expandAll(true);

	}
	
	public void setMeasurementTypes(Collection measurementTypes) {
		this.measurementTypes = measurementTypes;
		this.regenerateTree();			
	}
	

	public void setMonitoredElement(MonitoredElement monitoredElement) {
		try {
			MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(
				monitoredElement.getMeasurementPortId(), true);
			this.paramMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY, measurementPort);
			this.paramMap.put(ObjectEntities.ME_ENTITY, monitoredElement);
			this.expandAll(true);

		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
	}
	

	public void setMonitoredElementEditors(Collection monitoredElements) {
		this.monitoredElements = monitoredElements;
		this.regenerateTree();	
		
	}

	private Object getObject(Class clazz) {
		JTree tree = getTree();
		TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				ObjectResourceTreeNode node = (ObjectResourceTreeNode) treePath.getPathComponent(i);
				Object obj = node.getObject();
				if (obj.getClass().equals(clazz))
					return obj;
			}
		}
		JOptionPane.showMessageDialog(this,
			LangModelSchedule.getString("Have not choosen Measurement element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
			JOptionPane.OK_OPTION);
		this.schedulerModel.setBreakData();
		return null;
	}

	public JTree getTree() {
		return this.utp.getTree();
	}

	public void operationPerformed(OperationEvent oe) {
		String commandName = oe.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent) oe;
			// selectedObject = dse.selectedObject;
			Class selectedClass = dse.getDataClass();
			// DataSet set = dse.getDataSet();
			java.util.List col = dse.getList();

			if (selectedClass.equals(MonitoredElement.class)) {
				this.loadButton.setEnabled(true);
				// (MonitoredElement)
				// col.get(dse.getSelectedObject());
				{
					// Object obj = dse.getSelectedObject();
					// if ((col.contains(obj)) && (obj instanceof
					// MonitoredElement)) {
					// MonitoredElement me = (MonitoredElement) obj;
					// this.dispatcher
					// .notify(new OperationEvent(me.getId(), 0,
					// SchedulerModel.COMMAND_CHANGE_ME_TYPE));
					// }

				}
			} else
				this.loadButton.setEnabled(false);

		} 
//		else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
//			try {
//				this.paramMap.clear();
//				ObjectResourceTreeModel model = new TestsTreeModel(this.aContext);
//				((SchedulerModel) this.aContext.getApplicationModel()).setTreeModel(model);
//				this.utp.setModel(model);
//				this.utp.revalidate();
//			} catch (ApplicationException ae) {
//				SchedulerModel.showErrorMessage(this, ae);
//			}
//		}
	}
	
	private void regenerateTree() {
		if (this.kiss == null || this.measurementTypes == null || this.monitoredElements == null)
			return;
		try {
			this.paramMap.clear();
			ObjectResourceTreeModel model = new TestsTreeModel(this.aContext);
			((SchedulerModel) this.aContext.getApplicationModel()).setTreeModel(model);
			this.utp.setModel(model);
			this.utp.revalidate();
			this.kiss = null;
			this.measurementTypes = null;
			this.monitoredElements = null;
		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	private void expandAll(boolean expand) {
		// TreeNode root = (TreeNode) tree.getModel().getRoot();
		ObjectResourceTreeNode root = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel().getRoot();
		// Traverse tree from root
		expandAll(root, new TreePath(root), expand);
	}

	private void expandAll(	ObjectResourceTreeNode node,
							TreePath parent,
							boolean expand) {
		java.util.List list = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel().getChildNodes(node);
		for (Iterator it = list.iterator(); it.hasNext();) {
			ObjectResourceTreeNode n = (ObjectResourceTreeNode) it.next();
			Object obj = n.getObject();
			TreePath path = parent.pathByAddingChild(n);
			// System.out.println("obj:" + obj.getClass().getName()
			// + "\t" +
			// obj);
			boolean found = false;
			if (obj instanceof MeasurementType) {
				MeasurementType measurementType = (MeasurementType) obj;
				MeasurementType paramMeasurementType = (MeasurementType) this.paramMap
						.get(ObjectEntities.MEASUREMENTTYPE_ENTITY);
				if (paramMeasurementType != null && measurementType.getId().equals(paramMeasurementType.getId())) {
					// System.out.println("+testType:" +
					// testType.id);
					found = true;
				}
			} else if (obj instanceof KIS) {
				KIS kis = (KIS) obj;
				KIS paramKis = (KIS) this.paramMap.get(ObjectEntities.KIS_ENTITY);				
				if (paramKis != null && kis.getId().equals(paramKis.getId())) {
					// System.out.println("+kis:" + kis.id);
					found = true;
				}
			} else if (obj instanceof MeasurementPort) {
				MeasurementPort port = (MeasurementPort) obj;
				MeasurementPort paramPort = (MeasurementPort) this.paramMap.get(ObjectEntities.MEASUREMENTPORT_ENTITY);
				if (paramPort != null && port.getId().equals(paramPort.getId())) {
					// System.out.println("+port:" +
					// port.id);
					found = true;
				}
			} else if (obj instanceof MonitoredElement) {
				MonitoredElement me = (MonitoredElement) obj;
				MonitoredElement paramMe = (MonitoredElement) this.paramMap.get(ObjectEntities.ME_ENTITY);
				if (paramMe != null && me.getId().equals(paramMe.getId())) {
					// System.out.println("+me:" + me.id);
					found = true;
				}
			}
			if (found) {
				JTree tree = this.getTree();
				// Expansion or collapse must be done bottom-up
				if (expand) {
					tree.expandPath(parent);
				} else {
					tree.collapsePath(parent);
				}
				// System.out.println("path:" +
				// path.toString());
				tree.setSelectionPath(path);
				expandAll(n, path, expand);
			}
		}

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
//		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TreeDataSelectionEvent.type);
//		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
	}
}