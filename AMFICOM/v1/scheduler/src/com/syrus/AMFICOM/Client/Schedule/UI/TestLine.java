
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.general.UIStorage;
import com.syrus.AMFICOM.client.UI.ProcessingDialogDummy;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestView;
import com.syrus.AMFICOM.measurement.TestViewAdapter;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.IdlMeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;

final class TestLine extends TimeLine {

	private static final long	serialVersionUID	= 3978424736810416184L;

	private class TestTimeLine implements Comparable<TestTimeLine> {
		protected long duration;
		
		Color color;
		Color selectedColor;

		protected String title;
		protected Date date;

		protected long startTime;
		protected Identifier testId;

		public int compareTo(final TestTimeLine testTimeLine) {
			return (int) (this.startTime - testTimeLine.startTime);
		}
	}

	static class TestTimeItem implements Comparable<TestTimeItem> {
		int x;
		int width;

		TestTimeLine testTimeLine;

		public int compareTo(final TestTimeItem item) {
//			final int diff0 = this.x + this.width - item.x;
//			if (diff0 < 0) {
//				return diff0;				
//			}
//			
//			final int diff1 = this.x - (item.x + item.width);
//			if (diff1 > 0) {
//				return diff1;
//			}
//			
//			if (!this.testTimeLine.testId.equals(item.testTimeLine.testId)) {			
				final int dx = (this.x - item.x);
//				if (dx != 0) {
//					return dx;
//				}
//				assert Log.debugMessage(this.testTimeLine.testId + " > " + item.testTimeLine.testId, Log.DEBUGLEVEL03);
//				assert Log.debugMessage(this.testTimeLine.date + " > " + item.testTimeLine.date, Log.DEBUGLEVEL03);
//				return -this.testTimeLine.date.compareTo(item.testTimeLine.date);
				if (dx == 1 || dx == -1) {
					return 0;
				}
				return dx;
//			}
//			
//			return 0;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}

			if (obj instanceof TestLine.TestTimeItem) {
				final TestTimeItem testTimeItem = (TestTimeItem) obj;
				return testTimeItem.testTimeLine.testId == this.testTimeLine.testId
						|| testTimeItem.testTimeLine.testId != null
							&& testTimeItem.testTimeLine.testId.equals(
									this.testTimeLine.testId);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.testTimeLine.testId == null ?
					0 : this.testTimeLine.testId.hashCode();
		}

		public void setWidth(final int width) {			
			this.width = width;
		}

	}

	final Set<Identifier> testIds;

	Dispatcher dispatcher;

	boolean flash = false;
	SchedulerModel schedulerModel;
	// Set selectedTests;
	Set<Identifier> selectedTestIds;

	final SortedSet<TestTimeItem> unsavedTestTimeItems = 
		Collections.synchronizedSortedSet(new TreeSet<TestTimeItem>());

	final Map<Identifier, List<TestTimeLine>> measurements = 
		new HashMap<Identifier, List<TestTimeLine>>();
	private Identifier monitoredElementId;

	// private static final long ONE_MINUTE = 60L * 1000L;

	final SortedSet<TestTimeItem> selectedItems = new TreeSet<TestTimeItem>();

//	SortedMap offsetIdBuffer = new TreeMap();
//	SortedMap offsetDurationBuffer = new TreeMap();

	JPopupMenu			popupMenu;
	int					popupRelativeX;

	Point				startPoint;
	Point				currentPoint;
	Point				previousPoint;

	private int lastX = -1;

	protected volatile boolean skip = false;

	private Comparator<Test>	statusTestComparator;

	public TestLine(final ApplicationContext aContext, 
			final String title, final 
			Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.createMouseListener();
		this.title = title;
		this.monitoredElementId = monitoredElementId;
		this.testIds = new HashSet<Identifier>();

		this.dispatcher = aContext.getDispatcher();
		this.setToolTipText("");
	}

	final void unselect() {
		this.selectedItems.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		this.schedulerModel.unselectTests(this);
	}

