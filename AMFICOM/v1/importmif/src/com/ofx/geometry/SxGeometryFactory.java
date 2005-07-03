// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.geometry;

import com.ofx.base.SxDistance;
import com.ofx.projection.SxProjectionEllipsoid;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ofx.geometry:
//			SxPoint, SxMultiPoint, SxPolyline, SxMultiPolyline, 
//			SxPolylineInterface, SxPolygon, SxMultiPolygon, SxPolygonInterface, 
//			SxGeometryCollection, SxDoublePoint, SxCircle, SxEllipse, 
//			SxRectangle, SxGeometrySplitterInterface, SxGeometryInterface, SxGeometry, 
//			SxPointInterface

public class SxGeometryFactory
{

	public SxGeometryFactory()
	{
		this(null, null);
	}

	public SxGeometryFactory(com.ofx.projection.SxProjectionInterface sxprojectioninterface, com.ofx.projection.SxProjectionInterface sxprojectioninterface1)
	{
		projX = new double[100];
		projY = new double[100];
		xPoints = new ArrayList();
		yPoints = new ArrayList();
		vCount = new ArrayList();
		geom = 0;
		count = 0;
		capacity = 0;
		inProj = sxprojectioninterface;
		outProj = sxprojectioninterface1;
		distanceUnits = sxprojectioninterface != null ? sxprojectioninterface.getEllipsoid().getUnitId() : 203;
		initialize();
	}

	public com.ofx.projection.SxProjectionInterface getInputProjection()
	{
		return inProj;
	}

	public com.ofx.projection.SxProjectionInterface getOutputProjection()
	{
		return outProj;
	}

	public int getDistanceUnits()
	{
		return distanceUnits;
	}

	public void setDistanceUnits(int i)
	{
		distanceUnits = i;
	}

	public void initialize()
	{
		xPoints.clear();
		yPoints.clear();
		vCount.clear();
		geom = 0;
		count = 0;
		capacity = 0;
	}

	public void partitionGeometry()
	{
		if(count > 0)
		{
			vCount.add(new Integer(count));
			geom++;
			count = 0;
			capacity = 0;
		}
	}

	private void ensureCapacity(int i)
	{
		if(count + i <= capacity)
			return;
		boolean flag = capacity == 0;
		capacity = ((count + i) / 100 + 1) * 100;
		double ad[] = new double[capacity];
		double ad1[] = new double[capacity];
		if(flag)
		{
			xPoints.add(ad);
			yPoints.add(ad1);
		} else
		{
			double ad2[] = (double[])xPoints.get(geom);
			double ad3[] = (double[])yPoints.get(geom);
			for(int j = 0; j < count; j++)
			{
				ad[j] = ad2[j];
				ad1[j] = ad3[j];
			}

			xPoints.set(geom, ad);
			yPoints.set(geom, ad1);
		}
		if(capacity > projX.length)
		{
			projX = new double[capacity];
			projY = new double[capacity];
		}
	}

	public void addVertex(double d, double d1)
	{
		ensureCapacity(1);
		if(doProjection(d, d1))
		{
			d = projX[0];
			d1 = projY[0];
		}
		double ad[] = (double[])xPoints.get(geom);
		double ad1[] = (double[])yPoints.get(geom);
		ad[count] = d;
		ad1[count] = d1;
		count++;
		xPoints.set(geom, ad);
		yPoints.set(geom, ad1);
	}

	public void addVertices(double ad[], double ad1[])
	{
		if(ad == null || ad1 == null || ad.length != ad1.length)
			throw new RuntimeException("SxGeometryFactory.add(): Invalid vertices");
		int i = ad.length;
		ensureCapacity(i);
		if(doProjection(ad, ad1, i))
		{
			ad = projX;
			ad1 = projY;
		}
		double ad2[] = (double[])xPoints.get(geom);
		double ad3[] = (double[])yPoints.get(geom);
		for(int j = 0; j < i; j++)
		{
			ad2[count] = ad[j];
			ad3[count] = ad1[j];
			count++;
		}

		xPoints.set(geom, ad2);
		yPoints.set(geom, ad3);
	}

