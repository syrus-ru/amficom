package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.tools.MapTool;
import com.mapinfo.beans.vmapj.VisualMapJ;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
//import com.mapinfo.util.DoublePoint;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MapInfoLogicalNetLayer extends LogicalNetLayer 
{
	protected MapImagePanel mapImagePanel = null;
	
//	MapTool logicalLayerMapTool = null;
  protected MapJ localMapJ = null;
	
	public static final double ZOOM_FACTOR = 2D;

	public MapInfoLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"SpatialLogicalNetLayer(" + viewer + ")");

//		logicalLayerMapTool = new LogicalLayerMapTool(this);
		setMapViewer(viewer);
	}

  public void setMapImageSize(int width, int height)
  {
    if ((width > 0) && (height > 0))
      this.localMapJ.setDeviceBounds(new DoubleRect(
        0,
        0,
        width,
        height));
  }

	/**
	 * �������� �������� ���������� �� �������������� �����������
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
    try
    {
      com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(
        point.getX(), point.getY());
      com.mapinfo.util.DoublePoint screendp =
        this.localMapJ.transformNumericToScreen(mapdp);
      return new Point( (int) screendp.x, (int) screendp.y);
    }
    catch (Exception exc)
    {
    }
    
    return null;
	}
	
	/**
	 * �������� �������������� ���������� �� ��������
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		try
		{
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.
				DoublePoint(point.x, point.y);
			com.mapinfo.util.DoublePoint mapdp =
        this.localMapJ.transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point( (int) screenDistance, 0));
		double d = distance(p1, p2);

		return d;
	}
	
	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
	}

	public DoublePoint pointAtDistance(
			DoublePoint startPoint, 
			DoublePoint endPoint, 
			double dist)
	{
		DoublePoint point = new DoublePoint(startPoint.getX(), startPoint.getY());
		double len = distance(startPoint,endPoint);
		point.x += (endPoint.getX() - startPoint.getX()) / len * dist;
		point.y += (endPoint.getY() - startPoint.getY()) / len * dist;
		return point;
	}

	/**
	 * �������� ��������� ����� ����� ������� � �������� �����������
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		try
		{
			return this.localMapJ.sphericalDistance(
				new com.mapinfo.util.DoublePoint(from.getX(), from.getY()),
				new com.mapinfo.util.DoublePoint(to.getX(), to.getY()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0.0D;
	}

	/**
	 * ���������� ����������� ����� ���� �����
	 */
  public void setCenter(DoublePoint center)
  {
    try
    {
      localMapJ.setCenter(new com.mapinfo.util.DoublePoint(center.getX(),center.getY()));
    }
    catch(Exception exc)
    {
			System.out.println("MILNL - Failed setting center.");
    }
    this.repaint(true);    
  }

	/**
	 * �������� ����������� ����� ���� �����
	 */
  public DoublePoint getCenter()
  {
    com.mapinfo.util.DoublePoint center = null;
    try
    {
      center = localMapJ.getCenter();
      return new DoublePoint(center.x,center.y);
    }
    catch(Exception exc)
    {
			System.out.println("MapImagePanel - Failed getting center.");
    }
    
    return null;
  }

	public Rectangle2D.Double getVisibleBounds()
	{
		try
		{
			DoubleRect rect = this.localMapJ.getBounds();
			Rectangle2D.Double vb = new Rectangle2D.Double(rect.xmin, rect.ymin, rect.width(),
													rect.height());

			return vb;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * ���������� ������� ���������� � ������ � ��������� ����������� �����
	 */
	public void release()
	{
	}

	/**
	 * ������������ ���������� ���������� � ������
	 */
	public void repaint(boolean fullRepaint)
	{
    if (fullRepaint)
    {  
      String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
      url += this.getMapMainParamString();    
  
      try
      {
        URL mapServer = new URL(url);
        System.out.println("url: " + mapServer);
        URLConnection s = mapServer.openConnection();
  
        System.out.println("MIFLNL - repaint - Conection opened");
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        System.out.println("MIFLNL - repaint - ObjectInputStream exists");

        try
        {
          Object readObject = ois.readObject();
          if (readObject instanceof String)
          {
            Environment.log(
                Environment.LOG_LEVEL_FINER, 
                (String)readObject);
            return;
          }
        }
        catch (IOException optExc)
        {}
        
        int dataSize = mapImagePanel.getWidth() * mapImagePanel.getHeight() * 2;
        byte[] img = new byte[dataSize];
  
        try
        {
          ois.readFully(img);
        }
        catch (EOFException eofExc)
        {}
        System.out.println("MIFLNL - repaint - Image read");
          
        ois.close();
        System.out.println("MIFLNL - repaint - Stream closed");
        
        Image imageReceived = Toolkit.getDefaultToolkit().createImage(img);
        mapImagePanel.setImage(imageReceived);
        System.out.println("MIFLNL - repaint - Image setted");
      }
      catch(Exception exc)
      {
        exc.printStackTrace();
      }
    }
    else
      mapImagePanel.repaint();

//    super.paint(mapImagePanel.getGraphics());
	}
	
	/**
	 * ���������� ������ ���� �� ���������� ����������� �����
	 */
	public void setCursor(Cursor cursor)
	{
		System.out.println("Set cursor " + cursor.getName());
		mapImagePanel.setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return mapImagePanel.getCursor();
	}


	/**
	 * �������� ������� ������� ���� �����
	 */
  public double getScale()
  {
    double currentZoom = 0.0D;
    try
    {
      currentZoom = localMapJ.getZoom();
    }
    catch(Exception exc)
    {
			System.out.println("MapImagePanel - Failed setting zoom.");
    }

    return currentZoom;
  }

	/**
	 * ���������� �������� ������� ���� �����
	 */
  public void setScale(double z)
  {
    try
    {
      localMapJ.setZoom(z);
      updateZoom();
      repaint(true);
    }
    catch(Exception exc)
    {
			System.out.println("MapImagePanel - Failed setting zoom.");
    }
  }

	/**
	 * ���������� ������� ���� ����� � �������� �������������
	 */
	public void scaleTo(double scale�oef)
	{
    this.setScale(this.getScale() * scale�oef);
	}

	/**
	 * ���������� ��� ����� �� ����������� �������������
	 */
	public void zoomIn()
	{
		scaleTo(1.0D / ZOOM_FACTOR);
	}

	/**
	 * �������� ��� ����� �� ����������� �������������
	 */
	public void zoomOut()
	{
		scaleTo(ZOOM_FACTOR);
	}

	/**
	 * ���������� ��� ����������� ������� ����� (� ����������� �����)
	 * �� ����������� ������� �����
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
//		System.out.println("Zoom to box (" + from.getX() + ", " + from.getY() + ") - (" +
//								 to.getX() + ", " + to.getY() + ")");
		try
		{
			localMapJ.setBounds(new DoubleRect(
				from.getX(),
				from.getY(),
				to.getX(),
				to.getY()));

      updateZoom();
      repaint(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateZoom()
	{
		super.updateZoom();

		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		Double p = new Double(getScale());
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
	}

	public void handDragged(MouseEvent me)
	{
    int shiftX = (int) (me.getX() - this.startPoint.getX());
    int shiftY = (int) (me.getY() - this.startPoint.getY());

    this.mapImagePanel.repaint(this.mapImagePanel.getGraphics(),shiftX,shiftY);
	}

	public List findSpatialObjects(String searchText)
	{
		throw new UnsupportedOperationException();
	}

	public void centerSpatialObject(SpatialObject so)
	{
		throw new UnsupportedOperationException();
	}

	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setMapViewer(" + mapViewer + ")");
		
		super.setMapViewer(mapViewer);
		
		MapInfoNetMapViewer snmv = (MapInfoNetMapViewer)mapViewer;
		this.mapImagePanel = snmv.mapImagePanel;
    this.localMapJ = snmv.localMapJ;
	}
  
  public String getMapMainParamString()
  {
    String result = "";
    result += "?" + ServletCommandNames.WIDTH + "=" + this.mapImagePanel.getWidth();
    result += "&" + ServletCommandNames.HEIGHT + "=" + this.mapImagePanel.getHeight();
    result += "&" + ServletCommandNames.CENTER_X + "=" + this.getCenter().getX();
    result += "&" + ServletCommandNames.CENTER_Y + "=" + this.getCenter().getY();
    result += "&" + ServletCommandNames.ZOOM_FACTOR + "=" + this.getScale();
    
    int index = 1;
    Iterator layersIt = ((MapInfoNetMapViewer)this.viewer).getLayers().iterator();
    for (;layersIt.hasNext();)
    {
      SpatialLayer spL = (SpatialLayer)layersIt.next();
      result += "&" + ServletCommandNames.LAYER_VISIBLE + index + "=" + spL.isVisible();
      result += "&" + ServletCommandNames.LAYER_LABELS_VISIBLE + index + "=" + spL.isLabelVisible();
      index++;
    }
    
    return result;
  }
}
