package com.syrus.AMFICOM.Client.Schedule.UI;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Schedule.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class MyTimeFrame extends JInternalFrame
		implements OperationListener
{
	String spin_pressed = "left";
	Dispatcher internal_dispatcher;
	ApplicationContext aContext;
	ScheduleMDIMain parent;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	SimpleDateFormat sdf_ = new SimpleDateFormat("HH:mm:ss");
	int testtype = 0;
	boolean editing = false;
	Calendar cal = Calendar.getInstance();
	int day_temp = cal.get(Calendar.DAY_OF_MONTH);
	public static IniFile iniFile;
	static String iniFileName = "My.properties";
	boolean initial_init = true;
	private JScrollPane jScrollPane1 = new JScrollPane();
	public JPanel jPanel4 = new JPanel();
	private JPanel jPanel3 = new JPanel();
	public JPanel jPanel2 = new JPanel();
	private MonthCanvas canva = new MonthCanvas(cal,day_temp,this);
	SpinnerDateModel sdm1 = new SpinnerDateModel();
	SpinnerDateModel sdm2 = new SpinnerDateModel();
	SpinnerDateModel sdm3 = new SpinnerDateModel();
	public JSpinner jSpin1 = new JSpinner(sdm1);
	public JSpinner jSpin2 = new JSpinner(sdm2);
	public JSpinner jSpin3 = new JSpinner(sdm3);
	JFormattedTextField jTextField3;
	JFormattedTextField jTextField2;
	JFormattedTextField jTextField1;
	private BorderLayout borderLayout1 = new BorderLayout();
	private GridLayout gridLayout1 = new GridLayout();
	private BorderLayout borderLayout2 = new BorderLayout();
	private BorderLayout borderLayout3 = new BorderLayout();
	private JPanel jPanel1 = new JPanel();
	private BorderLayout borderLayout4 = new BorderLayout();
	private JPanel jPanel5 = new JPanel();
	private BorderLayout borderLayout5 = new BorderLayout();
	private JLabel jLabel1 = new JLabel();
	private JPanel jPanel6 = new JPanel();
	private BorderLayout borderLayout6 = new BorderLayout();
	private JPanel jPanel7 = new JPanel();
	private BorderLayout borderLayout7 = new BorderLayout();
	private JPanel jPanel8 = new JPanel();
	private BorderLayout borderLayout8 = new BorderLayout();
	private JPanel jPanel9 = new JPanel();
	private BorderLayout borderLayout9 = new BorderLayout();
	private JPanel jPanel10 = new JPanel();
	private BorderLayout borderLayout10 = new BorderLayout();
	private JPanel jPanel11 = new JPanel();
	private BorderLayout borderLayout11 = new BorderLayout();
	private JLabel jLabel5 = new JLabel();
	private JPanel jPanel12 = new JPanel();
	private BorderLayout borderLayout12 = new BorderLayout();
	private JPanel jPanel13 = new JPanel();
	private BorderLayout borderLayout13 = new BorderLayout();
	private JPanel jPanel14 = new JPanel();
	private JPanel jPanel15 = new JPanel();
	private BorderLayout borderLayout14 = new BorderLayout();
	private BorderLayout borderLayout15 = new BorderLayout();
	private JLabel jLabel3 = new JLabel();
	private JPanel jPanel16 = new JPanel();
	private BorderLayout borderLayout16 = new BorderLayout();
	private JPanel jPanel17 = new JPanel();
	private BorderLayout borderLayout17 = new BorderLayout();
	private JButton jButton1 = new JButton();

	public MyTimeFrame(ScheduleMDIMain parent, ApplicationContext aContext)
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
		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		javax.swing.JSpinner.DateEditor de1 = (javax.swing.JSpinner.DateEditor )jSpin1.getEditor();
		jTextField1 = de1.getTextField();
		javax.swing.JSpinner.DateEditor de2 = (javax.swing.JSpinner.DateEditor )jSpin2.getEditor();
		jTextField2 = de2.getTextField();
		javax.swing.JSpinner.DateEditor de3 = (javax.swing.JSpinner.DateEditor )jSpin3.getEditor();
		jTextField3 = de3.getTextField();
		jTextField1.setEditable(true);
		jTextField2.setEditable(true);
		jTextField3.setEditable(true);
		DateFormatter formatter = new DateFormatter(sdf);
		DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
		DateFormatter formatter_ = new DateFormatter(sdf_);
		DefaultFormatterFactory factory_ = new DefaultFormatterFactory(formatter_);
		jTextField1.setFormatterFactory(factory);
		jTextField2.setFormatterFactory(factory);
		jTextField3.setFormatterFactory(factory_);
		internal_dispatcher=parent.getInternalDispatcher();
		internal_dispatcher.register(this,"RemoveTimeFrame");
		internal_dispatcher.register(this,"Remove3aFrame");
		internal_dispatcher.register(this,"TimeTestType");
		internal_dispatcher.register(this,"EditTest");
		internal_dispatcher.register(this,"TesttTime");
		testtype = 1;
		this.setClosable(true);
		this.setIconifiable(true);
		this.setResizable(true);
		this.setTitle(LangModelScheduleOld.String("MyTimeTitle"));
		this.getContentPane().setLayout(borderLayout1);
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
		jPanel4.setBorder(BorderFactory.createEtchedBorder());
		jPanel4.setLayout(borderLayout3);
		jPanel3.setLayout(gridLayout1);
		jPanel3.setBorder(BorderFactory.createEtchedBorder());
		jPanel2.setBorder(BorderFactory.createEtchedBorder());
		jPanel2.setLayout(borderLayout2);
		sdm3.setCalendarField(Calendar.MINUTE);
		jSpin3.setValue(new Date(-10200000));
		jSpin3.setVisible(false);
		cal = Calendar.getInstance();
		jTextField2.setValue(cal.getTime());
		jSpin2.setVisible(false);
		jTextField1.setValue(cal.getTime());
		jLabel5.setVisible(false);
		jLabel3.setVisible(false);
		jButton1.setVisible(false);
		jSpin3.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jSpin3_stateChanged(e);
			}
		});
		jSpin1.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jSpin1_stateChanged(e);
			}
		});
		jSpin2.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				jSpin2_stateChanged(e);
			}
		});
		jPanel1.setLayout(borderLayout4);
		jPanel5.setLayout(borderLayout5);
		jLabel1.setText(LangModelScheduleOld.String("labelBegin"));
		jPanel6.setLayout(borderLayout6);
		jPanel7.setLayout(borderLayout7);
		jPanel8.setLayout(borderLayout8);
		jPanel9.setLayout(borderLayout9);
		jPanel10.setLayout(borderLayout10);
		jPanel11.setLayout(borderLayout11);
		jLabel5.setText(LangModelScheduleOld.String("labelEnd"));
		jPanel12.setLayout(borderLayout12);
		jPanel13.setLayout(borderLayout13);
		jPanel14.setLayout(borderLayout14);
		jPanel15.setLayout(borderLayout15);
		jLabel3.setText(LangModelScheduleOld.String("labelInterval"));
		jPanel16.setLayout(borderLayout16);
		jPanel17.setLayout(borderLayout17);
		jButton1.setText(LangModelScheduleOld.String("Enter"));
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButton1_actionPerformed(e);
			}
		});
		this.getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jPanel3, null);
		jPanel3.add(jPanel4, null);
		jPanel4.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(jLabel1,  BorderLayout.CENTER);
		jPanel4.add(jPanel5, BorderLayout.CENTER);
		jPanel5.add(jPanel6, BorderLayout.NORTH);
		jPanel5.add(jPanel7, BorderLayout.CENTER);
		jPanel7.add(jPanel8, BorderLayout.NORTH);
		jPanel7.add(jPanel9, BorderLayout.CENTER);
		jPanel9.add(jPanel10, BorderLayout.NORTH);
		jPanel10.add(jLabel5,  BorderLayout.CENTER);
		jPanel10.add(jButton1,  BorderLayout.WEST);
		jPanel9.add(jPanel11, BorderLayout.CENTER);
		jPanel11.add(jPanel12, BorderLayout.NORTH);
		jPanel11.add(jPanel13, BorderLayout.CENTER);
		jPanel13.add(jPanel14, BorderLayout.NORTH);
		jPanel14.add(jLabel3,  BorderLayout.CENTER);
		jPanel13.add(jPanel15, BorderLayout.CENTER);
		jPanel15.add(jPanel16, BorderLayout.NORTH);
		jPanel15.add(jPanel17, BorderLayout.CENTER);
		jPanel3.add(jPanel2, null);
		jPanel2.add(canva, BorderLayout.NORTH);
		jPanel8.add(jSpin1,  BorderLayout.CENTER);
		jPanel12.add(jSpin2,  BorderLayout.CENTER);
		jPanel16.add(jSpin3, BorderLayout.CENTER);
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Spin1_mousePressed(e);
			}
		});
		jTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Spin2_mousePressed(e);
			}
		});
		jTextField3.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Spin3_mousePressed(e);
			}
		});
		internal_dispatcher.notify(new OperationEvent("",0,"AskTimeTestType"));
		internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"StartTime"));
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
		if (ae.getActionCommand().equals("EditTest"))
		{
			jSpin1.setValue(new Date( Long.parseLong(ae.getSource().toString()) ));
			jButton1.setVisible(true);
			jSpin2.setVisible(false);
			jSpin3.setVisible(false);
			jLabel3.setVisible(false);
			jLabel5.setVisible(false);
			jLabel1.setText(LangModelScheduleOld.String("labelEdit"));
			editing = true;
		}
		if(ae.getActionCommand().equals("RemoveTimeFrame"))
		{
			this.dispose();
		}
		if(ae.getActionCommand().equals("Remove3aFrame"))
		{
			this.dispose();
		}
		if(ae.getActionCommand().equals("TimeTestType"))
		{
			jButton1.setVisible(false);
			jLabel1.setText(LangModelScheduleOld.String("labelBegin"));
			jSpin1.setValue(Calendar.getInstance().getTime());
			jSpin2.setValue(Calendar.getInstance().getTime());
			if (ae.getSource().toString().equals("TEST_TEMPORAL_TYPE_ONETIME"))
			{
				jSpin2.setVisible(false);
				jSpin3.setVisible(false);
				jLabel3.setVisible(false);
				jLabel5.setVisible(false);
				testtype = 1;
				internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"StartTime"));
			}
				else if (ae.getSource().toString().equals("TEST_TEMPORAL_TYPE_TIMETABLE"))
				{
					jButton1.setVisible(true);
					jSpin2.setVisible(false);
					jSpin3.setVisible(false);
					jLabel3.setVisible(false);
					jLabel5.setVisible(false);
					testtype = 2;
					//internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"StartTime"));
				}
				else if (ae.getSource().toString().equals("TEST_TEMPORAL_TYPE_PERIODICAL"))
				{
					jSpin2.setVisible(true);
					jSpin3.setVisible(true);
					jLabel3.setVisible(true);
					jLabel5.setVisible(true);
					testtype = 3;
					internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"StartTime"));
					internal_dispatcher.notify(new OperationEvent(jSpin2.getValue(),0,"EndTime"));
					String vremya = jTextField3.getText();
					int dt = (Integer.parseInt(vremya.substring(0,2))*60*60 +
							Integer.parseInt(vremya.substring(3,5))*60 +
							Integer.parseInt(vremya.substring(6,8)))*1000;
					internal_dispatcher.notify(new OperationEvent(String.valueOf(dt),0,"Interval"));
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

	void jSpin1_stateChanged(ChangeEvent e) {
		if (testtype!=2)
		{
			internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"StartTime"));
		}

		Date date = (Date )jSpin1.getValue();
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		day_temp=ca.get(Calendar.DAY_OF_MONTH);
		jPanel2.removeAll();
		cal.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), day_temp);
		MonthCanvas canva = new MonthCanvas(cal,day_temp, this);
		jPanel2.add(canva);
		jPanel2.revalidate();
	}

	void jSpin2_stateChanged(ChangeEvent e) {
		if (jSpin2.isVisible())
		{
			internal_dispatcher.notify(new OperationEvent(jSpin2.getValue(),0,"EndTime"));
			Date date = (Date )jSpin2.getValue();
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			day_temp=ca.get(Calendar.DAY_OF_MONTH);
			jPanel2.removeAll();
			cal.set(ca.get(Calendar.YEAR), ca.get(Calendar.MONTH), day_temp);
			MonthCanvas canva = new MonthCanvas(cal,day_temp, this);
			jPanel2.add(canva);
			jPanel2.revalidate();
		}
	}

	void jSpin3_stateChanged(ChangeEvent e) {
		Date date = (Date )jSpin3.getValue();
		int dt = (date.getHours()*60*60 +
				date.getMinutes()*60 +
				date.getSeconds())*1000;
		internal_dispatcher.notify(new OperationEvent(String.valueOf(dt),0,"Interval"));
	}

	void Spin1_mousePressed(MouseEvent e) {
		spin_pressed = "left";
		FontMetrics fm = jTextField1.getFontMetrics(jTextField1.getFont());
		String temp = sdf.format(jSpin1.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl > x)
			{
				if ( i >= 0 && i < 5)
				{
					sdm1.setCalendarField(Calendar.YEAR);
					jTextField1.select(0,4);
					break;
				}
				else if ( i >= 5 && i < 8)
				{
					sdm1.setCalendarField(Calendar.MONTH);
					jTextField1.select(5,7);
					break;
				}
				else if ( i >= 8 && i < 11)
				{
					sdm1.setCalendarField(Calendar.DAY_OF_MONTH);
					jTextField1.select(8,10);
					break;
				}
				else if ( i >= 11 && i < 14)
				{
					sdm1.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField1.select(11,13);
					break;
				}
				else if ( i >= 14 && i < 17)
				{
					sdm1.setCalendarField(Calendar.MINUTE);
					jTextField1.select(14,16);
					break;
				}
				else
				{
					sdm1.setCalendarField(Calendar.SECOND);
					jTextField1.select(17,19);
					break;
				}
			}
		}
	}

	void Spin2_mousePressed(MouseEvent e) {
		spin_pressed = "right";
		FontMetrics fm = jTextField2.getFontMetrics(jTextField2.getFont());
		String temp = sdf.format(jSpin2.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl > x)
			{
				if ( i >= 0 && i < 5)
				{
					sdm2.setCalendarField(Calendar.YEAR);
					jTextField2.select(0,4);
					break;
				}
				else if ( i >= 5 && i < 8)
				{
					sdm2.setCalendarField(Calendar.MONTH);
					jTextField2.select(5,7);
					break;
				}
				else if ( i >= 8 && i < 11)
				{
					sdm2.setCalendarField(Calendar.DAY_OF_MONTH);
					jTextField2.select(8,10);
					break;
				}
				else if ( i >= 11 && i < 14)
				{
					sdm2.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField2.select(11,13);
					break;
				}
				else if ( i >= 14 && i < 17)
				{
					sdm2.setCalendarField(Calendar.MINUTE);
					jTextField2.select(14,16);
					break;
				}
				else
				{
					sdm2.setCalendarField(Calendar.SECOND);
					jTextField2.select(17,19);
					break;
				}
			}
		}
	}

	void Spin3_mousePressed(MouseEvent e) {
		FontMetrics fm = jTextField3.getFontMetrics(jTextField3.getFont());
		String temp = sdf.format(jSpin3.getValue());
		int x = e.getX();
		int xl = 0;
		for (int i = 0; i < temp.length(); i++)
		{
			xl = fm.stringWidth(temp.substring(0, i+1));
			if (xl > x)
			{
				if ( i >= 0 && i < 2)
				{
					sdm3.setCalendarField(Calendar.HOUR_OF_DAY);
					jTextField3.select(0,2);
					break;
				}
				else if ( i >= 3 && i < 5)
				{
					sdm3.setCalendarField(Calendar.MINUTE);
					jTextField3.select(3,5);
					break;
				}
				else
				{
					sdm3.setCalendarField(Calendar.SECOND);
					jTextField3.select(6,8);
					break;
				}
			}
		}
	}

	void jButton1_actionPerformed(ActionEvent e) {
		if (editing == false)
		{
			internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"TesttTime"));
		}
		else
		{
			internal_dispatcher.notify(new OperationEvent(jSpin1.getValue(),0,"EditTime"));
			editing = false;
			if (testtype == 1)
			{
				internal_dispatcher.notify(new OperationEvent("OneTime",0,"TimeTestType"));
			}
			if (testtype == 2)
			{
				internal_dispatcher.notify(new OperationEvent("TimeTable",0,"TimeTestType"));
			}
			if (testtype == 3)
			{
				internal_dispatcher.notify(new OperationEvent("Periodical",0,"TimeTestType"));
			}
		}
	}
}

