package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;

public class ScaledGraphPanel extends SimpleGraphPanel
{
	//���������� ������ ������������� � ���� ���������� ��� Y
	public boolean inversed_y = true;
	public boolean select_by_mouse = false;
	public double grid_shift_y = 0;
	public double grid_shift_x = 0;

	protected ResizableLayeredPanel parent;

	protected int cell_w = 40; // ������ ������ ����� � ��������
	protected int cell_h = 40; // ������ ������ ����� � ��������

	protected double Kx = 1; // ����������� ��������� �� ����� � ������� �������
	protected double Ky = 1; // ����������� ��������� �� ����� � ������� �������

//	protected Color scaleColor; // color to paint scale mesh
//	protected Color scaleDigitColor; // color to paint scale digits
//	protected Color selectColor;

	protected Point startpos = new Point();
	protected Point currpos = new Point();
	protected Point tmppos;

	public static Stroke SELECTION_STROKE =
			new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 5f, 5f }, 0.0f);
	public static Stroke DASHED_STROKE = SELECTION_STROKE;
	public static Stroke DEFAULT_STROKE = new BasicStroke(1);

	public ScaledGraphPanel(ResizableLayeredPanel panel, double[] y, double deltaX)
	{
		super (y, deltaX);

		parent = panel;

		try
		{
			jbInit();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				this_mouseDragged(e);
			}

			public void mouseMoved(MouseEvent e)
			{
				this_mouseMoved(e);
			}
		});
		this.addMouseListener(new java.awt.event.MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				this_mousePressed(e);
			}
			public void mouseReleased(MouseEvent e)
			{
				this_mouseReleased(e);
			}
			public void mouseClicked(MouseEvent e)
			{
			    this_mouseClicked(e);
			}
		});
	}

	protected void this_mousePressed(MouseEvent e)
	{
		startpos = e.getPoint();
		currpos = e.getPoint();
	}
	
	protected void this_mouseMoved(MouseEvent e)
	{
		// do nothing
	}
	
	protected void this_mouseClicked(MouseEvent e)
	{
		// do nothing
	}

	protected void this_mouseDragged(MouseEvent e)
	{
		if (select_by_mouse)
		{
			upd_currpos(e);

			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			paint_select(getGraphics().create());
		}
	}

	protected void this_mouseReleased(MouseEvent e) // the formally unused argument may be needed to overriding methods
	{
		if (select_by_mouse)
		{
			if (((Math.abs(currpos.x - startpos.x)) > 5) && ((Math.abs(currpos.y - startpos.y)) > 5 ))
			{
				double kx = (double)(getWidth()) / (double)Math.abs(currpos.x - startpos.x);
				double ky = (double)(getHeight())/ (double)Math.abs(currpos.y - startpos.y);
				double rx = (double)Math.max(0, Math.min(currpos.x, startpos.x)) / (double)(getWidth()) + 1d / (kx * 2d);
				double ry = (double)Math.max(0, Math.min(currpos.y, startpos.y)) / (double)(getHeight()) + 1d / (ky * 2d);
				parent.updScale(kx, ky, rx, ry);
			} else
				parent.repaint();

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	protected void limit_currpos()
	{
		// check if we go out the borders of panel
		int vx = 0;
		int wx = getWidth();
		int vy = 0;
		int wy = getHeight();

		if (currpos.x < vx)
			currpos.x = vx;
		if (currpos.x > vx + wx)
			currpos.x = vx + wx;
		if (currpos.y < vy)
			currpos.y = vy;
		if (currpos.y > vy + wy)
			currpos.y = vy + wy;
	}

	protected void upd_currpos(MouseEvent e)
	{
		tmppos = currpos;
		currpos = e.getPoint();
		
		limit_currpos();
	}

	protected void updColorModel()
	{
		super.updColorModel();
	}

	public void paint(Graphics g)
	{
		paint_scales(g);
		paint_trace(g);
		paint_scale_digits(g);
	}

	protected void paint_scales(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE));

		double m = calcNodeDistance (cell_w / scaleX * Kx); // ������ �� ���� �������
		double delta =	m * scaleX / Kx; // ����� �������� ����� �� ���� �������
		int x = (int)(((int)(start * Kx / m) ) * delta - start * scaleX); // ����� ������������ ������

		for (int i = 0; i < jw / delta + 1; i++)
			g.drawLine((int)(i * delta + x), 0, (int)(i * delta + x), jh);

		m = calcNodeDistance (cell_h / scaleY * Ky);
		delta =m * scaleY / Ky;

		if (inversed_y)
		{
			x = (int)(((int)(top * Ky / m) ) * delta - top * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
				g.drawLine(0, (int)Math.round(i * delta + x),
						jw, (int)Math.round(i * delta + x));
		} else
		{
			x = (int)(((int)(bottom * Ky / m) ) * delta - bottom * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
				g.drawLine(0, (int)Math.round(jh - (i * delta + x)),
						jw,	(int)Math.round(jh - (i * delta + x)));
		}
	}

	protected void paint_scale_digits(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));

		double m = calcNodeDistance (cell_w / scaleX * Kx); // ������ �� ���� �������
		double delta =	m * scaleX / Kx; // ����� �������� ����� �� ���� �������
		int x = (int)(((int)(start * Kx / m) ) * delta - start * scaleX); // ����� ������������ ������

		for (int i = 0; i < jw / delta + 1; i++)
			g.drawString(String.valueOf(MathRef.round_4 (grid_shift_x + (i + (int)(start * Kx / m) ) * m )), (int)(i * delta + x - 12), jh - 5);

		m = calcNodeDistance (cell_h / scaleY * Ky);
		delta =	m * scaleY / Ky;

		if (inversed_y)
		{
			x = (int) (((int)(top * Ky / m) ) * delta - top * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
				g.drawString(String.valueOf(MathRef.round_4 (grid_shift_y + (i + (int)(top * Ky / m) ) * m )), 1, (int)(i * delta + x + 10));
		} else
		{
			x = (int) (((int)(bottom * Ky / m) ) * delta - bottom * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
				g.drawString(String.valueOf(MathRef.round_4 (grid_shift_y + (i + (int)(bottom * Ky / m) ) * m )), 1, (int)(jh - (i * delta + x) + 10));
		}
	}

	protected void paint_select (Graphics g)
	{
		g.setXORMode(UIManager.getColor(AnalysisResourceKeys.COLOR_SELECT));
		g.drawRect (Math.min(startpos.x, tmppos.x),
								Math.min(startpos.y, tmppos.y),
								Math.abs(tmppos.x - startpos.x),
								Math.abs(tmppos.y - startpos.y));
		g.drawRect (Math.min(startpos.x, currpos.x),
								Math.min(startpos.y, currpos.y),
								Math.abs(currpos.x - startpos.x),
								Math.abs(currpos.y - startpos.y));
	}

	protected double calcNodeDistance(double d)
	{
		if (d < .001) return .001;
		if (d < .002) return .002;
		if (d < .005) return .005;
		if (d < .01) return .01;
		if (d < .02) return .02;
		if (d < .05) return .05;
		if (d < .1) return .1;
		if (d < .2) return .2;
		if (d < .5) return .5;
		if (d < 1.) return 1.;
		if (d < 2.) return 2.;
		if (d < 5.) return 5.;
		if (d < 10.) return 10.;
		if (d < 20.) return 20.;
		if (d < 50.) return 50.;
		if (d < 100.) return 100.;
		if (d < 200.) return 200.;
		if (d < 500.) return 500.;
		if (d < 1000.) return 1000.;
		if (d < 2000.) return 2000.;
		if (d < 5000.) return 5000.;
		return 10000.;
	}

	protected double getTrueScaleX()
	{
		return scaleX;
	}

	protected double getTrueScaleY()
	{
		return -scaleY;
	}

	protected int coord2index(int coord)
	{
		return (int)(coord / scaleX + .5) + start;
	}
	
	// ������ ����� ���� ����� ������������� �������� ����������
	protected double coord2indexF(int coord)
	{
		return coord / scaleX + start;
	}

	protected int index2coord(int index)
	{
		// FIXME - ��������, ����� �� (+1) ����� � � value2coord
		// ���� ����� - �������� � ������������ index2coord � coord2index
		//return (int)(((double)(index - start))*scaleX+.5)+1;
		return (int)((index - start) * scaleX + .5);
	}

	protected int value2coord(double value)
	{
		return (int)Math.round((maxY - value - top) * scaleY);
	}

	protected double coord2value(int coord)
	{
		return maxY - top - coord / scaleY;
	}
}
