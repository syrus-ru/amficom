package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.AnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;
//import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
//import com.syrus.AMFICOM.Client.Analysis.MathRef;

public class HistogrammPanel extends ScaledGraphPanel
{
	public static int wLetTyp = 1;

	protected int nBins = 400;
	protected double down_limit = -0.4;
	protected double up_limit = 0.8;
	protected double level = 0.2;

	protected double[] derivative;
	protected double[] gauss;
	protected double[] threshold;

	private static float gauss_w = 1.6f; // width of gaussian in pixels
	public static Stroke GAUSS_STROKE = new BasicStroke(gauss_w);
	public static Stroke THRESHOLD_STROKE = new BasicStroke(1);

	protected Color gaussColor;
	protected Color thresholdColor;

	private boolean move_level = false;

	public HistogrammPanel(ResizableLayeredPanel panel, double[] y, double delta_x)
	{
		super(panel, y, delta_x);
		inversed_y = false;
		grid_shift_x = down_limit;

		Kx = (up_limit - down_limit) / (double)nBins;
		Ky = 1;

		init();

		ReflectogramEvent[] ep = (ReflectogramEvent[])Pool.get("eventparams", "primarytrace");
		if (ep != null)
			updateHistogrammData(0, ep[ep.length-1].end);
		else
			updateHistogrammData(0, y.length / 2);
	}

	public void init()
	{
		int event_size = ReflectogramMath.getReflectiveEventSize(y, 0.1);
		derivative = ReflectogramMath.getDerivative(y, event_size, wLetTyp);

		for (int i = 0; i < derivative.length; i++)
			derivative[i] = -derivative[i];

		//Normalizing of the derivative to the dimension db/km
		double tmp = 1000./(double)event_size/delta_x;
		for (int i = 0; i < derivative.length; i++)
			derivative[i] = derivative[i]*tmp;
	}

	protected void updColorModel()
	{
		super.updColorModel();

		gaussColor = ColorManager.getColor("connectColor");
		thresholdColor = ColorManager.getColor("endColor");
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
		paint_scales(g);
		paint_gauss(g);
		paint_threshold(g);
		paint_scale_digits(g);
		paint_level(g);
	}

	protected void paint_trace(Graphics g)
	{
		g.setColor(traceColor);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, y.length) - start - 1; i++)
		{
			if (y[i+start] > level)
				g.setColor(traceColor);
			else
				g.setColor(Color.gray);

			g.fillRect((int)(i * scale_x + 1), (int)((max_y - y[i+start] - top) * scale_y - 1),
								 (int)(scale_x+1), (int)((max_y - bottom) * scale_y + 1));
		}
	}

	protected void paint_gauss(Graphics g)
	{
		g.setColor(gaussColor);
		((Graphics2D) g).setStroke(GAUSS_STROKE);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, gauss.length) - start - 1; i++)
			g.drawLine((int)(i*scale_x+1), (int)((max_y - gauss[i+start] - top) * scale_y - 1),
								 (int)((i+1)*scale_x+1), (int)((max_y - gauss[i+start+1] - top) * scale_y - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_threshold(Graphics g)
	{
		g.setColor(thresholdColor);
		((Graphics2D) g).setStroke(THRESHOLD_STROKE);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, threshold.length) - start - 1; i++)
			g.drawLine((int)(i*scale_x+1), (int)((max_y - threshold[i+start] - top) * scale_y - 1),
								 (int)((i+1)*scale_x+1), (int)((max_y - threshold[i+start+1] - top) * scale_y - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_level(Graphics g)
	{
		g.setColor(Color.RED);
		int jw = getWidth();

		g.drawLine(10, (int)((max_y - level - top) * scale_y - 1),
							 jw - 10, (int)((max_y - level - top) * scale_y - 1));

		g.setColor(scaleDigitColor);
		g.drawString(String.valueOf(MathRef.round_2(level)),
								 jw - 30,
								 (int)((max_y - level - top) * scale_y - 4));
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (SwingUtilities.isRightMouseButton(e) ||
				Math.abs(currpos.y-(int)((max_y - level - top)*scale_y)) < mouse_coupling)
		{
			move_level = true;
			level = coord2value(currpos.y);
			setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			parent.repaint();
			return;
		}

		super.this_mousePressed(e);
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (move_level)
		{
			upd_currpos(e);

			level = coord2value(currpos.y);
			parent.repaint();
			return;
		}
		super.this_mouseDragged(e);
	}

	protected void this_mouseReleased(MouseEvent e)
	{
		if (move_level)
		{
			move_level = false;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			parent.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}


	public void updateHistogrammData(int start, int end)
	{
		Histogramm histo = new Histogramm(down_limit, up_limit, nBins);
		y = histo.init(derivative, start, end);
		int max_index = histo.getMaximumIndex();

		init(y, delta_x);
		gauss = AnalysisManager.calcGaussian(y, max_index);
		threshold = AnalysisManager.calcThresholdCurve(y, max_index);

		for (int i = 0; i < y.length; i++)
		{
			y[i] /= max_y;
			gauss[i] /= max_y;
		}
		max_y = 1;
		min_y = 0;
	}
}