	private boolean unproject(double d, double d1)
	{
		projX[0] = d;
		projY[0] = d1;
		return unproject(projX, projY, 1);
	}

	private boolean unproject(double ad[], double ad1[], int i)
	{
		try
		{
			if(inProj != null)
			{
				inProj.unproject(ad, ad1, projX, projY, i);
				return true;
			}
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			throw new RuntimeException("SxGeometryFactory unproject() exception: " + sxprojectionexception);
		}
		return false;
	}

	private boolean project(double d, double d1)
	{
		projX[0] = d;
		projY[0] = d1;
		return project(projX, projY, 1);
	}

	private boolean project(double ad[], double ad1[], int i)
	{
		try
		{
			if(outProj != null)
			{
				outProj.project(ad, ad1, projX, projY, i);
				return true;
			}
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			throw new RuntimeException("SxGeometryFactory project() exception: " + sxprojectionexception);
		}
		return false;
	}

	private boolean doProjection(double d, double d1)
	{
		projX[0] = d;
		projY[0] = d1;
		return doProjection(projX, projY, 1);
	}

	private boolean doProjection(double ad[], double ad1[], int i)
	{
		boolean flag = false;
		if(inProj != null)
		{
			flag = unproject(ad, ad1, i);
			ad = projX;
			ad1 = projY;
		}
		if(outProj != null)
			flag = project(ad, ad1, i);
		return flag;
	}

	private void verifySufficientPoints(int i)
	{
		int j = vCount.size();
		boolean flag = j >= 1;
		for(int k = 0; k < j; k++)
		{
			java.lang.Integer integer = (java.lang.Integer)vCount.get(k);
			if(integer.intValue() >= i)
				continue;
			flag = false;
			break;
		}

		if(!flag)
			throw new RuntimeException("SxGeometryFactory: insufficient points");
		else
			return;
	}

	public com.ofx.geometry.SxGeometryInterface splitGeometry(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		if(outProj != null)
		{
			com.ofx.geometry.SxGeometrySplitterInterface sxgeometrysplitterinterface = outProj.getGeometrySplitter();
			sxgeometryinterface = sxgeometrysplitterinterface.split(sxgeometryinterface, outProj);
		}
		return sxgeometryinterface;
	}

	public com.ofx.geometry.SxPointInterface asPointGeometry()
	{
		partitionGeometry();
		verifySufficientPoints(1);
		java.lang.Object obj = null;
		int i = vCount.size();
		if(i == 1)
		{
			double ad[] = (double[])xPoints.get(0);
			double ad1[] = (double[])yPoints.get(0);
			obj = new SxPoint(ad[0], ad1[0]);
		} else
		{
			com.ofx.geometry.SxMultiPoint sxmultipoint = new SxMultiPoint();
			for(int j = 0; j < i; j++)
			{
				double ad2[] = (double[])xPoints.get(j);
				double ad3[] = (double[])yPoints.get(j);
				sxmultipoint.addGeometry(new SxPoint(ad2[0], ad3[0]));
			}

			obj = sxmultipoint;
		}
		initialize();
		return ((com.ofx.geometry.SxPointInterface) (obj));
	}

	public com.ofx.geometry.SxPointInterface asTextGeometry()
	{
		partitionGeometry();
		verifySufficientPoints(1);
		java.lang.Object obj = null;
		int i = vCount.size();
		if(i == 1)
		{
			double ad[] = (double[])xPoints.get(0);
			double ad1[] = (double[])yPoints.get(0);
			obj = new SxText(SxText.coordArrayFromArrays(ad, ad1), "");
		} 
		else
		{
			return null;
		}
		initialize();
		return ((com.ofx.geometry.SxPointInterface) (obj));
	}

