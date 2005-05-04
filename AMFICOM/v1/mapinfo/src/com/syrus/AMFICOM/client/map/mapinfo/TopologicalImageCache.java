/*
 * $Id: TopologicalImageCache.java,v 1.9.2.5 2005/05/04 13:14:09 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
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
	
	private MapInfoLogicalNetLayer miLayer = null;
	
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
	 * ����� � ����������� �����������
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
	{
		if (this.visibleImage == null)
			return;
		
		double newScale = this.miLayer.getScale();  
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
	 * ������ � ����� ������� � ������� ��� ����������� ��� ����������� ������
	 */
	private void createMovingRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
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
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
	/**
	 * ��������� ������� � ��� ������������� ������ �������� �� �������� �����������
	 * @param neighbourhood �����������
	 */
	private void createMovingRequests(int neighbourhood)
	{
		//������ ����������� �����������
		for (int i = (-1) * neighbourhood; i <= neighbourhood; i++)
			for (int j = (-1) * neighbourhood; j <= neighbourhood; j++)			
			{
				if (	(Math.abs(i) != neighbourhood)
						&& (Math.abs(j) != neighbourhood))
					//�� �������� ������ � �������� �����������
					continue;
				
				DoublePoint imageCenter =	this.miLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + j * (int)Math.round(this.imageSize.width * MapFrame.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + i * (int)Math.round(this.imageSize.height * MapFrame.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//����, ���� �� ��� ������� � ����� �������
				for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
				{
					TopologicalRequest curRequest = (TopologicalRequest)it.next();
					if (this.miLayer.convertMapToScreen(curRequest.getTopoCenter(),imageCenter) < 10)					
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
							this.miLayer.getScale(),
							imageCenter,
							requestCurPriority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//����� � ������� �� ��������
					this.loadingThread.addRequest(requestForCenter);
				}
				
				if (this.miLayer.convertMapToScreen(requestForCenter.getTopoCenter(),this.miLayer.getCenter()) < 10)
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
		
		//����, ���� �� ��� ������� � ����� �������
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (	(!currCacheBorders.contains(curRequest.getTopoCenter().getX(),curRequest.getTopoCenter().getY()))
					&&(curRequest.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED))					
			{
				//������� ������� - �� ����� ������ ��� ����������
				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
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
	{
		Point prevScreenCenter = this.miLayer.convertMapToScreen(this.center);
		Point currScreenCenter = this.miLayer.convertMapToScreen(this.miLayer.getCenter());
		
		Dimension result = this.miLayer.getDiscreteShifts(
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
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - just entered.");
		
		//��������� ������ (�� ��������, ����� ����� ����� ����� � ������ ������� ����)
		Collections.sort(this.cacheOfImages);

		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - clearOldSegments - Collection sorted.");
		
		//������� ��������� MAX_EXCEEDING_COUNT ���������
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
	
	//////////////////////////////////////������� ��� ���� �� ��������
	
	/**
	 * ���������� ����������� � ������� � ������� ���������� ��� ��������
	 * ��������
	 */
	private void createScaleRequests()
	{
		if (	(this.cacheOfImages.size() == 0)
				||(		(!TopologicalImageCache.compare(this.scale,this.miLayer.getScale() * MapInfoLogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * MapInfoLogicalNetLayer.ZOOM_FACTOR,this.miLayer.getScale()))))
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
			if (TopologicalImageCache.compare(curRequest.getTopoScale(),this.miLayer.getScale()))
			{
				//����� ������� � ������� �� ���������
				this.imagesToPaint.add(curRequest);
				break;
			}
		}
		
		//��������, ������� �������� �����������
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale,this.miLayer.getScale() * MapInfoLogicalNetLayer.ZOOM_FACTOR))		
		{
			//����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);
		}
		else if (TopologicalImageCache.compare(this.scale * MapInfoLogicalNetLayer.ZOOM_FACTOR,this.miLayer.getScale()))
		{
			//����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.CACHE_SIZE);			
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
			this.createRequestForExpressArea(scaleToCheck,this.miLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND_HIGH);
		
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
	{
		//������ ������� �����������
		TopologicalRequest currImageRequest =	this.createRequestForExpressArea(
				this.miLayer.getScale(),
				this.miLayer.getCenter(),
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
					this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

			this.cacheOfImages.add(smallScaledImage);
			
			//����� � ������� �� ��������
			this.loadingThread.addRequest(smallScaledImage);
			
			//�������
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
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

class LoadingThread extends Thread
{
	private MapInfoLogicalNetLayer miLayer = null;
	private String uriString = null;
	/**
	 * ������� �������� �� ��������� ����������� � �������
	 */
	private List requestQueue =
		Collections.synchronizedList(new LinkedList());	
	
	/**
	 * ������, �������������� � ������� ������
	 */
	private TopologicalRequest requestCurrentlyProcessed = null;
	
	/**
	 * ����, ����� ���������� �����
	 */
	private boolean toBreak = false;
	
	/**
	 * ����, ����� �������� ��������� ����������� � �������
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
			//�������� ������ � ������� ������
			Iterator rqIt = this.requestQueue.iterator();
			if (rqIt.hasNext())
			{
				this.requestCurrentlyProcessed = (TopologicalRequest)rqIt.next();
				rqIt.remove();
			}
			
			if (this.requestCurrentlyProcessed == null)
			{
				//�������� � ������� ���
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
			
			//�������� ������ �� ���������			
			this.renderMapImageAtServer(this.requestCurrentlyProcessed);

			ImageIcon imageForRequest = null;
			while (		(imageForRequest == null)
							&&(!this.toBreakGettingRendition))
			{
				//��� ��������� ����������� ��� ������� �� ����������
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
	 * ��������� ������ � �������, �������� ��� ���� ������� �� ����������
	 * @param requestToAdd ������
	 */
	public void addRequest(TopologicalRequest requestToAdd)
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - addRequest - adding request (" + requestToAdd + ")");
		
		//���� ������ ������ � ����������� ����, ��� � ������ �������
		ListIterator lIt = this.requestQueue.listIterator();
		for (;lIt.hasNext();)
		{
			TopologicalRequest curRequest = 
				(TopologicalRequest) lIt.next();
			if (requestToAdd.getPriority() < curRequest.getPriority())
			{
				//��������� ����� ���� ��������
				lIt.previous();
				lIt.add(requestToAdd);
				return;
			}
		}

		//�� ����� - ��������� � ����� �������
		lIt.add(requestToAdd);
		
		//���� ��������� ������������ ������� ����, ��� � �������, ������������ � ���������
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
			//���� �� ������� ������, ������� ��� � ��������� - �� ��� ��� ��� � �������.
			//������� ������ ������������� ���������.
			this.stopRenderingAtServer();
		else
			//������� ������ �� �������
			this.requestQueue.remove(request);
	}
	
	/**
	 * ������ ��������� �������������� ������� � �������
	 * @param request ������
	 * @param newPriority ����� ���������
	 */
	public void changeRequestPriority(TopologicalRequest request, int newPriority)
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - loadingThread - changeRequestPriority - changing request's (" + request +
			") priority for " + newPriority);

		//������� ������ �� �������
		if (this.requestQueue.remove(request))
		{
			//���� ������ ���������� � �������
			//����� ��� ����� ���������
			request.setPriority(newPriority);
			
			//����� ������ ������ � �������
			this.addRequest(request);
		}
	}

	public void clearQueue()
	{
		this.requestQueue.clear();
	}
	
	/**
	 * �������� ������ �� ������ �� ��������� ����������.
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
	 * �������� ������ �� ��������� ����������� �� �������
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
	 * ���������� ����������� � ������� �� HTTP-�������
	 * @return �����������
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
	 * ������ �� �������� ������� ������ ���������� ��� HTTP ������� � �������
	 * ���������� ��� ���������� ����������� ������
	 * @param request ������
	 * @return ������ ���������� ��� HTTP ������� � �������
	 * ���������� 
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
			
			//��������� ���� ������� �� ����, ����� �� ��� ������ ������, ����� �� �� ��� ������� �������� �� �������
			//� ���� �� ���������� ������� ��� �������� �������
			boolean toShowLayerObjects =	spL.isVisible()
				&& spL.isVisibleAtScale(this.miLayer.getScale());

			//�� �� ����� ��� ��������
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
 * ��������� ������� ����������� � �������
 * @author $Author: peskovsky $
 * @version $Revision: 1.9.2.5 $, $Date: 2005/05/04 13:14:09 $
 * @module mapinfo_v1
 */
class TopologicalRequest implements Comparable
{
	/**
	 * ��� �������� �����������, ����������� � ��� "�� ������ ������",
	 * �� ������� ������������ � ���������������� � ������� ������
	 */
	private static final int PRIORITY_BACKGROUND = 20;
	
	public static final int PRIORITY_BACKGROUND_HIGH = TopologicalRequest.PRIORITY_BACKGROUND + 1;
	public static final int PRIORITY_BACKGROUND_MIDDLE = TopologicalRequest.PRIORITY_BACKGROUND + 2;	
	public static final int PRIORITY_BACKGROUND_LOW = TopologicalRequest.PRIORITY_BACKGROUND + 3;

	/**
	 * ��� �������� �����������, ������� ��������� ���������� ��
	 * �������� ������� ������������ (����� ������� ���������)
	 */
	public static final int PRIORITY_EXPRESS = 10;	
	
	/**
	 * ��� �������� �����������, ��� ������������, ������� ���������
	 * ������ ������������
	 */
	public static final int PRIORITY_ALREADY_LOADED = 0;
	
	/**
	 * ��������� �������
	 */
	private int priority = TopologicalRequest.PRIORITY_BACKGROUND;
	
	/**
	 * 
	 */
	private boolean isCurrentlyProcessed = false;
	
	/**
	 * ������������ �����������
	 */
	private ImageIcon image = null;
	/**
	 * ������� ���������� (��� ������� ������ �����������!)
	 */
	private double topoScale = 1.f;
	/**
	 * ����������� ���������� ������� ����������
	 */
	private DoublePoint topoCenter = null;
	/**
	 * ����� ���������� �������������
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
	 * ��� ���������� ������� �� �������� �� ������� 
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
