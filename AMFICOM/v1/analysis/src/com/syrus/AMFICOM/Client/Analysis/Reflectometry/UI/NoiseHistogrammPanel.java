package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.Histogramm;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.io.BellcoreStructure;

// TEST VERSION!

public class NoiseHistogrammPanel extends ScaledGraphPanel
{
	private int nBins = 400;
	private double downLimit;
	private double upLimit;

	private double[] histData;

    private double level = 0.98;

	private boolean moveLevel = false;

	public NoiseHistogrammPanel(ResizableLayeredPanel panel)
	{
		super(panel, new double[] {}, 0.0); // XXX
		inversed_y = false;
		grid_shift_x = downLimit;

		updateHistogrammData();
        setDefaultScales();
	}

	protected void updColorModel()
	{
		super.updColorModel();
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
		paint_scales(g);
		paint_scale_digits(g);
		paint_level(g);
	}

	protected void paint_trace(Graphics g)
	{
//		g.setColor(traceColor);
        g.setColor(Color.gray);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, y.length) - start - 1; i++)
		{
//			if (y[i+start] > level)
//				g.setColor(traceColor);
//			else

			g.fillRect((int)(i * scaleX + 1), (int)((maxY - y[i+start] - top) * scaleY - 1),
								 (int)(scaleX+1), (int)((maxY - bottom) * scaleY + 1));
		}
	}

	protected void paint_level(Graphics g)
	{
		g.setColor(Color.RED);
		int jw = getWidth();

		g.drawLine(10, (int)((maxY - level - top) * scaleY - 1),
							 jw - 10, (int)((maxY - level - top) * scaleY - 1));

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));
		g.drawString(String.valueOf(MathRef.round_2(level)),
								 jw - 30,
								 (int)((maxY - level - top) * scaleY - 4));
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (SwingUtilities.isRightMouseButton(e) ||
				Math.abs(currpos.y-(int)((maxY - level - top)*scaleY)) < MOUSE_COUPLING)
		{
			moveLevel = true;
			level = coord2value(currpos.y);
            levelUpdated();
            setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			parent.repaint();
			return;
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (moveLevel)
		{
			upd_currpos(e);

			level = coord2value(currpos.y);
            levelUpdated();
			parent.repaint();
			return;
		}
		super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (moveLevel)
		{
			moveLevel = false;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}

    protected void levelUpdated() {
        // do nothing
    }

	public void updateHistogrammData()
	{
        // get reflectogram
        BellcoreStructure bs = Heap.getBSPrimaryTrace();
        if (bs == null)
            return; // XXX: no data to process
        double[] y1 = bs.getTraceData();

        // get end of trace
        ModelTraceAndEvents mtae = Heap.getMTAEPrimary();
        int noiseStart;
        if (mtae == null || mtae.getNEvents() == 0)
            noiseStart = 0;
        else
            noiseStart = mtae.getSimpleEvent(mtae.getNEvents() - 1).getEnd();
        if (noiseStart >= y1.length)
            return; // XXX: no data to process

        // --- we will process y[noiseStart] .. y[y.length-1] ---

        // find scales
        downLimit = y1[ReflectogramMath.getArrayMinIndex(y1, noiseStart, y1.length - 1)];
        upLimit = y1[ReflectogramMath.getArrayMaxIndex(y1, noiseStart, y1.length - 1)];
        double yTop = y1[ReflectogramMath.getArrayMaxIndex(y1, 0, y1.length - 1)];

        grid_shift_x = -yTop; // we treat abs max of trace data as 0.0 dB 
        Kx = (upLimit - downLimit) / nBins;
        Ky = 1;

        // calc histogram
        Histogramm histo = new Histogramm(downLimit, upLimit, nBins);
		histData = histo.init(y1, noiseStart, y1.length - 1);

        // convert histogram to cumulative
        for (int i = 1; i < histData.length; i++)
            histData[i] += histData[i - 1];

        // normalize
        double vMax = histData[histData.length - 1];
        for (int i = 0; i < histData.length; i++)
            histData[i] /= vMax;

		super.init(histData, 1.0); // XXX: 1.0 -- ?
	}
}
