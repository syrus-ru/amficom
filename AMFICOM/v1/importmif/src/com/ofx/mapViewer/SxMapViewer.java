// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.mapViewer;

import com.ofx.base.SxBaseProperties;
import com.ofx.base.SxDistance;
import com.ofx.base.SxEnvironment;
import com.ofx.base.SxEvent;
import com.ofx.base.SxFactory;
import com.ofx.base.SxImageConverterInterface;
import com.ofx.base.SxLogInterface;
import com.ofx.base.SxProperties;
import com.ofx.base.SxStateChangeListener;
import com.ofx.base.SxStateChangeSupport;
import com.ofx.collections.SxLexicographicAscendingStringComparator;
import com.ofx.collections.SxSortInterface;
import com.ofx.geometry.SxCircle;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxGeometry;
import com.ofx.geometry.SxGeometryCollection;
import com.ofx.geometry.SxGeometryFactory;
import com.ofx.geometry.SxGeometryInterface;
import com.ofx.geometry.SxPoint;
import com.ofx.geometry.SxPolygon;
import com.ofx.geometry.SxRectangle;
import com.ofx.printing.SxMapPrinter;
import com.ofx.printing.SxMapPrinterInterface;
import com.ofx.projection.SxProjectionEllipsoid;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.query.SxQueriableObjectInterface;
import com.ofx.query.SxQueryAdministrationInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import com.ofx.query.SxQuerySessionInterface;
import com.ofx.query.SxQueryTransactionInterface;
import com.ofx.query.SxTransientObjectManager;
import com.ofx.query.evaluation.SxQueryResultOverHashtable;
import com.ofx.query.evaluation.SxQueryResultOverVector;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxMap;
import com.ofx.repository.SxRepositoryException;
import com.ofx.repository.SxSpatialObject;
import com.ofx.repository.SxSymbology;
import com.ofx.repository.SxTextSpec;
import com.ofx.service.SxServiceDefinition;
import com.ofx.service.SxServiceException;
import com.ofx.service.SxServiceFactory;
import com.ofx.service.SxServiceReferenceNotFoundException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package com.ofx.mapViewer:
//			SxBinLayerRegistry, SxInvalidNameException, SxMapLayerEventQueue, SxBinLayer, 
//			SxBinLabelLayer, SxMarkerLayer, SxMapLayer, SxHighlightBin, 
//			SxMapCanvas, SxColorBackgroundLayer, SxMapLayerException, SxMapLayerRegistry, 
//			SxMapLayerInterface