	public com.ofx.geometry.SxPolylineInterface asLineGeometry()
	{
		partitionGeometry();
		verifySufficientPoints(2);
		java.lang.Object obj = null;
		int i = vCount.size();
		if(i == 1)
		{
			double ad[] = (double[])xPoints.get(0);
			double ad1[] = (double[])yPoints.get(0);
			int k = ((java.lang.Integer)vCount.get(0)).intValue();
			obj = new SxPolyline(ad, ad1, k);
		} else
		{
			com.ofx.geometry.SxMultiPolyline sxmultipolyline = new SxMultiPolyline();
			for(int j = 0; j < i; j++)
			{
				double ad2[] = (double[])xPoints.get(j);
				double ad3[] = (double[])yPoints.get(j);
				int l = ((java.lang.Integer)vCount.get(j)).intValue();
				sxmultipolyline.addGeometry(new SxPolyline(ad2, ad3, l));
			}

			obj = sxmultipolyline;
		}
		initialize();
		return (com.ofx.geometry.SxPolylineInterface)splitGeometry(((com.ofx.geometry.SxGeometryInterface) (obj)));
	}

	public com.ofx.geometry.SxPolygonInterface asAreaGeometry()
	{
		partitionGeometry();
		verifySufficientPoints(3);
		java.lang.Object obj = null;
		int i = vCount.size();
		if(i == 1)
		{
			double ad[] = (double[])xPoints.get(0);
			double ad1[] = (double[])yPoints.get(0);
			int k = ((java.lang.Integer)vCount.get(0)).intValue();
			obj = new SxPolygon(ad, ad1, k);
		} else
		{
			com.ofx.geometry.SxMultiPolygon sxmultipolygon = new SxMultiPolygon();
			for(int j = 0; j < i; j++)
			{
				double ad2[] = (double[])xPoints.get(j);
				double ad3[] = (double[])yPoints.get(j);
				int l = ((java.lang.Integer)vCount.get(j)).intValue();
				sxmultipolygon.addGeometry(new SxPolygon(ad2, ad3, l));
			}

			obj = sxmultipolygon;
		}
		initialize();
		return (com.ofx.geometry.SxPolygonInterface)splitGeometry(((com.ofx.geometry.SxGeometryInterface) (obj)));
	}

