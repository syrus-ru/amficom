package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mapinfo.mapj.MapJ;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.map.DoublePoint;

public class MapInfoLogicalNetLayer extends LogicalNetLayer
{
	/**
	 * Для вывода времени в отладочных сообщениях
	 */
	public SimpleDateFormat sdFormat = new SimpleDateFormat("E M d H:m:s:S");
	
	public static final double ZOOM_FACTOR = 2D;

	private MapInfoNetMapViewer nmViewer = null;
	
	private TopologicalImageCache imageCache = null;
	
	public MapInfoLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"constructor call",
				getClass().getName(),
				"SpatialLogicalNetLayer(" + viewer + ")");

		setMapViewer(viewer);
	}

	/**
	 * Вызывается после создания визуальной компоненты
	 *
	 */
	public void initializeImageCache()
	{
		this.imageCache = new TopologicalImageCache(this);		
	}
	
	public void refreshLayers()
	{
		this.imageCache.refreshLayers();		
	}
	
	public void setMapImageSize(int width, int height)
	{
		if((width > 0) && (height > 0))
		{
			getLocalMapJ().setDeviceBounds(new DoubleRect(
					0,
					0,
					width,
					height));

			this.imageCache.sizeChanged();
			this.setCenter(this.convertScreenToMap(new Point(width / 2,height / 2)));			
			
			repaint(true);
		}
	}

	/**
	 * Получить экранные координаты по географическим координатам
	 */
	public Point convertMapToScreen(DoublePoint point)
	{
		try
		{
			com.mapinfo.util.DoublePoint mapdp = new com.mapinfo.util.DoublePoint(
					point.getX(),
					point.getY());
			com.mapinfo.util.DoublePoint screendp =
				getLocalMapJ().transformNumericToScreen(mapdp);
			return new Point((int )screendp.x, (int )screendp.y);
		}
		catch(Exception exc)
		{
				exc.printStackTrace();
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
			com.mapinfo.util.DoublePoint screendp = new com.mapinfo.util.DoublePoint(
					point.x,
					point.y);
			com.mapinfo.util.DoublePoint mapdp = 
				getLocalMapJ().transformScreenToNumeric(screendp);
			return new DoublePoint(mapdp.x, mapdp.y);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public double convertScreenToMap(double screenDistance)
	{
		DoublePoint p1 = convertScreenToMap(new Point(0, 0));
		DoublePoint p2 = convertScreenToMap(new Point((int )screenDistance, 0));
		double d = distance(p1, p2);

		return d;
	}

	/**
	 * Это не работает в силу того, что масштабы могут разниться по разным координатным осям.
	 */
	public double convertMapToScreen(double topologicalDistance)
	{
		Point p1 = convertMapToScreen(new DoublePoint(0, 0));
		Point p2 = convertMapToScreen(new DoublePoint(topologicalDistance, 0));		
		
		double returnValue = Math.pow((Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y,2)),0.5);
		return returnValue;
	}

	/**
	 * Считает экранное расстояние между двумя точками в сферических координатах
	 * @param sphP1 
	 * @param sphP2
	 * @return Расстояние
	 */
	public double convertMapToScreen(DoublePoint sphP1, DoublePoint sphP2)
	{
		Point p1 = convertMapToScreen(sphP1);
		Point p2 = convertMapToScreen(sphP2);		
		
		double returnValue = Math.pow((Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y,2)),0.5);
		return returnValue;
	}
	
	public DoublePoint pointAtDistance(
			DoublePoint startPoint,
			DoublePoint endPoint,
			double dist)
	{
		double len = distance(startPoint, endPoint);

		DoublePoint point = new DoublePoint(
				startPoint.getX() + (endPoint.getX() - startPoint.getX()) / len
						* dist,
				startPoint.getY() + (endPoint.getY() - startPoint.getY()) / len
						* dist);

		return point;
	}

	/**
	 * Получить дистанцию между двумя точками в экранных координатах
	 */
	public double distance(DoublePoint from, DoublePoint to)
	{
		try
		{
			return getLocalMapJ().sphericalDistance(
					new com.mapinfo.util.DoublePoint(from.getX(), from.getY()),
					new com.mapinfo.util.DoublePoint(to.getX(), to.getY()));
		}
		catch(Exception e)
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
			getLocalMapJ().setCenter(new com.mapinfo.util.DoublePoint(
					center.getX(),
					center.getY()));
			
			this.imageCache.centerChanged();			
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
			center = getLocalMapJ().getCenter();
			return new DoublePoint(center.x, center.y);
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
			DoubleRect rect = getLocalMapJ().getBounds();
			Rectangle2D.Double vb = new Rectangle2D.Double(
					rect.xmin,
					rect.ymin,
					rect.width(),
					rect.height());

			return vb;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Освободить ресурсы компонента с картой и завершить отображение карты
	 */
	public void release()
	{//empty
	}

	/**
	 * Перерисовать содержимое компонента с картой
	 */
	public void repaint(boolean fullRepaint)
	{
		if(fullRepaint)
		{
			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - repaint - Entered full repaint");
			
			this.nmViewer.mapImagePanel.setImage(this.imageCache.getImage());
			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
				" MIFLNL - repaint - Exiting full repaint");
		}
		this.nmViewer.mapImagePanel.repaint();		
	}

	/**
	 * Устанавить курсор мыши на компоненте отображения карты
	 */
	public void setCursor(Cursor cursor)
	{
		System.out.println("Set cursor " + cursor.getName());
		this.nmViewer.mapImagePanel.setCursor(cursor);
	}

	public Cursor getCursor()
	{
		return this.nmViewer.mapImagePanel.getCursor();
	}

	/**
	 * Получить текущий масштаб вида карты
	 */
	public double getScale()
	{
		double currentZoom = 0.0D;
		try
		{
			currentZoom = getLocalMapJ().getZoom();
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
			getLocalMapJ().setZoom(z);
			updateZoom();
			
			this.imageCache.scaleChanged();			
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
	 * Приблизить вид выделенного участка карты (в координатах карты) по
	 * координатам угловых точек
	 */
	public void zoomToBox(DoublePoint from, DoublePoint to)
	{
		// System.out.println("Zoom to box (" + from.getX() + ", " + from.getY()
		// + ") - (" +
		// to.getX() + ", " + to.getY() + ")");
		try
		{
			getLocalMapJ().setBounds(new DoubleRect(from.getX(), from.getY(),
					to.getX(), to.getY()));

			updateZoom();
			
			this.imageCache.scaleChanged();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateZoom()
	{
		super.updateZoom();

		if(this.aContext == null)
			return;
		Dispatcher disp = this.aContext.getDispatcher();
		if(disp == null)
			return;
		Double p = new Double(getScale());
		disp.notify(new MapEvent(p, MapEvent.MAP_VIEW_SCALE_CHANGED));
	}

	public void handDragged(MouseEvent me)
	{
		Point hdEndPoint = me.getPoint();
		int shiftX = (int )(me.getX() - this.startPoint.getX());
		int shiftY = (int )(me.getY() - this.startPoint.getY());
		Dimension visSize = this.getMapViewer().getVisualComponent().getSize();

		int boundedShiftX = 0;
		int boundedShiftY = 0;
		//Определяем угол смещения - если он ближе к Pi*n/2, n = 2k + 1 - тогда смещаем по диагонали 
		double angle = Math.toDegrees(Math.acos(shiftX / Math.sqrt(Math.pow(shiftX,2) + Math.pow(shiftY,2))));
		
		if (angle > 90)
			angle = 180 - angle;
		
		if ((22.5 < angle) && (angle < 67.5))
		{
			if (	(Math.abs(shiftX) >= visSize.getWidth() * MapFrame.MOVE_CENTER_STEP_SIZE)
					&&(Math.abs(shiftY) >= visSize.getHeight() * MapFrame.MOVE_CENTER_STEP_SIZE))
			{
				boundedShiftX = (int)(shiftX / Math.abs(shiftX) * visSize.getWidth() * MapFrame.MOVE_CENTER_STEP_SIZE);
				boundedShiftY = (int)(shiftY / Math.abs(shiftY) * visSize.getHeight() * MapFrame.MOVE_CENTER_STEP_SIZE);
			}
		}
		else if (angle <= 22.5)
		{
			if (Math.abs(shiftX) >= visSize.getWidth() * MapFrame.MOVE_CENTER_STEP_SIZE)			
				boundedShiftX = (int)(shiftX / Math.abs(shiftX) * visSize.getWidth() * MapFrame.MOVE_CENTER_STEP_SIZE);
			boundedShiftY = 0;			
		}
		else if (67.5 <= angle)
		{
			boundedShiftX = 0;
			if (Math.abs(shiftY) >= visSize.getHeight() * MapFrame.MOVE_CENTER_STEP_SIZE)
				boundedShiftY = (int)(shiftY / Math.abs(shiftY) * visSize.getHeight() * MapFrame.MOVE_CENTER_STEP_SIZE);			
		}
		
		if ((boundedShiftX != 0) || (boundedShiftY != 0))
		{
			hdEndPoint.setLocation(
					this.getStartPoint().x + boundedShiftX,
					this.getStartPoint().y + boundedShiftY);
			
			DoublePoint center = this.getCenter();
			DoublePoint p1 = this.convertScreenToMap(this.getStartPoint());
			DoublePoint p2 = this.convertScreenToMap(hdEndPoint);
			
			double dx = p1.getX() - p2.getX();
			double dy = p1.getY() - p2.getY();
			center.setLocation(center.getX() + dx, center.getY() + dy);
			this.setCenter(center);
			this.getMapView().setCenter(this.getCenter());
			this.repaint(true);
			
			this.setStartPoint(hdEndPoint);
		}
		
		this.nmViewer.mapImagePanel.repaint(
				this.nmViewer.mapImagePanel.getGraphics(),
				shiftX,
				shiftY);
	}

	public List findSpatialObjects(String searchText)
	{
		List resultList = new ArrayList();

		String uriString = ((MapInfoNetMapViewer )this.viewer).getConnection().getURL();
		uriString += createSearchCommandString(searchText);

		try
		{
			URI mapServerURI = new URI(uriString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			System.out.println("url: " + mapServerURL);
			URLConnection s = mapServerURL.openConnection();

			System.out.println("MIFLNL - searchText - Conection opened");

			if(s.getInputStream() == null)
				return resultList;
			
			ObjectInputStream ois = new ObjectInputStream(s
					.getInputStream());

			System.out
					.println("MIFLNL - searchText - ObjectInputStream exists");

			//reading possible error from server
			try
			{
				Object readObject = ois.readObject();
				if(readObject instanceof String)
				{
					Environment.log(
							Environment.LOG_LEVEL_FINER,
							(String )readObject);
					return resultList;
				}
			}
			catch(IOException optExc)
			{
			}

			//reading names and centers
			try
			{
				for(;;)
				{
					double xCoord  = ois.readDouble();
					double yCoord  = ois.readDouble();				
					String featureName = (String)ois.readObject();
					
					resultList.add(new MapInfoSpatialObject(
							new DoublePoint(xCoord,yCoord),
							featureName));
				}
			}
			catch(EOFException eofExc)
			{
			}
			System.out.println("MIFLNL - searchText - Spatial objects read");

			ois.close();
			System.out.println("MIFLNL - searchText - Stream closed");
		}

		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return resultList;
	}

	public void centerSpatialObject(SpatialObject so)
	{
		MapInfoSpatialObject miso = (MapInfoSpatialObject )so;
		this.setCenter(miso.getCenter());
	}

	public void setMapViewer(NetMapViewer mapViewer)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass()
				.getName(), "setMapViewer(" + mapViewer + ")");

		super.setMapViewer(mapViewer);

		this.nmViewer = (MapInfoNetMapViewer )mapViewer;
	}
	
	public String createSearchCommandString(String nameToSearch)
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
			+ ServletCommandNames.CN_SEARCH_NAME;
		
		result += "&" + ServletCommandNames.PAR_NAME_TO_SEARCH + "=" + nameToSearch;

		return result;
	}
	
	public MapJ getLocalMapJ()
	{
		MapInfoConnection miConnection = 
			(MapInfoConnection)this.nmViewer.getConnection();
		
		return miConnection.getLocalMapJ();
	}
}
