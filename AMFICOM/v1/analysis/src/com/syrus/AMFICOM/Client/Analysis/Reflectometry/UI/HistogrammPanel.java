package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.*;

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

	public HistogrammPanel(ResizableLayeredPanel panel, double[] y, double deltaX)
	{
		super(panel, y, deltaX);
		inversed_y = false;
		grid_shift_x = down_limit;

		Kx = (up_limit - down_limit) / (double)nBins;
		Ky = 1;

		init();

		ModelTraceManager mtm = (ModelTraceManager )Pool.get(ModelTraceManager.CODENAME, "primarytrace");
		if (mtm != null)
			updateHistogrammData(0, mtm.getModelTrace().getLength() - 1);
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
		double tmp = 1000./(double)event_size/deltaX;
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

			g.fillRect((int)(i * scaleX + 1), (int)((maxY - y[i+start] - top) * scaleY - 1),
								 (int)(scaleX+1), (int)((maxY - bottom) * scaleY + 1));
		}
	}

	protected void paint_gauss(Graphics g)
	{
		g.setColor(gaussColor);
		((Graphics2D) g).setStroke(GAUSS_STROKE);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, gauss.length) - start - 1; i++)
			g.drawLine((int)(i*scaleX+1), (int)((maxY - gauss[i+start] - top) * scaleY - 1),
								 (int)((i+1)*scaleX+1), (int)((maxY - gauss[i+start+1] - top) * scaleY - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_threshold(Graphics g)
	{
		g.setColor(thresholdColor);
		((Graphics2D) g).setStroke(THRESHOLD_STROKE);

		for (int i= Math.max(0, -start); i < Math.min (end + 1, threshold.length) - start - 1; i++)
			g.drawLine((int)(i*scaleX+1), (int)((maxY - threshold[i+start] - top) * scaleY - 1),
								 (int)((i+1)*scaleX+1), (int)((maxY - threshold[i+start+1] - top) * scaleY - 1));

		((Graphics2D) g).setStroke(DEFAULT_STROKE);
	}

	protected void paint_level(Graphics g)
	{
		g.setColor(Color.RED);
		int jw = getWidth();

		g.drawLine(10, (int)((maxY - level - top) * scaleY - 1),
							 jw - 10, (int)((maxY - level - top) * scaleY - 1));

		g.setColor(scaleDigitColor);
		g.drawString(String.valueOf(MathRef.round_2(level)),
								 jw - 30,
								 (int)((maxY - level - top) * scaleY - 4));
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();

		if (SwingUtilities.isRightMouseButton(e) ||
				Math.abs(currpos.y-(int)((maxY - level - top)*scaleY)) < mouse_coupling)
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

		init(y, deltaX);
		gauss = CoreAnalysisManager.calcGaussian(y, max_index);
		threshold = CoreAnalysisManager.calcThresholdCurve(y, max_index);

		for (int i = 0; i < y.length; i++)
		{
			y[i] /= maxY;
			gauss[i] /= maxY;
		}
		maxY = 1;
		minY = 0;
	}
}