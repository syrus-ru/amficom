
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import oracle.jdeveloper.layout.VerticalFlowLayout;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Schedule.TestsEditor;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Test;

public class PlanPanel extends JPanel implements OperationListener, TestsEditor {

	private static final long	serialVersionUID	= 1032754807376863494L;

	private static class Step {

		int	align;		// выравнивание по кратному

		int	one;		// число единиц измерения внутри одного деления

		int	scale;		// степень градации основных делений шкалы

		int	subscales;	// число дополнительных делений внутри основного

		int	total;		// общее число единиц измерения

		Step(int scale, int one, int total, int subscales, int align) {
			this.scale = scale;
			this.one = one;
			this.total = total;
			this.subscales = subscales;
			this.align = align;
		}
	}
	public static final String[]	SCALES			= new String[] {
			"10 min", "1 hour", "6 hours", "1 day", "1 week", "1 month"};					//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

	/**
	 * SCALES in milliseconds
	 */
	public static final long[]		SCALES_MS		= new long[] { 1000 * 60 * 10, 1000 * 60 * 60, 1000 * 60 * 60 * 6,
			1000 * 60 * 60 * 24, 1000 * 60 * 60 * 24 * 7, 1000 * 60 * 60 * 31};

	protected static final Step[]	STEPS			= new Step[] { new Step(Calendar.MINUTE, 1, 10, 6, 1),
			new Step(Calendar.MINUTE, 10, 60, 5, 5), new Step(Calendar.HOUR_OF_DAY, 1, 6, 6, 1),
			new Step(Calendar.HOUR_OF_DAY, 4, 24, 4, 2), new Step(Calendar.DAY_OF_MONTH, 1, 7, 4, 1),
			new Step(Calendar.DAY_OF_MONTH, 5, 30, 5, 1),};
	protected int					actualScale		= 0;

	protected Calendar				cal				= Calendar.getInstance();

	protected Point					currpos			= new Point();

	protected final int				margin			= 14;
	protected JScrollPane			parent;
	protected int					scale			= 0;
	protected Date					scaleEnd;

	// rounded start and end time of range displayed
	protected Date					scaleStart;

	protected SimpleDateFormat		sdf				= new SimpleDateFormat();

	protected boolean				selectedByMouse	= false;

	// real start time including minutes and seconds
	protected Date					startDate		= new Date(System.currentTimeMillis());

	protected Point					startpos		= new Point();

	// saved tests, loaded from server
	// private DataSet tests;

	protected Point					tmppos;
	private ApplicationContext		aContext;
	private SchedulerModel schedulerModel;

	private Dispatcher				dispatcher;
	private int						maxZoom			= 50;
	private Map						testLines		= new HashMap();

	private PlanToolBar				toolBar;

	// new tests, which haven't saved yet
	// private ArrayList unsavedTests;

	public PlanPanel(JScrollPane parent, ApplicationContext aContext) {
		this.aContext = aContext;
		this.parent = parent;
		this.toolBar = new PlanToolBar(aContext, this);
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		this.schedulerModel.addTestsEditor(this);

		setLayout(new VerticalFlowLayout());
		// setLayout(new GridLayout(0,1));
		setBackground(SystemColor.window);
		setPreferredSize(new Dimension(600, 20));
		// setCursor(UIStorage.DEFAULT_CURSOR);

		this.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				PlanPanel.this.startpos = e.getPoint();
				PlanPanel.this.currpos = e.getPoint();
				if (e.getButton() == MouseEvent.BUTTON1) {
					setCursor(UIStorage.CROSS_HAIR_CURSOR);
					PlanPanel.this.selectedByMouse = true;
				}
			}