	final boolean selectTest(final int x, 
			final SortedSet<TestTimeItem> testTimeItems,
			final boolean unselect) {
		boolean selected = false;

		synchronized (this) {
		for (final TestTimeItem testTimeItem : testTimeItems) {
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
				if (this.selectedTestIds == null) {
					this.selectedTestIds = new HashSet<Identifier>();
				}

				boolean selectTheSameTest = false;

				for (final TestTimeItem item : this.selectedItems) {
					if (item.testTimeLine.testId.equals(
							testTimeItem.testTimeLine.testId)) {
						Log.debugMessage("was:" + item.testTimeLine.testId
								+ ", now:" + testTimeItem.testTimeLine.testId,
							Log.DEBUGLEVEL10);
						selectTheSameTest = true;
						break;
					}
				}

				this.selectedItems.add(testTimeItem);

				Log.debugMessage("selectTheSameTest " + selectTheSameTest + ", unselect:" + unselect,
					Log.DEBUGLEVEL10);

				if (!selectTheSameTest) {
//					if (unselect) {
//						this.unselect();
//					}

					this.selectedTestIds.add(testTimeItem.testTimeLine.testId);
					final Test test = TestView.valueOf(testTimeItem.testTimeLine.testId).getTest();
					new ProcessingDialogDummy(new Runnable() {

						public void run() {
							TestLine.this.skip = true;
//							assert Log.debugMessage("set skip true | " + System.currentTimeMillis(), Log.DEBUGLEVEL03);							
							try {
								TestLine.this.schedulerModel.addSelectedTest(TestLine.this, test);
							} catch (final ApplicationException e) {
								AbstractMainFrame.showErrorMessage(
									I18N.getString("Scheduler.Error.CannotSelectTest"));
							}
//							assert Log.debugMessage("set skip false | " + System.currentTimeMillis(), Log.DEBUGLEVEL03);
							TestLine.this.skip = false;
						}

					}, I18N.getString("Common.ProcessingDialog.PlsWait"));

					for (final TestTimeItem testTimeItem2 : testTimeItems) {
						if (testTimeItem2.testTimeLine.testId.equals(testTimeItem.testTimeLine.testId)) {
							this.selectedItems.add(testTimeItem2);
						}
					}
				}

				selected = true;
				break;
			}
		} // for
		} // syncronized
		this.lastX = x;
		return selected;
	}

