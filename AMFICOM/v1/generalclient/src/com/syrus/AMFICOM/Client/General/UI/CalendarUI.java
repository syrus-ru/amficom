package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;

public class CalendarUI extends JPanel
{
	public static final int STATUS_CANCEL = 0;
	public static final int STATUS_OK = 1;
	public Dimension btn_size = new Dimension(24, 24);

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
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		mCanvas.init(this, cal);
		monthLabel.setText(" " + sdf.format(getCalendar().getTime()) + LangModel.getString("YearPostfix"));
	}

	public static JDialog createDialogInstance(Calendar cal, boolean hideOnChoose)
	{
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog()
		{
			public void dispose()
			{
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

	public static JDialog createDialogInstance(Frame owner, Calendar cal, boolean hideOnChoose)
	{
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner)
		{
			public void dispose()
			{
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

	public static JDialog createDialogInstance(Frame owner, Calendar cal, boolean hideOnChoose, boolean modal)
	{
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner)
		{
			public void dispose()
			{
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

	public static JDialog createDialogInstance(Dialog owner, Calendar cal, boolean hideOnChoose)
	{
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner)
		{
			public void dispose()
			{
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

	public static JDialog createDialogInstance(Dialog owner, Calendar cal, boolean hideOnChoose, boolean modal)
	{
		CalendarUI instance = new CalendarUI(cal);
		dialog = new JDialog(owner)
		{
			public void dispose()
			{
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

	public static JFrame createFrameInstance(Calendar cal, boolean hideOnChoose)
	{
		CalendarUI instance = new CalendarUI(cal);
		frame = new JFrame()
		{
			public void dispose()
			{
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

	public static JInternalFrame createInternalFrameInstance(Calendar cal)
	{
		CalendarUI instance = new CalendarUI(cal);
		internalFrame = new JInternalFrame();
		internalFrame.setSize(new Dimension(190, 190));
		internalFrame.setTitle(LangModel.getString("CalendarTitle"));
		internalFrame.setContentPane(instance);
		return internalFrame;
	}

	public static JPanel createPanelInstance(Calendar cal)
	{
		CalendarUI instance = new CalendarUI(cal);
		return instance;
	}

	private void jbInit() throws Exception
	{
		setLayout(new BorderLayout());

		lessButton.setFocusable(false);
		lessButton.setBorder(BorderFactory.createEtchedBorder());
		lessButton.setPreferredSize(btn_size);
		lessButton.setMinimumSize(btn_size);
		lessButton.setMinimumSize(btn_size);
		moreButton.setFocusable(false);
		moreButton.setBorder(BorderFactory.createEtchedBorder());
		moreButton.setPreferredSize(btn_size);
		moreButton.setMinimumSize(btn_size);
		moreButton.setMinimumSize(btn_size);
		lessButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				decrease();
			}
		});
		moreButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ev)
			{
				increase();
			}
		});

		//monthPanel = new JPanel(new FlowLayout());
		monthPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new FlowLayout());
		labelPanel.add(monthLabel);
		monthPanel.add(lessButton, BorderLayout.WEST);
		monthPanel.add(labelPanel, BorderLayout.CENTER);
		monthPanel.add(moreButton, BorderLayout.EAST);

		mCanvas = new MonthCanvas();

		todayPanel = new JPanel(new FlowLayout());
		todayPanel.add(
			new JLabel(
				LangModel.getString("Today") + ": " +
				new SimpleDateFormat("dd.MM.yyyy").format(MonthCanvas.todayCal.getTime()) +
				LangModel.getString("YearPostfix")),
			BorderLayout.SOUTH);

		add(monthPanel, BorderLayout.NORTH);
		add(mCanvas, BorderLayout.CENTER);
		add(todayPanel, BorderLayout.SOUTH);
	}

	public Calendar getCalendar()
	{
		return mCanvas.cal;
	}

	public void setCalendar(Calendar cal)
	{
		mCanvas.cal = cal;
		mCanvas.reinit();
	}

	public int getStatus()
	{
		return status;
	}

	public void increase()
	{
		Calendar cal = getCalendar();
		if (cal.get(Calendar.MONTH) == cal.getMaximum(Calendar.MONTH))
		{
			cal.set(Calendar.MONTH, cal.getMinimum(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		}
		else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);

		mCanvas.reinit();
		monthLabel.setText(" " + sdf.format(cal.getTime()) + LangModel.getString("YearPostfix"));
	}

	public void decrease()
	{
		Calendar cal = getCalendar();
		if (cal.get(Calendar.MONTH) == cal.getMinimum(Calendar.MONTH))
		{
			cal.set(Calendar.MONTH, cal.getMaximum(Calendar.MONTH));
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		}
		else
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

		mCanvas.reinit();
		monthLabel.setText(" " + sdf.format(cal.getTime()) + LangModel.getString("YearPostfix"));
	}

	public void setMonthVisible(boolean b)
	{
		monthPanel.setVisible(b);
	}

	public void setTodayVisible(boolean b)
	{
		todayPanel.setVisible(b);
	}

	public void setHideOnChoose(boolean b)
	{
		mCanvas.hideOnChoose = b;
	}

	void okClose()
	{
		status = CalendarUI.STATUS_OK;
		if (dialog != null)
			dialog.dispose();
		else if (frame != null)
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
	CalendarUI parent;
	Calendar cal;
	JPanel calPanel;

	void init(CalendarUI panel, Calendar cal)
	{
		this.parent = panel;
		this.cal = cal;
		reinit();
	}

	void reinit()
	{
		if (todayCal.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
			&& todayCal.get(Calendar.YEAR) == cal.get(Calendar.YEAR))
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
			JLabel l = new JLabel(weekNames[i], JLabel.CENTER);
			q.setBackground(Color.gray);
			q.add(l);
		}
		p.add(q);
		add(p, BorderLayout.NORTH);

		// Create a grid for the days.
		calPanel = new JPanel(new GridLayout(0, weekNames.length));

		// Get the day of the week of the first day.
		int temp_day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int fday = cal.getFirstDayOfWeek();
		if (fday > dayOfWeek)
			dayOfWeek += 7;

		cal.set(Calendar.DAY_OF_MONTH, temp_day);
		// Fill the first few cells with blanks.
		for (int i = fday; i < dayOfWeek; i++) {
			calPanel.add(new JLabel(" ", JLabel.CENTER));
		}

		// Add the days.
		for (int i = cal.getMinimum(Calendar.DAY_OF_MONTH); i <= cal.getMaximum(Calendar.DAY_OF_MONTH); i++)
		{
			Calendar c = Calendar.getInstance();
			c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i);

			// Make sure we haven't skipped to the next month.
			if (c.get(Calendar.MONTH) != cal.get(Calendar.MONTH))
			{
				break;
			}
			calPanel.add(new DayCanvas(c, this));
		}
		add(calPanel, BorderLayout.CENTER);

		repaint();
	}

	void close()
	{
		parent.okClose();
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
		Calendar cal;
		MonthCanvas parent;
		boolean selected = false;

		DayCanvas(Calendar cal, MonthCanvas parent)
		{
			this.cal = cal;
			this.parent = parent;
			addMouseListener(this);
		}

		public void paint(Graphics g)
		{
			super.paint(g);
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int week = cal.get(Calendar.DAY_OF_WEEK);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);
			int hour = cal.get(Calendar.HOUR);
			int minute = cal.get(Calendar.MINUTE);
			int second = cal.get(Calendar.SECOND);

			FontMetrics fm = g.getFontMetrics();

			int x = (getSize().width - fm.stringWidth("" + day)) / 2;
			int y = (getSize().height - fm.getHeight()) / 2;

			if (selected)
				g.setColor(Color.white);
			else if (day == parent.cal.get(Calendar.DAY_OF_MONTH))
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
			selected = true;
			repaint();
		}
		public void mouseClicked(MouseEvent e) {}
		public void mouseReleased(MouseEvent e)
		{
			parent.cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
			if (hideOnChoose)
				parent.close();
			else
			{
				selected = false;
				parent.repaint();
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
}
