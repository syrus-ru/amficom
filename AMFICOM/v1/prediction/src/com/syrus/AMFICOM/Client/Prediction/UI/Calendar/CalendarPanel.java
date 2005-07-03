package com.syrus.AMFICOM.Client.Prediction.UI.Calendar;

import java.text.SimpleDateFormat;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.UI.AComboBox;
import oracle.jdeveloper.layout.*;

public class CalendarPanel extends JPanel
{
	public static int width  = 299;
	public static int height = 125;

	SimpleDateFormat sdf_hh_mm_ss = new SimpleDateFormat("HH:mm:ss");
	boolean editing = false;
	String[] mmonth = {"\u042F\u043D\u0432\u0430\u0440\u044C","\u0424\u0435\u0432\u0440\u0430\u043B\u044C","\u041C\u0430\u0440\u0442","\u0410\u043F\u0440\u0435\u043B\u044C","\u041C\u0430\u0439","\u0418\u044E\u043D\u044C","\u0418\u044E\u043B\u044C","\u0410\u0432\u0433\u0443\u0441\u0442",
				"\u0421\u0435\u043D\u0442\u044F\u0431\u0440\u044C","\u041E\u043A\u0442\u044F\u0431\u0440\u044C","\u041D\u043E\u044F\u0431\u0440\u044C","\u0414\u0435\u043A\u0430\u0431\u0440\u044C",};
	String[] yyear = {"2002","2003","2004","2005","2006","2007","2008","2009","2010",};
	String[] dday = {"1","2","3","4","5","6","7","8","9","10",
				"11","12","13","14","15","16","17","18","19","20",
				"21","22","23","24","25","26","27","28","29","30","31"};
	Calendar cal = Calendar.getInstance();
	int day_temp = Integer.parseInt(cal.getTime().toString().substring(8,10));

	boolean initial_init = true;
	private JPanel calendarPanel = new JPanel();
	private AComboBox comboBoxDay = new AComboBox(dday);
	private AComboBox comboBoxMonth = new AComboBox(mmonth);
	private MonthCanvas canva = new MonthCanvas(cal,day_temp,calendarPanel,comboBoxDay);
	private AComboBox comboBoxYear = new AComboBox(yyear);
	private XYLayout xYLayout1 = new XYLayout();


	public CalendarPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{

		this.setLayout(xYLayout1);
		this.setBorder(BorderFactory.createEtchedBorder());
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		xYLayout1.setWidth(width);
		xYLayout1.setHeight(height);
		calendarPanel.setBorder(BorderFactory.createEtchedBorder());


		cal = Calendar.getInstance();
		comboBoxDay.setSelectedIndex(day_temp-1);
		comboBoxDay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			comboBoxDay_actionPerformed(e);
			}
		});
		comboBoxMonth.setSelectedIndex(cal.get(Calendar.MONTH));
		comboBoxMonth.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			comboBoxMonth_actionPerformed(e);
			}
		});
		comboBoxYear.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
		comboBoxYear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
			comboBoxYear_actionPerformed(e);
			}
		});
		comboBoxYear.setSize(10, -1);
		this.add(calendarPanel,               new XYConstraints(85, 1, 208, 116));
		calendarPanel.add(canva, null);
		comboBoxYear.getSize();
		this.add(comboBoxMonth,    new XYConstraints(1, 48, 83, -1));
		this.add(comboBoxYear,     new XYConstraints(1, 10, 83, -1));
		this.add(comboBoxDay,      new XYConstraints(1, 86, 83, -1));
		GregorianCalendar tt_new = new GregorianCalendar(Integer.parseInt(comboBoxYear.getSelectedItem().toString()),
				comboBoxMonth.getSelectedIndex(), comboBoxDay.getSelectedIndex()+1);
	}



	public long getSelectedTime()
	{
		GregorianCalendar tt_new2 = new GregorianCalendar(Integer.parseInt(comboBoxYear.getSelectedItem().toString()),
				comboBoxMonth.getSelectedIndex(), comboBoxDay.getSelectedIndex()+1);

		return tt_new2.getTime().getTime();
	}


	void comboBoxMonth_actionPerformed(ActionEvent e) {
	day_temp=comboBoxDay.getSelectedIndex()+1;
		calendarPanel.removeAll(); ;
		cal.set(Integer.parseInt(comboBoxYear.getSelectedItem().toString()),
			comboBoxMonth.getSelectedIndex(), day_temp);
		MonthCanvas canva = new MonthCanvas(cal,day_temp, calendarPanel, comboBoxDay);
		calendarPanel.add(canva);
		calendarPanel.revalidate();
	}

	void comboBoxDay_actionPerformed(ActionEvent e) {
		day_temp=comboBoxDay.getSelectedIndex()+1;
		calendarPanel.removeAll();
		cal.set(Integer.parseInt(comboBoxYear.getSelectedItem().toString()),
			comboBoxMonth.getSelectedIndex(), day_temp);
		MonthCanvas canva = new MonthCanvas(cal,day_temp, calendarPanel, comboBoxDay);
		calendarPanel.add(canva);
		calendarPanel.revalidate();
	}

	void comboBoxYear_actionPerformed(ActionEvent e) {
		day_temp=comboBoxDay.getSelectedIndex()+1;
		calendarPanel.removeAll();
		cal.set(Integer.parseInt(comboBoxYear.getSelectedItem().toString()),
			comboBoxMonth.getSelectedIndex(), day_temp);
		MonthCanvas canva = new MonthCanvas(cal,day_temp, calendarPanel, comboBoxDay);
		calendarPanel.add(canva);
		calendarPanel.revalidate();
	}

}
///////////////////////////////////////////////////////////////////////////////


class MonthCanvas extends Container {
	static String[] monthNames;
	static String[] weekNames;
	int day_temp;
	JPanel jPanel2;
	AComboBox jComboBox6;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public int width = screenSize.width;
	public int height = screenSize.height;

	public MonthCanvas(Calendar cal, int day_temp, JPanel jPanel2, AComboBox jComboBox6) {
	this.jPanel2 = jPanel2;
	this.day_temp=day_temp;
	this.jComboBox6 = jComboBox6;
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
	p1.setPreferredSize( new Dimension(width * 160 / 1280, height * 90 / 1024));

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
			p1.add(new DayCanvas(c, day_temp, jPanel2, jComboBox6));
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
	JPanel jPanel2;
	AComboBox jComboBox6;

	DayCanvas(Calendar cal, int day_temp, JPanel jPanel2, AComboBox jComboBox6) {
	this.jPanel2 = jPanel2;
	this.jComboBox6 = jComboBox6;
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

	day_temp=cal.get(fieldValues[3]);
	jComboBox6.setSelectedIndex(day_temp-1);
	jPanel2.removeAll(); ;
	MonthCanvas canva = new MonthCanvas(cal,day_temp, jPanel2, jComboBox6);
	jPanel2.add(canva);
	jPanel2.revalidate();
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
}