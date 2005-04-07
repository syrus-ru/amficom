/*
 * $Id: TopologicalImageCache.java,v 1.1 2005/04/07 14:19:18 peskovsky Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/07 14:19:18 $
 * @module mapinfo_v1
 */
public class TopologicalImageCache
{
	/**
	 * �������� "����" ������ �������� ������, ����������� ������
	 * �������� ������������ � ��� � ������� ������ 
	 */
	private static final double MARGIN_SIZE = 0.33;
	
	private static final int CACHE_ELEMENTS_COUNT = 200;
	private static final int MAX_EXCEEDING_COUNT = 50;	

	/**
	 * ������ ��� ������������ ��� �������� ��������� �����������,
	 * ������� ����� ���� ������������ ��� ��������� ������
	 */
	private List cacheOfImages = new ArrayList();

	private Image visibleImage = null;	
	
	private TopologicalCacheThread loadingThread = null;
	
	private MapInfoLogicalNetLayer miLayer = null;
	
	private DoublePoint center = null;
	private double scale = 1.f;
	private Dimension imageSize = null;	
	
	private Rectangle2D.Double expressAreaSphBorders = new Rectangle2D.Double();
	private Rectangle2D.Double cacheAreaSphBorders = new Rectangle2D.Double();	
	
	public TopologicalImageCache(MapInfoLogicalNetLayer miLayer)
	{
		this.miLayer = miLayer;

		this.sizeChanged();
		this.loadingThread = new TopologicalCacheThread(miLayer);
		this.loadingThread.start();
		
		this.centerChanged();
		this.scaleChanged();
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
	
			this.loadingThread.setImage(this.visibleImage);
			
			this.imageSize = newSize;			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - setSize - new visible image is created");
			
			setECRegionBorders();
			
			this.createRequests();			
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
			this.center = newCenter;
			setECRegionBorders();
			
			this.createRequests();			
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
			this.scale = newScale;
			setECRegionBorders();
			
			this.cacheOfImages.clear();
			
			this.createRequests();
		}
	}
	
	public Image getImage()
	{
		if (this.visibleImage == null)
			return null;

		//��� ���� ����������� ����������
		while (!this.loadingThread.expressRequestsRealized())
		{
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
						(int)Math.round(this.imageSize.width * TopologicalImageCache.MARGIN_SIZE),
						(int)Math.round(this.imageSize.height * TopologicalImageCache.MARGIN_SIZE)));
		
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

			if (	(curRequest.priority == TopologicalRequest.PRIORITY_ALREADY_LOADED)
					&&curRequest.topoBounds.contains(segmentBorders))
			{
				//��� ������ ��� ���������� � ����� �� �������������
				result = curRequest;
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
		}
		
		result.was_used = false;
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
	
	
	private void createRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
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

				if (requestToSend.was_used)
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
								if (requestsMatrix[areaY + k][areaX + l].was_used)
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
						requestsMatrix[i + areaY][j + areaX].was_used = true;


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
						(int)Math.round(Math.abs(this.miLayer.convertMapToScreen(newWidth))) + 2,
						(int)Math.round(Math.abs(this.miLayer.convertMapToScreen(newHeight))) + 2);
					
					if ((requestToSend.size.width <= 0) || (requestToSend.size.height <= 0))
					{
						//������� ��������� �� ������ �� ��������� �������
						//��������� ����� ��� ���������� ������� ���������� ������������ ���������
						//� ������� � ����������� ����������� ����� "�����" �������
						continue;
					}
					
					//���������� �������� (��������� �� ������!!!)
					requestToSend.topoScale = 
						this.miLayer.getScale() * requestToSend.size.width / this.imageSize.width;
				}
			
				//���������� ���������� ������ �������� ���� �� ������
				requestToSend.start = this.getScreenUpperLeftForMapRectangle(requestToSend.topoBounds);					
				
				//���� ����� ������ - ����� � ������ ������������ ��������
				if (requestToSend.priority != TopologicalRequest.PRIORITY_ALREADY_LOADED)
					this.cacheOfImages.add(requestToSend);

				//���� � ���� ������� ����� ��������� ������� ����� ������				
				if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT + 
						TopologicalImageCache.MAX_EXCEEDING_COUNT)
					this.clearOldSegments();
				
				//����� � ������� �� ���������
				this.loadingThread.addRequest(requestToSend);
			}
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - exiting. Reports created and queued.");
	}
	
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
}

