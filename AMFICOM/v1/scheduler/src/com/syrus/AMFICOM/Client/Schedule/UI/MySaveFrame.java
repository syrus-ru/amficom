package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MySaveFrame extends JInternalFrame
		implements OperationListener
{
	Dispatcher internal_dispatcher;
	ApplicationContext aContext;
	ScheduleMDIMain parent;
	String meid = null;

	public static IniFile iniFile;
	static String iniFileName = "My.properties";
	boolean initial_init = true;
	private JScrollPane jScrollPane1 = new JScrollPane();
	private JPanel jPanel1 = new JPanel();
	private ButtonGroup group = new ButtonGroup();
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private JLabel jLabel4 = new JLabel();
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout3 = new BorderLayout();
	private JLabel jLabel2 = new JLabel();
	private JPanel jPanel3 = new JPanel();
	private JPanel jPanel4 = new JPanel();
	private BorderLayout borderLayout4 = new BorderLayout();
	private JLabel jLabel1 = new JLabel();
	private JPanel jPanel5 = new JPanel();
	private BorderLayout borderLayout5 = new BorderLayout();
	private JRadioButton jButton3 = new JRadioButton();
	private JPanel jPanel6 = new JPanel();
	private JRadioButton jButton2 = new JRadioButton();
	private JRadioButton jButton1 = new JRadioButton();
	private BorderLayout borderLayout6 = new BorderLayout();
	private JPanel jPanel7 = new JPanel();
	private BorderLayout borderLayout7 = new BorderLayout();
	private ObjectResourceComboBox jComboBox1 = new ObjectResourceComboBox(AnalysisType.typ, true);
	private JPanel jPanel8 = new JPanel();
	private BorderLayout borderLayout8 = new BorderLayout();
	private ObjectResourceComboBox jComboBox2 = new ObjectResourceComboBox(EvaluationType.typ, true);
	private JLabel jLabel3 = new JLabel();
	private BorderLayout borderLayout9 = new BorderLayout();

	public MySaveFrame(ScheduleMDIMain parent, ApplicationContext aContext)
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
		jLabel3.setText(LangModelScheduleOld.String("labelMakeEvalAnalysis"));
		jComboBox2.setEnabled(false);
		jComboBox2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox2_actionPerformed(e);
			}
		});
		jPanel8.setLayout(borderLayout8);
		jComboBox1.setEnabled(false);
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox1_actionPerformed(e);
			}
		});
		jPanel7.setLayout(borderLayout7);
		jButton1.setText(LangModelScheduleOld.String("labelAllTestResults"));
		jButton1.setVerticalAlignment(SwingConstants.TOP);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		jButton1.setSelected(true);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton2_actionPerformed(e);
			}
		});
		jButton2.setText(LangModelScheduleOld.String("labelKnownEvents"));
		jPanel6.setBorder(BorderFactory.createEtchedBorder());
		jPanel6.setLayout(borderLayout9);
		jButton3.setText(LangModelScheduleOld.String("labelIdIzmer"));
		jButton3.setVerticalAlignment(SwingConstants.BOTTOM);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton3_actionPerformed(e);
			}
		});
		jPanel5.setLayout(borderLayout5);
		jLabel1.setText(LangModelScheduleOld.String("labelSaveResBD"));
		jPanel4.setLayout(borderLayout4);
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		jPanel3.setLayout(borderLayout6);
		jLabel2.setText(LangModelScheduleOld.String("labelMakeAnalysis"));
		jPanel2.setLayout(borderLayout3);
		jLabel4.setText(LangModelScheduleOld.String("labelDataProcParam"));
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"TestSetup");
		internal_dispatcher.register(this,"StopAnalysis");
		internal_dispatcher.register(this,"METype");
		internal_dispatcher.register(this,"VisualTestSetup");
		internal_dispatcher.register(this,"ExtendedAfterUsual_RootFrame");
		this.setResizable(true);
		this.getContentPane().setLayout(borderLayout1);
		jPanel1.setBorder(BorderFactory.createEtchedBorder());
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"RemoveSaveFrame");
		internal_dispatcher.register(this,"Remove3aFrame");
		this.setClosable(true);
		this.setIconifiable(true);
		group.add(jButton1);
		group.add(jButton2);
		group.add(jButton3);
