/*-
 * $Id: PlanPanel.java,v 1.52 2005/09/23 05:39:26 bob Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Schedule.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @version $Revision: 1.52 $, $Date: 2005/09/23 05:39:26 $
 * @author $Author: bob $
 * @module scheduler
 */
final class PlanPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long	serialVersionUID	= 3258417243925984817L;

	public static final int	TIME_OUT	= 500;

	private Timer			timer		= new Timer(TIME_OUT, this);

	private final static class Step {

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
	static final String[]	SCALES				= new String[] {
			"Text.Plan.Toolbar.Scope.10Minutes", 
			"Text.Plan.Toolbar.Scope.1Hour", 
			"Text.Plan.Toolbar.Scope.6Hours", 
			"Text.Plan.Toolbar.Scope.1Day", 
			"Text.Plan.Toolbar.Scope.1Week", 
			"Text.Plan.Toolbar.Scope.1Month"};

	private static final  String[] SCALE_PATTERNS = new String[] {"HH:mm:ss", 
			"HH:mm", 
			"HH:mm", 
			"HH:mm", 
			"dd.MM", 
			"dd.MM"};
	
	/**
	 * SCALES in milliseconds
	 */
	public static final long[]		SCALES_MS			= new long[] { 
		1000L * 60L * 10L, 
		1000L * 60L * 60L,
		1000L * 60L * 60L * 6L, 
		1000L * 60L * 60L * 24L, 
		1000L * 60L * 60L * 24L * 7L, 
		1000L * 60L * 60L * 31L};

	protected static final Step[]	STEPS				= new Step[] { 
		new Step(Calendar.MINUTE, 1, 10, 6, 1),
		new Step(Calendar.MINUTE, 10, 60, 5, 5), 
		new Step(Calendar.HOUR_OF_DAY, 1, 6, 6, 1),
		new Step(Calendar.HOUR_OF_DAY, 4, 24, 4, 2), 
		new Step(Calendar.DAY_OF_MONTH, 1, 7, 4, 1),
		new Step(Calendar.DAY_OF_MONTH, 5, 30, 5, 1)};
	protected int					actualScale			= 0;

	protected Calendar				cal					= Calendar.getInstance();

	

	protected final static int		MARGIN				= 14;
	protected JScrollPane			parent;
	protected int					scale				= 0;
	protected Date					scaleEnd;

	// rounded start and end time of range displayed
	protected Date					scaleStart;

	protected SimpleDateFormat		sdf					= new SimpleDateFormat();

	// real start time including minutes and seconds
	protected Date					startDate			= new Date();

	protected Point					startPosition;
	protected Point					currentPosition;
	
	ApplicationContext				aContext;
//	private SchedulerModel			schedulerModel;

	private static final int		MAX_ZOOM			= 200;
	Map<Identifier, TestLine> testLines = new HashMap<Identifier, TestLine>();
	Map testTemporalLines = new HashMap();

	private PlanToolBar				toolBar;

	private Color selectionColor;

	Dispatcher	dispatcher;

	SchedulerModel	schedulerModel;

	private SimpleDateFormat	sdf2;
	
	public PlanPanel(JScrollPane parent, ApplicationContext aContext) {
		this.aContext = aContext;
		
		
		this.sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		
		this.parent = parent;
		this.toolBar = new PlanToolBar(aContext, this);
		this.dispatcher = aContext.getDispatcher();		
		
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TESTS, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		
		this.addComponentListener(this.createComponentListener());
		this.addMouseListener(this.createMouseListener());
		this.addMouseMotionListener(this.createMouseMotionListener());

		this.timer.start();

		this.setToolTipText("");
		this.setStartDate(new Date(System.currentTimeMillis()));
		this.updateRealScale();
		this.setBackground(UIManager.getColor(ResourceKeys.COLOR_GRAPHICS_BACKGROUND));
	}

	// private static final int TIME_OUT = 500;


	public PlanPanel(final JScrollPane parent, 
	                 final ApplicationContext aContext, 
	                 final Date start,
	                 final int scale) {
		this(parent, aContext);
		this.setScale(scale);
		this.setStartDate(start);
		this.updateRealScale();
	}
	
	private final ComponentAdapter createComponentListener() {
		return new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent e) {
				int width = PlanPanel.this.getWidth();
				Dimension dimension = null;
				for (int i = 0; i < PlanPanel.this.getComponentCount(); i++) {
					Component component = PlanPanel.this.getComponent(i);
					if (component instanceof JComponent) {
						JComponent jcomponent = (JComponent) component;
						if (dimension == null) {
							dimension = new Dimension(width, jcomponent.getHeight());
						}
						jcomponent.setPreferredSize(dimension);
					}
				}
			}
		};		
	}
	
	final Date getDateByMousePosition(final MouseEvent e) {
		long start = PlanPanel.this.scaleStart.getTime();
		long end = PlanPanel.this.scaleEnd.getTime();
		
		return new Date((start + ((e.getX() - PlanPanel.MARGIN / 2) *  (end - start) / ((PlanPanel.this.getWidth() - PlanPanel.MARGIN)))));
	}
	
	private final MouseListener createMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				this.mouseClicked(e);
			}
			
			@Override
			public void mouseClicked(final MouseEvent e) {
				PlanPanel.this.schedulerModel.unselectTests();
				
				
				PlanPanel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, 
					SchedulerModel.COMMAND_DATE_OPERATION, 
					null, 
					PlanPanel.this.getDateByMousePosition(e)));
				
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				if (PlanPanel.this.startPosition != null && PlanPanel.this.currentPosition != null
						&& Math.abs(PlanPanel.this.startPosition.x - PlanPanel.this.currentPosition.x) > 5) {
					double k = (PlanPanel.this.parent.getVisibleRect().width - 2 * MARGIN)
							/ Math.abs((double) (PlanPanel.this.startPosition.x - PlanPanel.this.currentPosition.x));
					int viewX = Math.min(PlanPanel.this.startPosition.x, PlanPanel.this.currentPosition.x);
					updateScale(k, viewX);
				}
				PlanPanel.this.startPosition = null;
				PlanPanel.this.currentPosition = null;
				PlanPanel.this.repaint();
			}
		};
		
	}
	
	private final MouseMotionAdapter createMouseMotionListener() {
		return new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				
//				System.out.println(".mouseDragged()");
				
				PlanPanel.this.currentPosition = e.getPoint();

				if (PlanPanel.this.startPosition == null) {
					PlanPanel.this.startPosition = PlanPanel.this.currentPosition;
					PlanPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
					return;
				}
				
				// check if we go out the borders of panel
				int vx = 0;
				int wx = getWidth();
				int vy = 0;
				int wy = getHeight();

				if (PlanPanel.this.currentPosition.x < vx)
					PlanPanel.this.currentPosition.x = vx;
				if (PlanPanel.this.currentPosition.x > vx + wx)
					PlanPanel.this.currentPosition.x = vx + wx;
				if (PlanPanel.this.currentPosition.y < vy)
					PlanPanel.this.currentPosition.y = vy;
				if (PlanPanel.this.currentPosition.y > vy + wy)
					PlanPanel.this.currentPosition.y = vy + wy;

				PlanPanel.this.repaint();
			}

		};
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

	public void setScale(final int n) {
		if (n < 0 || n >= SCALES.length) {
			throw new IllegalArgumentException("Unsupported scale: " //$NON-NLS-1$
					+ n + ". Use setScale(n);" //$NON-NLS-1$
					+ " where n determine one of PlanPanel.getSupportedScales() element"); //$NON-NLS-1$
		}
		this.scale = n;
		this.setStartDate(this.scaleStart);
	}

	public void setStartDate(Date start) {

		this.startDate = start;
		if (start != null) {
			this.cal.setTime(start);
			if (this.scale > 1) {
				this.cal.set(Calendar.MINUTE, 0);
			}
			this.cal.set(Calendar.SECOND, 0);

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

	@Override
	public String getToolTipText(final MouseEvent event) {
		final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		return sdf.format(this.getDateByMousePosition(event));
	}

	public void actionPerformed(final ActionEvent e) {
		this.revalidate();
		this.repaint();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TESTS)) {
			updateTests();
		} else if (propertyName.equals(SchedulerModel.COMMAND_REFRESH_TEST)) {
			try {
				updateTest();
			} catch (final ApplicationException e) {
				e.printStackTrace();
				AbstractMainFrame.showErrorMessage(e.getMessage());
			}
		}
	
		
	}

	protected void paintScaleDigits(Graphics g,
									long diff,
									double delta,
									double subDelta) {
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
			g.drawLine((int) (tmpDelta * counter) + MARGIN, 0, (int) (tmpDelta * counter) + MARGIN, h);
			g.setColor(Color.BLACK);
			g.drawLine((int) (tmpDelta * counter) + MARGIN, h, (int) (tmpDelta * counter) + MARGIN, h - 5);

			int h1 = this.cal.get(Calendar.HOUR_OF_DAY);
			String value = this.sdf.format(this.cal.getTime());
			this.cal.setTimeInMillis(this.cal.getTimeInMillis() + tmpDiff);
			int h2 = this.cal.get(Calendar.HOUR_OF_DAY);
			g.drawString(value, (int) (tmpDelta * counter) + MARGIN - shift, h - 7);
			if (value.startsWith("00:00")) {
				g.drawString(this.sdf2.format(this.cal 
						.getTime()), (int) (tmpDelta * counter) + MARGIN - 27, h - 17);
			} else if (0 != h2 && h2 < h1) {
				g.drawString(this.sdf2.format(this.cal 
					.getTime()), (int) (tmpDelta * (counter + 1)) + MARGIN - 27, h - 17);
			}
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
			g.drawLine((int) (tmpDelta * counter) + MARGIN, h, (int) (tmpDelta * counter) + MARGIN, h - 3);
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

	void setDate(	Date startDate,
					int scale) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setScale(scale);
		setStartDate(startDate);
		final SchedulerModel model = (SchedulerModel) this.aContext.getApplicationModel();
		try {
			model.updateTests(this.scaleStart.getTime(), this.scaleEnd.getTime());
			updateTestLines();
		} catch (final ApplicationException e) {
			e.printStackTrace();
			AbstractMainFrame.showErrorMessage(LangModelSchedule.getString("Error.CannotRefreshTests"));
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		this.cal.setTimeInMillis(0);
		this.cal.add(STEPS[this.actualScale].scale, STEPS[this.actualScale].one);
		long diff = this.cal.getTimeInMillis();
		double delta = (getWidth() - 2 * PlanPanel.MARGIN)
				/ ((double) (this.scaleEnd.getTime() - this.scaleStart.getTime()) / (double) diff);
		double subDelta = delta / STEPS[this.actualScale].subscales;

		paintScales(g, diff, delta, subDelta);
		paintScaleDigits(g, diff, delta, subDelta);

		if (this.currentPosition != null && this.startPosition != null) {
			if (this.selectionColor == null) {
				Color color = UIManager.getColor("List.selectionBackground");
				this.selectionColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
			}
			Color color = g.getColor();
			g.setColor(this.selectionColor);
			int x;
			int w;
			if (this.currentPosition.x > this.startPosition.x) {
				x = this.startPosition.x;
			    w = this.currentPosition.x - this.startPosition.x;
			} else {
				x = this.currentPosition.x;
			    w = this.startPosition.x - this.currentPosition.x;
			}
			
			g.fillRect(x, 0, w, this.getHeight());
			g.setColor(color);
		}
	}

	void updateTestLinesTimeRegion() {
		long scaleStartTime = this.scaleStart.getTime();
		long scaleEndTime = this.scaleEnd.getTime();

		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = PlanPanel.this.getComponent(i);
			if (component instanceof TimeLine) {
				TimeLine timeLine = (TimeLine) component;
				timeLine.setStart(scaleStartTime);
				timeLine.setEnd(scaleEndTime);
			}
		}
		this.updateRealScale();
	}

	void updateScale(double k) {
		updateScale(k, (int) (this.parent.getViewport().getViewPosition().x + this.parent.getVisibleRect().width
				* (k * 0.5 - 0.5) - MARGIN));
		// BoundedRangeModel model =
		// this.parent.getHorizontalScrollBar().getModel();
		updateRealScale();
		this.parent.revalidate();
	}

	void updateScale(	double k,
						int view_x) {
		if (getSize().width / this.parent.getVisibleRect().width >= MAX_ZOOM - 1 && k > 1) {
			this.parent.repaint();
			return;
		}

		if (getSize().width * k > MAX_ZOOM * this.parent.getVisibleRect().width)
			k = (double) (MAX_ZOOM * this.parent.getVisibleRect().width) / (double) getSize().width;

		setPreferredSize(new Dimension((int) ((getSize().width - 2 * MARGIN) * k), getPreferredSize().height));
		setSize(new Dimension((int) ((getSize().width - 2 * MARGIN) * k), Math.max(getSize().height,
			getPreferredSize().height)));
		/*
		 * for (Iterator it = testLines.values().iterator(); it.hasNext();) {
		 * TestLine testLine = (TestLine)it.next();
		 * testLine.setPreferredSize(new Dimension(getPreferredSize().width,
		 * 20)); }
		 */
		Point p = new Point((int) ((view_x - MARGIN) * k), view_x);
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
			double delta = (getWidth() - 2 * MARGIN)
					/ ((double) (this.scaleEnd.getTime() - this.scaleStart.getTime()) / (double) diff);

			// double sub_delta = delta / STEPS[actualScale].subscales;
			if (delta >= 0 && delta < 35 && this.actualScale < STEPS.length - 1) {
				this.actualScale++;
			}

			this.sdf.applyPattern(SCALE_PATTERNS[this.actualScale]);
			if (this.actualScale == 5) {
				STEPS[5].total = this.cal.getActualMaximum(STEPS[5].scale);
			}
			
			this.cal.setTime(this.startDate);
			if (this.actualScale > 3) {
				this.cal.set(Calendar.HOUR_OF_DAY, 0);
			} 
			if (this.actualScale > 2) {
				this.cal.set(Calendar.MINUTE, 0);
			}
			this.cal.set(Calendar.SECOND, 0);
		}
		
		for (int i = 0; i < this.getComponentCount(); i++) {
			Component component = PlanPanel.this.getComponent(i);
			if (component instanceof TimeLine) {
				TimeLine timeLine = (TimeLine) component;
				timeLine.updateScale();
//				timeLine.refreshTimeItems();
			}
		}
		this.revalidate();
		this.repaint();
	}

	private void updateTests() {
		this.updateTestLines();
		super.repaint();
		super.revalidate();
		this.parent.repaint();
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.updateTests();
			line.refreshTimeItems();
		}
	}

	private void updateTest() throws ApplicationException {
		Test selectedTest = this.schedulerModel.getSelectedTest();
		if (selectedTest != null) {
			final Date startTime = selectedTest.getStartTime();
//			System.out.println("PlanPanel.updateTest() | startTime " + startTime);
//			System.out.println("PlanPanel.updateTest() | this.scaleStart " + this.scaleStart);
//			System.out.println("PlanPanel.updateTest() | this.scaleEnd " + this.scaleEnd);
			
			// if selected test is not in visible range			
			if (!(this.scaleStart.before(startTime) && startTime.before(this.scaleEnd))) {
				final Date time = new Date(startTime.getTime() - 30L * 60L * 1000);
				this.toolBar.dateSpinner.setValue(time);
				this.toolBar.timeSpinner.setValue(time);
				
//				System.out.println("PlanPanel.updateTest() || set " + startTime);
				
				for (final Identifier monitoredElementId : this.testLines.keySet()) {
					final TestLine line = this.testLines.get(monitoredElementId);
					line.refreshTimeItems();
				}

			}
		}

		
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.updateTest();
			final Rectangle visibleRectangle = line.getVisibleRectangle();
//			System.out.println("PlanPanel.updateTest() | " + visibleRectangle);
			if (visibleRectangle != null) {
				this.scrollRectToVisible(visibleRectangle);
			}
		}
	}

	protected void updateTestLines() {
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(((SchedulerModel) this.aContext.getApplicationModel()).getTestIds(), true);
			for (final Test test : tests) {
				final MonitoredElement monitoredElement = test.getMonitoredElement();
				final Identifier monitoredElementId = monitoredElement.getId();
				if (!this.testLines.containsKey(monitoredElementId)) {
					final TestLine testLine = new TestLine(this.aContext, monitoredElement.getName(), monitoredElementId);
					// testLine.setTestTemporalStamps((TestTemporalStamps)
					// this.testTemporalLines.get(monitoredElement));
					this.testLines.put(monitoredElementId, testLine);
					testLine.setPreferredSize(new Dimension(this.getWidth(), 25));
				}
			}

			this.removeAll();
			for (final Identifier monitoredElementId : this.testLines.keySet()) {
				final TestLine testLine = this.testLines.get(monitoredElementId);
				this.add(testLine);
			}

			super.setPreferredSize(new Dimension(getPreferredSize().width, 30 + 25 * this.testLines.values().size()));
			this.updateTestLinesTimeRegion();
		} catch (final ApplicationException e) {
			
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
				LangModelGeneral.getString("Error.CannotAcquireObject"),
				LangModelGeneral.getString("Error"),
				JOptionPane.OK_OPTION);
			return;
		}
		
		this.revalidate();
	}

}
