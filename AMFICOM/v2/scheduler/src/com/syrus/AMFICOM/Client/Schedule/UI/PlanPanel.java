package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Filter.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.Result.*;

import com.syrus.AMFICOM.Client.Schedule.ScheduleMainFrame;
import com.syrus.AMFICOM.Client.Schedule.Filter.*;
import com.syrus.AMFICOM.Client.Scheduler.General.I18N;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

import oracle.jdeveloper.layout.*;

public class PlanPanel extends JPanel implements OperationListener {

	private static class Step {

		int	align;		//выравнивание по кратному

		int	one;		//число единиц измерения внутри одного деления

		int	scale;		//степень градации основных делений шкалы

		int	subscales;	//число дополнительных делений внутри основного

		int	total;		//общее число единиц измерения

		Step(int scale, int one, int total, int subscales, int align) {
			this.scale = scale;
			this.one = one;
			this.total = total;
			this.subscales = subscales;
			this.align = align;
		}
	}

	public static final String		COMMAND_NAME_ALL_TESTS	= "AllTests";							//$NON-NLS-1$

	private static final String[]	scales					= new String[] {
			"10 min", "1 hour", "6 hours", "1 day", "1 week", "1 month"};							//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	protected static final Step[]	steps					= new Step[] {
			new Step(Calendar.MINUTE, 1, 10, 6, 1),
			new Step(Calendar.MINUTE, 10, 60, 5, 5),
			new Step(Calendar.HOUR_OF_DAY, 1, 6, 6, 1),
			new Step(Calendar.HOUR_OF_DAY, 4, 24, 4, 2),
			new Step(Calendar.DAY_OF_MONTH, 1, 7, 4, 1),
			new Step(Calendar.DAY_OF_MONTH, 5, 30, 5, 1),	};
	private ApplicationContext		aContext;

	protected Calendar				cal						= Calendar
																	.getInstance();

	protected Point					currpos					= new Point();

	private Dispatcher				dispatcher;

	private ObjectResourceFilter	filter					= null;

	protected int					margin					= 14;
	private int						maxZoom					= 50;
	protected JScrollPane			parent;
	protected int					real_scale				= 0;
	protected int					scale					= 0;
	protected Date					scaleEnd;

	// rounded start and end time of range displayed
	protected Date					scaleStart;

	protected SimpleDateFormat		sdf						= new SimpleDateFormat();

	protected boolean				select_by_mouse			= false;

	// real start time including minutes and seconds
	protected Date					startDate				= new Date(
																	System
																			.currentTimeMillis());

	protected Point					startpos				= new Point();

	private HashMap					testLines				= new HashMap();

	// saved tests, loaded from server
	private DataSet					tests;

	protected Point					tmppos;

	private PlanToolBar				toolBar;

	// new tests, which haven't saved yet
	private ArrayList				unsavedTests;

	public PlanPanel(JScrollPane parent, ApplicationContext aContext) {
		this.aContext = aContext;
		this.parent = parent;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		initModule(aContext.getDispatcher());
		filter = new TestFilter();

		setStartDate(new Date(System.currentTimeMillis()));
		updateRealScale();
	}

	//private static final int TIME_OUT = 500;

	public PlanPanel(JScrollPane parent, ApplicationContext aContext,
			Date start, int scale) {
		this(parent, aContext);
		setScale(scale);
		setStartDate(start);
		updateRealScale();
	}

	public static String[] getSupportedScales() {
		return scales;
	}

	/**
	 * @todo  only for testing mode
	 */
	public static void main(String[] args) {
		JScrollPane scroll = new JScrollPane();

		//		PlanLayeredPanel panel = new PlanLayeredPanel();
		PlanPanel demo = new PlanPanel(scroll, null);
		PlanToolBar toolBar = demo.getToolBar();
		//		panel.setGraphPanel(mainPanel);
		scroll.getViewport().add(demo);
		JFrame mainFrame = new JFrame("TimeParametersPanel"); //$NON-NLS-1$
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//mainFrame.getContentPane().add(demo);
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
		mainFrame.getContentPane().add(scroll, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setSize(new Dimension(600, 465));
		mainFrame.setVisible(true);
	}

	public int getScale() {
		return scale;
	}

	public Date getStartDate() {
		return startDate;
	}

	public PlanToolBar getToolBar() {
		return toolBar;
	}

	//	public void actionPerformed(ActionEvent e) {
	//		for (Iterator it = testLines.values().iterator(); it.hasNext();) {
	//			TestLine testLine = (TestLine) it.next();
	//			testLine.flashUnsavedTest();
	//		}
	//	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		if (ScheduleMainFrame.DEBUG >= 5)
				System.out.println(getClass().getName() + " commandName: " //$NON-NLS-1$
						+ commandName);
		if (commandName.equals(TestUpdateEvent.typ)) {
			TestUpdateEvent tue = (TestUpdateEvent) ae;
			if (tue.TEST_SELECTED) {
				Test test = tue.test;
				boolean found = false;
				if (tests != null) {
					for (Enumeration en = tests.elements(); en
							.hasMoreElements();) {
						Test t = (Test) en.nextElement();
						if (t.getId().equals(test.getId())) {
							found = true;
							break;
						}
					}
				}
				if (unsavedTests.contains(test)) {
					found = true;
				}
				if (!found) {
					if (ScheduleMainFrame.DEBUG >= 3)
							System.out.println("new test catched"); //$NON-NLS-1$
					unsavedTests.add(test);
					{
						TestLine testLine;
						if (testLines == null)
								System.out.println("testLines is null"); //$NON-NLS-1$
						if (test == null)
							System.out.println("test is null"); //$NON-NLS-1$
						else if (test.getMonitoredElementId() == null)
								System.out
										.println("test.monitored_element_id is null"); //$NON-NLS-1$
						if (testLines.containsKey(test.getMonitoredElementId()))
							testLine = (TestLine) testLines.get(test
									.getMonitoredElementId());
						else {
							String me_name = Pool.getName(MonitoredElement.typ,
									test.getMonitoredElementId());
							testLine = new TestLine(aContext,
							//parent.getViewport(),
									me_name, scaleStart.getTime(), scaleEnd
											.getTime(), margin / 2);
							testLine.setPreferredSize(new Dimension(0, 20));
							testLines.put(test.getMonitoredElementId(),
									testLine);
							add(testLine);
						}
						testLine.addTest(test);

					}

				}
			}
			revalidate();
			parent.repaint();
		}

	}

	public void setScale(int n) {
		if (n < 0 || n >= scales.length) {
			System.err
					.println("Unsupported scale: " //$NON-NLS-1$
							+ n
							+ ". Use setScale(n);" //$NON-NLS-1$
							+ " where n determine one of PlanPanel.getSupportedScales() element"); //$NON-NLS-1$
			return;
		}
		scale = n;
	}

	public void setStartDate(Date start) {
		startDate = start;

		cal.setTime(start);
		switch (scale) {
			case 4:
			case 5:
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case 2:
			case 3:
				cal.set(Calendar.MINUTE, 0);
			case 0:
			case 1:
				cal.set(Calendar.SECOND, 0);
		}

		//округляем до шага
		int num = cal.get(steps[scale].scale);
		while (num / steps[scale].align * steps[scale].align != num) {
			cal.add(steps[scale].scale, -1);
			num = cal.get(steps[scale].scale);
		}

		scaleStart = cal.getTime();
		//scroll calendar to end of period
		cal.add(steps[scale].scale, steps[scale].total);
		scaleEnd = cal.getTime();
		//scroll calendar to start point
		cal.setTime(startDate);
	}

	protected void paintComponent(Graphics g) {
		// for paint testlines

		super.paintComponent(g);
		cal.setTimeInMillis(0);
		cal.add(steps[real_scale].scale, steps[real_scale].one);
		long diff = cal.getTimeInMillis();
		double delta = (getWidth() - 2 * margin)
				/ ((double) (scaleEnd.getTime() - scaleStart.getTime()) / (double) diff);
		double sub_delta = delta / steps[real_scale].subscales;

		paintScales(g, diff, delta, sub_delta);
		/*
		 * for (Iterator it = testLines.values().iterator(); it.hasNext();) {
		 * TestLine testLine = (TestLine)it.next(); testLine.paint(g); }
		 */
		paintScaleDigits(g, diff, delta, sub_delta);

	}

	protected void paintScaleDigits(Graphics g, long diff, double delta,
			double sub_delta) //, double sub_sub_delta)
	{
		int h = getHeight() - 1;
		//		int w = getWidth();

		long _diff = diff;
		double _delta = delta;
		if (sub_delta > 60) {
			_delta = sub_delta;
			_diff = diff / steps[real_scale].subscales;
		} else if (real_scale == 0) sdf.applyPattern("HH:mm"); //$NON-NLS-1$

		int shift = (int) g.getFontMetrics().getStringBounds(
				sdf.format(cal.getTime()), g).getCenterX();

		cal.setTime(scaleStart);
		g.setColor(Color.black);
		int counter = 0;
		Color c = new Color(240, 240, 240);
		while (cal.getTime().compareTo(scaleEnd) <= 0) {
			g.setColor(c);
			g.drawLine((int) (_delta * counter) + margin, 0,
					(int) (_delta * counter) + margin, h);
			g.setColor(Color.BLACK);
			g.drawLine((int) (_delta * counter) + margin, h,
					(int) (_delta * counter) + margin, h - 5);

			String value = sdf.format(cal.getTime());
			cal.setTimeInMillis(cal.getTimeInMillis() + _diff);
			g.drawString(value, (int) (_delta * counter) + margin - shift,
					h - 7);
			if (value.startsWith("00:00")) //$NON-NLS-1$
					g.drawString(new SimpleDateFormat("dd.MM.yyyy").format(cal //$NON-NLS-1$
							.getTime()),
							(int) (_delta * counter) + margin - 27, h - 17);
			counter++;
		}
		cal.setTime(scaleStart);
	}

	protected void paintScales(Graphics g, long diff, double delta,
			double sub_delta) {

		int h = getHeight() - 1;
		int w = getWidth();

		long _diff = diff;
		double _delta = delta;
		if (sub_delta > 10) {
			_delta = sub_delta;
			_diff = diff / steps[real_scale].subscales;
		}

		cal.setTime(scaleStart);
		g.setColor(Color.black);
		int counter = 0;
		while (cal.getTime().compareTo(scaleEnd) <= 0) {
			cal.setTimeInMillis(cal.getTimeInMillis() + _diff);
			g.drawLine((int) (_delta * counter) + margin, h,
					(int) (_delta * counter) + margin, h - 3);
			counter++;
		}
		cal.setTime(scaleStart);

		/*
		 * int h = getHeight() - 1; int w = getWidth(); double range =
		 * (double)steps[scale].total / (double)steps[scale].one; double step =
		 * (double)(w - 2 * margin) / range; int tmp;
		 * 
		 * for (int i = 0; i <= Math.round(range); i++) { tmp = (int)(step*i);
		 * g.setColor(new Color(240, 240, 240)); g.drawLine(tmp + margin, 0, tmp +
		 * margin, h);
		 * 
		 * g.setColor(Color.black); g.drawLine(tmp + margin, h - 5, tmp +
		 * margin, h); for (int j = 1; j < steps[scale].subscales; j++)
		 * g.drawLine(tmp + (int)(step/(double)steps[scale].subscales*j) +
		 * margin, h - 3, tmp + (int)(step/(double)steps[scale].subscales*j) +
		 * margin, h); }
		 */
		g.drawLine(0, h, w, h);
	}

	protected void paintSelect(Graphics g) {
		g.setXORMode(Color.gray);
		g.drawLine(startpos.x, 0, startpos.x, getHeight());
		g.drawLine(startpos.x, 0, startpos.x, getHeight());
		g.drawLine(tmppos.x, 0, tmppos.x, getHeight());
		g.drawLine(currpos.x, 0, currpos.x, getHeight());

	}

	protected void upd_currpos(MouseEvent e) {
		tmppos = currpos;
		currpos = e.getPoint();

		// check if we go out the borders of panel
		int vx = 0;
		int wx = getWidth();
		int vy = 0;
		int wy = getHeight();

		if (currpos.x < vx) currpos.x = vx;
		if (currpos.x > vx + wx) currpos.x = vx + wx;
		if (currpos.y < vy) currpos.y = vy;
		if (currpos.y > vy + wy) currpos.y = vy + wy;
	}

	/**
	 * @todo recast without this stupid jbuilder pattern
	 */

	void this_mouseDragged(MouseEvent e) {
		upd_currpos(e);
		if (select_by_mouse) paintSelect(getGraphics().create());
	}

	/**
	 * @todo recast without this stupid jbuilder pattern
	 */

	void this_mousePressed(MouseEvent e) {
		startpos = e.getPoint();
		currpos = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			setCursor(UIStorage.CROSS_HAIR_CURSOR);
			select_by_mouse = true;
		}
	}

	/**
	 * @todo recast without this stupid jbuilder pattern
	 */
	void this_mouseReleased(MouseEvent e) {
		if (e.getClickCount() > 0) {
			setCursor(UIStorage.DEFAULT_CURSOR);

			if (currpos.x == startpos.x) {
				parent.repaint();
				return;
			}
			if (select_by_mouse) {
				select_by_mouse = false;
				double k = (parent.getVisibleRect().width - 2 * margin)
						/ Math.abs((double) (startpos.x - currpos.x));
				int view_x = Math.min(startpos.x, currpos.x);
				updateScale(k, view_x);
			}
		}
	}

	void updateDate(Date date, int scale) {
		setScale(scale);
		setStartDate(date);
		updateTests();
		updateRealScale();
		parent.updateUI();
	}

	private void updateRealScale() {
		real_scale = scale;

		double k = (double) parent.getViewport().getViewRect().width
				/ (double) getSize().width;
		long visible_time = (long) ((scaleEnd.getTime() - scaleStart.getTime()) * k);
		while (visible_time != 0 && real_scale > 0) {
			cal.setTimeInMillis(0);
			cal.add(steps[real_scale].scale, steps[real_scale].total
					/ steps[real_scale].one);
			if (cal.getTimeInMillis() > visible_time)
				real_scale--;
			else
				break;
		}

		cal.setTimeInMillis(0);
		cal.add(steps[real_scale].scale, steps[real_scale].one);
		long diff = cal.getTimeInMillis();
		double delta = (getWidth() - 2 * margin)
				/ ((double) (scaleEnd.getTime() - scaleStart.getTime()) / (double) diff);

		//double sub_delta = delta / steps[real_scale].subscales;
		if (delta >= 0 && delta < 35 && real_scale < steps.length - 1)
				real_scale++;

		switch (real_scale) {
			case 1:
				sdf.applyPattern("HH:mm"); //$NON-NLS-1$
				break;
			case 0:
				sdf.applyPattern("HH:mm:ss"); //$NON-NLS-1$
				break;
			case 2:
			case 3:
				sdf.applyPattern("HH:mm"); //$NON-NLS-1$
				break;
			case 5:
				steps[5].total = cal.getActualMaximum(steps[5].scale);
			case 4:
				sdf.applyPattern("dd.MM"); //$NON-NLS-1$
		}
		cal.setTime(startDate);
		switch (real_scale) {
			case 4:
			case 5:
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case 3:
				cal.set(Calendar.MINUTE, 0);
			case 0:
			case 1:
			case 2:
				cal.set(Calendar.SECOND, 0);
		}
	}

	void updateScale(double k) {
		updateScale(k, (int) (parent.getViewport().getViewPosition().x
				+ parent.getVisibleRect().width * (k * 0.5 - 0.5) - margin));
	}

	void updateScale(double k, int view_x) {
		if (getSize().width / parent.getVisibleRect().width >= maxZoom - 1
				&& k > 1) {
			parent.repaint();
			return;
		}

		if (getSize().width * k > maxZoom * parent.getVisibleRect().width)
				k = (double) (maxZoom * parent.getVisibleRect().width)
						/ (double) getSize().width;

		setPreferredSize(new Dimension(
				(int) ((getSize().width - 2 * margin) * k),
				getPreferredSize().height));
		setSize(new Dimension((int) ((getSize().width - 2 * margin) * k), Math
				.max(getSize().height, getPreferredSize().height)));
		/*
		 * for (Iterator it = testLines.values().iterator(); it.hasNext();) {
		 * TestLine testLine = (TestLine)it.next();
		 * testLine.setPreferredSize(new Dimension(getPreferredSize().width,
		 * 20)); }
		 */
		Point p = new Point((int) ((view_x - margin) * k), view_x);
		parent.getViewport().setViewPosition(p);

		updateRealScale();

		parent.updateUI();
	}

	void updScale2Fit() {
		setPreferredSize(new Dimension(600, getPreferredSize().height));
		setSize(new Dimension(600, getPreferredSize().height));

		updateRealScale();
		parent.updateUI();
	}

	private void initModule(Dispatcher dispatcher) {
		unsavedTests = new ArrayList();
		//timer.start();
		this.dispatcher = dispatcher;
		this.dispatcher.register(this, TestUpdateEvent.typ);
	}

	private void jbInit() throws Exception {
		toolBar = new PlanToolBar(aContext, this);

		setLayout(new VerticalFlowLayout());
		//setLayout(new GridLayout(0,1));
		setBackground(SystemColor.window);
		setPreferredSize(new Dimension(600, 20));
		//		setCursor(UIStorage.DEFAULT_CURSOR);

		this.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				// nothing
			}

			public void mouseEntered(MouseEvent e) {
				// nothing
			}

			public void mouseExited(MouseEvent e) {
				// nothing
			}

			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				this_mouseReleased(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				this_mouseDragged(e);
			}

			public void mouseMoved(MouseEvent e) {
				// nothing
			}
		});
	}

	private void updateTests() {
		if (ScheduleMainFrame.DEBUG >= 3)
				System.out.println(getClass().getName() + " updateTests"); //$NON-NLS-1$
		//		this.setCursor(UIStorage.WAIT_CURSOR);
		aContext.getDispatcher()
				.notify(
						new StatusMessageEvent(I18N
								.getString("Updating_tests_from_BD"))); //$NON-NLS-1$
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (dsi == null) return;

		//		dsi.GetTests(scaleStart.getTime(), scaleEnd.getTime());
		SurveyDataSourceImage sDataSrcImg = new SurveyDataSourceImage(dsi);
		String[] ids = sDataSrcImg.GetTests(scaleStart.getTime(), scaleEnd
				.getTime());

		SimpleDateFormat lsdf = new SimpleDateFormat("dd.MM.yyyy HH:mm"); //$NON-NLS-1$
		if (ScheduleMainFrame.DEBUG >= 3)
				System.out.println(ids.length + " test(s) found from " //$NON-NLS-1$
						+ lsdf.format(scaleStart) + " till " //$NON-NLS-1$
						+ lsdf.format(scaleEnd));

		//выбираем необходимые тесты из пула
		Hashtable hash = new Hashtable();
		for (int i = 0; i < ids.length; i++) {
			if (ScheduleMainFrame.DEBUG >= 5)
					System.out.println("get test#" + ids[i]); //$NON-NLS-1$
			Test test = (Test) Pool.get(Test.typ, ids[i]);
			//			DataSourceInterface dsi = aContext.getDataSourceInterface();
			if (test.getAnalysisId().length() > 0) //$NON-NLS-1$
					dsi.GetAnalysis(test.getAnalysisId());
			if (test.getEvaluationId().length() > 0) //$NON-NLS-1$
					dsi.GetEvaluation(test.getEvaluationId());
			//test.setChanged(false);

			/**
			 * todo fix problem due to null test
			 */

			if (test != null) {
				hash.put(test.getId(), test);
			} else {
				if (ScheduleMainFrame.DEBUG >= 3)
						System.err.println("test " + ids[i] + " is null"); //$NON-NLS-1$ //$NON-NLS-2$
			}

		}
		//фильтруем текущим фильтром
		tests = new DataSet(hash);
		tests = filter.filter(tests);

		//подгружаем тестреквесты и недостающие тесты
		HashSet treqs = new HashSet();
		for (Enumeration enum = tests.elements(); enum.hasMoreElements();) {
			Test test = (Test) enum.nextElement();
			treqs.add(test.getRequestId());
		}
		dsi.GetRequests();
		HashSet add_tests = new HashSet();
		for (Iterator it = treqs.iterator(); it.hasNext();) {
			TestRequest treq = (TestRequest) Pool.get(TestRequest.typ,
					(String) it.next());
			if (treq != null) {
				//				for (Enumeration en = treq.test_ids.elements();
				//					en.hasMoreElements();
				//					) {
				{
					java.util.List testIds = treq.getTestIds();
					for (Iterator it2 = testIds.iterator(); it2.hasNext();) {
						String testId = (String) it2.next();
						if (ScheduleMainFrame.DEBUG >= 3)
								System.out.println("test_id:" + testId); //$NON-NLS-1$
						if (tests.get(testId) == null) add_tests.add(testId);
					}
				}
			}
		}
		if (!add_tests.isEmpty())
				new SurveyDataSourceImage(dsi).GetTests((String[]) add_tests
						.toArray(new String[add_tests.size()]));

		// clear old tests
		if (testLines == null)
			testLines = new HashMap();
		else
			testLines.clear();
		// and fill with new ones

		removeAll();
		for (Enumeration en = tests.elements(); en.hasMoreElements();) {
			TestLine testLine;
			Test test = (Test) en.nextElement();
			if (testLines.containsKey(test.getMonitoredElementId()))
				testLine = (TestLine) testLines.get(test
						.getMonitoredElementId());
			else {
				String me_name = Pool.getName(MonitoredElement.typ, test
						.getMonitoredElementId());
				testLine = new TestLine(aContext,
				//parent.getViewport(),
						me_name, scaleStart.getTime(), scaleEnd.getTime(),
						margin / 2);
				testLine.setPreferredSize(new Dimension(0, 20));
				testLines.put(test.getMonitoredElementId(), testLine);
			}
			testLine.addTest(test);
			add(testLine);
		}

		//adding testlines to panel
		//		removeAll();
		//		for (Iterator it = testLines.values().iterator(); it.hasNext();)
		//		{
		//			testLine = (TestLine)it.next();
		//			add(testLine);
		//		}
		setPreferredSize(new Dimension(getPreferredSize().width,
				30 + 25 * testLines.values().size()));
		parent.repaint();
		//		this.setCursor(UIStorage.DEFAULT_CURSOR);
		dispatcher.notify(new StatusMessageEvent(I18N
				.getString("Updating_tests_from_BD_finished"))); //$NON-NLS-1$
		dispatcher.notify(new OperationEvent(tests, 0, COMMAND_NAME_ALL_TESTS));
	}
}