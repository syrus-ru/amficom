package com.syrus.AMFICOM.Client.Map.Mapinfo;

import com.mapinfo.beans.tools.MapTool;
import com.mapinfo.beans.vmapj.VisualMapJ;
import com.mapinfo.dp.Attribute;
import com.mapinfo.dp.Feature;
import com.mapinfo.dp.FeatureSet;
import com.mapinfo.dp.QueryParams;
import com.mapinfo.dp.TableInfo;
import com.mapinfo.dp.jdbc.QueryBuilder;
import com.mapinfo.mapj.FeatureLayer;
import com.mapinfo.mapj.LayerType;
import com.mapinfo.mapj.MapJ;
import com.mapinfo.unit.LinearUnit;
import com.mapinfo.util.DoubleRect;
import com.mapinfo.xmlprot.mxtj.bh;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;

import com.syrus.AMFICOM.map.DoublePoint;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MapInfoLogicalNetLayer extends LogicalNetLayer 
{
//	protected MapImagePanel mapImagePanel = null;
	
//  protected MapJ localMapJ = null;
  protected MapInfoNetMapViewer nmViewer = null;
	
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
      nmViewer.localMapJ.setDeviceBounds(new DoubleRect(
        0,
        0,
        width,
        height));
  }

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
    try
    {
      com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(
        point.getX(), point.getY());
      com.mapinfo.util.DoublePoint screendp =
        nmViewer.localMapJ.transformNumericToScreen(mapdp);
      return new Point( (int) screendp.x, (int) screendp.y);
    }
    catch (Exception exc)
    {
    }
    
    return null;
	}
	
	/**
	 * Получить географические координаты по экранным
	 */
	public DoublePoint convertScreenToMap(Point point)
	{
		try
		{
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.
				DoublePoint(point.x, point.y);
			com.mapinfo.util.DoublePoint mapdp =
        nmViewer.localMapJ.transformScreenToNumeric(screendp);
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
		double len = distance(startPoint,endPoint);
    
    DoublePoint point = new DoublePoint(
      startPoint.getX() + (endPoint.getX() - startPoint.getX()) / len * dist,
      startPoint.getY() + (endPoint.getY() - startPoint.getY()) / len * dist);
      
		return point;
	}

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		try
		{
			return nmViewer.localMapJ.sphericalDistance(
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
	 * Установить центральную точку вида карты
	 */
  public void setCenter(DoublePoint center)
  {
    try
    {
      nmViewer.localMapJ.setCenter(new com.mapinfo.util.DoublePoint(center.getX(),center.getY()));
    }
    catch(Exception exc)
    {
			System.out.println("MILNL - Failed setting center.");
			exc.printStackTrace();
    }
  }

	/**
	 * Получить центральную точку вида карты
	 */
  public DoublePoint getCenter()
  {
    com.mapinfo.util.DoublePoint center = null;
    try
    {
      center = nmViewer.localMapJ.getCenter();
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
			DoubleRect rect = nmViewer.localMapJ.getBounds();
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
        
        if (s.getInputStream() == null)
          return;
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
        
        int dataSize = nmViewer.mapImagePanel.getWidth() * nmViewer.mapImagePanel.getHeight() * 2;
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
        nmViewer.mapImagePanel.setImage(imageReceived);
        System.out.println("MIFLNL - repaint - Image setted");
      }
      catch(Exception exc)
      {
        exc.printStackTrace();
      }
    }
    else
      nmViewer.mapImagePanel.repaint();

//    super.paint(nmViewer.mapImagePanel.getGraphics());
	}
	
	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
		System.out.println("Set cursor " + cursor.getName());
		nmViewer.mapImagePanel.setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return nmViewer.mapImagePanel.getCursor();
	}


	/**
	 * Получить текущий масштаб вида карты
	 */
  public double getScale()
  {
    double currentZoom = 0.0D;
    try
    {
      currentZoom = nmViewer.localMapJ.getZoom();
    }
    catch(Exception exc)
    {
			System.out.println("MapImagePanel - Failed setting zoom.");
    }

    return currentZoom;
  }

	/**
	 * Установить заданный масштаб вида карты
	 */
  public void setScale(double z)
  {
	try
    {
      nmViewer.localMapJ.setZoom(z);
      updateZoom();
    }
    catch(Exception exc)
    {
			System.out.println("MapImagePanel - Failed setting zoom.");
			exc.printStackTrace();
    }
  }

	/**
	 * Установить масштаб вида карты с заданным коэффициентом
	 */
	public void scaleTo(double scaleСoef)
	{
    this.setScale(this.getScale() * scaleСoef);
	}

	/**
	 * Приблизить вид карты со стандартным коэффициентом
	 */
	public void zoomIn()
	{
		scaleTo(1.0D / ZOOM_FACTOR);
	}

	/**
	 * Отдалить вид карты со стандартным коэффициентом
	 */
	public void zoomOut()
	{
		scaleTo(ZOOM_FACTOR);
	}

	/**
	 * Приблизить вид выделенного участка карты (в координатах карты)
	 * по координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
//		System.out.println("Zoom to box (" + from.getX() + ", " + from.getY() + ") - (" +
//								 to.getX() + ", " + to.getY() + ")");
		try
		{
			nmViewer.localMapJ.setBounds(new DoubleRect(
				from.getX(),
				from.getY(),
				to.getX(),
				to.getY()));

      updateZoom();
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

    nmViewer.mapImagePanel.repaint(nmViewer.mapImagePanel.getGraphics(),shiftX,shiftY);
	}

	public List findSpatialObjects(String searchText)
	{
    List resultList = new ArrayList();
    Iterator layersIt =  nmViewer.localMapJ.getLayers().iterator(LayerType.FEATURE);
    for (;layersIt.hasNext();)
    {
      FeatureLayer currLayer = (FeatureLayer)layersIt.next();
      
      try
      {
        //Названия всех колонок - чтобы достать инфу об объекте
        //Может они и не понадобятся!!!!!!!!
        List allColumnNames = new ArrayList();
        for (int i = 0; i < currLayer.getTableInfo().getColumnCount(); i++)
          allColumnNames.add(currLayer.getTableInfo().getColumnName(i));

        //Название колонки с надписями  
        String labelColumnName = (String)currLayer.getLabelProperties().getLabelColumns().get(0);
        //Её индекс в TableInfo
        int labelColumnIndex = currLayer.getTableInfo().getColumnIndex(labelColumnName);
        
        //Поиск для "лэйбловой колонки"
        FeatureSet fs = currLayer.searchByAttribute(
          allColumnNames,
          labelColumnName,
          new Attribute(searchText),
          QueryParams.ALL_PARAMS);
            
        Feature feature = null;
        // Loop until FeatureSet.getNextFeature() returns null
        while ( (feature = fs.getNextFeature()) != null )
        {
          String featureName = feature.getAttribute(labelColumnIndex).getString();
          resultList.add(new MapInfoSpatialObject(feature,featureName));
        }
      }
      catch (Exception exc)
      {
        exc.printStackTrace();
      }
    }
    
    return resultList;
	}

	public void centerSpatialObject(SpatialObject so)
	{
    MapInfoSpatialObject miso = (MapInfoSpatialObject) so;
    com.mapinfo.util.DoublePoint miDp = miso.getCenter();
    DoublePoint dp = new DoublePoint(miDp.x,miDp.y);
		this.setCenter(dp);
	}

	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setMapViewer(" + mapViewer + ")");
		
		super.setMapViewer(mapViewer);
		
		this.nmViewer = (MapInfoNetMapViewer)mapViewer;
//		this.mapImagePanel = nmViewer.mapImagePanel;
//    this.localMapJ = nmViewer.localMapJ;
	}
  
  public String getMapMainParamString()
  {
    String result = "";
    result += "?" + ServletCommandNames.WIDTH + "=" + nmViewer.mapImagePanel.getWidth();
    result += "&" + ServletCommandNames.HEIGHT + "=" + nmViewer.mapImagePanel.getHeight();
    result += "&" + ServletCommandNames.CENTER_X + "=" + this.getCenter().getX();
    result += "&" + ServletCommandNames.CENTER_Y + "=" + this.getCenter().getY();
    result += "&" + ServletCommandNames.ZOOM_FACTOR + "=" + this.getScale();
    
    int index = 0;
    Iterator layersIt = ((MapInfoNetMapViewer)this.viewer).getLayers().iterator();
    for (;layersIt.hasNext();)
    {
      SpatialLayer spL = (SpatialLayer)layersIt.next();
      result += "&" + ServletCommandNames.LAYER_VISIBLE + index + "=" + (spL.isVisible() ? 1:0);
      result += "&" + ServletCommandNames.LAYER_LABELS_VISIBLE + index + "=" + (spL.isLabelVisible() ? 1:0);
      index++;
    }
    
    return result;
  }
}
