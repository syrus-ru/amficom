
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;

public class TestLine extends TimeLine {

	private static final long	serialVersionUID	= 3978424736810416184L;

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
		private int		width;

		Color	color;

		Color	selectedColor;
		
		Test test;

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

		
		public int getWidth() {
			return this.width;
		}
		

		
		public void setWidth(int width) {
//			if (width < 20) {
//			try {
//				throw new Exception();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				Log.debugMessage("TestTimeItem.setWidth | width " + width, Log.FINEST);
//				e.printStackTrace();
//			}
//			}
			this.width = width;
		}
		
	}

	Set					testIds					= new HashSet();

	Dispatcher					dispatcher;

	boolean						flash					= false;
	SchedulerModel				schedulerModel;
//	Set						selectedTests;
	Set selectedTestIds;


	Collection					unsavedTests			= new LinkedList();
	SortedSet					unsavedTestTimeItems	= new TreeSet();

	Map							measurements			= new HashMap();
	private Identifier			monitoredElementId;

//	private static final long	ONE_MINUTE				= 60L * 1000L;
	
	
	SortedSet selectedItems = new TreeSet();
	
	SortedMap offsetIdBuffer = new TreeMap();
	SortedMap offsetDurationBuffer = new TreeMap();
	
	JPopupMenu popupMenu;
	int popupRelativeX;
	
	Point startPoint;
	Point currentPoint;
	Point previousPoint;

	protected boolean	skip = false;

	public TestLine(ApplicationContext aContext, String title, Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
//		this.schedulerModel.addTestsEditor(this);
//		this.schedulerModel.addTestEditor(this);
//		this.createTestLineMouseListener();
		this.createMouseListener();
		this.title = title;
		this.monitoredElementId = monitoredElementId;
		
		this.acquireTests();
		
		this.dispatcher = aContext.getDispatcher();
		OperationListener operationListener = new OperationListener() {
			public void operationPerformed(OperationEvent e) {
				String actionCommand = e.getActionCommand();
				if (actionCommand.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
					updateTests();
				} else if (actionCommand.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
					updateTest();
				}
			}
		};
		
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_REFRESH_TESTS);
		this.dispatcher.register(operationListener, SchedulerModel.COMMAND_REFRESH_TEST);
		this.setToolTipText("");		
	}
