package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;

public class ReflectogramEventsPanel extends TraceEventsPanel
{
	protected Dispatcher dispatcher;

	public boolean draw_modeled = false;
	public boolean draw_alarms = false;
	public boolean draw_min_trace_level = false;
	public boolean draw_noise_level = false;

	//protected ReflectogramEvent[] ep;
	protected ModelTraceManager mtm; // используется только в методах, вызываемых из paint()
	protected ReflectogramAlarm[] alarms;

	protected Double min_trace_level;
	protected double noise_level = 28; // ???!
	protected boolean moving_level = false;

	//protected double[] modeled_y;

	protected Color modeledColor;
	protected Color minLevelColor;

	public ReflectogramEventsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double[] y, double deltaX)
	{
		super (panel, y, deltaX);
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void updateTrace (ModelTraceManager mtm) // was: updateEvents
	{
		this.mtm = mtm;
	}

	public void updateAlarms (ReflectogramAlarm[] alarms)
	{
		this.alarms = alarms;
		if (alarms != null)
		{
			draw_alarms = true;
		}
		else
			draw_alarms = false;
	}

	public void updateMinTraceLevel(Double value)
	{
		min_trace_level = value;
		Pool.put("min_trace_level", "primarytrace", min_trace_level);
	}

	public void updateNoiseLevel()
	{
		RefAnalysis ana = (RefAnalysis)Pool.get("refanalysis", "primarytrace");
		if (ana != null)
		{
			TraceEvent ev = ana.overallStats;
			if (ev != null)
				noise_level = ev.data[2];
		}

		min_trace_level = (Double)Pool.get("min_trace_level", "primarytrace");
		if (min_trace_level == null)
		{
			min_trace_level = new Double(noise_level - 3);
			Pool.put("min_trace_level", "primarytrace", min_trace_level);
		}
	}

	protected void updColorModel()
	{
		super.updColorModel();

		modeledColor = ColorManager.getColor("modeledColor");
		minLevelColor = ColorManager.getColor("minTraceLevelColor");
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (draw_min_trace_level)
		{
			if (coord2index(currpos.x) > y.length)
				return;

			if(Math.abs(currpos.y-(int)((min_trace_level.doubleValue() - top) * scaleY - 1)) < mouse_coupling)
			{
				moving_level = true;
				setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
				return;
			}
		}
		super.this_mousePressed(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (moving_level)
		{
			moving_level = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			updateMinTraceLevel(min_trace_level);
			return;
		}
		super.this_mouseReleased(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (moving_level)
		{
			upd_currpos(e);

			double pos = min_trace_level.doubleValue();
			pos += (currpos.y - tmppos.y)/scaleY;
			min_trace_level = new Double(pos);
			parent.repaint();
			dispatcher.notify(new RefUpdateEvent(min_trace_level, RefUpdateEvent.MIN_TRACE_LEVEL_CHANGED_EVENT));
			return;
		}
		super.this_mouseDragged(e);
	}

	public void paint(Graphics g)
	{
		paint_scales(g);

		if (draw_events)
		{
			paint_reflectogram_events(g);
		}
		else
		{
			paint_trace(g);
		}
		if (draw_modeled)
		{
			paint_modeled_trace(g);
		}

		if (draw_alarms)
			paint_alarms(g);

		if (draw_min_trace_level && draw_events)
		{
			paint_noise_level(g);
			paint_min_trace_level(g);
		}
		else if (draw_noise_level && draw_events)
			paint_noise_level(g);

		paint_scale_digits(g);
	}

	protected void paint_noise_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(scaleColor.darker());
		int h = (int)((noise_level - top) * scaleY - 1);
		g.drawLine(0, h, jw, h);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString("Уровень шумов", jw - 87, h - 1);
	}

	protected void paint_min_trace_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(minLevelColor);
		g.drawLine(0, (int)((min_trace_level.doubleValue() - top) * scaleY - 1),
							 jw, (int)((min_trace_level.doubleValue() - top) * scaleY - 1));
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}
	
	protected void paint_alarms(Graphics g)
	{
		if (alarms == null)
			return;

		g.setColor(Color.red);
		for (int j = 0; j < alarms.length; j++)
		{
			if ((alarms[j].alarmPointCoord < end) && (alarms[j].alarmEndPointCoord > start))
				for (int i = Math.max(0, alarms[j].alarmPointCoord - start); i < Math.min (end, alarms[j].alarmEndPointCoord) - start; i++)
				{
					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY - 1),
					(int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY - 1));
				}
		}
	}

	/**
	 * Draws model curve using current graphigs plotting settings.
	 * May be used for drawing both model and threshold curve 
	 * @param g graphics to plot
	 * @param mt trace to plot
	 * @param sre not null: range to plot coded as SimpleReflectogramEvent; null: plot whole trace 
	 */
	protected void drawModelCurve(Graphics g, ModelTrace mt, SimpleReflectogramEvent sre)
	{
		int n1 = sre == null ? 0 : sre.getBegin();
		int n2 = sre == null ? mt.getLength() - 1 : sre.getEnd();
		if ((n1 <= end) && (n2 >= start))
		{
		    int iFrom = Math.max(0, n1 - start);
		    int iTo = Math.min(end, n2) - start;
		    if (iTo - iFrom >= 0)
		    {
		        double[] vArr = mt.getYArrayZeroPad(iFrom + start, iTo - iFrom + 1);
		        draw_y_curve(g, vArr, 0, iFrom, iTo - iFrom);
		    }
		}
	}
	/*
	protected void draw_joint_of_two_model_curves(Graphics g, ReflectogramEvent reL, ReflectogramEvent reR)
	{
		int i = reR.getBegin() - start;
		if (i == reL.getEnd() - start && i <= end - start && i >= 0)
		{
			g.drawLine(
					(int )(i*scaleX+1), (int )((maxY - reL.refAmplitude(i+start) - top)*scaleY),
					(int )(i*scaleX+1), (int )((maxY - reR.refAmplitude(i+start) - top)*scaleY));
		}
	}
	*/
	protected void paint_modeled_trace(Graphics g)
	{
		if (mtm == null)
			return;
		g.setColor(modeledColor);
		drawModelCurve(g, mtm.getModelTrace(), null);
	}

	protected void paint_reflectogram_events(Graphics g)
	{
		if (mtm == null)
		{
			paint_events(g); // метод суперкласса....
			return;
		}
		SimpleReflectogramEvent []ep = mtm.getSimpleEvents();
		for(int j=0; j<ep.length; j++)
		{
			if ((ep[j].getBegin() < end) && (ep[j].getEnd() > start))
			{
				int type = ep[j].getEventType();
				Color color;
				switch (type)
				{
					case ReflectogramEvent.LINEAR:
					    color = linezoneColor;
						break;
					case ReflectogramEvent.CONNECTOR:
						if (j == 0)
						    color = deadzoneColor;
						else if (j == ep.length - 1)
						    color = endColor;
						else
						    color = connectColor;
						break;
					case ReflectogramEvent.WELD:
					    color = weldColor;
						break;
					default:
					    color = noiseColor;
				}
				g.setColor(correctColor(color));

				int iFrom = Math.max(start, ep[j].getBegin()) - start;
				int iTo = Math.min (end, ep[j].getEnd()) - start;
				draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom);
//				for (int i = iFrom; i < iFrom; i++)
//				{
//					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
//					        (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
//				}
			}
		}

		g.setColor(correctColor(noiseColor));
		int iFrom = Math.max(0, ep[ep.length-1].getEnd() - start);
		int iTo = Math.min (end, y.length - start - 1);
		draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom);
//		if (ep[ep.length-1].getEnd() < end)
//			for (int i = iFrom; i < iTo; i++)
//				g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
//					(int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
	}
}