			public void mouseReleased(MouseEvent e) {
				// if (e.getClickCount() > 0)
				{
					setCursor(UIStorage.DEFAULT_CURSOR);

					if (PlanPanel.this.currpos.x == PlanPanel.this.startpos.x) {
						PlanPanel.this.parent.repaint();
						return;
					}
					if (PlanPanel.this.selectedByMouse) {
						PlanPanel.this.selectedByMouse = false;
						double k = (PlanPanel.this.parent.getVisibleRect().width - 2 * PlanPanel.this.margin)
								/ Math.abs((double) (PlanPanel.this.startpos.x - PlanPanel.this.currpos.x));
						int viewX = Math.min(PlanPanel.this.startpos.x, PlanPanel.this.currpos.x);
						updateScale(k, viewX);
					}
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				PlanPanel.this.tmppos = PlanPanel.this.currpos;
				PlanPanel.this.currpos = e.getPoint();

				// check if we go out the borders of panel
				int vx = 0;
				int wx = getWidth();
				int vy = 0;
				int wy = getHeight();

				if (PlanPanel.this.currpos.x < vx)
					PlanPanel.this.currpos.x = vx;
				if (PlanPanel.this.currpos.x > vx + wx)
					PlanPanel.this.currpos.x = vx + wx;
				if (PlanPanel.this.currpos.y < vy)
					PlanPanel.this.currpos.y = vy;
				if (PlanPanel.this.currpos.y > vy + wy)
					PlanPanel.this.currpos.y = vy + wy;
				if (PlanPanel.this.selectedByMouse)
					paintSelect(getGraphics().create());
			}

			public void mouseMoved(MouseEvent e) {
				// nothing
			}
		});

		initModule(aContext.getDispatcher());
		setStartDate(new Date(System.currentTimeMillis()));
		updateRealScale();
	}

	// private static final int TIME_OUT = 500;

	public PlanPanel(JScrollPane parent, ApplicationContext aContext, Date start, int scale) {
		this(parent, aContext);
		setScale(scale);
		setStartDate(start);
		updateRealScale();
	}

	/**
	 * @todo only for testing mode
	 */
	public static void main(String[] args) {
		JScrollPane scroll = new JScrollPane();

		// PlanLayeredPanel panel = new PlanLayeredPanel();
		PlanPanel demo = new PlanPanel(scroll, null);
		PlanToolBar toolBar = demo.getToolBar();
		// panel.setGraphPanel(mainPanel);
		scroll.getViewport().add(demo);
		JFrame mainFrame = new JFrame("TimeParametersPanel"); //$NON-NLS-1$
		mainFrame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// mainFrame.getContentPane().add(demo);
		mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
		mainFrame.getContentPane().add(scroll, BorderLayout.CENTER);
		mainFrame.pack();
		mainFrame.setSize(new Dimension(600, 465));
		mainFrame.setVisible(true);
	}

	public int getScale() {
		return this.scale;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public PlanToolBar getToolBar() {
		return this.toolBar;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CLEAN)) {
			for (Iterator it = this.testLines.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				TestLine line = (TestLine) this.testLines.get(key);
				((SchedulerModel)this.aContext.getApplicationModel()).removeTestsEditor(line);
			}
			this.testLines.clear();
			removeAll();
			revalidate();
			this.parent.repaint();
		}
