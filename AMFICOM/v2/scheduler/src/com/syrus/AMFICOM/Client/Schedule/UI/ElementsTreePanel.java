
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.MeasurementSetupCondition;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Test;

public class ElementsTreePanel extends JPanel implements OperationListener {

	private class TestsTreeModel extends ObjectResourceTreeModel {

		//private DataSourceInterface dsi;

		//private ApplicationContext aContext;

		//private Dispatcher dispatcher;

		private TreeModel		model;

		private ObjectResourceTreeNode	root;

		//	private Hashtable testTypeTable;
		//
		//	private Hashtable kisTable;
		//
		//	private Hashtable meTable;

		public TestsTreeModel(ApplicationContext aContext) {
			//this.aContext = aContext;
			//this.dsi = aContext.getDataSourceInterface();
			final Dispatcher dispatcher = aContext.getDispatcher();

			this.root = new ObjectResourceTreeNode(ROOT_NODE_NAME,
								LangModelSchedule.getString("TestType"), true, //$NON-NLS-1$ //$NON-NLS-2$
								(ImageIcon) UIStorage.FOLDER_ICON);

			DomainCondition domainCondition = ((SchedulerModel) aContext.getApplicationModel())
					.getDomainCondition(ObjectEntities.KIS_ENTITY_CODE);
			List kisList = ConfigurationStorableObjectPool.getStorableObjectsByCondition(domainCondition,
													true);
			domainCondition = ((SchedulerModel) aContext.getApplicationModel())
					.getDomainCondition(ObjectEntities.ME_ENTITY_CODE);
			List meList = ConfigurationStorableObjectPool.getStorableObjectsByCondition(domainCondition,
													true);
			Map aptMap = Pool.getMap(AccessPortType.typ);

			List measurementTypeList = MeasurementStorableObjectPool
					.getStorableObjectsByCondition(
									((SchedulerModel) aContext
											.getApplicationModel())
											.getDomainCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE),
									true);

			for (Iterator measurementTypeIt = measurementTypeList.iterator(); measurementTypeIt.hasNext();) {
				MeasurementType measurementType = (MeasurementType) measurementTypeIt.next();

				//ElementsTreePanel.this.surveyDsi.getTestSetupByTestType(testType.getId());

				MeasurementStorableObjectPool
						.getStorableObjectsByCondition(
										new MeasurementSetupCondition(
														measurementType),
										true);

				ObjectResourceTreeNode testTypeNode = new ObjectResourceTreeNode(
													measurementType,
													measurementType
															.getDescription(),
													true,
													(ImageIcon) UIStorage.FOLDER_ICON);
				this.root.add(testTypeNode);
				if (kisList != null) {
					List measurementPortTypeIds = new LinkedList();
					for (Iterator kisTypeIt = kisList.iterator(); kisTypeIt.hasNext();) {
						KIS kis = (KIS) kisTypeIt.next();
						for (Iterator it = kis.getMeasurementPortIds().iterator(); it.hasNext();) {
							MeasurementPort acessPort = (MeasurementPort) ConfigurationStorableObjectPool
									.getStorableObject((Identifier) it.next(), true);
							MeasurementPortType measurementPortType = (MeasurementPortType) acessPort
									.getType();
							measurementPortTypeIds.add(measurementPortType.getId());
						}
					}

					LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
													measurementPortTypeIds,
													ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					List measurementTypesFormeasurementPortType = MeasurementStorableObjectPool
							.getStorableObjectsByCondition(linkedIdsCondition, true);

					for (Iterator kisTypeIt = kisList.iterator(); kisTypeIt.hasNext();) {
						KIS kis = (KIS) kisTypeIt.next();
						for (Iterator it = kis.getMeasurementPortIds().iterator(); it.hasNext();) {
							MeasurementPort acessPort = (MeasurementPort) ConfigurationStorableObjectPool
									.getStorableObject((Identifier) it.next(), true);
							MeasurementPortType measurementPortType = (MeasurementPortType) acessPort
									.getType();

							if (measurementTypesFormeasurementPortType
									.contains(measurementType)) {
								ObjectResourceTreeNode kisNode = new ObjectResourceTreeNode(
																kis,
																kis
																		.getName(),
																true,
																(ImageIcon) UIStorage.TESTING_ICON,
																false);
								testTypeNode.add(kisNode);

								ObjectResourceTreeNode accessPortNode = new ObjectResourceTreeNode(
																	acessPort,
																	acessPort
																			.getName(),
																	true,
																	(ImageIcon) UIStorage.PORT_ICON,
																	false);
								kisNode.add(accessPortNode);
								for (Iterator meIt = meList.iterator(); meIt.hasNext();) {
									MonitoredElement me = (MonitoredElement) meIt
											.next();
									ElementsTreePanel.this.surveyDsi
											.getTestSetupByME(me.getId());
									if (me.getMeasurementPortId()
											.equals(acessPort.getId())) {
										meList.add(me);
										ObjectResourceTreeNode meNode = new ObjectResourceTreeNode(
																		me,
																		me
																				.getName(),
																		true,
																		(ImageIcon) UIStorage.PATHMODE_ICON,
																		true);
										accessPortNode.add(meNode);
									}
								}
							}
							break;
						}
					}
				}
			}

