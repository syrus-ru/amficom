/*
 * $Id: TopologicalImageCache.java,v 1.9.2.5 2005/05/04 13:14:09 peskovsky Exp $
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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
 * @version $Revision: 1.9.2.5 $, $Date: 2005/05/04 13:14:09 $
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
	
	private Dimension discreteShifts = null;
	
	
	public void sample()
	{
		TopologicalRequest req1 = this.createRequestForExpressArea(
				1000.d,
				new DoublePoint(37.5199,55.7446),
				TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE);
		
		TopologicalRequest req2 = this.createRequestForExpressArea(
				8000.d,
				new DoublePoint(37.5199,55.7446),
				TopologicalRequest.PRIORITY_BACKGROUND_LOW);
		
		
		this.loadingThread.addRequest(req1);
		this.loadingThread.addRequest(req2);		
		try
		{
			Thread.sleep(100);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		this.loadingThread.removeRequest(req1);
		this.loadingThread.changeRequestPriority(req2,TopologicalRequest.PRIORITY_EXPRESS);
	}
	
	
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
			else
			{
				//Дискретное смещение в экранных координатах относительно предыдущего центра
				this.discreteShifts = this.getDiscreteShifts();
			}
			
			this.createMovingRequests();
			this.center = newCenter;			
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
//		if (this.visibleImage == null)
//			return null;
//
//		while (this.imagesToPaint.size() > 0)
//		{
//			for (ListIterator it = this.imagesToPaint.listIterator(); it.hasNext();)
//			{
//				TopologicalRequest request = (TopologicalRequest)it.next();
//				if (request.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
//					continue;
//
//				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
//						" TIC - getImage - painting request: " + request);
//
//				this.visibleImage.getGraphics().drawImage(
//						request.getImage().getImage(),
//						0,
//						0,
//						this.miLayer.getMapViewer().getVisualComponent());
//
//				request.setLastUsed(System.currentTimeMillis());
//				
//				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
//					" TIC - getImage - request image painted");
//				
//				it.remove();
//			}
//			try
//			{
//				Thread.sleep(20);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
//			" TIC - getImage - returning image");
		this.sample();
		return this.visibleImage;
	}

	/**
	 * Создаёт и кладёт запросы к серверу для кэширования при перемещении центра
	 */
	private void createMovingRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
		//Создаём недостающие изображения, постепенно увеличивая окрестность
		for (int i = 0; i <= TopologicalImageCache.CACHE_SIZE; i++)
			this.createMovingRequests(i);
			
		//Удаляем неподгруженные очёты вышедшие из кэш области
		this.clearFarAndUnloadedSegments();
		
		//Если в кэше слишком много сегментов удаляем самые старые				
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
				TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
	/**
	 * проверяет наличие и при необходимости создаёт сегменты на заданной окрестности
	 * @param neighbourhood Окрестность
	 */
	private void createMovingRequests(int neighbourhood)
	{
		//Создаём недостающие изображения
		for (int i = (-1) * neighbourhood; i <= neighbourhood; i++)
			for (int j = (-1) * neighbourhood; j <= neighbourhood; j++)			
			{
				if (	(Math.abs(i) != neighbourhood)
						&& (Math.abs(j) != neighbourhood))
					//Мы работаем только с границей окрестности
					continue;
				
				DoublePoint imageCenter =	this.miLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + j * (int)Math.round(this.imageSize.width * MapFrame.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + i * (int)Math.round(this.imageSize.height * MapFrame.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//Ищем, есть ли уже сегмент с таким центром
				for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
				{
					TopologicalRequest curRequest = (TopologicalRequest)it.next();
					if (this.miLayer.convertMapToScreen(curRequest.getTopoCenter(),imageCenter) < 10)					
					{
						requestForCenter = curRequest;
						break;
					}
				}
				
				//Приоритет, который должен быть у данного запроса в текущий запрос
				int requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_LOW;
				if ((i == 0) && (j == 0))
					requestCurPriority = TopologicalRequest.PRIORITY_EXPRESS;
				else
				{
					if (	((this.discreteShifts.width * j > 0) || (this.discreteShifts.width == j))
							&&((this.discreteShifts.height * i > 0) || (this.discreteShifts.height == i)))
						//Если текущий радиус-вектор сонаправлен с направлением движения
						if (neighbourhood == 1)
							//Ближайший по тому же направлению сегмент
							requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_HIGH;
						else
							//В том же квадранте
							requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE;							
				}				
				
				if (requestForCenter != null)
				{
					if (	(requestForCenter.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
							&&(requestForCenter.getPriority() != requestCurPriority))
						//Если текущий приоритет запроса ниже предлагаемого (в принятой
						//численной системе приоритетов - больше)
						this.loadingThread.changeRequestPriority(requestForCenter,requestCurPriority);
				}
				else
				{
					//Если нет
					requestForCenter = this.createRequestForExpressArea(
							this.miLayer.getScale(),
							imageCenter,
							requestCurPriority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//Кладём в очередь на загрузку
					this.loadingThread.addRequest(requestForCenter);
				}
				
				if (this.miLayer.convertMapToScreen(requestForCenter.getTopoCenter(),this.miLayer.getCenter()) < 10)
					//Кладём сегмент в очередь на отрисовку
					this.imagesToPaint.add(requestForCenter);
			}
	}
	
	/**
	 * Удаляет сегменты, которые
	 * а)были помещены в кэш загрузку, но
	 * не успели подгрузиться,
	 * б)центр которых отступает от текущего центра более, чем на
	 * TopologicalImageCache.SCALE_SIZE * MapFrame.MOVE_CENTER_STEP_SIZE *
	 * _VisualComponent_.getSize();
	 *
	 */
	private void clearFarAndUnloadedSegments()
	{
		Point upperLeftScreen = new Point(
				(int)Math.round(this.imageSize.width / 2 - MapFrame.MOVE_CENTER_STEP_SIZE *
						this.imageSize.width * 2.d - 10.d),
				(int)Math.round(this.imageSize.height / 2 - MapFrame.MOVE_CENTER_STEP_SIZE *
						this.imageSize.height * 2.D - 10.d));
		
		Point downRightScreen = new Point(
				(int)Math.round(this.imageSize.width / 2 + MapFrame.MOVE_CENTER_STEP_SIZE *
						this.imageSize.width * 2.D + 10.d),
				(int)Math.round(this.imageSize.height / 2 + MapFrame.MOVE_CENTER_STEP_SIZE *
						this.imageSize.height * 2.D + 10.d));
		
		DoublePoint upperLeftSph = this.miLayer.convertScreenToMap(upperLeftScreen);
		DoublePoint downRightSph = this.miLayer.convertScreenToMap(downRightScreen);		
		
		double x = (upperLeftSph.getX() < downRightSph.getX()) ? upperLeftSph.getX() : downRightSph.getX();
		double y = (upperLeftSph.getY() < downRightSph.getY()) ? upperLeftSph.getY() : downRightSph.getY();		
		
		Rectangle2D.Double currCacheBorders = new Rectangle2D.Double(
				x,
				y,
				Math.abs(downRightSph.getX() - upperLeftSph.getX()),
				Math.abs(downRightSph.getY() - upperLeftSph.getY()));
		
		//Ищем, есть ли уже сегмент с таким центром
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (	(!currCacheBorders.contains(curRequest.getTopoCenter().getX(),curRequest.getTopoCenter().getY()))
					&&(curRequest.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED))					
			{
				//Удаляем сегмент - не имеет смысла его подгружает
				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - clearFarAndUnloadedSegments - removing request." + curRequest);
				this.loadingThread.removeRequest(curRequest);
				this.imagesToPaint.remove(curRequest);
				it.remove();
			}
		}
	}
	
	/**
	 * Возвращает дискретные смещения относительно предыдущего центра
	 * @return смещения
	 */
	private Dimension getDiscreteShifts()
	{
		Point prevScreenCenter = this.miLayer.convertMapToScreen(this.center);
		Point currScreenCenter = this.miLayer.convertMapToScreen(this.miLayer.getCenter());
		
		Dimension result = this.miLayer.getDiscreteShifts(
				currScreenCenter.x - prevScreenCenter.x,
				currScreenCenter.y - prevScreenCenter.y);
		
		return result;
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
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),this.miLayer.getScale()))
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
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),scaleToCheck))
				//Есть такое, не надо подгружать
				return;
		}

		//Нет такого - создаём изображение
		TopologicalRequest newImageRequest =
			this.createRequestForExpressArea(scaleToCheck,this.miLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND_HIGH);
		
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
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

			this.cacheOfImages.add(smallScaledImage);
			
			//Кладём в очередь на загрузку
			this.loadingThread.addRequest(smallScaledImage);
			
			//Большое
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

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
		result.setLastUsed(System.currentTimeMillis());
		
		result.setPriority(asPriority);

		result.setTopoScale(asScale);
		result.setTopoCenter(asCenter);
		
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
	
	/**
	 * Запрос, обрабатываемый в текущий момент
	 */
	private TopologicalRequest requestCurrentlyProcessed = null;
	
	/**
	 * Флаг, чтобы остановить поток
	 */
	private boolean toBreak = false;
	
	/**
	 * Флаг, чтобы прервать получение изображения с сервера
	 */
	private boolean toBreakGettingRendition = false;
	
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
			Iterator rqIt = this.requestQueue.iterator();
			if (rqIt.hasNext())
			{
				this.requestCurrentlyProcessed = (TopologicalRequest)rqIt.next();
				rqIt.remove();
			}
			
			if (this.requestCurrentlyProcessed == null)
			{
				//Запросов к серверу нет
				try
				{
					Thread.sleep(10);
					continue;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - loading request: " + this.requestCurrentlyProcessed);

			this.toBreakGettingRendition = false;
			
			//Посылаем запрос на рендеринг			
			this.renderMapImageAtServer(this.requestCurrentlyProcessed);

			ImageIcon imageForRequest = null;
			while (		(imageForRequest == null)
							&&(!this.toBreakGettingRendition))
			{
				//Ждём получения изображения или сигнала на прерывание
				try
				{
					Thread.sleep(20);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				imageForRequest = this.getServerMapImage();
			}
			
			this.requestCurrentlyProcessed.setImage(imageForRequest);
			this.requestCurrentlyProcessed.setPriority(TopologicalRequest.PRIORITY_ALREADY_LOADED);
			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - image loaded for request (" + this.requestCurrentlyProcessed + ")");
				
			synchronized (this.requestCurrentlyProcessed)
			{
				this.requestCurrentlyProcessed = null;
			}
		}
	}
	
	public void cancel()
	{
		this.toBreak = true;
		this.toBreakGettingRendition = true;
	}
	
	/**
	 * Добавляет запрос в очередь, сортируя при этом запросы по приоритету
	 * @param requestToAdd Запрос
	 */
	public void addRequest(TopologicalRequest requestToAdd)
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - addRequest - adding request (" + requestToAdd + ")");
		
		//Ищем первый запрос с приоритетом ниже, чем у нового запроса
		ListIterator lIt = this.requestQueue.listIterator();
		for (;lIt.hasNext();)
		{
			TopologicalRequest curRequest = 
				(TopologicalRequest) lIt.next();
			if (requestToAdd.getPriority() < curRequest.getPriority())
			{
				//Добавляем перед этим запросом
				lIt.previous();
				lIt.add(requestToAdd);
				return;
			}
		}

		//Не нашли - добавляем в конец очереди
		lIt.add(requestToAdd);
		
		//Если приоритет добавляемого запроса выше, чем у запроса, находящегося в обработке
		synchronized (this.requestCurrentlyProcessed)
		{
			if (	(this.requestCurrentlyProcessed != null) 
					&&(this.requestCurrentlyProcessed.getPriority() > requestToAdd.getPriority()))
				this.stopRenderingAtServer();
		}		
	}

	public void removeRequest(TopologicalRequest request)
	{
		if (this.requestCurrentlyProcessed == request)
			//Если мы удаляем запрос, который уже в обработке - то его уже нет в очереди.
			//поэтому просто останавливаем рендеринг.
			this.stopRenderingAtServer();
		else
			//Убираем запрос из очереди
			this.requestQueue.remove(request);
	}
	
	/**
	 * Меняет приоритет невыполненного запроса в очереди
	 * @param request Запрос
	 * @param newPriority Новый приоритет
	 */
	public void changeRequestPriority(TopologicalRequest request, int newPriority)
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - loadingThread - changeRequestPriority - changing request's (" + request +
			") priority for " + newPriority);

		//Удаляем запрос из очереди
		if (this.requestQueue.remove(request))
		{
			//Если запрос содержался в очереди
			//Задаём ему новый приоритет
			request.setPriority(newPriority);
			
			//Снова ставим запрос в очередь
			this.addRequest(request);
		}
	}

	public void clearQueue()
	{
		this.requestQueue.clear();
	}
	
	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	private void stopRenderingAtServer()
	{
		this.toBreakGettingRendition = true;
		
		String requestString = new String(
				this.uriString + "?" + ServletCommandNames.COMMAND_NAME + "="
				+ ServletCommandNames.CN_CANCEL_RENDERING);

		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - stopping rendering at server");
		
		try
		{
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection connection = mapServerURL.openConnection();
			
			if(connection.getInputStream() == null)
				return;
			
			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - got data at ObjectInputStream");

			Object readObject = ois.readObject();
			System.out.println(readObject);
			if(readObject instanceof String)
			{
				Environment.log(
						Environment.LOG_LEVEL_FINER,
						(String )readObject);
				return;
			}
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 */
	private void renderMapImageAtServer(TopologicalRequest request)
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - loadingthread - starting rendering at server");

		try
		{
			String requestString = this.uriString + this.createRenderCommandString(request);			
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection connection = mapServerURL.openConnection();
			
			if(connection.getInputStream() == null)
				return;
			
			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - got data at ObjectInputStream");

			Object readObject = ois.readObject();
			System.out.println(readObject);
			if(readObject instanceof String)
			{
				Environment.log(
						Environment.LOG_LEVEL_FINER,
						(String )readObject);
				return;
			}
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Подгружает изображение с сервера по HTTP-запросу
	 * @return Изображение
	 */
	private ImageIcon getServerMapImage()
	{
		try
		{
			String requestURIString = new String(
					this.uriString + "?" + ServletCommandNames.COMMAND_NAME + "="
					+ ServletCommandNames.CN_GET_RENDITION);
			
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
	 * топографии для рендеринга графических данных
	 * @param request Запрос
	 * @return Строка параметров для HTTP запроса к серверу
	 * топографии 
	 */
	private String createRenderCommandString(TopologicalRequest request)
		throws MapDataException
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_START_RENDER_IMAGE;
		
		Dimension size = this.miLayer.getMapViewer().getVisualComponent().getSize();
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ size.width;
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + size.height;
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + request.getTopoCenter().getX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + request.getTopoCenter().getY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + request.getTopoScale();

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
 * @version $Revision: 1.9.2.5 $, $Date: 2005/05/04 13:14:09 $
 * @module mapinfo_v1
 */
class TopologicalRequest implements Comparable
{
	/**
	 * Для участков изображения, добавляемых в кэш "на всякий случай",
	 * до запроса пользователя и перерисовываемых в фоновом режиме
	 */
	private static final int PRIORITY_BACKGROUND = 20;
	
	public static final int PRIORITY_BACKGROUND_HIGH = TopologicalRequest.PRIORITY_BACKGROUND + 1;
	public static final int PRIORITY_BACKGROUND_MIDDLE = TopologicalRequest.PRIORITY_BACKGROUND + 2;	
	public static final int PRIORITY_BACKGROUND_LOW = TopologicalRequest.PRIORITY_BACKGROUND + 3;

	/**
	 * Для участков изображения, которые требуется отобразить по
	 * текущему запросу пользователя (самый высокий приоритет)
	 */
	public static final int PRIORITY_EXPRESS = 10;	
	
	/**
	 * Для участков изображения, уже подгруженных, которые требуется
	 * только перерисовать
	 */
	public static final int PRIORITY_ALREADY_LOADED = 0;
	
	/**
	 * Приоритет запроса
	 */
	private int priority = TopologicalRequest.PRIORITY_BACKGROUND;
	
	/**
	 * 
	 */
	private boolean isCurrentlyProcessed = false;
	
	/**
	 * Отображаемое изображение
	 */
	private ImageIcon image = null;
	/**
	 * Масштаб избражения (для текущей ширины изображения!)
	 */
	private double topoScale = 1.f;
	/**
	 * Сферические координаты границы избражения
	 */
	private DoublePoint topoCenter = null;
	/**
	 * Время последнего использования
	 */
	private long lastUsed = 0;
	
	public String toString()
	{
		String resultString = 
			"priority (" + this.priority + "), " +
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
	/**
	 * @return Returns the image.
	 */
	public synchronized ImageIcon getImage()
	{
		return this.image;
	}
	/**
	 * @param image The image to set.
	 */
	public synchronized void setImage(ImageIcon image)
	{
		this.image = image;
	}
	/**
	 * @return Returns the lastUsed.
	 */
	public synchronized long getLastUsed()
	{
		return this.lastUsed;
	}
	/**
	 * @param lastUsed The lastUsed to set.
	 */
	public synchronized void setLastUsed(long lastUsed)
	{
		this.lastUsed = lastUsed;
	}
	/**
	 * @return Returns the priority.
	 */
	public synchronized int getPriority()
	{
		return this.priority;
	}
	/**
	 * @param priority The priority to set.
	 */
	public synchronized void setPriority(int priority)
	{
		this.priority = priority;
	}
	/**
	 * @return Returns the topoCenter.
	 */
	public synchronized DoublePoint getTopoCenter()
	{
		return this.topoCenter;
	}
	/**
	 * @param topoCenter The topoCenter to set.
	 */
	public synchronized void setTopoCenter(DoublePoint topoCenter)
	{
		this.topoCenter = topoCenter;
	}
	/**
	 * @return Returns the topoScale.
	 */
	public synchronized double getTopoScale()
	{
		return this.topoScale;
	}
	/**
	 * @param topoScale The topoScale to set.
	 */
	public synchronized void setTopoScale(double topoScale)
	{
		this.topoScale = topoScale;
	}
	/**
	 * @return Returns the isCurrentlyProcessed.
	 */
}