//		this.setMaximizable(true);

		this.setTitle(LangModelScheduleOld.String("MySaveTitle"));

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
		jPanel1.setLayout(borderLayout2);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jPanel1, null);
		jPanel1.add(jLabel4,  BorderLayout.NORTH);
		jPanel1.add(jPanel2, BorderLayout.CENTER);
		jPanel3.add(jLabel2,  BorderLayout.NORTH);
		jPanel3.add(jPanel7,  BorderLayout.CENTER);
		jPanel7.add(jComboBox1, BorderLayout.NORTH);
		jPanel7.add(jPanel8,  BorderLayout.CENTER);
		jPanel8.add(jComboBox2, BorderLayout.CENTER);
		jPanel8.add(jLabel3,  BorderLayout.NORTH);
		jPanel2.add(jPanel4, BorderLayout.CENTER);
		jPanel4.add(jLabel1, BorderLayout.NORTH);
		jPanel4.add(jPanel5, BorderLayout.CENTER);
		jPanel5.add(jPanel6, BorderLayout.NORTH);
		jPanel6.add(jButton1, BorderLayout.NORTH);
		jPanel6.add(jButton3, BorderLayout.SOUTH);
		jPanel6.add(jButton2, BorderLayout.CENTER);
		jPanel2.add(jPanel3,  BorderLayout.NORTH);
		jComboBox1.setSelected("");
		jComboBox2.setSelected("");
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
		if(ae.getActionCommand().equals("VisualTestSetup"))
		{
			Test par_test = (Test)ae.getSource();
			if (par_test.analysis_id.equals(""))
			{
				jComboBox1.setSelected("");
			}
			if (par_test.evaluation_id.equals(""))
			{
				jComboBox2.setSelected("");
			}
		}
		if(ae.getActionCommand().equals("TestSetup"))
		{
			jComboBox1.setEnabled(true);
			jComboBox2.setEnabled(true);
			TestSetup ts = (TestSetup )ae.getSource();

			DataSet ds_a = new DataSet();
			ds_a.add((ObjectResource )Pool.get(AnalysisType.typ, ts.analysis_type_id));

			DataSet ds_e = new DataSet();
			ds_e.add((ObjectResource )Pool.get(EvaluationType.typ, ts.evaluation_type_id));

			jComboBox1.setContents(ds_a.elements(), true);
			jComboBox2.setContents(ds_e.elements(), true);

			if (ds_a.size()>0)
				 jComboBox1.setSelected(ts.analysis_type_id);
			if (ds_e.size()>0)
				 jComboBox2.setSelected(ts.evaluation_type_id);
			internal_dispatcher.notify(new OperationEvent(ts.getId(),0,"TestSetupChoise"));
		}
		if(ae.getActionCommand().equals("METype") || ae.getActionCommand().equals("TestType") ||
			 ae.getActionCommand().equals("StopAnalysis") || ae.getActionCommand().equals("ExtendedAfterUsual_RootFrame"))
		{
			meid = ae.getSource().toString();
			jComboBox1.setSelected("");
			jComboBox2.setSelected("");
			internal_dispatcher.notify(new OperationEvent("",0,"AnalysisType"));
			internal_dispatcher.notify(new OperationEvent("",0,"EvaluationType"));
			internal_dispatcher.notify(new OperationEvent("",0,"TestSetupChoise"));
			jComboBox1.setEnabled(false);
			jComboBox2.setEnabled(false);
		}
		if(ae.getActionCommand().equals("RemoveSaveFrame"))
		{
			this.dispose();
		}
		if(ae.getActionCommand().equals("Remove3aFrame"))
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

	void jButton1_actionPerformed(ActionEvent e) {
		internal_dispatcher.notify(new OperationEvent(TestReturnType.TEST_RETURN_TYPE_WHOLE,0,"TestReturnType"));
	}

	void jButton2_actionPerformed(ActionEvent e) {
		internal_dispatcher.notify(new OperationEvent(TestReturnType.TEST_RETURN_TYPE_EVENTS,0,"TestReturnType"));
	}

	void jButton3_actionPerformed(ActionEvent e) {
		internal_dispatcher.notify(new OperationEvent(TestReturnType.TEST_RETURN_TYPE_REFERENCE,0,"TestReturnType"));
	}

	void jComboBox1_actionPerformed(ActionEvent e)
	{
		String val = (String )jComboBox1.getSelectedId();
		if(val == null)
			val = "";
		internal_dispatcher.notify(new OperationEvent(val,0,"AnalysisType"));
	}

	void jComboBox2_actionPerformed(ActionEvent e) {
		String val = (String )jComboBox2.getSelectedId();
		if(val == null)
			val = "";
		internal_dispatcher.notify(new OperationEvent(val,0,"EvaluationType"));
	}
}