package com.syrus.AMFICOM.Client.Schedule.UI;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.io.IniFile;
import com.syrus.util.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import oracle.jdeveloper.layout.*;

public class MyPlanFrame extends JInternalFrame
		implements OperationListener
{
	Dispatcher internal_dispatcher;
	ApplicationContext aContext;
	String[] test_plan_diskret = {LangModelScheduleOld.String("DetHour"),
		LangModelScheduleOld.String("DetDay"),LangModelScheduleOld.String("DetMonth")};
	ScheduleMDIMain parent;
	public static IniFile iniFile;
	static String iniFileName = "Schedule.properties";
	boolean initial_init = true;
	SimpleDateFormat test_sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	Test temp_test = new Test(""); //текущий тест
	int testing_id = 0;  //тип тестирования - 0-обычное, 1-расширенное, 2-фильтрация
	long t_sys = 0;     //время задержки сети (для подсчета длительности выполнения теста)
	long start_time;    //время начала теста (не элементарного)
	long end_time;      //время окончания теста
	long interval;      //интервал между элементарными тестами
	long warm_up_time = 0;         //время включения порта (для подсчета длительности выполнения теста)
	long cool_down_time = 0;       //время выключения порта (для подсчета длительности выполнения теста)
	int buttonX;        //координаты последнего нажатия мыши
	int buttonY;
	String number_of_kises = "All"; //в окне "План-график" отображать все КИСы или текущий
	int test_time_long = 8;         //длительность теста в пикселах
	int test_time_long_const = 8;   //длительность теста в пикселах по умолчанию
	int shag = 0;   //Очередность задания теста
	TestTemporalType ttt;         //временной тип тестирования
	Dimension button_size = new Dimension(24,24); //размер кнопок на тулбаре
	String test_type_id = "";       //id текущего типа теста
	String kis_id = "";             //id текущего КИСа
	String port_id = "";            //id текущего порта
	String me_id = "";              //id текущего MonitoredElement
	String test_setup_id = "";      //id текущего TestSetup
	String analysis_type_id = "";   //id текущего типа анализа
	String evaluation_type_id = ""; //id текущего типа оценки
	TestReturnType returntype = TestReturnType.TEST_RETURN_TYPE_WHOLE;        //параметр сохранения (распознанные события в БД сохранять или всю рефлект.)
	Vector new_parameters = new Vector();   //физические параметры теста
	DataSet baza_test = new DataSet();      //тесты, сохраненные в БД
	DataSet baza_test_new = new DataSet();  //тесты, не сохраненные в БД
	DataSet filter_test = new DataSet();    //отфильтрованные тесты
	Vector tps_panel = new Vector();        //набор панелей для тестов
	Vector button_global_test = new Vector();       //кнопки, соответствующие тестам
	Vector button_elementary_test = new Vector();   //кнопки, соответствующие элементарным тестам
	Vector temp_times = new Vector();   //времена элементарных тестов для теста по расписанию
	TreeModelClone KISes_and_ports;     //набор КИСов и портов при расширенном тестировании

	JToolBar jToolBar1 = new JToolBar();
	JToggleButton OneTimeButton = new JToggleButton();  //кнопки тулбара
	JToggleButton TimeTableButton = new JToggleButton();
	JToggleButton PeriodicButton = new JToggleButton();
	JToggleButton jButtonAllKISes = new JToggleButton();
	JButton AddTestButton = new JButton();
	JButton SaveTestsButton = new JButton();
	JToggleButton KISChoise1Button = new JToggleButton();
	AComboBox jComboBox8 = new AComboBox(test_plan_diskret);    //детализация временная
	JScrollPane jScrollPane1 = new JScrollPane();
	JPanel jPanel1 = new JPanel();
	Object comp;   //последний выбранный объект на план-графике
	Object edit_comp;          //редактируемый объект
	MouseListener ml;
	MouseListener ml_spin;
	XYLayout xYLayout1 = new XYLayout();
	JButton jButton5 = new JButton();
	JToggleButton ExtendedTestingButton = new JToggleButton();
	JToggleButton FiltrationButton = new JToggleButton();
	JToggleButton UsualTestingButton = new JToggleButton();
	JToggleButton KISChoise2Button = new JToggleButton();
	Checker checker;

	ButtonGroup bg1 = new ButtonGroup();
	ButtonGroup bg2 = new ButtonGroup();
	ButtonGroup bg3 = new ButtonGroup();

	private JMenuBar jMenuBar1 = new JMenuBar();
	JCheckBoxMenuItem onetimetest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuOmeTimeTest"));
	JCheckBoxMenuItem timetabletest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuTimeTableTest"));
	JCheckBoxMenuItem periodictest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuPeriodicalTest"));
	JCheckBoxMenuItem addtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuAddTest"));
	JCheckBoxMenuItem refreshtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuRefresh"));
	JCheckBoxMenuItem savetest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuSaveTest"));
	JCheckBoxMenuItem KISChoise1 = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuKISChoise1"));
	JCheckBoxMenuItem KISChoise2 = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuKISChoise2"));
	JCheckBoxMenuItem originaltest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuOriginalTest"));
	JCheckBoxMenuItem extendedtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuExtendedTest"));
	JCheckBoxMenuItem filtertest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuFilterTest"));

	JMenu menuParametersTesting = new JMenu(LangModelScheduleOld.String("menuParametersTesting"));
	JMenu menuOperationsTesting = new JMenu(LangModelScheduleOld.String("menuOperationsTesting"));
	JMenu menuTypeTesting = new JMenu(LangModelScheduleOld.String("menuTypeTesting"));
	SpinnerDateModel sdm = new SpinnerDateModel();
	JSpinner jSpin1 = new JSpinner(sdm);
	JFormattedTextField jTextField1;
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JSlider jSlider2 = new JSlider(-31,31,0);

	public MyPlanFrame(ScheduleMDIMain parent, ApplicationContext aContext)
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
		this.checker = new Checker(this.aContext.getDataSourceInterface());
	}

	private void jbInit() throws Exception
	{
		jComboBox8.setMinimumSize(new Dimension(150, 21));
		jComboBox8.setSelectedIndex(1);
		jComboBox8.setPreferredSize(new Dimension(200, 21));
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		javax.swing.JSpinner.DateEditor de = (javax.swing.JSpinner.DateEditor )jSpin1.getEditor();
		jTextField1 = de.getTextField();
		jTextField1.setEditable(true);
		DateFormatter formatter = new DateFormatter(test_sdf);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		jTextField1.setFormatterFactory(factory);

		jSlider2.createStandardLabels(1);
		jSlider2.setMajorTickSpacing(1);
		jSlider2.setPaintLabels(true);
		jSlider2.setPaintTicks(true);
		jSlider2.setSnapToTicks(true);

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		temp_test.id = dataSource.GetUId("test");
		OneTimeButton.setSelected(true);
		onetimetest.setSelected(true);
		UsualTestingButton.setSelected(true);
		KISChoise2.setSelected(true);
		KISChoise2Button.setSelected(true);
		originaltest.setSelected(true);
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"AskTimeTestType");
		internal_dispatcher.register(this,"TestSetupChoise");
		internal_dispatcher.register(this,"NavigationTime");
		internal_dispatcher.register(this,"EditTime");
		internal_dispatcher.register(this,"TestReturnType");
		internal_dispatcher.register(this,"AnalysisType");
		internal_dispatcher.register(this,"EvaluationType");
		internal_dispatcher.register(this,"TestType");
		internal_dispatcher.register(this,"KISType");
		internal_dispatcher.register(this,"PortType");
		internal_dispatcher.register(this,"PlanRefresh");
		internal_dispatcher.register(this,"METype");
		internal_dispatcher.register(this,"Parameter");
		internal_dispatcher.register(this,"StartTime");
		internal_dispatcher.register(this,"EndTime");
		internal_dispatcher.register(this,"Interval");
		internal_dispatcher.register(this,"TesttTime");
		internal_dispatcher.register(this,"KISes_and_Ports");
		internal_dispatcher.register(this,"Filtration_parameters");
		OneTimeButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/one_test.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		OneTimeButton.setToolTipText(LangModelScheduleOld.ToolTip("OmeTimeTest"));
		OneTimeButton.setPreferredSize(button_size);
		OneTimeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			OneTimeButton_actionPerformed(e);
			}
		});
		TimeTableButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/noperiodic_test.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		TimeTableButton.setToolTipText(LangModelScheduleOld.ToolTip("TimeTableTest"));
		TimeTableButton.setPreferredSize(button_size);
		TimeTableButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			TimeTableButton_actionPerformed(e);
			}
		});
		PeriodicButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/periodic_test.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		PeriodicButton.setToolTipText(LangModelScheduleOld.ToolTip("PeriodicalTest"));
		PeriodicButton.setPreferredSize(button_size);
		PeriodicButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			PeriodicButton_actionPerformed(e);
			}
		});
		AddTestButton.setToolTipText(LangModelScheduleOld.ToolTip("AddTest"));
		AddTestButton.setPreferredSize(button_size);
		AddTestButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addTest.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		AddTestButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.addTests))
				{
					AddTestButton_actionPerformed(e);
				}
			}
		});
		jButton5.setToolTipText(LangModelScheduleOld.ToolTip("Refresh"));
		jButton5.setPreferredSize(button_size);
		jButton5.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/refresh.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		jButton5.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton5_actionPerformed(e);
			}
		});
		SaveTestsButton.setToolTipText(LangModelScheduleOld.ToolTip("SaveTest"));
		SaveTestsButton.setPreferredSize(button_size);
		SaveTestsButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addTestBD.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		SaveTestsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.saveTests))
				{
					aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestSave")));
					SaveTestsButton_actionPerformed(e);
					aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestSaveFinish")));
				}
			}
		});
		KISChoise1Button.setToolTipText(LangModelScheduleOld.ToolTip("KISChoise1"));
		KISChoise1Button.setPreferredSize(button_size);
		KISChoise1Button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/OneKIS.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		KISChoise1Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setOneKIStest))
				{
					KISChoise1Button_actionPerformed(e);
				}
			}
		});
		ExtendedTestingButton.setToolTipText(LangModelScheduleOld.ToolTip("AutomatTest"));
		ExtendedTestingButton.setPreferredSize(button_size);
		ExtendedTestingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testir2.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		ExtendedTestingButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setExtendedTest))
				{
					ExtendedTestingButton_actionPerformed(e);
				}
			}
		});
		FiltrationButton.setToolTipText(LangModelScheduleOld.ToolTip("FilterTest"));
		FiltrationButton.setPreferredSize(button_size);
		FiltrationButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/filter.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		FiltrationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setTestFiltration))
				{
					FiltrationButton_actionPerformed(e);
				}
			}
		});
		UsualTestingButton.setToolTipText(LangModelScheduleOld.ToolTip("OriginalTest"));
		UsualTestingButton.setPreferredSize(button_size);
		UsualTestingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testir1.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		UsualTestingButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setUsualTest))
				{
					UsualTestingButton_actionPerformed(e);
				}
			}
		});
		KISChoise2Button.setToolTipText(LangModelScheduleOld.ToolTip("KISChoise2"));
		KISChoise2Button.setPreferredSize(button_size);
		KISChoise2Button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/AllKISs.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		KISChoise2Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setAllKIStest))
				{
					KISChoise2Button_actionPerformed(e);
				}
			}
		});
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		setSize(new Dimension(749, 603));
		this.setTitle(LangModelScheduleOld.String("MyPlanTitle"));

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

		jPanel1.setLayout(xYLayout1);
		jComboBox8.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox8_actionPerformed(e);
			}
		});
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				jSpin1_mousePressed(e);
			}
		});
		jPanel2.setLayout(borderLayout1);
		jPanel2.setBorder(BorderFactory.createEtchedBorder());
		jToolBar1.add(OneTimeButton, null);
		jToolBar1.add(TimeTableButton, null);
		jToolBar1.add(PeriodicButton, null);
		jToolBar1.addSeparator();
		jToolBar1.add(AddTestButton, null);
		jToolBar1.add(SaveTestsButton, null);
		jToolBar1.addSeparator();
		jToolBar1.add(KISChoise1Button, null);
		jToolBar1.add(KISChoise2Button, null);
		jToolBar1.addSeparator();
		jToolBar1.add(UsualTestingButton, null);
		jToolBar1.add(ExtendedTestingButton, null);
		jToolBar1.addSeparator();
		jToolBar1.add(FiltrationButton, null);
		jToolBar1.addSeparator();
		jToolBar1.add(jComboBox8, null);
		jToolBar1.addSeparator();
		jToolBar1.add(jSpin1, null);
		jToolBar1.addSeparator();
		jToolBar1.add(jButton5, null);
		this.getContentPane().add(jPanel2,  BorderLayout.SOUTH);
		jPanel2.add(jSlider2,  BorderLayout.NORTH);
		ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_mousePressed (e);
			}
		};
		jTextField1.addMouseListener(ml_spin);
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		this.getContentPane().add(jToolBar1, BorderLayout.NORTH);
		jScrollPane1.getViewport().add(jPanel1, null);
		ttt = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;

		jMenuBar1.add(menuOperationsTesting);
		jMenuBar1.add(menuParametersTesting);
		jMenuBar1.add(menuTypeTesting);

		menuOperationsTesting.add(addtest);
		menuOperationsTesting.addSeparator();
		menuOperationsTesting.add(savetest);
		menuOperationsTesting.addSeparator();
		menuOperationsTesting.add(refreshtest);

		menuParametersTesting.add(onetimetest);
		menuParametersTesting.add(timetabletest);
		menuParametersTesting.add(periodictest);
		menuParametersTesting.addSeparator();
		menuParametersTesting.add(KISChoise1);
		menuParametersTesting.add(KISChoise2);

		menuTypeTesting.add(originaltest);
		menuTypeTesting.add(extendedtest);
		menuTypeTesting.addSeparator();
		menuTypeTesting.add(filtertest);

		refreshtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				jButton5_actionPerformed(e);
				}
			});
		addtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				AddTestButton_actionPerformed(e);
				}
			});
		savetest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveTestsButton_actionPerformed(e);
				}
			});
		onetimetest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OneTimeButton_actionPerformed(e);
				}
			});
		timetabletest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				TimeTableButton_actionPerformed(e);
				}
			});
		periodictest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				PeriodicButton_actionPerformed(e);
				}
			});
		KISChoise1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				KISChoise1Button_actionPerformed(e);
				}
			});
		KISChoise2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				KISChoise2Button_actionPerformed(e);
				}
			});
		originaltest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				UsualTestingButton_actionPerformed(e);
				}
			});
		extendedtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				ExtendedTestingButton_actionPerformed(e);
				}
			});
		filtertest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				FiltrationButton_actionPerformed(e);
				}
			});
		this.setJMenuBar(jMenuBar1);

		bg1.add(ExtendedTestingButton);
		bg1.add(FiltrationButton);
		bg1.add(UsualTestingButton);

		bg2.add(KISChoise1Button);
		bg2.add(KISChoise2Button);

		bg3.add(OneTimeButton);
		bg3.add(TimeTableButton);
		bg3.add(PeriodicButton);
	}

	void this_mousePressed (MouseEvent e)
	{
		this.comp = e.getSource();
		buttonX = e.getX();
		buttonY = e.getY();
		if ((comp instanceof NewButton) && ((NewButton)comp).baza_type.equals("BazaTest"))
		{
			Test per_test = (Test )Pool.get("test", ((NewButton )comp).testid);
			internal_dispatcher.notify(new OperationEvent(per_test.test_type_id,0,"TestType"));
			internal_dispatcher.notify(new OperationEvent(per_test,0,"VisualTestParams"));
			internal_dispatcher.notify(new OperationEvent(comp,0,"SelectedButton"));
		}
		if (SwingUtilities.isRightMouseButton(e))
		{
			if (comp instanceof NewButton)
			{
				JPopupMenu popup = new JPopupMenu();
				JMenuItem edit_test = new JMenuItem(LangModelScheduleOld.String("labelPopupEdit"));
				Calendar plan_ca1 = Calendar.getInstance();
				if (drop_coef(buttonX + ((NewButton )comp).getX()) < plan_ca1.getTime().getTime())
				{
					edit_test.setEnabled(false);
				}
				edit_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						edit_test_actionPerformed(e);
					}
				});
				JMenuItem delete_test = new JMenuItem(LangModelScheduleOld.String("labelPopupDel"));
				delete_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delete_test_actionPerformed(e);
					}
				});
				popup.add(edit_test);
				popup.addSeparator();
				popup.add(delete_test);
				popup.show(((NewButton)comp),buttonX,e.getY());
			}
			if (comp instanceof TestPlan)
			{
				JPopupMenu popup = new JPopupMenu();
				JMenuItem add_test = new JMenuItem(LangModelScheduleOld.String("labelPopupAdd"));
				add_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						add_test_actionPerformed(e);
					}
				});
				JMenuItem delete_panel = new JMenuItem(LangModelScheduleOld.String("labelPopupDelKIS"));
				delete_panel.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delete_panel_actionPerformed(e);
					}
				});
				popup.add(add_test);
				popup.addSeparator();
				popup.add(delete_panel);
				popup.show(((TestPlan)comp),buttonX,e.getY());
			}
		}
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
		if(ae.getActionCommand().equals("AskTimeTestType"))
		{
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
		}
		if(ae.getActionCommand().equals("EditTime"))
		{
			edit_Button_func (edit_comp, ((Date )ae.getSource()).getTime());
			My_Refresh();
		}
		if(ae.getActionCommand().equals("TestSetupChoise"))
		{
			test_setup_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("NavigationTime"))
		{
			jSpin1.setValue(new Date(Long.parseLong(ae.getSource().toString())));
		}
		if(ae.getActionCommand().equals("TestReturnType"))
		{
			returntype = (TestReturnType) ae.getSource();
		}
		if(ae.getActionCommand().equals("AnalysisType"))
		{
			analysis_type_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("EvaluationType"))
		{
			evaluation_type_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("TestType"))
		{
			test_type_id = ae.getSource().toString();
			new_parameters.clear();
			if(test_type_id.equals("trace_and_analyse"))
			{
				new_parameters.addElement("1.467");
				new_parameters.addElement("1550");
				new_parameters.addElement("4000");
				new_parameters.addElement("4.096");
				new_parameters.addElement("0.25");
				new_parameters.addElement("100");
			}
			else if (test_type_id.equals("voice_analyse"))
			{
				new_parameters.addElement("1550");
			}
		}
		if(ae.getActionCommand().equals("KISType"))
		{
			kis_id = ae.getSource().toString();
			My_Refresh();
		}
		if(ae.getActionCommand().equals("KISes_and_Ports"))
		{
			KISes_and_ports = (TreeModelClone )ae.getSource();
			number_of_kises = "All";
		}
		if(ae.getActionCommand().equals("Filtration_parameters"))
		{
			filter_test = (DataSet)ae.getSource();
			number_of_kises = "Filt";
			My_Refresh();
		}
		if(ae.getActionCommand().equals("PortType"))
		{
			port_id = ae.getSource().toString();
			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, port_id);
			Hashtable ht = ap.characteristics;
			if (ht.size() != 0)
			{
				warm_up_time = Long.parseLong(((Characteristic) ht.get("warm_up_time")).value);
				cool_down_time = Long.parseLong(((Characteristic) ht.get("cool_down_time")).value);
			}
			else
			{
				warm_up_time = 0;
				cool_down_time = 0;
			}
		}
		if(ae.getActionCommand().equals("METype"))
		{
			me_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("Parameter"))
		{
			new_parameters.clear();
			for (int ii=0; ii < ((Vector)ae.getSource()).size(); ii++)
			{
				new_parameters.add( ((Vector)ae.getSource()).elementAt(ii) );
			}
		}
		if(ae.getActionCommand().equals("StartTime"))
		{
			start_time = ((Date )ae.getSource()).getTime();
		}
		if(ae.getActionCommand().equals("EndTime"))
		{
			end_time = ((Date )ae.getSource()).getTime();
		}
		if(ae.getActionCommand().equals("Interval"))
		{
			interval = Long.parseLong(ae.getSource().toString());
		}
		if(ae.getActionCommand().equals("TesttTime"))
		{
			long tt = ((Date )ae.getSource()).getTime();
			if (temp_times.size() == 0 || tt < Long.parseLong(temp_times.elementAt(0).toString())) {
				start_time = tt;
				temp_times.insertElementAt(String.valueOf(tt), 0);
			}
			else if (tt > Long.parseLong(temp_times.elementAt(temp_times.size() - 1).toString()))
			{
				temp_times.insertElementAt(String.valueOf(tt), temp_times.size());
			}
			else
			{
				for (int i = 1; i < temp_times.size(); i++)
				{
					if (tt > Long.parseLong(temp_times.elementAt(i-1).toString()) && tt < Long.parseLong(temp_times.elementAt(i).toString()) )
					{
						temp_times.insertElementAt(String.valueOf(tt), i);
					}
				}
			}
		}
		if(ae.getActionCommand().equals("PlanRefresh"))
		{
			Vector new_test_ids = (Vector )ae.getSource();
			for (int i = 0; i < new_test_ids.size(); i++)
			{
				baza_test_new.remove((String )new_test_ids.elementAt(i));
			}
			My_Refresh();
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

	void OneTimeButton_actionPerformed(ActionEvent e)
	{
		if (checker.checkCommand(Checker.setOneTimeTest))
		{
			OneTimeButton.setSelected(true);
			onetimetest.setSelected(true);
			timetabletest.setSelected(false);
			periodictest.setSelected(false);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
			shag = 0;
		}
	}

	void TimeTableButton_actionPerformed(ActionEvent e) {
		if (checker.checkCommand(Checker.setTestTimeTable))
		{
			TimeTableButton.setSelected(true);
			onetimetest.setSelected(false);
			timetabletest.setSelected(true);
			periodictest.setSelected(false);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
			shag = 0;
		}
	}

	void PeriodicButton_actionPerformed(ActionEvent e) {
		if (checker.checkCommand(Checker.setPeriodicTest))
		{
			PeriodicButton.setSelected(true);
			onetimetest.setSelected(false);
			timetabletest.setSelected(false);
			periodictest.setSelected(true);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));

			shag = 0;
		}
	}

	void SaveTestsButton_actionPerformed(ActionEvent e)
	{
		String[] test_ids = new String [baza_test_new.size()];
		jPanel1.removeAll();
		baza_test.clear();
		tps_panel.clear();
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		for (int i = 0; i < baza_test_new.size(); i++)
		{
			Test tt = (Test) baza_test_new.get(i);
			if (!same_tests(tt))
			{
				tt.user_id = aContext.getSessionInterface().getUserId();
				tt.name = test_sdf.format(new Date(System.currentTimeMillis()));
				Pool.put("test", tt.id, tt);
				test_ids[i] = tt.getId();
				if(tt.analysis_id != null && !tt.analysis_id.equals(""))
				{
					dataSource.createAnalysis(tt.analysis_id);
				}
				if(tt.evaluation_id != null && !tt.evaluation_id.equals(""))
				{
					dataSource.createEvaluation(tt.evaluation_id);
				}
			}
		}

		if(test_ids.length != 0)
		{
			TestRequest treq = new TestRequest(dataSource.GetUId("testrequest"));
			treq.user_id = aContext.getSessionInterface().getUserId();
			treq.name = treq.user_id + " at " + test_sdf.format(new Date(System.currentTimeMillis()));
			Pool.put("testrequest", treq.id, treq);
			for (int i = 0; i < test_ids.length; i++)
			{
				Test tt = (Test )Pool.get(Test.typ, test_ids[i]);
				tt.request_id = treq.getId();
				treq.test_ids.add(tt.getId());
			}
			treq.updateLocalFromTransferable();
			dataSource.RequestTest(treq.id, test_ids);
		}
		baza_test_new.clear();
		getTests();
		tps_addButtons(baza_test, "BazaTest");
		internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
		internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
		jPanel1.revalidate();
		setDrag();
	}

	void AddTestButton_actionPerformed(ActionEvent e)
	{
		boolean ext_bool = true;
		boolean int_bool_1 = false;
		boolean int_bool_2 = false;
		if (end_time < start_time && ttt.equals(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL))
		{
			JOptionPane.showMessageDialog ( Environment.getActiveWindow(),LangModelScheduleOld.String("errorWrongTestTime"), LangModelScheduleOld.String("errorWrongTest"), JOptionPane.OK_OPTION);
			ext_bool = false;
		}
		KIS kis = (KIS )Pool.get(KIS.typ, kis_id);
		if (kis != null)
		{
			int i;
			Vector vec = kis.access_ports;
			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, port_id);
			for (i = 0; i < vec.size();)
			{
				if (ap.equals((AccessPort )vec.elementAt(i)))
				{
				 break;
				}
				else i++;
			}
			if (i < vec.size())
			{
				Hashtable ht = Pool.getHash("monitoredelement");
				Hashtable ht2 = new Hashtable();
				for(Enumeration en = ht.elements(); en.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						ht2.put(me.getId(), me);
					}
				}
				for(Enumeration en2 = ht2.elements(); en2.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en2.nextElement();
					if(me.getId().equals(me_id))
					{
						int_bool_1 = true;
						break;
					}
				}

				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				if(apt.test_type_ids.contains(test_type_id))
				{
					int_bool_2 = true;
				}
			}
		}
		if (ext_bool == true)
		{
			shag = 0;
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			if (UsualTestingButton.isSelected())
			{
				if (int_bool_1 == true && int_bool_2 == true)
				{
					Make_new_Test( kis_id, port_id, me_id);
					temp_test = new Test("");
					temp_test.id = dataSource.GetUId("test");
					temp_times.clear();
				}
				else
				{
					JOptionPane.showMessageDialog ( Environment.getActiveWindow(),LangModelScheduleOld.String("errorWrongTestObjects"), LangModelScheduleOld.String("errorWrongTest"), JOptionPane.OK_OPTION);
				}
			}
			else if (ExtendedTestingButton.isSelected())
			{
				FilterTreeNode mmtn = (FilterTreeNode )KISes_and_ports.getRoot();
				for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
				{
					FilterTreeNode kis_mte = (FilterTreeNode )enum.nextElement();
					if (kis_mte.state != 0)
					{
						for(Enumeration enu = kis_mte.children(); enu.hasMoreElements();)
						{
							FilterTreeNode port_mte = (FilterTreeNode )enu.nextElement();
							if (port_mte.state != 0)
							{
								for(Enumeration en = port_mte.children(); en.hasMoreElements();)
								{
									FilterTreeNode volokno_mte = (FilterTreeNode )en.nextElement();
									if (volokno_mte.state == 2)
									{
										Make_new_Test( kis_mte.id, port_mte.id, volokno_mte.id);
										temp_test = new Test("");
										temp_test.id = dataSource.GetUId("test");
										temp_times.clear();
									}
								}
							}
						}
					}
				}
			}
			My_Refresh();
		}
	}

	void  Make_new_Test(String kisid, String portid, String meid)
	{
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		temp_test.test_setup_id = test_setup_id;
		TestArgumentSet as = new TestArgumentSet();
		if (!test_setup_id.equals(""))
		{
			TestSetup ts = (TestSetup )Pool.get(TestSetup.typ, test_setup_id);
			temp_test.test_argument_set_id = ts.test_argument_set_id;
			as = (TestArgumentSet )Pool.get(TestArgumentSet.typ, ts.test_argument_set_id);
			if (!analysis_type_id.equals(""))
			{
				Analysis anal = new Analysis(dsi.GetUId(Analysis.typ));
				anal.monitored_element_id = meid;
				anal.type_id = analysis_type_id;
				anal.criteria_set_id = ts.criteria_set_id;
				temp_test.analysis_id = anal.getId();
				Pool.put(Analysis.typ, anal.getId(), anal);
			}
			else
			{
				temp_test.analysis_id = "";
			}

			if (!evaluation_type_id.equals(""))
			{
				Evaluation eval = new Evaluation(dsi.GetUId(Evaluation.typ));
				eval.monitored_element_id = meid;
				eval.type_id = evaluation_type_id;
				eval.threshold_set_id = ts.threshold_set_id;
				eval.etalon_id = ts.etalon_id;
				temp_test.evaluation_id = eval.getId();
				Pool.put(Evaluation.typ, eval.getId(), eval);
			}
			else
			{
				temp_test.evaluation_id = "";
			}
		}
		temp_test.kis_id = kisid;
		temp_test.monitored_element_id = meid;
		temp_test.test_type_id = test_type_id;
		temp_test.temporal_type = ttt;
		String aa = temp_test.temporal_type.toString();
		temp_test.status = TestStatus.TEST_STATUS_SCHEDULED;
		temp_test.start_time = start_time;
//---------------------------------------------------------------------
		temp_test.return_type = returntype;
		TestTimeStamps tts = new TestTimeStamps();
		switch(temp_test.temporal_type.value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
				tts._default();
				break;
			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
				int temp_ti = (int)((end_time - start_time) / interval);
				PeriodicalTestParameters ptp = new PeriodicalTestParameters(interval, start_time + temp_ti * interval);
				tts.ptpars(ptp);
				break;
			case  TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
				long[] times = new long[temp_times.size()];
				for (int i = 0; i <temp_times.size(); i++)
					times[i] = Long.parseLong(temp_times.elementAt(i).toString());
				tts.ti(times);
				break;
			default:
				System.out.println("ERROR: Unknown temporal type: " + temp_test.temporal_type.value());
		}
		temp_test.time_stamps = tts;

		if (test_setup_id.equals(""))
		{
			as.id = dsi.GetUId(TestArgumentSet.typ);
			Pool.put(TestArgumentSet.typ, as.getId(), as);
			as.name = as.id;
			as.created = 0;
			as.created_by = "";
			as.test_type_id = test_type_id;

			Parameter par_trans;
			if(temp_test.test_type_id.equals("trace_and_analyse"))
			{
				ByteArray ref_ior;
				ByteArray ref_wvlen;
				ByteArray ref_scans;
				ByteArray ref_trclen;
				ByteArray ref_res;
				ByteArray ref_pulswd;
				ActionParameterType apt;
				TestType tttt = (TestType )Pool.get("testtype",temp_test.test_type_id);
				try
				{
					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_ior");
					ref_ior = new ByteArray(Double.parseDouble(new_parameters.elementAt(0).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_ior.getBytes(), "ref_ior", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_wvlen");
					ref_wvlen = new ByteArray(Integer.parseInt(new_parameters.elementAt(1).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_wvlen.getBytes(), "ref_wvlen", "int");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_scans");
					ref_scans = new ByteArray(Double.parseDouble(new_parameters.elementAt(2).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_scans.getBytes(), "ref_scans", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_trclen");
					ref_trclen = new ByteArray(Double.parseDouble(new_parameters.elementAt(3).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_trclen.getBytes(), "ref_trclen", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_res");
					ref_res = new ByteArray(Double.parseDouble(new_parameters.elementAt(4).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_res.getBytes(), "ref_res", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_pulswd");
					ref_pulswd = new ByteArray(Long.parseLong(new_parameters.elementAt(5).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_pulswd.getBytes(), "ref_pulswd", "double");
					as.addArgument(par_trans);
				}
				catch(java.io.IOException ex) {}
			}
			else if (temp_test.test_type_id.equals("voice_analyse"))
			{
				ByteArray ref_characterizationidentity;
				ActionParameterType apt;
				TestType tttt = (TestType )Pool.get("testtype",temp_test.test_type_id);
				try
				{
					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_characterizationidentity");
					ref_characterizationidentity = new ByteArray(new_parameters.elementAt(0).toString());
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_characterizationidentity.getBytes(), "ref_characterizationidentity", "string");
					as.addArgument(par_trans);
				}
				catch(java.io.IOException ex){}
			}
			dsi.saveTestArgumentSet(as.getId());
			temp_test.test_argument_set_id = as.getId();
		}

//   Длительность теста
		if (temp_test.test_type_id.equals("trace_and_analyse"))
		{
			Vector arg = as.arguments;
			double param1 = 0, param2 = 0, param3 = 0;

			try
			{
				for (int i = 0; i < arg.size(); i++)
				{
					Parameter par = (Parameter )arg.elementAt(i);
					if (par.codename.equals("ref_ior"))
					{
						param1 = (new ByteArray(par.value)).toDouble();
					}
					else if (par.codename.equals("ref_trclen"))
					{
						param2 = (new ByteArray(par.value)).toDouble();
					}
					else if (par.codename.equals("ref_scans"))
					{
						param3 = (new ByteArray(par.value)).toDouble();
					}
					//else if (par.codename.equals("ref_wvlen"))
					//else if (par.codename.equals("ref_trclen"))
					//else if (par.codename.equals("ref_res"))
				}
			}
			catch(java.io.IOException ex) {}

			long t_warm = 0;
			long t_cool = 0;

			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, portid);
			Hashtable ht = ap.characteristics;
			if (ht.size() != 0)
			{
				t_warm = Long.parseLong(((Characteristic) ht.get("warm_up_time")).value);
				t_cool = Long.parseLong(((Characteristic) ht.get("cool_down_time")).value);
			}

			double wave_speed = 300000000 / param1;
			double izmer_time = (param2 * 1000) / wave_speed;
			long t_work = (long) (izmer_time * param3 * 1000);

			long all_time = t_sys + t_warm + t_cool + t_work;
			temp_test.duration = all_time;
		}
// Окончание счета длительности теста

		Test clone_test = temp_test.myclone();
		baza_test_new.add(clone_test);
	}

	void edit_test_actionPerformed(ActionEvent e)
	{
		edit_comp = comp;
		internal_dispatcher.notify(new OperationEvent( String.valueOf(((NewButton)comp).testtime),0,"EditTest"));
	}

	void delete_test_actionPerformed(ActionEvent e)
	{
		int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), LangModelScheduleOld.String("labelDelTestRealQ"),
				LangModelScheduleOld.String("labelDelTestReal"), JOptionPane.YES_NO_OPTION);
		if (temp == JOptionPane.YES_OPTION)
			delete_test_func(comp);
	}

	void jButton5_actionPerformed(ActionEvent e)
	{
		My_Refresh();
	}

	void add_test_actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < tps_panel.size(); i++)
		{
			if (this.comp==tps_panel.elementAt(i))
			{
				if (shag >= 1)
				{
					long add_time = drop_coef(buttonX);
					if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
					{
						button_elementary_test.addElement(new NewButton(Color.blue, 2, "MouseTest", false, temp_test.id, shag, add_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
						temp_times.addElement(String.valueOf(add_time));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
						((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
						shag++;
					}
					else if ((ttt == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL) && shag <= 1)
					{
						button_elementary_test.addElement(new NewButton(Color.green, 1, "MouseTest", false, temp_test.id, shag, add_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
						end_time = add_time;
						((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
						((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
						shag++;
					}
				}
				if (shag == 0)
				{
					start_time = drop_coef(buttonX);
					button_global_test.addElement(new NewButton((new JButton()).getBackground(), ttt.value(), "MouseTest", true, temp_test.id, -1, start_time,
						((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					((NewButton)button_global_test.lastElement()).addMouseListener(ml);
					((TestPlan) comp).add((NewButton)button_global_test.lastElement(),  new XYConstraints(buttonX, 0, test_time_long_const, 10));
					if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
						button_elementary_test.addElement(new NewButton(Color.white, 0, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					else if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
						button_elementary_test.addElement(new NewButton(Color.blue, 2, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					else if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
						button_elementary_test.addElement(new NewButton(Color.green, 1, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					((NewButton)button_elementary_test.lastElement()).setName(temp_test.id+" "+String.valueOf(shag));
					((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
					((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
					shag++;
				}
			}
		}
		((TestPlan) comp).revalidate();
		setDrag();
	}

	void delete_panel_actionPerformed(ActionEvent e) {
	for (int i = 0; i < tps_panel.size(); i++)
	{
		if (((TestPlan)comp).equals((TestPlan)tps_panel.elementAt(i)))
		{
			if (((JPanel)comp).getComponentCount()==0)
			{
				tps_panel.removeElementAt(i);
				My_Refresh();
			}
		}
	}
	}

	void getTests()
	{
		DataSourceInterface dataSource = this.aContext.getDataSourceInterface();
		if(dataSource == null)
		 return;
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		baza_test.clear();
		String[] ids = new SurveyDataSourceImage(dataSource).GetTests(drop_coef(0),drop_coef(-1));
		new SurveyDataSourceImage(dataSource).GetAlarms();
		Vector res_ids_vec = new Vector();//1
		for (int i = 0; i < ids.length; i++)
		{
			System.out.println("get testID:"+ids[i]);
			Test t = (Test )Pool.get(Test.typ, ids[i]);
			baza_test.add(t);
//		  String[] res_ids = t.result_ids;
//		  new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetTestResult(ids[i]);
			for(int j = 0; j < t.result_ids.length; j++)//2
			res_ids_vec.add(t.result_ids[j]);//3
		}
		String[] res_ids = new String[res_ids_vec.size()];//4
		res_ids_vec.copyInto(res_ids);
		new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetResults(res_ids);//5
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	void tps_addButtons(DataSet baza_test, String button_type)
	{
		int delta = 0;
		for (int count = 0; count < baza_test.size(); count++)
		{
			int element_test_id = 0;
			Test temp_t = new Test("");
			if (number_of_kises.equals("All"))
			{
				temp_t = (Test) baza_test.get(count);
			}
			else if (number_of_kises.equals("One"))
			{
				while (count < baza_test.size())
				{
					temp_t = (Test) baza_test.get(count);
					if (temp_t.kis_id.equals(kis_id))
						break;
					count++;
				}
			}
			else if (number_of_kises.equals("Filt"))
			{
				while (count < baza_test.size())
				{
					temp_t = (Test) baza_test.get(count);
					if ((filter_test.get(temp_t.id)) != null)
						break;
					count++;
				}
			}
			if (count == baza_test.size())
				break;

			String portid = "";
			String meid = temp_t.monitored_element_id;

			TransmissionPath path;
			if(Pool.getHash(TransmissionPath.typ) != null)
				for(Enumeration e = Pool.getHash(TransmissionPath.typ).elements();
					e.hasMoreElements();)
				{
					path = (TransmissionPath )e.nextElement();
					if(path.monitored_element_id.equals(temp_t.monitored_element_id))
					{
						portid = path.access_port_id;
						break;
					}
				}

			test_time_long = detalize_long_to_int(temp_t.duration);
			int i = 0;
				while ( i < tps_panel.size())
				{
					if ( ((TestPlan)tps_panel.elementAt(i)).kisid.equals(temp_t.kis_id)&& ((TestPlan)tps_panel.elementAt(i)).portid.equals(portid)
						&&((TestPlan)tps_panel.elementAt(i)).meid.equals(meid))
						break;
					i++;
				}
				if (i==tps_panel.size())
				{
					tps_panel.addElement(new TestPlan(parent, aContext, jComboBox8, temp_t.kis_id, portid, meid));
					((TestPlan)tps_panel.elementAt(i)).addMouseListener(ml);
					jPanel1.add((TestPlan)tps_panel.elementAt(i), new XYConstraints(0, 40*i, -1, -1));
				}

			if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
			{
				long start = temp_t.start_time;
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 0, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(ml);
				button_color = Alarm_Check_Color(Color.white, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 0, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
			{
				long start = temp_t.start_time;
				long end = temp_t.time_stamps.ptpars().end_time;
				long interval = temp_t.time_stamps.ptpars().dt;
				long temp_time = interval;
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 1, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(ml);
				button_color = Alarm_Check_Color( Color.green, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 1, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
				while(start + temp_time <= end)
				{
					element_test_id++;
					if (detalize(start+temp_time)==true)
					{
						GregorianCalendar cala = new GregorianCalendar();
						delta = detalize_coef(start+temp_time);
						button_color = Alarm_Check_Color( Color.green, temp_t, start + temp_time, false);
						button_elementary_test.addElement(new NewButton(button_color, 1, button_type, false, temp_t.id, element_test_id, start+temp_time, temp_t.kis_id, portid, meid));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
						((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
					}
					temp_time+=interval;
				}
			 }
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
			{
				long start = temp_t.start_time;
				long time_massiv[] = temp_t.time_stamps.ti();
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 2, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(ml);
				button_color = Alarm_Check_Color( Color.blue, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 2, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
				for (int j = 0; j < time_massiv.length; j++)
				{
					element_test_id++;
					if (detalize(time_massiv[j])==true)
					{
						GregorianCalendar cala = new GregorianCalendar();
						delta = detalize_coef(time_massiv[j]);
						button_color = Alarm_Check_Color( Color.blue, temp_t, time_massiv[j], false);
						button_elementary_test.addElement(new NewButton(button_color, 2, button_type, false, temp_t.id, element_test_id, time_massiv[j], temp_t.kis_id, portid, meid));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(ml);
						((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
					}
				}
			}
		}
	}
	void setDrag()
 {
 // Внутренние классы для поддержки Drag and Drop
 class ViewTransferable implements Transferable
 {
	DataFlavor[] flavors = new DataFlavor[]{DataFlavor.stringFlavor};
	public ViewTransferable()
	{
	}
	public DataFlavor[] getTransferDataFlavors()
	{
	 return flavors;
	}
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
	 if (flavor.equals(flavors[0]))
	 {
	return true;
	 }
	 return false;
	}
	public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException
	{
	 if (!isDataFlavorSupported(flavor))
	 {
	System.out.println("unsuported flavor");
	return null;
	 }
	 if (flavor.equals(flavors[0]))
	 {
	return(null);
	 }
	 return null;
	}
 }
 class CanvasDragSource implements DragGestureListener, DragSourceListener
 {
	DragSource dragSource = null;
	CanvasDragSource()
	{
	 dragSource = DragSource.getDefaultDragSource();
	 Calendar plan_ca = Calendar.getInstance();
	 long current_time = plan_ca.getTime().getTime();
	 for (int i = 0; i < button_global_test.size(); i++)
	if ( ((NewButton)button_global_test.elementAt(i)).testtime > current_time)
		dragSource.createDefaultDragGestureRecognizer((NewButton)button_global_test.elementAt(i),DnDConstants.ACTION_COPY_OR_MOVE,this);
	 for (int i = 0; i < button_elementary_test.size(); i++)
	if ( ((NewButton)button_elementary_test.elementAt(i)).testtime > current_time)
		dragSource.createDefaultDragGestureRecognizer((NewButton)button_elementary_test.elementAt(i),DnDConstants.ACTION_COPY_OR_MOVE,this);
	 }
	public void dragGestureRecognized(DragGestureEvent dge)
	{
	 buttonX = (int) dge.getComponent().getX();
	 buttonY = (int) dge.getComponent().getY();
	 Transferable transferable = new ViewTransferable();
	 dge.startDrag(null,transferable,this);
	}
	public void dropActionChanged(DragSourceDragEvent dsde){}
	public void dragEnter(DragSourceDragEvent dsde){}
	public void dragOver(DragSourceDragEvent dsde){}
	public void dragExit(DragSourceEvent dse){}
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		My_Refresh();
	}
 }
 class CanvasDropTarget implements DropTargetListener
 {
	DropTarget[] dropTarget= new DropTarget[tps_panel.size()];
	boolean acceptableType;
	CanvasDropTarget()
	{
	for (int i = 0; i < tps_panel.size(); i++) {
		dropTarget[i] = new DropTarget((TestPlan)tps_panel.elementAt(i), DnDConstants.ACTION_COPY_OR_MOVE,this,true,null);
	}
	}
	public void dragOver(DropTargetDragEvent dtde)
	{
	 acceptOrRejectDrag(dtde);
	}
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	 acceptOrRejectDrag(dtde);
	}
	public void dragExit(DropTargetEvent dte){}
	public void drop(DropTargetDropEvent dtde)
	{
	 if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0)
	 {
		dtde.acceptDrop(dtde.getDropAction());
		dtde.getDropTargetContext().dropComplete(true);
		int x = (int) dtde.getLocation().getX();
		//double y = dtde.getLocation().getY();
		drop_Button_func (comp, x);
	 }
	 else
	 {
	dtde.rejectDrop();
	 }
	}
	public void dragEnter(DropTargetDragEvent dtde)
	{
	 checkTransferType(dtde);
	 acceptOrRejectDrag(dtde);
	}
	boolean acceptOrRejectDrag(DropTargetDragEvent dtde)
	{
	 int dropAction = dtde.getDropAction();
	 int sourceActions = dtde.getSourceActions();
	 boolean acceptedDrag = false;
	 if (!acceptableType || (sourceActions &DnDConstants.ACTION_COPY_OR_MOVE) == 0)
	 {
		 dtde.acceptDrag(DnDConstants.ACTION_COPY);
		 acceptedDrag = true;
	 }
	 else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE ) == 0)
	 {
		 dtde.acceptDrag(DnDConstants.ACTION_COPY);
		 acceptedDrag = true;
	 }
	 return acceptedDrag;
	}
	void checkTransferType(DropTargetDragEvent dtde)
	{
		acceptableType = dtde.isDataFlavorSupported(DataFlavor.stringFlavor);
	}
 }
 new CanvasDragSource();
 new CanvasDropTarget();
 }

 void drop_Button_func (Object comp, int x) {
	DataSourceInterface dataSource = aContext.getDataSourceInterface();
	int w = 0;
	boolean type_of_panel_is_tps_panel_new = true;
	if (((NewButton)comp).global==true)
		{
		for (int i = 0; i < button_global_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_global_test.elementAt(i)))
			{
				((NewButton)button_global_test.elementAt(i)).setLocation((int)x,2);
				String temp1 = ((NewButton)button_global_test.elementAt(i)).testid;
				for (int j = 0; j < button_elementary_test.size(); j++)
				{
					String temp2 = ((NewButton)button_elementary_test.elementAt(j)).testid;
					if (temp1.equals(temp2))
					{
						int loc = ((NewButton)button_elementary_test.elementAt(j)).getX();
						((NewButton)button_elementary_test.elementAt(j)).setLocation(loc + (x - buttonX),17);
					}
				}
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = drop_coef(x);
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 0)
					{
						TestTimeStamps tts = new TestTimeStamps();
						tts._default();
						move_test.time_stamps = tts;
					}
					if ( ((NewButton)comp).int_tempor_type == 1)
					{
						move_test.time_stamps.ptpars().end_time += time_step;
					}
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
					internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
					baza_test_new.remove(((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = drop_coef(x);
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 1)
						move_test.time_stamps.ptpars().end_time += time_step;
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					baza_test_new.add(move_test);
					internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
				}
				break;
			}
		}
		}
		else
		{
			for (int i = 0; i < button_elementary_test.size(); i++)
			{
			if (((NewButton)comp).equals((NewButton)button_elementary_test.elementAt(i)))
			{
			String temp1 = ((NewButton)button_elementary_test.elementAt(i)).testid;
			if (((NewButton)comp).int_tempor_type==0)
			{
						((NewButton)button_elementary_test.elementAt(i)).setLocation((int)x,17);
						for (int j = 0; j < button_global_test.size(); j++)
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1))
						{
							int loc = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc + (x - buttonX),2);
							break;
						}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							move_test.start_time = drop_coef(x);
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							baza_test_new.remove(((NewButton)comp).testid);
							move_test.start_time = drop_coef(x);
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}
					}

					if (((NewButton)comp).int_tempor_type==2)
					{
						((NewButton)button_elementary_test.elementAt(i)).setLocation((int)x,17);
						for (int j = 0; j < button_global_test.size(); j++)
						{
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1)&&
							((NewButton)button_elementary_test.elementAt(i)).count > 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = drop_coef(x);
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = drop_coef(x);
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								baza_test_new.add(move_test);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1)&&
							((NewButton)button_elementary_test.elementAt(i)).count == 0)
						{
							int loc = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc + (x - buttonX),2);
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								move_test.start_time = drop_coef(x);
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								move_test.start_time = drop_coef(x);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						int loc_glob = 0;
						int loc_elem = 0;
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1))
							{
							loc_glob = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc_glob + (x - buttonX),2);
						}
						for (int j = 0; j < button_elementary_test.size(); j++)
							if (((NewButton)button_elementary_test.elementAt(j)).testid.equals(temp1))
							{
							loc_elem = ((NewButton)button_elementary_test.elementAt(j)).getX();
							((NewButton)button_elementary_test.elementAt(j)).setLocation(loc_elem + (x - buttonX),17);
						}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							long b_time = move_test.start_time;
							move_test.start_time = drop_coef(loc_glob + (x - buttonX));
							long time_step = move_test.start_time - b_time;
							if ( ((NewButton)comp).int_tempor_type == 1)
							{
								move_test.time_stamps.ptpars().end_time += time_step;
							}
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							baza_test_new.remove(((NewButton)comp).testid);
							long b_time = move_test.start_time;
							move_test.start_time = drop_coef(x);
							long time_step = move_test.start_time - b_time;
							if ( ((NewButton)comp).int_tempor_type == 1)
							{
								move_test.time_stamps.ptpars().end_time += time_step;
							}
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}

					}

					break;
					}
				}
			}
 }

 void edit_Button_func (Object comp, long edit_time) {
	DataSourceInterface dataSource = aContext.getDataSourceInterface();
	int w = 0;
	if (((NewButton)comp).global==true)
		{
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = edit_time;
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 0)
					{
						TestTimeStamps tts = new TestTimeStamps();
						tts._default();
						move_test.time_stamps = tts;
					}
					if ( ((NewButton)comp).int_tempor_type == 1)
					{
						move_test.time_stamps.ptpars().end_time += time_step;
					}
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
					internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
					baza_test_new.remove(((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = edit_time;
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 1)
						move_test.time_stamps.ptpars().end_time += time_step;
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					baza_test_new.add(move_test);
					internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
				}
		}
		else
		{
			if (((NewButton)comp).int_tempor_type==0)
			{
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							move_test.start_time = edit_time;
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							baza_test_new.remove(((NewButton)comp).testid);
							move_test.start_time = edit_time;
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}
					}

					if (((NewButton)comp).int_tempor_type==2)
					{
						for (int j = 0; j < button_global_test.size(); j++)
						{
						if ( ((NewButton)comp).count > 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = edit_time;
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = edit_time;
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								baza_test_new.add(move_test);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						if ( ((NewButton)comp).count == 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								move_test.start_time = edit_time;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								move_test.start_time = edit_time;
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							long b_time = ((NewButton)comp).testtime;
							long time_step = b_time - edit_time;
							move_test.start_time -= time_step;
							move_test.time_stamps.ptpars().end_time -= time_step;
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							baza_test_new.remove(((NewButton)comp).testid);
							long b_time = ((NewButton)comp).testtime;
							long time_step = b_time - edit_time;
							move_test.start_time -= time_step;
							move_test.time_stamps.ptpars().end_time -= time_step;
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}

					}
		}
 }
	void delete_test_func(Object comp)
	{
		if ( ((NewButton)comp).global==true)
		{
		for (int i = 0; i < button_global_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_global_test.elementAt(i)))
			{
				int k = 0;
				String temp1 = ((NewButton)button_global_test.elementAt(i)).kisid;
				String temp2 = ((NewButton)button_global_test.elementAt(i)).portid;
				String temp3 = ((NewButton)button_global_test.elementAt(i)).meid;
				String temp4 = ((NewButton)button_global_test.elementAt(i)).testid;
				((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(i));
				button_global_test.removeElementAt(i);
				i--;
				for (int j = 0; j < button_elementary_test.size(); j++)
				{
					String temp5 = ((NewButton)button_elementary_test.elementAt(j)).testid;
					if (temp5.equals(temp4))
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(j));
						button_elementary_test.removeElementAt(j);
						j--;
					}
				}
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					DataSourceInterface dataSource = aContext.getDataSourceInterface();
					dataSource.RemoveTests(new String[] {temp4});
					Pool.remove("test",temp4);
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					baza_test_new.remove(temp4);
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
					break;
				}
			}
		}
		}
		else
		{
		for (int i = 0; i < button_elementary_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_elementary_test.elementAt(i)))
				{
					int k = 0;
					String temp1 = ((NewButton)button_elementary_test.elementAt(i)).kisid;
					String temp2 = ((NewButton)button_elementary_test.elementAt(i)).portid;
					String temp3 = ((NewButton)button_elementary_test.elementAt(i)).meid;
					String temp4 = ((NewButton)button_elementary_test.elementAt(i)).testid;
					int temp5 = ((NewButton)button_elementary_test.elementAt(i)).count;

					if (((NewButton)comp).int_tempor_type==0)
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(i));
						button_elementary_test.removeElementAt(i);
						i--;
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(j));
								button_global_test.removeElementAt(j);
								j--;
							}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							DataSourceInterface dataSource = aContext.getDataSourceInterface();
							dataSource.RemoveTests(new String[] {temp4});
							Pool.remove("test",temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							baza_test_new.remove(temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
							break;
						}
					}
					if (((NewButton)comp).int_tempor_type==2)
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(i));
						button_elementary_test.removeElementAt(i);
						i--;
						Test del_test = (Test) Pool.get("test", temp4);
						if (temp5 == 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								DataSourceInterface dataSource = aContext.getDataSourceInterface();
								dataSource.RemoveTests(new String[] {temp4});
								Pool.remove("test",temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								baza_test_new.remove(temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
								break;
							}
						}
						else
						{
							long[] del_times = del_test.time_stamps.ti();
							int p = 0;
							long[] new_del_times = new long [del_times.length - 1];
							for (int q = 0; q < del_times.length; q++)
								if (q!=temp5-1)
									new_del_times[p++] = del_times[q];
							TestTimeStamps tts = new TestTimeStamps();
							tts.ti(new_del_times);
							del_test.time_stamps = tts;
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								DataSourceInterface dataSource = aContext.getDataSourceInterface();
								dataSource.UpdateTests(new String[] {temp4});
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								baza_test_new.remove(temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
								break;
							}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(j));
								button_global_test.removeElementAt(j);
								j--;
							}
						for (int j = 0; j < button_elementary_test.size(); j++)
							if (((NewButton)button_elementary_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(j));
								button_elementary_test.removeElementAt(j);
								j--;
							}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							DataSourceInterface dataSource = aContext.getDataSourceInterface();
							dataSource.RemoveTests(new String[] {temp4});
							Pool.remove("test",temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							baza_test_new.remove(temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
							break;
						}
					}
				}
		}
		}
		My_Refresh();
	}

	void ExtendedTestingButton_actionPerformed(ActionEvent e) {
		number_of_kises = "All";
		KISChoise1Button.setEnabled(false);
		KISChoise2Button.setSelected(true);
		ExtendedTestingButton.setSelected(true);
		originaltest.setSelected(false);
		extendedtest.setSelected(true);
		filtertest.setSelected(false);
		if (testing_id==0)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveTreeFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"ExtendedAfterUsual_RootFrame"));
		}
		else if (testing_id==2)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveFiltrationFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"ExtendedAfterFiltration_RootFrame"));
		}
		testing_id = 1;
		My_Refresh();
	}

	void FiltrationButton_actionPerformed(ActionEvent e) {
		KISChoise1Button.setEnabled(false);
		KISChoise2Button.setSelected(true);
		FiltrationButton.setSelected(true);
		originaltest.setSelected(false);
		extendedtest.setSelected(false);
		filtertest.setSelected(true);
		filter_test.add(baza_test);
		filter_test.add(baza_test_new);
		if (testing_id==0)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"Remove3aFrame"));
			internal_dispatcher.notify(new OperationEvent(filter_test,0,"FiltrationAfterUsual_RootFrame"));
		}
		else if (testing_id==1)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveExtendedFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveTimeFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveParamFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveSaveFrame"));
			internal_dispatcher.notify(new OperationEvent(filter_test,0,"FiltrationAfterExtended_RootFrame"));
		}
		testing_id = 2;
	}

	void KISChoise1Button_actionPerformed(ActionEvent e) {
		KISChoise1Button.setSelected(true);
		KISChoise1.setSelected(true);
		KISChoise2.setSelected(false);
		number_of_kises = "One";
		My_Refresh();
	}

	void KISChoise2Button_actionPerformed(ActionEvent e) {
		KISChoise2Button.setSelected(true);
		KISChoise1.setSelected(false);
		KISChoise2.setSelected(true);
		number_of_kises = "All";
		My_Refresh();
	}

	void UsualTestingButton_actionPerformed(ActionEvent e) {
		number_of_kises = "All";
		UsualTestingButton.setSelected(true);
		originaltest.setSelected(true);
		extendedtest.setSelected(false);
		filtertest.setSelected(false);
		KISChoise1Button.setEnabled(true);
		if (testing_id==1)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveExtendedFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"UsualAfterExtended_RootFrame"));
		}
		else if (testing_id==2)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveFiltrationFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"UsualAfterFiltration_RootFrame"));
		}
		testing_id = 0;
	}

	boolean detalize (long det_time)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl;
		long ftl;
		boolean det_ok = true;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23,59,59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		return det_ok;
	}

	void slider_coef()
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		plan_ca1.setTime(date);
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH),
						 plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY) + jSlider2.getValue(),0,0);
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH),
						 plan_ca1.get(Calendar.DAY_OF_MONTH)+jSlider2.getValue(),0,0,0);
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH) + jSlider2.getValue(),
						 1, 0, 0, 0);
		}
		jSpin1.setValue(plan_ca1.getTime());
	}

	int detalize_coef(long my_time)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl = 0;
		long ftl = 0;
		float f_coord = 0;
		int delta = 0;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 960;
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23,59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 984;
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 992;
		}
		return delta = (int)(f_coord * (float)(my_time - btl )/(ftl - btl) );
	}

	int detalize_long_to_int(long my_time)
	{
		int delta = 0;
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			delta = (int) ((my_time * 16) / (60 * 1000));
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			delta = (int) ((my_time * 41) / (60 * 60 * 1000));
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			delta = (int) ((my_time * 32) / (24 * 60 * 60 * 1000));
		}

		if (delta < test_time_long_const)
		{
			delta = test_time_long_const;
		}
		return delta;
	}

	long drop_coef(int my_coord)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl = 0;
		long ftl = 0;
		int f_coord = 0;
		long delta = 0;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 960;
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0 ,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23 ,59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 984;
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 992;
		}
		if (my_coord == -1)
			 my_coord = f_coord;
		return delta = btl + (long)( my_coord * (float)(ftl-btl)/f_coord );
	}

	void My_Refresh()
	{
		if (checker.checkCommand(Checker.refreshTest))
		{
			slider_coef();
			aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestRefresh")));
			jPanel1.removeAll();
			tps_panel.clear();
			button_global_test.clear();
			button_elementary_test.clear();
			getTests();
			tps_addButtons(baza_test_new, "NewTest");
			tps_addButtons(baza_test, "BazaTest");
			internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
			internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
			jPanel1.repaint();
			jPanel1.revalidate();
			setDrag();
			internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"TestPlan_Time"));
			aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestRefreshFinish")));
			jSlider2.setValue(0);
		}
	}

	boolean same_tests(Test tt)
	{
		boolean flag = false;
		/*getTests();
		for (int i = 0; i < baza_test.size(); i++)
		{
			Test temp_t = (Test )baza_test.get(i);
			if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
			{
				if ((temp_t.start_time + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
				{
					flag = false;
				}
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
			{
				long start = temp_t.start_time;
				long end = temp_t.time_stamps.ptpars().end_time;
				long interval = temp_t.time_stamps.ptpars().dt;
				long temp_time = interval;
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
			{
				long start = temp_t.start_time;
				if ((temp_t.start_time + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
				{
					flag = false;
				}
				long time_massiv[] = temp_t.time_stamps.ti();
				for (int j = 0; j < time_massiv.length; j++)
				{
					if ((time_massiv[j] + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
					{
						 flag = false;
					}
				}
			}
		}*/
		return flag;
	}

	Color Alarm_Check_Color(Color button_color, Test temp_t, long start, boolean global)
	{
		if (global == true)
		{
			if (temp_t.elementary_test_alarms.length !=0 )
			{
				ElementaryTestAlarm[] eta = temp_t.elementary_test_alarms;
				int a = 0;
				while (a < eta.length)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, eta[a].alarm_id);
					if(alarm != null)
					{
						if(alarm.type_id.equals("rtutestalarm"))
						{
							return Color.red;
						}
						else if(alarm.type_id.equals("rtutestwarning"))
							 button_color = Color.yellow;
					}
					a++;
				}
			}
		}
		else
		{
			for (int a = 0; a < temp_t.elementary_test_alarms.length; a++)
			{
				if (temp_t.elementary_test_alarms[a].elementary_start_time == start)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, temp_t.elementary_test_alarms[a].alarm_id);
					if(alarm != null)
					{
						if(alarm.type_id.equals("rtutestalarm"))
						{
							button_color = Color.red;
						}
						else if(alarm.type_id.equals("rtutestwarning"))
						{
							button_color = Color.yellow;
						}
					}
					break;
				}
			}
		}
		return button_color;
	}

	void jComboBox8_actionPerformed(ActionEvent e)
	{
		if (this.jComboBox8.getSelectedIndex()==0)
		{
			jSlider2.setMinimum(-24);
			jSlider2.setMaximum(24);
		}
		if (this.jComboBox8.getSelectedIndex()==1)
		{
			jSlider2.setMinimum(-31);
			jSlider2.setMaximum(31);
		}
		if (this.jComboBox8.getSelectedIndex()==2)
		{
			jSlider2.setMinimum(-12);
			jSlider2.setMaximum(12);
		}
		My_Refresh();
	}

	void jSpin1_mousePressed(MouseEvent e) {
		FontMetrics fm = jTextField1.getFontMetrics(jTextField1.getFont());
		String temp = test_sdf.format(jSpin1.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl >= x)
			{
				if ( i >= 0 && i < 3)
				{
					sdm.setCalendarField(Calendar.DAY_OF_MONTH);
					jTextField1.select(0,2);
					break;
				}
				else if ( i >= 3 && i < 6)
				{
					sdm.setCalendarField(Calendar.MONTH);
					jTextField1.select(3,5);
					break;
				}
				else if ( i >= 6 && i < 11)
				{
					sdm.setCalendarField(Calendar.YEAR);
					jTextField1.select(6,10);
					break;
				}
				else if ( i >= 11 && i < 14)
				{
					sdm.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField1.select(11,13);
					break;
				}
				else if ( i >= 14 && i < 17)
				{
					sdm.setCalendarField(Calendar.MINUTE);
					jTextField1.select(14,16);
					break;
				}
				else
				{
					sdm.setCalendarField(Calendar.SECOND);
					jTextField1.select(17,19);
					break;
				}
			}
		}
	}
}

