/*
 * $Id: TopologicalImageCache.java,v 1.9.2.1 2005/04/25 08:38:26 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map.Mapinfo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;
import java.util.ListIterator;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.9.2.1 $, $Date: 2005/04/25 08:38:26 $
 * @module mapinfo_v1
 */
public class TopologicalImageCache
{
	/**
	 * Для кэша по масштабу:
	 * Число изображений с большим и меньшим в MapInfologicalNetLayer.ZOOM_FACTOR
	 * масштабом, которое будет подгружено в кэш масштаба. Приоритет выполнения 
	 * кэширования этих изображений самый низкий. Для SCALE_CACHE_SIZE = 1 будет
	 * подгружено одно изображение с большим и одно с меньшим масштабом.
	 * Для кэша по изменению центра (дискретному):
	 * Число изображений, смещённых на шаг дискретизации
	 */
	private static final int CACHE_SIZE = 2;
	
	/**
	 * "Рекомендуемое" количество сегментов в кэше
	 */
	private static final int CACHE_ELEMENTS_COUNT = 70;
	
	/**
	 * Предельное число "лишних" сегментов в кэше. Если число сегментов
	 * в кэше превысит сумму CACHE_ELEMENTS_COUNT и MAX_EXCEEDING_COUNT,
	 * состоится очистка кэша от MAX_EXCEEDING_COUNT элементов, которые были
	 * использованы наиболе давно.
	 */
	private static final int MAX_EXCEEDING_COUNT = 30;	

	/**
	 * Работа кэша в режиме изменения центра карты
	 */
	private static final int MODE_CENTER_CHANGING = 1;	

	/**
	 * Работа кэша в режиме изменения масштаба
	 */
	private static final int MODE_SCALE_CHANGING = 2;	
	
	/**
	 * Список уже подгруженных для текущего массштаба сегментов,
	 * которые могут быть использованы для отрисовки нового
	 */
	private List cacheOfImages = Collections.synchronizedList(new ArrayList());

	/**
	 * Список сегментов, которые должны быть отображены на текущем изображении
	 */
	private List imagesToPaint = Collections.synchronizedList(new ArrayList());
	
	/**
	 * Отображаемое изображение. На нём отрисовываются все подгруженные
	 * сегменты.
	 */
	private Image visibleImage = null;	
	
	/**
	 * Ссылка на экземпляр потока, осуществляющего подгрузку данных по запросам
	 * к серверу из очереди 
	 */
	private LoadingThread loadingThread = null;
	
	private MapInfoLogicalNetLayer miLayer = null;
	
	/**
	 * Режим работы кэша
	 */
	private int mode = TopologicalImageCache.MODE_CENTER_CHANGING;
	
	private DoublePoint center = null;
	private double scale = 1.f;
	private Dimension imageSize = null;	
	
	public TopologicalImageCache(MapInfoLogicalNetLayer miLayer)
	{
		this.miLayer = miLayer;
		this.scale = this.miLayer.getScale();		
		this.center = this.miLayer.getCenter();		

		this.sizeChanged();
		this.loadingThread = new LoadingThread(miLayer);
		this.loadingThread.start();
	}
	
	public void sizeChanged()
	{
		Dimension newSize = this.miLayer.getMapViewer().getVisualComponent().getSize();
		if ((newSize.width <= 0) || (newSize.height <= 0))
			return;
		
		if (	(this.imageSize == null)
				||(!this.imageSize.equals(newSize)))
		{
			this.visibleImage = new BufferedImage(
					newSize.width,
					newSize.height,
					BufferedImage.TYPE_USHORT_565_RGB);
	
			this.imageSize = newSize;			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - setSize - new visible image is created");
		}
	}
	