//		else if (commandName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
//			updateTests1();
//			repaint();
//			revalidate();
//			this.parent.repaint();
//		}

	}
	
	public void setScale(int n) {
		if (n < 0 || n >= SCALES.length) {
			Environment.log(Environment.LOG_LEVEL_WARNING, "Unsupported scale: " //$NON-NLS-1$
					+ n + ". Use setScale(n);" //$NON-NLS-1$
					+ " where n determine one of PlanPanel.getSupportedScales() element"); //$NON-NLS-1$
			return;
		}
		this.scale = n;
		setStartDate(this.scaleStart);
	}

	public void setStartDate(Date start) {
		this.startDate = start;
		if (start != null) {
			this.cal.setTime(start);
			switch (this.scale) {
				case 4:
				case 5:
					this.cal.set(Calendar.HOUR_OF_DAY, 0);
				case 2:
				case 3:
					this.cal.set(Calendar.MINUTE, 0);
				case 0:
				case 1:
					this.cal.set(Calendar.SECOND, 0);
			}

			// округляем до шага
			int num = this.cal.get(STEPS[this.scale].scale);
			while (num / STEPS[this.scale].align * STEPS[this.scale].align != num) {
				this.cal.add(STEPS[this.scale].scale, -1);
				num = this.cal.get(STEPS[this.scale].scale);
			}

			this.scaleStart = this.cal.getTime();
			// scroll calendar to end of period
			this.cal.add(STEPS[this.scale].scale, STEPS[this.scale].total);
			this.scaleEnd = this.cal.getTime();
			// scroll calendar to start point
			this.cal.setTime(this.startDate);
		}
	}

	protected void paintComponent(Graphics g) {
		// for paint testlines

		super.paintComponent(g);
		this.cal.setTimeInMillis(0);
		this.cal.add(STEPS[this.actualScale].scale, STEPS[this.actualScale].one);
		long diff = this.cal.getTimeInMillis();
		double delta = (getWidth() - 2 * this.margin)
				/ ((double) (this.scaleEnd.getTime() - this.scaleStart.getTime()) / (double) diff);
		double subDelta = delta / STEPS[this.actualScale].subscales;

		paintScales(g, diff, delta, subDelta);
		/*
		 * for (Iterator it = testLines.values().iterator(); it.hasNext();) {
		 * TestLine testLine = (TestLine)it.next(); testLine.paint(g); }
		 */
		paintScaleDigits(g, diff, delta, subDelta);

	}

	protected void paintScaleDigits(Graphics g,
									long diff,
									double delta,
									double subDelta) // ,
	// double
	// sub_sub_delta)
	{
		int h = getHeight() - 1;
		// int w = getWidth();

		long tmpDiff = diff;
		double tmpDelta = delta;
		if (subDelta > 60) {
			tmpDelta = subDelta;
			tmpDiff = diff / STEPS[this.actualScale].subscales;
		} else if (this.actualScale == 0)
			this.sdf.applyPattern("HH:mm"); //$NON-NLS-1$

		int shift = (int) g.getFontMetrics().getStringBounds(this.sdf.format(this.cal.getTime()), g).getCenterX();

		this.cal.setTime(this.scaleStart);
		g.setColor(Color.black);
		int counter = 0;
		Color c = new Color(240, 240, 240);
		while (this.cal.getTime().compareTo(this.scaleEnd) <= 0) {
			g.setColor(c);
			g.drawLine((int) (tmpDelta * counter) + this.margin, 0, (int) (tmpDelta * counter) + this.margin, h);
			g.setColor(Color.BLACK);
			g.drawLine((int) (tmpDelta * counter) + this.margin, h, (int) (tmpDelta * counter) + this.margin, h - 5);

			String value = this.sdf.format(this.cal.getTime());
			this.cal.setTimeInMillis(this.cal.getTimeInMillis() + tmpDiff);
			g.drawString(value, (int) (tmpDelta * counter) + this.margin - shift, h - 7);
			if (value.startsWith("00:00")) //$NON-NLS-1$
				g.drawString(new SimpleDateFormat("dd.MM.yyyy").format(this.cal //$NON-NLS-1$
						.getTime()), (int) (tmpDelta * counter) + this.margin - 27, h - 17);
			counter++;
		}
		this.cal.setTime(this.scaleStart);
	}

	protected void paintScales(	Graphics g,
								long diff,
								double delta,
								double subDelta) {

		int h = getHeight() - 1;
		int w = getWidth();

		long tmpDiff = diff;
		double tmpDelta = delta;
		if (subDelta > 10) {
			tmpDelta = subDelta;
			tmpDiff = diff / STEPS[this.actualScale].subscales;
		}

		this.cal.setTime(this.scaleStart);
		g.setColor(Color.black);
		int counter = 0;
		while (this.cal.getTime().compareTo(this.scaleEnd) <= 0) {
			this.cal.setTimeInMillis(this.cal.getTimeInMillis() + tmpDiff);
			g.drawLine((int) (tmpDelta * counter) + this.margin, h, (int) (tmpDelta * counter) + this.margin, h - 3);
			counter++;
		}
		this.cal.setTime(this.scaleStart);

		/*
		 * int h = getHeight() - 1; int w = getWidth(); double range =
		 * (double)STEPS[scale].total / (double)STEPS[scale].one; double step =
		 * (double)(w - 2 * margin) / range; int tmp;
		 * 
		 * for (int i = 0; i <= Math.round(range); i++) { tmp = (int)(step*i);
		 * g.setColor(new Color(240, 240, 240)); g.drawLine(tmp + margin, 0, tmp +
		 * margin, h);
		 * 
		 * g.setColor(Color.black); g.drawLine(tmp + margin, h - 5, tmp +
		 * margin, h); for (int j = 1; j < STEPS[scale].subscales; j++)
		 * g.drawLine(tmp + (int)(step/(double)STEPS[scale].subscales*j) +
		 * margin, h - 3, tmp + (int)(step/(double)STEPS[scale].subscales*j) +
		 * margin, h); }
		 */
		g.drawLine(0, h, w, h);
	}

	protected void paintSelect(Graphics g) {
		g.setXORMode(Color.gray);
		g.drawLine(this.startpos.x, 0, this.startpos.x, getHeight());
		g.drawLine(this.startpos.x, 0, this.startpos.x, getHeight());
		g.drawLine(this.tmppos.x, 0, this.tmppos.x, getHeight());
		g.drawLine(this.currpos.x, 0, this.currpos.x, getHeight());

	}

	void setDate(	Date startDate,
					int scale) {
		this.setCursor(UIStorage.WAIT_CURSOR);
		setScale(scale);
		setStartDate(startDate);
		SchedulerModel model = (SchedulerModel) this.aContext.getApplicationModel();
		try {
			model.updateTests(this.scaleStart.getTime(), this.scaleEnd.getTime());
			updateTests1();
			this.setCursor(UIStorage.DEFAULT_CURSOR);

		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
		
	}

	void updateTestLines() {
		for (Iterator it = this.testLines.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			TestLine line = (TestLine) this.testLines.get(key);
			line.setStart(this.scaleStart.getTime());
			line.setEnd(this.scaleEnd.getTime());
		}
		updateRealScale();
	}

	void updateScale(double k) {
		updateScale(k, (int) (this.parent.getViewport().getViewPosition().x + this.parent.getVisibleRect().width
				* (k * 0.5 - 0.5) - this.margin));
		// BoundedRangeModel model =
		// this.parent.getHorizontalScrollBar().getModel();
		updateRealScale();
		this.parent.revalidate();
	}

	void updateScale(	double k,
						int view_x) {
		if (getSize().width / this.parent.getVisibleRect().width >= this.maxZoom - 1 && k > 1) {
			this.parent.repaint();
			return;
		}

		if (getSize().width * k > this.maxZoom * this.parent.getVisibleRect().width)
			k = (double) (this.maxZoom * this.parent.getVisibleRect().width) / (double) getSize().width;

		setPreferredSize(new Dimension((int) ((getSize().width - 2 * this.margin) * k), getPreferredSize().height));
		setSize(new Dimension((int) ((getSize().width - 2 * this.margin) * k), Math.max(getSize().height,
			getPreferredSize().height)));
		/*
		 * for (Iterator it = testLines.values().iterator(); it.hasNext();) {
		 * TestLine testLine = (TestLine)it.next();
		 * testLine.setPreferredSize(new Dimension(getPreferredSize().width,
		 * 20)); }
		 */
		Point p = new Point((int) ((view_x - this.margin) * k), view_x);
		this.parent.getViewport().setViewPosition(p);

		updateRealScale();

		this.parent.revalidate();
	}

	void updateScale2Fit() {
		setPreferredSize(new Dimension(600, getPreferredSize().height));
		setSize(new Dimension(600, getPreferredSize().height));

		updateRealScale();
		this.parent.revalidate();
	}

	private void initModule(Dispatcher dispatcher) {
		// timer.start();
		this.dispatcher = dispatcher;
//		this.dispatcher.register(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	public void unregisterDispatcher() {
//		this.dispatcher.unregister(this, SchedulerModel.COMMAND_REFRESH_TESTS);
	}

	private void updateRealScale() {
		if ((this.scaleEnd != null) && (this.scaleEnd != null)) {
			this.actualScale = this.scale;

			double k = (double) this.parent.getViewport().getViewRect().width / (double) getSize().width;
			long visibleTime = (long) ((this.scaleEnd.getTime() - this.scaleStart.getTime()) * k);
			while (visibleTime != 0 && this.actualScale > 0) {
				this.cal.setTimeInMillis(0);
				this.cal
						.add(STEPS[this.actualScale].scale, STEPS[this.actualScale].total / STEPS[this.actualScale].one);
				if (this.cal.getTimeInMillis() > visibleTime)
					this.actualScale--;
				else
					break;
			}

			this.cal.setTimeInMillis(0);
			this.cal.add(STEPS[this.actualScale].scale, STEPS[this.actualScale].one);
			long diff = this.cal.getTimeInMillis();
			double delta = (getWidth() - 2 * this.margin)
					/ ((double) (this.scaleEnd.getTime() - this.scaleStart.getTime()) / (double) diff);

			// double sub_delta = delta / STEPS[actualScale].subscales;
			if (delta >= 0 && delta < 35 && this.actualScale < STEPS.length - 1)
				this.actualScale++;

			switch (this.actualScale) {
				case 1:
					this.sdf.applyPattern("HH:mm"); //$NON-NLS-1$
					break;
				case 0:
					this.sdf.applyPattern("HH:mm:ss"); //$NON-NLS-1$
					break;
				case 2:
				case 3:
					this.sdf.applyPattern("HH:mm"); //$NON-NLS-1$
					break;
				case 5:
					STEPS[5].total = this.cal.getActualMaximum(STEPS[5].scale);
				case 4:
					this.sdf.applyPattern("dd.MM"); //$NON-NLS-1$
			}
			this.cal.setTime(this.startDate);
			switch (this.actualScale) {
				case 4:
				case 5:
					this.cal.set(Calendar.HOUR_OF_DAY, 0);
				case 3:
					this.cal.set(Calendar.MINUTE, 0);
				case 0:
				case 1:
				case 2:
					this.cal.set(Calendar.SECOND, 0);
			}
		}
		revalidate();
		repaint();
		this.parent.revalidate();
		this.parent.repaint();
	}

	private void addTestLine(MonitoredElement monitoredElement) {
		TestLine testLine;
		if (this.testLines.containsKey(monitoredElement.getId()))
			testLine = (TestLine) this.testLines.get(monitoredElement.getId());
		else {
			testLine = new TestLine(this.aContext, monitoredElement.getName(), this.scaleStart.getTime(), this.scaleEnd
					.getTime(), this.margin / 2, monitoredElement.getId());
			testLine.setPreferredSize(new Dimension(0, 25));
			this.testLines.put(monitoredElement.getId(), testLine);
			super.add(testLine);
		}
	}

	public void updateTests() {
		updateTests1();
		repaint();
		revalidate();
		this.parent.repaint();
	}
	
	protected void updateTests1() {
		Collection tests = ((SchedulerModel) this.aContext.getApplicationModel()).getTests();
		for (Iterator it = tests.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			this.addTestLine(test.getMonitoredElement());
		}
		super.setPreferredSize(new Dimension(getPreferredSize().width, 30 + 25 * this.testLines.values().size()));
		this.updateRealScale();
	}
}