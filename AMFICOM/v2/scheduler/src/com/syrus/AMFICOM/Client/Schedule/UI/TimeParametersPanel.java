package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.util.*;
import java.text.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;
import com.syrus.AMFICOM.Client.General.Model.*;

public class TimeParametersPanel extends JPanel implements OperationListener {

	//public TestRequest treq;
	private TestRequest					treq;

	private Test						test				= null;

	protected static final Dimension	btn_size			= new Dimension(30,
																	20);

	private ApplicationContext			aContext;

	private Dispatcher					dispatcher;

	private static final String			PARAM_PANEL_NAME	= "PARAM_PANEL";

	private static final String			PATTERN_PANEL_NAME	= "PATTERN_PANEL";

	private static final String			PERIODIC_NULL_NAME	= "PERIODIC_NULL";

	private static final String			PERIODIC_MIN_NAME	= "PERIODIC_MIN";

	private static final String			PERIODIC_HOUR_NAME	= "PERIODIC_HOUR";

	private static final String			PERIODIC_DAY_NAME	= "PERIODIC_DAY";

	private static final String			PERIODIC_WEEK_NAME	= "PERIODIC_WEEK";

	private static final String			PERIODIC_MONTH_NAME	= "PERIODIC_MONTH";

	private TimeSpinner					startTimeSpinner;

	private DateSpinner					startDateSpinner;

	private TimeSpinner					endTimeSpinner;

	private DateSpinner					endDateSpinner;

	private JRadioButton				patternRadioButton;

	private JList						timeStamps;

	private JRadioButton				paramsRadioButton;

	private JRadioButton				oneRadioButton;

	private JRadioButton				continuosRadioButton;

	private JRadioButton				periodicalRadioButton;

	private JRadioButton				hourRadioButton;

	private JRadioButton				minuteRadioButton;

	private JRadioButton				dayRadioButton;

	private JRadioButton				weekRadioButton;

	private JRadioButton				monthRadioButton;

	private JRadioButton				synchroRadioButton;

	private JRadioButton				alternateRadioButton;

	private JButton						createButton;

	private JButton						applyButton;

	private MinutePanel					minPanel;

	private HourPanel					hourPanel;

	private DayPanel					dayPanel;

	private WeekPanel					weekPanel;

	private MonthPanel					monthPanel;

	private TimeStampFiller				tempPanel;

	public TimeParametersPanel() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TimeParametersPanel(ApplicationContext aContext) {
		this.aContext = aContext;
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
		this.dispatcher.register(this, TestRequestFrame.COMMAND_DATA_REQUEST);
	}

	public void setTestRequest(TestRequest treq) {
		System.out.println("set Test Request" + treq.getId());
		this.treq = treq;
	}