	/**
	 * Центр в сферических координатах
	 *
	 */
	public void centerChanged()
	{
		if (this.visibleImage == null)
			return;
		
		DoublePoint newCenter = this.miLayer.getCenter();  
		if (	(this.center == null)
				||(!this.center.equals(newCenter)))
		{
			//Если до этого занимались изменением масштаба - очищаем очередь
			//подгружающего потока, меняем режим работы, очищаем кэш
			if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
				this.imagesToPaint.clear();				
				this.cacheOfImages.clear();
				this.loadingThread.clearQueue();
			}
			
			this.center = newCenter;
			this.createMovingRequests();			
		}
	}

	/**
	 * Масштаб
	 *
	 */
	public void scaleChanged()
	{
		if (this.visibleImage == null)
			return;
		
		double newScale = this.miLayer.getScale();  
		if (this.scale != newScale)
		{
			//Если до этого занимались изменением центра - очищаем очередь
			//подгружающего потока, меняем режим работы, очищаем кэш
			if (this.mode == TopologicalImageCache.MODE_CENTER_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_SCALE_CHANGING;
				this.imagesToPaint.clear();				
				this.cacheOfImages.clear();
				this.loadingThread.clearQueue();				
			}

			//Здесь должно быть известно предыдущее значение масштаба,
			//чтобы отследить динамику
			this.createScaleRequests();

			this.scale = newScale;			
		}
	}
	
	public void refreshLayers()
	{
		//TODO Здесь по-хорошему надо перерисовывать отдельные слои,
		//но такого механизма пока нет.
		this.imagesToPaint.clear();				
		this.cacheOfImages.clear();
		this.loadingThread.clearQueue();

		this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
		this.createMovingRequests();		
	}
	
	public Image getImage()
	{
		if (this.visibleImage == null)
			return null;

		while (this.imagesToPaint.size() > 0)
		{
			for (ListIterator it = this.imagesToPaint.listIterator(); it.hasNext();)
			{
				TopologicalRequest request = (TopologicalRequest)it.next();
				if (request.priority != TopologicalRequest.PRIORITY_ALREADY_LOADED)
					continue;

				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
						" TIC - getImage - painting request: " + request);

				this.visibleImage.getGraphics().drawImage(
						request.image.getImage(),
						request.start.x - 1,
						request.start.y - 1,
						this.miLayer.getMapViewer().getVisualComponent());

				request.lastUsed = System.currentTimeMillis();
				
				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - getImage - request image painted");
				
				it.remove();
			}
			try
			{
				Thread.sleep(20);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - getImage - returning image");
		
		return this.visibleImage;
	}

	/**
	 * Создаёт и кладёт запросы к серверу для кэширования при перемещении центра
	 */
	private void createMovingRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
		//Создаём недостающие изображения
		for (int i = (-1) * TopologicalImageCache.CACHE_SIZE; i <= TopologicalImageCache.CACHE_SIZE; i++)
			for (int j = (-1) * TopologicalImageCache.CACHE_SIZE; j <= TopologicalImageCache.CACHE_SIZE; j++)			
			{
				DoublePoint imageCenter =	this.miLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + i * (int)Math.round(this.imageSize.width * MapFrame.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + j * (int)Math.round(this.imageSize.height * MapFrame.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//Ищем, есть ли уже сегмент с таким центром
				for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
				{
					TopologicalRequest curRequest = (TopologicalRequest)it.next();
					if (this.miLayer.convertMapToScreen(curRequest.topoCenter,imageCenter) < 10)					
					{
						requestForCenter = curRequest;
						break;
					}
				}
				
				if (requestForCenter == null)
				{
					//Если нет
					int priority = TopologicalRequest.PRIORITY_BACKGROUND;
					if ((i == 0) && (j == 0))
						priority = TopologicalRequest.PRIORITY_EXPRESS;
					
					requestForCenter = this.createRequestForExpressArea(
							this.miLayer.getScale(),
							imageCenter,
							priority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//Кладём в очередь на загрузку
					this.loadingThread.addRequest(requestForCenter);
				}

				if (this.miLayer.convertMapToScreen(requestForCenter.topoCenter,this.miLayer.getCenter()) < 10)
					//Кладём сегмент в очередь на отрисовку
					this.imagesToPaint.add(requestForCenter);
			}
		
		//Если в кэше слишком много сегментов удаляем самые старые				
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
				TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
	
	/**
	 * Чистит список подгруженных изображений
	 *
	 */
	private void clearOldSegments()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - just entered.");
		
		//Сортируем отчёты (по убыванию, чтобы более новые потом и искать недолго было)
		Collections.sort(this.cacheOfImages);

		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Collection sorted.");
		
		//Удаляем последние MAX_EXCEEDING_COUNT элементов
		for (ListIterator lIt =	this.cacheOfImages.listIterator(TopologicalImageCache.CACHE_ELEMENTS_COUNT -
																								TopologicalImageCache.MAX_EXCEEDING_COUNT);
					lIt.hasNext();)
		{
			lIt.next();
			lIt.remove();
		}
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Old elements removed. Exiting.");
	}
	
	//////////////////////////////////////Функции для кэша по масштабу
	
	/**
	 * Подгружает изображения с большим и меньшим масштабами для текущего
	 * масштаба
	 */
	private void createScaleRequests()
	{
		if (	(this.cacheOfImages.size() == 0)
				||(		(!TopologicalImageCache.compare(this.scale,this.miLayer.getScale() * MapInfoLogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * MapInfoLogicalNetLayer.ZOOM_FACTOR,this.miLayer.getScale()))))
		{
			// Если новый масштаб не является кратным предыдущему (zoom_to_box)
			// или мы только что вышли из режима изменения центра
			// грузим все изображения заново.
			renewScaleImages();
			return;
		}
		
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (TopologicalImageCache.compare(curRequest.topoScale,this.miLayer.getScale()))
			{
				//Кладём сегмент в очередь на отрисовку
				this.imagesToPaint.add(curRequest);
				break;
			}
		}
		
		//Массштаб, который возможно понадобится
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale,this.miLayer.getScale() * MapInfoLogicalNetLayer.ZOOM_FACTOR))		
		{
			//Новый масштаб в ZOOM_FACTOR раз меньше предыдущего
			scaleToCheck = this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);
		}
		else if (TopologicalImageCache.compare(this.scale * MapInfoLogicalNetLayer.ZOOM_FACTOR,this.miLayer.getScale()))
		{
			//Новый масштаб в ZOOM_FACTOR раз больше предыдущего
			scaleToCheck = this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);			
		}
		
		//Ищем изображение с таким масштабом
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (TopologicalImageCache.compare(curRequest.topoScale,scaleToCheck))
				//Есть такое, не надо подгружать
				return;
		}

		//Нет такого - создаём изображение
		TopologicalRequest newImageRequest =
			this.createRequestForExpressArea(scaleToCheck,this.miLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND);
		
		this.cacheOfImages.add(newImageRequest);
		//Кладём сегмент на загрузку
		this.loadingThread.addRequest(newImageRequest);
		
		//Если в кэше слишком много сегментов удаляем самые старые				
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
				TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
	}

	
	private static boolean compare (double p1, double p2)
	{
		if ((long) (p1 * 1000000D) == (long) (p2 * 1000000D))
			return true;
		
		return false;
	}
	
	/**
	 * Заново подгружает ВСЕ изображения с большим и меньшим масштабами
	 */
	private void renewScaleImages()
	{
		//Рисуем текущее изображение
		TopologicalRequest currImageRequest =	this.createRequestForExpressArea(
				this.miLayer.getScale(),
				this.miLayer.getCenter(),
				TopologicalRequest.PRIORITY_EXPRESS);

		//кладём в кэш
		this.cacheOfImages.add(currImageRequest);
		//Кладём в очередь на отрисовку
		this.imagesToPaint.add(currImageRequest);		
		//Кладём в очередь на загрузку		
		this.loadingThread.addRequest(currImageRequest);
		
		//Делаем изображения большего и меньшего изображения
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
		{
			//Маленькое
			TopologicalRequest smallScaledImage =	this.createRequestForExpressArea(
					this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND);

			this.cacheOfImages.add(smallScaledImage);
			
			//Кладём в очередь на загрузку
			this.loadingThread.addRequest(smallScaledImage);
			
			//Большое
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND);

			this.cacheOfImages.add(bigScaledImage);
			
			//Кладём в очередь на загрузку
			this.loadingThread.addRequest(bigScaledImage);
		}
	}
	
	private TopologicalRequest createRequestForExpressArea (
			double asScale,
			DoublePoint asCenter, 
			int asPriority)
	{
		TopologicalRequest result = new TopologicalRequest();
		result.lastUsed = System.currentTimeMillis();
		
		result.priority = asPriority;
		result.isUsedForCurrentImage = false;

		result.topoScale = asScale;
		result.topoCenter = asCenter;
		
		result.size = new Dimension(this.imageSize.width + 2,this.imageSize.height + 2);
		result.start = new Point(0,0);
		
		return result;
	}
}

