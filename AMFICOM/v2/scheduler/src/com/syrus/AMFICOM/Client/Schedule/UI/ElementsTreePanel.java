package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.tree.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;

public class ElementsTreePanel extends JPanel implements OperationListener {

	public static final String		ACCESSPORT_NAME_REFLECTOMETER	= "reflectometeringnt";

	private JButton					delMapGroupButton;

	private JButton					loadButton;

	private ApplicationContext		aContext;

	private Dispatcher				dispatcher;

	private ObjectResourceTreeModel	model;

	private UniTreePanel			utp;

	//private Object selectedObject;

	public ElementsTreePanel(ApplicationContext aContext,
			ObjectResourceTreeModel model) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.model = model;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initModule(dispatcher);
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		//Toolbar
		loadButton = new JButton();
		loadButton.setIcon(UIUtil.OPEN_FILE_ICON);
		loadButton.setToolTipText("Открыть");
		loadButton.setMargin(UIUtil.INSET_NULL);
		loadButton.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		loadButton.setFocusPainted(false);
		loadButton.setEnabled(false);
		loadButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				loadButton_actionPerformed();
			}
		});

		delMapGroupButton = new JButton();
		delMapGroupButton.setMargin(UIUtil.INSET_NULL);
		delMapGroupButton.setIcon(UIUtil.DELETE_ICON);
		delMapGroupButton.setToolTipText("Удалить");
		delMapGroupButton.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
		delMapGroupButton.setFocusPainted(false);
		delMapGroupButton.setEnabled(false);
		delMapGroupButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent ev) {
				delMapGroupButton_actionPerformed();
			}
		});

		{
			Dimension d = new Dimension(24, 24);
			UIUtil.setRigidSize(delMapGroupButton, d);
			UIUtil.setRigidSize(loadButton, d);
		}

		Box toolBar = new Box(BoxLayout.X_AXIS);
		toolBar.add(loadButton);
		toolBar.add(delMapGroupButton);
		toolBar.add(Box.createHorizontalGlue());
		add(toolBar, BorderLayout.NORTH);

		// TREE
		utp = new UniTreePanel(dispatcher, aContext, model);
		utp.getTree().setRootVisible(true);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(utp);
		add(scrollPane, BorderLayout.CENTER);
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TreeDataSelectionEvent.type);
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, TestRequestFrame.COMMAND_DATA_REQUEST);
	}

	public JTree getTree() {
		return utp.getTree();
	}

	public void operationPerformed(OperationEvent oe) {
		String commandName = oe.getActionCommand();
		System.out
				.println(getClass().getName() + " commandName:" + commandName);
		if (commandName.equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent) oe;
			//			selectedObject = dse.selectedObject;
			Class selected_class = dse.getDataClass();

			if (selected_class.equals(MonitoredElement.class)) {
				loadButton.setEnabled(true);
			} else
				loadButton.setEnabled(false);

			if (dse.getSelectionNumber() != -1) {
			}
		} else if (commandName
				.equalsIgnoreCase(TestRequestFrame.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			dispatcher.notify(new OperationEvent("", 0,
					TestRequestFrame.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) oe;
			if (tue.TEST_SELECTED) {
				System.out.println("commandName.equals(TestUpdateEvent.typ)");
				Test test = tue.test;
				KIS kis = (KIS) Pool.get(KIS.typ, test.kis_id);
				String kisName = kis.name;
				System.out.println(kisName + "\t" + kis.id);
				expandAll(utp.getTree(), true);
			}
		}
	}

	private void loadButton_actionPerformed() {
	}

	private void delMapGroupButton_actionPerformed() {
	}

	// If expand is true, expands all nodes in the tree.
	// Otherwise, collapses all nodes in the tree.
	private void expandAll(JTree tree, boolean expand) {
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		// Traverse tree from root
		expandAll(tree, new TreePath(root), expand);
	}

	private void expandAll(JTree tree, TreePath parent, boolean expand) {
		// Traverse children
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				String name = n.toString();
				System.out.println("name:" + name);
				if (name.equals("Node Label1")) {
					System.out.println("!!!");
					tree.setSelectionPath(path);
				}
				expandAll(tree, path, expand);
			}
		}

		// Expansion or collapse must be done bottom-up
		if (expand) {
			tree.expandPath(parent);
		} else {
			tree.collapsePath(parent);
		}
	}

}

