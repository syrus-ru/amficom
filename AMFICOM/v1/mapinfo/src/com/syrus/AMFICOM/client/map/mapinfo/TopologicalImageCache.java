/*
 * $Id: TopologicalImageCache.java,v 1.7 2005/04/18 09:22:13 peskovsky Exp $
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

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.7 $, $Date: 2005/04/18 09:22:13 $
 * @module mapinfo_v1
 */
public class TopologicalImageCache
{
	/**
	 * �������� "����" ������ �������� ������, ����������� ������
	 * �������� ������������ � ��� � ������� ������ 
	 */
	private static final double MARGIN_SIZE = 0.33;

	/**
	 * ����� ����������� � ������� � ������� � MapInfologicalNetLayer.ZOOM_FACTOR
	 * ���������, ������� ����� ���������� � ��� ��������. ��������� ���������� 
	 * ����������� ���� ����������� ����� ������. ��� SCALE_CACHE_SIZE = 1 �����
	 * ���������� ���� ����������� � ������� � ���� � ������� ���������.
	 */
	private static final int SCALE_CACHE_SIZE = 2;
	
	/**
	 * "�������������" ���������� ��������� � ����
	 */
	private static final int CACHE_ELEMENTS_COUNT = 200;
	
	/**
	 * ���������� ����� "������" ��������� � ����. ���� ����� ���������
	 * � ���� �������� ����� CACHE_ELEMENTS_COUNT � MAX_EXCEEDING_COUNT,
	 * ��������� ������� ���� �� MAX_EXCEEDING_COUNT ���������, ������� ����
	 * ������������ ������� �����.
	 */
	private static final int MAX_EXCEEDING_COUNT = 50;	

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
	
	private Rectangle2D.Double expressAreaSphBorders = new Rectangle2D.Double();
	private Rectangle2D.Double cacheAreaSphBorders = new Rectangle2D.Double();	
	
//	private boolean isRepainting = false;
	
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
		
//		//�� ����� �������� EXPRESS ������� ������ ������ ���������
//		while (this.isRepainting)
//		{
//			try
//			{
//				Thread.sleep(20);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
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
			
			this.center = newCenter;
			setECRegionBorders();
			
