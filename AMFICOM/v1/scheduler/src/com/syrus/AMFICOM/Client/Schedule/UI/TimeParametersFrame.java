
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.CalendarUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternWrapper;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.newFilter.DateSpinner;
import com.syrus.AMFICOM.newFilter.TimeSpinner;
import com.syrus.util.Log;

public class TimeParametersFrame extends JInternalFrame {

	private static final long	serialVersionUID	= 6562288896016470275L;

	public class TimeParametersPanel implements	PropertyChangeListener {

		private static final long	serialVersionUID	= -7975294015403739057L;

		SchedulerModel schedulerModel;
		ApplicationContext			aContext;
		Dispatcher dispatcher;

		TimeSpinner			startTimeSpinner;
		DateSpinner			startDateSpinner;

		private JLabel interavalLabel;
//		private JLabel monthIntervalLabel;
		private JLabel dayIntervalLabel;
		
//		private JSpinner					periodMonthSpinner;
		private JSpinner					periodDaySpinner;
		private JSpinner					periodTimeSpinner;
		
		JButton startTimeButton;
		JButton pediodTimeButton;
		private JButton endTimeButton;
		
		JButton choosedButton;		
		
		private JLabel endingLabel;
		private JButton endDateButton;
		
		private TimeSpinner					endTimeSpinner;		
		private DateSpinner					endDateSpinner;
		
//		ObjList						timeStamps;

		JRadioButton				oneRadioButton;

		private JRadioButton		continuosRadioButton;

		private JRadioButton		periodicalRadioButton;
		
		JRadioButton		groupRadioButton;

		private JRadioButton		synchroRadioButton;

		private JRadioButton		alternateRadioButton;

		Collection					temporalPatterns;

		
		
		TestTemporalStamps	temporalStamps;
		
		JPanel panel;
		
		public static final long MINUTE_LONG = 60L * 1000L;
		public static final long HOUR_LONG = 60L * MINUTE_LONG;
		public static final long DAY_LONG = 24L * HOUR_LONG;

		PropertyChangeEvent	propertyChangeEvent;
		
		

		public TimeParametersPanel() {
			this.createGUI();
		}

