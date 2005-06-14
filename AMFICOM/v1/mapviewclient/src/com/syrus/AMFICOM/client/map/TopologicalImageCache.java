/*
 * $Id: TopologicalImageCache.java,v 1.3 2005/06/14 12:20:29 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.syrus.AMFICOM.map.DoublePoint;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.3 $, $Date: 2005/06/14 12:20:29 $
 * @module mapinfo_v1
 */
public class TopologicalImageCache implements MapImageRenderer
{
	/**
	 * �������� �������� �������� ������� � ��������� �� �������� ���� �����
	 */
	private static final double ACTIVE_AREA_SIZE = 0.25;

	/**
	 * �������� �������� ������� ������� (��� ����� � �� ���������� �������� ������)
	 * � ��������� �� �������� ���� �����
	 */
	private static final double BORDER_AREA_SIZE = 0.1;
	
	/**
	 * ��������� ����� ������-������� �� ����� ����� � �������� �������. ����
	 * ����� ������-������� ������ ���������, ���������� ����� ������������
	 * ������� ��� �������� �� ������������.
	 */
	private static final int RV_THRESHOLD_LENGTH = 100;
	
	/**
	 * ���������� �������� ������ ��� ���������� ��������������� ������ ��������.
	 * ������� ������ �������� � ����������� ����� �������� ������ ���������
	 * ��������� �����������. 
	 */
	private static final int CENTER_COMPUTING_ERROR = 10;

	/**
	 * ��� ���� �� ��������: ����� ����������� � ������� � ������� �
	 * MapInfologicalNetLayer.ZOOM_FACTOR ���������, ������� ����� ���������� �
	 * ��� ��������. ��������� ���������� ����������� ���� ����������� �����
	 * ������. ��� SCALE_CACHE_SIZE = 1 ����� ���������� ���� ����������� �
	 * ������� � ���� � ������� ���������. ��� ���� �� ��������� ������
	 * (�����������): ����� �����������, ��������� �� ��� �������������
	 */
	private static final int CACHE_SIZE = 2;

	/**
	 * "�������������" ���������� ��������� � ����
	 */
	private static final int CACHE_ELEMENTS_COUNT = 50;

	/**
	 * ���������� ����� "������" ��������� � ����. ���� ����� ��������� � ����
	 * �������� ����� CACHE_ELEMENTS_COUNT � MAX_EXCEEDING_COUNT, ���������
	 * ������� ���� �� MAX_EXCEEDING_COUNT ���������, ������� ���� ������������
	 * ������� �����.
	 */
	private static final int MAX_EXCEEDING_COUNT = 20;

	/**
	 * ������ ���� � ������ ��������� ������ �����
	 */
	private static final int MODE_CENTER_CHANGING = 1;

	/**
	 * ������ ���� � ������ ��������� ��������
	 */
	private static final int MODE_SCALE_CHANGING = 2;

	private LogicalNetLayer logicalNetLayer = null;

	/**
	 * ������ ��� ������������ ��� �������� ������ ������ ���������
	 */
	private List cacheOfImages = Collections.synchronizedList(new ArrayList());

	/**
	 * ������ �� ��������� ������, ��������������� ��������� ������ �� ��������
	 * � ������� �� �������
	 */
	private LoadingThread loadingThread = null;
	
	private boolean toBreak = false;

	/**
	 * ����� ������ ����
	 */
	private int mode = TopologicalImageCache.MODE_CENTER_CHANGING;

	// ������� ��������� ���� �����
	private DoublePoint center = null;

	private double scale = 1.f;

	private Dimension imageSize = null;

	/**
	 * ������� �����������
	 */
	private TopologicalRequest requestToPaint = null;

	/**
	 * ��������� ������� ���������� ������� � ������� �� ������������.
	 */
	private Dimension nonActiveZoneLastSector = new Dimension();

	/**
	 * ��������� �������� ������� (������������ �������� ������), � ������� ��
	 * ��������
	 */
	private Dimension lastActiveArea = new Dimension();

//	/**
//	 * ����� ����� � ��������� �������� �������
//	 */
//	private Point activeAreaEntryPoint = new Point();

	/**
	 * ������ �����, ���������� ������������ ������������ �������������� ������
	 * � ������-�������� �� ������ ����������� � ��������� �����.
	 * ����� 8 - �� 2 �� ������ �������. ���� ����������� �� �����������. 
	 */
	private double[] edgePointsAngles = new double[8];
	