class MonthCanvas extends Container {
	static String[] monthNames;
	static String[] weekNames;
	int day_temp;
	MyTimeFrame mtf;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public int width = screenSize.width;
	public int height = screenSize.height;

	public MonthCanvas(Calendar cal, int day_temp, MyTimeFrame mtf) {
	this.mtf = mtf;
	this.day_temp=day_temp;
	setLayout(new BorderLayout());

	// Add the month label.
	JPanel p = new JPanel(new GridLayout(1, 1));

	// Add the day of the week labels.
	JPanel q = new JPanel(new GridLayout(0, weekNames.length));
	for (int i=0; i<weekNames.length; i++) {
		JLabel l = new JLabel(weekNames[i], JLabel.CENTER);
		q.setBackground(Color.gray);
		q.add(l);
	}
	p.add(q);
	add(p, BorderLayout.NORTH);

	// Create a grid for the days.
	JPanel p1 = new JPanel(new GridLayout(0, weekNames.length));
	p1.setPreferredSize( new Dimension(width * 140 / 1280, height * 100 / 1024));

	// Get the day of the week of the first day.
	cal.set(Calendar.DAY_OF_MONTH, 1);
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
	int fday = cal.getFirstDayOfWeek();
	if (fday > dayOfWeek)
		dayOfWeek += 7;

	// Fill the first few cells with blanks.
	for (int i = fday; i < dayOfWeek; i++) {
		p1.add(new JLabel(" ",JLabel.CENTER));
	}

	// Add the days.
	for (int i=cal.getMinimum(Calendar.DAY_OF_MONTH);
		i<=cal.getMaximum(Calendar.DAY_OF_MONTH); i++)
		{
		Calendar c = Calendar.getInstance();
		c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i);

		// Make sure we haven't skipped to the next month.
		if (c.get(Calendar.MONTH) != cal.get(Calendar.MONTH))
		{
			break;
		}
			p1.add(new DayCanvas(c, day_temp, mtf));
		}
	add(p1, BorderLayout.CENTER);
	}

	static {
	Calendar cal = Calendar.getInstance();

	// Roll the calendar until it uses the first month.
	while (cal.get(Calendar.MONTH) != cal.getMinimum(Calendar.MONTH)) {
		cal.roll(Calendar.MONTH, false);
	}

	// Initialize day of week names.  The first element contains the name
	// of getFirstDayOfWeek().
	weekNames = new String[cal.getMaximum(Calendar.DAY_OF_WEEK) -
					 cal.getMinimum(Calendar.DAY_OF_WEEK)+1];

	// Roll the calendar until it uses the first day of week.
	while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
		cal.roll(Calendar.DAY_OF_WEEK, false);
	}

	// Use SimpleDateFormat to fill weekNames.
	for (int i=0; i<weekNames.length; i++) {
		SimpleDateFormat sdf = new SimpleDateFormat("E");
		weekNames[i] = sdf.format(cal.getTime());

		// Only take the first two characters.
		if (weekNames[i].length() > 2)
		{
		if (weekNames[i].substring(0, 2).equals("Mo"))
			weekNames[i]="\u041F\u043D";
		if (weekNames[i].substring(0, 2).equals("Tu"))
			weekNames[i]="\u0412\u0442";
		if (weekNames[i].substring(0, 2).equals("We"))
			weekNames[i]="\u0421\u0440";
		if (weekNames[i].substring(0, 2).equals("Th"))
			weekNames[i]="\u0427\u0442";
		if (weekNames[i].substring(0, 2).equals("Fr"))
			weekNames[i]="\u041F\u0442";
		if (weekNames[i].substring(0, 2).equals("Sa"))
			weekNames[i]="\u0421\u0431";
		if (weekNames[i].substring(0, 2).equals("Su"))
			weekNames[i]="\u0412\u0441";
		if (weekNames[i].length() > 2)
		weekNames[i] = weekNames[i].substring(0, 2);
		}
		cal.roll(Calendar.DAY_OF_WEEK, true);
	}
	}
}

