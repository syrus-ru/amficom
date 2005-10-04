
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
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
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.filter.UI.CalendarUI;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternWrapper;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.AMFICOM.newFilter.DateSpinner;
import com.syrus.AMFICOM.newFilter.TimeSpinner;

@SuppressWarnings("serial")
public class TimeParametersFrame extends JInternalFrame {

	public class TimeParametersPanel implements	PropertyChangeListener {

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

//		private JRadioButton		synchroRadioButton;
//		private JRadioButton		alternateRadioButton;

		Collection					temporalPatterns;

		
		
		TestTemporalStamps	temporalStamps;
		
		JPanel panel;
		
		public static final long MINUTE_LONG = 60L * 1000L;
		public static final long HOUR_LONG = 60L * MINUTE_LONG;
		public static final long DAY_LONG = 24L * HOUR_LONG;

		PropertyChangeEvent	propertyChangeEvent;

		public TimeParametersPanel(ApplicationContext aContext) {
			this.aContext = aContext;
			this.dispatcher = this.aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_DATE_OPERATION, this);
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.dispatcher = aContext.getDispatcher();
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEMPORAL_STAMPS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_GET_TEMPORAL_STAMPS, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_SET_START_GROUP_TIME, this);
			this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
			this.createGUI();
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

			JLabel beginLabel = new JLabel(LangModelSchedule.getString("Text.TimePanel.Start") + ':');
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

