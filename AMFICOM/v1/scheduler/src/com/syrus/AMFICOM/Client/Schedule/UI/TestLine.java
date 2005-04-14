
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.UIManager;

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
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;

public class TestLine extends JLabel implements TestsEditor, TestEditor {

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

	Collection					tests						= new LinkedList();

	Dispatcher					dispatcher;

	boolean						flash						= false;
	int							height;
	double						scale;
	SchedulerModel				schedulerModel;
	Test						selectedTest;

	int							titleHeight;
	Collection					unsavedTests				= new LinkedList();

	int							width;

	private long				end;

	Map					measurements				= new HashMap();
	private Identifier			monitoredElementId;
	long				start;

	private String				title;
	
	int minimalWidth = 0;
	
	private static final long ONE_MINUTE = 60L * 1000L;
	
	private MouseListener testLineMouseListener;

	public TestLine(ApplicationContext aContext,
			String title,
			Identifier monitoredElementId) {
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestsEditor(this);
		this.schedulerModel.addTestEditor(this);
		this.title = title;
		this.monitoredElementId = monitoredElementId;
		this.acquireTests();
		Font font2 = UIManager.getLookAndFeelDefaults().getFont("Button.font");
		FontMetrics fontMetrics = this.getFontMetrics(font2);
		this.minimalWidth = fontMetrics.charWidth('W');
	}
	
	public MouseListener getTestLineMouseListener() {
		if (this.testLineMouseListener == null) {
			this.testLineMouseListener = new MouseAdapter() {

				public void mousePressed(MouseEvent e) {
					if (!TestLine.this.tests.isEmpty()) {
						try {
							int x = e.getX();
							int y = e.getY();

							for (Iterator iter = TestLine.this.tests.iterator(); iter.hasNext();) {
								// Test selectedTest = (Test) it.next();
								Test test = (Test) iter.next();
								TestTemporalType temporalType = test.getTemporalType();
								List testTimeLineList = (List) TestLine.this.measurements.get(test.getId());
								for (Iterator it = testTimeLineList.iterator(); it.hasNext();) {
									TestTimeLine testTimeLine = (TestTimeLine) it.next();
								
								
//								System.out.println("test: "
//										+ test.getId()
//										+ " ( "
//										+ test.getStartTime()
//										+ ", "
//										+ test.getEndTime());
								
								int st = PlanPanel.MARGIN / 2 + (int) (TestLine.this.scale * (testTimeLine.startTime - TestLine.this.start));
								int en = PlanPanel.MARGIN / 2 + (int) (TestLine.this.scale * (testTimeLine.startTime + testTimeLine.duration - TestLine.this.start));
								
								en = (en - st < minimalWidth) ? st + minimalWidth : en;
								// System.out.println("."+((x >= st) && (x <=
								// en) && (y
								// >=
								// titleHeight / 2 + 4)));
								int w = en - st + 1;
								en = st + w;
								boolean condition = false;
								switch (temporalType.value()) {
									case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
										Set times = ((TemporalPattern) MeasurementStorableObjectPool
												.getStorableObject(test.getTemporalPatternId(), true)).getTimes(test
												.getStartTime(), test.getEndTime());
										for (Iterator timeIt = times.iterator(); timeIt.hasNext();) {
											Date time = (Date) timeIt.next();
											st = PlanPanel.MARGIN
													/ 2
													+ (int) (TestLine.this.scale * (time.getTime() - TestLine.this
															.getStart()));
											en = st + w;
											if ((x >= st) && (x <= en) && (y >= TestLine.this.titleHeight / 2 + 4)) {
												condition = true;
												// System.out.println("selected:"
												// + j);
												break;
											}
										}
										break;
									default:
//										System.out.println("search at (" + st + " .. " + en + ", " + TestLine.this.titleHeight / 2 + 4 + ") yours (" + x + ", " + y + ")");
										if ((x >= st) && (x <= en) && (y >= TestLine.this.titleHeight / 2 + 4)) {
											condition = true;
										}
										break;
								}

								if (condition) {
									TestLine.this.schedulerModel.setSelectedTest(test);

									break;
								}
							}
							}
						} catch (ApplicationException e1) {
							SchedulerModel.showErrorMessage(TestLine.this, e1);
						}
					}

				}
			};
		}
		return this.testLineMouseListener;

	}
	
