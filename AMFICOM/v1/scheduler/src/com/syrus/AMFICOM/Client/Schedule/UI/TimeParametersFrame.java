
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
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.CalendarUI;
import com.syrus.AMFICOM.Client.General.UI.DateSpinner;
import com.syrus.AMFICOM.Client.General.UI.TimeSpinner;
import com.syrus.AMFICOM.Client.General.UI.UIGeneralStorage;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.Commandable;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestTemporalStampsEditor;
import com.syrus.AMFICOM.Client.Schedule.WindowCommand;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternWrapper;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

public class TimeParametersFrame extends JInternalFrame  implements Commandable {

	private static final long	serialVersionUID	= 6562288896016470275L;

	public class TimeParametersPanel extends JPanel implements TestTemporalStampsEditor {

		private static final long	serialVersionUID	= -7975294015403739057L;

		SchedulerModel schedulerModel;
		ApplicationContext			aContext;

		private TimeSpinner			startTimeSpinner;

		private DateSpinner			startDateSpinner;

		private JLabel interavalLabel;
//		private JLabel monthIntervalLabel;
		private JLabel dayIntervalLabel;
		
//		private JSpinner					periodMonthSpinner;
		private JSpinner					periodDaySpinner;
		private JSpinner					periodTimeSpinner;
		
		private JButton periodOneTimeButton;
		private JButton periodPediodTimeButton;
		
		private JLabel endingLabel;
		private JButton endDateButton;
		private TimeSpinner					endTimeSpinner;		
		private DateSpinner					endDateSpinner;

//		ObjList						timeStamps;

		JRadioButton				oneRadioButton;

		private JRadioButton		continuosRadioButton;

		private JRadioButton		periodicalRadioButton;
		
		private JRadioButton		timeTableRadioButton;

		private JRadioButton		synchroRadioButton;

		private JRadioButton		alternateRadioButton;

		Collection					temporalPatterns;

		TestTemporalStamps	temporalStamps;
		
		public static final long MINUTE_LONG = 60L * 1000L;
		public static final long HOUR_LONG = 60L * MINUTE_LONG;
		public static final long DAY_LONG = 24L * HOUR_LONG;
		
		

		public TimeParametersPanel() {
			super(new GridBagLayout());
			this.init();
		}

		public TimeParametersPanel(ApplicationContext aContext) {
			this();
			this.aContext = aContext;
			this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
			this.schedulerModel.setTestTemporalStampsEditor(this);
		}

		private void init() {
			final GridBagConstraints gbc = new GridBagConstraints();
			this.setBorder(BorderFactory.createEtchedBorder());			
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
			add(this.oneRadioButton, gbc);
			gbc.gridy++;
			add(this.periodicalRadioButton, gbc);
			gbc.gridy++;
			add(this.continuosRadioButton, gbc);
			gbc.gridy++;
			add(this.timeTableRadioButton, gbc);

			JLabel beginLabel = new JLabel(LangModelSchedule.getString("Start") + ':');
			gbc.gridx = 1;
			gbc.gridy++;			
			add(beginLabel, gbc);
			gbc.gridwidth = 1;
			gbc.gridx = 1;
			gbc.gridy++;

			this.startTimeSpinner = new TimeSpinner();
			this.startDateSpinner = new DateSpinner();
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
				UIGeneralStorage.fixHorizontalSize(this.startDateSpinner);
				box.add(this.startDateSpinner);
				box.add(startDateButton);
				box.add(Box.createHorizontalGlue());
				UIGeneralStorage.fixHorizontalSize(this.startTimeSpinner);
				box.add(this.startTimeSpinner);
				box.add(this.periodOneTimeButton);
				gbc.gridwidth = GridBagConstraints.REMAINDER;				
				add(box, gbc);
			}

