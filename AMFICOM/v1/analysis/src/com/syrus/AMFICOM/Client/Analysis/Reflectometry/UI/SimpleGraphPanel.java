package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;

import javax.swing.JPanel;

public class SimpleGraphPanel extends JPanel
{
	public static final int mouse_coupling = 5;

	protected String color_id;

	protected Color traceColor; // color, which used to paint graphic itself
	
	protected boolean weakColor;

	protected double[] y; // array of graphic points
	protected double deltaX;  // range between two neighbour points
	protected double maxY, minY; // maximum & minimum value of graph
	protected double scaleX, scaleY; // scales, used to resize graphic

	protected int start = 0; // ����� ��������� �����
	protected int end = 0; // ����� �������� �����
	protected double top = 0; // ������� ��������� �� ��������� ������ ������ (� �������� ��������� - ��� �������������� ��)
	protected double bottom = 0; // ������� ��������� �� ��������� ������ ����� (� �������� ��������� - ��� �������������� ��)

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
	
	public void init (double[] y, double deltaX)
	{
		this.deltaX = deltaX;
		if (y == null)
			y = new double[2];
		else
			this.y = y;

		minY = y[0];
		maxY = y[0];
		for (int i = 1; i < y.length; i++)
		{
			if (y[i] < minY)
				minY = y[i];
			else if (y[i] > maxY)
				maxY = y[i];
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

	public void setColorModel(String color_id)
	{
		this.color_id = color_id;
		updColorModel();
	}
	
	protected Color correctColor(Color color)
	{
	    double weight = 0.3; // XXX
	    double a = weight;
	    double b = 255 * (1.0 - weight);
	    return weakColor ?
			new Color(
				(int )(color.getRed() * a + b),
				(int )(color.getGreen() * a + b),
				(int )(color.getBlue() * a + b))
			: color;
	    /*
	    return weakColor ?
	            new Color(
	                color.getRed(),
	                color.getGreen(),
	                color.getBlue(),
	                250)
	    		: color;
	    */
	}

	protected void updColorModel()
	{
		System.out.println(color_id);
		traceColor = correctColor(ColorManager.getColor(color_id));
	}

	// plots from y[i0] to y[i0+N] _inclusively_ at x=x0..x0+N
	protected void draw_y_curve(Graphics g, double[] y, int i0, int x0, int N)
	{
		if (N < 0)
			return;
		int[] xArr = new int[N + 1];
		int[] yArr = new int[N + 1];
		for (int j = 0; j <= N; j++)
		{
			xArr[j] = (int )((j + x0) * scaleX + 1);
			yArr[j] = (int )((maxY - y[j + i0] - top) * scaleY);
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