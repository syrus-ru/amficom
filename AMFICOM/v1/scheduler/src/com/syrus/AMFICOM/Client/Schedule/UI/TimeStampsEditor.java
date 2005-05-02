/*-
 * $Id: TimeStampsEditor.java,v 1.6 2005/05/02 12:40:13 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.UI.TestLine.TestTimeItem;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/02 12:40:13 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class TimeStampsEditor extends TimeLine {

	private static final long	serialVersionUID	= 3258417243959605561L;

	private SchedulerModel	schedulerModel;

	TestTemporalStamps testTemporalStamps; 
	
	private SortedMap intervalsAbstractTemporalPatternMap;
	private SortedMap intervalsDuration;

	SortedSet selectedItems = new TreeSet();
	
	SortedMap offsetIdBuffer = new TreeMap();
	SortedMap offsetDurationBuffer = new TreeMap();
	
	JPopupMenu popupMenu;
	int popupRelativeX;
	
	Point startPoint;
	Point currentPoint;
	Point previousPoint;
	
	JMenuItem undoMenuItem;
	JMenuItem cutMenuItem;
	JMenuItem copyMenuItem;
	JMenuItem pasteMenuItem;
	JMenuItem deleteMenuItem;
	
	JMenuItem joinMenuItem;
	JMenuItem disjoinMenuItem;
	
	public TimeStampsEditor(ApplicationContext aContext, String title) {
		this.title = title;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();		
		this.createMouseListener();
		this.createPopupMenu();
		this.setToolTipText("");		
	}
	
	void updateMenuItemsState() {
		boolean b = (this.selectedItems != null && !this.selectedItems.isEmpty());
		this.cutMenuItem.setEnabled(b);
		this.copyMenuItem.setEnabled(b);
		this.deleteMenuItem.setEnabled(b);
		this.joinMenuItem.setEnabled(b ? this.selectedItems.size() > 2 : b);
		this.disjoinMenuItem.setEnabled(b);
	}

	private void createMouseListener() {
		this.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();

				if (SwingUtilities.isLeftMouseButton(e)) {
					if (!e.isShiftDown() && TimeStampsEditor.this.selectedItems != null) {
						TimeStampsEditor.this.selectedItems.clear();
					}

					if (!TimeStampsEditor.this.timeItems.isEmpty()) {
						for (Iterator it = TimeStampsEditor.this.timeItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
								if (TimeStampsEditor.this.selectedItems.contains(testTimeItem)) {
									TimeStampsEditor.this.selectedItems.remove(testTimeItem);
									break;
								}								
								TimeStampsEditor.this.selectedItems.add(testTimeItem);
								TimeStampsEditor.this.repaint();
								break;
							}
						}
					}
					
					TimeStampsEditor.this.updateMenuItemsState();
					
				} else if (SwingUtilities.isRightMouseButton(e)) {
					TimeStampsEditor.this.popupRelativeX = x;
					TimeStampsEditor.this.popupMenu.show(TimeStampsEditor.this, x, y);
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps.getTemporalPattern();
				if (temporalPattern instanceof IntervalsTemporalPattern) {
					IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
					if (TimeStampsEditor.this.currentPoint != null && TimeStampsEditor.this.startPoint != null) {
						// Log.debugMessage(".mouseReleased |
						// testTemporalStamps.getStartTime:" +
						// testTemporalStamps.getStartTime(), Log.FINEST);
						// Log.debugMessage(".mouseReleased |
						// testTemporalStamps.getEndTime:" +
						// testTemporalStamps.getEndTime(), Log.FINEST);

						long offset = (long) ((TimeStampsEditor.this.currentPoint.x - TimeStampsEditor.this.startPoint.x) / TimeStampsEditor.this.scale);
						// Log.debugMessage(".mouseReleased | offset: " +
						// offset, Log.FINEST);
						Set offsets = new HashSet();
						long maxValue = TimeStampsEditor.this.testTemporalStamps.getEndTime().getTime();
						long minValue = TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
						long baseMinValue = minValue;
						long baseMaxValue = maxValue;
						if (TimeStampsEditor.this.selectedItems != null) {
							long startTime = TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
							for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
								TestTimeItem testTimeItem = (TestTimeItem) it.next();
								Long ms = (Long) testTimeItem.object;
								long oldTime = ms.longValue();
								if (oldTime + offset + startTime > maxValue) {
									maxValue = oldTime + offset + startTime;
								}
								if (oldTime + offset + startTime < minValue) {
									minValue = oldTime + offset + startTime;
								}
								offsets.add(ms);
							}
						}

						if (baseMinValue != minValue) {
							TimeStampsEditor.this.testTemporalStamps.setStartTime(new Date(minValue));
						}
						if (baseMaxValue != maxValue) {
							TimeStampsEditor.this.testTemporalStamps.setEndTime(new Date(maxValue));
						}

						// Log.debugMessage(".mouseReleased |
						// testTemporalStamps.getStartTime:" +
						// testTemporalStamps.getStartTime(), Log.FINEST);
						// Log.debugMessage(".mouseReleased |
						// testTemporalStamps.getEndTime:" +
						// testTemporalStamps.getEndTime(), Log.FINEST);

						try {
							intervalsTemporalPattern.moveIntervalItems(offsets, offset);							
							TimeStampsEditor.this.undoMenuItem.setEnabled(true);
							TimeStampsEditor.this.selectedItems.clear();
						} catch (IllegalDataException e1) {							
							TimeStampsEditor.this.undoMenuItem.setEnabled(false);
							TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
							SchedulerModel.showErrorMessage(TimeStampsEditor.this, e1);
						}

						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
						TimeStampsEditor.this.updateMenuItemsState();
					}

					TimeStampsEditor.this.startPoint = null;
					TimeStampsEditor.this.previousPoint = null;
					TimeStampsEditor.this.currentPoint = null;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseDragged(MouseEvent e) {
				TimeStampsEditor.this.currentPoint = e.getPoint();
				if (TimeStampsEditor.this.startPoint == null) {
					TimeStampsEditor.this.startPoint = e.getPoint();
					TimeStampsEditor.this.previousPoint = TimeStampsEditor.this.startPoint;
					return;
				}
				if (TimeStampsEditor.this.previousPoint != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
//					SwingUtilities.invokeLater(new Runnable() {
//
//						public void run() {
							int dx = (TimeStampsEditor.this.currentPoint.x - TimeStampsEditor.this.previousPoint.x);
							
							if (dx == 0)
								return;
							
							for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
								TestTimeItem testTimeItem = (TestTimeItem) it.next();
								testTimeItem.x += dx;
							}
							TimeStampsEditor.this.repaint();
							TimeStampsEditor.this.revalidate();
							TimeStampsEditor.this.previousPoint = TimeStampsEditor.this.currentPoint;
						}
//					});
//				}
			}
		});
	}
	
	void copyToBuffer() {
		this.offsetDurationBuffer.clear();
		this.offsetIdBuffer.clear();
		
		if (this.selectedItems == null || this.selectedItems.isEmpty())
			return;
		
		long firstDelta = 0;
		boolean initFirstDelta = false;
		
		for (Iterator it = this.selectedItems.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.next();
			Long offset = (Long) testTimeItem.object;
			if (!initFirstDelta) {
				firstDelta = offset.longValue();
				initFirstDelta = true;
			}

			Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(offset);
			Long duration = (Long) this.intervalsDuration.get(offset);

			Long newOffset = new Long(offset.longValue() - firstDelta);

			this.offsetIdBuffer.put(newOffset, temporalPatternId);
			this.offsetDurationBuffer.put(newOffset, duration);
		}
	}
	
	private void createPopupMenu() {
		this.popupMenu = new JPopupMenu();
		this.undoMenuItem = new JMenuItem(LangModelSchedule.getString("Undo"));
		this.undoMenuItem.setEnabled(false);
		this.undoMenuItem.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				JMenuItem menuItem =(JMenuItem) e.getSource();
				if (TimeStampsEditor.this.testTemporalStamps != null) {
					TimeStampsEditor.this.testTemporalStamps.undo();
					if (menuItem.getText().equals(LangModelSchedule.getString("Undo"))) {
						menuItem.setText(LangModelSchedule.getString("Redo"));
					} else {
						menuItem.setText(LangModelSchedule.getString("Undo"));
					}
					TimeStampsEditor.this.updateMenuItemsState();
					TimeStampsEditor.this.refreshTimeItems();
					TimeStampsEditor.this.repaint();					
				}
			}
		});		
		this.popupMenu.add(this.undoMenuItem);
		
		this.popupMenu.addSeparator();
		this.cutMenuItem = new JMenuItem(LangModelSchedule.getString("Cut"));
		this.cutMenuItem.setEnabled(false);
		this.cutMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null) {					
					TimeStampsEditor.this.copyToBuffer();
					

					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
						Set set = new HashSet();
						for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							Long ms = (Long) testTimeItem.object;
							set.add(ms);
						}
						intervalsTemporalPattern.removeIntervalItems(set);
						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
					}
					TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
					TimeStampsEditor.this.undoMenuItem.setEnabled(true);
					TimeStampsEditor.this.pasteMenuItem.setEnabled(!TimeStampsEditor.this.offsetIdBuffer.isEmpty());
				
				}
			}
		});
		this.popupMenu.add(this.cutMenuItem);
		
		this.copyMenuItem = new JMenuItem(LangModelSchedule.getString("Copy"));
		this.copyMenuItem.setEnabled(false);
		this.copyMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null) {
					TimeStampsEditor.this.copyToBuffer();
					TimeStampsEditor.this.pasteMenuItem.setEnabled(!TimeStampsEditor.this.offsetIdBuffer.isEmpty());
				}
			}
		});
		this.popupMenu.add(this.copyMenuItem);
		
		this.pasteMenuItem = new JMenuItem(LangModelSchedule.getString("Paste"));
		this.pasteMenuItem.setEnabled(false);
		this.pasteMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.offsetIdBuffer != null
						&& !TimeStampsEditor.this.offsetIdBuffer.isEmpty()) {

					// Log.debugMessage("TimeStampsEditor.createPopupMenu |
					// TimeStampsEditor.this.popupMenu.getX(): " +
					// TimeStampsEditor.this.popupRelativeX, Log.FINEST);
					// Log.debugMessage(".actionPerformed | date is " + new
					// Date((long) (TimeStampsEditor.this.start +
					// (TimeStampsEditor.this.popupRelativeX - PlanPanel.MARGIN
					// / 2)
					// / TimeStampsEditor.this.scale)), Log.FINEST);
					long pointTime = (long) (TimeStampsEditor.this.start + (TimeStampsEditor.this.popupRelativeX - PlanPanel.MARGIN / 2)
							/ TimeStampsEditor.this.scale)
							- TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();

					 Log.debugMessage(".actionPerformed | pointTime is " +
					 pointTime, Log.FINEST);

					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;

						long localStartTime = 0;
						long localStartTime_ = TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
						long localEndTime_ = TimeStampsEditor.this.testTemporalStamps.getEndTime().getTime();
						long localEndTime = localEndTime_ - localStartTime_;

						Map map = new HashMap();
						Map mapDuration = new HashMap();
						for (Iterator it = TimeStampsEditor.this.offsetIdBuffer.keySet().iterator(); it.hasNext();) {
							Long ms = (Long) it.next();
//							Log.debugMessage(".actionPerformed | ms " + ms, Log.FINEST);
							long time = ms.longValue() + pointTime;
//							Log.debugMessage(".actionPerformed | time " + time, Log.FINEST);
							if (time < localStartTime) {
								localStartTime = time;
							}
							if (time > localEndTime) {
								localEndTime = time;
							}

							Long newMs = new Long(time);

							map.put(newMs, TimeStampsEditor.this.offsetIdBuffer.get(ms));
							mapDuration.put(newMs, TimeStampsEditor.this.offsetDurationBuffer.get(ms));
						}

						try {
							intervalsTemporalPattern.addIntervalItems(map, mapDuration);
							if (localStartTime < 0) {
								TimeStampsEditor.this.testTemporalStamps.setStartTime(new Date(localStartTime
										+ localStartTime_));
							}

							if (localEndTime > localEndTime_ - localStartTime_) {
								TimeStampsEditor.this.testTemporalStamps.setEndTime(new Date(localStartTime_
										+ localEndTime));

							}

							TimeStampsEditor.this.undoMenuItem.setEnabled(true);
							TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
							TimeStampsEditor.this.refreshTimeItems();
							TimeStampsEditor.this.repaint();
						} catch (IllegalDataException ide) {
							SchedulerModel.showErrorMessage(TimeStampsEditor.this, ide);
						}
					}
				}
			}
		});
		this.popupMenu.add(this.pasteMenuItem);

		this.deleteMenuItem = new JMenuItem(LangModelSchedule.getString("Delete") + "...");
		this.deleteMenuItem.setEnabled(false);
		this.deleteMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
						Set set = new HashSet();
						for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							Long ms = (Long) testTimeItem.object;
							set.add(ms);
						}
						TimeStampsEditor.this.selectedItems.clear();
						intervalsTemporalPattern.removeIntervalItems(set);
						TimeStampsEditor.this.undoMenuItem.setEnabled(true);
						TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
					}
				}

			}
		});
		this.popupMenu.add(this.deleteMenuItem);
		
		this.popupMenu.addSeparator();
		
		this.joinMenuItem = new JMenuItem(LangModelSchedule.getString("Join"));
		this.joinMenuItem.setEnabled(false);
		this.joinMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
						SortedSet set = new TreeSet();
						for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							Long ms = (Long) testTimeItem.object;
							set.add(ms);
						}
						TimeStampsEditor.this.selectedItems.clear();
						try {
							intervalsTemporalPattern.joinIntervalItems(set);
						} catch (CreateObjectException e1) {
							SchedulerModel.showErrorMessage(TimeStampsEditor.this, e1);
						}
						TimeStampsEditor.this.undoMenuItem.setEnabled(true);
						TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
					}
				}

			}
		});
		this.popupMenu.add(this.joinMenuItem);
		
		this.disjoinMenuItem = new JMenuItem(LangModelSchedule.getString("Disjoin"));
		this.disjoinMenuItem.setEnabled(false);
		this.disjoinMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
						SortedSet set = new TreeSet();
						for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							Long ms = (Long) testTimeItem.object;
							set.add(ms);
						}
						TimeStampsEditor.this.selectedItems.clear();
						try {
							intervalsTemporalPattern.disjoinIntervalItems(set);							
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TimeStampsEditor.this, e1);
						}
						TimeStampsEditor.this.undoMenuItem.setEnabled(true);
						TimeStampsEditor.this.undoMenuItem.setText(LangModelSchedule.getString("Undo"));
						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
					}
				}

			}
		});
		this.popupMenu.add(this.disjoinMenuItem);
		
//		JMenuItem infoItem = new JMenuItem(LangModelSchedule.getString("Info"));
//		infoItem.addActionListener(new ActionListener() {
//
//			public void actionPerformed(ActionEvent e) {
//				if (TimeStampsEditor.this.testTemporalStamps != null) {
//					
//					String string = TimeStampsEditor.this.testTemporalStamps.getStartTime() + ", " + TimeStampsEditor.this.testTemporalStamps.getEndTime();
//					JOptionPane.showMessageDialog(TimeStampsEditor.this, string);
//					
//				}
//
//			}
//		});
//		this.popupMenu.add(infoItem);

	}
	
	
	void refreshTimeItems() {
		this.timeItems.clear();
		if (this.testTemporalStamps == null) {
			return;
		}
		Color selectedColor = SchedulerModel.COLOR_SCHEDULED;
		Color unselectedColor = SchedulerModel.COLOR_UNRECOGNIZED;
		
		AbstractTemporalPattern temporalPattern = this.testTemporalStamps.getTemporalPattern();
		if (temporalPattern == null)
			return;
		
		//SortedSet times = temporalPattern.getTimes(this.testTemporalStamps.getStartTime(), this.testTemporalStamps.getEndTime());
		if (temporalPattern instanceof IntervalsTemporalPattern) {
			long startTime = this.testTemporalStamps.getStartTime().getTime();
			IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
			this.intervalsAbstractTemporalPatternMap = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
			this.intervalsDuration = intervalsTemporalPattern.getIntervalsDuration();
			for (Iterator it = this.intervalsAbstractTemporalPatternMap.keySet().iterator(); it.hasNext();) {
				Long offset = (Long) it.next();
				long time = offset.longValue() + startTime;

				int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (time - this.start));
				//int w = this.minimalWidth;

				TestTimeItem testTimeItem = new TestTimeItem();
				testTimeItem.x = x;
				Long duration = (Long)this.intervalsDuration.get(offset);
				Log.debugMessage("TimeStampsEditor.refreshTimeItems | offset:" + offset + ", duration:" + duration, Log.FINEST);
				testTimeItem.width = duration != null ? (int) (this.scale * ((Long)this.intervalsDuration.get(offset)).longValue()) : this.minimalWidth;
				if (testTimeItem.width < this.minimalWidth) {
					testTimeItem.width = this.minimalWidth;
				}
				testTimeItem.object = offset;
				testTimeItem.selectedColor = selectedColor;
				testTimeItem.color = unselectedColor;

				this.timeItems.add(testTimeItem);
			}

			TestTimeItem prevItem = null;
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (prevItem != null && (testTimeItem.x - (prevItem.x + prevItem.width)) < 0) {
					it.remove();
					// Log.debugMessage("TimeStampsEditor.refreshTimeItems |
					// remove testTimeItem " + testTimeItem.object, Log.FINEST);
				} else {
					prevItem = testTimeItem;
				}
				// Log.debugMessage("TimeStampsEditor.refreshTimeItems | x:" +
				// testTimeItem.x +", " + testTimeItem.object, Log.FINEST);

			}
		} else {
			assert false : temporalPattern.getClass().getName() + " isn't support as root temporal pattern";
		}
	}
	
	public String getToolTipText(MouseEvent event) {
		int x = event.getX();
		if (!this.timeItems.isEmpty()) {
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
					Long offset = (Long) testTimeItem.object;
					String dateString;
					{
						SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
						Date date = new Date(this.testTemporalStamps.getStartTime().getTime() + offset.longValue());
						dateString = sdf.format(date);
					}
					Identifier temporalPatternId = (Identifier) this.intervalsAbstractTemporalPatternMap.get(offset);
					short major = temporalPatternId.getMajor();
					switch (major) {
						case ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE: {
							try {
								PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) MeasurementStorableObjectPool
										.getStorableObject(temporalPatternId, true);
								long period = periodicalTemporalPattern.getPeriod();
								long day = period / TimeParametersFrame.TimeParametersPanel.DAY_LONG;
								period = period % TimeParametersFrame.TimeParametersPanel.DAY_LONG;
								long hours = period / TimeParametersFrame.TimeParametersPanel.HOUR_LONG;
								period = period % TimeParametersFrame.TimeParametersPanel.HOUR_LONG;
								long minutes = period / TimeParametersFrame.TimeParametersPanel.MINUTE_LONG;
								period = period % TimeParametersFrame.TimeParametersPanel.MINUTE_LONG;
								return LangModelSchedule.getString("Start") + ":" + dateString + ", "
										+ LangModelSchedule.getString("Periodical sequence") + ", "
										+ LangModelSchedule.getString("period") + ":"
										+ (day > 0 ? "" + day + LangModelSchedule.getString("days") + " " : "")
										+ (hours > 0 ? "" + hours + LangModelSchedule.getString("hours") + " " : "")
										+ (minutes > 0 ? "" + minutes + LangModelSchedule.getString("minutes") : "");
							} catch (ApplicationException e) {
								SchedulerModel.showErrorMessage(this, e);
							}
						}
							break;
						case ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE: {
							return LangModelSchedule.getString("Start") + ":" + dateString + ", "
									+ LangModelSchedule.getString("Complex sequence");
						}

						default:
							return dateString;
					}
				}
			}
		}
		return this.title;

	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = this.titleHeight / 2 + 4;
		int h = this.getHeight() - (this.titleHeight / 2 + 4) - 2;
		
		if (!this.timeItems.isEmpty() && super.scale != 0.0) {
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h, this.selectedItems.contains(testTimeItem) ?  testTimeItem.selectedColor : testTimeItem.color);
			}
		}
	}

	public TestTemporalStamps getTestTemporalStamps() {
		return this.testTemporalStamps;
	}
	
	public void setTestTemporalStamps(TestTemporalStamps testTemporalStamps) {
		this.testTemporalStamps = testTemporalStamps;
		this.refreshTimeItems();
	}	
	
	
//	public void operationPerformed(OperationEvent e) {
//		// TODO Auto-generated method stub
//		
//	}
	
}
