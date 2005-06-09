
/**
 * $Id: LoadingThread.java,v 1.2 2005/06/09 08:52:37 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;


public class LoadingThread extends Thread {
    private MapImageLoader mapImageLoader = null;

    /**
     * Очередь запросов на получение изображений с сервера
     */
    private List requestQueue = Collections.synchronizedList(new LinkedList());

    /**
     * Запрос, обрабатываемый в текущий момент
     */
    private TopologicalRequest requestCurrentlyProcessed = null;
    
    private boolean toCancelLoading = false;

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

    public LoadingThread(MapImageLoader mapImageLoader) {
        this.mapImageLoader = mapImageLoader;
        this.state = new State();
        this.state.setValue(State.STATE_IDLE);
    }

    public void run()
    {
      while (!this.toCancelLoading)
      {
      	//Задержка - для пустой очереди
        try
        {
            Thread.sleep(10);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
      	
        synchronized (this.state)
        {        	
          Iterator rqIt = this.requestQueue.iterator();
          if (rqIt.hasNext())
          {
              this.requestCurrentlyProcessed = (TopologicalRequest) rqIt.next();
              rqIt.remove();
              this.state.setValue(State.STATE_RENDERING);
          }
          else
          	continue;          	
        }

        // Посылаем запрос на рендеринг
        ImageIcon imageForRequest = null;
        try
        {
	          Logger.log(" TIC - loadingThread - run - processing request ("
	              + this.requestCurrentlyProcessed + ")");
        	
            imageForRequest = this.mapImageLoader.renderMapImageAtServer(this.requestCurrentlyProcessed);

            if (imageForRequest == null)
            {
	            synchronized (this.state)
	            {
	            	//Если рендеринг был остановлен
                this.state.setValue(State.STATE_IDLE);
                this.requestCurrentlyProcessed = null;            
              
	              continue;
	            }
            }
        } 
        catch (MapConnectionException e1)
        {
            e1.printStackTrace();
            this.add(this.requestCurrentlyProcessed);
        }
        catch (MapDataException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            this.add(this.requestCurrentlyProcessed);
        }

        synchronized (this.state)
        {
        	//Рендеринг успешно завершён. Ставим синхронизированный блок, чтобы
        	//нельзя было для подгруженного запроса приоритет изменить на более низкий
          this.requestCurrentlyProcessed.setImage(imageForRequest);
          this.requestCurrentlyProcessed
                  .setPriority(TopologicalRequest.PRIORITY_ALREADY_LOADED);

          Logger.log(" TIC - loadingThread - run - image loaded for request ("
                        + this.requestCurrentlyProcessed + ")");
        	
          this.state.setValue(State.STATE_IDLE);
          this.requestCurrentlyProcessed = null;            
        }
      }
    }

    private void add(TopologicalRequest requestToAdd) {
        // Ищем первый запрос с приоритетом ниже, чем у нового запроса
        ListIterator lIt = this.requestQueue.listIterator();
        for (; lIt.hasNext();) {
            TopologicalRequest curRequest = (TopologicalRequest) lIt.next();
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

    private void stopRendering() {
        this.mapImageLoader.stopRenderingAtServer();
    }

    /**
     * Добавляет запрос в очередь, сортируя при этом запросы по приоритету
     * 
     * @param requestToAdd
     *            Запрос
     */
    public void addRequest(TopologicalRequest requestToAdd) {
    		Logger.log(" TIC - loadingThread - addRequest - adding request ("
          + requestToAdd + ")");
    	
        add(requestToAdd);

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed.getPriority() > requestToAdd
                            .getPriority()))
                stopRendering();
        }
    }

    public boolean removeRequest(TopologicalRequest request) {
        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request)) {
                //Если мы удаляем запрос, который уже в обработке - то его уже нет в очереди.
                //поэтому просто останавливаем рендеринг.
                this.stopRendering();
                return true;
            }
            //Убираем запрос из очереди
            else if (this.requestQueue.remove(request))
                return true;
        }
        return false;
    }

    /**
     * Меняет приоритет невыполненного запроса в очереди
     * @param request Запрос
     * @param newPriority Новый приоритет
     */
    public void changeRequestPriority(TopologicalRequest request,
            int newPriority)
    {
    	if (request.getPriority() == newPriority)
    	{
        Logger.log(" TIC - loadingThread - changeRequestPriority - the request ("
            + request + ") already has the priority " + newPriority + ". Exiting.");
        return;
    	}

    	Logger.log(" TIC - loadingThread - changeRequestPriority - changing request's ("
                 + request + ") priority for " + newPriority);

      synchronized (this.state)
      {
      	if (	(this.state.getValue() == State.STATE_RENDERING)
            &&(this.requestCurrentlyProcessed == request))
        //Если запрос уже в обработке, проверяем:
        //если при установке для него нового приоритета, он всё равно останется
        //самым приоритетным - ничего не делаем, иначе останавливаем рендеринг
        {
      		this.requestCurrentlyProcessed.setPriority(newPriority);
      		
        	Iterator rqIt = this.requestQueue.iterator();      		
        	if (!rqIt.hasNext())
        		//Очередь пуста
        		return;
        	
        	TopologicalRequest firstElement = (TopologicalRequest)rqIt.next();
        	if (firstElement.getPriority() < newPriority)
        	{
        		//Первый элемент будет иметь приоритет более высокий,
        		//чем новый приоритет для обрабатываемого запроса - останавливаем рендеринг.
        		this.stopRendering();
        		//И рассматриваемый запрос ставим обратно в очередь
        		this.add(this.requestCurrentlyProcessed);
        	}
        }
        else if (this.requestQueue.remove(request))
        {
          //Если запрос содержался в очереди
          //Задаём ему новый приоритет
          request.setPriority(newPriority);

          //Снова ставим запрос в очередь
          this.add(request);
        }
      }
    }

    /**
     * Меняет приоритет всех запросов из очереди на самый низкий
     */
    public void setTheLowestPriorityForAll() {
        Logger.log(" TIC - loadingThread - setTheLowestPriorityForAll - entering");

        Iterator it = this.requestQueue.iterator();
        for (; it.hasNext();)
        {
            TopologicalRequest curRequest = (TopologicalRequest) it.next();
            curRequest.setPriority(TopologicalRequest.PRIORITY_BACKGROUND_LOW);
        }
        
        synchronized (this.state)
        {
        	if (this.state.getValue() == State.STATE_RENDERING)
          {
        		this.requestCurrentlyProcessed.setPriority(TopologicalRequest.PRIORITY_BACKGROUND_LOW);
          }
        }
        Logger.log(" TIC - loadingThread - setTheLowestPriorityForAll - done");        
    }
    
    public void clearQueue() {
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
    public void cancel()
    {
    	this.toCancelLoading = true;
    }
}