		public TimeParametersPanel(ApplicationContext aContext) {
			this();
			this.aContext = aContext;
			this.dispatcher = this.aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_DATE_OPERATION, this);
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.dispatcher = aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_TEMPORAL_STAMPS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_TEMPORAL_STAMPS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_START_GROUP_TIME, this);
		}

		private void createGUI() {
			this.panel = new JPanel(new GridBagLayout());
			final GridBagConstraints gbc = new GridBagConstraints();
			this.panel.setBorder(BorderFactory.createEtchedBorder());			
			gbc.anchor = GridBagConstraints.NORTH;
			gbc.weightx = 1.0;
			gbc.weighty = 0.0;
			gbc.ipadx = 0;
			gbc.ipady = 0;
			gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
			Insets gbcInsetsDefault = gbc.insets;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			this.createTemporalTypeButtons();
			
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridy++;
			this.panel.add(this.oneRadioButton, gbc);
			gbc.gridy++;
			this.panel.add(this.periodicalRadioButton, gbc);
			gbc.gridy++;
			this.panel.add(this.continuosRadioButton, gbc);
			gbc.gridy++;
			this.panel.add(this.groupRadioButton, gbc);

			JLabel beginLabel = new JLabel(LangModelSchedule.getString("Start") + ':');
			gbc.gridx = 1;
			gbc.gridy++;			
			this.panel.add(beginLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			gbc.gridy++;

			{
				this.startTimeSpinner = new TimeSpinner();
				this.startDateSpinner = new DateSpinner();
				ChangeListener changeListener = new ChangeListener() {
					
					//OperationEvent event;
					boolean waiting = false;
					long previousEventTime;		
					boolean startedThread = false;
					static final long TIMEOUT = 500;
					
					private Thread		thread			= new Thread() {

															public void run() {
																while (true) {
																	if (waiting 
																			&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {

																		Date startDate = TimeParametersPanel.this
																				.getStartDate();
																		Date endDate = TimeParametersPanel.this
																				.getEndDate();

																		if (waiting) {
																			Set selectedTestIds = TimeParametersPanel.this.schedulerModel
																					.getSelectedTestIds();
																			if (selectedTestIds != null
																					&& !selectedTestIds.isEmpty()) {
																				int size = selectedTestIds.size();
																				boolean b = !oneRadioButton
																						.isSelected()
																						&& !groupRadioButton
																								.isSelected();
																				if (size == 1 || !b) {
																					if (b) {
																						if (startDate.getTime() > endDate
																								.getTime()) {
																							waiting = false;

																						} else {
																							Test selectedTest = TimeParametersPanel.this.schedulerModel
																									.getSelectedTest();
																							if (selectedTest
																									.isChanged()
																									&& TimeParametersPanel.this.schedulerModel
																											.isValid(
																												startDate,
																												selectedTest
																														.getEndTime(),
																												selectedTest
																														.getMonitoredElement()
																														.getId())) {
																								selectedTest
																										.setStartTime(startDate);

																								if (selectedTest
																										.getGroupTestId() == null) {
																									TimeParametersPanel.this.dispatcher
																											.firePropertyChange(new PropertyChangeEvent(
																																						this,
																																						SchedulerModel.COMMAND_REFRESH_TESTS,
																																						null,
																																						null));
																								} else {
																									TimeParametersPanel.this.dispatcher
																											.firePropertyChange(new PropertyChangeEvent(
																																						this,
																																						SchedulerModel.COMMAND_SET_START_GROUP_TIME,
																																						null,
																																						selectedTest
																																								.getStartTime()));
																								}
																							}
																						}
																					} else {
																						TimeParametersPanel.this.schedulerModel
																								.moveSelectedTests(startDate);
																					}
																				} else {
																					TimeParametersPanel.this.schedulerModel
																							.moveSelectedTests(startDate);
																				}
																			}

																		}
																		waiting = false;
																	}

																	try {
																		Thread.sleep(TIMEOUT);
																	} catch (InterruptedException e) {
																		// nothing
																	}
																}
															}

														};
					

					public void stateChanged(ChangeEvent e) {
						if (!this.waiting) {
							if (!this.startedThread) {
								this.thread.start();
								this.startedThread = true;
							}
							this.waiting = TimeParametersPanel.this.propertyChangeEvent == null && TimeParametersPanel.this.isTestAgree(TimeParametersPanel.this.schedulerModel
								.getSelectedTest());
						}
						this.previousEventTime = System.currentTimeMillis();						
					}
				};

				this.startTimeSpinner.addChangeListener(changeListener);
				this.startDateSpinner.addChangeListener(changeListener);
			}
			
			final JButton startDateButton = new JButton(UIStorage.CALENDAR_ICON);
			startDateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
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
				CommonUIUtilities.fixHorizontalSize(this.startDateSpinner);
				box.add(this.startDateSpinner);
				box.add(startDateButton);
				box.add(Box.createHorizontalGlue());
				CommonUIUtilities.fixHorizontalSize(this.startTimeSpinner);
				box.add(this.startTimeSpinner);
				box.add(this.startTimeButton);
				gbc.gridwidth = GridBagConstraints.REMAINDER;				
				this.panel.add(box, gbc);
			}

			{
				gbc.gridx = 1;
				gbc.gridy++;
				this.interavalLabel = new JLabel(LangModelSchedule.getString("Interval") + ':');
				this.panel.add(this.interavalLabel, gbc);
				gbc.weightx = 1.0;
				gbc.gridx = 1;
				gbc.gridy++;
				this.periodTimeSpinner = new TimeSpinner();
				this.periodDaySpinner = new JSpinner();
//				this.periodMonthSpinner = new JSpinner();
				{
					Box box = new Box(BoxLayout.X_AXIS);
					box.add(Box.createHorizontalGlue());
					CommonUIUtilities.fixHorizontalSize(this.periodTimeSpinner);
//					box.add(this.periodMonthSpinner);
//					this.monthIntervalLabel = new JLabel(LangModelSchedule.getString("month"));
//					box.add(this.monthIntervalLabel);
					box.add(this.periodDaySpinner);
					this.dayIntervalLabel = new JLabel(LangModelSchedule.getString("day"));
					box.add(this.dayIntervalLabel);
					box.add(this.periodTimeSpinner);
					box.add(this.pediodTimeButton);
					Calendar calendar = Calendar.getInstance();
					/* TODO debug mode */
//					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.HOUR_OF_DAY, 1);
					calendar.set(Calendar.MINUTE, 0);
					this.panel.add(box, gbc);
					((SpinnerDateModel)this.periodTimeSpinner.getModel()).setValue(calendar.getTime());
					
					ChangeListener changeListener = new ChangeListener() {
						
						//OperationEvent event;
						boolean waiting = false;
						long previousEventTime;		
						boolean startedThread = false;
						static final long TIMEOUT = 500;
						
						private Thread		thread			= new Thread() {

																public void run() {
																	while (true) {
																		if (waiting
																				&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {
																			if (waiting) {
																				Test selectedTest = TimeParametersPanel.this.schedulerModel
																						.getSelectedTest();
																				if (selectedTest != null
																						&& selectedTest.isChanged()) {
																					Identifier temporalPatternId = selectedTest
																							.getTemporalPatternId();
																					if (temporalPatternId != null
																							&& temporalPatternId
																									.getMajor() == ObjectEntities.PERIODICALTEMPORALPATTERN_CODE) {
																						try {
																							PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) StorableObjectPool
																									.getStorableObject(
																										temporalPatternId,
																										true);
																							long intervalLength = TimeParametersPanel.this.getIntervalLength();
																							if (periodicalTemporalPattern.isChanged()) {
																								periodicalTemporalPattern.setPeriod(intervalLength);
																							} else {
																								periodicalTemporalPattern = PeriodicalTemporalPattern.createInstance(LoginManager.getUserId(), intervalLength);
																								StorableObjectPool.putStorableObject(periodicalTemporalPattern);
																								selectedTest.setTemporalPatternId(periodicalTemporalPattern.getId());
																							}
																						} catch (ApplicationException e) {
																							// TODO Auto-generated catch block
																							e.printStackTrace();
																						}
																					}
																				}

																			}
																			waiting = false;
																		}

																		try {
																			Thread.sleep(TIMEOUT);
																		} catch (InterruptedException e) {
																			// nothing
																		}
																	}
																}

															};
						

						public void stateChanged(ChangeEvent e) {
							
							if (!this.waiting) {
//								this.event = ;
								if (!this.startedThread) {
									this.thread.start();
									this.startedThread = true;
								}
								this.waiting = TimeParametersPanel.this.propertyChangeEvent == null &&  TimeParametersPanel.this.isTestAgree(TimeParametersPanel.this.schedulerModel
									.getSelectedTest());
							}
							this.previousEventTime = System.currentTimeMillis();						
						}
					};
					
					
					this.periodDaySpinner.addChangeListener(changeListener);
					this.periodTimeSpinner.addChangeListener(changeListener);
				}
			}
			
			gbc.gridx = 1;
			gbc.gridy++;
			this.endingLabel = new JLabel(LangModelSchedule.getString("Finish") + ':');
			this.panel.add(this.endingLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridy++;
			
			this.endTimeSpinner = new TimeSpinner();
			this.endDateSpinner = new DateSpinner();
			{
			/* TODO debug */
			SpinnerDateModel model = (SpinnerDateModel) this.endTimeSpinner.getModel();
			model.setValue(new Date(((Date)model.getValue()).getTime() + 5L * 60L * 60L * 1000L));
			}
			
			this.endDateButton = new JButton(UIStorage.CALENDAR_ICON);
			this.endDateButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			this.endDateButton.setDefaultCapable(false);

			this.endDateButton.setFocusable(false);
			this.endDateButton.setToolTipText(LangModelSchedule.getString("Calendar"));
			this.endDateButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					showEndCalendar();
				}
			});
			{
				Box box = new Box(BoxLayout.X_AXIS);
				CommonUIUtilities.fixHorizontalSize(this.endDateSpinner);
				box.add(this.endDateSpinner);
				box.add(this.endDateButton);
				box.add(Box.createHorizontalGlue());
				CommonUIUtilities.fixHorizontalSize(this.endTimeSpinner);
				box.add(this.endTimeSpinner);
				box.add(this.endTimeButton);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				this.panel.add(box, gbc);
			}
			
			{
				ChangeListener changeListener = new ChangeListener() {
					
					//OperationEvent event;
					boolean waiting = false;
					long previousEventTime;		
					boolean startedThread = false;
					static final long TIMEOUT = 500;
					
					private Thread		thread			= new Thread() {

															public void run() {
																while (true) {
																	if (waiting
																			&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {
//																		Log.debugMessage(".run | 1 ", Log.FINEST);
																		Test selectedTest = TimeParametersPanel.this.schedulerModel
																				.getSelectedTest();
																		if (selectedTest != null
																				&& selectedTest.isChanged()) {
//																			Log.debugMessage(".run | 2 ", Log.FINEST);
																			Date startDate = TimeParametersPanel.this
																					.getStartDate();
																			Date endDate = TimeParametersPanel.this
																					.getEndDate();
																			if (startDate.getTime() < endDate.getTime()) {
//																				Log.debugMessage(".run | 3 ", Log.FINEST);
																				if (TimeParametersPanel.this.schedulerModel.isValid(startDate, endDate, selectedTest.getMonitoredElement().getId())){
//																					Log.debugMessage(".run | 4 ", Log.FINEST);
																					selectedTest.setEndTime(endDate);
																				} else {
																					JOptionPane
																							.showMessageDialog(
																								Environment
																										.getActiveWindow(),
																								LangModelSchedule
																										.getString("Cannot change end test"),
																								LangModelSchedule
																										.getString("Error"),
																								JOptionPane.OK_OPTION);

																				}
																			} else {
//																				Log.debugMessage(".run | 5 ", Log.FINEST);
																				waiting = false;
																			}

																			if (waiting) {
//																				Log.debugMessage(".run | 6 ", Log.FINEST);
																				TimeParametersPanel.this.dispatcher
																						.firePropertyChange(new PropertyChangeEvent(
																														TimeParametersPanel.this,

																														SchedulerModel.COMMAND_REFRESH_TESTS,
																														null,
																														null));
																			}
																		}
//																		Log.debugMessage(".run | 7 ", Log.FINEST);
																		waiting = false;
																	}

																	try {
																		Thread.sleep(TIMEOUT);
																	} catch (InterruptedException e) {
																		// nothing
																	}
																}
															}

														};
					

					public void stateChanged(ChangeEvent e) {
						if (!this.waiting) {
//							this.event = ;
							if (!this.startedThread) {
								this.thread.start();
								this.startedThread = true;
							}
							this.waiting = TimeParametersPanel.this.propertyChangeEvent == null && TimeParametersPanel.this.isTestAgree(TimeParametersPanel.this.schedulerModel
								.getSelectedTest());
						}
						this.previousEventTime = System.currentTimeMillis();						
					}
				};

				this.endTimeSpinner.addChangeListener(changeListener);
				this.endDateSpinner.addChangeListener(changeListener);
			}

//			JSeparator jsep2 = new JSeparator();
//			gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//			gbc.gridy++;
//			jsep2.setBorder(BorderFactory.createEtchedBorder());
//			add(jsep2, gbc);
//			gbc.insets = gbcInsetsDefault;

//			final TimeStampUI demo = new TimeStampUI();

//			this.timeStamps = new ObjList(TemporalPatternController.getInstance(), TemporalPatternController.KEY_NAME);
//
//			this.timeStamps.addMouseListener(new MouseAdapter() {
//
//				public void mouseClicked(MouseEvent e) {
//					if (e.getClickCount() == 2) {
//						final JList jlist = (JList) e.getSource();
//						TemporalPattern temporalPattern = (TemporalPattern) jlist.getSelectedValue();
//						if (temporalPattern.isChanged()) {
//							// JFrame mainFrame = demo.getTimeLineEditor();
//							TimeLine timeLine = (TimeLine) temporalPattern.getTimeLines().iterator().next();
//							final String template = timeLine.getTemplate();
//							JSplitPane pane = demo.getPane();
//							demo.setTimeLine(timeLine);
//							int result = JOptionPane.showConfirmDialog(jlist, pane, "Time Line",
//								JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//							if (result == JOptionPane.OK_OPTION) {
//								temporalPattern.removeTemplate(template);
//								String template2 = demo.getTemplate();
//								temporalPattern.addTemplate(template2);
//								DefaultListModel model = (DefaultListModel) jlist.getModel();
//								model.removeAllElements();
//								for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
//									TemporalPattern pattern = (TemporalPattern) it.next();
//									model.addElement(pattern);
//								}
//
//							}
//						}
//					}
//
//				}
//
//			});

//			final JScrollPane timeStampPane = new JScrollPane(this.timeStamps);
//			timeStampPane.setBorder(BorderFactory.createEtchedBorder());
//
//			final JButton addTemporalPatternButton = new JButton(UIStorage.PLUS_ICON);
//			addTemporalPatternButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					JSplitPane pane = demo.getPane();
//					int result = JOptionPane.showConfirmDialog(addTemporalPatternButton, pane, "Time Line",
//						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
//					if (result == JOptionPane.OK_OPTION) {
//						// System.out.println("was:" + template);
//						// timeStamp.removeTemplate(template);
//						String template = demo.getTemplate();
//						System.out.println("now:" + template);
//						try {
//							RISDSessionInfo sessionInterface = (RISDSessionInfo) TimeParametersPanel.this.aContext
//									.getSessionInterface();
//							TemporalPattern temporalPattern = TemporalPattern.createInstance(sessionInterface
//									.getUserIdentifier(), template, new HashSet());
//							temporalPattern.addTemplate(template);
//							TimeParametersPanel.this.temporalPatterns.add(temporalPattern);
//							MeasurementStorableObjectPool.putStorableObject(temporalPattern);
//							// timeStamp.addTemplate(template2);
//							ObjListModel model = (ObjListModel) TimeParametersPanel.this.timeStamps.getModel();
//							model.removeAllElements();
//							for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
//								TemporalPattern timeLine2 = (TemporalPattern) it.next();
//								model.addElement(timeLine2);
//							}
//
//						} catch (CreateObjectException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						} catch (IllegalObjectEntityException ioee) {
//							ModuleMainFrame.showErrorMessage(TimeParametersPanel.this, ioee);
//						}
//
//					}
//
//				}
//
//			});
//
//			addTemporalPatternButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
//
//			final JButton removeTemporalPatternButton = new JButton(UIStorage.MINUS_ICON);
//			removeTemporalPatternButton.setEnabled(false);
//			removeTemporalPatternButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
//
//			removeTemporalPatternButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					TemporalPattern temporalPattern = (TemporalPattern) TimeParametersPanel.this.timeStamps
//							.getSelectedValue();
//					if (temporalPattern != null) {
//						TimeParametersPanel.this.temporalPatterns.remove(temporalPattern);
//						DefaultListModel model = (DefaultListModel) TimeParametersPanel.this.timeStamps.getModel();
//						model.removeAllElements();
//						for (Iterator it = TimeParametersPanel.this.temporalPatterns.iterator(); it.hasNext();) {
//							TemporalPattern timeLine2 = (TemporalPattern) it.next();
//							model.addElement(timeLine2);
//						}
//					}
//				}
//			});
//
//			this.timeStamps.addListSelectionListener(new ListSelectionListener() {
//
//				public void valueChanged(ListSelectionEvent e) {
//					JList jlist = (JList) e.getSource();
//					TemporalPattern temporalPattern = (TemporalPattern) jlist.getSelectedValue();
//					removeTemporalPatternButton.setEnabled((temporalPattern == null) ? false : temporalPattern
//							.isChanged());
//				}
//			});

			
			gbc.insets = gbcInsetsDefault;
//			{
//				Box box = new Box(BoxLayout.X_AXIS);
//				box.add(addTemporalPatternButton);
//				box.add(Box.createHorizontalGlue());
//				box.add(removeTemporalPatternButton);
//				gbc.gridy++;
//				gbc.weighty = 1.0;
//				add(timeStampPane, gbc);
//				gbc.weighty = 0.0;
//				gbc.gridy++;
//				gbc.gridwidth = GridBagConstraints.REMAINDER;
//				add(box, gbc);
//			}

			ButtonGroup group = new ButtonGroup();
			group.add(this.oneRadioButton);
			group.add(this.periodicalRadioButton);
			group.add(this.continuosRadioButton);
			group.add(this.groupRadioButton);


//			JSeparator jsep3 = new JSeparator();
//			gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
//			jsep3.setBorder(BorderFactory.createEtchedBorder());
//			gbc.gridy++;
//			add(jsep3, gbc);
//			gbc.insets = gbcInsetsDefault;

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
				this.panel.add(this.synchroRadioButton, gbc);
				gbc.gridy++;
				this.panel.add(this.alternateRadioButton, gbc);

				JSeparator jsep4 = new JSeparator();
				gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
				gbc.gridheight = GridBagConstraints.RELATIVE;
				jsep4.setBorder(BorderFactory.createEtchedBorder());
				gbc.gridy++;
				this.panel.add(jsep4, gbc);
				gbc.insets = gbcInsetsDefault;
			} else {
//				gbc.gridy++;
//				gbc.gridheight = GridBagConstraints.RELATIVE;
//				add(new JLabel(), gbc);
			}
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 1.0;
			this.panel.add(Box.createVerticalGlue(), gbc);
			
			this.synchroRadioButton.doClick();
			this.periodicalRadioButton.doClick();
			this.oneRadioButton.doClick();
			
		}
		
		
		private void createTemporalTypeButtons() {
			this.oneRadioButton = new JRadioButton(LangModelSchedule.getString("Onetime"));
			this.continuosRadioButton = new JRadioButton(LangModelSchedule.getString("Continual"));		
			this.periodicalRadioButton = new JRadioButton(LangModelSchedule.getString("Periodical"));			
			this.groupRadioButton = new JRadioButton(LangModelSchedule.getString("Sectional"));


			this.oneRadioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
//						setEndDateEnable(false);
//						setPeriodEnabled(false);
						setOneDateEnable(true);
					}
				});

			
			this.periodicalRadioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					//						setEndDateEnable(true);
						setPeriodEnabled(true);
					}				
			});
			
			this.continuosRadioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
