package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.*;

public class TraceEventsPanel extends ScaledGraphPanel
{
	protected boolean draw_events = true;

	protected SimpleReflectogramEvent[] sevents;

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
        if (!id.equals(Heap.PRIMARY_TRACE_KEY))
            return; // XXX: do not expect any refAnalysis other than one for PRIMARY_TRACE
        sevents = Heap.getMTAEPrimary().getSimpleEvents();
        
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
		if (sevents == null)
		{
			paint_trace(g);
			return;
		}
		for(int j=0; j<sevents.length; j++)
		{
			if ((sevents[j].getBegin() < end) && (sevents[j].getEnd() > start))
			{
				int stype = sevents[j].getEventType();
				switch (stype)
				{
					case SimpleReflectogramEvent.LINEAR:   g.setColor(linezoneColor); break;
					case SimpleReflectogramEvent.DEADZONE: g.setColor(deadzoneColor); break;
					case SimpleReflectogramEvent.GAIN:     g.setColor(weldColor); break;
					case SimpleReflectogramEvent.LOSS:     g.setColor(weldColor); break;
					case SimpleReflectogramEvent.CONNECTOR: g.setColor(connectColor); break;
					case SimpleReflectogramEvent.ENDOFTRACE: g.setColor(endColor); break;
					case SimpleReflectogramEvent.NOTIDENTIFIED: g.setColor(nonidColor); break;
					default: g.setColor(noiseColor);
				}

				int iFrom = sevents[j].getBegin() - start;
				int iTo = Math.min (end, sevents[j].getEnd()) - start;
				draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom + 1);
//				for (int i = events[j].first_point - start; i <= Math.min (end, events[j].last_point) - start; i++)
//				{
//					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
//										 (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
//				}
			}
		}
        g.setColor(noiseColor);
        int lastPoint = sevents.length > 0 ? sevents[sevents.length - 1].getEnd() : 0; 
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
