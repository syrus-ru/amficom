package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
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
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.mapinfo.mapj.MapJ;
import com.mapinfo.util.DoubleRect;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.NetMapViewer;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.SpatialObject;
import com.syrus.AMFICOM.map.DoublePoint;

public class MapInfoLogicalNetLayer extends LogicalNetLayer
{
	public static final double ZOOM_FACTOR = 2D;

	protected MapInfoNetMapViewer nmViewer = null;

	/** Флаг того, что после последней перерисовки карты был изменён
	 *  масштаб.
	 */	
	private boolean zoomChanged = true;
	
	/** Флаг того, что после последней перерисовки карты был изменён
	 *  масштаб.
	 */	
	private DoublePoint lastCenter = null;
	
	/**
	 * Для вывода времени в отладочных сообщениях
	 */
	private SimpleDateFormat sdFormat = new SimpleDateFormat("E M d H:m:s:S");
	
	public MapInfoLogicalNetLayer(NetMapViewer viewer)
	{
		super();
		Environment.log(
				Environment.LOG_LEVEL_FINER,
				"constructor call",
				getClass().getName(),
				"SpatialLogicalNetLayer(" + viewer + ")");

		// logicalLayerMapTool = new LogicalLayerMapTool(this);
		setMapViewer(viewer);
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

	public double convertMapToScreen(double topologicalDistance)
	{
		throw new UnsupportedOperationException();
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
			this.lastCenter = getCenter();
			getLocalMapJ().setCenter(new com.mapinfo.util.DoublePoint(
					center.getX(),
					center.getY()));
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
			
			this.nmViewer.mapImagePanel.refreshImages();
			Image resultImage = this.nmViewer.mapImagePanel.getPaintableImage();
			Graphics riGraphics = resultImage.getGraphics();
		
			String uriString = ((MapInfoNetMapViewer )this.viewer).getConnection().getURL();
			
			Dimension mipSize = this.nmViewer.mapImagePanel.getSize();	
			Point curCenterScreen = new Point(
					(int)mipSize.getWidth() / 2,
					(int)mipSize.getHeight() / 2);
			
			Point lastCenterScreen = this.convertMapToScreen(this.lastCenter);

			int shiftX = lastCenterScreen.x - curCenterScreen.x;
			int shiftY = lastCenterScreen.y - curCenterScreen.y;
			
			if (			((mipSize.width - Math.abs(shiftX)) * //Если площадь пересечения старого изображения и нового меньше 1/4
								 (mipSize.height - Math.abs(shiftY))
								 < mipSize.width * mipSize.height / 4)
						|| (mipSize.width - Math.abs(shiftX) < 0) //или был сдвиг больше чем на ширину/высоту экрана
						|| (mipSize.height - Math.abs(shiftY) < 0)		 
						||	this.zoomChanged) //или был изменён масштаб
			{
				//Перерисовываем всю область карты
				uriString += createRenderCommandString(
						this.nmViewer.mapImagePanel.getWidth(),
						this.nmViewer.mapImagePanel.getHeight(),
						this.getCenter().getX(),
						this.getCenter().getY(),
						this.getScale());
				
				Image fullImage = this.getServerMapImage(uriString,mipSize.width,mipSize.height);
				
				riGraphics.drawImage(
						(new ImageIcon(fullImage)).getImage(),
						0,
						0,
						this.nmViewer.mapImagePanel);

				System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - repaint - full screen image received");
				
				this.zoomChanged = false;
			}
			else
			{
				//иначе перерисовываем два прямоугольника: вертикальный и горизонтальный -
				//а также добавляем прямоугольник со старого изображения

				if ((shiftX == 0) && (shiftY == 0))
					//Сдвига не было, масштаб не менялся - перерисовывать нечего!
					return;
		
				//Вертикальный (либо сверху, либо снизу ограничен горизонтальным)
				Image vertImage = null;
				if (shiftX != 0)
				{
					Point vertSegmentCenter = new Point();
				 	vertSegmentCenter.x = (shiftX > 0) ? shiftX / 2 : mipSize.width + shiftX / 2;
				 	vertSegmentCenter.y = mipSize.height / 2 + shiftY / 2;				 	
				 	
					DoublePoint vertSegmentCenterSphCoords =
						this.convertScreenToMap(vertSegmentCenter);
					
					String reqVString = uriString + createRenderCommandString(
							Math.abs(shiftX),
							mipSize.height - Math.abs(shiftY),
							vertSegmentCenterSphCoords.getX(),
							vertSegmentCenterSphCoords.getY(),
							this.getScale() * Math.abs(shiftX) / mipSize.width);
					
					vertImage = this.getServerMapImage(reqVString,Math.abs(shiftX),mipSize.height);					

					System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
						" MIFLNL - repaint - got vertical screen segment");
				}
				
				//Горизонтальный
				Image horizImage = null;
				if (shiftY != 0)
				{
					Point horizSegmentCenter = new Point();
				 	horizSegmentCenter.x = mipSize.width / 2;
					horizSegmentCenter.y = (shiftY > 0) ? shiftY / 2 : mipSize.height + shiftY / 2;
				 	
					DoublePoint horizSegmentCenterSphCoords =
						this.convertScreenToMap(horizSegmentCenter);
					
					String reqHString = uriString + createRenderCommandString(
							mipSize.width,
							Math.abs(shiftY),
							horizSegmentCenterSphCoords.getX(),
							horizSegmentCenterSphCoords.getY(),
							this.getScale());
					
					horizImage = this.getServerMapImage(reqHString,mipSize.width,Math.abs(shiftY));					

					System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
						" MIFLNL - repaint - got horizontal screen segment");
				}

				
				//Drawing old image usable area to new image				
				riGraphics.drawImage(
					this.nmViewer.mapImagePanel.getCurrentImage(),
					shiftX,
					shiftY,
					this.nmViewer.mapImagePanel);

				System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
				" MIFLNL - repaint - old image segment drawn");
				
