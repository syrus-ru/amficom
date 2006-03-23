package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.LinearCoeffs;
import com.syrus.AMFICOM.Client.Prediction.StatisticsMath.TimeDependenceData;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.util.Log;

public class TimeDependencePanel extends TraceEventsPanel
{
	protected long max_x, min_x; // maximum & minimum value of graph
	protected long left, right;

	protected TimeDependenceData[] data;
	protected LinearCoeffs linearCoeffs;
	public int c_event = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

	protected boolean show_points = true;
	protected boolean show_lines = true;
	protected boolean show_interpolation = true;

	public TimeDependencePanel(ResizableLayeredPanel panel, double[] y, double delta_x)
	{
		super(panel, y, delta_x);

		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
	}

	public void init (TimeDependenceData[] data)
	{
		this.data = data;
		if (data.length == 0) {
			Log.debugMessage("No TimeDependenceData", Level.FINER);
			return;
		}
		
		min_x = data[0].date;
		max_x = data[0].date;
		minY = data[0].value;
		maxY = data[0].value;
		for (int i = 1; i < data.length; i++)
		{
			if (data[i].date < min_x)
				min_x = data[i].date;
			else if (data[i].date > max_x)
				max_x = data[i].date;

			if (data[i].value < minY)
				minY = data[i].value;
			else if (data[i].value > maxY)
				maxY = data[i].value;
		}

		start = 0;
		end = data.length;
		left = 0;
		right = 0;

		// default values of scales - fitted to panel size
		scaleX = (double)getWidth() / (max_x - min_x);
		scaleY = (double)getHeight() / (maxY - minY);

		//Kx = 1/3600000d; // ����� � �����
		Kx = 1/60000d; // ����� � �������
		Ky = 1;
	}

	public void setLinearCoeffs(LinearCoeffs linearCoeffs)
	{
			this.linearCoeffs = linearCoeffs;
	}

	public void showEvent (int num)
	{
		if (data == null)
			return;
		if (num == -1)
			return;

		c_event = num;
	}

