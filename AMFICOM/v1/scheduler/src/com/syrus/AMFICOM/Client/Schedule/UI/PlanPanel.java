/*-
 * $Id: PlanPanel.java,v 1.72 2006/01/27 14:44:49 bob Exp $
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
import java.awt.FontMetrics;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Schedule.SchedulerModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.MonitoredElement;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestView;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;

/**
 * @version $Revision: 1.72 $, $Date: 2006/01/27 14:44:49 $
 * @author $Author: bob $
 * @module scheduler
 */
final class PlanPanel extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long	serialVersionUID	= 3258417243925984817L;

	public static final int	TIME_OUT	= 500;

	private Timer			timer		= new Timer(TIME_OUT, this);
	
	@Shitlet
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
			"Scheduler.Text.Plan.Toolbar.Scope.10Minutes", 
			"Scheduler.Text.Plan.Toolbar.Scope.1Hour", 
			"Scheduler.Text.Plan.Toolbar.Scope.6Hours", 
			"Scheduler.Text.Plan.Toolbar.Scope.1Day", 
			"Scheduler.Text.Plan.Toolbar.Scope.1Week", 
			"Scheduler.Text.Plan.Toolbar.Scope.1Month"};

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

	@Shitlet
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

	private Comparator<TestLine>	comparator;
	
	public PlanPanel(JScrollPane parent, ApplicationContext aContext) {
		this.aContext = aContext;
		
		this.schedulerModel = (SchedulerModel) aContext.getApplicationModel();
		
		this.sdf2 = new SimpleDateFormat("dd.MM.yyyy");
		
		this.parent = parent;
		this.toolBar = new PlanToolBar(aContext, this);
		this.dispatcher = aContext.getDispatcher();		
		
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TESTS, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REFRESH_TEST, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_ADD_TEST, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_REMOVE_TEST, this);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CLEAN, this);
		//
		this.addComponentListener(this.createComponentListener());
		this.addMouseListener(this.createMouseListener());
		this.addMouseMotionListener(this.createMouseMotionListener());

		this.createComparator();
		
		this.timer.start();

		this.setToolTipText("");
		this.setStartDate(new Date(System.currentTimeMillis()));
		this.updateRealScale();
		this.setBackground(UIManager.getColor(ResourceKeys.COLOR_GRAPHICS_BACKGROUND));
	}

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
	
	private final MouseListener createMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mousePressed(final MouseEvent e) {
				this.mouseClicked(e);
			}
			
			@Override
			public void mouseClicked(final MouseEvent e) {
				PlanPanel.this.schedulerModel.unselectTests(this);
				
				PlanPanel.this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, 
					SchedulerModel.COMMAND_DATE_OPERATION, 
					null, 
					PlanPanel.this.getDateByMousePosition(e)));
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				PlanPanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

				if (PlanPanel.this.startPosition != null && PlanPanel.this.currentPosition != null
						&& Math.abs(PlanPanel.this.startPosition.x - PlanPanel.this.currentPosition.x) > 5) {
					final double k = (PlanPanel.this.parent.getVisibleRect().width - 2 * MARGIN)
							/ Math.abs((double) (PlanPanel.this.startPosition.x - PlanPanel.this.currentPosition.x));
					PlanPanel.this.updateScale(k, Math.min(PlanPanel.this.startPosition.x, PlanPanel.this.currentPosition.x));
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
	
	private void createComparator() {
		this.comparator = new Comparator<TestLine>() {
			public int compare(	final TestLine o1,
			                   	final TestLine o2) {
				return o1.getTitle().compareTo(o2.getTitle());
			}
		};
	}
	
	private void add(final TestLine testLine) {
		int index = 0;
		for(int i = 0 ; i < this.getComponentCount(); i++) {
			final Component component = this.getComponent(i);
			if (component instanceof TestLine) {
				final TestLine otherTestLine = (TestLine) component;
				final int compare = this.comparator.compare(testLine, otherTestLine);
				index = i;
				if (compare <= 0) {					
					break;
				}
				index++;
			}
		}
		this.add(testLine, index);
	}
	
	final Date getDateByMousePosition(final MouseEvent e) {
		final long start = PlanPanel.this.scaleStart.getTime();
		final long end = PlanPanel.this.scaleEnd.getTime();
		
		return new Date((start + ((e.getX() - PlanPanel.MARGIN / 2) *  (end - start) / ((PlanPanel.this.getWidth() - PlanPanel.MARGIN)))));
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

	public void setStartDate(final Date start) {
		this.startDate = start;
		if (start != null) {
			this.cal.setTime(start);
//			if (this.scale > 1) {
//				this.cal.set(Calendar.MINUTE, 0);
//			}
			this.cal.set(Calendar.SECOND, 0);

//			// округляем до шага
//			int num = this.cal.get(STEPS[this.scale].scale);
//			while (num / STEPS[this.scale].align * STEPS[this.scale].align != num) {
//				this.cal.add(STEPS[this.scale].scale, -1);
//				num = this.cal.get(STEPS[this.scale].scale);
//			}

			this.scaleStart = this.cal.getTime();
			// scroll calendar to end of period
			this.cal.add(STEPS[this.scale].scale, STEPS[this.scale].total);
			this.scaleEnd = this.cal.getTime();
			// scroll calendar to start point
			this.cal.setTime(this.startDate);
			
			try {
				this.schedulerModel.updateTestIds(this.startDate, this.scaleEnd);
			} catch (final ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public String getToolTipText(final MouseEvent event) {
		final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
		return sdf.format(this.getDateByMousePosition(event))
//			+ ", (" + event.getX() + ", " + event.getY()+")"
			;
	}

	public void actionPerformed(final ActionEvent e) {
		// self repainting better neither repaint each item
		this.revalidate();
		this.repaint();
	}
	
	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName().intern();
		if (propertyName == SchedulerModel.COMMAND_REFRESH_TESTS) {
			this.updateTests((Set<Identifier>)evt.getNewValue());
		} else if (propertyName == SchedulerModel.COMMAND_REFRESH_TEST) {
			if (!this.testLines.containsValue(evt.getSource())) {
				try {
					this.updateTest();
				} catch (final ApplicationException e) {
					e.printStackTrace();
					AbstractMainFrame.showErrorMessage(e.getMessage());
				}
			}
		} else if (propertyName == SchedulerModel.COMMAND_ADD_TEST) {
			this.addTest((Set<Identifier>)evt.getNewValue());
		} else if (propertyName == SchedulerModel.COMMAND_REMOVE_TEST) {
			this.updateTests((Set<Identifier>)evt.getNewValue());
		} else if (propertyName == SchedulerModel.COMMAND_CLEAN) {
			this.clearTests();
		}
	}
	
	void setDate(	Date startDate,
					int scale) {
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		setScale(scale);
		setStartDate(startDate);
		final SchedulerModel model = (SchedulerModel) this.aContext.getApplicationModel();
		try {
			model.updateTests(this.scaleStart, this.scaleEnd);
			updateTestLines();
		} catch (final ApplicationException e) {
			e.printStackTrace();
			AbstractMainFrame.showErrorMessage(I18N.getString("Scheduler.Error.CannotRefreshTests"));
		}

		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private final void paintScale(final Graphics g,
	                                final long diff,
	                                final double subDelta) {
		final int h = super.getHeight() - 1;
		final int w = super.getWidth();

		long tmpDiff = diff;
		if (subDelta > 60) {
			tmpDiff = diff / STEPS[this.actualScale].subscales;
		} 

		final FontMetrics fontMetrics = g.getFontMetrics();
		final int shift = fontMetrics.stringWidth(this.sdf.format(this.cal.getTime())) / 2;
		
		
		g.setColor(Color.BLACK);
		
		final long start = PlanPanel.this.scaleStart.getTime();
		final long end = PlanPanel.this.scaleEnd.getTime();
		
		this.cal.setTimeInMillis(start);
//		this.cal.set(Calendar.HOUR_OF_DAY, 0);
//		this.cal.set(Calendar.MINUTE, 0);

		long start2 = this.cal.getTimeInMillis(); 
		
		final long timeRegion = end - start;
		
		if (timeRegion == 0) {
			System.err.println("PlanPanel.paintScale | timeRegion is 0");
			return;
		}
		
		final Color edgeColor = UIManager.getColor(UIStorage.EDGE_COLOR);
		for (long calendarTime = start2; calendarTime <= end;) {			
			final int currentX = (int)((w - MARGIN) * (calendarTime - start)/timeRegion) + MARGIN/2;
			g.setColor(edgeColor);			
			g.drawLine(currentX, 0, currentX, h);
			
			g.setColor(Color.BLACK);
			g.drawLine(currentX, h, currentX, h - 5);

			g.drawString(this.sdf.format(this.cal.getTime()), currentX - shift, h - 7);
			
			if (subDelta > 10) {
				int d2 = (int)(w/6 * tmpDiff/timeRegion);
				int currentX2 = currentX;
				for(int i = 1; i < 6; i++) {
					currentX2 += d2;
					g.drawLine(currentX2, h, currentX2, h - 3);
				}
			}			
			
			int hour = this.cal.get(Calendar.HOUR_OF_DAY);
			calendarTime += tmpDiff;
			this.cal.setTimeInMillis(calendarTime);
			int hour1 = this.cal.get(Calendar.HOUR_OF_DAY);			
			
			if (hour1 < hour) {
				this.cal.set(Calendar.HOUR_OF_DAY, 0);
				this.cal.set(Calendar.MINUTE, 0);
				final String dayString = this.sdf2.format(this.cal.getTime());
				g.drawString(dayString, 
						(int)(w * (this.cal.getTimeInMillis() - start)/timeRegion) 
						- fontMetrics.stringWidth(dayString)/2, 
					h - 17);
				this.cal.setTimeInMillis(calendarTime);
			}
		}
		this.cal.setTime(this.scaleStart);
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);

		// ЛЗ/БН чПФ ЬФПФ ВЩ ЙУЮП ЛХУПЮЕЗ ЗПЖОБ РЙТЙРЙУГБФШ ...
		this.cal.setTimeInMillis(0);
		this.cal.add(STEPS[this.actualScale].scale, STEPS[this.actualScale].one);
		long diff = this.cal.getTimeInMillis();
		double delta = (getWidth() - 2 * PlanPanel.MARGIN)
				/ ((double) (this.scaleEnd.getTime() - this.scaleStart.getTime()) / (double) diff);
		double subDelta = delta / STEPS[this.actualScale].subscales;

		// ЛЗ/БН Й ЮП ЬФБ ЪБ diff Й subDelta - ЮЕУФОБ УЛПЦХ - ОЙЙВХ
		this.paintScale(g, diff, subDelta);

		if (this.currentPosition != null && this.startPosition != null) {
			if (this.selectionColor == null) {
				final Color color = UIManager.getColor("List.selectionBackground");
				this.selectionColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);
			}
			final Color color = g.getColor();
			g.setColor(this.selectionColor);
			final int x;
			final int w;
			if (this.currentPosition.x > this.startPosition.x) {
				x = this.startPosition.x;
			    w = this.currentPosition.x - this.startPosition.x;
			} else {
				x = this.currentPosition.x;
			    w = this.startPosition.x - this.currentPosition.x;
			}
			
			g.fillRect(x, 0, w, super.getHeight());
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
//			if (this.actualScale > 3) {
//				this.cal.set(Calendar.HOUR_OF_DAY, 0);
//			} 
//			if (this.actualScale > 2) {
				this.cal.set(Calendar.MINUTE, 0);
//			}
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

	private void clearTests() {
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.clearTests();
			line.refreshTimeItems();
		}		
	}
	
	private void updateTests(final Set<Identifier> testIds) {
		this.updateTestLines();
		super.repaint();
		super.revalidate();
		this.parent.repaint();
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.updateTests(testIds);
			if (testIds != null && !testIds.isEmpty()) {
				line.refreshTimeItems();
			}
		}
	}

	private void updateTest() throws ApplicationException {
		final Test selectedTest = this.schedulerModel.getSelectedTest();
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.updateTest();			
		}
		
		if (selectedTest != null) {
			final TestLine line = this.testLines.get(selectedTest.getMonitoredElementId());
			final Rectangle visibleRectangle = line.getVisibleRectangle();
			if (visibleRectangle != null) {
				this.scrollRectToVisible(visibleRectangle);
			}
		}
	}
	
	private void addTest(final Set<Identifier> testIds) {
		for (final Identifier monitoredElementId : this.testLines.keySet()) {
			final TestLine line = this.testLines.get(monitoredElementId);
			line.addTest(testIds);
			final Rectangle visibleRectangle = line.getVisibleRectangle();
			if (visibleRectangle != null) {
				this.scrollRectToVisible(visibleRectangle);
			}
		}
	}

	void updateVisibleRectOfTestLines() {
		for(int i = 0 ; i < this.getComponentCount(); i++) {
			final Component component = this.getComponent(i);
			if (component instanceof TimeLine) {
				final TimeLine timeLine = (TimeLine) component;
				timeLine.updateCachedVisibleRect();
			}
		}
	}
	
	protected void updateTestLines() {
		final Set<Identifier> testIds = ((SchedulerModel) this.aContext.getApplicationModel()).getTestIds();
		for (final Identifier testId : testIds) {
			final Test test = TestView.valueOf(testId).getTest();
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
			testLine.updateTest();
		}

		super.setPreferredSize(new Dimension(getPreferredSize().width, 30 + 25 * this.testLines.values().size()));
		this.updateTestLinesTimeRegion();
		
		this.revalidate();
	}

}