	/**
	 * ���������� ����� �������� ���� �������� �� ����������� ���������
	 * � �������������� ����������� 
	 */
	private double xDifferenceSph = 0.D;

	/**
	 * ���������� ����� �������� ���� �������� �� ��������� ���������
	 * � �������������� ����������� 
	 */
	private double yDifferenceSph = 0.D;
	
	public TopologicalImageCache(LogicalNetLayer miLayer, MapConnection connection)
			throws MapConnectionException, MapDataException
	{
		this.logicalNetLayer = miLayer;
		this.scale = this.logicalNetLayer.getScale();
		this.center = this.logicalNetLayer.getCenter();

		this.sizeChanged();
		this.loadingThread = new LoadingThread(connection);
		this.loadingThread.start();
	}

	public void sizeChanged() throws MapConnectionException, MapDataException
	{
		Logger.log(" TIC - sizeChanged - just entered.");
		
		Dimension newSize = this.logicalNetLayer.getMapViewer()
				.getVisualComponent().getSize();
		if ((newSize.width <= 0) || (newSize.height <= 0))
			return;

		this.imageSize = newSize;
		
		//������� ����� �������� �������� �� ����������� ��������� � �������� 
		int xDifferenceScr = (int)Math.round(this.imageSize.width * (1.D - TopologicalImageCache.ACTIVE_AREA_SIZE));
		//������� ����� �������� �������� �� ��������� ��������� � ��������		
		int yDifferenceScr = (int)Math.round(this.imageSize.height * (1.D - TopologicalImageCache.ACTIVE_AREA_SIZE));		
		
		//���������� �������� ������
		Point curCenterScr = new Point(this.imageSize.width / 2,this.imageSize.height / 2);
		DoublePoint curCenterSph = this.logicalNetLayer.convertScreenToMap(curCenterScr);
		
		//������� ���������� ������ ���������� �� ����������� �������� 
		Point nextHorizCenterScr = new Point(this.imageSize.width / 2 + xDifferenceScr,this.imageSize.height / 2);		
		DoublePoint nextHorizCenterSph = this.logicalNetLayer.convertScreenToMap(nextHorizCenterScr);
		//������� ���������� ����� ��������
		this.xDifferenceSph = nextHorizCenterSph.getX() - curCenterSph.getX();

		//������� ���������� ������ ���������� �� ����������� �������� 
		Point nextVertCenterScr = new Point(this.imageSize.width / 2,this.imageSize.height / 2 + yDifferenceScr);		
		DoublePoint nextVertCenterSph = this.logicalNetLayer.convertScreenToMap(nextVertCenterScr);
		//������� ���������� ����� ��������
		this.yDifferenceSph = nextVertCenterSph.getY() - curCenterSph.getY();
		
		
		//������������� ���� ��� ��������� ����� - � ������-�������
		this.edgePointsAngles[0] = this.getAngleForPoint(this.imageSize.width,
				this.imageSize.height * TopologicalImageCache.ACTIVE_AREA_SIZE,
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[1] = this.getAngleForPoint(this.imageSize.width
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE), 0,
				this.imageSize.width / 2, this.imageSize.height / 2);

		//������-�����
		this.edgePointsAngles[2] = this.getAngleForPoint(this.imageSize.width
				* TopologicalImageCache.ACTIVE_AREA_SIZE, 0, this.imageSize.width / 2,
				this.imageSize.height / 2);

		this.edgePointsAngles[3] = this.getAngleForPoint(0, this.imageSize.height
				* TopologicalImageCache.ACTIVE_AREA_SIZE, this.imageSize.width / 2,
				this.imageSize.height / 2);

		//���-�����    
		this.edgePointsAngles[4] = this.getAngleForPoint(0, this.imageSize.height
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE),
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[5] = this.getAngleForPoint(this.imageSize.width
				* TopologicalImageCache.ACTIVE_AREA_SIZE, this.imageSize.height,
				this.imageSize.width / 2, this.imageSize.height / 2);

		//���-������
		this.edgePointsAngles[6] = this.getAngleForPoint(this.imageSize.width
				* (1 - TopologicalImageCache.ACTIVE_AREA_SIZE), this.imageSize.height,
				this.imageSize.width / 2, this.imageSize.height / 2);

		this.edgePointsAngles[7] = this.getAngleForPoint(this.imageSize.width,
				this.imageSize.height * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE),
				this.imageSize.width / 2, this.imageSize.height / 2);
	}

	/**
	 * ����� � ����������� �����������
	 */
	public void setCenter(DoublePoint newCenter) throws MapConnectionException, MapDataException
	{
		Logger.log(" TIC - setCenter - just entered.");		
		if (this.imageSize == null)
		{
			this.center = newCenter;			
			return;
		}
		
		if ((this.center == null) || (!this.center.equals(newCenter)))
		{
			// ���� �� ����� ���������� ���������� �������� - ������� �������
			// ������������� ������, ������ ����� ������, ������� ���

			if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
				nulifyCache();
				
				//������������� �������� ������� ���������� ������� � ��������� �������� ������� �� ����
				this.lastActiveArea.setSize(0,0);
				this.nonActiveZoneLastSector.setSize(0,0);				
			}
			
			this.center = newCenter;			
			this.createMovingRequests();
			
			//TODO ��� ������ ������ ���� �������������, ���� � MapMouseListener'�
			//������ ���������� ����� ��������� ������
			reactMouseLocation();
		}
	}

	/**
	 * �������
	 */
	public void setScale(double newScale) throws MapConnectionException, MapDataException
	{
		Logger.log(" TIC - setScale - just entered.");		
		if (this.imageSize == null)
		{
			this.scale = newScale;			
			return;
		}

		if (this.scale != newScale)
		{
			// ���� �� ����� ���������� ���������� ������ - ������� �������
			// ������������� ������, ������ ����� ������, ������� ���
			if (this.mode == TopologicalImageCache.MODE_CENTER_CHANGING)
			{
				this.mode = TopologicalImageCache.MODE_SCALE_CHANGING;
				nulifyCache();
			}

			// ����� ������ ���� �������� ���������� �������� ��������,
			// ����� ��������� ��������
			this.createScaleRequests();

			this.scale = newScale;
		}
	}

	private void nulifyCache()
	{
		Logger.log(" TIC - nulifying cache.");		
//		this.requestToPaint = null;
		this.cacheOfImages.clear();
		this.loadingThread.clearQueue();
	}
	
	private void reactMouseLocation() throws MapDataException,MapConnectionException
	{
		//������������� ���� �������� ����� ������ ����������
		this.loadingThread.setTheLowestPriorityForAll();

		if (this.nonActiveZoneLastSector.width == 0)
			this.createVerticalRequests(this.nonActiveZoneLastSector.height);
		else if (this.nonActiveZoneLastSector.height == 0)
			this.createHorizontalRequests(this.nonActiveZoneLastSector.width);
		else
			this.createDiagonalRequests(this.nonActiveZoneLastSector);
//	}
//	else
//	{
		// ��������� ����� ����� � �������� �������
//		if ((this.lastActiveArea.width != dX) || (this.lastActiveArea.height != dY))
//		{
//			this.activeAreaEntryPoint.x = mouseX;
//			this.activeAreaEntryPoint.y = mouseY;
//		}

//		// ����������� ����� ������-�������, ���� ������ ��������� - �������
//		double rvLength = Math.pow(Math
//				.pow(mouseX - this.activeAreaEntryPoint.x, 2)
//				+ Math.pow(mouseY - this.activeAreaEntryPoint.y, 2), 0.5);
//		if (rvLength < TopologicalImageCache.RV_THRESHOLD_LENGTH)
//			return;
//
//		// ��������� ����� ��������, �������� ��������� �������� �������
//		// ��� �������� ����������
//		Point newCenterScr = new Point((int) Math.round(this.imageSize.width
//				* (0.5 + dX * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))),
//				(int) Math.round(this.imageSize.height
//						* (0.5 + dY * (1 - TopologicalImageCache.ACTIVE_AREA_SIZE))));
//
//		DoublePoint newCenterSph = this.logicalNetLayer
//				.convertScreenToMap(newCenterScr);

		// ��������� �������������� ����� ��������

		// ��������� � ����� �������� ����� ����� ��� ������ ������ -
		// ������ � ������� PRIORITY_HIGH-������ �� �������������� ������
		// (????????????� PRIORITY_MEDIUM �� ��������????????)
//	}
		
		// ������� �������������� ����� �������� �� ��� �������
		this.clearFarAndUnloadedSegments();

		// ���� � ���� ������� ����� ��������� ������� ����� ������
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT
				+ TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
	}
	
	public void analyzeMouseLocation(MouseEvent event)
			throws MapDataException, MapConnectionException
	{
		Logger.log(" TIC - analyzeMouseLocation - just entered.");
		
		if (this.mode == TopologicalImageCache.MODE_SCALE_CHANGING)
			//� ��� ������, ���� �� �� ����� ����������� ���, ������ �����.
			//���� �� ����� �� �������, �� ��-�� ��������� ������ ��� ���������
			//��� ������� �� ��������� ������.
		{
			this.mode = TopologicalImageCache.MODE_CENTER_CHANGING;
			nulifyCache();
		}
		
		int mouseX = event.getPoint().x;
		int mouseY = event.getPoint().y;		
		// ����������� �������
		// ����������� ����� ������-�������, ���� ������ ��������� - �������
		double rvLength = Math.pow(Math
				.pow(mouseX - this.imageSize.width / 2, 2)
				+ Math.pow(mouseY - this.imageSize.height / 2, 2), 0.5);
		if (rvLength < TopologicalImageCache.RV_THRESHOLD_LENGTH)
			return;
		
		// ���������� ���� ������������ ������� "������" � ������� � ������ ������
		double currentAngle = this.getAngleForPoint(mouseX, mouseY,
				this.imageSize.width / 2, this.imageSize.height / 2);
		
		//���������� ����������� ���������
		Dimension direction = this.getDirectionForAngle(currentAngle);
		
//		
//		// ��������� � ����� �� ������ �������, ���� dX ��� dY
//		// ������� �� ���� - �� � ��������
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
//		// ��������� �������� �������
//		this.lastActiveArea.setSize(dX, dY);

//		if ((dX == 0) && (dY == 0))
//		{
			// �� ��������� � ���������� ����� ������
			
			
		//���� �� ��� ����������� ������ ��� ����� ������� ���������� ������� - �������
		if (	(this.nonActiveZoneLastSector.width == direction.width)
				&&(this.nonActiveZoneLastSector.height == direction.height))
			return;
		
		//��������� ����� �������� �������
		this.nonActiveZoneLastSector = direction;
		
		reactMouseLocation();		
	}
	
	private Dimension getDirectionForAngle(double angle)
	{
		Dimension direction = new Dimension();
		
		if ((this.edgePointsAngles[0] < angle) && (angle <= this.edgePointsAngles[1]))
		{
			//������-������
			direction.width = 1;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[1] < angle) && (angle <= this.edgePointsAngles[2]))
		{
			//�����
			direction.width = 0;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[2] < angle) && (angle <= this.edgePointsAngles[3]))
		{
			//������-�����
			direction.width = -1;
			direction.height = -1;			
		}
		else if ((this.edgePointsAngles[3] < angle) && (angle <= this.edgePointsAngles[4]))
		{
			//�����
			direction.width = -1;
			direction.height = 0;			
		}
		else if ((this.edgePointsAngles[4] < angle) && (angle <= this.edgePointsAngles[5]))
		{
			//���-�����
			direction.width = -1;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[5] < angle) && (angle <= this.edgePointsAngles[6]))
		{
			//��
			direction.width = 0;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[6] < angle) && (angle <= this.edgePointsAngles[7]))
		{
			//���-������
			direction.width = 1;
			direction.height = 1;			
		}
		else if ((this.edgePointsAngles[7] < angle) || (angle <= this.edgePointsAngles[0]))
		{
			//������
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

		if ((pointX - coordNullY < 0) && (pointY - coordNullY > 0))//���� ������ ��������
			angle = 180 + angle;
		else if (pointX - coordNullY < 0) //���� ������ ��������
			angle = 180 - angle;
		else if (pointY - coordNullY > 0) //���� �������� ��������
			angle = 360 - angle;

		return angle;
	}
	
	/**
	 * ���� � ���� ������ � ��������� ����������� ������ � ��������
	 * � ������ ��� ��������� �� ��������.
	 * ���� ������� � ������ ����������� �� ������� - �������� ����� ������ � ��������
	 * � ������� �� ���������. 
	 * @param topoCenter
	 * @param topoScale
	 * @return �����, ��� ������� ������������� ��������
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
				
				request.setLastUsed(System.currentTimeMillis());
				
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
	 * ������������ ��� ���������� ������� �� "�����", "��".
	 * ���������� ������ "��������" �� ��������� � �������� � ��������, �����������
	 * � �������������. � ������� ��������� BACKGROUND_HIGH, � ��������� BACKGROUND_MIDDLE  
	 * @param direction �����������
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
	 * ������������ ��� ���������� ������� �� "������", "�����".
	 * ��. createVerticalRequests
	 * @param direction �����������
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
	 * ������������ ��� ���������� ������� �� "������", "�����".
	 * ���������� �������� � ��������� "��������" ��������� �� ��������� �����������. 
	 * @param direction �����������
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
		// TODO ����� ��-�������� ���� �������������� ��������� ����,
		// �� ������ ��������� ���� ���.
//		this.requestToPaint = null;
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
			// ����������� �� ������� ��� �� ����������
			
			if (this.toBreak)
			{
				//������������� ������ ������ ���������
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
	 * ���� ����� ���������� ���, ���� ����� ���������� �� ������ �� ������
	 *
	 */
	public void cancel()
	{
		this.toBreak = true;
	}
	
	/**
	 * ������������ ��� ������� ������ ������ ��� �����.
	 * ����� ��������� � ��������� ����� ����� ��� ����������� ������ ����������� 
	 * @param point 
	 * @return ��������� �����
	 */
	public DoublePoint getNearestCenter(DoublePoint point)
	{
		//� ������, ���� ��� �� ������ �������� ������ � ��������
		if ((this.center == null) || (this.imageSize == null))
			return point;
		
		//������� ���������� �� �������� � �������� ����� ������� � ������
		double dX = point.getX() - this.center.getX();
		double dY = point.getY() - this.center.getY();		
		
		//������� ������� �������� �� �������� � �������� �������� �� ��� ����������
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
	 * ������� �������� ���������� ����� ����� ������� � ����������� �����������
	 * 
	 * @param sphP1
	 * @param sphP2
	 * @return ����������
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
	 * ��������� ������� � ��� ������������� ������ �������
	 * ��� �����������
	 */
	private void createMovingRequests()
			throws MapConnectionException, MapDataException
	{
		Logger.log(" TIC - createMovingRequests - just entered.");		
		this.requestToPaint = this.setPriorityForRequest(this.center,this.scale,TopologicalRequest.PRIORITY_EXPRESS);
		Logger.log(" TIC - createMovingRequests - exiting.");		
	}

	/**
	 * ������� ��������, ������� �)���� �������� � ��� ��������, �� �� ������
	 * ������������, �)����� ������� ��������� �� �������� ������ �����, ��� ��
	 * TopologicalImageCache.SCALE_SIZE * MapFrame.MOVE_CENTER_STEP_SIZE *
	 * _VisualComponent_.getSize();
	 * 
	 */
	private void clearFarAndUnloadedSegments()
	{
		Logger.log(" TIC - clearFarAndUnloadedSegments - just entered.");
		
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

		// ����, ���� �� ��� ������� � ����� �������
		for (Iterator it = this.cacheOfImages.iterator(); it.hasNext();)
		{
			TopologicalRequest curRequest = (TopologicalRequest) it.next();
			if ((!currCacheBorders.contains(curRequest.getTopoCenter().getX(),curRequest.getTopoCenter().getY()))
					&& (curRequest.getPriority() != TopologicalRequest.PRIORITY_ALREADY_LOADED)
					&& (curRequest != this.requestToPaint))
			{
				// ������� ������� - �� ����� ������ ��� ����������
				Logger.log(" TIC - clearFarAndUnloadedSegments - removing request."
						+ curRequest);
				this.loadingThread.removeRequest(curRequest);
				it.remove();
			}
		}
		
		Logger.log(" TIC - clearFarAndUnloadedSegments - exiting.");		
	}

	/**
	 * ������ ������ ������������ �����������
	 * 
	 */
	private void clearOldSegments()
	{
		Logger.log(" TIC - clearOldSegments - just entered.");

		// ��������� ������ (�� ��������, ����� ����� ����� ����� � ������
		// ������� ����)
		Collections.sort(this.cacheOfImages);

		Logger.log(" TIC - clearOldSegments - Collection sorted.");

		// ������� ��������� MAX_EXCEEDING_COUNT ���������
		for (ListIterator lIt = this.cacheOfImages
				.listIterator(TopologicalImageCache.CACHE_ELEMENTS_COUNT
						- TopologicalImageCache.MAX_EXCEEDING_COUNT); lIt.hasNext();)
		{
			lIt.next();
			lIt.remove();
		}

		Logger.log(" TIC - clearOldSegments - Old elements removed. Exiting.");
	}

	// ////////////////////////////////////������� ��� ���� �� ��������

	/**
	 * ���������� ����������� � ������� � ������� ���������� ��� ��������
	 * ��������
	 */
	private void createScaleRequests() throws MapConnectionException,
			MapDataException
	{
		Logger.log(" TIC - createScaleRequests - just entered.");		
		if ((this.cacheOfImages.size() == 0)
				|| (	(!TopologicalImageCache.compare(this.scale, this.logicalNetLayer.getScale()	* LogicalNetLayer.ZOOM_FACTOR))
						&&(!TopologicalImageCache.compare(this.scale * LogicalNetLayer.ZOOM_FACTOR, this.logicalNetLayer.getScale()))))
		{
			// ���� ����� ������� �� �������� ������� ����������� (zoom_to_box)
			// ��� �� ������ ��� ����� �� ������ ��������� ������
			// ������ ��� ����������� ������.
			renewScaleImages();
			Logger.log(" TIC - createScaleRequests - exiting.");			
			return;
		}

		//������������� ������������ �����������. ���� ��� ��� - ��� ����� ������� ���������
		this.requestToPaint = this.setPriorityForRequest(this.center,this.logicalNetLayer.getScale(),TopologicalRequest.PRIORITY_EXPRESS);

		// ��������, ������� �������� ����������� � ����������� ���������� ���������
		double scaleToCheck = 1;
		if (TopologicalImageCache.compare(this.scale, this.logicalNetLayer
				.getScale()
				* LogicalNetLayer.ZOOM_FACTOR))
		{
			// ����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.logicalNetLayer.getScale()
					/ Math.pow(LogicalNetLayer.ZOOM_FACTOR,
							TopologicalImageCache.CACHE_SIZE);
		} else if (TopologicalImageCache.compare(this.scale
				* LogicalNetLayer.ZOOM_FACTOR, this.logicalNetLayer.getScale()))
		{
			// ����� ������� � ZOOM_FACTOR ��� ������ �����������
			scaleToCheck = this.logicalNetLayer.getScale()
					* Math.pow(LogicalNetLayer.ZOOM_FACTOR,
							TopologicalImageCache.CACHE_SIZE);
		}

		// ���� ����������� � ����� ��������� ��� - ������ � ���������� PRIORITY_BACKGROUND_HIGH
		this.setPriorityForRequest(this.center,scaleToCheck,TopologicalRequest.PRIORITY_BACKGROUND_HIGH);

		// ���� � ���� ������� ����� ��������� ������� ����� ������
		if (this.cacheOfImages.size() > TopologicalImageCache.CACHE_ELEMENTS_COUNT
				+ TopologicalImageCache.MAX_EXCEEDING_COUNT)
			this.clearOldSegments();
		
		Logger.log(" TIC - createScaleRequests - exiting.");		
	}

	private static boolean compare(double p1, double p2)
	{
		if (Math.round(p1 * 1000000D) == Math.round(p2 * 1000000D))
			return true;

		return false;
	}

	/**
	 * ������ ���������� ��� ����������� � ������� � ������� ����������
	 */
	private void renewScaleImages() throws MapConnectionException,
			MapDataException
	{
		//������������� ������������ �����������. ������ ��� � ����� ������� �����������
		this.requestToPaint = this.setPriorityForRequest(this.center,this.logicalNetLayer.getScale(),TopologicalRequest.PRIORITY_EXPRESS);

		// ������ ����������� �������� � �������� �����������
		for (int i = 0; i < TopologicalImageCache.CACHE_SIZE; i++)
		{
			// ���������
			this.setPriorityForRequest(
					this.center,
					this.logicalNetLayer.getScale()	/ Math.pow(LogicalNetLayer.ZOOM_FACTOR, i + 1),
					TopologicalRequest.PRIORITY_BACKGROUND_MIDDLE);

			// �������
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