//
//	private void createTestLineMouseListener() {
//		this.addMouseListener(new MouseAdapter() {
//
//			public void mousePressed(MouseEvent e) {
//				int x = e.getX();
//				int y = e.getY();
//				if (SwingUtilities.isLeftMouseButton(e)) {
//					TestLine.this.selectedTest = null;
//					if (!TestLine.this.timeItems.isEmpty()) {
//						if (!selectTest(x, y, TestLine.this.timeItems)) {
//							if (!selectTest(x, y, TestLine.this.unsavedTestTimeItems)) {								
//								try {
//									TestLine.this.schedulerModel.setSelectedTest(null);	
//								} catch (ApplicationException e1) {
//									SchedulerModel.showErrorMessage(TestLine.this, e1);
//								}
//							}
//						}
//					}
//				}
//			}
//		});
//	}

	boolean selectTest(int x,
					int y,
					Collection collection) {
		boolean selected = false;
		for (Iterator it = collection.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.next();
//			Log.debugMessage("TestLine.selectTest | testTimeItem.x  " + testTimeItem.x , Log.FINEST);
//			Log.debugMessage("TestLine.selectTest | x  " + x , Log.FINEST);
//			Log.debugMessage("TestLine.selectTest | testTimeItem.x + testTimeItem.width " + (testTimeItem.x + testTimeItem.width), Log.FINEST);
			if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.getWidth()) {
				try {
					if (this.selectedTestIds == null) {
						this.selectedTestIds = new HashSet();
					}
					Test test =(Test)testTimeItem.object;
					this.selectedTestIds.add(test.getId());
					this.skip = true;
					this.schedulerModel.addSelectedTest(test);
					this.skip = false;
//					Log.debugMessage("TestLine.selectTest | select " + ((Test) testTimeItem.object).getId(), Log.FINEST);
				} catch (ApplicationException e) {
					SchedulerModel.showErrorMessage(this, e);
				}
				this.selectedItems.add(testTimeItem);
//				Log.debugMessage("TestLine.selectTest | selectedItems.size() " + selectedItems.size(), Log.FINEST);
				selected = true;
				break;
			}
		}
		return selected;
	}

	public Rectangle getVisibleRectangle() {
		Rectangle rectangle = null;
		if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {

			// List testTimeLineList = (List)
			// this.measurements.get(this.selectedTests.getId());
			Identifier testId = (Identifier) this.selectedTestIds.iterator().next();
			for (Iterator iter = this.timeItems.iterator(); iter.hasNext();) {
				TestTimeItem element = (TestTimeItem) iter.next();
				if (((Test) element.object).getId().equals(testId)) {
					int x = element.x;
					rectangle = new Rectangle(x - PlanPanel.MARGIN / 2, 0, element.x, this.getHeight()
							- (this.titleHeight / 2 + 4) - 2);
					break;
				}
			}
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
//				if (this.startPoint != null) {
//				Log.debugMessage("TestLine.paintFlash | " + testTimeItem.x, Log.FINEST);
//				Log.debugMessage("TestLine.paintFlash | testTimeItem >  " + ((Test) testTimeItem.object).getId(), Log.FINEST);
//				}
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.getWidth(), h, this.flash
						? (((this.selectedTestIds == null) || (!this.selectedTestIds.contains(test.getId())))
								? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_SCHEDULED_SELECTED)
						: SchedulerModel.COLOR_UNRECOGNIZED);
			}

		}
	}

	public void updateTest() {
		if (this.skip) {
			return;
		}
		
//		Log.debugMessage("TestLine.updateTest | ", Log.FINEST);
//		Test test = this.schedulerModel.getSelectedTest();
		Set selectedTestIds2 = this.schedulerModel.getSelectedTestIds();
		this.selectedItems.clear();
		if (this.selectedTestIds != null) {
			this.selectedTestIds.clear();
		}
		if (selectedTestIds2 != null) {
			for (Iterator iterator = selectedTestIds2.iterator(); iterator.hasNext();) {
				Identifier testId = (Identifier) iterator.next();
				if (this.testIds.contains(testId)) {
					if (this.selectedTestIds == null) {
						this.selectedTestIds = new HashSet();
					}
					this.selectedTestIds.add(testId);
//					Log.debugMessage("TestLine.updateTest | add testId " + testId, Log.FINEST);
				}
			}
			
			if (this.selectedTestIds != null && !this.selectedTestIds.isEmpty()) {
				for (Iterator it = this.unsavedTestTimeItems.iterator(); it.hasNext();) {
					TestTimeItem testTimeItem = (TestTimeItem) it.next();
					Test test1 = (Test) testTimeItem.object;
					for (Iterator iterator = this.selectedTestIds.iterator(); iterator.hasNext();) {
						Identifier identifier = (Identifier) iterator.next();
						if (test1.getId().equals(identifier)) {
//							Log.debugMessage("TestLine.updateTest | selectedItems add " + identifier, Log.FINEST);
							this.selectedItems.add(testTimeItem);
							break;
						}
					}
					
				}
				
				for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
					TestTimeItem testTimeItem = (TestTimeItem) it.next();
					Test test1 = (Test) testTimeItem.object;
					for (Iterator iterator = this.selectedTestIds.iterator(); iterator.hasNext();) {
						Identifier identifier = (Identifier) iterator.next();
						if (test1.getId().equals(identifier)) {
//							Log.debugMessage("TestLine.updateTest | selectedItems add " + identifier, Log.FINEST);
							this.selectedItems.add(testTimeItem);
							break;
						}
					}
					
				}
			}
		} 
		