			dispatcher.notify(new OperationEvent(meList, 0, SchedulerModel.COMMAND_AVAILABLE_ME));
			this.model = new DefaultTreeModel(this.root);

		}

		public java.util.List getChildNodes(ObjectResourceTreeNode node) {

			//		Object obj = node.getObject();
			int count = node.getChildCount();
			//		ObjectResourceTreeNode parent =
			// (ObjectResourceTreeNode)
			// node.getParent();
			//		System.out.println("getChildNodes\t" +
			// obj.getClass().getName()
			//				+ "\tnode.getChildCount():" + count);

			java.util.List list = new LinkedList();
			for (int i = 0; i < count; i++)
				list.add(this.model.getChild(node, i));
			return list;
		}

		public Class getNodeChildClass(ObjectResourceTreeNode node) {
			Object obj = node.getObject();
			Class ret = null;
			if (obj instanceof String) {
				if (node.getObject().equals(ROOT_NODE_NAME))
					ret = MeasurementType.class; //$NON-NLS-1$
			} else if (obj instanceof MeasurementType) {
				//				//System.out.println("testType:" +
				// ((TestType) obj).name);
				MeasurementType measurementType = (MeasurementType) obj;
				//				//System.out.println("testType.id:"+testType.id);
				ElementsTreePanel.this.skipTestUpdate = true;
				ElementsTreePanel.this.dispatcher
						.notify(new OperationEvent(measurementType.getId(), 0,
										SchedulerModel.COMMAND_CHANGE_TEST_TYPE));
				ElementsTreePanel.this.skipTestUpdate = false;
				ret = KIS.class;
			} else if (obj instanceof KIS) {
				//KIS kis = (KIS) obj;
				//System.out.println("KIS:" + kis.type_id);
				//				skipTestUpdate = true;
				//				dispatcher.notify(new
				// OperationEvent(kis.getId(), 0,
				// TestParametersPanel.COMMAND_CHANGE_KIS));
				//				// Vector ports = kis.access_ports;
				//				// for (Enumeration e = ports.elements();
				// e.hasMoreElements();)
				//				// {
				//				// AccessPort port = (AccessPort)
				// e.nextElement();
				//				// dispatcher.notify(new
				// OperationEvent(port.type_id, 0,
				//				//
				// TestParametersPanel.COMMAND_CHANGE_PORT_TYPE));
				//				// }
				//				skipTestUpdate = false;
				ret = MeasurementPort.class;
			} else if (obj instanceof MeasurementPort) {
				//System.out.println("AccessPort:" +
				// ((AccessPort)
				// obj).type_id);
				ElementsTreePanel.this.skipTestUpdate = true;
				MeasurementPort port = (MeasurementPort) obj;
				ret = MonitoredElement.class;
				ElementsTreePanel.this.dispatcher
						.notify(new OperationEvent(port, 0,
										SchedulerModel.COMMAND_CHANGE_PORT_TYPE));

				java.util.List list = this.getChildNodes(node);
				for (Iterator it = list.iterator(); it.hasNext();) {
					ObjectResourceTreeNode n = (ObjectResourceTreeNode) it.next();
					Object o = n.getObject();
					MonitoredElement me = (MonitoredElement) o;
					ElementsTreePanel.this.dispatcher
							.notify(new OperationEvent(
											me.getId(),
											0,
											SchedulerModel.COMMAND_CHANGE_ME_TYPE));

				}
				ElementsTreePanel.this.skipTestUpdate = false;
			}
			return ret;
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

	public static final String	ACCESSPORT_NAME_REFLECTOMETER	= "reflectometeringnt"; //$NON-NLS-1$

	private static final String	ROOT_NODE_NAME			= "root";

	boolean				skipTestUpdate			= false;

	private ApplicationContext	aContext;
	//	private
	// ApplicationContext
	// aContext;

	private JButton			delMapGroupButton;
	Dispatcher			dispatcher;
	private JButton			loadButton;
	//private ObjectResourceTreeModel model;
	//SurveyDataSourceImage surveyDsi;

	//private Object selectedObject;
	private HashMap			paramMap;
	private JScrollPane		scrollPane			= new JScrollPane();
	private UniTreePanel		utp;
	private Test			currentTest;

	public ElementsTreePanel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		((SchedulerModel) this.aContext.getApplicationModel()).setTreeModel(new TestsTreeModel(aContext));

		setLayout(new BorderLayout());

		//Toolbar
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
				//loadButton_actionPerformed();
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
				//delMapGroupButton_actionPerformed();
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
		this.utp = new UniTreePanel(this.dispatcher, aContext, ((SchedulerModel) this.aContext
				.getApplicationModel()).getTreeModel());
		this.utp.getTree().setRootVisible(true);
		this.utp.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.scrollPane.getViewport().add(this.utp);
		add(this.scrollPane, BorderLayout.CENTER);

		initModule(this.dispatcher);
	}

