/*
 * $Id: TopologicalImageCache.java,v 1.9.2.1 2005/04/25 08:38:26 peskovsky Exp $
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
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;

/**
 * @author $Author: peskovsky $
 * @version $Revision: 1.9.2.1 $, $Date: 2005/04/25 08:38:26 $
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
			
			this.center = newCenter;
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
	 * ������ � ����� ������� � ������� ��� ����������� ��� ����������� ������
	 */
	private void createMovingRequests()
	{
		System.out.println(this.miLayer.sdFormat.format(new Date(System.currentTimeMillis())) +
			" TIC - createRequests - just entered");
		
		//������ ����������� �����������
		for (int i = (-1) * TopologicalImageCache.CACHE_SIZE; i <= TopologicalImageCache.CACHE_SIZE; i++)
			for (int j = (-1) * TopologicalImageCache.CACHE_SIZE; j <= TopologicalImageCache.CACHE_SIZE; j++)			
			{
				DoublePoint imageCenter =	this.miLayer.convertScreenToMap(
						new Point(
								this.imageSize.width / 2 + i * (int)Math.round(this.imageSize.width * MapFrame.MOVE_CENTER_STEP_SIZE),
								this.imageSize.height / 2 + j * (int)Math.round(this.imageSize.height * MapFrame.MOVE_CENTER_STEP_SIZE)));
				
				
				TopologicalRequest requestForCenter = null;
				
				//����, ���� �� ��� ������� � ����� �������
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
					//���� ���
					int priority = TopologicalRequest.PRIORITY_BACKGROUND;
					if ((i == 0) && (j == 0))
						priority = TopologicalRequest.PRIORITY_EXPRESS;
					
					requestForCenter = this.createRequestForExpressArea(
							this.miLayer.getScale(),
							imageCenter,
							priority);
		
					this.cacheOfImages.add(requestForCenter);
					
					//����� � ������� �� ��������
					this.loadingThread.addRequest(requestForCenter);
				}

				if (this.miLayer.convertMapToScreen(requestForCenter.topoCenter,this.miLayer.getCenter()) < 10)
					//����� ������� � ������� �� ���������
					this.imagesToPaint.add(requestForCenter);
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
			if (TopologicalImageCache.compare(curRequest.topoScale,scaleToCheck))
				//���� �����, �� ���� ����������
				return;
		}

		//��� ������ - ������ �����������
		TopologicalRequest newImageRequest =
			this.createRequestForExpressArea(scaleToCheck,this.miLayer.getCenter(), TopologicalRequest.PRIORITY_BACKGROUND);
		
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
					TopologicalRequest.PRIORITY_BACKGROUND);

			this.cacheOfImages.add(smallScaledImage);
			
			//����� � ������� �� ��������
			this.loadingThread.addRequest(smallScaledImage);
			
			//�������
			TopologicalRequest bigScaledImage =	this.createRequestForExpressArea(
					this.miLayer.getScale() * Math.pow(MapInfoLogicalNetLayer.ZOOM_FACTOR,i + 1),
					this.miLayer.getCenter(),
					TopologicalRequest.PRIORITY_BACKGROUND);

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
	 * ���������� ��� ��������� ����������� ������
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
 * @version $Revision: 1.9.2.1 $, $Date: 2005/04/25 08:38:26 $
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
