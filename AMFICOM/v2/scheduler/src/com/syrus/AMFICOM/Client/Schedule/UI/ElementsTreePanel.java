package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;

public class ElementsTreePanel extends JPanel implements OperationListener {
	JButton delMapGroupButton;
	JButton loadButton;
	ApplicationContext aContext;
	Dispatcher dispatcher;
	ObjectResourceTreeModel model;
	UniTreePanel utp;

	Object selectedObject;

	public ElementsTreePanel(
		ApplicationContext aContext,
		Dispatcher dispatcher,
		ObjectResourceTreeModel model) {
		this.aContext = aContext;
		this.dispatcher = dispatcher;
		this.model = model;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		init_module(dispatcher);
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		//Toolbar
		loadButton = new JButton();
		loadButton.setIcon(UIUtil.openFileIcon);
		loadButton.setToolTipText("Открыть");
		loadButton.setMargin(UIUtil.nullInsets);
		loadButton.setBorder(
			BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		loadButton.setFocusPainted(false);
		loadButton.setEnabled(false);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				loadButton_actionPerformed();
			}
		});

		delMapGroupButton = new JButton();
		delMapGroupButton.setMargin(UIUtil.nullInsets);
		delMapGroupButton.setIcon(UIUtil.deleteIcon);
		delMapGroupButton.setToolTipText("Удалить");
		delMapGroupButton.setBorder(
			BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
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
		utp.getTree().setRootVisible(false);

		JScrollPane scroll_pane = new JScrollPane();
		scroll_pane.getViewport().add(utp);
		add(scroll_pane, BorderLayout.CENTER);
	}

	void init_module(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public JTree getTree() {
		return utp.getTree();
	}

	public void operationPerformed(OperationEvent oe) {
		if (oe.getActionCommand().equals(TreeDataSelectionEvent.type)) {
			TreeDataSelectionEvent dse = (TreeDataSelectionEvent) oe;
			selectedObject = dse.selectedObject;
			Class selected_class = dse.getDataClass();

			if (selected_class.equals(MonitoredElement.class)) {
				loadButton.setEnabled(true);
			} else
				loadButton.setEnabled(false);

			if (dse.getSelectionNumber() != -1) {
			}
		}
	}

	void loadButton_actionPerformed() {
	}

	void delMapGroupButton_actionPerformed() {
	}
}

class TestsTreeModel extends ObjectResourceTreeModel {
	DataSourceInterface dsi;

	public TestsTreeModel(DataSourceInterface dsi) {
		this.dsi = dsi;
	}

	public ObjectResourceTreeNode getRoot() {
		return new ObjectResourceTreeNode(
			"root",
			"Вид тестирования",
			true,
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/folder.gif")));

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
		if (node.getObject() instanceof String) {
			if (node.getObject().equals("root"))
				return TestType.class;
		}
		if (node.getObject() instanceof TestType)
			return KIS.class;
		if (node.getObject() instanceof KIS)
			return AccessPort.class;
		if (node.getObject() instanceof AccessPort)
			return MonitoredElement.class;
		return null;
	}

	public Vector getChildNodes(ObjectResourceTreeNode node) {
		Vector vec = new Vector();

		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();

			if (s.equals("root")) {
				for (Enumeration enum = Pool.getHash(TestType.typ).elements();
					enum.hasMoreElements();
					) {
					TestType tt = (TestType) enum.nextElement();
					vec.add(
						new ObjectResourceTreeNode(
							tt,
							tt.getName(),
							true,
							new ImageIcon(
								Toolkit.getDefaultToolkit().getImage(
									"images/folder.gif"))));
				}
			}
		} else if (node.getObject() instanceof TestType) {
			TestType tt = (TestType) node.getObject();
			Hashtable kiss = Pool.getHash(KIS.typ);
			if (kiss != null)
				for (Enumeration enum = kiss.elements();
					enum.hasMoreElements();
					) {
					KIS kis = (KIS) enum.nextElement();
					for (Enumeration en = kis.access_ports.elements();
						en.hasMoreElements();
						) {
						AccessPort aport = (AccessPort) en.nextElement();
						AccessPortType apt =
							(AccessPortType) Pool.get(
								AccessPortType.typ,
								aport.type_id);
						if (apt.test_type_ids.contains(tt.getId())) {
							vec.add(
								new ObjectResourceTreeNode(
									kis,
									kis.getName(),
									true,
									new ImageIcon(
										(Toolkit
											.getDefaultToolkit()
											.getImage("images/testing.gif")
											.getScaledInstance(
												16,
												16,
												Image.SCALE_SMOOTH))),
									false));
							break;
						}
					}
				}
		} else if (node.getObject() instanceof KIS) {
			TestType tt =
				(TestType) ((ObjectResourceTreeNode) node.getParent())
					.getObject();
			KIS parent_kis = (KIS) node.getObject();

			for (int i = 0; i < parent_kis.access_ports.size(); i++) {
				AccessPort aport = (AccessPort) parent_kis.access_ports.get(i);
				AccessPortType apt =
					(AccessPortType) Pool.get(
						AccessPortType.typ,
						aport.type_id);
				if (apt.test_type_ids.contains(tt.getId())) {
					vec.add(
						new ObjectResourceTreeNode(
							aport,
							aport.getName(),
							true,
							new ImageIcon(
								Toolkit.getDefaultToolkit().getImage(
									"images/port.gif")),
							false));
				}
			}
		} else if (node.getObject() instanceof AccessPort) {
			AccessPort aport = (AccessPort) node.getObject();

			if (Pool.getHash(MonitoredElement.typ) != null);
			{
				for (Enumeration en =
					Pool.getHash(MonitoredElement.typ).elements();
					en.hasMoreElements();
					) {
					MonitoredElement me = (MonitoredElement) en.nextElement();
					if (me.access_port_id.equals(aport.getId()))
						vec.add(
							new ObjectResourceTreeNode(
								me,
								me.getName(),
								true,
								new ImageIcon(
									Toolkit.getDefaultToolkit().getImage(
										"images/pathmode.gif")),
								true));
				}
			}
		}
		return vec;
	}

}
