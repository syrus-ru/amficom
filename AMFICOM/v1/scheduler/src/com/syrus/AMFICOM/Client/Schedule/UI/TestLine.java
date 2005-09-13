
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
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
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestController;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;

public class TestLine extends TimeLine {

	private static final long	serialVersionUID	= 3978424736810416184L;

	private class TestTimeLine implements Comparable<TestTimeLine> {
		protected long duration;
		protected boolean haveMeasurement;

		protected long startTime;
		protected Identifier testId;

		public int compareTo(final TestTimeLine testTimeLine) {
			return (int) (this.startTime - testTimeLine.startTime);
		}
	}

	static class TestTimeItem implements Comparable<TestTimeItem> {
		int x;
		private int width;

		Color color;
		Color selectedColor;

		// Test test;
		Object object;

		public int compareTo(final TestTimeItem item) {
			return (this.x - item.x);
		}

		@Override
		public boolean equals(final Object obj) {
			final boolean result = super.equals(obj);
			if (result) {
				return result;
			}
			if (!(obj instanceof TestLine.TestTimeItem)) {
				return false;
			}
			final TestTimeItem testTimeItem = (TestTimeItem) obj;
			final boolean b1 = (testTimeItem.object == this.object && this.object == null);
			final boolean b2 = (testTimeItem.object != null && testTimeItem.object.equals(this.object));
			return b1 || b2;
		}

		@Override
		public int hashCode() {
			return this.object == null ? 0 : this.object.hashCode();
		}

		public int getWidth() {
			return this.width;
		}

		public void setWidth(final int width) {			
			this.width = width;
		}

	}

	Set<Identifier> testIds = new HashSet<Identifier>();

	Dispatcher dispatcher;

	boolean flash = false;
	SchedulerModel schedulerModel;
	// Set selectedTests;
	Set<Identifier> selectedTestIds;

	Collection<Identifier> unsavedTestIds = new LinkedList<Identifier>();
	SortedSet<TestTimeItem> unsavedTestTimeItems = new TreeSet<TestTimeItem>();

	Map<Identifier, List<TestTimeLine>> measurements = new HashMap<Identifier, List<TestTimeLine>>();
	private Identifier monitoredElementId;

	// private static final long ONE_MINUTE = 60L * 1000L;

	SortedSet<TestTimeItem> selectedItems = new TreeSet<TestTimeItem>();

	SortedMap offsetIdBuffer = new TreeMap();
	SortedMap offsetDurationBuffer = new TreeMap();

	JPopupMenu			popupMenu;
	int					popupRelativeX;

	Point				startPoint;
	Point				currentPoint;
	Point				previousPoint;

	protected boolean	skip					= false;

