
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPattern.TimeLine;

/*
 * 
 * Created on 20.05.2004
 *  
 */

/**
 * @author bob
 *  
 */
public class TimeStampUI {

	public static final Insets			NULL_INSETS				= new Insets(0, 0, 0, 0);

	public static final String[]		SHORT_DAY_OF_WEEK_NAMES	= new String[] { LangModelSchedule.getString("mon"),
			LangModelSchedule.getString("tue"), LangModelSchedule.getString("wed"), LangModelSchedule.getString("thu"),
			LangModelSchedule.getString("fri"), LangModelSchedule.getString("sat"), LangModelSchedule.getString("sun")};

	public static final String[]		SHORT_MONTH_NAMES		= new String[] { LangModelSchedule.getString("jan"),
			LangModelSchedule.getString("feb"), LangModelSchedule.getString("mar"), LangModelSchedule.getString("apr"),
			LangModelSchedule.getString("may"), LangModelSchedule.getString("jun"), LangModelSchedule.getString("jul"),
			LangModelSchedule.getString("aug"), LangModelSchedule.getString("sep"), LangModelSchedule.getString("oct"),
			LangModelSchedule.getString("nov"), LangModelSchedule.getString("dec")};

	private static final String			MINPANEL_EXTENDED		= "extended";												//$NON-NLS-1$

	private static final String			MINPANEL_NORMAL			= "normal";												//$NON-NLS-1$

	private TemporalPattern.TimeLine	timeLine;

	private JCheckBox					eachDayCheckBox;

	private JFrame						mainFrame;

	JSpinner							hourSpinnder;

	JCheckBox							hourCheckBox;

	JToggleButton[]						hoursToggleButton;

	JCheckBox							minuteCheckBox;

	JSpinner							minuteSpinnder;

	JCheckBox							minExtendedCheckBox;

	JToggleButton[]						minutesToggleButton;

	JToggleButton[]						normalMinToggleButton;

	JCheckBox							dayCheckBox;

	JSpinner							daySpinnder;

	JToggleButton[]						daysToggleButton;

	JToggleButton[]						weekToggleButton;

	JCheckBox							monthCheckBox;

	JSpinner							monthSpinnder;

	JToggleButton[]						monthToggleButton;

	private static final Dimension		PANE_DIMENSION			= new Dimension(390, 400);

	//	private static final Dimension PANEL_DIMENSION = new Dimension(420,
	//			400);

	public TimeStampUI() {
		// empty
	}

