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

import com.syrus.AMFICOM.Client.Schedule.Filter.*;
import oracle.jdeveloper.layout.*;

public class PlanPanel extends JPanel implements OperationListener {
	private ApplicationContext aContext;
	private PlanToolBar toolBar;
	public int maxZoom = 50;

	protected int margin = 14;
	private HashMap testLines = new HashMap();
	// saved tests, loaded from server
	private DataSet tests;
	// new tests, which haven't saved yet
	private DataSet unsavedTests;
	private ObjectResourceFilter filter = null;

	private final static String[] scales =
		new String[] {
			"10 min",
			"1 hour",
			"6 hours",
			"1 day",
			"1 week",
			"1 month" };

	protected final static Step[] steps =
		new Step[] {
			new Step(Calendar.MINUTE, 1, 10, 6, 1),
			new Step(Calendar.MINUTE, 10, 60, 5, 5),
			new Step(Calendar.HOUR_OF_DAY, 1, 6, 6, 1),
			new Step(Calendar.HOUR_OF_DAY, 4, 24, 4, 2),
			new Step(Calendar.DAY_OF_MONTH, 1, 7, 4, 1),
			new Step(Calendar.DAY_OF_MONTH, 5, 30, 5, 1),
			};

	protected Calendar cal = Calendar.getInstance();
	protected SimpleDateFormat sdf = new SimpleDateFormat();

	// real start time including minutes and seconds
	protected Date startDate = new Date(System.currentTimeMillis());
	// rounded start and end time of range displayed
	protected Date scaleStart;
	protected Date scaleEnd;

	protected int scale = 0;
	protected int real_scale = 0;
	protected JScrollPane parent;

	protected Point startpos = new Point();
	protected Point currpos = new Point();
	protected Point tmppos;
	protected boolean select_by_mouse = false;

	public PlanPanel(
		JScrollPane parent,
		ApplicationContext aContext,
		Date start,
		int scale) {
		this(parent, aContext);
		setScale(scale);
		setStartDate(start);
		updRealScale();
	}

	public PlanPanel(JScrollPane parent, ApplicationContext aContext) {
		this.aContext = aContext;
		this.parent = parent;

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		init_module();

		filter = new TestFilter();

		setStartDate(new Date(System.currentTimeMillis()));
		updRealScale();
	}

	void init_module() {
	}