															@SuppressWarnings("unqualified-field-access")
															@Override
															public void run() {
																while (true) {
																	if (waiting 
																			&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {

																		Date startDate = TimeParametersPanel.this
																				.getStartDate();
																		Date endDate = TimeParametersPanel.this
																				.getEndDate();

																		if (waiting) {
																			final Set<Identifier> selectedTestIds = TimeParametersPanel.this.schedulerModel
																					.getSelectedTestIds();
																			if (!selectedTestIds.isEmpty()) {
																				int size = selectedTestIds.size();
																				boolean b = !TimeParametersPanel.this.oneRadioButton
																						.isSelected()
																						&& !TimeParametersPanel.this.groupRadioButton
																								.isSelected();
																				if (size == 1 || !b) {
																					if (b) {
																						final long startTime = startDate.getTime();
																						if (startTime > endDate
																								.getTime()) {
																							waiting = false;

																						} else {
																							final Test selectedTest;
																							try {
																							selectedTest = TimeParametersPanel.this.schedulerModel
																									.getSelectedTest();
//																								canBeMoved = selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)
//																										&& 
//																										TimeParametersPanel.this.schedulerModel
//																												.isValid(
//																													selectedTest, 
//																													startTime - selectedTest.getStartTime().getTime());
//																										
																							} catch (final ApplicationException e) {
																								waiting = false;
																								AbstractMainFrame.showErrorMessage(e.getMessage());
																								continue;
																							}
//																							if (canBeMoved) 
																							{
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
																						try {
																							TimeParametersPanel.this.schedulerModel
																									.moveSelectedTests(startDate);
																						} catch (final ApplicationException e) {
																							waiting = false;
																							AbstractMainFrame.showErrorMessage(e.getMessage());
																							continue;
																						}
																					}
																				} else {
																					try {
																						TimeParametersPanel.this.schedulerModel
																								.moveSelectedTests(startDate);
																					} catch (final ApplicationException e) {
																						waiting = false;
																						AbstractMainFrame.showErrorMessage(e.getMessage());
																						continue;
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
							synchronized (this) {
								if (!this.startedThread) {
									synchronized (this) {
										this.thread.start();
										this.startedThread = true;
									}
								}
							}
							final Test selectedTest;
							try {
								selectedTest = TimeParametersPanel.this.schedulerModel.getSelectedTest();
							} catch (final ApplicationException e1) {
								AbstractMainFrame.showErrorMessage(e1.getMessage());
								return;
							}
							this.waiting = TimeParametersPanel.this.propertyChangeEvent == null && 
							TimeParametersPanel.this.isTestAgree(selectedTest);
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
			startDateButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.Calendar"));
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
				final JButton	nowButton		= new JButton(UIStorage.TIME_ICON);
				nowButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
				box.add(nowButton);
				nowButton.setFocusable(false);
				nowButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.CurrentTime")); //$NON-NLS-1$
				nowButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						Date date = new Date(System.currentTimeMillis());
						TimeParametersPanel.this.startDateSpinner.setValue(date);
						TimeParametersPanel.this.startTimeSpinner.setValue(date);
					}
				});
				

				CommonUIUtilities.fixHorizontalSize(this.startTimeSpinner);
				box.add(this.startTimeSpinner);
				box.add(this.startTimeButton);
				gbc.gridwidth = GridBagConstraints.REMAINDER;				
				this.panel.add(box, gbc);
			}

			{
				gbc.gridx = 1;
				gbc.gridy++;
				this.interavalLabel = new JLabel(LangModelSchedule.getString("Text.TimePanel.Interval") + ':');
				this.panel.add(this.interavalLabel, gbc);
				gbc.weightx = 1.0;
				gbc.gridx = 1;
				gbc.gridy++;
				this.periodTimeSpinner = new TimeSpinner();
				this.periodDaySpinner = new JSpinner(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
//				this.periodMonthSpinner = new JSpinner();
				{
					Box box = new Box(BoxLayout.X_AXIS);
//					
					CommonUIUtilities.fixHorizontalSize(this.periodTimeSpinner);
//					box.add(this.periodMonthSpinner);
//					this.monthIntervalLabel = new JLabel(LangModelSchedule.getString("month"));
//					box.add(this.monthIntervalLabel);
					
					box.add(this.periodDaySpinner);
					this.dayIntervalLabel = new JLabel(LangModelSchedule.getString("Text.TimePanel.DayOrDays"));
					box.add(this.dayIntervalLabel);
					box.add(Box.createHorizontalGlue());
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

																@SuppressWarnings("unqualified-field-access")
																@Override
																public void run() {
																	while (true) {
																		if (waiting
																				&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {
																			if (waiting) {
																				final Test selectedTest;
																				try {
																					selectedTest = TimeParametersPanel.this.schedulerModel
																							.getSelectedTest();
																				} catch (ApplicationException e1) {
																					waiting = false;
																					AbstractMainFrame.showErrorMessage(e1.getMessage());
																					continue;
																				}
																				if (selectedTest != null
																						&& selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
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
									synchronized (this.thread) {
										this.thread.start();
										this.startedThread = true;
									}
								}
								try {
									this.waiting = TimeParametersPanel.this.propertyChangeEvent == null &&  TimeParametersPanel.this.isTestAgree(TimeParametersPanel.this.schedulerModel
										.getSelectedTest());
								} catch (ApplicationException e1) {
									this.waiting = false;
									AbstractMainFrame.showErrorMessage(e1.getMessage());
									return;
								}
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
			this.endingLabel = new JLabel(LangModelSchedule.getString("Text.TimePanel.Finish") + ':');
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
			this.endDateButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.Calendar"));
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

															@SuppressWarnings("unqualified-field-access")
															@Override
															public void run() {
																while (true) {
																	if (waiting
																			&& (System.currentTimeMillis() - previousEventTime) > TIMEOUT) {
//																		Log.debugMessage(".run | 1 ", Log.FINEST);
																		final Test selectedTest;
																		try {
																			selectedTest = TimeParametersPanel.this.schedulerModel
																					.getSelectedTest();
																		} catch (final ApplicationException e) {
																			waiting = false;
																			AbstractMainFrame.showErrorMessage(e.getMessage());
																			continue;
																		}
																		if (selectedTest != null
																				&& selectedTest.getVersion().equals(StorableObjectVersion.INITIAL_VERSION)) {
//																			Log.debugMessage(".run | 2 ", Log.FINEST);
																			Date startDate = TimeParametersPanel.this
																					.getStartDate();
																			Date endDate = TimeParametersPanel.this
																					.getEndDate();
																			if (startDate.getTime() < endDate.getTime()) {
//																				Log.debugMessage(".run | 3 ", Log.FINEST);
																				final boolean valid2;
																				try {
																					valid2 = TimeParametersPanel.this.schedulerModel.isValid(startDate, endDate, selectedTest.getMonitoredElement().getId());
																				} catch (ApplicationException e) {
																					waiting = false;
																					AbstractMainFrame.showErrorMessage(e.getMessage());
																					continue;
																				}
																				if (valid2){
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
							try {
								this.waiting = TimeParametersPanel.this.propertyChangeEvent == null && TimeParametersPanel.this.isTestAgree(TimeParametersPanel.this.schedulerModel
									.getSelectedTest());
							} catch (ApplicationException e1) {
								this.waiting = false;
								AbstractMainFrame.showErrorMessage(e1.getMessage());
								return;
							}
						}
						this.previousEventTime = System.currentTimeMillis();						
					}
				};

				this.endTimeSpinner.addChangeListener(changeListener);
				this.endDateSpinner.addChangeListener(changeListener);
			}
			
			gbc.insets = gbcInsetsDefault;

			ButtonGroup group = new ButtonGroup();
			group.add(this.oneRadioButton);
			group.add(this.periodicalRadioButton);
			group.add(this.continuosRadioButton);
			group.add(this.groupRadioButton);

//			this.synchroRadioButton = new JRadioButton(LangModelSchedule.getString("Together"));
//			this.alternateRadioButton = new JRadioButton(LangModelSchedule.getString("InTurn"));
//			this.synchroRadioButton.setEnabled(false);
//			this.alternateRadioButton.setEnabled(false);
//			ButtonGroup group3 = new ButtonGroup();
//			group3.add(this.synchroRadioButton);
//			group3.add(this.alternateRadioButton);

			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 1.0;
			this.panel.add(Box.createVerticalGlue(), gbc);
			
//			this.synchroRadioButton.doClick();
			this.periodicalRadioButton.doClick();		
			this.oneRadioButton.doClick();
			
		}
		
		
		private void createTemporalTypeButtons() {
			this.oneRadioButton = new JRadioButton(LangModelSchedule.getString("Text.Test.TemporalType.Onetime"));
			this.continuosRadioButton = new JRadioButton(LangModelSchedule.getString("Text.Test.TemporalType.Continual"));		
			this.periodicalRadioButton = new JRadioButton(LangModelSchedule.getString("Text.Test.TemporalType.Periodical"));			
			this.groupRadioButton = new JRadioButton(LangModelSchedule.getString("Text.Test.TemporalType.Sectional"));


			this.oneRadioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
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
			this.startTimeButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.AddTimeItem"));
			this.startTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
			this.endTimeButton = new JButton(">>");
			this.endTimeButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.AddTimeItem"));
			this.endTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
			this.pediodTimeButton = new JButton(">>");
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.AddPeriodicalSequence"));
			this.pediodTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));

			
			ActionListener actionListener = new ActionListener() {
				
				public void actionPerformed(final ActionEvent event) {
					TimeParametersPanel.this.choosedButton = (JButton)event.getSource();
					try {
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
					} catch (final ApplicationException e) {
						AbstractMainFrame.showErrorMessage(e.getMessage());
					}
				}
			};

			this.startTimeButton.addActionListener(actionListener);
			this.pediodTimeButton.addActionListener(actionListener);
			this.endTimeButton.addActionListener(actionListener);
			
		}
		
		boolean isTestAgree(final Test test) {
			boolean result = false;
			
			if (test != null) {
				TestTemporalType temporalType = test.getTemporalType();
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
				if (!test.getGroupTestId().isVoid()) {
					result = this.groupRadioButton.isSelected();
				}
			}
			return result;
		}
		
		
		public void propertyChange(PropertyChangeEvent propertyChangeEvent1) {
			this.propertyChangeEvent = propertyChangeEvent1;
			String propertyName = propertyChangeEvent1.getPropertyName();
			Object newValue = propertyChangeEvent1.getNewValue();
			if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEMPORAL_STAMPS)) {
				try {
					final Test test;
					try {
						test = this.schedulerModel.getSelectedTest();
					} catch (final ApplicationException e) {
						throw new ApplicationException(LangModelGeneral.getString("Error.CannotAcquireObject"));
					}
				
					if (test != null) {
						if (test.getGroupTestId().isVoid()) {
							final Identifier temporalPatternId = test.getTemporalPatternId();
							AbstractTemporalPattern temporalPattern = null;
							if (temporalPatternId != null) {
								try {
									temporalPattern = 
										StorableObjectPool.getStorableObject(temporalPatternId, true);
								}catch (final ApplicationException e) {
									throw new ApplicationException(LangModelGeneral.getString("Error.CannotAcquireObject"));
								}
							}
							this.setTestTemporalStamps(test.getStartTime(),
									test.getEndTime(),
									temporalPattern,
									test.getTemporalType());
						} else {
							this.setGroupTestSelected(test.getStartTime());
						}
					}
				} catch (final ApplicationException e) {
					AbstractMainFrame.showErrorMessage(e.getMessage());
				}
			} else if (propertyName.equals(SchedulerModel.COMMAND_DATE_OPERATION)) {
				Date date = (Date) newValue;
				this.startDateSpinner.getModel().setValue(date);
				this.startTimeSpinner.getModel().setValue(date);
			} 
//			else if (propertyName.equals(SchedulerModel.COMMAND_SET_TEMPORAL_STAMPS)) {
//					this.setTestTemporalStamps((TestTemporalStamps) newValue);				
//			} 
			else if (propertyName.equals(SchedulerModel.COMMAND_GET_TEMPORAL_STAMPS)){
				TestTemporalStamps testTemporalStamps = getTestTemporalStamps();
				if (testTemporalStamps != null) {
					this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_TEMPORAL_STAMPS, null, testTemporalStamps));
					if (this.groupRadioButton.isSelected()) {
						this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_SET_GROUP_TEST, null, testTemporalStamps));
					}
				}
			} else if(propertyName.equals(SchedulerModel.COMMAND_SET_START_GROUP_TIME)){
				this.setGroupTestSelected((Date) newValue);
			} else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
				final Test selectedTest;
				try {
					selectedTest = this.schedulerModel.getSelectedTest();
				} catch (final ApplicationException e) {
					AbstractMainFrame.showErrorMessage(e.getMessage());
					return;
				}
				boolean enable = selectedTest == null;
				this.startTimeButton.setEnabled(enable);
				this.endTimeButton.setEnabled(enable);				
				enable = enable || !selectedTest.getGroupTestId().isVoid();
				this.pediodTimeButton.setEnabled(enable);				
			}
			this.propertyChangeEvent = null;
		}
		
		void setGroupTestSelected(final Date date) {
			if(!this.groupRadioButton.isSelected()) {
				this.groupRadioButton.doClick();
			}
			this.startDateSpinner.getModel().setValue(date);
			this.startTimeSpinner.getModel().setValue(date);
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
			this.startTimeButton.setVisible(false);
			this.periodTimeSpinner.setVisible(false);
			this.periodDaySpinner.setVisible(false);
			this.pediodTimeButton.setVisible(false);
			this.dayIntervalLabel.setVisible(false);
		}
		
		void setGroupEnabled(boolean enable) {
			final Test selectedTest;
			try {
				selectedTest = this.schedulerModel.getSelectedTest();
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(e.getMessage());
				return;
			}
			
			this.interavalLabel.setText(LangModelSchedule.getString("Text.TimePanel.Interval") + ':');
			// this.endTimeButton.setEnabled(false);
			this.endTimeButton.setVisible(false);
			this.startTimeButton.setVisible(enable);
			this.pediodTimeButton.setVisible(enable);
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.AddTimeItem"));
			this.periodTimeSpinner.setVisible(enable);
			this.periodDaySpinner.setVisible(enable);
			this.dayIntervalLabel.setVisible(enable);
			this.interavalLabel.setVisible(enable);

			if (selectedTest != null && !selectedTest.getGroupTestId().isVoid()) {
				this.pediodTimeButton.setEnabled(true);
			}
			
			this.endingLabel.setVisible(false);
			this.endTimeSpinner.setVisible(false);
			this.endDateSpinner.setVisible(false);
			this.endDateButton.setVisible(false);
		}

		
		void setPeriodEnabled(boolean enable) {		
			final Test selectedTest;
			try {
				selectedTest = this.schedulerModel.getSelectedTest();
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(e.getMessage());
				return;
			}
			this.startTimeButton.setVisible(false);
			this.endTimeButton.setVisible(false);
			this.endingLabel.setVisible(true);
			this.endTimeSpinner.setVisible(enable);
			this.endDateSpinner.setVisible(enable);
			this.endDateButton.setVisible(enable);

			
			this.pediodTimeButton.setVisible(enable);
			this.pediodTimeButton.setToolTipText(LangModelSchedule.getString("Text.TimePanel.AddPeriodicalSequence"));
			this.interavalLabel.setText(LangModelSchedule.getString("Text.TimePanel.Period") + ':');
			this.periodTimeSpinner.setVisible(enable);
			this.periodDaySpinner.setVisible(enable);
			this.dayIntervalLabel.setVisible(enable);
			this.interavalLabel.setVisible(enable);
			

			this.pediodTimeButton.setEnabled(selectedTest == null);
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
			if (this.temporalStamps != null) {
				return this.temporalStamps;
			}
				
			final Date start = this.getStartDate();
			Date end = null;
			TestTemporalType temporalType = null;
			AbstractTemporalPattern temporalPattern = null;
			if (this.oneRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			} else if (this.periodicalRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
//				temporalPattern = (TemporalPattern) TimeParametersPanel.this.timeStamps.getSelectedValue();
				long intervalLength = this.getIntervalLength();
				TypicalCondition typicalCondition = new TypicalCondition(intervalLength, 
					intervalLength, 
					OperationSort.OPERATION_EQUALS, 
					ObjectEntities.PERIODICALTEMPORALPATTERN_CODE, 
					PeriodicalTemporalPatternWrapper.COLUMN_PERIOD); 
				try {
					final Set<PeriodicalTemporalPattern> periodicalTemporalPatterns = StorableObjectPool.getStorableObjectsByCondition(typicalCondition, true, true);
					if (!periodicalTemporalPatterns.isEmpty()) {
						temporalPattern = periodicalTemporalPatterns.iterator().next();
					}
				} catch (final ApplicationException e) {
					this.schedulerModel.setBreakData();
					JOptionPane.showMessageDialog(Environment.getActiveWindow(),
						LangModelGeneral.getString("Error.CannotAcquireObject"),
						LangModelGeneral.getString("Error"),
						JOptionPane.OK_OPTION);
					return null;
				}
				
				if (temporalPattern == null) {
					try {
						temporalPattern = PeriodicalTemporalPattern.createInstance(LoginManager.getUserId(), intervalLength);
					} catch (CreateObjectException e) {
						this.schedulerModel.setBreakData();
						JOptionPane.showMessageDialog(Environment.getActiveWindow(),
							LangModelSchedule.getString("Error.CannotCreatePeriodicalPattern"),
							LangModelGeneral.getString("Error"),
							JOptionPane.OK_OPTION);
						return null;
					}

				}				
				 end = this.getEndDate();
			} else if (this.continuosRadioButton.isSelected()) {
				temporalType = TestTemporalType.TEST_TEMPORAL_TYPE_CONTINUOUS;
				end = this.getEndDate();
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
			
			if (!temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME) && !start.before(end)) {
				JOptionPane.showMessageDialog(this.panel,
					LangModelSchedule.getString("Error.EndTimeNotGreaterThanBeginTime"), 
					LangModelSchedule.getString("Error"), 
					JOptionPane.OK_OPTION);
				this.schedulerModel.setBreakData();
				return null;				
			}

			assert end == null || start.before(end) : "end time less than start time";
			
			return new TestTemporalStamps(temporalType, 
				start, 
				end,
				temporalPattern);
			
		}
		
		private void setTestTemporalStamps(final Date startTime,
		                                   final Date endTime,
		                                   final AbstractTemporalPattern temporalPattern,
		                                   final TestTemporalType testTemporalType) {		
			this.startDateSpinner.getModel().setValue(startTime);
			this.startTimeSpinner.getModel().setValue(startTime);
			if (endTime != null) {
				this.endDateSpinner.getModel().setValue(endTime);
				this.endTimeSpinner.getModel().setValue(endTime);
			}

			switch (testTemporalType.value()) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
					if (!this.oneRadioButton.isSelected()) {
						this.oneRadioButton.doClick();
					}
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					if (!this.periodicalRadioButton.isSelected()) {
						this.periodicalRadioButton.doClick();
					}
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
					break;
				case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
					if (!this.continuosRadioButton.isSelected()) {
						this.continuosRadioButton.doClick();
					}
					break;

			}
		}
		
		void showEndCalendar() {
			Calendar cal = Calendar.getInstance();
			Date date = (Date) this.endDateSpinner.getModel().getValue();
			cal.setTime(date);

			JDialog calendarDialog = CalendarUI.createDialogInstance(Environment.getActiveWindow(), cal, true, true);
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
			calendarDialog.setLocation(new Point(this.startDateSpinner.getLocationOnScreen().x - 35,
													this.startDateSpinner.getLocationOnScreen().y + 22));
			calendarDialog.setVisible(true);			
			
			if (((CalendarUI) calendarDialog.getContentPane()).getStatus() == CalendarUI.STATUS_OK)
				this.startDateSpinner.getModel().setValue(cal.getTime());
		}

	}

	TimeParametersPanel	timeParametersPanel;

	public TimeParametersFrame(ApplicationContext aContext) {
		super.setTitle(LangModelSchedule.getString("Text.TimePanel.Title")); //$NON-NLS-1$
		super.setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		super.setResizable(true);
		super.setClosable(false);
		super.setIconifiable(true);
		this.timeParametersPanel = new TimeParametersPanel(aContext);
		this.getContentPane().add(this.timeParametersPanel.panel, BorderLayout.CENTER);
	}
	
}
