package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Analysis.Heap;

public class SimpleGraphPanel extends JPanel
{
	public static final int MOUSE_COUPLING = 5;

	private Color color; // color given externally for curve
	protected Color traceColor; // color really used to paint graphic itself (shadowed color)

	protected boolean weakColor;

	protected double[] y; // array of graphic points
	protected double deltaX;  // range between two neighbour points
	protected double maxY, minY; // maximum & minimum value of graph
	protected double scaleX, scaleY; // scales, used to resize graphic

	protected int start = 0; // номер начальной точки
	protected int end = 0; // номер конечной точки
	protected double top = 0; // столько находится за пределами экрана вверху (в единицах измерения - для рефлектограммы дБ)
	protected double bottom = 0; // столько находится за пределами экрана внизу (в единицах измерения - для рефлектограммы дБ)

	public SimpleGraphPanel (double[] y, double deltaX, Color color)
	{
		init (y, deltaX);
		setDefaultScales();
		traceColor = correctColor(color);
	}

	public SimpleGraphPanel (double[] y, double deltaX)
	{
		init (y, deltaX);
		setDefaultScales();
	}
	
	public void setWeakColors(boolean weakColors)
	{
	    this.weakColor = weakColors;
	}
	
	public void init (double[] y1, double deltaX1)
	{
		this.deltaX = deltaX1;
		if (y1 == null)
			y1 = new double[2];
		else
			this.y = y1;

		minY = y1.length > 0 ? y1[0] : 0;
		maxY = minY;
		for (int i = 1; i < y1.length; i++)
		{
			if (y1[i] < minY)
				minY = y1[i];
			else if (y1[i] > maxY)
				maxY = y1[i];
		}
	}

	protected void setGraphBounds(int start, int end)
	{
		this.start = start;
		this.end = end;
	}

	public void setGraphSize(Dimension d)
	{
		Dimension dim = getSize();
		double kx = d.getWidth() / dim.getWidth();
		double ky = d.getHeight() / dim.getHeight();
		scaleX *= kx;
		scaleY *= ky;
		super.setSize(d);
	}

	public void setDefaultScales()
	{
		setGraphBounds(0, y.length);
		top = 0;
		bottom = 0;

		// default values of scales - fitted to panel size
		scaleX = (double)getWidth() / (double)y.length;
		scaleY = getHeight() / (maxY - minY);
	}

	public synchronized void setColorModel(String id) {
		this.color = Heap.getColor(id);
		updColorModel();
	}

	protected void updColorModel() {
		this.traceColor = this.correctColor(this.color);
	}

	protected Color correctColor(Color color1)
	{
	    double weight = 0.3; // XXX
	    double a = weight;
	    double b = 255 * (1.0 - weight);
	    return weakColor ?
			new Color(
				(int )(color1.getRed() * a + b),
				(int )(color1.getGreen() * a + b),
				(int )(color1.getBlue() * a + b))
			: color1;
	}

	// plots from y[i0] to y[i0+N] _inclusively_ at x=x0..x0+N
	protected void draw_y_curve(Graphics g, double[] y1, int i0, int x0, int N)
	{
		if (N < 0)
			return;
		int[] xArr = new int[N + 1];
		int[] yArr = new int[N + 1];
		for (int j = 0; j <= N; j++)
		{
			xArr[j] = (int )((j + x0) * scaleX + 1);
			yArr[j] = (int )((maxY - y1[j + i0] - top) * scaleY);
			// XXX: to avoid rounding errors, we could use smth like this:
			//double vx = (j + x0) * scaleX + 1;
			//double vy = (maxY - y[j + i0] - top) * scaleY;
			//xArr[j] = Math.round((float )vx);
			//yArr[j] = Math.round((float )vy);
		}
		g.drawPolyline(xArr, yArr, N + 1);
//		for (int j = 0; j < N; j++)
//		{
//			g.drawLine(
//				(int)((j + x0    )*scaleX+1), (int)((maxY - y[j + i0] - top) * scaleY),
//				(int)((j + x0 + 1)*scaleX+1), (int)((maxY - y[j + i0 + 1] - top) * scaleY));
//		}
	}

	protected void paint_trace(Graphics g)
	{
		g.setColor(traceColor);

		int iFrom = Math.max(0, -start);
		int iTo = Math.min(end + 1, y.length) - start - 1;
		draw_y_curve(g, y, iFrom + start, iFrom, iTo - iFrom);

//		for (int i= Math.max(0, -start); i< Math.min (end + 1, y.length) - start - 1; i++)
//			g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
//				 (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
	}
}
