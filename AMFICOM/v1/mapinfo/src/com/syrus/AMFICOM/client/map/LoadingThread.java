/**
 * $Id: LoadingThread.java,v 1.1.2.2 2005/05/05 12:04:48 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.Client.Map;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;

public class LoadingThread extends Thread
{
	private MapImageLoader mapImageLoader = null;
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

	
	private static final int STATE_IDLE = 0;
	private static final int STATE_RENDERING = 1;
	
	private int state = STATE_IDLE;
	
	private static final int STATE_DELAYS[] = {10, 20}; 
	
	public void run()
	{
		while(true) {
			checkStateEvents();
			try
			{
				Thread.sleep(STATE_DELAYS[this.state]);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	void checkStateEvents() {
		switch(this.state) {
			case STATE_IDLE:
				synchronized(this.requestQueue) {
					Iterator rqIt = this.requestQueue.iterator();
					if (rqIt.hasNext())
					{
						this.requestCurrentlyProcessed = (TopologicalRequest )rqIt.next();
						rqIt.remove();

						//�������� ������ �� ���������			
						try {
							this.mapImageLoader.renderMapImageAtServer(this.requestCurrentlyProcessed);
						} catch(MapConnectionException e1) {
							e1.printStackTrace();
							this.addRequest(this.requestCurrentlyProcessed);
							return;
						}
						setState(STATE_RENDERING);
					}
				}
				break;
			case STATE_RENDERING: 
				synchronized(this.requestQueue) {
					ImageIcon imageForRequest = this.mapImageLoader.getServerMapImage();
					if(imageForRequest != null) {
						this.requestCurrentlyProcessed.setImage(imageForRequest);
						this.requestCurrentlyProcessed.setPriority(TopologicalRequest.PRIORITY_ALREADY_LOADED);
						
						System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
							" TIC - loadingThread - run - image loaded for request (" + this.requestCurrentlyProcessed + ")");
							
						this.requestCurrentlyProcessed = null;
						setState(STATE_IDLE);
					}
				}
				break;
			default:
				throw new UnsupportedOperationException("Wrong state!");
		}
	}
	
	void setState(int state) {
		this.state = state;
	}
	
	
	
	public LoadingThread(
			MapImageLoader mapImageLoader)
	{
		this.mapImageLoader = mapImageLoader;
	}
/*
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
			
			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - loading request: " + this.requestCurrentlyProcessed);

			this.toBreakGettingRendition = false;
			
			//�������� ������ �� ���������			
			try {
				this.mapImageLoader.renderMapImageAtServer(this.requestCurrentlyProcessed);
			} catch(MapConnectionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				this.addRequest(this.requestCurrentlyProcessed);
				continue;
			}

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

				imageForRequest = this.mapImageLoader.getServerMapImage();
			}
			
			this.requestCurrentlyProcessed.setImage(imageForRequest);
			this.requestCurrentlyProcessed.setPriority(TopologicalRequest.PRIORITY_ALREADY_LOADED);
			
			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - image loaded for request (" + this.requestCurrentlyProcessed + ")");
				
			synchronized (this.requestCurrentlyProcessed)
			{
				this.requestCurrentlyProcessed = null;
			}
		}
	}
*/
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
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
				this.mapImageLoader.stopRenderingAtServer();
		}		
	}

	public void removeRequest(TopologicalRequest request)
	{
		if (this.requestCurrentlyProcessed == request)
			//���� �� ������� ������, ������� ��� � ��������� - �� ��� ��� ��� � �������.
			//������� ������ ������������� ���������.
			this.mapImageLoader.stopRenderingAtServer();
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
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
}