//		Log.debugMessage("TestLine.updateTest | this.selectedTest is "
//				+ (this.selectedTest == null ? "null" : this.selectedTest.getId().toString()), Log.FINEST);
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

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int y = this.titleHeight / 2 + 4;
		int h = this.getHeight() - y - 2;

		if (!this.timeItems.isEmpty() && super.scale > 0.0) {
			for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();				
				this.drawItemRect(g, testTimeItem.x, y, testTimeItem.getWidth(), h, (this.selectedTestIds == null)
						|| (!this.selectedTestIds.contains(((Test)testTimeItem.object).getId()))
						? testTimeItem.color : testTimeItem.selectedColor);
			}
		}
		this.paintFlash(g);
	}
	
	private void createMouseListener() {
		this.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
//				Log.debugMessage("TimeStampsEditor.mouseClicked | ", Log.FINEST);
				int x = e.getX();
				int y = e.getY();

				if (SwingUtilities.isLeftMouseButton(e)) {
					if (TestLine.this.selectedItems != null && !e.isShiftDown()) {
						TestLine.this.selectedItems.clear();
						if (TestLine.this.selectedTestIds != null) {
							TestLine.this.selectedTestIds.clear();
						}
						try {
							TestLine.this.schedulerModel.unselectTests();
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TestLine.this, e1);
						}
					}
					if (!TestLine.this.timeItems.isEmpty()) {
						if (!selectTest(x, y, TestLine.this.timeItems)) {
							if (!selectTest(x, y, TestLine.this.unsavedTestTimeItems)) {								
								try {
									TestLine.this.schedulerModel.unselectTests();	
								} catch (ApplicationException e1) {
									SchedulerModel.showErrorMessage(TestLine.this, e1);
								}
							}
						} 
					} else if (!selectTest(x, y, TestLine.this.unsavedTestTimeItems)) {								
						try {
							TestLine.this.schedulerModel.unselectTests();	
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TestLine.this, e1);
						}
					}
				} else if (SwingUtilities.isRightMouseButton(e)) {
					// popupRelativeX = x;
					// popupMenu.show(TimeStampsEditor.this, x, y);
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (TestLine.this.currentPoint != null && TestLine.this.startPoint != null) {
					long offset = (long) ((TestLine.this.currentPoint.x - TestLine.this.startPoint.x) / scale);
//					moveIntervals(offset);
					
					
					for (Iterator iterator = TestLine.this.selectedItems.iterator(); iterator.hasNext();) {
						TestTimeItem testTimeItem = (TestTimeItem) iterator.next();
						Test test = (Test) testTimeItem.object;
						Date startTime = test.getStartTime();
						Date endTime = test.getEndTime();
						test.setStartTime(new Date(startTime.getTime() + offset));
						if (endTime != null) {
							test.setEndTime(new Date(endTime.getTime() + offset));
						}				
					
					}
//					TestLine.this.selectedTests = null;
//					Log.debugMessage("TestLine.createMouseListener | mouseReleased ", Log.FINEST);
					TestLine.this.dispatcher.notify(new OperationEvent(this, 0, SchedulerModel.COMMAND_REFRESH_TIME_STAMPS));

					TestLine.this.startPoint = null;
					TestLine.this.previousPoint = null;
					TestLine.this.currentPoint = null;
				}

			
			}
		});

		this.addMouseMotionListener(new MouseMotionAdapter() {

			public void mouseDragged(MouseEvent e) {
				TestLine.this.currentPoint = e.getPoint();
				if (TestLine.this.startPoint == null) {
					TestLine.this.startPoint = e.getPoint();
					TestLine.this.previousPoint = TestLine.this.startPoint;
//					Log.debugMessage(".mouseDragged | 1", Log.FINEST);
					return;
				}
				if (TestLine.this.previousPoint != null && TestLine.this.selectedItems != null
						&& !TestLine.this.selectedItems.isEmpty()) {
//					SwingUtilities.invokeLater(new Runnable() {
//
//						public void run() {
							int dx = (TestLine.this.currentPoint.x - TestLine.this.previousPoint.x);
							
							if (dx == 0) {
//								Log.debugMessage(".mouseDragged | dx = 0 ", Log.FINEST);
								return;
							}
							
							for (Iterator it = TestLine.this.selectedItems.iterator(); it.hasNext();) {
								TestTimeItem testTimeItem = (TestTimeItem) it.next();
								if (testTimeItem.object instanceof StorableObject && !((StorableObject)testTimeItem.object).isChanged()) {
									continue;
								}
								testTimeItem.x += dx;
//								Log.debugMessage(".mouseDragged | dx is " + dx, Log.FINEST);
//								Log.debugMessage(".mouseDragged | testTimeItem.x " + testTimeItem.x, Log.FINEST);
//								Log.debugMessage(".mouseDragged | testTimeItem >  " + ((Test) testTimeItem.object).getId(), Log.FINEST);
							}
							TestLine.this.repaint();
							TestLine.this.revalidate();
							TestLine.this.previousPoint = TestLine.this.currentPoint;
						}
//				else {
//							Log.debugMessage(".mouseDragged | TestLine.this.previousPoint != null " + (TestLine.this.previousPoint != null), Log.FINEST);
//							Log.debugMessage(".mouseDragged | TestLine.this.selectedItems != null " + (TestLine.this.selectedItems != null), Log.FINEST);
//							Log.debugMessage(".mouseDragged | isEmpty " + (TestLine.this.selectedItems != null
//									&& !TestLine.this.selectedItems.isEmpty()), Log.FINEST);
//							Log.debugMessage(".mouseDragged | 2", Log.FINEST);
//						}
//					});
//				}
			}
		});
	}

	private void acquireTests() {
		Collection tests1 = this.schedulerModel.getTests();
//		Log.debugMessage("TestLine.acquireTests | test1.size() " + tests1.size(), Log.FINEST);
		this.testIds.clear();
		this.unsavedTests.clear();
		this.measurements.clear();
		for (Iterator it = tests1.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
//			Log.debugMessage("TestLine.acquireTests | test is " + test.getId(), Log.FINEST);
			if (test.getMonitoredElement().getId().equals(this.monitoredElementId)) {
				this.testIds.add(test.getId());
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
					testTimeLine.haveMeasurement = false;
					switch (test.getTemporalType().value()) {
						case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
						{
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
				//					MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
//							.getStorableObject((Identifier) test.getMeasurementSetupIds().iterator().next(), true);
//				switch (test.getTemporalType().value()) {
//					case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
////							Set times = ((AbstractTemporalPattern) MeasurementStorableObjectPool.getStorableObject(test
////									.getTemporalPatternId(), true)).getTimes(test.getStartTime(), test.getEndTime());
////							List addMeasurementTestList = new LinkedList();
////							for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
////								Date date = (Date) timeIt.next();
////								long time = date.getTime();
////								boolean found = false;
////								for (Iterator iter = measurementTestList.iterator(); iter.hasNext();) {
////									TestTimeLine testTimeLine = (TestTimeLine) iter.next();
////									if (Math.abs(testTimeLine.startTime - time) < ONE_MINUTE) {
////										found = true;
////										break;
////									}
////								}
////								if (!found) {
////									TestTimeLine testTimeLine = new TestTimeLine();
////									testTimeLine.test = test;
////									testTimeLine.startTime = time;
////									testTimeLine.duration = measurementSetup.getMeasurementDuration();
////									testTimeLine.haveMeasurement = false;
////									addMeasurementTestList.add(testTimeLine);
////								}
////							}
////							measurementTestList.addAll(addMeasurementTestList);
//					{
//						TestTimeLine testTimeLine = new TestTimeLine();
//						testTimeLine.test = test;
//						testTimeLine.startTime = test.getStartTime().getTime();
//						testTimeLine.duration = test.getEndTime().getTime() - testTimeLine.startTime;
////							Log.debugMessage("TestLine.acquireTests | " + testTimeLine.duration, Log.FINEST);
//						testTimeLine.haveMeasurement = false;
//						measurementTestList.add(testTimeLine);
//					}
//						
//						
//						break;
//					default:
//						TestTimeLine testTimeLine = new TestTimeLine();
//						testTimeLine.test = test;
//						testTimeLine.startTime = test.getStartTime().getTime();
//						testTimeLine.duration = 0;
//						measurementTestList.add(testTimeLine);
//						break;
//
//				}
				Collections.sort(measurementTestList);

			}
		}

		this.updateScale();
		this.refreshTimeItems();
	}

	void refreshTimeItems() {
		
		this.timeItems.clear();
		if (this.scale <= 0.0 || super.start == 0 || super.end == 0) {
			return;
		}

//		try {
//			throw new Exception();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
		for (Iterator iterator = MeasurementStorableObjectPool.getStorableObjects(this.testIds, true).iterator(); iterator.hasNext();) {
			Test test = (Test) iterator.next();

			int i = 0;

			List testTimeLineList = (List) this.measurements.get(test.getId());
			if (testTimeLineList == null)
				return;

			Color selectedColor = null;
			Color unselectedColor = null;
//			Log.debugMessage("TestLine.refreshTimeItems | testTimeLineList.size " + testTimeLineList.size(), Log.FINEST);
			
			for (Iterator it = testTimeLineList.iterator(); it.hasNext(); i++) {
				TestTimeLine testTimeLine = (TestTimeLine) it.next();

				TestTimeItem testTimeItem = new TestTimeItem();
//				Log.debugMessage("TestLine.refreshTimeItems | " + testTimeLine.test.getId(), Log.FINEST);

				testTimeItem.x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
//				Log.debugMessage("TestLine.refreshTimeItems | testTimeLine.duration " + testTimeLine.duration, Log.FINEST);
				int width = (int) (this.scale * testTimeLine.duration);
//				Log.debugMessage("TestLine.refreshTimeItems | scale " +scale , Log.FINEST);

//				Log.debugMessage("TestLine.refreshTimeItems | width  " + width, Log.FINEST);

				width = width > this.minimalWidth ? width : this.minimalWidth;
//				Log.debugMessage("TestLine.refreshTimeItems | width  " + width, Log.FINEST);
				testTimeItem.setWidth(width);
				
				

//				Log.debugMessage("TestLine.refreshTimeItems |testTimeItem.width  " + testTimeItem.width, Log.FINEST);
				
				if (testTimeLine.haveMeasurement) {
					selectedColor = SchedulerModel.COLOR_COMPLETED_SELECTED;
					unselectedColor = SchedulerModel.COLOR_COMPLETED;
				} else {
					selectedColor = SchedulerModel.getColor(test.getStatus(), true);
					unselectedColor = SchedulerModel.getColor(test.getStatus(), false);
				}
//				Log.debugMessage("TestLine.refreshTimeItems | test is " + test.getId(), Log.FINEST);
				testTimeItem.object = test;
				testTimeItem.selectedColor = selectedColor;
				testTimeItem.color = unselectedColor;

				this.timeItems.add(testTimeItem);
			}

		}
		
//		Log.debugMessage("TestLine.refreshTimeItems | timeItems.size " + timeItems.size(), Log.FINEST);
		
		TestTimeItem prevItem = null;
		for (Iterator it = this.timeItems.iterator(); it.hasNext();) {
			TestTimeItem testTimeItem = (TestTimeItem) it.next();
			if (prevItem != null && (testTimeItem.x - (prevItem.x + prevItem.getWidth())) < 0) {
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
		} catch(ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
		super.repaint();
		super.revalidate();
	}
	
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
	
	private String getTitle(int x, Collection collection) {
//		Log.debugMessage("TestLine.getTitle | collection.size() " + collection.size(), Log.FINEST);
		if (!collection.isEmpty()) {
			for (Iterator it = collection.iterator(); it.hasNext();) {
				TestTimeItem testTimeItem = (TestTimeItem) it.next();
				if (testTimeItem.x < x && x < testTimeItem.x + testTimeItem.getWidth()) {
					Object object = testTimeItem.object;
					SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					if (object instanceof Test) {
						Test test = (Test)object;
						return sdf
						.format(test.getStartTime());
					}
					return sdf
							.format(new Date((long) (this.start + ((testTimeItem.x - PlanPanel.MARGIN / 2) / this.scale))));
				}
			}
		}
		return null;
	}

}
