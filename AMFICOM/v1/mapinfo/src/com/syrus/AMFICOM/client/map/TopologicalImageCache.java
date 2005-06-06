/*
 * $Id: TopologicalImageCache.java,v 1.1.2.6 2005/06/06 12:19:38 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.Client.Map;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.1.2.6 $, $Date: 2005/06/06 12:19:38 $
 * @module mapinfo_v1
 */
public class TopologicalImageCache
{
	/**
	 * Величина габарита активной области в процентах от габарита окна карты
	 */
	private static final double ACTIVE_AREA_SIZE = 0.25;

	/**
	 * Величина габарита области границы (при входе в неё происходит смещение экрана)
	 * в процентах от габарита окна карты
	 */
	private static final double BORDER_AREA_SIZE = 0.1;
	
	/**
	 * Пороговая длина радиус-вектора от точки входа в активную область. Если
	 * длина радиус-вектора меньше пороговой, вычисление самой приоритетной
	 * области для загрузки не производится.
	 */
	private static final int RV_THRESHOLD_LENGTH = 100;
	
	/**
	 * Предельное значение ошибки при вычислении топологического центра сегмента.
	 * Запросы одного масштаба с расстоянием между центрами меньше заданного
	 * считаются идентичными. 
	 */
	private static final int CENTER_COMPUTING_ERROR = 10;

	/**
	 * Для кэша по масштабу: Число изображений с большим и меньшим в
	 * MapInfologicalNetLayer.ZOOM_FACTOR масштабом, которое будет подгружено в
	 * кэш масштаба. Приоритет выполнения кэширования этих изображений самый
	 * низкий. Для SCALE_CACHE_SIZE = 1 будет подгружено одно изображение с
	 * большим и одно с меньшим масштабом. Для кэша по изменению центра
	 * (дискретному): Число изображений, смещённых на шаг дискретизации
	 */
	private static final int CACHE_SIZE = 2;

	/**
	 * "Рекомендуемое" количество сегментов в кэше
	 */
	private static final int CACHE_ELEMENTS_COUNT = 50;

	/**
	 * Предельное число "лишних" сегментов в кэше. Если число сегментов в кэше
	 * превысит сумму CACHE_ELEMENTS_COUNT и MAX_EXCEEDING_COUNT, состоится
	 * очистка кэша от MAX_EXCEEDING_COUNT элементов, которые были использованы
	 * наиболе давно.
	 */
	private static final int MAX_EXCEEDING_COUNT = 20;

	/**
	 * Работа кэша в режиме изменения центра карты
	 */
	private static final int MODE_CENTER_CHANGING = 1;

	/**
	 * Работа кэша в режиме изменения масштаба
	 */
	private static final int MODE_SCALE_CHANGING = 2;

	private LogicalNetLayer logicalNetLayer = null;

	/**
	 * Список уже подгруженных для текущего режима работы сегментов
	 */
	private List cacheOfImages = Collections.synchronizedList(new ArrayList());

	/**
	 * Ссылка на экземпляр потока, осуществляющего подгрузку данных по запросам
	 * к серверу из очереди
	 */
	private LoadingThread loadingThread = null;
	
	private boolean toBreak = false;

	/**
	 * Режим работы кэша
	 */
	private int mode = TopologicalImageCache.MODE_CENTER_CHANGING;

	// Текущие параметры окна карты
	private DoublePoint center = null;

	private double scale = 1.f;

	private Dimension imageSize = null;

	/**
	 * Текущее изображение
	 */
	private TopologicalRequest requestToPaint = null;

	/**
	 * Последний сегмент неактивной области в котором мы присутвовали.
	 */
	private Dimension nonActiveZoneLastSector = new Dimension();

	/**
	 * Последняя активная область (относительно текущего экрана), с которой мы
	 * работали
	 */
	private Dimension lastActiveArea = new Dimension();

	/**
	 * Точка входа в последнюю активную область
	 */
	private Point activeAreaEntryPoint = new Point();

