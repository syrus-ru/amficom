package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;


public class MyExtendedFrame extends JInternalFrame
		implements OperationListener
{
	TestType tt0;
	Dispatcher internal_dispatcher;
	ScheduleMDIMain parent;
	ApplicationContext aContext;
	FilterTreeRenderer renderer = new FilterTreeRenderer();
	MouseListener  ml;

	public static IniFile iniFile;
	static String iniFileName = "My.properties";

	boolean initial_init = true;
	private JPanel jPanel1 = new JPanel();
	private GridLayout gridLayout1 = new GridLayout();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel jLabel3 = new JLabel();
	private JPanel jPanel3 = new JPanel();
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout2 = new BorderLayout();
	private BorderLayout borderLayout3 = new BorderLayout();
	private ObjectResourceComboBox jComboBox3 = new ObjectResourceComboBox("testtype");
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JTree tree;

	public MyExtendedFrame(ScheduleMDIMain parent, ApplicationContext aContext)
	{
		this.aContext=aContext;
		this.parent=parent;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
	}

	private void jbInit() throws Exception
	{
		tt0 = (TestType )jComboBox3.getSelectedItem();
		tree = ExtendedTree(tt0);
		jPanel1.setLayout(borderLayout1);
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		tt0 = (TestType )jComboBox3.getItemAt(0);
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"RemoveExtendedFrame");
		this.setClosable(true);
		this.setIconifiable(true);
//		this.setMaximizable(true);
		this.setResizable(true);
		//setSize(new Dimension(500, 200));
		this.setTitle(LangModelScheduleOld.String("MyExtendedTitle"));
		this.getContentPane().setLayout(borderLayout2);
		internal_dispatcher.notify(new OperationEvent(tt0.getId(),0,"TestType"));
		internal_dispatcher.notify(new OperationEvent(tree.getModel(),0,"KISes_and_Ports"));
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e))
				{
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					FilterTreeNode mte = (FilterTreeNode )selPath.getLastPathComponent();
					Choise(mte);
					tree.repaint();
					internal_dispatcher.notify(new OperationEvent(tree.getModel(),0,"KISes_and_Ports"));
				}
			}
		};
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
			{
				public void internalFrameActivated(InternalFrameEvent e)
				{
					this_internalFrameActivated(e);
				}

				public void internalFrameOpened(InternalFrameEvent e)
				{
					this_internalFrameOpened(e);
				}
			});
		this.addComponentListener(new java.awt.event.ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					this_componentShown(e);
				}
			});
		jLabel3.setText(LangModelScheduleOld.String("labelTestType"));
		jPanel3.setLayout(gridLayout1);
		jPanel2.setLayout(borderLayout3);
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			jComboBox3_actionPerformed(e);
			}
		});
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox3_actionPerformed(e);
			}
		});
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox3_actionPerformed(e);
			}
		});
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox3_actionPerformed(e);
			}
		});
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox3_actionPerformed(e);
			}
		});
		jComboBox3.setToolTipText(tt0.getName());
		jPanel2.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		tree.setCellRenderer(renderer);
		jScrollPane1.setBorder(null);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(tree, null);
		jPanel1.add(jPanel3, BorderLayout.NORTH);
		jPanel3.add(jLabel3, null);
		jPanel3.add(jComboBox3, null);
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		tree.addMouseListener(ml);
	}

	public void init_module()
	{
		initial_init = false;
		ApplicationModel aModel = aContext.getApplicationModel();
		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
		setFromIniFile();
		}
		catch(java.io.IOException e)
		{
			setDefaults();
		}

		aModel.setCommand("myEntry", new VoidCommand());

		aModel.setEnabled("myEntry", true);
		aModel.fireModelChanged("");
	}

	public void setFromIniFile()
	{
	}

	public void setDefaults()
	{
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext == null)
			return;
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());

		if(aContext.getDispatcher() != null)
			aContext.getDispatcher().register(this, "myevent");
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	public void operationPerformed(OperationEvent ae)
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		if(ae.getActionCommand().equals("RemoveExtendedFrame"))
		{
			this.dispose();
		}
		aModel.fireModelChanged("");
	}

	void this_componentShown(ComponentEvent e)
	{
		if(initial_init)
			init_module();
	}

	void this_internalFrameActivated(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_internalFrameOpened(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void jComboBox3_actionPerformed(ActionEvent e) {
		tt0 = (TestType )jComboBox3.getSelectedItem();
		jComboBox3.setToolTipText(tt0.getName());
		jPanel2.remove(tree);
		tree = ExtendedTree(tt0);
		tree.setCellRenderer(renderer);
		tree.addMouseListener(ml);
		jScrollPane1.getViewport().add(tree, null);
		internal_dispatcher.notify(new OperationEvent(tree.getModel(),0,"KISes_and_Ports"));
		internal_dispatcher.notify(new OperationEvent(tt0.getId(),0,"TestType"));
	}

	public JTree ExtendedTree(TestType tt)
	{
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		ObjectResourceFilter filter = new ObjectResourceDomainFilter(dsi.getSession().getDomainId());
		FilterTreeNode root = new FilterTreeNode(LangModelScheduleOld.String("labelRoot"), "");
		DataSet dSet = new DataSet(Pool.getHash(KIS.typ));
		dSet = filter.filter(dSet);
		for(Enumeration en = dSet.elements(); en.hasMoreElements();)
		{
			KIS kis = (KIS )en.nextElement();
			FilterTreeNode kisnode = new FilterTreeNode(kis.getName(), kis.getId());
			for(Enumeration enu = kis.access_ports.elements(); enu.hasMoreElements();)
			{
				AccessPort ap = (AccessPort )enu.nextElement();
				FilterTreeNode portnode = new FilterTreeNode(ap.getName(), ap.getId());
				kisnode.add(portnode);
				Hashtable htb = Pool.getHash("path");
				DataSet dSet2 = new DataSet(Pool.getHash(TransmissionPath.typ));
				dSet2 = filter.filter(dSet2);
				for(Enumeration enum = dSet2.elements(); enum.hasMoreElements();)
				{
					TransmissionPath path = (TransmissionPath )enum.nextElement();
					if(path.access_port_id.equals(ap.getId()))
					{
						FilterTreeNode pathnode = new FilterTreeNode(path.getName(), path.monitored_element_id);
						portnode.add(pathnode);
					}
				}
				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				if(apt.test_type_ids.contains(tt0.getId()))
				{
					root.add(kisnode);
				}
			}
		}
		TreeModelClone myModel = new TreeModelClone(root);
		tree = new JTree(myModel);
		tree.setRootVisible(false);
		tree.setShowsRootHandles(true);
		return tree;
	}

	void Choise(FilterTreeNode mte)
	{
		mte.setChildrenState(mte.state);
		if (mte.state == 0)
		{
			mte.state = 2;
		}
		else if (mte.state == 1 || mte.state == 2)
		{
			mte.state = 0;
		}
		if (!mte.equals(mte.getRoot()))
		{
			mte.MyGetParent(mte);
		}
	}
}