	private void jbInit() throws Exception {
		toolBar = new PlanToolBar(aContext, this);

		setLayout(new VerticalFlowLayout());
		//setLayout(new GridLayout(0,1));
		setBackground(SystemColor.window);
		setPreferredSize(new Dimension(600, 20));

		this.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}
			public void mouseClicked(MouseEvent e) {
			}
			public void mouseReleased(MouseEvent e) {
				this_mouseReleased(e);
			}
			public void mouseEntered(MouseEvent e) {
			}
			public void mouseExited(MouseEvent e) {
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				this_mouseDragged(e);
			}
			public void mouseMoved(MouseEvent e) {
			}
		});
	}

	public void operationPerformed(OperationEvent ae) {
	}

	void updateDate(Date date, int scale) {
		setScale(scale);
		setStartDate(date);
		update_tests();
		updRealScale();
		parent.updateUI();
	}

	public PlanToolBar getToolBar() {
		return toolBar;
	}

	public static String[] getSupportedScales() {
		return scales;
	}

	public void setScale(int n) {
		if (n < 0 || n >= scales.length) {
			System.err.println(
				"Unsupported scale: "
					+ n
					+ ". Use setScale(n);"
					+ " where n determine one of PlanPanel.getSupportedScales() element");
			return;
		}
		scale = n;
	}

	public void setStartDate(Date start) {
		startDate = start;

		cal.setTime(start);
		switch (scale) {
			case 4 :
			case 5 :
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case 2 :
			case 3 :
				cal.set(Calendar.MINUTE, 0);
			case 0 :
			case 1 :
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
		cal.add(steps[scale].scale, (int) ((double) steps[scale].total));
		scaleEnd = cal.getTime();
		//scroll calendar to start point
		cal.setTime(startDate);
	}

	public int getScale() {
		return scale;
	}

	public Date getStartDate() {
		return startDate;
	}

	void this_mousePressed(MouseEvent e) {
		startpos = e.getPoint();
		currpos = e.getPoint();
		if (e.getButton() == MouseEvent.BUTTON1) {
			setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			select_by_mouse = true;
		}
	}

	void this_mouseDragged(MouseEvent e) {
		upd_currpos(e);
		if (select_by_mouse)
			paintSelect(getGraphics().create());
	}

	void this_mouseReleased(MouseEvent e) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		if (currpos.x == startpos.x) {
			parent.repaint();
			return;
		}
		if (select_by_mouse) {
			select_by_mouse = false;
			double k =
				(double) (parent.getVisibleRect().width - 2 * margin)
					/ Math.abs((double) (startpos.x - currpos.x));
			int view_x = Math.min(startpos.x, currpos.x);
			updScale(k, view_x);
		}
	}

	void updScale(double k) {
		updScale(
			k,
			(int) (parent.getViewport().getViewPosition().x
				+ parent.getVisibleRect().width * (k * 0.5 - 0.5)
				- margin));
	}

	void updScale2Fit() {
		setPreferredSize(new Dimension(600, getPreferredSize().height));
		setSize(new Dimension(600, getPreferredSize().height));

		updRealScale();
		parent.updateUI();
	}

	void updScale(double k, int view_x) {
		if (getSize().width / parent.getVisibleRect().width >= maxZoom - 1
			&& k > 1) {
			parent.repaint();
			return;
		}

		if (getSize().width * k > maxZoom * parent.getVisibleRect().width)
			k =
				(double) (maxZoom * parent.getVisibleRect().width)
					/ (double) getSize().width;

		setPreferredSize(
			new Dimension(
				(int) ((double) (getSize().width - 2 * margin) * k),
				getPreferredSize().height));
		setSize(
			new Dimension(
				(int) ((double) (getSize().width - 2 * margin) * k),
				Math.max(getSize().height, getPreferredSize().height)));
		/*
				for (Iterator it = testLines.values().iterator(); it.hasNext();)
				{
					TestLine testLine = (TestLine)it.next();
					testLine.setPreferredSize(new Dimension(getPreferredSize().width, 20));
				}
		*/
		Point p = new Point((int) ((view_x - margin) * k), view_x);
		parent.getViewport().setViewPosition(p);

		updRealScale();

		parent.updateUI();
	}

	void updRealScale() {
		real_scale = scale;

		double k =
			(double) parent.getViewport().getViewRect().width
				/ (double) getSize().width;
		long visible_time =
			(long) ((scaleEnd.getTime() - scaleStart.getTime()) * k);
		while (visible_time != 0 && real_scale > 0) {
			cal.setTimeInMillis(0);
			cal.add(
				steps[real_scale].scale,
				steps[real_scale].total / steps[real_scale].one);
			if (cal.getTimeInMillis() > visible_time)
				real_scale--;
			else
				break;
		}

		cal.setTimeInMillis(0);
		cal.add(steps[real_scale].scale, steps[real_scale].one);
		long diff = cal.getTimeInMillis();
		double delta =
			(double) (getWidth() - 2 * margin)
				/ ((double) (scaleEnd.getTime() - scaleStart.getTime())
					/ (double) diff);

		double sub_delta = delta / (double) steps[real_scale].subscales;
		if (delta >= 0 && delta < 35 && real_scale < steps.length - 1)
			real_scale++;

		switch (real_scale) {
			case 1 :
				sdf.applyPattern("HH:mm");
				break;
			case 0 :
				sdf.applyPattern("HH:mm:ss");
				break;
			case 2 :
			case 3 :
				sdf.applyPattern("HH:mm");
				break;
			case 5 :
				steps[5].total = cal.getActualMaximum(steps[5].scale);
			case 4 :
				sdf.applyPattern("dd.MM");
		}
		cal.setTime(startDate);
		switch (real_scale) {
			case 4 :
			case 5 :
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case 3 :
				cal.set(Calendar.MINUTE, 0);
			case 0 :
			case 1 :
			case 2 :
				cal.set(Calendar.SECOND, 0);
		}
	}

	void update_tests() {
		DataSourceInterface dsi = aContext.getDataSourceInterface();
		if (dsi == null)
			return;

		String[] ids =
			new SurveyDataSourceImage(dsi).GetTests(
				scaleStart.getTime(),
				scaleEnd.getTime());		
		

		SimpleDateFormat lsdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		System.out.println(
			ids.length
				+ " test(s) found from "
				+ lsdf.format(scaleStart)
				+ " till "
				+ lsdf.format(scaleEnd));

		//выбираем необходимые тесты из пула
		Hashtable hash = new Hashtable();
		for (int i = 0; i < ids.length; i++) {
			System.out.println("get test#"+ids[i]);
			Test test = (Test) Pool.get(Test.typ, ids[i]);
			hash.put(test.getId(), test);
		}
		//фильтруем текущим фильтром
		tests = new DataSet(hash);
		tests = filter.filter(tests);

		//подгружаем тестреквесты и недостающие тесты
		HashSet treqs = new HashSet();
		for (Enumeration enum = tests.elements(); enum.hasMoreElements();) {
			Test test = (Test) enum.nextElement();
			treqs.add(test.request_id);
		}
		dsi.GetRequests();
		HashSet add_tests = new HashSet();
		for (Iterator it = treqs.iterator(); it.hasNext();) {
			TestRequest treq =
				(TestRequest) Pool.get(TestRequest.typ, (String) it.next());
			if (treq != null){			
//				for (Enumeration en = treq.test_ids.elements();
//					en.hasMoreElements();
//					) {
					for (int i=0;i<treq.test_ids.size();i++){
					String test_id = (String) treq.test_ids.get(i);
					System.out.println("test_id:"+test_id);
					if (tests.get(test_id) == null)
						add_tests.add(test_id);
				}
			}
		}
		if (!add_tests.isEmpty())
			new SurveyDataSourceImage(dsi).GetTests(
				(String[]) add_tests.toArray(new String[add_tests.size()]));

		// clear old tests
		testLines = new HashMap();
		// and fill with new ones

		removeAll();
		for (Enumeration enum = tests.elements(); enum.hasMoreElements();) {
			TestLine testLine;
			Test test = (Test) enum.nextElement();
			if (testLines.containsKey(test.monitored_element_id))
				testLine = (TestLine) testLines.get(test.monitored_element_id);
			else {
				String me_name =
					Pool.getName(
						MonitoredElement.typ,
						test.monitored_element_id);
				testLine = new TestLine(aContext,
					//parent.getViewport(),
	me_name, scaleStart.getTime(), scaleEnd.getTime(), margin / 2);
				testLine.setPreferredSize(new Dimension(0, 20));
				testLines.put(test.monitored_element_id, testLine);
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
		setPreferredSize(
			new Dimension(
				getPreferredSize().width,
				30 + 25 * testLines.values().size()));
		parent.repaint();
	}

	protected void paintComponent(Graphics g) {
		// for paint testlines

		super.paintComponent(g);
		cal.setTimeInMillis(0);
		cal.add(steps[real_scale].scale, steps[real_scale].one);
		long diff = cal.getTimeInMillis();
		double delta =
			(double) (getWidth() - 2 * margin)
				/ ((double) (scaleEnd.getTime() - scaleStart.getTime())
					/ (double) diff);
		double sub_delta = delta / (double) steps[real_scale].subscales;

		paintScales(g, diff, delta, sub_delta);
		/*
				for (Iterator it = testLines.values().iterator(); it.hasNext();)
				{
					TestLine testLine = (TestLine)it.next();
					testLine.paint(g);
				}*/
		paintScaleDigits(g, diff, delta, sub_delta);

	}

	protected void paintScales(
		Graphics g,
		long diff,
		double delta,
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
			g.drawLine(
				(int) (_delta * counter) + margin,
				h,
				(int) (_delta * counter) + margin,
				h - 3);
			counter++;
		}
		cal.setTime(scaleStart);

		/*
		int h = getHeight() - 1;
		int w = getWidth();
		double range = (double)steps[scale].total / (double)steps[scale].one;
		double step = (double)(w - 2 * margin) / range;
		int tmp;
		
		for (int i = 0; i <= Math.round(range); i++)
		{
			tmp = (int)(step*i);
			g.setColor(new Color(240, 240, 240));
			g.drawLine(tmp + margin, 0, tmp + margin, h);
		
			g.setColor(Color.black);
			g.drawLine(tmp + margin, h - 5, tmp + margin, h);
			for (int j = 1; j < steps[scale].subscales; j++)
				g.drawLine(tmp + (int)(step/(double)steps[scale].subscales*j) + margin, h - 3,
									 tmp + (int)(step/(double)steps[scale].subscales*j) + margin, h);
		
		
		}
		*/
		g.drawLine(0, h, w, h);
	}

	protected void paintScaleDigits(
		Graphics g,
		long diff,
		double delta,
		double sub_delta) //, double sub_sub_delta)
	{
		int h = getHeight() - 1;
		int w = getWidth();

		long _diff = diff;
		double _delta = delta;
		if (sub_delta > 60) {
			_delta = sub_delta;
			_diff = diff / steps[real_scale].subscales;
		} else if (real_scale == 0)
			sdf.applyPattern("HH:mm");

		int shift =
			(int) g
				.getFontMetrics()
				.getStringBounds(sdf.format(cal.getTime()), g)
				.getCenterX();

		cal.setTime(scaleStart);
		g.setColor(Color.black);
		int counter = 0;
		Color c = new Color(240, 240, 240);
		while (cal.getTime().compareTo(scaleEnd) <= 0) {
			g.setColor(c);
			g.drawLine(
				(int) (_delta * counter) + margin,
				0,
				(int) (_delta * counter) + margin,
				h);
			g.setColor(Color.BLACK);
			g.drawLine(
				(int) (_delta * counter) + margin,
				h,
				(int) (_delta * counter) + margin,
				h - 5);

			String value = sdf.format(cal.getTime());
			cal.setTimeInMillis(cal.getTimeInMillis() + _diff);
			g.drawString(
				value,
				(int) (_delta * counter) + margin - shift,
				h - 7);
			if (value.startsWith("00:00"))
				g.drawString(
					new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime()),
					(int) (_delta * counter) + margin - 27,
					h - 17);
			counter++;
		}
		cal.setTime(scaleStart);
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

		if (currpos.x < vx)
			currpos.x = vx;
		if (currpos.x > vx + wx)
			currpos.x = vx + wx;
		if (currpos.y < vy)
			currpos.y = vy;
		if (currpos.y > vy + wy)
			currpos.y = vy + wy;
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
		JFrame mainFrame = new JFrame("TimeParametersPanel");
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
	

	static class Step {
		Step(int scale, int one, int total, int subscales, int align) {
			this.scale = scale;
			this.one = one;
			this.total = total;
			this.subscales = subscales;
			this.align = align;
		}
		int scale; //степень градации основных делений шкалы
		int one; //число единиц измерения внутри одного деления
		int total; //общее число единиц измерения
		int subscales; //число дополнительных делений внутри основного
		int align; //выравнивание по кратному
	}
}
