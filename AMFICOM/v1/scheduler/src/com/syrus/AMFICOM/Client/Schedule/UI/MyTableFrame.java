package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Schedule.Sorter.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class MyTableFrame extends JInternalFrame
		implements OperationListener,
		ListSelectionListener
{
	Dispatcher internal_dispatcher;
	ApplicationContext aContext;
	ScheduleMDIMain parent;
	DataSet baza_test_new_table = new DataSet();
	DataSet baza_test_table = new DataSet();
	DataSet tableContents = new DataSet();
	String sort_type = "start_time";
	int sort_dir = 1;


	public static IniFile iniFile;
	static String iniFileName = "Table.properties";

	boolean initial_init = true;

	ObjectResourceTablePane listPane = new ObjectResourceTablePane();
	MouseListener ml;
	MouseListener ml_header;
	JTable jt;
	JTableHeader jth;

	public MyTableFrame(ScheduleMDIMain parent, ApplicationContext aContext)
	{
		super();
		this.aContext = aContext;
		this.parent = parent;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
		listPane.initialize(new TestDisplayModel(), new DataSet());
	}

	private void jbInit() throws Exception
	{
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_mousePressed (e);
			}
		};
		ml_header = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_header_mousePressed (e);
			}
		};
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"TestChanged");
		internal_dispatcher.register(this,"NewTestChanged");
		internal_dispatcher.register(this,"SelectedButton");
		internal_dispatcher.register(this,"Filtration_parameters");
		this.setTitle(LangModelScheduleOld.String("MyTableTitle"));

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
		listPane.addListSelectionListener(this);
		jt = listPane.getTable();
		jth = jt.getTableHeader();
		jt.addMouseListener(ml);
		jth.addMouseListener(ml_header);
		jt.setSelectionMode(2);

/*
		JTable jTable = listPane.getTable();
		jTable.setDefaultRenderer(
				ObjectResource.class,
				new MyTableRenderer(listPane));
		testTableInit(jTable);

		jTable.setDefaultRenderer(
				Integer.class,
				new MyTableRenderer(listPane));
*/
		this.getContentPane().add(listPane, BorderLayout.CENTER);
	}

	public void init_module()
	{
		initial_init = false;
		System.out.println("this file ");
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

//		aModel.setCommand("menuMapNew", new MapNewCommand(this));
//		aModel.setCommand("menuMapClose", new MapCloseCommand(this));
//		aModel.setCommand("menuMapOpen", new MapOpenCommand((JDesktopPane )this.getParent(), this, aContext));
//		aModel.setCommand("menuMapSave", new MapSaveCommand((JDesktopPane )this.getParent(), this, aContext));
//		aModel.setCommand("menuExit", new ExitCommand(this));
		//aModel.add("menuHelpAbout", new HelpAboutCommand(this, new ConfigureMDIMain_AboutBoxPanel1()));

//		aModel.setEnabled("menuExit", true);
//		aModel.setEnabled("menuHelp", true);
//		aModel.setEnabled("menuHelpAbout", true);
		//baza_test_table = (new TestSorter(baza_test_table)).sort(sort_type, sort_dir);
		//updateContents();

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
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());
		//aContext.getDispatcher().register(this, "alarmreceived");
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
//		aModel.addListener(toolBar);
//		toolBar.setModel(aModel);

//		aModel.fireModelChanged("");
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
		if(ae.getActionCommand().equals("NewTestChanged"))
		{
			baza_test_new_table = new DataSet();
			DataSet baza_test_table_temp = (DataSet)ae.getSource();
			baza_test_new_table.add(baza_test_table_temp);
			baza_test_new_table = new TestSorter(baza_test_new_table).sort(sort_type, sort_dir);
			updateContents(null);
		}
		if(ae.getActionCommand().equals("TestChanged"))
		{
			baza_test_table = new DataSet();
			DataSet baza_test_table_temp = (DataSet)ae.getSource();
			baza_test_table.add(baza_test_table_temp);
			baza_test_table = new TestSorter(baza_test_table).sort(sort_type, sort_dir);
			updateContents(null);
		}
		if(ae.getActionCommand().equals("Filtration_parameters"))
		{
			baza_test_new_table = new DataSet();
			DataSet baza_test_table_temp = (DataSet)ae.getSource();
			baza_test_table.add(baza_test_table_temp);
			baza_test_table = new TestSorter(baza_test_table).sort(sort_type, sort_dir);
			updateContents(null);
		}
		if(ae.getActionCommand().equals("SelectedButton"))
		{
			NewButton nb = (NewButton) ae.getSource();
			Test test = (Test )Pool.get("test", nb.testid);
			listPane.setSelected(test);
			jt.scrollRectToVisible( jt.getCellRect( jt.getSelectedRow(), jt.getSelectedColumn(), true) );
		}
	}
