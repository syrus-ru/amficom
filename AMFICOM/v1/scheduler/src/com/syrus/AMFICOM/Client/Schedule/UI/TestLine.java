
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.Timer;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestEditor;
import com.syrus.AMFICOM.Client.Schedule.TestsEditor;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

public class TestLine extends JLabel implements ActionListener, TestsEditor, TestEditor {

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

	Collection					tests					= new LinkedList();

	Dispatcher					dispatcher;

	boolean						flash						= false;
	int							height;
	int							margin;
	double						scale;
	SchedulerModel				schedulerModel;
	Test						selectedTest;

	int							titleHeight;
	Collection					unsavedTests					= new LinkedList();

	int							width;

	private long				end;

	// Map <Identifier testId, List<TestTimeLine>>
	private Map					measurements				= new HashMap();
	private Identifier			monitoredElementId;
	private long				start;

	private Timer				timer;

	private String				title;

	public TestLine(ApplicationContext aContext,
			String title,
			long start,
			long end,
			int margin,
			Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestsEditor(this);
		this.schedulerModel.addTestEditor(this);
		this.title = title;
		this.start = start;
		this.end = end;
		this.margin = margin;
		this.monitoredElementId = monitoredElementId;
//		initModule(aContext.getDispatcher());
		this.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (!TestLine.this.tests.isEmpty()) {
					int x = e.getX();
					int y = e.getY();

					for (Iterator it = TestLine.this.tests.iterator(); it.hasNext();) {
						// Test selectedTest = (Test) it.next();
						Test test = (Test) it.next();
						TestTemporalType temporalType = test.getTemporalType();
						int st = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getStartTime().getTime() - TestLine.this
										.getStart())) - 1;
						int en = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getEndTime().getTime() - TestLine.this.getStart()))
								+ 1;
						en = (en - st < MINIMAL_WIDTH) ? st + MINIMAL_WIDTH : en;
						// System.out.println("."+((x >= st) && (x <= en) && (y
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
										// System.out.println("selected:" + j);
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
							try {
								TestLine.this.schedulerModel.setSelectedTest(test);
							} catch (ApplicationException e1) {
								SchedulerModel.showErrorMessage(TestLine.this, e1);
							}
							break;
						}
					}
				}

			}

		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (TestLine.this.selectedTest != null) {
					// nothing
				}

			}

			public void mouseMoved(MouseEvent e) {
				// nothing
			}
		});

		this.acquireTests();
	}

	public void actionPerformed(ActionEvent e) {
		// System.out.println("actionPerformed:");
		flashUnsavedTest();
	}

	public void flashUnsavedTest() {

		if ((this.isVisible()) && (!this.unsavedTests.isEmpty())) {
			Graphics g = this.getGraphics();
			if (g != null) {
				this.flash = !this.flash;
				for (Iterator it = this.unsavedTests.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					g.setColor(this.flash ? (((this.selectedTest == null) || (!this.selectedTest.getId().equals(
						test.getId()))) ? COLOR_SCHEDULED : COLOR_SCHEDULED_SELECTED) : COLOR_UNRECOGNIZED);
					drawTestRect(g, test);
				}
			}
		}

	}
	
	public void updateTest() {
		Test test = this.schedulerModel.getSelectedTest();
		if (test == null) {
			this.selectedTest = test;			
		} else if (((this.selectedTest != null && !this.selectedTest.getId().equals(test.getId())) || this.selectedTest == null)
				&& (this.tests.contains(test) || this.unsavedTests.contains(test))) {
			this.selectedTest = test;
			this.repaint();
		}		
	}
	
	public void updateTests() {
		this.acquireTests();
		Test test = this.schedulerModel.getSelectedTest();
		if (test == null) {
			this.selectedTest = test;			
		} else if (((this.selectedTest != null && !this.selectedTest.getId().equals(test.getId())) || this.selectedTest == null)
				&& (this.tests.contains(test) || this.unsavedTests.contains(test))) {
			this.selectedTest = test;
			this.repaint();
		}
	}
	

	public void paintComponent(Graphics g) {
		this.height = getHeight();
		this.width = getWidth();

		if (this.schedulerModel.getTests() != null) {
			this.scale = (double) (this.width - 2 * this.margin) / (double) (this.end - this.start);
			Font font = UIStorage.ARIAL_12_FONT;
			g.setFont(font);
			this.titleHeight = g.getFontMetrics().getHeight();
			g.setColor(Color.gray);
			g.clearRect(0, 0, this.width, this.titleHeight / 2 + 3);
			g.setColor(Color.black);
			g.drawString(this.title, 5, this.titleHeight / 2 + 2);
			g.drawLine(0, this.titleHeight / 2 + 3, this.width, this.titleHeight / 2 + 3);
			g.drawLine(0, this.height - 1, this.width, this.height - 1);
			synchronized (this.tests) {
				for (Iterator it = this.tests.iterator(); it.hasNext();) {
					Color color;
					Test test = (Test) it.next();
					if (test.isChanged())
						continue;
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
					g.setColor(color);
					drawTestRect(g, test);
				}
			}
		}
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

//	public void unregisterDispatcher() {
//		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TEST);
//		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TESTS);
//	}

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

	private void acquireTests() {
		Collection tests = this.schedulerModel.getTests();
		this.tests.clear();
		this.unsavedTests.clear();
		this.measurements.clear();
		for (Iterator it = tests.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			if (test.getMonitoredElement().getId().equals(this.monitoredElementId)) {
				this.tests.add(test);
				if (test.isChanged())
					this.unsavedTests.add(test);
				else {					
					LinkedIdsCondition linkedIdsCondition = new LinkedIdsCondition(
																					test.getId(),
																					ObjectEntities.MEASUREMENT_ENTITY_CODE);
					try {
						Collection testMeasurements = MeasurementStorableObjectPool.getStorableObjectsByCondition(
							linkedIdsCondition, true);
						List measurementTestList = new LinkedList();
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
					testTimeLine.duration = (test.getEndTime() != null) ? test.getEndTime().getTime() - testTimeLine.startTime : 0;
					testTimeLine.haveMeasurement = false;
					measurementTestList.add(testTimeLine);
					this.measurements.put(test.getId(), measurementTestList);
				}

				try {
					MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
							.getStorableObject((Identifier) test.getMeasurementSetupIds().iterator().next(), true);
					switch (test.getTemporalType().value()) {
						case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
							List times = test.getTemporalPattern().getTimes(test.getStartTime(), test.getEndTime());
							List addMeasurementTestList = new LinkedList();
							for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
								Date date = (Date) timeIt.next();
								long time = date.getTime();
								boolean found = false;
								for (Iterator iter = measurementTestList.iterator(); iter.hasNext();) {
									TestTimeLine testTimeLine = (TestTimeLine) iter.next();
									if (testTimeLine.startTime == time) {
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
							testTimeLine.duration = (test.getEndTime() != null) ? test.getEndTime().getTime() - testTimeLine.startTime : 0;
							break;

					}
					Collections.sort(measurementTestList);
				} catch (ApplicationException ae) {
					SchedulerModel.showErrorMessage(this, ae);
				}
				// ///

			}
		}

		if (!this.unsavedTests.isEmpty()) {
			if (this.timer == null) {
				this.timer = new Timer(TIME_OUT, this);
			}
			this.timer.restart();
		} else
			this.timer.stop();

	}

	private void drawTestRect(	Graphics g,
								Test test) {
		// System.out.println("drawTestRect:"+selectedTest.getId());
		int y = this.titleHeight / 2 + 4;
		int h = this.height - (this.titleHeight / 2 + 4) - 2;
		// System.out.println(">>"+timeStamp.getType());

		/**
		 * TODO remove when will enable again
		 */
		// ElementaryTestAlarm[] testAlarms =
		// selectedTest.getElementaryTestAlarms();
		/**
		 * TODO remove when ok
		 */
		// ElementaryTestAlarm[] testAlarms = new ElementaryTestAlarm[0];
		// switch (temporalType.value()) {
		// case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
		int i = 0;

		// List times =
		// selectedTest.getTemporalPattern().getTimes(selectedTest.getStartTime(),selectedTest.getEndTime());
		// for (Iterator timeIt = times.iterator(); timeIt.hasNext();i++) {
		// Date time = (Date) timeIt.next();
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

			/**
			 * TODO remove when ok
			 */

			// if (testAlarms.length > 0) {
			// for (int j = 0; j < testAlarms.length; j++) {
			// if (Math.abs(testAlarms[j].elementary_start_time -
			// testTimeLine.startTime) < 1000 * 30) {
			// Alarm alarm = (Alarm) Pool.get(Alarm.typ,
			// testAlarms[j].alarm_id);
			// if (alarm != null) {
			// // System.out.println("alarm.type_id:" +
			// // alarm.type_id);
			// if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_ALARM)) {
			// // System.out.println("ID_RTU_TEST_ALARM");
			// if ((this.selectedTest != null) &&
			// (this.selectedTest.getId().equals(test.getId())))
			// g.setColor(TestLine.COLOR_ALARM_SELECTED);
			// else
			// g.setColor(TestLine.COLOR_ALARM);
			// } else if
			// (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_WARNING)) {
			// // System.out.println("ID_RTU_TEST_WARNING");
			// if ((this.selectedTest != null) &&
			// (this.selectedTest.getId().equals(test.getId())))
			// g.setColor(TestLine.COLOR_WARNING_SELECTED);
			// else
			// g.setColor(TestLine.COLOR_WARNING);
			// }
			// }
			//
			// }
			// // System.out.println(
			// // (testAlarms[j].elementary_start_time-times[i]));
			//
			// // alarm.
			// }
			// }
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
				// g.drawLine(x + 2, y + h / 4, x + w - 4, y + h / 4);
				g.drawRect(x + w / 2, y + 1, 1, h - 2);
				// g.drawString("T", x, y);
				g.setColor(c);
			}
			if (testTimeLine.haveMeasurement) {
				g.setColor(mColor);
			}
		}

	}

//	private void initModule(Dispatcher dispatcher) {
//		this.dispatcher = dispatcher;
//		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TEST);
//		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TESTS);
//	}
}