	public JSplitPane getPane() {
		final JPanel duringDayPanel = new JPanel(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weighty = 0.0;

		final JButton checkButton = new JButton(LangModelSchedule.getString("Check"));
		checkButton.setMargin(UIStorage.INSET_NULL);
		checkButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String description;
				int type = JOptionPane.INFORMATION_MESSAGE;
				try {
					description = TemporalPattern.getCronStringsDescription(new String[] { getTemplate()});
				} catch (IllegalArgumentException iae) {
					description = LangModelSchedule.getString("Some paremeters are not set.");
					type = JOptionPane.ERROR_MESSAGE;
				}

				JOptionPane.showMessageDialog(duringDayPanel, description, null, type);
			}
		});
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringDayPanel.add(checkButton, gbc);

		this.eachDayCheckBox = new JCheckBox(LangModelSchedule.getString("each_day"), false); //$NON-NLS-1$
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringDayPanel.add(this.eachDayCheckBox, gbc);
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("Hours") + ":"), gbc); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = 1;
		this.hourCheckBox = new JCheckBox(LangModelSchedule.getString("each"), true); //$NON-NLS-1$
		this.hourSpinnder = new JSpinner(new SpinnerNumberModel(1, 1, 23, 1));
		this.hourCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				TimeStampUI.this.hourSpinnder.setEnabled(checkBox.isSelected());
			}
		});
		//hourCheckBox.doClick();
		gbc.gridwidth = 1;
		duringDayPanel.add(this.hourCheckBox, gbc);
		//gbc.weightx = 10.0;
		//gbc.gridwidth = GridBagConstraints.RELATIVE;
		duringDayPanel.add(this.hourSpinnder, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("hour_s")), gbc); //$NON-NLS-1$
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		//duringDayPanel.add(new JLabel(), gbc);
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("and")), gbc); //$NON-NLS-1$

		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		JPanel hourPanel = new JPanel(new GridBagLayout());
		this.hoursToggleButton = new JToggleButton[24];
		gbc.fill = GridBagConstraints.BOTH;
		for (int i = 0; i < this.hoursToggleButton.length; i++) {
			this.hoursToggleButton[i] = new JToggleButton(Integer.toString(i), false);
			this.hoursToggleButton[i].setMargin(NULL_INSETS);
			gbc.gridwidth = 1;
			if ((i + 2) % 6 == 0)
				gbc.gridwidth = GridBagConstraints.RELATIVE;
			if ((i + 1) % 6 == 0)
				gbc.gridwidth = GridBagConstraints.REMAINDER;
			hourPanel.add(this.hoursToggleButton[i], gbc);
		}
		gbc.fill = GridBagConstraints.NONE;
		duringDayPanel.add(hourPanel, gbc);

		gbc.weightx = 1.0;
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("Minutes") + ":"), gbc); //$NON-NLS-1$ //$NON-NLS-2$
		this.minuteCheckBox = new JCheckBox(LangModelSchedule.getString("each"), true);
		this.minuteSpinnder = new JSpinner(new SpinnerNumberModel(1, 1, 59, 1));
		this.minuteCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				TimeStampUI.this.minuteSpinnder.setEnabled(checkBox.isSelected());
			}
		});
		//minuteCheckBox.doClick();

		//gbc.weightx = 1.0;
		//gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		duringDayPanel.add(this.minuteCheckBox, gbc);
		//gbc.weightx = 1.0;
		duringDayPanel.add(this.minuteSpinnder, gbc);
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("minute_s")), gbc); //$NON-NLS-1$
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringDayPanel.add(new JLabel(), gbc);
		duringDayPanel.add(new JLabel(LangModelSchedule.getString("and")), gbc);
		this.minExtendedCheckBox = new JCheckBox(LangModelSchedule.getString("extended_min_list")); //$NON-NLS-1$

		//gbc.weightx = 11.0;
		duringDayPanel.add(this.minExtendedCheckBox, gbc);
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		//gbc.anchor = GridBagConstraints.LINE_START;
		JPanel normalMinPanel = new JPanel(new GridBagLayout());
		JPanel extMinPanel = new JPanel(new GridBagLayout());
		gbc.fill = GridBagConstraints.BOTH;
		this.minutesToggleButton = new JToggleButton[60];
		this.normalMinToggleButton = new JToggleButton[12];
		for (int i = 0; i < this.minutesToggleButton.length; i++) {
			this.minutesToggleButton[i] = new JToggleButton(Integer.toString(i), false);
			this.minutesToggleButton[i].setMargin(NULL_INSETS);
			gbc.gridwidth = 1;
			if ((i + 2) % 10 == 0)
				gbc.gridwidth = GridBagConstraints.RELATIVE;
			if ((i + 1) % 10 == 0)
				gbc.gridwidth = GridBagConstraints.REMAINDER;
			extMinPanel.add(this.minutesToggleButton[i], gbc);
			if (i % 5 == 0) {
				gbc.gridwidth = 1;
				this.normalMinToggleButton[i / 5] = new JToggleButton(Integer.toString(i), false);
				this.normalMinToggleButton[i / 5].setMargin(NULL_INSETS);
				if ((i * 5 + 2) % 6 == 0)
					gbc.gridwidth = GridBagConstraints.RELATIVE;
				if ((i * 5 + 1) % 6 == 0)
					gbc.gridwidth = GridBagConstraints.REMAINDER;

				normalMinPanel.add(this.normalMinToggleButton[i / 5], gbc);
			}

		}
		gbc.fill = GridBagConstraints.NONE;
		gbc.weighty = 1.0;
		normalMinPanel.add(new JLabel(), gbc);
		gbc.weighty = 0.0;
		gbc.weightx = 1.0;

		final JPanel minPanel = new JPanel(new CardLayout());
		//minPanel.setBorder(BorderFactory.createEtchedBorder());
		minPanel.add(normalMinPanel, MINPANEL_NORMAL);
		minPanel.add(extMinPanel, MINPANEL_EXTENDED);
		//normalMinPanel.add(new JLabel("A"),gbc);

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 0.0;
		duringDayPanel.add(minPanel, gbc);
		{
			CardLayout cl = (CardLayout) (minPanel.getLayout());
			cl.show(minPanel, MINPANEL_NORMAL);
		}
		minPanel.revalidate();
		this.minExtendedCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				CardLayout cl = (CardLayout) (minPanel.getLayout());
				if (checkBox.isSelected()) {
					for (int i = 0; i < TimeStampUI.this.normalMinToggleButton.length; i++) {
						TimeStampUI.this.minutesToggleButton[i * 5]
								.setSelected(TimeStampUI.this.normalMinToggleButton[i].isSelected());
					}
					cl.show(minPanel, MINPANEL_EXTENDED);
				} else {
					for (int i = 0; i < TimeStampUI.this.normalMinToggleButton.length; i++) {
						TimeStampUI.this.normalMinToggleButton[i]
								.setSelected(TimeStampUI.this.minutesToggleButton[i * 5].isSelected());
					}
					cl.show(minPanel, MINPANEL_NORMAL);
				}
			}
		});
		gbc.weighty = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringDayPanel.add(new JLabel(), gbc);

		JPanel duringMonthPanel = new JPanel(new GridBagLayout());
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.weighty = 0.0;
		//gbc.weightx = 11.0;

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("Days") + ":"), gbc); //$NON-NLS-1$ //$NON-NLS-2$
		gbc.gridwidth = 1;
		this.dayCheckBox = new JCheckBox(LangModelSchedule.getString("each"), true);
		this.daySpinnder = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
		this.dayCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				TimeStampUI.this.daySpinnder.setEnabled(checkBox.isSelected());
			}
		});
		this.hourCheckBox.doClick();
		//gbc.weightx = 10.0;
		gbc.weightx = 1.0;
		duringMonthPanel.add(this.dayCheckBox, gbc);
		gbc.weightx = 1.0;

		gbc.gridwidth = GridBagConstraints.RELATIVE;
		duringMonthPanel.add(this.daySpinnder, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("day_s")), gbc); //$NON-NLS-1$
		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("and")), gbc);
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		JPanel dayOfMonthPanel = new JPanel(new GridBagLayout());
		this.daysToggleButton = new JToggleButton[31];
		gbc.fill = GridBagConstraints.BOTH;
		for (int i = 0; i < this.daysToggleButton.length; i++) {
			this.daysToggleButton[i] = new JToggleButton(Integer.toString(i + 1), false);
			this.daysToggleButton[i].setMargin(NULL_INSETS);
			gbc.gridwidth = 1;
			if ((i + 2) % 7 == 0)
				gbc.gridwidth = GridBagConstraints.RELATIVE;
			if ((i + 1) % 7 == 0)
				gbc.gridwidth = GridBagConstraints.REMAINDER;
			dayOfMonthPanel.add(this.daysToggleButton[i], gbc);
		}
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(dayOfMonthPanel, gbc);

		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("DayOfWeek") + ":"), gbc); //$NON-NLS-1$ //$NON-NLS-2$

		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		JPanel dayOfWeekPanel = new JPanel(new GridBagLayout());
		this.weekToggleButton = new JToggleButton[SHORT_DAY_OF_WEEK_NAMES.length];
		gbc.fill = GridBagConstraints.BOTH;
		for (int i = 0; i < this.weekToggleButton.length; i++) {
			this.weekToggleButton[i] = new JToggleButton(SHORT_DAY_OF_WEEK_NAMES[i], true);
			this.weekToggleButton[i].setMargin(NULL_INSETS);
			gbc.gridwidth = 1;
			if ((i + 2) % 4 == 0)
				gbc.gridwidth = GridBagConstraints.RELATIVE;
			if ((i + 1) % 4 == 0)
				gbc.gridwidth = GridBagConstraints.REMAINDER;
			dayOfWeekPanel.add(this.weekToggleButton[i], gbc);
		}
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(dayOfWeekPanel, gbc);

		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("Month") + ":"), gbc); //$NON-NLS-1$ //$NON-NLS-2$
		this.monthSpinnder = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
		this.monthCheckBox = new JCheckBox(LangModelSchedule.getString("each"), true);
		this.monthCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				TimeStampUI.this.monthSpinnder.setEnabled(checkBox.isSelected());
			}
		});
		this.hourCheckBox.doClick();
		gbc.weightx = 1.0;
		//gbc.weightx = 10.0;
		gbc.gridwidth = 1;
		duringMonthPanel.add(this.monthCheckBox, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		duringMonthPanel.add(this.monthSpinnder, gbc);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("month_s")), gbc); //$NON-NLS-1$
		duringMonthPanel.add(new JLabel(LangModelSchedule.getString("and")), gbc);
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		JPanel monthsPanel = new JPanel(new GridBagLayout());
		this.monthToggleButton = new JToggleButton[SHORT_MONTH_NAMES.length];
		gbc.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < this.monthToggleButton.length; i++) {
			this.monthToggleButton[i] = new JToggleButton(SHORT_MONTH_NAMES[i], false);
			this.monthToggleButton[i].setMargin(NULL_INSETS);
			gbc.gridwidth = 1;
			if ((i + 2) % 4 == 0)
				gbc.gridwidth = GridBagConstraints.RELATIVE;
			if ((i + 1) % 4 == 0)
				gbc.gridwidth = GridBagConstraints.REMAINDER;
			monthsPanel.add(this.monthToggleButton[i], gbc);
		}
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(monthsPanel, gbc);

		gbc.weighty = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		duringMonthPanel.add(new JLabel(), gbc);

		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, duringMonthPanel, duringDayPanel);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(180);
		this.eachDayCheckBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JCheckBox checkBox = (JCheckBox) e.getSource();
				boolean eachDay = !checkBox.isSelected();
				splitPane.getLeftComponent().setVisible(eachDay);
				splitPane.setOneTouchExpandable(eachDay);
				splitPane.setDividerLocation(eachDay ? 180 : 0);

			}
		});
		this.eachDayCheckBox.doClick();
		splitPane.setPreferredSize(PANE_DIMENSION);
		splitPane.setMinimumSize(PANE_DIMENSION);
		return splitPane;
	}

	public static void main(String[] args) {

		final TemporalPattern timeStamp;
		//Calendar c = Calendar.getInstance();
		try {
			timeStamp = TemporalPattern.createInstance(null, null, new LinkedList());
		
			// 5);
			timeStamp.addTemplate("*/20 0-9 */2 2,5 0,6"); //$NON-NLS-1$
			timeStamp.addTemplate("*/20 * * * *");
			//timeStamp.addTemplate("*/10 * * * *");
	
			final JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//		java.util.List descriptions = new LinkedList();
			JList list = new JList(new DefaultListModel());
			DefaultListModel model = (DefaultListModel) list.getModel();
			for (Iterator it = timeStamp.getTimeLines().iterator(); it.hasNext();) {
				TimeLine timeLine = (TimeLine) it.next();
				//			descriptions.add(timeLine.getDescription());
				model.addElement(timeLine);
			}
			
			//		JList list = new JList(descriptions.toArray());
			JScrollPane scrollPane = new JScrollPane(list);
	
			final TimeStampUI demo = new TimeStampUI();
			list.addMouseListener(new MouseListener() {
	
				public void mouseClicked(MouseEvent e) {
					final JList jlist = (JList) e.getSource();
					TimeLine timeLine = (TimeLine) jlist.getSelectedValue();
					//JFrame mainFrame = demo.getTimeLineEditor();
					final String template = timeLine.getTemplate();
					JSplitPane pane = demo.getPane();
					demo.setTimeLine(timeLine);
					int result = JOptionPane.showConfirmDialog(frame, pane, "Time Line", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						System.out.println("was:" + template);
						timeStamp.removeTemplate(template);
						String template2 = demo.getTemplate();
						System.out.println("now:" + template2);
						timeStamp.addTemplate(template2);
						DefaultListModel model = (DefaultListModel) jlist.getModel();
						model.removeAllElements();
						for (Iterator it = timeStamp.getTimeLines().iterator(); it.hasNext();) {
							TimeLine timeLine2 = (TimeLine) it.next();
							model.addElement(timeLine2);
						}
	
					}
	
				}
	
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
	
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
	
				public void mousePressed(MouseEvent e) {
					this.mouseClicked(e);
	
				}
	
				public void mouseReleased(MouseEvent e) {
					this.mouseClicked(e);
				}
			});
			frame.getContentPane().add(scrollPane);
			frame.pack();
			frame.setVisible(true);
	
			//		TimeStampUI demo = new TimeStampUI();
			//		JFrame mainFrame = demo.getTimeLineEditor();
			//		demo.setTimeLine((TimeLine)
			// timeStamp.getTimeLines().iterator().next());
			//		mainFrame.addWindowListener(new WindowAdapter() {
			//
			//			public void windowClosing(WindowEvent e) {
			//				System.exit(0);
			//			}
			//		});
		} catch (CreateObjectException e1) {
			e1.printStackTrace();
		}

	}

	public JFrame getTimeLineEditor() {
		if (this.mainFrame == null) {
			this.mainFrame = new JFrame(this.timeLine == null ? "TimeLine" : this.timeLine //$NON-NLS-1$
					.getDescription());
			this.mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			this.mainFrame.getContentPane().add(getPane());
		}
		this.mainFrame.pack();
		this.mainFrame.setVisible(true);

		return this.mainFrame;
	}

	/**
	 * @return Returns the timeLine.
	 */
	public TemporalPattern.TimeLine getTimeLine() {
		return this.timeLine;
	}

	public String getTemplate() {
		StringBuffer template = new StringBuffer();
		boolean needComma = false;

		if (this.minuteCheckBox.isSelected()) {
			needComma = true;
			template.append("*");
			int del = Integer.parseInt(this.minuteSpinnder.getValue().toString());
			if (del > 1) {
				template.append("/");
				template.append(del);
			}
			for (int i = 0; i < this.minutesToggleButton.length; i++) {
				if ((this.minutesToggleButton[i].isSelected()) && (i % del == 0)) {
					this.minutesToggleButton[i].setSelected(false);
				}
			}
			for (int i = 0; i < this.normalMinToggleButton.length; i++) {
				if ((this.normalMinToggleButton[i].isSelected()) && ((i * 5) % del == 0)) {
					this.normalMinToggleButton[i].setSelected(false);
				}
			}
		}
		if (this.minExtendedCheckBox.isSelected()) {
			for (int i = 0; i < this.minutesToggleButton.length; i++) {
				int start = i;
				int end = i;
				if (this.minutesToggleButton[i].isSelected()) {
					for (int j = i + 1; j < this.minutesToggleButton.length; j++) {
						if (!this.minutesToggleButton[j].isSelected()) {
							end = j - 1;
							break;
						}
					}
					if (needComma)
						template.append(",");

					if (start == end) {
						template.append(start);
					} else {
						template.append(start);
						template.append("-");
						template.append(end);
						i = end;
					}
					needComma = true;
				}
			}
		} else {
			for (int i = 0; i < this.normalMinToggleButton.length; i++) {
				if (this.normalMinToggleButton[i].isSelected()) {
					if (needComma)
						template.append(",");
					template.append(i * 5);
					needComma = true;
				}
			}
		}
		template.append(" ");

		needComma = false;
		if (this.hourCheckBox.isSelected()) {
			needComma = true;
			template.append("*");
			int del = Integer.parseInt(this.hourSpinnder.getValue().toString());
			if (del > 1) {
				template.append("/");
				template.append(del);
			}
			for (int i = 0; i < this.hoursToggleButton.length; i++) {
				if ((this.hoursToggleButton[i].isSelected()) && (i % del == 0)) {
					this.hoursToggleButton[i].setSelected(false);
				}
			}
		}
		for (int i = 0; i < this.hoursToggleButton.length; i++) {
			int start = i;
			int end = i;
			if (this.hoursToggleButton[i].isSelected()) {
				for (int j = i + 1; j < this.hoursToggleButton.length; j++) {
					if (!this.hoursToggleButton[j].isSelected()) {
						end = j - 1;
						break;
					}
				}
				if (needComma)
					template.append(",");
				if (start == end) {
					template.append(start);
				} else {
					template.append(start);
					template.append("-");
					template.append(end);
					i = end;
				}
				needComma = true;
			}
		}

		needComma = false;
		template.append(" ");

		if (this.eachDayCheckBox.isSelected()) {
			template.append("* * *");
		} else {
			if (this.dayCheckBox.isSelected()) {
				needComma = true;
				template.append("*");
				int del = Integer.parseInt(this.daySpinnder.getValue().toString());
				if (del > 1) {
					template.append("/");
					template.append(del);
				}
				for (int i = 0; i < this.daysToggleButton.length; i++) {
					if ((this.daysToggleButton[i].isSelected()) && (i + 1 % del == 0)) {
						this.daysToggleButton[i].setSelected(false);
					}
				}

			}
			for (int i = 0; i < this.daysToggleButton.length; i++) {
				int start = i;
				int end = i;
				if (this.daysToggleButton[i].isSelected()) {
					for (int j = i + 1; j < this.daysToggleButton.length; j++) {
						if (!this.daysToggleButton[j].isSelected()) {
							end = j - 1;
							break;
						}
					}

					if (needComma)
						template.append(",");
					if (start == end) {
						template.append(start + 1);
					} else {
						template.append(start + 1);
						template.append("-");
						template.append(end);
						i = end;
					}
					needComma = true;
				}
			}

			needComma = false;
			template.append(" ");

			if (this.monthCheckBox.isSelected()) {
				needComma = true;
				template.append("*");
				int del = Integer.parseInt(this.monthSpinnder.getValue().toString());
				if (del > 1) {
					template.append("/");
					template.append(del);
				}
				for (int i = 0; i < this.monthToggleButton.length; i++) {
					if ((this.monthToggleButton[i].isSelected()) && (i % del == 0)) {
						this.monthToggleButton[i].setSelected(false);
					}
				}

			}

			for (int i = 0; i < this.monthToggleButton.length; i++) {
				int start = i;
				int end = i;
				if (this.monthToggleButton[i].isSelected()) {
					for (int j = i; j < this.monthToggleButton.length; j++) {
						if (!this.monthToggleButton[j].isSelected()) {
							end = j - 1;
							break;
						}
					}

					if (needComma)
						template.append(",");
					if (start == end) {
						template.append(start);
					} else {
						template.append(start);
						template.append("-");
						template.append(end);
						i = end;
					}
					needComma = true;
				}
			}

			needComma = false;
			template.append(" ");

			for (int i = 0; i < this.weekToggleButton.length; i++) {
				if (this.weekToggleButton[i].isSelected()) {
					if (needComma)
						template.append(",");
					template.append((i == 6) ? 0 : i + 1);
					needComma = true;
				}
			}

		}

		return template.toString();
	}

	/**
	 * @param timeLine
	 *            The timeLine to set.
	 */
	public void setTimeLine(TemporalPattern.TimeLine timeLine) {
		this.timeLine = timeLine;
		if (timeLine.getDateList() == null)
			timeLine.parseTemplate();
		String description = timeLine.getDescription();
		//this.mainFrame.setTitle(description);
		TemporalPattern.TimeValue minute = timeLine.getMinutes();
		TemporalPattern.TimeValue hour = timeLine.getHours();
		TemporalPattern.TimeValue dayOfMonth = timeLine.getDayOfMonth();
		TemporalPattern.TimeValue dayOfWeek = timeLine.getDayOfWeek();
		TemporalPattern.TimeValue month = timeLine.getMonth();
		{
			boolean moreThanOneDay = (timeLine.getEndPeriod() - timeLine.getStartPeriod() > 1000 * 60 * 60 * 24);
			moreThanOneDay = true;
			if ((month.isAll()) && (dayOfWeek.isAll()) && (dayOfMonth.isAll()) || (!moreThanOneDay)) {
				if (!this.eachDayCheckBox.isSelected())
					this.eachDayCheckBox.doClick();
			} else {
				if (this.eachDayCheckBox.isSelected())
					this.eachDayCheckBox.doClick();
			}

			//			this.eachDayCheckBox
			//					.setVisible((timeLine.getEndPeriod() -
			// timeLine.getStartPeriod() > 1000 * 60 * 60 * 24));
		}

		{
			// set hours
			/**
			 * @todo just only one periodic value
			 */
			boolean divisorFound = false;
			if (hour.divisor != null) {
				if (hour.divisor.length == 1) {
					if (!this.hourCheckBox.isSelected())
						this.hourCheckBox.doClick();
					divisorFound = true;
					this.hourSpinnder.getModel().setValue(new Integer(hour.divisor[0]));
				}
			}

			if (!divisorFound) {
				if (this.hourCheckBox.isSelected())
					this.hourCheckBox.doClick();
			}

			for (int i = 0; i < this.hoursToggleButton.length; i++)
				this.hoursToggleButton[i].setSelected(false);
			if ((hour.starts != null) && (hour.ends != null)) {
				if (hour.starts.length == hour.ends.length) {
					for (int i = 0; i < hour.starts.length; i++) {
						int start = hour.starts[i];
						int end = hour.ends[i];
						for (int j = start; j <= end; j++) {
							this.hoursToggleButton[j].setSelected(true);
						}
					}
				}
			}

		}

		{
			// set minutes
			/**
			 * @todo just only one periodic value
			 */
			boolean divisorFound = false;
			if (minute.divisor != null) {
				if (minute.divisor.length == 1) {
					if (!this.minuteCheckBox.isSelected())
						this.minuteCheckBox.doClick();
					divisorFound = true;
					this.minuteSpinnder.getModel().setValue(new Integer(minute.divisor[0]));
				}
			}

			if (!divisorFound) {
				if (this.minuteCheckBox.isSelected())
					this.minuteCheckBox.doClick();
			}

			boolean useNormalMin = true;
			if ((minute.starts != null) && (minute.ends != null)) {
				if (minute.starts.length == minute.ends.length) {
					for (int i = 0; i < minute.starts.length; i++) {
						int start = minute.starts[i];
						int end = minute.ends[i];
						if (!((start == end) && (start % 5 == 0))) {
							useNormalMin = false;
							break;
						}
					}
				}
			}
			if (useNormalMin) {
				if (this.minExtendedCheckBox.isSelected())
					this.minExtendedCheckBox.doClick();
				for (int i = 0; i < this.normalMinToggleButton.length; i++)
					this.normalMinToggleButton[i].setSelected(false);
				if ((minute.starts != null) && (minute.ends != null)) {
					if (minute.starts.length == minute.ends.length) {
						for (int i = 0; i < minute.starts.length; i++) {
							int start = minute.starts[i];
							this.normalMinToggleButton[start / 5].setSelected(true);
						}
					}
				}
			} else {
				if (!this.minExtendedCheckBox.isSelected())
					this.minExtendedCheckBox.doClick();
				for (int i = 0; i < this.minutesToggleButton.length; i++)
					this.minutesToggleButton[i].setSelected(false);
				if ((minute.starts != null) && (minute.ends != null)) {
					if (minute.starts.length == minute.ends.length) {
						for (int i = 0; i < minute.starts.length; i++) {
							int start = minute.starts[i];
							int end = minute.ends[i];
							for (int j = start; j <= end; j++) {
								this.minutesToggleButton[j].setSelected(true);
							}
						}
					}
				}
			}

		}
		{
			// set days
			/**
			 * @todo just only one periodic value
			 */
			boolean divisorFound = false;
			if (dayOfMonth.divisor != null) {
				if (dayOfMonth.divisor.length == 1) {
					if (!this.dayCheckBox.isSelected())
						this.dayCheckBox.doClick();
					divisorFound = true;
					this.daySpinnder.getModel().setValue(new Integer(dayOfMonth.divisor[0]));
				}
			}

			if (!divisorFound) {
				if (this.dayCheckBox.isSelected())
					this.dayCheckBox.doClick();
			}

			for (int i = 0; i < this.daysToggleButton.length; i++)
				this.daysToggleButton[i].setSelected(false);
			if ((dayOfMonth.starts != null) && (dayOfMonth.ends != null)) {
				if (dayOfMonth.starts.length == dayOfMonth.ends.length) {
					for (int i = 0; i < dayOfMonth.starts.length; i++) {
						int start = dayOfMonth.starts[i];
						int end = dayOfMonth.ends[i];
						for (int j = start; j <= end; j++) {
							this.daysToggleButton[j - 1].setSelected(true);
						}
					}
				}
			}

		}

		{
			// set months
			/**
			 * @todo just only one periodic value
			 */
			boolean divisorFound = false;
			if (month.divisor != null) {
				if (month.divisor.length == 1) {
					if (!this.monthCheckBox.isSelected())
						this.monthCheckBox.doClick();
					divisorFound = true;
					this.monthSpinnder.getModel().setValue(new Integer(month.divisor[0]));
				}
			}

			if (!divisorFound) {
				if (this.monthCheckBox.isSelected())
					this.monthCheckBox.doClick();
			}

			for (int i = 0; i < this.monthToggleButton.length; i++)
				this.monthToggleButton[i].setSelected(false);
			if ((month.starts != null) && (month.ends != null)) {
				if (month.starts.length == month.ends.length) {
					for (int i = 0; i < month.starts.length; i++) {
						int start = month.starts[i];
						int end = month.ends[i];
						for (int j = start; j <= end; j++) {
							this.monthToggleButton[j].setSelected(true);
						}
					}
				}
			}

		}

		{
			//			 set day of week
			boolean[] week = new boolean[this.weekToggleButton.length];
			for (int i = 0; i < week.length; i++)
				week[i] = false;
			if (dayOfWeek.divisor != null) {
				for (int i = 0; i < dayOfWeek.divisor.length; i++) {
					int divisor = dayOfWeek.divisor[i];
					for (int j = 0; j < week.length; j++)
						if (j % divisor == 0)
							week[j] = true;
				}
			}

			if ((dayOfWeek.starts != null) && (dayOfWeek.ends != null)) {
				if (dayOfWeek.starts.length == dayOfWeek.ends.length) {
					for (int i = 0; i < dayOfWeek.starts.length; i++) {
						int start = dayOfWeek.starts[i];
						int end = dayOfWeek.ends[i];
						for (int j = start; j <= end; j++) {
							week[j] = true;
						}
					}
				}
			}

			for (int i = 0; i < this.weekToggleButton.length; i++) {
				if (i == 0)
					this.weekToggleButton[this.weekToggleButton.length - 1].setSelected(week[i]);
				else
					this.weekToggleButton[i - 1].setSelected(week[i]);
			}

		}

	}
}