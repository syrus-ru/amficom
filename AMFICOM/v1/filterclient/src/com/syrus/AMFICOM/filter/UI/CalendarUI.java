/*-
 * $Id: CalendarUI.java,v 1.3 2005/06/17 12:38:54 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.filter.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.syrus.AMFICOM.client.resource.LangModel;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/17 12:38:54 $
 * @author $Author: bass $
 * @module filterclient_v1
 */
public class CalendarUI extends JPanel {
	private static final long serialVersionUID = 3257845476471485492L;

	public static final int STATUS_CANCEL = 0;
	public static final int STATUS_OK = 1;
	public Dimension btnSize = new Dimension(24, 24);

	protected MonthCanvas mCanvas;
	protected SimpleDateFormat sdf = new SimpleDateFormat("MMMMMMMMM yyyy");

	protected JButton lessButton = new JButton("-");
	protected JButton moreButton = new JButton("+");
	protected JLabel monthLabel = new JLabel();

	protected JPanel monthPanel;
	protected JPanel todayPanel;

	protected static JDialog dialog;
	protected static JFrame frame;
	protected static JInternalFrame internalFrame;
	protected int status = STATUS_CANCEL;

	protected CalendarUI(Calendar cal)
	{
		try
		{
			jbInit();
		} catch(Exception e)
		{
			e.printStackTrace();
		}

		this.mCanvas.init(this, cal);
		this.monthLabel.setText(" " + this.sdf.format(getCalendar().getTime()) + LangModel.getString("YearPostfix"));
	}

	public static JDialog createDialogInstance(Calendar cal, boolean hideOnChoose) {
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog() {
			public void dispose() {
				dialog = null;
				super.dispose();
			}
		};
		dialog.setSize(new Dimension(190, 190));
		dialog.setTitle(LangModel.getString("CalendarTitle"));
		dialog.setContentPane(instance);
		instance.setHideOnChoose(hideOnChoose);
		return dialog;
	}

