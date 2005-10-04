
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.ProcessingDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.measurement.AbstractTemporalPattern;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;

final class TestLine extends TimeLine {

	private static final long	serialVersionUID	= 3978424736810416184L;

	private class TestTimeLine implements Comparable<TestTimeLine> {
		protected long duration;
//		protected boolean haveMeasurement;
		
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
			return (this.x - item.x);
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			}
			
			if (obj instanceof TestLine.TestTimeItem) {
				final TestTimeItem testTimeItem = (TestTimeItem) obj;
				return testTimeItem.testTimeLine.testId == this.testTimeLine.testId ||
						testTimeItem.testTimeLine.testId != null && testTimeItem.testTimeLine.testId.equals(this.testTimeLine.testId);
			}
			return false;
		}

		@Override
		public int hashCode() {
			return this.testTimeLine.testId == null ? 0 : this.testTimeLine.testId.hashCode();
		}

		public void setWidth(final int width) {			
			this.width = width;
		}

	}

	final Set<Identifier> testIds = new HashSet<Identifier>();

	Dispatcher dispatcher;

	boolean flash = false;
	SchedulerModel schedulerModel;
	// Set selectedTests;
	Set<Identifier> selectedTestIds;

	final Collection<Identifier> unsavedTestIds = new LinkedList<Identifier>();
	final SortedSet<TestTimeItem> unsavedTestTimeItems = new TreeSet<TestTimeItem>();

	final Map<Identifier, List<TestTimeLine>> measurements = new HashMap<Identifier, List<TestTimeLine>>();
	private Identifier monitoredElementId;

	// private static final long ONE_MINUTE = 60L * 1000L;

	final SortedSet<TestTimeItem> selectedItems = new TreeSet<TestTimeItem>();

	SortedMap offsetIdBuffer = new TreeMap();
	SortedMap offsetDurationBuffer = new TreeMap();

	JPopupMenu			popupMenu;
	int					popupRelativeX;

	Point				startPoint;
	Point				currentPoint;
	Point				previousPoint;

	protected volatile boolean 	skip					= false;

	public TestLine(final ApplicationContext aContext, 
	                final String title, final 
	                Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.createMouseListener();
		this.title = title;
		this.monitoredElementId = monitoredElementId;

		this.acquireTests();

		this.dispatcher = aContext.getDispatcher();
		this.setToolTipText("");

	}

	boolean selectTest(final int x, final SortedSet<TestTimeItem> testTimeItems) {
		boolean selected = false;
		for (final TestTimeItem testTimeItem : testTimeItems) {
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
				if (this.selectedTestIds == null) {
					this.selectedTestIds = new HashSet<Identifier>();
				}
				try {
					final Test test = 
						StorableObjectPool.getStorableObject(
							testTimeItem.testTimeLine.testId,
							true);
					this.selectedTestIds.add(test.getId());
					new ProcessingDialog(new Runnable() {

						public void run() {
							TestLine.this.skip = true;
							TestLine.this.schedulerModel.addSelectedTest(TestLine.this, test);
							TestLine.this.skip = false;
						}
					}, LangModelGeneral.getString("Message.Information.PlsWait"));
				} catch (final ApplicationException e) {
					AbstractMainFrame.showErrorMessage(
						LangModelSchedule.getString("Error.CannotSelectTest"));
					return false;
				}
				this.selectedItems.add(testTimeItem);
				
				for (final TestTimeItem testTimeItem2 : testTimeItems) {
					if (testTimeItem2.testTimeLine.testId.equals(testTimeItem.testTimeLine.testId)) {
						this.selectedItems.add(testTimeItem2);
					}
				}
				
				selected = true;
				break;
			}
		}
		return selected;
	}

	public Rectangle getVisibleRectangle() {
		Rectangle rectangle = null;
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
			final Identifier testId = this.selectedTestIds.iterator().next();
			for (final TestTimeItem element : this.timeItems) {
				if (element.testTimeLine.testId.equals(testId)) {
					rectangle = new Rectangle(element.x - PlanPanel.MARGIN / 2, 0, element.width, this.getHeight()
							- (this.titleHeight / 2 + 4)
							- 2);
					break;
				}
			}
			
			if (rectangle == null) {
				for (final TestTimeItem element : this.unsavedTestTimeItems) {
					if (element.testTimeLine.testId.equals(testId)) {
						rectangle = new Rectangle(element.x - PlanPanel.MARGIN / 2, 0, element.width, this.getHeight()
								- (this.titleHeight / 2 + 4)
								- 2);
						break;
					}
				}
			}
		}
		return rectangle;
	}
	
	boolean isTestNewer(final Test test) {
		return test.getVersion().equals(StorableObjectVersion.INITIAL_VERSION) 
			&& test.getStatus().value() == TestStatus._TEST_STATUS_NEW;
	}

	private void paintFlash(Graphics g) {

		if (g != null) {
			this.flash = !this.flash;
			int y = this.titleHeight / 2 + 4;
			int h = this.getHeight() - y - 2;

			for (final TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.width, h, this.flash
						? (((this.selectedTestIds == null) || (!this.selectedTestIds.contains(testTimeItem.testTimeLine.testId)))
								? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_SCHEDULED_SELECTED)
						: SchedulerModel.COLOR_UNRECOGNIZED);
			}

		}
	}

	public void updateTest() {
		if (this.skip) { 
			return; 
		}

		this.selectedItems.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		for (final Identifier testId : this.schedulerModel.getSelectedTestIds()) {
			if (this.testIds.contains(testId)) {
				if (this.selectedTestIds == null) {
					this.selectedTestIds = new HashSet<Identifier>();
				}
				this.selectedTestIds.add(testId);
			}
		}

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
		}
		super.repaint();
		super.revalidate();
	}

	public void updateTests() {
		if (this.skip) { 
			return; 
		}
		this.acquireTests();
		this.updateTest();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		final int y = this.titleHeight / 2 + 4;
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
			
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				if (SwingUtilities.isLeftMouseButton(e)) {
					
					boolean unselect = false;
					boolean selected = false;
					
					if (TestLine.this.selectedItems != null && !e.isShiftDown()) {
						unselect = true;
					}
					if (!TestLine.this.timeItems.isEmpty()) {
						if (!(selected = selectTest(x, TestLine.this.timeItems))) {
							if (!(selected = selectTest(x, TestLine.this.unsavedTestTimeItems))) {
								TestLine.this.schedulerModel.unselectTests(TestLine.this);
								for(final MouseListener mouseListener : TestLine.this.getParent().getMouseListeners()) {
									mouseListener.mousePressed(e);
								}
							}
						}
					} else if (!(selected = selectTest(x, TestLine.this.unsavedTestTimeItems))) {
						for(final MouseListener mouseListener : TestLine.this.getParent().getMouseListeners()) {
							mouseListener.mousePressed(e);
						}
					}
					
					if (unselect && !selected){
						TestLine.this.selectedItems.clear();
						if (TestLine.this.selectedTestIds != null) {
							TestLine.this.selectedTestIds.clear();
						}
						TestLine.this.schedulerModel.unselectTests(TestLine.this);
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
//						final TestTimeItem testTimeItem = TestLine.this.selectedItems.first();
//						final Test test;
//						try {
//							test = (Test) StorableObjectPool.getStorableObject(testTimeItem.testTimeLine.testId, true);
//						} catch (final ApplicationException e1) {
//							AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
//							return;
//						}
						try {
//							final Date startTime = test.getStartTime();
//							TestLine.this.schedulerModel.moveSelectedTests(new Date(startTime.getTime() + offset));
							TestLine.this.schedulerModel.moveSelectedTests(offset);
							TestLine.this.dispatcher
									.firePropertyChange(new PropertyChangeEvent(
																				this,
																				SchedulerModel.COMMAND_REFRESH_TEMPORAL_STAMPS,
																				null, null));
						} catch (final ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(e1.getMessage());
						}

					}

					TestLine.this.startPoint = null;
					TestLine.this.previousPoint = null;
					TestLine.this.currentPoint = null;
				}

			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

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
				if (TestLine.this.previousPoint != null && TestLine.this.selectedItems != null
						&& !TestLine.this.selectedItems.isEmpty()) {
					// SwingUtilities.invokeLater(new Runnable() {
					//
					// public void run() {
					int dx = (TestLine.this.currentPoint.x - TestLine.this.previousPoint.x);

					if (dx == 0) {
						return;
					}

					for (final TestTimeItem testTimeItem : TestLine.this.selectedItems) {
						try {
							final Test test = StorableObjectPool.getStorableObject(testTimeItem.testTimeLine.testId, true);
							if (!isTestNewer(test)) {
								continue;
							}
						} catch (final ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
						}
						
						testTimeItem.x += dx;
					}
					TestLine.this.repaint();
					TestLine.this.revalidate();
					TestLine.this.previousPoint = TestLine.this.currentPoint;
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void acquireTests() {
		this.testIds.clear();
		this.unsavedTestIds.clear();
		this.measurements.clear();
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(this.schedulerModel.getTestIds(), true);
			for (final Test test : tests) {
				if (!test.getMonitoredElementId().equals(this.monitoredElementId)) {
					continue;
				}

				final Identifier testId = test.getId();
				
				final MeasurementSetup measurementSetup = StorableObjectPool.getStorableObject(test.getMainMeasurementSetupId(), true);
				final long measurementDuration = measurementSetup.getMeasurementDuration();
				final SortedMap<Date, String> stoppings = test.getStoppingMap();
				final Date testTime = test.getStartTime();

				final TestStatus status = test.getStatus();
				Color selectedColor = SchedulerModel.getColor(status, true);
				Color color = SchedulerModel.getColor(status, false);
				String title = SchedulerModel.getStatusName(status);
				
				if (status == TestStatus.TEST_STATUS_PROCESSING) {
					title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_SCHEDULED);
					selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, true);
					color = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, false);
				}
				
				this.testIds.add(testId);
				
				final boolean newerTest = this.isTestNewer(test);

				final List<TestTimeLine> measurementTestList = new LinkedList<TestTimeLine>();
				this.measurements.put(testId, measurementTestList);
				
				final Set<Measurement> testMeasurements;
				if (newerTest) {
					testMeasurements = Collections.emptySet();
					this.unsavedTestIds.add(testId);
				} else {
					final LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(testId, ObjectEntities.MEASUREMENT_CODE);	
					testMeasurements = new HashSet(StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true));
				}
				
				switch (test.getTemporalType().value()) {
					case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL: {
						final AbstractTemporalPattern temporalPattern = StorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
						final SortedSet<Date> times = temporalPattern.getTimes(testTime, test.getEndTime());
						if (status != TestStatus.TEST_STATUS_COMPLETED) {
							for(final Date date : times) {
								final TestTimeLine testTimeLine = new TestTimeLine();
								testTimeLine.testId = testId;
								final long time = date.getTime();
								boolean foundMeasurement = false;
								for (final Measurement measurement : testMeasurements) {
									final Date measurementTime = measurement.getStartTime();
									final long time2 = measurementTime.getTime();
									if (time <= time2 &&  time2 <= time + measurementDuration) {
										testTimeLine.date = measurementTime;
										testTimeLine.startTime = time2;
										testTimeLine.duration = measurement.getDuration();										
										this.updateTestTimeLine(testTimeLine, measurement);
										foundMeasurement = true;
										testMeasurements.remove(measurement);
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
											testTimeLine.title = title;
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
								final TestTimeLine testTimeLine = new TestTimeLine();
								testTimeLine.testId = testId;
								final long time = date.getTime();
								boolean foundMeasurement = false;
								for (final Measurement measurement : testMeasurements) {
									final Date measurementTime = measurement.getStartTime();
									final long time2 = measurementTime.getTime();
									if (time <= time2 &&  time2 <= time + measurementDuration) {
										testTimeLine.date = measurementTime;
										testTimeLine.startTime = time2;
										testTimeLine.duration = measurement.getDuration();										
										this.updateTestTimeLine(testTimeLine, measurement);
										foundMeasurement = true;
										testMeasurements.remove(measurement);
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
									}						
								}
								measurementTestList.add(testTimeLine);
							}
						}
					}
						break;
					default:
						TestTimeLine testTimeLine = new TestTimeLine();
						testTimeLine.testId = testId;
						
						final long time = testTime.getTime();
						boolean foundMeasurement = false;
						for (final Measurement measurement : testMeasurements) {
							final Date measurementTime = measurement.getStartTime();
							final long time2 = measurementTime.getTime();
							if (time <= time2 &&  time2 <= time + measurementDuration) {
								testTimeLine.date = measurementTime;
								testTimeLine.startTime = time2;
								testTimeLine.duration = measurement.getDuration();
								this.updateTestTimeLine(testTimeLine, measurement);
								foundMeasurement = true;
								testMeasurements.remove(measurement);
								break;
							}
						}
						
						if (!foundMeasurement) {
							testTimeLine.date = testTime;
							testTimeLine.title = title;
							testTimeLine.startTime = time;
							testTimeLine.duration =  test.getEndTime().getTime() - testTimeLine.startTime +
									measurementSetup.getMeasurementDuration();
							
//							assert Log.debugMessage("TestLine.acquireTests | " + testTimeLine.duration,
//								Log.DEBUGLEVEL09);
							
							testTimeLine.color = color;
							testTimeLine.selectedColor = selectedColor;
						}
						measurementTestList.add(testTimeLine);
						break;
				}


				Collections.sort(measurementTestList);
//				for (final TestTimeLine testTimeLine : measurementTestList) {
//					assert Log.debugMessage("TestLine.acquireTests | " + testTimeLine.testId + " > " + testTimeLine.date,
//						Log.DEBUGLEVEL09);
//				}
			}
		} catch (final ApplicationException e) {
			AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
		}

		this.updateScale();
		this.refreshTimeItems();
	}

	private final void updateTestTimeLine(final TestTimeLine testTimeLine,  
	                                final Measurement measurement) {
		switch (measurement.getStatus().value()) {
		case MeasurementStatus._MEASUREMENT_STATUS_ABORTED:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_ABORTED);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_ABORTED, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_ABORTED, true);
			break;
		case MeasurementStatus._MEASUREMENT_STATUS_SCHEDULED:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_SCHEDULED);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_SCHEDULED, true);
			break;
		case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRING:
			testTimeLine.title = SchedulerModel.getStatusName(TestStatus.TEST_STATUS_PROCESSING);
			testTimeLine.color = SchedulerModel.getColor(TestStatus.TEST_STATUS_PROCESSING, false);
			testTimeLine.selectedColor = SchedulerModel.getColor(TestStatus.TEST_STATUS_PROCESSING, true);
			break;
		case MeasurementStatus._MEASUREMENT_STATUS_ACQUIRED:
		case MeasurementStatus._MEASUREMENT_STATUS_COMPLETED:
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
		
		for(TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
			testTimeItem.x += diff;
		}		
	}
	
	@Override
	void refreshTimeItems() {
		synchronized (this) {
			this.timeItems.clear();
			if (this.scale <= 0.0 || super.start == 0 || super.end == 0) {
				super.scale = (double) (this.getWidth() - PlanPanel.MARGIN) / (double) (this.end - this.start);
			}
	
			try {
				final Set<Test> tests = StorableObjectPool.getStorableObjects(this.testIds, true);
				for (final Test test : tests) {				
					final List<TestTimeLine> testTimeLineList = this.measurements.get(test.getId());
					if (testTimeLineList == null) {
						continue;
					}
	
					for (final TestTimeLine testTimeLine : testTimeLineList) {
						final TestTimeItem testTimeItem = new TestTimeItem();
	
						testTimeItem.x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
						int width = (int) (this.scale * testTimeLine.duration);
	
						width = width > this.minimalWidth ? width : this.minimalWidth;
						testTimeItem.setWidth(width);						
						
						testTimeItem.testTimeLine = testTimeLine;
						
						this.timeItems.add(testTimeItem);
					}	
				}
	
				this.unsavedTestTimeItems.clear();
				for (final Iterator it = this.timeItems.iterator(); it.hasNext();) {
					final TestTimeItem testTimeItem = (TestTimeItem) it.next();
					if (this.isTestNewer((Test) StorableObjectPool.getStorableObject(testTimeItem.testTimeLine.testId, true))) {
						it.remove();
						this.unsavedTestTimeItems.add(testTimeItem);
					}
				}
			} catch (final ApplicationException e) {
				AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
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
					(long) (this.start + ((x - PlanPanel.MARGIN / 2) / this.scale))));

			}
		} 
		return toolTip;
	}

	private String getTitle(final int x,
							final Set<TestTimeItem> testTimeItems) {
		
		final TestController testController = TestController.getInstance();
		for (final TestTimeItem testTimeItem : testTimeItems) {
			final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.width) {
				
				try {
					final Test test = (Test) StorableObjectPool.getStorableObject(testTimeItem.testTimeLine.testId, true);
					return "<html>" + testController.getValue(test, TestController.KEY_TEMPORAL_TYPE_NAME).toString()
							+ "<br>" + testController.getName(TestController.KEY_START_TIME) 
							+ " : " 
							+ testController.getValue(test, TestController.KEY_START_TIME) 
							+ "<br><br>" 
							+ sdf.format(testTimeItem.testTimeLine.date)
							+ " : "
							+ testTimeItem.testTimeLine.title
							+ "</html>";
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(LangModelGeneral.getString("Error.CannotAcquireObject"));
				}
			}
		}
		return null;
	}

}