/*
	void testTableInit(JTable jTable)
	{
		String[] l_colHeadings = new String [] { "a", "b" };
		Object[] l_defaultValues = new Object[l_colHeadings.length];
		for(int i = l_colHeadings.length; i > 0; i--)
			l_defaultValues[i - 1] = "Test";

		testTableModel = new MyObjectResourceTableModel(l_colHeadings, l_defaultValues, 0);
		jTable.setModel(testTableModel);
		jTable.doLayout();
	}
*/
	public void updateContents(Test temp)
	{
		Vector vec = new Vector();
		DataSet tests = new DataSet();
		tests.add(baza_test_new_table);
		tests.add(baza_test_table);
		for (int i = 0; i < tests.size(); i++)
		{
			Test tt = (Test) tests.get(i);
			vec.addElement(tt);
			if (tt.equals(temp) || tableContents.get(tt.getId()) != null)
			{
				ObjectResource or = tableContents.get(tt.getId());
				if (or == null || !tt.equals(temp))
				{
					if (tt.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
					{
						long start = tt.start_time;
						ElementaryTest et = new ElementaryTest(tt, start);
						et.count = 1;
						vec.addElement(et);
					}
					else if (tt.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
					{
						long start =tt.start_time;
						long end = tt.time_stamps.ptpars().end_time;
						long interval = tt.time_stamps.ptpars().dt;
						long temp_time = interval;
						int iii = 1;
						ElementaryTest et = new ElementaryTest(tt, start);
						et.count = iii;
						iii++;
						vec.addElement(et);
						while(start + temp_time <= end)
						{
							et = new ElementaryTest(tt, start + temp_time);
							et.count = iii;
							iii++;
							vec.addElement(et);
							temp_time+=interval;
						}
					}
					else if (tt.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
					{
						long start = tt.start_time;
						long time_massiv[] = tt.time_stamps.ti();
						int iii = 1;
						ElementaryTest et = new ElementaryTest(tt, start);
						et.count = iii;
						iii++;
						vec.addElement(et);
						for (int j = 0; j < time_massiv.length; j++)
						{
							et = new ElementaryTest(tt, time_massiv[j]);
							et.count = iii;
							iii++;
							vec.addElement(et);
						}
					}
				}

				if (or == null && tt.equals(temp))
				{
					tableContents.add(temp);
				}
				else if (or != null && tt.equals(temp))
				{
					tableContents.remove(or);
				}
			}
		}
		listPane.setContents(new DataSet(vec));
		listPane.updateUI();
	}

	public void valueChanged(ListSelectionEvent e)
	{
/*
		JTable jTable = listPane.getTable();
		int index = jTable.getSelectedRow();
		//Test test = (Test )testTableModel.getObjectByIndex(index);
		//SystemEvent event = (SystemEvent)Pool.get("event", test.getName());

		//Pool.put("activecontext", "useractionselected", "evaluation_selected");
		//Pool.put("activecontext", "selected_id", event.descriptor);

		if (aContext.getDispatcher() != null)
			aContext.getDispatcher().notify(new OperationEvent(this, 0, "activecontextevent"));
*/
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

	void this_header_mousePressed(MouseEvent e)
	{
		JTableHeader jth = (JTableHeader )e.getSource();
		TableColumn tc = jth.getDraggedColumn();
		if (tc != null)
		{
			String str = (String )tc.getHeaderValue();

			int index = jt.getColumnModel().getColumnIndex(tc.getIdentifier());
			index = jt.convertColumnIndexToModel(index);
			ObjectResourceTableModel ortm = (ObjectResourceTableModel )jt.getModel();
			sort_type = ortm.getColumnByNumber(index);

			for (int i = 0; i < jt.getColumnCount(); i++)
			{
				String sss = (String )jt.getColumnModel().getColumn(i).getHeaderValue();
				if (jt.getColumnModel().getColumn(i).equals(tc))
				{
					if (str.substring(0,1).equals(">"))
					{
						tc.setHeaderValue("< " + str.substring(2));
						sort_dir = 0;
						baza_test_table = new TestSorter(baza_test_table).sort(sort_type, sort_dir);
						updateContents(null);
					}
					else if (str.substring(0,1).equals("<"))
					{
						tc.setHeaderValue("> " + str.substring(2));
						sort_dir = 1;
						baza_test_table = new TestSorter(baza_test_table).sort(sort_type, sort_dir);
						updateContents(null);
					}
					else
					{
						sort_dir = 1;
						tc.setHeaderValue("> " + str);
						baza_test_table = new TestSorter(baza_test_table).sort(sort_type, sort_dir);
						updateContents(null);
					}
				}
				else if (sss.substring(0,1).equals(">") || sss.substring(0,1).equals("<"))
				{
					jt.getColumnModel().getColumn(i).setHeaderValue(sss.substring(2));
				}
			}
		}
	}

	void this_mousePressed(MouseEvent e)
	{
		Object o = listPane.getObjectAt(e.getY() / jt.getRowHeight());
		int count[] = jt.getSelectedRows();
		Vector ob = new Vector();
		for (int i = 0; i < count.length; i++)
		{
			if (listPane.getObjectAt(count[i]) instanceof Test)
				ob.add(listPane.getObjectAt(count[i]));
		}

		if (o instanceof Test)
		{
			Test test = (Test )o;
			System.out.println("Object " + test.toString());
			internal_dispatcher.notify(new OperationEvent(test.test_type_id,0,"TestType"));
			internal_dispatcher.notify(new OperationEvent(test,0,"VisualTestParams"));
			internal_dispatcher.notify(new OperationEvent(String.valueOf(test.start_time),0,"NavigationTime"));
			if (SwingUtilities.isLeftMouseButton(e))
			{
				if (e.getClickCount() == 2)
				{
					updateContents(test);
				}
			}
		}
		else if (o instanceof ElementaryTest)
		{
			ElementaryTest etest = (ElementaryTest )o;
			System.out.println("Object " + etest.toString());
			internal_dispatcher.notify(new OperationEvent(etest.test.test_type_id,0,"TestType"));
			internal_dispatcher.notify(new OperationEvent(etest.test,0,"VisualTestParams"));
			internal_dispatcher.notify(new OperationEvent(String.valueOf(etest.et_time),0,"NavigationTime"));
		}
		if (SwingUtilities.isRightMouseButton(e) && ob.size() > 0)
		{
			JPopupMenu popup = new JPopupMenu();
			JMenuItem delete_test = new JMenuItem(LangModelScheduleOld.String("labelPopupDel"));
			delete_test.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delete_test_actionPerformed(e);
				}
			});
			popup.add(delete_test);
			popup.show(jt,e.getX(),e.getY());
		}
	}

	void delete_test_actionPerformed(ActionEvent e)
	{
		int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), LangModelScheduleOld.String("labelDelTestRealQ"),
				LangModelScheduleOld.String("labelDelTestReal"), JOptionPane.YES_NO_OPTION);
		if (temp == JOptionPane.YES_OPTION)
			 delete_test_func();
	}

	void delete_test_func()
	{
		int count[] = jt.getSelectedRows();
		Vector ob = new Vector();
		for (int i = 0; i < count.length; i++)
		{
			if (listPane.getObjectAt(count[i]) instanceof Test)
			{
				ob.add(listPane.getObjectAt(count[i]));
			}
		}
		Vector test_ids = new Vector();
		Vector new_test_ids = new Vector();
		for (int i = 0; i < ob.size(); i++)
		{
			Test temp_test = (Test )Pool.get("test", ((Test )ob.elementAt(i)).getId());
			if (temp_test != null)
			{
				String stroka = ((Test )ob.elementAt(i)).getId();
				test_ids.add(stroka);
				baza_test_table.remove(stroka);
				Pool.remove("test",stroka);
			}
			else
			{
				String stroka = ((Test )ob.elementAt(i)).getId();
				new_test_ids.add(stroka);
				baza_test_new_table.remove(stroka);
			}
		}

		//String test_ids[] = new String[ob.size()];
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		dataSource.RemoveTests((String[])test_ids.toArray(new String[test_ids.size()]));
		internal_dispatcher.notify(new OperationEvent(new_test_ids,0,"PlanRefresh"));
		updateContents(null);
	}
}

