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
	private ApplicationContext aContext;
	public String title;

	protected HashMap tests = new HashMap();
	protected long start;
	protected long end;
	protected int margin;

	private int titleHeight;
	private int minimalWidth = 5;

	protected Test currentTest;

	public TestLine(
		ApplicationContext aContext,
		String title,
		long start,
		long end,
		int margin) {
		this.aContext = aContext;
		this.title = title;
		this.start = start;
		this.end = end;
		this.margin = margin;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		this.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int width = getWidth();
//				System.out.println("mousePressed: (" + x + "," + y + ")");
				double scale =
					(double) (width - 2 * margin) / (double) (end - start);

				for (Iterator it = getTests().iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					int st =
						margin + (int) (scale * (test.start_time - start)) - 1;
					int en =
						margin
							+ (int) (scale
								* (test.start_time - start + test.duration))
							+ 1;
					en = (en - st < minimalWidth) ? st + minimalWidth : en;
//					System.out.println("."+((x >= st) && (x <= en) && (y >= titleHeight / 2 + 4)));
					if ((x >= st) && (x <= en) && (y >= titleHeight / 2 + 4)) {
						System.out.println("test:" + test.id);
						aContext.getDispatcher().notify(
							new TestUpdateEvent(
								this,
								test,
								TestUpdateEvent.TEST_SELECTED_EVENT));
						currentTest = test;
						break;
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
		tests.put(test.getId(), test);
	}

	public void addTest(String id) {
		Test test = (Test) Pool.get(Test.typ, id);
		if (test != null)
			addTest(test);
	}

	public void removeTest(String id) {
		tests.remove(id);
	}

	public void removeAllTests() {
		tests = new HashMap();
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
		int height = getHeight();
		int width = getWidth();
		double scale = (double) (width - 2 * margin) / (double) (end - start);
		Font font = new Font("Arial", Font.PLAIN, 12);
		g.setFont(font);
		this.titleHeight = g.getFontMetrics().getHeight();
//		System.out.println("titleHeight:"+titleHeight);
		g.setColor(Color.gray);
		g.clearRect(0, 0, width, this.titleHeight / 2 + 3);
		g.setColor(Color.black);
		g.drawString(title, 5, this.titleHeight / 2 + 2);
		g.drawLine(
			0,
			this.titleHeight / 2 + 3,
			width,
			this.titleHeight / 2 + 3);
		g.drawLine(0, height - 1, width, height - 1);

		int curStatus = TestStatus._TEST_STATUS_COMPLETED;
		Color color = Color.green;
		g.setColor(Color.green);

		for (Iterator it = getTests().iterator(); it.hasNext();) {
			int tmpStatus = curStatus;
			Test test = (Test) it.next();
			int st = margin + (int) (scale * (test.start_time - start));
			int en =
				margin
					+ (int) (scale * (test.start_time - start + test.duration));
			int w = en - st + 1;
			w = (w > minimalWidth) ? w : minimalWidth;
			if (test.status.equals(TestStatus.TEST_STATUS_COMPLETED)) {
				//				System.out.println("TestStatus.TEST_STATUS_COMPLETED");
				tmpStatus = TestStatus._TEST_STATUS_COMPLETED;
				color = Color.green;
			} else if (test.status.equals(TestStatus.TEST_STATUS_SCHEDULED)) {
				//				System.out.println("TestStatus.TEST_STATUS_SCHEDULED");
				tmpStatus = TestStatus._TEST_STATUS_SCHEDULED;
				color = Color.red;
			}
			if (tmpStatus != curStatus) {
				g.setColor(color);
			}
			g.fill3DRect(
				st,
				titleHeight / 2 + 4,
				w,
				height - (titleHeight / 2 + 4) - 1,
				true);
		}
	}
}