	/** 
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {

		TimeParametersPanel demo = new TimeParametersPanel();
		JFrame mainFrame = new JFrame("TimeParametersPanel");
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		mainFrame.getContentPane().add(demo);
		mainFrame.pack();
		mainFrame.setSize(new Dimension(250, 465));
		mainFrame.setVisible(true);
	}

	private void jbInit() throws Exception {
		final JPanel paramPatternPanel = new JPanel(new CardLayout());
		final JPanel extraParamPanel = new JPanel(new CardLayout());
		extraParamPanel.add(new JLabel(), PERIODIC_NULL_NAME);
		final boolean[] periodicElementInit = new boolean[] { true, false,
				false, false, false, false};
		final GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.insets = UIUtil.INSET_NULL;
		Insets gbcInsetsDefault = gbc.insets;
		gbc.fill = GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());
		JLabel beginLabel = new JLabel("Начало:");
		gbc.gridx = 1;
		gbc.gridy++;
		add(beginLabel, gbc);
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy++;

		startTimeSpinner = new TimeSpinner();
		startDateSpinner = new DateSpinner();
		final JButton startDateButton = new JButton(UIUtil.CALENDAR_ICON);
		startDateButton.setMargin(UIUtil.INSET_NULL);
		startDateButton.setDefaultCapable(false);

		startDateButton.setFocusable(false);
		startDateButton.setToolTipText(LangModelSchedule
				.getComponentToolTip("CalendarButton"));
		startDateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showStartCalendar();
			}
		});
		{
			Box box = new Box(BoxLayout.X_AXIS);
			box.add(startDateSpinner);
			box.add(startDateButton);
			box.add(Box.createHorizontalGlue());
			box.add(startTimeSpinner);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(box, gbc);
		}

		gbc.gridx = 1;
		gbc.gridy++;
		JLabel endingLabel = new JLabel("Окончание:");
		add(endingLabel, gbc);
		gbc.weightx = 1.0;
		gbc.gridx = 1;
		gbc.gridy++;
		endTimeSpinner = new TimeSpinner();
		endDateSpinner = new DateSpinner();
		final JButton endDateButton = new JButton(UIUtil.CALENDAR_ICON);
		endDateButton.setMargin(UIUtil.INSET_NULL);
		endDateButton.setDefaultCapable(false);

		endDateButton.setFocusable(false);
		endDateButton.setToolTipText(LangModelSchedule
				.getComponentToolTip("CalendarButton"));
		endDateButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				showEndCalendar();
			}
		});
		{
			Box box = new Box(BoxLayout.X_AXIS);
			box.add(endDateSpinner);
			box.add(endDateButton);
			box.add(Box.createHorizontalGlue());
			box.add(endTimeSpinner);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(box, gbc);
		}

		JSeparator jsep1 = new JSeparator();
		gbc.insets = UIUtil.INSETS1010;
		jsep1.setBorder(BorderFactory.createEtchedBorder());
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		add(jsep1, gbc);
		gbc.insets = gbcInsetsDefault;

		patternRadioButton = UIUtil.createRadioButton(LangModelSchedule
				.getString("labelUsePattern"), new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (paramPatternPanel.getLayout());
				cl.show(paramPatternPanel, PATTERN_PANEL_NAME);
				endTimeSpinner.setEnabled(true);
				endDateSpinner.setEnabled(true);
				endDateButton.setEnabled(true);
				revalidate();
			}
		});
		minuteRadioButton = UIUtil.createRadioButton("минута",
				new AbstractAction() {

					public void actionPerformed(ActionEvent e) {
						if (!periodicElementInit[1]) {
							minPanel = new MinutePanel();
							extraParamPanel.add(minPanel, PERIODIC_MIN_NAME);
							periodicElementInit[1] = true;
						}
						CardLayout cl = (CardLayout) (extraParamPanel
								.getLayout());
						cl.show(extraParamPanel, PERIODIC_MIN_NAME);
						tempPanel = minPanel;
						revalidate();
					}
				});
		hourRadioButton = UIUtil.createRadioButton("час", new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				if (!periodicElementInit[2]) {
					hourPanel = new HourPanel();
					extraParamPanel.add(hourPanel, PERIODIC_HOUR_NAME);
					periodicElementInit[2] = true;
				}
				CardLayout cl = (CardLayout) (extraParamPanel.getLayout());
				cl.show(extraParamPanel, PERIODIC_HOUR_NAME);
				tempPanel = hourPanel;
				revalidate();
			}
		});
		dayRadioButton = UIUtil.createRadioButton("день", new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				if (!periodicElementInit[3]) {
					dayPanel = new DayPanel();
					extraParamPanel.add(dayPanel, PERIODIC_DAY_NAME);
					periodicElementInit[3] = true;
				}
				CardLayout cl = (CardLayout) (extraParamPanel.getLayout());
				cl.show(extraParamPanel, PERIODIC_DAY_NAME);
				tempPanel = dayPanel;
				revalidate();
			}

		});
		weekRadioButton = UIUtil.createRadioButton("неделя",
				new AbstractAction() {

					public void actionPerformed(ActionEvent e) {
						if (!periodicElementInit[4]) {
							weekPanel = new WeekPanel();
							extraParamPanel.add(weekPanel, PERIODIC_WEEK_NAME);
							periodicElementInit[4] = true;
						}
						CardLayout cl = (CardLayout) (extraParamPanel
								.getLayout());
						cl.show(extraParamPanel, PERIODIC_WEEK_NAME);
						tempPanel = weekPanel;
						revalidate();
					}

				});
		monthRadioButton = UIUtil.createRadioButton("месяц",
				new AbstractAction() {

					public void actionPerformed(ActionEvent e) {
						if (!periodicElementInit[5]) {
							monthPanel = new MonthPanel();
							extraParamPanel
									.add(monthPanel, PERIODIC_MONTH_NAME);
							periodicElementInit[5] = true;
						}
						CardLayout cl = (CardLayout) (extraParamPanel
								.getLayout());
						cl.show(extraParamPanel, PERIODIC_MONTH_NAME);
						tempPanel = monthPanel;
						revalidate();
					}

				});

		oneRadioButton = UIUtil.createRadioButton(LangModelSchedule
				.getString("labelOnetime"), new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				endTimeSpinner.setEnabled(false);
				endDateSpinner.setEnabled(false);
				endDateButton.setEnabled(false);

				minuteRadioButton.setEnabled(false);
				hourRadioButton.setEnabled(false);
				dayRadioButton.setEnabled(false);
				weekRadioButton.setEnabled(false);
				monthRadioButton.setEnabled(false);
				extraParamPanel.setVisible(false);
				revalidate();
			}
		});

		paramsRadioButton = UIUtil.createRadioButton(LangModelSchedule
				.getString("labelUseParameters"), new AbstractAction() {

			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (paramPatternPanel.getLayout());
				cl.show(paramPatternPanel, PARAM_PANEL_NAME);
				if (oneRadioButton.isSelected()) {
					endTimeSpinner.setEnabled(false);
					endDateSpinner.setEnabled(false);
					endDateButton.setEnabled(false);
				}
				revalidate();

			}

		});
		ButtonGroup group4 = new ButtonGroup();
		group4.add(patternRadioButton);
		group4.add(paramsRadioButton);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridy = 6;
		add(patternRadioButton, gbc);
		gbc.gridy = 7;
		add(paramsRadioButton, gbc);

		JSeparator jsep2 = new JSeparator();
		gbc.insets = UIUtil.INSETS1010;
		gbc.gridy = 8;
		jsep2.setBorder(BorderFactory.createEtchedBorder());
		add(jsep2, gbc);
		gbc.insets = gbcInsetsDefault;

		continuosRadioButton = new JRadioButton(LangModelSchedule
				.getString("labelContinual"));
		continuosRadioButton.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					endTimeSpinner.setEnabled(true);
					endDateSpinner.setEnabled(true);
					endDateButton.setEnabled(true);

					minuteRadioButton.setEnabled(false);
					hourRadioButton.setEnabled(false);
					dayRadioButton.setEnabled(false);
					weekRadioButton.setEnabled(false);
					monthRadioButton.setEnabled(false);
					extraParamPanel.setVisible(false);
					revalidate();
				}
			}
		});

		periodicalRadioButton = new JRadioButton(LangModelSchedule
				.getString("labelPeriod"));
		periodicalRadioButton.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					endTimeSpinner.setEnabled(true);
					endDateSpinner.setEnabled(true);
					endDateButton.setEnabled(true);

					minuteRadioButton.setEnabled(true);
					hourRadioButton.setEnabled(true);
					dayRadioButton.setEnabled(true);
					weekRadioButton.setEnabled(true);
					monthRadioButton.setEnabled(true);
					extraParamPanel.setVisible(true);
					revalidate();
				}
			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(oneRadioButton);
		group.add(continuosRadioButton);
		group.add(periodicalRadioButton);

		ButtonGroup group2 = new ButtonGroup();
		group2.add(minuteRadioButton);
		group2.add(hourRadioButton);
		group2.add(dayRadioButton);
		group2.add(weekRadioButton);
		group2.add(monthRadioButton);

		{
			JPanel pOptPanel = new JPanel(new GridBagLayout());
			pOptPanel
					.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			gbc.gridx = GridBagConstraints.RELATIVE;
			gbc.gridy = GridBagConstraints.RELATIVE;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.gridheight = 8;
			pOptPanel.add(extraParamPanel, gbc);

			gbc.gridheight = 1;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			pOptPanel.add(oneRadioButton, gbc);
			pOptPanel.add(continuosRadioButton, gbc);
			pOptPanel.add(periodicalRadioButton, gbc);

			gbc.insets = new Insets(0, 10, 0, 10);
			pOptPanel.add(minuteRadioButton, gbc);
			pOptPanel.add(hourRadioButton, gbc);
			pOptPanel.add(dayRadioButton, gbc);
			pOptPanel.add(weekRadioButton, gbc);
			pOptPanel.add(monthRadioButton, gbc);
			gbc.insets = gbcInsetsDefault;

			timeStamps = new JList();
			JScrollPane timeStampPanel = new JScrollPane(timeStamps);
			timeStampPanel.setBorder(BorderFactory.createEtchedBorder());

			/*
			 * JSplitPane paramPanel = new JSplitPane(
			 * JSplitPane.HORIZONTAL_SPLIT, pOptPanel, extraParamPanel);*
			 * paramPanel.setResizeWeight(0.5);
			 * paramPanel.setOneTouchExpandable(true);
			 * paramPanel.setContinuousLayout(true);
			 * paramPanel.setBorder(BorderFactory.createEtchedBorder());
			 */
			paramPatternPanel.add(pOptPanel, PARAM_PANEL_NAME);
			paramPatternPanel.add(timeStampPanel, PATTERN_PANEL_NAME);