	public HashMap getParameters() {
		HashMap parameters = new HashMap();
		JTree tree = getTree();
		TreePath treePath = tree.getSelectionPath();
		if (treePath != null) {
			for (int i = 0; i < treePath.getPathCount(); i++) {
				ObjectResourceTreeNode node = (ObjectResourceTreeNode) treePath.getPathComponent(i);
				Object obj = node.getObject();

				if (obj instanceof MeasurementType) {
					MeasurementType measurementType = (MeasurementType) obj;
					parameters.put(ObjectEntities.MEASUREMENTTYPE_ENTITY, measurementType);
				} else if (obj instanceof KIS) {
					KIS kis = (KIS) obj;
					parameters.put(ObjectEntities.KIS_ENTITY, kis);
				} else if (obj instanceof MeasurementPort) {
					MeasurementPort port = (MeasurementPort) obj;
					parameters.put(ObjectEntities.MEASUREMENTPORT_ENTITY, port);
				} else if (obj instanceof MonitoredElement) {
					MonitoredElement me = (MonitoredElement) obj;
					parameters.put(ObjectEntities.ME_ENTITY, me);
				}

			}
		}
		if (parameters.keySet().size() != 4) {
			JOptionPane
					.showMessageDialog(
								this,
								LangModelSchedule
										.getString("Do_not_choose_Measurement_element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.OK_OPTION);
			parameters = null;
		}
		return parameters;
	}

	public JTree getTree() {
		return this.utp.getTree();
	}

	public void operationPerformed(OperationEvent oe) {
		String commandName = oe.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent) oe;
			//			selectedObject = dse.selectedObject;
			Class selectedClass = dse.getDataClass();
			//DataSet set = dse.getDataSet();
			java.util.List col = dse.getList();

			if (selectedClass.equals(MonitoredElement.class)) {
				this.loadButton.setEnabled(true);
				//(MonitoredElement)
				// col.get(dse.getSelectedObject());
				{
					Object obj = dse.getSelectedObject();
					if ((col.contains(obj)) && (obj instanceof MonitoredElement)) {
						MonitoredElement me = (MonitoredElement) obj;
						this.dispatcher
								.notify(new OperationEvent(
												me.getId(),
												0,
												SchedulerModel.COMMAND_CHANGE_ME_TYPE));
					}

				}
			} else
				this.loadButton.setEnabled(false);

		} else if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {

			HashMap param = getParameters();
			if (param != null) {
				this.dispatcher.notify(new OperationEvent(param, SchedulerModel.DATA_ID_ELEMENTS,
										SchedulerModel.COMMAND_SEND_DATA));
			}
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			if (!this.skipTestUpdate) {
				TestUpdateEvent tue = (TestUpdateEvent) oe;
				if (tue.testSelected) {
					Test test = tue.test;
					if ((this.currentTest == null)
							|| (!this.currentTest.getId().equals(test.getId()))) {
						try {
							this.currentTest = test;
							MeasurementType measurementType = test.getMeasurementType();
							MonitoredElement me = test.getMonitoredElement();
							MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool
									.getStorableObject(me.getMeasurementPortId(),
												true);
							KIS kis = (KIS) ConfigurationStorableObjectPool
									.getStorableObject(measurementPort.getKISId(),
												true);

							if (this.paramMap == null)
								this.paramMap = new HashMap();
							this.paramMap.clear();

							this.paramMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY,
										measurementType);
							this.paramMap.put(ObjectEntities.KIS_ENTITY, kis);
							this.paramMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY,
										measurementPort);
							this.paramMap.put(ObjectEntities.ME_ENTITY, me);

							expandAll(true);
						} catch (DatabaseException databaseException) {
							/**
							 * FIXME alert exception
							 */
							databaseException.printStackTrace();
						} catch (CommunicationException e) {
							/**
							 * FIXME alert exception
							 */
							e.printStackTrace();
						}
					}
				}
			}
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			if (this.paramMap == null)
				this.paramMap = new HashMap();
			this.paramMap.clear();
			ObjectResourceTreeModel model = new TestsTreeModel(this.aContext);
			((SchedulerModel) this.aContext.getApplicationModel()).setTreeModel(model);
			this.utp.setModel(model);
			this.utp.revalidate();
		}
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	private void expandAll(boolean expand) {
		//TreeNode root = (TreeNode) tree.getModel().getRoot();
		ObjectResourceTreeNode root = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel()
				.getRoot();
		// Traverse tree from root
		expandAll(root, new TreePath(root), expand);
	}

	private void expandAll(ObjectResourceTreeNode node, TreePath parent, boolean expand) {
		java.util.List list = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel()
				.getChildNodes(node);
		for (Iterator it = list.iterator(); it.hasNext();) {
			ObjectResourceTreeNode n = (ObjectResourceTreeNode) it.next();
			Object obj = n.getObject();
			TreePath path = parent.pathByAddingChild(n);
			//System.out.println("obj:" + obj.getClass().getName()
			// + "\t" +
			// obj);
			boolean found = false;
			if (obj instanceof MeasurementType) {
				MeasurementType measurementType = (MeasurementType) obj;
				MeasurementType paramMeasurementType = (MeasurementType) this.paramMap
						.get(ObjectEntities.MEASUREMENTTYPE_ENTITY);
				if (measurementType.getId().equals(paramMeasurementType.getId())) {
					//System.out.println("+testType:" +
					// testType.id);
					found = true;
				}
			} else if (obj instanceof KIS) {
				KIS kis = (KIS) obj;
				KIS paramKis = (KIS) this.paramMap.get(ObjectEntities.KIS_ENTITY);
				if (kis.getId().equals(paramKis.getId())) {
					//System.out.println("+kis:" + kis.id);
					found = true;
				}
			} else if (obj instanceof MeasurementPort) {
				MeasurementPort port = (MeasurementPort) obj;
				MeasurementPort paramPort = (MeasurementPort) this.paramMap
						.get(ObjectEntities.MEASUREMENTPORT_ENTITY);
				if ((paramPort != null) && (port.getId().equals(paramPort.getId()))) {
					//System.out.println("+port:" +
					// port.id);
					found = true;
				}
			} else if (obj instanceof MonitoredElement) {
				MonitoredElement me = (MonitoredElement) obj;
				MonitoredElement paramMe = (MonitoredElement) this.paramMap
						.get(ObjectEntities.ME_ENTITY);
				if (me.getId().equals(paramMe.getId())) {
					//System.out.println("+me:" + me.id);
					found = true;
				}
			}
			if (found) {
				JTree tree = this.getTree();
				//				 Expansion or collapse must be done bottom-up
				if (expand) {
					tree.expandPath(parent);
				} else {
					tree.collapsePath(parent);
				}
				//System.out.println("path:" +
				// path.toString());
				tree.setSelectionPath(path);
				expandAll(n, path, expand);
			}
		}

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TreeDataSelectionEvent.type);
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_DATA_REQUEST);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
	}
}