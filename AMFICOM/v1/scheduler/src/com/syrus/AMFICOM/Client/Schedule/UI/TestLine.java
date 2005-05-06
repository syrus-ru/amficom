
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestEditor;
import com.syrus.AMFICOM.Client.Schedule.TestsEditor;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

public class TestLine extends TimeLine implements TestsEditor, TestEditor {

	private class TestTimeLine implements Comparable {

		protected long		duration;
		protected boolean	haveMeasurement;

		protected long		startTime;
		protected Test		test;

		public int compareTo(Object o) {
			TestTimeLine testTimeLine = (TestTimeLine) o;
			return (int) (this.startTime - testTimeLine.startTime);
		}
	}

	static class TestTimeItem implements Comparable {

		int		x;
		int		width;

		Color	color;

		Color	selectedColor;

		Object	object;

		public int compareTo(Object o) {
			TestTimeItem item = (TestTimeItem) o;
			return (this.x - item.x);
		}

		public boolean equals(Object obj) {
			boolean result = super.equals(obj);
			if (result)
				return result;
			if (!(obj instanceof TestLine.TestTimeItem)) { return false; }
			TestTimeItem testTimeItem = (TestTimeItem) obj;
			boolean b1 = (testTimeItem.object == this.object && this.object == null);
			boolean b2 = (testTimeItem.object != null && testTimeItem.object.equals(this.object));
			return b1 || b2;
		}

		public int hashCode() {
			return this.object == null ? 0 : this.object.hashCode();
		}
	}

	Collection					tests					= new LinkedList();

	Dispatcher					dispatcher;

	boolean						flash					= false;
	SchedulerModel				schedulerModel;
	Test						selectedTest;

	Collection					unsavedTests			= new LinkedList();
	SortedSet					unsavedTestTimeItems	= new TreeSet();

	Map							measurements			= new HashMap();
	private Identifier			monitoredElementId;

	private static final long	ONE_MINUTE				= 60L * 1000L;

	public TestLine(ApplicationContext aContext, String title, Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestsEditor(this);
		this.schedulerModel.addTestEditor(this);
		this.createTestLineMouseListener();
		this.title = title;
		this.monitoredElementId = monitoredElementId;
		this.acquireTests();
	}

