// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.geometry;

import com.ofx.base.SxDistance;
import com.ofx.base.SxEnvironment;
import com.ofx.base.SxProperties;
import com.ofx.collections.SxComparatorInterface;
import com.ofx.collections.SxQSort;
import com.ofx.mapViewer.SxDisplayHint;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.projection.SxProjectionConical;
import com.ofx.projection.SxProjectionEllipsoid;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.repository.SxSpatialObject;
import com.ofx.repository.SxSymbology;
import java.awt.Graphics;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

// Referenced classes of package com.ofx.geometry:
//			SxRectangle, SxPolyline, SxPolygon, SxText, 
//			SxCircle, SxDoublePoint, SxGeometryInterface, SxGeometrySplitterInterface

public abstract class SxGeometry
	implements com.ofx.geometry.SxGeometryInterface
{
	class ClockwiseComparator
		implements com.ofx.collections.SxComparatorInterface
	{

		public int rank(java.lang.Object obj, java.lang.Object obj1)
		{
			double d = refPoint.smallerAngle((com.ofx.geometry.SxDoublePoint)obj1) - refPoint.smallerAngle((com.ofx.geometry.SxDoublePoint)obj);
			if(d < 0.0D)
				return 1;
			return d <= 0.0D ? 0 : -1;
		}

		private com.ofx.geometry.SxDoublePoint refPoint;

		public ClockwiseComparator(com.ofx.geometry.SxDoublePoint sxdoublepoint)
		{
			refPoint = null;
			refPoint = sxdoublepoint;
		}
	}

	class AscendingYComparator
		implements com.ofx.collections.SxComparatorInterface
	{

		public int rank(java.lang.Object obj, java.lang.Object obj1)
		{
			double d = ((com.ofx.geometry.SxDoublePoint)obj1).getY() - ((com.ofx.geometry.SxDoublePoint)obj).getY();
			if(d < 0.0D)
				return 1;
			return d <= 0.0D ? 0 : -1;
		}

		AscendingYComparator()
		{
		}
	}


	public SxGeometry()
	{
		dimension = -1;
		collection = false;
		hole = false;
		convexHullEpsilon = 0.0001D;
		nPoints = 0;
		xPoints = null;
		yPoints = null;
		bounds = null;
		priCenter = null;
		priBounds = null;
	}

	public static double distance(double d, double d1, double d2, double d3, 
			com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		double d4 = 0.0D;
		if(com.ofx.geometry.SxGeometry.domainDimension == 2)
		{
			d4 = com.ofx.geometry.SxGeometry.distance(d, d1, d2, d3);
		} else
		{
			double ad[] = {
				d, d2
			};
			double ad1[] = {
				d1, d3
			};
			if(sxprojectioninterface != null && !sxprojectioninterface.isDefaultGeoLatLong())
				try
				{
					sxprojectioninterface.unproject(ad, ad1, ad, ad1, 2);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception)
				{
					return 0.0D;
				}
			ad[0] *= 0.017453292519943295D;
			ad1[0] *= 0.017453292519943295D;
			ad[1] *= 0.017453292519943295D;
			ad1[1] *= 0.017453292519943295D;
			d4 = java.lang.Math.acos(java.lang.Math.sin(ad1[1]) * java.lang.Math.sin(ad1[0]) + java.lang.Math.cos(ad1[1]) * java.lang.Math.cos(ad1[0]) * java.lang.Math.cos(ad[1] - ad[0]));
			if(d4 < 0.0D)
				d4 += 3.1415926535897931D;
			d4 *= 6370820D;
		}
		int i = sxprojectioninterface != null ? sxprojectioninterface.getEllipsoid().getUnitId() : 203;
		if(i != 203)
			d4 = (new SxDistance(d4, 203)).convertTo(i);
		return d4;
	}

	public static double distance(double d, double d1, double d2, double d3)
	{
		return java.lang.Math.sqrt((d - d2) * (d - d2) + (d1 - d3) * (d1 - d3));
	}

	public static double groundDistance(double d, double d1, double d2, double d3, 
			int i)
	{
		if(!com.ofx.base.SxDistance.unitIdValid(i))
			return 0.0D;
		d *= 0.017453292519943295D;
		d1 *= 0.017453292519943295D;
		d2 *= 0.017453292519943295D;
		d3 *= 0.017453292519943295D;
		double d4 = java.lang.Math.acos(java.lang.Math.sin(d2) * java.lang.Math.sin(d) + java.lang.Math.cos(d2) * java.lang.Math.cos(d) * java.lang.Math.cos(d3 - d1));
		if(d4 < 0.0D)
			d4 += 3.1415926535897931D;
		d4 *= 6370820D;
		if(i != 203)
			d4 = (new SxDistance(d4, 203)).convertTo(i);
		return d4;
	}

	public abstract java.lang.Object clone()
		throws java.lang.CloneNotSupportedException;

	public abstract java.lang.Object shallowCopy()
		throws java.lang.CloneNotSupportedException;

	public abstract com.ofx.geometry.SxRectangle getBounds();

	public com.ofx.geometry.SxRectangle getPrimaryBounds()
	{
		priBounds = getBounds();
		return priBounds;
	}

	public com.ofx.geometry.SxRectangle[] getAllBounds()
	{
		com.ofx.geometry.SxRectangle asxrectangle[] = new com.ofx.geometry.SxRectangle[1];
		asxrectangle[0] = getBounds();
		return asxrectangle;
	}

	public com.ofx.geometry.SxDoublePoint getCenter()
	{
		if(center == null)
			center = getBounds().getCenter();
		return center;
	}

	public com.ofx.geometry.SxDoublePoint getClosestCenter(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		return getCenter();
	}

	public com.ofx.geometry.SxDoublePoint getPrimaryCenter()
	{
		priCenter = getCenter();
		return priCenter;
	}

	public com.ofx.geometry.SxDoublePoint asPoint()
	{
		return getCenter();
	}

	public com.ofx.geometry.SxPolyline asPolyline()
	{
		return new SxPolyline(getXPoints(), getYPoints(), getNPoints());
	}

	public com.ofx.geometry.SxPolygon asPolygon()
	{
		com.ofx.geometry.SxPolygon sxpolygon = new SxPolygon(getXPoints(), getYPoints(), getNPoints());
		sxpolygon.closePolygon();
		return sxpolygon;
	}

	public com.ofx.geometry.SxText asText()
	{
		double da[] = com.ofx.geometry.SxText.coordArrayFromArrays(getXPoints(), getYPoints());
		com.ofx.geometry.SxText sxtext = new SxText(da, "");
		return sxtext;
	}

	public boolean isRectangle()
	{
		return false;
	}

	public boolean regionIntersectsOrTouches(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		if(sxgeometryinterface.isCollection())
			return sxgeometryinterface.regionIntersectsOrTouches(this);
		if(!getBounds().intersectsOrTouches(sxgeometryinterface.getBounds()))
			return false;
		if(inside(sxgeometryinterface))
			return true;
		if(sxgeometryinterface.inside(this))
			return true;
		return boundaryIntersects(sxgeometryinterface);
	}

	public boolean inside(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		return inside(sxdoublepoint.x, sxdoublepoint.y);
	}

	public abstract boolean inside(double d, double d1);

	public boolean inside(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		int i = sxgeometryinterface.getNPoints();
		double ad[] = sxgeometryinterface.getXPoints();
		double ad1[] = sxgeometryinterface.getYPoints();
		for(int j = 0; j < i; j++)
			if(inside(ad[j], ad1[j]))
				return true;

		return false;
	}

	public void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, int ai[], 
			int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint)
	{
		display(g, d, sxdoublepoint, sxsymbology, null, ai, ai1, sxdisplayhint);
	}

	public com.ofx.geometry.SxGeometryInterface getBuffer(double d)
	{
		return new SxCircle(getCenter(), d);
	}

	public double closestDistanceTo(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		if(inside(sxdoublepoint))
			return 0.0D;
		else
			return closestPoint(sxdoublepoint).dist(sxdoublepoint);
	}

	public com.ofx.geometry.SxDoublePoint closestPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.geometry.SxDoublePoint asxdoublepoint[] = closestPointAndSegment(sxdoublepoint);
		return asxdoublepoint[0];
	}

	public com.ofx.geometry.SxDoublePoint[] closestPointAndSegment(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		int i = getNPoints();
		double ad[] = getXPoints();
		double ad1[] = getYPoints();
		double d = 0.0D;
		double d1 = 0.0D;
		double d2 = 0.0D;
		double d3 = 0.0D;
		double d4 = ad[0];
		double d6 = ad1[0];
		double d10 = sxdoublepoint.x;
		double d11 = sxdoublepoint.y;
		double d12 = d4;
		double d13 = d6;
		double d14 = 1.7976931348623157E+308D;
		for(int j = 1; j < i; j++)
		{
			int k = j - 1;
			double d5 = ad[k];
			double d7 = ad1[k];
			double d8 = ad[j];
			double d9 = ad1[j];
			double d18 = d8 - d5;
			double d19 = d9 - d7;
			double d16 = d18 * d18 + d19 * d19;
			double d17;
			if(d16 == 0.0D)
			{
				d17 = 0.0D;
			} else
			{
				double d15 = (d10 - d5) * d18 + (d11 - d7) * d19;
				d17 = d15 / d16;
			}
			if(d17 < 0.0D)
				d17 = 0.0D;
			if(d17 > 1.0D)
				d17 = 1.0D;
			double d20 = d5 + d17 * d18;
			double d21 = d7 + d17 * d19;
			double d22 = com.ofx.geometry.SxGeometry.distance(d10, d11, d20, d21);
			if(d22 < d14)
			{
				d14 = d22;
				d12 = d20;
				d13 = d21;
				d = d5;
				d1 = d7;
				d2 = d8;
				d3 = d9;
			}
		}

		com.ofx.geometry.SxDoublePoint asxdoublepoint[] = new com.ofx.geometry.SxDoublePoint[3];
		asxdoublepoint[0] = new SxDoublePoint(d12, d13);
		asxdoublepoint[1] = new SxDoublePoint(d, d1);
		asxdoublepoint[2] = new SxDoublePoint(d2, d3);
		return asxdoublepoint;
	}

	public int getNPoints()
	{
		if(isText() && nPoints < 2)
			java.lang.System.out.println("nPoints = " + nPoints);
		return nPoints;
	}

	public int getNumOfVertices()
	{
		return getNPoints();
	}

	public double[] getXPoints()
	{
		return xPoints;
	}

	public double[] getYPoints()
	{
		return yPoints;
	}

	public double rotation(com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.geometry.SxDoublePoint sxdoublepoint1)
	{
		return rotation(sxdoublepoint.getX(), sxdoublepoint.getY(), sxdoublepoint1.getX(), sxdoublepoint1.getY());
	}

	public double rotation(double d, double d1, double d2, double d3)
	{
		double d5 = d - d2;
		double d4;
		if(d5 == 0.0D)
			d4 = 90D;
		else
			d4 = (java.lang.Math.atan((d1 - d3) / d5) / 3.1415926535897931D) * 180D;
		return d4;
	}

	public static boolean intersects(double d, double d1, double d2, double d3, 
			double d4, double d5)
	{
		double d6 = (d2 - d) * (d2 - d) + (d3 - d1) * (d3 - d1);
		if(d6 == 0.0D)
		{
			return false;
		} else
		{
			double d7 = ((d4 - d) * (d2 - d) + (d5 - d1) * (d3 - d1)) / d6;
			return 0.0D <= d7 && d7 <= 1.0D;
		}
	}

	public static boolean intersects(double d, double d1, double d2, double d3, 
			double d4, double d5, double d6, double d7)
	{
		double d8 = (d2 - d) * (d7 - d5) - (d3 - d1) * (d6 - d4);
		double d9 = (d1 - d5) * (d6 - d4) - (d - d4) * (d7 - d5);
		if(d8 == 0.0D)
		{
			return d9 != 0.0D ? false : false;
		} else
		{
			double d10 = d9 / d8;
			double d11 = ((d1 - d5) * (d2 - d) - (d - d4) * (d3 - d1)) / d8;
			return 0.0D <= d10 && d10 <= 1.0D && 0.0D <= d11 && d11 <= 1.0D;
		}
	}

	public static com.ofx.geometry.SxDoublePoint pointAtDistance(double d, double d1, double d2, double d3, 
			double d4)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = com.ofx.geometry.SxDoublePoint.minusS(d2, d3, d, d1);
		double d5 = java.lang.Math.sqrt(sxdoublepoint.x * sxdoublepoint.x + sxdoublepoint.y * sxdoublepoint.y);
		sxdoublepoint.x = sxdoublepoint.x / d5;
		sxdoublepoint.y = sxdoublepoint.y / d5;
		sxdoublepoint.x = sxdoublepoint.x * d4;
		sxdoublepoint.y = sxdoublepoint.y * d4;
		sxdoublepoint.x = sxdoublepoint.x + d;
		sxdoublepoint.y = sxdoublepoint.y + d1;
		return sxdoublepoint;
	}

	public boolean boundaryIntersects(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		if(sxgeometryinterface.isCollection())
			return sxgeometryinterface.boundaryIntersects(this);
		if(!getBounds().intersectsOrTouches(sxgeometryinterface.getBounds()))
			return false;
		double ad[] = getXPoints();
		double ad1[] = getYPoints();
		int i = getNPoints();
		int j = sxgeometryinterface.getNPoints();
		double ad2[] = sxgeometryinterface.getXPoints();
		double ad3[] = sxgeometryinterface.getYPoints();
		double d = ad2[j - 1];
		double d1 = ad3[j - 1];
		for(int k = 0; k < j; k++)
		{
			double d2 = ad[i - 1];
			double d3 = ad1[i - 1];
			for(int l = 0; l < i; l++)
			{
				if(com.ofx.geometry.SxGeometry.intersects(d2, d3, ad[l], ad1[l], d, d1, ad2[k], ad3[k]))
					return true;
				d2 = ad[l];
				d3 = ad1[l];
			}

			d = ad2[k];
			d1 = ad3[k];
		}

		return false;
	}

	public double distance(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		return distance(sxgeometryinterface, ((com.ofx.projection.SxProjectionInterface) (null)));
	}

	public double distance(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = getCenter();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxgeometryinterface.getCenter();
		return com.ofx.geometry.SxGeometry.distance(sxdoublepoint.getX(), sxdoublepoint.getY(), sxdoublepoint1.getX(), sxdoublepoint1.getY(), sxprojectioninterface);
	}

	public double distance(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, int i)
	{
		return distance(sxgeometryinterface, i, ((com.ofx.projection.SxProjectionInterface) (null)), ((com.ofx.projection.SxProjectionInterface) (null)));
	}

	public double distance(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, int i, com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = getCenter();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxgeometryinterface.getCenter();
		return sxdoublepoint.distance(sxdoublepoint1, i, sxprojectioninterface, sxprojectioninterface1);
	}

	public com.ofx.geometry.SxGeometryInterface getLengthGeometry()
	{
		return asPolygon();
	}

	public double length(int i, com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
	{
		com.ofx.geometry.SxGeometryInterface sxgeometryinterface = getLengthGeometry();
		int j = sxgeometryinterface.getNPoints();
		if(j < 2)
			return 0.0D;
		double d = 0.0D;
		double ad[] = sxgeometryinterface.getXPoints();
		double ad1[] = sxgeometryinterface.getYPoints();
		if(sxprojectioninterface1 != null)
			try
			{
				sxprojectioninterface.unproject(ad, ad1, ad, ad1, j);
				sxprojectioninterface1.project(ad, ad1, ad, ad1, j);
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception)
			{
				return 0.0D;
			}
		for(int k = 0; k < j - 1; k++)
			d += com.ofx.geometry.SxGeometry.distance(ad[k], ad1[k], ad[k + 1], ad1[k + 1]);

		if(i != 203)
		{
			com.ofx.base.SxDistance sxdistance = new SxDistance(d, 203);
			d = sxdistance.convertTo(i);
		}
		return d;
	}

	public double length(int i)
	{
		return length(i, null, null);
	}

	public double length()
	{
		return length(((com.ofx.projection.SxProjectionInterface) (null)));
	}

	public double length(com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		com.ofx.geometry.SxGeometryInterface sxgeometryinterface = getLengthGeometry();
		int i = sxgeometryinterface.getNPoints();
		double d = 0.0D;
		if(i > 1)
		{
			double ad[] = sxgeometryinterface.getXPoints();
			double ad1[] = sxgeometryinterface.getYPoints();
			for(int j = 1; j < i; j++)
				d += com.ofx.geometry.SxGeometry.distance(ad[j - 1], ad1[j - 1], ad[j], ad1[j], sxprojectioninterface);

		}
		return d;
	}

	public boolean isHole()
	{
		return hole;
	}

	public void setHole(boolean flag)
	{
		hole = flag;
	}

	public boolean isCollection()
	{
		return collection;
	}

	public boolean isPoint()
	{
		return dimension == 0;
	}

	public boolean isPolyline()
	{
		return dimension == 1;
	}

	public boolean isPolygon()
	{
		return dimension == 2;
	}

	public boolean isText()
	{
		return dimension == 3;
	}

	public void setGeomType(java.lang.String s)
	{
		geomType = s;
		if(s.equals("Point"))
			dimension = 0;
		else
		if(s.equals("Polyline"))
			dimension = 1;
		else
		if(s.equals("Polygon"))
			dimension = 2;
		else
		if(s.equals("Text"))
			dimension = 3;
	}

	public java.lang.String getGeomType()
	{
		return geomType;
	}

	public int getDimension()
	{
		return dimension;
	}

	public int getNGeometrys()
	{
		return 1;
	}

	public float[] compressCoordinates(com.ofx.geometry.SxDoublePoint sxdoublepoint, double d, float af[])
	{
		int i = getNPoints();
		double d1 = 0.0D;
		double d3 = 0.0D;
		double ad[] = getXPoints();
		double ad1[] = getYPoints();
		for(int j = 0; j < i; j++)
		{
			double d2 = ad[j] - sxdoublepoint.x;
			double d4 = ad1[j] - sxdoublepoint.y;
			if(java.lang.Math.abs(d2 - (double)(float)d2) > d || java.lang.Math.abs(d4 - (double)(float)d4) > d)
				return null;
			af[j] = (float)d2;
			af[j + i] = (float)d4;
		}

		return af;
	}

	public com.ofx.base.SxProperties getProperties()
	{
		return com.ofx.base.SxEnvironment.singleton().getProperties();
	}

	int getQueryInterpolationPoints()
	{
		return 0;
	}

	public com.ofx.geometry.SxGeometryInterface getQueryGeometry(com.ofx.mapViewer.SxMapViewer sxmapviewer, com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
	{
		int i = getQueryInterpolationPoints();
		com.ofx.geometry.SxPolygon sxpolygon = asPolygon();
		sxpolygon = sxpolygon.asPolygon(i);
		if(sxprojectioninterface instanceof com.ofx.projection.SxProjectionConical)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(0, 90);
			try
			{
				sxprojectioninterface.project(sxdoublepoint, sxdoublepoint);
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception) { }
			if(sxpolygon.inside(sxdoublepoint))
			{
				double d = sxpolygon.getXPoints()[0];
				double d1 = sxpolygon.getYPoints()[0];
				sxpolygon.addPoint(sxdoublepoint.getX(), sxdoublepoint.getY());
				sxpolygon.addPoint(d, d1);
			}
		}
		int j = sxpolygon.getNPoints();
		double ad[] = sxpolygon.getXPoints();
		double ad1[] = sxpolygon.getYPoints();
		int k = j >> 1;
		if(!sxprojectioninterface1.equals(sxprojectioninterface))
		{
			if(sxprojectioninterface != null)
				try
				{
					sxprojectioninterface.unproject(ad, ad1, ad, ad1, j);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception1)
				{
					com.ofx.base.SxEnvironment.log("Unprojection of query geometry failed: " + sxprojectionexception1);
				}
				finally
				{
					sxpolygon.removeNaNs();
					if(sxpolygon.getNPoints() < k)
					{
						com.ofx.geometry.SxRectangle sxrectangle = new SxRectangle(-180D, -90D, 360D, 180D);
						return sxrectangle.getQueryGeometry(sxmapviewer, null, sxprojectioninterface1);
					}
				}
			if(sxprojectioninterface1 != null)
				try
				{
					sxprojectioninterface1.project(ad, ad1, ad, ad1, j);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception2)
				{
					com.ofx.base.SxEnvironment.log("Projection of query geometry failed: " + sxprojectionexception2);
				}
				finally
				{
					sxpolygon.removeNaNs();
					if(sxpolygon.getNPoints() < k)
						return null;
				}
		}
		java.lang.Object obj = sxpolygon;
		if(sxmapviewer.isGeometrySplittingEnabled())
		{
			com.ofx.geometry.SxGeometrySplitterInterface sxgeometrysplitterinterface = sxprojectioninterface1.getGeometrySplitter();
			obj = sxgeometrysplitterinterface.split(sxpolygon, sxprojectioninterface1);
			if(obj == null)
				obj = sxpolygon;
		}
		return ((com.ofx.geometry.SxGeometryInterface) (obj));
	}

	public void setInternalState(double ad[], double ad1[], int i, boolean flag)
	{
		throw new RuntimeException(getClass().getName() + ".setInternalState(double[], double[], int, boolean) not supported.");
	}

	public void setInternalState(int i, int j, int ai[], com.ofx.geometry.SxDoublePoint sxdoublepoint, double d)
	{
		throw new RuntimeException(getClass().getName() + ".setInternalState(aVertStart, aVertEnd, int[] aVertices, aRevAssumedDecPt) not supported.");
	}

	public void setInternalState(int i, int j, double ad[])
	{
		throw new RuntimeException(getClass().getName() + ".setInternalState(aVertStart, int aVertEnd, double[] aVertices) not supported.");
	}

	protected void removeNaNs()
	{
		double ad[] = getXPoints();
		double ad1[] = getYPoints();
		int i = getNPoints();
		int j = 0;
		int k = 0;
		int l = 0;
		for(; k < i; k++)
			if(java.lang.Double.isNaN(ad[k]) || java.lang.Double.isNaN(ad1[k]))
			{
				j++;
			} else
			{
				if(l < k)
				{
					ad[l] = ad[k];
					ad1[l] = ad1[k];
				}
				l++;
			}

		if(j > 0)
			nPoints -= j;
	}

	public com.ofx.geometry.SxGeometryInterface createConvexHull()
	{
		return createConvexHull(convexHullEpsilon);
	}

	public com.ofx.geometry.SxGeometryInterface createConvexHull(double d)
	{
		int i = getNPoints();
		if(i < 3)
			return this;
		double ad[] = getXPoints();
		double ad1[] = getYPoints();
		java.util.ArrayList arraylist = new ArrayList();
		for(int j = 0; j < i; j++)
			arraylist.add(new SxDoublePoint(ad[j], ad1[j]));

		com.ofx.geometry.SxDoublePoint sxdoublepoint = null;
		try
		{
			com.ofx.collections.SxQSort sxqsort = new SxQSort();
			sxqsort.sort(arraylist, new AscendingYComparator());
			int k = arraylist.size() - 1;
			sxdoublepoint = (com.ofx.geometry.SxDoublePoint)arraylist.get(k);
			arraylist.remove(k);
			sxqsort.sort(arraylist, new ClockwiseComparator(sxdoublepoint));
		}
		catch(java.lang.Exception exception)
		{
			return null;
		}
		java.util.ArrayList arraylist1 = (java.util.ArrayList)arraylist.clone();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = (com.ofx.geometry.SxDoublePoint)arraylist.get(0);
		for(int l = 2; l < arraylist.size(); l++)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint2 = (com.ofx.geometry.SxDoublePoint)arraylist.get(l);
			if(sxdoublepoint.smallerAngle(sxdoublepoint2) == sxdoublepoint.smallerAngle(sxdoublepoint1))
			{
				double d1 = sxdoublepoint.dist(sxdoublepoint1);
				double d2 = sxdoublepoint.dist(sxdoublepoint2);
				if(d2 <= d1)
					arraylist1.remove(arraylist1.indexOf(sxdoublepoint2));
				else
					arraylist1.remove(arraylist1.indexOf(sxdoublepoint1));
				sxdoublepoint1 = sxdoublepoint2;
			}
		}

		java.util.Stack stack = new Stack();
		stack.push(sxdoublepoint);
		int i1 = arraylist.size();
		i1--;
		stack.push(arraylist.get(i1));
		arraylist.remove(i1);
		i1--;
		stack.push(arraylist.get(i1));
		arraylist.remove(i1);
		for(int j1 = --i1; j1 >= 0; j1--)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint3 = (com.ofx.geometry.SxDoublePoint)arraylist.get(j1);
			while(stack.size() > 1) 
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint4 = (com.ofx.geometry.SxDoublePoint)stack.pop();
				com.ofx.geometry.SxDoublePoint sxdoublepoint6 = (com.ofx.geometry.SxDoublePoint)stack.peek();
				if(sxdoublepoint4.isP1ToTheLeftOfP2(sxdoublepoint3, sxdoublepoint6) > d)
				{
					stack.push(sxdoublepoint4);
					break;
				}
			}
			stack.push(sxdoublepoint3);
		}

		i = stack.size();
		ad = new double[i];
		ad1 = new double[i];
		i = 0;
		for(java.util.Enumeration enumeration = stack.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint5 = (com.ofx.geometry.SxDoublePoint)enumeration.nextElement();
			ad[i] = sxdoublepoint5.getX();
			ad1[i] = sxdoublepoint5.getY();
			i++;
		}

		return new SxPolygon(ad, ad1, i);
	}

	public void reproject(com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
		throws com.ofx.projection.SxProjectionException
	{
		throw new RuntimeException(getClass().getName() + ".reproject(SxProjectionInterface, SxProjectionInterface) not supported.");
	}

	public abstract void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.repository.SxSpatialObject sxspatialobject, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, 
			com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1, int ai[], int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint);

	public abstract void display(java.awt.Graphics g, double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, com.ofx.repository.SxSymbology sxsymbology, com.ofx.repository.SxSpatialObject sxspatialobject, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface, 
			int ai[], int ai1[], com.ofx.mapViewer.SxDisplayHint sxdisplayhint);

	static final int domainDimension = com.ofx.base.SxEnvironment.singleton().getProperties().getInteger("ofx.domainDimension", new Integer(2)).intValue();
	private static final long serialVersionUID = 0x3d971dae5a30e1dcL;
	public static final int POINT = 0;
	public static final int POLYLINE = 1;
	public static final int POLYGON = 2;
	public static final int TEXT = 3;
	public static final java.lang.String POINT_TYPE = "Point";
	public static final java.lang.String POLYLINE_TYPE = "Polyline";
	public static final java.lang.String POLYGON_TYPE = "Polygon";
	public static final java.lang.String TEXT_TYPE = "Text";
	protected int dimension;
	protected java.lang.String geomType;
	protected boolean collection;
	protected boolean hole;
	protected double convexHullEpsilon;
	protected int nPoints;
	protected double xPoints[];
	protected double yPoints[];
	protected com.ofx.geometry.SxDoublePoint center;
	protected com.ofx.geometry.SxRectangle bounds;
	protected com.ofx.geometry.SxDoublePoint priCenter;
	protected com.ofx.geometry.SxRectangle priBounds;

}