	public TestLine(ApplicationContext aContext, String title, Identifier monitoredElementId) {
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
		List<TestTimeItem> list = new ArrayList<TestTimeItem>(testTimeItems); 
		for (ListIterator it = list.listIterator(list.size()); it.hasPrevious();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.previous();
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.getWidth()) {
					if (this.selectedTestIds == null) {
						this.selectedTestIds = new HashSet<Identifier>();
					}
					try {
					final Test test = (Test) StorableObjectPool.getStorableObject((Identifier) testTimeItem.object,
						true);
					this.selectedTestIds.add(test.getId());

					CommonUIUtilities.invokeAsynchronously(new Runnable() {

						public void run() {
							try {
								TestLine.this.skip = true;
								TestLine.this.schedulerModel.addSelectedTest(test);
								TestLine.this.skip = false;
							} catch (ApplicationException e) {
								AbstractMainFrame.showErrorMessage(TestLine.this, e);
							}

						}
					}, LangModelGeneral.getString("Message.Information.PlsWait"));
				} catch (ApplicationException e) {
					AbstractMainFrame.showErrorMessage(TestLine.this, e);
				}
				
				this.selectedItems.add(testTimeItem);
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
				if (((Identifier) element.object).equals(testId)) {
					rectangle = new Rectangle(element.x - PlanPanel.MARGIN / 2, 0, element.getWidth(), this.getHeight()
							- (this.titleHeight / 2 + 4)
							- 2);
					break;
				}
			}
			
			if (rectangle == null) {
				for (final TestTimeItem element : this.unsavedTestTimeItems) {
					if (((Identifier) element.object).equals(testId)) {
						rectangle = new Rectangle(element.x - PlanPanel.MARGIN / 2, 0, element.getWidth(), this.getHeight()
								- (this.titleHeight / 2 + 4)
								- 2);
						break;
					}
				}
			}
		}
		return rectangle;
	}

	// public void propertyChange(PropertyChangeEvent evt) {
	// String propertyName = evt.getPropertyName();
	// if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
	// updateTests();
	// } else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
	// updateTest();
	// }
	//		
	// }
	
	boolean isTestNewer(final Test test) {
		return test.isChanged() && test.getStatus().value() == TestStatus._TEST_STATUS_NEW;
	}

	private void paintFlash(Graphics g) {

		if (g != null) {
			this.flash = !this.flash;
			int y = this.titleHeight / 2 + 4;
			int h = this.getHeight() - y - 2;

			for (final TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
				Identifier testId = (Identifier) testTimeItem.object;
//				 if (this.startPoint != null) {
//				 Log.debugMessage("TestLine.paintFlash | " + testTimeItem.x,
//				 Log.DEBUGLEVEL08);
//				 Log.debugMessage("TestLine.paintFlash | testTimeItem > " +
//				 ((Test) testTimeItem.object).getId(), Log.DEBUGLEVEL08);
//				 }
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.getWidth(), h, this.flash
						? (((this.selectedTestIds == null) || (!this.selectedTestIds.contains(testId)))
								? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_SCHEDULED_SELECTED)
						: SchedulerModel.COLOR_UNRECOGNIZED);
			}

		}
	}

	public void updateTest() {
		if (this.skip) { 
			return; 
		}

		final Set<Identifier> selectedTestIds2 = this.schedulerModel.getSelectedTestIds();
		this.selectedItems.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		if (selectedTestIds2 != null) {
			for (final Identifier testId : selectedTestIds2) {
				if (this.testIds.contains(testId)) {
					if (this.selectedTestIds == null) {
						this.selectedTestIds = new HashSet<Identifier>();
					}
					this.selectedTestIds.add(testId);
				}
			}

			if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
				for (final TestTimeItem testTimeItem : this.unsavedTestTimeItems) {
					final Identifier testId = (Identifier) testTimeItem.object;
					for (final Identifier identifier : this.selectedTestIds) {
						if (testId.equals(identifier)) {
							this.selectedItems.add(testTimeItem);
							break;
						}
					}

				}

				for (final TestTimeItem testTimeItem : this.timeItems) {
					final Identifier testId = (Identifier) testTimeItem.object;
					for (final Identifier identifier : this.selectedTestIds) {
						if (testId.equals(identifier)) {
							this.selectedItems.add(testTimeItem);
							break;
						}
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
			for (final TestTimeItem testTimeItem : this.timeItems) {
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.getWidth(), h, (this.selectedTestIds == null)
						|| (!this.selectedTestIds.contains(testTimeItem.object)) ? testTimeItem.color
						: testTimeItem.selectedColor);
			}
		}
		this.paintFlash(g);
	}

	private void createMouseListener() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				if (SwingUtilities.isLeftMouseButton(e)) {
					if (TestLine.this.selectedItems != null && !e.isShiftDown()) {
						TestLine.this.selectedItems.clear();
						if (TestLine.this.selectedTestIds != null) {
							TestLine.this.selectedTestIds.clear();
						}
						TestLine.this.schedulerModel.unselectTests();
					}
					if (!TestLine.this.timeItems.isEmpty()) {
						if (!selectTest(x, TestLine.this.timeItems)) {
							if (!selectTest(x, TestLine.this.unsavedTestTimeItems)) {
								TestLine.this.schedulerModel.unselectTests();
							}
						}
					} else if (!selectTest(x, TestLine.this.unsavedTestTimeItems)) {
						TestLine.this.schedulerModel.unselectTests();
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
						final TestTimeItem testTimeItem = TestLine.this.selectedItems.first();
						try {
							final Test test = (Test) StorableObjectPool.getStorableObject((Identifier) testTimeItem.object, true);
							final Date startTime = test.getStartTime();
							TestLine.this.schedulerModel.moveSelectedTests(new Date(startTime.getTime() + offset));
							TestLine.this.dispatcher
									.firePropertyChange(new PropertyChangeEvent(
																				this,
																				SchedulerModel.COMMAND_REFRESH_TIME_STAMPS,
																				null, null));
						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestLine.this, e1);
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
						int minX = Integer.MAX_VALUE;
						int width = 5;
						for (Iterator it = TestLine.this.selectedItems.iterator(); it.hasNext();) {
							TestTimeItem testTimeItem = (TestTimeItem) it.next();
							if (testTimeItem.x < minX) {
								minX = testTimeItem.x;
								width = testTimeItem.getWidth();
							}
							
						}
						
						if (Math.abs(e.getX() - minX) > width) {
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

					for (Iterator it = TestLine.this.selectedItems.iterator(); it.hasNext();) {
						TestTimeItem testTimeItem = (TestTimeItem) it.next();
						try {
							final Test test = StorableObjectPool.getStorableObject((Identifier) testTimeItem.object, true);
							if (!isTestNewer(test)) {
								continue;
							}
						} catch (ApplicationException e1) {
							AbstractMainFrame.showErrorMessage(TestLine.this, e1);
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
				if (test.getMonitoredElement().getId().equals(this.monitoredElementId)) {
					this.testIds.add(test.getId());
					if (this.isTestNewer(test)) {
						this.unsavedTestIds.add(test.getId());
					} else {
						LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(test.getId(), ObjectEntities.MEASUREMENT_CODE);
						try {
							final Set<Measurement> testMeasurements = StorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition, true);
							final List<TestTimeLine> measurementTestList = new LinkedList<TestTimeLine>();
							if (!testMeasurements.isEmpty()) {
								for (Iterator iter = testMeasurements.iterator(); iter.hasNext();) {
									Measurement measurement = (Measurement) iter.next();
									TestTimeLine testTimeLine = new TestTimeLine();
									testTimeLine.testId = test.getId();
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
							AbstractMainFrame.showErrorMessage(this, e);
						}

					}

					// ///
					List<TestTimeLine> measurementTestList = this.measurements.get(test.getId());
					if (measurementTestList == null) {
						measurementTestList = new LinkedList<TestTimeLine>();
						TestTimeLine testTimeLine = new TestTimeLine();
						testTimeLine.testId = test.getId();
						testTimeLine.startTime = test.getStartTime().getTime();
						testTimeLine.haveMeasurement = false;
						switch (test.getTemporalType().value()) {
							case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL: {
								testTimeLine.duration = test.getEndTime().getTime() - testTimeLine.startTime;
							}
								break;
							default:
								testTimeLine.duration = 0;
								break;
						}

						measurementTestList.add(testTimeLine);
						this.measurements.put(test.getId(), measurementTestList);
					}
					Collections.sort(measurementTestList);

				}
			}
		} catch (ApplicationException e) {
			AbstractMainFrame.showErrorMessage(this, e);
		}

		this.updateScale();
		this.refreshTimeItems();
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

				Color selectedColor = null;
				Color unselectedColor = null;

				for (final TestTimeLine testTimeLine : testTimeLineList) {
					final TestTimeItem testTimeItem = new TestTimeItem();

					testTimeItem.x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
					int width = (int) (this.scale * testTimeLine.duration);

					width = width > this.minimalWidth ? width : this.minimalWidth;
					testTimeItem.setWidth(width);

					if (testTimeLine.haveMeasurement) {
						selectedColor = SchedulerModel.COLOR_COMPLETED_SELECTED;
						unselectedColor = SchedulerModel.COLOR_COMPLETED;
					}
					// TODO : testing bypass
					else 
					{
						selectedColor = SchedulerModel.getColor(test.getStatus(), true);
						unselectedColor = SchedulerModel.getColor(test.getStatus(), false);
					}
					
					testTimeItem.object = test.getId();
					testTimeItem.selectedColor = selectedColor;
					testTimeItem.color = unselectedColor;
					
					this.timeItems.add(testTimeItem);
				}

			}

			this.unsavedTestTimeItems.clear();
			for (final Iterator it = this.timeItems.iterator(); it.hasNext();) {
				final TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (this.isTestNewer((Test) StorableObjectPool.getStorableObject((Identifier) testTimeItem.object, true))) {
					it.remove();
					this.unsavedTestTimeItems.add(testTimeItem);
				}
			}
		} catch (ApplicationException e) {
			AbstractMainFrame.showErrorMessage(this, e);
		}
		super.repaint();
		super.revalidate();
	}

	@Override
	public String getToolTipText(MouseEvent event) {
		int x = event.getX();
		String title1 = this.getTitle(x, this.timeItems);
		if (title1 == null) {
			title1 = this.getTitle(x, this.unsavedTestTimeItems);
			if (title1 == null) {
				title1 = this.title;
			}
		}
		return title1;
	}

	private String getTitle(final int x,
							final Collection collection) {
		final TestController testController = TestController.getInstance();
		if (!collection.isEmpty()) {
			for (final Iterator it = collection.iterator(); it.hasNext();) {
				final TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.getWidth()) {
					Object object = testTimeItem.object;
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					if (object instanceof Identifier) {
						try {
							final Test test = (Test) StorableObjectPool.getStorableObject((Identifier) object, true);
							return "<html>" + testController.getValue(test, TestController.KEY_TEMPORAL_TYPE_NAME).toString()
									+ "<br>" + testController.getName(TestController.KEY_START_TIME) + " : " 
									+ testController.getValue(test, TestController.KEY_START_TIME) + "</html>";
						} catch (ApplicationException e) {
							AbstractMainFrame.showErrorMessage(this, e);
						}

					}
					return sdf
							.format(new Date(
												(long) (this.start + ((testTimeItem.x - PlanPanel.MARGIN / 2) / this.scale))));
				}
			}
		}
		return null;
	}

}
