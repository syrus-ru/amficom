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

	protected ReflectogramEvent[] ep;
	protected ReflectogramAlarm[] alarms;

	protected Double min_trace_level;
	protected double noise_level = 28; // ???!
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
			int n = ep[ep.length-1].getEnd()+2;
			modeled_y = new double[n];
			for (int i = 0; i < ep.length; i++)
			{
				for (int j = ep[i].getBegin(); j <= ep[i].getEnd() && j < n; j++)
					modeled_y[j] = ep[i].refAmplitude(j);
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

	protected void draw_one_model_curve(Graphics g, ReflectogramEvent re)
	{
		if ((re.getBegin() <= end) && (re.getEnd() >= start))
		{
		    int i_from = Math.max(0, re.getBegin() - start);
		    int i_to = Math.min (end, re.getEnd()) - start;
		    int len = i_to - i_from + 1;
		    if (len >= 1)
		    {
		        int[] xArr = new int[len]; 
		        int[] yArr = new int[len];
		        for (int i = i_from; i <= i_to; i++)
		        {
		            xArr[i - i_from] = (int)(i * scale_x + 1);
		            yArr[i - i_from] = (int)((max_y - re.refAmplitude(i + start) - top) * scale_y);
		        }
		        g.drawPolyline(xArr, yArr, len);
		    }
		    /*
		    int i_from = Math.max(0, re.getBegin() - start);
		    int i_to = Math.min (end, re.getEnd() - 1) - start;
			for (int i = i_from; i <= i_to; i++)
			{
				g.drawLine((int)(i*scale_x+1), (int)((max_y - re.refAmplitude(i+start) - top) * scale_y),
						   (int)((i+1)*scale_x+1), (int)((max_y - re.refAmplitude(i+start+1) - top) * scale_y));
			}
			*/
		}
	}
	
	protected void draw_joint_of_two_model_curves(Graphics g, ReflectogramEvent reL, ReflectogramEvent reR)
	{
		int i = reR.getBegin() - start;
		if (i == reL.getEnd() - start && i <= end - start && i >= 0)
		{
			g.drawLine(
					(int )(i*scale_x+1), (int )((max_y - reL.refAmplitude(i+start) - top)*scale_y),
					(int )(i*scale_x+1), (int )((max_y - reR.refAmplitude(i+start) - top)*scale_y));
		}
	}

	protected void paint_modeled_trace(Graphics g)
	{
		if (modeled_y == null)
			return;

		g.setColor(modeledColor);
		for(int j=0; j<ep.length; j++)
			draw_one_model_curve(g, ep[j]);
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

				int i_begin = Math.max(start, ep[j].getBegin()) - start;
				int i_end = Math.min (end, ep[j].getEnd()) - start;
				for (int i = i_begin; i < i_end; i++)
				{
					g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y),
					        (int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y));
				}
			}
		}

		g.setColor(correctColor(noiseColor));
		int i_begin = Math.max(0, ep[ep.length-1].getEnd() - start);
		int i_end = Math.min (end, y.length - start - 1);
		if (ep[ep.length-1].getEnd() < end)
			for (int i = i_begin; i < i_end; i++)
				g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y),
					(int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y));
	}
}
