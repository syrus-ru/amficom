package com.syrus.AMFICOM.Client.Schedule.UI;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import java.awt.Dimension;


public class MyTreeFrame extends JInternalFrame
		implements OperationListener
{
	Dispatcher internal_dispatcher;
	ScheduleMDIMain parent;
	ApplicationContext aContext;

	public static IniFile iniFile;
	static String iniFileName = "My.properties";

	boolean initial_init = true;
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JPanel jPanel1 = new JPanel();

	public boolean perform_processing = true;
	private BorderLayout borderLayout1 = new BorderLayout();

	GridBagLayout gridBagLayout1 = new GridBagLayout();

	private JLabel jLabel1 = new JLabel();
	private ObjectResourceComboBox jComboBox1 = new ObjectResourceComboBox("kis");
	private JLabel jLabel2 = new JLabel();
	private ObjectResourceComboBox jComboBox2 = new ObjectResourceComboBox();
	private JLabel jLabel3 = new JLabel();
	private ObjectResourceComboBox jComboBox3 = new ObjectResourceComboBox();//"testtype"
	private JLabel jLabel4 = new JLabel();
	private ObjectResourceComboBox jComboBox4 = new ObjectResourceComboBox();

	public MyTreeFrame(ScheduleMDIMain parent, ApplicationContext aContext)
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
		jComboBox1.setDomainId(aContext.getSessionInterface().getDomainId());
		jComboBox1.restrictToDomain(true);
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this, CatalogNavigateEvent.type);
		internal_dispatcher.register(this,"RemoveTreeFrame");
		internal_dispatcher.register(this,"Remove3aFrame");
		this.setClosable(true);
		this.setIconifiable(true);
		this.setResizable(true);