//						setPeriodEnabled(false);
						setContinousEnable(true);			
						
					
				}
			});
			
			this.groupRadioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
//						setEndDateEnable(true);						
						setGroupEnabled(true);
					}				
			});
			
			this.startTimeButton = new JButton(">>");
			this.startTimeButton.setToolTipText(LangModelSchedule.getString("Add time item"));
			this.startTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
			this.endTimeButton = new JButton(">>");
			this.endTimeButton.setToolTipText(LangModelSchedule.getString("Add time item"));
			this.endTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
//			this.startTimeButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
//					Date startDate = getStartDate();
//
//					long startTime = startDate.getTime();
//
////				 if (TimeParametersPanel.this.timeTableRadioButton.isSelected()) {
////					
////						if (TimeParametersPanel.this.temporalStamps == null) {
////							try {
////								IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern.createInstance(LoginManager.getUserId(), null, null);
////								MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
////								TimeParametersPanel.this.temporalStamps = new TestTemporalStamps(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL, getStartDate(), getEndDate(), intervalsTemporalPattern);
////							} catch (IllegalObjectEntityException e1) {
////								ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////							} catch (CreateObjectException e1) {
////								ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////							}
////							
////						} 
////						
////						IntervalsTemporalPattern temporalPattern = (IntervalsTemporalPattern) TimeParametersPanel.this.temporalStamps.getTemporalPattern();
////						
////												
////						long ms = startTime - TimeParametersPanel.this.temporalStamps.getStartTime().getTime();
////						Log.debugMessage(".actionPerformed | ms:" + ms, Log.FINEST);
////
////						if (ms < 0) {
////							TimeParametersPanel.this.temporalStamps.setStartTime(startDate);
////							try {
////								temporalPattern.moveAllItems(-ms);
////							} catch (IllegalDataException e1) {
////								// never occur !!!
////								assert false;
////							}
////							ms = 0;
////						}
////						
////						if (startDate.compareTo(TimeParametersPanel.this.temporalStamps.getEndTime()) > 0) {
////							TimeParametersPanel.this.temporalStamps.setEndTime(startDate);
////						}
////
////						Long newMs = new Long(ms);
////						try {
////							temporalPattern.addIntervalItems(Collections.singletonMap(newMs, Identifier.VOID_IDENTIFIER), Collections.singletonMap(newMs, null));
////						} catch (IllegalDataException e1) {
////							ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////						}
////						/* TODO */
//////						 TimeParametersPanel.this.schedulerModel.refreshTestTemporalStamps();
////						
////					} else {
//						
//						TimeParametersPanel.this.schedulerModel.createTest();
////					}
//				}
//			});

			
			this.pediodTimeButton = new JButton(">>");
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Add periodical sequence"));
			this.pediodTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