			gbc.gridx = 1;
			gbc.gridy = 9;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(paramPatternPanel, gbc);
		}

		JSeparator jsep3 = new JSeparator();
		gbc.insets = UIUtil.INSETS1010;
		jsep3.setBorder(BorderFactory.createEtchedBorder());
		gbc.gridy++;
		add(jsep3, gbc);
		gbc.insets = gbcInsetsDefault;

		synchroRadioButton = new JRadioButton(LangModelSchedule
				.getString("labelTogether"));
		alternateRadioButton = new JRadioButton(LangModelSchedule
				.getString("labelInTurn"));
		ButtonGroup group3 = new ButtonGroup();
		group3.add(synchroRadioButton);
		group3.add(alternateRadioButton);
		gbc.gridy++;
		add(synchroRadioButton, gbc);
		gbc.gridy++;
		add(alternateRadioButton, gbc);

		JSeparator jsep4 = new JSeparator();
		gbc.insets = UIUtil.INSETS1010;
		gbc.gridheight = GridBagConstraints.RELATIVE;
		jsep4.setBorder(BorderFactory.createEtchedBorder());
		gbc.gridy++;
		add(jsep4, gbc);
		gbc.insets = gbcInsetsDefault;

		{
			applyButton = new JButton(LangModelSchedule.getString("labelApply"));
			createButton = new JButton(LangModelSchedule
					.getString("labelCreate"));

			applyButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					createButton.setEnabled(false);
					applyButton.setEnabled(false);
					apply();
					/**
					 * @todo does we need enable create button always ?
					 */
					createButton.setEnabled(true);
					applyButton.setEnabled(true);

				}
			});

			createButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					createButton.setEnabled(false);
					applyButton.setEnabled(false);
					dispatcher.notify(new OperationEvent("", 0,
							TestRequestFrame.COMMAND_CREATE_TEST));
					createButton.setEnabled(true);
					applyButton.setEnabled(true);
				}
			});

			Box box = new Box(BoxLayout.X_AXIS);
			createButton.setDefaultCapable(false);
			box.add(createButton);
			box.add(Box.createGlue());
			applyButton.setDefaultCapable(false);
			box.add(applyButton);
			gbc.anchor = GridBagConstraints.SOUTH;
			gbc.gridy++;
			gbc.gridx = 1;
			//gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(box, gbc);
		}

		minuteRadioButton.doClick();
		paramsRadioButton.doClick();
		oneRadioButton.doClick();
		synchroRadioButton.doClick();
		periodicalRadioButton.doClick();
	}

	private void showStartCalendar() {
		Calendar cal = Calendar.getInstance();
		Date date = (Date) startDateSpinner.getModel().getValue();
		cal.setTime(date);

		JDialog calendarDialog = CalendarUI.createDialogInstance(Environment
				.getActiveWindow(), cal, true, true);
		calendarDialog.setSize(new Dimension(200, 200));
		calendarDialog.setResizable(false);
		calendarDialog.setLocation(new Point(startDateSpinner
				.getLocationOnScreen().x - 35, startDateSpinner
				.getLocationOnScreen().y + 22));
		calendarDialog.setVisible(true);
		if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				startDateSpinner.getModel().setValue(cal.getTime());
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (ScheduleMainFrame.DEBUG)
				System.out.println(getClass().getName() + " commandName: "
						+ commandName);
		if (commandName.equalsIgnoreCase(TestRequestFrame.COMMAND_DATA_REQUEST)) {
			/**
			 * @todo must send data edit in this form 
			 */
			TimeStamp timeStamp = this.getTimeStamp();
			dispatcher.notify(new OperationEvent(timeStamp,
					TestRequestFrame.DATA_ID_TIMESTAMP,
					TestRequestFrame.COMMAND_SEND_DATA));
		} else if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			this.test = tue.test;

			/**
			 * todo set TimeStamp from Test
			 */
			if (tue.TEST_SELECTED) {
				/**
				 * todo this is ONLY for backward compatibility
				 */
				setAllRadioButtonEnabled(true);
				TimeStamp timeStamp = test.timeStamp;
				{
					long start = timeStamp.getPeriodStart();
					long end = timeStamp.getPeriodEnd();
					Date date = new Date(start);
					startDateSpinner.getModel().setValue(date);
					startTimeSpinner.getModel().setValue(date);
					date.setTime(end);
					endDateSpinner.getModel().setValue(date);
					endTimeSpinner.getModel().setValue(date);
				}

				switch (timeStamp.getType()) {
					case TimeStamp.TIMESTAMPTYPE_ONETIME:
						oneRadioButton.doClick();
						break;
					case TimeStamp.TIMESTAMPTYPE_CONTINUOS:
						continuosRadioButton.doClick();
						break;
					case TimeStamp.TIMESTAMPTYPE_PERIODIC:
						periodicalRadioButton.doClick();
						//Time period =timeStamp.getPeriod();
						if (minPanel != null) minPanel.setTimeStamp(timeStamp);
						if (hourPanel != null)
								hourPanel.setTimeStamp(timeStamp);
						if (dayPanel != null) dayPanel.setTimeStamp(timeStamp);
						if (weekPanel != null)
								weekPanel.setTimeStamp(timeStamp);
						if (monthPanel != null)
								monthPanel.setTimeStamp(timeStamp);
						break;

				}
				enableTestStatus();
			}
		}
	}

	private void setAllRadioButtonEnabled(boolean value) {
		//		paramsRadioButton.setEnabled(value);
		//		patternRadioButton.setEnabled(value);
		//		oneRadioButton.setEnabled(value);
		//		continuosRadioButton.setEnabled(value);
		//		periodicalRadioButton.setEnabled(value);
		//		alternateRadioButton.setEnabled(value);
		//		synchroRadioButton.setEnabled(value);
		//		hourRadioButton.setEnabled(value);
		//		minuteRadioButton.setEnabled(value);
		//		dayRadioButton.setEnabled(value);
		//		weekRadioButton.setEnabled(value);
		//		monthRadioButton.setEnabled(value);
		//		minPanel.setEnabled(value);

		applyButton.setEnabled(value);
	}

	private void enableTestStatus() {
		TestStatus status = test.status;
		switch (status.value()) {
			case TestStatus._TEST_STATUS_COMPLETED:
			// break; // because of the same action
			case TestStatus._TEST_STATUS_PROCESSING:
				setAllRadioButtonEnabled(false);
				break;
			case TestStatus._TEST_STATUS_SCHEDULED:
				setAllRadioButtonEnabled(true);
				break;
		}
	}

	private TimeStamp getTimeStamp() {
		TimeStamp ts = new TimeStamp();

		Calendar dateCal = Calendar.getInstance();
		Calendar timeCal = Calendar.getInstance();

		dateCal.setTime((Date) startDateSpinner.getValue());
		timeCal.setTime((Date) startTimeSpinner.getValue());
		dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		long start = dateCal.getTimeInMillis();

		dateCal.setTime((Date) endDateSpinner.getValue());
		timeCal.setTime((Date) endTimeSpinner.getValue());
		dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, 0);
		dateCal.set(Calendar.MILLISECOND, 0);
		long end = dateCal.getTimeInMillis();

		if (end < start) {
			JOptionPane.showMessageDialog(this,
					"Время начала теста больше времени окончания", "Ошибка",
					JOptionPane.OK_OPTION);
			return null;
		}

		ts.setPeriodStart(start);
		ts.setPeriodEnd(end);
		//if (tempPanel != null)
		//	tempPanel.fillTimeStamp(ts);
		if (periodicalRadioButton.isSelected()) {
			ts.setType(TimeStamp.TIMESTAMPTYPE_PERIODIC);
			tempPanel.fillTimeStamp(ts);
		} else if (continuosRadioButton.isSelected()) {
			ts.setType(TimeStamp.TIMESTAMPTYPE_CONTINUOS);
		} else if (oneRadioButton.isSelected()) {
			ts.setType(TimeStamp.TIMESTAMPTYPE_ONETIME);
		}

		return ts;
	}

	private void showEndCalendar() {
		Calendar cal = Calendar.getInstance();
		Date date = (Date) endDateSpinner.getModel().getValue();
		cal.setTime(date);

		JDialog calendarDialog = CalendarUI.createDialogInstance(Environment
				.getActiveWindow(), cal, true, true);
		calendarDialog.setSize(new Dimension(200, 200));
		calendarDialog.setResizable(false);
		calendarDialog.setLocation(new Point(endDateSpinner
				.getLocationOnScreen().x - 35, endDateSpinner
				.getLocationOnScreen().y + 22));
		calendarDialog.setVisible(true);
		if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				endDateSpinner.getModel().setValue(cal.getTime());
	}

	private void apply() {
		TimeStamp ts = this.getTimeStamp();
		//		long[] times = ts.getTestTimes();
		//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		//		for (int i = 0; i < times.length; i++)
		//			System.out.println(
		//				"test #"
		//					+ i
		//					+ ": "
		//					+ sdf.format(new Date(times[i]))
		//					+ " = "
		//					+ times[i]);
		/**
		 * @todo apply test ? 
		 */
		dispatcher.notify(new OperationEvent("", 0,
				TestRequestFrame.COMMAND_APPLY_TEST));
	}

	private interface TimeStampFiller {

		void fillTimeStamp(TimeStamp ts);

		void setTimeStamp(TimeStamp ts);
	}

	private class TimePanelExt extends JPanel {

		protected GridBagLayout			layout;

		protected GridBagConstraints	gbc;

		protected Insets				defaultInsets;

		protected Insets				addInsets	= new Insets(1, 1, 1, 3);

		protected JButton addPlusButton() {
			//JButton button = new JButton("+");
			JButton button = new JButton(UIUtil.PLUS_ICON);
			button.setFocusable(false);
			button.setMargin(UIUtil.INSET_NULL);
			button.setEnabled(true);
			button.setDefaultCapable(false);
			return button;
		}

		protected JButton addMinusButton() {
			//JButton button = new JButton("-");
			JButton button = new JButton(UIUtil.MINUS_ICON);
			button.setFocusable(false);
			button.setMargin(UIUtil.INSET_NULL);
			button.setEnabled(false);
			button.setDefaultCapable(false);
			return button;
		}

		TimePanelExt() {
			layout = new GridBagLayout();
			//setBorder(BorderFactory.createEtchedBorder());
			gbc = new GridBagConstraints();
			defaultInsets = gbc.insets;
			setLayout(layout);
			gbc.ipadx = 0;
			gbc.ipady = 0;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = GridBagConstraints.REMAINDER;
			JSeparator jsep = new JSeparator(JSeparator.VERTICAL);
			gbc.insets = addInsets;
			jsep.setBorder(BorderFactory.createEtchedBorder());
			add(jsep, gbc);
			gbc.insets = defaultInsets;

			gbc.gridheight = 1;

		}

		void addHorizontalSeparator() {
			JSeparator jsep = new JSeparator();
			gbc.insets = addInsets;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			jsep.setBorder(BorderFactory.createEtchedBorder());
			add(jsep, gbc);
			gbc.insets = defaultInsets;
		}

		void addGlueLabel() {
			/** 
			 * @todo fix without creating unnecessary JLabel()
			 */
			gbc.anchor = GridBagConstraints.PAGE_END;
			gbc.weighty = 1.0;
			gbc.gridy++;
			add(new JLabel(), gbc);
		}
	}

	private class MinutePanel extends TimePanelExt implements TimeStampFiller {

		private JSpinner	minSpin	= new JSpinner(new SpinnerNumberModel(1, 1,
											59, 1));

		protected MinutePanel() {
			super();
			gbc.gridx = 1;
			gbc.weightx = 1.0;
			gbc.gridwidth = 1;
			add(new JLabel("Каждые"), gbc);
			gbc.gridx++;
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			//gbc.ipadx = TimeParametersPanel.btn_size.width;
			add(minSpin, gbc);
			gbc.ipadx = 0;
			gbc.gridx++;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			add(new JLabel("мин."), gbc);
			addGlueLabel();
		}

		public void setTimeStamp(TimeStamp ts) {
			Time period = ts.getPeriod();
			switch (period.getScale()) {
				case Calendar.MINUTE:
					minuteRadioButton.doClick();
					minSpin.setValue(new Integer(period.getValue()));
					break;

				default:
					minSpin.setValue(new Integer(0));
					break;
			}

		}

		public void setEnabled(boolean value) {
			minSpin.setEnabled(value);
		}

		public void fillTimeStamp(TimeStamp ts) {
			ts.setPeriod(Calendar.MINUTE, ((Number) minSpin.getValue())
					.intValue());
			ts.addTestDate(Calendar.MINUTE, 0);
			ts.addTestTime(0, 0, 0);
			//return ts;
		}
	}

	private class HourPanel extends TimePanelExt implements TimeStampFiller {

		private JSpinner	hourSpin	= new JSpinner(new SpinnerNumberModel(
												1, 1, 23, 1));

		private JList		list;

		private Vector		time		= new Vector();

		protected HourPanel() {
			super();
			gbc.weightx = 1.0;
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			add(new JLabel("Каждые"), gbc);
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.gridx++;
			//gbc.ipadx = TimeParametersPanel.btn_size.width;
			add(hourSpin, gbc);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx++;
			gbc.ipadx = 0;
			add(new JLabel("час."), gbc);
			gbc.gridx = 1;
			gbc.gridy++;
			addHorizontalSeparator();

			list = new JList();
			list.setListData(time);
			JScrollPane scroll = new JScrollPane(list);
			scroll
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JButton addButton = addPlusButton();
			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel p = new JPanel();
					p.add(new JLabel("Время тестирования:"));
					HourSpinner hs = new HourSpinner();
					hs.getModel().setValue(new Date(0));
					p.add(hs);
					p.add(new JLabel(" (mm:ss)"));
					int res = JOptionPane.showConfirmDialog(HourPanel.this, p,
							"Добавить", JOptionPane.OK_CANCEL_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						Date date = (Date) hs.getModel().getValue();
						time.add(new SimpleDateFormat("mm:ss").format(date));
						list.setListData(time);
					}
				}
			});
			final JButton removeButton = addMinusButton();
			removeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					time.remove(list.getSelectedIndex());
					list.setListData(time);
				}
			});
			list.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					removeButton.setEnabled(list.getSelectedIndex() >= 0);
				}
			});

			{
				JPanel panel = new JPanel(new GridBagLayout());
				int prevGridY = gbc.gridy;

				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				panel.add(new JLabel("Время"), gbc);
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridheight = 2;
				gbc.gridx++;
				gbc.ipadx = TimeParametersPanel.btn_size.height;
				gbc.ipady = TimeParametersPanel.btn_size.height;
				panel.add(scroll, gbc);
				gbc.ipadx = 0;
				gbc.ipady = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx++;
				gbc.weightx = 0.0;
				panel.add(addButton, gbc);
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.ipadx = 0;
				gbc.weightx = 1.0;
				panel.add(new JLabel("чч:мм"), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx += 2;
				gbc.weightx = 0.0;
				panel.add(removeButton, gbc);
				gbc.gridx = 1;
				gbc.gridy = prevGridY + 1;
				add(panel, gbc);
				gbc.weighty = 0.0;
			}

			addGlueLabel();
		}

		public void setTimeStamp(TimeStamp ts) {
			Time period = ts.getPeriod();
			switch (period.getScale()) {
				case Calendar.HOUR:
					hourRadioButton.doClick();
					hourSpin.setValue(new Integer(period.getValue()));

					LinkedList list = ts.getTimeList();
					time.clear();
					Calendar cal = Calendar.getInstance();
					for (Iterator it = list.iterator(); it.hasNext();) {
						DayTime dayTime = (DayTime) it.next();
						cal.set(Calendar.MINUTE, dayTime.getMinute());
						cal.set(Calendar.SECOND, dayTime.getSecond());
						this.time.add(new SimpleDateFormat("mm:ss").format(cal
								.getTime()));
					}
					this.list.setListData(time);
					break;

				default:
					hourSpin.setValue(new Integer(0));
					this.list.removeAll();
					break;
			}

		}

		public void fillTimeStamp(TimeStamp ts) {
			ts.setPeriod(Calendar.HOUR_OF_DAY, ((Number) hourSpin.getValue())
					.intValue());

			ts.addTestDate(Calendar.HOUR_OF_DAY, 0);
			for (Iterator it = time.iterator(); it.hasNext();) {
				String str = (String) it.next();
				int m = Integer.parseInt(str.substring(0, 2));
				int s = Integer.parseInt(str.substring(3, 5));
				ts.addTestTime(0, m, s);
			}

			//return ts;
		}
	}

	private class DayPanel extends TimePanelExt implements TimeStampFiller {

		private JSpinner			daySpin			= new JSpinner(
															new SpinnerNumberModel(
																	1, 1, 30, 1));

		private Vector				time			= new Vector();

		private JList				list;

		/**
		 * todo set and fill dayType
		 */
		private AComboBox			dayType;

		private static final int	DAYTYPE_ANY		= 0;

		private static final int	DAYTYPE_WORK	= 1;

		private static final int	DAYTYPE_REST	= 2;

		protected DayPanel() {
			super();
			list = new JList();
			list.setListData(time);
			JScrollPane scroll = new JScrollPane(list);
			scroll
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JButton addButton = addPlusButton();
			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel p = new JPanel();
					p.add(new JLabel("Время тестирования:"));
					TimeSpinner ts = new TimeSpinner();
					ts.getModel().setValue(new Date(0));
					p.add(ts);
					p.add(new JLabel(" (hh:mm)"));
					int res = JOptionPane.showConfirmDialog(DayPanel.this, p,
							"Добавить", JOptionPane.OK_CANCEL_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						Date date = (Date) ts.getModel().getValue();
						time.add(new SimpleDateFormat("HH:mm").format(date));
						list.setListData(time);
					}
				}
			});
			final JButton removeButton = addMinusButton();
			removeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					time.remove(list.getSelectedIndex());
					list.setListData(time);
				}
			});

			list.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					removeButton.setEnabled(list.getSelectedIndex() >= 0);
				}
			});

			gbc.weightx = 0.0;

			gbc.gridx = 1;
			gbc.gridwidth = 1;
			add(new JLabel("Каждые"), gbc);
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.gridx++;
			add(daySpin, gbc);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx++;
			add(new JLabel("сут."), gbc);
			gbc.gridx = 1;
			gbc.gridy++;
			addHorizontalSeparator();
			{
				JPanel panel = new JPanel(new GridBagLayout());
				int prevGridY = gbc.gridy;

				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				panel.add(new JLabel("Время"), gbc);
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridheight = 2;
				gbc.gridx++;
				gbc.ipadx = TimeParametersPanel.btn_size.height;
				gbc.ipady = TimeParametersPanel.btn_size.height;
				panel.add(scroll, gbc);
				gbc.ipadx = 0;
				gbc.ipady = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx++;
				gbc.weightx = 0.0;
				panel.add(addButton, gbc);
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.ipadx = 0;
				gbc.weightx = 1.0;
				panel.add(new JLabel("чч:мм"), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx += 2;
				gbc.weightx = 0.0;
				panel.add(removeButton, gbc);
				gbc.gridx = 1;
				gbc.gridy = prevGridY + 1;
				add(panel, gbc);
				gbc.weighty = 0.0;
			}
			{

				Box box = new Box(BoxLayout.X_AXIS);
				dayType = new AComboBox(new String[] { "любой", "рабочий",
						"выходной"});
				box.add(new JLabel("В"));
				box.add(dayType);
				box.add(new JLabel("день"));
				gbc.gridx = 1;
				gbc.gridy++;

				add(box, gbc);
			}
			gbc.ipadx = 0;
			addGlueLabel();
		}

		public void fillTimeStamp(TimeStamp ts) {
			ts.setPeriod(Calendar.DAY_OF_MONTH, ((Number) daySpin.getValue())
					.intValue());

			ts.addTestDate(Calendar.DAY_OF_MONTH, 0);
			for (Iterator it = time.iterator(); it.hasNext();) {
				String str = (String) it.next();
				int h = Integer.parseInt(str.substring(0, 2));
				int m = Integer.parseInt(str.substring(3, 5));
				ts.addTestTime(h, m, 0);
			}

			switch (dayType.getSelectedIndex()) {
				case DAYTYPE_WORK:
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					break;
				case DAYTYPE_REST:
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					break;
				case DAYTYPE_ANY:
				default:
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					break;
			}

			//return ts;
		}

		public void setTimeStamp(TimeStamp ts) {
			Time period = ts.getPeriod();
			switch (period.getScale()) {
				case Calendar.DAY_OF_MONTH:
					dayRadioButton.doClick();
					daySpin.setValue(new Integer(period.getValue()));

					LinkedList list = ts.getTimeList();
					time.clear();
					Calendar cal = Calendar.getInstance();
					for (Iterator it = list.iterator(); it.hasNext();) {
						DayTime dayTime = (DayTime) it.next();
						cal.set(Calendar.MINUTE, dayTime.getMinute());
						cal.set(Calendar.HOUR, dayTime.getHour());
						time.add(new SimpleDateFormat("HH:mm").format(cal
								.getTime()));
					}
					this.list.setListData(time);

					list = ts.getDateList();
					/*
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					 * break; case DAYTYPE_REST :
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					 * break; case DAYTYPE_ANY : default :
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
					 * ts.addTestDate(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
					 */
					int workDayCount = 0;
					int restDayCount = 0;
					for (Iterator it = list.iterator(); it.hasNext();) {
						Time dt = (Time) it.next();
						if (dt.getScale() == Calendar.DAY_OF_WEEK) {
							int value = dt.getValue();
							switch (value) {
								case Calendar.SATURDAY:
								case Calendar.SUNDAY:
									restDayCount++;
									break;
								default:
									workDayCount++;
									break;
							}
						}
					}

					if ((workDayCount > 0) && (restDayCount == 0)) {
						dayType.setSelectedIndex(DAYTYPE_WORK);
					} else if ((workDayCount == 0) && (restDayCount > 0)) {
						dayType.setSelectedIndex(DAYTYPE_REST);
					} else {
						dayType.setSelectedIndex(DAYTYPE_ANY);
					}

					break;

				default:
					daySpin.setValue(new Integer(0));
					this.list.removeAll();
					break;
			}

		}

	}

	private class WeekPanel extends TimePanelExt implements TimeStampFiller {

		private JSpinner			weekSpin	= new JSpinner(
														new SpinnerNumberModel(
																1, 1, 4, 1));

		private Vector				time		= new Vector();

		private JCheckBox[]			days;

		//private Hashtable ht = new Hashtable();
		private int[]				daysIds;

		private JList				list;

		private SimpleDateFormat	sdf			= new SimpleDateFormat("E");

		protected WeekPanel() {
			super();
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridwidth = 1;
			add(new JLabel("Каждые"), gbc);
			//gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.gridx++;
			gbc.gridwidth = 2;
			add(weekSpin, gbc);
			gbc.gridwidth = 1;
			gbc.gridx += 2;
			add(new JLabel("нед."), gbc);
			gbc.gridx = 1;
			gbc.gridy++;
			addHorizontalSeparator();
			list = new JList();
			list.setListData(time);
			JScrollPane scroll = new JScrollPane(list);
			scroll
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JButton addButton = addPlusButton();
			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel p = new JPanel();
					p.add(new JLabel("Время тестирования:"));
					TimeSpinner ts = new TimeSpinner();
					ts.getModel().setValue(new Date(0));
					p.add(ts);
					p.add(new JLabel(" (hh:mm)"));
					int res = JOptionPane.showConfirmDialog(WeekPanel.this, p,
							"Добавить", JOptionPane.OK_CANCEL_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						Date date = (Date) ts.getModel().getValue();
						time.add(new SimpleDateFormat("HH:mm").format(date));
						list.setListData(time);
					}
				}
			});
			final JButton removeButton = addMinusButton();
			removeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					time.remove(list.getSelectedIndex());
					list.setListData(time);
				}
			});

			list.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					removeButton.setEnabled(list.getSelectedIndex() >= 0);
				}
			});
			{
				JPanel panel = new JPanel(new GridBagLayout());
				int prevGridY = gbc.gridy;

				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				panel.add(new JLabel("Время"), gbc);
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridheight = 2;
				gbc.gridx++;
				gbc.ipadx = TimeParametersPanel.btn_size.height;
				gbc.ipady = TimeParametersPanel.btn_size.height;
				panel.add(scroll, gbc);
				gbc.ipadx = 0;
				gbc.ipady = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx++;
				gbc.weightx = 0.0;
				panel.add(addButton, gbc);
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.ipadx = 0;
				gbc.weightx = 1.0;
				panel.add(new JLabel("чч:мм"), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx += 2;
				gbc.weightx = 0.0;
				panel.add(removeButton, gbc);
				gbc.gridx = 1;
				gbc.gridy = prevGridY + 1;
				add(panel, gbc);
				gbc.weighty = 0.0;
			}

			Calendar cal = Calendar.getInstance();
			while (cal.get(Calendar.MONTH) != cal.getMinimum(Calendar.MONTH))
				cal.roll(Calendar.MONTH, false);
			String[] weekNames = new String[cal
					.getMaximum(Calendar.DAY_OF_WEEK)
					- cal.getMinimum(Calendar.DAY_OF_WEEK) + 1];
			days = new JCheckBox[weekNames.length];
			while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek())
				cal.roll(Calendar.DAY_OF_WEEK, false);
			daysIds = new int[weekNames.length];
			for (int i = 0; i < weekNames.length; i++) {
				weekNames[i] = sdf.format(cal.getTime());
				days[i] = new JCheckBox(weekNames[i], true);

				//ht.put(days[i], new Integer(cal.get(Calendar.DAY_OF_WEEK)));
				daysIds[i] = cal.get(Calendar.DAY_OF_WEEK);
				cal.roll(Calendar.DAY_OF_WEEK, true);
			}

			gbc.gridx = 1;
			gbc.gridy++;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			{
				JPanel panel = new JPanel(new GridLayout(0, 3));
				for (int i = 0; i < days.length; i++)
					panel.add(days[i]);
				add(panel, gbc);
			}
			addGlueLabel();
		}

		public void fillTimeStamp(TimeStamp ts) {
			ts.setPeriod(Calendar.WEEK_OF_YEAR, ((Number) weekSpin.getValue())
					.intValue());

			for (int i = 0; i < days.length; i++) {
				if (days[i].isSelected()) ts.addTestDate(Calendar.DAY_OF_WEEK,
				//((Integer) ht.get(days[i])).intValue()
						daysIds[i]);
			}

			for (Iterator it = time.iterator(); it.hasNext();) {
				String str = (String) it.next();
				int h = Integer.parseInt(str.substring(0, 2));
				int m = Integer.parseInt(str.substring(3, 5));
				ts.addTestTime(h, m, 0);
			}
			//return ts;
		}

		public void setTimeStamp(TimeStamp ts) {
			Time period = ts.getPeriod();
			switch (period.getScale()) {
				case Calendar.WEEK_OF_YEAR:
					weekRadioButton.doClick();
					weekSpin.setValue(new Integer(period.getValue()));

					LinkedList list = ts.getTimeList();
					time.clear();
					Calendar cal = Calendar.getInstance();
					for (Iterator it = list.iterator(); it.hasNext();) {
						DayTime dayTime = (DayTime) it.next();
						cal.set(Calendar.MINUTE, dayTime.getMinute());
						cal.set(Calendar.HOUR, dayTime.getHour());
						time.add(new SimpleDateFormat("HH:mm").format(cal
								.getTime()));
					}
					this.list.setListData(time);

					for (int i = 0; i < this.days.length; i++)
						this.days[i].setSelected(false);

					LinkedList days = ts.getDateList();

					for (Iterator it = days.iterator(); it.hasNext();) {
						Time time = (Time) it.next();
						if (time.getScale() == Calendar.DAY_OF_WEEK) {
							int value = time.getValue();
							for (int i = 0; i < daysIds.length; i++) {
								if (daysIds[i] == value)
										this.days[i].setSelected(true);
							}
						}
					}
					break;

				default:
					weekSpin.setValue(new Integer(0));
					this.list.removeAll();
					break;
			}

		}

	}

	private class MonthPanel extends TimePanelExt implements TimeStampFiller {

		private JSpinner	monthSpin	= new JSpinner(new SpinnerNumberModel(
												1, 1, 30, 1));

		private Vector		time		= new Vector();

		private Vector		days		= new Vector();

		private JList		list;

		private JList		dayList;

		protected MonthPanel() {
			super();
			gbc.weightx = 1.0;
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("Каждые"), gbc);
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			gbc.gridx++;
			//gbc.ipadx = TimeParametersPanel.btn_size.width;
			gbc.anchor = GridBagConstraints.CENTER;
			add(monthSpin, gbc);
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridx++;
			gbc.ipadx = 0;
			add(new JLabel("мес."), gbc);
			gbc.gridx = 1;
			gbc.gridy++;
			gbc.anchor = GridBagConstraints.LINE_START;
			addHorizontalSeparator();

			list = new JList();
			list.setListData(time);
			JScrollPane scroll = new JScrollPane(list);
			scroll
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scroll
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JButton addButton = addPlusButton();
			addButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel p = new JPanel();
					p.add(new JLabel("Время тестирования:"));
					TimeSpinner ts = new TimeSpinner();
					ts.getModel().setValue(new Date(0));
					p.add(ts);
					p.add(new JLabel(" (hh:mm)"));
					int res = JOptionPane.showConfirmDialog(MonthPanel.this, p,
							"Добавить", JOptionPane.OK_CANCEL_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						Date date = (Date) ts.getModel().getValue();
						time.add(new SimpleDateFormat("HH:mm").format(date));
						list.setListData(time);
					}
				}
			});
			final JButton removeButton = addMinusButton();
			removeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					time.remove(list.getSelectedIndex());
					list.setListData(time);
				}
			});

			list.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					removeButton.setEnabled(list.getSelectedIndex() >= 0);
				}
			});

			{
				JPanel panel = new JPanel(new GridBagLayout());
				int prevGridY = gbc.gridy;

				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				panel.add(new JLabel("Время"), gbc);
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridheight = 2;
				gbc.gridx++;
				gbc.ipadx = TimeParametersPanel.btn_size.height;
				gbc.ipady = TimeParametersPanel.btn_size.height;
				panel.add(scroll, gbc);
				gbc.ipadx = 0;
				gbc.ipady = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx++;
				gbc.weightx = 0.0;
				panel.add(addButton, gbc);
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.ipadx = 0;
				gbc.weightx = 1.0;
				panel.add(new JLabel("чч:мм"), gbc);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx += 2;
				gbc.weightx = 0.0;
				panel.add(removeButton, gbc);
				gbc.gridx = 1;
				gbc.gridy = prevGridY + 1;
				add(panel, gbc);
				gbc.weighty = 0.0;
			}

			dayList = new JList();
			dayList.setListData(days);
			JScrollPane dayscroll = new JScrollPane(dayList);
			dayscroll
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			dayscroll
					.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			JButton addDayButton = addPlusButton();
			addDayButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JPanel p = new JPanel();
					p.add(new JLabel("Число тестирования:"));
					JSpinner ts = new JSpinner(new SpinnerNumberModel(1, 1, 31,
							1));
					p.add(ts);
					int res = JOptionPane.showConfirmDialog(MonthPanel.this, p,
							"Добавить", JOptionPane.OK_CANCEL_OPTION);
					if (res == JOptionPane.OK_OPTION) {
						Integer num = (Integer) ts.getModel().getValue();
						days.add(num.toString());
						dayList.setListData(days);
					}
				}
			});
			final JButton removeDayButton = addMinusButton();
			removeDayButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (dayList.getSelectedIndex() != -1) {
						days.remove(dayList.getSelectedIndex());
						dayList.setListData(days);
					}
				}
			});
			dayList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					removeDayButton.setEnabled(dayList.getSelectedIndex() >= 0);
				}
			});
			{
				JPanel panel = new JPanel(new GridBagLayout());
				int prevGridY = gbc.gridy;

				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.weightx = 1.0;
				gbc.weighty = 0.0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				panel.add(new JLabel("Число"), gbc);
				gbc.gridwidth = GridBagConstraints.RELATIVE;
				gbc.gridheight = 2;
				gbc.gridx++;
				gbc.ipadx = TimeParametersPanel.btn_size.height;
				gbc.ipady = TimeParametersPanel.btn_size.height;
				panel.add(dayscroll, gbc);
				gbc.ipadx = 0;
				gbc.ipady = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx++;
				gbc.weightx = 0.0;
				panel.add(addDayButton, gbc);
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy++;
				gbc.ipadx = 0;
				gbc.weightx = 1.0;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				gbc.gridx += 2;
				gbc.weightx = 0.0;
				panel.add(removeDayButton, gbc);
				gbc.gridx = 1;
				gbc.gridy = prevGridY + 1;
				add(panel, gbc);
				gbc.weighty = 0.0;
			}

			addGlueLabel();
		}

		public void fillTimeStamp(TimeStamp ts) {
			ts.setPeriod(Calendar.MONTH, ((Number) monthSpin.getValue())
					.intValue());

			for (Iterator it = days.iterator(); it.hasNext();) {
				String str = (String) it.next();
				int d = Integer.parseInt(str);
				ts.addTestDate(Calendar.DAY_OF_MONTH, d);
			}

			for (Iterator it = time.iterator(); it.hasNext();) {
				String str = (String) it.next();
				int h = Integer.parseInt(str.substring(0, 2));
				int m = Integer.parseInt(str.substring(3, 5));
				ts.addTestTime(h, m, 0);
			}

			//return ts;
		}

		public void setTimeStamp(TimeStamp ts) {
			Time period = ts.getPeriod();
			switch (period.getScale()) {
				case Calendar.MONTH:
					hourRadioButton.doClick();
					monthSpin.setValue(new Integer(period.getValue()));

					LinkedList list = ts.getTimeList();
					time.clear();
					Calendar cal = Calendar.getInstance();
					for (Iterator it = list.iterator(); it.hasNext();) {
						DayTime dayTime = (DayTime) it.next();
						cal.set(Calendar.MINUTE, dayTime.getMinute());
						cal.set(Calendar.HOUR, dayTime.getHour());
						time.add(new SimpleDateFormat("HH:mm").format(cal
								.getTime()));
					}

					days.clear();
					list = ts.getDateList();
					for (Iterator it = list.iterator(); it.hasNext();) {
						Time time = (Time) it.next();
						if (time.getScale() == Calendar.DAY_OF_MONTH) {
							days.add(Integer.toString(time.getValue()));
						}
					}

					this.list.setListData(time);
					this.dayList.setListData(days);
					break;

				default:
					monthSpin.setValue(new Integer(0));
					this.list.removeAll();
					this.dayList.removeAll();
					break;
			}

		}

	}
}