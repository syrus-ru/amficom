package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.Constant.AlarmTypeConstants;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.Survey.ElementaryTestAlarm;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TestLine extends JLabel implements ActionListener, OperationListener {

	/**
	 * @TODO recast using alpha
	 */
	public static final Color	COLOR_ABORDED				= Color.RED;
	public static final Color	COLOR_ABORDED_SELECTED		= new Color(255, 128, 128);
	public static final Color	COLOR_COMPLETED				= new Color(0, 128, 0);
	public static final Color	COLOR_COMPLETED_SELECTED	= new Color(0, 255, 0);
	public static final Color	COLOR_PROCCESSING			= new Color(0, 128, 128);
	public static final Color	COLOR_PROCCESSING_SELECTED	= new Color(64, 192, 192);

	public static final Color	COLOR_SCHEDULED				= new Color(128, 128, 128);
	public static final Color	COLOR_SCHEDULED_SELECTED	= Color.WHITE;
	public static final Color	COLOR_UNRECOGNIZED			= new Color(20, 20, 60);

	public static final Color	COLOR_WARNING				= new Color(128, 128, 0);
	public static final Color	COLOR_WARNING_SELECTED		= Color.YELLOW;

	public static final Color	COLOR_ALARM					= new Color(128, 0, 128);
	public static final Color	COLOR_ALARM_SELECTED		= new Color(255, 0, 255);

	public static final int		MINIMAL_WIDTH				= 7;

	public static final int		TIME_OUT					= 500;
	ArrayList					allTests;
	//Test currentTest;

	Dispatcher					dispatcher;

	boolean						flash						= false;
	int							height;
	int							margin;
	double						scale;
	//	boolean skipTestUpdate = false;
	long						start;

	int							titleHeight;

	int							width;
	private long				end;

	private HashMap				tests						= new HashMap();
	private javax.swing.Timer	timer;													//		=
	//private ApplicationContext aContext;
	private String				title;
	Test						test;

	private HashMap				unsavedTests;

	// new
	// javax.swing.Timer(
	//			TIME_OUT,
	// this);
	// ;

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
						//	Test test = (Test) it.next();
						Test test = (Test) it.next();
						TimeStamp timeStamp = test.getTimeStamp();
						int st = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getTimeStamp().getPeriodStart() - TestLine.this.start))
								- 1;
						int en = TestLine.this.margin
								+ (int) (TestLine.this.scale * (test.getTimeStamp().getPeriodEnd() - TestLine.this.start))
								+ 1;
						en = (en - st < MINIMAL_WIDTH) ? st + MINIMAL_WIDTH : en;
						//					System.out.println("."+((x >= st) && (x <= en) && (y
						// >=
						// titleHeight / 2 + 4)));
						int w = en - st + 1;
						if (timeStamp.getType() == TimeStamp.TIMESTAMPTYPE_CONTINUOS)
							w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
						else
							w = MINIMAL_WIDTH;
						en = st + w;
						boolean condition = false;
						switch (timeStamp.getType()) {
							case TimeStamp.TIMESTAMPTYPE_PERIODIC:
								long[] times = timeStamp.getTestTimes();
								for (int j = 0; j < times.length; j++) {
									st = TestLine.this.margin
											+ (int) (TestLine.this.scale * (times[j] - TestLine.this.start));
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
							//System.out.println("test:" + test.id);
							//							System.out.println("test.status.value():"
							//									+ test.status.value());
							//							System.out.println("TestLine>onClick: test==null
							// : " //$NON-NLS-1$
							//									+ (test.isChanged()));
							//TestLine.this.skipTestUpdate = true;
							TestLine.this.dispatcher.notify(new TestUpdateEvent(this, test,
																				TestUpdateEvent.TEST_SELECTED_EVENT));
							//TestLine.this.skipTestUpdate = false;
							//TestLine.this.test = test;
							break;
						}
					}
				}

			}

			public void mouseReleased(MouseEvent e) {
				//				if (TestLine.this.test != null) {
				//					TestLine.this.test = null;
				//				}

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (TestLine.this.test != null) {
					//nothing
				}

			}

			public void mouseMoved(MouseEvent e) {
				//				nothing
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		flashUnsavedTest();
	}

	public void addTest(String id) {
		Test test = (Test) Pool.get(Test.TYPE, id);
		if (test != null)
			addTest(test);
	}

	public void addTest(Test test) {
		//		if (!this.skipTestUpdate) {
		if (test.isChanged()) {
			//System.out.println("test is changed");
			if (this.unsavedTests == null) {
				this.unsavedTests = new HashMap();
				this.timer = new javax.swing.Timer(TIME_OUT, this);
				this.timer.start();
				//System.out.println("timer created");
			}
			if (this.unsavedTests.containsValue(test)) {
				//System.out.println("unsavedTests.contains(test)");
				// //$NON-NLS-1$
			} else {
				//System.out.println("unsavedTests.put(" + test.getId() + ",
				// test);");
				this.unsavedTests.put(test.getId(), test);
			}
		} else {
			//System.out.println("test is NOT changed");
			this.tests.put(test.getId(), test);
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
					//System.out.println("testID:" + test.getId());
					//System.out.println("test:" + (this.test == null ? " is
					// null" : test.getId()));
					g.setColor(this.flash ? (((this.test == null) || (!this.test.getId().equals(test.getId())))
							? COLOR_SCHEDULED : COLOR_SCHEDULED_SELECTED) : COLOR_UNRECOGNIZED);
					//System.out.println(g.getColor());
					drawTestRect(g, test);
				}
			}
		}

	}

	public boolean isEmpty() {
		return this.allTests.isEmpty();
	}

	//	public Collection getTests() {
	//		return tests.values();
	//	}
	//
	//	public Set getTestIds() {
	//		return tests.keySet();
	//	}

	public Test getTest(String id) {
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
			if (this.unsavedTests != null)
				this.unsavedTests.clear();
			if (this.tests != null)
				this.tests.clear();
		} else if (commandName.equals(TestUpdateEvent.TYPE)) {
			TestUpdateEvent tue = (TestUpdateEvent) e;
			Test test = tue.test;
			if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
				this.test = test;
				//System.out.println("set test :"+test.getId());
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

				if ((this.test == null) || (!this.test.getId().equals(test.getId()))) {
					if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
						color = COLOR_COMPLETED;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_SCHEDULED)) {
						color = COLOR_SCHEDULED;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_PROCESSING)) {
						color = COLOR_PROCCESSING;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
						color = COLOR_ABORDED;
					} else {
						color = COLOR_UNRECOGNIZED;
					}
				} else {
					if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
						color = COLOR_COMPLETED_SELECTED;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_SCHEDULED)) {
						color = COLOR_SCHEDULED_SELECTED;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_PROCESSING)) {
						color = COLOR_PROCCESSING_SELECTED;
					} else if (test.getStatus().equals(TestStatus.TEST_STATUS_ABORTED)) {
						color = COLOR_ABORDED_SELECTED;
					} else {
						color = COLOR_UNRECOGNIZED;
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
		this.tests.clear();
		this.allTests.clear();
		this.unsavedTests.clear();
	}

	public void removeTest(Test test) {
		//Test test = (Test) this.tests.get(id);
		String testId = test.getId();
		if (this.unsavedTests != null) {
			this.unsavedTests.remove(test.getId());
			//			System.out.println("this.unsavedTests.remove(" + test.getId() +
			// "):"
			//					+ this.unsavedTests.containsKey(test.getId()));
		}
		if (this.allTests != null) {
			//			for (Iterator it = this.allTests.iterator(); it.hasNext();) {
			//				Test t = (Test) it.next();
			//				System.out.println(t.getId());
			//			}
			this.allTests.remove(test);
			//			System.out.println("this.allTests.remove(test):" +
			// this.allTests.contains(test));
		}
		this.tests.remove(testId);
		//		System.out.println("this.tests.remove(testId):" +
		// this.tests.containsKey(testId));
	}

	private void drawTestRect(Graphics g, Test test) {
		TimeStamp timeStamp = test.getTimeStamp();
		int x = this.margin + (int) (this.scale * (test.getTimeStamp().getPeriodStart() - this.start));
		int en = this.margin + (int) (this.scale * (test.getTimeStamp().getPeriodEnd() - this.start));
		int w = en - x + 1;
		if (timeStamp.getType() == TimeStamp.TIMESTAMPTYPE_CONTINUOS)
			w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
		else
			w = MINIMAL_WIDTH;
		int y = this.titleHeight / 2 + 4;
		int h = this.height - (this.titleHeight / 2 + 4) - 2;
		//System.out.println(">>"+timeStamp.getType());

		ElementaryTestAlarm[] testAlarms = test.getElementaryTestAlarms();

		switch (timeStamp.getType()) {
			case TimeStamp.TIMESTAMPTYPE_PERIODIC:

				long[] times = timeStamp.getTestTimes();
				for (int i = 0; i < times.length; i++) {
					if (testAlarms.length > 0) {
						for (int j = 0; j < testAlarms.length; j++) {
							if (Math.abs(testAlarms[j].elementary_start_time - times[i]) < 1000 * 30) {
								Alarm alarm = (Alarm) Pool.get(Alarm.typ, testAlarms[j].alarm_id);
								if (alarm != null) {
									//System.out.println("alarm.type_id:" +
									// alarm.type_id);
									if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_ALARM)) {
										//System.out.println("ID_RTU_TEST_ALARM");
										if ((this.test != null) && (this.test.getId().equals(test.getId())))
											g.setColor(TestLine.COLOR_ALARM_SELECTED);
										else
											g.setColor(TestLine.COLOR_ALARM);
									} else if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_WARNING)) {
										//System.out.println("ID_RTU_TEST_WARNING");
										if ((this.test != null) && (this.test.getId().equals(test.getId())))
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
					x = this.margin + (int) (this.scale * (times[i] - this.start));
					g.fillRect(x + 2, y + 2, w - 3, h - 3);
					//System.out.println(i + "\t" + times[i] + "\tx:" + x);
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
				}
				break;
			default:
				/**
				 * @TODO reuse code
				 */
				if (testAlarms.length > 0) {
					for (int j = 0; j < testAlarms.length; j++) {
							Alarm alarm = (Alarm) Pool.get(Alarm.typ, testAlarms[j].alarm_id);
							if (alarm != null) {
								//System.out.println("alarm.type_id:" +
								// alarm.type_id);
								if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_ALARM)) {
									//System.out.println("ID_RTU_TEST_ALARM");
									if ((this.test != null) && (this.test.getId().equals(test.getId())))
										g.setColor(TestLine.COLOR_ALARM_SELECTED);
									else
										g.setColor(TestLine.COLOR_ALARM);
								} else if (alarm.type_id.equals(AlarmTypeConstants.ID_RTU_TEST_WARNING)) {
									//System.out.println("ID_RTU_TEST_WARNING");
									if ((this.test != null) && (this.test.getId().equals(test.getId())))
										g.setColor(TestLine.COLOR_WARNING_SELECTED);
									else
										g.setColor(TestLine.COLOR_WARNING);
								}
							}
					}
				}

				g.fillRect(x + 2, y + 2, w - 3, h - 3);
				g.draw3DRect(x, y, w, h, true);
				break;
		}
	}
	
	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.TYPE);
		this.dispatcher.register(this, SchedulerModel.COMMAND_TEST_SAVED_OK);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CLEAN);
	}

}