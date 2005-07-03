// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.component;

import com.ofx.base.SxDistance;
import com.ofx.base.SxEnvironment;
import com.ofx.base.SxEvent;
import com.ofx.base.SxLogInterface;
import com.ofx.base.SxStateChangeListener;
import com.ofx.geometry.SxCircle;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxEllipse;
import com.ofx.geometry.SxGeometryFactory;
import com.ofx.geometry.SxGeometryInterface;
import com.ofx.geometry.SxPoint;
import com.ofx.geometry.SxPolygon;
import com.ofx.geometry.SxPolyline;
import com.ofx.geometry.SxRectangle;
import com.ofx.mapViewer.SxInvalidNameException;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.repository.SxSpatialObject;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.TextArea;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

// Referenced classes of package com.ofx.component:
//			SxBorderPanel, MapExceptionRegistry, MapEvent, MapHighlightBin, 
//			MapResultSet, MapListenerSupport, MapAutoConnect, MapExceptionCallback, 
//			MapListener

public class MapViewer extends java.awt.Container
	implements com.ofx.base.SxStateChangeListener, java.io.Serializable
{

	public MapViewer()
	{
		this(((java.awt.Container) (new SxBorderPanel())));
	}

	protected MapViewer(java.awt.Container container)
	{
		mer = new MapExceptionRegistry(this);
		setLayout(new BorderLayout());
		childContainer = container;
		add(childContainer, "Center");
		childContainer.setLayout(new BorderLayout());
		childContainer.add(getMapCanvas(), "Center");
		processInterestedParties();
	}

	public void paint()
	{
		try
		{
			getSxMapViewer().paint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void setBounds(int i, int j, int k, int l)
	{
		super.setBounds(i, j, k, l);
		childContainer.setBounds(0, 0, k, l);
	}

	public void display()
	{
		try
		{
			getSxMapViewer().paint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public boolean isVisible(java.lang.String s)
	{
		try
		{
			return getSxMapViewer().isVisible(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return false;
	}

	public boolean isLabelVisible(java.lang.String s)
	{
		try
		{
			return getSxMapViewer().isLabelVisible(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return false;
	}

	public void registerCallback(java.lang.String s, com.ofx.component.MapExceptionCallback mapexceptioncallback)
	{
		mer.registerCallback(s, mapexceptioncallback);
	}

	public void setSymbologyVisibility(java.lang.String s, boolean flag)
	{
		try
		{
			getSxMapViewer().setSymbologyVisibility(s, flag);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void setLabelVisibility(java.lang.String s, boolean flag)
	{
		try
		{
			getSxMapViewer().setLabelVisibility(s, flag);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public static java.awt.Component mapViewerFor(java.awt.Component component)
	{
		java.awt.Component component1 = null;
		try
		{
			component1 = com.ofx.component.MapViewer.findMapViewer(com.ofx.component.MapViewer.parentOf(component));
		}
		catch(java.lang.Exception exception) { }
		if(component1 == null && !com.ofx.component.MapViewer.interestedParties.contains(component))
			com.ofx.component.MapViewer.interestedParties.addElement(component);
		return component1;
	}

	public void setLayout(java.awt.LayoutManager layoutmanager)
	{
	}

	public void stateChanged(com.ofx.base.SxEvent sxevent)
	{
		try
		{
			if(com.ofx.component.MapViewer.interestedParties.size() > 0)
				processInterestedParties();
			if(sxevent.getState() == 107 || sxevent.getState() == 109 || sxevent.getState() == 116 || sxevent.getState() == 117)
				getMLS().fireMapResult(new MapEvent(this, sxevent));
			getMLS().fireMapAction(new MapEvent(this, sxevent));
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public java.util.Vector getHighlightBinNames()
	{
		return getSxMapViewer().getHighlightBinNames();
	}

	public double[] getCenter()
	{
		try
		{
			return getSxMapViewer().getCenter();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void setCenter(double d, double d1)
	{
		try
		{
			getSxMapViewer().setCenter(d, d1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public java.lang.String getDBNameDefault()
	{
		try
		{
			return getSxMapViewer().getDBNameDefault();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.lang.String getDBName()
	{
		return dbName;
	}

	public void openSession(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		try
		{
			getSxMapViewer().openSession(null, s, s1, s2);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void release()
	{
		getSxMapViewer().release();
	}

	public void closeSession()
	{
		try
		{
			getSxMapViewer().closeSession();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomReset()
	{
		try
		{
			getSxMapViewer().zoomReset();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void setDBName(java.lang.String s)
		throws com.ofx.mapViewer.SxInvalidNameException
	{
		try
		{
			if(s != null && s.length() != 0)
			{
				dbName = s;
				getSxMapViewer().setDBName(s);
				if(mapName != null)
				{
					setMapName(mapName);
					mapName = null;
				}
			}
		}
		catch(com.ofx.mapViewer.SxInvalidNameException sxinvalidnameexception)
		{
			dbName = null;
			mer.processException(sxinvalidnameexception);
			throw sxinvalidnameexception;
		}
	}

	public java.lang.String getDBUsername()
	{
		try
		{
			return getSxMapViewer().getDBUsername();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void setDBUsername(java.lang.String s)
	{
		try
		{
			getSxMapViewer().setDBUsername(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void setDBPassword(java.lang.String s)
	{
		try
		{
			getSxMapViewer().setDBPassword(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public java.lang.String getMapName()
	{
		try
		{
			return getSxMapViewer().getMapName();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void setMapName(java.lang.String s)
		throws com.ofx.mapViewer.SxInvalidNameException
	{
		try
		{
			if(s != null && s.length() != 0)
				getSxMapViewer().setMapName(s);
		}
		catch(com.ofx.mapViewer.SxInvalidNameException sxinvalidnameexception)
		{
			mer.processException(sxinvalidnameexception);
			throw sxinvalidnameexception;
		}
	}

	public int getPanPercentage()
	{
		try
		{
			return getSxMapViewer().getPanPercentage();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return 0;
	}

	public void setPanPercentage(int i)
	{
		try
		{
			getSxMapViewer().setPanPercentage(i);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public int getSelectionTolerance()
	{
		try
		{
			return getSxMapViewer().getSelectionTolerance();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return 0;
	}

	public void setSelectionTolerance(int i)
	{
		try
		{
			getSxMapViewer().setSelectionTolerance(i);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public int getDistanceUnits()
	{
		return getSxMapViewer().getUnits();
	}

	public void setDistanceUnits(int i)
	{
		getSxMapViewer().setUnits(i);
	}

	public double getZoomFactor()
	{
		try
		{
			return getSxMapViewer().getZoomFactor();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return 0.0D;
	}

	public void setZoomFactor(double d)
	{
		try
		{
			getSxMapViewer().setZoomFactor(d);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapHighlightBin createBin(java.lang.String s)
	{
		try
		{
			return new MapHighlightBin(getSxMapViewer().createBin(s));
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void deleteBin(java.lang.String s)
	{
		try
		{
			getSxMapViewer().deleteBin(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapHighlightBin getBin(java.lang.String s)
	{
		try
		{
			com.ofx.mapViewer.SxHighlightBin sxhighlightbin = getSxMapViewer().getBin(s);
			return sxhighlightbin != null ? new MapHighlightBin(sxhighlightbin) : null;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector getAvailableMaps()
	{
		try
		{
			return getSxMapViewer().getAvailableMaps();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector getForegroundClasses()
	{
		try
		{
			return getSxMapViewer().getForegroundClasses();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector getBackgroundClasses()
	{
		try
		{
			return getSxMapViewer().getBackgroundClasses();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public double getScale()
	{
		try
		{
			return getSxMapViewer().getScale();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return 0.0D;
	}

	public double getViewWidth()
	{
		double d;
		try
		{
			d = getSxMapViewer().getViewWidth();
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().debug(exception);
			return -1D;
		}
		return d;
	}

	public void setGeographicPanning(boolean flag)
	{
		try
		{
			getSxMapViewer().setGeographicPanning(flag);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public boolean getGeographicPanning()
	{
		boolean flag = false;
		try
		{
			flag = getSxMapViewer().getGeographicPanning();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return flag;
	}

	public void panNorth()
	{
		try
		{
			getSxMapViewer().panNorth();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panNorthEast()
	{
		try
		{
			getSxMapViewer().panNorthEast();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panEast()
	{
		try
		{
			getSxMapViewer().panEast();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panSouthEast()
	{
		try
		{
			getSxMapViewer().panSouthEast();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panSouth()
	{
		try
		{
			getSxMapViewer().panSouth();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panSouthWest()
	{
		try
		{
			getSxMapViewer().panSouthWest();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panWest()
	{
		try
		{
			getSxMapViewer().panWest();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panNorthWest()
	{
		try
		{
			getSxMapViewer().panNorthWest();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panHand()
	{
		try
		{
			getSxMapViewer().panHand();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panToPointNoArgs()
	{
		panToPoint();
	}

	public void panToPoint()
	{
		try
		{
			getSxMapViewer().panToPoint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panToPoint(double d, double d1)
	{
		try
		{
			getSxMapViewer().panToPoint(d, d1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void panToObject(java.lang.String s, java.lang.String s1)
	{
		try
		{
			getSxMapViewer().panToObject(s, s1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomIn()
	{
		try
		{
			getSxMapViewer().zoomIn();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomOut()
	{
		try
		{
			getSxMapViewer().zoomOut();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomToBox()
	{
		try
		{
			getSxMapViewer().zoomToRect();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomToBox(double d, double d1, double d2, double d3)
	{
		try
		{
			getSxMapViewer().zoomToRect(d, d1, d2, d3);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomToPoint()
	{
		try
		{
			getSxMapViewer().zoomToPoint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void zoomToPoint(double d, double d1)
	{
		try
		{
			getSxMapViewer().zoomToPoint(d, d1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet find(java.lang.String s, java.lang.String s1)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().getObjectWithId(s, s1));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void findAtPoint(java.lang.String s)
	{
		try
		{
			getSxMapViewer().findAtPoint(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet findAtPoint(java.lang.String s, double d, double d1)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findAtPoint(s, d, d1));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void findAllAtPoint(java.lang.String s)
	{
		try
		{
			getSxMapViewer().findAllAtPoint(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet findAllAtPoint(java.lang.String s, double d, double d1)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findAllAtPoint(s, d, d1));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.component.MapResultSet findInEllipse(java.lang.String s, double d, double d1, double d2, 
			double d3, double d4)
	{
		try
		{
			com.ofx.base.SxDistance sxdistance = new SxDistance(d2, getSxMapViewer().getUnits());
			com.ofx.base.SxDistance sxdistance1 = new SxDistance(d3, getSxMapViewer().getUnits());
			com.ofx.geometry.SxEllipse sxellipse = new SxEllipse(convertToGroundCoord(d, d1), sxdistance.convertTo(203), sxdistance1.convertTo(203), (90D - d4) * 0.017453292519943295D);
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findInGeometry(s, sxellipse));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void findInBox(java.lang.String s)
	{
		try
		{
			getSxMapViewer().findInRect(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet findInBox(java.lang.String s, double d, double d1, double d2, 
			double d3)
	{
		try
		{
			com.ofx.geometry.SxRectangle sxrectangle = new SxRectangle(convertToGroundCoord(d, d1), convertToGroundCoord(d2, d3));
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findInGeometry(s, sxrectangle));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.component.MapResultSet findInPointBuffer(java.lang.String s, double d, java.lang.String s1, java.lang.String s2)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findInPointBuffer(s, d, s1, s2));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void findInCircle(java.lang.String s)
	{
		try
		{
			getSxMapViewer().findInCircle(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet findInCircle(java.lang.String s, double d, double d1, double d2)
	{
		try
		{
			com.ofx.base.SxDistance sxdistance = new SxDistance(d, getSxMapViewer().getUnits());
			com.ofx.geometry.SxCircle sxcircle = new SxCircle(convertToGroundCoord(d1, d2), sxdistance.convertTo(203));
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findInGeometry(s, sxcircle));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void findInArea(java.lang.String s)
	{
		try
		{
			getSxMapViewer().findInRegion(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.component.MapResultSet findInAreaObject(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findWithinObject(s, s1, s2));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.component.MapResultSet findInGeometry(java.lang.String s, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		try
		{
			com.ofx.component.MapResultSet mapresultset = new MapResultSet(getSxMapViewer().findInGeometry(s, sxgeometryinterface));
			mapresultset.setProjection(getSxMapViewer().getDBProjection());
			return mapresultset;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void selectAtPoint()
	{
		try
		{
			getSxMapViewer().selectAtPoint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectAllAtPoint()
	{
		try
		{
			getSxMapViewer().selectAllAtPoint();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectGroupInBox(java.util.Vector vector)
	{
		try
		{
			getSxMapViewer().selectInRect(vector);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectGroupInCircle(java.util.Vector vector)
	{
		try
		{
			getSxMapViewer().selectInCircle(vector);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectGroupInArea(java.util.Vector vector)
	{
		try
		{
			getSxMapViewer().selectInRegion(vector);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectInBox(java.lang.String s)
	{
		try
		{
			getSxMapViewer().selectInRect(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectInCircle(java.lang.String s)
	{
		try
		{
			getSxMapViewer().selectInCircle(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectInArea(java.lang.String s)
	{
		try
		{
			getSxMapViewer().selectInRegion(s);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void selectInAreaObject(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		try
		{
			getSxMapViewer().selectWithinObject(s, s1, s2);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void clear()
	{
		try
		{
			getSxMapViewer().clear();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void clearSelection()
	{
		try
		{
			getBin("SELECTION").clear();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void clearMarker()
	{
		try
		{
			getBin("MARKER").clear();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.geometry.SxGeometryFactory getGeometryFactory(com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		try
		{
			return getSxMapViewer().getGeometryFactory(sxprojectioninterface);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createSpatialObject(java.lang.String s, java.lang.String s1, java.lang.String s2, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		try
		{
			return getSxMapViewer().createSpatialObject(s, s1, s2, sxgeometryinterface);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createSpatialObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], com.ofx.geometry.SxGeometryInterface asxgeometryinterface[])
	{
		try
		{
			return getSxMapViewer().createSpatialObjects(s, as, as1, asxgeometryinterface);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createPointObject(java.lang.String s, java.lang.String s1, java.lang.String s2, double d, double d1, 
			boolean flag)
	{
		try
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = convertToGroundCoord(d, d1);
			com.ofx.geometry.SxPoint sxpoint = new SxPoint(sxdoublepoint);
			com.ofx.repository.SxSpatialObject sxspatialobject = getSxMapViewer().createSpatialObject(s, s1, s2, sxpoint);
			if(flag)
				paint();
			return sxspatialobject;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createPointObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], double ad[], double ad1[], boolean flag)
	{
		try
		{
			java.util.Vector vector = new Vector(as.length);
			for(int i = 0; i < as.length; i++)
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = createPointObject(s, as[i], as1[i], ad[i], ad1[i], false);
				if(sxspatialobject != null)
					vector.addElement(sxspatialobject);
			}

			if(flag)
				paint();
			return vector;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createTextObject(java.lang.String s, java.lang.String s1, java.lang.String s2, double ad[], double ad1[], boolean flag)
	{
		try
		{
			int i = ad.length;
			if(ad1.length != i)
				throw new RuntimeException("Inconsistent length between longitude and latitude");
			double ad2[] = new double[i];
			double ad3[] = new double[i];
			try
			{
				getSxMapViewer().getDBProjection().project(ad, ad1, ad2, ad3, i);
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception)
			{
				return null;
			}
			com.ofx.geometry.SxText sxtext = new com.ofx.geometry.SxText(ad2, ad3, s1);
			com.ofx.repository.SxSpatialObject sxspatialobject = getSxMapViewer().createSpatialObject(s, s1, s2, sxtext);
			if(flag)
				paint();
			return sxspatialobject;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createTextObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], double ad[][], double ad1[][], boolean flag)
	{
		try
		{
			java.util.Vector vector = new Vector(as.length);
			for(int i = 0; i < as.length; i++)
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = createTextObject(s, as[i], as1[i], ad[i], ad1[i], false);
				if(sxspatialobject != null)
					vector.addElement(sxspatialobject);
			}

			if(flag)
				paint();
			return vector;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createLinearObject(java.lang.String s, java.lang.String s1, java.lang.String s2, double ad[], double ad1[], boolean flag)
	{
		try
		{
			int i = ad.length;
			if(ad1.length != i)
				throw new RuntimeException("Inconsistent length between longitude and latitude");
			double ad2[] = new double[i];
			double ad3[] = new double[i];
			try
			{
				getSxMapViewer().getDBProjection().project(ad, ad1, ad2, ad3, i);
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception)
			{
				return null;
			}
			com.ofx.geometry.SxPolyline sxpolyline = new SxPolyline(ad2, ad3, i);
			com.ofx.repository.SxSpatialObject sxspatialobject = getSxMapViewer().createSpatialObject(s, s1, s2, sxpolyline);
			if(flag)
				paint();
			return sxspatialobject;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createLinearObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], double ad[][], double ad1[][], boolean flag)
	{
		try
		{
			java.util.Vector vector = new Vector(as.length);
			for(int i = 0; i < as.length; i++)
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = createLinearObject(s, as[i], as1[i], ad[i], ad1[i], false);
				if(sxspatialobject != null)
					vector.addElement(sxspatialobject);
			}

			if(flag)
				paint();
			return vector;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createAreaObject(java.lang.String s, java.lang.String s1, java.lang.String s2, double ad[], double ad1[], boolean flag)
	{
		try
		{
			int i = ad.length;
			if(ad1.length != i)
				throw new RuntimeException("Inconsistent length between longitude and latitude");
			double ad2[] = new double[i];
			double ad3[] = new double[i];
			try
			{
				getSxMapViewer().getDBProjection().project(ad, ad1, ad2, ad3, i);
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception)
			{
				return null;
			}
			com.ofx.geometry.SxPolygon sxpolygon = new SxPolygon(ad2, ad3, i);
			sxpolygon.closePolygon();
			com.ofx.repository.SxSpatialObject sxspatialobject = getSxMapViewer().createSpatialObject(s, s1, s2, sxpolygon);
			if(flag)
				paint();
			return sxspatialobject;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createAreaObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], double ad[][], double ad1[][], boolean flag)
	{
		try
		{
			java.util.Vector vector = new Vector(as.length);
			for(int i = 0; i < as.length; i++)
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = createAreaObject(s, as[i], as1[i], ad[i], ad1[i], false);
				if(sxspatialobject != null)
					vector.addElement(sxspatialobject);
			}

			if(flag)
				paint();
			return vector;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.repository.SxSpatialObject createEllipseObject(java.lang.String s, java.lang.String s1, java.lang.String s2, double d, double d1, 
			double d2, double d3, double d4, boolean flag)
	{
		try
		{
			com.ofx.base.SxDistance sxdistance = new SxDistance(d2, getSxMapViewer().getUnits());
			com.ofx.base.SxDistance sxdistance1 = new SxDistance(d3, getSxMapViewer().getUnits());
			com.ofx.geometry.SxEllipse sxellipse = new SxEllipse(convertToGroundCoord(d, d1), sxdistance.convertTo(203), sxdistance1.convertTo(203), (90D - d4) * 0.017453292519943295D);
			com.ofx.repository.SxSpatialObject sxspatialobject = getSxMapViewer().createSpatialObject(s, s1, s2, sxellipse);
			if(flag)
				paint();
			return sxspatialobject;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public java.util.Vector createEllipseObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], double ad[], double ad1[], double ad2[], double ad3[], 
			double ad4[], boolean flag)
	{
		try
		{
			java.util.Vector vector = new Vector(as.length);
			for(int i = 0; i < as.length; i++)
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = createEllipseObject(s, as[i], as1[i], ad[i], ad1[i], ad2[i], ad3[i], ad4[i], false);
				if(sxspatialobject != null)
					vector.addElement(sxspatialobject);
			}

			if(flag)
				paint();
			return vector;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public void modifyLabel(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		try
		{
			getSxMapViewer().modifyLabel(s, s1, s2);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void movePointObject(java.lang.String s, java.lang.String s1, double d, double d1)
	{
		try
		{
			getSxMapViewer().movePointObject(s, s1, d, d1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void movePointObjects(java.lang.String as[], java.lang.String as1[], double ad[], double ad1[])
	{
		try
		{
			getSxMapViewer().movePointObjects(as, as1, ad, ad1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void deleteMapObject(java.lang.String s, java.lang.String s1)
	{
		try
		{
			getSxMapViewer().deleteMapObject(s, s1);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void deleteMapObjects(java.lang.String s, java.lang.String as[])
	{
		try
		{
			getSxMapViewer().deleteMapObjects(s, as);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public com.ofx.geometry.SxDoublePoint convertToLatLong(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		try
		{
			return getSxMapViewer().convertDBToLatLong(sxdoublepoint);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		throw new RuntimeException("ERROR");
	}

	public com.ofx.geometry.SxDoublePoint convertToGroundCoord(double d, double d1)
	{
		try
		{
			return getSxMapViewer().convertLatLongToDB(d1, d);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		throw new RuntimeException("ERROR");
	}

	public com.ofx.geometry.SxDoublePoint convertToGroundCoord(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		try
		{
			return getSxMapViewer().convertLatLongToDB(sxdoublepoint);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		throw new RuntimeException("ERROR");
	}

	public void addMapListener(com.ofx.component.MapListener maplistener)
	{
		try
		{
			getMLS().addMapListener(maplistener);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public void removeMapListener(com.ofx.component.MapListener maplistener)
	{
		try
		{
			getMLS().removeMapListener(maplistener);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
	}

	public java.awt.Dimension getMinimumSize()
	{
		try
		{
			return new Dimension(200, 200);
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	private static java.awt.Frame parentOf(java.awt.Component component)
	{
		if(component instanceof java.awt.Frame)
			return (java.awt.Frame)component;
		else
			return com.ofx.component.MapViewer.parentOf(((java.awt.Component) (component.getParent())));
	}

	public void printMap()
	{
		getSxMapViewer().printMap();
	}

	public com.ofx.component.MapViewer getMapViewer()
	{
		try
		{
			return this;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	public com.ofx.mapViewer.SxMapViewer getSxMapViewer()
	{
		try
		{
			if(mapViewer == null)
			{
				mapViewer = new SxMapViewer();
				if(mapViewer != null)
					mapViewer.addStateChangeListener(this);
			}
			return mapViewer;
		}
		catch(java.lang.Exception exception)
		{
			failure = exception.toString();
			mer.processException(exception);
			return null;
		}
	}

	public double convertUnits(double d, int i, int j)
	{
		com.ofx.base.SxDistance sxdistance = new SxDistance(d, i);
		return sxdistance.convertTo(j);
	}

	public java.awt.Component getMapCanvas()
	{
		try
		{
			com.ofx.mapViewer.SxMapViewer sxmapviewer = getSxMapViewer();
			if(sxmapviewer == null)
				return (java.awt.Component )new TextArea(failure == null ? "Server failure" : failure); 
			return (java.awt.Component) sxmapviewer.getMapCanvas();
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	private com.ofx.component.MapListenerSupport getMLS()
	{
		try
		{
			if(mls == null)
				mls = new MapListenerSupport(this);
			return mls;
		}
		catch(java.lang.Exception exception)
		{
			mer.processException(exception);
		}
		return null;
	}

	private java.util.Vector targets()
	{
		java.util.Vector vector;
		synchronized(this)
		{
			vector = (java.util.Vector)com.ofx.component.MapViewer.interestedParties.clone();
			com.ofx.component.MapViewer.interestedParties.removeAllElements();
		}
		return vector;
	}

	protected void processInterestedParties()
	{
		if(getSxMapViewer() != null)
		{
			java.util.Vector vector = targets();
			for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
			{
				com.ofx.component.MapAutoConnect mapautoconnect = (com.ofx.component.MapAutoConnect)enumeration.nextElement();
				if(mapautoconnect.isAutoConnect())
					mapautoconnect.setMapViewer(this);
			}

		}
	}

	private static java.awt.Component findMapViewer(java.awt.Container container)
	{
		java.awt.Component acomponent[] = container.getComponents();
		Object obj = null;
		for(int i = 0; i < acomponent.length; i++)
		{
			java.awt.Component component = acomponent[i];
			if(component instanceof com.ofx.component.MapViewer)
				return component;
			if(component instanceof java.awt.Container)
			{
				java.awt.Component component1 = com.ofx.component.MapViewer.findMapViewer((java.awt.Container)component);
				if(component1 != null)
					return component1;
			}
		}

		return null;
	}

	public static final int FEET = 201;
	public static final int MILES = 202;
	public static final int NAUTICAL_MILES = 205;
	public static final int METERS = 203;
	public static final int KILOMETERS = 204;
	public static final java.lang.String SELECTION = "SELECTION";
	public static final java.lang.String MARKER = "MARKER";
	private static java.util.Vector interestedParties = new Vector();
	private java.lang.String mapName;
	private java.lang.String dbName;
	private com.ofx.mapViewer.SxMapViewer mapViewer;
	private com.ofx.component.MapListenerSupport mls;
	private com.ofx.component.MapExceptionRegistry mer;
	protected java.awt.Container childContainer;
	private java.lang.String failure;

}
