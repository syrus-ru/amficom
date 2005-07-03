// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames 
// Source File Name:   SxText.java

package com.ofx.geometry;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.mapViewer.SxDisplayHint;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionGeneral;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.repository.SxSpatialObject;
import com.ofx.repository.SxSymbology;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.StringTokenizer;

// Referenced classes of package com.ofx.geometry:
//			SxPolygon, temp_sxtext_class, SxRectangle, SxDoublePoint, 
//			SxPointInterface, SxGeometry

public class SxText extends com.ofx.geometry.SxPolygon
	implements com.ofx.geometry.SxPointInterface, java.io.Serializable
{

	public static double[] coordArrayFromArrays(double xp[], double yp[])
	{
		double da[] = new double[4];
		da[0] = xp[0];
		da[1] = yp[0];
		da[2] = xp[1];
		da[3] = yp[1];
		return da;
	}

	public static double[] xCoordArray(double p[])
	{
		double da[] = new double[2];
		da[0] = p[0];
		da[1] = p[2];
		return da;
	}

	public static double[] yCoordArray(double p[])
	{
		double da[] = new double[2];
		da[0] = p[1];
		da[1] = p[3];
		return da;
	}

	public void settextcontents(com.ofx.geometry.temp_sxtext_class tsc)
	{
		font_name = tsc.sxtext_font_name;
		font_params = tsc.sxtext_font_params;
		angle = tsc.sxtext_angle;
		text = tsc.sxtext_text;
		font_style = tsc.sxtext_font_style;
		font_size = tsc.sxtext_font_size;
		font_color = new Color(tsc.sxtext_font_color_rgb);
	}

	public com.ofx.geometry.temp_sxtext_class gettextcontents()
	{
		com.ofx.geometry.temp_sxtext_class tsc = new temp_sxtext_class();
		tsc.sxtext_font_name = font_name;
		tsc.sxtext_font_params = font_params;
		tsc.sxtext_angle = angle;
		tsc.sxtext_text = text;
		tsc.sxtext_font_style = font_style;
		tsc.sxtext_font_size = font_size;
		tsc.sxtext_font_color_rgb = font_color.getRGB();
		return tsc;
	}

	public SxText()
	{
		angle = 0;
		text = "\u0422\u0415\u041A\u0421\u0422 \u0422\u0415\u041A\u0421\u0422";
		font_name = "Arial Cyr";
		font_params = "0,0,0";
		font_style = 0;
		font_size = 0;
		font_color = java.awt.Color.black;
	}

	public SxText(double d[], double d1[], java.lang.String txt)
	{
		super(d, d1, 2);
		angle = 0;
		text = "\u0422\u0415\u041A\u0421\u0422 \u0422\u0415\u041A\u0421\u0422";
		font_name = "Arial Cyr";
		font_params = "0,0,0";
		font_style = 0;
		font_size = 0;
		font_color = java.awt.Color.black;
		text = txt;
		if(text == null)
			text = "";
		rectcoords = com.ofx.geometry.SxText.coordArrayFromArrays(d, d1);
	}

	public SxText(double re[], java.lang.String txt)
	{
		super(com.ofx.geometry.SxText.xCoordArray(re), com.ofx.geometry.SxText.yCoordArray(re), 2);
		angle = 0;
		text = "\u0422\u0415\u041A\u0421\u0422 \u0422\u0415\u041A\u0421\u0422";
		font_name = "Arial Cyr";
		font_params = "0,0,0";
		font_style = 0;
		font_size = 0;
		font_color = java.awt.Color.black;
		text = txt;
		if(text == null)
			text = "";
		rectcoords = re;
	}

	public SxText(double re[], java.lang.String txt, int angle)
	{
		super(com.ofx.geometry.SxText.xCoordArray(re), com.ofx.geometry.SxText.yCoordArray(re), 2);
		this.angle = 0;
		text = "\u0422\u0415\u041A\u0421\u0422 \u0422\u0415\u041A\u0421\u0422";
		font_name = "Arial Cyr";
		font_params = "0,0,0";
		font_style = 0;
		font_size = 0;
		font_color = java.awt.Color.black;
		this.angle = angle;
		text = txt;
		if(text == null)
			text = "";
		rectcoords = re;
	}

	public SxText(double re[], java.lang.String txt, java.lang.String font, java.lang.String fontparams)
	{
		this(re, txt, font, fontparams, 0);
	}

	public SxText(double re[], java.lang.String txt, java.lang.String font, java.lang.String fontparams, int angle)
	{
		this(re, txt);
		font_name = font;
		font_params = fontparams;
		this.angle = angle;
		rectcoords = re;
		java.util.StringTokenizer tz = new StringTokenizer(font_params, ",");
		java.lang.String s1 = tz.nextToken();
		font_style = java.lang.Integer.parseInt(s1);
		java.lang.String s2 = tz.nextToken();
		font_size = java.lang.Integer.parseInt(s2);
		java.lang.String s3 = tz.nextToken();
		font_color = new Color(java.lang.Integer.parseInt(s3));
	}

	public java.lang.Object clone()
		throws java.lang.CloneNotSupportedException
	{
		return new SxText(rectcoords, text, font_name, font_params, angle);
	}

	public int getDimension()
	{
		return 3;
	}

	public com.ofx.geometry.SxRectangle getBounds()
	{
		if(bounds == null)
			bounds = new SxRectangle(
					rectcoords[0], 
					rectcoords[1], 
					rectcoords[2] - rectcoords[0], 
					rectcoords[3] - rectcoords[1]);
		return bounds;
	}

	public void drawText(java.awt.Graphics g, double rect[])
	{
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		java.awt.geom.AffineTransform t = g2.getTransform();
		boolean flag;
		if(angle != 0)
			flag = false;
		double alpha = java.lang.Math.toRadians(angle);
		double xcenter = rect[0];
		double ycenter = rect[1];
		g2.setColor(font_color);
		font_size = java.lang.Math.abs((int)(rect[3] - rect[1]));
		java.awt.Font font = new Font(font_name, font_style, font_size);
		g2.setFont(font);
		if(text == null)
			text = "";
		double rotatexcenter = xcenter;
		double rotateycenter = ycenter;
		rotateycenter -= (double)font_size / 2D;
		g2.rotate(java.lang.Math.toRadians(-angle), rotatexcenter, rotateycenter);
		g2.drawString(text, (float)xcenter, (float)ycenter);
		g2.setTransform(t);
	}

	public void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.repository.SxSpatialObject sxspatialobject, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, 
			int ai[], int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint)
	{
		if(d <= 0.0D)
		{
			return;
		} else
		{
			rectcoords[0] = getXPoints()[0];
			rectcoords[1] = getYPoints()[0];
			rectcoords[2] = getXPoints()[1];
			rectcoords[3] = getYPoints()[1];
			double rect[] = new double[4];
			rect[0] = (rectcoords[0] - sxdoublepoint.x) / d;
			rect[1] = (sxdoublepoint.y - rectcoords[1]) / d;
			rect[2] = (rectcoords[2] - sxdoublepoint.x) / d;
			rect[3] = (sxdoublepoint.y - rectcoords[3]) / d;
/*
			String ttt = text.toLowerCase();
			if(ttt.indexOf("манеж") != -1)
			{
				System.out.println(text + ":");
				System.out.print("	" + getXPoints()[0] + ", " + getYPoints()[0]);
				System.out.println("	" + getXPoints()[1] + ", " + getYPoints()[1]);
				System.out.println("Draw at:");
				System.out.print("	" + rect[0] + ", " + rect[1]);
				System.out.println("	" + rect[2] + ", " + rect[3]);
			}
*/
			drawText(g, rect);
			return;
		}
	}

	public void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.repository.SxSpatialObject sxspatialobject, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, 
			com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1, int ai[], int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint)
	{
		if(d <= 0.0D)
			return;
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(rectcoords[0], rectcoords[1]);
		com.ofx.geometry.SxDoublePoint sxdoublepoint2 = new SxDoublePoint(rectcoords[2], rectcoords[3]);
		try
		{
			if(sxprojectioninterface != null)
			{
				sxprojectioninterface.unproject(sxdoublepoint1, sxdoublepoint1);
				sxprojectioninterface.unproject(sxdoublepoint2, sxdoublepoint2);
			}
			if((sxprojectioninterface1 instanceof com.ofx.projection.SxProjectionGeneral) && ((com.ofx.projection.SxProjectionGeneral)sxprojectioninterface1).projectsOverHorizon(sxdoublepoint1))
				return;
			sxprojectioninterface1.project(sxdoublepoint1, sxdoublepoint1);
			double rect[] = new double[4];
			rect[0] = (sxdoublepoint1.x - sxdoublepoint.x) / d;
			rect[1] = (sxdoublepoint.y - sxdoublepoint1.y) / d;
			rect[2] = (sxdoublepoint2.x - sxdoublepoint.x) / d;
			rect[3] = (sxdoublepoint.y - sxdoublepoint2.y) / d;
			drawText(g, rect);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			com.ofx.base.SxEnvironment.singleton();
			com.ofx.base.SxEnvironment.log().println("SxText.display: " + sxprojectionexception);
		}
	}

	public void reproject(com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
		throws com.ofx.projection.SxProjectionException
	{
		if(sxprojectioninterface == null && sxprojectioninterface1 == null)
			return;
		if(sxprojectioninterface != null && sxprojectioninterface1 != null && sxprojectioninterface.equals(sxprojectioninterface1))
			return;
		com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(rectcoords[0], rectcoords[1]);
		com.ofx.geometry.SxDoublePoint sxdoublepoint2 = new SxDoublePoint(rectcoords[2], rectcoords[3]);
		if(sxprojectioninterface != null && !sxprojectioninterface.isDefaultGeoLatLong())
		{
			sxprojectioninterface.unproject(sxdoublepoint, sxdoublepoint);
			sxprojectioninterface.unproject(sxdoublepoint2, sxdoublepoint2);
		}
		if(sxprojectioninterface1 != null && !sxprojectioninterface1.isDefaultGeoLatLong())
		{
			sxprojectioninterface1.project(sxdoublepoint, sxdoublepoint);
			sxprojectioninterface1.project(sxdoublepoint2, sxdoublepoint2);
		}
		double ad[] = {
			sxdoublepoint.x, sxdoublepoint2.x
		};
		double ad1[] = {
			sxdoublepoint.y, sxdoublepoint2.y
		};
		setInternalState(ad, ad1, 2, false);
	}

	public int angle;
	public java.lang.String text;
	public java.lang.String font_name;
	public java.lang.String font_params;
	public int font_style;
	public int font_size;
	public java.awt.Color font_color;
	public double rectcoords[] = {
		0.0D, 0.0D, 0.0D, 0.0D
	};
}
