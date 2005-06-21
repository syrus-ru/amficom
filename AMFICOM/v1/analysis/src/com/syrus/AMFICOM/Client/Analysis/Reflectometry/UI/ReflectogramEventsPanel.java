package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.ClientAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
import com.syrus.AMFICOM.client.event.Dispatcher;

public class ReflectogramEventsPanel extends TraceEventsPanel
{
	protected Dispatcher dispatcher;

	public boolean draw_modeled = false;
	public boolean draw_alarms = false;
	public boolean draw_min_trace_level = false;
	public boolean draw_noise_level = false;

	//protected ModelTraceAndEvents mtae; // использовалось только в методах, вызываемых из paint()
	protected ReflectogramAlarm[] alarms;

	protected double noise_level = 28; // ???!
	protected boolean moving_level = false;

	public ReflectogramEventsPanel(ResizableLayeredPanel panel, Dispatcher dispatcher, double[] y, double deltaX)
	{
		super (panel, y, deltaX);
		init_module(dispatcher);
	}

	void init_module(Dispatcher dispatcher1)
	{
		this.dispatcher = dispatcher1;
	}

	public void updateAlarms (ReflectogramAlarm[] alarms1)
	{
		this.alarms = alarms1;
		if (alarms1 != null)
		{
			draw_alarms = true;
		} else
			draw_alarms = false;
	}

	public void updateNoiseLevel()
	{
		RefAnalysis ana = Heap.getRefAnalysisPrimary();
		if (ana != null)
		{
			TraceEvent ev = ana.overallStats;
			if (ev != null)
				noise_level = ev.overallStatsNoiseLevel98Pct();
		}

		// FIXME: способ выбора minTraceLevel (оба не годятся -- saa)
		//updateMinTraceLevel(noise_level - 3); // по +3 дБ от ур. шума?
		ClientAnalysisManager.setDefaultMinTraceLevel(); // между мин. фит. кр. и абс. мин. р/г? 
	}

	protected void updColorModel()
	{
		super.updColorModel();
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (draw_min_trace_level)
		{
			if (coord2index(currpos.x) > y.length)
				return;

			if(Math.abs(currpos.y-(int)((getMinTraceLevel() - top) * scaleY - 1)) < MOUSE_COUPLING)
			{
				moving_level = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
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
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (moving_level)
		{
			upd_currpos(e);

			//double pos = getMinTraceLevel() + (currpos.y - tmppos.y)/scaleY;
            //updateMinTraceLevel(-pos);
			double pos = coord2value(currpos.y);
			updateMinTraceLevel(-pos);
			parent.repaint();
			dispatcher.firePropertyChange(new RefUpdateEvent(this, RefUpdateEvent.MIN_TRACE_LEVEL_CHANGED_EVENT));
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
		} else
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
		} else if (draw_noise_level && draw_events)
			paint_noise_level(g);

		paint_scale_digits(g);
	}

	protected void paint_noise_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE).darker());
		int h = (int)((noise_level - top) * scaleY - 1);
		g.drawLine(0, h, jw, h);
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
		g.drawString(LangModelAnalyse.getString("Noise level"), jw - 87, h - 1);
	}

	protected void paint_min_trace_level(Graphics g)
	{
		int jw = getWidth();
		((Graphics2D) g).setStroke(SELECTION_STROKE);
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_MIN_TRACE_LEVEL));
		g.drawLine(0, (int)((getMinTraceLevel() - top) * scaleY - 1),
							 jw, (int)((getMinTraceLevel() - top) * scaleY - 1));
		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_alarms(Graphics g)
	{
		if (alarms == null)
			return;

		g.setColor(Color.red);
		for (int j = 0; j < alarms.length; j++)
		{
			if ((alarms[j].pointCoord <= end) && (alarms[j].pointCoord >= start))
				for (int i = Math.max(0, alarms[j].pointCoord - start); i < Math.min (end, alarms[j].endPointCoord) - start; i++)
				{
					g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY - 1),
					(int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY - 1));
				}
		}
	}

    /**
     * Draw model curve using current graphics plotting settings.
     * @param g graphics to plot (not null)
     * @param mtr trace and range to plot
     * @param avoidLastPoint true to draw [sre.begin .. sre.end-1];
     *   false to draw [sre.begin .. sre.end].
     */
    protected void drawModelCurve(Graphics g, ModelTraceRange mtr,
            boolean avoidLastPoint)
    {
        int n1 = mtr.getBegin();
        int n2 = mtr.getEnd() - (avoidLastPoint ? 1 : 0);
        if ((n1 <= end) && (n2 >= start))
        {
            int iFrom = Math.max(start, n1);
            int iTo = Math.min(end, n2);
            if (iTo - iFrom >= 0)
            {
                double[] vArr = mtr.getYArray(iFrom, iTo - iFrom + 1);
                draw_y_curve(g, vArr, 0, iFrom - start, iTo - iFrom);
            }
        }
    }

    /**
     * Draw model curve using current graphics plotting settings.
     * @param g graphics to plot, or null if no plotting is required
     * @param r GraphRange to be updated, or null if update not requied
     * @param mtr trace and range to plot
     * @param avoidLastPoint true to draw [sre.begin .. sre.end-1];
     *   false to draw [sre.begin .. sre.end].
     */
    protected void drawModelCurve(Graphics g, GraphRange r, ModelTraceRange mtr,
            boolean avoidLastPoint)
    {
        if (g != null)
            drawModelCurve(g, mtr, avoidLastPoint);
        if (r != null) {
            int n1 = mtr.getBegin();
            int n2 = mtr.getEnd() - (avoidLastPoint ? 1 : 0);
            int iFrom = n1;
            int iTo = n2;
            if (iTo - iFrom >= 0)
            {
                double[] vArr = mtr.getYArray(iFrom, iTo - iFrom + 1);
                update_range_by_y_curve(r, vArr, 0, iFrom - start, iTo - iFrom);
            }
        }
    }

    protected void paint_modeled_trace(Graphics g)
	{
    	ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
		if (mtae == null)
			return;
		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_MODELED));
		drawModelCurve(g, mtae.getModelTrace(), false);
	}

    protected void paint_reflectogram_events(Graphics g)
    {
        paint_events(g);
    }

    protected void updateMinTraceLevel(double value)
    {
        Heap.setMinTraceLevel(-value);
    }
    protected double getMinTraceLevel() {
        return -Heap.getMinTraceLevel();
    }
}