	public Rectangle getVisibleRectangle() {
		Rectangle rectangle = null;
		final Rectangle visibleRect = super.cachedVisibleRect;
		if (this.lastX >= 0) {
			return new Rectangle(this.lastX - PlanPanel.MARGIN / 2, 0, 10,
					this.getHeight() - (this.titleHeight / 2 + 4) - 2);
			// TODO width instead of 10 
		}

		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			final Identifier testId = this.selectedTestIds.iterator().next();
			rectangle = this.getMinimalBounds(this.timeItems, testId);
			if (rectangle == null) {
				rectangle = this.getMinimalBounds(this.unsavedTestTimeItems,
						testId);
			}
		}
//		assert Log.debugMessage("rectangle:" + rectangle + ", visibleRect:" + visibleRect, Log.DEBUGLEVEL03);
		if (rectangle == null || visibleRect == null ||
				rectangle.x < visibleRect.x + visibleRect.width &&
					rectangle.x + rectangle.width > visibleRect.x) {
//			assert Log.debugMessage("visibleRect:" + visibleRect, Log.DEBUGLEVEL03);
			return visibleRect;
		}
//		assert Log.debugMessage("rectangle:" + rectangle, Log.DEBUGLEVEL03);
		return rectangle;
	}

	private Rectangle getMinimalBounds(final SortedSet<TestTimeItem> sortedSet,
		final Identifier testId) {
		TestTimeItem firstElement = null;
		TestTimeItem lastElement = null;
		for (final TestTimeItem element : sortedSet) {
			if (element.testTimeLine.testId.equals(testId)) {
				if (firstElement == null) {
					firstElement = element;
				}
				lastElement = element;
			}
		}
		if (firstElement != null) {
			return new Rectangle(firstElement.x,
					0,
					lastElement.x + lastElement.width - firstElement.x,
					this.getHeight());
		}
		return null;
	}

	private void paintFlash(Graphics g) {

		if (g != null) {
			this.flash = !this.flash;
			int y = this.titleHeight / 2 + 4;
			int h = this.getHeight() - y - 2;
			synchronized (this) {
			for (final TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h,
					UIManager.getColor(
					this.flash
						? (((this.selectedTestIds == null) || (!this.selectedTestIds.contains(testTimeItem.testTimeLine.testId)))
								? UIStorage.COLOR_SCHEDULED : UIStorage.COLOR_SCHEDULED_SELECTED)
						: UIStorage.COLOR_UNRECOGNIZED));
			} // for
			} // synchronized
		}
	}

	public void updateTest() {
		if (this.skip) {
			return;
		}

//		assert Log.debugMessage(this.title, Log.DEBUGLEVEL03);

		final Set<Identifier> selectedTestIds2 =
			this.schedulerModel.getSelectedTestIds();
		boolean theSame = (this.selectedTestIds != null ?
					this.selectedTestIds.size() : 0)
				== selectedTestIds2.size();
		if (theSame) {
			for (final Identifier identifier : selectedTestIds2) {
				theSame &= this.selectedTestIds.contains(identifier);
				if (!theSame) {
					break;
				}
			}
			if (theSame) {
				return;
			}
		}

		this.lastX = -1;

		this.selectedItems.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}

		for (final Identifier testId : selectedTestIds2) {
			if (this.testIds.contains(testId)) {
				if (this.selectedTestIds == null) {
					this.selectedTestIds = new HashSet<Identifier>();
				}
//				assert Log.debugMessage(this.title + ", " + testId, Log.DEBUGLEVEL03);
				this.selectedTestIds.add(testId);
			}
		}

		synchronized (this) {
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			for (final TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
				for (final Identifier identifier : this.selectedTestIds) {
					if (testTimeItem.testTimeLine.testId.equals(identifier)) {
						this.selectedItems.add(testTimeItem);
						break;
					}
				}
			}

			for (final TestTimeItem testTimeItem : this.timeItems) {
				for (final Identifier identifier : this.selectedTestIds) {
					if (testTimeItem.testTimeLine.testId.equals(identifier)) {
						this.selectedItems.add(testTimeItem);
						break;
					}
				}
			}
		} // if
		} // synchronized

		super.repaint();
		super.revalidate();
	}

	public void updateTests(final Set<Identifier> testIds) {
//		assert Log.debugMessage("1:" + this.title, Log.DEBUGLEVEL03);
		if (this.skip) {
			return;
		}
//		assert Log.debugMessage("2:" + this.title, Log.DEBUGLEVEL03);
		this.acquireTests(testIds);
//		assert Log.debugMessage("3:" + this.title + "testIds:" + testIds, Log.DEBUGLEVEL03);
//		if (testIds != null && !testIds.isEmpty())
		{
			this.updateTest();
		}
	}

	final int getItemY() {
		return this.titleHeight / 2 + 4;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		final int y = this.getItemY();
		final int h = this.getHeight() - y - 2;

		if (!this.timeItems.isEmpty() && super.scale > 0.0) {
			synchronized (this) {
				for (final TestTimeItem testTimeItem : this.timeItems) {
					this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h, (this.selectedTestIds == null)
							|| (!this.selectedTestIds.contains(testTimeItem.testTimeLine.testId)) ? testTimeItem.testTimeLine.color
							: testTimeItem.testTimeLine.selectedColor);
				}
			}
		}
		this.paintFlash(g);
	}

	private void createMouseListener() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}

			private void forwardMousePressedToParent(final MouseEvent e) {
				for(final MouseListener mouseListener : TestLine.this.getParent().getMouseListeners()) {
					mouseListener.mousePressed(e);
				}
			}

			private void forwardMouseReleasedToParent(final MouseEvent e) {
				for(final MouseListener mouseListener : TestLine.this.getParent().getMouseListeners()) {
					mouseListener.mouseReleased(e);
				}
			}