//			this.pediodTimeButton.addActionListener(new ActionListener() {
//
//				public void actionPerformed(ActionEvent e) {
////					if (TimeParametersPanel.this.timeTableRadioButton.isSelected()) {
////						Date startDate = getStartDate();
////						Date endDate = getEndDate();
////
////						long startTime = startDate.getTime();
////						long endTime = endDate.getTime();
////
////						if (startTime >= endTime) {
////							JOptionPane.showMessageDialog(TimeParametersPanel.this.panel, LangModelSchedule
////									.getString("End time less than begin time"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
////								JOptionPane.OK_OPTION);
////							return;
////						}
////
////						Identifier userIdentifier = LoginManager.getUserId();
////
////						if (TimeParametersPanel.this.temporalStamps == null) {
////
////							try {
////								IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern
////										.createInstance(userIdentifier, null, null);
////
////								MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
////
////								TimeParametersPanel.this.temporalStamps = new TestTemporalStamps(
////																									TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
////																									startDate, endDate,
////																									intervalsTemporalPattern);
////							} catch (IllegalObjectEntityException e1) {
////								ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////							} catch (CreateObjectException e1) {
////								ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////							}
////
////						}
////
////						long intervalLength = getIntervalLength();
////
////						IntervalsTemporalPattern temporalPattern = (IntervalsTemporalPattern) TimeParametersPanel.this.temporalStamps
////								.getTemporalPattern();
////
////						try {
////							PeriodicalTemporalPattern periodicTemporalPattern = null;
////							TypicalCondition typicalCondition = new TypicalCondition(
////																						intervalLength,
////																						intervalLength,
////																						OperationSort.OPERATION_EQUALS,
////																						ObjectEntities.PERIODICALTEMPORALPATTERN_CODE,
////																						PeriodicalTemporalPatternWrapper.COLUMN_PERIOD);
////							try {
////								java.util.Set set = MeasurementStorableObjectPool.getStorableObjectsByCondition(
////									typicalCondition, true);
////								if (!set.isEmpty()) {
////									periodicTemporalPattern = (PeriodicalTemporalPattern) set.iterator().next();
////								}
////							} catch (ApplicationException e1) {
////								ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////								return;
////							}
////
////							if (periodicTemporalPattern == null) {
////								periodicTemporalPattern = PeriodicalTemporalPattern.createInstance(userIdentifier,
////									intervalLength);
////								MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern);
////							}
////
////							Log.debugMessage(".actionPerformed | temporalStamps.getStartTime(): "
////									+ TimeParametersPanel.this.temporalStamps.getStartTime(), Log.FINEST);
////							long ms = startTime - TimeParametersPanel.this.temporalStamps.getStartTime().getTime();
////							Log.debugMessage(".actionPerformed | ms:" + ms, Log.FINEST);
////
////							if (ms < 0) {
////								TimeParametersPanel.this.temporalStamps.setStartTime(startDate);
////								temporalPattern.moveAllItems(-ms);
////								ms = 0;
////							}
////
////							if (endTime > TimeParametersPanel.this.temporalStamps.getEndTime().getTime()) {
////								TimeParametersPanel.this.temporalStamps.setEndTime(endDate);
////							}
////
////							Long newMs = new Long(ms);
////							temporalPattern.addIntervalItems(Collections.singletonMap(newMs, periodicTemporalPattern
////									.getId()), Collections.singletonMap(newMs, new Long(endTime - startTime)));
////						} catch (IllegalObjectEntityException e1) {
////							ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////						} catch (CreateObjectException e1) {
////							ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////						} catch (IllegalDataException e1) {
////							ModuleMainFrame.showErrorMessage(TimeParametersPanel.this.panel, e1);
////						}
////
////						// while(startTime + times < endTime) {
////						// temporalPattern.addIntervalItem(times,
////						// Identifier.VOID_IDENTIFIER);
////						// times += intervalLength;
////						// }
////
////						/* TODO */
//////						TimeParametersPanel.this.schedulerModel.refreshTestTemporalStamps();
////
////					} else {
//						TimeParametersPanel.this.schedulerModel.createTest();
//
////					}
//				}
//			}
//			);
			
			ActionListener actionListener = new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					TimeParametersPanel.this.choosedButton = (JButton)e.getSource();
					if (!TimeParametersPanel.this.groupRadioButton.isSelected()) { 
					TimeParametersPanel.this.schedulerModel.createTest();
					} else {
						TimeParametersPanel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_GET_MONITORED_ELEMENT, null, null));
						if (TimeParametersPanel.this.choosedButton == TimeParametersPanel.this.startTimeButton) {
							TimeParametersPanel.this.schedulerModel.addGroupTest(TimeParametersPanel.this.getStartDate());
						} else if (TimeParametersPanel.this.choosedButton == TimeParametersPanel.this.pediodTimeButton) {
							TimeParametersPanel.this.schedulerModel.addGroupTests(TimeParametersPanel.this.getStartDate(), TimeParametersPanel.this.getIntervalLength());
						}
					}
				}
			};

			this.startTimeButton.addActionListener(actionListener);
			this.pediodTimeButton.addActionListener(actionListener);
			this.endTimeButton.addActionListener(actionListener);
			
		}
		
		boolean isTestAgree(Test test) {
			boolean result = false;
			
			if (test != null) {
				TestTemporalType temporalType = test.getTemporalType();
				Log.debugMessage("TimeParametersPanel.isTestAgree | temporalType.value() " + temporalType.value(), Log.FINEST);
				switch (temporalType.value()) {
					case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
						result = this.oneRadioButton.isSelected();
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
						result = this.periodicalRadioButton.isSelected();
						break;
					case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
						result = this.continuosRadioButton.isSelected();
						break;
				}
				if (test.getGroupTestId() != null) {
					Log.debugMessage("TimeParametersPanel.isTestAgree | " + test.getGroupTestId(), Log.FINEST);
					result = this.groupRadioButton.isSelected();
				}
			}
			Log.debugMessage("TimeParametersPanel.isTestAgree | test " + (test != null ? test.getId() : null) + " , result " + result, Log.FINEST);
			return result;
		}
		
		
		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			this.propertyChangeEvent = propertyChangeEvent;
			String propertyName = propertyChangeEvent.getPropertyName();
			Object newValue = propertyChangeEvent.getNewValue();
			if (propertyName.equals(SchedulerModel.COMMAND_DATE_OPERATION)) {
				Date date = (Date) newValue;
				this.startDateSpinner.getModel().setValue(date);
				this.startTimeSpinner.getModel().setValue(date);
			} else if (propertyName.equals(SchedulerModel.COMMAND_SET_TEMPORAL_STAMPS)) {
					this.setTestTemporalStamps((TestTemporalStamps) newValue);				
			} else if (propertyName.equals(SchedulerModel.COMMAND_GET_TEMPORAL_STAMPS)){
				TestTemporalStamps testTemporalStamps = getTestTemporalStamps();
				if (testTemporalStamps != null) {
					TimeParametersPanel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_TEMPORAL_STAMPS, null, testTemporalStamps));
					if (TimeParametersPanel.this.groupRadioButton.isSelected()) {
						TimeParametersPanel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_GROUP_TEST, null, testTemporalStamps));
					}
				}
			} else if(propertyName.equals(SchedulerModel.COMMAND_SET_START_GROUP_TIME)){
				TimeParametersPanel.this.groupRadioButton.doClick();
				Date date = (Date) newValue;
				TimeParametersPanel.this.startDateSpinner.getModel().setValue(date);
				TimeParametersPanel.this.startTimeSpinner.getModel().setValue(date);
			}
			this.propertyChangeEvent = null;
		}
		
		void setOneDateEnable(boolean enable) {
			this.setPeriodEnabled(false);
			this.setContinousEnable(false);
			this.startTimeButton.setVisible(enable);
		}
		
		void setContinousEnable(boolean enable) {
			this.endingLabel.setVisible(enable);
			this.endTimeSpinner.setVisible(enable);
			this.endDateSpinner.setVisible(enable);
			this.endDateButton.setVisible(enable);
			this.endTimeButton.setVisible(enable);
			
			this.interavalLabel.setVisible(false);
			this.periodTimeSpinner.setVisible(false);
			this.periodDaySpinner.setVisible(false);
			this.pediodTimeButton.setVisible(false);
			this.dayIntervalLabel.setVisible(false);
		}
		
		void setGroupEnabled(boolean enable) {
			this.interavalLabel.setText(LangModelSchedule.getString("Interval"));
			// this.endTimeButton.setEnabled(false);
			this.endTimeButton.setVisible(false);
			this.startTimeButton.setVisible(enable);
			this.pediodTimeButton.setVisible(enable);
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Add time item"));
			this.periodTimeSpinner.setVisible(enable);
			this.periodDaySpinner.setVisible(enable);
			this.dayIntervalLabel.setVisible(enable);
			this.interavalLabel.setVisible(enable);

			this.endingLabel.setVisible(false);
			this.endTimeSpinner.setVisible(false);
			this.endDateSpinner.setVisible(false);
			this.endDateButton.setVisible(false);
		}

		
		void setPeriodEnabled(boolean enable) {		
			this.startTimeButton.setVisible(false);
			this.endTimeButton.setVisible(false);
			this.endingLabel.setVisible(false);
			this.endTimeSpinner.setVisible(enable);
			this.endDateSpinner.setVisible(enable);
			this.endDateButton.setVisible(enable);

			
			this.pediodTimeButton.setVisible(enable);
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Add periodical sequence"));
			this.interavalLabel.setText(LangModelSchedule.getString("Period"));
			this.periodTimeSpinner.setVisible(enable);
			this.periodDaySpinner.setVisible(enable);
			this.dayIntervalLabel.setVisible(enable);
			this.interavalLabel.setVisible(enable);

		}
		
		Date getStartDate() {
			Calendar dateCal = Calendar.getInstance();
			Calendar timeCal = Calendar.getInstance();

			dateCal.setTime((Date) this.startDateSpinner.getValue());
			timeCal.setTime((Date) this.startTimeSpinner.getValue());
			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			return dateCal.getTime();
		}
		
		Date getEndDate() {
			Calendar dateCal = Calendar.getInstance();
			Calendar timeCal = Calendar.getInstance();

			dateCal.setTime((Date) this.endDateSpinner.getValue());
			timeCal.setTime((Date) this.endTimeSpinner.getValue());
			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, 0);
			dateCal.set(Calendar.MILLISECOND, 0);
			return dateCal.getTime();
		}
		
		long getIntervalLength(){
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) this.periodTimeSpinner.getValue());
			
			long time = calendar.get(Calendar.HOUR_OF_DAY) * HOUR_LONG + calendar.get(Calendar.MINUTE) * MINUTE_LONG			
				+ (((Integer)this.periodDaySpinner.getValue())).intValue() * DAY_LONG;

			return time;

		}
		
		public TestTemporalStamps getTestTemporalStamps() {
			if (this.temporalStamps != null)
				return this.temporalStamps;
				
				
			TestTemporalType temporalType = null;
			AbstractTemporalPattern temporalPattern = null;
			if (this.oneRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			} else if (this.periodicalRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
//				temporalPattern = (TemporalPattern) TimeParametersPanel.this.timeStamps.getSelectedValue();
				long intervalLength = this.getIntervalLength();
				TypicalCondition typicalCondition = new TypicalCondition(intervalLength, intervalLength, OperationSort.OPERATION_EQUALS, ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, PeriodicalTemporalPatternWrapper.COLUMN_PERIOD); 
				try {
					java.util.Set set = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true, true);
					if (!set.isEmpty()) {
						temporalPattern = (AbstractTemporalPattern)set.iterator().next();
					}
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(this.panel, e);
					return null;
				}
				
				if (temporalPattern == null) {
					try {
						temporalPattern = PeriodicalTemporalPattern.createInstance(LoginManager.getUserId(), intervalLength);
					} catch (CreateObjectException e) {
						AbstractMainFrame.showErrorMessage(this.panel, e);
						return null;
					}
					
					try {
						StorableObjectPool.putStorableObject(temporalPattern);
					} catch (IllegalObjectEntityException e) {
						AbstractMainFrame.showErrorMessage(this.panel, e);
						return null;
					}
				}
				
				if (temporalPattern == null) {
					JOptionPane.showMessageDialog(this.panel,
						LangModelSchedule.getString("Have not choosen temporal pattern"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
						JOptionPane.OK_OPTION);
					this.schedulerModel.setBreakData();
					return null;
				}
			} else if (this.continuosRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS;
			} else  if (this.groupRadioButton.isSelected()) {
				if (this.choosedButton != null) {
					if (this.choosedButton == this.startTimeButton) {
						temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
					} else {
						this.schedulerModel.setBreakData();
						return null;
					}  
				} else {
					this.schedulerModel.setBreakData();
					return null;
				}
			}
			else {
				// ModuleMainFrame.showErrorMessage(this, ne)
				this.schedulerModel.setBreakData();
				return null;
			}

			Date start = this.getStartDate();
			Date end = this.getEndDate();
		
			if (!temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME) && end.getTime() < start.getTime()) {
				JOptionPane.showMessageDialog(this.panel,
					LangModelSchedule.getString("End time less than begin time"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.OK_OPTION);
				this.schedulerModel.setBreakData();
				return null;
				
			}

			return new TestTemporalStamps(temporalType, start, end,
																	temporalPattern);
			
		}
		
		private void setTestTemporalStamps(TestTemporalStamps testTemporalStamps) {		
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
					AbstractTemporalPattern temporalPattern = testTemporalStamps.getTemporalPattern();
					if (temporalPattern instanceof PeriodicalTemporalPattern) {
						PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern)temporalPattern;
						long period = periodicalTemporalPattern.getPeriod();
						this.periodDaySpinner.setValue(new Integer((int) (period / DAY_LONG)));
						period = period % DAY_LONG;
						
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.HOUR_OF_DAY, (int) (period / HOUR_LONG));
						calendar.set(Calendar.MINUTE, (int) ((period % HOUR_LONG)/MINUTE_LONG));
						
						this.periodTimeSpinner.setValue(calendar.getTime());
						
					}