class LoadingThread extends Thread
{
	private MapInfoLogicalNetLayer miLayer = null;
	private String uriString = null;
	/**
	 * Очередь запросов на получение изображений с сервера
	 */
	private List requestQueue =
		Collections.synchronizedList(new LinkedList());	
	
	private boolean toBreak = false;
	
	private byte[] imageBuffer = null;
	
	public LoadingThread(
			MapInfoLogicalNetLayer miLayer)
	{
		this.miLayer = miLayer;
		this.uriString = this.miLayer.getMapViewer().getConnection().getURL();
		
		Dimension maximumImageSize = Toolkit.getDefaultToolkit().getScreenSize();
		int dataSize = maximumImageSize.width * maximumImageSize.height * 2;

		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - setImage - allocating for image buffer " + dataSize + " bytes of memory");
		
		this.imageBuffer = new byte[dataSize];
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - setImage - memory allocated");
	}
	
	public void run()
	{
		while (!this.toBreak)
		{
			//Получаем первый в очереди запрос
			TopologicalRequest request = null;
			synchronized (this.requestQueue)
			{
				if (!this.requestQueue.isEmpty())
				{
					request = (TopologicalRequest)this.requestQueue.get(0);
					this.requestQueue.remove(request);
				}
			}
			
			if (request == null)
			{
				//Запросов к серверу нет
				try
				{
					Thread.sleep(50);
					continue;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - loading request: " + request);
			
			//Формируем строку HTTP запроса
			String requestString = new String(this.uriString);
			
			try
			{
				requestString += this.createRenderCommandString(request);

				//Получили изображение
				request.image = this.getServerMapImage(requestString);
				request.priority = TopologicalRequest.PRIORITY_ALREADY_LOADED;

				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingThread - run - request image loaded");
				
			} catch (MapDataException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cancel()
	{
		this.toBreak = true;
	}
	
	/**
	 * Добавляет запрос в очередь, сортируя при этом запросы по приоритету
	 * @param request Запрос
	 */
	public void addRequest(TopologicalRequest request)
	{
		//Ищем первый запрос с приоритетом ниже, чем у нового запроса
		//К сожалению нельзя воспользоваться итератором
		for (int i = 0; i < this.requestQueue.size(); i++)
		{
			TopologicalRequest curRequest = 
				(TopologicalRequest) this.requestQueue.get(i);
			if (request.priority < curRequest.priority)
			{
				//Нашли
				this.requestQueue.add(i,request);
				return;
			}
		}
		//Не нашли
		this.requestQueue.add(request);			
	}

	public void removeRequest(TopologicalRequest request)
	{
		this.requestQueue.remove(request);			
	}
	
	/**
	 * Меняет приоритет невыполненного запроса в очереди
	 * @param request Запрос
	 * @param newPriority Новый приоритет
	 */
	public void changeRequestPriority(TopologicalRequest request, int newPriority)
	{
		//Удаляем запрос из очереди
		this.requestQueue.remove(request);
		
		//Задаём ему новый приоритет
		request.priority = newPriority;
		
		//Снова ставим запрос в очередь
		this.addRequest(request);
	}

	public void clearQueue()
	{
		this.requestQueue.clear();
	}
	/**
	 * Подгружает изображение с сервера по HTTP-запросу
	 * @param requestURIString Строка запроса
	 * @return Изображение
	 */
	private ImageIcon getServerMapImage(String requestURIString)
	{
		try
		{
			URI mapServerURI = new URI(requestURIString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());

			URLConnection s = mapServerURL.openConnection();

			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - Conection opened for URL: " + mapServerURL);

			if(s.getInputStream() == null)
				return null;
			
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - got data at ObjectInputStream");

			try
			{
				Object readObject = ois.readObject();
				System.out.println(readObject);
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

			try
			{
				ois.readFully(this.imageBuffer);
			}
			catch(EOFException eofExc)
			{
			}
			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - Read array from stream");

			ois.close();

			Image imageReceived = Toolkit.getDefaultToolkit().createImage(this.imageBuffer);

			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - Image created");
			
			return new ImageIcon(imageReceived);
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Создаёт по текущему запросу строку параметров для HTTP запроса к серверу
	 * топографии для получения графических данных
	 * @param request Запрос
	 * @return Строка параметров для HTTP запроса к серверу
	 * топографии 
	 */
	private String createRenderCommandString(TopologicalRequest request)
		throws MapDataException
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_RENDER_IMAGE;
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ request.size.width;
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + request.size.height;
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + request.topoCenter.getX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + request.topoCenter.getY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + request.topoScale;

		int index = 0;
		
		Iterator layersIt = this.miLayer.getMapViewer().getLayers()
				.iterator();
		for(; layersIt.hasNext();)
		{
			SpatialLayer spL = (SpatialLayer )layersIt.next();
			
			//Видимость слоя зависит от того, хочет ли его видеть клиент, виден ли он при текущем масштабе на сервере
			//и надо ли отображать объекты для текущего запроса
			boolean toShowLayerObjects =	spL.isVisible()
				&& spL.isVisibleAtScale(this.miLayer.getScale());

			//то же самое для надписей
			boolean toShowLayerLabels =	spL.isLabelVisible()
				&& spL.isVisibleAtScale(this.miLayer.getScale());
			
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="	+ (toShowLayerObjects ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index + "=" + (toShowLayerLabels ? 1 : 0);

			index++;
		}

		return result;
	}
}
/**
 * Структура запроса изображения с сервера
 * @author $Author: peskovsky $
 * @version $Revision: 1.9.2.1 $, $Date: 2005/04/25 08:38:26 $
 * @module mapinfo_v1
 */
class TopologicalRequest implements Comparable
{
	/**
	 * Равен true для участков изображения, уже включёных в какой-либо запрос
	 * для текущего изображения - их будут отрисовывать
	 */
	public boolean isUsedForCurrentImage = false;
	
	/**
	 * Для участков изображения, добавляемых в кэш "на всякий случай",
	 * до запроса пользователя и перерисовываемых в фоновом режиме
	 */
	public static final int PRIORITY_BACKGROUND = 2;
	/**
	 * Для участков изображения, уже подгруженных, которые требуется
	 * только перерисовать
	 */
	public static final int PRIORITY_ALREADY_LOADED = 1;
	/**
	 * Для участков изображения, которые требуется отобразить по
	 * текущему запросу пользователя (самый высокий приоритет)
	 */
	public static final int PRIORITY_EXPRESS = 0;	
	/**
	 * Приоритет запроса
	 */
	protected int priority = TopologicalRequest.PRIORITY_BACKGROUND;
	/**
	 * Отображаемое изображение
	 */
	protected ImageIcon image = null;
	/**
	 * Габариты изображения в пикселях
	 */
	protected Dimension size = null;
	/**
	 * Масштаб избражения (для текущей ширины изображения!)
	 */
	protected double topoScale = 1.f;
	/**
	 * Сферические координаты границы избражения
	 */
	protected DoublePoint topoCenter = null;
	/**
	 * Сферические координаты границ избражения
	 */
	protected Rectangle2D.Double topoBounds = null;
	/**
	 * Экранные координаты в пикселях для левого-верхнего угла участка на кэш-изображении
	 */
	protected Point start = null;
	/**
	 * Время последнего использования
	 */
	protected long lastUsed = 0;
	
	public String toString()
	{
		String resultString = 
			"priority (" + this.priority + "), " +
			"screen start (" + this.start.x + ":" + this.start.y + "), " + 
			"screen width/height (" + this.size.width + ":" + this.size.height + "), " +
			"topo scale (" + this.topoScale + "), " +
			"topo center (" + this.topoCenter.getX() + ":" + this.topoCenter.getY() + ")";
		
		return resultString;
	}

	/**
	 * Для сортировки отчётов по убыванию по времени 
	 */
	public int compareTo(Object o)
	{
		TopologicalRequest req = (TopologicalRequest)o;
		if (this.lastUsed > req.lastUsed)
			return -1;
		else if (this.lastUsed < req.lastUsed)
			return 1;
		
		return 0;
	}
}
