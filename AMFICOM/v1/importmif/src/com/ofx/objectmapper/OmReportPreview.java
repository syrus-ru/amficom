// Decompiled by Jad v1.5.7f. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: fullnames lnc 

package com.ofx.objectmapper;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxLogInterface;
import com.ofx.base.SxParameterInterface;
import com.ofx.base.SxStringTokenizer;
import com.ofx.geometry.SxDoublePoint;
import com.ofx.geometry.SxRectangle;
import com.ofx.projection.SxProjection;
import com.ofx.projection.SxProjectionEllipsoid;
import com.ofx.projection.SxProjectionException;
import com.ofx.projection.SxProjectionGeoLatLong;
import com.ofx.projection.SxProjectionInterface;
import com.ofx.query.SxQueryInterface;
import com.ofx.query.SxQueryResultInterface;
import com.ofx.query.SxQueryRetrievalInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxDataSource;
import com.ofx.repository.SxExternalDataDef;
import com.ofx.repository.SxMap;
import com.ofx.repository.SxRaster;
import com.ofx.repository.SxSymbology;
import com.ofx.repository.SxTextSpec;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.PrintJob;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.NoSuchElementException;
import java.util.Vector;
import symantec.itools.awt.BorderPanel;

// Referenced classes of package com.ofx.objectmapper:
//			OmFrame, OmSpatialDataReport