//		this.setMaximizable(true);

		this.getContentPane().setLayout(borderLayout1);
		this.setTitle(LangModelScheduleOld.String("MyTreeTitle"));
		KIS kis = (KIS )jComboBox1.getItemAt(0);
		this.setSize(new Dimension(407, 228));
		if (kis != null)
		{
			internal_dispatcher.notify(new OperationEvent(kis.getId(),0,"KISType"));
			jComboBox1.setToolTipText(kis.getName());
			jComboBox2.setContents(kis.access_ports.elements(), true);
			if (kis.access_ports.size() != 0)
			{
				AccessPort ap = ((AccessPort)jComboBox2.getItemAt(0));
				internal_dispatcher.notify(new OperationEvent( ap.getId() ,0,"PortType"));
				jComboBox2.setToolTipText(ap.getName());
				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				Hashtable ht2 = new Hashtable();
				for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						ht2.put(me.getId(), me);
					}
				}
				jComboBox4.setContents(ht2.elements(), false);
				Vector vec = new Vector();
				for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
				{
					TestType tt = (TestType )enum.nextElement();
					if(apt.test_type_ids.contains(tt.getId()))
					{
						vec.add(tt);
					}
				}
				jComboBox3.setContents(vec.elements(), false);
				MonitoredElement mone = ((MonitoredElement)jComboBox4.getItemAt(0));
				if (mone != null)
				{
					internal_dispatcher.notify(new OperationEvent( mone.getId(),0,"METype"));
					jComboBox4.setToolTipText(mone.getName());
				}
				TestType tt = ((TestType)jComboBox3.getItemAt(0));
				if (tt != null)
				{
					internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
					jComboBox3.setToolTipText(tt.getName());
				}
			}
		}

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
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		jPanel1.setLayout(gridBagLayout1);
		jScrollPane1.setAutoscrolls(true);
		jLabel1.setText(LangModelScheduleOld.String("ORKIS"));
		jLabel2.setText(LangModelScheduleOld.String("ORPort"));
		jLabel4.setText(LangModelScheduleOld.String("ORPath1"));
		jLabel3.setText(LangModelScheduleOld.String("ORTestType1"));
		jComboBox2.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jComboBox2_actionPerformed(e);
				}
			});
		jComboBox4.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					jComboBox4_actionPerformed(e);
				}
			});
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox1_actionPerformed(e);
			}
		});
		jComboBox3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox3_actionPerformed(e);
			}
		});
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jPanel1, null);
		
		jComboBox1.setPreferredSize(new Dimension(70, 20));
		jComboBox2.setPreferredSize(new Dimension(70, 20));
		jComboBox3.setPreferredSize(new Dimension(70, 20));
		jComboBox4.setPreferredSize(new Dimension(70, 20));

		jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 35, 0));
		jPanel1.add(jComboBox1, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 35, 0));
		jPanel1.add(jComboBox2, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jPanel1.add(jLabel4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 35, 0));
		jPanel1.add(jComboBox4, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jPanel1.add(jLabel3, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 35, 0));
		jPanel1.add(jComboBox3, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		jPanel1.add(Box.createVerticalGlue(), new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
//		aModel.addListener(toolBar);
//		toolBar.setModel(aModel);

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
		if(ae.getActionCommand().equals("RemoveTreeFrame"))
		{
			this.dispose();
		}
		if(ae.getActionCommand().equals("Remove3aFrame"))
		{
			this.dispose();
		}
		if(ae.getActionCommand().equals(CatalogNavigateEvent.type))
		if(perform_processing)
		{
			CatalogNavigateEvent cne = (CatalogNavigateEvent )ae;
			if(cne.CATALOG_ACCESS_PORT_SELECTED)
			{
				AccessPort[] ap = (AccessPort[] )cne.getSource();
				internal_dispatcher.notify(new OperationEvent(ap[0].getId(),0,"PortType"));
				KIS kis = (KIS )Pool.get("kis", ap[0].KIS_id);
				internal_dispatcher.notify(new OperationEvent( kis.getId(),0,"KISType"));
				jComboBox1.setSelected(kis.getId());
				jComboBox1.setToolTipText(kis.getName());
				jComboBox2.setContents(kis.access_ports.elements(), false);
				jComboBox2.setSelected(ap[0].getId());
				jComboBox2.setToolTipText(ap[0].getName());
				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap[0].type_id);
				Hashtable ht2 = new Hashtable();
				for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en.nextElement();
					if(me.access_port_id.equals(ap[0].getId()))
					{
						ht2.put(me.getId(), me);
					}
				}
				jComboBox4.setContents(ht2.elements(), false);
				MonitoredElement me = ((MonitoredElement)jComboBox4.getItemAt(0));
				if (me != null)
				{
					internal_dispatcher.notify(new OperationEvent( me.getId() ,0,"METype"));
					jComboBox4.setToolTipText(me.getName());
				}
				Vector vec = new Vector();
				for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
				{
					TestType tt = (TestType )enum.nextElement();
					if(apt.test_type_ids.contains(tt.getId()))
						vec.add(tt);
				}
				jComboBox3.setContents(vec.elements(), false);
				TestType tt = ((TestType)jComboBox3.getItemAt(0));
				if (tt != null)
				{
					internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
					jComboBox3.setToolTipText(tt.getName());
				}
			}
			if(cne.CATALOG_EQUIPMENT_SELECTED)
			{
				Equipment[] eq = (Equipment[] )cne.getSource();
				if (eq[0].is_kis)
				{
					jComboBox1.setSelected(eq[0].getId());
					internal_dispatcher.notify(new OperationEvent( eq[0].getId(),0,"KISType"));
					jComboBox1.setToolTipText(eq[0].getName());
					jComboBox2.setContents(((KIS )eq[0]).access_ports.elements(), false);
					AccessPort ap = ((AccessPort)jComboBox2.getItemAt(0));
					if (ap != null)
					{
						internal_dispatcher.notify(new OperationEvent( ap.getId() ,0,"PortType"));
						jComboBox2.setToolTipText(ap.getName());
					}

					AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
					Hashtable ht2 = new Hashtable();
					for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
					{
						MonitoredElement me = (MonitoredElement )en.nextElement();
						if(me.access_port_id.equals(ap.getId()))
						{
							ht2.put(me.getId(), me);
						}
					}
					jComboBox4.setContents(ht2.elements(), false);
					MonitoredElement me = ((MonitoredElement)jComboBox4.getItemAt(0));
					if (me != null)
					{
						internal_dispatcher.notify(new OperationEvent( me.getId() ,0,"METype"));
						jComboBox4.setToolTipText(me.getName());
					}
					Vector vec = new Vector();
					for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
					{
						TestType tt = (TestType )enum.nextElement();
						if(apt.test_type_ids.contains(tt.getId()))
							vec.add(tt);
					}
					jComboBox3.setContents(vec.elements(), false);
					TestType tt = ((TestType)jComboBox3.getItemAt(0));
					if (tt != null)
					{
						internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
						jComboBox3.setToolTipText(tt.getName());
					}
				}
				else if (!eq[0].is_kis)
				{
					MonitoredElement mone = null;
					Hashtable ht0 = Pool.getHash("monitoredelement");
					for(Enumeration en = ht0.elements(); en.hasMoreElements();)
					{
						MonitoredElement me = (MonitoredElement )en.nextElement();
						if(me.element_type.equals("equipment"))
						{
							if(me.element_id.equals(eq[0].getId()))
							{
								mone = me;
							}
						}
					}
					if(mone != null)
					{
						AccessPort ap = (AccessPort )Pool.get("accessport", mone.access_port_id);

						KIS kis = (KIS )Pool.get("kis", ap.KIS_id);

						jComboBox1.setSelected(kis.getId());
						internal_dispatcher.notify(new OperationEvent( kis.getId(),0,"KISType"));
						jComboBox1.setToolTipText(kis.getName());

						jComboBox2.setContents(kis.access_ports.elements(), true);
						jComboBox2.setSelected(ap.getId());
						internal_dispatcher.notify(new OperationEvent( ap.getId(),0,"PortType"));
						jComboBox2.setToolTipText(ap.getName());
						AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
						Hashtable ht2 = new Hashtable();
						for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
						{
							MonitoredElement me = (MonitoredElement )en.nextElement();
							if(me.access_port_id.equals(ap.getId()))
							{
								ht2.put(me.getId(), me);
							}
						}
						jComboBox4.setContents(ht2.elements(), false);
						jComboBox4.setSelected(mone.getId());
						internal_dispatcher.notify(new OperationEvent( mone.getId(),0,"METype"));
						jComboBox4.setToolTipText(mone.getName());
						Vector vec = new Vector();
						for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
						{
							TestType tt = (TestType )enum.nextElement();
							if(apt.test_type_ids.contains(tt.getId()))
								vec.add(tt);
						}
						jComboBox3.setContents(vec.elements(), false);
						TestType tt = ((TestType)jComboBox3.getItemAt(0));
						if (tt != null)
						{
							internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
							jComboBox3.setToolTipText(tt.getName());
						}
					}
				}
			}
			if(cne.CATALOG_PATH_SELECTED)
			{
				TransmissionPath[] path = (TransmissionPath[] )cne.getSource();

				MonitoredElement mone = (MonitoredElement )Pool.get("monitoredelement", path[0].monitored_element_id);
				AccessPort ap = (AccessPort )Pool.get("accessport", mone.access_port_id);

				KIS kis = (KIS )Pool.get("kis", ap.KIS_id);

				jComboBox1.setSelected(kis.getId());
				internal_dispatcher.notify(new OperationEvent( kis.getId(),0,"KISType"));
				jComboBox1.setToolTipText(kis.getName());

				jComboBox2.setContents(kis.access_ports.elements(), false);
				jComboBox2.setSelected(ap.getId());
				internal_dispatcher.notify(new OperationEvent( ap.getId(),0,"PortType"));
				jComboBox2.setToolTipText(ap.getName());

				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);

				Hashtable ht2 = new Hashtable();
				for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						ht2.put(me.getId(), me);
					}
				}

				jComboBox4.setContents(ht2.elements(), false);
				jComboBox4.setSelected(mone.getId());
				internal_dispatcher.notify(new OperationEvent( mone.getId(),0,"METype"));
				jComboBox4.setToolTipText(mone.getName());

				Vector vec = new Vector();
				for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
				{
					TestType tt = (TestType )enum.nextElement();
					if(apt.test_type_ids.contains(tt.getId()))
						vec.add(tt);
				}
				jComboBox3.setContents(vec.elements(), false);
				TestType tt = ((TestType)jComboBox3.getItemAt(0));
				if (tt != null)
				{
					internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
					jComboBox3.setToolTipText(tt.getName());
				}

			}
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

	void jComboBox1_actionPerformed(ActionEvent e) {
	KIS kis = (KIS )jComboBox1.getSelectedItem();
	internal_dispatcher.notify(new OperationEvent(kis.getId(),0,"KISType"));
	jComboBox1.setToolTipText(kis.getName());
	jComboBox2.setContents(kis.access_ports.elements(), false);
	if (kis.access_ports.size() != 0)
	{
		jComboBox2.setToolTipText(((AccessPort)jComboBox2.getItemAt(0)).getName());
	}
	jComboBox3.removeAllItems();
	jComboBox4.removeAllItems();
	perform_processing = false;
	internal_dispatcher.notify(new CatalogNavigateEvent(new KIS[] {kis},
											CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT));
	perform_processing = true;
	}

	void jComboBox2_actionPerformed(ActionEvent e)
	{
		if (!(jComboBox2.getSelectedItem() instanceof AccessPort))
			return;
		AccessPort ap = (AccessPort )jComboBox2.getSelectedItem();
		if (ap != null)
		{
			internal_dispatcher.notify(new OperationEvent(ap.getId(),0,"PortType"));
			jComboBox2.setToolTipText(ap.getName());
			AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);

			Hashtable ht2 = new Hashtable();
			for(Enumeration en = Pool.getHash("monitoredelement").elements(); en.hasMoreElements();)
			{
				MonitoredElement me = (MonitoredElement )en.nextElement();
				if(me.access_port_id.equals(ap.getId()))
				{
					ht2.put(me.getId(), me);
				}
			}
			jComboBox4.setContents(ht2.elements(), false);
			MonitoredElement me = ((MonitoredElement)jComboBox4.getItemAt(0));
			if (me != null)
			{
				internal_dispatcher.notify(new OperationEvent( me.getId() ,0,"METype"));
				jComboBox4.setToolTipText(me.getName());
			}


			Vector vec = new Vector();
			for(Enumeration enum = Pool.getHash("testtype").elements(); enum.hasMoreElements();)
			{
				TestType tt = (TestType )enum.nextElement();
				if(apt.test_type_ids.contains(tt.getId()))
					vec.add(tt);
			}
			jComboBox3.setContents(vec.elements(), false);
			TestType tt = ((TestType)jComboBox3.getItemAt(0));
			if (tt != null)
			{
				internal_dispatcher.notify(new OperationEvent( tt.getId() ,0,"TestType"));
				jComboBox3.setToolTipText(tt.getName());
			}

			perform_processing = false;
			internal_dispatcher.notify(new CatalogNavigateEvent(new AccessPort[] {ap},
					CatalogNavigateEvent.CATALOG_ACCESS_PORT_SELECTED_EVENT));
			perform_processing = true;
		}
		else
		{
			jComboBox3.removeAllItems();
			jComboBox4.removeAllItems();
		}
	}

	void jComboBox3_actionPerformed(ActionEvent e)
	{
		TestType tt = (TestType )jComboBox3.getSelectedItem();
		if (tt != null)
		{
			jComboBox3.setToolTipText(tt.getName());
			internal_dispatcher.notify(new OperationEvent(tt.getId(),0,"TestType"));
		}
	}

	void jComboBox4_actionPerformed(ActionEvent e)
	{
		MonitoredElement me = (MonitoredElement )jComboBox4.getSelectedItem();

		if(me.element_type.equals("path"))
		{
			TransmissionPath tp = (TransmissionPath )Pool.get("transmissionpath", me.element_id);
			perform_processing = false;
			internal_dispatcher.notify(new CatalogNavigateEvent(new TransmissionPath[] {tp},
					CatalogNavigateEvent.CATALOG_PATH_SELECTED_EVENT));
			perform_processing = true;
		}
		else
		if(me.element_type.equals("equipment"))
		{
			Equipment eq = (Equipment )Pool.get("equipment", me.element_id);
			perform_processing = false;
			internal_dispatcher.notify(new CatalogNavigateEvent(new Equipment[] {eq},
					CatalogNavigateEvent.CATALOG_EQUIPMENT_SELECTED_EVENT));
			perform_processing = true;
		}
		jComboBox4.setToolTipText(me.getName());
		internal_dispatcher.notify(new OperationEvent(me.getId(),0,"METype"));
	}
}

