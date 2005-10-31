/*-
 * $$Id: LoadingThread.java,v 1.18 2005/10/31 12:30:09 bass Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.cache;

import java.awt.Image;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/10/31 12:30:09 $
 * @author $Author: bass $
 * @author Peter Peskovsky
 * @module mapviewclient
 */
public class LoadingThread extends Thread {
    private MapImageLoader mapImageLoader = null;

    /**
     * Очередь запросов на получение изображений с сервера
     */
    private List<TopologicalImageQuery> requestQueue = 
    	Collections.synchronizedList(new LinkedList<TopologicalImageQuery>());

    /**
     * Запрос, обрабатываемый в текущий момент
     */
    private TopologicalImageQuery requestCurrentlyProcessed = null;

    /**
     * Установить true для завершения работы
     */
    private boolean toCancelLoading = false;

    /**
     * Массив в котором хранится информация о видимости слоёв.
     */
    private boolean[] layerVisibilities = null;

    /**
     * Массив в котором хранится информация о видимости надписей на
     * соответствующих слоях.
     */
    private boolean[] labelVisibilities = null;

    class State {
        public static final int STATE_IDLE = 0;

        public static final int STATE_RENDERING = 1;

        private int value;

        /**
         * @return Returns the value.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * @param value
         *            The value to set.
         */
        public void setValue(int value) {
            this.value = value;
        }
    }

    private State state = null;

    public LoadingThread(MapImageLoader mapImageLoader)
            throws MapDataException, MapConnectionException {
        this.mapImageLoader = mapImageLoader;

        int layerCount = this.mapImageLoader.getMapConnection().getLayers()
                .size();
        this.layerVisibilities = new boolean[layerCount];
        this.labelVisibilities = new boolean[layerCount];

        this.state = new State();
        this.state.setValue(State.STATE_IDLE);
    }

