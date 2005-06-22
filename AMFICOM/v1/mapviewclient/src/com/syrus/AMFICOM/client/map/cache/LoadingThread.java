/**
 * $Id: LoadingThread.java,v 1.3 2005/06/22 12:18:26 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.cache;

import java.awt.Image;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

public class LoadingThread extends Thread {
    private MapImageLoader mapImageLoader = null;

    /**
     * Очередь запросов на получение изображений с сервера
     */
    private List requestQueue = Collections.synchronizedList(new LinkedList());

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

    public void run() {
        while (!this.toCancelLoading) {
            // Задержка - для пустой очереди
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this.state) {
                Iterator rqIt = this.requestQueue.iterator();
                if (rqIt.hasNext()) {
                    this.requestCurrentlyProcessed = (TopologicalImageQuery) rqIt
                            .next();
                    rqIt.remove();
                    this.state.setValue(State.STATE_RENDERING);
                } else
                    continue;
            }

            // Посылаем запрос на рендеринг
            Image imageForRequest = null;
            try {
            	Log.debugMessage(" TIC - loadingThread - run - processing request ("
                        + this.requestCurrentlyProcessed + ")",Log.DEBUGLEVEL10);

                this.setQueryLayerVisibilities(this.requestCurrentlyProcessed);

                imageForRequest = this.mapImageLoader
                        .renderMapImage(this.requestCurrentlyProcessed);

                if (imageForRequest == null) {
                    synchronized (this.state) {
                        // Если рендеринг был остановлен
                        this.add(this.requestCurrentlyProcessed);
                        this.state.setValue(State.STATE_IDLE);
                        this.requestCurrentlyProcessed = null;

                        continue;
                    }
                }
            } catch (MapConnectionException e1) {
                e1.printStackTrace();
                this.add(this.requestCurrentlyProcessed);
            } catch (MapDataException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                this.add(this.requestCurrentlyProcessed);
            }

            synchronized (this.state) {
                // Рендеринг успешно завершён. Ставим синхронизированный блок,
                // чтобы
                // нельзя было для подгруженного запроса приоритет изменить на
                // более низкий
                this.requestCurrentlyProcessed.setImage(imageForRequest);
                this.requestCurrentlyProcessed
                        .setPriority(TopologicalImageQuery.PRIORITY_ALREADY_LOADED);

                Log.debugMessage(" TIC - loadingThread - run - image loaded for request ("
                                + this.requestCurrentlyProcessed + ")",Log.DEBUGLEVEL10);

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
		Log.debugMessage(" TIC - loadingThread - run - visibilities are set.",Log.DEBUGLEVEL10);
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
		
		Log.debugMessage(" TIC - loadingThread - run - visibilities are set.",Log.DEBUGLEVEL10);
	}
    
    private void add(TopologicalImageQuery requestToAdd) {
        // Ищем первый запрос с приоритетом ниже, чем у нового запроса
        ListIterator lIt = this.requestQueue.listIterator();
        for (; lIt.hasNext();) {
            TopologicalImageQuery curRequest = (TopologicalImageQuery) lIt
                    .next();
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
     * Добавляет запрос в очередь, сортируя при этом запросы по приоритету
     * 
     * @param requestToAdd
     *            Запрос
     * @throws MapDataException
     * @throws MapConnectionException
     */
    public void addRequest(TopologicalImageQuery requestToAdd)
            throws MapConnectionException, MapDataException {
        Log.debugMessage(" TIC - loadingThread - addRequest - adding request ("
                + requestToAdd + ")",Log.DEBUGLEVEL10);

        add(requestToAdd);

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed.getPriority() > requestToAdd
                            .getPriority()))
                stopRendering();
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
        	Log.debugMessage(" TIC - loadingThread - changeRequestPriority - the request ("
                            + request
                            + ") already has the priority "
                            + newPriority + ". Exiting.",Log.DEBUGLEVEL10);
            return;
        }

        Log.debugMessage(" TIC - loadingThread - changeRequestPriority - changing request's ("
                        + request + ") priority for " + newPriority,Log.DEBUGLEVEL10);

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request))
            // Если запрос уже в обработке, проверяем:
            // если при установке для него нового приоритета, он всё равно
            // останется
            // самым приоритетным - ничего не делаем, иначе останавливаем
            // рендеринг
            {
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
                    this.add(this.requestCurrentlyProcessed);
                }
            } else if (this.requestQueue.remove(request)) {
                // Если запрос содержался в очереди
                // Задаём ему новый приоритет
                request.setPriority(newPriority);

                // Снова ставим запрос в очередь
                this.add(request);
            }
        }
    }

    /**
     * Меняет приоритет всех запросов из очереди, кроме запросов с приоритетом
     * EXPRESS, на самый низкий
     */
    public void setTheLowestPriorityForAll() {
    	Log.debugMessage(" TIC - loadingThread - setTheLowestPriorityForAll - entering",Log.DEBUGLEVEL10);

        Iterator it = this.requestQueue.iterator();
        for (; it.hasNext();) {
            TopologicalImageQuery curRequest = (TopologicalImageQuery) it
                    .next();
            if (curRequest.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
                curRequest
                        .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
        }

        synchronized (this.state) {
            if (this.state.getValue() == State.STATE_RENDERING) {
                if (this.requestCurrentlyProcessed.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
                    this.requestCurrentlyProcessed
                            .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
            }
        }
        Log.debugMessage(" TIC - loadingThread - setTheLowestPriorityForAll - done",Log.DEBUGLEVEL10);
    }

    public void clearQueue() throws MapConnectionException, MapDataException {
        this.requestQueue.clear();
        synchronized (this.state) {
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