/*-
 * $Id: TimeStampsEditor.java,v 1.1 2005/04/27 15:26:37 bob Exp $
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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.UI.TestLine.TestTimeItem;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.TestTemporalStamps;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 15:26:37 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public class TimeStampsEditor extends TimeLine {

	private SchedulerModel	schedulerModel;

	TestTemporalStamps testTemporalStamps; 
	//SortedSet timeItems = new TreeSet();
	
//	List timeItems = new LinkedList(); 
	List selectedItems = new LinkedList();
	SortedSet bufferedItems = new TreeSet();
	
	JPopupMenu popupMenu;
	int popupRelativeX;
	
	Point startPoint;
	Point currentPoint;
	Point previousPoint;
	
	public TimeStampsEditor(ApplicationContext aContext, String title) {
		this.title = title;
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();		
		this.createMouseListener();
		this.setToolTipText("");
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
									break;
								}								
								TimeStampsEditor.this.selectedItems.add(testTimeItem);
								TimeStampsEditor.this.repaint();
								break;
							}
						}
					}
					
				} else if (SwingUtilities.isRightMouseButton(e)) {
					if (TimeStampsEditor.this.popupMenu == null) {
						createPopupMenu();
					}
					popupRelativeX = x;
					TimeStampsEditor.this.popupMenu.show(TimeStampsEditor.this, x, y);
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps.getTemporalPattern();
				if (temporalPattern instanceof IntervalsTemporalPattern) {
					IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
					if (TimeStampsEditor.this.currentPoint != null && TimeStampsEditor.this.startPoint != null) {
//						Log.debugMessage(".mouseReleased | testTemporalStamps.getStartTime:" + testTemporalStamps.getStartTime(), Log.FINEST);
//						Log.debugMessage(".mouseReleased | testTemporalStamps.getEndTime:" + testTemporalStamps.getEndTime(), Log.FINEST);

						long offset = (long) ((TimeStampsEditor.this.currentPoint.x - TimeStampsEditor.this.startPoint.x) / TimeStampsEditor.this.scale);
//						Log.debugMessage(".mouseReleased | offset: " + offset, Log.FINEST);
						Set offsets = new HashSet();
						long maxValue = TimeStampsEditor.this.testTemporalStamps.getEndTime().getTime();
						long minValue = TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
						long baseMinValue = minValue;
						long baseMaxValue = maxValue;
						if (TimeStampsEditor.this.selectedItems != null) {
							long startTime = TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
							for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
								TestTimeItem testTimeItem = (TestTimeItem) it.next();
								Date ms = (Date) testTimeItem.object;
								long oldTime = ms.getTime();
								if (oldTime + offset > maxValue) {
									maxValue = oldTime + offset;
								}
								if (oldTime + offset < minValue) {
									minValue = oldTime + offset;
								}
								offsets.add(new Long(oldTime - startTime));
							}
						}

						if (baseMinValue != minValue) {
							TimeStampsEditor.this.testTemporalStamps.setStartTime(new Date(minValue));
						}
						if (baseMaxValue != maxValue) {
							TimeStampsEditor.this.testTemporalStamps.setEndTime(new Date(maxValue));
						}
						
//						Log.debugMessage(".mouseReleased | testTemporalStamps.getStartTime:" + testTemporalStamps.getStartTime(), Log.FINEST);
//						Log.debugMessage(".mouseReleased | testTemporalStamps.getEndTime:" + testTemporalStamps.getEndTime(), Log.FINEST);
						
						intervalsTemporalPattern.moveIntervalItems(offsets, offset);
						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.selectedItems.clear();
						TimeStampsEditor.this.repaint();
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
	
	void createPopupMenu() {
		this.popupMenu = new JPopupMenu();

		JMenuItem copyMenuItem = new JMenuItem(LangModelSchedule.getString("Copy"));
		copyMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
//					if (TimeStampsEditor.this.bufferedItems == null) {
//						TimeStampsEditor.this.bufferedItems = new ArrayList(TimeStampsEditor.this.selectedItems);
//					} else 
					{
						TimeStampsEditor.this.bufferedItems.clear();
						TimeStampsEditor.this.bufferedItems.addAll(TimeStampsEditor.this.selectedItems);
					}
				}
			}
		});
		this.popupMenu.add(copyMenuItem);
		
		JMenuItem pasteMenuItem = new JMenuItem(LangModelSchedule.getString("Paste"));
		pasteMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.bufferedItems != null
						&& !TimeStampsEditor.this.bufferedItems.isEmpty()) {

					TestTimeItem firstTestTimeItem = null;
					
//					Log.debugMessage("TimeStampsEditor.createPopupMenu | TimeStampsEditor.this.popupMenu.getX(): " + TimeStampsEditor.this.popupRelativeX, Log.FINEST);
//					Log.debugMessage(".actionPerformed | date is " + new Date((long) (TimeStampsEditor.this.start + (TimeStampsEditor.this.popupRelativeX - PlanPanel.MARGIN / 2)
//							/ TimeStampsEditor.this.scale)), Log.FINEST);
					long pointTime = (long) (TimeStampsEditor.this.start + (TimeStampsEditor.this.popupRelativeX - PlanPanel.MARGIN / 2)
							/ TimeStampsEditor.this.scale) - TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime();
					
//					Log.debugMessage(".actionPerformed | pointTime is " + pointTime, Log.FINEST);

					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;

						long firstTime = 0;
						for (Iterator it = TimeStampsEditor.this.bufferedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							if (firstTestTimeItem == null) {
								firstTestTimeItem = testTimeItem;
								firstTime = ((Date) firstTestTimeItem.object).getTime();
							}
							long time = ((Date) testTimeItem.object).getTime() - firstTime;
//							Log.debugMessage(".actionPerformed | time " + time, Log.FINEST);
							intervalsTemporalPattern.addIntervalItem(time + pointTime, Identifier.VOID_IDENTIFIER);
						}

						TimeStampsEditor.this.refreshTimeItems();
						TimeStampsEditor.this.repaint();
					}
				}
			}
		});
		this.popupMenu.add(pasteMenuItem);

		JMenuItem deleteSelectedItem = new JMenuItem(LangModelSchedule.getString("Delete") + "...");
		deleteSelectedItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (TimeStampsEditor.this.testTemporalStamps != null && TimeStampsEditor.this.selectedItems != null
						&& !TimeStampsEditor.this.selectedItems.isEmpty()) {
					AbstractTemporalPattern temporalPattern = TimeStampsEditor.this.testTemporalStamps
							.getTemporalPattern();
					if (temporalPattern instanceof IntervalsTemporalPattern) {
						IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) temporalPattern;
						for (Iterator it = TimeStampsEditor.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							Date ms = (Date) testTimeItem.object;
							// Log.debugMessage("TimeStampsEditor.createPopupMenu
							// | ms " + ms, Log.FINEST);
							intervalsTemporalPattern.removeIntervalItem(ms.getTime()
									- TimeStampsEditor.this.testTemporalStamps.getStartTime().getTime());
							TimeStampsEditor.this.refreshTimeItems();
							TimeStampsEditor.this.repaint();
						}
					}
				}

			}
		});
		this.popupMenu.add(deleteSelectedItem);

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
		
		SortedSet times = temporalPattern.getTimes(this.testTemporalStamps.getStartTime(), this.testTemporalStamps.getEndTime());
		
		for (Iterator it = times.iterator(); it.hasNext();) {
			Date date = (Date) it.next();
			long time= date.getTime();

			int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (time - this.start));
			int w = this.minimalWidth;

			TestTimeItem testTimeItem = new TestTimeItem();
			testTimeItem.x = x;
			testTimeItem.width = w;
			testTimeItem.object = date;
			testTimeItem.selectedColor = selectedColor;
			testTimeItem.color = unselectedColor;

			this.timeItems.add(testTimeItem);
		}
		
		
		TestTimeItem prevItem = null;
		for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem  = (TestTimeItem) it.next();
			if (prevItem != null && (testTimeItem.x - (prevItem.x + prevItem.width))<0) {
				it.remove();
//				Log.debugMessage("TimeStampsEditor.refreshTimeItems | remove testTimeItem " + testTimeItem.object, Log.FINEST);
			} else {
				prevItem = testTimeItem;
			}
//			Log.debugMessage("TimeStampsEditor.refreshTimeItems | x:" + testTimeItem.x +", " + testTimeItem.object, Log.FINEST);
			
		}
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
	
}