class TopologicalCacheThread extends Thread
{
	private MapInfoLogicalNetLayer miLayer = null;
	private String uriString = null;
	private Image imageToPaintAt = null;
	
	/**
	 * ������� �������� �� ��������� ����������� � �������
	 */
	private List requestQueue = new LinkedList();	
	
	private boolean toBreak = false;
	
	private byte[] imageBuffer = null;

	
	public TopologicalCacheThread(
			MapInfoLogicalNetLayer miLayer)
	{
		this.miLayer = miLayer;
		this.uriString = this.miLayer.getMapViewer().getConnection().getURL();
	}
	
	public void setImage (Image newImage)
	{
		this.imageToPaintAt = newImage;

		Dimension imageSize = this.miLayer.getMapViewer().getVisualComponent().getSize();
		int dataSize = imageSize.width * imageSize.height * 2;
		
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - setImage - allocated for image buffer " + dataSize + " bytes of memory");


		this.imageBuffer = new byte[dataSize];
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
				}
			}
			
			if (request == null)
			{
				try
				{
					///������� ��������
					Thread.sleep(50);
					continue;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - realizing request: " + request);
			
			//��������� ������ HTTP �������
			String requestString = new String(this.uriString);
			
			if (request.priority != TopologicalRequest.PRIORITY_ALREADY_LOADED)
				try
				{
					requestString += this.createRenderCommandString(request);
	
					//�������� �����������
					request.image = this.getServerMapImage(requestString);
					
					request.priority = TopologicalRequest.PRIORITY_ALREADY_LOADED;
				} catch (MapDataException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			//������������ ��� �� ���-�����������
				this.imageToPaintAt.getGraphics().drawImage(
						new ImageIcon(request.image).getImage(),
						request.start.x - 1,
						request.start.y - 1,
						this.miLayer.getMapViewer().getVisualComponent());
				
				System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
					" TIC - loadingThread - run - request realized");
				
				synchronized (this.requestQueue)
				{
					this.requestQueue.remove(request);
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
		synchronized (this.requestQueue)
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
	}
	
	public boolean expressRequestsRealized()
	{
		synchronized (this.requestQueue)
		{
			//���� ������� ������ ��� ��� �������
			for (Iterator it = this.requestQueue.iterator(); it.hasNext();)
			{
				TopologicalRequest request = (TopologicalRequest)it.next();
				if (request.priority !=	TopologicalRequest.PRIORITY_BACKGROUND)
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * ���������� ����������� � ������� �� HTTP-�������
	 * @param requestURIString ������ �������
	 * @return �����������
	 */
	private Image getServerMapImage(String requestURIString)
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
			
			return imageReceived;
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
		
		DoublePoint topoCenter = request.getTopoCenter();
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ request.size.width;
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + request.size.height;
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + topoCenter.getX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + topoCenter.getY();
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
 * @version $Revision: 1.1 $, $Date: 2005/04/07 14:19:18 $
 * @module mapinfo_v1
 */
class TopologicalRequest implements Comparable
{
	/**
	 * ����� true ��� �������� �����������, ��� ��������� � �����-���� ������
	 * ��� �������� �����������
	 */
	public boolean was_used = false;
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
	protected Image image = null;
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
	protected Rectangle2D.Double topoBounds = null;
	/**
	 * �������� ���������� � �������� ��� ������-�������� ���� ������� �� ���-�����������
	 */
	protected Point start = null;
	/**
	 * ����� ���������� �������������
	 */
	protected long lastUsed = 0;
	

/**
* ����������� ���������� ������ ����������
*/
	public DoublePoint getTopoCenter()
	{
		DoublePoint topoCenter = null;
		if (this.topoBounds != null)
			topoCenter = new DoublePoint(
					this.topoBounds.x + this.topoBounds.width / 2,
					this.topoBounds.y + this.topoBounds.height / 2);
		
		return topoCenter;
	}
	
	public String toString()
	{
		String resultString = 
			"priority (" + this.priority + "), " +
			"screen start (" + this.start.x + ":" + this.start.y + "), " + 
			"screen width/height (" + this.size.width + ":" + this.size.height + ")";
		
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