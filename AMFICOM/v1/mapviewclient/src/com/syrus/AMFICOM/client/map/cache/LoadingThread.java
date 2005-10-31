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
     * ������� �������� �� ��������� ����������� � �������
     */
    private List<TopologicalImageQuery> requestQueue = 
    	Collections.synchronizedList(new LinkedList<TopologicalImageQuery>());

    /**
     * ������, �������������� � ������� ������
     */
    private TopologicalImageQuery requestCurrentlyProcessed = null;

    /**
     * ���������� true ��� ���������� ������
     */
    private boolean toCancelLoading = false;

    /**
     * ������ � ������� �������� ���������� � ��������� ����.
     */
    private boolean[] layerVisibilities = null;

    /**
     * ������ � ������� �������� ���������� � ��������� �������� ��
     * ��������������� �����.
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
            // �������� - ��� ������ �������
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

            // �������� ������ �� ���������
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
            	//���� ��� Exception - ������ �������� ������� � �������
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            } catch (MapDataException e) {
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            }

            synchronized (this.state) {
                // ��������� �������� (�� ����������� ������� - �� ��� ���� �
            	// ������� ������� � ��-�� Excpetion'�). ������ ������������������ ����,
                // ����� ������ ���� ��� ������������� ������� ��������� �������� ��
                // ����� ������
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
     * ������������� ��������� ��������� ���� � �������� �� ��� ��� ��������� ������� 
     * @param query ������
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

			// ��������� ���� ������� �� ����, ����� �� ��� ������ ������ � �����
			// �� �� ��� ������� �������� �� �������
			this.layerVisibilities[index] = spL.isVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			// �� �� ����� ��� ��������
			this.labelVisibilities[index] = spL.isLabelVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			index++;
		}

		query.setLayerVisibilities(this.layerVisibilities);		
		query.setLabelVisibilities(this.labelVisibilities);
		
		Log.debugMessage(" LoadingThread | setQueryLayerVisibilities | visibilities are set.",Level.FINEST); //$NON-NLS-1$
	}
    
    
    private void setRequestInItsPlace(TopologicalImageQuery requestToAdd) {
        // ���� ������ ������ � ����������� ����, ��� � ������ �������
        ListIterator<TopologicalImageQuery> lIt = this.requestQueue.listIterator();
        for (; lIt.hasNext();) {
            TopologicalImageQuery curRequest = lIt.next();
            if (requestToAdd.getPriority() < curRequest.getPriority()) {
                // ��������� ����� ���� ��������
                lIt.previous();
                lIt.add(requestToAdd);
                return;
            }
        }

        // �� ����� - ��������� � ����� �������
        lIt.add(requestToAdd);
    }

    private void stopRendering() throws MapConnectionException,
            MapDataException {
        this.mapImageLoader.stopRendering();
    }

    /**
     * ��������� ������ � �������, �������� ��� ���� ������� �� ����������.
     * ���� ��������� ������������ ������� ���� ���������� ���������������� -
     * ��������� ��������������� (������������� ������ ��������� ������ �� ������� - 
     * ������ ��� �����������)
     * 
     * @param requestToAdd
     *            ������
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
                // � ��������������� ������ ������ ������� � �������
                this.setRequestInItsPlace(this.requestCurrentlyProcessed);
            }
        }
    }

    public boolean removeRequest(TopologicalImageQuery request)
            throws MapConnectionException, MapDataException {
        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request)) {
                // ���� �� ������� ������, ������� ��� � ��������� - �� ��� ���
                // ��� � �������.
                // ������� ������ ������������� ���������.
                this.stopRendering();
                return true;
            }
            // ������� ������ �� �������
            else if (this.requestQueue.remove(request))
                return true;
        }
        return false;
    }

    /**
     * ������ ��������� �������������� ������� � �������
     * 
     * @param request
     *            ������
     * @param newPriority
     *            ����� ���������
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
	            // ���� ������ ��� � ���������, ���������:
	            // ���� ��� ��������� ��� ���� ������ ����������, �� �� �����
	            // ���������
	            // ����� ������������ - ������ �� ������, ����� �������������
	            // ���������
                this.requestCurrentlyProcessed.setPriority(newPriority);

                Iterator rqIt = this.requestQueue.iterator();
                if (!rqIt.hasNext())
                    // ������� �����
                    return;

                TopologicalImageQuery firstElement = (TopologicalImageQuery) rqIt
                        .next();
                if (firstElement.getPriority() < newPriority) {
                    // ������ ������� ����� ����� ��������� ����� �������,
                    // ��� ����� ��������� ��� ��������������� ������� -
                    // ������������� ���������.
                    this.stopRendering();
                    // � ��������������� ������ ������ ������� � �������
                    this.setRequestInItsPlace(this.requestCurrentlyProcessed);
                }
            } else if (this.requestQueue.remove(request)) {
                // ���� ������ ���������� � �������
                // ����� ��� ����� ���������
                request.setPriority(newPriority);

                // ����� ������ ������ � ������� (� ������������ ��������� ����������!!!)
                this.addRequest(request);
            }
        }
    }

    /**
     * ������ ��������� ���� �������� �� �������, ����� �������� � �����������
     * EXPRESS, �� ����� ������
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
     * ��������� ������ ������ ���������.
     *
     */
    public void cancel() {
        this.toCancelLoading = true;
    }
}
