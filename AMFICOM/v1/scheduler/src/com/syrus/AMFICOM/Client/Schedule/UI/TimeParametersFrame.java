
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.CalendarUI;
import com.syrus.AMFICOM.Client.General.UI.DateSpinner;
import com.syrus.AMFICOM.Client.General.UI.TimeSpinner;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestTemporalStampsEditor;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client_.general.ui_.ObjList;
import com.syrus.AMFICOM.client_.general.ui_.ObjListModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternController;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.TemporalPattern.TimeLine;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

public class TimeParametersFrame extends JInternalFrame {

	private static final long	serialVersionUID	= 6562288896016470275L;

	public class TimeParametersPanel extends JPanel implements TestTemporalStampsEditor {

		private static final long	serialVersionUID	= -7975294015403739057L;

		SchedulerModel schedulerModel;
		ApplicationContext			aContext;

		private TimeSpinner			startTimeSpinner;

		private DateSpinner			startDateSpinner;

		TimeSpinner					endTimeSpinner;

		DateSpinner					endDateSpinner;

		ObjList						timeStamps;

		JRadioButton				oneRadioButton;

		private JRadioButton		continuosRadioButton;

		private JRadioButton		periodicalRadioButton;

		private JRadioButton		synchroRadioButton;

		private JRadioButton		alternateRadioButton;

		JButton						createButton;

		JButton						applyButton;
		Collection					temporalPatterns;

//		private Test				test;

		public TimeParametersPanel() {
			init();
		}

		public TimeParametersPanel(ApplicationContext aContext) {
			this.aContext = aContext;
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.schedulerModel.setTestTemporalStampsEditor(this);
			init();
		}