	public com.ofx.geometry.SxPolygonInterface interpolatePolygon(com.ofx.geometry.SxPolygonInterface sxpolygoninterface, int i)
	{
		if(sxpolygoninterface == null || i <= 0)
			return sxpolygoninterface;
		Object obj = null;
		if(sxpolygoninterface.isCollection())
		{
			com.ofx.geometry.SxMultiPolygon sxmultipolygon = new SxMultiPolygon();
			java.util.Vector vector = ((com.ofx.geometry.SxGeometryCollection)sxpolygoninterface).getGeometry();
			double ad[];
			com.ofx.geometry.SxPolygon sxpolygon;
			int k;
			for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements(); sxmultipolygon.addGeometry(ad, k, sxpolygon.isHole()))
			{
				sxpolygon = (com.ofx.geometry.SxPolygon)enumeration.nextElement();
				ad = interpolatePoints(sxpolygon, i);
				k = ad.length >>> 1;
			}

			return sxmultipolygon;
		} else
		{
			double ad1[] = interpolatePoints((com.ofx.geometry.SxPolygon)sxpolygoninterface, i);
			int j = ad1.length >>> 1;
			return new SxPolygon(ad1, j);
		}
	}

	private double[] interpolatePoints(com.ofx.geometry.SxPolygon sxpolygon, int i)
	{
		double ad[] = sxpolygon.getXPoints();
		double ad1[] = sxpolygon.getYPoints();
		int j = sxpolygon.getNPoints();
		boolean flag = ad[0] == ad[j - 1] && ad1[0] == ad1[j - 1];
		int k = j + i * (flag ? j - 1 : j);
		double ad2[] = new double[k * 2];
		double ad3[][] = {
			ad, ad1
		};
		double d = 1.0D / (double)(i + 1);
		int l = flag ? j - 1 : j;
		k = 0;
		for(int i1 = 0; i1 < 2; i1++)
		{
			for(int j1 = 0; j1 < l; j1++)
			{
				int k1 = j1 + 1 >= j ? 0 : j1 + 1;
				double d1 = (ad3[i1][k1] - ad3[i1][j1]) * d;
				for(int l1 = 0; l1 <= i; l1++)
					ad2[k++] = ad3[i1][j1] + (double)l1 * d1;

			}

			if(flag)
				ad2[k++] = ad3[i1][j - 1];
		}

		return ad2;
	}

	public com.ofx.geometry.SxPolygonInterface createCircularPolygon(double d, double d1, double d2)
	{
		return createCircularPolygon(d, d1, d2, com.ofx.geometry.SxCircle.nInterPoints);
	}

	public com.ofx.geometry.SxPolygonInterface createCircularPolygon(double d, double d1, double d2, int i)
	{
		int j = i;
		ensureCapacity(j);
		com.ofx.geometry.SxPolygon sxpolygon = null;
		if(com.ofx.geometry.SxGeometry.domainDimension == 2)
		{
			int k = outProj != null ? outProj.getEllipsoid().getUnitId() : 203;
			d2 = com.ofx.base.SxDistance.convert(d2, distanceUnits, k);
			if(doProjection(d, d1))
			{
				d = projX[0];
				d1 = projY[0];
			}
			com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(d, d1);
			sxpolygon = (new SxCircle(sxdoublepoint, d2)).asPolygon(j);
		} else
		{
			char c = '\313';
			d2 = com.ofx.base.SxDistance.convert(d2, distanceUnits, c);
			if(unproject(d, d1))
			{
				d = projX[0];
				d1 = projY[0];
			}
			double d3 = 6.2831853071795862D / (double)j;
			double d4 = d * 0.017453292519943295D;
			double d5 = d1 * 0.017453292519943295D;
			double d6 = java.lang.Math.cos(d5);
			double d7 = java.lang.Math.sin(d5);
			double ad[] = new double[j];
			double ad1[] = new double[j];
			double d8 = d2 * 1.5696566533036565E-007D;
			double d9 = java.lang.Math.cos(d8);
			double d10 = java.lang.Math.sin(d8);
			for(int l = 0; l < j; l++)
			{
				double d11 = (double)l * d3;
				double d12 = java.lang.Math.cos(d11);
				double d13 = java.lang.Math.sin(d11);
				double d14 = java.lang.Math.asin(d7 * d9 + d6 * d10 * d12);
				double d15 = d4 + java.lang.Math.atan((d10 * d13) / (d6 * d9 - d7 * d10 * d12));
				ad[l] = d15 * 57.295779513082323D;
				ad1[l] = d14 * 57.295779513082323D;
			}

			if(project(ad, ad1, j))
			{
				ad = projX;
				ad1 = projY;
			}
			sxpolygon = new SxPolygon(ad, ad1, j);
		}
		return (com.ofx.geometry.SxPolygonInterface)splitGeometry(sxpolygon);
	}

	public com.ofx.geometry.SxPolygonInterface createEllipticalPolygon(double d, double d1, double d2, double d3, double d4)
	{
		return createEllipticalPolygon(d, d1, d2, d3, d4, com.ofx.geometry.SxCircle.nInterPoints);
	}

	public com.ofx.geometry.SxPolygonInterface createEllipticalPolygon(double d, double d1, double d2, double d3, double d4, int i)
	{
		int j = i;
		ensureCapacity(j);
		com.ofx.geometry.SxPolygon sxpolygon = null;
		if(com.ofx.geometry.SxGeometry.domainDimension == 2)
		{
			int k = outProj != null ? outProj.getEllipsoid().getUnitId() : 203;
			d2 = com.ofx.base.SxDistance.convert(d2, distanceUnits, k);
			d3 = com.ofx.base.SxDistance.convert(d3, distanceUnits, k);
			if(doProjection(d, d1))
			{
				d = projX[0];
				d1 = projY[0];
			}
			com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(d, d1);
			d4 = (90D - d4) * 0.017453292519943295D;
			sxpolygon = (new SxEllipse(sxdoublepoint, d2, d3, d4)).asPolygon(j);
		} else
		{
			char c = '\313';
			d2 = com.ofx.base.SxDistance.convert(d2, distanceUnits, c);
			d3 = com.ofx.base.SxDistance.convert(d3, distanceUnits, c);
			if(unproject(d, d1))
			{
				d = projX[0];
				d1 = projY[0];
			}
			d4 *= 0.017453292519943295D;
			double d5 = 6.2831853071795862D / (double)j;
			double d6 = d * 0.017453292519943295D;
			double d7 = d1 * 0.017453292519943295D;
			double d8 = java.lang.Math.cos(d7);
			double d9 = java.lang.Math.sin(d7);
			double ad[] = new double[j];
			double ad1[] = new double[j];
			for(int l = 0; l < j; l++)
			{
				double d10 = (double)l * d5;
				double d11 = java.lang.Math.cos(d10);
				double d12 = java.lang.Math.sin(d10);
				double d13 = d12 * d2;
				double d14 = d11 * d3;
				double d15 = (d2 * d3) / java.lang.Math.sqrt(d13 * d13 + d14 * d14);
				double d16 = d15 * 1.5696566533036565E-007D;
				d10 += d4;
				d11 = java.lang.Math.cos(d10);
				d12 = java.lang.Math.sin(d10);
				double d17 = java.lang.Math.cos(d16);
				double d18 = java.lang.Math.sin(d16);
				double d19 = java.lang.Math.asin(d9 * d17 + d8 * d18 * d11);
				double d20 = d6 + java.lang.Math.atan((d18 * d12) / (d8 * d17 - d9 * d18 * d11));
				ad[l] = d20 * 57.295779513082323D;
				ad1[l] = d19 * 57.295779513082323D;
			}

			if(project(ad, ad1, j))
			{
				ad = projX;
				ad1 = projY;
			}
			sxpolygon = new SxPolygon(ad, ad1, j);
		}
		return (com.ofx.geometry.SxPolygonInterface)splitGeometry(sxpolygon);
	}

	public com.ofx.geometry.SxPolygonInterface createRectangularPolygon(double d, double d1, double d2, double d3)
	{
		return createRectangularPolygon(d, d1, d2, d3, com.ofx.geometry.SxRectangle.nInterPoints);
	}

	public com.ofx.geometry.SxPolygonInterface createRectangularPolygon(double d, double d1, double d2, double d3, int i)
	{
		int j = i;
		int k = (j + 1) * 4;
		ensureCapacity(k);
		com.ofx.geometry.SxPolygon sxpolygon = null;
		double ad[] = {
			d, d2
		};
		double ad1[] = {
			d1, d3
		};
		if(com.ofx.geometry.SxGeometry.domainDimension == 2)
		{
			if(doProjection(ad, ad1, 2))
			{
				ad = projX;
				ad1 = projY;
			}
			sxpolygon = (new SxRectangle(ad[0], ad1[0], ad[1] - ad[0], ad1[1] - ad1[0])).asPolygon();
		} else
		{
			if(unproject(ad, ad1, 2))
			{
				ad = projX;
				ad1 = projY;
			}
			sxpolygon = (new SxRectangle(ad[0], ad1[0], ad[1] - ad[0], ad1[1] - ad1[0])).asPolygon(j);
			int l = sxpolygon.getNPoints();
			ad = sxpolygon.getXPoints();
			ad1 = sxpolygon.getYPoints();
			if(project(ad, ad1, l))
				sxpolygon = new SxPolygon(projX, projY, l);
		}
		return (com.ofx.geometry.SxPolygonInterface)splitGeometry(sxpolygon);
	}

	private static final int MOD = 100;
	private static final double RADIANS_PER_METER = 1.5696566533036565E-007D;
	com.ofx.projection.SxProjectionInterface inProj;
	com.ofx.projection.SxProjectionInterface outProj;
	int distanceUnits;
	double projX[];
	double projY[];
	java.util.ArrayList xPoints;
	java.util.ArrayList yPoints;
	java.util.ArrayList vCount;
	int geom;
	int count;
	int capacity;
}