	/**
	 * Массив углов, образуемый положительно направленной горизонтальной прямой
	 * и радиус-вектором из центра изображения в граничные точки.
	 * Точек 8 - по 2 на каждую сторону. Углы упорядочены по возрастанию. 
	 */
	private double[] edgePointsAngles = new double[8];
	
	/**
	 * Расстояние между центрами двух соседних по горизонтали сегментов
	 * в географических координатах 
	 */
	private double xDifferenceSph = 0.D;

	/**
	 * Расстояние между центрами двух соседних по вертикали сегментов
	 * в географических координатах 
	 */
	private double yDifferenceSph = 0.D;
	
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

	public void sizeChanged() throws MapConnectionException, MapDataException
	{
		Dimension newSize = this.logicalNetLayer.getMapViewer()
				.getVisualComponent().getSize();
		if ((newSize.width <= 0) || (newSize.height <= 0))
			return;

		this.imageSize = newSize;
		
		//Разница между центрами соседних по горизонтали сегментов в пикселах 
		int xDifferenceScr = (int)Math.round(this.imageSize.width * (1.D - TopologicalImageCache.ACTIVE_AREA_SIZE));
		//Разница между центрами соседних по вертикали сегментов в пикселах		
		int yDifferenceScr = (int)Math.round(this.imageSize.height * (1.D - TopologicalImageCache.ACTIVE_AREA_SIZE));		
		
		//Координаты текущего центра
		Point curCenterScr = new Point(this.imageSize.width / 2,this.imageSize.height / 2);
		DoublePoint curCenterSph = this.logicalNetLayer.convertScreenToMap(curCenterScr);
		
		//Считаем координаты центра следующего по горизонтали сегмента 
		Point nextHorizCenterScr = new Point(this.imageSize.width / 2 + xDifferenceScr,this.imageSize.height / 2);		
		DoublePoint nextHorizCenterSph = this.logicalNetLayer.convertScreenToMap(nextHorizCenterScr);
		//Считаем расстояние между центрами
		this.xDifferenceSph = nextHorizCenterSph.getX() - curCenterSph.getX();

		//Считаем координаты центра следующего по горизонтали сегмента 
		Point nextVertCenterScr = new Point(this.imageSize.width / 2,this.imageSize.height / 2 + yDifferenceScr);		
		DoublePoint nextVertCenterSph = this.logicalNetLayer.convertScreenToMap(nextVertCenterScr);
		//Считаем расстояние между центрами
		this.yDifferenceSph = nextVertCenterSph.getY() - curCenterSph.getY();
		
		
		//Устанавливаем углы для граничных точек - с северо-востока
		this.edgePointsAngles[0] = this.getAngleForPoint(this.imageSize.width,
				this.imageSize.height * TopologicalImageCache.ACTIVE_AREA_SIZE,
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[1] = this.getAngleForPoint(this.imageSize.width
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE), 0,
				this.imageSize.width / 2, this.imageSize.height / 2);

		//Северо-запад
		this.edgePointsAngles[2] = this.getAngleForPoint(this.imageSize.width
				* TopologicalImageCache.ACTIVE_AREA_SIZE, 0, this.imageSize.width / 2,
				this.imageSize.height / 2);

		this.edgePointsAngles[3] = this.getAngleForPoint(0, this.imageSize.height
				* TopologicalImageCache.ACTIVE_AREA_SIZE, this.imageSize.width / 2,
				this.imageSize.height / 2);

		//Юго-запад    
		this.edgePointsAngles[4] = this.getAngleForPoint(0, this.imageSize.height
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE),
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[5] = this.getAngleForPoint(this.imageSize.width
				* TopologicalImageCache.ACTIVE_AREA_SIZE, this.imageSize.height,
				this.imageSize.width / 2, this.imageSize.height / 2);

		//Юго-восток
		this.edgePointsAngles[6] = this.getAngleForPoint(this.imageSize.width
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE), this.imageSize.height,
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[7] = this.getAngleForPoint(this.imageSize.width,
				this.imageSize.height * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE),
				this.imageSize.width / 2, this.imageSize.height / 2);
	}

	/**
	 * Центр в сферических координатах
	 */
	public void setCenter(DoublePoint newCenter) throws MapConnectionException, MapDataException
	{
		if (this.imageSize == null)
		{
			this.center = newCenter;			
			return;
		}
		
		if ((this.center == null) || (!this.center.equals(newCenter)))
		{
			// Если до этого занимались изменением масштаба - очищаем очередь
			// подгружающего потока, меняем режим работы, очищаем кэш

			if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
				nulifyCache();
				
				//Устанавливаем значения сектора неактивной области и послденей активной области на нули 
				this.lastActiveArea.setSize(0,0);
				this.nonActiveZoneLastSector.setSize(0,0);				
			}

			this.center = newCenter;			
			this.createMovingRequests();
		}
	}

	/**
	 * Масштаб
	 */
	public void setScale(double newScale) throws MapConnectionException, MapDataException
	{
		if (this.imageSize == null)
		{
			this.scale = newScale;			
			return;
		}

		if (this.scale != newScale)
		{
			// Если до этого занимались изменением центра - очищаем очередь
			// подгружающего потока, меняем режим работы, очищаем кэш
			if (this.mode == TopologicalImageCache.MODE_CENTER_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_SCALE_CHANGING;
				nulifyCache();
			}

			// Здесь должно быть известно предыдущее значение масштаба,
			// чтобы отследить динамику
			this.createScaleRequests();

			this.scale = newScale;
		}
	}

	private void nulifyCache()
	{
		this.requestToPaint = null;
		this.cacheOfImages.clear();
		this.loadingThread.clearQueue();
	}
	
	public void analyzeMouseLocation(MouseEvent event)
			throws MapDataException, MapConnectionException
	{
		if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			//В том случае, если мы до этого переключали кэш, меняем режим.
			//Если мы этого не сделаем, то из-за изменения режима кэш обнулится
			//при команде на изменение центра.
		{
			this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
			nulifyCache();
		}
		
		int mouseX = event.getPoint().x;
		int mouseY = event.getPoint().y;		
		// Определение сектора
		// Расчитываем длину радиус-вектора, если меньше пороговой - выходим
		double rvLength = Math.pow(Math
				.pow(mouseX - this.imageSize.width / 2, 2)
				+ Math.pow(mouseY - this.imageSize.height / 2, 2), 0.5);
		if (rvLength < TopologicalImageCache.RV_THRESHOLD_LENGTH)
			return;
		
		// Определяем угол относительно вектора "восток" с началом в центре экрана
		double currentAngle = this.getAngleForPoint(mouseX, mouseY,
				this.imageSize.width / 2, this.imageSize.height / 2);
		
		//Определяем направление подгрузки
		Dimension direction = this.getDirectionForAngle(currentAngle);
		
//		//Если мы находимся на границе экрана -
//		//делаем centerChanged для LogicalNetLeyer'а и выходим.
//		if (	(mouseX < this.imageSize.width * TopologicalImageCache.BORDER_AREA_SIZE)
//				||(mouseX > this.imageSize.width * (1 - TopologicalImageCache.BORDER_AREA_SIZE))
//				||(mouseY < this.imageSize.height * TopologicalImageCache.BORDER_AREA_SIZE)
//				||(mouseY > this.imageSize.height * (1 - TopologicalImageCache.BORDER_AREA_SIZE)))
//		{
//			//Выставляем курсор-стрелку
//			this.logicalNetLayer.setCursor(this.getCursorForDirection(direction));
//			
//			if (SwingUtilities.isRightMouseButton(event))
//			{
//				//Перемещаем центр
//				//Географические координаты нового центра
//				DoublePoint newCenter = new DoublePoint(
//						this.center.getX() + direction.getWidth() * this.xDifferenceSph,
//						this.center.getY() + direction.getHeight() * this.yDifferenceSph);
//				
//				//Географические координаты текущего положения мыши
//				DoublePoint mousePositionSph = this.logicalNetLayer.convertScreenToMap(new Point(mouseX,mouseY));
//				
//				this.logicalNetLayer.setCenter(newCenter);
//				
//				//Курсор ставим в ту же (в топографических координатах) точку - центр уже сменен
//				Point newMousePosition = this.logicalNetLayer.convertMapToScreen(mousePositionSph);
//				
//				Point frameLocation = 
//					this.logicalNetLayer.getMapViewer().getVisualComponent().getLocationOnScreen();
//				
//				this.robot.mouseMove(
//						frameLocation.x + newMousePosition.x,
//						frameLocation.y + newMousePosition.y);
//	
//				return;
//			}
//		}
//		else
//			//Если мы не на границе окна - курсор обычный, не направленный
//			this.logicalNetLayer.setCursor(this.getCursorForDirection(new Dimension(0,0)));
//		
		
		
		
		
//		
//		// Вычисляем в какой мы сейчас области, если dX или dY
//		// отличны от нуля - то в активной
//		int dX = 0;
//		if (mouseX < this.imageSize.width * TopologicalImageCache.ACTIVE_AREA_SIZE)
//			dX = -1;
//		if (mouseX > this.imageSize.width * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))
//			dX = 1;
//
//		int dY = 0;
//		if (mouseY < this.imageSize.height * TopologicalImageCache.ACTIVE_AREA_SIZE)
//			dY = -1;
//		if (mouseY > this.imageSize.height * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))
//			dY = 1;
//
//		// Фиксируем активную область
//		this.lastActiveArea.setSize(dX, dY);

//		if ((dX == 0) && (dY == 0))
//		{
			// Мы находимся в неактивной части экрана
			
			
			//Если мы уже производили расчёт для этого сектора неактивной области - выходим
			if (	(this.nonActiveZoneLastSector.width == direction.width)
					&&(this.nonActiveZoneLastSector.height == direction.height))
				return;
			
			//Фиксируем новое значение сектора
			this.nonActiveZoneLastSector = direction;
			
			//Устанавливаем всем запросам самые низкие приоритеты
			this.loadingThread.setTheLowestPriorityForAll();

			if (this.nonActiveZoneLastSector.width == 0)
				this.createVerticalRequests(this.nonActiveZoneLastSector.height);
			else if (this.nonActiveZoneLastSector.height == 0)
				this.createHorizontalRequests(this.nonActiveZoneLastSector.width);
			else
				this.createDiagonalRequests(this.nonActiveZoneLastSector);
//		}
//		else
//		{
			// Сохраняем точку входа в активную область
//			if ((this.lastActiveArea.width != dX) || (this.lastActiveArea.height != dY))
//			{
//				this.activeAreaEntryPoint.x = mouseX;
//				this.activeAreaEntryPoint.y = mouseY;
//			}
	
//			// Расчитываем длину радиус-вектора, если меньше пороговой - выходим
//			double rvLength = Math.pow(Math
//					.pow(mouseX - this.activeAreaEntryPoint.x, 2)
//					+ Math.pow(mouseY - this.activeAreaEntryPoint.y, 2), 0.5);
//			if (rvLength < TopologicalImageCache.RV_THRESHOLD_LENGTH)
//				return;
//	
//			// Вычисляем центр сегмента, которому относится активная область
//			// Его экранные координаты
//			Point newCenterScr = new Point((int) Math.round(this.imageSize.width
//					* (0.5 + dX * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))),
//					(int) Math.round(this.imageSize.height
//							* (0.5 + dY * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))));
//	
//			DoublePoint newCenterSph = this.logicalNetLayer
//					.convertScreenToMap(newCenterScr);
	
			// Вычисляем местоположение углов сегмента
	
			// Вычисляем в каком сегменте круга лежит наш радиус вектор -
			// ставим в очередь PRIORITY_HIGH-запрос на соответсвующий запрос
			// (????????????и PRIORITY_MEDIUM на соседние????????)
//		}
			
			// Удаляем неподгруженные очёты вышедшие из кэш области
			this.clearFarAndUnloadedSegments();

			// Если в кэше слишком много сегментов удаляем самые старые
			if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT
					+ TopologicalImageCache.MAX_EXCEEDING_COUNT)
				this.clearOldSegments();
	}
	
	private Dimension getDirectionForAngle(double angle)
	{
		Dimension direction = new Dimension();
		
		if ((this.edgePointsAngles[0] < angle) && (angle <= this.edgePointsAngles[1]))
		{
			//Северо-восток
			direction.width = 1;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[1] < angle) && (angle <= this.edgePointsAngles[2]))
		{
			//Север
			direction.width = 0;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[2] < angle) && (angle <= this.edgePointsAngles[3]))
		{
			//Северо-запад
			direction.width = -1;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[3] < angle) && (angle <= this.edgePointsAngles[4]))
		{
			//Запад
			direction.width = -1;
			direction.height = 0;			
		}
		else if ((this.edgePointsAngles[4] < angle) && (angle <= this.edgePointsAngles[5]))
		{
			//Юго-запад
			direction.width = -1;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[5] < angle) && (angle <= this.edgePointsAngles[6]))
		{
			//Юг
			direction.width = 0;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[6] < angle) && (angle <= this.edgePointsAngles[7]))
		{
			//Юго-восток
			direction.width = 1;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[7] < angle) || (angle <= this.edgePointsAngles[0]))
		{
			//Восток
			direction.width = 1;
			direction.height = 0;			
		}
		
		return direction;
	}
	
	private double getAngleForPoint(
			double pointX,
			double pointY,
			double coordNullX,
			double coordNullY)
	{
		double angle = Math.toDegrees(Math.atan(Math.abs(coordNullY - pointY)
				/ Math.abs(coordNullX - pointX)));

		if ((pointX - coordNullY < 0) && (pointY - coordNullY > 0))//Если третий квадрант
			angle = 180 + angle;
		else if (pointX - coordNullY < 0) //Если второй квадрант
			angle = 180 - angle;
		else if (pointY - coordNullY > 0) //Если четвёртый квадрант
			angle = 360 - angle;

		return angle;
	}
	
	/**
	 * Ищет в кэше запрос с заданными параметрами центра и масштаба
	 * и меняет ему приоритет на заданный.
	 * Если запроса с такими параметрами не найдено - создаётся новый запрос и ставится
	 * в очередь на подгрузку. 
	 * @param topoCenter
	 * @param topoScale
	 * @return Отчёт, над которым производилась операция
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	private TopologicalRequest setPriorityForRequest(
			DoublePoint topoCenter,
			double topoScale,
			int priority) throws MapConnectionException, MapDataException
	{
		
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest request = (TopologicalRequest) it.next();
			if (	TopologicalImageCache.compare(request.getTopoScale(),topoScale)
					&&(this.screenDistance(request.getTopoCenter(),topoCenter) < TopologicalImageCache.CENTER_COMPUTING_ERROR))
			{
				if (request.getPriority() > TopologicalRequest.PRIORITY_ALREADY_LOADED)
					this.loadingThread.changeRequestPriority(request,priority);
				
				return request;
			}
		}
		
		TopologicalRequest request = this.createRequestForExpressArea(
				topoScale,
				topoCenter,
				priority);
		
		this.cacheOfImages.add(request);
		this.loadingThread.addRequest(request);
		
		return request;
	}

	/**
	 * Используется для построения отчётов на "север", "юг".
	 * Фактически строит "пирамиду" из сегментов с вершиной в сегменте, прилегающем
	 * к отображаемому. В вершине приоритет BACKGROUND_HIGH, в остальных BACKGROUND_MIDDLE  
	 * @param direction Направление
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	private void createVerticalRequests (int direction) throws MapConnectionException, MapDataException
	{
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
			for (int j = -i; j <= i; j++)			
			{
				DoublePoint topoCenter = new DoublePoint(
						this.center.getX() + j * this.xDifferenceSph,
						this.center.getY() + (i + 1) * direction * this.yDifferenceSph);
				
				int priority = TopologicalRequest.PRIORITY_BACKGROUND_LOW;
				if (i == 0)
					priority = TopologicalRequest.PRIORITY_BACKGROUND_HIGH;
				else if (j == 0)
					priority = TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE;
				
				this.setPriorityForRequest(topoCenter,this.scale,priority);
			}
	}
	
	/**
	 * Используется для построения отчётов на "восток", "запад".
	 * см. createVerticalRequests
	 * @param direction Направление
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	private void createHorizontalRequests (int direction) throws MapConnectionException, MapDataException
	{
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
			for (int j = -i; j <= i; j++)			
			{
				DoublePoint topoCenter = new DoublePoint(
						this.center.getX() + (i + 1) * direction * this.xDifferenceSph,
						this.center.getY() + j * this.yDifferenceSph);
				
				int priority = TopologicalRequest.PRIORITY_BACKGROUND_LOW;
				if (i == 0)
					priority = TopologicalRequest.PRIORITY_BACKGROUND_HIGH;
				else if (j == 0)
					priority = TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE;
				
				this.setPriorityForRequest(topoCenter,this.scale,priority);
			}
	}
	
	/**
	 * Используется для построения отчётов на "восток", "запад".
	 * Фактически сводится к постоению "квадрата" сегментов по заданному направлению. 
	 * @param direction Направление
	 * @throws MapDataException 
	 * @throws MapConnectionException 
	 */
	private void createDiagonalRequests (Dimension direction) throws MapConnectionException, MapDataException
	{
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
			for (int j = 0; j < TopologicalImageCache.CACHE_SIZE; j++)			
			{
				DoublePoint topoCenter = new DoublePoint(
						this.center.getX() + (i + 1) * direction.width * this.xDifferenceSph,
						this.center.getY() + (j + 1) * direction.height * this.yDifferenceSph);
				
				int priority = TopologicalRequest.PRIORITY_BACKGROUND_LOW;
				if (i == j)
				{
					if (i == 0)
						priority = TopologicalRequest.PRIORITY_BACKGROUND_HIGH;
					else
						priority = TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE;						
				}
				
				this.setPriorityForRequest(topoCenter,this.scale,priority);
			}
	}

	public void refreshLayers() throws MapConnectionException, MapDataException
	{
		// TODO Здесь по-хорошему надо перерисовывать отдельные слои,
		// но такого механизма пока нет.
		this.requestToPaint = null;
		this.cacheOfImages.clear();
		this.loadingThread.clearQueue();

		this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
		this.createMovingRequests();
	}

	public Image getImage()
	{
		if (this.requestToPaint == null)
			return null;

		while (this.requestToPaint.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
		{
			// Изображение по запросу ещё не подгружено
			
			if (this.toBreak)
			{
				//Остановливаем работу потока подгрузки
				this.loadingThread.cancel();
				return null;
			}
			
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Logger.log(" TIC - getImage - returning image");

		if (this.requestToPaint.getImage() == null)
			return null;
		
		Image imageToReturn = this.requestToPaint.getImage().getImage();

		this.requestToPaint = null;

		return imageToReturn;
	}

	/**
	 * Если карта использует кэш, этот метод выызвается на выходе из модуля
	 *
	 */
	public void cancel()
	{
		this.toBreak = true;
	}
	
	/**
	 * Используется при задании нового центра для карты.
	 * Выдаёт ближайший к указанной точке центр для дискретного режима перемещения 
	 * @param point 
	 * @return Ближайший центр
	 */
	public DoublePoint getNearestCenter(DoublePoint point)
	{
		//В случае, если ещё не заданы значения центра и смещений
		if ((this.center == null) || (this.imageSize == null))
			return point;
		
		//Считаем расстояния по абсциссе и ординате между центром и точкой
		double dX = point.getX() - this.center.getX();
		double dY = point.getY() - this.center.getY();		
		
		//Считаем сколько смещений по абсциссе и ординате уложится на эти расстояния
		int xShiftCount = (int) Math.round(dX / this.xDifferenceSph);
		int yShiftCount = (int) Math.round(dY / this.yDifferenceSph);		
		
		if ((Math.abs(xShiftCount) > 0) || (Math.abs(yShiftCount) > 0))
		{
			return new DoublePoint(
					this.center.getX() + xShiftCount * this.xDifferenceSph,
					this.center.getY() + yShiftCount * this.yDifferenceSph);
		}
		
		return this.center;
	}
	/**
	 * Считает экранное расстояние между двумя точками в сферических координатах
	 * 
	 * @param sphP1
	 * @param sphP2
	 * @return Расстояние
	 */
	double screenDistance(DoublePoint sphP1, DoublePoint sphP2)
			throws MapConnectionException, MapDataException
	{
		Point p1 = this.logicalNetLayer.convertMapToScreen(sphP1);
		Point p2 = this.logicalNetLayer.convertMapToScreen(sphP2);

		double returnValue = Math.pow((Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y
				- p1.y, 2)), 0.5);
		return returnValue;
	}

	/**
	 * Проверяет наличие и при необходимости создаёт сегмент
	 * для отображения
	 */
	private void createMovingRequests()
			throws MapConnectionException, MapDataException
	{
		this.requestToPaint = this.setPriorityForRequest(this.center,this.scale,TopologicalRequest.PRIORITY_EXPRESS);
	}

	/**
	 * Удаляет сегменты, которые а)были помещены в кэш загрузку, но не успели
	 * подгрузиться, б)центр которых отступает от текущего центра более, чем на
	 * TopologicalImageCache.SCALE_SIZE * MapFrame.MOVE_CENTER_STEP_SIZE *
	 * _VisualComponent_.getSize();
	 * 
	 */
	private void clearFarAndUnloadedSegments() throws MapConnectionException,
			MapDataException
	{
		double dX = TopologicalImageCache.CACHE_SIZE * 1.01 * this.xDifferenceSph;
		double dY = TopologicalImageCache.CACHE_SIZE * 1.01 * this.yDifferenceSph;		
		DoublePoint upperLeftSph = new DoublePoint(this.center.getX() - dX,this.center.getY() - dY);
		DoublePoint downRightSph = new DoublePoint(this.center.getX() + dX,this.center.getY() + dY);		

		double x = (upperLeftSph.getX() < downRightSph.getX()) ? upperLeftSph
				.getX() : downRightSph.getX();
		double y = (upperLeftSph.getY() < downRightSph.getY()) ? upperLeftSph
				.getY() : downRightSph.getY();

		Rectangle2D.Double currCacheBorders = new Rectangle2D.Double(x, y, Math
				.abs(downRightSph.getX() - upperLeftSph.getX()), Math.abs(downRightSph
				.getY()
				- upperLeftSph.getY()));

		// Ищем, есть ли уже сегмент с таким центром
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest) it.next();
			if ((!currCacheBorders.contains(curRequest.getTopoCenter().getX(),
					curRequest.getTopoCenter().getY()))
					&& (curRequest.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED))
			{
				// Удаляем сегмент - не имеет смысла его подгружает
				Logger.log(" TIC - clearFarAndUnloadedSegments - removing request."
						+ curRequest);
				this.loadingThread.removeRequest(curRequest);
				it.remove();
			}
		}
	}

	/**
	 * Чистит список подгруженных изображений
	 * 
	 */
	private void clearOldSegments()
	{
		Logger.log(" TIC - clearOldSegments - just entered.");

		// Сортируем отчёты (по убыванию, чтобы более новые потом и искать
		// недолго было)
		Collections.sort(this.cacheOfImages);

		Logger.log(" TIC - clearOldSegments - Collection sorted.");

		// Удаляем последние MAX_EXCEEDING_COUNT элементов
		for (ListIterator lIt = this.cacheOfImages
				.listIterator(TopologicalImageCache.CACHE_ELEMENTS_COUNT
						- TopologicalImageCache.MAX_EXCEEDING_COUNT); lIt.hasNext();)
		{
			lIt.next();
			lIt.remove();
		}

		Logger.log(" TIC - clearOldSegments - Old elements removed. Exiting.");
	}

	// ////////////////////////////////////Функции для кэша по масштабу

	/**
	 * Подгружает изображения с большим и меньшим масштабами для текущего
	 * масштаба
	 */
	private void createScaleRequests() throws MapConnectionException,
			MapDataException
	{
		if ((this.cacheOfImages.size() == 0)
				|| (	(!TopologicalImageCache.compare(this.scale, this.logicalNetLayer.getScale()	* LogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR, this.logicalNetLayer.getScale()))))
		{
			// Если новый масштаб не является кратным предыдущему (zoom_to_box)
			// или мы только что вышли из режима изменения центра
			// грузим все изображения заново.
			renewScaleImages();
			return;
		}

		//Устанавливаем отображаемое изображение. Если его нет - ему самый высокий приоритет
		this.requestToPaint = this.setPriorityForRequest(this.center,this.logicalNetLayer.getScale(),TopologicalRequest.PRIORITY_EXPRESS);

		// Массштаб, который возможно понадобится в направлении измененеия массштаба
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale, this.logicalNetLayer
				.getScale()
				* LogicalNetLayer.ZOOM_FACTOR))
		{
			// Новый масштаб в ZOOM_FACTOR раз меньше предыдущего
			scaleToCheck = this.logicalNetLayer.getScale()
					/ Math.pow(LogicalNetLayer.ZOOM_FACTOR,
							TopologicalImageCache.CACHE_SIZE);
		} else if (TopologicalImageCache.compare(this.scale
				* LogicalNetLayer.ZOOM_FACTOR, this.logicalNetLayer.getScale()))
		{
			// Новый масштаб в ZOOM_FACTOR раз больше предыдущего
			scaleToCheck = this.logicalNetLayer.getScale()
					* Math.pow(LogicalNetLayer.ZOOM_FACTOR,
							TopologicalImageCache.CACHE_SIZE);
		}

		// Если изображения с таким масштабом нет - грузим с приритетом PRIORITY_BACKGROUND_HIGH
		this.setPriorityForRequest(this.center,scaleToCheck,TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

		// Если в кэше слишком много сегментов удаляем самые старые
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT
				+ TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
	}

	private static boolean compare(double p1, double p2)
	{
		if (Math.round(p1 * 1000000D) == Math.round(p2 * 1000000D))
			return true;

		return false;
	}

	/**
	 * Заново подгружает ВСЕ изображения с большим и меньшим масштабами
	 */
	private void renewScaleImages() throws MapConnectionException,
			MapDataException
	{
		//Устанавливаем отображаемое изображение. Грузим его с самым высоким приоритетом
		this.requestToPaint = this.setPriorityForRequest(this.center,this.logicalNetLayer.getScale(),TopologicalRequest.PRIORITY_EXPRESS);

		// Делаем изображения большего и меньшего изображения
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
		{
			// Маленькое
			this.setPriorityForRequest(
					this.center,
					this.logicalNetLayer.getScale()	/ Math.pow(LogicalNetLayer.ZOOM_FACTOR, i + 1),
					TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE);

			// Большое
			this.setPriorityForRequest(
					this.center,
					this.logicalNetLayer.getScale()	* Math.pow(LogicalNetLayer.ZOOM_FACTOR, i + 1),
					TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE);
		}
	}

	private TopologicalRequest createRequestForExpressArea(
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
	
	public Dimension getImageSize()
	{
		return this.imageSize;
	}
}