//// ???????? ?????? ???. ??? ? ??? ?????? ? ??? ?? ??????? - ?? ????
//
//			@Override
//			public void mousePressed(MouseEvent e) {
//				final int x = e.getX();
//				final int y = e.getY();
//				if (SwingUtilities.isLeftMouseButton(e)) {
//
//					boolean unselect = false;
//					boolean selected = false;
//
//					if (TestLine.this.selectedItems != null && !e.isShiftDown()) {
//						unselect = true;
//					}
//					if (!TestLine.this.timeItems.isEmpty() && y > getItemY()) {
//						if (!(selected = selectTest(x, TestLine.this.timeItems, unselect))) {
//							if (!(selected = selectTest(x, TestLine.this.unsavedTestTimeItems, unselect))) {
////								TestLine.this.schedulerModel.unselectTests(TestLine.this);
//								this.forwardMousePressedToParent(e);
//							}
//						}
//					} else if (!(selected = selectTest(x, TestLine.this.unsavedTestTimeItems, unselect))) {
//						this.forwardMousePressedToParent(e);
//					}
//
//					if (unselect && !selected) {
//						unselect();
//					}
//				} else if (SwingUtilities.isRightMouseButton(e)) {
//					// popupRelativeX = x;
//					// popupMenu.show(TimeStampsEditor.this, x, y);
//				}
//			}

			@Override
			public void mousePressed(MouseEvent e) {
				final int x = e.getX();
				final int y = e.getY();
				if (SwingUtilities.isLeftMouseButton(e)) {

					final boolean unselect = true;
//						TestLine.this.selectedItems != null && !e.isShiftDown();

					// ?????????? ????????? ?? ???? ??????
					if (unselect && TestLine.this.getParent() instanceof PlanPanel) {
						PlanPanel planPanel = (PlanPanel) TestLine.this.getParent();
						planPanel.schedulerModel.unselectTests(this); // NB: source=TestLine.this ?? ??????? - ???????? PlanPanel ?? ??????? ?????????; ?????????? this MouseAdaper'? 
					}

					boolean selected = false;

					if (y > getItemY()) {
						selected =
							selectTest(x, TestLine.this.timeItems, unselect) ||
							selectTest(x, TestLine.this.unsavedTestTimeItems, unselect);
						}
					if (!selected) {
						// ??? ????? ????????? ?????? ?????? ?? ?????? ????????? ????? - ?? ?? ?????
						this.forwardMousePressedToParent(e);
					}

				} else if (SwingUtilities.isRightMouseButton(e)) {
					// popupRelativeX = x;
					// popupMenu.show(TimeStampsEditor.this, x, y);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (TestLine.this.currentPoint != null && TestLine.this.startPoint != null) {
					int deltaX = TestLine.this.currentPoint.x - TestLine.this.startPoint.x;
					if (Math.abs(deltaX) < 5) {
						return;
					}
					long offset = (long) ((deltaX) / TestLine.this.scale);
					// moveIntervals(offset);

					if (TestLine.this.selectedItems != null && !TestLine.this.selectedItems.isEmpty()) {
						try {
							TestLine.this.schedulerModel.moveSelectedTests(offset);
							TestLine.this.dispatcher.firePropertyChange(
									new PropertyChangeEvent(
											this,
											SchedulerModel.COMMAND_REFRESH_TEMPORAL_STAMPS,
											null, null));
						} catch (final ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(e1.getMessage());
							updateTests(null);
						}
					}

					TestLine.this.startPoint = null;
					TestLine.this.previousPoint = null;
					TestLine.this.currentPoint = null;
				} else {
					this.forwardMouseReleasedToParent(e);
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			private void forwardmouseDraggedToParent(final MouseEvent e) {
				for(final MouseMotionListener mouseListener :
						TestLine.this.getParent().getMouseMotionListeners()) {
					mouseListener.mouseDragged(e);
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				TestLine.this.currentPoint = e.getPoint();
				if (TestLine.this.startPoint == null) {
					TestLine.this.startPoint = e.getPoint();
					TestLine.this.previousPoint = TestLine.this.startPoint;

					if (!TestLine.this.selectedItems.isEmpty()) {

						final int mouseX = e.getX();

						TestTimeItem currentTestTimeItem = null;
						for (final TestTimeItem testTimeItem : TestLine.this.selectedItems) {
							if (testTimeItem.x <= mouseX &&
									mouseX <= testTimeItem.x + testTimeItem.width) {
								currentTestTimeItem = testTimeItem;
								break;
							}
						}

						if (currentTestTimeItem == null) {
							TestLine.this.startPoint = null;
						}
					}
					return;
				}
				if (TestLine.this.previousPoint != null
						&& TestLine.this.selectedItems != null
						&& !TestLine.this.selectedItems.isEmpty()) {

					final int dx = TestLine.this.currentPoint.x
							- TestLine.this.previousPoint.x;

					if (dx == 0) {
						return;
					}

					for (final TestTimeItem testTimeItem : TestLine.this.selectedItems) {
						final Test test = TestView.valueOf(testTimeItem.testTimeLine.testId).getTest();
						if (!TestLine.this.schedulerModel.isTestNewer(test)) {
							continue;
						}

						testTimeItem.x += dx;
					}
					TestLine.this.repaint();
					TestLine.this.revalidate();
					TestLine.this.previousPoint = TestLine.this.currentPoint;
				} else {
					TestLine.this.currentPoint = null;
					forwardmouseDraggedToParent(e);
				}
			}
		});
	}

	private void acqureTest(final Test test) throws ApplicationException {
		final PlanPanel planPanel = (PlanPanel) this.getParent();
		final Date start1 = planPanel.getStartDate();
		final Date end1 = planPanel.scaleEnd;

		final Identifier testId = test.getId();

		final TestView testView = TestView.valueOf(test);

		final MeasurementSetup measurementSetup = testView.getMeasurementSetup();
		final long measurementDuration = measurementSetup.getMeasurementDuration();
		final SortedMap<Date, String> stoppings = test.getStoppingMap();
		final Date testTime = test.getStartTime();

//		assert Log.debugMessage(test.getId() + " > " + testTime, Log.DEBUGLEVEL03);

		if (testTime.compareTo(end1) > 0) {
//			assert Log.debugMessage("1", Log.DEBUGLEVEL03);
			return;
		}
		final TestStatus status = test.getStatus();
		Color selectedColor = SchedulerModel.getColor(status, true);
		Color color = SchedulerModel.getColor(status, false);
		String testTitle = SchedulerModel.getStatusName(status);

		if (status == TestStatus.TEST_STATUS_PROCESSING) {
			testTitle = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_SCHEDULED);
			selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, true);
			color = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, false);
		}

		this.testIds.add(testId);

		final List<TestTimeLine> measurementTestList = new LinkedList<TestTimeLine>();
		this.measurements.put(testId, measurementTestList);

		final Set<Measurement> testMeasurements = testView.getMeasurements();

		switch (test.getTemporalType().value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL: {
				final AbstractTemporalPattern temporalPattern =
					StorableObjectPool.getStorableObject(
							test.getTemporalPatternId(), true);
				final SortedSet<Date> times = temporalPattern.getTimes(
						testTime, test.getEndTime(), start1, end1);
				if (status != TestStatus.TEST_STATUS_COMPLETED) {
					for(final Date date : times) {

//						if (date.compareTo(start1) < 0 || date.compareTo(end1) > 0) {
//							continue;
//						}

						final TestTimeLine testTimeLine = new TestTimeLine();
						testTimeLine.testId = testId;
						final long time = date.getTime();
						boolean foundMeasurement = false;
						for (final Measurement measurement : testMeasurements) {
							final Date measurementTime = measurement.getStartTime();
							final long time2 = measurementTime.getTime();
							if (time <= time2 && time2 <= time + measurementDuration) {
								testTimeLine.date = measurementTime;
								testTimeLine.startTime = time2;
								testTimeLine.duration = measurement.getDuration();
								this.updateTestTimeLine(testTimeLine, measurement);
								foundMeasurement = true;
								break;
							}
						}

						if (!foundMeasurement) {
							testTimeLine.date = date;
							testTimeLine.startTime = time;
							testTimeLine.duration = measurementDuration;
							if (stoppings.tailMap(date).isEmpty()) {
								boolean foundMeasurementAfterThisSheduledTime = false;
								for (final Measurement measurement : testMeasurements) {
									final Date measurementTime = measurement.getStartTime();
									if (date.before(measurementTime)) {
										foundMeasurementAfterThisSheduledTime = true;
										break;
									}
								}
								if (!foundMeasurementAfterThisSheduledTime) {
									testTimeLine.title = testTitle;
									testTimeLine.color = color;
									testTimeLine.selectedColor = selectedColor;
								} else {
									continue;
								}

							} else {
								testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_STOPPED);
								testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_STOPPED, false);
								testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_STOPPED, true);
							}

						}
						measurementTestList.add(testTimeLine);
					}
				} else {
					for(final Date date : times) {
//						if (date.compareTo(start1) < 0 || date.compareTo(end1) > 0) {
//							continue;
//						}
						final TestTimeLine testTimeLine = new TestTimeLine();
						testTimeLine.testId = testId;
						final long time = date.getTime();
						boolean foundMeasurement = false;
						for (final Measurement measurement : testMeasurements) {
							final Date measurementTime = measurement.getStartTime();
							final long time2 = measurementTime.getTime();
							if (time <= time2 && time2 <= time + measurementDuration) {
								testTimeLine.date = measurementTime;
								testTimeLine.startTime = time2;
								testTimeLine.duration = measurement.getDuration();
								this.updateTestTimeLine(testTimeLine, measurement);
								foundMeasurement = true;
								break;
							}
						}

						if (!foundMeasurement) {
							testTimeLine.date = date;
							testTimeLine.startTime = time;
							testTimeLine.duration = measurementDuration;
							if (!stoppings.tailMap(date).isEmpty()) {
								testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_STOPPED);
								testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_STOPPED, false);
								testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_STOPPED, true);
							} else {
								testTimeLine.title = testTitle;
								testTimeLine.color = color;
								testTimeLine.selectedColor = selectedColor;
							}
						}
						measurementTestList.add(testTimeLine);
					}
				}
			}
				break;
			default:
				if (test.getEndTime().getTime() + measurementDuration < start1.getTime()) {
//					assert Log.debugMessage("2", Log.DEBUGLEVEL03);
					return;
				}
				TestTimeLine testTimeLine = new TestTimeLine();
				testTimeLine.testId = testId;

				final long time = testTime.getTime();
				boolean foundMeasurement = false;
				for (final Measurement measurement : testMeasurements) {
					final Date measurementTime = measurement.getStartTime();
					final long time2 = measurementTime.getTime();
					if (time <= time2 && time2 <= time + measurementDuration) {
						testTimeLine.date = measurementTime;
						testTimeLine.startTime = time2;
						testTimeLine.duration = measurement.getDuration();
						this.updateTestTimeLine(testTimeLine, measurement);
						foundMeasurement = true;
						break;
					}
				}

				if (!foundMeasurement) {
					testTimeLine.date = testTime;
					testTimeLine.title = testTitle;
					testTimeLine.startTime = time;
					testTimeLine.duration = test.getEndTime().getTime()
							- testTimeLine.startTime;

					testTimeLine.color = color;
					testTimeLine.selectedColor = selectedColor;
				}
				measurementTestList.add(testTimeLine);
				break;
		}
		Collections.sort(measurementTestList);
	}

	@SuppressWarnings("unchecked")
	private void acquireTests(final Set<Identifier> testIds) {
		if (testIds != null && testIds.isEmpty()) {
			return;
		}

		synchronized (this) {
			if (testIds == null) {
				this.testIds.clear();
				this.measurements.clear();
			} else {
				this.testIds.removeAll(testIds);
				for (final Identifier testId : testIds) {
					this.measurements.remove(testId);
				}
			}
			try {
				for (final Identifier testId : testIds != null ? testIds : this.schedulerModel.getTestIds()) {
					final Test test = TestView.valueOf(testId).getTest();
					if (!test.getMonitoredElementId().equals(this.monitoredElementId)) {
						continue;
					}
					this.acqureTest(test);
				}
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
			}
		}
		this.updateScale();
		this.refreshTimeItems();

	}

	private final void updateTestTimeLine(final TestTimeLine testTimeLine,
			final Measurement measurement) {
		switch (measurement.getStatus().value()) {
		case IdlMeasurementStatus._MEASUREMENT_STATUS_ABORTED:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_ABORTED);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_ABORTED, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_ABORTED, true);
			break;
		case IdlMeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_SCHEDULED);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, true);
			break;
		case IdlMeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_PROCESSING);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_PROCESSING, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_PROCESSING, true);
			break;
		case IdlMeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
		case IdlMeasurementStatus._MEASUREMENT_STATUS_COMPLETED:
		default:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_COMPLETED);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_COMPLETED, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_COMPLETED, true);
			break;
		}
	}

	@Override
	public void setStart(long start) {
		long oldValue = super.start;
		super.start = start;
		int diff = (int) (this.scale * (oldValue - this.start));
		for(TestTimeItem testTimeItem : this.timeItems) {
			testTimeItem.x += diff;
		}

		synchronized (this) {
		for(TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
			testTimeItem.x += diff;
		}
		}
	}

	@Override
	void refreshTimeItems() {
		synchronized (this) {
			this.unsavedTestTimeItems.clear();
			this.timeItems.clear();
			if (this.scale <= 0.0 || super.start == 0 || super.end == 0) {
				super.scale = (double) (this.getWidth() - PlanPanel.MARGIN) / (double) (this.end - this.start);
			}

			if (this.statusTestComparator == null) {
				this.statusTestComparator = new Comparator<Test>(){
					public final int compare(final Test t1,
										final Test t2) {
						final TestStatus status1 = t1.getStatus();
						final TestStatus status2 = t2.getStatus();
						return status1.value() - status2.value();
					}
				};
			}

			final List<Test> tests = new ArrayList<Test>(this.testIds.size());
			for (final Identifier testId : this.testIds) {
				final Test test = TestView.valueOf(testId).getTest();
				tests.add(test);
			}

			Collections.sort(tests, this.statusTestComparator);

			for (final Test test : tests) {
				final List<TestTimeLine> testTimeLineList = this.measurements.get(test.getId());
//				assert Log.debugMessage("Test:" + test.getId() + ", " + test.getStartTime(), Log.DEBUGLEVEL03);
				if (testTimeLineList == null || testTimeLineList.isEmpty()) {
					System.err.println("TestLine.refreshTimeItems | List<TestTimeLine> for " + test.getId() + " is null or empty ");
					continue;
				}

				for (final TestTimeLine testTimeLine : testTimeLineList) {
					final TestTimeItem testTimeItem = new TestTimeItem();

					testTimeItem.x = PlanPanel.MARGIN / 2 + (int) (this.scale *
								(testTimeLine.startTime - this.start));
					int width = (int) (this.scale * testTimeLine.duration);

					width = width > this.minimalWidth ? width : this.minimalWidth;
					testTimeItem.setWidth(width);

					testTimeItem.testTimeLine = testTimeLine;

					if (this.schedulerModel.isTestNewer(test)) {
						this.unsavedTestTimeItems.add(testTimeItem);
					} else {
						this.timeItems.add(testTimeItem);
					}
				}
			}
		}
		super.repaint();
		super.revalidate();
	}

	@Override
	public String getToolTipText(final MouseEvent event) {
		final int x = event.getX();
		String toolTip = this.getTitle(x, this.timeItems);
		if (toolTip == null) {
			toolTip = this.getTitle(x, this.unsavedTestTimeItems);
			if (toolTip == null) {
				final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
				toolTip = this.title + " " + sdf.format(new Date(
					(long) (this.start +
							((x - PlanPanel.MARGIN / 2) / this.scale))));

			}
		}
		return toolTip;
	}

	private String getTitle(final int x,
							final Set<TestTimeItem> testTimeItems) {

		final TestViewAdapter testController = TestViewAdapter.getInstance();
		for (final TestTimeItem testTimeItem : testTimeItems) {
			final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
				final Test test = TestView.valueOf(testTimeItem.testTimeLine.testId).getTest();
				final TestView view = TestView.valueOf(test);
				final String string = "<html>" + testController.getValue(view, TestViewAdapter.KEY_TEMPORAL_TYPE_NAME).toString()
						+ "<br>" + testController.getName(TestViewAdapter.KEY_START_TIME)
						+ " : "
						+ sdf.format(testController.getValue(view, TestViewAdapter.KEY_START_TIME))
						+ "<br><br>"
						+ sdf.format(testTimeItem.testTimeLine.date)
						+ " : "
						+ testTimeItem.testTimeLine.title
						+ "</html>";
				return string;
			}
		}
		return null;
	}

	public void addTest(final Set<Identifier> testIds) {
		try {
			for (final Identifier testId : testIds) {
				final Test test = TestView.valueOf(testId).getTest();
				if (!test.getMonitoredElementId().equals(
						this.monitoredElementId)) {
					continue;
				}
				this.acqureTest(test);
			}
		} catch (final ApplicationException e) {
			AbstractMainFrame.showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
		}

		this.updateScale();
		this.refreshTimeItems();
		this.updateTest();
	}

	public void clearTests() {
		this.measurements.clear();
		this.unsavedTestTimeItems.clear();
		this.timeItems.clear();
		this.testIds.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		this.selectedItems.clear();
	}

}
