package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.dadara.*;

public class ReflectogramEventsPanel extends TraceEventsPanel
{
	public Dispatcher dispatcher;

	public boolean draw_modeled = false;
	public boolean draw_alarms = false;
	public boolean draw_min_trace_level = false;
	public boolean draw_noise_level = false;

	protected ReflectogramEvent[] ep;
	protected ReflectogramAlarm[] alarms;

	protected Double min_trace_level;
	protected double noise_level = 28;
	protected boolean moving_level = false;

	protected double[] modeled_y;

	protected Color modeledColor;
	protected Color minLevelColor;

	public ReflectogramEventsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double[] y, double delta_x)
	{
		super (panel, y, delta_x);
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher)
	{
		this.dispatcher = dispatcher;
	}

	public void updateEvents (ReflectogramEvent[] ep)
	{
		this.ep = ep;

		if (ep != null)
		{
			int n = ep[ep.length-1].end+2;
			modeled_y = new double[n];
			for (int i = 0; i < ep.length; i++)
			{
				for (int j = ep[i].begin; j <= ep[i].end && j < n; j++)
					modeled_y[j] = ep[i].refAmpl(j)[0];
			}
		}
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

			if(Math.abs(currpos.y-(int)((min_trace_level.doubleValue() - top) * scale_y - 1)) < mouse_coupling)
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
			pos += (currpos.y - tmppos.y)/scale_y;
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
		int h = (int)((noise_level - top) * scale_y - 1);
		g.drawLine(0, h, jw, h);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString("Уровень шумов", jw - 87, h - 1);
	}

	protected void paint_min_trace_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(minLevelColor);
		g.drawLine(0, (int)((min_trace_level.doubleValue() - top) * scale_y - 1),
							 jw, (int)((min_trace_level.doubleValue() - top) * scale_y - 1));
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
					g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y - 1),
					(int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y - 1));
				}
		}
	}

	protected void paint_modeled_trace(Graphics g)
	{
		if (modeled_y == null)
			return;

		g.setColor(modeledColor);
		for(int j=0; j<ep.length; j++)
		{
			if ((ep[j].begin < end) && (ep[j].end > start))
			{
				for (int i = Math.max(0, ep[j].begin - start); i < Math.min (end, ep[j].end) - start; i++)
				{
					g.drawLine((int)(i*scale_x+1), (int)((max_y - modeled_y[i+start] - top) * scale_y),
										 (int)((i+1)*scale_x+1), (int)((max_y - modeled_y[i+start+1] - top) * scale_y));
				}
			}
		}
	}

	protected void paint_reflectogram_events(Graphics g)
	{
		if (ep == null)
		{
			paint_events(g);
			return;
		}
		for(int j=0; j<ep.length; j++)
		{
			if ((ep[j].begin < end) && (ep[j].end > start))
			{
				int type = ep[j].getType();
				switch (type)
				{
					case ReflectogramEvent.LINEAR: g.setColor(linezoneColor); break;
					case ReflectogramEvent.CONNECTOR:
					{
						if (j == 0)
							g.setColor(deadzoneColor);
						else if (j == ep.length - 1)
							g.setColor(endColor);
						else
							g.setColor(connectColor);
						break;
					}
					case ReflectogramEvent.WELD: g.setColor(weldColor); break;
					default: g.setColor(noiseColor);
				}

				for (int i = Math.max(0, ep[j].begin - start); i <= Math.min (end, ep[j].end) - start; i++)
				{
//					g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y - 1),
//										 (int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y - 1));
					g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y),
										 (int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y));
				}
			}
		}

		g.setColor(noiseColor);
		if (ep[ep.length-1].end < end)
			for (int i =  Math.max(0, ep[ep.length-1].end - start); i< Math.min (end, y.length - start - 1); i++)
				g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y),
									 (int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y));
	}
}
