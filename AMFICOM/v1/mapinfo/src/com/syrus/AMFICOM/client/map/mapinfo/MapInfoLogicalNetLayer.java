package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.tools.MapTool;
import com.mapinfo.beans.vmapj.VisualMapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import com.syrus.AMFICOM.Client.Resource.Map.DoublePoint;
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
import java.util.LinkedList;
import java.util.List;

public class MapInfoLogicalNetLayer extends LogicalNetLayer 
{
	protected MapImagePanel mapImagePanel = null;
	
//	MapTool logicalLayerMapTool = null;
	
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

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
    Point result = new Point();
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.CONVERT_MAP_SCREEN;
    url += mapImagePanel.getMapMainParamString();    
    url += "&par1=" + point.x;
    url += "&par2=" + point.y;
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return null;
      }
      
      result.x = ((Integer)objRead).intValue();
      result.y = ((Integer)ois.readObject()).intValue();

      ois.close();
      return result;
    }
    catch (Throwable th)
    {
    }
    
    return null;
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
    DoublePoint result = new DoublePoint();
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.CONVERT_SCREEN_MAP_POINT;
    url += mapImagePanel.getMapMainParamString();
    url += "&par1=" + point.x;
    url += "&par2=" + point.y;
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return null;
      }
      
      result.x = ((Double)objRead).doubleValue();
      result.y = ((Double)ois.readObject()).doubleValue();
      ois.close();
      return result;
    }
    catch (Throwable th)
    {
    }
    
    return null;
	}

	public double convertScreenToMap(double screenDistance)
	{
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.CONVERT_SCREEN_MAP_DOUBLE;
    url += mapImagePanel.getMapMainParamString();    
    url += "&par1=" + screenDistance;
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return 0.0D;
      }

      double result = ((Double)ois.readObject()).doubleValue();
      ois.close();
      return result;
    }
    catch (Throwable th)
    {
    }
    
    return 0.0D;
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
    DoublePoint result = new DoublePoint();
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.POINT_AT_DISTANCE;
    url += mapImagePanel.getMapMainParamString();    
    url += "&par1=" + startPoint.x;
    url += "&par2=" + startPoint.y;
    url += "&par3=" + endPoint.x;
    url += "&par4=" + endPoint.y;
    url += "&par5=" + dist;
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return null;
      }
      
      result.x = ((Double)objRead).doubleValue();
      result.y = ((Double)ois.readObject()).doubleValue();
      ois.close();
      return result;
    }
    catch (Throwable th)
    {
    }
    
    return null;
	}

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.DISTANCE;
    url += mapImagePanel.getMapMainParamString();
    url += "&par1=" + from.x;
    url += "&par2=" + from.y;
    url += "&par3=" + to.x;
    url += "&par4=" + to.y;
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return 0.0D;
      }
      
      double result = ((Double)objRead).doubleValue();
      ois.close();
      return result;
    }
    catch (Throwable th)
    {
    }
    
		return 0.0D;
	}

	/**
	 * Установить центральную точку вида карты
	 */
	public void setCenter(DoublePoint center)
	{
    this.mapImagePanel.setCenter(center);
    this.repaint(true);
  }

	/**
	 * Получить центральную точку вида карты
	 */
	public DoublePoint getCenter()
	{
    return mapImagePanel.getCenter();    
	}

	public Rectangle2D.Double getVisibleBounds()
	{
    String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.GET_VISIBLE_BOUNDS;
    url += mapImagePanel.getMapMainParamString();
    
    try
    {
      URL mapServer = new URL(url);
      System.out.println("url: " + mapServer);
      URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      Object objRead = ois.readObject();
      
      if (objRead instanceof String)
      {
        Environment.log(
            Environment.LOG_LEVEL_FINER, 
            (String)objRead);
        return null;
      }
      
      Rectangle2D.Double rect = new Rectangle2D.Double();      
      rect.width = ((Double)objRead).doubleValue();
      rect.height = ((Double)ois.readObject()).doubleValue();
      ois.close();
      return rect;
    }
    catch (Throwable th)
    {
    }
    
		return null;
	}

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты
	 */
	public void release()
	{
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
    if (!fullRepaint)
      mapImagePanel.repaint();
    else
    {  
      String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
      url += "?commandName=" + ServletCommandNames.RENDER_MAP;
      url += mapImagePanel.getMapMainParamString();    
  
      try
      {
        URL mapServer = new URL(url);
        System.out.println("url: " + mapServer);
        URLConnection s = mapServer.openConnection();
  
        System.out.println("MIFLNL - repaint - Conection opened");
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        System.out.println("MIFLNL - repaint - ObjectInputStream exists");
        
        int dataSize = mapImagePanel.getWidth() * mapImagePanel.getHeight() * 2;
        byte[] img = new byte[dataSize];
  
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
    
    super.paint(mapImagePanel.getGraphics());
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
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
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
    return mapImagePanel.getZoom();
	}

	/**
	 * Установить заданный масштаб вида карты
	 */
	public void setScale(double scale)
	{
    mapImagePanel.setZoom(scale);
		updateZoom();
		repaint(true);
	}

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
    mapImagePanel.scaleTo(scaleСoef);
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		mapImagePanel.zoomIn();
		updateZoom();
		repaint(true);
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		mapImagePanel.zoomOut();
		updateZoom();
		repaint(true);
	}

	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
    DoublePoint newCenter = new DoublePoint((to.x + from.x)/2,(to.y + from.y)/2);
    this.mapImagePanel.setCenter(newCenter);
  
		String url = ((MapInfoNetMapViewer)viewer).mapperServletURL;
    url += "?commandName=" + ServletCommandNames.ZOOM_TO_BOX;
    url += mapImagePanel.getMapMainParamString();
    url += "&par1=" + from.x;
    url += "&par2=" + from.y;
    url += "&par3=" + to.x;
    url += "&par4=" + to.y;
  
    try
    {
			URL mapServer = new URL(url);
			System.out.println("url: " + mapServer);
			URLConnection s = mapServer.openConnection();

      ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
      
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
        
        double newScale = ((Double)readObject).doubleValue();
        this.mapImagePanel.setZoom(newScale);
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
      System.out.println("MIFLNL - zoomToBox - Image read");      
      
      ois.close();
      mapImagePanel.setImage(Toolkit.getDefaultToolkit().createImage(img));
  
      updateZoom();
    }
    catch(Exception exc)
    {
      exc.printStackTrace();    
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
		repaint(false);
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
//		System.out.println("Units " + this.visualMapJ.getMapJ().getDistanceUnits().toString());
//		this.visualMapJ.getMapJ().setDistanceUnits(LinearUnit.meter);
	}
}