	public static JDialog createDialogInstance(Frame owner, Calendar cal, boolean hideOnChoose) {
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner) {
			public void dispose() {
				dialog = null;
				super.dispose();
			}
		};
		dialog.setSize(new Dimension(190, 190));
		dialog.setTitle(LangModel.getString("CalendarTitle"));
		dialog.setContentPane(instance);
		instance.setHideOnChoose(hideOnChoose);
		return dialog;
	}

	public static JDialog createDialogInstance(Frame owner, Calendar cal, boolean hideOnChoose, boolean modal) {
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner) {
			public void dispose() {
				dialog = null;
				super.dispose();
			}
		};
		dialog.setModal(modal);
		// dialog.setSize(new Dimension(190, 190));
		dialog.pack();
		dialog.setTitle(LangModel.getString("CalendarTitle"));
		dialog.setContentPane(instance);
		instance.setHideOnChoose(hideOnChoose);
		return dialog;
	}

	public static JDialog createDialogInstance(Dialog owner, Calendar cal, boolean hideOnChoose) {
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner) {
			public void dispose() {
				dialog = null;
				super.dispose();
			}
		};
		dialog.setContentPane(instance);
		dialog.setSize(new Dimension(190, 190));
		dialog.setTitle(LangModel.getString("CalendarTitle"));
		instance.setHideOnChoose(hideOnChoose);
		return dialog;
	}

	public static JDialog createDialogInstance(Dialog owner, Calendar cal, boolean hideOnChoose, boolean modal) {
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner) {
			public void dispose() {
				dialog = null;
				super.dispose();
			}
		};
		dialog.setModal(modal);
		dialog.setSize(new Dimension(190, 190));
		dialog.setTitle(LangModel.getString("CalendarTitle"));
		dialog.setContentPane(instance);
		instance.setHideOnChoose(hideOnChoose);
		return dialog;
	}

	public static JFrame createFrameInstance(Calendar cal, boolean hideOnChoose) {
		CalendarUI instance = new CalendarUI(cal);
		frame = new JFrame() {
			public void dispose() {
				frame = null;
				super.dispose();
			}
		};
		frame.setSize(new Dimension(190, 190));
		frame.setTitle(LangModel.getString("CalendarTitle"));
		frame.setContentPane(instance);
		instance.setHideOnChoose(hideOnChoose);
		return frame;
	}

	public static JInternalFrame createInternalFrameInstance(Calendar cal) {
		CalendarUI instance = new CalendarUI(cal);
		internalFrame = new JInternalFrame();
		internalFrame.setSize(new Dimension(190, 190));
		internalFrame.setTitle(LangModel.getString("CalendarTitle"));
		internalFrame.setContentPane(instance);
		return internalFrame;
	}

	public static JPanel createPanelInstance(Calendar cal) {
		CalendarUI instance = new CalendarUI(cal);
		return instance;
	}

	private void jbInit() throws Exception {
		setLayout(new BorderLayout());

		this.lessButton.setFocusable(false);
		this.lessButton.setBorder(BorderFactory.createEtchedBorder());
		this.lessButton.setPreferredSize(this.btnSize);
		this.lessButton.setMinimumSize(this.btnSize);
		this.lessButton.setMinimumSize(this.btnSize);
		this.moreButton.setFocusable(false);
		this.moreButton.setBorder(BorderFactory.createEtchedBorder());
		this.moreButton.setPreferredSize(this.btnSize);
		this.moreButton.setMinimumSize(this.btnSize);
		this.moreButton.setMinimumSize(this.btnSize);
		this.lessButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				decrease();
			}
		});
		this.moreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				increase();
			}
		});

		// monthPanel = new JPanel(new FlowLayout());
		this.monthPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new FlowLayout());
		labelPanel.add(this.monthLabel);
		this.monthPanel.add(this.lessButton, BorderLayout.WEST);
		this.monthPanel.add(labelPanel, BorderLayout.CENTER);
		this.monthPanel.add(this.moreButton, BorderLayout.EAST);

		this.mCanvas = new MonthCanvas();

		this.todayPanel = new JPanel(new FlowLayout());
		this.todayPanel.add(new JLabel(LangModel.getString("Today")
				+ ": "
				+ new SimpleDateFormat("dd.MM.yyyy").format(MonthCanvas.todayCal.getTime())
				+ LangModel.getString("YearPostfix")), BorderLayout.SOUTH);

		add(this.monthPanel, BorderLayout.NORTH);
		add(this.mCanvas, BorderLayout.CENTER);
		add(this.todayPanel, BorderLayout.SOUTH);
	}

	public Calendar getCalendar() {
		return this.mCanvas.monthCal;
	}

	public void setCalendar(Calendar cal) {
		this.mCanvas.monthCal = cal;
		this.mCanvas.reinit();
	}

	public int getStatus() {
		return this.status;
	}

	public void increase() {
		Calendar cal = getCalendar();
		if (cal.get(Calendar.MONTH) == cal.getMaximum(Calendar.MONTH)) {
			cal.set(Calendar.MONTH, cal.getMinimum(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		} else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);

		this.mCanvas.reinit();
		this.monthLabel.setText(" " + this.sdf.format(cal.getTime()) + LangModel.getString("YearPostfix"));
	}

	public void decrease() {
		Calendar cal = getCalendar();
		if (cal.get(Calendar.MONTH) == cal.getMinimum(Calendar.MONTH)) {
			cal.set(Calendar.MONTH, cal.getMaximum(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		} else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

		this.mCanvas.reinit();
		this.monthLabel.setText(" " + this.sdf.format(cal.getTime()) + LangModel.getString("YearPostfix"));
	}

	public void setMonthVisible(boolean b) {
		this.monthPanel.setVisible(b);
	}

	public void setTodayVisible(boolean b) {
		this.todayPanel.setVisible(b);
	}

	public void setHideOnChoose(boolean b) {
		this.mCanvas.hideOnChoose = b;
	}

	void okClose() {
		this.status = CalendarUI.STATUS_OK;
		if (dialog != null)
			dialog.dispose();
		else
			if (frame != null)
				frame.dispose();
	}
}

class MonthCanvas extends Container
{
	static ImageIcon today_icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/cal_today.gif"));
	static String[] monthNames;
	static String[] weekNames;
	static int today;
	static Calendar todayCal = Calendar.getInstance();

	boolean hideOnChoose = false;
	CalendarUI monthParent;
	Calendar monthCal;
	JPanel calPanel;

	void init(CalendarUI panel, Calendar cal)
	{
		this.monthParent = panel;
		this.monthCal = cal;
		reinit();
	}

	void reinit()
	{
		if (todayCal.get(Calendar.MONTH) == this.monthCal.get(Calendar.MONTH)
			&& todayCal.get(Calendar.YEAR) == this.monthCal.get(Calendar.YEAR))
			today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		else
			today = 0;

		// Add the month label.
		removeAll();
		setLayout(new BorderLayout());
		JPanel p = new JPanel(new GridLayout(1, 1));

		// Add the day of the week labels.
		JPanel q = new JPanel(new GridLayout(0, weekNames.length));
		for (int i=0; i<weekNames.length; i++)
		{
			JLabel l = new JLabel(weekNames[i], SwingConstants.CENTER);
			q.setBackground(Color.gray);
			q.add(l);
		}
		p.add(q);
		add(p, BorderLayout.NORTH);

		// Create a grid for the days.
		this.calPanel = new JPanel(new GridLayout(0, weekNames.length));

		// Get the day of the week of the first day.
		int temp_day = this.monthCal.get(Calendar.DAY_OF_MONTH);
		this.monthCal.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = this.monthCal.get(Calendar.DAY_OF_WEEK);
		int fday = this.monthCal.getFirstDayOfWeek();
		if (fday > dayOfWeek)
			dayOfWeek += 7;

		this.monthCal.set(Calendar.DAY_OF_MONTH, temp_day);
		// Fill the first few cells with blanks.
		for (int i = fday; i < dayOfWeek; i++) {
			this.calPanel.add(new JLabel(" ", SwingConstants.CENTER));
		}

		// Add the days.
		for (int i = this.monthCal.getMinimum(Calendar.DAY_OF_MONTH); i <= this.monthCal.getMaximum(Calendar.DAY_OF_MONTH); i++)
		{
			Calendar c = Calendar.getInstance();
			c.set(this.monthCal.get(Calendar.YEAR), this.monthCal.get(Calendar.MONTH), i);

			// Make sure we haven't skipped to the next month.
			if (c.get(Calendar.MONTH) != this.monthCal.get(Calendar.MONTH))
			{
				break;
			}
			this.calPanel.add(new DayCanvas(c, this));
		}
		add(this.calPanel, BorderLayout.CENTER);

		repaint();
	}

	void close()
	{
		this.monthParent.okClose();
	}

	static
	{
		Calendar cal = Calendar.getInstance();

		// Roll the calendar until it uses the first month.
		while (cal.get(Calendar.MONTH) != cal.getMinimum(Calendar.MONTH))
			cal.roll(Calendar.MONTH, false);

		// Initialize day of week names.  The first element contains the name
		// of getFirstDayOfWeek().
		weekNames = new String[cal.getMaximum(Calendar.DAY_OF_WEEK) -
								cal.getMinimum(Calendar.DAY_OF_WEEK) + 1];

		// Roll the calendar until it uses the first day of week.
		while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek())
			cal.roll(Calendar.DAY_OF_WEEK, false);

		// Use SimpleDateFormat to fill weekNames.
		for (int i = 0; i < weekNames.length; i++)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("E");
			weekNames[i] = sdf.format(cal.getTime());

			// Only take the first two characters.
/*			if (weekNames[i].length() > 2)
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
			}*/
			cal.roll(Calendar.DAY_OF_WEEK, true);
		}
	}

	class DayCanvas extends Component implements MouseListener
	{
		Calendar dayCal;
		MonthCanvas dayParent;
		boolean selected = false;

		DayCanvas(Calendar cal, MonthCanvas parent)
		{
			this.dayCal = cal;
			this.dayParent = parent;
			addMouseListener(this);
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			int day = this.dayCal.get(Calendar.DAY_OF_MONTH);
			int week = this.dayCal.get(Calendar.DAY_OF_WEEK);
			int month = this.dayCal.get(Calendar.MONTH);
			int year = this.dayCal.get(Calendar.YEAR);
			int hour = this.dayCal.get(Calendar.HOUR);
			int minute = this.dayCal.get(Calendar.MINUTE);
			int second = this.dayCal.get(Calendar.SECOND);

			FontMetrics fm = g.getFontMetrics();

			int x = (getSize().width - fm.stringWidth("" + day)) / 2;
			int y = (getSize().height - fm.getHeight()) / 2;

			if (this.selected)
				g.setColor(Color.white);
			else if (day == this.dayParent.monthCal.get(Calendar.DAY_OF_MONTH))
				g.setColor(Color.blue);
			else
				g.setColor(Color.black);
			g.drawString("" + day, x, y + fm.getAscent());

			if (day == MonthCanvas.today)
			{
				g.setColor(Color.red);
				g.drawImage(
						today_icon.getImage(),
						x - fm.stringWidth("w") / 2,
						y,
						fm.stringWidth("ww"),
						fm.getHeight(),
						today_icon.getImageObserver() );
			}

		}

		// Event handling methods.
		public void mousePressed(MouseEvent e)
		{
			this.selected = true;
			repaint();
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e)
		{
			this.dayParent.monthCal.set(Calendar.DAY_OF_MONTH, dayCal.get(Calendar.DAY_OF_MONTH));
			if (MonthCanvas.this.hideOnChoose)
				this.dayParent.close();
			else
			{
				this.selected = false;
				this.dayParent.repaint();
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
}