public class SxMapViewer
	implements com.ofx.base.SxStateChangeListener, java.io.Serializable
{
	class MapPrintable
		implements java.awt.print.Printable
	{

		public int print(java.awt.Graphics g, java.awt.print.PageFormat pageformat, int i)
		{
			if(i != 0)
			{
				return 1;
			} else
			{
				java.awt.Graphics2D graphics2d = (java.awt.Graphics2D)g;
				graphics2d.translate(pageformat.getImageableX(), pageformat.getImageableY());
				getMapCanvas().print(graphics2d);
				return 0;
			}
		}

		MapPrintable()
		{
		}
	}


	public SxMapViewer()
	{
		panPercentage = 50;
		geographicPanning = false;
		units = 203;
		zoomFactor = 2D;
		selectionTolerance = env().getProperties().getInteger("ofx.selectionTolerance", new Integer(1)).intValue();
		allowShiftSelect = true;
		isImageServer = false;
		logProjectionExceptions = false;
		dbProjection = null;
		logProjectionExceptions = properties().getBoolean("ofx.logProjectionExceptions", new Boolean(false)).booleanValue();
	}

	public com.ofx.mapViewer.SxMapLayerRegistry getMapLayerRegistry()
	{
		if(mapLayerRegistry == null && getMap() != null)
			mapLayerRegistry = getMap().buildMapLayerRegistry(this);
		return mapLayerRegistry;
	}

	public com.ofx.mapViewer.SxBinLayerRegistry getBinLayerRegistry()
	{
		if(binLayerRegistry == null)
			try
			{
				binLayerRegistry = new SxBinLayerRegistry(this);
				if(getMapLayerRegistry().includesLayer("SELECTION"))
					getMapLayerRegistry().addLayerBelow("SELECTION", "BINSLAYER", binLayerRegistry);
				else
					getMapLayerRegistry().addLayerBelow("MARKER", "BINSLAYER", binLayerRegistry);
			}
			catch(java.lang.Exception exception)
			{
				log(exception);
			}
		return binLayerRegistry;
	}

	public com.ofx.mapViewer.SxMapLayerInterface getLayer(java.lang.String s)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		return getMapLayerRegistry().getLayer(s);
	}

	public com.ofx.mapViewer.SxMapLayerInterface getNamedLayer(java.lang.String s)
	{
		return getMapLayerRegistry().getNamedLayer(s);
	}

	public void removeLayer(java.lang.String s)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		getMapLayerRegistry().removeLayer(s);
	}

	public void removeNamedLayer(java.lang.String s)
	{
		getMapLayerRegistry().removeNamedLayer(s);
	}

	public void setLayer(java.lang.String s, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		getMapLayerRegistry().setLayer(s, sxmaplayerinterface);
	}

	public void addLayer(java.lang.String s, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		getMapLayerRegistry().addLayer(s, sxmaplayerinterface);
	}

	public void addLayerBelow(java.lang.String s, java.lang.String s1, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		getMapLayerRegistry().addLayerBelow(s, s1, sxmaplayerinterface);
	}

	public void addLayerAbove(java.lang.String s, java.lang.String s1, com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		getMapLayerRegistry().addLayerAbove(s, s1, sxmaplayerinterface);
	}

	public synchronized void moveLayerAbove(java.lang.String s, java.lang.String s1)
	{
		getMapLayerRegistry().moveLayerAbove(s, s1);
	}

	public synchronized void moveLayerBelow(java.lang.String s, java.lang.String s1)
	{
		getMapLayerRegistry().moveLayerBelow(s, s1);
	}

	public void paint()
	{
		if(getMapLayerRegistry() != null)
		{
			getMapLayerRegistry().postAllDirtyEvent();
			getMapLayerRegistry().postPaintEvent();
		}
	}

	protected void postUpdateAllEvent()
	{
		fireExternalEvent(this, 104, 218);
	}

	public void postPaintEvent()
	{
		if(getMapLayerRegistry() != null)
			getMapLayerRegistry().postPaintEvent();
	}

	public boolean allowShiftSelect()
	{
		return allowShiftSelect;
	}

	public void allowShiftSelect(boolean flag)
	{
		allowShiftSelect = flag;
	}

	public boolean isLabelVisible(java.lang.String s)
	{
		com.ofx.repository.SxClass sxclass = getSxClass(s);
		return isLabelVisible(sxclass);
	}

	public boolean isLabelVisible(com.ofx.repository.SxClass sxclass)
	{
		try
		{
			return isLabelVisible(sxclass, sxclass.getLabelSpec());
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return false;
	}

	public boolean isLabelVisible(com.ofx.repository.SxClass sxclass, com.ofx.repository.SxTextSpec sxtextspec)
	{
		try
		{
			if(sxtextspec == null)
				return false;
			if(!sxtextspec.getIsDisplayable())
			{
				return false;
			} else
			{
				double d = getMapCanvas().getScale();
				return sxclass.getTextMinScale() <= d && sxclass.getTextMaxScale() >= d;
			}
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return false;
	}

	public boolean isLabelVisible(java.lang.String s, com.ofx.repository.SxTextSpec sxtextspec)
	{
		com.ofx.repository.SxClass sxclass = getSxClass(s);
		return isLabelVisible(sxclass, sxtextspec);
	}

	public boolean isVisible(java.lang.String s)
	{
		com.ofx.repository.SxClass sxclass = getSxClass(s);
		if(sxclass != null)
			return isVisible(sxclass, sxclass.getSymbology());
		else
			return false;
	}

	public boolean isVisible(com.ofx.repository.SxClass sxclass)
	{
		return isVisible(sxclass, sxclass.getSymbology());
	}

	public com.ofx.repository.SxClass getSxClass(java.lang.String s)
	{
		com.ofx.repository.SxClass sxclass = (com.ofx.repository.SxClass)getQuery().retrieve("OfxCSxClass", s);
		return sxclass;
	}

	public com.ofx.repository.SxMap getSxMap(java.lang.String s)
	{
		com.ofx.repository.SxMap sxmap = (com.ofx.repository.SxMap)getQuery().retrieve("OfxCSxMap", s);
		return sxmap;
	}

	public boolean isVisible(java.lang.String s, com.ofx.repository.SxSymbology sxsymbology)
	{
		com.ofx.repository.SxClass sxclass = getSxClass(s);
		return isVisible(sxclass, sxsymbology);
	}

	public boolean isVisible(com.ofx.repository.SxClass sxclass, com.ofx.repository.SxSymbology sxsymbology)
	{
		if(!sxsymbology.getIsDisplayable())
		{
			return false;
		} else
		{
			double d = getMapCanvas().getScale();
			return sxclass.getSymbolMinScale() <= d && sxclass.getSymbolMaxScale() >= d;
		}
	}

	public void addStateChangeListener(com.ofx.base.SxStateChangeListener sxstatechangelistener)
	{
		getExternalListeners().addStateChangeListener(sxstatechangelistener);
	}

	public void removeStateChangeListener(com.ofx.base.SxStateChangeListener sxstatechangelistener)
	{
		getExternalListeners().removeStateChangeListener(sxstatechangelistener);
	}

	public void addInternalStateChangeListener(com.ofx.base.SxStateChangeListener sxstatechangelistener)
	{
		getInternalListeners().addStateChangeListener(sxstatechangelistener);
	}

	public void removeInternalStateChangeListener(com.ofx.base.SxStateChangeListener sxstatechangelistener)
	{
		getInternalListeners().removeStateChangeListener(sxstatechangelistener);
	}

	private com.ofx.base.SxEnvironment env()
	{
		return com.ofx.base.SxEnvironment.singleton();
	}

	public com.ofx.base.SxProperties properties()
	{
		return env().getProperties();
	}

	public void setSymbologyVisibility(java.lang.String s, boolean flag)
	{
		try
		{
			com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface = getNamedLayer(s);
			if(sxmaplayerinterface != null)
				sxmaplayerinterface.setEnabled(flag);
			paint();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
	}

	public void setLabelVisibility(java.lang.String s, boolean flag)
	{
		try
		{
			com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface = getNamedLayer(s + "LABELS");
			if(sxmaplayerinterface != null)
				sxmaplayerinterface.setEnabled(flag);
			paint();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
	}

	public void fireExternalEvent(java.lang.Object obj, int i, int j)
	{
		fireExternalEvent(new SxEvent(obj, i, j));
	}

	public void fireExternalEvent(com.ofx.base.SxEvent sxevent)
	{
		getExternalListeners().fireStateChange(sxevent);
	}

	public void fireInternalEvent(java.lang.Object obj, int i, int j)
	{
		fireInternalEvent(new SxEvent(obj, i, j));
	}

	protected void fireInternalEvent(java.lang.Object obj, int i, int j, java.lang.String s)
	{
		com.ofx.base.SxEvent sxevent = new SxEvent(obj, i, j);
		sxevent.setClasses(asVector(s));
		fireInternalEvent(sxevent);
	}

	protected void fireInternalEvent(java.lang.Object obj, int i, int j, java.util.Vector vector)
	{
		com.ofx.base.SxEvent sxevent = new SxEvent(obj, i, j);
		sxevent.setClasses(vector);
		sxevent.isGroupOperation(true);
		fireInternalEvent(sxevent);
	}

	public void fireInternalEvent(com.ofx.base.SxEvent sxevent)
	{
		getInternalListeners().fireStateChange(sxevent);
	}

	public void log(java.lang.Object obj)
	{
		env();
		com.ofx.base.SxEnvironment.log().println(obj);
	}

	public void log(java.lang.Exception exception)
	{
		env();
		com.ofx.base.SxEnvironment.log().println(exception);
	}

	public void stateChanged(com.ofx.base.SxEvent sxevent)
	{
		int i = sxevent.getState();
		int j = sxevent.getContext();
		java.lang.Object obj = sxevent.getObject();
		if(i == 104)
		{
			switch(j)
			{
			default:
				break;

			case 218: 
			case 224: 
			case 228: 
			case 229: 
				if(getMapLayerRegistry() != null)
				{
					getMapLayerRegistry().postAllDirtyEvent();
					getMapLayerRegistry().postPaintEvent();
				}
				break;
			}
			fireExternalEvent(sxevent);
			return;
		}
		if(i == 109)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint = sxevent.getGeometry().asPoint();
				java.util.Vector vector2 = sxevent.getClasses();
				if(vector2 != null)
				{
					java.lang.String s2 = (java.lang.String)vector2.firstElement();
					sxevent.setObject(findAtPoint(s2, sxdoublepoint));
				} else
				{
					sxevent.setObject(findAtPoint(sxdoublepoint));
				}
			} else
			{
				switch(j)
				{
				case 221: 
				case 222: 
				case 223: 
					java.util.Vector vector = sxevent.getClasses();
					if(sxevent.isGroupOperation())
					{
						sxevent.setObject(findInGeometry(vector, sxevent.getGeometry()));
					} else
					{
						java.lang.String s = (java.lang.String)vector.firstElement();
						sxevent.setObject(findInGeometry(s, sxevent.getGeometry()));
					}
					break;
				}
			}
			fireExternalEvent(sxevent);
			return;
		}
		if(i == 116)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxevent.getGeometry().asPoint();
				java.util.Vector vector3 = sxevent.getClasses();
				if(vector3 != null)
				{
					java.lang.String s3 = (java.lang.String)vector3.firstElement();
					sxevent.setObject(findAllAtPoint(s3, sxdoublepoint1));
				} else
				{
					sxevent.setObject(findAllAtPoint(sxdoublepoint1));
				}
				fireExternalEvent(sxevent);
			}
			return;
		}
		if(i == 107 || i == 101)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint2 = sxevent.getGeometry().asPoint();
				if(allowShiftSelect & ((sxevent.getModifiers() & 0x1) != 0 || sxevent.getModifiers() == 1))
					sxevent.setObject(selectAtPointToggle(sxdoublepoint2));
				else
					sxevent.setObject(selectAtPoint(sxdoublepoint2));
			} else
			{
				switch(j)
				{
				case 221: 
				case 222: 
				case 223: 
					java.util.Vector vector1 = sxevent.getClasses();
					if(sxevent.isGroupOperation())
					{
						sxevent.setObject(selectInGeometry(vector1, sxevent.getGeometry()));
					} else
					{
						java.lang.String s1 = (java.lang.String)vector1.firstElement();
						sxevent.setObject(selectInGeometry(s1, sxevent.getGeometry()));
					}
					break;
				}
			}
			fireExternalEvent(sxevent);
			return;
		}
		if(i == 117)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint3 = sxevent.getGeometry().asPoint();
				if(allowShiftSelect & ((sxevent.getModifiers() & 0x1) != 0 || sxevent.getModifiers() == 1))
					sxevent.setObject(selectAllAtPointToggle(sxdoublepoint3));
				else
					sxevent.setObject(selectAllAtPoint(sxdoublepoint3));
				fireExternalEvent(sxevent);
			}
			return;
		}
		if(i == 108)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint4 = ((com.ofx.geometry.SxPoint)sxevent.getGeometry()).asPoint();
				zoomToPoint(sxdoublepoint4);
			}
			if(j == 222)
				zoomToGeometry(sxevent.getGeometry());
			fireExternalEvent(sxevent);
			return;
		}
		if(i == 113)
		{
			if(j == 220)
				zoomOut();
			fireExternalEvent(sxevent);
			return;
		}
		if(i == 110)
		{
			if(j == 220)
			{
				com.ofx.geometry.SxDoublePoint sxdoublepoint5 = ((com.ofx.geometry.SxPoint)sxevent.getGeometry()).asPoint();
				try
				{
					if(getMapProjectionEnabled())
					{
						getDBProjection().unproject(sxdoublepoint5, sxdoublepoint5);
						getMapProjection().project(sxdoublepoint5, sxdoublepoint5);
						if(sxdoublepoint5.isNaN())
							throw new SxProjectionException("Invalid pan point");
					}
					getMapCanvas().setCenter(sxdoublepoint5);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception)
				{
					if(logProjectionExceptions)
						log("SxMapViewer.stateChanged: context PAN POINT: " + sxprojectionexception);
				}
			}
			fireExternalEvent(sxevent);
			return;
		} else
		{
			return;
		}
	}

	public java.util.Vector getAvailableMaps()
		throws com.ofx.repository.SxRepositoryException
	{
		java.util.Vector vector = new Vector(5);
		java.lang.String s;
		for(java.util.Enumeration enumeration = com.ofx.repository.SxMap.objectIDs(getQuery()); enumeration.hasMoreElements(); vector.addElement(s))
			s = (java.lang.String)enumeration.nextElement();

		return vector;
	}

	public void clearSelectionLayer()
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.clear();
	}

	public void clearSelectionLayerQuietly()
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.clearQuietly();
	}

	public void clearMarkerLayerQuietly()
	{
		com.ofx.mapViewer.SxMarkerLayer sxmarkerlayer = getMarkerLayer();
		if(sxmarkerlayer != null)
			sxmarkerlayer.clearQuietly();
	}

	public void clearMarkerLayer()
	{
		com.ofx.mapViewer.SxMarkerLayer sxmarkerlayer = getMarkerLayer();
		if(sxmarkerlayer != null)
			sxmarkerlayer.clear();
	}

	public void clear()
	{
		if(getSelectionBin().isEmpty())
		{
			clearMarkerLayer();
		} else
		{
			clearMarkerLayerQuietly();
			clearSelectionLayer();
		}
	}

	public void clearBinLayers()
	{
		getBinLayerRegistry().clear();
	}

	public void clearBinLayersQuietly()
	{
		getBinLayerRegistry().clearQuietly();
	}

	public void clearAllBinLayers()
	{
		clearAllBinLayersQuietly();
		getMapLayerRegistry().postAllDirtyEvent();
		getMapLayerRegistry().postPaintEvent();
	}

	public void clearAllBinLayersQuietly()
	{
		clearSelectionLayerQuietly();
		clearMarkerLayerQuietly();
		clearBinLayersQuietly();
	}

	public java.lang.String getMapName()
	{
		return mapName;
	}

	public synchronized void releaseMapResources()
	{
		try
		{
			if(eventQueue != null)
				eventQueue.stopDispatching();
			if(map != null)
			{
				map.release();
				map = null;
			}
			eventQueue = null;
			mapLayerRegistry = null;
			binLayerRegistry = null;
			foreGroundClasses = null;
			backGroundClasses = null;
			internalListeners = null;
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
	}

	public synchronized void setMapName(java.lang.String s)
		throws com.ofx.mapViewer.SxInvalidNameException
	{
		try
		{
			getQuery();
			if(!getAvailableMaps().contains(s))
				throw new SxInvalidNameException(s);
			mapName = s;
			com.ofx.repository.SxMap sxmap = com.ofx.repository.SxMap.retrieve(mapName, getQuery());
			setMap(sxmap);
			if(isImageServer())
				getEventQueue().imageServerMode(true);
			else
				getEventQueue().startDispatching();
			paint();
			com.ofx.base.SxEvent sxevent = new SxEvent(this, 104, 227);
			stateChanged(sxevent);
		}
		catch(com.ofx.mapViewer.SxInvalidNameException sxinvalidnameexception)
		{
			throw sxinvalidnameexception;
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
			throw new SxInvalidNameException(mapName, exception);
		}
	}

	public com.ofx.mapViewer.SxMapLayerEventQueue getEventQueue()
	{
		if(eventQueue == null)
			eventQueue = new SxMapLayerEventQueue();
		return eventQueue;
	}

	public com.ofx.mapViewer.SxHighlightBin getBin(java.lang.String s)
	{
		if(s == null)
			return null;
		try
		{
			if(s.equals("SELECTION"))
				return getSelectionBin();
			if(s.equals("MARKER"))
				return getMarkerBin();
			com.ofx.mapViewer.SxBinLayer sxbinlayer = getBinLayer(s);
			if(sxbinlayer != null)
				return sxbinlayer.getBin();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return null;
	}

	public void deleteBin(java.lang.String s)
	{
		try
		{
			deleteBinLayer(s);
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
	}

	public void deleteBinLayer(java.lang.String s)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		java.lang.String s1 = s + "LABELS";
		getBinLayerRegistry().removeNamedLayer(s);
		getBinLayerRegistry().removeNamedLayer(s1);
	}

	public com.ofx.mapViewer.SxBinLayer createBinLayer(java.lang.String s)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		return createBinLayer(s, "DEFAULT");
	}

	public com.ofx.mapViewer.SxBinLayer createBinLayer(java.lang.String s, java.lang.String s1)
		throws com.ofx.mapViewer.SxMapLayerException
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = new SxBinLayer(s, s1, getQuery());
		sxbinlayer.setMapViewer(this);
		com.ofx.mapViewer.SxBinLabelLayer sxbinlabellayer = new SxBinLabelLayer(sxbinlayer);
		getBinLayerRegistry().addBinLayer(s, sxbinlayer);
		getBinLayerRegistry().addBinLabelLayer(s + "LABELS", sxbinlabellayer);
		return sxbinlayer;
	}

	public java.lang.String getDBNameDefault()
	{
		return env().getDbNameDefault();
	}

	public java.lang.String getDBName()
	{
		return env().getDbName(getPropertyGroupName());
	}

	public synchronized void setDBName(java.lang.String s)
		throws com.ofx.mapViewer.SxInvalidNameException
	{
		if(!env().isClient())
		{
			env().setDbName(getPropertyGroupName(), s);
			if(getQuery() != null)
				query.close();
			try
			{
				setupQuery();
			}
			catch(java.lang.RuntimeException runtimeexception)
			{
				throw new SxInvalidNameException("Exception occurred opening database named: " + s + " exception: " + runtimeexception);
			}
		}
	}

	public java.lang.String getDBUsername()
	{
		return env().getUserID(getPropertyGroupName());
	}

	public void setDBUsername(java.lang.String s)
	{
		env().setUserID(getPropertyGroupName(), s);
	}

	public void setDBPassword(java.lang.String s)
	{
		env().setPassword(getPropertyGroupName(), s);
	}

	public double[] getCenter()
	{
		return convertMapToLatLong(getMapCanvas().getCenter()).asArray();
	}

	public void setCenter(double d, double d1)
	{
		getMapCanvas().setCenter(convertLatLongToMap(d1, d));
	}

	public java.util.Vector getHighlightBinNames()
	{
		return getBinLayerNames();
	}

	public java.util.Vector getHighlightBins()
	{
		java.util.Vector vector = new Vector();
		com.ofx.mapViewer.SxBinLayer sxbinlayer;
		for(java.util.Enumeration enumeration = getBinLayers(); enumeration.hasMoreElements(); vector.addElement(sxbinlayer.getBin()))
			sxbinlayer = (com.ofx.mapViewer.SxBinLayer)enumeration.nextElement();

		return vector;
	}

	public com.ofx.repository.SxMap getMap()
	{
		return map;
	}

	public void setMap(com.ofx.repository.SxMap sxmap)
	{
		releaseMapResources();
		com.ofx.projection.SxProjectionInterface sxprojectioninterface = sxmap.getProjection();
		if(sxprojectioninterface == getDBProjection())
			sxmap.setProjection((com.ofx.projection.SxProjectionInterface)sxprojectioninterface.clone());
		if(sxmap.getGeometrySplittingEnabled() && !sxprojectioninterface.isGeometrySplittingSupported())
			log("<WARNING> Map " + sxmap.getIdentifier() + " enables geometry splitting for a projection which does not support it.");
		com.ofx.mapViewer.SxMapCanvas sxmapcanvas = getMapCanvas();
		sxmapcanvas.setScaleAndCenter(sxmap.getInitialScale(), sxmap.getOrigin(), true);
		map = sxmap;
		getMapLayerRegistry();
	}

	public java.awt.Cursor getWaitCursor()
	{
		if(waitCursor == null)
			waitCursor = java.awt.Cursor.getPredefinedCursor(3);
		return waitCursor;
	}

	public void setWaitCursor(java.awt.Cursor cursor)
	{
		waitCursor = cursor;
	}

	public void clearWaitComponent()
	{
		waitComponent = null;
	}

	public java.awt.Component getWaitComponent()
	{
		return waitComponent;
	}

	public void setWaitComponent(java.awt.Component component)
	{
		waitComponent = component;
	}

	public double getScale()
	{
		return getMapCanvas().getScale();
	}

	public void setScale(double d)
	{
		double d1 = getScale();
		double d2 = clampScale(d);
		if(d1 != d2)
			getMapCanvas().setScale(d2);
	}

	public void setSelectionTolerance(int i)
	{
		selectionTolerance = i;
	}

	public int getSelectionTolerance()
	{
		return selectionTolerance;
	}

	public int getUnits()
	{
		return units;
	}

	public void setUnits(int i)
	{
		units = i;
	}

	public double getViewWidth()
	{
		double d = getScale() * (double)getMapCanvas().getSize().width;
		com.ofx.base.SxDistance sxdistance = new SxDistance(d, 203);
		return sxdistance.convertTo(getUnits());
	}

	public com.ofx.mapViewer.SxHighlightBin getMarkerBin()
	{
		com.ofx.mapViewer.SxMarkerLayer sxmarkerlayer = getMarkerLayer();
		if(sxmarkerlayer != null)
			return sxmarkerlayer.getBin();
		else
			return null;
	}

	public com.ofx.mapViewer.SxMarkerLayer getMarkerLayer()
	{
		return (com.ofx.mapViewer.SxMarkerLayer)getNamedLayer("MARKER");
	}

	public com.ofx.mapViewer.SxHighlightBin getSelectionBin()
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			return sxbinlayer.getBin();
		else
			return null;
	}

	public com.ofx.mapViewer.SxBinLayer getSelectionLayer()
	{
		return (com.ofx.mapViewer.SxBinLayer)getNamedLayer("SELECTION");
	}

	public java.util.Enumeration getBinLayers()
	{
		java.util.Vector vector = getMapLayerRegistry().getBinLayers(new Vector());
		vector.removeElement(getSelectionLayer());
		return vector.elements();
	}

	public java.util.Enumeration getBinLabelLayers()
	{
		java.util.Vector vector = getMapLayerRegistry().getBinLabelLayers(new Vector());
		com.ofx.mapViewer.SxMapLayerInterface sxmaplayerinterface = getNamedLayer("SELECTIONLABELS");
		if(sxmaplayerinterface != null)
			vector.removeElement(sxmaplayerinterface);
		return vector.elements();
	}

	public java.util.Vector getBinLayerNames()
	{
		java.util.Vector vector = new Vector();
		com.ofx.mapViewer.SxMapLayer sxmaplayer;
		for(java.util.Enumeration enumeration = getBinLayers(); enumeration.hasMoreElements(); vector.addElement(sxmaplayer.getName()))
			sxmaplayer = (com.ofx.mapViewer.SxMapLayer)enumeration.nextElement();

		return vector;
	}

	public java.util.Vector getBinLabelLayerNames()
	{
		java.util.Vector vector = new Vector();
		com.ofx.mapViewer.SxMapLayer sxmaplayer;
		for(java.util.Enumeration enumeration = getBinLabelLayers(); enumeration.hasMoreElements(); vector.addElement(sxmaplayer.getName()))
			sxmaplayer = (com.ofx.mapViewer.SxMapLayer)enumeration.nextElement();

		return vector;
	}

	public com.ofx.mapViewer.SxBinLayer getBinLayer(java.lang.String s)
	{
		java.util.Vector vector = getMapLayerRegistry().getBinLayers(new Vector());
		for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.mapViewer.SxBinLayer sxbinlayer = (com.ofx.mapViewer.SxBinLayer)enumeration.nextElement();
			if(sxbinlayer.getName().equals(s))
				return sxbinlayer;
		}

		return null;
	}

	public com.ofx.mapViewer.SxBinLabelLayer getBinLabelLayer(java.lang.String s)
	{
		java.util.Vector vector = getMapLayerRegistry().getBinLabelLayers(new Vector());
		for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.mapViewer.SxBinLabelLayer sxbinlabellayer = (com.ofx.mapViewer.SxBinLabelLayer)enumeration.nextElement();
			if(sxbinlabellayer.getName().equals(s))
				return sxbinlabellayer;
		}

		return null;
	}

	public void addSelectionLayerListener(com.ofx.base.SxStateChangeListener sxstatechangelistener)
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.addStateChangeListener(sxstatechangelistener);
	}

	public com.ofx.query.SxQueryResultInterface findWithinObject(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = getQuery().getObjectWithId(s1, s2);
		if(sxqueryresultinterface.isEmpty())
			return null;
		com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)sxqueryresultinterface.firstElement();
		if(sxspatialobject.getGeometry().getDimension() != 2)
			return null;
		else
			return findInGeometry(s, (com.ofx.geometry.SxPolygon)sxspatialobject.getGeometry());
	}

	public void selectWithinObject(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = getQuery().getObjectWithId(s1, s2);
		if(sxqueryresultinterface.isEmpty())
			return;
		com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)sxqueryresultinterface.firstElement();
		if(sxspatialobject.getGeometry().getDimension() != 2)
		{
			return;
		} else
		{
			selectInGeometry(asVector(s), (com.ofx.geometry.SxPolygon)sxspatialobject.getGeometry());
			return;
		}
	}

	public com.ofx.mapViewer.SxHighlightBin createBin(java.lang.String s)
	{
		try
		{
			com.ofx.mapViewer.SxBinLayer sxbinlayer = createBinLayer(s);
			return sxbinlayer.getBin();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return null;
	}

	public com.ofx.mapViewer.SxHighlightBin createBin(java.lang.String s, java.lang.String s1)
	{
		try
		{
			com.ofx.mapViewer.SxBinLayer sxbinlayer = createBinLayer(s, s1);
			return sxbinlayer.getBin();
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return null;
	}

	void updateForegroundClasses()
	{
		foreGroundClasses = null;
	}

	void updateBackgroundClasses()
	{
		backGroundClasses = null;
	}

	public java.util.Vector getForegroundClasses()
	{
		if(getMap() != null)
		{
			java.util.Vector vector = (java.util.Vector)getMap().getForegroundClassNames().clone();
			foreGroundClasses = new Vector(vector.size());
			for(int i = vector.size(); i > 0; i--)
				foreGroundClasses.addElement(vector.elementAt(i - 1));

		} else
		{
			return new Vector(0);
		}
		return foreGroundClasses;
	}

	public java.util.Vector getForegroundClasses(int i)
	{
		java.util.Vector vector = getForegroundClasses();
		if(i == 300)
		{
			com.ofx.collections.SxSortInterface sxsortinterface = com.ofx.base.SxFactory.getSortInterface();
			try
			{
				sxsortinterface.sort(vector, new SxLexicographicAscendingStringComparator());
			}
			catch(java.lang.Exception exception)
			{
				log(exception);
			}
		}
		return vector;
	}

	public java.util.Vector getBackgroundClasses()
	{
		if(backGroundClasses == null)
			if(getMap() != null)
			{
				java.util.Vector vector = (java.util.Vector)getMap().getBackgroundClassNames().clone();
				backGroundClasses = new Vector(vector.size());
				for(int i = vector.size(); i > 0; i--)
					backGroundClasses.addElement(vector.elementAt(i - 1));

			} else
			{
				return new Vector(0);
			}
		return backGroundClasses;
	}

	public java.util.Vector classBinDescriptions(java.lang.String s)
	{
		java.util.Vector vector = new Vector();
		java.util.Vector vector1 = getHighlightBins();
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			vector1.insertElementAt(sxbinlayer.getBin(), 0);
		for(java.util.Enumeration enumeration = vector1.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.mapViewer.SxHighlightBin sxhighlightbin = (com.ofx.mapViewer.SxHighlightBin)enumeration.nextElement();
			if(sxhighlightbin.classBinObjects(s).size() > 0)
				vector.addElement(sxhighlightbin.getDescription());
		}

		return vector;
	}

	public java.util.Vector classBinNames(java.lang.String s)
	{
		java.util.Vector vector = new Vector();
		java.util.Vector vector1 = getHighlightBins();
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			vector1.insertElementAt(sxbinlayer.getBin(), 0);
		for(java.util.Enumeration enumeration = vector1.elements(); enumeration.hasMoreElements();)
		{
			com.ofx.mapViewer.SxHighlightBin sxhighlightbin = (com.ofx.mapViewer.SxHighlightBin)enumeration.nextElement();
			if(sxhighlightbin.entryExists(s) && sxhighlightbin.classBinObjects(s).size() > 0)
				vector.addElement(sxhighlightbin.getName());
		}

		return vector;
	}

	public com.ofx.query.SxQueriableObjectInterface getObject(java.lang.String s, java.lang.String s1)
	{
		return getQuery().retrieve(s, s1);
	}

	public com.ofx.query.SxQueryResultInterface getObjects(java.lang.String s)
	{
		return getQuery().getObjects(s);
	}

	public com.ofx.query.SxQueryResultInterface getObjectWithId(java.lang.String s, java.lang.Object obj)
	{
		return getQuery().getObjectWithId(s, obj);
	}

	public com.ofx.query.SxQueryResultInterface getObjectsWithIDs(java.lang.String s, java.util.Vector vector)
	{
		return getQuery().getObjectsWithIDs(s, vector);
	}

	public com.ofx.query.SxQueryResultInterface getObjectsAtPoint(java.lang.String s, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		double d = (double)getSelectionTolerance() * getMapCanvas().getScale();
		com.ofx.geometry.SxCircle sxcircle = new SxCircle(sxdoublepoint, d);
		return getObjectsInGeometry(s, sxcircle);
	}

	public void selectInCircle(java.lang.String s)
	{
		fireInternalEvent(this, 107, 221, s);
	}

	public void selectInCircle(java.util.Vector vector)
	{
		fireInternalEvent(this, 107, 221, vector);
	}

	public void findInCircle(java.lang.String s)
	{
		fireInternalEvent(this, 109, 221, s);
	}

	public void findInCircle(java.util.Vector vector)
	{
		fireInternalEvent(this, 109, 221, vector);
	}

	public com.ofx.query.SxQueryResultInterface getObjectsInGeometry(java.lang.String s, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		return getQuery().getCoincidentObjects(s, sxgeometryinterface, false);
	}

	public boolean openSession(java.lang.String s, java.lang.String s1, java.lang.String s2, java.lang.String s3)
	{
		env().setDbType(getPropertyGroupName(), s);
		setDBUsername(s2);
		setDBPassword(s3);
		try
		{
			setDBName(s1);
		}
		catch(com.ofx.mapViewer.SxInvalidNameException sxinvalidnameexception)
		{
			log(sxinvalidnameexception);
			return false;
		}
		return true;
	}

	public void setSession(com.ofx.query.SxQueryInterface sxqueryinterface)
	{
		query = null;
		com.ofx.mapViewer.SxMapCanvas sxmapcanvas = getMapCanvas();
		query = sxqueryinterface;
	}

	public void closeSession()
	{
	}

	public void zoomReset()
	{
		try
		{
			getMapCanvas().setScaleAndCenter(getMap().getInitialScale(), getMap().getOrigin(), false);
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
	}

	public java.util.Vector asVector(java.lang.String s)
	{
		java.util.Vector vector = new Vector(1);
		vector.addElement(s);
		return vector;
	}

	public void selectInRect(java.lang.String s)
	{
		fireInternalEvent(this, 107, 222, s);
	}

	public void selectInRect(java.util.Vector vector)
	{
		fireInternalEvent(this, 107, 222, vector);
	}

	public com.ofx.query.SxQueryResultInterface selectInGeometry(java.lang.String s, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findInGeometry(s, sxgeometryinterface);
		displayInSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public java.util.Vector selectInGeometry(java.util.Vector vector, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		java.util.Vector vector1 = findInGeometry(vector, sxgeometryinterface);
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.displayQueryResults(vector1);
		return vector1;
	}

	public com.ofx.query.SxQueryResultInterface findInGeometry(java.lang.String s, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		java.util.Hashtable hashtable = getMapLayerRegistry().findInGeometry(s, sxgeometryinterface);
		com.ofx.query.evaluation.SxQueryResultOverHashtable sxqueryresultoverhashtable = new SxQueryResultOverHashtable(hashtable, s, getQuery());
		return sxqueryresultoverhashtable;
	}

	public java.util.Vector findInGeometry(java.util.Vector vector, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		if(vector == null)
			return new Vector(0);
		java.util.Vector vector1 = new Vector(vector.size());
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface;
		for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements(); vector1.addElement(sxqueryresultinterface))
		{
			java.lang.String s = (java.lang.String)enumeration.nextElement();
			sxqueryresultinterface = findInGeometry(s, sxgeometryinterface);
		}

		return vector1;
	}

	public void findInRect(java.lang.String s)
	{
		fireInternalEvent(this, 109, 222, s);
	}

	public void findInRect(java.util.Vector vector)
	{
		fireInternalEvent(this, 109, 222, vector);
	}

	public void selectInRegion(java.lang.String s)
	{
		fireInternalEvent(this, 107, 223, s);
	}

	public void selectInRegion(java.util.Vector vector)
	{
		fireInternalEvent(this, 107, 223, vector);
	}

	public void findInRegion(java.lang.String s)
	{
		fireInternalEvent(this, 109, 223, s);
	}

	public void findInRegion(java.util.Vector vector)
	{
		fireInternalEvent(this, 109, 223, vector);
	}

	public void displayInSelectionLayer(com.ofx.query.SxQueryResultInterface sxqueryresultinterface)
	{
		displayInSelectionLayer(sxqueryresultinterface.asVector());
	}

	public void displayInSelectionLayer(java.util.Vector vector)
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.displaySpatialObjects(vector);
	}

	public void displayMultipleClassesInSelectionLayer(com.ofx.query.SxQueryResultInterface sxqueryresultinterface)
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
		{
			sxbinlayer.clearQuietly();
			sxbinlayer.addOrRemoveIfPresent(sxqueryresultinterface);
		}
	}

	public void addOrRemoveFromSelectionLayer(com.ofx.query.SxQueryResultInterface sxqueryresultinterface)
	{
		com.ofx.mapViewer.SxBinLayer sxbinlayer = getSelectionLayer();
		if(sxbinlayer != null)
			sxbinlayer.addOrRemoveIfPresent(sxqueryresultinterface);
	}

	public com.ofx.query.SxQueryResultInterface selectAtPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAtPoint(sxdoublepoint);
		if(!sxqueryresultinterface.isEmpty())
			displayInSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface selectAllAtPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAllAtPoint(sxdoublepoint);
		if(!sxqueryresultinterface.isEmpty())
			displayMultipleClassesInSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface selectAtPointToggle(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAtPoint(sxdoublepoint);
		addOrRemoveFromSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface selectAllAtPointToggle(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAllAtPoint(sxdoublepoint);
		addOrRemoveFromSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface selectAtPoint(java.lang.String s, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAtPoint(s, sxdoublepoint);
		if(!sxqueryresultinterface.isEmpty())
			displayInSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface selectAllAtPoint(java.lang.String s, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = findAllAtPoint(s, sxdoublepoint);
		if(!sxqueryresultinterface.isEmpty())
			displayMultipleClassesInSelectionLayer(sxqueryresultinterface);
		return sxqueryresultinterface;
	}

	public com.ofx.query.SxQueryResultInterface findAtPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		java.util.Vector vector = getMapLayerRegistry().findAtPoint(sxdoublepoint);
		java.lang.String s = "";
		if(!vector.isEmpty())
			s = ((com.ofx.repository.SxSpatialObject)vector.firstElement()).getClassDefinition().getClassificationName();
		return new SxQueryResultOverVector(vector, s, getQuery());
	}

	public com.ofx.query.SxQueryResultInterface findAllAtPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		java.util.Vector vector = getMapLayerRegistry().findAllAtPoint(sxdoublepoint);
		java.lang.String s = "";
		if(!vector.isEmpty())
			s = ((com.ofx.repository.SxSpatialObject)vector.firstElement()).getClassDefinition().getClassificationName();
		return new SxQueryResultOverVector(vector, s, getQuery());
	}

	public com.ofx.query.SxQueryResultInterface findAtPoint(java.lang.String s, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		java.util.Vector vector = getMapLayerRegistry().findAtPoint(s, sxdoublepoint);
		java.lang.String s1 = s;
		if(s1 == null)
			s1 = "";
		return new SxQueryResultOverVector(vector, s, getQuery());
	}

	public com.ofx.query.SxQueryResultInterface findAllAtPoint(java.lang.String s, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		java.util.Vector vector = getMapLayerRegistry().findAllAtPoint(s, sxdoublepoint);
		java.lang.String s1 = s;
		if(s1 == null)
			s1 = "";
		return new SxQueryResultOverVector(vector, s, getQuery());
	}

	private java.util.Hashtable hashtableFor(java.util.Vector vector)
	{
		java.util.Hashtable hashtable = new Hashtable(25);
		if(vector == null)
			return hashtable;
		com.ofx.repository.SxSpatialObject sxspatialobject;
		for(java.util.Enumeration enumeration = vector.elements(); enumeration.hasMoreElements(); hashtable.put(sxspatialobject.getID(), sxspatialobject))
			sxspatialobject = (com.ofx.repository.SxSpatialObject)enumeration.nextElement();

		return hashtable;
	}

	public com.ofx.query.SxQueryResultInterface findAtPoint(java.lang.String s, double d, double d1)
	{
		return findAtPoint(s, convertLatLongToDB(d1, d));
	}

	public com.ofx.query.SxQueryResultInterface findAllAtPoint(java.lang.String s, double d, double d1)
	{
		return findAllAtPoint(s, convertLatLongToDB(d1, d));
	}

	public com.ofx.query.SxQueryResultInterface getObjectsAtPoint(java.lang.String s, double d, double d1)
	{
		return getObjectsAtPoint(s, convertLatLongToDB(d1, d));
	}

	public void selectAtPoint()
	{
		fireInternalEvent(this, 107, 220);
	}

	public void selectAllAtPoint()
	{
		fireInternalEvent(this, 117, 220);
	}

	public void findAtPoint()
	{
		fireInternalEvent(this, 109, 220);
	}

	public void findAtPoint(java.lang.String s)
	{
		fireInternalEvent(this, 109, 220, s);
	}

	public void findAtPoint(java.util.Vector vector)
	{
		fireInternalEvent(this, 109, 220, vector);
	}

	public void findAllAtPoint()
	{
		fireInternalEvent(this, 116, 220);
	}

	public void findAllAtPoint(java.lang.String s)
	{
		fireInternalEvent(this, 116, 220, s);
	}

	public void findAllAtPoint(java.util.Vector vector)
	{
		fireInternalEvent(this, 116, 220, vector);
	}

	public double getZoomFactor()
	{
		return zoomFactor;
	}

	public void setZoomFactor(double d)
	{
		if(zoomFactor == 0.0D)
			log("zoom factor zero not supported");
		zoomFactor = d;
	}

	public int getPanPercentage()
	{
		return panPercentage;
	}

	public void setPanPercentage(int i)
	{
		panPercentage = i;
	}

	public void zoomIn()
	{
		zoomIn(getZoomFactor());
	}

	public void zoomOut()
	{
		zoomOut(getZoomFactor());
	}

	public void zoomIn(double d)
	{
		double d1 = getScale();
		double d2 = clampScale(zoomInScale(d));
		if(d1 != d2)
		{
			getMapCanvas().setScaleAndCenter(d2, getMapCanvas().getCenter(), true);
			fireExternalEvent(this, 108, 205);
		}
	}

	public void zoomOut(double d)
	{
		double d1 = getScale();
		double d2 = clampScale(zoomOutScale(d));
		if(d1 != d2)
		{
			getMapCanvas().setScaleAndCenter(d2, getMapCanvas().getCenter(), true);
			fireExternalEvent(this, 113, 205);
		}
	}

	public void isImageServer(boolean flag)
	{
		if(isImageServer == flag)
			return;
		isImageServer = flag;
		if(isImageServer)
			startImageServing();
		else
			stopImageServing();
	}

	protected void startImageServing()
	{
		com.ofx.mapViewer.SxMapLayerRegistry.cacheForAllRegistries(false);
		java.lang.String s = properties().getStringInGroup("ofx.imageConverter", "com.ofx.base.SxPNGConverter", getPropertyGroupName());
		com.ofx.base.SxImageConverterInterface sximageconverterinterface = (com.ofx.base.SxImageConverterInterface)com.ofx.base.SxFactory.resolve(s);
		setImageConverter(sximageconverterinterface);
	}

	protected void stopImageServing()
	{
		com.ofx.mapViewer.SxMapLayerRegistry.cacheForAllRegistries(true);
		setImageConverter(null);
	}

	public boolean isImageServer()
	{
		return isImageServer;
	}

	public double zoomInScale(double d)
	{
		return getMapCanvas().getScale() / d;
	}

	public double zoomOutScale(double d)
	{
		return getMapCanvas().getScale() * d;
	}

	private double clampScale(double d)
	{
		double d1 = getMap().getLowScale();
		double d2 = getMap().getHighScale();
		if(d < d1)
			return d1;
		if(d > d2)
			return d2;
		else
			return d;
	}

	public void zoomToPoint()
	{
		clearMarkerLayer();
		fireInternalEvent(this, 108, 220);
	}

	public void zoomToPoint(double d, double d1)
	{
		zoomToPoint(convertLatLongToDB(d1, d));
	}

	public double distance(double d, double d1, double d2, double d3)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(d1, d);
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(d3, d2);
		try
		{
			if(!getDBProjection().isDefaultGeoLatLong())
			{
				getDBProjection().project(sxdoublepoint, sxdoublepoint);
				getDBProjection().project(sxdoublepoint1, sxdoublepoint1);
			}
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.distance: " + sxprojectionexception);
		}
		return distance(((com.ofx.geometry.SxGeometryInterface) (new SxPoint(sxdoublepoint))), ((com.ofx.geometry.SxGeometryInterface) (new SxPoint(sxdoublepoint1))));
	}

	public double distance(com.ofx.repository.SxSpatialObject sxspatialobject, com.ofx.repository.SxSpatialObject sxspatialobject1)
	{
		return distance(sxspatialobject.getGeometry(), sxspatialobject1.getGeometry());
	}

	public double distance(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, com.ofx.geometry.SxGeometryInterface sxgeometryinterface1)
	{
		double d = sxgeometryinterface.distance(sxgeometryinterface1, getDBProjection());
		int i = getDBProjection().getEllipsoid().getUnitId();
		if(getUnits() != i)
			d = (new SxDistance(d, i)).convertTo(getUnits());
		return d;
	}

	public double length(com.ofx.repository.SxSpatialObject sxspatialobject)
	{
		return length(sxspatialobject.getGeometry());
	}

	public double length(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		double d = sxgeometryinterface.length(getDBProjection());
		int i = getDBProjection().getEllipsoid().getUnitId();
		if(getUnits() != i)
			d = (new SxDistance(d, i)).convertTo(getUnits());
		return d;
	}

	public void zoomToPoint(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		try
		{
			if(getMapProjectionEnabled())
			{
				getDBProjection().unproject(sxdoublepoint, sxdoublepoint);
				getMapProjection().project(sxdoublepoint, sxdoublepoint);
			}
			if(!java.lang.Double.isNaN(sxdoublepoint.getX()) && !java.lang.Double.isNaN(sxdoublepoint.getY()))
				setScaleAndCenter(zoomInScale(getZoomFactor()), sxdoublepoint);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.zoomToPoint: context ZOOM POINT: " + sxprojectionexception);
		}
	}

	public void zoomToGeometry(com.ofx.geometry.SxGeometryInterface sxgeometryinterface, boolean flag)
	{
		if(sxgeometryinterface.getDimension() != 2)
			return;
		double d = getScale();
		com.ofx.geometry.SxRectangle sxrectangle = sxgeometryinterface.getBounds();
		double d1 = 1.0D;
		com.ofx.geometry.SxRectangle asxrectangle[] = null;
		if(sxgeometryinterface instanceof com.ofx.geometry.SxGeometryCollection)
		{
			java.util.Vector vector = ((com.ofx.geometry.SxGeometryCollection)sxgeometryinterface).getGeometry();
			asxrectangle = new com.ofx.geometry.SxRectangle[vector.size()];
			java.util.Enumeration enumeration = vector.elements();
			for(int i = 0; enumeration.hasMoreElements(); i++)
			{
				sxgeometryinterface = (com.ofx.geometry.SxGeometryInterface)enumeration.nextElement();
				double ad2[] = sxgeometryinterface.getXPoints();
				double ad3[] = sxgeometryinterface.getYPoints();
				int l = sxgeometryinterface.getNPoints();
				if(getMapProjectionEnabled())
					try
					{
						getDBProjection().unproject(ad2, ad3, ad2, ad3, l);
						getMapProjection().project(ad2, ad3, ad2, ad3, l);
					}
					catch(com.ofx.projection.SxProjectionException sxprojectionexception1)
					{
						if(logProjectionExceptions)
							log("SxMapViewer.zoomToGeometry: context ZOOM GEOMETRY: " + sxprojectionexception1);
					}
				asxrectangle[i] = (new SxPolygon(ad2, ad3, l)).getBounds();
			}

		} else
		{
			double ad[] = sxgeometryinterface.getXPoints();
			double ad1[] = sxgeometryinterface.getYPoints();
			int j = sxgeometryinterface.getNPoints();
			if(getMapProjectionEnabled())
				try
				{
					getDBProjection().unproject(ad, ad1, ad, ad1, j);
					getMapProjection().project(ad, ad1, ad, ad1, j);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception)
				{
					if(logProjectionExceptions)
						log("SxMapViewer.zoomToGeometry: context ZOOM GEOMETRY: " + sxprojectionexception);
				}
			asxrectangle = new com.ofx.geometry.SxRectangle[1];
			asxrectangle[0] = (new SxPolygon(ad, ad1, j)).getBounds();
		}
		double d3 = 0.0D;
		double d4 = 0.0D;
		for(int k = 0; k < asxrectangle.length; k++)
		{
			d3 += asxrectangle[k].getWidth();
			d4 += asxrectangle[k].getHeight();
			sxrectangle = k != 0 ? sxrectangle.union(asxrectangle[k]) : asxrectangle[k];
		}

		if(d3 * d4 != 0.0D)
		{
			java.awt.Dimension dimension = getMapCanvas().getSize();
			double d2 = java.lang.Math.max(d3 / (d * (double)dimension.width), d4 / (d * (double)dimension.height));
			setScaleAndCenter(d * d2, sxrectangle.getCenter(), flag);
		}
	}

	public void zoomToGeometry(com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		zoomToGeometry(sxgeometryinterface, true);
	}

	public void setScaleAndCenter(double d, com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		setScaleAndCenter(d, sxdoublepoint, true);
	}

	public void setScaleAndCenter(double d, com.ofx.geometry.SxDoublePoint sxdoublepoint, boolean flag)
	{
		com.ofx.mapViewer.SxMapCanvas sxmapcanvas = getMapCanvas();
		double d1 = getScale();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxmapcanvas.getCenter();
		double d2 = clampScale(d);
		if(d1 != d2 || !sxdoublepoint1.equals(sxdoublepoint))
			sxmapcanvas.setScaleAndCenter(d2, sxdoublepoint, flag);
	}

	public void zoomToRect()
	{
		clearMarkerLayer();
		fireInternalEvent(this, 108, 222);
	}

	public void zoomToRect(double d, double d1, double d2, double d3)
	{
		try
		{
			double ad[] = {
				d, d2
			};
			double ad1[] = {
				d1, d3
			};
			getMapProjection().project(ad, ad1, ad, ad1, 2);
			com.ofx.geometry.SxRectangle sxrectangle = com.ofx.geometry.SxRectangle.fromCorners(ad[0], ad1[0], ad[1], ad1[1]);
			com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxrectangle.getQueryGeometry(this, getMapProjection(), getDBProjection());
			zoomToGeometry(sxgeometryinterface);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.zoomToRect: context ZOOM RECT: " + sxprojectionexception);
		}
	}

	public boolean isGeometrySplittingEnabled()
	{
		if(!com.ofx.base.SxEnvironment.isGeometrySplittingEnabled())
			return false;
		return getMap() != null && getMap().getGeometrySplittingEnabled();
	}

	public void setGeographicPanning(boolean flag)
	{
		geographicPanning = flag;
	}

	public boolean getGeographicPanning()
	{
		return geographicPanning;
	}

	public void panNorth()
	{
		panDegrees(0.0D);
	}

	public void panNorthEast()
	{
		panDegrees(45D);
	}

	public void panEast()
	{
		panDegrees(90D);
	}

	public void panSouthEast()
	{
		panDegrees(135D);
	}

	public void panSouth()
	{
		panDegrees(180D);
	}

	public void panSouthWest()
	{
		panDegrees(225D);
	}

	public void panWest()
	{
		panDegrees(270D);
	}

	public void panNorthWest()
	{
		panDegrees(315D);
	}

	public void panDegrees(double d)
	{
		com.ofx.mapViewer.SxMapCanvas sxmapcanvas = getMapCanvas();
		com.ofx.projection.SxProjectionInterface sxprojectioninterface = getMapProjection();
		com.ofx.projection.SxProjectionInterface sxprojectioninterface1 = getDBProjection();
		com.ofx.geometry.SxDoublePoint sxdoublepoint = null;
		int i;
		for(i = panPercentage; i > 0; i >>= 1)
		{
			sxdoublepoint = sxmapcanvas.getPanPoint(d, i);
			try
			{
				sxprojectioninterface.unproject(sxdoublepoint, sxdoublepoint);
				sxprojectioninterface1.project(sxdoublepoint, sxdoublepoint);
				if(!sxdoublepoint.isNaN())
					break;
			}
			catch(com.ofx.projection.SxProjectionException sxprojectionexception) { }
		}

		sxdoublepoint = i <= 0 ? null : sxmapcanvas.getPanPoint(d, i);
		if(sxdoublepoint == null)
		{
			if(logProjectionExceptions)
				log("Directional pan could not find a valid pan point");
			return;
		} else
		{
			sxmapcanvas.setCenter(sxdoublepoint);
			fireExternalEvent(new Double(d), 110, 205);
			return;
		}
	}

	public void panHand()
	{
		fireInternalEvent(this, 110, 300);
	}

	public void panToPoint()
	{
		fireInternalEvent(this, 110, 220);
	}

	public void panToPoint(double d, double d1)
	{
		setCenter(d, d1);
	}

	public void panToObject(java.lang.String s, java.lang.String s1)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = getObjectWithId(s, s1);
		if(sxqueryresultinterface.isEmpty())
		{
			log(" panToObject failed, Spatial Object not found, class name was: \"" + s + "\" object id was: \"" + s1 + "\"");
			return;
		} else
		{
			panToObject((com.ofx.repository.SxSpatialObject)sxqueryresultinterface.firstElement());
			return;
		}
	}

	public void panToObject(com.ofx.repository.SxSpatialObject sxspatialobject)
	{
		if(sxspatialobject == null)
		{
			log(" panToObject failed, so is null ");
			return;
		}
		com.ofx.geometry.SxDoublePoint sxdoublepoint = sxspatialobject.getGeometry().getCenter();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(0, 0);
		try
		{
			if(getMapProjectionEnabled())
			{
				getDBProjection().unproject(sxdoublepoint, sxdoublepoint1);
				getMapProjection().project(sxdoublepoint1, sxdoublepoint1);
				if(sxdoublepoint1.isNaN())
					throw new SxProjectionException("Invalid pan point");
			} else
			{
				sxdoublepoint1 = sxdoublepoint;
			}
			getMapCanvas().setCenter(sxdoublepoint1);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.panToObject: " + sxprojectionexception);
		}
	}

	public com.ofx.query.SxQueryInterface getQuery()
	{
		if(query == null)
			try
			{
				setupQuery();
			}
			catch(com.ofx.mapViewer.SxInvalidNameException sxinvalidnameexception)
			{
				throw new RuntimeException("Exception opening query session exception: " + sxinvalidnameexception);
			}
		return query;
	}

	protected synchronized void setupQuery()
		throws com.ofx.mapViewer.SxInvalidNameException
	{
		try
		{
			query = (com.ofx.query.SxQueryInterface)com.ofx.service.SxServiceFactory.lookup("Query", getPropertyGroupName());
		}
		catch(java.lang.ClassCastException classcastexception)
		{
			query = startNewQuery();
		}
		catch(com.ofx.service.SxServiceReferenceNotFoundException sxservicereferencenotfoundexception)
		{
			try
			{
				query = (com.ofx.query.SxQueryInterface)com.ofx.service.SxServiceFactory.get("Query");
			}
			catch(com.ofx.service.SxServiceException sxserviceexception)
			{
				query = startNewQuery();
			}
		}
		java.lang.String s = getPropertyGroupName();
		query.openSession(env().getDbType(s), env().getDbName(s), env().getUserID(s), env().getPassword(s), env().getDbURL(s), env().getDriverName(s), env().getAdminName(s));
		if(!query.doesSpatialSchemaExist())
		{
			query = null;
			throw new SxInvalidNameException("Spatial database tables could not be found in the database.");
		} else
		{
			dbProjection = null;
			return;
		}
	}

	private com.ofx.query.SxQueryInterface startNewQuery()
	{
		com.ofx.service.SxServiceDefinition sxservicedefinition = com.ofx.service.SxServiceDefinition.forName("Query");
		if(sxservicedefinition == null)
			sxservicedefinition = com.ofx.service.SxServiceDefinition.createQuery(getPropertyGroupName());
		return (com.ofx.query.SxQueryInterface)com.ofx.service.SxServiceFactory.start(sxservicedefinition);
	}

	public com.ofx.geometry.SxDoublePoint convertDBToLatLong(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(0, 0);
		try
		{
			getDBProjection().unproject(sxdoublepoint, sxdoublepoint1);
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
			return null;
		}
		return sxdoublepoint1;
	}

	public com.ofx.geometry.SxRectangle convertDBToLatLong(com.ofx.geometry.SxRectangle sxrectangle)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(sxrectangle.x, sxrectangle.y);
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(sxrectangle.x + sxrectangle.width, sxrectangle.y + sxrectangle.height);
		com.ofx.geometry.SxDoublePoint sxdoublepoint2 = convertDBToLatLong(sxdoublepoint);
		com.ofx.geometry.SxDoublePoint sxdoublepoint3 = convertDBToLatLong(sxdoublepoint1);
		return new SxRectangle(sxdoublepoint2, sxdoublepoint3);
	}

	public com.ofx.geometry.SxDoublePoint convertMapToLatLong(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(0, 0);
		try
		{
			getMapProjection().unproject(sxdoublepoint, sxdoublepoint1);
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
			return null;
		}
		return sxdoublepoint1;
	}

	public com.ofx.geometry.SxRectangle convertMapToLatLong(com.ofx.geometry.SxRectangle sxrectangle)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(sxrectangle.x, sxrectangle.y);
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(sxrectangle.x + sxrectangle.width, sxrectangle.y + sxrectangle.height);
		com.ofx.geometry.SxDoublePoint sxdoublepoint2 = convertMapToLatLong(sxdoublepoint);
		com.ofx.geometry.SxDoublePoint sxdoublepoint3 = convertMapToLatLong(sxdoublepoint1);
		return new SxRectangle(sxdoublepoint2, sxdoublepoint3);
	}

	public com.ofx.geometry.SxDoublePoint convertLatLongToMap(double d, double d1)
	{
		return convertLatLongToMap(new SxDoublePoint(d1, d));
	}

	public com.ofx.geometry.SxDoublePoint convertLatLongToMap(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.projection.SxProjectionInterface sxprojectioninterface = getMapProjection();
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(0, 0);
		try
		{
			sxprojectioninterface.project(sxdoublepoint, sxdoublepoint1);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log(sxprojectionexception);
			return null;
		}
		return sxdoublepoint1;
	}

	public com.ofx.geometry.SxRectangle convertLatLongToMap(com.ofx.geometry.SxRectangle sxrectangle)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = convertLatLongToMap(sxrectangle.y, sxrectangle.x);
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = convertLatLongToMap(sxrectangle.y + sxrectangle.height, sxrectangle.x + sxrectangle.width);
		return new SxRectangle(sxdoublepoint, sxdoublepoint1);
	}

	public com.ofx.query.SxQueryResultInterface findInPointBuffer(java.lang.String s, double d, java.lang.String s1, java.lang.String s2)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = getObjectWithId(s1, s2);
		if((sxqueryresultinterface != null) & (!sxqueryresultinterface.isEmpty()))
		{
			java.lang.Object obj = sxqueryresultinterface.firstElement();
			if(obj != null && (obj instanceof com.ofx.repository.SxSpatialObject))
			{
				com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)obj;
				com.ofx.geometry.SxGeometryInterface sxgeometryinterface = sxspatialobject.getGeometry();
				com.ofx.base.SxDistance sxdistance = new SxDistance(d, getUnits());
				com.ofx.geometry.SxCircle sxcircle = new SxCircle(sxgeometryinterface.getCenter(), sxdistance.convertTo(203));
				return findInGeometry(s, sxcircle);
			}
		}
		return new SxQueryResultOverVector(new Vector(1), s, getQuery());
	}

	public void setMapCanvas(com.ofx.mapViewer.SxMapCanvas sxmapcanvas)
	{
		mapCanvas = sxmapcanvas;
	}

	public com.ofx.mapViewer.SxMapCanvas getMapCanvas()
	{
		if(mapCanvas == null)
		{
			mapCanvas = new SxMapCanvas();
			mapCanvas.setMapViewer(this);
			mapCanvas.addStateChangeListener(this);
			setMapCanvas(mapCanvas);
		}
		return mapCanvas;
	}

	public void modifyLabel(java.lang.String s, java.lang.String s1, java.lang.String s2)
	{
		com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)getObject(s, s1);
		sxspatialobject.setLabel(s2.trim());
		fireExternalEvent(sxspatialobject, 104, 256);
	}

	public void movePointObject(java.lang.String s, java.lang.String s1, double d, double d1)
	{
		java.util.Vector vector = new Vector(1);
		com.ofx.geometry.SxDoublePoint sxdoublepoint = convertLatLongToDB(d1, d);
		com.ofx.geometry.SxPoint sxpoint = new SxPoint(sxdoublepoint);
		com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)getObject(s, s1);
		if(sxspatialobject == null)
		{
			log("SxMapViewer.movePointObject - object not found, className: \"" + s + "\" id: " + "\"" + s1 + "\"");
		} else
		{
			sxspatialobject.setGeometry(sxpoint);
			vector.addElement(sxspatialobject);
		}
		fireExternalEvent(vector, 104, 204);
	}

	public void movePointObjects(java.lang.String as[], java.lang.String as1[], double ad[], double ad1[])
	{
		java.util.Vector vector = new Vector(as1.length);
		for(int i = 0; i < as1.length; i++)
		{
			com.ofx.geometry.SxDoublePoint sxdoublepoint = convertLatLongToDB(ad1[i], ad[i]);
			com.ofx.geometry.SxPoint sxpoint = new SxPoint(sxdoublepoint);
			com.ofx.repository.SxSpatialObject sxspatialobject = (com.ofx.repository.SxSpatialObject)getObject(as[i], as1[i]);
			if(sxspatialobject == null)
			{
				log("SxMapViewer.movePointObject - object not found, className: \"" + as[i] + "\" id: " + "\"" + as1[i] + "\"");
			} else
			{
				sxspatialobject.setGeometry(sxpoint);
				vector.addElement(sxspatialobject);
			}
		}

		fireExternalEvent(vector, 104, 204);
	}

	public void deleteMapObject(java.lang.String s, java.lang.String s1)
	{
		java.util.Vector vector = new Vector(1);
		com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface = getObject(s, s1);
		if(sxqueriableobjectinterface == null)
		{
			log("SxMapViewer.deleteMapObjects - No object found to delete. ");
			log("Verify spatial className of: \"" + s + "\".  Also verify spatial object id: " + s1);
			return;
		}
		boolean flag = false;
		try
		{
			flag = getQuery().remove(sxqueriableobjectinterface);
			vector.addElement(sxqueriableobjectinterface);
		}
		catch(java.lang.Exception exception)
		{
			flag = false;
			log(exception);
		}
		if(!flag)
			log("SxMapViewer.deleteMapObject failed - possible bad spatial className");
		fireExternalEvent(vector, 103, 204);
	}

	public void deleteMapObjects(java.lang.String s, java.util.Vector vector)
	{
		com.ofx.query.SxQueryResultInterface sxqueryresultinterface = getObjectsWithIDs(s, vector);
		java.util.Vector vector1 = sxqueryresultinterface.asVector();
		if(vector1.isEmpty())
		{
			log("SxMapViewer.deleteMapObjects - No objects found to delete. ");
			log("Verify spatial className of: \"" + s + "\". Also verify spatial object ids ");
			return;
		}
		boolean flag = false;
		try
		{
			Object obj = null;
			for(java.util.Enumeration enumeration = vector1.elements(); enumeration.hasMoreElements();)
			{
				com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface = (com.ofx.query.SxQueriableObjectInterface)enumeration.nextElement();
				flag = getQuery().remove(sxqueriableobjectinterface);
			}

		}
		catch(java.lang.Exception exception)
		{
			flag = false;
			vector1 = new Vector(0);
			log(exception);
		}
		if(!flag)
			log("SxMapViewer.deleteMapObjects failed - possible bad spatial className");
		fireExternalEvent(vector1, 103, 204);
	}

	public void deleteMapObjects(java.lang.String s, java.lang.String as[])
	{
		java.util.Vector vector = new Vector(as.length);
		for(int i = 0; i < as.length; i++)
			vector.addElement(as[i]);

		deleteMapObjects(s, vector);
	}

	private com.ofx.base.SxEvent sxEvent(int i, int j)
	{
		return (new SxEvent(this)).setState(i).setContext(j);
	}

	public com.ofx.base.SxStateChangeSupport getExternalListeners()
	{
		if(externalListeners == null)
			externalListeners = new SxStateChangeSupport(this);
		return externalListeners;
	}

	public com.ofx.base.SxStateChangeSupport getInternalListeners()
	{
		if(internalListeners == null)
			internalListeners = new SxStateChangeSupport(this);
		return internalListeners;
	}

	public double convertDegrees(double d)
	{
		double d1 = -d + 90D;
		return (d1 * 3.1415926535897931D) / 180D;
	}

	public void setMapBackgroundColor(java.awt.Color color)
	{
		com.ofx.mapViewer.SxColorBackgroundLayer sxcolorbackgroundlayer = (com.ofx.mapViewer.SxColorBackgroundLayer)getNamedLayer("COLOR BACKGROUND");
		if(sxcolorbackgroundlayer != null)
		{
			sxcolorbackgroundlayer.setColor(color);
			sxcolorbackgroundlayer.postDirtyEvent();
			sxcolorbackgroundlayer.postPaintEvent();
		}
	}

	public void setImageConverter(com.ofx.base.SxImageConverterInterface sximageconverterinterface)
	{
		imageConverter = sximageconverterinterface;
	}

	public com.ofx.base.SxImageConverterInterface getImageConverter()
	{
		return imageConverter;
	}

	public java.awt.Image getImage()
	{
		if(getMapLayerRegistry() == null)
			return null;
		else
			return getMapLayerRegistry().getCache();
	}

	public byte[] getImageBytes()
	{
		return getImageBytes(getMapCanvas().getWidth(), getMapCanvas().getHeight());
	}

	public byte[] getImageBytes(int i, int j)
	{
		return getMapLayerRegistry().getImageBytes(getImageConverter(), i, j);
	}

	public void writeImage(java.lang.String s)
	{
		getMapLayerRegistry().writeImage(getImageConverter(), s);
	}

	public void setPropertyGroupName(java.lang.String s)
	{
		propertyGroupName = s;
	}

	public java.lang.String getPropertyGroupName()
	{
		if(propertyGroupName == null)
			setPropertyGroupName("DEFAULT");
		return propertyGroupName;
	}

	public synchronized void release()
	{
		releaseMapResources();
		externalListeners = null;
		closeSession();
		setImageConverter(null);
	}

	public void addMouseListener(java.awt.event.MouseListener mouselistener)
	{
		getMapCanvas().addMouseListener(mouselistener);
	}

	public void addMouseMotionListener(java.awt.event.MouseMotionListener mousemotionlistener)
	{
		getMapCanvas().addMouseMotionListener(mousemotionlistener);
	}

	public void removeMouseListener(java.awt.event.MouseListener mouselistener)
	{
		getMapCanvas().removeMouseListener(mouselistener);
	}

	public void removeMouseMotionListener(java.awt.event.MouseMotionListener mousemotionlistener)
	{
		getMapCanvas().removeMouseMotionListener(mousemotionlistener);
	}

	public com.ofx.geometry.SxGeometryFactory getGeometryFactory(com.ofx.projection.SxProjectionInterface sxprojectioninterface)
	{
		return new SxGeometryFactory(sxprojectioninterface, getDBProjection());
	}

	public com.ofx.repository.SxSpatialObject createSpatialObject(java.lang.String s, java.lang.String s1, java.lang.String s2, com.ofx.geometry.SxGeometryInterface sxgeometryinterface)
	{
		try
		{
			com.ofx.repository.SxClass sxclass = getSxClass(s);
			if(sxclass.getDimension() != sxgeometryinterface.getDimension())
			{
				log("SxMapViewer.createSpatialObject failed -- incompatible class/object geometries");
				return null;
			}
			com.ofx.repository.SxSpatialObject sxspatialobject = new SxSpatialObject(sxclass, s1);
			sxspatialobject.setGeometry(sxgeometryinterface);
			sxspatialobject.setLabel(s2.trim());
			boolean flag = getQuery().add(sxspatialobject);
			if(!flag)
			{
				log("SxMapViewer.createSpatialObject failed -- possible duplicate id: " + s1);
				return null;
			} else
			{
				return sxspatialobject;
			}
		}
		catch(java.lang.Exception exception)
		{
			log(exception);
		}
		return null;
	}

	public java.util.Vector createSpatialObjects(java.lang.String s, java.lang.String as[], java.lang.String as1[], com.ofx.geometry.SxGeometryInterface asxgeometryinterface[])
	{
		com.ofx.repository.SxClass sxclass = getSxClass(s);
		for(int i = 0; i < asxgeometryinterface.length; i++)
			if(sxclass.getDimension() != asxgeometryinterface[i].getDimension())
			{
				log("SxMapViewer.createSpatialObjects failed -- incompatible class/object geometries");
				return null;
			}

		java.util.Vector vector = new Vector(as.length);
		for(int j = 0; j < as.length; j++)
		{
			com.ofx.repository.SxSpatialObject sxspatialobject = createSpatialObject(s, as[j], as1[j], asxgeometryinterface[j]);
			if(sxspatialobject != null)
				vector.addElement(sxspatialobject);
		}

		return vector;
	}

	public void addTransient(com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface)
	{
		getQuery().add(sxqueriableobjectinterface);
	}

	public void deleteTransient(com.ofx.query.SxQueriableObjectInterface sxqueriableobjectinterface)
	{
		getQuery().remove(sxqueriableobjectinterface);
	}

	public void deleteAllTransients(java.lang.String s)
	{
		getQuery().deleteAllTransients(s);
	}

	public void addTransientSpatialClass(com.ofx.repository.SxClass sxclass)
	{
		getQuery().add(sxclass);
	}

	public void deleteTransientSpatialClass(com.ofx.repository.SxClass sxclass)
	{
		getQuery().remove(sxclass);
	}

	public com.ofx.query.SxTransientObjectManager getTransientObjectManager()
	{
		return getQuery().getTransientObjectManager();
	}

	public void setTransientObjectManager(com.ofx.query.SxTransientObjectManager sxtransientobjectmanager)
	{
		getQuery().setTransientObjectManager(sxtransientobjectmanager);
	}

	public void printLayerHierarchy()
	{
		getMapLayerRegistry().printLayerHierarchy("    ");
	}

	public void printDetailedLayerHierarchy()
	{
		getMapLayerRegistry().printDetailedLayerHierarchy("    ");
	}

	public boolean getMapProjectionEnabled()
	{
		if(getGeographicPanning())
		{
			return true;
		} else
		{
			com.ofx.projection.SxProjectionInterface sxprojectioninterface = getDBProjection();
			com.ofx.projection.SxProjectionInterface sxprojectioninterface1 = getMapProjection();
			return !sxprojectioninterface.equals(sxprojectioninterface1);
		}
	}

	public com.ofx.projection.SxProjectionInterface getMapProjection()
	{
		com.ofx.projection.SxProjectionInterface sxprojectioninterface = null;
		if(getMap() != null)
			sxprojectioninterface = getMap().getProjection();
		return sxprojectioninterface;
	}

	public com.ofx.projection.SxProjectionInterface getDBProjection()
	{
		if(dbProjection == null && getQuery() != null)
			dbProjection = getQuery().getDatabaseProjection();
		return dbProjection;
	}

	public com.ofx.geometry.SxDoublePoint convertLatLongToDB(com.ofx.geometry.SxDoublePoint sxdoublepoint)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = new SxDoublePoint(sxdoublepoint.x, sxdoublepoint.y);
		try
		{
			getDBProjection().project(sxdoublepoint1, sxdoublepoint1);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.convertLatLongToDB (" + sxdoublepoint + "): " + sxprojectionexception);
		}
		return sxdoublepoint1;
	}

	public com.ofx.geometry.SxDoublePoint convertLatLongToDB(double d, double d1)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = new SxDoublePoint(d1, d);
		try
		{
			getDBProjection().project(sxdoublepoint, sxdoublepoint);
		}
		catch(com.ofx.projection.SxProjectionException sxprojectionexception)
		{
			if(logProjectionExceptions)
				log("SxMapViewer.convertLatLongToDB (" + d + ", " + d1 + "): " + sxprojectionexception);
		}
		return sxdoublepoint;
	}

	public com.ofx.geometry.SxRectangle convertLatLongToDB(com.ofx.geometry.SxRectangle sxrectangle)
	{
		com.ofx.geometry.SxDoublePoint sxdoublepoint = convertLatLongToDB(sxrectangle.y, sxrectangle.x);
		com.ofx.geometry.SxDoublePoint sxdoublepoint1 = convertLatLongToDB(sxrectangle.y + sxrectangle.height, sxrectangle.x + sxrectangle.width);
		return new SxRectangle(sxdoublepoint, sxdoublepoint1);
	}

	public void setLogProjectionExceptions(boolean flag)
	{
		logProjectionExceptions = flag;
	}

	public boolean getLogProjectionExceptions()
	{
		return logProjectionExceptions;
	}

	public void printMap()
	{
		boolean flag = false;
		try
		{
			java.lang.Class.forName("com.bristol.jpclient.JpPrintJob");
			flag = true;
		}
		catch(java.lang.ClassNotFoundException classnotfoundexception) { }
		if(flag)
			try
			{
				com.ofx.printing.SxMapPrinter.getInstance("com.ofx.printing.SxJpMapPrinter").print(this);
			}
			catch(java.lang.Exception exception)
			{
				exception.printStackTrace();
			}
		else
			doPrint();
	}

	private void doPrint()
	{
		java.awt.print.PrinterJob printerjob = java.awt.print.PrinterJob.getPrinterJob();
		java.awt.print.PageFormat pageformat = printerjob.defaultPage();
		pageformat = printerjob.pageDialog(pageformat);
		printerjob.setPrintable(new MapPrintable(), pageformat);
		if(printerjob.printDialog())
			try
			{
				printerjob.print();
			}
			catch(java.awt.print.PrinterException printerexception)
			{
				printerexception.printStackTrace();
			}
	}

	protected java.util.Vector foreGroundClasses;
	protected java.util.Vector backGroundClasses;
	protected com.ofx.mapViewer.SxMapLayerRegistry mapLayerRegistry;
	protected com.ofx.mapViewer.SxBinLayerRegistry binLayerRegistry;
	protected java.lang.String mapName;
	protected java.lang.String propertyGroupName;
	protected int panPercentage;
	protected boolean geographicPanning;
	protected int units;
	protected double zoomFactor;
	protected int selectionTolerance;
	protected boolean allowShiftSelect;
	protected boolean isImageServer;
	protected java.awt.Component waitComponent;
	protected java.awt.Cursor waitCursor;
	protected com.ofx.repository.SxMap map;
	protected com.ofx.base.SxStateChangeSupport externalListeners;
	protected com.ofx.base.SxStateChangeSupport internalListeners;
	protected com.ofx.mapViewer.SxMapCanvas mapCanvas;
	protected com.ofx.query.SxQueryInterface query;
	protected com.ofx.mapViewer.SxMapLayerEventQueue eventQueue;
	protected com.ofx.base.SxImageConverterInterface imageConverter;
	protected boolean logProjectionExceptions;
	private com.ofx.projection.SxProjectionInterface dbProjection;
	private static final boolean debug = false;
}