	private void createTestLineMouseListener() {
		this.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if (SwingUtilities.isLeftMouseButton(e)) {
					TestLine.this.selectedTest = null;
					if (!TestLine.this.timeItems.isEmpty()) {
						selectTest(x, y, TestLine.this.timeItems);
					}
					if (TestLine.this.selectedTest == null && !TestLine.this.unsavedTestTimeItems.isEmpty()) {
						selectTest(x, y, TestLine.this.unsavedTestTimeItems);

					}
				}
			}
		});
	}

	void selectTest(int x,
					int y,
					Collection collection) {
		for (Iterator it = collection.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.next();
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
				try {
					this.schedulerModel.setSelectedTest((Test) testTimeItem.object);
				} catch (ApplicationException e) {
					SchedulerModel.showErrorMessage(this, e);
				}
				break;
			}
		}
	}

	public Rectangle getVisibleRectangle() {
		Rectangle rectangle = null;
		if (this.selectedTest != null) {
			List testTimeLineList = (List) this.measurements.get(this.selectedTest.getId());
			TestTimeLine testTimeLine = (TestTimeLine) testTimeLineList.get(0);
			int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
			int en = PlanPanel.MARGIN / 2
					+ (int) (this.scale * (testTimeLine.startTime + testTimeLine.duration - this.start));
			rectangle = new Rectangle(x - PlanPanel.MARGIN / 2, 0, en - x, this.getHeight()
					- (this.titleHeight / 2 + 4) - 2);
		}
		return rectangle;
	}

	private void paintFlash(Graphics g) {

		if (g != null) {
			this.flash = !this.flash;
			int y = this.titleHeight / 2  + 4;
			int h = this.getHeight() - y - 2;

			for (Iterator it = this.unsavedTestTimeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				Test test = (Test) testTimeItem.object;
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h, this.flash
						? (((this.selectedTest == null) || (!this.selectedTest.getId().equals(test.getId())))
								? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_SCHEDULED_SELECTED)
						: SchedulerModel.COLOR_UNRECOGNIZED);
			}

		}
	}

	public void updateTest() {
		Test test = this.schedulerModel.getSelectedTest();
		if (test == null) {
			this.selectedTest = test;
		} else if (((this.selectedTest != null && !this.selectedTest.getId().equals(test.getId())) || this.selectedTest == null)
				&& this.tests.contains(test)) {
			this.selectedTest = test;
		} else {
			this.selectedTest = null;
		}

		Log.debugMessage("TestLine.updateTest | this.selectedTest is "
				+ (this.selectedTest == null ? "null" : this.selectedTest.getId().toString()), Log.FINEST);
		this.repaint();
	}

	public void updateTests() {
		this.acquireTests();
		this.updateTest();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = this.titleHeight / 2 + 4;
		int h = this.getHeight() - y - 2;

		if (!this.timeItems.isEmpty() && super.scale > 0.0) {
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();				
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h, (this.selectedTest == null)
						|| (!this.selectedTest.getId().equals(((Test) testTimeItem.object).getId()))
						? testTimeItem.color : testTimeItem.selectedColor);
			}
		}
		this.paintFlash(g);
	}

	private void acquireTests() {
		Collection tests1 = this.schedulerModel.getTests();
		this.tests.clear();
		this.unsavedTests.clear();
		this.measurements.clear();
		for (Iterator it = tests1.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			if (test.getMonitoredElement().getId().equals(this.monitoredElementId)) {
				this.tests.add(test);
				if (test.isChanged()) {
					this.unsavedTests.add(test);
				} else {
					LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																					test.getId(),
																					ObjectEntities.MEASUREMENT_ENTITY_CODE);
					try {
						Set testMeasurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(
							linkedIdsCondition, true);
						List measurementTestList = new LinkedList();
						if (!testMeasurements.isEmpty()) {
							for (Iterator iter = testMeasurements.iterator(); iter.hasNext();) {
								Measurement measurement = (Measurement) iter.next();
								TestTimeLine testTimeLine = new TestTimeLine();
								testTimeLine.test = test;
								testTimeLine.startTime = measurement.getStartTime().getTime();
								testTimeLine.duration = measurement.getDuration();
								testTimeLine.haveMeasurement = true;
								measurementTestList.add(testTimeLine);
							}
							this.measurements.put(test.getId(), measurementTestList);
						} else {
							this.measurements.put(test.getId(), null);
						}

					} catch (ApplicationException e) {
						SchedulerModel.showErrorMessage(this, e);
					}

				}

				// ///
				List measurementTestList = (List) this.measurements.get(test.getId());
				if (measurementTestList == null) {
					measurementTestList = new LinkedList();
					TestTimeLine testTimeLine = new TestTimeLine();
					testTimeLine.test = test;
					testTimeLine.startTime = test.getStartTime().getTime();
					testTimeLine.duration = 0;
					testTimeLine.haveMeasurement = false;
					measurementTestList.add(testTimeLine);
					this.measurements.put(test.getId(), measurementTestList);
				}
				try {
					MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
							.getStorableObject((Identifier) test.getMeasurementSetupIds().iterator().next(), true);
					switch (test.getTemporalType().value()) {
						case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
							Set times = ((AbstractTemporalPattern) MeasurementStorableObjectPool.getStorableObject(test
									.getTemporalPatternId(), true)).getTimes(test.getStartTime(), test.getEndTime());
							List addMeasurementTestList = new LinkedList();
							for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
								Date date = (Date) timeIt.next();
								long time = date.getTime();
								boolean found = false;
								for (Iterator iter = measurementTestList.iterator(); iter.hasNext();) {
									TestTimeLine testTimeLine = (TestTimeLine) iter.next();
									if (Math.abs(testTimeLine.startTime - time) < ONE_MINUTE) {
										found = true;
										break;
									}
								}
								if (!found) {
									TestTimeLine testTimeLine = new TestTimeLine();
									testTimeLine.test = test;
									testTimeLine.startTime = time;
									testTimeLine.duration = measurementSetup.getMeasurementDuration();
									testTimeLine.haveMeasurement = false;
									addMeasurementTestList.add(testTimeLine);
								}
							}
							measurementTestList.addAll(addMeasurementTestList);
							break;
						default:
							TestTimeLine testTimeLine = new TestTimeLine();
							testTimeLine.test = test;
							testTimeLine.startTime = test.getStartTime().getTime();
							testTimeLine.duration = 0;
							measurementTestList.add(testTimeLine);
							break;

					}
					Collections.sort(measurementTestList);
				} catch (ApplicationException ae) {
					SchedulerModel.showErrorMessage(this, ae);
				}
				// ///

			}
		}

		this.refreshTimeItems();
	}

	void refreshTimeItems() {
		this.timeItems.clear();
		for (Iterator iterator = this.tests.iterator(); iterator.hasNext();) {
			Test test = (Test) iterator.next();

			int i = 0;

			List testTimeLineList = (List) this.measurements.get(test.getId());
			if (testTimeLineList == null)
				return;

			Color selectedColor = null;
			Color unselectedColor = null;
			for (Iterator it = testTimeLineList.iterator(); it.hasNext(); i++) {
				TestTimeLine testTimeLine = (TestTimeLine) it.next();

				int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
				int en = PlanPanel.MARGIN / 2
						+ (int) (this.scale * (testTimeLine.startTime + testTimeLine.duration - this.start));
				int w = en - x + 1;

				w = w > this.minimalWidth ? w : this.minimalWidth;

				if (testTimeLine.haveMeasurement) {
					selectedColor = SchedulerModel.COLOR_COMPLETED_SELECTED;
					unselectedColor = SchedulerModel.COLOR_COMPLETED;
				} else {
					selectedColor = SchedulerModel.getColor(test.getStatus(), true);
					unselectedColor = SchedulerModel.getColor(test.getStatus(), false);
				}

				TestTimeItem testTimeItem = new TestTimeItem();
				
				testTimeItem.x = x;
				testTimeItem.width = w;
				testTimeItem.object = test;
				testTimeItem.selectedColor = selectedColor;
				testTimeItem.color = unselectedColor;

				this.timeItems.add(testTimeItem);
			}

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
		
		this.unsavedTestTimeItems.clear();
		for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.next();
			if (((Test) testTimeItem.object).isChanged()) {
				it.remove();
				this.unsavedTestTimeItems.add(testTimeItem);
			}
		}
		
//		Log.debugMessage("TestLine.refreshTimeItems | timeItems " + timeItems.size(), Log.FINEST);
//		Log.debugMessage("TestLine.refreshTimeItems | unsavedTestTimeItems " + unsavedTestTimeItems.size(), Log.FINEST);
	}

}
