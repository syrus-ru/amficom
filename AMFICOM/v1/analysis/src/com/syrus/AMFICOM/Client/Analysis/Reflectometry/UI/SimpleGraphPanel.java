package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import javax.swing.JPanel;

public class SimpleGraphPanel extends JPanel
{
	public static final int mouse_coupling = 5;

	protected String color_id;

	protected Color traceColor; // color, which used to paint graphic itself

	protected double[] y; // array of graphic points
	protected double delta_x;  // range between two neighbour points
	protected double max_y, min_y; // maximum & minimum value of graph
	protected double scale_x, scale_y; // scales, used to resize graphic

	protected int start = 0; // номер начальной точки
	protected int end = 0; // номер конечной точки
	protected double top = 0; // столько находится за пределами экрана вверху (в единицах измерения - для рефлектограммы дБ)
	protected double bottom = 0; // столько находится за пределами экрана внизу (в единицах измерения - для рефлектограммы дБ)

	public SimpleGraphPanel (double[] y, double delta_x, Color color)
	{
		init (y, delta_x);
		setDefaultScales();
		traceColor = color;
	}

	public SimpleGraphPanel (double[] y, double delta_x)
	{
		init (y, delta_x);
		setDefaultScales();
	}

	public void init (double[] y, double delta_x)
	{
		this.delta_x = delta_x;
		if (y == null)
			y = new double[2];
		else
			this.y = y;

		min_y = y[0];
		max_y = y[0];
		for (int i = 1; i < y.length; i++)
		{
			if (y[i] < min_y)
				min_y = y[i];
			else if (y[i] > max_y)
				max_y = y[i];
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
		scale_x *= kx;
		scale_y *= ky;
		super.setSize(d);
	}

	public void setDefaultScales()
	{
		setGraphBounds(0, y.length);
		top = 0;
		bottom = 0;

		// default values of scales - fitted to panel size
		scale_x = (double)getWidth() / (double)y.length;
		scale_y = (double)getHeight() / (max_y - min_y);
	}

	public void setColorModel(String color_id)
	{
		this.color_id = color_id;
		updColorModel();
	}

	protected void updColorModel()
	{
		traceColor = ColorManager.getColor(color_id);
	}

	protected void paint_trace(Graphics g)
	{
		g.setColor(traceColor);

		for (int i= Math.max(0, -start); i< Math.min (end + 1, y.length) - start - 1; i++)
			g.drawLine((int)(i*scale_x+1), (int)((max_y - y[i+start] - top) * scale_y),
								 (int)((i+1)*scale_x+1), (int)((max_y - y[i+start+1] - top) * scale_y));
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
	}
}