		private void init() {
			final GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.ipadx = 0;
			gbc.ipady = 0;
			gbc.insets = UIStorage.INSET_NULL;
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

			this.startTimeSpinner = new TimeSpinner();
			this.startDateSpinner = new DateSpinner();
			final JButton startDateButton = new JButton(UIStorage.CALENDAR_ICON);
			startDateButton.setMargin(UIStorage.INSET_NULL);
			startDateButton.setDefaultCapable(false);

			startDateButton.setFocusable(false);
			startDateButton.setToolTipText(LangModelSchedule.getString("Calendar"));
			startDateButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					showStartCalendar();
				}
			});
			{
				Box box = new Box(BoxLayout.X_AXIS);
				box.add(this.startDateSpinner);
				box.add(startDateButton);
				box.add(Box.createHorizontalGlue());
				box.add(this.startTimeSpinner);
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
			this.endTimeSpinner = new TimeSpinner();
			this.endDateSpinner = new DateSpinner();
			final JButton endDateButton = new JButton(UIStorage.CALENDAR_ICON);
			endDateButton.setMargin(UIStorage.INSET_NULL);
			endDateButton.setDefaultCapable(false);

			endDateButton.setFocusable(false);
			endDateButton.setToolTipText(LangModelSchedule.getString("Calendar"));
			endDateButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					showEndCalendar();
				}
			});
			{
				Box box = new Box(BoxLayout.X_AXIS);
				box.add(this.endDateSpinner);
				box.add(endDateButton);
				box.add(Box.createHorizontalGlue());
				box.add(this.endTimeSpinner);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				add(box, gbc);
			}

			JSeparator jsep2 = new JSeparator();
			gbc.insets = UIStorage.INSETS1010;
			gbc.gridy++;
			jsep2.setBorder(BorderFactory.createEtchedBorder());
			add(jsep2, gbc);
			gbc.insets = gbcInsetsDefault;

			this.continuosRadioButton = new JRadioButton(LangModelSchedule.getString("Continual"));
			this.continuosRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						TimeParametersPanel.this.endTimeSpinner.setEnabled(true);
						TimeParametersPanel.this.endDateSpinner.setEnabled(true);
						endDateButton.setEnabled(true);

						// extraParamPanel.setVisible(false);
						TimeParametersPanel.this.revalidate();
					}
				}
			});

			this.periodicalRadioButton = new JRadioButton(LangModelSchedule.getString("Periodical"));

			final TimeStampUI demo = new TimeStampUI();

			this.timeStamps = new ObjList(TemporalPatternController.getInstance(), TemporalPatternController.KEY_NAME);

			this.timeStamps.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						final JList jlist = (JList) e.getSource();
						TemporalPattern temporalPattern = (TemporalPattern) jlist.getSelectedValue();
						if (temporalPattern.isChanged()) {
							// JFrame mainFrame = demo.getTimeLineEditor();
							TimeLine timeLine = (TimeLine) temporalPattern.getTimeLines().iterator().next();
							final String template = timeLine.getTemplate();
							JSplitPane pane = demo.getPane();
							demo.setTimeLine(timeLine);
							int result = JOptionPane.showConfirmDialog(jlist, pane, "Time Line",
								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								temporalPattern.removeTemplate(template);
								String template2 = demo.getTemplate();
								temporalPattern.addTemplate(template2);
								DefaultListModel model = (DefaultListModel) jlist.getModel();
								model.removeAllElements();
								for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
									TemporalPattern pattern = (TemporalPattern) it.next();
									model.addElement(pattern);
								}

							}
						}
					}

				}

			});

			final JScrollPane timeStampPane = new JScrollPane(this.timeStamps);
			timeStampPane.setBorder(BorderFactory.createEtchedBorder());

			final JButton addTemporalPatternButton = new JButton(UIStorage.PLUS_ICON);
			addTemporalPatternButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JSplitPane pane = demo.getPane();
					int result = JOptionPane.showConfirmDialog(addTemporalPatternButton, pane, "Time Line",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						// System.out.println("was:" + template);
						// timeStamp.removeTemplate(template);
						String template = demo.getTemplate();
						System.out.println("now:" + template);
						try {
							RISDSessionInfo sessionInterface = (RISDSessionInfo) TimeParametersPanel.this.aContext
									.getSessionInterface();
							TemporalPattern temporalPattern = TemporalPattern.createInstance(sessionInterface
									.getUserIdentifier(), null, new LinkedList());
							temporalPattern.addTemplate(template);
							TimeParametersPanel.this.temporalPatterns.add(temporalPattern);

							// timeStamp.addTemplate(template2);
							DefaultListModel model = (DefaultListModel) TimeParametersPanel.this.timeStamps.getModel();
							model.removeAllElements();
							for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
								TemporalPattern timeLine2 = (TemporalPattern) it.next();
								model.addElement(timeLine2);
							}

						} catch (CreateObjectException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					}

				}

			});

			addTemporalPatternButton.setMargin(UIStorage.INSET_NULL);

			final JButton removeTemporalPatternButton = new JButton(UIStorage.MINUS_ICON);
			removeTemporalPatternButton.setEnabled(false);
			removeTemporalPatternButton.setMargin(UIStorage.INSET_NULL);

			removeTemporalPatternButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					TemporalPattern temporalPattern = (TemporalPattern) TimeParametersPanel.this.timeStamps
							.getSelectedValue();
					if (temporalPattern != null) {
						TimeParametersPanel.this.temporalPatterns.remove(temporalPattern);
						DefaultListModel model = (DefaultListModel) TimeParametersPanel.this.timeStamps.getModel();
						model.removeAllElements();
						for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
							TemporalPattern timeLine2 = (TemporalPattern) it.next();
							model.addElement(timeLine2);
						}
					}
				}
			});

			this.timeStamps.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					JList jlist = (JList) e.getSource();
					TemporalPattern temporalPattern = (TemporalPattern) jlist.getSelectedValue();
					removeTemporalPatternButton.setEnabled((temporalPattern == null) ? false : temporalPattern
							.isChanged());
				}
			});

			this.oneRadioButton = UIStorage.createRadioButton(LangModelSchedule.getString("Onetime"),
				new AbstractAction() {

					public void actionPerformed(ActionEvent e) {
						TimeParametersPanel.this.timeStamps.setEnabled(false);
						addTemporalPatternButton.setEnabled(false);
						removeTemporalPatternButton.setEnabled(false);
						TimeParametersPanel.this.endTimeSpinner.setEnabled(false);
						TimeParametersPanel.this.endDateSpinner.setEnabled(false);
						endDateButton.setEnabled(false);
						revalidate();
					}
				});

			this.periodicalRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						TimeParametersPanel.this.timeStamps.setEnabled(true);
						addTemporalPatternButton.setEnabled(true);
						TimeParametersPanel.this.endTimeSpinner.setEnabled(true);
						TimeParametersPanel.this.endDateSpinner.setEnabled(true);
						endDateButton.setEnabled(true);

						revalidate();
					}
				}
			});

			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridy++;
			add(this.oneRadioButton, gbc);
			gbc.gridy++;
			add(this.periodicalRadioButton, gbc);

			gbc.insets = gbcInsetsDefault;
			{
				Box box = new Box(BoxLayout.X_AXIS);
				box.add(addTemporalPatternButton);
				box.add(Box.createHorizontalGlue());
				box.add(removeTemporalPatternButton);
				gbc.gridy++;
				gbc.weighty = 1.0;
				add(timeStampPane, gbc);
				gbc.weighty = 0.0;
				gbc.gridy++;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				add(box, gbc);
			}

			ButtonGroup group = new ButtonGroup();
			group.add(this.oneRadioButton);
			group.add(this.continuosRadioButton);
			group.add(this.periodicalRadioButton);

			JSeparator jsep3 = new JSeparator();
			gbc.insets = UIStorage.INSETS1010;
			jsep3.setBorder(BorderFactory.createEtchedBorder());
			gbc.gridy++;
			add(jsep3, gbc);
			gbc.insets = gbcInsetsDefault;

			this.synchroRadioButton = new JRadioButton(LangModelSchedule.getString("Together"));
			this.alternateRadioButton = new JRadioButton(LangModelSchedule.getString("InTurn"));
			this.synchroRadioButton.setEnabled(false);
			this.alternateRadioButton.setEnabled(false);
			ButtonGroup group3 = new ButtonGroup();
			group3.add(this.synchroRadioButton);
			group3.add(this.alternateRadioButton);
			/**
			 * @TODO when ok
			 */
			if (false) {
				gbc.gridy++;
				add(this.synchroRadioButton, gbc);
				gbc.gridy++;
				add(this.alternateRadioButton, gbc);

				JSeparator jsep4 = new JSeparator();
				gbc.insets = UIStorage.INSETS1010;
				gbc.gridheight = GridBagConstraints.RELATIVE;
				jsep4.setBorder(BorderFactory.createEtchedBorder());
				gbc.gridy++;
				add(jsep4, gbc);
				gbc.insets = gbcInsetsDefault;
			} else {
				gbc.gridy++;
				gbc.gridheight = GridBagConstraints.RELATIVE;
				add(new JLabel(), gbc);
			}

			{
				this.applyButton = new JButton(LangModelSchedule.getString("Apply"));
				this.createButton = new JButton(LangModelSchedule.getString("Create"));

				this.applyButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						TimeParametersPanel.this.createButton.setEnabled(false);
						TimeParametersPanel.this.applyButton.setEnabled(false);
						try {
							TimeParametersPanel.this.schedulerModel.applyTest();
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
						}
						TimeParametersPanel.this.createButton.setEnabled(true);
						TimeParametersPanel.this.applyButton.setEnabled(true);

					}
				});

				this.createButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						TimeParametersPanel.this.createButton.setEnabled(false);
						TimeParametersPanel.this.applyButton.setEnabled(false);
						try {
							TimeParametersPanel.this.schedulerModel.createTest();
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
						}
						TimeParametersPanel.this.createButton.setEnabled(true);
						TimeParametersPanel.this.applyButton.setEnabled(true);
					}
				});

				Box box = new Box(BoxLayout.X_AXIS);
				this.createButton.setDefaultCapable(false);
				box.add(this.createButton);
				box.add(Box.createGlue());
				this.applyButton.setDefaultCapable(false);
				box.add(this.applyButton);
				gbc.anchor = GridBagConstraints.SOUTH;
				gbc.gridy++;
				gbc.gridx = 1;
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				add(box, gbc);
			}

			this.synchroRadioButton.doClick();
			this.periodicalRadioButton.doClick();
			this.oneRadioButton.doClick();
			
		}
		

		
		public TestTemporalStamps getTestTemporalStamps() {
			TestTemporalType temporalType = null;
			TemporalPattern temporalPattern = null;
			if (this.oneRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			} else if (this.periodicalRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
				temporalPattern = (TemporalPattern) TimeParametersPanel.this.timeStamps.getSelectedValue();
				if (temporalPattern == null) {
					JOptionPane.showMessageDialog(this,
						LangModelSchedule.getString("Have not choosen temporal pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.OK_OPTION);
					this.schedulerModel.setBreakData();
					return null;
				}
			} else if (this.continuosRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS;
			} else {
				// SchedulerModel.showErrorMessage(this, ne)
				return null;
			}

			Calendar dateCal = Calendar.getInstance();
			Calendar timeCal = Calendar.getInstance();

			dateCal.setTime((Date) this.startDateSpinner.getValue());
			timeCal.setTime((Date) this.startTimeSpinner.getValue());
			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			long start = dateCal.getTimeInMillis();

			dateCal.setTime((Date) this.endDateSpinner.getValue());
			timeCal.setTime((Date) this.endTimeSpinner.getValue());
			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			long end = dateCal.getTimeInMillis();
			
			if (!temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME) && end < start) {
				JOptionPane.showMessageDialog(this,
					LangModelSchedule.getString("End time less than begin time"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.OK_OPTION);
				this.schedulerModel.setBreakData();
				return null;
				
			}

			return new TestTemporalStamps(temporalType, new Date(start), new Date(end),
																	temporalPattern);
			
		}
		
		public void setTestTemporalStamps(TestTemporalStamps testTemporalStamps) {		

			Date startTime = testTemporalStamps.getStartTime();
			this.startDateSpinner.getModel().setValue(startTime);
			this.startTimeSpinner.getModel().setValue(startTime);
			Date endTime = testTemporalStamps.getEndTime();
			if (endTime != null) {
				this.endDateSpinner.getModel().setValue(endTime);
				this.endTimeSpinner.getModel().setValue(endTime);
			}

			switch (testTemporalStamps.getTestTemporalType().value()) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					this.oneRadioButton.doClick();
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					this.periodicalRadioButton.doClick();
					this.timeStamps.setSelectedValue(testTemporalStamps.getTemporalPattern(), true);
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.continuosRadioButton.doClick();
					break;

			}		
		}
		
		public void setTemporalPatterns(Collection temporalPatterns) {
			this.temporalPatterns = temporalPatterns;
			ObjListModel model = (ObjListModel) this.timeStamps.getModel();
			for (Iterator it = this.temporalPatterns.iterator(); it.hasNext();) {
				TemporalPattern pattern = (TemporalPattern) it.next();
				model.addElement(pattern);
			}
			
		}		

		void showEndCalendar() {
			Calendar cal = Calendar.getInstance();
			Date date = (Date) this.endDateSpinner.getModel().getValue();
			cal.setTime(date);

			JDialog calendarDialog = CalendarUI.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
			calendarDialog.setSize(new Dimension(200, 200));
			calendarDialog.setResizable(false);
			calendarDialog.setLocation(new Point(this.endDateSpinner.getLocationOnScreen().x - 35, this.endDateSpinner
					.getLocationOnScreen().y + 22));
			calendarDialog.setVisible(true);
			if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				this.endDateSpinner.getModel().setValue(cal.getTime());
		}

		void showStartCalendar() {
			Calendar cal = Calendar.getInstance();
			Date date = (Date) this.startDateSpinner.getModel().getValue();
			cal.setTime(date);

			JDialog calendarDialog = CalendarUI.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
			calendarDialog.setSize(new Dimension(200, 200));
			calendarDialog.setResizable(false);
			calendarDialog.setLocation(new Point(this.startDateSpinner.getLocationOnScreen().x - 35,
													this.startDateSpinner.getLocationOnScreen().y + 22));
			calendarDialog.setVisible(true);
			if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				this.startDateSpinner.getModel().setValue(cal.getTime());
		}

	}

	private TimeParametersPanel	panel;

	private Command				command;

	public TimeParametersFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon(UIStorage.GENERAL_ICON);
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.panel = new TimeParametersPanel(aContext);
		this.getContentPane().add(this.panel, BorderLayout.CENTER);
		this.command = new WindowCommand(this);
	}
	

	/**
	 * @return Returns the command.
	 */
	public Command getCommand() {
		return this.command;
	}
}