			{
				gbc.gridx = 1;
				gbc.gridy++;
				this.interavalLabel = new JLabel(LangModelSchedule.getString("Interval") + ':');
				add(this.interavalLabel, gbc);
				gbc.weightx = 1.0;
				gbc.gridx = 1;
				gbc.gridy++;
				this.periodTimeSpinner = new TimeSpinner();
				this.periodDaySpinner = new JSpinner();
//				this.periodMonthSpinner = new JSpinner();
				{
					Box box = new Box(BoxLayout.X_AXIS);
					box.add(Box.createHorizontalGlue());
					UIGeneralStorage.fixHorizontalSize(this.periodTimeSpinner);
//					box.add(this.periodMonthSpinner);
//					this.monthIntervalLabel = new JLabel(LangModelSchedule.getString("month"));
//					box.add(this.monthIntervalLabel);
					box.add(this.periodDaySpinner);
					this.dayIntervalLabel = new JLabel(LangModelSchedule.getString("day"));
					box.add(this.dayIntervalLabel);
					box.add(this.periodTimeSpinner);
					box.add(this.periodPediodTimeButton);
					Calendar calendar = Calendar.getInstance();
					/* TODO debug mode */
//					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.HOUR_OF_DAY, 1);
					calendar.set(Calendar.MINUTE, 0);
					((SpinnerDateModel)this.periodTimeSpinner.getModel()).setValue(calendar.getTime());
					
					gbc.gridwidth = GridBagConstraints.REMAINDER;
					add(box, gbc);
				}
			}
			
			gbc.gridx = 1;
			gbc.gridy++;
			this.endingLabel = new JLabel(LangModelSchedule.getString("Finish") + ':');
			add(this.endingLabel, gbc);
			gbc.weightx = 1.0;
			gbc.gridx = 1;
			gbc.gridy++;
			this.endTimeSpinner = new TimeSpinner();
			{
			/* TODO debug */
			SpinnerDateModel model = (SpinnerDateModel) this.endTimeSpinner.getModel();
			model.setValue(new Date(((Date)model.getValue()).getTime() + 5L * 60L * 60L * 1000L));
			}
			this.endDateSpinner = new DateSpinner();
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
				UIGeneralStorage.fixHorizontalSize(this.endDateSpinner);
				box.add(this.endDateSpinner);
				box.add(this.endDateButton);
				box.add(Box.createHorizontalGlue());
				UIGeneralStorage.fixHorizontalSize(this.endTimeSpinner);
				box.add(this.endTimeSpinner);
				gbc.gridwidth = GridBagConstraints.REMAINDER;
				add(box, gbc);
			}

			JSeparator jsep2 = new JSeparator();
			gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
			gbc.gridy++;
			jsep2.setBorder(BorderFactory.createEtchedBorder());
			add(jsep2, gbc);
			gbc.insets = gbcInsetsDefault;

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
//							SchedulerModel.showErrorMessage(TimeParametersPanel.this, ioee);
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
			group.add(this.timeTableRadioButton);