	public Rectangle getVisibleRectangle() {
		Rectangle rectangle = null;
		if (this.selectedTest != null) {
			List testTimeLineList = (List) this.measurements.get(this.selectedTest.getId());
			TestTimeLine testTimeLine = (TestTimeLine)testTimeLineList.get(0);
			int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
			int en = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime + testTimeLine.duration - this.start));
			rectangle = new Rectangle(x - PlanPanel.MARGIN / 2, 0, en - x, this.height - (this.titleHeight / 2 + 4) - 2);
		}
		return rectangle;
	}

	private void paintFlash(Graphics g) {
		if (g != null) {
			this.flash = !this.flash;
			for (Iterator it = this.unsavedTests.iterator(); it.hasNext();) {
				Test test = (Test) it.next();
				g.setColor(this.flash ? (((this.selectedTest == null) || (!this.selectedTest.getId().equals(
					test.getId()))) ? SchedulerModel.COLOR_SCHEDULED : SchedulerModel.COLOR_SCHEDULED_SELECTED) : SchedulerModel.COLOR_UNRECOGNIZED);
				this.drawTestRect(g, test);
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

		if (!this.tests.isEmpty()) {
			this.scale = (double) (this.width - PlanPanel.MARGIN) / (double) (this.end - this.start);
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
					Test test = (Test) it.next();
					if (test.isChanged())
						continue;
					drawTestRect(g, test);
				}
				
				this.paintFlash(g);
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

	// public void unregisterDispatcher() {
	// this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TEST);
	// this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	// }

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
					testTimeLine.duration = (test.getEndTime() != null) ? test.getEndTime().getTime()
							- testTimeLine.startTime : 0;
					testTimeLine.haveMeasurement = false;
					measurementTestList.add(testTimeLine);
					this.measurements.put(test.getId(), measurementTestList);
				}				
				try {
					MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
							.getStorableObject((Identifier) test.getMeasurementSetupIds().iterator().next(), true);
					switch (test.getTemporalType().value()) {
						case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
							Set times = ((TemporalPattern)MeasurementStorableObjectPool
									.getStorableObject(test.getTemporalPatternId(), true)).getTimes(test.getStartTime(), test.getEndTime());
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
							testTimeLine.duration = (test.getEndTime() != null) ? test.getEndTime().getTime()
									- testTimeLine.startTime : 0;
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
	}

	private void drawTestRect(	Graphics g,
								Test test) {
		int y = this.titleHeight / 2 + 4;
		int h = this.height - (this.titleHeight / 2 + 4) - 2;
	
		int i = 0;
		
		Color color = SchedulerModel.getColor(test.getStatus());
		if ((this.selectedTest == null) || (!this.selectedTest.getId().equals(test.getId()))) {
			color = color.darker();
		} 
		
		List testTimeLineList = (List) this.measurements.get(test.getId());
		if (testTimeLineList == null)
			return;
		
		int x1 = 0;
		int w1 = 0;
		Color color1 = null;
		boolean painted = false;
		for (Iterator it = testTimeLineList.iterator(); it.hasNext(); i++) {
			TestTimeLine testTimeLine = (TestTimeLine) it.next();

			int x = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime - this.start));
			int en = PlanPanel.MARGIN / 2 + (int) (this.scale * (testTimeLine.startTime + testTimeLine.duration - this.start));
			int w = en - x + 1;
			TestTemporalType temporalType = test.getTemporalType();
			if (temporalType.value() == TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS)
				w = (w > minimalWidth) ? w : minimalWidth;
			else
				w = minimalWidth;
			
			if (testTimeLine.haveMeasurement) {
				if ((this.selectedTest == null) || (!this.selectedTest.getId().equals(test.getId()))) {
					color1 = SchedulerModel.COLOR_COMPLETED.darker();
					g.setColor(color1);
				} else {
					g.setColor(SchedulerModel.COLOR_COMPLETED);
					color1 = SchedulerModel.COLOR_COMPLETED;
				}
			} else {
				g.setColor(color);
				color1 = color;
			}
			
			if (i == 0) {
				g.fillRect(x + 2, y + 2, w - 3, h - 3);
				g.draw3DRect(x, y, w, h, true);
				
				Color c = g.getColor();
				g.setColor(new Color(0, 255 - c.getGreen(), 255 - c.getBlue()));
				g.drawRect(x + 1, y + 1, w - 2, h - 2);
				g.drawRect(x + w / 2, y + 1, 1, h - 2);
				
				x1 = x;
				w1 = w;
				painted = true;
			} else {			
				if (x1 + w1 < x) {
					g.fillRect(x1 + 2, y + 2, w1 - 3, h - 3);
					g.draw3DRect(x1, y, w1, h, true);					
					x1 = x;
					w1 = w;
					painted = false;
				} else {
					w1 = x + w - x1;
					painted = false;
				}
			}
			
		}
		
		if (!painted) {
			g.setColor(color1);
			g.fillRect(x1 + 2, y + 2, w1 - 3, h - 3);
			g.draw3DRect(x1, y, w1, h, true);	
		}

	}	
}