    @Override
	public void run() {
        while (!this.toCancelLoading) {
            // Задержка - для пустой очереди
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this.state) {
                Iterator<TopologicalImageQuery> rqIt = this.requestQueue.iterator();
                if (rqIt.hasNext()) {
                    this.requestCurrentlyProcessed = rqIt.next();
                    rqIt.remove();
                    this.state.setValue(State.STATE_RENDERING);
                } else {
                	continue;
                }
            }

            // Посылаем запрос на рендеринг
            Image imageForRequest = null;
            long t1 = 0;
            long t2 = 0;
            long t3 = 0;
            long t4 = 0;
            long t5 = 0;
            t1 = System.currentTimeMillis();
            try {
            	Log.debugMessage(" LoadingThread | run | processing request (" //$NON-NLS-1$
                        + this.requestCurrentlyProcessed + ")",Level.FINEST); //$NON-NLS-1$

                this.setQueryLayerVisibilities(this.requestCurrentlyProcessed);

                t2 = System.currentTimeMillis();                
                imageForRequest = this.mapImageLoader
                        .renderMapImage(this.requestCurrentlyProcessed);
                t3 = System.currentTimeMillis();                
            } catch (MapConnectionException e1) {
            	//Если был Exception - запрос ставится обратно в очередь
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            } catch (MapDataException e) {
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            }

            synchronized (this.state) {
                // Рендеринг завершён (не обязательно успешно - он мог быть и
            	// прерван вручную и из-за Excpetion'а). Ставим синхронизированный блок,
                // чтобы нельзя было для подгруженного запроса приоритет изменить на
                // более низкий
                t4 = System.currentTimeMillis();
                if (imageForRequest != null) {
	                this.requestCurrentlyProcessed.setImage(imageForRequest);
	                this.requestCurrentlyProcessed
	                        .setPriority(TopologicalImageQuery.PRIORITY_ALREADY_LOADED);
	                this.requestCurrentlyProcessed.setTimeCreated(t4);
                }
                t5 = System.currentTimeMillis();                
                Log.debugMessage(" LoadingThread | run | image loaded for request (" //$NON-NLS-1$
                		+ this.requestCurrentlyProcessed + ")\n" //$NON-NLS-1$
                		+ "total: " + (t5 - t1) + " ms\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + "		" + (t2 - t1) + " ms (setting layer visibilities)\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + "		" + (t3 - t2) + " ms (requesting for rendering)\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + "		" + (t4 - t3) + " ms (after synchronized)\n" //$NON-NLS-1$ //$NON-NLS-2$
                        + "		" + (t5 - t4) + " ms (setting image) ms.",Level.FINER); //$NON-NLS-1$ //$NON-NLS-2$

                this.state.setValue(State.STATE_IDLE);
                this.requestCurrentlyProcessed = null;
            }
        }
    }

    /**
     * Устанавливает параметры видимости слоёв и надписей на них для заданного запроса 
     * @param query Запрос
     * @throws MapDataException
     * @throws MapConnectionException
     */
    private void setQueryLayerVisibilities(TopologicalImageQuery query)
    	throws MapDataException,MapConnectionException {
		Log.debugMessage(" LoadingThread | setQueryLayerVisibilities | setting visibilities.",Level.FINEST); //$NON-NLS-1$
		Iterator layersIt = this.mapImageLoader.getMapConnection().getLayers()
				.iterator();
		int index = 0;
		for (; layersIt.hasNext();) {
			SpatialLayer spL = (SpatialLayer) layersIt.next();

			// Видимость слоя зависит от того, хочет ли его видеть клиент и виден
			// ли он при текущем масштабе на сервере
			this.layerVisibilities[index] = spL.isVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			// то же самое для надписей
			this.labelVisibilities[index] = spL.isLabelVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			index++;
		}

		query.setLayerVisibilities(this.layerVisibilities);		
		query.setLabelVisibilities(this.labelVisibilities);
		
		Log.debugMessage(" LoadingThread | setQueryLayerVisibilities | visibilities are set.",Level.FINEST); //$NON-NLS-1$
	}
    
    
    private void setRequestInItsPlace(TopologicalImageQuery requestToAdd) {
        // Ищем первый запрос с приоритетом ниже, чем у нового запроса
        ListIterator<TopologicalImageQuery> lIt = this.requestQueue.listIterator();
        for (; lIt.hasNext();) {
            TopologicalImageQuery curRequest = lIt.next();
            if (requestToAdd.getPriority() < curRequest.getPriority()) {
                // Добавляем перед этим запросом
                lIt.previous();
                lIt.add(requestToAdd);
                return;
            }
        }

        // Не нашли - добавляем в конец очереди
        lIt.add(requestToAdd);
    }

    private void stopRendering() throws MapConnectionException,
            MapDataException {
        this.mapImageLoader.stopRendering();
    }

    /**
     * Добавляет запрос в очередь, сортируя при этом запросы по приоритету.
     * Если приоритет добавляемого запроса выше приоритета рассматриваемого -
     * рендеринг останавливается (соответсвенно берётся следующий запрос из очереди - 
     * только что добавленный)
     * 
     * @param requestToAdd
     *            Запрос
     * @throws MapDataException
     * @throws MapConnectionException
     */
    public void addRequest(TopologicalImageQuery requestToAdd)
            throws MapConnectionException, MapDataException {
        Log.debugMessage(" LoadingThread | addRequest | adding request (" //$NON-NLS-1$
                + requestToAdd + ")",Level.FINEST); //$NON-NLS-1$

        synchronized (this.state) {
            setRequestInItsPlace(requestToAdd);
            
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed.getPriority() > requestToAdd.getPriority())) {
                this.stopRendering();
                // И рассматриваемый запрос ставим обратно в очередь
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            }
        }
    }

    public boolean removeRequest(TopologicalImageQuery request)
            throws MapConnectionException, MapDataException {
        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request)) {
                // Если мы удаляем запрос, который уже в обработке - то его уже
                // нет в очереди.
                // поэтому просто останавливаем рендеринг.
                this.stopRendering();
                return true;
            }
            // Убираем запрос из очереди
            else if (this.requestQueue.remove(request))
                return true;
        }
        return false;
    }

    /**
     * Меняет приоритет невыполненного запроса в очереди
     * 
     * @param request
     *            Запрос
     * @param newPriority
     *            Новый приоритет
     * @throws MapDataException
     * @throws MapConnectionException
     */
    public void changeRequestPriority(TopologicalImageQuery request,
            int newPriority) throws MapConnectionException, MapDataException {
        if (request.getPriority() == newPriority) {
        	Log.debugMessage(" LoadingThread | changeRequestPriority | the request (" //$NON-NLS-1$
                            + request
                            + ") already has the priority " //$NON-NLS-1$
                            + newPriority + ". Exiting.",Level.FINEST); //$NON-NLS-1$
            return;
        }

        Log.debugMessage(" LoadingThread | changeRequestPriority | changing request's (" //$NON-NLS-1$
                        + request + ") priority for " + newPriority,Level.FINEST); //$NON-NLS-1$

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request)) {
	            // Если запрос уже в обработке, проверяем:
	            // если при установке для него нового приоритета, он всё равно
	            // останется
	            // самым приоритетным - ничего не делаем, иначе останавливаем
	            // рендеринг
                this.requestCurrentlyProcessed.setPriority(newPriority);

                Iterator rqIt = this.requestQueue.iterator();
                if (!rqIt.hasNext())
                    // Очередь пуста
                    return;

                TopologicalImageQuery firstElement = (TopologicalImageQuery) rqIt
                        .next();
                if (firstElement.getPriority() < newPriority) {
                    // Первый элемент будет иметь приоритет более высокий,
                    // чем новый приоритет для обрабатываемого запроса -
                    // останавливаем рендеринг.
                    this.stopRendering();
                    // И рассматриваемый запрос ставим обратно в очередь
                    this.setRequestInItsPlace(this.requestCurrentlyProcessed);
                }
            } else if (this.requestQueue.remove(request)) {
                // Если запрос содержался в очереди
                // Задаём ему новый приоритет
                request.setPriority(newPriority);

                // Снова ставим запрос в очередь (С ВОЗМОЖНОСТЬЮ остановки рендеринга!!!)
                this.addRequest(request);
            }
        }
    }

    /**
     * Меняет приоритет всех запросов из очереди, кроме запросов с приоритетом
     * EXPRESS, на самый низкий
     */
    public void setTheLowestPriorityForAll() {
    	Log.debugMessage(" LoadingThread | setTheLowestPriorityForAll | entering",Level.FINEST); //$NON-NLS-1$

        synchronized (this.state) {    	
	        Iterator it = this.requestQueue.iterator();
	        for (; it.hasNext();) {
	            TopologicalImageQuery curRequest = (TopologicalImageQuery) it
	                    .next();
	            if (curRequest.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
	                curRequest
	                        .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
	        }

            if (this.state.getValue() == State.STATE_RENDERING) {
                if (this.requestCurrentlyProcessed.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
                    this.requestCurrentlyProcessed
                            .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
            }
        }
        Log.debugMessage(" LoadingThread | setTheLowestPriorityForAll | done",Level.FINEST); //$NON-NLS-1$
    }

    public void clearQueue() throws MapConnectionException, MapDataException {
        synchronized (this.state) {
            this.requestQueue.clear();        	
            if (this.state.getValue() == State.STATE_RENDERING)
                this.stopRendering();
        }
    }

    /**
     * Завершает работу потока подгрузки.
     *
     */
    public void cancel() {
        this.toCancelLoading = true;
    }
}
