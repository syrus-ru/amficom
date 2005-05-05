/*
 * $Id: TopologicalImageCache.java,v 1.1.2.1 2005/05/05 10:22:38 krupenn Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: krupenn $
 * @version $Revision: 1.1.2.1 $, $Date: 2005/05/05 10:22:38 $
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
	
	private LogicalNetLayer logicalNetLayer = null;
	
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
	
	
	public TopologicalImageCache(LogicalNetLayer miLayer, MapImageLoader loader)
			throws MapConnectionException, MapDataException
	{
		this.logicalNetLayer = miLayer;
		this.scale = this.logicalNetLayer.getScale();		
		this.center = this.logicalNetLayer.getCenter();		

		this.sizeChanged();
		this.loadingThread = new LoadingThread(loader);
		this.loadingThread.start();
	}
	
	public void sizeChanged()
	{
		Dimension newSize = this.logicalNetLayer.getMapViewer().getVisualComponent().getSize();
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
			System.out.println(MapPropertiesManager.getDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - setSize - new visible image is created");
		}
	}
	
	/**
	 * Центр в сферических координатах
	 *
	 */
	public void centerChanged()
			throws MapConnectionException, MapDataException
	{
		if (this.visibleImage == null)
			return;
		
		DoublePoint newCenter = this.logicalNetLayer.getCenter();  
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
			throws MapConnectionException, MapDataException
	{
		if (this.visibleImage == null)
			return;
		
		double newScale = this.logicalNetLayer.getScale();  
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
			throws MapConnectionException, MapDataException
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
				if (request.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
					continue;

				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
						" TIC - getImage - painting request: " + request);

				this.visibleImage.getGraphics().drawImage(
						request.getImage().getImage(),
						0,
						0,
						this.logicalNetLayer.getMapViewer().getVisualComponent());

				request.setLastUsed(System.currentTimeMillis());
				
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - getImage - returning image");
//		this.sample();
		return this.visibleImage;
	}

	/**
	 * Создаёт и кладёт запросы к серверу для кэширования при перемещении центра
	 */
	private void createMovingRequests()
			throws MapConnectionException, MapDataException
	{
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
		
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
	/**
	 * Считает экранное расстояние между двумя точками в сферических координатах
	 * @param sphP1 
	 * @param sphP2
	 * @return Расстояние
	 */
	double convertMapToScreen(DoublePoint sphP1, DoublePoint sphP2)
			throws MapConnectionException, MapDataException
	{
		Point p1 = this.logicalNetLayer.convertMapToScreen(sphP1);
		Point p2 = this.logicalNetLayer.convertMapToScreen(sphP2);		
		
		double returnValue = Math.pow((Math.pow(p2.x - p1.x,2) + Math.pow(p2.y - p1.y,2)),0.5);
		return returnValue;
	}
	
	/**
	 * проверяет наличие и при необходимости создаёт сегменты на заданной окрестности
	 * @param neighbourhood Окрестность
	 */
	private void createMovingRequests(int neighbourhood)
			throws MapConnectionException, MapDataException
	{
		//Создаём недостающие изображения
		for (int i = (-1) * neighbourhood; i <= neighbourhood; i++)
			for (int j = (-1) * neighbourhood; j <= neighbourhood; j++)			
			{
				if (	(Math.abs(i) != neighbourhood)
						&& (Math.abs(j) != neighbourhood))
					//Мы работаем только с границей окрестности
					continue;
				
				DoublePoint imageCenter =	this.logicalNetLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + j * (int)Math.round(this.imageSize.width * LogicalNetLayer.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + i * (int)Math.round(this.imageSize.height * LogicalNetLayer.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//Ищем, есть ли уже сегмент с таким центром
				for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
				{
					TopologicalRequest curRequest = (TopologicalRequest)it.next();
					if (convertMapToScreen(curRequest.getTopoCenter(),imageCenter) < 10)					
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
							this.logicalNetLayer.getScale(),
							imageCenter,
							requestCurPriority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//Кладём в очередь на загрузку
					this.loadingThread.addRequest(requestForCenter);
				}
				
				if (convertMapToScreen(requestForCenter.getTopoCenter(),this.logicalNetLayer.getCenter()) < 10)
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
			throws MapConnectionException, MapDataException
	{
		Point upperLeftScreen = new Point(
				(int)Math.round(this.imageSize.width / 2 - LogicalNetLayer.MOVE_CENTER_STEP_SIZE *
						this.imageSize.width * 2.d - 10.d),
				(int)Math.round(this.imageSize.height / 2 - LogicalNetLayer.MOVE_CENTER_STEP_SIZE *
						this.imageSize.height * 2.D - 10.d));
		
		Point downRightScreen = new Point(
				(int)Math.round(this.imageSize.width / 2 + LogicalNetLayer.MOVE_CENTER_STEP_SIZE *
						this.imageSize.width * 2.D + 10.d),
				(int)Math.round(this.imageSize.height / 2 + LogicalNetLayer.MOVE_CENTER_STEP_SIZE *
						this.imageSize.height * 2.D + 10.d));
		
		DoublePoint upperLeftSph = this.logicalNetLayer.convertScreenToMap(upperLeftScreen);
		DoublePoint downRightSph = this.logicalNetLayer.convertScreenToMap(downRightScreen);		
		
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
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
			throws MapConnectionException, MapDataException
	{
		Point prevScreenCenter = this.logicalNetLayer.convertMapToScreen(this.center);
		Point currScreenCenter = this.logicalNetLayer.convertMapToScreen(this.logicalNetLayer.getCenter());
		
		Dimension result = this.logicalNetLayer.getDiscreteShifts(
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
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - just entered.");
		
		//Сортируем отчёты (по убыванию, чтобы более новые потом и искать недолго было)
		Collections.sort(this.cacheOfImages);

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Collection sorted.");
		
		//Удаляем последние MAX_EXCEEDING_COUNT элементов
		for (ListIterator lIt =	this.cacheOfImages.listIterator(TopologicalImageCache.CACHE_ELEMENTS_COUNT -
																								TopologicalImageCache.MAX_EXCEEDING_COUNT);
					lIt.hasNext();)
		{
			lIt.next();
			lIt.remove();
		}
		
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Old elements removed. Exiting.");
	}
	
	//////////////////////////////////////Функции для кэша по масштабу
	
	/**
	 * Подгружает изображения с большим и меньшим масштабами для текущего
	 * масштаба
	 */
	private void createScaleRequests()
			throws MapConnectionException, MapDataException
	{
		if (	(this.cacheOfImages.size() == 0)
				||(		(!TopologicalImageCache.compare(this.scale,this.logicalNetLayer.getScale() * LogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR,this.logicalNetLayer.getScale()))))
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
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),this.logicalNetLayer.getScale()))
			{
				//Кладём сегмент в очередь на отрисовку
				this.imagesToPaint.add(curRequest);
				break;
			}
		}
		
		//Массштаб, который возможно понадобится
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale,this.logicalNetLayer.getScale() * LogicalNetLayer.ZOOM_FACTOR))		
		{
			//Новый масштаб в ZOOM_FACTOR раз меньше предыдущего
			scaleToCheck = this.logicalNetLayer.getScale() / Math.pow(LogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);
		}
		else if (TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR,this.logicalNetLayer.getScale()))
		{
			//Новый масштаб в ZOOM_FACTOR раз больше предыдущего
			scaleToCheck = this.logicalNetLayer.getScale() * Math.pow(LogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);			
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
			this.createRequestForExpressArea(scaleToCheck,this.logicalNetLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND_HIGH);
		
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
			throws MapConnectionException, MapDataException
	{
		//Рисуем текущее изображение
		TopologicalRequest currImageRequest =	this.createRequestForExpressArea(
				this.logicalNetLayer.getScale(),
				this.logicalNetLayer.getCenter(),
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
					this.logicalNetLayer.getScale() / Math.pow(LogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.logicalNetLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

			this.cacheOfImages.add(smallScaledImage);
			
			//Кладём в очередь на загрузку
			this.loadingThread.addRequest(smallScaledImage);
			
			//Большое
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.logicalNetLayer.getScale() * Math.pow(LogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.logicalNetLayer.getCenter(),
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