//					this.timeStamps.setSelectedValue(testTemporalStamps.getTemporalPattern(), true);
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					this.continuosRadioButton.doClick();
					break;

			}
		}		
		
		public void setTemporalPatterns(Collection temporalPatterns) {
			this.temporalPatterns = temporalPatterns;
//			ObjListModel model = (ObjListModel) this.timeStamps.getModel();
//			for (Iterator it = this.temporalPatterns.iterator(); it.hasNext();) {
//				TemporalPattern pattern = (TemporalPattern) it.next();
//				model.addElement(pattern);
//			}
			
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

			final JDialog calendarDialog = CalendarUI.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
			calendarDialog.setSize(new Dimension(200, 200));
			calendarDialog.setResizable(false);
			calendarDialog.setLocation(new Point(this.startDateSpinner.getLocationOnScreen().x - 35,
													this.startDateSpinner.getLocationOnScreen().y + 22));
			calendarDialog.setVisible(true);			
			
			if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				this.startDateSpinner.getModel().setValue(cal.getTime());
		}

	}

	private TimeParametersPanel	timeParametersPanel;

	public TimeParametersFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		setResizable(true);
		setClosable(true);
		setIconifiable(true);
		this.timeParametersPanel = new TimeParametersPanel(aContext);
		this.getContentPane().add(this.timeParametersPanel.panel, BorderLayout.CENTER);
	}
	
}