			JSeparator jsep3 = new JSeparator();
			gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
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
				gbc.insets = UIManager.getInsets(ResourceKeys.INSETS_NULL);
				gbc.gridheight = GridBagConstraints.RELATIVE;
				jsep4.setBorder(BorderFactory.createEtchedBorder());
				gbc.gridy++;
				add(jsep4, gbc);
				gbc.insets = gbcInsetsDefault;
			} else {
//				gbc.gridy++;
//				gbc.gridheight = GridBagConstraints.RELATIVE;
//				add(new JLabel(), gbc);
			}
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.gridheight = GridBagConstraints.REMAINDER;
			gbc.weighty = 1.0;
			add(Box.createVerticalGlue(), gbc);
			
			this.synchroRadioButton.doClick();
			this.periodicalRadioButton.doClick();
			this.oneRadioButton.doClick();
			
		}
		
		
		private void createTemporalTypeButtons() {
			this.oneRadioButton = new JRadioButton(LangModelSchedule.getString("Onetime"));
			this.continuosRadioButton = new JRadioButton(LangModelSchedule.getString("Continual"));		
			this.periodicalRadioButton = new JRadioButton(LangModelSchedule.getString("Periodical"));			
			this.timeTableRadioButton = new JRadioButton(LangModelSchedule.getString("Time table"));


			this.oneRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						setOneDateEnable(true);
						setEndDateEnable(false);
						setPeriodEnabled(false);
					}
					}
				});

			
			this.periodicalRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						setEndDateEnable(true);
						setPeriodEnabled(true);
					}
				}
			});
			
			this.continuosRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						setEndDateEnable(true);						
						setPeriodEnabled(false);
					}
				}
			});
			
			this.timeTableRadioButton.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						setEndDateEnable(true);						
						setIntervalEnabled(true);
					}
				}
			});
			
			this.periodOneTimeButton = new JButton(">>");
			this.periodOneTimeButton.setToolTipText(LangModelSchedule.getString("Add time item"));
			this.periodOneTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
			this.periodOneTimeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
						if (TimeParametersPanel.this.temporalStamps == null) {
							RISDSessionInfo sessionInterface = (RISDSessionInfo) TimeParametersPanel.this.aContext.getSessionInterface();
							try {
								IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern.createInstance(sessionInterface.getUserIdentifier(), null, null);
								MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
								TimeParametersPanel.this.temporalStamps = new TestTemporalStamps(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL, getStartDate(), getEndDate(), intervalsTemporalPattern);
							} catch (IllegalObjectEntityException e1) {
								SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
							} catch (CreateObjectException e1) {
								SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
							}
							
						} 
						
						IntervalsTemporalPattern temporalPattern = (IntervalsTemporalPattern) TimeParametersPanel.this.temporalStamps.getTemporalPattern();
						temporalPattern.addIntervalItem((getStartDate().getTime() - TimeParametersPanel.this.temporalStamps.getStartTime().getTime()), Identifier.VOID_IDENTIFIER);
						
						TimeParametersPanel.this.schedulerModel.refreshTestTemporalStamps();

						
						
					}
			});

			
			this.periodPediodTimeButton = new JButton(">>");
			this.periodPediodTimeButton.setToolTipText(LangModelSchedule.getString("Add periodical sequence"));
			this.periodPediodTimeButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
			
			this.periodPediodTimeButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
						Date startDate = getStartDate();
						Date endDate = getEndDate();
						
						long startTime = startDate.getTime();
						long endTime = endDate.getTime();
					
						RISDSessionInfo sessionInterface = (RISDSessionInfo) TimeParametersPanel.this.aContext.getSessionInterface();
						Identifier userIdentifier = sessionInterface.getUserIdentifier();
						
						if (TimeParametersPanel.this.temporalStamps == null) {
							
							try {
								IntervalsTemporalPattern intervalsTemporalPattern = IntervalsTemporalPattern.createInstance(userIdentifier, null, null);

								MeasurementStorableObjectPool.putStorableObject(intervalsTemporalPattern);
								
								TimeParametersPanel.this.temporalStamps = new TestTemporalStamps(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL, startDate, endDate, intervalsTemporalPattern);
							} catch (IllegalObjectEntityException e1) {
								SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
							} catch (CreateObjectException e1) {
								SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
							}
							
						} 

						long intervalLength = getIntervalLength();

						IntervalsTemporalPattern temporalPattern = (IntervalsTemporalPattern) TimeParametersPanel.this.temporalStamps.getTemporalPattern();
						
						
						
						
						try {
						PeriodicalTemporalPattern periodicTemporalPattern = null;
						TypicalCondition typicalCondition = new TypicalCondition(
																					intervalLength,
																					intervalLength,
																					OperationSort.OPERATION_EQUALS,
																					ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE,
																					PeriodicalTemporalPatternWrapper.COLUMN_PERIOD);
						try {
							java.util.Set set = MeasurementStorableObjectPool.getStorableObjectsByCondition(
								typicalCondition, true);
							if (!set.isEmpty()) {
								periodicTemporalPattern = (PeriodicalTemporalPattern) set.iterator().next();
							}
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
							return;
						}

						if (periodicTemporalPattern == null) {
							periodicTemporalPattern = PeriodicalTemporalPattern.createInstance(userIdentifier,
								intervalLength);
							MeasurementStorableObjectPool.putStorableObject(periodicTemporalPattern);
						}

						Log.debugMessage(".actionPerformed | temporalStamps.getStartTime(): " + TimeParametersPanel.this.temporalStamps.getStartTime(), Log.FINEST);
						long ms = startTime - TimeParametersPanel.this.temporalStamps.getStartTime().getTime();
						Log.debugMessage(".actionPerformed | ms:" + ms, Log.FINEST);
						temporalPattern.addIntervalItem(ms, periodicTemporalPattern.getId());
						temporalPattern.setIntervalDuration(ms, endTime - startTime);
					} catch (IllegalObjectEntityException e1) {
							SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
						} catch (CreateObjectException e1) {
							SchedulerModel.showErrorMessage(TimeParametersPanel.this, e1);
						}
						
						
						
