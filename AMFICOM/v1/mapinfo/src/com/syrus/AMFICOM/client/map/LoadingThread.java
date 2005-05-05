/**
 * $Id: LoadingThread.java,v 1.1.2.1 2005/05/05 10:21:52 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
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
	
	public LoadingThread(
			MapImageLoader mapImageLoader)
	{
		this.mapImageLoader = mapImageLoader;
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
			
			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingThread - run - loading request: " + this.requestCurrentlyProcessed);

			this.toBreakGettingRendition = false;
			
			//Посылаем запрос на рендеринг			
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
				//Ждём получения изображения или сигнала на прерывание
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
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
				this.mapImageLoader.stopRenderingAtServer();
		}		
	}

	public void removeRequest(TopologicalRequest request)
	{
		if (this.requestCurrentlyProcessed == request)
			//Если мы удаляем запрос, который уже в обработке - то его уже нет в очереди.
			//поэтому просто останавливаем рендеринг.
			this.mapImageLoader.stopRenderingAtServer();
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
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
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
}