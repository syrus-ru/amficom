package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.Analysis.GUIUtil;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class SimpleGraphPanel extends JPanel
{
	public static final int MOUSE_COUPLING = 5;

	protected Color color; // color given for curve (real shadowed color may be different)

	protected double[] y; // array of graphic points
	protected double deltaX;  // range between two neighbour points
	protected double maxY, minY; // maximum & minimum value of graph
	protected double scaleX, scaleY; // scales, used to resize graphic

	protected int start = 0; // ����� ��������� �����
	protected int end = 0; // ����� �������� �����
	protected double top = 0; // ������� ��������� �� ��������� ������ ������ (� �������� ��������� - ��� �������������� ��)
	protected double bottom = 0; // ������� ��������� �� ��������� ������ ����� (� �������� ��������� - ��� �������������� ��)

	protected boolean showAll = true;

	/**
	 * �������� �� ������� ��������������
	 */
	public static class GraphRange {
		private int xMin;
		private int xMax;
		private double yMin;
		private double yMax;
		/**
		 * ������� ������ ��������
		 */
		public GraphRange() {
			xMin = 1;
			xMax = 0;
			yMin = 1;
			yMax = 0;
		}
		/**
		 * ���������, ���� �� ��������
		 * @return true, ���� �������� ����, ���� false, ���� �� ����
		 */
		public boolean isEmpty() {
			return xMin > xMax || yMin > yMax;
		}
		/**
		 * ��������� �������� ���, ����� �� �������� ��������� X-����������
		 * @param x
		 */
		public void coverX(int x) {
			if (xMin > xMax) {
				xMin = x;
				xMax = x;
			} else if (x < xMin) {
				xMin = x;
			} else if (x > xMax) {
				xMax = x;
			}
		}
		/**
		 * ��������� �������� ���, ����� �� �������� ��������� Y-����������
		 * @param y
		 */
		public void coverY(double y) {
			if (yMin > yMax) {
				yMin = y;
				yMax = y;
			} else if (y < yMin) {
				yMin = y;
			} else if (y > yMax) {
				yMax = y;
			}
		}
		/**
		 * @return ����� ���� ��������� (������������).
		 * �� ��������� ��� ������� ���������.
		 */
		public int getXMin() {
			return xMin;
		}
		/**
		 * @return ������ ���� ��������� (������������).
		 * �� ��������� ��� ������� ���������.
		 */
		public int getXMax() {
			return xMax;
		}
		/**
		 * @return ����������� �������� Y ���������.
		 * �� ���������� ��� ������� ���������.
		 */
		public double getYMin() {
			return yMin;
		}
		/**
		 * @return ������������ �������� Y ���������.
		 * �� ���������� ��� ������� ���������.
		 */
		public double getYMax() {
			return yMax;
		}
	}

	public SimpleGraphPanel (double[] y, double deltaX)
	{
		init (y, deltaX);
		setDefaultScales();
	}

	protected boolean hasWeakTraceColors() {
		return false;
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

	/**
	 * ���� �� �����, ���� ����� �� ������ �� �������
	 * ���� � ������, �.�. ����� ��� ������ (��� ��������) ������� ��� �����
	 * ������������� ��������������� ����� ������ ������ � ����� �������� ��
	 * ��������������� LayeredPanel'��.
	 * 
	 * ���� ����� ������������ � ��������� ������� - deprecate �����
	 * Stas
	 */
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
		this.color = GUIUtil.getColor(id);
		updColorModel();
	}

	protected void updColorModel() {
	}

	protected Color correctColor(Color color1, boolean isTraceColor)
	{
		double weight = 0.3; // XXX: color soften factor
		double a = weight;
		double b = 255 * (1.0 - weight);
		return isTraceColor && hasWeakTraceColors() ?
			new Color( // XXX: ������ ��� ��������� ����� ������ �����
				(int )(color1.getRed() * a + b),
				(int )(color1.getGreen() * a + b),
				(int )(color1.getBlue() * a + b))
			: color1;
	}

	protected void updateRangeByYCurve(
			GraphRange r, double[] y1, int i0, int xT0, int N) {
		if (N < 0)
			return;
		r.coverX(xT0);
		r.coverX(xT0 + N);
		r.coverY(y1[ReflectogramMath.getArrayMaxIndex(y1, i0, i0 + N)]); // i0+N is included
		r.coverY(y1[ReflectogramMath.getArrayMinIndex(y1, i0, i0 + N)]);
	}
	/**
	 * plots array data from y[i0] to y[i0+N] <b>inclusively</b>
	 * to graph's (xTrace == xScreen/scaleX-start) ranging from xT0 to xT0+N <b>inclusively</b>.
	 * Does not perform clipping
	 */
	protected void drawYCurve(Graphics g, double[] y1, int i0, int xT0, int N)
	{
		if (N < 0)
			return;
		int[] xArr = new int[N + 1];
		int[] yArr = new int[N + 1];
		for (int j = 0; j <= N; j++)
		{
//			xArr[j] = (int )((j + xT0 - start) * scaleX + 1);
//			yArr[j] = (int )((maxY - y1[j + i0] - top) * scaleY);
			xArr[j] = index2coord(j + xT0);
			yArr[j] = value2coord(y1[j + i0]);
			// XXX: to avoid rounding errors, we could use smth like this:
			//double vx = (j + xT0 - start) * scaleX + 1;
			//double vy = (maxY - y[j + i0] - top) * scaleY;
			//xArr[j] = Math.round((float )vx);
			//yArr[j] = Math.round((float )vy);
		}
		g.drawPolyline(xArr, yArr, N + 1);
	}

	protected void paint_trace(Graphics g) {
		if (!isShowGraph()) {
			return;
		}
		g.setColor(correctColor(color, true));
		int iFromP = Math.max(start, 0);
		int iToP = Math.min(end + 1, y.length) - 1;
		drawYCurve(g, y, iFromP, iFromP, iToP - iFromP);
	}

	@Override
	public void paint(Graphics g)
	{
		if (showAll)
			paint_trace(g);
	}

	public void setShowAll(boolean b) {
		this.showAll = b;
	}

	protected boolean isShowGraph() {
		return true;
	}

	protected double getTrueScaleX() {
		return scaleX;
	}

	protected double getTrueScaleY() {
		return -scaleY;
	}

	protected int coord2index(int coord) {
		return (int)(coord / scaleX + .5) + start;
	}

	protected double coord2indexF(int coord) {
		return coord / scaleX + start;
	}

	protected int index2coord(int index) {
		// FIXME - ��������, ����� �� (+1) ����� � � value2coord
		// ���� ����� - �������� � ������������ index2coord � coord2index
		//return (int)(((double)(index - start))*scaleX+.5)+1;
		return (int)((index - start) * scaleX + .5);
	}

	protected int value2coord(double value) {
		return (int)Math.round((maxY - value - top) * scaleY);
	}

	protected double coord2value(int coord) {
		return maxY - top - coord / scaleY;
	}
}
