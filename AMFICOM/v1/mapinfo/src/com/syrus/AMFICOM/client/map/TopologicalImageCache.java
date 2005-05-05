/*
 * $Id: TopologicalImageCache.java,v 1.1.2.1 2005/05/05 10:22:38 krupenn Exp $
 *
 * Copyright � 2004 Syrus Systems.
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
	 * ��� ���� �� ��������:
	 * ����� ����������� � ������� � ������� � MapInfologicalNetLayer.ZOOM_FACTOR
	 * ���������, ������� ����� ���������� � ��� ��������. ��������� ���������� 
	 * ����������� ���� ����������� ����� ������. ��� SCALE_CACHE_SIZE = 1 �����
	 * ���������� ���� ����������� � ������� � ���� � ������� ���������.
	 * ��� ���� �� ��������� ������ (�����������):
	 * ����� �����������, ��������� �� ��� �������������
	 */
	private static final int CACHE_SIZE = 2;
	
	/**
	 * "�������������" ���������� ��������� � ����
	 */
	private static final int CACHE_ELEMENTS_COUNT = 70;
	
	/**
	 * ���������� ����� "������" ��������� � ����. ���� ����� ���������
	 * � ���� �������� ����� CACHE_ELEMENTS_COUNT � MAX_EXCEEDING_COUNT,
	 * ��������� ������� ���� �� MAX_EXCEEDING_COUNT ���������, ������� ����
	 * ������������ ������� �����.
	 */
	private static final int MAX_EXCEEDING_COUNT = 30;	

	/**
	 * ������ ���� � ������ ��������� ������ �����
	 */
	private static final int MODE_CENTER_CHANGING = 1;	

	/**
	 * ������ ���� � ������ ��������� ��������
	 */
	private static final int MODE_SCALE_CHANGING = 2;	
	
	/**
	 * ������ ��� ������������ ��� �������� ��������� ���������,
	 * ������� ����� ���� ������������ ��� ��������� ������
	 */
	private List cacheOfImages = Collections.synchronizedList(new ArrayList());

	/**
	 * ������ ���������, ������� ������ ���� ���������� �� ������� �����������
	 */
	private List imagesToPaint = Collections.synchronizedList(new ArrayList());
	
	/**
	 * ������������ �����������. �� �� �������������� ��� ������������
	 * ��������.
	 */
	private Image visibleImage = null;	
	
	/**
	 * ������ �� ��������� ������, ��������������� ��������� ������ �� ��������
	 * � ������� �� ������� 
	 */
	private LoadingThread loadingThread = null;
	
	private LogicalNetLayer logicalNetLayer = null;
	
	/**
	 * ����� ������ ����
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
	 * ����� � ����������� �����������
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
			//���� �� ����� ���������� ���������� �������� - ������� �������
			//������������� ������, ������ ����� ������, ������� ���
			if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
				this.imagesToPaint.clear();				
				this.cacheOfImages.clear();
				this.loadingThread.clearQueue();
			}
			else
			{
				//���������� �������� � �������� ����������� ������������ ����������� ������
				this.discreteShifts = this.getDiscreteShifts();
			}
			
			this.createMovingRequests();
			this.center = newCenter;			
		}
	}

	/**
	 * �������
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
			//���� �� ����� ���������� ���������� ������ - ������� �������
			//������������� ������, ������ ����� ������, ������� ���
			if (this.mode == TopologicalImageCache.MODE_CENTER_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_SCALE_CHANGING;
				this.imagesToPaint.clear();				
				this.cacheOfImages.clear();
				this.loadingThread.clearQueue();				
			}

			//����� ������ ���� �������� ���������� �������� ��������,
			//����� ��������� ��������
			this.createScaleRequests();

			this.scale = newScale;			
		}
	}
	
	public void refreshLayers()
			throws MapConnectionException, MapDataException
	{
		//TODO ����� ��-�������� ���� �������������� ��������� ����,
		//�� ������ ��������� ���� ���.
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
	 * ������ � ����� ������� � ������� ��� ����������� ��� ����������� ������
	 */
	private void createMovingRequests()
			throws MapConnectionException, MapDataException
	{
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
		//������ ����������� �����������, ���������� ���������� �����������
		for (int i = 0; i <= TopologicalImageCache.CACHE_SIZE; i++)
			this.createMovingRequests(i);
			
		//������� �������������� ����� �������� �� ��� �������
		this.clearFarAndUnloadedSegments();
		
		//���� � ���� ������� ����� ��������� ������� ����� ������				
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
				TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
		
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
	/**
	 * ������� �������� ���������� ����� ����� ������� � ����������� �����������
	 * @param sphP1 
	 * @param sphP2
	 * @return ����������
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
	 * ��������� ������� � ��� ������������� ������ �������� �� �������� �����������
	 * @param neighbourhood �����������
	 */
	private void createMovingRequests(int neighbourhood)
			throws MapConnectionException, MapDataException
	{
		//������ ����������� �����������
		for (int i = (-1) * neighbourhood; i <= neighbourhood; i++)
			for (int j = (-1) * neighbourhood; j <= neighbourhood; j++)			
			{
				if (	(Math.abs(i) != neighbourhood)
						&& (Math.abs(j) != neighbourhood))
					//�� �������� ������ � �������� �����������
					continue;
				
				DoublePoint imageCenter =	this.logicalNetLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + j * (int)Math.round(this.imageSize.width * LogicalNetLayer.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + i * (int)Math.round(this.imageSize.height * LogicalNetLayer.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//����, ���� �� ��� ������� � ����� �������
				for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
				{
					TopologicalRequest curRequest = (TopologicalRequest)it.next();
					if (convertMapToScreen(curRequest.getTopoCenter(),imageCenter) < 10)					
					{
						requestForCenter = curRequest;
						break;
					}
				}
				
				//���������, ������� ������ ���� � ������� ������� � ������� ������
				int requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_LOW;
				if ((i == 0) && (j == 0))
					requestCurPriority = TopologicalRequest.PRIORITY_EXPRESS;
				else
				{
					if (	((this.discreteShifts.width * j > 0) || (this.discreteShifts.width == j))
							&&((this.discreteShifts.height * i > 0) || (this.discreteShifts.height == i)))
						//���� ������� ������-������ ����������� � ������������ ��������
						if (neighbourhood == 1)
							//��������� �� ���� �� ����������� �������
							requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_HIGH;
						else
							//� ��� �� ���������
							requestCurPriority = TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE;							
				}				
				
				if (requestForCenter != null)
				{
					if (	(requestForCenter.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
							&&(requestForCenter.getPriority() != requestCurPriority))
						//���� ������� ��������� ������� ���� ������������� (� ��������
						//��������� ������� ����������� - ������)
						this.loadingThread.changeRequestPriority(requestForCenter,requestCurPriority);
				}
				else
				{
					//���� ���
					requestForCenter = this.createRequestForExpressArea(
							this.logicalNetLayer.getScale(),
							imageCenter,
							requestCurPriority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//����� � ������� �� ��������
					this.loadingThread.addRequest(requestForCenter);
				}
				
				if (convertMapToScreen(requestForCenter.getTopoCenter(),this.logicalNetLayer.getCenter()) < 10)
					//����� ������� � ������� �� ���������
					this.imagesToPaint.add(requestForCenter);
			}
	}
	
	/**
	 * ������� ��������, �������
	 * �)���� �������� � ��� ��������, ��
	 * �� ������ ������������,
	 * �)����� ������� ��������� �� �������� ������ �����, ��� ��
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
		
		//����, ���� �� ��� ������� � ����� �������
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (	(!currCacheBorders.contains(curRequest.getTopoCenter().getX(),curRequest.getTopoCenter().getY()))
					&&(curRequest.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED))					
			{
				//������� ������� - �� ����� ������ ��� ����������
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
					" TIC - clearFarAndUnloadedSegments - removing request." + curRequest);
				this.loadingThread.removeRequest(curRequest);
				this.imagesToPaint.remove(curRequest);
				it.remove();
			}
		}
	}
	
	/**
	 * ���������� ���������� �������� ������������ ����������� ������
	 * @return ��������
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
	 * ������ ������ ������������ �����������
	 *
	 */
	private void clearOldSegments()
	{
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - just entered.");
		
		//��������� ������ (�� ��������, ����� ����� ����� ����� � ������ ������� ����)
		Collections.sort(this.cacheOfImages);

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Collection sorted.");
		
		//������� ��������� MAX_EXCEEDING_COUNT ���������
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
	
	//////////////////////////////////////������� ��� ���� �� ��������
	
	/**
	 * ���������� ����������� � ������� � ������� ���������� ��� ��������
	 * ��������
	 */
	private void createScaleRequests()
			throws MapConnectionException, MapDataException
	{
		if (	(this.cacheOfImages.size() == 0)
				||(		(!TopologicalImageCache.compare(this.scale,this.logicalNetLayer.getScale() * LogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR,this.logicalNetLayer.getScale()))))
		{
			// ���� ����� ������� �� �������� ������� ����������� (zoom_to_box)
			// ��� �� ������ ��� ����� �� ������ ��������� ������
			// ������ ��� ����������� ������.
			renewScaleImages();
			return;
		}
		
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),this.logicalNetLayer.getScale()))
			{
				//����� ������� � ������� �� ���������
				this.imagesToPaint.add(curRequest);
				break;
			}
		}
		
		//��������, ������� �������� �����������
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale,this.logicalNetLayer.getScale() * LogicalNetLayer.ZOOM_FACTOR))		
		{
			//����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.logicalNetLayer.getScale() / Math.pow(LogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);
		}
		else if (TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR,this.logicalNetLayer.getScale()))
		{
			//����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.logicalNetLayer.getScale() * Math.pow(LogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);			
		}
		
		//���� ����������� � ����� ���������
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),scaleToCheck))
				//���� �����, �� ���� ����������
				return;
		}

		//��� ������ - ������ �����������
		TopologicalRequest newImageRequest =
			this.createRequestForExpressArea(scaleToCheck,this.logicalNetLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND_HIGH);
		
		this.cacheOfImages.add(newImageRequest);
		//����� ������� �� ��������
		this.loadingThread.addRequest(newImageRequest);
		
		//���� � ���� ������� ����� ��������� ������� ����� ������				
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
	 * ������ ���������� ��� ����������� � ������� � ������� ����������
	 */
	private void renewScaleImages()
			throws MapConnectionException, MapDataException
	{
		//������ ������� �����������
		TopologicalRequest currImageRequest =	this.createRequestForExpressArea(
				this.logicalNetLayer.getScale(),
				this.logicalNetLayer.getCenter(),
				TopologicalRequest.PRIORITY_EXPRESS);

		//����� � ���
		this.cacheOfImages.add(currImageRequest);
		//����� � ������� �� ���������
		this.imagesToPaint.add(currImageRequest);		
		//����� � ������� �� ��������		
		this.loadingThread.addRequest(currImageRequest);
		
		//������ ����������� �������� � �������� �����������
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
		{
			//���������
			TopologicalRequest smallScaledImage =	this.createRequestForExpressArea(
					this.logicalNetLayer.getScale() / Math.pow(LogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.logicalNetLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

			this.cacheOfImages.add(smallScaledImage);
			
			//����� � ������� �� ��������
			this.loadingThread.addRequest(smallScaledImage);
			
			//�������
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.logicalNetLayer.getScale() * Math.pow(LogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.logicalNetLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

			this.cacheOfImages.add(bigScaledImage);
			
			//����� � ������� �� ��������
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
