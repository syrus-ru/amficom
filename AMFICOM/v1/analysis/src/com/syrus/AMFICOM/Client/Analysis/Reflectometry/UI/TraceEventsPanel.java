package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;

public class TraceEventsPanel extends ScaledGraphPanel
{
	protected boolean draw_events = true;

	protected TraceEvent[] events;

	protected Color connectColor;
	protected Color deadzoneColor;
	protected Color weldColor;
	protected Color linezoneColor;
	protected Color nonidColor;
	protected Color noiseColor;
	protected Color endColor;

	public TraceEventsPanel(ResizableLayeredPanel panel, double[] y, double deltaX)
	{
		super (panel, y, deltaX);

		Kx = deltaX / 1000d;
		Ky = 1;
	}

	public void updEvents(String id)
	{
		RefAnalysis ra = Heap.getRefAnalysisByKey(id);
		if (ra != null)
			events = ra.events;
	}

	protected void updColorModel()
	{
		super.updColorModel();

		connectColor = UIManager.getColor(AnalysisResourceKeys.COLOR_CONNECTOR);
		deadzoneColor = UIManager.getColor(AnalysisResourceKeys.COLOR_DEADZONE);
		weldColor =  UIManager.getColor(AnalysisResourceKeys.COLOR_WELD);
		linezoneColor = UIManager.getColor(AnalysisResourceKeys.COLOR_LINEZONE);
		nonidColor = UIManager.getColor(AnalysisResourceKeys.COLOR_NON_ID);
		endColor = UIManager.getColor(AnalysisResourceKeys.COLOR_END);
		noiseColor = UIManager.getColor(AnalysisResourceKeys.COLOR_NOISE);
	}

	public void paint(Graphics g)
	{
		paint_scales(g);

		if (draw_events)
		{
			paint_events(g);
		}
		else
		{
			paint_trace(g);
		}
		paint_scale_digits(g);
	}

	protected void paint_events(Graphics g)
	{
		if (events == null)
		{
			paint_trace(g);
			return;
		}
		for(int j=0; j<events.length; j++)
		{
			if ((events[j].first_point < end) && (events[j].last_point > start))
			{
				int type = events[j].getType();
				switch (type)
				{
					case TraceEvent.LINEAR: g.setColor(linezoneColor); break;
					case TraceEvent.INITIATE: g.setColor(deadzoneColor); break;
					case TraceEvent.GAIN:
						g.setColor(weldColor); break;
					case TraceEvent.LOSS:
						g.setColor(weldColor); break;
					case TraceEvent.CONNECTOR: g.setColor(connectColor); break;
					case TraceEvent.TERMINATE: g.setColor(endColor); break;
					case TraceEvent.NON_IDENTIFIED: g.setColor(nonidColor); break;
					default: g.setColor(noiseColor);
				}

				int iFrom = events[j].first_point - start;
				int iTo = Math.min (end, events[j].last_point) - start;
				draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom + 1);
//				for (int i = events[j].first_point - start; i <= Math.min (end, events[j].last_point) - start; i++)
//				{
//					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
//										 (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
//				}
			}
		}
        g.setColor(noiseColor);
        int lastPoint = events.length > 0 ? events[events.length - 1].last_point : 0; 
		if (lastPoint < end)
		{
			int iFrom = lastPoint - start;
			int iTo = Math.min (end, y.length - start - 1);
			draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom);
//			for (int i = events[events.length-1].last_point - start; i< Math.min (end, y.length - start - 1); i++)
//				g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY - 1),
//									 (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY - 1));
		}
	}
}