//						while(startTime + times < endTime) {
//							temporalPattern.addIntervalItem(times, Identifier.VOID_IDENTIFIER);
//							times += intervalLength;
//						}
						
						TimeParametersPanel.this.schedulerModel.refreshTestTemporalStamps();
				}
			}
			);

			
		}
		
		void setOneDateEnable(boolean enable) {
			this.periodOneTimeButton.setVisible(false);
			this.periodPediodTimeButton.setVisible(false);
		}
		
		void setEndDateEnable(boolean enable) {
			this.endingLabel.setEnabled(enable);
			this.endTimeSpinner.setEnabled(enable);
			this.endDateSpinner.setEnabled(enable);
			this.endDateButton.setEnabled(enable);
			
			this.endingLabel.setVisible(enable);
			this.endTimeSpinner.setVisible(enable);
			this.endDateSpinner.setVisible(enable);
			this.endDateButton.setVisible(enable);


		}
		
		void setIntervalEnabled(boolean enable) {
			this.periodOneTimeButton.setVisible(true);
			this.periodPediodTimeButton.setVisible(true);
			setPeriodEnabled(enable, LangModelSchedule.getString("Interval"));
		}

		
		void setPeriodEnabled(boolean enable) {		
			this.periodOneTimeButton.setVisible(false);
			this.periodPediodTimeButton.setVisible(false);
			setPeriodEnabled(enable, LangModelSchedule.getString("Period"));
		}
		
		private void setPeriodEnabled(boolean enable, String text) {
			this.periodTimeSpinner.setVisible(enable);
			this.periodDaySpinner.setVisible(enable);
//			this.periodMonthSpinner.setVisible(enable);
//			this.monthIntervalLabel.setVisible(enable);
			this.dayIntervalLabel.setVisible(enable);
			this.interavalLabel.setVisible(enable);
			
			this.periodTimeSpinner.setEnabled(enable);
			this.periodDaySpinner.setEnabled(enable);
//			this.periodMonthSpinner.setEnabled(enable);
			this.interavalLabel.setText(text);
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
				TypicalCondition typicalCondition = new TypicalCondition(intervalLength, intervalLength, OperationSort.OPERATION_EQUALS, ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, PeriodicalTemporalPatternWrapper.COLUMN_PERIOD); 
				try {
					java.util.Set set = MeasurementStorableObjectPool.getStorableObjectsByCondition(typicalCondition, true);
					if (!set.isEmpty()) {
						temporalPattern = (AbstractTemporalPattern)set.iterator().next();
					}
				} catch (ApplicationException e) {
					SchedulerModel.showErrorMessage(this, e);
					return null;
				}
				
				if (temporalPattern == null) {
					RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
					try {
						temporalPattern = PeriodicalTemporalPattern.createInstance(sessionInterface.getUserIdentifier(), intervalLength);
					} catch (CreateObjectException e) {
						SchedulerModel.showErrorMessage(this, e);
						return null;
					}
					
					try {
						MeasurementStorableObjectPool.putStorableObject(temporalPattern);
					} catch (IllegalObjectEntityException e) {
						SchedulerModel.showErrorMessage(this, e);
						return null;
					}
				}
				
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

			Date start = this.getStartDate();
			Date end = this.getEndDate();
		
			if (!temporalType.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME) && end.getTime() < start.getTime()) {
				JOptionPane.showMessageDialog(this,
					LangModelSchedule.getString("End time less than begin time"), LangModelSchedule.getString("Error"), //$NON-NLS-1$ //$NON-NLS-2$
					JOptionPane.OK_OPTION);
				this.schedulerModel.setBreakData();
				return null;
				
			}

			return new TestTemporalStamps(temporalType, start, end,
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

	private TimeParametersPanel	panel;

	private Command				command;

	public TimeParametersFrame(ApplicationContext aContext) {
		setTitle(LangModelSchedule.getString("TemporalType.Title")); //$NON-NLS-1$
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
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
