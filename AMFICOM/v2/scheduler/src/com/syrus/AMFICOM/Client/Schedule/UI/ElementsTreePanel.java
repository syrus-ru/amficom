package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class ElementsTreePanel extends JPanel implements OperationListener {

	private class TestsTreeModel extends ObjectResourceTreeModel {

		//private DataSourceInterface dsi;

		//private ApplicationContext aContext;

		private Dispatcher				dispatcher;

		private TreeModel				model;

		private ObjectResourceTreeNode	root;

		//	private Hashtable testTypeTable;
		//
		//	private Hashtable kisTable;
		//
		//	private Hashtable meTable;

		public TestsTreeModel(ApplicationContext aContext) {
			//this.aContext = aContext;
			//this.dsi = aContext.getDataSourceInterface();
			this.dispatcher = aContext.getDispatcher();

			this.root = new ObjectResourceTreeNode(ROOT_NODE_NAME, LangModelSchedule.getString("TestType"), true, //$NON-NLS-1$ //$NON-NLS-2$
													(ImageIcon) UIStorage.FOLDER_ICON);

			Hashtable kisTable = Pool.getHash(KIS.typ);
			Hashtable meTable = Pool.getHash(MonitoredElement.typ);
			Hashtable aptTable = Pool.getHash(AccessPortType.typ);
			java.util.List meList = new ArrayList();

			if (ElementsTreePanel.this.surveyDsi == null)
				ElementsTreePanel.this.surveyDsi = new SurveyDataSourceImage(aContext.getDataSourceInterface());

			for (Enumeration testTypeEn = Pool.getHash(TestType.typ).elements(); testTypeEn.hasMoreElements();) {
				TestType testType = (TestType) testTypeEn.nextElement();
				ElementsTreePanel.this.surveyDsi.getTestSetupByTestType(testType.getId());
				ObjectResourceTreeNode testTypeNode = new ObjectResourceTreeNode(testType, testType.getName(), true,
																					(ImageIcon) UIStorage.FOLDER_ICON);
				this.root.add(testTypeNode);
				if (kisTable != null) {
					for (Enumeration kisTypeEnum = kisTable.elements(); kisTypeEnum.hasMoreElements();) {
						KIS kis = (KIS) kisTypeEnum.nextElement();
						for (Enumeration en = kis.access_ports.elements(); en.hasMoreElements();) {
							AccessPort acessPort = (AccessPort) en.nextElement();
							AccessPortType apt = (AccessPortType) aptTable.get(acessPort.type_id);
							if (apt.test_type_ids.contains(testType.getId())) {
								ObjectResourceTreeNode kisNode = new ObjectResourceTreeNode(
																							kis,
																							kis.getName(),
																							true,
																							(ImageIcon) UIStorage.TESTING_ICON,
																							false);
								testTypeNode.add(kisNode);
								for (int i = 0; i < kis.access_ports.size(); i++) {
									AccessPort aport = (AccessPort) kis.access_ports.get(i);
									{
										ObjectResourceTreeNode accessPortNode = new ObjectResourceTreeNode(aport, aport
												.getName(), true, (ImageIcon) UIStorage.PORT_ICON, false);
										kisNode.add(accessPortNode);
										for (Enumeration meEnum = meTable.elements(); meEnum.hasMoreElements();) {
											MonitoredElement me = (MonitoredElement) meEnum.nextElement();
											ElementsTreePanel.this.surveyDsi.getTestSetupByME(me.getId());
											if (me.access_port_id.equals(aport.getId())) {
												meList.add(me);
												ObjectResourceTreeNode meNode = new ObjectResourceTreeNode(me, me
														.getName(), true, (ImageIcon) UIStorage.PATHMODE_ICON, true);
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
			}
			dispatcher.notify(new OperationEvent(meList, 0, SchedulerModel.COMMAND_AVAILABLE_ME));
			this.model = new DefaultTreeModel(this.root);

		}

		public Vector getChildNodes(ObjectResourceTreeNode node) {

			//		Object obj = node.getObject();
			int count = node.getChildCount();
			//		ObjectResourceTreeNode parent = (ObjectResourceTreeNode)
			// node.getParent();
			//		System.out.println("getChildNodes\t" + obj.getClass().getName()
			//				+ "\tnode.getChildCount():" + count);

			Vector vec = new Vector();
			for (int i = 0; i < count; i++)
				vec.add(model.getChild(node, i));
			return vec;
		}

		public Class getNodeChildClass(ObjectResourceTreeNode node) {
			Object obj = node.getObject();
			Class ret = null;
			if (obj instanceof String) {
				if (node.getObject().equals(ROOT_NODE_NAME))
					ret = TestType.class; //$NON-NLS-1$
			} else if (obj instanceof TestType) {
				//				//System.out.println("testType:" + ((TestType) obj).name);
				TestType testType = (TestType) obj;
				//				//System.out.println("testType.id:"+testType.id);
				skipTestUpdate = true;
				dispatcher.notify(new OperationEvent(testType.getId(), 0, SchedulerModel.COMMAND_CHANGE_TEST_TYPE));
				skipTestUpdate = false;
				ret = KIS.class;
			} else if (obj instanceof KIS) {
				//KIS kis = (KIS) obj;
				//System.out.println("KIS:" + kis.type_id);
				//				skipTestUpdate = true;
				//				dispatcher.notify(new OperationEvent(kis.getId(), 0,
				// TestParametersPanel.COMMAND_CHANGE_KIS));
				//				// Vector ports = kis.access_ports;
				//				// for (Enumeration e = ports.elements();
				// e.hasMoreElements();)
				//				// {
				//				// AccessPort port = (AccessPort) e.nextElement();
				//				// dispatcher.notify(new OperationEvent(port.type_id, 0,
				//				// TestParametersPanel.COMMAND_CHANGE_PORT_TYPE));
				//				// }
				//				skipTestUpdate = false;
				ret = AccessPort.class;
			} else if (obj instanceof AccessPort) {
				//System.out.println("AccessPort:" + ((AccessPort)
				// obj).type_id);
				ElementsTreePanel.this.skipTestUpdate = true;
				AccessPort port = (AccessPort) obj;
				ret = MonitoredElement.class;
				this.dispatcher.notify(new OperationEvent(port, 0, SchedulerModel.COMMAND_CHANGE_PORT_TYPE));

				Vector vec = this.getChildNodes(node);
				for (int i = 0; i < vec.size(); i++) {
					ObjectResourceTreeNode n = (ObjectResourceTreeNode) vec.get(i);
					Object o = n.getObject();
					MonitoredElement me = (MonitoredElement) o;
					this.dispatcher.notify(new OperationEvent(me.getId(), 0, SchedulerModel.COMMAND_CHANGE_ME_TYPE));

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
			return root;
		}

		public void nodeAfterSelected(ObjectResourceTreeNode node) {
			// nothing to do
		}

		public void nodeBeforeExpanded(ObjectResourceTreeNode node) {
			// nothing to do
		}

	}

	public static final String	ACCESSPORT_NAME_REFLECTOMETER	= "reflectometeringnt"; //$NON-NLS-1$

	private static final String	ROOT_NODE_NAME					= "root";

	boolean						skipTestUpdate					= false;

	private ApplicationContext	aContext;
	//	private
	// ApplicationContext
	// aContext;

	private JButton				delMapGroupButton;
	private Dispatcher			dispatcher;
	private JButton				loadButton;
	//private ObjectResourceTreeModel model;
	SurveyDataSourceImage		surveyDsi;

	//private Object selectedObject;
	private HashMap				paramMap;
	private JScrollPane			scrollPane						= new JScrollPane();
	private UniTreePanel		utp;
	private Test				currentTest;

	public ElementsTreePanel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		((SchedulerModel) this.aContext.getApplicationModel()).setTreeModel(new TestsTreeModel(aContext));

		setLayout(new BorderLayout());

		//Toolbar
		loadButton = new JButton();
		loadButton.setIcon(UIStorage.OPEN_FILE_ICON);
		loadButton.setToolTipText(LangModelSchedule.getString("Open")); //$NON-NLS-1$
		loadButton.setMargin(UIStorage.INSET_NULL);
		loadButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		loadButton.setFocusPainted(false);
		loadButton.setEnabled(false);
		loadButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				/**
				 * @todo do loading tree
				 */
				//loadButton_actionPerformed();
			}
		});

		delMapGroupButton = new JButton();
		delMapGroupButton.setMargin(UIStorage.INSET_NULL);
		delMapGroupButton.setIcon(UIStorage.DELETE_ICON);
		delMapGroupButton.setToolTipText(LangModelSchedule.getString("Delete")); //$NON-NLS-1$
		delMapGroupButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		delMapGroupButton.setFocusPainted(false);
		delMapGroupButton.setEnabled(false);
		delMapGroupButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				/**
				 * @todo do delete tree
				 */
				//delMapGroupButton_actionPerformed();
			}
		});

		{
			Dimension d = new Dimension(24, 24);
			UIStorage.setRigidSize(delMapGroupButton, d);
			UIStorage.setRigidSize(loadButton, d);
		}

		Box toolBar = new Box(BoxLayout.X_AXIS);
		toolBar.add(loadButton);
		toolBar.add(delMapGroupButton);
		toolBar.add(Box.createHorizontalGlue());
		add(toolBar, BorderLayout.NORTH);

		// TREE
		this.utp = new UniTreePanel(this.dispatcher, aContext, ((SchedulerModel) this.aContext.getApplicationModel())
				.getTreeModel());
		this.utp.getTree().setRootVisible(true);
		this.utp.getTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		scrollPane.getViewport().add(this.utp);
		add(scrollPane, BorderLayout.CENTER);

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

				if (obj instanceof TestType) {
					TestType testType = (TestType) obj;
					parameters.put(TestType.typ, testType);
				} else if (obj instanceof KIS) {
					KIS kis = (KIS) obj;
					parameters.put(KIS.typ, kis);
				} else if (obj instanceof AccessPort) {
					AccessPort port = (AccessPort) obj;
					parameters.put(AccessPort.typ, port);
				} else if (obj instanceof MonitoredElement) {
					MonitoredElement me = (MonitoredElement) obj;
					parameters.put(MonitoredElement.typ, me);
				}

			}
		}
		if (parameters.keySet().size() != 4) {
			JOptionPane
					.showMessageDialog(
										this,
										LangModelSchedule.getString("Do_not_choose_Measurement_element"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
										JOptionPane.OK_OPTION);
			parameters = null;
		}
		return parameters;
	}

	public JTree getTree() {
		return utp.getTree();
	}

	public void operationPerformed(OperationEvent oe) {
		String commandName = oe.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent) oe;
			//			selectedObject = dse.selectedObject;
			Class selectedClass = dse.getDataClass();
			//DataSet set = dse.getDataSet();
			Collection col = dse.getCollection();

			if (selectedClass.equals(MonitoredElement.class)) {
				this.loadButton.setEnabled(true);				 
					//(MonitoredElement) col.get(dse.getSelectedObject());
				{
					Object obj = dse.getSelectedObject();
					if ((col.contains(obj))&&(obj instanceof MonitoredElement)){
						MonitoredElement me = (MonitoredElement)obj;
						this.dispatcher.notify(new OperationEvent(me.getId(), 0, SchedulerModel.COMMAND_CHANGE_ME_TYPE));
					}
						
				}				
			} else
				this.loadButton.setEnabled(false);

		} else if (commandName.equalsIgnoreCase(SchedulerModel.COMMAND_DATA_REQUEST)) {

			HashMap param = getParameters();
			if (param != null) {
				dispatcher.notify(new OperationEvent(param, SchedulerModel.DATA_ID_ELEMENTS,
														SchedulerModel.COMMAND_SEND_DATA));
			}
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			if (!skipTestUpdate) {
				TestUpdateEvent tue = (TestUpdateEvent) oe;
				if (tue.testSelected) {
					Test test = tue.test;
					if ((this.currentTest == null) || (!this.currentTest.getId().equals(test.getId()))) {
						this.currentTest = test;
						TestType testType = (TestType) Pool.get(TestType.typ, test.getTestTypeId());
						//System.out.println("testType:" + testType.id + "\t" +
						// testType.name);
						KIS kis = (KIS) Pool.get(KIS.typ, test.getKisId());
						Vector accessPorts = kis.access_ports;
						//System.out.println("kis:" + kis.id + "\t" +
						// kis.name);
						MonitoredElement me = (MonitoredElement) Pool.get(MonitoredElement.typ, test
								.getMonitoredElementId());
						AccessPort port = null;
						for (int i = 0; i < accessPorts.size(); i++) {
							AccessPort aport = (AccessPort) accessPorts.get(i);
							if (me.access_port_id.equals(aport.getId())) {
								port = aport;
								//System.out.println("port:" + port.id + "\t" +
								// port.name);
								//System.out.println("me:" + me.element_id +
								// "\t" +
								// me.element_name);
								break;
							}
						}

						if (paramMap == null)
							paramMap = new HashMap();
						paramMap.clear();

						paramMap.put(TestType.typ, testType);
						paramMap.put(KIS.typ, kis);
						paramMap.put(AccessPort.typ, port);
						paramMap.put(MonitoredElement.typ, me);

						expandAll(true);
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
		ObjectResourceTreeNode root = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel().getRoot();
		// Traverse tree from root
		expandAll(root, new TreePath(root), expand);
	}

	private void expandAll(ObjectResourceTreeNode node, TreePath parent, boolean expand) {
		Vector vec = ((SchedulerModel) this.aContext.getApplicationModel()).getTreeModel().getChildNodes(node);
		for (int i = 0; i < vec.size(); i++) {
			ObjectResourceTreeNode n = (ObjectResourceTreeNode) vec.get(i);
			Object obj = n.getObject();
			TreePath path = parent.pathByAddingChild(n);
			//System.out.println("obj:" + obj.getClass().getName() + "\t" +
			// obj);
			boolean found = false;
			if (obj instanceof TestType) {
				TestType testType = (TestType) obj;
				TestType paramTestType = (TestType) paramMap.get(TestType.typ);
				if (testType.getId().equals(paramTestType.getId())) {
					//System.out.println("+testType:" + testType.id);
					found = true;
				}
			} else if (obj instanceof KIS) {
				KIS kis = (KIS) obj;
				KIS paramKis = (KIS) paramMap.get(KIS.typ);
				if (kis.getId().equals(paramKis.getId())) {
					//System.out.println("+kis:" + kis.id);
					found = true;
				}
			} else if (obj instanceof AccessPort) {
				AccessPort port = (AccessPort) obj;
				AccessPort paramPort = (AccessPort) paramMap.get(AccessPort.typ);
				if (port.getId().equals(paramPort.getId())) {
					//System.out.println("+port:" + port.id);
					found = true;
				}
			} else if (obj instanceof MonitoredElement) {
				MonitoredElement me = (MonitoredElement) obj;
				MonitoredElement paramMe = (MonitoredElement) paramMap.get(MonitoredElement.typ);
				if (me.id.equals(paramMe.id)) {
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
				//System.out.println("path:" + path.toString());
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