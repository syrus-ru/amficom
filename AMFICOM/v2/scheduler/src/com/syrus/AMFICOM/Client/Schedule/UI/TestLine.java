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

public class TestLine extends JPanel {

	public static final Color	COLOR_SCHEDULED		= Color.WHITE;
	public static final Color	COLOR_PROCCESSING	= Color.BLUE;
	public static final Color	COLOR_COMPLETED		= Color.GREEN;
	public static final Color	COLOR_ABORDED		= Color.RED;
	public static final Color	COLOR_UNRECOGNIZED	= new Color(20, 20, 60);

	private ApplicationContext	aContext;
	private String				title;

	private HashMap				tests				= new HashMap();
	private long				start;
	private long				end;
	private double				scale;
	private int					margin;

	private int					titleHeight;
	private int					minimalWidth		= 7;

	private ArrayList			unsavedTests;
	private ArrayList			allTests;
	private Test				currentTest;

	private Dispatcher			dispatcher;

	public TestLine(ApplicationContext aContext, String title, long start,
			long end, int margin) {
		this.aContext = aContext;
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

	private void initModule(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	private void jbInit() throws Exception {
		this.addMouseListener(new MouseListener() {

			public void mousePressed(MouseEvent e) {
				if (allTests != null) {
					int x = e.getX();
					int y = e.getY();
					int width = getWidth();
					//				System.out.println("mousePressed: (" + x + "," + y +
					// ")");
					// double scale = (double) (width - 2 * margin)/ (double)
					// (end - start);

					//for (Iterator it = getTests().iterator(); it.hasNext();)
					// {
					for (int i = 0; i < allTests.size(); i++) {
						//	Test test = (Test) it.next();
						Test test = (Test) allTests.get(i);
						int st = margin
								+ (int) (scale * (test.timeStamp
										.getPeriodStart() - start)) - 1;
						int en = margin
								+ (int) (scale * (test.timeStamp.getPeriodEnd() - start))
								+ 1;
						en = (en - st < minimalWidth) ? st + minimalWidth : en;
						//					System.out.println("."+((x >= st) && (x <= en) && (y
						// >=
						// titleHeight / 2 + 4)));
						if ((x >= st) && (x <= en)
								&& (y >= titleHeight / 2 + 4)) {
							//System.out.println("test:" + test.id);
							System.out.println("test.status.value():"
									+ test.status.value());
							dispatcher.notify(new TestUpdateEvent(this, test,
									TestUpdateEvent.TEST_SELECTED_EVENT));
							currentTest = test;
							break;
						}
					}
				}

			}

			public void mouseClicked(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
				if (currentTest != null) {
					currentTest = null;
				}

			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				if (currentTest != null) {
				}

			}

			public void mouseMoved(MouseEvent e) {
			}
		});
	}

	public void addTest(Test test) {
		if (test.id == null) {
			if (unsavedTests == null) unsavedTests = new ArrayList();
			unsavedTests.add(test);
		} else
			tests.put(test.getId(), test);
		if (allTests == null) allTests = new ArrayList();
		allTests.add(test);
		this.revalidate();
	}

	public void addTest(String id) {
		Test test = (Test) Pool.get(Test.typ, id);
		if (test != null) addTest(test);
	}

	public void removeTest(String id) {
		Test test = (Test) tests.get(id);
		if (allTests != null) allTests.remove(test);
		tests.remove(id);
	}

	public void removeAllTests() {
		tests.clear();
		allTests.clear();
	}

	public Collection getTests() {
		return tests.values();
	}

	public Set getTestIds() {
		return tests.keySet();
	}

	public Test getTest(String id) {
		return (Test) tests.get(id);
	}

	public void paintComponent(Graphics g) {
		//super.paint(g);
		if (allTests != null) {
			int height = getHeight();
			int width = getWidth();
			scale = (double) (width - 2 * margin) / (double) (end - start);
			Font font = UIUtil.ARIAL_12_FONT;
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

			//int curStatus = TestStatus._TEST_STATUS_COMPLETED;

			//for (Iterator it = tests.values().iterator(); it.hasNext();) {
			for (int i = 0; i < allTests.size(); i++) {
				//int tmpStatus = curStatus;
				//Test test = (Test) it.next();
				Color color;
				// type of figure to draw
				int type = 0;
				Test test = (Test) allTests.get(i);
				int st = margin
						+ (int) (scale * (test.timeStamp.getPeriodStart() - start));
				int en = margin
						+ (int) (scale * (test.timeStamp.getPeriodEnd() - start));
				int w = en - st + 1;
				w = (w > minimalWidth) ? w : minimalWidth;
				if (test.status.equals(TestStatus.TEST_STATUS_COMPLETED)) {
					color = COLOR_COMPLETED;
				} else if (test.status.equals(TestStatus.TEST_STATUS_SCHEDULED)) {
					color = COLOR_SCHEDULED;
				} else if (test.status
						.equals(TestStatus.TEST_STATUS_PROCESSING)) {
					color = COLOR_PROCCESSING;
				} else if (test.status.equals(TestStatus.TEST_STATUS_ABORTED)) {
					color = COLOR_ABORDED;
				} else {
					color = COLOR_UNRECOGNIZED;
				}
				if (tests.get(test) == null) {
					type = 1;
				}

				g.setColor(color);

				switch (type) {
					case 1:
						g.fillOval(st, titleHeight / 2 + 3, w, height
								- (titleHeight / 2 + 4));
						g.setColor(Color.BLACK);
						g.drawOval(st, titleHeight / 2 + 3, w, height
								- (titleHeight / 2 + 4));

						break;
					default:
						g.fill3DRect(st, titleHeight / 2 + 4, w, height
								- (titleHeight / 2 + 4) - 1, true);
						break;

				}
			}
		}
	}

}