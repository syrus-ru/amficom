package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;

class PlanToolBar extends JPanel {

	private Dimension btn_size = new Dimension(24, 24);	
	private static  int h = 22;
	private ApplicationContext aContext;
	private AComboBox scaleComboBox;
	private JSpinner dateSpinner = new DateSpinner();
	private JSpinner timeSpinner = new TimeSpinner();

	//JButton dateButton = new JButton("^");
	private JButton dateButton = new JButton(UIUtil.CALENDAR_ICON);

	private JButton nowButton = new JButton(UIUtil.TIME_ICON);
	private JButton applyButton = new JButton();

	private JButton zoomInButton = new JButton();
	private JButton zoomOutButton = new JButton();
	private JButton zoomNoneButton = new JButton();

	private PlanPanel panel;

	PlanToolBar(ApplicationContext aContext, PlanPanel panel) {
		this.aContext = aContext;
		this.panel = panel;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	


	private void jbInit() throws Exception {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LAST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		Box box = new Box(BoxLayout.X_AXIS);

		String[] suportedScales = PlanPanel.getSupportedScales();
		String[] scales = new String[suportedScales.length];
		for (int i = 0; i < scales.length; i++)
			scales[i] = new String(LangModelSchedule.getString(suportedScales[i]));
		scaleComboBox = new AComboBox(scales);
		scaleComboBox.setSelectedIndex(3);
		dateButton.setMargin(UIUtil.INSET_NULL);
		dateButton.setFocusable(false);
		dateButton.setToolTipText(LangModelSchedule.getComponentToolTip("CalendarButton"));
		dateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCalendar();
			}
		});
		zoomInButton.setPreferredSize(btn_size);
		zoomInButton.setMinimumSize(btn_size);
		zoomInButton.setMinimumSize(btn_size);
		zoomInButton.setFocusable(false);
		zoomInButton.setIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/zoom_in.gif")));
		zoomInButton.setToolTipText(LangModelSchedule.getComponentToolTip("ZoomIn"));
		zoomInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomInButton_actionPerformed();
			}
		});
		zoomOutButton.setPreferredSize(btn_size);
		zoomOutButton.setMinimumSize(btn_size);
		zoomOutButton.setMinimumSize(btn_size);
		zoomOutButton.setFocusable(false);
		zoomOutButton.setIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/zoom_out.gif")));
		zoomOutButton.setToolTipText(LangModelSchedule.getComponentToolTip("ZoomOut"));
		zoomOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomOutButton_actionPerformed();
			}
		});
		zoomNoneButton.setPreferredSize(btn_size);
		zoomNoneButton.setMinimumSize(btn_size);
		zoomNoneButton.setMinimumSize(btn_size);
		zoomNoneButton.setFocusable(false);
		zoomNoneButton.setIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage(
					"images/zoom_actual.gif")));
		zoomNoneButton.setToolTipText(LangModelSchedule.getComponentToolTip("ZoomNone"));
		zoomNoneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				zoomNoneButton_actionPerformed();
			}
		});

		box.add(new JLabel("Детализация:"));
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(75, h);
			UIUtil.setRigidSize(scaleComboBox, d);
		}
		
		box.add(scaleComboBox);		
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel("Дата:"));
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(137,h);
			UIUtil.setRigidSize(dateSpinner, d);
		}
		box.add(dateSpinner);
		box.add(dateButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(new JLabel("Время:"));
		box.add(Box.createHorizontalStrut(4));
		{
			Dimension d = new Dimension(55, h);
			UIUtil.setRigidSize(timeSpinner,d);
		}
		box.add(timeSpinner);
		nowButton.setMargin(UIUtil.INSET_NULL);
		box.add(nowButton);
		box.add(Box.createHorizontalStrut(10));
		box.add(applyButton);

		applyButton.setIcon(
			new ImageIcon(
				Toolkit.getDefaultToolkit().getImage("images/refresh.gif")));
		applyButton.setToolTipText(LangModelSchedule.getComponentToolTip("apply"));

		box.add(Box.createHorizontalGlue());
		box.add(zoomInButton);
		box.add(zoomOutButton);
		box.add(zoomNoneButton);
		add(box,gbc);

		nowButton.setFocusable(false);
		nowButton.setToolTipText(
			LangModelSchedule.getComponentToolTip("CurrentTimeButton"));
		nowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dateSpinner.setValue(new Date(System.currentTimeMillis()));
				timeSpinner.setValue(new Date(System.currentTimeMillis()));
			}
		});

		applyButton.setFocusable(false);
		applyButton.setToolTipText(LangModelSchedule.getComponentToolTip("ApplyButton"));
		applyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				apply_changes();
			}
		});

	}

	void apply_changes() {
		Calendar date_cal = Calendar.getInstance();
		date_cal.setTime((Date) dateSpinner.getValue());
		Calendar time_cal = Calendar.getInstance();
		time_cal.setTime((Date) timeSpinner.getValue());

		date_cal.set(Calendar.HOUR_OF_DAY, time_cal.get(Calendar.HOUR_OF_DAY));
		date_cal.set(Calendar.MINUTE, time_cal.get(Calendar.MINUTE));

		panel.updateDate(date_cal.getTime(), scaleComboBox.getSelectedIndex());
	}

	void showCalendar() {
		Calendar cal = Calendar.getInstance();
		Date date = (Date) dateSpinner.getModel().getValue();
		cal.setTime(date);

		JDialog calendarDialog =
			CalendarUI.createDialogInstance(
				Environment.getActiveWindow(),
				cal,
				true,
				true);
		calendarDialog.setSize(new Dimension(200, 200));
		calendarDialog.setResizable(false);
		calendarDialog.setLocation(
			new Point(
				dateSpinner.getLocationOnScreen().x - 35,
				dateSpinner.getLocationOnScreen().y + h));
		calendarDialog.setVisible(true);
		if (((CalendarUI) calendarDialog.getContentPane()).getStatus()
			== CalendarUI.STATUS_OK)
			dateSpinner.getModel().setValue(cal.getTime());
	}

	void zoomInButton_actionPerformed() {
		panel.updScale(1.25);
	}

	void zoomOutButton_actionPerformed() {
		panel.updScale(.8);
	}

	void zoomNoneButton_actionPerformed() {
		panel.updScale2Fit();
	}
}