				//Drawing vertical new part of image
				if (shiftX != 0)
				{
					int vertStartX = (shiftX > 0) ? 0 : mipSize.width + shiftX;
					int vertStartY = (shiftY > 0) ? shiftY : 0;				
					
					riGraphics.drawImage(
							(new ImageIcon(vertImage)).getImage(),
							vertStartX,
							vertStartY,
							this.nmViewer.mapImagePanel);
	
					System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - repaint - vertical image segment drawn");
				}
				
				//Drawing horizontal new part of image
				if (shiftY != 0)
				{
					int horizStartY = (shiftY > 0) ? 0 : mipSize.height + shiftY;				
					riGraphics.drawImage(
							(new ImageIcon(horizImage)).getImage(),
							0,
							horizStartY,
							this.nmViewer.mapImagePanel);
					
					System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - repaint - horizontal image segment drawn");
				}				
			}
		}
		this.nmViewer.mapImagePanel.redrawMainImage();
		this.nmViewer.mapImagePanel.repaint();
	}

	private Image getServerMapImage(String requestURIString, int imageWidth, int imageHeight)
	{
		try
		{
			URI mapServerURI = new URI(requestURIString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());

			URLConnection s = mapServerURL.openConnection();

			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - getServerMapImage - Conection opened for URL: " + mapServerURL);

			if(s.getInputStream() == null)
				return null;
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - getServerMapImage - got data at ObjectInputStream");

			try
			{
				Object readObject = ois.readObject();
				if(readObject instanceof String)
				{
					Environment.log(
							Environment.LOG_LEVEL_FINER,
							(String )readObject);
					return null;
				}
			}
			catch(IOException optExc)
			{	
			}

			int dataSize = imageWidth * imageHeight * 2;
			byte[] img = new byte[dataSize];

			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - getServerMapImage - allocated " + dataSize + " bytes of memory");

			try
			{
				ois.readFully(img);
			}
			catch(EOFException eofExc)
			{
			}
			
			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - getServerMapImage - Read array from stream");

			ois.close();

			Image imageReceived = Toolkit.getDefaultToolkit().createImage(img);

			System.out.println(this.sdFormat.format(new Date(System.currentTimeMillis())) +
					" MIFLNL - getServerMapImage - Image created");
			
			return imageReceived;
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		return null;
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
			this.zoomChanged = true;
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
			
			this.zoomChanged = true;			
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
		int shiftX = (int )(me.getX() - this.startPoint.getX());
		int shiftY = (int )(me.getY() - this.startPoint.getY());

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
		// this.mapImagePanel = this.nmViewer.mapImagePanel;
		// this.localMapJ = this.nmViewer.localMapJ;
	}
	
	/**
	 * 
	 * @param width Ширина сегмента
	 * @param height Высота сегмента
	 * @param centerX X центра сегмента в сферических координатах
	 * @param centerY Y центра сегмента в сферических координатах
	 * @param scale Массштаб
	 * @return Строку параметров для HTTP запроса к серверу
	 * топографии 
	 */
	public String createRenderCommandString(
			int width,
			int height,
			double centerX,
			double centerY,
			double scale)
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_RENDER_IMAGE;
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ width;
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + height;
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + centerX;
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + centerY;
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + scale;

		int index = 0;
		Iterator layersIt = ((MapInfoNetMapViewer )this.viewer).getLayers()
				.iterator();
		for(; layersIt.hasNext();)
		{
			SpatialLayer spL = (SpatialLayer )layersIt.next();
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="
					+ (spL.isVisible() ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index
					+ "=" + (spL.isLabelVisible() ? 1 : 0);
			index++;
		}

		return result;
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
