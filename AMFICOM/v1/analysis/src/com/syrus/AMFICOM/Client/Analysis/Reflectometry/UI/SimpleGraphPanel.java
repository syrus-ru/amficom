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
		scaleY = (double)getHeight() / (maxY - minY);
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
		traceColor = correctColor(ColorManager.getColor(color_id));
	}

	protected void paint_trace(Graphics g)
	{
		g.setColor(traceColor);

		for (int i= Math.max(0, -start); i< Math.min (end + 1, y.length) - start - 1; i++)
			g.drawLine((int)(i*scaleX+1), (int)((maxY - y[i+start] - top) * scaleY),
								 (int)((i+1)*scaleX+1), (int)((maxY - y[i+start+1] - top) * scaleY));
	}

	public void paint(Graphics g)
	{
		paint_trace(g);
	}
}