class TestsTreeModel extends ObjectResourceTreeModel {

	private DataSourceInterface		dsi;

	private ApplicationContext		aContext;

	private Dispatcher				dispatcher;

	private ObjectResourceTreeNode	root;

	private TreeModel				model;

	public TestsTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dsi = aContext.getDataSourceInterface();
		this.dispatcher = aContext.getDispatcher();

		root = new ObjectResourceTreeNode("root", "Вид тестирования", true,
				(ImageIcon) UIUtil.FOLDER_ICON);

		Hashtable kisTable = Pool.getHash(KIS.typ);
		Hashtable meTable = Pool.getHash(MonitoredElement.typ);
		for (Enumeration testTypeEn = Pool.getHash(TestType.typ).elements(); testTypeEn
				.hasMoreElements();) {
			TestType testType = (TestType) testTypeEn.nextElement();
			ObjectResourceTreeNode testTypeNode = new ObjectResourceTreeNode(
					testType, testType.getName(), true,
					(ImageIcon) UIUtil.FOLDER_ICON);
			root.add(testTypeNode);
			if (kisTable != null) {
				for (Enumeration kisTypeEnum = kisTable.elements(); kisTypeEnum
						.hasMoreElements();) {
					KIS kis = (KIS) kisTypeEnum.nextElement();
					for (Enumeration en = kis.access_ports.elements(); en
							.hasMoreElements();) {
						AccessPort acessPort = (AccessPort) en.nextElement();
						AccessPortType apt = (AccessPortType) Pool.get(
								AccessPortType.typ, acessPort.type_id);
						if (apt.test_type_ids.contains(testType.getId())) {
							ObjectResourceTreeNode kisNode = new ObjectResourceTreeNode(
									kis, kis.getName(), true,
									(ImageIcon) UIUtil.TESTING_ICON, false);
							testTypeNode.add(kisNode);
							for (int i = 0; i < kis.access_ports.size(); i++) {
								AccessPort aport = (AccessPort) kis.access_ports
										.get(i);
								{
									ObjectResourceTreeNode accessPortNode = new ObjectResourceTreeNode(
											aport, aport.getName(), true,
											(ImageIcon) UIUtil.PORT_ICON, false);
									kisNode.add(accessPortNode);
									for (Enumeration meEnum = meTable
											.elements(); meEnum
											.hasMoreElements();) {
										MonitoredElement me = (MonitoredElement) meEnum
												.nextElement();
										if (me.access_port_id.equals(aport
												.getId())) {
											ObjectResourceTreeNode meNode = new ObjectResourceTreeNode(
													me,
													me.getName(),
													true,
													(ImageIcon) UIUtil.PATHMODE_ICON,
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
		}
		model = new DefaultTreeModel(root);

	}

	public ObjectResourceTreeNode getRoot() {
		System.out.println("TestsTreeModel getRoot()");
		return root;
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node) {
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node) {
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node) {
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node) {
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node) {
		Object obj = node.getObject();
		Class ret = null;
		if (obj instanceof String) {
			if (node.getObject().equals("root")) ret = TestType.class;
		} else if (obj instanceof TestType) {
			//System.out.println("testType:" + ((TestType) obj).name);
			ret = KIS.class;
		} else if (obj instanceof KIS) {
			KIS kis = (KIS) obj;
			//System.out.println("KIS:" + kis.type_id);
			Vector ports = kis.access_ports;
			for (Enumeration e = ports.elements(); e.hasMoreElements();) {
				AccessPort port = (AccessPort) e.nextElement();
				dispatcher.notify(new OperationEvent(port.type_id, 0,
						TestParametersPanel.COMMAND_CHANGE_PORT_TYPE));
			}
			ret = AccessPort.class;
		} else if (obj instanceof AccessPort) {
			//System.out.println("AccessPort:" + ((AccessPort) obj).type_id);
			AccessPort port = (AccessPort) obj;
			ret = MonitoredElement.class;
			dispatcher.notify(new OperationEvent(port.type_id, 0,
					TestParametersPanel.COMMAND_CHANGE_PORT_TYPE));
		}
		return ret;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node) {
		System.out.println("getChildNodes\t"+node.getObject().getClass().getName()+"\tnode.getChildCount():"
				+ node.getChildCount());

		if (node.getObject() instanceof AccessPort) {
			System.out.println("accessPort");
			AccessPort aport = (AccessPort) node.getObject();
			ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node
					.getParent();

			if (node.getChildCount() == 0) {
				if (Pool.getHash(MonitoredElement.typ) != null) {
					for (Enumeration en = Pool.getHash(MonitoredElement.typ)
							.elements(); en.hasMoreElements();) {
						MonitoredElement me = (MonitoredElement) en
								.nextElement();
						if (me.access_port_id.equals(aport.getId()))
								parent
										.add(new ObjectResourceTreeNode(
												me,
												me.getName(),
												true,
												(ImageIcon) UIUtil.PATHMODE_ICON,
												true));
					}
				}
			}
		}

		Vector vec = new Vector();
		for (int i = 0; i < node.getChildCount(); i++)
			vec.add(model.getChild(node, i));
		return vec;

		//		if (node.getObject() instanceof String) {
		//			if (stringChildren == null) {
		//				stringChildren = new Vector();
		//				String s = (String) node.getObject();
		//
		//				if (s.equals("root")) {
		//					for (Enumeration enum = Pool.getHash(TestType.typ)
		//							.elements(); enum.hasMoreElements();) {
		//						TestType tt = (TestType) enum.nextElement();
		//						stringChildren.add(new ObjectResourceTreeNode(tt, tt
		//								.getName(), true, new ImageIcon(Toolkit
		//								.getDefaultToolkit().getImage(
		//										"images/folder.gif"))));
		//					}
		//				}
		//			}
		//			children = stringChildren;
		//		} else if (node.getObject() instanceof TestType) {
		//			if (testTypeChildren == null) {
		//				testTypeChildren = new Vector();
		//				TestType tt = (TestType) node.getObject();
		//				Hashtable kiss = Pool.getHash(KIS.typ);
		//				if (kiss != null)
		//						for (Enumeration enum = kiss.elements(); enum
		//								.hasMoreElements();) {
		//							KIS kis = (KIS) enum.nextElement();
		//							for (Enumeration en = kis.access_ports.elements(); en
		//									.hasMoreElements();) {
		//								AccessPort aport = (AccessPort) en
		//										.nextElement();
		//								AccessPortType apt = (AccessPortType) Pool.get(
		//										AccessPortType.typ, aport.type_id);
		//								if (apt.test_type_ids.contains(tt.getId())) {
		//									testTypeChildren
		//											.add(new ObjectResourceTreeNode(
		//													kis,
		//													kis.getName(),
		//													true,
		//													new ImageIcon(
		//															(Toolkit
		//																	.getDefaultToolkit()
		//																	.getImage(
		//																			"images/testing.gif")
		//																	.getScaledInstance(
		//																			16,
		//																			16,
		//																			Image.SCALE_SMOOTH))),
		//													false));
		//									break;
		//								}
		//							}
		//						}
		//			}
		//			children = testTypeChildren;
		//		} else if (node.getObject() instanceof KIS) {
		//			if (kisChildren == null) {
		//				kisChildren = new Vector();
		//				ObjectResourceTreeNode parent = (ObjectResourceTreeNode) node
		//						.getParent();
		//				if (parent == null)
		//						System.err.println(this.getClass().getName()
		//								+ " parent is null");
		//				TestType tt = (TestType) (parent).getObject();
		//				KIS parent_kis = (KIS) node.getObject();
		//
		//				for (int i = 0; i < parent_kis.access_ports.size(); i++) {
		//					AccessPort aport = (AccessPort) parent_kis.access_ports
		//							.get(i);
		//					AccessPortType apt = (AccessPortType) Pool.get(
		//							AccessPortType.typ, aport.type_id);
		//					if (apt.test_type_ids.contains(tt.getId())) {
		//						kisChildren.add(new ObjectResourceTreeNode(aport, aport
		//								.getName(), true, new ImageIcon(Toolkit
		//								.getDefaultToolkit()
		//								.getImage("images/port.gif")), false));
		//					}
		//				}
		//			}
		//			children = kisChildren;
		//		} else

		//return children;
	}

}