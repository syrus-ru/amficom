
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import com.syrus.AMFICOM.CORBA.Constant.AlarmTypeConstants;
import com.syrus.AMFICOM.CORBA.Survey.ElementaryTestAlarm;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.TestUpdateEvent;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

public class TestLine extends JLabel implements ActionListener, OperationListener {

	/**
	 * @TODO recast using alpha
	 */
	public static final Color	COLOR_ABORDED				= Color.RED;
	public static final Color	COLOR_ABORDED_SELECTED		= new Color(255, 128, 128);

	public static final Color	COLOR_ALARM					= new Color(128, 0, 128);
	public static final Color	COLOR_ALARM_SELECTED		= new Color(255, 0, 255);
	public static final Color	COLOR_COMPLETED				= new Color(0, 128, 0);
	public static final Color	COLOR_COMPLETED_SELECTED	= new Color(0, 255, 0);
	public static final Color	COLOR_PROCCESSING			= new Color(0, 128, 128);
	public static final Color	COLOR_PROCCESSING_SELECTED	= new Color(64, 192, 192);

	public static final Color	COLOR_SCHEDULED				= new Color(128, 128, 128);
	public static final Color	COLOR_SCHEDULED_SELECTED	= Color.WHITE;
	public static final Color	COLOR_UNRECOGNIZED			= new Color(20, 20, 60);

	public static final Color	COLOR_WARNING				= new Color(128, 128, 0);
	public static final Color	COLOR_WARNING_SELECTED		= Color.YELLOW;

	public static final int		MINIMAL_WIDTH				= 7;

	public static final int		TIME_OUT					= 500;
	ArrayList					allTests;
	//Test currentTest;

	Dispatcher					dispatcher;

	boolean						flash						= false;
	int							height;
	int							margin;
	double						scale;
	Test						selectedTest;

	int							titleHeight;

	int							width;

	private long				end;
	private long				start;

	private HashMap				tests						= new HashMap();
	private javax.swing.Timer	timer;

	private String				title;

	private HashMap				unsavedTests;

	// Map <Identifier testId, List<TestTimeLine>>
	private Map					measurements;

	private class TestTimeLine implements Comparable {

		protected long		startTime;
		protected long		duration;
		protected Test		test;
		protected boolean	haveMeasurement;

		public int compareTo(Object o) {
			if (o instanceof TestTimeLine) {
				TestTimeLine testTimeLine = (TestTimeLine) o;
				return this.startTime < testTimeLine.startTime ? -1 : 1;
			}
			return 0;
		}

	}

	public TestLine(ApplicationContext aContext, String title, long start, long end, int margin) {
		//this.aContext = aContext;
		this.title = title;
		this.start = start;
		this.end = end;
		this.margin = margin;
		initModule(aContext.getDispatcher());
		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				//				nothing
			}

			public void mouseEntered(MouseEvent e) {
				//				nothing
			}

			public void mouseExited(MouseEvent e) {
				//				nothing
			}

