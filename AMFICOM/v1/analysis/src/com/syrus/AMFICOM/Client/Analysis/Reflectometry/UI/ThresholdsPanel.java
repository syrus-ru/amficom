package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;

public class ThresholdsPanel extends ReflectogramEventsPanel
{
	//public boolean paint_all_thresholds = false;
	public boolean paint_thresholds = true;

	protected boolean edit_thresholds = true;

	protected ModelTraceManager et_mtm;

	protected int c_event = 0;
	protected ModelTraceManager.ThresholdHandle c_TH = null;
	protected boolean isRbutton = false;

	public ThresholdsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double y[], double deltaX)
	{
		super (panel, dispatcher, y, deltaX);		
	}

	public void updateThresholds(ModelTraceManager mtm)
	{
//		updateTrace(mtm); // XXX: на что влияет? нужно ли?

		if (mtm == null)
			return;

		this.et_mtm = mtm;

		if (c_event >= mtm.getNEvents())
			c_event = mtm.getNEvents() - 1;
	}

	public void showEvent (int num)
	{
		if (events == null)
			return;
		if (num == -1)
			return;

		if (et_mtm != null && c_event >= et_mtm.getNEvents())
			c_event = et_mtm.getNEvents() - 1;
		else
			c_event = num;

		start = events[num].first_point;
		end = events[num].last_point;
	}

	protected void this_mousePressed(MouseEvent mev)
	{
		if (!edit_thresholds || et_mtm == null)
		{
			super.this_mousePressed(mev);
			return;
		}

		startpos = mev.getPoint();
		currpos = mev.getPoint();
		if (SwingUtilities.isRightMouseButton(mev))
			isRbutton = true;
		else
			isRbutton = false;

		/* TODO */
		SimpleReflectogramEvent simpleEvent = this.et_mtm.getSimpleEvent(this.c_event);
		
		boolean allThresholds = this.isToPaintAllThresholds();
		int currposF = (int) coord2indexF(this.currpos.x);
		int mouseCouplingF = (int)coord2indexF(MOUSE_COUPLING);
		if (!allThresholds && !(simpleEvent.getBegin() - mouseCouplingF <= currposF && currposF <= simpleEvent.getEnd() + mouseCouplingF))
			return;
		// если это текущее событие - пытаемся "ухватить" (drag) порог
		this.c_TH = this.et_mtm.getThresholdHandle(
			coord2indexF(this.currpos.x), // we need float value, without rounding
			coord2value(this.currpos.y),
			MOUSE_COUPLING / this.scaleX,
			MOUSE_COUPLING / this.scaleY,
			0.5,
			isRbutton ? 1 : 0);		
		
		if (this.c_TH != null && (allThresholds || this.c_TH.isRelevantToNEvent(this.c_event))) {

			// перемещаем мышь в точку захвата
			try
			{
				Robot r = new Robot();
				Point p = getLocationOnScreen();
				int x = index2coord(c_TH.getX());
				int y = value2coord(c_TH.getY());

				// не допускаем выхода на пределы окна
				currpos = new Point(x, y);
				limit_currpos();

				// перемещаем курсор
				r.mouseMove(currpos.x + p.x, currpos.y + p.y);
			}
			catch (AWTException ex)
			{
				System.out.println("ThresholdsPanel: Warning: MouseMove failed");
				ex.printStackTrace();
			}

			return;
		}

		super.this_mousePressed(mev);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (!edit_thresholds || et_mtm == null)
		{
			super.this_mouseDragged(e);
			return;
		}
		if (c_TH != null)
		{
			upd_currpos(e); // теперь tmppos - предыдущее положение, а  currpos - новое
			c_TH.moveBy(
				(currpos.x - tmppos.x) / getTrueScaleX(),
				(currpos.y - tmppos.y) / getTrueScaleY());

			parent.repaint();

			dispatcher.notify(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
		}
		else
			// иначе выделяем область квадратиком
			super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (!edit_thresholds || et_mtm == null || c_TH == null)
		{
			super.this_mouseReleased(e);
		}
		else
		{
			if (c_TH != null)
			{
				c_TH.release();
				parent.repaint();
				dispatcher.notify(new RefUpdateEvent(this,
					RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
				c_TH = null;
			}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	protected void this_mouseClicked(MouseEvent e)
	{
	    // если кликнули, но не на текущее событие, переходим к новому событию
	    int pos = coord2index(e.getPoint().x);
	    int evId = et_mtm != null ? et_mtm.getEventByCoord(pos) : -1;
		if (evId != -1 && evId != c_event)
		{
		    dispatcher.notify(new RefUpdateEvent(
		    	String.valueOf(evId), RefUpdateEvent.EVENT_SELECTED_EVENT
		    ));
		    return;
		}
	}

	public void paint (Graphics g)
	{
		long t0 = System.currentTimeMillis();
		paint_scales(g);
		long t1 = System.currentTimeMillis();

		if (draw_events)
		{
			paint_reflectogram_events(g);
		}
		else
		{
			paint_trace(g);
		}
		long t2 = System.currentTimeMillis();
		if (draw_modeled)
		{
			paint_modeled_trace(g);
		}
		long t3 = System.currentTimeMillis();

		if (draw_alarms)
			paint_alarms(g);
		long t4 = System.currentTimeMillis();

		if (draw_min_trace_level && draw_events)
		{
			paint_noise_level(g);
			paint_min_trace_level(g);
		}
		else if (draw_noise_level && draw_events)
			paint_noise_level(g);
		long t5 = System.currentTimeMillis();

		paint_scale_digits(g);
		long t6 = System.currentTimeMillis();

		if (paint_thresholds)
		{
			if(isToPaintAllThresholds())
				paint_all_thresholds(g);
			else
				paint_threshold(g);
		}
		long t7 = System.currentTimeMillis();  // XXX: remove t0 - t7
//		System.err.println("paint:"
//			+ " ta=" + (t1 - t0)
//			+ " tb=" + (t2 - t1)
//			+ " tc=" + (t3 - t2)
//			+ " td=" + (t4 - t3)
//			+ " te=" + (t5 - t4)
//			+ " tf=" + (t6 - t5)
//			+ " tg=" + (t7 - t6)
//		);
	}

	// nEvent < 0 => paint all thresholds
	private void paintThresholdsEx(Graphics g, int nEvent)
	{
		if (et_mtm == null)
			return;

		SimpleReflectogramEvent sre = nEvent >= 0
				? et_mtm.getSimpleEvent(nEvent)
				: null;

		for (int key = 0; key < 4; key++)
		{
			g.setColor(UIManager.getColor(Thresh.IS_KEY_HARD[key]
			     ? AnalysisResourceKeys.COLOR_ALARM_THRESHOLD
			     : AnalysisResourceKeys.COLOR_WARNING_THRESHOLD));
			// XXX: нет draw_joint_of_two_model_curves
			drawModelCurve(g, et_mtm.getThresholdMT(key), sre);
		}
	}

	private void paint_threshold(Graphics g)
	{
		paintThresholdsEx(g, c_event);
	}
	private void paint_all_thresholds(Graphics g)
	{
//		if (et_mtm == null)
//			return;
//		int nEvents = et_mtm.getNEvents();
//		for (int i = 0; i < nEvents; i++)
//			paintThresholdsEx(g, i);
		paintThresholdsEx(g, -1);
	}

	/*
	void paint_all_thresholds(Graphics g)
	{
		if(et_ep == null)
			return;

		ReflectogramEvent []up1   = ReflectogramMath.getThreshold(et_ep, 0);
		ReflectogramEvent []up2   = ReflectogramMath.getThreshold(et_ep, 1);
		ReflectogramEvent []down1 = ReflectogramMath.getThreshold(et_ep, 2);
		ReflectogramEvent []down2 = ReflectogramMath.getThreshold(et_ep, 3);
		
		ReflectogramEvent[][] p_curves = { up1, down1, up2, down2 };
		Color[] p_colors = {warningThresholdColor, warningThresholdColor, alarmThresholdColor, alarmThresholdColor };
		
		for (int key = 0; key < 4; key++)
		{
			g.setColor(p_colors[key]);
			for(int j=0; j<et_ep.length; j++)
			{
				((Graphics2D) g).setStroke(DASHED_STROKE);
				if (j != 0)
					draw_joint_of_two_model_curves(g, p_curves[key][j-1], p_curves[key][j]);
				((Graphics2D) g).setStroke(j == c_event ? DEFAULT_STROKE : DASHED_STROKE);
				draw_one_model_curve(g, p_curves[key][j]);
			}
		}
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	void paint_threshold (Graphics g)
	{
		if (et_ep == null)
			return;

		ReflectogramEvent up1   = et_ep[c_event].getThresholdReflectogramEvent(0);
		ReflectogramEvent up2   = et_ep[c_event].getThresholdReflectogramEvent(1);
		ReflectogramEvent down1 = et_ep[c_event].getThresholdReflectogramEvent(2);
		ReflectogramEvent down2 = et_ep[c_event].getThresholdReflectogramEvent(3);

		g.setColor(warningThresholdColor);
		draw_one_model_curve(g, up1);
		draw_one_model_curve(g, down1);
		g.setColor(alarmThresholdColor);
		draw_one_model_curve(g, up2);
		draw_one_model_curve(g, down2);
	}
	*/

	/*
	private double getShift(ReflectogramEvent []etalon, double []data)
	{
		if(data == null || etalon == null || etalon.length<1)
			return 0.;

		double maxEtalon = -1000.;
		double maxData   = -1000.;

		for(int i=etalon[0].getBegin(); i<=etalon[0].getEnd() && i<data.length; i++)
		{
			if(maxEtalon<etalon[0].refAmplitude(i))
				maxEtalon=etalon[0].refAmplitude(i);
			if(maxData<data[i])
				maxData = data[i];
		}

		return (maxData - maxEtalon);
	}
	*/

	private boolean isToPaintAllThresholds()
	{
		return parent instanceof ThresholdsLayeredPanel
			&& ((ThresholdsLayeredPanel )parent).hasShowThresholdButtonSelected();
	}
}
