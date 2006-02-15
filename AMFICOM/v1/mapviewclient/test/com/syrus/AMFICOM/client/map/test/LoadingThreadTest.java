/*
 * $Id: LoadingThreadTest.java,v 1.6 2006/02/15 12:42:12 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.test;

import junit.framework.TestCase;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.cache.LoadingThread;
import com.syrus.AMFICOM.client.map.cache.TopologicalImageCache;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2006/02/15 12:42:12 $
 * @module mapviewclient_modifying
 */
public class LoadingThreadTest extends TestCase {
	
	private static final long MAXIMUM_TIMEOUT = 30000;
	private static final int BETWEEN_COMMANDS_TIME = 20;
	
	/**
	 * <b>Название:</b> Очистка очереди подгрузки.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b> Очищается очередь подгрузки.<br>
	 * <b>Реакция:</b> Удаляются все запросы из очереди. Останавливается рендеринг.
	 * <b>Постусловие:</b> Все запросы будут иметь пустое поле Image.
	 */
	public void testRemoveAllRequests()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.060,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.062,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);

  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.064,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {
			Log.errorMessage(e);
		} catch (MapDataException e) {
			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {
			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {
			Log.errorMessage(e);
		} catch (MapDataException e) {
			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		//Очищаем очередь подгрузки
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq2.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The first request's image shouldn't be loaded!",tiq.getImage() == null); //$NON-NLS-1$
		assertTrue("The second request's image shouldn't be loaded!",tiq1.getImage() == null); //$NON-NLS-1$
		assertTrue("The last request's image shouldn't be loaded!",tiq2.getImage() == null); //$NON-NLS-1$
	}	
	
	/**
	 * <b>Название:</b> Добавление запроса в режиме ожидания.<br>
	 * <b>Предусловие:</b> Очередь запросов пуста, поток в режиме ожидания.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Забирается из очереди первый (самый высокоприоритетный) запрос в главном цикле
	 * потока подгрузки. Шлётся запрос на рендеринг в MapImageLoader.<br>
	 * <b>Постусловие:</b> Получение на выходе структуры запроса с заполенным полем image и приоритетом ALREADY_LOADED.
	 */
	public void testSendSingleRequestIdle()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}
		
		assertTrue("Image is not generated for the request!",tiq.getImage() != null); //$NON-NLS-1$
		assertTrue("Priority hasn't changed for the request!",tiq.getPriority() == TopologicalImageQuery.PRIORITY_ALREADY_LOADED);		 //$NON-NLS-1$
	}
	
	/**
	 * <b>Название:</b> Добавление запроса в режиме подгрузки изображения при пустой очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов пуста, поток в режиме подгрузки изображения.
	 *  Приоритеты обрабатываемого и добавляемого запросов равны.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Запрос ставится в очередь. Приоритет добавляемого запроса
	 * равен приоритету рассматриваемого запроса - рендеринг не останавливается.<br>
	 * <b>Постусловие:</b> Первый запрос обрабатывается быстрее второго.
	 */
	public void testSendRequestActiveSecondPriorityEquals()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.002,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.004,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  		
		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("Second request's image is not generated after the first one!",tiq1.getTimeCreated() > tiq.getTimeCreated()); //$NON-NLS-1$
	}

	/**
	 * <b>Название:</b> Добавление запроса в режиме подгрузки изображения при пустой очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов пуста, поток в режиме подгрузки изображения.
	 *  Приоритет добавляемого запросов меньше приоритета обрабатываемого.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Запрос ставится в очередь. Приоритет добавляемого запроса
	 * равен приоритету рассматриваемого запроса - рендеринг не останавливается. Главный
	 * цикл переходит к следующей итерации.<br>
	 * <b>Постусловие:</b> Первый запрос обрабатывается быстрее второго.
	 */
	public void testSendRequestActiveSecondPriorityLower()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.006,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.008,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("Second request's image is not generated after the first one!",tiq1.getTimeCreated() > tiq.getTimeCreated());	} //$NON-NLS-1$
	
	/**
	 * <b>Название:</b> Добавление запроса в режиме подгрузки изображения при пустой очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов пуста, поток в режиме подгрузки изображения.
	 *  Приоритет добавляемого запросов выше приоритета обрабатываемого.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Запрос ставится в очередь. Приоритет добавляемого запроса
	 * выше приоритета рассматриваемого запроса - рендеринг останавливается. Главный
	 * цикл переходит к следующей итерации.<br>
	 * <b>Постусловие:</b> Второй запрос обрабатывается быстрее первого.
	 */
	public void testSendRequestActiveSecondPriorityHigher()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.010,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.012,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_EXPRESS);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}
		assertTrue("First request's image is not generated!",tiq.getImage() != null); //$NON-NLS-1$
		assertTrue("Second request's image is not generated!",tiq1.getImage() != null);		 //$NON-NLS-1$
		assertTrue("First request's image is not generated after the second one!",tiq1.getTimeCreated() < tiq.getTimeCreated()); //$NON-NLS-1$
	}
	
	/**
	 * <b>Название:</b> Добавление запроса в режиме подгрузки изображения при непустой очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.
	 *  Приоритет добавляемого запросов выше приоритета обрабатываемого.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Запрос ставится в очередь. Приоритет добавляемого запроса
	 * выше приоритета рассматриваемого запроса - рендеринг останавливается.
	 * Рассматриваемый запрос становится в начало очереди. Посланный последним запрос начинает
	 * обрабатываться.<br>
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: последний, первый, второй.
	 */
	public void testSendRequestActiveLastPriorityHighestNotEmptyQueue()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.014,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.016,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.018,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_EXPRESS);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The queue of generating images is not last-first-second!",(tiq1.getTimeCreated() > tiq.getTimeCreated()) && (tiq.getTimeCreated() > tiq2.getTimeCreated()));		 //$NON-NLS-1$
	}

	/**
	 * <b>Название:</b> Добавление запроса в режиме подгрузки изображения при непустой очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.
	 *  Приоритет добавляемого запросов ниже приоритета обрабатываемого, но выше любого в очереди.<br>
	 * <b>Действие:</b> Добавить запрос в очередь.<br>
	 * <b>Реакция:</b> Запрос ставится в очередь. Приоритет добавляемого запроса
	 * ниже приоритета рассматриваемого запроса - рендеринг не останавливается.
	 * Добавляемый запрос становится в начало очереди. Посланный последним запрос начинает
	 * обрабатываться.<br>
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: первый, последний, второй.
	 */
	public void testSendRequestActiveLastPriorityMiddleNotEmptyQueue()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.020,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.022,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.024,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq2.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		//Первый и последний запросы уже должны быть подгружены, а второй - нет.
		assertTrue("Image is not generated for the last request!",tiq2.getImage() != null);		 //$NON-NLS-1$
		assertTrue("Image is not generated for the first request!",tiq.getImage() != null); //$NON-NLS-1$
		assertTrue("Image is generated for the second request!",tiq1.getImage() == null); //$NON-NLS-1$
	}
	
	/**
	 * <b>Название:</b> Изменение приоритета запроса, находящегося в очереди на более низкий, чем следующий в очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b>Приоритет первого в очереди запроса изменяется на самый низкий.<br>
	 * <b>Реакция:</b> Новый приоритет для запроса ниже приоритета рассматриваемого запроса -
	 * рендеринг не останавливается. Запрос становится в конец очереди.<br>
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: первый, последний, второй.
	 */
	public void testChangeRequestPriorityForLower()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.026,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_EXPRESS);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.028,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.030,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);  		
		
  		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		//Меняем приоритет второго запроса на более низкий чем у третьего		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.changeRequestPriority(tiq1,TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The queue of generating images is not first-last-second!",(tiq1.getTimeCreated() > tiq2.getTimeCreated()) && (tiq2.getTimeCreated() > tiq.getTimeCreated())); //$NON-NLS-1$
	}

	/**
	 * <b>Название:</b> Изменение приоритета запроса, находящегося в очереди на более высокий,
	 *  чем у запроса, находящегося в обработке .<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b>Приоритет последнего в очереди запроса изменяется на самый высокий.<br>
	 * <b>Реакция:</b> Новый приоритет для запроса выше приоритета рассматриваемого запроса -
	 * рендеринг останавливается. Рассматриваемый отчёт ставится в своё место в очереди.
	 * Начинается рендеринг запроса с изменённым приоритетом.<br>
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: последний, первый, второй.
	 */
	public void testChangeRequestPriorityForHigher()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.032,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.034,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.036,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		//Меняем приоритет последнего запроса на более высокий чем у первого в очереди,
		//но меньший, чем у запроса в обработке.
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.changeRequestPriority(tiq2,TopologicalImageQuery.PRIORITY_EXPRESS);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The queue of generating images is not last-first-second!",(tiq1.getTimeCreated() > tiq.getTimeCreated()) && (tiq.getTimeCreated() > tiq2.getTimeCreated())); //$NON-NLS-1$
	}
	
	/**
	 * <b>Название:</b> Изменение приоритета запроса, находящегося в обработке на более низкий,
	 *  чем у первого запроса из очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b>Приоритет запроса в обработке изменяется на более низкий,
	 *  чем у первого запроса из очереди.<br>
	 * <b>Реакция:</b> Рендеринг останавливается. Главный цикл переходит к следующей итерации -
	 * и забирает первый отчёт из очереди. Прерванный запрос становится в очередь.
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: второй, первый, последний.
	 */
	public void testChangeProcessingRequestPriorityForLower()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.038,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.040,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.042,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		//Меняем приоритет обрабатываемого запроса на более низкий чем первого в очереди		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.changeRequestPriority(tiq,TopologicalImageQuery.PRIORITY_BACKGROUND_MIDDLE);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq2.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The queue of generating images is not second-first-last!",(tiq2.getTimeCreated() > tiq.getTimeCreated()) && (tiq.getTimeCreated() > tiq1.getTimeCreated())); //$NON-NLS-1$
	}
	/**
	 * <b>Название:</b> Изменение приоритета запроса, находящегося в обработке на ещё
	 * более высокий.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b>Приоритет запроса в обработке изменяется на ещё более высокий.<br>
	 * <b>Реакция:</b> Реакция отсутсвует.
	 * <b>Постусловие:</b> Запросы обрабатываются в очерёдности: первый, второй, последний.
	 */
	public void testChangeProcessingRequestPriorityForHigher()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.044,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.046,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.048,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);  
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);  		
		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		//Меняем обрабатываемого запроса на ещё более высокий		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.changeRequestPriority(tiq,TopologicalImageQuery.PRIORITY_EXPRESS);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq2.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The queue of generating images is not first-second-last!",(tiq2.getTimeCreated() > tiq1.getTimeCreated()) && (tiq1.getTimeCreated() > tiq.getTimeCreated())); //$NON-NLS-1$
	}

	/**
	 * <b>Название:</b> Удаление запроса, находящегося в обработке.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b> Удаляется запрос в обработке.<br>
	 * <b>Реакция:</b> Рендеринг останавливается. Главный цикл переходит к следующей итерации.
	 * <b>Постусловие:</b> Первый запрос остаётся с пустым полем image, следующий за ним -
	 * с заполненным.
	 */
	public void testRemoveProcessingRequest()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.050,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.052,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		//Удаляем первый запрос		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.removeRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The first request's image shouldn't be loaded!",tiq.getImage() == null); //$NON-NLS-1$
		assertTrue("The second request's image should be loaded!",tiq1.getImage() != null);		 //$NON-NLS-1$
	}

	/**
	 * <b>Название:</b> Удаление запроса, находящегося в очереди.<br>
	 * <b>Предусловие:</b> Очередь запросов непуста, поток в режиме подгрузки изображения.<br>
	 * <b>Действие:</b> Удаляется запрос в обработке.<br>
	 * <b>Реакция:</b> Запрос удаляется.
	 * <b>Постусловие:</b> Первый и последний запросы будут иметь заполненное поле Image,
	 * второй - пустое.
	 */
	public void testRemoveQueueRequest()
	{
		LoadingThread loadingThread =
			((TopologicalImageCache)METS.netMapViewer.getRenderer()).getLoadingThread();
		
		try {
			loadingThread.clearQueue();
		} catch (MapConnectionException e2) {

			Log.errorMessage(e2);
		} catch (MapDataException e2) {

			Log.errorMessage(e2);
		}
		
  		boolean[] layerVis = {true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true,true};
  		TopologicalImageQuery tiq = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.054,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);  		

  		TopologicalImageQuery tiq1 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.056,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq1.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);

  		TopologicalImageQuery tiq2 = new TopologicalImageQuery(
  				800,
  				800,
  				37.59 + 0.058,
  				55.81,
  				2001.0000000345,
  				layerVis,
  				layerVis);
  		
  		tiq2.setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_HIGH);
  		
		try {
			loadingThread.addRequest(tiq);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}

		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.addRequest(tiq2);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		//Удаляем второй запрос (первый в очереди)		
		try {
			Thread.sleep(BETWEEN_COMMANDS_TIME);
		} catch (InterruptedException e1) {

			Log.errorMessage(e1);
		}
		
		try {
			loadingThread.removeRequest(tiq1);
		} catch (MapConnectionException e) {

			Log.errorMessage(e);
		} catch (MapDataException e) {

			Log.errorMessage(e);
		}
		
		long startTime = System.currentTimeMillis();
		while (true)
		{
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1) {
	
				Log.errorMessage(e1);
			}
			
			if (tiq1.getImage() != null)
				break;
			
			long currentTime = System.currentTimeMillis();
			if (currentTime - startTime > MAXIMUM_TIMEOUT)
				break;
		}

		assertTrue("The first request's image should be loaded!",tiq.getImage() != null); //$NON-NLS-1$
		assertTrue("The second request's image shouldn't be loaded!",tiq1.getImage() == null); //$NON-NLS-1$
		assertTrue("The last request's image should be loaded!",tiq2.getImage() != null); //$NON-NLS-1$
	}
}