/*
модификация Стаса от 06.01.04 когда с этим пытался разобраться

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Resource.Test.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.io.*;
import oracle.jdeveloper.layout.*;

public class MyPlanFrame extends JInternalFrame
		implements OperationListener
{
	Dispatcher internal_dispatcher;
	ApplicationContext aContext;
	Checker checker;
	public static final Dimension button_size = new Dimension(24,24); //размер кнопок на тулбаре

	String[] test_plan_diskret = {LangModelScheduleOld.String("DetHour"),
		LangModelScheduleOld.String("DetDay"),LangModelScheduleOld.String("DetMonth")};
	SimpleDateFormat test_sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	Test temp_test = new Test(""); //текущий тест
	int testing_id = 0;  //тип тестирования - 0-обычное, 1-расширенное, 2-фильтрация

	long t_sys = 0;     //время задержки сети (для подсчета длительности выполнения теста)
	long warm_up_time = 0;         //время включения порта (для подсчета длительности выполнения теста)
	long cool_down_time = 0;       //время выключения порта (для подсчета длительности выполнения теста)

	long start_time;    //время начала теста (не элементарного)
	long end_time;      //время окончания теста
	long interval;      //интервал между элементарными тестами

	int buttonX;        //координаты последнего нажатия мыши
	int buttonY;

	String number_of_kises = "All"; //в окне "План-график" отображать все КИСы или текущий
	int test_time_long = 8;         //длительность теста в пикселах
	final int test_time_long_const = 8;   //длительность теста в пикселах по умолчанию

	int shag = 0;   //Очередность задания теста
	TestTemporalType ttt;         //временной тип тестирования

	String test_type_id = "";       //id текущего типа теста
	String kis_id = "";             //id текущего КИСа
	String port_id = "";            //id текущего порта
	String me_id = "";              //id текущего MonitoredElement
	String test_setup_id = "";      //id текущего TestSetup
	String analysis_type_id = "";   //id текущего типа анализа
	String evaluation_type_id = ""; //id текущего типа оценки

	TestReturnType returntype = TestReturnType.WHOLE;        //параметр сохранения (распознанные события в БД сохранять или всю рефлект.)
	Vector new_parameters = new Vector();   //физические параметры теста
	DataSet baza_test = new DataSet();      //тесты, сохраненные в БД
	DataSet baza_test_new = new DataSet();  //тесты, не сохраненные в БД
	DataSet filter_test = new DataSet();    //отфильтрованные тесты
	Vector tps_panel = new Vector();        //набор панелей для тестов
	Vector button_global_test = new Vector();       //кнопки, соответствующие тестам
	Vector button_elementary_test = new Vector();   //кнопки, соответствующие элементарным тестам
	Vector temp_times = new Vector();   //времена элементарных тестов для теста по расписанию
	TreeModelClone KISes_and_ports;     //набор КИСов и портов при расширенном тестировании

	JToggleButton OneTimeButton = new JToggleButton();  //кнопки тулбара
	JToggleButton TimeTableButton = new JToggleButton();
	JToggleButton PeriodicButton = new JToggleButton();
	//JToggleButton jButtonAllKISes = new JToggleButton();
	JButton refreshButton = new JButton();

	JButton AddTestButton = new JButton();
	JButton SaveTestsButton = new JButton();
	JToggleButton KISChoise1Button = new JToggleButton();
	AComboBox timeIntervalComboBox = new AComboBox(test_plan_diskret);    //детализация временная

	JPanel mainPanel = new JPanel();
	Object comp;   //последний выбранный объект на план-графике
	Object edit_comp;  //редактируемый объект

	MouseListener testMouseListener;

	JToggleButton ExtendedTestingButton = new JToggleButton();
	JToggleButton FiltrationButton = new JToggleButton();
	JToggleButton UsualTestingButton = new JToggleButton();
	JToggleButton KISChoise2Button = new JToggleButton();

	JCheckBoxMenuItem onetimetest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuOmeTimeTest"));
	JCheckBoxMenuItem timetabletest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuTimeTableTest"));
	JCheckBoxMenuItem periodictest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuPeriodicalTest"));
	JCheckBoxMenuItem addtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuAddTest"));
	JCheckBoxMenuItem refreshtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuRefresh"));
	JCheckBoxMenuItem savetest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuSaveTest"));
	JCheckBoxMenuItem KISChoise1 = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuKISChoise1"));
	JCheckBoxMenuItem KISChoise2 = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuKISChoise2"));
	JCheckBoxMenuItem originaltest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuOriginalTest"));
	JCheckBoxMenuItem extendedtest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuExtendedTest"));
	JCheckBoxMenuItem filtertest = new JCheckBoxMenuItem(LangModelScheduleOld.String("menuFilterTest"));

	SpinnerDateModel sdm = new SpinnerDateModel();
	JSpinner jSpin1 = new JSpinner(sdm);
	JFormattedTextField jTextField1;
	private JPanel jPanel2 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JSlider jSlider2 = new JSlider(-31,31,0);

	public MyPlanFrame(ApplicationContext aContext, Dispatcher internal_dispatcher)
	{
		this.aContext=aContext;
		this.internal_dispatcher = internal_dispatcher;

		init_module(internal_dispatcher);

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);

		checker = new Checker(aContext.getDataSourceInterface());
	}

	private void jbInit() throws Exception
	{
		timeIntervalComboBox.setMinimumSize(new Dimension(150, 21));
		timeIntervalComboBox.setSelectedIndex(1);
		timeIntervalComboBox.setPreferredSize(new Dimension(200, 21));
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		javax.swing.JSpinner.DateEditor de = (javax.swing.JSpinner.DateEditor )jSpin1.getEditor();
		jTextField1 = de.getTextField();
		jTextField1.setEditable(true);
		DateFormatter formatter = new DateFormatter(test_sdf);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		jTextField1.setFormatterFactory(factory);

		jSlider2.createStandardLabels(1);
		jSlider2.setMajorTickSpacing(1);
		jSlider2.setPaintLabels(true);
		jSlider2.setPaintTicks(true);
		jSlider2.setSnapToTicks(true);

		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		temp_test.id = dataSource.GetUId("test");
		OneTimeButton.setSelected(true);
		onetimetest.setSelected(true);
		UsualTestingButton.setSelected(true);
		KISChoise2.setSelected(true);
		KISChoise2Button.setSelected(true);
		originaltest.setSelected(true);
		OneTimeButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/one.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		OneTimeButton.setToolTipText(LangModelScheduleOld.ToolTip("OmeTimeTest"));
		OneTimeButton.setPreferredSize(button_size);
		OneTimeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			OneTimeButton_actionPerformed(e);
			}
		});
		TimeTableButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/time_t.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		TimeTableButton.setToolTipText(LangModelScheduleOld.ToolTip("TimeTableTest"));
		TimeTableButton.setPreferredSize(button_size);
		TimeTableButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			TimeTableButton_actionPerformed(e);
			}
		});
		PeriodicButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/period.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		PeriodicButton.setToolTipText(LangModelScheduleOld.ToolTip("PeriodicalTest"));
		PeriodicButton.setPreferredSize(button_size);
		PeriodicButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			PeriodicButton_actionPerformed(e);
			}
		});

		AddTestButton.setToolTipText(LangModelScheduleOld.ToolTip("AddTest"));
		AddTestButton.setPreferredSize(button_size);
		AddTestButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addTest.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		AddTestButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.addTests))
				{
					AddTestButton_actionPerformed(e);
				}
			}
		});

		refreshButton.setToolTipText(LangModelScheduleOld.ToolTip("Refresh"));
		refreshButton.setPreferredSize(button_size);
		refreshButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/refresh.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		refreshButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshButton_actionPerformed(e);
			}
		});
		SaveTestsButton.setToolTipText(LangModelScheduleOld.ToolTip("SaveTest"));
		SaveTestsButton.setPreferredSize(button_size);
		SaveTestsButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/addTestBD.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		SaveTestsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.saveTests))
				{
					aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestSave")));
					SaveTestsButton_actionPerformed(e);
					aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestSaveFinish")));
				}
			}
		});
		KISChoise1Button.setToolTipText(LangModelScheduleOld.ToolTip("KISChoise1"));
		KISChoise1Button.setPreferredSize(button_size);
		KISChoise1Button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/OneKIS.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		KISChoise1Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setOneKIStest))
				{
					KISChoise1Button_actionPerformed(e);
				}
			}
		});
		ExtendedTestingButton.setToolTipText(LangModelScheduleOld.ToolTip("AutomatTest"));
		ExtendedTestingButton.setPreferredSize(button_size);
		ExtendedTestingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testir2.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		ExtendedTestingButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setExtendedTest))
				{
					ExtendedTestingButton_actionPerformed(e);
				}
			}
		});
		FiltrationButton.setToolTipText(LangModelScheduleOld.ToolTip("FilterTest"));
		FiltrationButton.setPreferredSize(button_size);
		FiltrationButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/filter.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		FiltrationButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setTestFiltration))
				{
					FiltrationButton_actionPerformed(e);
				}
			}
		});
		UsualTestingButton.setToolTipText(LangModelScheduleOld.ToolTip("OriginalTest"));
		UsualTestingButton.setPreferredSize(button_size);
		UsualTestingButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/testir1.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		UsualTestingButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setUsualTest))
				{
					UsualTestingButton_actionPerformed(e);
				}
			}
		});
		KISChoise2Button.setToolTipText(LangModelScheduleOld.ToolTip("KISChoise2"));
		KISChoise2Button.setPreferredSize(button_size);
		KISChoise2Button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/AllKISs.gif").getScaledInstance(
					16,
					16,
					Image.SCALE_DEFAULT)));
		KISChoise2Button.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (checker.checkCommand(Checker.setAllKIStest))
				{
					KISChoise2Button_actionPerformed(e);
				}
			}
		});
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		this.setResizable(true);

		setSize(new Dimension(749, 603));
		this.setTitle(LangModelScheduleOld.String("MyPlanTitle"));

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

		mainPanel.setLayout(new XYLayout());
		timeIntervalComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox8_actionPerformed(e);
			}
		});
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				jSpin1_mousePressed(e);
			}
		});
		jPanel2.setLayout(borderLayout1);
		jPanel2.setBorder(BorderFactory.createEtchedBorder());
		JToolBar toolBar = new JToolBar();
		toolBar.add(OneTimeButton, null);
		toolBar.add(TimeTableButton, null);
		toolBar.add(PeriodicButton, null);
		toolBar.addSeparator();
		toolBar.add(AddTestButton, null);
		toolBar.add(SaveTestsButton, null);
		toolBar.addSeparator();
		toolBar.add(KISChoise1Button, null);
		toolBar.add(KISChoise2Button, null);
		toolBar.addSeparator();
		toolBar.add(UsualTestingButton, null);
		toolBar.add(ExtendedTestingButton, null);
		toolBar.addSeparator();
		toolBar.add(FiltrationButton, null);
		toolBar.addSeparator();
		toolBar.add(timeIntervalComboBox, null);
		toolBar.addSeparator();
		toolBar.add(jSpin1, null);
		toolBar.addSeparator();
		toolBar.add(refreshButton, null);
		this.getContentPane().add(jPanel2,  BorderLayout.SOUTH);
		jPanel2.add(jSlider2,  BorderLayout.NORTH);
		testMouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			this_mousePressed (e);
			}
		};
		JScrollPane jScrollPane1 = new JScrollPane();
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		this.getContentPane().add(toolBar, BorderLayout.NORTH);
		jScrollPane1.getViewport().add(mainPanel, null);
		ttt = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;

		JMenu menuParametersTesting = new JMenu(LangModelScheduleOld.String("menuParametersTesting"));
		JMenu menuOperationsTesting = new JMenu(LangModelScheduleOld.String("menuOperationsTesting"));
		JMenu menuTypeTesting = new JMenu(LangModelScheduleOld.String("menuTypeTesting"));
		JMenuBar jMenuBar1 = new JMenuBar();
		jMenuBar1.add(menuOperationsTesting);
		jMenuBar1.add(menuParametersTesting);
		jMenuBar1.add(menuTypeTesting);

		menuOperationsTesting.add(addtest);
		menuOperationsTesting.addSeparator();
		menuOperationsTesting.add(savetest);
		menuOperationsTesting.addSeparator();
		menuOperationsTesting.add(refreshtest);

		menuParametersTesting.add(onetimetest);
		menuParametersTesting.add(timetabletest);
		menuParametersTesting.add(periodictest);
		menuParametersTesting.addSeparator();
		menuParametersTesting.add(KISChoise1);
		menuParametersTesting.add(KISChoise2);

		menuTypeTesting.add(originaltest);
		menuTypeTesting.add(extendedtest);
		menuTypeTesting.addSeparator();
		menuTypeTesting.add(filtertest);

		refreshtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				refreshButton.doClick();
				}
			});
		addtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				AddTestButton.doClick();
				}
			});
		savetest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SaveTestsButton.doClick();
				}
			});
		onetimetest.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OneTimeButton.doClick();
				}
			});
		timetabletest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				TimeTableButton.doClick();
				}
			});
		periodictest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				PeriodicButton.doClick();
				}
			});
		KISChoise1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				KISChoise1Button.doClick();
				}
			});
		KISChoise2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				KISChoise2Button.doClick();
				}
			});
		originaltest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				UsualTestingButton.doClick();
				}
			});
		extendedtest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				ExtendedTestingButton.doClick();
				}
			});
		filtertest.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
				FiltrationButton.doClick();
				}
			});
		this.setJMenuBar(jMenuBar1);

		ButtonGroup bg1 = new ButtonGroup();
		bg1.add(ExtendedTestingButton);
		bg1.add(FiltrationButton);
		bg1.add(UsualTestingButton);

		ButtonGroup bg2 = new ButtonGroup();
		bg2.add(KISChoise1Button);
		bg2.add(KISChoise2Button);

		ButtonGroup bg3 = new ButtonGroup();
		bg3.add(OneTimeButton);
		bg3.add(TimeTableButton);
		bg3.add(PeriodicButton);
	}

	void this_mousePressed (MouseEvent e)
	{
		this.comp = e.getSource();
		buttonX = e.getX();
		buttonY = e.getY();
		if ((comp instanceof NewButton) && ((NewButton)comp).baza_type.equals("BazaTest"))
		{
			Test per_test = (Test )Pool.get("test", ((NewButton )comp).testid);
			internal_dispatcher.notify(new OperationEvent(per_test.test_type_id,0,"TestType"));
			internal_dispatcher.notify(new OperationEvent(per_test,0,"VisualTestParams"));
			internal_dispatcher.notify(new OperationEvent(comp,0,"SelectedButton"));
		}
		if (SwingUtilities.isRightMouseButton(e))
		{
			if (comp instanceof NewButton)
			{
				JPopupMenu popup = new JPopupMenu();
				JMenuItem edit_test = new JMenuItem(LangModelScheduleOld.String("labelPopupEdit"));
				Calendar plan_ca1 = Calendar.getInstance();
				if (drop_coef(buttonX + ((NewButton )comp).getX()) < plan_ca1.getTime().getTime())
				{
					edit_test.setEnabled(false);
				}
				edit_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						edit_test_actionPerformed(e);
					}
				});
				JMenuItem delete_test = new JMenuItem(LangModelScheduleOld.String("labelPopupDel"));
				delete_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delete_test_actionPerformed(e);
					}
				});
				popup.add(edit_test);
				popup.addSeparator();
				popup.add(delete_test);
				popup.show(((NewButton)comp),buttonX,e.getY());
			}
			if (comp instanceof TestPlan)
			{
				JPopupMenu popup = new JPopupMenu();
				JMenuItem add_test = new JMenuItem(LangModelScheduleOld.String("labelPopupAdd"));
				add_test.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						add_test_actionPerformed(e);
					}
				});
				JMenuItem delete_panel = new JMenuItem(LangModelScheduleOld.String("labelPopupDelKIS"));
				delete_panel.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(ActionEvent e) {
						delete_panel_actionPerformed(e);
					}
				});
				popup.add(add_test);
				popup.addSeparator();
				popup.add(delete_panel);
				popup.show(((TestPlan)comp),buttonX,e.getY());
			}
		}
	}

	public void init_module(Dispatcher dispatcher)
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setCommand("myEntry", new VoidCommand());
		aModel.setEnabled("myEntry", true);
		aModel.fireModelChanged("");

		dispatcher.register(this,"AskTimeTestType");
		dispatcher.register(this,"TestSetupChoise");
		dispatcher.register(this,"NavigationTime");
		dispatcher.register(this,"EditTime");
		dispatcher.register(this,"TestReturnType");
		dispatcher.register(this,"AnalysisType");
		dispatcher.register(this,"EvaluationType");
		dispatcher.register(this,"TestType");
		dispatcher.register(this,"KISType");
		dispatcher.register(this,"PortType");
		dispatcher.register(this,"PlanRefresh");
		dispatcher.register(this,"METype");
		dispatcher.register(this,"Parameter");
		dispatcher.register(this,"StartTime");
		dispatcher.register(this,"EndTime");
		dispatcher.register(this,"Interval");
		dispatcher.register(this,"TesttTime");
		dispatcher.register(this,"KISes_and_Ports");
		dispatcher.register(this,"Filtration_parameters");
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
		if(ae.getActionCommand().equals("AskTimeTestType"))
		{
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
		}
		if(ae.getActionCommand().equals("EditTime"))
		{
			edit_Button_func (edit_comp, ((Date )ae.getSource()).getTime());
			My_Refresh();
		}
		if(ae.getActionCommand().equals("TestSetupChoise"))
		{
			test_setup_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("NavigationTime"))
		{
			jSpin1.setValue(new Date(Long.parseLong(ae.getSource().toString())));
		}
		if(ae.getActionCommand().equals("TestReturnType"))
		{
			returntype = (TestReturnType) ae.getSource();
		}
		if(ae.getActionCommand().equals("AnalysisType"))
		{
			analysis_type_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("EvaluationType"))
		{
			evaluation_type_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("TestType"))
		{
			test_type_id = ae.getSource().toString();
			new_parameters.clear();
			if(test_type_id.equals("trace_and_analyse"))
			{
				new_parameters.addElement("1.467");
				new_parameters.addElement("1550");
				new_parameters.addElement("4000");
				new_parameters.addElement("4.096");
				new_parameters.addElement("0.25");
				new_parameters.addElement("100");
			}
			else if (test_type_id.equals("voice_analyse"))
			{
				new_parameters.addElement("1550");
			}
		}
		if(ae.getActionCommand().equals("KISType"))
		{
			kis_id = ae.getSource().toString();
			My_Refresh();
		}
		if(ae.getActionCommand().equals("KISes_and_Ports"))
		{
			KISes_and_ports = (TreeModelClone )ae.getSource();
			number_of_kises = "All";
		}
		if(ae.getActionCommand().equals("Filtration_parameters"))
		{
			filter_test = (DataSet)ae.getSource();
			number_of_kises = "Filt";
			My_Refresh();
		}
		if(ae.getActionCommand().equals("PortType"))
		{
			port_id = ae.getSource().toString();
			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, port_id);
			Hashtable ht = ap.characteristics;
			if (ht.size() != 0)
			{
				warm_up_time = Long.parseLong(((Characteristic) ht.get("warm_up_time")).value);
				cool_down_time = Long.parseLong(((Characteristic) ht.get("cool_down_time")).value);
			}
			else
			{
				warm_up_time = 0;
				cool_down_time = 0;
			}
		}
		if(ae.getActionCommand().equals("METype"))
		{
			me_id = ae.getSource().toString();
		}
		if(ae.getActionCommand().equals("Parameter"))
		{
			new_parameters.clear();
			for (int ii=0; ii < ((Vector)ae.getSource()).size(); ii++)
			{
				new_parameters.add( ((Vector)ae.getSource()).elementAt(ii) );
			}
		}
		if(ae.getActionCommand().equals("StartTime"))
		{
			start_time = ((Date )ae.getSource()).getTime();
		}
		if(ae.getActionCommand().equals("EndTime"))
		{
			end_time = ((Date )ae.getSource()).getTime();
		}
		if(ae.getActionCommand().equals("Interval"))
		{
			interval = Long.parseLong(ae.getSource().toString());
		}
		if(ae.getActionCommand().equals("TesttTime"))
		{
			long tt = ((Date )ae.getSource()).getTime();
			if (temp_times.size() == 0 || tt < Long.parseLong(temp_times.elementAt(0).toString())) {
				start_time = tt;
				temp_times.insertElementAt(String.valueOf(tt), 0);
			}
			else if (tt > Long.parseLong(temp_times.elementAt(temp_times.size() - 1).toString()))
			{
				temp_times.insertElementAt(String.valueOf(tt), temp_times.size());
			}
			else
			{
				for (int i = 1; i < temp_times.size(); i++)
				{
					if (tt > Long.parseLong(temp_times.elementAt(i-1).toString()) && tt < Long.parseLong(temp_times.elementAt(i).toString()) )
					{
						temp_times.insertElementAt(String.valueOf(tt), i);
					}
				}
			}
		}
		if(ae.getActionCommand().equals("PlanRefresh"))
		{
			Vector new_test_ids = (Vector )ae.getSource();
			for (int i = 0; i < new_test_ids.size(); i++)
			{
				baza_test_new.remove((String )new_test_ids.elementAt(i));
			}
			My_Refresh();
		}

		aModel.fireModelChanged("");
	}

	void this_internalFrameActivated(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void this_internalFrameOpened(InternalFrameEvent e)
	{
		this.grabFocus();
	}

	void OneTimeButton_actionPerformed(ActionEvent e)
	{
		if (checker.checkCommand(Checker.setOneTimeTest))
		{
			onetimetest.setSelected(true);
			timetabletest.setSelected(false);
			periodictest.setSelected(false);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
			shag = 0;
		}
	}

	void TimeTableButton_actionPerformed(ActionEvent e) {
		if (checker.checkCommand(Checker.setTestTimeTable))
		{
			onetimetest.setSelected(false);
			timetabletest.setSelected(true);
			periodictest.setSelected(false);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));
			shag = 0;
		}
	}

	void PeriodicButton_actionPerformed(ActionEvent e) {
		if (checker.checkCommand(Checker.setPeriodicTest))
		{
			onetimetest.setSelected(false);
			timetabletest.setSelected(false);
			periodictest.setSelected(true);
			temp_times.clear();
			ttt = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
			internal_dispatcher.notify(new OperationEvent(ttt.toString(),0,"TimeTestType"));

			shag = 0;
		}
	}

	void SaveTestsButton_actionPerformed(ActionEvent e)
	{
		String[] test_ids = new String [baza_test_new.size()];
		mainPanel.removeAll();
		baza_test.clear();
		tps_panel.clear();
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		for (int i = 0; i < baza_test_new.size(); i++)
		{
			Test tt = (Test) baza_test_new.get(i);
			if (!same_tests(tt))
			{
				tt.user_id = aContext.getSessionInterface().getUserId();
				tt.name = test_sdf.format(new Date(System.currentTimeMillis()));
				Pool.put("test", tt.id, tt);
				test_ids[i] = tt.getId();
				if(tt.analysis_id != null && !tt.analysis_id.equals(""))
				{
					dataSource.createAnalysis(tt.analysis_id);
				}
				if(tt.evaluation_id != null && !tt.evaluation_id.equals(""))
				{
					dataSource.createEvaluation(tt.evaluation_id);
				}
			}
		}

		if(test_ids.length != 0)
		{
			TestRequest treq = new TestRequest(dataSource.GetUId("testrequest"));
			treq.user_id = aContext.getSessionInterface().getUserId();
			treq.name = treq.user_id + " at " + test_sdf.format(new Date(System.currentTimeMillis()));
			Pool.put("testrequest", treq.id, treq);
			for (int i = 0; i < test_ids.length; i++)
			{
				Test tt = (Test )Pool.get(Test.typ, test_ids[i]);
				tt.request_id = treq.getId();
				treq.test_ids.add(tt.getId());
			}
			treq.updateLocalFromTransferable();
			dataSource.RequestTest(treq.id, test_ids);
		}
		baza_test_new.clear();
		getTests();
		tps_addButtons(baza_test, "BazaTest");
		internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
		internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
		mainPanel.revalidate();
		setDrag();
	}

	void AddTestButton_actionPerformed(ActionEvent e)
	{
		boolean ext_bool = true;
		boolean int_bool_1 = false;
		boolean int_bool_2 = false;
		if (end_time < start_time && ttt.equals(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL))
		{
			JOptionPane.showMessageDialog ( Environment.getActiveWindow(),LangModelScheduleOld.String("errorWrongTestTime"), LangModelScheduleOld.String("errorWrongTest"), JOptionPane.OK_OPTION);
			ext_bool = false;
		}
		KIS kis = (KIS )Pool.get(KIS.typ, kis_id);
		if (kis != null)
		{
			int i;
			Vector vec = kis.access_ports;
			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, port_id);
			for (i = 0; i < vec.size();)
			{
				if (ap.equals((AccessPort )vec.elementAt(i)))
				{
				 break;
				}
				else i++;
			}
			if (i < vec.size())
			{
				Hashtable ht = Pool.getHash("monitoredelement");
				Hashtable ht2 = new Hashtable();
				for(Enumeration en = ht.elements(); en.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en.nextElement();
					if(me.access_port_id.equals(ap.getId()))
					{
						ht2.put(me.getId(), me);
					}
				}
				for(Enumeration en2 = ht2.elements(); en2.hasMoreElements();)
				{
					MonitoredElement me = (MonitoredElement )en2.nextElement();
					if(me.getId().equals(me_id))
					{
						int_bool_1 = true;
						break;
					}
				}

				AccessPortType apt = (AccessPortType )Pool.get("accessporttype", ap.type_id);
				if(apt.test_type_ids.contains(test_type_id))
				{
					int_bool_2 = true;
				}
			}
		}
		if (ext_bool == true)
		{
			shag = 0;
			DataSourceInterface dataSource = aContext.getDataSourceInterface();
			if (UsualTestingButton.isSelected())
			{
				if (int_bool_1 == true && int_bool_2 == true)
				{
					Make_new_Test( kis_id, port_id, me_id);
					temp_test = new Test("");
					temp_test.id = dataSource.GetUId("test");
					temp_times.clear();
				}
				else
				{
					JOptionPane.showMessageDialog ( Environment.getActiveWindow(),LangModelScheduleOld.String("errorWrongTestObjects"), LangModelScheduleOld.String("errorWrongTest"), JOptionPane.OK_OPTION);
				}
			}
			else if (ExtendedTestingButton.isSelected())
			{
				FilterTreeNode mmtn = (FilterTreeNode )KISes_and_ports.getRoot();
				for(Enumeration enum = mmtn.children(); enum.hasMoreElements();)
				{
					FilterTreeNode kis_mte = (FilterTreeNode )enum.nextElement();
					if (kis_mte.state != 0)
					{
						for(Enumeration enu = kis_mte.children(); enu.hasMoreElements();)
						{
							FilterTreeNode port_mte = (FilterTreeNode )enu.nextElement();
							if (port_mte.state != 0)
							{
								for(Enumeration en = port_mte.children(); en.hasMoreElements();)
								{
									FilterTreeNode volokno_mte = (FilterTreeNode )en.nextElement();
									if (volokno_mte.state == 2)
									{
										Make_new_Test( kis_mte.id, port_mte.id, volokno_mte.id);
										temp_test = new Test("");
										temp_test.id = dataSource.GetUId("test");
										temp_times.clear();
									}
								}
							}
						}
					}
				}
			}
			My_Refresh();
		}
	}

	void  Make_new_Test(String kisid, String portid, String meid)
	{
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		temp_test.test_setup_id = test_setup_id;
		TestArgumentSet as = new TestArgumentSet();
		if (!test_setup_id.equals(""))
		{
			TestSetup ts = (TestSetup )Pool.get(TestSetup.typ, test_setup_id);
			temp_test.test_argument_set_id = ts.test_argument_set_id;
			as = (TestArgumentSet )Pool.get(TestArgumentSet.typ, ts.test_argument_set_id);
			if (!analysis_type_id.equals(""))
			{
				Analysis anal = new Analysis(dsi.GetUId(Analysis.typ));
				anal.monitored_element_id = meid;
				anal.type_id = analysis_type_id;
				anal.criteria_set_id = ts.criteria_set_id;
				temp_test.analysis_id = anal.getId();
				Pool.put(Analysis.typ, anal.getId(), anal);
			}
			else
			{
				temp_test.analysis_id = "";
			}

			if (!evaluation_type_id.equals(""))
			{
				Evaluation eval = new Evaluation(dsi.GetUId(Evaluation.typ));
				eval.monitored_element_id = meid;
				eval.type_id = evaluation_type_id;
				eval.threshold_set_id = ts.threshold_set_id;
				eval.etalon_id = ts.etalon_id;
				temp_test.evaluation_id = eval.getId();
				Pool.put(Evaluation.typ, eval.getId(), eval);
			}
			else
			{
				temp_test.evaluation_id = "";
			}
		}
		temp_test.kis_id = kisid;
		temp_test.monitored_element_id = meid;
		temp_test.test_type_id = test_type_id;
		temp_test.temporal_type = ttt;
		String aa = temp_test.temporal_type.toString();
		temp_test.status = TestStatus.SCHEDULED;
		temp_test.start_time = start_time;
//---------------------------------------------------------------------
		temp_test.return_type = returntype;
		TestTimeStamps tts = new TestTimeStamps();
		switch(temp_test.temporal_type.value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
				tts._default();
				break;
			case TestTemporalType._PERIODICAL:
				int temp_ti = (int)((end_time - start_time) / interval);
				PeriodicalTestParameters ptp = new PeriodicalTestParameters(interval, start_time + temp_ti * interval);
				tts.ptpars(ptp);
				break;
			case  TestTemporalType._TIMETABLE:
				long[] times = new long[temp_times.size()];
				for (int i = 0; i <temp_times.size(); i++)
					times[i] = Long.parseLong(temp_times.elementAt(i).toString());
				tts.ti(times);
				break;
			default:
				System.out.println("ERROR: Unknown temporal type: " + temp_test.temporal_type.value());
		}
		temp_test.time_stamps = tts;

		if (test_setup_id.equals(""))
		{
			as.id = dsi.GetUId(TestArgumentSet.typ);
			Pool.put(TestArgumentSet.typ, as.getId(), as);
			as.name = as.id;
			as.created = 0;
			as.created_by = "";
			as.test_type_id = test_type_id;

			Parameter par_trans;
			if(temp_test.test_type_id.equals("trace_and_analyse"))
			{
				ByteArray ref_ior;
				ByteArray ref_wvlen;
				ByteArray ref_scans;
				ByteArray ref_trclen;
				ByteArray ref_res;
				ByteArray ref_pulswd;
				ActionParameterType apt;
				TestType tttt = (TestType )Pool.get("testtype",temp_test.test_type_id);
				try
				{
					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_ior");
					ref_ior = new ByteArray(Double.parseDouble(new_parameters.elementAt(0).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_ior.getBytes(), "ref_ior", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_wvlen");
					ref_wvlen = new ByteArray(Integer.parseInt(new_parameters.elementAt(1).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_wvlen.getBytes(), "ref_wvlen", "int");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_scans");
					ref_scans = new ByteArray(Double.parseDouble(new_parameters.elementAt(2).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_scans.getBytes(), "ref_scans", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_trclen");
					ref_trclen = new ByteArray(Double.parseDouble(new_parameters.elementAt(3).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_trclen.getBytes(), "ref_trclen", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_res");
					ref_res = new ByteArray(Double.parseDouble(new_parameters.elementAt(4).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_res.getBytes(), "ref_res", "double");
					as.addArgument(par_trans);

					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_pulswd");
					ref_pulswd = new ByteArray(Long.parseLong(new_parameters.elementAt(5).toString()));
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_pulswd.getBytes(), "ref_pulswd", "double");
					as.addArgument(par_trans);
				}
				catch(java.io.IOException ex) {}
			}
			else if (temp_test.test_type_id.equals("voice_analyse"))
			{
				ByteArray ref_characterizationidentity;
				ActionParameterType apt;
				TestType tttt = (TestType )Pool.get("testtype",temp_test.test_type_id);
				try
				{
					apt = (ActionParameterType )tttt.sorted_arguments.get("ref_characterizationidentity");
					ref_characterizationidentity = new ByteArray(new_parameters.elementAt(0).toString());
					par_trans = new Parameter(dsi.GetUId("testargument"), apt.getId(), ref_characterizationidentity.getBytes(), "ref_characterizationidentity", "string");
					as.addArgument(par_trans);
				}
				catch(java.io.IOException ex){}
			}
			dsi.saveTestArgumentSet(as.getId());
			temp_test.test_argument_set_id = as.getId();
		}

//   Длительность теста
		if (temp_test.test_type_id.equals("trace_and_analyse"))
		{
			Vector arg = as.arguments;
			double param1 = 0, param2 = 0, param3 = 0;

			try
			{
				for (int i = 0; i < arg.size(); i++)
				{
					Parameter par = (Parameter )arg.elementAt(i);
					if (par.codename.equals("ref_ior"))
					{
						param1 = (new ByteArray(par.value)).toDouble();
					}
					else if (par.codename.equals("ref_trclen"))
					{
						param2 = (new ByteArray(par.value)).toDouble();
					}
					else if (par.codename.equals("ref_scans"))
					{
						param3 = (new ByteArray(par.value)).toDouble();
					}
					//else if (par.codename.equals("ref_wvlen"))
					//else if (par.codename.equals("ref_trclen"))
					//else if (par.codename.equals("ref_res"))
				}
			}
			catch(java.io.IOException ex) {}

			long t_warm = 0;
			long t_cool = 0;

			AccessPort ap = (AccessPort )Pool.get(AccessPort.typ, portid);
			Hashtable ht = ap.characteristics;
			if (ht.size() != 0)
			{
				t_warm = Long.parseLong(((Characteristic) ht.get("warm_up_time")).value);
				t_cool = Long.parseLong(((Characteristic) ht.get("cool_down_time")).value);
			}

			double wave_speed = 300000000 / param1;
			double izmer_time = (param2 * 1000) / wave_speed;
			long t_work = (long) (izmer_time * param3 * 1000);

			long all_time = t_sys + t_warm + t_cool + t_work;
			temp_test.duration = all_time;
		}
// Окончание счета длительности теста

		Test clone_test = temp_test.myclone();
		baza_test_new.add(clone_test);
	}

	void edit_test_actionPerformed(ActionEvent e)
	{
		edit_comp = comp;
		internal_dispatcher.notify(new OperationEvent( String.valueOf(((NewButton)comp).testtime),0,"EditTest"));
	}

	void delete_test_actionPerformed(ActionEvent e)
	{
		int temp = JOptionPane.showConfirmDialog(Environment.getActiveWindow(), LangModelScheduleOld.String("labelDelTestRealQ"),
				LangModelScheduleOld.String("labelDelTestReal"), JOptionPane.YES_NO_OPTION);
		if (temp == JOptionPane.YES_OPTION)
			delete_test_func(comp);
	}

	void refreshButton_actionPerformed(ActionEvent e)
	{
		My_Refresh();
	}

	void add_test_actionPerformed(ActionEvent e)
	{
		for (int i = 0; i < tps_panel.size(); i++)
		{
			if (this.comp==tps_panel.elementAt(i))
			{
				if (shag >= 1)
				{
					long add_time = drop_coef(buttonX);
					if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
					{
						button_elementary_test.addElement(new NewButton(Color.blue, 2, "MouseTest", false, temp_test.id, shag, add_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
						temp_times.addElement(String.valueOf(add_time));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
						((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
						shag++;
					}
					else if ((ttt == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL) && shag <= 1)
					{
						button_elementary_test.addElement(new NewButton(Color.green, 1, "MouseTest", false, temp_test.id, shag, add_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
						end_time = add_time;
						((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
						((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
						shag++;
					}
				}
				if (shag == 0)
				{
					start_time = drop_coef(buttonX);
					button_global_test.addElement(new NewButton((new JButton()).getBackground(), ttt.value(), "MouseTest", true, temp_test.id, -1, start_time,
						((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					((NewButton)button_global_test.lastElement()).addMouseListener(testMouseListener);
					((TestPlan) comp).add((NewButton)button_global_test.lastElement(),  new XYConstraints(buttonX, 0, test_time_long_const, 10));
					if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
						button_elementary_test.addElement(new NewButton(Color.white, 0, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					else if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
						button_elementary_test.addElement(new NewButton(Color.blue, 2, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					else if (ttt == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
						button_elementary_test.addElement(new NewButton(Color.green, 1, "MouseTest", false, temp_test.id, shag, start_time,
							((TestPlan) comp).kisid, ((TestPlan) comp).portid, ((TestPlan) comp).meid));
					((NewButton)button_elementary_test.lastElement()).setName(temp_test.id+" "+String.valueOf(shag));
					((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
					((TestPlan) comp).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(buttonX, 15, test_time_long_const, 10));
					shag++;
				}
			}
		}
		((TestPlan) comp).revalidate();
		setDrag();
	}

	void delete_panel_actionPerformed(ActionEvent e) {
	for (int i = 0; i < tps_panel.size(); i++)
	{
		if (((TestPlan)comp).equals((TestPlan)tps_panel.elementAt(i)))
		{
			if (((JPanel)comp).getComponentCount()==0)
			{
				tps_panel.removeElementAt(i);
				My_Refresh();
			}
		}
	}
	}

	void getTests()
	{
		DataSourceInterface dataSource = this.aContext.getDataSourceInterface();
		if(dataSource == null)
		 return;
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		baza_test.clear();
		String[] ids = new SurveyDataSourceImage(dataSource).GetTests(drop_coef(0),drop_coef(-1));
		new SurveyDataSourceImage(dataSource).GetAlarms();
		Vector res_ids_vec = new Vector();//1
		for (int i = 0; i < ids.length; i++)
		{
			Test t = (Test )Pool.get(Test.typ, ids[i]);
			baza_test.add(t);
//		  String[] res_ids = t.result_ids;
//		  new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetTestResult(ids[i]);
			for(int j = 0; j < t.result_ids.length; j++)//2
			res_ids_vec.add(t.result_ids[j]);//3
		}
		String[] res_ids = new String[res_ids_vec.size()];//4
		res_ids_vec.copyInto(res_ids);
		new SurveyDataSourceImage(aContext.getDataSourceInterface()).GetResults(res_ids);//5
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	void tps_addButtons(DataSet baza_test, String button_type)
	{
		int delta = 0;
		for (int count = 0; count < baza_test.size(); count++)
		{
			int element_test_id = 0;
			Test temp_t = new Test("");
			if (number_of_kises.equals("All"))
			{
				temp_t = (Test) baza_test.get(count);
			}
			else if (number_of_kises.equals("One"))
			{
				while (count < baza_test.size())
				{
					temp_t = (Test) baza_test.get(count);
					if (temp_t.kis_id.equals(kis_id))
						break;
					count++;
				}
			}
			else if (number_of_kises.equals("Filt"))
			{
				while (count < baza_test.size())
				{
					temp_t = (Test) baza_test.get(count);
					if ((filter_test.get(temp_t.id)) != null)
						break;
					count++;
				}
			}
			if (count == baza_test.size())
				break;

			String portid = "";
			String meid = temp_t.monitored_element_id;

			TransmissionPath path;
			for(Enumeration e = Pool.getHash(TransmissionPath.typ).elements();
				e.hasMoreElements();)
			{
				path = (TransmissionPath )e.nextElement();
				if(path.monitored_element_id.equals(temp_t.monitored_element_id))
				{
					portid = path.access_port_id;
					break;
				}
			}

			test_time_long = detalize_long_to_int(temp_t.duration);
			int i = 0;
				while ( i < tps_panel.size())
				{
					if ( ((TestPlan)tps_panel.elementAt(i)).kisid.equals(temp_t.kis_id)&& ((TestPlan)tps_panel.elementAt(i)).portid.equals(portid)
						&&((TestPlan)tps_panel.elementAt(i)).meid.equals(meid))
						break;
					i++;
				}
				if (i==tps_panel.size())
				{
					tps_panel.addElement(new TestPlan(aContext, internal_dispatcher, timeIntervalComboBox, temp_t.kis_id, portid, meid));
					((TestPlan)tps_panel.elementAt(i)).addMouseListener(testMouseListener);
					mainPanel.add((TestPlan)tps_panel.elementAt(i), new XYConstraints(0, 40*i, -1, -1));
				}

			if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
			{
				long start = temp_t.start_time;
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 0, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(testMouseListener);
				button_color = Alarm_Check_Color(Color.white, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 0, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
			{
				long start = temp_t.start_time;
				long end = temp_t.time_stamps.ptpars().end_time;
				long interval = temp_t.time_stamps.ptpars().dt;
				long temp_time = interval;
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 1, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(testMouseListener);
				button_color = Alarm_Check_Color( Color.green, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 1, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
				while(start + temp_time <= end)
				{
					element_test_id++;
					if (detalize(start+temp_time)==true)
					{
						GregorianCalendar cala = new GregorianCalendar();
						delta = detalize_coef(start+temp_time);
						button_color = Alarm_Check_Color( Color.green, temp_t, start + temp_time, false);
						button_elementary_test.addElement(new NewButton(button_color, 1, button_type, false, temp_t.id, element_test_id, start+temp_time, temp_t.kis_id, portid, meid));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
						((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
					}
					temp_time+=interval;
				}
			 }
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
			{
				long start = temp_t.start_time;
				long time_massiv[] = temp_t.time_stamps.ti();
				delta = detalize_coef(start);
				Color button_color = Alarm_Check_Color( (new JButton()).getBackground(), temp_t, start, true);
				button_global_test.addElement(new NewButton(button_color, 2, button_type, true, temp_t.id, -1, start, temp_t.kis_id, portid, meid));
				((NewButton)button_global_test.lastElement()).addMouseListener(testMouseListener);
				button_color = Alarm_Check_Color( Color.blue, temp_t, start, false);
				button_elementary_test.addElement(new NewButton(button_color, 2, button_type, false, temp_t.id, 0, start, temp_t.kis_id, portid, meid));
				((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_global_test.lastElement(),  new XYConstraints(10 + delta, 0, test_time_long_const, 10));
				((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
				for (int j = 0; j < time_massiv.length; j++)
				{
					element_test_id++;
					if (detalize(time_massiv[j])==true)
					{
						GregorianCalendar cala = new GregorianCalendar();
						delta = detalize_coef(time_massiv[j]);
						button_color = Alarm_Check_Color( Color.blue, temp_t, time_massiv[j], false);
						button_elementary_test.addElement(new NewButton(button_color, 2, button_type, false, temp_t.id, element_test_id, time_massiv[j], temp_t.kis_id, portid, meid));
						((NewButton)button_elementary_test.lastElement()).addMouseListener(testMouseListener);
						((TestPlan)tps_panel.elementAt(i)).add((NewButton)button_elementary_test.lastElement(),  new XYConstraints(10 + delta, 15, test_time_long, 10));
					}
				}
			}
		}
	}
	void setDrag()
 {
 // Внутренние классы для поддержки Drag and Drop
 class ViewTransferable implements Transferable
 {
	DataFlavor[] flavors = new DataFlavor[]{DataFlavor.stringFlavor};
	public ViewTransferable()
	{
	}
	public DataFlavor[] getTransferDataFlavors()
	{
	 return flavors;
	}
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
	 if (flavor.equals(flavors[0]))
	 {
	return true;
	 }
	 return false;
	}
	public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException
	{
	 if (!isDataFlavorSupported(flavor))
	 {
	System.out.println("unsuported flavor");
	return null;
	 }
	 if (flavor.equals(flavors[0]))
	 {
	return(null);
	 }
	 return null;
	}
 }
 class CanvasDragSource implements DragGestureListener, DragSourceListener
 {
	DragSource dragSource = null;
	CanvasDragSource()
	{
	 dragSource = DragSource.getDefaultDragSource();
	 Calendar plan_ca = Calendar.getInstance();
	 long current_time = plan_ca.getTime().getTime();
	 for (int i = 0; i < button_global_test.size(); i++)
	if ( ((NewButton)button_global_test.elementAt(i)).testtime > current_time)
		dragSource.createDefaultDragGestureRecognizer((NewButton)button_global_test.elementAt(i),DnDConstants.ACTION_COPY_OR_MOVE,this);
	 for (int i = 0; i < button_elementary_test.size(); i++)
	if ( ((NewButton)button_elementary_test.elementAt(i)).testtime > current_time)
		dragSource.createDefaultDragGestureRecognizer((NewButton)button_elementary_test.elementAt(i),DnDConstants.ACTION_COPY_OR_MOVE,this);
	 }
	public void dragGestureRecognized(DragGestureEvent dge)
	{
	 buttonX = (int) dge.getComponent().getX();
	 buttonY = (int) dge.getComponent().getY();
	 Transferable transferable = new ViewTransferable();
	 dge.startDrag(null,transferable,this);
	}
	public void dropActionChanged(DragSourceDragEvent dsde){}
	public void dragEnter(DragSourceDragEvent dsde){}
	public void dragOver(DragSourceDragEvent dsde){}
	public void dragExit(DragSourceEvent dse){}
	public void dragDropEnd(DragSourceDropEvent dsde)
	{
		My_Refresh();
	}
 }
 class CanvasDropTarget implements DropTargetListener
 {
	DropTarget[] dropTarget= new DropTarget[tps_panel.size()];
	boolean acceptableType;
	CanvasDropTarget()
	{
	for (int i = 0; i < tps_panel.size(); i++) {
		dropTarget[i] = new DropTarget((TestPlan)tps_panel.elementAt(i), DnDConstants.ACTION_COPY_OR_MOVE,this,true,null);
	}
	}
	public void dragOver(DropTargetDragEvent dtde)
	{
	 acceptOrRejectDrag(dtde);
	}
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
	 acceptOrRejectDrag(dtde);
	}
	public void dragExit(DropTargetEvent dte){}
	public void drop(DropTargetDropEvent dtde)
	{
	 if ((dtde.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0)
	 {
		dtde.acceptDrop(dtde.getDropAction());
		dtde.getDropTargetContext().dropComplete(true);
		int x = (int) dtde.getLocation().getX();
		//double y = dtde.getLocation().getY();
		drop_Button_func (comp, x);
	 }
	 else
	 {
	dtde.rejectDrop();
	 }
	}
	public void dragEnter(DropTargetDragEvent dtde)
	{
	 checkTransferType(dtde);
	 acceptOrRejectDrag(dtde);
	}
	boolean acceptOrRejectDrag(DropTargetDragEvent dtde)
	{
	 int dropAction = dtde.getDropAction();
	 int sourceActions = dtde.getSourceActions();
	 boolean acceptedDrag = false;
	 if (!acceptableType || (sourceActions &DnDConstants.ACTION_COPY_OR_MOVE) == 0)
	 {
		 dtde.acceptDrag(DnDConstants.ACTION_COPY);
		 acceptedDrag = true;
	 }
	 else if ((dropAction & DnDConstants.ACTION_COPY_OR_MOVE ) == 0)
	 {
		 dtde.acceptDrag(DnDConstants.ACTION_COPY);
		 acceptedDrag = true;
	 }
	 return acceptedDrag;
	}
	void checkTransferType(DropTargetDragEvent dtde)
	{
		acceptableType = dtde.isDataFlavorSupported(DataFlavor.stringFlavor);
	}
 }
// new CanvasDragSource();
// new CanvasDropTarget();
 }

 void drop_Button_func (Object comp, int x) {
	DataSourceInterface dataSource = aContext.getDataSourceInterface();
	int w = 0;
	boolean type_of_panel_is_tps_panel_new = true;
	if (((NewButton)comp).global==true)
		{
		for (int i = 0; i < button_global_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_global_test.elementAt(i)))
			{
				((NewButton)button_global_test.elementAt(i)).setLocation((int)x,2);
				String temp1 = ((NewButton)button_global_test.elementAt(i)).testid;
				for (int j = 0; j < button_elementary_test.size(); j++)
				{
					String temp2 = ((NewButton)button_elementary_test.elementAt(j)).testid;
					if (temp1.equals(temp2))
					{
						int loc = ((NewButton)button_elementary_test.elementAt(j)).getX();
						((NewButton)button_elementary_test.elementAt(j)).setLocation(loc + (x - buttonX),17);
					}
				}
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = drop_coef(x);
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 0)
					{
						TestTimeStamps tts = new TestTimeStamps();
						tts._default();
						move_test.time_stamps = tts;
					}
					if ( ((NewButton)comp).int_tempor_type == 1)
					{
						move_test.time_stamps.ptpars().end_time += time_step;
					}
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
					internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
					baza_test_new.remove(((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = drop_coef(x);
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 1)
						move_test.time_stamps.ptpars().end_time += time_step;
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					baza_test_new.add(move_test);
					internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
				}
				break;
			}
		}
		}
		else
		{
			for (int i = 0; i < button_elementary_test.size(); i++)
			{
			if (((NewButton)comp).equals((NewButton)button_elementary_test.elementAt(i)))
			{
			String temp1 = ((NewButton)button_elementary_test.elementAt(i)).testid;
			if (((NewButton)comp).int_tempor_type==0)
			{
						((NewButton)button_elementary_test.elementAt(i)).setLocation((int)x,17);
						for (int j = 0; j < button_global_test.size(); j++)
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1))
						{
							int loc = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc + (x - buttonX),2);
							break;
						}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							move_test.start_time = drop_coef(x);
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							baza_test_new.remove(((NewButton)comp).testid);
							move_test.start_time = drop_coef(x);
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}
					}

					if (((NewButton)comp).int_tempor_type==2)
					{
						((NewButton)button_elementary_test.elementAt(i)).setLocation((int)x,17);
						for (int j = 0; j < button_global_test.size(); j++)
						{
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1)&&
							((NewButton)button_elementary_test.elementAt(i)).count > 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = drop_coef(x);
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = drop_coef(x);
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								baza_test_new.add(move_test);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1)&&
							((NewButton)button_elementary_test.elementAt(i)).count == 0)
						{
							int loc = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc + (x - buttonX),2);
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								move_test.start_time = drop_coef(x);
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								move_test.start_time = drop_coef(x);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						int loc_glob = 0;
						int loc_elem = 0;
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp1))
							{
							loc_glob = ((NewButton)button_global_test.elementAt(j)).getX();
							((NewButton)button_global_test.elementAt(j)).setLocation(loc_glob + (x - buttonX),2);
						}
						for (int j = 0; j < button_elementary_test.size(); j++)
							if (((NewButton)button_elementary_test.elementAt(j)).testid.equals(temp1))
							{
							loc_elem = ((NewButton)button_elementary_test.elementAt(j)).getX();
							((NewButton)button_elementary_test.elementAt(j)).setLocation(loc_elem + (x - buttonX),17);
						}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							long b_time = move_test.start_time;
							move_test.start_time = drop_coef(loc_glob + (x - buttonX));
							long time_step = move_test.start_time - b_time;
							if ( ((NewButton)comp).int_tempor_type == 1)
							{
								move_test.time_stamps.ptpars().end_time += time_step;
							}
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							baza_test_new.remove(((NewButton)comp).testid);
							long b_time = move_test.start_time;
							move_test.start_time = drop_coef(x);
							long time_step = move_test.start_time - b_time;
							if ( ((NewButton)comp).int_tempor_type == 1)
							{
								move_test.time_stamps.ptpars().end_time += time_step;
							}
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}

					}

					break;
					}
				}
			}
 }

 void edit_Button_func (Object comp, long edit_time) {
	DataSourceInterface dataSource = aContext.getDataSourceInterface();
	int w = 0;
	if (((NewButton)comp).global==true)
		{
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = edit_time;
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 0)
					{
						TestTimeStamps tts = new TestTimeStamps();
						tts._default();
						move_test.time_stamps = tts;
					}
					if ( ((NewButton)comp).int_tempor_type == 1)
					{
						move_test.time_stamps.ptpars().end_time += time_step;
					}
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
					internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
					baza_test_new.remove(((NewButton)comp).testid);
					long b_time = move_test.start_time;
					move_test.start_time = edit_time;
					long time_step = move_test.start_time - b_time;
					if ( ((NewButton)comp).int_tempor_type == 1)
						move_test.time_stamps.ptpars().end_time += time_step;
					else if ( ((NewButton)comp).int_tempor_type == 2)
					{
						long[] move_times = move_test.time_stamps.ti();
						for (int a = 0; a < move_times.length; a++)
							move_times[a] += time_step;
						TestTimeStamps tts = new TestTimeStamps();
						tts.ti(move_times);
						move_test.time_stamps = tts;
					}
					baza_test_new.add(move_test);
					internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
				}
		}
		else
		{
			if (((NewButton)comp).int_tempor_type==0)
			{
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							move_test.start_time = edit_time;
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							TestTimeStamps tts = new TestTimeStamps();
							tts._default();
							move_test.time_stamps = tts;
							baza_test_new.remove(((NewButton)comp).testid);
							move_test.start_time = edit_time;
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}
					}

					if (((NewButton)comp).int_tempor_type==2)
					{
						for (int j = 0; j < button_global_test.size(); j++)
						{
						if ( ((NewButton)comp).count > 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = edit_time;
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								long[] move_times = move_test.time_stamps.ti();
								move_times[((NewButton)comp).count-1] = edit_time;
								TestTimeStamps tts = new TestTimeStamps();
								tts.ti(move_times);
								move_test.time_stamps = tts;
								baza_test_new.add(move_test);
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						if ( ((NewButton)comp).count == 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
								move_test.start_time = edit_time;
								dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
								internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
								baza_test_new.remove(((NewButton)comp).testid);
								move_test.start_time = edit_time;
								internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
							}
						}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							Test move_test = (Test) baza_test.get( ((NewButton)comp).testid);
							long b_time = ((NewButton)comp).testtime;
							long time_step = b_time - edit_time;
							move_test.start_time -= time_step;
							move_test.time_stamps.ptpars().end_time -= time_step;
							dataSource.UpdateTests(new String[] {((NewButton)comp).testid});
							internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							Test move_test = (Test) baza_test_new.get( ((NewButton)comp).testid);
							baza_test_new.remove(((NewButton)comp).testid);
							long b_time = ((NewButton)comp).testtime;
							long time_step = b_time - edit_time;
							move_test.start_time -= time_step;
							move_test.time_stamps.ptpars().end_time -= time_step;
							baza_test_new.add(move_test);
							internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
						}

					}
		}
 }
	void delete_test_func(Object comp)
	{
		if ( ((NewButton)comp).global==true)
		{
		for (int i = 0; i < button_global_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_global_test.elementAt(i)))
			{
				int k = 0;
				String temp1 = ((NewButton)button_global_test.elementAt(i)).kisid;
				String temp2 = ((NewButton)button_global_test.elementAt(i)).portid;
				String temp3 = ((NewButton)button_global_test.elementAt(i)).meid;
				String temp4 = ((NewButton)button_global_test.elementAt(i)).testid;
				((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(i));
				button_global_test.removeElementAt(i);
				i--;
				for (int j = 0; j < button_elementary_test.size(); j++)
				{
					String temp5 = ((NewButton)button_elementary_test.elementAt(j)).testid;
					if (temp5.equals(temp4))
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(j));
						button_elementary_test.removeElementAt(j);
						j--;
					}
				}
				if ( ((NewButton)comp).baza_type.equals("BazaTest"))
				{
					DataSourceInterface dataSource = aContext.getDataSourceInterface();
					dataSource.RemoveTests(new String[] {temp4});
					Pool.remove("test",temp4);
				}
				else if ( ((NewButton)comp).baza_type.equals("NewTest"))
				{
					baza_test_new.remove(temp4);
				}
				else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
				{
					break;
				}
			}
		}
		}
		else
		{
		for (int i = 0; i < button_elementary_test.size(); i++)
		{
			if (((NewButton)comp).equals((NewButton)button_elementary_test.elementAt(i)))
				{
					int k = 0;
					String temp1 = ((NewButton)button_elementary_test.elementAt(i)).kisid;
					String temp2 = ((NewButton)button_elementary_test.elementAt(i)).portid;
					String temp3 = ((NewButton)button_elementary_test.elementAt(i)).meid;
					String temp4 = ((NewButton)button_elementary_test.elementAt(i)).testid;
					int temp5 = ((NewButton)button_elementary_test.elementAt(i)).count;

					if (((NewButton)comp).int_tempor_type==0)
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(i));
						button_elementary_test.removeElementAt(i);
						i--;
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(j));
								button_global_test.removeElementAt(j);
								j--;
							}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							DataSourceInterface dataSource = aContext.getDataSourceInterface();
							dataSource.RemoveTests(new String[] {temp4});
							Pool.remove("test",temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							baza_test_new.remove(temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
							break;
						}
					}
					if (((NewButton)comp).int_tempor_type==2)
					{
						((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(i));
						button_elementary_test.removeElementAt(i);
						i--;
						Test del_test = (Test) Pool.get("test", temp4);
						if (temp5 == 0)
						{
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								DataSourceInterface dataSource = aContext.getDataSourceInterface();
								dataSource.RemoveTests(new String[] {temp4});
								Pool.remove("test",temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								baza_test_new.remove(temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
								break;
							}
						}
						else
						{
							long[] del_times = del_test.time_stamps.ti();
							int p = 0;
							long[] new_del_times = new long [del_times.length - 1];
							for (int q = 0; q < del_times.length; q++)
								if (q!=temp5-1)
									new_del_times[p++] = del_times[q];
							TestTimeStamps tts = new TestTimeStamps();
							tts.ti(new_del_times);
							del_test.time_stamps = tts;
							if ( ((NewButton)comp).baza_type.equals("BazaTest"))
							{
								DataSourceInterface dataSource = aContext.getDataSourceInterface();
								dataSource.UpdateTests(new String[] {temp4});
							}
							else if ( ((NewButton)comp).baza_type.equals("NewTest"))
							{
								baza_test_new.remove(temp4);
							}
							else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
							{
								break;
							}
						}
					}
					if (((NewButton)comp).int_tempor_type==1)
					{
						for (int j = 0; j < button_global_test.size(); j++)
							if (((NewButton)button_global_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_global_test.elementAt(j));
								button_global_test.removeElementAt(j);
								j--;
							}
						for (int j = 0; j < button_elementary_test.size(); j++)
							if (((NewButton)button_elementary_test.elementAt(j)).testid.equals(temp4))
							{
								((TestPlan)tps_panel.elementAt(k)).remove((NewButton)button_elementary_test.elementAt(j));
								button_elementary_test.removeElementAt(j);
								j--;
							}
						if ( ((NewButton)comp).baza_type.equals("BazaTest"))
						{
							DataSourceInterface dataSource = aContext.getDataSourceInterface();
							dataSource.RemoveTests(new String[] {temp4});
							Pool.remove("test",temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("NewTest"))
						{
							baza_test_new.remove(temp4);
						}
						else if ( ((NewButton)comp).baza_type.equals("MouseTest"))
						{
							break;
						}
					}
				}
		}
		}
		My_Refresh();
	}

	void ExtendedTestingButton_actionPerformed(ActionEvent e) {
		number_of_kises = "All";
		KISChoise1Button.setEnabled(false);
		KISChoise2Button.setSelected(true);
		originaltest.setSelected(false);
		extendedtest.setSelected(true);
		filtertest.setSelected(false);
		if (testing_id==0)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveTreeFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"ExtendedAfterUsual_RootFrame"));
		}
		else if (testing_id==2)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveFiltrationFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"ExtendedAfterFiltration_RootFrame"));
		}
		testing_id = 1;
		My_Refresh();
	}

	void FiltrationButton_actionPerformed(ActionEvent e) {
		KISChoise1Button.setEnabled(false);
		KISChoise2Button.setSelected(true);
		originaltest.setSelected(false);
		extendedtest.setSelected(false);
		filtertest.setSelected(true);
		filter_test.add(baza_test);
		filter_test.add(baza_test_new);
		if (testing_id==0)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"Remove3aFrame"));
			internal_dispatcher.notify(new OperationEvent(filter_test,0,"FiltrationAfterUsual_RootFrame"));
		}
		else if (testing_id==1)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveExtendedFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveTimeFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveParamFrame"));
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveSaveFrame"));
			internal_dispatcher.notify(new OperationEvent(filter_test,0,"FiltrationAfterExtended_RootFrame"));
		}
		testing_id = 2;
	}

	void KISChoise1Button_actionPerformed(ActionEvent e) {
		KISChoise1Button.setSelected(true);
		KISChoise1.setSelected(true);
		KISChoise2.setSelected(false);
		number_of_kises = "One";
		My_Refresh();
	}

	void KISChoise2Button_actionPerformed(ActionEvent e) {
		KISChoise2Button.setSelected(true);
		KISChoise1.setSelected(false);
		KISChoise2.setSelected(true);
		number_of_kises = "All";
		My_Refresh();
	}

	void UsualTestingButton_actionPerformed(ActionEvent e) {
		number_of_kises = "All";
		UsualTestingButton.setSelected(true);
		originaltest.setSelected(true);
		extendedtest.setSelected(false);
		filtertest.setSelected(false);
		KISChoise1Button.setEnabled(true);
		if (testing_id==1)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveExtendedFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"UsualAfterExtended_RootFrame"));
		}
		else if (testing_id==2)
		{
			internal_dispatcher.notify(new OperationEvent("",0,"RemoveFiltrationFrame"));
			internal_dispatcher.notify(new OperationEvent(this,0,"UsualAfterFiltration_RootFrame"));
		}
		testing_id = 0;
	}

	boolean detalize (long det_time)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl;
		long ftl;
		boolean det_ok = true;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23,59,59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			if ((det_time >= btl )&&(det_time <= ftl ) )
				det_ok = true;
			else det_ok = false;
		}
		return det_ok;
	}

	void slider_coef()
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		plan_ca1.setTime(date);
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH),
						 plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY) + jSlider2.getValue(),0,0);
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH),
						 plan_ca1.get(Calendar.DAY_OF_MONTH)+jSlider2.getValue(),0,0,0);
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH) + jSlider2.getValue(),
						 1, 0, 0, 0);
		}
		jSpin1.setValue(plan_ca1.getTime());
	}

	int detalize_coef(long my_time)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl = 0;
		long ftl = 0;
		float f_coord = 0;
		int delta = 0;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 960;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23,59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 984;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 992;
		}
		return delta = (int)(f_coord * (float)(my_time - btl )/(ftl - btl) );
	}

	int detalize_long_to_int(long my_time)
	{
		int delta = 0;
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			delta = (int) ((my_time * 16) / (60 * 1000));
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			delta = (int) ((my_time * 41) / (60 * 60 * 1000));
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			delta = (int) ((my_time * 32) / (24 * 60 * 60 * 1000));
		}

		if (delta < test_time_long_const)
		{
			delta = test_time_long_const;
		}
		return delta;
	}

	long drop_coef(int my_coord)
	{
		Date date = (Date )jSpin1.getValue();
		Calendar plan_ca1 = Calendar.getInstance();
		Calendar plan_ca2 = Calendar.getInstance();
		long btl = 0;
		long ftl = 0;
		int f_coord = 0;
		long delta = 0;
		plan_ca1.setTime(date);
		plan_ca2.setTime(date);
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),plan_ca1.get(Calendar.HOUR_OF_DAY),0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),plan_ca2.get(Calendar.HOUR_OF_DAY),59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 960;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), plan_ca1.get(Calendar.DAY_OF_MONTH),0 ,0,0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca2.get(Calendar.DAY_OF_MONTH),23 ,59,59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 984;
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			plan_ca1.set(plan_ca1.get(Calendar.YEAR), plan_ca1.get(Calendar.MONTH), 1, 0, 0, 0);
			btl = plan_ca1.getTime().getTime();
			plan_ca2.set(plan_ca2.get(Calendar.YEAR), plan_ca2.get(Calendar.MONTH), plan_ca1.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			ftl = plan_ca2.getTime().getTime();
			f_coord = 992;
		}
		if (my_coord == -1)
			 my_coord = f_coord;
		return delta = btl + (long)( my_coord * (float)(ftl-btl)/f_coord );
	}

	void My_Refresh()
	{
		if (checker.checkCommand(Checker.refreshTest))
		{
			slider_coef();
			aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestRefresh")));
			mainPanel.removeAll();
			tps_panel.clear();
			button_global_test.clear();
			button_elementary_test.clear();
			getTests();
			tps_addButtons(baza_test_new, "NewTest");
			tps_addButtons(baza_test, "BazaTest");
			internal_dispatcher.notify(new OperationEvent(baza_test,0,"TestChanged"));
			internal_dispatcher.notify(new OperationEvent(baza_test_new,0,"NewTestChanged"));
			mainPanel.repaint();
			mainPanel.revalidate();
			setDrag();
			internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"TestPlan_Time"));
			aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusTestRefreshFinish")));
			jSlider2.setValue(0);
		}
	}

	boolean same_tests(Test tt)
	{
		boolean flag = false;
		/*getTests();
		for (int i = 0; i < baza_test.size(); i++)
		{
			Test temp_t = (Test )baza_test.get(i);
			if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)
			{
				if ((temp_t.start_time + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
				{
					flag = false;
				}
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)
			{
				long start = temp_t.start_time;
				long end = temp_t.time_stamps.ptpars().end_time;
				long interval = temp_t.time_stamps.ptpars().dt;
				long temp_time = interval;
			}
			else if (temp_t.temporal_type == TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)
			{
				long start = temp_t.start_time;
				if ((temp_t.start_time + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
				{
					flag = false;
				}
				long time_massiv[] = temp_t.time_stamps.ti();
				for (int j = 0; j < time_massiv.length; j++)
				{
					if ((time_massiv[j] + temp_t.duration > tt.start_time) && (tt.start_time + tt.duration > temp_t.start_time))
					{
						 flag = false;
					}
				}
			}
		}/
		return flag;
	}

	Color Alarm_Check_Color(Color button_color, Test temp_t, long start, boolean global)
	{
		if (global == true)
		{
			if (temp_t.elementary_test_alarms.length !=0 )
			{
				ElementaryTestAlarm[] eta = temp_t.elementary_test_alarms;
				int a = 0;
				while (a < eta.length)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, eta[a].alarm_id);
					if(alarm != null)
					{
						if(alarm.type_id.equals("rtutestalarm"))
						{
							return Color.red;
						}
						else if(alarm.type_id.equals("rtutestwarning"))
							 button_color = Color.yellow;
					}
					a++;
				}
			}
		}
		else
		{
			for (int a = 0; a < temp_t.elementary_test_alarms.length; a++)
			{
				if (temp_t.elementary_test_alarms[a].elementary_start_time == start)
				{
					Alarm alarm = (Alarm )Pool.get(Alarm.typ, temp_t.elementary_test_alarms[a].alarm_id);
					if(alarm != null)
					{
						if(alarm.type_id.equals("rtutestalarm"))
						{
							button_color = Color.red;
						}
						else if(alarm.type_id.equals("rtutestwarning"))
						{
							button_color = Color.yellow;
						}
					}
					break;
				}
			}
		}
		return button_color;
	}

	void jComboBox8_actionPerformed(ActionEvent e)
	{
		if (this.timeIntervalComboBox.getSelectedIndex()==0)
		{
			jSlider2.setMinimum(-24);
			jSlider2.setMaximum(24);
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==1)
		{
			jSlider2.setMinimum(-31);
			jSlider2.setMaximum(31);
		}
		if (this.timeIntervalComboBox.getSelectedIndex()==2)
		{
			jSlider2.setMinimum(-12);
			jSlider2.setMaximum(12);
		}
		My_Refresh();
	}

	void jSpin1_mousePressed(MouseEvent e) {
		FontMetrics fm = jTextField1.getFontMetrics(jTextField1.getFont());
		String temp = test_sdf.format(jSpin1.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl >= x)
			{
				if ( i >= 0 && i < 3)
				{
					sdm.setCalendarField(Calendar.DAY_OF_MONTH);
					jTextField1.select(0,2);
					break;
				}
				else if ( i >= 3 && i < 6)
				{
					sdm.setCalendarField(Calendar.MONTH);
					jTextField1.select(3,5);
					break;
				}
				else if ( i >= 6 && i < 11)
				{
					sdm.setCalendarField(Calendar.YEAR);
					jTextField1.select(6,10);
					break;
				}
				else if ( i >= 11 && i < 14)
				{
					sdm.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField1.select(11,13);
					break;
				}
				else if ( i >= 14 && i < 17)
				{
					sdm.setCalendarField(Calendar.MINUTE);
					jTextField1.select(14,16);
					break;
				}
				else
				{
					sdm.setCalendarField(Calendar.SECOND);
					jTextField1.select(17,19);
					break;
				}
			}
		}
	}
}




*/