	@Override
	protected void paint_scales(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE));

		double m = calcTimeDistance (cell_w / scaleX * Kx); // ������ �� ���� �������
		double delta =	m * scaleX / Kx; // ����� �������� ����� �� ���� �������
		int x = (int)(((int)(left * Kx / m) ) * delta - left * scaleX);

		for (int i = 0; i < jw / delta + 1; i++)
			g.drawLine((int)(i * delta + x), 0, (int)(i * delta + x), jh);

		m = calcNodeDistance (cell_h / scaleY * Ky);
		delta =m * scaleY / Ky;

		if (inversed_y)
		{
			x = (int)(((int)(top * Ky / m) ) * delta - top * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
				g.drawLine(0, (int)(i * delta + x - 1), jw,	(int)(i * delta + x - 1));
		}
		else
		{
			x = (int)(((int)(bottom * Ky / m) ) * delta - bottom * scaleY);
			for (int i=0; i < jh / delta + 2; i++)
				g.drawLine(0, (int)(jh - (i * delta + x) - 1), jw,	(int)(jh - (i * delta + x) - 1));
		}
	}
	
	@Override
	protected double calcNodeDistance(double d)
	{
		if (d < .000001) return .000001;
		if (d < .000002) return .000002;
		if (d < .000005) return .000005;
		if (d < .00001) return .00001;
		if (d < .00002) return .00002;
		if (d < .00005) return .00005;
		if (d < .0001) return .0001;
		if (d < .0002) return .0002;
		if (d < .0005) return .0005;
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

	@Override
	protected void paint_scale_digits(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(UIManager.getColor(AnalysisResourceKeys.COLOR_SCALE_DIGITS));

		double m = calcTimeDistance (cell_w / scaleX * Kx); // ������ �� ���� �������
		double delta =	m * scaleX / Kx; // ����� �������� ����� �� ���� �������
		int x = (int)(((int)(left * Kx / m) ) * delta - left * scaleX); // ����� ������������ ������

		for (int i = 0; i < jw / delta + 1; i++)
		{
			double d = ((int)(((min_x + left) * Kx) / m)) * m / Kx;
			double d2 = d + (i * m - x) / Kx;
			String s = calcTimeLabels((long)(d2), m);

			g.drawString(s, (int)(i * delta + x - 12), jh - 5);
		}

		m = calcNodeDistance (cell_h / scaleY * Ky);
		delta =	m * scaleY / Ky;

		if (inversed_y)
		{
			x = (int) (((int)(top * Ky / m) ) * delta - top * scaleY);
			for (int i=0; i < jh / delta + 1; i++)
			{
				double d = (int)(((minY * Ky) + (top * Ky)) / m + i) * m;
				g.drawString(String.valueOf(MathRef.floatRound(d, 6)), 1, (int)(i * delta + x + 10));
			}
		}
		else
		{
			x = (int) (((int)(bottom * Ky / m) ) * delta - bottom * scaleY);
			for (int i=0; i < jh / delta + 2; i++)
			{
				double d = ((int)((minY * Ky) / m) * m) + (i + (int)(bottom * Ky / m) ) * m;
				g.drawString(String.valueOf(MathRef.floatRound(d, 6)), 1, (int)(jh - (i * delta + x) + 10));
			}
		}
	}


	protected void paint_points(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(Color.red);

		for (int i = Math.max(0, -start); i < Math.min(end, data.length) - start; i++)
		{
			int x1 = (int)((data[i+start].date - min_x - left) * scaleX + 1);
			int y1 = (int)((maxY - data[i+start].value - top) * scaleY - 1);
			g.fillOval(x1 - 2, y1 - 2, 4, 4);
		}
	}

	protected void paint_lines(Graphics g)
	{
		int jh = getHeight();
		int jw = getWidth();

		g.setColor(Color.blue);

		for (int i = Math.max(0, -start); i < Math.min(end, data.length) - start - 1; i++)
		{
			int x1 = (int)((data[i+start].date - min_x - left) * scaleX + 1);
			int y1 = (int)((maxY - data[i+start].value - top) * scaleY - 1);
			int x2 = (int)((data[i+start+1].date - min_x - left) * scaleX + 1);
			int y2 = (int)((maxY - data[i+start+1].value - top) * scaleY - 1);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	protected void paint_interpolation(Graphics g)
	{
		if(linearCoeffs != null && data.length > 0)
		{
			int x1 = (int)((data[0].date - min_x - left) * scaleX + 1);
			int x2 = (int)((data[data.length - 1].date - min_x - left) * scaleX + 1);
			int y1 = (int)((maxY - linearCoeffs.f((double)data[0].date)-top)*scaleY - 1);
			int y2 = (int)((maxY - linearCoeffs.f((double)data[data.length - 1].date)-top)*scaleY - 1);

			g.setColor(Color.GREEN);
			g.drawLine(x1, y1, x2, y2);
		}
	}


	public void paint(Graphics g)
	{
		if (data == null)
			return;

		paint_scales(g);

		if (show_lines)
			paint_lines(g);
		if (show_points)
			paint_points(g);
		if (show_interpolation)
			paint_interpolation(g);

		paint_scale_digits(g);
	}

	protected double calcTimeDistance2(double d) // � �����
	{
		if (d < .0166) return .0166; //1 min
		if (d < .0833) return .0833; //5 min
		if (d < .166) return .166; //10 min
		if (d < .5) return .5; //30 min
		if (d < 1.) return 1.; //hour
		if (d < 2.) return 2.; //2 hour
		if (d < 6.) return 6.; //6 hour
		if (d < 24.) return 24.; //1 day
		if (d < 48.) return 48.; //2 day
		if (d < 120.) return 120.; //5 day
		if (d < 240.) return 240.; //10 day
		if (d < 730.5) return 730.5; //30.5 day
		if (d < 1461.) return 1461.; //61 day
		if (d < 4383.) return 4383.; //180 day
		if (d < 8766.) return 8766.; //365.25 day
		return 35064; //4 years
	}

	protected double calcTimeDistance(double d) // � �������
	{
		if (d < 1) return 1; //1 min
		if (d < 5) return 5; //5 min
		if (d < 10) return 10; //10 min
		if (d < 30) return 30; //30 min
		if (d < 60) return 60; //hour
		if (d < 120) return 120; //2 hour
		if (d < 360) return 360; //6 hour
		if (d < 1440) return 1440; //1 day
		if (d < 2880) return 2880; //2 day
		if (d < 7200) return 7200; //5 day
		if (d < 14400) return 14400; //10 day
		if (d < 43830) return 43830; //30.5 day
		if (d < 87660) return 87660; //61 day
		if (d < 262980) return 262980; //180 day
		if (d < 525960) return 525960; //365.25 day
		return 2103840; //4 years
	}

	protected String calcTimeLabels(long date, double d) // � �������
	{
		//"dd.MM.yy HH:mm"
		String str = sdf.format(new Date(date));
		if (d <= 30) //return minutes
		{
			int ret = (int)(((int)(Integer.parseInt(str.substring(12)) / d)) * d);
			if (ret == 0 && str.substring(9,11).equals("00"))
				return str.substring(0, 8);
			else
				return str.substring(9, 12) + ((ret < 10) ? "0" : "") + String.valueOf(ret);
		}
		if (d <= 360) //return hours
		{
			double d2 = d / 60; // hours
			int ret = (int)(((int)(Integer.parseInt(str.substring(9, 11)) / d2)) * d2);
			if (ret == 0)
				return str.substring(0, 8);
			else
				return ret + ":00";
		}
		if (d <= 14400) //return days
		{
			double d2 = d / 1440; // days
			int ret = (int)(((int)(Integer.parseInt(str.substring(0, 2)) / d2)) * d2);
			if (ret == 1)
				return str.substring(0, 8);
			else
				return ret + str.substring(2, 5);
		}
		if (d <= 262980) //return month
		{
			return str.substring(3, 8);
		}
		return str.substring(6, 8); // return year
	}

	protected String calcTimeLabels2(long date, double d) // � �����
	{
		//"dd.MM.yy HH:mm"
		String str = sdf.format(new Date(date));
		if (d <= .166) //return minutes
		{
			String ret = str.substring(12);
			if (ret.equals("00"))
				return str;
			else
				return ret + ":00";
		}
		if (d <= 6) //return hours
		{
			String ret = str.substring(9, 11);
			if (ret.equals("00"))
				return str;
			else
				return str.substring(9);
		}
		if (d <= 240) //return days
		{
			String ret = str.substring(0, 2);
			if (ret.equals("00"))
				return str.substring(0, 8);
			else
				return str.substring(0, 5);
		}
		if (d <= 4383) //return month
		{
			String ret = str.substring(3, 5);
//			 if (ret.equals("00"))
				 return str.substring(3, 8);
//			 else
//				 return ret;
		}
		return str.substring(6, 8); // return year
	}
}
