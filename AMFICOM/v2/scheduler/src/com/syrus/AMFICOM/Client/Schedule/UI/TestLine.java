package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class TestLine extends JLabel implements ActionListener,
		OperationListener {

	public static final Color	COLOR_ABORDED		= Color.RED;
	public static final Color	COLOR_COMPLETED		= Color.GREEN;
	public static final Color	COLOR_PROCCESSING	= new Color(0, 128, 128);

	public static final Color	COLOR_SCHEDULED		= Color.WHITE;
	public static final Color	COLOR_UNRECOGNIZED	= new Color(20, 20, 60);
	public static final int		MINIMAL_WIDTH		= 7;

	public static final int		TIME_OUT			= 500;
	ArrayList					allTests;
	Test						currentTest;

	Dispatcher					dispatcher;
	private long				end;

	boolean						flash				= false;
	int							height;
	int							margin;
	double						scale;
	boolean						skipTestUpdate		= false;
	long						start;

	private HashMap				tests				= new HashMap();
	private javax.swing.Timer	timer;											//		=
	//private ApplicationContext aContext;
	private String				title;

	int							titleHeight;

	private HashMap				unsavedTests;

	int							width;

	// new
	// javax.swing.Timer(
	//			TIME_OUT,
	// this);
	// ;

	public TestLine(ApplicationContext aContext, String title, long start,
			long end, int margin) {
		//this.aContext = aContext;
		this.title = title;
		this.start = start;
		this.end = end;
		this.margin = margin;
		initModule(aContext.getDispatcher());
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void operationPerformed(OperationEvent e) {
		String commandName = e.getActionCommand();
		System.out.println(getClass().getName() + " commandName: " //$NON-NLS-1$
				+ commandName);
		if (commandName.equals(SchedulerModel.COMMAND_TEST_SAVED_OK)) {
			for (Iterator it = unsavedTests.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				Test test = (Test) unsavedTests.get(key);
				if (!test.isChanged()) {
					System.out.println("remove " + key);
					unsavedTests.remove(key);
					tests.put(test.getId(), test);
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		flashUnsavedTest();
	}

	public void addTest(String id) {
		Test test = (Test) Pool.get(Test.typ, id);
		if (test != null) addTest(test);
	}

	public void addTest(Test test) {
		if (!skipTestUpdate) {
			if (test.isChanged()) {
				System.out.println("test is changed");
				if (unsavedTests == null) {
					unsavedTests = new HashMap();
					timer = new javax.swing.Timer(TIME_OUT, this);
					timer.start();
					//System.out.println("timer created");
				}
				if (unsavedTests.containsValue(test)) {
					System.out.println("unsavedTests.contains(test)"); //$NON-NLS-1$
				} else
					unsavedTests.put(test.getId(), test);
			} else {
				System.out.println("test is NOT changed");
				tests.put(test.getId(), test);
			}
			if (allTests == null) allTests = new ArrayList();
			allTests.add(test);
			this.revalidate();
		}
	}

	public void flashUnsavedTest() {
		if ((this.isVisible()) && (unsavedTests != null)) {
			Graphics g = this.getGraphics();
			if (g != null) {
				flash = !flash;
				for (Iterator it = unsavedTests.keySet().iterator(); it
						.hasNext();) {
					Test test = (Test) unsavedTests.get(it.next());
					g.setColor(flash ? COLOR_SCHEDULED : COLOR_UNRECOGNIZED);
					drawTestRect(g, test);
				}
			}
		}

	}

	//	public Collection getTests() {
	//		return tests.values();
	//	}
	//
	//	public Set getTestIds() {
	//		return tests.keySet();
	//	}

	public Test getTest(String id) {
		return (Test) tests.get(id);
	}

	public void paintComponent(Graphics g) {
		height = getHeight();
		width = getWidth();

		if (allTests != null) {
			scale = (double) (width - 2 * margin) / (double) (end - start);
			Font font = UIStorage.ARIAL_12_FONT;
			g.setFont(font);
			this.titleHeight = g.getFontMetrics().getHeight();
			//		System.out.println("titleHeight:"+titleHeight);
			g.setColor(Color.gray);
			g.clearRect(0, 0, width, this.titleHeight / 2 + 3);
			g.setColor(Color.black);
			g.drawString(title, 5, this.titleHeight / 2 + 2);
			g.drawLine(0, this.titleHeight / 2 + 3, width,
					this.titleHeight / 2 + 3);
			g.drawLine(0, height - 1, width, height - 1);
			//for (Iterator it = tests.values().iterator(); it.hasNext();) {
			//			System.out.println(":" + allTests.size() + "\t"
			//					+ System.currentTimeMillis());
			for (int i = 0; i < allTests.size(); i++) {
				//int tmpStatus = curStatus;
				//Test test = (Test) it.next();
				Color color;
				Test test = (Test) allTests.get(i);
				if (test.getStatus().equals(TestStatus.TEST_STATUS_COMPLETED)) {
					color = COLOR_COMPLETED;
				} else if (test.getStatus().equals(
						TestStatus.TEST_STATUS_SCHEDULED)) {
					color = COLOR_SCHEDULED;
				} else if (test.getStatus().equals(
						TestStatus.TEST_STATUS_PROCESSING)) {
					color = COLOR_PROCCESSING;
				} else if (test.getStatus().equals(
						TestStatus.TEST_STATUS_ABORTED)) {
					color = COLOR_ABORDED;
				} else {
					color = COLOR_UNRECOGNIZED;
				}
				if ((unsavedTests == null)
						|| (!unsavedTests.containsValue(test))) {
					g.setColor(color);
					drawTestRect(g, test);
				}

			}
		}
	}

	public void removeAllTests() {
		tests.clear();
		allTests.clear();
		unsavedTests.clear();
	}

	public void removeTest(String id) {
		Test test = (Test) tests.get(id);
		if (allTests != null) allTests.remove(test);
		tests.remove(id);
	}

	private void drawTestRect(Graphics g, Test test) {
		TimeStamp_dep timeStamp = test.getTimeStamp();
		int x = margin
				+ (int) (scale * (test.getTimeStamp().getPeriodStart() - start));
		int en = margin
				+ (int) (scale * (test.getTimeStamp().getPeriodEnd() - start));
		int w = en - x + 1;
		if (timeStamp.getType() == TimeStamp_dep.TIMESTAMPTYPE_CONTINUOS)
			w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
		else
			w = MINIMAL_WIDTH;
		int y = titleHeight / 2 + 4;
		int h = height - (titleHeight / 2 + 4) - 2;
		//System.out.println(">>"+timeStamp.getType());
		switch (timeStamp.getType()) {
			case TimeStamp_dep.TIMESTAMPTYPE_PERIODIC:

				long[] times = timeStamp.getTestTimes();
				for (int i = 0; i < times.length; i++) {
					x = margin + (int) (scale * (times[i] - start));
					g.fillRect(x + 2, y + 2, w - 3, h - 3);
					//System.out.println(i + "\t" + times[i] + "\tx:" + x);
					g.draw3DRect(x, y, w, h, true);
					if (i == 0) {
						Color c = g.getColor();
						g.setColor(new Color(0, 255 - c.getGreen(), 255 - c
								.getBlue()));
						g.drawRect(x + 1, y + 1, w - 2, h - 2);
						//g.drawLine(x + 2, y + h / 4, x + w - 4, y + h / 4);
						g.drawRect(x + w / 2, y + 1, 1, h - 2);
						//g.drawString("T", x, y);
						g.setColor(c);
					}
				}
				break;
			default:
				g.fillRect(x + 2, y + 2, w - 3, h - 3);
				g.draw3DRect(x, y, w, h, true);
				break;
		}
	}

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		dispatcher.register(this, SchedulerModel.COMMAND_TEST_SAVED_OK);
	}

	private void jbInit() throws Exception {
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
				if (allTests != null) {
					int x = e.getX();
					int y = e.getY();
					//int width = getWidth();
					//				System.out.println("mousePressed: (" + x + "," + y +
					// ")");
					// double scale = (double) (width - 2 * margin)/ (double)
					// (end - start);

					//for (Iterator it = getTests().iterator(); it.hasNext();)
					// {
					for (int i = 0; i < allTests.size(); i++) {
						//	Test test = (Test) it.next();
						Test test = (Test) allTests.get(i);
						TimeStamp_dep timeStamp = test.getTimeStamp();
						int st = margin
								+ (int) (scale * (test.getTimeStamp()
										.getPeriodStart() - start)) - 1;
						int en = margin
								+ (int) (scale * (test.getTimeStamp()
										.getPeriodEnd() - start)) + 1;
						en = (en - st < MINIMAL_WIDTH) ? st + MINIMAL_WIDTH
								: en;
						//					System.out.println("."+((x >= st) && (x <= en) && (y
						// >=
						// titleHeight / 2 + 4)));
						int w = en - st + 1;
						if (timeStamp.getType() == TimeStamp_dep.TIMESTAMPTYPE_CONTINUOS)
							w = (w > MINIMAL_WIDTH) ? w : MINIMAL_WIDTH;
						else
							w = MINIMAL_WIDTH;
						en = st + w;
						boolean condition = false;
						switch (timeStamp.getType()) {
							case TimeStamp_dep.TIMESTAMPTYPE_PERIODIC:
								long[] times = timeStamp.getTestTimes();
								for (int j = 0; j < times.length; j++) {
									st = margin
											+ (int) (scale * (times[j] - start));
									en = st + w;
									if ((x >= st) && (x <= en)
											&& (y >= titleHeight / 2 + 4)) {
										condition = true;
										//System.out.println("selected:" + j);
										break;
									}
								}
								break;
							default:
								if ((x >= st) && (x <= en)
										&& (y >= titleHeight / 2 + 4)) {
									condition = true;
								}
								break;
						}

						if (condition) {
							//System.out.println("test:" + test.id);
							//							System.out.println("test.status.value():"
							//									+ test.status.value());
							System.out
									.println("TestLine>onClick: test==null : " //$NON-NLS-1$
											+ (test.isChanged()));
							skipTestUpdate = true;
							dispatcher.notify(new TestUpdateEvent(this, test,
									TestUpdateEvent.TEST_SELECTED_EVENT));
							skipTestUpdate = false;
							currentTest = test;
							break;
						}
					}
				}

			}

			public void mouseReleased(MouseEvent e) {
				if (currentTest != null) {
					currentTest = null;
				}

			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (currentTest != null) {
					//nothing
				}

			}

			public void mouseMoved(MouseEvent e) {
				//				nothing
			}
		});
	}

}