			this.createMovingRequests();			
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
		
//		//�� ����� �������� EXPRESS ������� ������ ������ ���������
//		while (this.isRepainting)
//		{
//			try
//			{
//				Thread.sleep(20);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
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
		setECRegionBorders();		
		this.createMovingRequests();		
	}
	
	public Image getImage()
	{
		if (this.visibleImage == null)
			return null;

//		this.isRepainting = true;
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

//		this.isRepainting = false;
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - getImage - returning image");
		
		return this.visibleImage;
	}

	/**
	 * ���������� �������� ������ ��� �������� ������� � ���-���������
	 */
	private void setECRegionBorders()
	{
		//!!!!���������, ��� � MapInfo ����� ���� ������ ������������ �������;
		//�� ������ � ������ � ��� ������ ������ 0. �������������� ������ ������ ��������� ��� ��������
		
		DoublePoint expressTopLeft = this.miLayer.convertScreenToMap(new Point(0,0));
		DoublePoint expressBottomRight = this.miLayer.convertScreenToMap(
				new Point(this.imageSize.width,this.imageSize.height));
		
		if (expressTopLeft.getX() < expressBottomRight.getX())
			this.expressAreaSphBorders.x = expressTopLeft.getX();
		else		
			this.expressAreaSphBorders.x = expressBottomRight.getX();
		
			this.expressAreaSphBorders.width = Math.abs(expressBottomRight.getX() - expressTopLeft.getX());			

		if (expressTopLeft.getY() < expressBottomRight.getY())
			this.expressAreaSphBorders.y = expressTopLeft.getY();
		else		
			this.expressAreaSphBorders.y = expressBottomRight.getY();
		
		this.expressAreaSphBorders.height = Math.abs(expressBottomRight.getY() - expressTopLeft.getY());			

		
		DoublePoint cacheTopLeft = this.miLayer.convertScreenToMap(
				new Point(
						(int)Math.round(-this.imageSize.width * TopologicalImageCache.MARGIN_SIZE),
						(int)Math.round(-this.imageSize.height * TopologicalImageCache.MARGIN_SIZE)));
		
		DoublePoint cacheBottomRight = this.miLayer.convertScreenToMap(
				new Point(
						(int)Math.round(this.imageSize.width * (1 + TopologicalImageCache.MARGIN_SIZE)),
						(int)Math.round(this.imageSize.height * (1 + TopologicalImageCache.MARGIN_SIZE))));
		
		if (cacheTopLeft.getX() < cacheBottomRight.getX())
			this.cacheAreaSphBorders.x = cacheTopLeft.getX();
		else		
			this.cacheAreaSphBorders.x = cacheBottomRight.getX();
		
			this.cacheAreaSphBorders.width = Math.abs(cacheBottomRight.getX() - cacheTopLeft.getX());			

		if (cacheTopLeft.getY() < cacheBottomRight.getY())
			this.cacheAreaSphBorders.y = cacheTopLeft.getY();
		else		
			this.cacheAreaSphBorders.y = cacheBottomRight.getY();
		
		this.cacheAreaSphBorders.height = Math.abs(cacheBottomRight.getY() - cacheTopLeft.getY());			
	}
	
	/**
	 * ���� ��� ����� ������ ������� ���-���������
	 * @param xs ��������������� ������ X-��������� 
	 * @param ys ��������������� ������ Y-���������
	 */
	private void searchPointsInsideCacheArea(List xs, List ys)
	{
		//��������� �� ������� ����� �������� ��������, ������� ������ � ���-�������
		for (Iterator requestIt = this.cacheOfImages.iterator(); requestIt.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)requestIt.next();

			if (this.rectangleContainsPoint(
						this.cacheAreaSphBorders,
						curRequest.topoBounds.x,
						curRequest.topoBounds.y))
			{
				xs.add(new Double(curRequest.topoBounds.x));
				ys.add(new Double(curRequest.topoBounds.y));					
			}

			if (this.rectangleContainsPoint(
						this.cacheAreaSphBorders,
						curRequest.topoBounds.x + curRequest.topoBounds.width,
						curRequest.topoBounds.y))
			{
				xs.add(new Double(curRequest.topoBounds.x + curRequest.topoBounds.width));
				ys.add(new Double(curRequest.topoBounds.y));					
			}

			if (this.rectangleContainsPoint(
						this.cacheAreaSphBorders,
						curRequest.topoBounds.x,
						curRequest.topoBounds.y + curRequest.topoBounds.height))
			{
				xs.add(new Double(curRequest.topoBounds.x));
				ys.add(new Double(curRequest.topoBounds.y + curRequest.topoBounds.height));					
			}

			if (this.rectangleContainsPoint(
						this.cacheAreaSphBorders,
						curRequest.topoBounds.x + curRequest.topoBounds.width,
						curRequest.topoBounds.y + curRequest.topoBounds.height))
			{
				xs.add(new Double(curRequest.topoBounds.x + curRequest.topoBounds.width));
				ys.add(new Double(curRequest.topoBounds.y + curRequest.topoBounds.height));					
			}
		}
		
		//��������� ������� ����� ������� express-�����������
		xs.add(new Double(this.expressAreaSphBorders.x));
		xs.add(new Double(this.expressAreaSphBorders.x + this.expressAreaSphBorders.width));
		ys.add(new Double(this.expressAreaSphBorders.y));					
		ys.add(new Double(this.expressAreaSphBorders.y + this.expressAreaSphBorders.height));					
		
		//��������� ������� ����� ������� cache-�����������		
		xs.add(new Double(this.cacheAreaSphBorders.x));
		xs.add(new Double(this.cacheAreaSphBorders.x + this.cacheAreaSphBorders.width));
		ys.add(new Double(this.cacheAreaSphBorders.y));					
		ys.add(new Double(this.cacheAreaSphBorders.y + this.cacheAreaSphBorders.height));					
		
		//��������� �� �����������
		Collections.sort(xs);
		Collections.sort(ys);
		
		getUnrepeatedElements(xs);
		getUnrepeatedElements(ys);		
	}

	/**
	 * ������� �� ������ ������������� ��������
	 * @param sourceList ��������������� ������
	 */
	private void getUnrepeatedElements (List sourceList)
	{
		// TODO ���������� �� ���������		
		for (int i = 0; i < sourceList.size(); i++)
		{
			Double iElem = (Double)sourceList.get(i);
			while (true)
			{
				if (i + 1 == sourceList.size())
					break;
				
				Double jElem = (Double)sourceList.get(i + 1);
				if (jElem.equals(iElem))
					sourceList.remove(jElem);
				else
					break;
			}
		}
	}
	/**
	 * ���� ��� ����� ������ ������� ���-���������
	 * @param segmentBorders ������� ��������
	 * @return ������ (���� ����� ���������, ���� ��� �������������� � ����)
	 */
	private TopologicalRequest createRequestForSegment(Rectangle2D.Double segmentBorders)
	{
		TopologicalRequest result = null;
		for (Iterator requestIt = this.cacheOfImages.iterator(); requestIt.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)requestIt.next();

			if (	curRequest.topoBounds.contains(segmentBorders)
					&&(curRequest.priority == TopologicalRequest.PRIORITY_ALREADY_LOADED))
			{
				//��� ������ ��� ���������� � ����� �� �������������
				result = curRequest;
				
//				//���� �� ��������� � ����������� BACKGROUND, � ������ ��������� � �������� 
//				//������� - ������ ���������
//				if (	(curRequest.priority == TopologicalRequest.PRIORITY_BACKGROUND)
//						&&this.expressAreaSphBorders.contains(segmentBorders))
//				{
//					//������� ��������� �������
//					this.loadingThread.removeRequest(curRequest);
//					this.cacheOfImages.remove(curRequest);
//					curRequest.priority = TopologicalRequest.PRIORITY_EXPRESS;
//				}
					
				break;
			}
		}

		if (result == null)
		{
			//� �������� ������ ��������� ����� ������
			//�������� ������ ��������, ����� ��� ������ � ������� (������� �� ������!!!)
			//����� ��������� ����� ����������� (��������)
			result = new TopologicalRequest();
			result.topoBounds = segmentBorders;
			
			if (this.expressAreaSphBorders.contains(segmentBorders))
				result.priority = TopologicalRequest.PRIORITY_EXPRESS;
			else
				result.priority = TopologicalRequest.PRIORITY_BACKGROUND;
			
			result.isUsedForCurrentImage = false;			
		}

		result.isUsedForCurrentImage = false;
		result.lastUsed = System.currentTimeMillis();
		
		return result;
	}
	
	private Point getScreenUpperLeftForMapRectangle(Rectangle2D.Double rect)
	{
		Point result = new Point();
		
		//�������� ���������� ��� ��������������� ������ �������� ����
		Point topLeft = this.miLayer.convertMapToScreen(
				new DoublePoint(
						rect.x,
						rect.y));

		//�������� ���������� ��� ��������������� ������� ������� ����		
		Point bottomRight = this.miLayer.convertMapToScreen(
				new DoublePoint(
						rect.x + rect.width,
						rect.y + rect.height));
		
		result.x = (topLeft.x < bottomRight.x)? topLeft.x : bottomRight.x;
		result.y = (topLeft.y < bottomRight.y)? topLeft.y : bottomRight.y;		
		
		return result;
	}
	
	/**
	 * ���������: ���������� �� ����� ������ ������ ��������������.
	 * (����������� ����� Rectangle ��������� ��������)
	 * @param rect �������������
	 * @param pointX X �����
	 * @param pointY Y ����� 
	 * @return true, ���� ��������
	 */
	private boolean rectangleContainsPoint(
			Rectangle2D.Double rect,
			double pointX,
			double pointY)
	{
		boolean returnValue = false;
		if (	(rect.getX() < pointX) && (pointX < rect.getX() + rect.getWidth())
				&&(rect.getY() < pointY) && (pointY < rect.getY() + rect.getHeight()))
			returnValue = true;
		
		return returnValue;
	}
	
	/**
	 * ������ � ����� ������� � ������� ��� ����������� ��� ����������� ������
	 */
	private void createMovingRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
		//������� �� ���������� ������ ������� - �������������� � �� �������� � �������
		//��� �������
		List unusedReports = this.loadingThread.removeOutOfCacheRequests(this.cacheAreaSphBorders);
		this.imagesToPaint.removeAll(unusedReports);
		this.cacheOfImages.removeAll(unusedReports);		
		
		//���� ����� ��������� ������� �� �������
		List xs = new ArrayList();
		List ys = new ArrayList();		
		searchPointsInsideCacheArea(xs,ys);
		
		int rMatrixWidth = xs.size() - 1;
		int rMatrixHeight = ys.size() - 1;
		
		//������ ������� ��������
		TopologicalRequest[][] requestsMatrix =
			new TopologicalRequest[rMatrixHeight][rMatrixWidth];
		
		//��������� ������� ��������
		for (int i = 0; i < rMatrixHeight; i++)
			for (int j = 0; j < rMatrixWidth; j++)
			{
				//���������� ������
				final Rectangle2D.Double segmentBorders = new Rectangle2D.Double();
				segmentBorders.x = ((Double)xs.get(j)).doubleValue();
				segmentBorders.y = ((Double)ys.get(i)).doubleValue();
				segmentBorders.width =
					((Double)xs.get(j + 1)).doubleValue() - ((Double)xs.get(j)).doubleValue();
				segmentBorders.height =
					((Double)ys.get(i + 1)).doubleValue() - ((Double)ys.get(i)).doubleValue();
			
				requestsMatrix[i][j] = this.createRequestForSegment(segmentBorders);
			}
		
		//������������ ������� ��������, �������� ����� ���������� �����
		//"�������������" ������ ���������� � ����
		for (int areaY = 0; areaY < rMatrixHeight; areaY++)//������
			for (int areaX = 0; areaX < rMatrixWidth; areaX++)//�������
			{
				//���� ���������� ������� ��� �������� ������ ���������

				TopologicalRequest requestToSend = requestsMatrix[areaY][areaX];

				if (requestToSend.isUsedForCurrentImage)
				{
					//���� �� ������ �� ������� � ������� �������������� ����������� - ������� ������ ������ ���������
					continue;
				}
				
				//����� "�������������" ������ ���������� �� ��������� � �����������
				Dimension maxAreaSize = new Dimension(1,1);
				for (int areaHeight = 1; areaHeight < rMatrixHeight - areaY + 1; areaHeight++)//����������� ����� �����
				{
					for (int areaWidth = 1; areaWidth < rMatrixWidth - areaX + 1; areaWidth++)//����������� ����� ��������
					{
						//���� ������� ��� ���������, �� ��� ����������� �����������, ����� � �������� ����� ����
						//������ �� ���� � ��� �� ������������� ������					
						//����� ��������� ��������� ����������� � ������������������
						boolean maybeUsed = true;
						for (int k = 0; k < areaHeight; k++)
							for (int l = 0; l < areaWidth; l++)
							{
								if (requestsMatrix[areaY + k][areaX + l].isUsedForCurrentImage)
								{
									//���� ������� ��� �����������
									maybeUsed = false;
									break;
								}
								
								if (requestsMatrix[areaY + k][areaX + l].priority == TopologicalRequest.PRIORITY_ALREADY_LOADED)
								{
									//������ ��� ����������
									//��������� ����� � �������� ����� ���������� ��� ���� ������ 
									if (requestsMatrix[areaY + k][areaX + l] != requestsMatrix[areaY][areaX])
									{
										//����� ��� ������ ������������� ������
										maybeUsed = false;										
										break;
									}
								}	
								else if (requestsMatrix[areaY + k][areaX + l].priority !=	requestsMatrix[areaY][areaX].priority)
								{
									//��������������� ������� ������ �����������
									maybeUsed = false;
									break;
								}
							}
						//���� ��������� ��� �� � �������� �� ���� ������������ � ������ �������
						if (maybeUsed)
						{
							if (areaWidth * areaHeight > maxAreaSize.width * maxAreaSize.height)
							{
								maxAreaSize.width = areaWidth;
								maxAreaSize.height = areaHeight;							
							}
						}
						else
							break;
					}
				}
				
				//��� �������� �� ���� ������� ��� �� ����� ������������ � ������ ��������
				for (int i = 0; i < maxAreaSize.height; i++)//������
					for (int j = 0; j < maxAreaSize.width; j++)//�������
						requestsMatrix[i + areaY][j + areaX].isUsedForCurrentImage = true;


				if (requestsMatrix[areaY][areaX].priority != TopologicalRequest.PRIORITY_ALREADY_LOADED)
				{
					//���� ��� ����� �������, �� ������� ��� �� ������ � ������, �������
					//���� ��� ���������� - �� ��� ��� ���������
					double newWidth = 0;
					for (int i = 0; i < maxAreaSize.width; i++)//������				
						newWidth += requestsMatrix[areaY][i + areaX].topoBounds.width;
	
					requestToSend.topoBounds.width = newWidth;
					
					double newHeight = 0;
					for (int i = 0; i < maxAreaSize.height; i++)//������				
						newHeight += requestsMatrix[i + areaY][areaX].topoBounds.height;
					
					requestToSend.topoBounds.height = newHeight;
					
					//���������� �������� ������
					requestToSend.size = new Dimension(
						(int)Math.round(Math.abs(this.miLayer.convertMapToScreen(new DoublePoint(0,0),new DoublePoint(newWidth,0)))) + 2,
						(int)Math.round(Math.abs(this.miLayer.convertMapToScreen(new DoublePoint(0,0),new DoublePoint(0,newHeight)))) + 2);
					
					if ((requestToSend.size.width <= 0) || (requestToSend.size.height <= 0))
					{
						//������� ��������� �� ������ �� ��������� �������
						//��������� ����� ��� ���������� ������� ���������� ������������ ���������
						//� ������� � ����������� ����������� ����� "�����" �������
						continue;
					}
					
					//���������� �������� (��������� �� ������!!!)
					requestToSend.topoScale = 
						this.miLayer.getScale() * requestToSend.size.width / (this.imageSize.width + 2);
					
					//���������� ���������� ������ ��������
					requestToSend.topoCenter = new DoublePoint(
							requestToSend.topoBounds.x + requestToSend.topoBounds.width / 2.d,
							requestToSend.topoBounds.y + requestToSend.topoBounds.height / 2.d);
				}
				
				//���������� ���������� ������ �������� ���� �� ������
				requestToSend.start = this.getScreenUpperLeftForMapRectangle(requestToSend.topoBounds);					
				
				//���� ����� ������ - ����� � ������ ������������ ��������
				if (requestToSend.priority != TopologicalRequest.PRIORITY_ALREADY_LOADED)
				{
					this.cacheOfImages.add(requestToSend);
					this.loadingThread.addRequest(requestToSend);
				}

				//���� ������ �� EXPRESS ������� - ������ �� ��������� ��������������
				if (requestToSend.priority != TopologicalRequest.PRIORITY_BACKGROUND)
				{
					this.imagesToPaint.add(requestToSend);
				}
			}

		//���� � ���� ������� ����� ��������� ������� ����� ������				
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
				TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
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
			if (TopologicalImageCache.compare(curRequest.topoScale,this.miLayer.getScale()))
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
			scaleToCheck = this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.SCALE_CACHE_SIZE);
		}
		else if (TopologicalImageCache.compare(this.scale * MapInfoLogicalNetLayer.ZOOM_FACTOR,this.miLayer.getScale()))
		{
			//����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,TopologicalImageCache.SCALE_CACHE_SIZE);			
		}
		
		//���� ����������� � ����� ���������
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest)it.next();
			if (TopologicalImageCache.compare(curRequest.topoScale,scaleToCheck))
				//���� �����, �� ���� ����������
				return;
		}

		//��� ������ - ������ �����������
		TopologicalRequest newImageRequest =
			this.createRequestForScaledExpressArea(scaleToCheck,TopologicalRequest.PRIORITY_BACKGROUND);
		
		this.cacheOfImages.add(newImageRequest);
		//����� ������� �� ��������
		this.loadingThread.addRequest(newImageRequest);
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
		TopologicalRequest currImageRequest =	this.createRequestForScaledExpressArea(
				this.miLayer.getScale(),
				TopologicalRequest.PRIORITY_EXPRESS);

		//����� � ���
		this.cacheOfImages.add(currImageRequest);
		//����� � ������� �� ���������
		this.imagesToPaint.add(currImageRequest);		
		//����� � ������� �� ��������		
		this.loadingThread.addRequest(currImageRequest);
		
		//������ ����������� �������� � �������� �����������
		for (int i = 0; i < TopologicalImageCache.SCALE_CACHE_SIZE; i++)
		{
			//���������
			TopologicalRequest smallScaledImage =	this.createRequestForScaledExpressArea(
					this.miLayer.getScale() / Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					TopologicalRequest.PRIORITY_BACKGROUND);

			this.cacheOfImages.add(smallScaledImage);
			
			//����� � ������� �� ��������
			this.loadingThread.addRequest(smallScaledImage);
			
			//�������
			TopologicalRequest bigScaledImage =	this.createRequestForScaledExpressArea(
					this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					TopologicalRequest.PRIORITY_BACKGROUND);

			this.cacheOfImages.add(bigScaledImage);
			
			//����� � ������� �� ��������
			this.loadingThread.addRequest(bigScaledImage);
		}
	}
	
	private TopologicalRequest createRequestForScaledExpressArea (
			double asScale,
			int asPriority)
	{
		TopologicalRequest result = new TopologicalRequest();
		result.priority = asPriority;
		result.isUsedForCurrentImage = false;

		result.topoCenter = this.miLayer.getCenter();
		result.topoScale = asScale;
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
	 * ������� �������� �� ��������� ����������� � �������
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
			//�������� ������ � ������� ������
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
				//�������� � ������� ���
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
			
			//��������� ������ HTTP �������
			String requestString = new String(this.uriString);
			
			try
			{
				requestString += this.createRenderCommandString(request);

				//�������� �����������
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
	 * ��������� ������ � �������, �������� ��� ���� ������� �� ����������
	 * @param request ������
	 */
	public void addRequest(TopologicalRequest request)
	{
		//���� ������ ������ � ����������� ����, ��� � ������ �������
		//� ��������� ������ ��������������� ����������
		for (int i = 0; i < this.requestQueue.size(); i++)
		{
			TopologicalRequest curRequest = 
				(TopologicalRequest) this.requestQueue.get(i);
			if (request.priority < curRequest.priority)
			{
				//�����
				this.requestQueue.add(i,request);
				return;
			}
		}
		//�� �����
		this.requestQueue.add(request);			
	}

	public void removeRequest(TopologicalRequest request)
	{
		this.requestQueue.remove(request);			
	}
	
	/**
	 * ������ ��������� �������������� ������� � �������
	 * @param request ������
	 * @param newPriority ����� ���������
	 */
	public void changeRequestPriority(TopologicalRequest request, int newPriority)
	{
		//������� ������ �� �������
		this.requestQueue.remove(request);
		
		//����� ��� ����� ���������
		request.priority = newPriority;
		
		//����� ������ ������ � �������
		this.addRequest(request);
	}

	/**
	 * ������� �������������� �������, �������� �� ������� cache �������.
	 * @param cacheArea ������� ��� ���������
	 * @return ������ �������������� ��������, ������� �� ������ � ���
	 */
	public List removeOutOfCacheRequests(Rectangle2D.Double cacheArea)
	{
		List unusedRequests = new ArrayList();
		for (ListIterator it = this.requestQueue.listIterator(); it.hasNext();)
		{
			TopologicalRequest request = (TopologicalRequest)it.next();
			if (!request.topoBounds.intersects(cacheArea))
			{
				//���� ��� ������� � ������� �� ������������ -
				//������� ������ �� �������
				unusedRequests.add(request);
				it.remove();
			}
		}
		
		return unusedRequests;
	}
	
	public void clearQueue()
	{
		this.requestQueue.clear();
	}
	/**
	 * ���������� ����������� � ������� �� HTTP-�������
	 * @param requestURIString ������ �������
	 * @return �����������
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
	 * ������ �� �������� ������� ������ ���������� ��� HTTP ������� � �������
	 * ����������
	 * @param request ������
	 * @return ������ ���������� ��� HTTP ������� � �������
	 * ���������� 
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
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="
					+ (spL.isVisible() ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index
					+ "=" + (spL.isLabelVisible() ? 1 : 0);
			index++;
		}

		return result;
	}
}
/**
 * ��������� ������� ����������� � �������
 * @author $Author: peskovsky $
 * @version $Revision: 1.7 $, $Date: 2005/04/18 09:22:13 $
 * @module mapinfo_v1
 */
class TopologicalRequest implements Comparable
{
	/**
	 * ����� true ��� �������� �����������, ��� ��������� � �����-���� ������
	 * ��� �������� ����������� - �� ����� ������������
	 */
	public boolean isUsedForCurrentImage = false;
	
	/**
	 * ��� �������� �����������, ����������� � ��� "�� ������ ������",
	 * �� ������� ������������ � ���������������� � ������� ������
	 */
	public static final int PRIORITY_BACKGROUND = 2;
	/**
	 * ��� �������� �����������, ��� ������������, ������� ���������
	 * ������ ������������
	 */
	public static final int PRIORITY_ALREADY_LOADED = 1;
	/**
	 * ��� �������� �����������, ������� ��������� ���������� ��
	 * �������� ������� ������������ (����� ������� ���������)
	 */
	public static final int PRIORITY_EXPRESS = 0;	
	/**
	 * ��������� �������
	 */
	protected int priority = TopologicalRequest.PRIORITY_BACKGROUND;
	/**
	 * ������������ �����������
	 */
	protected ImageIcon image = null;
	/**
	 * �������� ����������� � ��������
	 */
	protected Dimension size = null;
	/**
	 * ������� ���������� (��� ������� ������ �����������!)
	 */
	protected double topoScale = 1.f;
	/**
	 * ����������� ���������� ������� ����������
	 */
	protected DoublePoint topoCenter = null;
	/**
	 * ����������� ���������� ������ ����������
	 */
	protected Rectangle2D.Double topoBounds = null;
	/**
	 * �������� ���������� � �������� ��� ������-�������� ���� ������� �� ���-�����������
	 */
	protected Point start = null;
	/**
	 * ����� ���������� �������������
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
}