public class OmReportPreview extends com.ofx.objectmapper.OmFrame
	implements java.awt.event.ActionListener, java.awt.event.WindowListener
{

	public void setFilename(java.lang.String s)
	{
		filename = s;
		if(filename != null)
			printReport();
	}

	public void setDbName(java.lang.String s)
	{
		dbName = s;
	}

	private boolean routingInstalled()
	{
		boolean flag = true;
		try
		{
			java.lang.Class.forName("com.ofx.routing.objectfx.SxRoutingEngine");
		}
		catch(java.lang.ClassNotFoundException classnotfoundexception)
		{
			flag = false;
		}
		return flag;
	}

	private void printReport()
	{
		java.lang.String s = getHeaderString();
		previewText.append(s);
		byte abyte0[] = s.getBytes();
		java.lang.String s1 = getMapsString();
		byte abyte1[] = new byte[s1.length()];
		if(rptWin.mapsCB.getState())
		{
			previewText.append(s1);
			abyte1 = s1.getBytes();
		}
		java.lang.String s2 = getClassesString();
		byte abyte2[] = new byte[s2.length()];
		if(rptWin.mapObjectsCB.getState())
		{
			previewText.append(s2);
			abyte2 = s2.getBytes();
		}
		java.lang.String s3 = getDataSourcesString();
		byte abyte3[] = new byte[s3.length()];
		if(rptWin.sourcesCB.getState())
		{
			previewText.append(s3);
			abyte3 = s3.getBytes();
		}
		java.lang.String s4 = getRastersString();
		byte abyte4[] = new byte[s4.length()];
		if(rptWin.rastersCB.getState())
		{
			previewText.append(s4);
			abyte4 = s4.getBytes();
		}
		java.lang.String s5 = getSymbolsString();
		byte abyte5[] = new byte[s5.length()];
		if(rptWin.symbolsCB.getState())
		{
			previewText.append(s5);
			abyte5 = s5.getBytes();
		}
		java.lang.String s6 = getLabelsString();
		byte abyte6[] = new byte[s6.length()];
		if(rptWin.labelsCB.getState())
		{
			previewText.append(s6);
			abyte6 = s6.getBytes();
		}
		if(rptWin.fileCB.getState())
			try
			{
				java.io.FileOutputStream fileoutputstream = new FileOutputStream(filename);
				fileoutputstream.write(abyte0);
				if(rptWin.mapsCB.getState())
					fileoutputstream.write(abyte1);
				if(rptWin.mapObjectsCB.getState())
					fileoutputstream.write(abyte2);
				if(rptWin.sourcesCB.getState())
					fileoutputstream.write(abyte3);
				if(rptWin.rastersCB.getState())
					fileoutputstream.write(abyte4);
				if(rptWin.symbolsCB.getState())
					fileoutputstream.write(abyte5);
				if(rptWin.labelsCB.getState())
					fileoutputstream.write(abyte6);
				fileoutputstream.close();
			}
			catch(java.io.IOException ioexception)
			{
				com.ofx.base.SxEnvironment.log().println(ioexception);
			}
	}

	private java.lang.String getHeaderString()
	{
		java.util.Date date = new Date();
		java.lang.String s = "SpatialFX\t\t\tSpatial Data Report\t\t\t" + date.toString();
		s = s + endOfLine + endOfLine;
		if(dbName != null)
		{
			com.ofx.projection.SxProjectionInterface sxprojectioninterface = getQuery().getDatabaseProjection();
			java.lang.String s1 = sep1 + "*************" + endOfLine + sep1 + " DATABASE" + endOfLine + sep1 + "*************" + endOfLine + sep1 + "DB Name:\t" + dbName + endOfLine;
			s1 = s1 + sep1 + "Projection:\t" + sxprojectioninterface.getName() + endOfLine;
			java.util.Vector vector = sxprojectioninterface.getParameters();
			Object obj = null;
			Object obj1 = null;
			Object obj2 = null;
			for(int i = 0; i < vector.size(); i++)
			{
				com.ofx.base.SxParameterInterface sxparameterinterface = (com.ofx.base.SxParameterInterface)vector.elementAt(i);
				java.lang.String s2 = sxparameterinterface.getPrompt();
				java.lang.String s3 = sxparameterinterface.getStringValue();
				s1 = s1 + sep1 + "\t" + s2 + ":\t" + s3 + endOfLine;
			}

			com.ofx.projection.SxProjectionEllipsoid sxprojectionellipsoid = sxprojectioninterface.getEllipsoid();
			s1 = s1 + sep1 + "\tEllipsoid Name:\t" + sxprojectionellipsoid.getName() + endOfLine;
			s1 = s1 + sep1 + "\t        Major Axis:\t" + sxprojectionellipsoid.getMajorAxis() + endOfLine;
			s1 = s1 + sep1 + "\t        Minor Axis:\t" + sxprojectionellipsoid.getMinorAxis() + endOfLine;
			s1 = s1 + sep1 + "\t        Flattening:\t" + sxprojectionellipsoid.getFlattening() + endOfLine;
			s1 = s1 + sep1 + "\t        Eccentricity:\t" + sxprojectionellipsoid.getEccentricity() + endOfLine;
			s1 = s1 + sep1 + "\t        Units:\t" + sxprojectionellipsoid.getUnitName() + endOfLine;
			s1 = s1 + endOfLine;
			s = s + s1 + separator + endOfLine;
		}
		return s;
	}

	private java.lang.String getMapsString()
	{
		java.lang.String s = sep1 + "*******" + endOfLine + sep1 + " MAPS " + endOfLine + sep1 + "*******" + endOfLine;
		Object obj = null;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxMap.objectIDs(getQuery());
			int i;
			for(i = 0; enumeration.hasMoreElements(); i++)
			{
				java.lang.String s1 = "";
				java.lang.String s2 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxMap sxmap = com.ofx.repository.SxMap.retrieve(s2, getQuery());
				s1 = s1 + sep1 + "Map Name:\t" + s2 + endOfLine;
				java.lang.String s3 = sxmap.getDescription();
				s1 = s1 + sep1 + "Description:\t" + s3 + endOfLine;
				com.ofx.projection.SxProjectionInterface sxprojectioninterface = sxmap.getProjection();
				s1 = s1 + sep1 + "Projection:\t" + sxprojectioninterface.getName() + endOfLine;
				java.util.Vector vector = sxprojectioninterface.getParameters();
				Object obj1 = null;
				Object obj2 = null;
				Object obj3 = null;
				for(int j = 0; j < vector.size(); j++)
				{
					com.ofx.base.SxParameterInterface sxparameterinterface = (com.ofx.base.SxParameterInterface)vector.elementAt(j);
					java.lang.String s4 = sxparameterinterface.getPrompt();
					java.lang.String s5 = sxparameterinterface.getStringValue();
					s1 = s1 + sep1 + "\t" + s4 + ":\t" + s5 + endOfLine;
				}

				com.ofx.projection.SxProjectionEllipsoid sxprojectionellipsoid = sxprojectioninterface.getEllipsoid();
				s1 = s1 + sep1 + "\tEllipsoid Name:\t" + sxprojectionellipsoid.getName() + endOfLine;
				s1 = s1 + sep1 + "\t        Major Axis:\t" + sxprojectionellipsoid.getMajorAxis() + endOfLine;
				s1 = s1 + sep1 + "\t        Minor Axis:\t" + sxprojectionellipsoid.getMinorAxis() + endOfLine;
				s1 = s1 + sep1 + "\t        Flattening:\t" + sxprojectionellipsoid.getFlattening() + endOfLine;
				s1 = s1 + sep1 + "\t        Eccentricity:\t" + sxprojectionellipsoid.getEccentricity() + endOfLine;
				s1 = s1 + sep1 + "\t        Units:\t" + sxprojectionellipsoid.getUnitName() + endOfLine;
				java.lang.String s6 = java.lang.String.valueOf(sxmap.getHighScale());
				java.lang.String s7 = java.lang.String.valueOf(sxmap.getLowScale());
				java.lang.String s8 = java.lang.String.valueOf(sxmap.getInitialScale());
				s1 = s1 + sep1 + "Scales:\t" + endOfLine;
				s1 = s1 + sep1 + "\tHigh:\t" + s6 + "\tLow:\t" + s7 + "\tInitial:\t" + s8 + endOfLine;
				com.ofx.geometry.SxRectangle sxrectangle = sxmap.getExtent();
				com.ofx.geometry.SxDoublePoint sxdoublepoint = sxrectangle.getOrigin();
				com.ofx.geometry.SxDoublePoint sxdoublepoint1 = sxrectangle.getCorner();
				com.ofx.projection.SxProjectionGeoLatLong sxprojectiongeolatlong = new SxProjectionGeoLatLong();
				com.ofx.geometry.SxDoublePoint sxdoublepoint2 = new SxDoublePoint(0, 0);
				try
				{
					com.ofx.projection.SxProjection.transform(sxdoublepoint, sxprojectioninterface, sxprojectiongeolatlong, sxdoublepoint2);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception) { }
				com.ofx.geometry.SxDoublePoint sxdoublepoint3 = new SxDoublePoint(0, 0);
				try
				{
					com.ofx.projection.SxProjection.transform(sxdoublepoint1, sxprojectioninterface, sxprojectiongeolatlong, sxdoublepoint3);
				}
				catch(com.ofx.projection.SxProjectionException sxprojectionexception1) { }
				double d = roundedOff(sxdoublepoint2.x);
				java.lang.String s9 = java.lang.String.valueOf(d);
				d = roundedOff(sxdoublepoint2.y);
				java.lang.String s10 = java.lang.String.valueOf(d);
				d = roundedOff(sxdoublepoint3.x);
				java.lang.String s11 = java.lang.String.valueOf(d);
				d = roundedOff(sxdoublepoint3.y);
				java.lang.String s12 = java.lang.String.valueOf(d);
				s1 = s1 + sep1 + "Extent:" + endOfLine;
				s1 = s1 + sep1 + "\tUpper Left\tx:\t" + s9 + "\ty:\t" + s10 + endOfLine;
				s1 = s1 + sep1 + "\tLower Right\tx:\t" + s11 + "\ty:\t" + s12 + endOfLine;
				s1 = s1 + sep1 + "Foreground Classes: " + endOfLine;
				java.util.Vector vector1 = sxmap.getForegroundClassNames();
				for(java.util.Enumeration enumeration1 = vector1.elements(); enumeration1.hasMoreElements();)
				{
					java.lang.String s13 = (java.lang.String)enumeration1.nextElement();
					s1 = s1 + "\t" + s13 + endOfLine;
				}

				s1 = s1 + sep1 + "Background Classes: " + endOfLine;
				vector1 = sxmap.getBackgroundClassNames();
				for(java.util.Enumeration enumeration2 = vector1.elements(); enumeration2.hasMoreElements();)
				{
					java.lang.String s14 = (java.lang.String)enumeration2.nextElement();
					s1 = "" + s1 + "\t" + s14 + endOfLine;
				}

				s1 = s1 + sep1 + "Raster Backgrounds: " + endOfLine;
				vector1 = sxmap.getRasterBackgroundNames();
				for(java.util.Enumeration enumeration3 = vector1.elements(); enumeration3.hasMoreElements();)
				{
					java.lang.String s15 = (java.lang.String)enumeration3.nextElement();
					s1 = "" + s1 + "\t" + s15 + endOfLine;
				}

				s = s + s1 + sep1 + "------------------------------------------" + endOfLine;
			}

			s = s + sep1 + "Number of maps:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			return s + endOfLine + separator + endOfLine;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	private double roundedOff(double d)
	{
		double d1 = d * 10000000D;
		if(d1 < 0.0D)
			d1 -= 0.5D;
		else
			d1 += 0.5D;
		int i = (int)d1;
		double d2 = i;
		d2 /= 10000000D;
		return d2;
	}

	private java.lang.String getClassesString()
	{
		java.lang.String s = sep1 + "*****************" + endOfLine + sep1 + " MAP OBJECTS " + endOfLine + sep1 + "*****************" + endOfLine;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxClass.objectIDs(getQuery());
			int i = 0;
			while(enumeration.hasMoreElements()) 
			{
				java.lang.String s1 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxClass sxclass = com.ofx.repository.SxClass.retrieve(s1, getQuery());
				if(routingInstalled() || sxclass.getIsUserDefined())
				{
					java.lang.String s2 = "";
					java.lang.String s3 = sxclass.getDescription();
					java.lang.String s4 = "";
					if(sxclass.isPointType())
						s4 = "Point";
					else
					if(sxclass.isPolylineType())
						s4 = "Line";
					else
					if(sxclass.isPolygonType())
						s4 = "Area";
					else
					if(sxclass.isTextType())
						s4 = "Text";
					com.ofx.repository.SxSymbology sxsymbology = sxclass.getSymbology();
					java.lang.String s5 = sxsymbology.getID();
					java.lang.String s6 = java.lang.String.valueOf(sxclass.getSymbolMinScale());
					java.lang.String s7 = java.lang.String.valueOf(sxclass.getSymbolMaxScale());
					com.ofx.repository.SxTextSpec sxtextspec = sxclass.getLabelSpec();
					java.lang.String s8 = sxtextspec.getID();
					java.lang.String s9 = java.lang.String.valueOf(sxclass.getTextMinScale());
					java.lang.String s10 = java.lang.String.valueOf(sxclass.getTextMaxScale());
					java.lang.String s11 = java.lang.String.valueOf(sxclass.getNeedsGeneratedIdValues());
					com.ofx.repository.SxDataSource sxdatasource = sxclass.getDataSource();
					com.ofx.repository.SxExternalDataDef sxexternaldatadef = sxclass.getExternalDataDef();
					s2 = s2 + sep1 + "Class Name:\t" + s1 + endOfLine;
					s2 = s2 + sep1 + "Description:\t" + s3 + endOfLine;
					s2 = s2 + sep1 + "Geometry:\t" + s4 + endOfLine;
					s2 = s2 + sep1 + "Symbology:\t" + s5 + endOfLine;
					s2 = s2 + sep1 + "\tLow Scale:\t" + s6 + "\tHigh Scale:\t" + s7 + endOfLine;
					s2 = s2 + sep1 + "Label Spec:\t" + s8 + endOfLine;
					s2 = s2 + sep1 + "\tLow Scale:\t" + s9 + "\tHigh Scale:\t" + s10 + endOfLine;
					s2 = s2 + sep1 + "Generated Identifier:   " + s11 + endOfLine;
					if(sxdatasource != null && sxexternaldatadef != null)
					{
						s2 = s2 + sep1 + "External Source:\t" + sxdatasource.getID() + endOfLine;
						s2 = s2 + sep1 + "Data Definition:\t" + (java.lang.String)sxexternaldatadef.getIdentifier() + endOfLine;
					}
					s = s + s2 + sep1 + "------------------------------------------";
					if(rptWin.countCB.getState())
					{
						java.lang.String s12 = sxclass.getSpatialObjectQueryClassification();
						int j = getQuery().getObjects(s12).size();
						s = s + endOfLine;
						s = s + sep1 + "Database contains " + java.lang.String.valueOf(j) + " instances of " + s1 + endOfLine;
						s = s + sep1 + "--------------------------------------------------------------------" + endOfLine + endOfLine;
					} else
					{
						s = s + endOfLine;
					}
					i++;
				}
			}
			s = s + sep1 + "Number of Map Object Classes:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			s = s + endOfLine + separator + endOfLine;
			return s;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	private java.lang.String getDataSourcesString()
	{
		java.lang.String s = sep1 + "*****************" + endOfLine + sep1 + " DATA SOURCES    " + endOfLine + sep1 + "*****************" + endOfLine;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxDataSource.objectIDs(getQuery());
			int i;
			for(i = 0; enumeration.hasMoreElements(); i++)
			{
				java.lang.String s1 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxDataSource sxdatasource = com.ofx.repository.SxDataSource.retrieve(s1, getQuery());
				java.lang.String s2 = "";
				java.lang.String s3 = sxdatasource.getFormatName();
				java.lang.String s4 = sxdatasource.getHostAddress();
				java.lang.String s5 = sxdatasource.getDataDirectory();
				s2 = s2 + sep1 + "Data Source Name:\t" + s1 + endOfLine;
				s2 = s2 + sep1 + "Host Address:\t\t" + s4 + endOfLine;
				s2 = s2 + sep1 + "Format:\t\t" + s3 + endOfLine;
				s2 = s2 + sep1 + "Data Directory:\t\t" + s5 + endOfLine;
				s = s + s2 + sep1 + "------------------------------------------" + endOfLine;
			}

			s = s + sep1 + "Number of Data Sources:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			s = s + endOfLine + separator + endOfLine;
			return s;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	private java.lang.String getRastersString()
	{
		java.lang.String s = sep1 + "***********************" + endOfLine + sep1 + " RASTER BACKGROUNDS    " + endOfLine + sep1 + "***********************" + endOfLine;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxRaster.objectIDs(getQuery());
			int i;
			for(i = 0; enumeration.hasMoreElements(); i++)
			{
				java.lang.String s1 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxRaster sxraster = com.ofx.repository.SxRaster.retrieve(s1, getQuery());
				java.lang.String s2 = "";
				java.lang.String s3 = sxraster.getDataSource().getID();
				java.lang.String s4 = java.lang.String.valueOf(sxraster.getMinScale());
				java.lang.String s5 = java.lang.String.valueOf(sxraster.getMaxScale());
				java.lang.String s6 = sxraster.getExternalDataDef().getLayerName();
				com.ofx.projection.SxProjectionInterface sxprojectioninterface = sxraster.getNativeProjection();
				java.lang.String s7 = sxprojectioninterface == null ? "Unknown" : sxprojectioninterface.toString();
				com.ofx.geometry.SxRectangle sxrectangle = com.ofx.projection.SxProjection.getBoundsOfTransform(sxraster.getExtent(), 8, getQuery().getDatabaseProjection(), null);
				java.lang.String s8 = "Upper Left: " + sxrectangle.getTopLeft() + "  Lower Right: " + sxrectangle.getBottomRight();
				s2 = s2 + sep1 + "Raster Background:\t" + s1 + endOfLine;
				s2 = s2 + sep1 + "External Name:\t\t" + s6 + endOfLine;
				s2 = s2 + sep1 + "Bounds:\t\t\t" + s8 + endOfLine;
				s2 = s2 + sep1 + "Projection:\t\t" + s7 + endOfLine;
				s2 = s2 + sep1 + "Low Display Scale:\t" + s4 + endOfLine;
				s2 = s2 + sep1 + "High Display Scale:\t" + s5 + endOfLine;
				s = s + s2 + sep1 + "------------------------------------------" + endOfLine;
			}

			s = s + sep1 + "Number of Raster Backgrounds:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			s = s + endOfLine + separator + endOfLine;
			return s;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	private java.lang.String getSymbolsString()
	{
		java.lang.String s = sep1 + "*********************" + endOfLine + sep1 + " SYMBOL LIBRARY" + endOfLine + sep1 + "*********************" + endOfLine;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxSymbology.objectIDs(getQuery());
			int i;
			for(i = 0; enumeration.hasMoreElements(); i++)
			{
				java.lang.String s1 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxSymbology sxsymbology = com.ofx.repository.SxSymbology.retrieve(s1, getQuery());
				java.lang.String s2 = "";
				java.lang.String s3 = java.lang.String.valueOf(sxsymbology.getSymbolSize());
				java.lang.String s4 = java.lang.String.valueOf(sxsymbology.getLineWidth());
				java.lang.String s5 = sxsymbology.getShape();
				java.lang.String s6 = java.lang.String.valueOf(sxsymbology.getIsDisplayable());
				java.lang.String s7 = java.lang.String.valueOf(sxsymbology.isFilled());
				int j = sxsymbology.getOutlineColorRGB();
				int k = sxsymbology.getFillColorRGB();
				int l = j >> 16 & 0xff;
				int i1 = j >> 8 & 0xff;
				int j1 = j >> 0 & 0xff;
				int k1 = k >> 16 & 0xff;
				int l1 = k >> 8 & 0xff;
				int i2 = k >> 0 & 0xff;
				java.lang.String s8 = java.lang.String.valueOf(l);
				java.lang.String s9 = java.lang.String.valueOf(i1);
				java.lang.String s10 = java.lang.String.valueOf(j1);
				java.lang.String s11 = java.lang.String.valueOf(k1);
				java.lang.String s12 = java.lang.String.valueOf(l1);
				java.lang.String s13 = java.lang.String.valueOf(i2);
				s2 = s2 + sep1 + "Name:\t\t" + s1 + endOfLine;
				s2 = s2 + sep1 + "Shape:\t\t" + s5 + endOfLine;
				s2 = s2 + sep1 + "Size:\t\t" + s3 + "\tLine Width:\t" + s4 + endOfLine;
				s2 = s2 + sep1 + "Is Filled:\t" + s7 + "\tIs Visible:\t\t" + s6 + endOfLine;
				s2 = s2 + sep1 + "Color:\t\tLine (RGB):\t" + s8 + "\t" + s9 + "\t" + s10 + "\tFill (RGB):\t" + s11 + "\t" + s12 + "\t" + s13 + endOfLine;
				s2 = s2 + sep1 + "Fill Opacity:\t";
				if(sxsymbology.getFillColor() != null)
					s2 = s2 + java.lang.String.valueOf(sxsymbology.getFillColor().getAlpha());
				else
					s2 = s2 + sep1 + "nill";
				s2 = s2 + sep1 + endOfLine;
				s2 = s2 + sep1 + "Outline Opacity:\t";
				if(sxsymbology.getOutlineColor() != null)
					s2 = s2 + java.lang.String.valueOf(sxsymbology.getOutlineColor().getAlpha());
				else
					s2 = s2 + sep1 + "nill";
				s2 = s2 + sep1 + endOfLine;
				java.lang.String s14 = com.ofx.repository.SxSymbology.getPatternNameByID(sxsymbology.getFillPatternID());
				if(s14 == null)
					s14 = "Solid";
				s2 = s2 + sep1 + "Fill Pattern:\t" + s14 + endOfLine;
				s2 = s2 + sep1 + "Outline Width:\t" + sxsymbology.getOutlineWidth() + endOfLine;
				s2 = s2 + sep1 + "Is Dashed: \t" + new Boolean(sxsymbology.getIsLineDashed()) + "\tLine End Style:\t" + com.ofx.repository.SxSymbology.getLineEndStyleNameByID(sxsymbology.getLineEndStyle()) + "\tLine Join Style:\t" + com.ofx.repository.SxSymbology.getLineJoinStyleNameByID(sxsymbology.getLineJoinStyle()) + "\tLine Miter Limit:\t" + sxsymbology.getLineMiterLimit() + "\tLine Dash Phase:\t" + sxsymbology.getLineDashPhase() + endOfLine;
				float af[] = sxsymbology.getLineDashArray();
				java.lang.String s15 = "";
				for(int j2 = 0; j2 < af.length; j2++)
					s15 = s15 + java.lang.String.valueOf(af[j2]) + ", ";

				s2 = s2 + sep1 + "Dash Array: \t" + s15 + endOfLine;
				s = s + s2 + sep1 + "--------------------------------------------------------------------" + endOfLine;
			}

			s = s + sep1 + "Number of Symbols:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			s = s + endOfLine + separator + endOfLine;
			return s;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	private java.lang.String getLabelsString()
	{
		java.lang.String s = sep1 + "************************" + endOfLine + sep1 + " LABEL SPECIFICATIONS" + endOfLine + sep1 + "************************" + endOfLine;
		try
		{
			java.util.Enumeration enumeration = com.ofx.repository.SxTextSpec.objectIDs(getQuery());
			int i;
			for(i = 0; enumeration.hasMoreElements(); i++)
			{
				java.lang.String s1 = (java.lang.String)enumeration.nextElement();
				com.ofx.repository.SxTextSpec sxtextspec = com.ofx.repository.SxTextSpec.retrieve(s1, getQuery());
				java.lang.String s2 = "";
				java.lang.String s3 = java.lang.String.valueOf(sxtextspec.getIsDisplayable());
				java.lang.String s4 = sxtextspec.getDescription();
				java.lang.String s5 = sxtextspec.getFontName();
				java.lang.String s6 = java.lang.String.valueOf(sxtextspec.getFontSize());
				java.lang.String s7 = java.lang.String.valueOf(sxtextspec.getBold());
				java.lang.String s8 = java.lang.String.valueOf(sxtextspec.getItalic());
				java.lang.String s9 = java.lang.String.valueOf(sxtextspec.getHorizontalOffset());
				java.lang.String s10 = java.lang.String.valueOf(sxtextspec.getVerticalOffset());
				java.lang.String s11 = java.lang.String.valueOf(sxtextspec.getIsDisplayable());
				java.awt.Color color = sxtextspec.getColor();
				java.awt.Color color1 = sxtextspec.getBackingFillColor();
				java.awt.Color color2 = sxtextspec.getBackingOutlineColor();
				java.lang.String s12 = "nil";
				java.lang.String s13 = "nil";
				java.lang.String s14 = "nil";
				java.lang.String s15 = "nil";
				java.lang.String s16 = "nil";
				java.lang.String s17 = "nil";
				java.lang.String s18 = "nil";
				java.lang.String s19 = "nil";
				if(color1 != null)
				{
					int j = color1.getRGB();
					int i1 = j >> 16 & 0xff;
					int l1 = j >> 8 & 0xff;
					int k2 = j >> 0 & 0xff;
					s12 = java.lang.String.valueOf(i1);
					s13 = java.lang.String.valueOf(l1);
					s14 = java.lang.String.valueOf(k2);
					s18 = java.lang.String.valueOf(color1.getAlpha());
				}
				if(color2 != null)
				{
					int k = color2.getRGB();
					int j1 = k >> 16 & 0xff;
					int i2 = k >> 8 & 0xff;
					int l2 = k >> 0 & 0xff;
					s15 = java.lang.String.valueOf(j1);
					s16 = java.lang.String.valueOf(i2);
					s17 = java.lang.String.valueOf(l2);
					s19 = java.lang.String.valueOf(color2.getAlpha());
				}
				int l = color.getRGB();
				int k1 = l >> 16 & 0xff;
				int j2 = l >> 8 & 0xff;
				int i3 = l >> 0 & 0xff;
				java.lang.String s20 = java.lang.String.valueOf(k1);
				java.lang.String s21 = java.lang.String.valueOf(j2);
				java.lang.String s22 = java.lang.String.valueOf(i3);
				s2 = s2 + sep1 + "Name:\t\t" + s1 + endOfLine;
				s2 = s2 + sep1 + "\tFont:\t" + s5 + "\tSize:\t" + s6 + endOfLine;
				s2 = s2 + sep1 + "\tIs Bold:\t" + s7 + "\tIs Italic:\t" + s8 + endOfLine;
				s2 = s2 + sep1 + "\tFont Color:\tRed:\t" + s20 + "\tGreen:\t" + s21 + "\tBlue:\t" + s22 + endOfLine;
				s2 = s2 + sep1 + "\tBacking Line Color:\tRed:\t" + s15 + "\tGreen:\t" + s16 + "\tBlue:\t" + s17 + endOfLine;
				s2 = s2 + sep1 + "\tBacking Line Opacity:\t" + s19 + endOfLine;
				s2 = s2 + sep1 + "\tBacking Fill Color:\tRed:\t" + s12 + "\tGreen:\t" + s13 + "\tBlue:\t" + s14 + endOfLine;
				s2 = s2 + sep1 + "\tBacking Fill Opacity:\t" + s18 + endOfLine;
				s2 = s2 + sep1 + "\tIs Visible:\t" + s3 + endOfLine;
				java.lang.String s23 = null;
				switch(sxtextspec.getUseShields())
				{
				case -2: 
					s23 = "Shields only";
					break;

				case 1: // '\001'
					s23 = "Text only";
					break;

				case -1: 
					s23 = "Text and shields only";
					break;
				}
				s2 = s2 + sep1 + "\t" + s23 + endOfLine;
				s2 = s2 + sep1 + "\tHorizontal Offset:\t" + sxtextspec.getHorizontalOffset() + "\tVertical Offset:\t" + sxtextspec.getVerticalOffset() + endOfLine;
				s = s + s2 + sep1 + "--------------------------------------------------------------------" + endOfLine;
			}

			s = s + sep1 + "Number of Label Specs:\t" + java.lang.String.valueOf(i) + endOfLine + endOfLine;
			s = s + separator;
			return s;
		}
		catch(java.lang.Exception exception)
		{
			com.ofx.base.SxEnvironment.log().println(exception);
		}
		return " ";
	}

	public com.ofx.query.SxQueryInterface getQuery()
	{
		return com.ofx.base.SxEnvironment.singleton().getQuery();
	}

	public OmReportPreview(java.awt.Frame frame, boolean flag)
	{
		filename = null;
		endOfLine = "\r\n";
		separator = "****************************************************************";
		sep1 = "   ";
		sep2 = "\t";
		dbName = null;
		rptWin = (com.ofx.objectmapper.OmSpatialDataReport)frame;
		setLayout(new BorderLayout(5, 5));
		addNotify();
		setSize(getInsets().left + getInsets().right + 675, getInsets().top + getInsets().bottom + 500);
		setBackground(new Color(0xc0c0c0));
		previewText = new TextArea();
		previewText.setEditable(false);
		previewText.setBounds(getInsets().left + 0, getInsets().top + 0, 582, 425);
		previewText.setBackground(new Color(0xffffff));
		add("Center", previewText);
		setTitle("Database Report Preview");
		buttonPanel = new BorderPanel();
		buttonPanel.setLayout(new FlowLayout(1, 80, 5));
		buttonPanel.setBounds(getInsets().left + 0, getInsets().top + 425, 582, 40);
		try
		{
			buttonPanel.setBevelStyle(0);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception) { }
		try
		{
			buttonPanel.setIPadBottom(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception1) { }
		try
		{
			buttonPanel.setIPadSides(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception2) { }
		try
		{
			buttonPanel.setPaddingRight(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception3) { }
		try
		{
			buttonPanel.setPaddingBottom(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception4) { }
		try
		{
			buttonPanel.setPaddingTop(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception5) { }
		try
		{
			buttonPanel.setPaddingLeft(2);
		}
		catch(java.beans.PropertyVetoException propertyvetoexception6) { }
		add("South", buttonPanel);
		printButton = new Button(" Print ");
		printButton.addActionListener(this);
		buttonPanel.add(printButton);
		closeButton = new Button(" Close ");
		closeButton.addActionListener(this);
		buttonPanel.add(closeButton);
	}

	public OmReportPreview(java.awt.Frame frame, java.lang.String s, boolean flag)
	{
		this(frame, flag);
		setTitle("Database Report Preview");
	}

	public synchronized void setVisible(boolean flag)
	{
		setLocation(150, 30);
		super.setVisible(flag);
	}

	public void windowClosing(java.awt.event.WindowEvent windowevent)
	{
		setVisible(false);
	}

	private static java.awt.Container componentRoot(java.awt.Component component)
	{
		java.awt.Container container = component.getParent();
		if(container == null || (container instanceof java.applet.Applet))
			return (java.awt.Container)component;
		else
			return com.ofx.objectmapper.OmReportPreview.componentRoot(((java.awt.Component) (container)));
	}

	private java.lang.String consumeUntil(com.ofx.base.SxStringTokenizer sxstringtokenizer, int i, java.awt.FontMetrics fontmetrics)
	{
		java.lang.StringBuffer stringbuffer = new StringBuffer();
		int j = 4;
		do
			try
			{
				java.lang.String s = sxstringtokenizer.nextToken();
				if(s.equals("\r") || s.equals("\n"))
				{
					sxstringtokenizer.back();
					return stringbuffer.toString();
				}
				if(s.equals("\t"))
				{
					double d = (double)stringbuffer.length() / (double)j;
					double d1 = d - (double)(int)d;
					int k = j - (int)(d1 * (double)j);
					java.lang.StringBuffer stringbuffer1 = new StringBuffer(k);
					for(int l = 0; l < k; l++)
						stringbuffer1.append(' ');

					s = stringbuffer1.toString();
				}
				if(fontmetrics.stringWidth(stringbuffer.toString() + s) > i)
				{
					sxstringtokenizer.back();
					return stringbuffer.toString();
				}
				stringbuffer.append(s);
			}
			catch(java.util.NoSuchElementException nosuchelementexception)
			{
				return stringbuffer.toString();
			}
		while(true);
	}

	public void printReport(java.awt.PrintJob printjob)
		throws java.lang.Exception
	{
		java.awt.Dimension dimension = printjob.getPageDimension();
		java.awt.Dimension dimension1 = new Dimension(50, 50);
		java.awt.Dimension dimension2 = new Dimension(dimension.width - dimension1.width, dimension.height - dimension1.height);
		com.ofx.base.SxStringTokenizer sxstringtokenizer = new SxStringTokenizer(previewText.getText(), "\t\n\r", true);
		java.lang.String s2 = new String();
		java.awt.Font font = new Font("Monospaced", 0, 10);
		java.awt.Graphics g = printjob.getGraphics();
		if(g == null)
			throw new Exception("Unable to get the Graphics object for the first page.");
		g.setColor(java.awt.Color.black);
		g.setFont(font);
		java.awt.FontMetrics fontmetrics = g.getFontMetrics();
		int i = dimension1.width;
		int j = dimension1.height + fontmetrics.getAscent();
		do
		{
			try
			{
				do
				{
					java.lang.String s3 = consumeUntil(sxstringtokenizer, dimension2.width, fontmetrics);
					g.drawString(s3, i, j);
					java.lang.String s = sxstringtokenizer.peek();
					if(s.equals("\r") || s.equals("\n"))
					{
						java.lang.String s4 = sxstringtokenizer.nextToken();
						java.lang.String s1 = sxstringtokenizer.peek();
						if(s4.equals("\r") && s1.equals("\n"))
							sxstringtokenizer.nextToken();
						else
						if(s4.equals("\n") && s1.equals("\r"))
							sxstringtokenizer.nextToken();
					}
					i = dimension1.width;
					j += fontmetrics.getMaxDescent() + fontmetrics.getAscent();
				} while(j < dimension2.height);
				g.dispose();
				g = printjob.getGraphics();
				if(g == null)
					break;
				g.setColor(java.awt.Color.black);
				g.setFont(font);
				fontmetrics = g.getFontMetrics();
				j = dimension1.height + fontmetrics.getAscent();
				continue;
			}
			catch(java.util.NoSuchElementException nosuchelementexception) { }
			break;
		} while(true);
	}

	public void actionPerformed(java.awt.event.ActionEvent actionevent)
	{
		java.lang.Object obj = actionevent.getSource();
		if(obj == printButton)
			try
			{
				java.awt.Frame frame = (java.awt.Frame)com.ofx.objectmapper.OmReportPreview.componentRoot(this);
				java.awt.PrintJob printjob = getToolkit().getPrintJob(frame, "SpatialFX Data Report", null);
				if(printjob != null)
				{
					printReport(printjob);
					printjob.end();
				}
			}
			catch(java.lang.Exception exception)
			{
				java.lang.System.out.println("Printing failed");
			}
		if(obj == closeButton)
		{
			setVisible(false);
			return;
		} else
		{
			return;
		}
	}

	java.lang.String filename;
	java.lang.String endOfLine;
	java.lang.String separator;
	java.lang.String sep1;
	java.lang.String sep2;
	java.lang.String dbName;
	com.ofx.objectmapper.OmSpatialDataReport rptWin;
	java.awt.TextArea previewText;
	java.awt.Button printButton;
	java.awt.Button closeButton;
	symantec.itools.awt.BorderPanel buttonPanel;
}