class DayCanvas extends Component implements MouseListener {
	int day;
	int day_temp;
	Calendar cal;
	MyTimeFrame mtf;

	DayCanvas(Calendar cal, int day_temp, MyTimeFrame mtf) {
	this.mtf = mtf;
	this.cal = cal;
	this.day_temp = day_temp;
	addMouseListener(this);
	}

	public void paint(Graphics g) {
	super.paint(g);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int week = cal.get(Calendar.DAY_OF_WEEK);
	int month = cal.get(Calendar.MONTH);
	int year = cal.get(Calendar.YEAR);
	int hour = cal.get(Calendar.HOUR);
	int minute = cal.get(Calendar.MINUTE);
	int second = cal.get(Calendar.SECOND);

	FontMetrics fm = g.getFontMetrics();

	if (day==day_temp)
		g.setColor(Color.blue);
	else
		g.setColor(Color.black);
	g.drawString(""+day,
		(getSize().width-fm.stringWidth(""+day))/2,
		(getSize().height-fm.getHeight())/2+fm.getAscent());
	}

	// Event handling methods.
	int[] fieldValues = {
	Calendar.ERA, Calendar.YEAR, Calendar.MONTH,
	Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK,
	Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.DAY_OF_YEAR,
	Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_YEAR,
	Calendar.HOUR, Calendar.HOUR_OF_DAY, Calendar.AM_PM,
	Calendar.MILLISECOND, Calendar.MINUTE, Calendar.SECOND,
	Calendar.ZONE_OFFSET, Calendar.DST_OFFSET,};
	String[] fieldNames = {
	"ERA", "YEAR", "MONTH",
	"DAY_OF_MONTH", "DAY_OF_WEEK",
	"DAY_OF_WEEK_IN_MONTH", "DAY_OF_YEAR",
	"WEEK_OF_MONTH", "WEEK_OF_YEAR",
	"HOUR", "HOUR_OF_DAY", "AM_PM",
	"MILLISECOND", "MINUTE", "SECOND",
	"ZONE_OFFSET", "DST_OFFSET", };

	public void mousePressed(MouseEvent e) {
	/*System.out.println("---------------------------------");
	for (int i=0; i<Calendar.FIELD_COUNT; i++) {
		System.out.print(fieldNames[i] + "=" + cal.get(fieldValues[i]));
		System.out.println("        ("+cal.getMaximum(fieldValues[i])+")");
	}*/
		day_temp=cal.get(fieldValues[3]);
		if (mtf.spin_pressed.equals("left"))
		{
			mtf.jSpin1.setValue(cal.getTime());
			mtf.jSpin1_stateChanged(new ChangeEvent(e));
		}
		else
		{
			mtf.jSpin2.setValue(cal.getTime());
			mtf.jSpin2_stateChanged(new ChangeEvent(e));
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}