			public void mousePressed(MouseEvent e) {
				if (TestLine.this.allTests != null) {
					int x = e.getX();
					int y = e.getY();
					//int width = getWidth();
					//				System.out.println("mousePressed: (" + x + "," + y +
					// ")");
					// double scale = (double) (width - 2 * margin)/ (double)
					// (end - start);

					//for (Iterator it = getTests().iterator(); it.hasNext();)
					// {
					for (Iterator it = TestLine.this.allTests.iterator(); it.hasNext();) {
						//	Test selectedTest = (Test) it.next();
						Test test = (Test) it.next();
						TestTemporalType temporalType = test.getTemporalType();
						int st = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getStartTime().getTime() - TestLine.this
										.getStart())) - 1;
						int en = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getEndTime().getTime() - TestLine.this.getStart()))
								+ 1;
						en = (en - st < MINIMAL_WIDTH) ? st + MINIMAL_WIDTH : en;
						//					System.out.println("."+((x >= st) && (x <= en) && (y
						// >=
						// titleHeight / 2 + 4)));
						int w = en - st + 1;
						if (temporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS)
							w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
						else
							w = MINIMAL_WIDTH;
						en = st + w;
						boolean condition = false;
						switch (temporalType.value()) {
							case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
								List times = test.getTemporalPattern().getTimes(test.getStartTime(), test.getEndTime());
								for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
									Date time = (Date) timeIt.next();
									st = TestLine.this.margin
											+ (int) (TestLine.this.scale * (time.getTime() - TestLine.this.getStart()));
									en = st + w;
									if ((x >= st) && (x <= en) && (y >= TestLine.this.titleHeight / 2 + 4)) {
										condition = true;
										//System.out.println("selected:" + j);
										break;
									}
								}
								break;
							default:
								if ((x >= st) && (x <= en) && (y >= TestLine.this.titleHeight / 2 + 4)) {
									condition = true;
								}
								break;
						}

						if (condition) {
							//System.out.println("selectedTest:" +
							// selectedTest.id);
							//							System.out.println("selectedTest.status.value():"
							//									+ selectedTest.status.value());
							//							System.out.println("TestLine>onClick:
							// selectedTest==null
							// : " //$NON-NLS-1$
							//									+ (selectedTest.isChanged()));
							//TestLine.this.skipTestUpdate = true;
							TestLine.this.dispatcher.notify(new TestUpdateEvent(this, test,
																				TestUpdateEvent.TEST_SELECTED_EVENT));
							//TestLine.this.skipTestUpdate = false;
							//TestLine.this.selectedTest = selectedTest;
							break;
						}
					}
				}

			}

			public void mouseReleased(MouseEvent e) {
				//				if (TestLine.this.selectedTest != null) {
				//					TestLine.this.selectedTest = null;
				//				}

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (TestLine.this.selectedTest != null) {
					//nothing
				}

			}

			public void mouseMoved(MouseEvent e) {
				//				nothing
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		//System.out.println("actionPerformed:");
		flashUnsavedTest();
	}

	public void addTest(Identifier id) throws DatabaseException, CommunicationException {
		Test test = (Test) MeasurementStorableObjectPool.getStorableObject(id, true);
		if (test != null)
			addTest(test);
	}

	public synchronized void addTest(Test test) {
		//		if (!this.skipTestUpdate) {
		if (test.isChanged()) {
			//System.out.println("selectedTest is changed");
			if (this.unsavedTests == null) {
				this.unsavedTests = new HashMap();
				if (this.timer == null) {
					this.timer = new javax.swing.Timer(TIME_OUT, this);
					//System.out.println("timer created");

				}
			}
			if (this.unsavedTests.containsValue(test)) {
				//System.out.println("unsavedTests.contains(selectedTest)");
				// //$NON-NLS-1$
			} else {
				//System.out.println("unsavedTests.put(" + selectedTest.getId()
				// + ")");
				this.unsavedTests.put(test.getId(), test);
			}
			this.timer.restart();
		} else {
			//System.out.println("selectedTest is NOT changed");
			this.tests.put(test.getId(), test);
			LinkedIdsCondition linkedIdsCondition = LinkedIdsCondition.getInstance();
			linkedIdsCondition.setEntityCode(ObjectEntities.MEASUREMENT_ENTITY_CODE);
			linkedIdsCondition.setIdentifier(test.getId());
			try {
				List measurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(linkedIdsCondition,
																								true);
				if (this.measurements == null)
					this.measurements = new HashMap();

				List measurementTestList = new LinkedList();
				for (Iterator it = measurements.iterator(); it.hasNext();) {
					Measurement measurement = (Measurement) it.next();
					TestTimeLine testTimeLine = new TestTimeLine();
					testTimeLine.test = test;
					testTimeLine.startTime = measurement.getStartTime().getTime();
					testTimeLine.duration = measurement.getDuration();
					testTimeLine.haveMeasurement = true;
					measurementTestList.add(testTimeLine);
				}
				this.measurements.put(test.getId(), measurementTestList);
			} catch (ApplicationException e) {
				SchedulerModel.showErrorMessage(this, e);
			}
		}

		Color color;
		switch (test.getStatus().value()) {
			case TestStatus._TEST_STATUS_COMPLETED:
				color = COLOR_COMPLETED_SELECTED;
				break;
			case TestStatus._TEST_STATUS_SCHEDULED:
				color = COLOR_SCHEDULED_SELECTED;
				break;
			case TestStatus._TEST_STATUS_PROCESSING:
				color = COLOR_PROCCESSING_SELECTED;
				break;
			case TestStatus._TEST_STATUS_ABORTED:
				color = COLOR_ABORDED_SELECTED;
				break;
			default:
				color = COLOR_UNRECOGNIZED;
				break;
		}

		List measurementTestList = (List) this.measurements.get(test.getId());
		if (measurementTestList == null) {
			measurementTestList = new LinkedList();
			this.measurements.put(test.getId(), measurementTestList);
		}

		try {
			MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
					.getStorableObject((Identifier) test.getMeasurementSetupIds().get(0), true);
			switch (test.getTemporalType().value()) {
				case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
					List times = test.getTemporalPattern().getTimes(test.getStartTime(), test.getEndTime());
					List addMeasurementTestList = new LinkedList();
					for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
						Date time = (Date) timeIt.next();
						long l = time.getTime();
						boolean found = false;
						for (Iterator it = measurementTestList.iterator(); it.hasNext();) {
							TestTimeLine testTimeLine = (TestTimeLine) it.next();
							if (testTimeLine.startTime == l) {
								found = true;
								break;
							}
						}
						if (!found) {
							TestTimeLine testTimeLine = new TestTimeLine();
							testTimeLine.test = test;
							testTimeLine.startTime = l;
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
					testTimeLine.duration = test.getEndTime().getTime() - testTimeLine.startTime;
					break;

			}
			Collections.sort(measurementTestList);
		} catch (ApplicationException ae) {
			SchedulerModel.showErrorMessage(this, ae);
		}

		if (this.allTests == null)
			this.allTests = new ArrayList();
		if (!this.allTests.contains(test))
			this.allTests.add(test);
		this.revalidate();
		//	}
	}

	public void flashUnsavedTest() {
		if ((this.isVisible()) && (this.unsavedTests != null)) {
			Graphics g = this.getGraphics();
			if (g != null) {
				this.flash = !this.flash;
				for (Iterator it = this.unsavedTests.keySet().iterator(); it.hasNext();) {
					Test test = (Test) this.unsavedTests.get(it.next());
					g.setColor(this.flash ? (((this.selectedTest == null) || (!this.selectedTest.getId()
							.equals(test.getId()))) ? COLOR_SCHEDULED : COLOR_SCHEDULED_SELECTED) : COLOR_UNRECOGNIZED);
					drawTestRect(g, test);
				}
			}
		}

	}

	/**
	 * @return Returns the end.
	 */
	long getEnd() {
		return this.end;
	}

	/**
	 * @return Returns the start.
	 */
	long getStart() {
		return this.start;
	}

	//	public Collection getTests() {
	//		return tests.values();
	//	}
	//
	//	public Set getTestIds() {
	//		return tests.keySet();
	//	}

	public Test getTest(Identifier id) {
		Test test = (Test) this.tests.get(id);
		if (test == null) {
			for (Iterator it = this.allTests.iterator(); it.hasNext();) {
				Test t = (Test) it.next();
				if (t.getId().equals(id)) {
					test = t;
					break;
				}

			}
		}

		return test;
	}

	public boolean isEmpty() {
		return this.allTests.isEmpty();
	}

	public void operationPerformed(OperationEvent e) {
		String commandName = e.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_TEST_SAVED_OK)) {
			if (this.unsavedTests != null) {
				for (Iterator it = this.unsavedTests.keySet().iterator(); it.hasNext();) {
					Object key = it.next();
					Test test = (Test) this.unsavedTests.get(key);
					if (!test.isChanged()) {
						//						System.out.println("remove " + key);
						this.unsavedTests.remove(key);
						this.tests.put(test.getId(), test);
					}
				}
			}
		} else if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			if (this.timer != null)
				this.timer.stop();
			if (this.unsavedTests != null)
				this.unsavedTests.clear();
			if (this.tests != null)
				this.tests.clear();
			if (this.measurements != null)
				this.measurements.clear();
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) e;
			Test test = tue.test;
			if ((this.selectedTest == null) || (!this.selectedTest.getId().equals(test.getId()))) {
				this.selectedTest = test;
				//System.out.println("set selectedTest
				// :"+selectedTest.getId());
				this.repaint();
			}
		}
	}

	public void paintComponent(Graphics g) {
		this.height = getHeight();
		this.width = getWidth();

		if (this.allTests != null) {
			this.scale = (double) (this.width - 2 * this.margin) / (double) (this.end - this.start);
			Font font = UIStorage.ARIAL_12_FONT;
			g.setFont(font);
			this.titleHeight = g.getFontMetrics().getHeight();
			//		System.out.println("titleHeight:"+titleHeight);
			g.setColor(Color.gray);
			g.clearRect(0, 0, this.width, this.titleHeight / 2 + 3);
			g.setColor(Color.black);
			g.drawString(this.title, 5, this.titleHeight / 2 + 2);
			g.drawLine(0, this.titleHeight / 2 + 3, this.width, this.titleHeight / 2 + 3);
			g.drawLine(0, this.height - 1, this.width, this.height - 1);
			//for (Iterator it = tests.values().iterator(); it.hasNext();) {
			//			System.out.println(":" + allTests.size() + "\t"
			//					+ System.currentTimeMillis());
			for (Iterator it = this.allTests.iterator(); it.hasNext();) {
				Color color;
				Test test = (Test) it.next();

				if ((this.selectedTest == null) || (!this.selectedTest.getId().equals(test.getId()))) {
					switch (test.getStatus().value()) {
						case TestStatus._TEST_STATUS_COMPLETED:
							color = COLOR_COMPLETED_SELECTED;
							break;
						case TestStatus._TEST_STATUS_SCHEDULED:
							color = COLOR_SCHEDULED_SELECTED;
							break;
						case TestStatus._TEST_STATUS_PROCESSING:
							color = COLOR_PROCCESSING_SELECTED;
							break;
						case TestStatus._TEST_STATUS_ABORTED:
							color = COLOR_ABORDED_SELECTED;
							break;
						default:
							color = COLOR_UNRECOGNIZED;
							break;
					}
				} else {
					switch (test.getStatus().value()) {
						case TestStatus._TEST_STATUS_COMPLETED:
							color = COLOR_COMPLETED;
							break;
						case TestStatus._TEST_STATUS_SCHEDULED:
							color = COLOR_SCHEDULED;
							break;
						case TestStatus._TEST_STATUS_PROCESSING:
							color = COLOR_PROCCESSING;
							break;
						case TestStatus._TEST_STATUS_ABORTED:
							color = COLOR_ABORDED;
							break;
						default:
							color = COLOR_UNRECOGNIZED;
							break;
					}
				}
				//System.out.println("color:" + color);
				if ((this.unsavedTests == null) || (!this.unsavedTests.containsValue(test))) {
					g.setColor(color);
					drawTestRect(g, test);
				}

			}
		}
	}

	public void removeAllTests() {
		synchronized (this) {
			this.tests.clear();
			this.allTests.clear();
			this.unsavedTests.clear();
		}
	}

	public void removeTest(Test test) {
		//Test selectedTest = (Test) this.tests.get(id);
		Identifier testId = test.getId();
		if (this.unsavedTests != null) {
			if (this.unsavedTests.containsKey(testId)) {
				this.timer.stop();
				this.unsavedTests.remove(testId);
				if (!this.unsavedTests.isEmpty())
					this.timer.restart();
				//			System.out.println("this.unsavedTests.remove(" +
				// selectedTest.getId()
				// +
				// "):"
				//					+ this.unsavedTests.containsKey(selectedTest.getId()));
			}
		}
		if (this.allTests != null) {
			//			for (Iterator it = this.allTests.iterator(); it.hasNext();) {
			//				Test t = (Test) it.next();
			//				System.out.println(t.getId());
			//			}
			this.allTests.remove(test);
			//			System.out.println("this.allTests.remove(selectedTest):" +
			// this.allTests.contains(selectedTest));
		}
		this.tests.remove(testId);
		//		System.out.println("this.tests.remove(testId):" +
		// this.tests.containsKey(testId));
	}

	/**
	 * @param end
	 *            The end to set.
	 */
	public void setEnd(long end) {
		this.end = end;
	}

	/**
	 * @param start
	 *            The start to set.
	 */
	public void setStart(long start) {
		this.start = start;
	}

	private void drawTestRect(Graphics g, Test test) {
		//System.out.println("drawTestRect:"+selectedTest.getId());
		int y = this.titleHeight / 2 + 4;
		int h = this.height - (this.titleHeight / 2 + 4) - 2;
		//System.out.println(">>"+timeStamp.getType());

		/**
		 * TODO remove when will enable again
		 */
		//ElementaryTestAlarm[] testAlarms =
		// selectedTest.getElementaryTestAlarms();
		ElementaryTestAlarm[] testAlarms = new ElementaryTestAlarm[0];

		//switch (temporalType.value()) {
		//			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:

		int i = 0;

		//				List times =
		// selectedTest.getTemporalPattern().getTimes(selectedTest.getStartTime(),selectedTest.getEndTime());
		//				for (Iterator timeIt = times.iterator(); timeIt.hasNext();i++) {
		//					Date time = (Date) timeIt.next();
		List testTimeLineList = (List) this.measurements.get(test.getId());
		for (Iterator it = testTimeLineList.iterator(); it.hasNext(); i++) {
			TestTimeLine testTimeLine = (TestTimeLine) it.next();

			int x = this.margin + (int) (this.scale * (testTimeLine.startTime - this.start));
			int en = this.margin + (int) (this.scale * (testTimeLine.startTime + testTimeLine.duration - this.start));
			int w = en - x + 1;
			TestTemporalType temporalType = test.getTemporalType();
			if (temporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS)
				w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
			else
				w = MINIMAL_WIDTH;

			if (testAlarms.length > 0) {
				for (int j = 0; j < testAlarms.length; j++) {
					if (Math.abs(testAlarms[j].elementary_start_time - testTimeLine.startTime) < 1000 * 30) {
						Alarm alarm = (Alarm) Pool.get(Alarm.typ, testAlarms[j].alarm_id);
						if (alarm != null) {
							//System.out.println("alarm.type_id:" +
							// alarm.type_id);
							if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_ALARM)) {
								//System.out.println("ID_RTU_TEST_ALARM");
								if ((this.selectedTest != null) && (this.selectedTest.getId().equals(test.getId())))
									g.setColor(TestLine.COLOR_ALARM_SELECTED);
								else
									g.setColor(TestLine.COLOR_ALARM);
							} else if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_WARNING)) {
								//System.out.println("ID_RTU_TEST_WARNING");
								if ((this.selectedTest != null) && (this.selectedTest.getId().equals(test.getId())))
									g.setColor(TestLine.COLOR_WARNING_SELECTED);
								else
									g.setColor(TestLine.COLOR_WARNING);
							}
						}

					}
					//System.out.println(
					// (testAlarms[j].elementary_start_time-times[i]));

					//alarm.
				}
			}
			x = this.margin + (int) (this.scale * (testTimeLine.startTime - this.start));
			Color mColor = g.getColor();
			if (testTimeLine.haveMeasurement) {
				g.setColor(TestLine.COLOR_COMPLETED);
			}
			g.fillRect(x + 2, y + 2, w - 3, h - 3);
			g.draw3DRect(x, y, w, h, true);
			if (i == 0) {
				Color c = g.getColor();
				g.setColor(new Color(0, 255 - c.getGreen(), 255 - c.getBlue()));
				g.drawRect(x + 1, y + 1, w - 2, h - 2);
				//g.drawLine(x + 2, y + h / 4, x + w - 4, y + h / 4);
				g.drawRect(x + w / 2, y + 1, 1, h - 2);
				//g.drawString("T", x, y);
				g.setColor(c);
			}
			if (testTimeLine.haveMeasurement) {
				g.setColor(mColor);
			}
		}

	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_TEST_SAVED_OK);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

	public void unregisterDispatcher() {
		this.dispatcher.unregister(this, TestUpdateEvent.TYPE);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_TEST_SAVED_OK);
		this.dispatcher.unregister(this, SchedulerModel.COMMAND_CLEAN);
	}
}